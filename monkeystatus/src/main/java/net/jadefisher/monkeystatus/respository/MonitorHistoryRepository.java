package net.jadefisher.monkeystatus.respository;

import java.util.List;

import net.jadefisher.monkeystatus.model.monitor.MonitorRecording;

public interface MonitorHistoryRepository {
	void create(MonitorRecording logEntry);

	List<MonitorRecording> findByService(String serviceKey);

	List<MonitorRecording> findByMonitor(String monitorKey);

	MonitorRecording findMostRecentByMonitor(String monitorKey);
}
