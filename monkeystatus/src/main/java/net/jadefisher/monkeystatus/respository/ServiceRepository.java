package net.jadefisher.monkeystatus.respository;

import java.util.List;

import net.jadefisher.monkeystatus.model.monitor.Monitor;
import net.jadefisher.monkeystatus.model.service.Service;
import net.jadefisher.monkeystatus.model.service.ServiceEvent;

public interface ServiceRepository {
	List<Service> findAll();

	Service find(String serviceId);

	Service setCurrentEvent(Service service, ServiceEvent currentEvent);

	Service updateCurrentEvent(Service service, Monitor monitor,
			String message, boolean failed);

	Service clearCurrentEvent(Service service);
}
