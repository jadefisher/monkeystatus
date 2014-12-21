package net.jadefisher.monkeystatus.model.service;

import java.util.Set;

public class Service {
	private String id;

	private String name;

	private String owner;

	private ServiceEvent currentEvent;

	private Set<MaintenanceWindow> maintenanceWindows;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
