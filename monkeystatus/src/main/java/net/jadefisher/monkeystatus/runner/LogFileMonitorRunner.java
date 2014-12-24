package net.jadefisher.monkeystatus.runner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import net.jadefisher.monkeystatus.event.EventManager;
import net.jadefisher.monkeystatus.model.monitor.LogFileMonitor;
import net.jadefisher.monkeystatus.model.monitor.RecordingType;
import net.jadefisher.monkeystatus.respository.ServiceRepository;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogFileMonitorRunner extends MonitorRunner<LogFileMonitor>
		implements TailerListener {
	private static final Log log = LogFactory
			.getLog(LogFileMonitorRunner.class);

	private ScheduledExecutorService executorService;
	private ScheduledFuture<?> future;
	private final List<Pattern> patterns;
	private EventManager eventManager;
	private Tailer tailer;
	private long lastFailure;
	private long lastPass;

	public LogFileMonitorRunner(ServiceRepository serviceReop,
			ScheduledExecutorService executorService, LogFileMonitor monitor) {
		super(serviceReop);
		this.executorService = executorService;
		this.monitor = monitor;
		this.patterns = new ArrayList<Pattern>();
		for (String pattern : monitor.getPatterns())
			this.patterns.add(Pattern.compile(pattern));
	}

	@Override
	public void startMonitoring(EventManager eventManager) {
		this.eventManager = eventManager;
		log.warn("Checking monitor: " + monitor.getName()
				+ " ----------------------------------------");

		future = this.executorService.schedule(this::setUpMonitoring, 10,
				TimeUnit.SECONDS);
	}

	@Override
	public void stopMonitoring() {
		log.warn("Done checking monitor: " + monitor.getName()
				+ " ----------------------------------------");
		tailer.stop();
		future.cancel(true);
	}

	private void setUpMonitoring() {
		tailer = new Tailer(new File(this.monitor.getLogFile()), this, 500);
		tailer.run();
	}

	@Override
	public void handle(String line) {

		if (!monitorServiceNow(monitor.getServiceKey())) {
			log.info("Skipping monitoring " + monitor.getKey()
					+ " as now is a maintenance window");
			return;
		}

		long stablePeriodMillis = monitor.getRequiredStablePeriod() * 1000;
		long now = System.currentTimeMillis();

		for (Pattern pattern : patterns) {
			if (pattern.matcher(line).matches()) {
				log.warn(monitor.getKey() + " failed!");
				eventManager.logMonitorResult(monitor, RecordingType.FAILED,
						monitor.getLogFile() + " contained pattern match for "
								+ pattern);
				lastFailure = now;
			}
		}

		// We don't want to create 1000s of PASSED logs, but we do want to
		// create them regularly
		if ((now - lastFailure) > stablePeriodMillis
				&& (now - lastPass) > stablePeriodMillis) {
			log.warn(monitor.getKey() + " passed!");
			eventManager.logMonitorResult(monitor, RecordingType.PASSED, null);
			lastPass = now;
		}
	}

	@Override
	public void init(Tailer tailer) {
	}

	@Override
	public void fileNotFound() {

		if (!monitorServiceNow(monitor.getServiceKey())) {
			log.info("Skipping monitoring as now is a maintenance window");
			return;
		}

		this.eventManager.logMonitorResult(monitor, RecordingType.ERROR,
				"Couldn't find log file: " + this.monitor.getLogFile());
	}

	@Override
	public void fileRotated() {
	}

	@Override
	public void handle(Exception ex) {

		if (!monitorServiceNow(monitor.getServiceKey())) {
			log.info("Skipping monitoring as now is a maintenance window");
			return;
		}

		this.eventManager.logMonitorResult(monitor, RecordingType.ERROR,
				"Couldn't read log file: " + this.monitor.getLogFile());
	}
}
