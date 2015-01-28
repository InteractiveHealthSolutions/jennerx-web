package org.ird.unfepi.autosys.smser;

import java.util.List;
import java.util.TimerTask;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Model.SmsStatus;
import org.ird.unfepi.model.UserSms;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.OutboundMessage;
import org.irdresearch.smstarseel.data.OutboundMessage.OutboundStatus;

public class UserSmsUpdaterJob extends TimerTask
{
	@Override
	public void run() 
	{
		ServiceContext sc = Context.getServices();
		
		try{
			List<UserSms> smslist = sc.getUserSmsService().findUserSmsByCriteria(null, null, null, null, SmsStatus.PENDING, false, null, null, 0, Integer.MAX_VALUE, false, null);
			
			for (UserSms sms : smslist) 
			{
				TarseelServices tsc = TarseelContext.getServices();
				try{
					OutboundMessage om = tsc.getSmsService().findOutboundMessageByReferenceNumber(sms.getReferenceNumber(), true);
					
					if(!om.getStatus().equals(OutboundStatus.PENDING)
							&& !om.getStatus().equals(OutboundStatus.UNKNOWN))
					{
						if(om.getStatus().equals(OutboundStatus.FAILED)){
							sms.setSmsStatus(SmsStatus.FAILED);
							sms.setOriginator(om.getOriginator());
							sms.setSmsCancelReason(om.getFailureCause());
						}
						else{
							if(om.getStatus().equals(OutboundStatus.SENT)){
								sms.setSmsStatus(SmsStatus.SENT);
							}
							else {
								sms.setSmsStatus(SmsStatus.MISSED);
							}
						
							sms.setOriginator(om.getOriginator());
							sms.setSentDate(om.getSentdate());
							sms.setSmsCancelReason(om.getFailureCause());
						}
						
						sc.getUserSmsService().updateUserSms(sms);
					}//end if
					
				}
				catch(Exception e){
					e.printStackTrace();
				}
				finally{
					tsc.closeSession();
				}
			}//end for
			
			sc.commitTransaction();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}

}
