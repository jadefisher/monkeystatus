package net.jadefisher.appmonitor.controller;

import java.util.List;

import net.jadefisher.appmonitor.model.Service;
import net.jadefisher.appmonitor.respository.ServiceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/services")
public class ServiceController {

	@Autowired
	private ServiceRepository serviceRepository;

	public ServiceController() {
		System.out.println("starting up");
	}

	@RequestMapping(method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody List<Service> list() {
		return serviceRepository.getServices();
	}
}
