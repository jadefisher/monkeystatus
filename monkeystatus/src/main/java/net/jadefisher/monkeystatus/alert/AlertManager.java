package net.jadefisher.monkeystatus.alert;

import java.util.Set;

import net.jadefisher.monkeystatus.model.service.Service;
import net.jadefisher.monkeystatus.model.service.ServiceEventType;

public interface AlertManager {
	void statusChange(Service service, ServiceEventType eventType,
			Set<String> tags);
}
