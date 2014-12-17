package net.jadefisher.monkeystatus.respository;

import java.util.List;

import net.jadefisher.monkeystatus.model.MonitorLogEntry;

public interface MonitorLogRepository {
	void create(MonitorLogEntry logEntry);

	List<MonitorLogEntry> findByService(String serviceId);

	List<MonitorLogEntry> findByMonitor(String monitorId);
}
