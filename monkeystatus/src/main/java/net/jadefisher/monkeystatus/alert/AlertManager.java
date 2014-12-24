package net.jadefisher.monkeystatus.alert;

import net.jadefisher.monkeystatus.model.service.ServiceEventChange;

public interface AlertManager {
	void statusChange(ServiceEventChange change);
}
