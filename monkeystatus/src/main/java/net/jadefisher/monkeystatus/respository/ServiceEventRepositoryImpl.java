package net.jadefisher.monkeystatus.respository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jadefisher.monkeystatus.model.event.ServiceEvent;
import net.jadefisher.monkeystatus.model.monitor.Monitor;

import org.springframework.stereotype.Repository;

@Repository
public class ServiceEventRepositoryImpl implements ServiceEventRepository {

	private Map<String, List<ServiceEvent>> events = new HashMap<String, List<ServiceEvent>>();

	@Override
	public List<ServiceEvent> findByService(String serviceId) {
		return events.get(serviceId);
	}

	@Override
	public ServiceEvent findCurrentEvent(String serviceId) {
		List<ServiceEvent> serviceEvents = findByService(serviceId);
		if (serviceEvents != null) {
			for (ServiceEvent event : serviceEvents) {
				if (event.getEndDate() == null) {
					return event;
				}
			}
		}
		return null;
	}

	@Override
	public void create(ServiceEvent serviceEvent) {
		if (!events.containsKey(serviceEvent.getServiceId()))
			events.put(serviceEvent.getServiceId(),
					new ArrayList<ServiceEvent>());

		events.get(serviceEvent.getServiceId()).add(serviceEvent);
	}

	@Override
	public ServiceEvent registerMonitorFail(ServiceEvent serviceEvent,
			Monitor monitor, String message) {
		serviceEvent.getAssociatedMonitors().put(monitor.getId(), true);
		return serviceEvent;
	}

	@Override
	public ServiceEvent registerMonitorPass(ServiceEvent serviceEvent,
			Monitor monitor) {
		if (serviceEvent.getAssociatedMonitors().keySet()
				.contains(monitor.getId())) {
			serviceEvent.getAssociatedMonitors().put(monitor.getId(), false);
			if (!serviceEvent.getAssociatedMonitors().values().contains(true)) {
				endEvent(serviceEvent);
			}
		}
		return serviceEvent;
	}

	@Override
	public void endEvent(ServiceEvent serviceEvent) {
		serviceEvent.setEndDate(new Date());
	}
}
