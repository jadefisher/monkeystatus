package net.jadefisher.monkeystatus.event;

import net.jadefisher.monkeystatus.model.LogType;
import net.jadefisher.monkeystatus.model.MonitorLogEntry;
import net.jadefisher.monkeystatus.model.event.ServiceEvent;
import net.jadefisher.monkeystatus.model.event.ServiceEventType;
import net.jadefisher.monkeystatus.model.monitor.Monitor;
import net.jadefisher.monkeystatus.respository.MonitorLogRepository;
import net.jadefisher.monkeystatus.respository.ServiceEventRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventManagerImpl implements EventManager {
	private static final Log log = LogFactory.getLog(EventManagerImpl.class);

	@Autowired
	private MonitorLogRepository monitorLogRepository;

	@Autowired
	private ServiceEventRepository serviceEventRepository;

	@Override
	public void logMonitor(Monitor monitor, LogType type, String message) {
		// Record monitor reading
		MonitorLogEntry logEntry = new MonitorLogEntry(monitor, message, type);
		monitorLogRepository.create(logEntry);

		// TODO need to create events on the service
		if (monitor.getServiceId() != null) {
			ServiceEvent currentEvent = serviceEventRepository
					.findCurrentEvent(monitor.getServiceId());

			switch (type) {
			case ERROR:
				break;
			case FAILED:
				if (currentEvent == null) {
					serviceEventRepository.create(new ServiceEvent(monitor,
							message));
				} else if (currentEvent.getType() != ServiceEventType.PLANNED_OUTAGE) {
					serviceEventRepository.registerFail(currentEvent, monitor,
							message);
				}
				break;
			case PASSED:
				if (currentEvent != null
						&& currentEvent.getType() != ServiceEventType.PLANNED_OUTAGE) {
					serviceEventRepository.registerPass(currentEvent, monitor);
				}
				break;
			default:
				break;
			}
		}
	}
}
