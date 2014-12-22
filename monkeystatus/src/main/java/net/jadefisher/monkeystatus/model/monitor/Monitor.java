package net.jadefisher.monkeystatus.model.monitor;

import java.util.Set;

import net.jadefisher.monkeystatus.model.service.ServiceEventType;

public abstract class Monitor {
	private String id;

	private String name;

	private String serviceId;

	private ServiceEventType serviceEventType;

	private Set<String> tags;

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

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public ServiceEventType getServiceEventType() {
		return serviceEventType;
	}

	public void setServiceEventType(ServiceEventType serviceEventType) {
		this.serviceEventType = serviceEventType;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public String getType() {
		return this.getClass().getName();
	}
}
