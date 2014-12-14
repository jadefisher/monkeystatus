package net.jadefisher.appmonitor.model.monitor;


public class PingMonitor extends Monitor {
	private String targetHost;

	private int pingTimeout = 5000;

	public String getTargetHost() {
		return targetHost;
	}

	public void setTargetHost(String targetHost) {
		this.targetHost = targetHost;
	}

	public int getPingTimeout() {
		return pingTimeout;
	}

	public void setPingTimeout(int pingTimeout) {
		this.pingTimeout = pingTimeout;
	}
}
