package net.jadefisher.monkeystatus.model.monitor;

import java.util.Date;
import java.util.Set;

public class MonitorLogEntry {
	private String serviceId;

	private String monitorId;

	private LogType logType;

	private Set<String> tags;

	private Date createdDate;

	private String message;

	public MonitorLogEntry() {
		this.createdDate = new Date();
	}

	public MonitorLogEntry(Monitor monitor, String message, LogType logType) {
		this();
		this.serviceId = monitor.getServiceId();
		this.monitorId = monitor.getId();
		this.tags = monitor.getTags();
		this.message = message;
		this.logType = logType;
	}

	public LogType getLogType() {
		return logType;
	}

	public void setLogType(LogType logType) {
		this.logType = logType;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getMonitorId() {
		return monitorId;
	}

	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
