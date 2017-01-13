package org.ird.unfepi.web.validator;

import java.util.List;

import org.ird.unfepi.beans.VaccineRegistrationWrapper;
import org.ird.unfepi.constants.ErrorMessages;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.VaccineGap;
import org.ird.unfepi.model.VaccinePrerequisite;
import org.ird.unfepi.utils.validation.DataValidation;
import org.ird.unfepi.utils.validation.REG_EX;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mysql.jdbc.StringUtils;

public class CalendarVaccineValidator implements Validator{

	@Override
	public boolean supports(Class clazz) {
		return VaccineRegistrationWrapper.class.equals(clazz);
	}

	@Override
	public void validate(Object command, Errors error) {
		VaccineRegistrationWrapper vrw = (VaccineRegistrationWrapper)command;
		ServiceContext sc = Context.getServices();		
		
//		System.out.println("validator called -------");
		
		if(StringUtils.isEmptyOrWhitespaceOnly(vrw.getVaccineId().toString())){
			error.rejectValue("vaccineId", "error.vaccineId.empty-vaccineId", "vaccine must be provided");
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(vrw.getVaccinationCalendarId().toString())){
			error.rejectValue("vaccinationCalendarId", "error.vaccinationCalendarId.empty-vaccinationCalendarId", "vaccinationCalendar must be provided");
		}
				
		if((vrw.getVaccineGapList() == null || vrw.getVaccineGapList().size() == 0) &&
		   (vrw.getVaccinePrerequisites() == null || vrw.getVaccinePrerequisites().length == 0)){
			error.reject("", null, "atleast one gap or Prerequisite should be entered");
		}

		if(vrw.getVaccineGapList() != null/* && vrw.getVaccineGapList().size() > 0*/){
			for (VaccineGap vg : vrw.getVaccineGapList()) {
				//check in db unique or not / already present
				
				List<VaccineGap> records = sc.getCustomQueryService().getDataByHQL("from VaccineGap where vaccineId = " + vg.getId().getVaccineId() 
						+ " and vaccineGapTypeId = " + vg.getId().getVaccineGapTypeId() + " and vaccinationcalendarId = " 
						+ vg.getId().getVaccinationcalendarId());
				
				if(records.size() > 0){
					int id = records.get(0).getId().getVaccineGapTypeId()-1;
					error.rejectValue(/*"vaccineGapList["+id+"].value"*/"", null, records.get(0).getVaccineGapType().getName() +" is already exist !");
//					error.rejectValue("vaccineGapList["+id+"].value", null, records.get(0).getVaccineGapType().getName() +" is already exist !");
					
				}
			}
		}

		if(vrw.getVaccinePrerequisites() != null /*&& vrw.getVaccinePrerequisiteList().size() > 0*/){
			for (String vp : vrw.getVaccinePrerequisites()) {
				//check in db unique or not / already present
				
				if (Integer.parseInt(vp) == vrw.getVaccineId()) {
					error.rejectValue("vaccinePrerequisites", null, "vaccine and prerequisite vaccine are same");
				}
				
				List<VaccinePrerequisite> records = sc.getCustomQueryService().getDataByHQL("from VaccinePrerequisite where vaccinePrerequisiteId.vaccineId = " + vrw.getVaccineId()
						+ " and vaccinePrerequisiteId.vaccinePrerequisiteId = " + vp + " and vaccinePrerequisiteId.vaccinationcalendarId = " + vrw.getVaccinationCalendarId());
				
				if(records.size() > 0){
					error.rejectValue("vaccinePrerequisites", null, records.get(0).getPrerequisite().getName() +" is already exist as prerequisite ");
				}
				
			}
		}
	}
	
	public void validate(Object command, Errors error, boolean isNew) {
		VaccineRegistrationWrapper vrw = (VaccineRegistrationWrapper)command;
		ServiceContext sc = Context.getServices();		
		
//		System.out.println("validator called isNew -------");
		
		if(StringUtils.isEmptyOrWhitespaceOnly(vrw.getVaccineId().toString())){
			error.rejectValue("vaccineId", "error.vaccineId.empty-vaccineId", "vaccine must be provided");
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(vrw.getVaccinationCalendarId().toString())){
			error.rejectValue("vaccinationCalendarId", "error.vaccinationCalendarId.empty-vaccinationCalendarId", "vaccinationCalendar must be provided");
		}
				
//		if((vrw.getVaccineGapList() == null || vrw.getVaccineGapList().size() == 0) &&
//		   (vrw.getVaccinePrerequisites() == null || vrw.getVaccinePrerequisites().length == 0)){
//			error.reject("", null, "atleast one gap or Prerequisite should be entered");
//		}

		if(isNew){
			if(vrw.getVaccineGapList() != null){
				for (VaccineGap vg : vrw.getVaccineGapList()) {//check in db unique or not / already present
					
					List<VaccineGap> records = sc.getCustomQueryService().getDataByHQL("from VaccineGap where vaccineId = " + vg.getId().getVaccineId() 
							+ " and vaccineGapTypeId = " + vg.getId().getVaccineGapTypeId() + " and vaccinationcalendarId = " 
							+ vg.getId().getVaccinationcalendarId());
					
					if(records.size() > 0){
						error.rejectValue("", null, records.get(0).getVaccineGapType().getName() +" is already exist !");
					}
				}
			}
		}
		

		if(vrw.getVaccinePrerequisites() != null){
			for (String vp : vrw.getVaccinePrerequisites()) {//check in db unique or not / already present
				
				if (Integer.parseInt(vp) == vrw.getVaccineId()) {
					error.rejectValue("vaccinePrerequisites", null, "vaccine and prerequisite vaccine are same");
				}
				
				if(isNew){
					List<VaccinePrerequisite> records = sc.getCustomQueryService().getDataByHQL("from VaccinePrerequisite where vaccinePrerequisiteId.vaccineId = " + vrw.getVaccineId()
							+ " and vaccinePrerequisiteId.vaccinePrerequisiteId = " + vp + " and vaccinePrerequisiteId.vaccinationcalendarId = " + vrw.getVaccinationCalendarId());

					if(records.size() > 0){
						error.rejectValue("vaccinePrerequisites", null, records.get(0).getPrerequisite().getName() +" is already exist as prerequisite ");
					}
				}
			}
		}
	}

}
