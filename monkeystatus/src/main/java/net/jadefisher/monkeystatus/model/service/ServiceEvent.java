package net.jadefisher.monkeystatus.model.service;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "events")
public class ServiceEvent {
	@Id
	private String docId;

	@Indexed
	private String serviceKey;

	private Map<String, Boolean> associatedMonitors;

	private ServiceEventType type;

	private String description;

	@Indexed
	private Date startDate;

	@Indexed
	private Date endDate;

	public ServiceEvent() {
		this.startDate = new Date();
	}

	public ServiceEvent(String serviceKey, ServiceEventType type,
			String description) {
		this();
		this.serviceKey = serviceKey;
		this.type = type;
		this.description = description;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getServiceKey() {
		return serviceKey;
	}

	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
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
