package net.jadefisher.appmonitor.respository;

import java.util.List;

import net.jadefisher.appmonitor.model.Service;

public interface ServiceRepository {
	List<Service> getServices();
}
