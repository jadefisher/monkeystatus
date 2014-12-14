package net.jadefisher.monkeystatus.model;

import java.util.ArrayList;
import java.util.List;

public enum ServiceEventType {
	PLANNED_OUTAGE(0), UNPLANNED_FULL_OUTAGE(1), UNPLANNED_PARTIAL_OUTAGE(2), INTERMITTENT_OUTAGE(
			3), INFORMATIONAL(4);

	private int level;

	private ServiceEventType(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public List<ServiceEventType> higherLevels() {
		List<ServiceEventType> higherTypes = new ArrayList<ServiceEventType>();
		for (ServiceEventType type : ServiceEventType.values()) {
			if (type.level >= this.level) {
				higherTypes.add(type);
			}
		}
		return higherTypes;
	}
}
