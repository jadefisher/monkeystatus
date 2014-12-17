package net.jadefisher.monkeystatus.event;

import net.jadefisher.monkeystatus.model.LogType;
import net.jadefisher.monkeystatus.model.monitor.Monitor;

public interface EventManager {

	void logMonitorResult(Monitor monitor, LogType type, String message);
}
