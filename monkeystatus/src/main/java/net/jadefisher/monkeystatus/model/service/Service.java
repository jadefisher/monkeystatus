package net.jadefisher.monkeystatus.model.service;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "services")
public class Service {
	@Id
	private String docId;

	@Indexed
	private String key;

	private String name;

	private String owner;

	private ServiceEvent currentEvent;

	private Set<MaintenanceWindow> maintenanceWindows;

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public ServiceEvent getCurrentEvent() {
		return currentEvent;
	}

	public void setCurrentEvent(ServiceEvent currentEvent) {
		this.currentEvent = currentEvent;
	}

	public Set<MaintenanceWindow> getMaintenanceWindows() {
		return maintenanceWindows;
	}

	public void setMaintenanceWindows(Set<MaintenanceWindow> maintenanceWindows) {
		this.maintenanceWindows = maintenanceWindows;
	}
}
