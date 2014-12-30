package org.ird.unfepi.autosys.smser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.EmailEngine;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Response;
import org.ird.unfepi.model.Response.ResponseType;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.CallLog;
import org.irdresearch.smstarseel.data.CallLog.CallStatus;
import org.irdresearch.smstarseel.data.CallLog.CallType;
import org.irdresearch.smstarseel.data.DataException;
import org.irdresearch.smstarseel.data.InboundMessage;
import org.irdresearch.smstarseel.data.InboundMessage.InboundStatus;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

public class ResponseReaderJob implements Job{

	private static int MAX_CELL_NUMBER_MATCH_LENGTH = Integer.parseInt(
			Context.getSetting("cellnumber.number-length-without-zero","10")
			);
	private static int MAX_RESPONSE_LENGTH = Integer.parseInt(
			Context.getSetting("response.max-response-length","500")
			);
	
	@Override
	public void execute(JobExecutionContext jxc) throws JobExecutionException 
	{
		TarseelServices tsc = TarseelContext.getServices();

		GlobalParams.RESPONSEJOBLOGGER.info("Running Job: "+jxc.getJobDetail().getFullName());

		try{
			List<InboundMessage> list = new ArrayList<InboundMessage>();
			try {
				list = tsc.getSmsService().findInbound(null, null, InboundStatus.UNREAD, null, null, null, tsc.getDeviceService().findProjectById(GlobalParams.SMS_TARSEEL_PROJECT_ID).getProjectId(), false);
			} catch (DataException e1) {
				e1.printStackTrace();
				GlobalParams.RESPONSEJOBLOGGER.error(jxc.getJobDetail().getFullName() ,e1);
			}
			
			GlobalParams.RESPONSEJOBLOGGER.info("Fetched "+list.size()+" RECEIVED sms");

			for (InboundMessage ib : list) {
				ServiceContext sc = Context.getServices();
	
				try{
					String sender = ib.getOriginator();
					if(sender.length() > MAX_CELL_NUMBER_MATCH_LENGTH){
						sender = sender.substring(sender.length() - MAX_CELL_NUMBER_MATCH_LENGTH);
					}
					
					List<ContactNumber> conl = null;
					conl = sc.getDemographicDetailsService().findContactNumber(sender, true, null);
					
					ContactNumber con = null;
					
					if(conl.size() > 0){
						con = conl.get(0);
					}
					
					if(conl.size() > 1){
						String msg = "Multiple ("+conl.size()+") contact numbers found on cell number "+ib.getOriginator()
								+ ". Response will be logged against first entity ("+con.getMappedId()+":"+con.getIdMapper().getIdentifiers().get(0).getIdentifier()+")"
								+ ". All mapped entities on cell number are \n\n";
						for (ContactNumber contactNumber : conl) {
							msg += "("+contactNumber.getMappedId()+":"+contactNumber.getIdMapper().getIdentifiers().get(0).getIdentifier()+")\n";
						}
						///////shouldReadInbound = false;
						try{
							EmailEngine.getInstance().emailErrorReportToAdminAsASeparateThread("Multiple entities found on a cell number", msg);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						GlobalParams.RESPONSEJOBLOGGER.error(jxc.getJobDetail().getFullName() +" : "+conl.size()+" contacts found on same number "+ib.getOriginator());
					}
					
					Response response = new Response();
					
					if(con != null){
						String roleName = con.getIdMapper().getRole().getRolename().toLowerCase();
						
						if(roleName.startsWith("child")){
							Calendar cal = Calendar.getInstance();
							cal.setTime( new Date(System.currentTimeMillis()-1000*60*60*24*365L) );
							
							Calendar cal2 = Calendar.getInstance();
							cal2.setTime( new Date(System.currentTimeMillis()+1000*60*60*24*15L) );
						
							List<Vaccination> pv = new ArrayList<Vaccination>();
							pv = sc.getVaccinationService().findVaccinationRecordByCriteria(con.getMappedId(), null, null, null, null, cal.getTime(), cal2.getTime(), null, null, null, VACCINATION_STATUS.PENDING, false, 0,1, true, null, null);
		
							if(pv.size()==0){
								pv= sc.getVaccinationService().findVaccinationRecordByCriteria(con.getMappedId(), null, null, null, null, cal.getTime(), cal2.getTime(), null, null, null, null, false, 0, 11,true, null, null);
							}
						
							Vaccination vacc = pv.size() > 0 ? pv.get(0) : null;
		
							//ideally it will never happen that these vars are null
							if(vacc != null && vacc.getVaccinationDuedate()!=null){
								response.setEventClass(Vaccination.class.getName());
								response.setEventId(vacc.getVaccinationRecordNum());
							}
						}
					}
						
					response.setMappedId(con==null?null:con.getMappedId());
					response.setOriginator(ib.getOriginator());
					response.setResponseDate(ib.getRecieveDate());
					response.setRecipient(ib.getRecipient());
					response.setReferenceNumber(ib.getReferenceNumber());
					response.setResponseBody(ib.getText());
					response.setResponseType(ResponseType.SMS);
					//response.setEventClass(eventClass); SET ABOVE
					//response.setEventId(eventId);
					response.setSystemLoggingDate(new Date());
					sc.getCommunicationService().saveResponse(response );
					sc.commitTransaction();
					
					tsc.getSmsService().markInboundAsRead(ib.getReferenceNumber());
				}
				catch (Exception e) {
					e.printStackTrace();
					GlobalParams.RESPONSEJOBLOGGER.error(jxc.getJobDetail().getFullName(), e);
				}
				finally{
					sc.closeSession();
				}
			}
			
			// READ CALLS
			
			List<CallLog> listcl = new ArrayList<CallLog>();
			try {
				listcl = tsc.getCallService().findCall(null, null, CallType.OUTGOING, null, null, true, CallStatus.UNREAD, null, tsc.getDeviceService().findProjectById(GlobalParams.SMS_TARSEEL_PROJECT_ID).getProjectId());
			} catch (DataException e1) {
				e1.printStackTrace();
				GlobalParams.RESPONSEJOBLOGGER.error(jxc.getJobDetail().getFullName() ,e1);
			}
			
			GlobalParams.RESPONSEJOBLOGGER.info("Fetched "+listcl.size()+" UNREAD CALLS");

			for (CallLog cl : listcl) {
				ServiceContext sc = Context.getServices();
	
				try{
					String caller = cl.getCallerNumber();
					if(caller.length() > MAX_CELL_NUMBER_MATCH_LENGTH){
						caller = caller.substring(caller.length() - MAX_CELL_NUMBER_MATCH_LENGTH);
					}
					
					List<ContactNumber> conl = null;
					conl = sc.getDemographicDetailsService().findContactNumber(caller, true, null);
					
					ContactNumber con = null;
					
					if(conl.size() > 0){
						con = conl.get(0);
					}
					
					if(conl.size() > 1){
						String msg = "Multiple ("+conl.size()+") contact numbers found on cell number "+caller
								+ ". Response will be logged against first entity ("+con.getMappedId()+":"+con.getIdMapper().getIdentifiers().get(0).getIdentifier()+")"
								+ ". All mapped entities on cell number are \n\n";
						for (ContactNumber contactNumber : conl) {
							msg += "("+contactNumber.getMappedId()+":"+contactNumber.getIdMapper().getIdentifiers().get(0).getIdentifier()+")\n";
						}
						///////shouldReadInbound = false;
						try{
							EmailEngine.getInstance().emailErrorReportToAdminAsASeparateThread("Multiple entities found on a cell number", msg);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						GlobalParams.RESPONSEJOBLOGGER.error(jxc.getJobDetail().getFullName() +" : "+conl.size()+" contacts found on same number "+caller);
					}
					
					Response response = new Response();
					
					if(con != null){
						String roleName = con.getIdMapper().getRole().getRolename().toLowerCase();
						
						if(roleName.startsWith("child")){
							Calendar cal = Calendar.getInstance();
							cal.setTime( new Date(System.currentTimeMillis()-1000*60*60*24*365L) );
							
							Calendar cal2 = Calendar.getInstance();
							cal2.setTime( new Date(System.currentTimeMillis()+1000*60*60*24*15L) );
						
							List<Vaccination> pv = new ArrayList<Vaccination>();
							pv = sc.getVaccinationService().findVaccinationRecordByCriteria(con.getMappedId(), null, null, null, null, cal.getTime(), cal2.getTime(), null, null, null, VACCINATION_STATUS.PENDING, false, 0,1, true, null, null);
		
							if(pv.size()==0){
								pv= sc.getVaccinationService().findVaccinationRecordByCriteria(con.getMappedId(), null, null, null, null, cal.getTime(), cal2.getTime(), null, null, null, null, false, 0, 11,true, null, null);
							}
						
							Vaccination vacc = pv.size() > 0 ? pv.get(0) : null;
		
							//ideally it will never happen that these vars are null
							if(vacc != null && vacc.getVaccinationDuedate()!=null){
								response.setEventClass(Vaccination.class.getName());
								response.setEventId(vacc.getVaccinationRecordNum());
							}
						}
					}
						
					response.setMappedId(con==null?null:con.getMappedId());
					response.setOriginator(cl.getCallerNumber());
					response.setResponseDate(cl.getCallDate());
					response.setRecipient(cl.getRecipient());
					response.setReferenceNumber(cl.getReferenceNumber());
					response.setResponseBody("CALL");
					if(cl.getCallType().equals(CallType.INCOMING)){
						response.setResponseType(ResponseType.R_CALL);
					}
					else if(cl.getCallType().equals(CallType.MISSED)){
						response.setResponseType(ResponseType.M_CALL);
					}
					//response.setEventClass(eventClass); SET ABOVE
					//response.setEventId(eventId);
					response.setSystemLoggingDate(new Date());
					sc.getCommunicationService().saveResponse(response);
					sc.commitTransaction();
					
					tsc.getCallService().markCallAsRead(cl.getReferenceNumber());
				}
				catch (Exception e) {
					e.printStackTrace();
					GlobalParams.RESPONSEJOBLOGGER.error(jxc.getJobDetail().getFullName(), e);
				}
				finally{
					sc.closeSession();
				}
			}
			
			
			tsc.commitTransaction();
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.RESPONSEJOBLOGGER.error(jxc.getJobDetail().getFullName(), e);
		}
		finally{
			tsc.closeSession();
		}
		// reschedule if cron changed
		String latestCron = Context.getSetting(GlobalParams.RESPONSE_READER_CRON_SETTING, GlobalParams.RESPONSE_READER_DEFAULT_CRON);
		if(!((CronTrigger)jxc.getTrigger()).getCronExpression().equalsIgnoreCase(latestCron)){
			try 
			{
				CronTrigger responseReaderTrigger = new CronTrigger(GlobalParams.RESPONSE_READER_TRIGGERNAME, GlobalParams.RESPONSE_READER_TRIGGERGROUP, latestCron);
				JobDetail job = jxc.getScheduler().getJobDetail(GlobalParams.RESPONSE_READER_JOBNAME, GlobalParams.RESPONSE_READER_JOBGROUP);
				jxc.getScheduler().deleteJob(GlobalParams.RESPONSE_READER_JOBNAME, GlobalParams.RESPONSE_READER_JOBGROUP);
				jxc.getScheduler().scheduleJob(job, responseReaderTrigger);

				GlobalParams.RESPONSEJOBLOGGER.info("Cron Updated from "+latestCron+" to "+responseReaderTrigger.getCronExpression());
			} 
			catch (ParseException e) {
				e.printStackTrace();
				GlobalParams.RESPONSEJOBLOGGER.error(jxc.getJobDetail().getFullName(), e);
			} 
			catch (SchedulerException e) {
				e.printStackTrace();
				GlobalParams.RESPONSEJOBLOGGER.error(jxc.getJobDetail().getFullName(), e);
			}
		}
	}
}
