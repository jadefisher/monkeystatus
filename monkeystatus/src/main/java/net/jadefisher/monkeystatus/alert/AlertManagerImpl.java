package net.jadefisher.monkeystatus.alert;

import java.util.Set;

import net.jadefisher.monkeystatus.model.alert.Alert;
import net.jadefisher.monkeystatus.model.service.Service;
import net.jadefisher.monkeystatus.model.service.ServiceEvent;
import net.jadefisher.monkeystatus.model.service.ServiceEventType;
import net.jadefisher.monkeystatus.respository.SubscriberRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlertManagerImpl implements AlertManager {
	private static final Log log = LogFactory.getLog(AlertManagerImpl.class);

	@Autowired
	private AlertSender alertSender;

	@Autowired
	private SubscriberRepository subscriberRepo;

	@Override
	public void statusChange(Service service, ServiceEventType eventType,
			Set<String> tags) {
		String subject;
		String message;
		ServiceEvent currentEvent = service.getCurrentEvent();
		if (currentEvent == null) {
			subject = service.getName() + " is clear!";
			message = "";
		} else {
			subject = service.getName() + " is experiencing an " + eventType;
			message = "Description: " + currentEvent.getDescription();
		}
		log.warn("sending alert for service " + service.getName());

		try {
			alertSender.sendAlert(new Alert(subject, message),
					subscriberRepo.find(service.getId(), tags, eventType));
		} catch (Exception e) {
			log.error(e);
		}
	}

}
