package net.jadefisher.monkeystatus.respository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jadefisher.monkeystatus.model.service.ServiceEvent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class EventHistoryRepositoryImpl implements EventHistoryRepository {

	private Map<String, List<ServiceEvent>> events = new HashMap<String, List<ServiceEvent>>();

	@Value("${monkeystatus.events.minimumTime}")
	private int minimumTime;

	@Override
	public List<ServiceEvent> findByService(String serviceId) {
		return events.get(serviceId);
	}

	@Override
	public void create(ServiceEvent serviceEvent) {
		if (!events.containsKey(serviceEvent.getServiceKey()))
			events.put(serviceEvent.getServiceKey(),
					new ArrayList<ServiceEvent>());

		serviceEvent.setEndDate(new Date());

		// if ((serviceEvent.getEndDate().getTime() -
		// serviceEvent.getStartDate()
		// .getTime()) >= minimumTime)
		events.get(serviceEvent.getServiceKey()).add(serviceEvent);
	}
}
