package net.jadefisher.monkeystatus.respository;

import java.util.List;

import net.jadefisher.monkeystatus.model.Service;

public interface ServiceRepository {
	List<Service> findAll();
}
