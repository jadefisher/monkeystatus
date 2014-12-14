package net.jadefisher.monkeystatus.respository;

import net.jadefisher.monkeystatus.model.MonitorLogEntry;

public interface MonitorLogRepository {
	void appendMonitorLog(MonitorLogEntry logEntry);
}
