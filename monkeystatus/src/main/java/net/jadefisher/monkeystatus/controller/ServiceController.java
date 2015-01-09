package net.jadefisher.monkeystatus.controller;

import java.util.List;

import net.jadefisher.monkeystatus.model.service.Service;
import net.jadefisher.monkeystatus.model.service.ServiceEvent;
import net.jadefisher.monkeystatus.respository.EventHistoryRepository;
import net.jadefisher.monkeystatus.respository.ServiceRepository;

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
@RequestMapping("/services")
public class ServiceController {
	@Autowired
	private ServiceRepository serviceRepository;

	@Autowired
	private EventHistoryRepository eventHistoryRepository;

	public ServiceController() {
		System.out.println("starting up");
	}

	@RequestMapping(method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody List<Service> list() {
		return serviceRepository.findAll();
	}

	@RequestMapping(value = "/{serviceKey}", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody Service getService(
			@PathVariable("serviceKey") String serviceKey) {
		return serviceRepository.find(serviceKey);
	}

	@RequestMapping(value = "/{serviceKey}/history", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody Page<ServiceEvent> listEvents(
			@PathVariable("serviceKey") String serviceKey,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		return eventHistoryRepository.findByServiceKey(serviceKey,
				new PageRequest(page == null ? 0 : page, pageSize == null ? 50
						: pageSize, new Sort(Direction.DESC, "startDate")));
	}
}
