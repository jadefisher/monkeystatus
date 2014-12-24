package net.jadefisher.monkeystatus.runner;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import net.jadefisher.monkeystatus.event.EventManager;
import net.jadefisher.monkeystatus.model.monitor.PingMonitor;
import net.jadefisher.monkeystatus.model.monitor.RecordingType;
import net.jadefisher.monkeystatus.respository.ServiceRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PingMonitorRunner extends MonitorRunner<PingMonitor> {
	private static final Log log = LogFactory.getLog(PingMonitorRunner.class);

	private ScheduledFuture<?> future;
	private EventManager eventManager;
	private ScheduledExecutorService executorService;

	public PingMonitorRunner(ServiceRepository serviceReop,
			ScheduledExecutorService executorService, PingMonitor monitor) {
		super(serviceReop);
		this.executorService = executorService;
		this.monitor = monitor;
	}

	@Override
	public void startMonitoring(EventManager eventManager) {
		this.eventManager = eventManager;
		log.info("Skipping monitoring " + monitor.getKey()
				+ " as now is a maintenance window");
		this.future = executorService.scheduleAtFixedRate(this::runMonitor, 5,
				20, TimeUnit.SECONDS);
	}

	@Override
	public void stopMonitoring() {
		this.future.cancel(true);
	}

	private void runMonitor() {

		if (!monitorServiceNow(monitor.getServiceKey())) {
			log.info("Skipping monitoring as now is a maintenance window");
			return;
		}

		try {
			InetAddress.getByName(monitor.getTargetHost()).isReachable(
					monitor.getPingTimeout());
			eventManager.logMonitorResult(monitor, RecordingType.PASSED,
					monitor.getTargetHost() + " is reachable");
		} catch (IOException e) {
			eventManager.logMonitorResult(monitor, RecordingType.FAILED,
					monitor.getTargetHost() + " is unreachable");
		}
	}
}
