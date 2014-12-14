package net.jadefisher.monkeystatus.model.monitor;

public class FormAuthentication {

	private HttpRequestDefinition logoffRequest;

	private HttpRequestDefinition logonRequest;

	public HttpRequestDefinition getLogoffRequest() {
		return logoffRequest;
	}

	public void setLogoffRequest(HttpRequestDefinition logoffRequest) {
		this.logoffRequest = logoffRequest;
	}

	public HttpRequestDefinition getLogonRequest() {
		return logonRequest;
	}

	public void setLogonRequest(HttpRequestDefinition logonRequest) {
		this.logonRequest = logonRequest;
	}
}
