package net.jadefisher.monkeystatus.respository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.annotation.PostConstruct;

import net.jadefisher.monkeystatus.model.service.Service;
import net.jadefisher.monkeystatus.model.service.ServiceEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.Yaml;

import com.mongodb.WriteResult;

@Repository
public class ServiceRepositoryImpl implements ServiceRepository {
	private static final Log log = LogFactory
			.getLog(ServiceRepositoryImpl.class);

	@Autowired
	private MongoOperations mongoOperation;

	@Value("${monkeystatus.serviceDefsPath}")
	private String serviceDefsPath;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		Yaml yaml = new Yaml();

		InputStream yamlStream = null;
		try {
			yamlStream = new FileInputStream(serviceDefsPath);
			List<Service> services = (List<Service>) yaml.load(yamlStream);

			mongoOperation.dropCollection(Service.class);
			mongoOperation.insert(services, Service.class);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (yamlStream != null) {
				try {
					yamlStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public List<Service> findAll() {
		return mongoOperation.findAll(Service.class);
	}

	@Override
	public Service find(String serviceKey) {
		return mongoOperation.findOne(
				Query.query(Criteria.where("key").is(serviceKey)),
				Service.class);
	}

	@Override
	public Service setCurrentEvent(Service service, ServiceEvent currentEvent) {
		Update update = new Update();
		update.set("currentEvent", currentEvent);
		WriteResult result = mongoOperation.updateFirst(
				Query.query(Criteria
						.where("key")
						.is(service.getKey())
						.andOperator(
								Criteria.where("version").is(
										service.getVersion()))),
				new Update().set("currentEvent", currentEvent), Service.class);
		if (result.getN() == 1) {
			return find(service.getKey());
		} else {
			throw new ConcurrentModificationException("Service is out of date");
		}
	}

	@Override
	public Service clearCurrentEvent(Service service) {
		return setCurrentEvent(service, null);
	}

	@Override
	public void updateCurrentEvent(Service service, String description) {
		WriteResult result = mongoOperation.updateFirst(
				Query.query(Criteria
						.where("key")
						.is(service.getKey())
						.andOperator(
								Criteria.where("version").is(
										service.getVersion()))),
				new Update().set("currentEvent.description", description),
				Service.class);
		if (result.getN() != 1) {
			throw new ConcurrentModificationException("Service is out of date");
		}
	}
}
