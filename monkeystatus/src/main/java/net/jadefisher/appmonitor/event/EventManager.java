package net.jadefisher.appmonitor.event;

import net.jadefisher.appmonitor.model.LogType;
import net.jadefisher.appmonitor.model.monitor.Monitor;

public interface EventManager {

	void logMonitor(Monitor monitor, LogType type, String message);
}
