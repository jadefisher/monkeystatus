package net.jadefisher.monkeystatus.model.event;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.jadefisher.monkeystatus.model.monitor.Monitor;

public class ServiceEvent {
	private String serviceId;

	private Map<String, Boolean> associatedMonitors;

	private ServiceEventType type;

	private String description;

	private Date startDate;

	private Date endDate;

	public ServiceEvent() {
		this.startDate = new Date();
	}

	public ServiceEvent(Monitor monitor, String message) {
		this();
		this.serviceId = monitor.getServiceId();
		this.description = message;
		this.associatedMonitors = new HashMap<String, Boolean>();
		this.associatedMonitors.put(monitor.getId(), true);
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Map<String, Boolean> getAssociatedMonitors() {
		return associatedMonitors;
	}

	public void setAssociatedMonitors(Map<String, Boolean> associatedMonitors) {
		this.associatedMonitors = associatedMonitors;
	}

	public ServiceEventType getType() {
		return type;
	}

	public void setType(ServiceEventType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
