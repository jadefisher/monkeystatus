package net.jadefisher.monkeystatus.runner;

import net.jadefisher.monkeystatus.event.EventManager;
import net.jadefisher.monkeystatus.model.monitor.Monitor;

public abstract class MonitorRunner<T extends Monitor> {

	protected T monitor;

	public abstract void startMonitoring(EventManager eventManager);

	public abstract void stopMonitoring();
}
