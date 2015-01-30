package org.ird.unfepi.web.validator;

import java.util.Date;

import org.ird.unfepi.beans.DailySummaryWrapper;
import org.ird.unfepi.constants.ErrorMessages;
import org.ird.unfepi.model.DailySummary;
import org.ird.unfepi.model.DailySummaryVaccineGiven;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DailySummaryValidator implements Validator{
	
	public boolean supports(Class cls) {
		return DailySummaryWrapper.class.equals(cls);
	}
	
	public void validate(Object command, Errors error) 
	{
		DailySummaryWrapper dsw = (DailySummaryWrapper) command;
		
		DailySummary ds = dsw.getDailySummary();

		if(ds.getSummaryDate() == null 
				|| ds.getSummaryDate().after(new Date())){
			error.rejectValue("summaryDate" , "" , ErrorMessages.DAILY_SUMMARY_DATE_MISSING);
		}
		
		int i = 0;
		for (DailySummaryVaccineGiven el : dsw.getDsvgList()) {
			if(el.getQuantityGiven() == null
					|| el.getQuantityGiven() < 0 
					|| el.getQuantityGiven() > 999){
				error.rejectValue("dsvgList["+i+"].quantityGiven" , "" , el.getVaccineName()+ " value should be valid, numeric and between 0-999");
			}
			
			i++;
		}
	}
}
