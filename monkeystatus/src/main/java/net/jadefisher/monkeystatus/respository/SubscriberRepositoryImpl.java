package net.jadefisher.monkeystatus.respository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.jadefisher.monkeystatus.model.alert.Subscriber;
import net.jadefisher.monkeystatus.model.service.ServiceEventType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.Yaml;

import com.google.common.collect.Sets;

@Repository
public class SubscriberRepositoryImpl implements SubscriberRepository {
	@Value("${monkeystatus.subscriberDefsPath}")
	private String subscriberDefsPath;

	private List<Subscriber> subscribers;

	@SuppressWarnings("unchecked")
	@Override
	public List<String> find(String serviceId, Set<String> tags,
			ServiceEventType eventType) {
		List<String> subs = new ArrayList<String>();
		if (subscribers == null) {
			Yaml yaml = new Yaml();

			InputStream yamlStream = null;
			try {
				yamlStream = new FileInputStream(subscriberDefsPath);
				subscribers = (List<Subscriber>) yaml.load(yamlStream);

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

		for (Subscriber s : subscribers) {
			if (s.getServiceIds().contains(serviceId) && filterByTags(s, tags)
					&& filterByType(s, eventType)) {
				subs.add(s.getRecipient());
			}
		}

		return subs;
	}

	private boolean filterByTags(Subscriber s, Set<String> tags) {
		if (s.getTags() == null) {
			return true;
		}
		return tags == null ? false : !Sets.intersection(s.getTags(), tags)
				.isEmpty();
	}

	private boolean filterByType(Subscriber s, ServiceEventType eventType) {
		if (s.getEventTypes() == null) {
			return true;
		}
		return s.getEventTypes().contains(eventType);
	}
}
