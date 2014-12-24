package net.jadefisher.monkeystatus.model.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.jadefisher.monkeystatus.model.monitor.Monitor;
import net.jadefisher.monkeystatus.model.monitor.MonitorRecording;

public class ServiceEventChange {
	private Service service;
	private Map<Monitor, MonitorRecording> currentState;
	private ServiceEvent currentEvent;
	private ServiceEvent previousEvent;

	public ServiceEventChange(Service service,
			Map<Monitor, MonitorRecording> currentState,
			ServiceEvent currentEvent, ServiceEvent previousEvent) {
		this.service = service;
		this.currentState = currentState;
		this.currentEvent = currentEvent;
		this.previousEvent = previousEvent;
	}

	public Set<String> allTags() {
		Set<String> tags = new HashSet<String>();

		if (this.currentState != null) {
			for (Monitor mon : currentState.keySet()) {
				if (mon.getTags() != null)
					tags.addAll(mon.getTags());
			}
		}

		return tags;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public Map<Monitor, MonitorRecording> getCurrentState() {
		return currentState;
	}

	public void setCurrentState(Map<Monitor, MonitorRecording> currentState) {
		this.currentState = currentState;
	}

	public ServiceEvent getCurrentEvent() {
		return currentEvent;
	}

	public void setCurrentEvent(ServiceEvent currentEvent) {
		this.currentEvent = currentEvent;
	}

	public ServiceEvent getPreviousEvent() {
		return previousEvent;
	}

	public void setPreviousEvent(ServiceEvent previousEvent) {
		this.previousEvent = previousEvent;
	}
}