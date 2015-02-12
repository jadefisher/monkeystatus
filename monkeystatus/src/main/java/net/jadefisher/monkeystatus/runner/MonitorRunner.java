package net.jadefisher.monkeystatus.runner;

import java.time.LocalDate;
import java.time.LocalTime;

import net.jadefisher.monkeystatus.event.EventManager;
import net.jadefisher.monkeystatus.model.monitor.Monitor;
import net.jadefisher.monkeystatus.model.service.MaintenanceWindow;
import net.jadefisher.monkeystatus.model.service.Service;
import net.jadefisher.monkeystatus.respository.ServiceRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class MonitorRunner<T extends Monitor> {
	private static final Log log = LogFactory.getLog(MonitorRunner.class);

	protected T monitor;

	private ServiceRepository serviceRepo;

	public MonitorRunner(ServiceRepository serviceRepo) {
		this.serviceRepo = serviceRepo;
	}

	protected boolean shouldMonitor() {
		Service service = this.serviceRepo.find(monitor.getServiceKey());
		boolean monitorNow = true;

		if (service != null && service.getMaintenanceWindows() != null) {
			LocalDate today = LocalDate.now();
			LocalTime now = LocalTime.now();
			for (MaintenanceWindow window : service.getMaintenanceWindows()) {
				if (!monitorNow) {
					break;
				}

				if (window.getDaysOfWeek() != null
						&& window.getDaysOfWeek()
								.contains(today.getDayOfWeek())) {

					if (window.getStart() != null && window.getEnd() != null) {
						LocalTime windowStart = LocalTime.of(window.getStart()
								.getHours(), window.getStart().getMinutes());
						LocalTime windowEnd = LocalTime.of(window.getEnd()
								.getHours(), window.getEnd().getMinutes());

						if (windowStart.isBefore(windowEnd)) {
							monitorNow = now.isBefore(windowStart)
									|| now.isAfter(windowEnd);
						} else {
							monitorNow = now.isAfter(windowEnd)
									&& now.isBefore(windowStart);
						}
					} else {
						monitorNow = false;
					}
				}
			}
		}

		if (!monitorNow) {
			log.info("Skipping monitoring " + monitor.getKey()
					+ " as now is a maintenance window");
		}

		return monitorNow;
	}

	public abstract void startMonitoring(EventManager eventManager);

	public abstract void stopMonitoring();
}
