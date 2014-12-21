package net.jadefisher.monkeystatus.alert;

import java.util.List;

import net.jadefisher.monkeystatus.exception.AlertSendingException;
import net.jadefisher.monkeystatus.model.alert.Alert;

public interface AlertSender {
	void sendAlert(Alert alert, List<String> recipients)
			throws AlertSendingException;
}
