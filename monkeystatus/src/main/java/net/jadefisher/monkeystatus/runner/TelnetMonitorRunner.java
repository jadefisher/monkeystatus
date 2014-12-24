package net.jadefisher.monkeystatus.runner;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import net.jadefisher.monkeystatus.event.EventManager;
import net.jadefisher.monkeystatus.model.monitor.RecordingType;
import net.jadefisher.monkeystatus.model.monitor.TelnetMonitor;
import net.jadefisher.monkeystatus.respository.ServiceRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TelnetMonitorRunner extends MonitorRunner<TelnetMonitor> {
	private static final Log log = LogFactory.getLog(TelnetMonitorRunner.class);

	private ScheduledFuture<?> future;
	private EventManager eventManager;
	private ScheduledExecutorService executorService;

	public TelnetMonitorRunner(ServiceRepository serviceReop,
			ScheduledExecutorService executorService, TelnetMonitor monitor) {
		super(serviceReop);
		this.executorService = executorService;
		this.monitor = monitor;
	}

	@Override
	public void startMonitoring(EventManager eventManager) {
		this.eventManager = eventManager;
		log.info("Monitoring " + this.monitor.getTargetHost() + ":"
				+ this.monitor.getTargetPort());
		this.future = executorService.scheduleAtFixedRate(this::runMonitor, 5,
				20, TimeUnit.SECONDS);
	}

	@Override
	public void stopMonitoring() {
		this.future.cancel(true);
	}

	private void runMonitor() {

		if (!monitorServiceNow(monitor.getServiceKey())) {
			log.info("Skipping monitoring " + monitor.getKey()
					+ " as now is a maintenance window");
			return;
		}

		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(this.monitor.getTargetHost(),
					this.monitor.getTargetPort()));
			eventManager.logMonitorResult(monitor, RecordingType.PASSED,
					"Connection okay to " + monitor.getTargetHost() + ":"
							+ monitor.getTargetPort());
		} catch (IOException e) {
			eventManager.logMonitorResult(monitor, RecordingType.FAILED,
					"Connection refused to " + monitor.getTargetHost() + ":"
							+ monitor.getTargetPort());
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				log.warn("Exception closing socket");
			}
		}
	}
}
