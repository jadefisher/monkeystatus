package net.jadefisher.monkeystatus.respository;

import java.util.List;

import net.jadefisher.monkeystatus.model.monitor.MonitorRecording;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class MonitorHistoryRepositoryImpl implements MonitorHistoryRepository {
	private static final Log log = LogFactory
			.getLog(MonitorHistoryRepositoryImpl.class);

	@Autowired
	private MongoOperations mongoOperation;

	@Override
	public void create(MonitorRecording recording) {
		try {
			mongoOperation.insert(recording);

		} catch (Exception e) {
			log.warn("exception logging monitor recording", e);
		}
	}

	@Override
	public List<MonitorRecording> findByService(String serviceKey) {
		return mongoOperation.find(
				Query.query(Criteria.where("serviceKey").is(serviceKey)),
				MonitorRecording.class);
	}

	@Override
	public List<MonitorRecording> findByMonitor(String monitorKey) {
		return mongoOperation.find(
				Query.query(Criteria.where("monitorKey").is(monitorKey)),
				MonitorRecording.class);
	}

	@Override
	public MonitorRecording findMostRecentByMonitor(String monitorKey) {
		return mongoOperation.findOne(
				Query.query(Criteria.where("monitorKey").is(monitorKey)).with(
						new Sort(Direction.DESC, "timestamp")),
				MonitorRecording.class);
	}
}
