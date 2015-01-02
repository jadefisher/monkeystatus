package net.jadefisher.monkeystatus.model.monitor;

import java.util.Date;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "recordings")
public class MonitorRecording {
	@Id
	private String docId;

	@Indexed
	private String serviceKey;

	@Indexed
	private String monitorKey;

	private RecordingType logType;

	@Indexed
	private Set<String> tags;

	@Indexed
	private Date timestamp;

	private String message;

	public MonitorRecording() {
	}

	public MonitorRecording(Monitor monitor, String message,
			RecordingType logType) {
		this.timestamp = new Date();
		this.serviceKey = monitor.getServiceKey();
		this.monitorKey = monitor.getKey();
		this.tags = monitor.getTags();
		this.message = message;
		this.logType = logType;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public RecordingType getLogType() {
		return logType;
	}

	public void setLogType(RecordingType logType) {
		this.logType = logType;
	}

	public String getServiceKey() {
		return serviceKey;
	}

	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
	}

	public String getMonitorKey() {
		return monitorKey;
	}

	public void setMonitorKey(String monitorKey) {
		this.monitorKey = monitorKey;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
