package net.jadefisher.monkeystatus.controller;

import java.util.List;

import net.jadefisher.monkeystatus.model.monitor.Monitor;
import net.jadefisher.monkeystatus.model.monitor.MonitorLogEntry;
import net.jadefisher.monkeystatus.respository.MonitorHistoryRepository;
import net.jadefisher.monkeystatus.respository.MonitorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/monitors")
public class MonitorController {
	@Autowired
	private MonitorRepository monitorRepository;

	@Autowired
	private MonitorHistoryRepository monitorLogRepository;

	@RequestMapping(method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody List<Monitor> list() {
		return monitorRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.GET, params = { "serviceId" }, produces = { "application/json" })
	public @ResponseBody List<Monitor> listByService(
			@RequestParam("serviceId") String serviceId) {
		return monitorRepository.findByService(serviceId);
	}

	@RequestMapping(value = "/{monitorId}", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody Monitor getMonitor(
			@PathVariable("monitorId") String monitorId) {
		return monitorRepository.find(monitorId);
	}

	@RequestMapping(value = "/{monitorId}/mostRecent", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody MonitorLogEntry getMostRecentMonitorLog(
			@PathVariable("monitorId") String monitorId) {
		return monitorLogRepository.findMostRecentByMonitor(monitorId);
	}

	@RequestMapping(value = "/{monitorId}/history", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody List<MonitorLogEntry> getMonitorLogs(
			@PathVariable("monitorId") String monitorId) {
		return monitorLogRepository.findByMonitor(monitorId);
	}
}
