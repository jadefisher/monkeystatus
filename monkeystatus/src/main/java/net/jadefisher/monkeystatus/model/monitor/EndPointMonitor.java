package net.jadefisher.monkeystatus.model.monitor;

import java.util.List;

public class EndPointMonitor extends Monitor {
	private FormAuthentication formAuthentication;

	private BasicAuthentication basicAuthentication;

	private List<HttpRequestDefinition> requests;

	public FormAuthentication getFormAuthentication() {
		return formAuthentication;
	}

	public void setFormAuthentication(FormAuthentication formAuthentication) {
		this.formAuthentication = formAuthentication;
	}

	public BasicAuthentication getBasicAuthentication() {
		return basicAuthentication;
	}

	public void setBasicAuthentication(BasicAuthentication basicAuthentication) {
		this.basicAuthentication = basicAuthentication;
	}

	public List<HttpRequestDefinition> getRequests() {
		return requests;
	}

	public void setRequests(List<HttpRequestDefinition> requests) {
		this.requests = requests;
	}
}
