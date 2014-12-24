package net.jadefisher.monkeystatus.model.service;

public enum ServiceEventType {
	PLANNED_FULL_OUTAGE(0, true), UNPLANNED_FULL_OUTAGE(1, false), PLANNED_PARTIAL_OUTAGE(
			2, true), UNPLANNED_PARTIAL_OUTAGE(3, false), PLANNED_INTERMITTENT_OUTAGE(
			4, true), UNPLANNED_INTERMITTENT_OUTAGE(5, false), PLANNED_INFORMATIONAL(
			6, true), UNPLANNED_INFORMATIONAL(7, false);

	private int level;

	private boolean planned;

	private ServiceEventType(int level, boolean planned) {
		this.level = level;
		this.planned = planned;
	}

	public boolean moreSevere(ServiceEventType other) {
		return this.level < other.level;
	}

	public boolean isPlanned() {
		return planned;
	}
}
