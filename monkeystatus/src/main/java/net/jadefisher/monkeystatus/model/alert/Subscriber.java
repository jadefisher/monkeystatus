package net.jadefisher.monkeystatus.model.alert;

import java.util.Set;

import net.jadefisher.monkeystatus.model.service.ServiceEventType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "subscribers")
public class Subscriber {
	@Id
	private String docId;

	private Set<String> serviceKeys;

	private Set<String> tags;

	private Set<ServiceEventType> eventTypes;

	private String recipient;

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public Set<String> getServiceKeys() {
		return serviceKeys;
	}

	public void setServiceKeys(Set<String> serviceKeys) {
		this.serviceKeys = serviceKeys;
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
