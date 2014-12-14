package net.jadefisher.appmonitor.respository;

import net.jadefisher.appmonitor.model.MonitorLogEntry;

public interface MonitorLogRepository {
	void appendMonitorLog(MonitorLogEntry logEntry);
}
