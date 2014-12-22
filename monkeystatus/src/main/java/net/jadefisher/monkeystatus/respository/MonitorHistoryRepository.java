package net.jadefisher.monkeystatus.respository;

import java.util.List;

import net.jadefisher.monkeystatus.model.monitor.MonitorLogEntry;

public interface MonitorHistoryRepository {
	void create(MonitorLogEntry logEntry);

	List<MonitorLogEntry> findByService(String serviceId);

	List<MonitorLogEntry> findByMonitor(String monitorId);

	MonitorLogEntry findMostRecentByMonitor(String monitorId);
}
