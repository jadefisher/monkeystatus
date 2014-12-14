package net.jadefisher.appmonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.jadefisher.appmonitor.event.EventManager;
import net.jadefisher.appmonitor.model.monitor.EndPointMonitor;
import net.jadefisher.appmonitor.model.monitor.LogFileMonitor;
import net.jadefisher.appmonitor.model.monitor.Monitor;
import net.jadefisher.appmonitor.model.monitor.PingMonitor;
import net.jadefisher.appmonitor.model.monitor.TelnetMonitor;
import net.jadefisher.appmonitor.respository.MonitorRepository;
import net.jadefisher.appmonitor.runner.EndPointMonitorRunner;
import net.jadefisher.appmonitor.runner.LogFileMonitorRunner;
import net.jadefisher.appmonitor.runner.MonitorRunner;
import net.jadefisher.appmonitor.runner.PingMonitorRunner;
import net.jadefisher.appmonitor.runner.TelnetMonitorRunner;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MonitorManagerImpl implements MonitorManager {
	@Autowired
	private MonitorRepository monitorRepository;

	@Autowired
	private EventManager eventManager;

	@Autowired
	private PoolingHttpClientConnectionManager cmgr;

	@Autowired
	private ScheduledExecutorService scheduledExecutorService;

	private List<MonitorRunner<?>> runners;

	@PostConstruct
	@Override
	public void initialiseMonitoring() {
		System.out.println("hello");
		runners = new ArrayList<MonitorRunner<?>>();
		List<Monitor> monitors = monitorRepository.getMonitors();

		for (Monitor monitor : monitors) {
			MonitorRunner<?> runner = null;
			if (monitor instanceof EndPointMonitor) {
				runner = new EndPointMonitorRunner(cmgr,
						scheduledExecutorService, (EndPointMonitor) monitor);
			} else if (monitor instanceof LogFileMonitor) {
				runner = new LogFileMonitorRunner(scheduledExecutorService,
						(LogFileMonitor) monitor);
			} else if (monitor instanceof TelnetMonitor) {
				runner = new TelnetMonitorRunner(scheduledExecutorService,
						(TelnetMonitor) monitor);
			} else if (monitor instanceof PingMonitor) {
				runner = new PingMonitorRunner(scheduledExecutorService,
						(PingMonitor) monitor);
			}

			if (runner != null) {
				runners.add(runner);
				runner.startMonitoring(eventManager);
			}
		}
	}

	@PreDestroy
	public void shutdown() {
		for (MonitorRunner<?> runner : runners) {
			runner.stopMonitoring();
		}
		this.scheduledExecutorService.shutdown();
	}

}
