package net.jadefisher.monkeystatus.event;

import net.jadefisher.monkeystatus.model.LogType;
import net.jadefisher.monkeystatus.model.MonitorLogEntry;
import net.jadefisher.monkeystatus.model.monitor.Monitor;
import net.jadefisher.monkeystatus.respository.MonitorLogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventManagerImpl implements EventManager {

	@Autowired
	private MonitorLogRepository monitorLogRepository;

	@Override
	public void logMonitor(Monitor monitor, LogType type, String message) {
		// Record monitor reading
		MonitorLogEntry logEntry = new MonitorLogEntry(monitor, message, type);
		monitorLogRepository.appendMonitorLog(logEntry);

		// TODO need to create events on the service

	}
}
