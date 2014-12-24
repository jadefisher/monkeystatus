package net.jadefisher.monkeystatus.respository;

import java.util.List;

import net.jadefisher.monkeystatus.model.service.Service;
import net.jadefisher.monkeystatus.model.service.ServiceEvent;

public interface ServiceRepository {
	List<Service> findAll();

	Service find(String serviceKey);

	Service setCurrentEvent(Service service, ServiceEvent currentEvent);

	Service clearCurrentEvent(Service service);

	void updateCurrentEvent(Service service, String description);
}
