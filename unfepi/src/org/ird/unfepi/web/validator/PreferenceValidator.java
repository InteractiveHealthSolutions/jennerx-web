package org.ird.unfepi.web.validator;

import org.ird.unfepi.constants.ErrorMessages;
import org.ird.unfepi.model.LotterySms;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PreferenceValidator  implements Validator{
	
	public boolean supports(Class cls) {
		return LotterySms.class.equals(cls);
	}
	
	public void validate(Object command, Errors error) 
	{
		LotterySms ls = (LotterySms) command;
		//Status / preferences validations
		/*if(ls.getHasApprovedLottery() == null){
			error.rejectValue("hasApprovedLottery", "", ErrorMessages.APPROVAL_LOTTERY_MISSING);
		}*/
		
		if(ls.getHasApprovedReminders() == null){
			error.rejectValue("hasApprovedReminders", "", ErrorMessages.APPROVAL_REMINDERS_MISSING);
		}
	}
}
