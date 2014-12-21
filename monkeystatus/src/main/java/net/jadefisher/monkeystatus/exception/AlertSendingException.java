package net.jadefisher.monkeystatus.exception;

public class AlertSendingException extends Exception {
	private static final long serialVersionUID = 1L;

	public AlertSendingException(String msg) {
		super(msg);
	}

	public AlertSendingException(String msg, Exception cause) {
		super(msg, cause);
	}
}
