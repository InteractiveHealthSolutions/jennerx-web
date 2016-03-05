package org.ird.unfepi.beans;

import java.util.ArrayList;
import java.util.List;

import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.ReminderSms.REMINDER_STATUS;
import org.ird.unfepi.model.Response;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.Vaccinator;

public class APVRRAnalysisGridRow {

	private Child child;
	List<VaccinationRecord> record=new ArrayList<VaccinationRecord>();
	
	public Child getChild() {
		return child;
	}
	public void setChild(Child p) {
		child=p;
	}
	public  List<VaccinationRecord> getRecord() {
		return record;
	}
/*	public  void setRecord(List<VaccinationRecord> rec) {
		record=rec;
	}*/
	public  void addRecord(Vaccination pvacc,Vaccinator vaccinator, VaccinationCenter center,List<ReminderSms> rh,List<Response> pr) {
		record.add(new VaccinationRecord( pvacc,vaccinator, center, rh, pr));
	}
	public class VaccinationRecord{
		private List<ReminderSms> reminderSms;
		private List<Response> response;
		private Vaccination vaccination;
		private Vaccinator vaccinator;
		private VaccinationCenter center;
		private int remindersPending=0;
		private int unsentReminders = 0;
		private int cancelledReminder = 0;
		private int sentReminder = 0;
		private Boolean anyReminderLate = false;
		private Integer maxDaysLate = null;
		public VaccinationRecord(Vaccination pv,Vaccinator vaccinator, VaccinationCenter center,List<ReminderSms> rh,List<Response> pr){
			reminderSms=rh;
			response=pr;
			vaccination=pv;
			this.vaccinator = vaccinator;
			this.center = center;
			for (ReminderSms rem : rh) {
				if(rem.getReminderStatus().equals(REMINDER_STATUS.FAILED) 
						|| rem.getReminderStatus().equals(REMINDER_STATUS.MISSED)){
					unsentReminders++;
				}
				if(rem.getReminderStatus().equals(REMINDER_STATUS.CANCELLED) ){
					cancelledReminder++;
				}
				if(rem.getReminderStatus().equals(REMINDER_STATUS.SENT) ){
					sentReminder++;
				}
				if(rem.getReminderStatus().equals(REMINDER_STATUS.SCHEDULED) ){
					remindersPending++;
				}
				if(rem.getHoursDifference() != null 
						&& rem.getHoursDifference() > 12){
					anyReminderLate = true;
					
					if(maxDaysLate == null){
						maxDaysLate = rem.getHoursDifference()/12;
					}else{
						maxDaysLate = (rem.getHoursDifference() != null
								&& rem.getHoursDifference()/12 > maxDaysLate) 
										? rem.getHoursDifference()/12 
										: maxDaysLate;
					}
				}
			}
		}
			public List<ReminderSms> getReminderSms() {
			return reminderSms;
			}
			public List<Response> getResponse() {
				return response;
			}
/*			public void setReminderSms(List<ReminderSms> r) {
				reminderSms=r;
			}
			public void setChildResponse(List<ChildResponse> patResp) {
				response=patResp;
			}
			public void setVaccination(Vaccination pvacc) {
				this.pvacc = pvacc;
			}*/
			public Vaccination getVaccination() {
				return vaccination;
			}
			public void setRemindersPending(int remindersPending) {
				this.remindersPending = remindersPending;
			}
			public int getRemindersPending() {
				return remindersPending;
			}
			public void setAnyReminderLate(Boolean anyReminderLate) {
				this.anyReminderLate = anyReminderLate;
			}
			public Boolean getAnyReminderLate() {
				return anyReminderLate;
			}
			public void setMaxDaysLate(Integer maxDaysLate) {
				this.maxDaysLate = maxDaysLate;
			}
			public Integer getMaxDaysLate() {
				return maxDaysLate;
			}
			public void setUnsentReminders(int unsentReminders) {
				this.unsentReminders = unsentReminders;
			}
			public int getUnsentReminders() {
				return unsentReminders;
			}
			public void setCancelledReminder(int cancelledReminder) {
				this.cancelledReminder = cancelledReminder;
			}
			public int getCancelledReminder() {
				return cancelledReminder;
			}
			public void setSentReminder(int sentReminder) {
				this.sentReminder = sentReminder;
			}
			public int getSentReminder() {
				return sentReminder;
			}
			public Vaccinator getVaccinator() {
				return vaccinator;
			}
			public void setVaccinator(Vaccinator vaccinator) {
				this.vaccinator = vaccinator;
			}
			public VaccinationCenter getCenter() {
				return center;
			}
			public void setCenter(VaccinationCenter center) {
				this.center = center;
			}
	}
}