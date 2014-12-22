package net.jadefisher.monkeystatus.model.service;

import java.time.DayOfWeek;
import java.util.Set;

public class MaintenanceWindow {
	private Set<DayOfWeek> daysOfWeek;

	private TimeOfDay start;

	private TimeOfDay end;

	public Set<DayOfWeek> getDaysOfWeek() {
		return daysOfWeek;
	}

	public void setDaysOfWeek(Set<DayOfWeek> daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}

	public TimeOfDay getStart() {
		return start;
	}

	public void setStart(TimeOfDay start) {
		this.start = start;
	}

	public TimeOfDay getEnd() {
		return end;
	}

	public void setEnd(TimeOfDay end) {
		this.end = end;
	}
}
