package net.jadefisher.monkeystatus.respository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.jadefisher.monkeystatus.model.monitor.Monitor;
import net.jadefisher.monkeystatus.model.service.Service;
import net.jadefisher.monkeystatus.model.service.ServiceEvent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.Yaml;

@Repository
public class ServiceRepositoryImpl implements ServiceRepository {

	@Value("${monkeystatus.serviceDefsPath}")
	private String serviceDefsPath;

	private List<Service> services;

	@SuppressWarnings("unchecked")
	@Override
	public List<Service> findAll() {

		if (services == null) {
			Yaml yaml = new Yaml();

			InputStream yamlStream = null;
			try {
				yamlStream = new FileInputStream(serviceDefsPath);
				services = (List<Service>) yaml.load(yamlStream);

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
		return services;
	}

	@Override
	public Service find(String serviceId) {
		if (this.services != null) {
			for (Service service : this.services) {
				if (service.getId().equals(serviceId))
					return service;
			}
		}
		return null;
	}

	@Override
	public Service setCurrentEvent(Service service, ServiceEvent currentEvent) {
		if (service != null)
			service.setCurrentEvent(currentEvent);
		return service;
	}

	@Override
	public Service updateCurrentEvent(Service service, Monitor monitor,
			String message, boolean failed) {
		service.getCurrentEvent().getAssociatedMonitors()
				.put(monitor.getId(), failed);
		return service;
	}

	@Override
	public Service clearCurrentEvent(Service service) {
		if (service != null)
			service.setCurrentEvent(null);
		return service;
	}
}
