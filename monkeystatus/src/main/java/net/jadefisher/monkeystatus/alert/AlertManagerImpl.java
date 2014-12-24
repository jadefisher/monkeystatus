package net.jadefisher.monkeystatus.alert;

import net.jadefisher.monkeystatus.model.alert.Alert;
import net.jadefisher.monkeystatus.model.service.ServiceEvent;
import net.jadefisher.monkeystatus.model.service.ServiceEventChange;
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
	public void statusChange(ServiceEventChange change) {
		String subject;
		String message;
		ServiceEvent currentEvent = change.getCurrentEvent();
		ServiceEvent previousEvent = change.getPreviousEvent();
		if (currentEvent == null) {
			subject = change.getService().getName() + " is clear!";
			message = "";
		} else {
			subject = change.getService().getName() + " is experiencing an "
					+ currentEvent.getType();
			message = "Description: " + currentEvent.getDescription();
		}
		log.warn("sending alert for service " + change.getService().getName());

		try {
			ServiceEventType alertType = currentEvent != null ? currentEvent
					.getType() : (previousEvent != null ? previousEvent
					.getType() : null);

			alertSender.sendAlert(
					new Alert(subject, message),
					subscriberRepo.find(change.getService().getKey(),
							change.allTags(), alertType));
		} catch (Exception e) {
			log.error(e);
		}
	}

}
