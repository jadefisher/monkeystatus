package net.jadefisher.monkeystatus.respository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jadefisher.monkeystatus.model.MonitorLogEntry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

@Repository
public class MonitorLogRepositoryImpl implements MonitorLogRepository {
	private static final Log log = LogFactory
			.getLog(MonitorLogRepositoryImpl.class);

	private Map<String, List<MonitorLogEntry>> entriesByServiceId = new HashMap<String, List<MonitorLogEntry>>();

	private Map<String, List<MonitorLogEntry>> entriesByMonitorId = new HashMap<String, List<MonitorLogEntry>>();

	@Override
	public void create(MonitorLogEntry logEntry) {
		try {
			// String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")
			// .format(logEntry.getCreatedDate());
			// log.warn(dateStr + " " + logEntry.getLogType() + " "
			// + logEntry.getServiceId() + " - " + logEntry.getMonitorId()
			// + " - " + logEntry.getMessage());

			if (!entriesByServiceId.containsKey(logEntry.getServiceId()))
				entriesByServiceId.put(logEntry.getServiceId(),
						new ArrayList<MonitorLogEntry>());

			if (!entriesByMonitorId.containsKey(logEntry.getMonitorId()))
				entriesByMonitorId.put(logEntry.getMonitorId(),
						new ArrayList<MonitorLogEntry>());

			entriesByServiceId.get(logEntry.getServiceId()).add(0, logEntry);
			entriesByMonitorId.get(logEntry.getMonitorId()).add(0, logEntry);

		} catch (Exception e) {
			log.warn("exception logging monitor log", e);
		}
	}

	@Override
	public List<MonitorLogEntry> findByService(String serviceId) {
		return entriesByServiceId.get(serviceId);
	}

	@Override
	public List<MonitorLogEntry> findByMonitor(String monitorId) {
		return entriesByMonitorId.get(monitorId);
	}
}
