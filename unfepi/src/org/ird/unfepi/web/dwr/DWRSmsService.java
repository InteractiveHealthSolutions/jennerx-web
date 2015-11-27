package org.ird.unfepi.web.dwr;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.directwebremoting.WebContextFactory;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.WebGlobals.DGUserSmsFieldNames;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Model.SmsStatus;
import org.ird.unfepi.model.UserSms.SmsType;
import org.ird.unfepi.model.UserSms;
import org.ird.unfepi.utils.UserSessionUtils;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.OutboundMessage.PeriodType;
import org.irdresearch.smstarseel.data.OutboundMessage.Priority;

public class DWRSmsService {

	public String queueSms(List<Map<String,String>> recipientMap, String smsText, String smsDescription, int validityPeriod){
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){
			try {
				WebContextFactory.get().forwardToString("login.htm");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		ServiceContext sc = Context.getServices();
		TarseelServices tsc = TarseelContext.getServices();
		try{
			for (Map<String, String> map : recipientMap) 
			{
				String recipient = map.get(DGUserSmsFieldNames.number.name());
				Date duedate = new Date();
				Priority priority = Priority.MEDIUM;
				Integer recipientId = map.get(DGUserSmsFieldNames.mappedId.name())==null?null:Integer.parseInt(map.get(DGUserSmsFieldNames.mappedId.name()));
				String referenceNum = tsc.getSmsService().createNewOutboundSms(recipient, smsText, duedate, priority, validityPeriod, PeriodType.HOUR, GlobalParams.SMS_TARSEEL_PROJECT_ID, null);
				

				UserSms sms = new UserSms();
				sms.setCreator(user.getUser());
				sms.setDescription(smsDescription);
				sms.setDueDate(duedate);
				sms.setRecipient(recipient);
				sms.setRecipientId(recipientId);
				sms.setReferenceNumber(referenceNum);
				sms.setSmsStatus(SmsStatus.PENDING);
				sms.setSmsType(SmsType.N_A);
				sms.setText(smsText);
				
				sc.getUserSmsService().saveUserSms(sms);
				
			}
			tsc.commitTransaction();
			sc.commitTransaction();

			return "Smses successfully logged";
		}
		catch (Exception e) {
			e.printStackTrace();
			return "Error: "+e.getMessage();
		}
		finally{
			tsc.closeSession();
			sc.closeSession();
		}
	}
}
