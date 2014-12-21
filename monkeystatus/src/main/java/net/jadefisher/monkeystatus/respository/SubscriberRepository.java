package net.jadefisher.monkeystatus.respository;

import java.util.List;
import java.util.Set;

import net.jadefisher.monkeystatus.model.service.ServiceEventType;

public interface SubscriberRepository {
	List<String> find(String serviceId, Set<String> tags,
			ServiceEventType eventType);
}
