package net.jadefisher.monkeystatus.controller;

import java.util.List;

import net.jadefisher.monkeystatus.model.service.Service;
import net.jadefisher.monkeystatus.model.service.ServiceEvent;
import net.jadefisher.monkeystatus.respository.EventHistoryRepository;
import net.jadefisher.monkeystatus.respository.ServiceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@RequestMapping(value = "/{serviceId}", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody Service getService(
			@PathVariable("serviceId") String serviceId) {
		return serviceRepository.find(serviceId);
	}

	@RequestMapping(value = "/{serviceId}/history", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody List<ServiceEvent> listEvents(
			@PathVariable("serviceId") String serviceId) {
		return eventHistoryRepository.findByService(serviceId);
	}
}
