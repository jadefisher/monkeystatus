package net.jadefisher.monkeystatus.controller;

import java.util.List;

import net.jadefisher.monkeystatus.model.monitor.Monitor;
import net.jadefisher.monkeystatus.model.monitor.MonitorRecording;
import net.jadefisher.monkeystatus.respository.MonitorHistoryRepository;
import net.jadefisher.monkeystatus.respository.MonitorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

	@RequestMapping(method = RequestMethod.GET, params = { "serviceKey" }, produces = { "application/json" })
	public @ResponseBody List<Monitor> listByService(
			@RequestParam("serviceKey") String serviceKey) {
		return monitorRepository.findByService(serviceKey);
	}

	@RequestMapping(value = "/{monitorKey}", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody Monitor getMonitor(
			@PathVariable("monitorKey") String monitorKey) {
		return monitorRepository.find(monitorKey);
	}

	@RequestMapping(value = "/{monitorKey}/mostRecent", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody MonitorRecording getMostRecentMonitorLog(
			@PathVariable("monitorKey") String monitorKey) {
		return monitorLogRepository.findOneByMonitorKey(monitorKey, new Sort(
				Direction.DESC, "timestamp"));
	}

	@RequestMapping(value = "/{monitorKey}/history", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody Page<MonitorRecording> getMonitorLogs(
			@PathVariable("monitorKey") String monitorKey,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		return monitorLogRepository.findByMonitorKey(monitorKey,
				new PageRequest(page == null ? 0 : page, pageSize == null ? 50
						: pageSize, new Sort(Direction.DESC, "timestamp")));
	}
}
