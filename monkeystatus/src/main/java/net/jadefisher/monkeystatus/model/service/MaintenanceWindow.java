package net.jadefisher.monkeystatus.model.service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public class MaintenanceWindow {
	private Set<DayOfWeek> daysOfWeek;

	private LocalTime start;

	private LocalTime end;

	public Set<DayOfWeek> getDaysOfWeek() {
		return daysOfWeek;
	}

	public void setDaysOfWeek(Set<DayOfWeek> daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}

	public LocalTime getStart() {
		return start;
	}

	public void setStart(LocalTime start) {
		this.start = start;
	}

	public LocalTime getEnd() {
		return end;
	}

	public void setEnd(LocalTime end) {
		this.end = end;
	}
}
