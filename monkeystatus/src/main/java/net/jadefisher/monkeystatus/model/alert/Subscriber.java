package net.jadefisher.monkeystatus.model.alert;

import java.util.Set;

import net.jadefisher.monkeystatus.model.service.ServiceEventType;

public class Subscriber {
	private Set<String> serviceIds;

	private Set<String> tags;

	private Set<ServiceEventType> eventTypes;

	private String recipient;

	public Set<String> getServiceIds() {
		return serviceIds;
	}

	public void setServiceIds(Set<String> serviceIds) {
		this.serviceIds = serviceIds;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public Set<ServiceEventType> getEventTypes() {
		return eventTypes;
	}

	public void setEventTypes(Set<ServiceEventType> eventTypes) {
		this.eventTypes = eventTypes;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
}
