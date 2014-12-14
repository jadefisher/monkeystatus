package net.jadefisher.appmonitor.runner;

import net.jadefisher.appmonitor.event.EventManager;
import net.jadefisher.appmonitor.model.monitor.Monitor;

public abstract class MonitorRunner<T extends Monitor> {

	protected T monitor;

	public abstract void startMonitoring(EventManager eventManager);

	public abstract void stopMonitoring();
}
