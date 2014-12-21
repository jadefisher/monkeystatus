package net.jadefisher.monkeystatus.respository;

import java.util.List;

import net.jadefisher.monkeystatus.model.service.ServiceEvent;

public interface EventHistoryRepository {
	List<ServiceEvent> findByService(String serviceId);

	void create(ServiceEvent serviceEvent);
}
