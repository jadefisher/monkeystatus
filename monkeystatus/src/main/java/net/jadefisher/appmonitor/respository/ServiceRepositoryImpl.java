package net.jadefisher.appmonitor.respository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.jadefisher.appmonitor.model.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.Yaml;

@Repository
public class ServiceRepositoryImpl implements ServiceRepository {

	@Value("${appmonitor.serviceDefsPath}")
	private String serviceDefsPath;

	@SuppressWarnings("unchecked")
	@Override
	public List<Service> getServices() {
		Yaml yaml = new Yaml();

		InputStream yamlStream = null;
		try {
			yamlStream = new FileInputStream(serviceDefsPath);
			return (List<Service>) yaml.load(yamlStream);

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
		return null;
	}

}
