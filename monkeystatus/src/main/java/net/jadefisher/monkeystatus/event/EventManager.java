package net.jadefisher.monkeystatus.event;

import net.jadefisher.monkeystatus.model.monitor.RecordingType;
import net.jadefisher.monkeystatus.model.monitor.Monitor;

public interface EventManager {

	void logMonitorResult(Monitor monitor, RecordingType type, String message);
}
