package net.jadefisher.monkeystatus.respository;

import java.util.List;

import net.jadefisher.monkeystatus.model.event.ServiceEvent;
import net.jadefisher.monkeystatus.model.monitor.Monitor;

public interface ServiceEventRepository {
	List<ServiceEvent> findByService(String serviceId);

	ServiceEvent findCurrentEvent(String serviceId);

	void create(ServiceEvent serviceEvent);

	ServiceEvent registerMonitorFail(ServiceEvent serviceEvent,
			Monitor monitor, String message);

	ServiceEvent registerMonitorPass(ServiceEvent serviceEvent, Monitor monitor);

	void endEvent(ServiceEvent serviceEvent);
}
