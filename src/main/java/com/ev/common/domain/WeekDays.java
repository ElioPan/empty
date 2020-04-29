package com.ev.common.domain;

public enum WeekDays {
	MON(1, "monday"), TUE(2, "tuesday"), WED(3, "wednesday"), THU(4, "thursday"), FRI(5, "friday"), SAT(6, "saturday"),
	SUM(7, "sunday");

	private final int id;
	private final String name;

	WeekDays(int id, String name) {
		this.name = name;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
