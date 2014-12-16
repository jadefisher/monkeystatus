package net.jadefisher.monkeystatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.jadefisher.monkeystatus.event.EventManager;
import net.jadefisher.monkeystatus.model.monitor.EndPointMonitor;
import net.jadefisher.monkeystatus.model.monitor.LogFileMonitor;
import net.jadefisher.monkeystatus.model.monitor.Monitor;
import net.jadefisher.monkeystatus.model.monitor.PingMonitor;
import net.jadefisher.monkeystatus.model.monitor.TelnetMonitor;
import net.jadefisher.monkeystatus.respository.MonitorRepository;
import net.jadefisher.monkeystatus.runner.EndPointMonitorRunner;
import net.jadefisher.monkeystatus.runner.LogFileMonitorRunner;
import net.jadefisher.monkeystatus.runner.MonitorRunner;
import net.jadefisher.monkeystatus.runner.PingMonitorRunner;
import net.jadefisher.monkeystatus.runner.TelnetMonitorRunner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MonitorManagerImpl implements MonitorManager {
	private static final Log log = LogFactory.getLog(MonitorManagerImpl.class);

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
		log.info("Starting up");
		runners = new ArrayList<MonitorRunner<?>>();
		List<Monitor> monitors = monitorRepository.findAll();

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
