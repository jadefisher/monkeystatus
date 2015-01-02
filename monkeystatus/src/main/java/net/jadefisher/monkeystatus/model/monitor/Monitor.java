package net.jadefisher.monkeystatus.model.monitor;

import java.util.Set;

import net.jadefisher.monkeystatus.model.service.ServiceEventType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "monitors")
public abstract class Monitor {
	@Id
	private String docId;

	@Indexed
	private String key;

	private String name;

	private String serviceKey;

	private ServiceEventType serviceEventType;

	private Set<String> tags;

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

	public String getServiceKey() {
		return serviceKey;
	}

	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
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
