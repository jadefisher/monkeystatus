package net.jadefisher.appmonitor.respository;

import java.util.List;

import net.jadefisher.appmonitor.model.monitor.Monitor;

public interface MonitorRepository {

	List<Monitor> getMonitors();
}
