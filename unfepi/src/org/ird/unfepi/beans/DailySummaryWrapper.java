package org.ird.unfepi.beans;

import java.util.List;

import org.ird.unfepi.model.DailySummary;
import org.ird.unfepi.model.DailySummaryVaccineGiven;

public class DailySummaryWrapper {

	private DailySummary dailySummary;
	private List<DailySummaryVaccineGiven> dsvgList;
	
	public DailySummaryWrapper(DailySummary dailySummary, List<DailySummaryVaccineGiven> dsvgList) {
		this.dailySummary = dailySummary;
		this.dsvgList = dsvgList;
	}

	public DailySummary getDailySummary() {
		return dailySummary;
	}

	public void setDailySummary(DailySummary dailySummary) {
		this.dailySummary = dailySummary;
	}

	public List<DailySummaryVaccineGiven> getDsvgList() {
		return dsvgList;
	}

	public void setDsvgList(List<DailySummaryVaccineGiven> dsvgList) {
		this.dsvgList = dsvgList;
	}
	
}
