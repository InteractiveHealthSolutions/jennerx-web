package org.ird.unfepi.web.validator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Encounter;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.EncounterResults;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.web.utils.VaccinationCenterVisit;
import org.ird.unfepi.web.utils.VaccineSchedule;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mysql.jdbc.StringUtils;

public class VaccinationValidator implements Validator {

    public boolean supports(Class clazz) {
        return VaccinationCenterVisit.class.equals(clazz);
    }

    public void validate(Object obj, Errors errors) {
    	
    }
    
    public void validateVaccinationForm(VaccinationCenterVisit centerVisit, List<VaccineSchedule> vaccineSchedule, Errors errors, HttpServletRequest request){
    	ServiceContext sc = Context.getServices();
		try{
			ValidatorUtils.validateFollowupForm(DataEntrySource.WEB, vaccineSchedule, centerVisit, null, errors, sc);
		}
		finally{
			sc.closeSession();
		}
    }
    
    public void validateVaccinationPrivilegedForm(Object obj, Errors errors, HttpServletRequest request){
    	Vaccination vaccination = (Vaccination) obj;
    	Vaccine nextVaccine = null;
		ServiceContext sc = Context.getServices();
		try{
			String nextVaccineStr = request.getParameter("nextVaccine");
			if(!StringUtils.isEmptyOrWhitespaceOnly(nextVaccineStr)){
				nextVaccine = sc.getVaccinationService().getByName(nextVaccineStr);
			}
			
			String remPrfStr = request.getParameter("reminderPreference");
			String remCellNumStr = request.getParameter("reminderCellNumber");
			Boolean hasApprovedReminders = StringUtils.isEmptyOrWhitespaceOnly(remPrfStr)?null:Boolean.valueOf(remPrfStr);
	    	
			ValidatorUtils.validateFollowupPrivilegedForm(DataEntrySource.WEB, vaccination, nextVaccine, 
	    			hasApprovedReminders, remCellNumStr, null, errors, sc);
		}
		finally{
			sc.closeSession();
		}
    }
    
    public void validateVaccinationFillHalfForm(Object obj, Errors errors, HttpServletRequest request){
    	Vaccination vaccination = (Vaccination) obj;
    	Vaccine nextVaccine = null;
		
		Encounter encounter = (Encounter) request.getSession().getAttribute("encounter"+vaccination.getChildId());
		List<EncounterResults> encreslist = (List<EncounterResults>) request.getSession().getAttribute("encounterres"+vaccination.getChildId());
		
		ServiceContext sc = Context.getServices();
		try{
			String nextVaccineStr = request.getParameter("nextVaccine");
			if(!StringUtils.isEmptyOrWhitespaceOnly(nextVaccineStr)){
				nextVaccine = sc.getVaccinationService().getByName(nextVaccineStr);
			}
			
			String remPrfStr = request.getParameter("reminderPreference");
			String remCellNumStr = request.getParameter("reminderCellNumber");
			Boolean hasApprovedReminders = StringUtils.isEmptyOrWhitespaceOnly(remPrfStr)?null:Boolean.valueOf(remPrfStr);
	    	
			ValidatorUtils.validateFollowupFillLotteryGenForm(DataEntrySource.WEB, vaccination, nextVaccine, hasApprovedReminders, remCellNumStr, encounter, encreslist, null, errors, sc);
		}
		finally{
			sc.closeSession();
		}
    }
}
