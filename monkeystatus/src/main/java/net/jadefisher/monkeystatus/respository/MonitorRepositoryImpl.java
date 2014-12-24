package net.jadefisher.monkeystatus.respository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.jadefisher.monkeystatus.model.monitor.Monitor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.Yaml;

@Repository
public class MonitorRepositoryImpl implements MonitorRepository {

	@Value("${monkeystatus.monitorDefsPath}")
	private String monitorDefsPath;

	private List<Monitor> monitors;

	@SuppressWarnings("unchecked")
	@Override
	public List<Monitor> findAll() {

		if (monitors == null) {
			Yaml yaml = new Yaml();

			InputStream yamlStream = null;
			try {
				yamlStream = new FileInputStream(monitorDefsPath);
				monitors = (List<Monitor>) yaml.load(yamlStream);

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

		return monitors;
	}

	@Override
	public Monitor find(String monitorKey) {
		if (this.monitors != null) {
			for (Monitor monitor : this.monitors) {
				if (monitor.getKey().equals(monitorKey))
					return monitor;
			}
		}
		return null;
	}

	@Override
	public List<Monitor> findByService(String serviceKey) {
		List<Monitor> serviceMonitors = new ArrayList<Monitor>();
		if (this.monitors != null) {
			for (Monitor monitor : this.monitors) {
				if (monitor.getServiceKey().equals(serviceKey))
					serviceMonitors.add(monitor);
			}
		}
		return serviceMonitors;
	}

}
