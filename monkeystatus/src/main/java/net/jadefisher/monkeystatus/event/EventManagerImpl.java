package net.jadefisher.monkeystatus.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jadefisher.monkeystatus.alert.AlertManager;
import net.jadefisher.monkeystatus.model.monitor.Monitor;
import net.jadefisher.monkeystatus.model.monitor.MonitorRecording;
import net.jadefisher.monkeystatus.model.monitor.RecordingType;
import net.jadefisher.monkeystatus.model.service.Service;
import net.jadefisher.monkeystatus.model.service.ServiceEvent;
import net.jadefisher.monkeystatus.model.service.ServiceEventChange;
import net.jadefisher.monkeystatus.model.service.ServiceEventType;
import net.jadefisher.monkeystatus.respository.EventHistoryRepository;
import net.jadefisher.monkeystatus.respository.MonitorHistoryRepository;
import net.jadefisher.monkeystatus.respository.MonitorRepository;
import net.jadefisher.monkeystatus.respository.ServiceRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventManagerImpl implements EventManager {
	private static final Log log = LogFactory.getLog(EventManagerImpl.class);

	@Autowired
	private AlertManager alertManager;

	@Autowired
	private MonitorHistoryRepository monitorHistoryRepo;

	@Autowired
	private EventHistoryRepository eventHistoryRepo;

	@Autowired
	private ServiceRepository serviceRepo;

	@Autowired
	private MonitorRepository monitorRepo;

	@Override
	public void logMonitorResult(Monitor monitor, RecordingType type,
			String message) {
		// Record monitor reading
		if (type == RecordingType.FAILED)
			log.warn("monitor: " + monitor.getKey() + " " + message);
		monitorHistoryRepo.create(new MonitorRecording(monitor, message, type));

		Service service = monitor.getServiceKey() != null ? serviceRepo
				.find(monitor.getServiceKey()) : null;

		// Update events on the service
		if (service != null) {
			ServiceEventChange change = updateServiceEvent(service);

			if (change != null) {
				alertManager.statusChange(change);
			}
		}
	}

	private ServiceEventChange updateServiceEvent(Service service) {
		ServiceEvent currentEvent = service.getCurrentEvent();
		ServiceEventChange change = null;

		Map<Monitor, MonitorRecording> currentState = new HashMap<Monitor, MonitorRecording>();

		for (Monitor monitor : monitorRepo.findByService(service.getKey())) {
			MonitorRecording recentEntry = monitorHistoryRepo
					.findMostRecentByMonitor(monitor.getKey());

			if (recentEntry != null) {
				currentState.put(monitor, recentEntry);
			}
		}
		ServiceEvent newEvent = createServiceEvent(service.getKey(),
				currentState);

		if (newEvent == null) {
			if (currentEvent != null && !currentEvent.getType().isPlanned()) {
				service = serviceRepo.clearCurrentEvent(service);
				eventHistoryRepo.create(currentEvent);
				change = new ServiceEventChange(service, currentState, null,
						currentEvent);
			}
		} else if (currentEvent == null) {
			service = serviceRepo.setCurrentEvent(service, newEvent);
			change = new ServiceEventChange(service, currentState, newEvent,
					currentEvent);
		} else if (currentEvent.getType() == newEvent.getType()) {
			if (!currentEvent.getType().isPlanned()) {
				serviceRepo.updateCurrentEvent(service,
						newEvent.getDescription());
			}
		} else {
			if (!currentEvent.getType().isPlanned()
					|| currentEvent.getType().isPlanned()
					&& newEvent.getType().moreSevere(currentEvent.getType())) {
				service = serviceRepo.setCurrentEvent(service, newEvent);
				eventHistoryRepo.create(currentEvent);
				change = new ServiceEventChange(service, currentState,
						newEvent, currentEvent);
			}
		}
		return change;
	}

	private ServiceEvent createServiceEvent(String serviceId,
			Map<Monitor, MonitorRecording> currentState) {
		List<String> failedMonitors = new ArrayList<String>();
		ServiceEventType currentEventType = null;

		for (Map.Entry<Monitor, MonitorRecording> e : currentState.entrySet()) {
			if (e.getValue().getLogType() == RecordingType.FAILED) {
				failedMonitors.add(e.getKey().getName());
				if (currentEventType == null
						|| e.getKey().getServiceEventType()
								.moreSevere(currentEventType)) {
					currentEventType = e.getKey().getServiceEventType();
				}
			}
		}
		return currentEventType == null ? null : new ServiceEvent(serviceId,
				currentEventType, failedMonitors.toString()
						+ " are in a failed state");
	}
}
