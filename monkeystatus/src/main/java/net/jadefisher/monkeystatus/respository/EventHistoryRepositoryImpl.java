package net.jadefisher.monkeystatus.respository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jadefisher.monkeystatus.model.monitor.MonitorRecording;
import net.jadefisher.monkeystatus.model.service.ServiceEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class EventHistoryRepositoryImpl implements EventHistoryRepository {
	@Value("${monkeystatus.events.minimumTime}")
	private int minimumTime;

	@Autowired
	private MongoOperations mongoOperation;
	
	@Override
	public List<ServiceEvent> findByService(String serviceKey) {
		return mongoOperation.find(
				Query.query(Criteria.where("serviceKey").is(serviceKey)),
				ServiceEvent.class);
	}

	@Override
	public void create(ServiceEvent serviceEvent) {
		mongoOperation.insert(serviceEvent);
	}
}
