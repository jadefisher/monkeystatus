package net.jadefisher.monkeystatus.event;

import net.jadefisher.monkeystatus.alert.AlertManager;
import net.jadefisher.monkeystatus.model.monitor.LogType;
import net.jadefisher.monkeystatus.model.monitor.Monitor;
import net.jadefisher.monkeystatus.model.monitor.MonitorLogEntry;
import net.jadefisher.monkeystatus.model.service.Service;
import net.jadefisher.monkeystatus.model.service.ServiceEvent;
import net.jadefisher.monkeystatus.model.service.ServiceEventType;
import net.jadefisher.monkeystatus.respository.EventHistoryRepository;
import net.jadefisher.monkeystatus.respository.MonitorHistoryRepository;
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

	@Override
	public void logMonitorResult(Monitor monitor, LogType type, String message) {
		// Record monitor reading
		log.warn("monitor: " + monitor.getId() + " " + message);
		monitorHistoryRepo.create(new MonitorLogEntry(monitor, message, type));

		Service service = monitor.getServiceId() != null ? serviceRepo
				.find(monitor.getServiceId()) : null;

		// TODO need to create events on the service
		if (service != null) {
			ServiceEvent currentEvent = service.getCurrentEvent();
			ServiceEventType changedEventType = null;

			switch (type) {
			case ERROR:
				break;
			case FAILED:
				if (currentEvent == null) {
					service = serviceRepo.setCurrentEvent(service,
							new ServiceEvent(monitor, message, null));
					changedEventType = monitor.getServiceEventType();
				} else if (currentEvent.getType() != ServiceEventType.PLANNED_OUTAGE) {

					if (currentEvent.getType().getLevel() > monitor
							.getServiceEventType().getLevel()) {
						// create new event with a new (higher priority) type
						service = serviceRepo
								.setCurrentEvent(service, new ServiceEvent(
										monitor, message, currentEvent));
						eventHistoryRepo.create(currentEvent);
						changedEventType = monitor.getServiceEventType();
					} else {
						// add monitor failure to event
						serviceRepo.updateCurrentEvent(service, monitor,
								message, true);
					}
				}
				break;
			case PASSED:
				if (currentEvent != null
						&& currentEvent.getType() != ServiceEventType.PLANNED_OUTAGE) {
					currentEvent = serviceRepo.updateCurrentEvent(service,
							monitor, message, false).getCurrentEvent();
					if (!currentEvent.getAssociatedMonitors().values()
							.contains(true)) {
						service = serviceRepo.clearCurrentEvent(service);
						eventHistoryRepo.create(currentEvent);
						changedEventType = currentEvent.getType();
					}
				}
				break;
			default:
				break;
			}

			if (changedEventType != null) {
				alertManager.statusChange(service, changedEventType,
						monitor.getTags());
			}
		}
	}
}
