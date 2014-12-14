package net.jadefisher.appmonitor.exception;

public class AssertionFailedException extends Exception {
	private static final long serialVersionUID = 1L;

	public AssertionFailedException(String assertion) {
		super(assertion);
	}
}
