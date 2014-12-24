package net.jadefisher.monkeystatus.respository;

import java.util.List;

import net.jadefisher.monkeystatus.model.monitor.Monitor;

public interface MonitorRepository {

	List<Monitor> findAll();

	Monitor find(String monitorKey);

	List<Monitor> findByService(String serviceKey);
}
