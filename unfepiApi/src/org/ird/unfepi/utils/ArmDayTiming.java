package org.ird.unfepi.utils;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class ArmDayTiming {
	private Map<Integer, Time> timer = new HashMap<Integer, Time>();

	public void addDayTiming(int dayNumer, Time time) {
		timer.put(dayNumer, time);
	}

	public Time getTiming(int dayNumer) {
		return timer.get(dayNumer);
	}
}
