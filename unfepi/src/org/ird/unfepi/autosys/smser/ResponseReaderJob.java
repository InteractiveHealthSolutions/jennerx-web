package org.ird.unfepi.autosys.smser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.ird.unfepi.EmailEngine;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.CommunicationNote;
import org.ird.unfepi.model.CommunicationNote.CommunicationCategory;
import org.ird.unfepi.model.CommunicationNote.CommunicationEventType;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Model.SmsStatus;
import org.ird.unfepi.model.Response;
import org.ird.unfepi.model.Response.ResponseStatus;
import org.ird.unfepi.model.Response.ResponseType;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.UserSms;
import org.ird.unfepi.model.UserSms.SmsType;
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
import org.irdresearch.smstarseel.data.OutboundMessage.PeriodType;
import org.irdresearch.smstarseel.data.OutboundMessage.Priority;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

public class ResponseReaderJob implements Job{

	public enum CommunicationSolution{
		RESEND_VALID_SERVICE_REQUEST_SMS,
		IMMUNIZATION_STATUS_SMS
	}
	
	public enum CommunicationProblem{
		NO_ID_SPECIFIED,
		INVALID_ID_PROVIDED,
		NO_CHILD_FOUND_FOR_ID_SPECIFIED,
		VACCINE_SCHEDULE_INQUIRY
	}
	
	
	private static int MAX_CELL_NUMBER_MATCH_LENGTH = Integer.parseInt(
			Context.getSetting("cellnumber.number-length-without-zero","10")
			);
	private static int MAX_RESPONSE_LENGTH = Integer.parseInt(
			Context.getSetting("response.max-response-length","500")
			);
	
	public final static String SERVICE_SEPARATOR_REGEX = "[\\s:\\-_]{1,3}";
	public static final String INQUIRY_ELIGIBILTIY_REGEX = "(?i)(vs"+SERVICE_SEPARATOR_REGEX+".*)";
	
	public void readResponses(String errorTag){
		TarseelServices tsc = TarseelContext.getServices();

		GlobalParams.RESPONSEJOBLOGGER.info("Running Job: "+errorTag);

		try{
			List<InboundMessage> list = new ArrayList<InboundMessage>();
			try {
				list = tsc.getSmsService().findInbound(null, null, InboundStatus.UNREAD, null, null, null, tsc.getDeviceService().findProjectById(GlobalParams.SMS_TARSEEL_PROJECT_ID).getProjectId(), false);
			} catch (DataException e1) {
				e1.printStackTrace();
				GlobalParams.RESPONSEJOBLOGGER.error(errorTag ,e1);
			}
			
			GlobalParams.RESPONSEJOBLOGGER.info("Fetched "+list.size()+" RECEIVED sms");

			for (InboundMessage ib : list) {
				//sms eligible for vaccine certificate 
				if(ib.getText().trim().toLowerCase().matches(INQUIRY_ELIGIBILTIY_REGEX)){
					handleEPICertifiacteInquiry(ib);
					// donot forget this line otherwise it would stuck in an infinite loop with reading same sms again and again
					tsc.getSmsService().markInboundAsRead(ib.getReferenceNumber());

					continue;// move to next sms
				}
				
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
						GlobalParams.RESPONSEJOBLOGGER.error(errorTag +" : "+conl.size()+" contacts found on same number "+ib.getOriginator());
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
							pv = sc.getVaccinationService().findVaccinationRecordByCriteria(con.getMappedId(), null, null, null, null, cal.getTime(), cal2.getTime(), null, null, null, VACCINATION_STATUS.SCHEDULED, false, 0,1, true, null, null);
		
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
					response.setResponseStatus(ResponseStatus.N_A);
					//response.setEventClass(eventClass); SET ABOVE
					//response.setEventId(eventId);
					response.setSystemLoggingDate(new Date());
					sc.getCommunicationService().saveResponse(response );
					sc.commitTransaction();
					
					tsc.getSmsService().markInboundAsRead(ib.getReferenceNumber());
				}
				catch (Exception e) {
					e.printStackTrace();
					GlobalParams.RESPONSEJOBLOGGER.error(errorTag, e);
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
				GlobalParams.RESPONSEJOBLOGGER.error(errorTag ,e1);
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
						GlobalParams.RESPONSEJOBLOGGER.error(errorTag +" : "+conl.size()+" contacts found on same number "+caller);
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
							pv = sc.getVaccinationService().findVaccinationRecordByCriteria(con.getMappedId(), null, null, null, null, cal.getTime(), cal2.getTime(), null, null, null, VACCINATION_STATUS.SCHEDULED, false, 0,1, true, null, null);
		
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
					response.setResponseStatus(ResponseStatus.N_A);
					//response.setEventClass(eventClass); SET ABOVE
					//response.setEventId(eventId);
					response.setSystemLoggingDate(new Date());
					sc.getCommunicationService().saveResponse(response);
					sc.commitTransaction();
					
					tsc.getCallService().markCallAsRead(cl.getReferenceNumber());
				}
				catch (Exception e) {
					e.printStackTrace();
					GlobalParams.RESPONSEJOBLOGGER.error(errorTag, e);
				}
				finally{
					sc.closeSession();
				}
			}
			
			
			tsc.commitTransaction();
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.RESPONSEJOBLOGGER.error(errorTag, e);
		}
		finally{
			tsc.closeSession();
		}
	}
	
	@Override
	public void execute(JobExecutionContext jxc) throws JobExecutionException 
	{
		readResponses(jxc.getJobDetail().getFullName());
		
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
	
	private int createInquiryReceivedResponse(InboundMessage ib, ResponseStatus responseStatus, Integer mappedId, ServiceContext sc) {
		Response r = new Response();
		// r.setEventClass(eventClass);
		// r.setEventId(eventId);
		r.setMappedId(mappedId);
		r.setOriginator(ib.getOriginator());
		r.setRecipient(ib.getRecipient());
		r.setReferenceNumber(ib.getReferenceNumber());
		r.setResponseBody(ib.getText());
		r.setResponseDate(ib.getRecieveDate());
		r.setResponseStatus(responseStatus);
		r.setResponseType(ResponseType.VACCINE_SCHEDULE_INQUIRY);
		r.setSystemLoggingDate(new Date());
		
		return Integer.parseInt(sc.getCommunicationService().saveResponse(r).toString());
	}
	
	private int createInquiryReplySms(InboundMessage ib, String text, Integer mappedId, User u, ServiceContext sc) {
		UserSms s = new UserSms();
		s.setCreator(u);
		s.setDueDate(new Date());
		//s.setOriginator();
		s.setRecipient(ib.getOriginator());
		s.setRecipientId(mappedId);
		s.setSmsStatus(SmsStatus.LOGGED);
		s.setSmsType(SmsType.VACCINE_SCHEDULE_INQUIRY);
		s.setText(text);
		
		TarseelServices tsc = TarseelContext.getServices();
		try{
			String referenceNumber = tsc.getSmsService().createNewOutboundSms(s.getRecipient(), text, s.getDueDate(), Priority.HIGHEST, 24, PeriodType.HOUR, GlobalParams.SMS_TARSEEL_PROJECT_ID, null);
			tsc.commitTransaction();
			
			s.setReferenceNumber(referenceNumber);
		}
		finally{
			tsc.closeSession();
		}		
		return Integer.parseInt(sc.getUserSmsService().saveUserSms(s).toString());
	}
	
	private void createInquiryCommunicationNotes(InboundMessage ib, String groupId, 
			int receivedResponseId, Class sentSmsClass, int sentSmsId, 
			CommunicationProblem problemGroup, CommunicationSolution solutionGroup, 
			String subject, User u, ServiceContext sc) {
		CommunicationNote c = new CommunicationNote();
		c.setCommunicationCategory(CommunicationCategory.VACCINE_SCHEDULE_INQUIRY);
		c.setCommunicationEventType(CommunicationEventType.INBOUND);
		c.setCreator(u);
		c.setEventDate(ib.getRecieveDate());
		c.setGroupId(groupId);
		c.setProbeClass(Response.class.getName());
		c.setProbeId(receivedResponseId+"");
		c.setProblem("this");
		c.setProblemGroup(problemGroup.name());
		c.setReceiver("n/a");
		c.setSolution(sentSmsClass.getName()+":"+sentSmsId);
		c.setSolutionGroup(solutionGroup.name());
		c.setSource(ib.getOriginator());//Fields this and below are used to connect different responses and inbounds
		c.setSubject(subject);
		
		sc.getCustomQueryService().save(c);
		
		CommunicationNote c2 = new CommunicationNote();
		c2.setCommunicationCategory(CommunicationCategory.VACCINE_SCHEDULE_INQUIRY);
		c2.setCommunicationEventType(CommunicationEventType.OUTBOUND);
		c2.setCreator(u);
		c2.setEventDate(ib.getRecieveDate());
		c2.setGroupId(groupId);
		c2.setProbeClass(sentSmsClass.getName());
		c2.setProbeId(sentSmsId+"");
		c2.setProblem(Response.class.getName()+":"+receivedResponseId);
		c2.setProblemGroup(problemGroup.name());
		c2.setReceiver("n/a");
		c2.setSolution("this");
		c2.setSolutionGroup(solutionGroup.name());
		c2.setSource(ib.getOriginator());//Fields this and below are used to connect different responses and inbounds
		c2.setSubject(subject);
		
		sc.getCustomQueryService().save(c2);
	}
	public void handleInvalidRequest(InboundMessage ib, ServiceContext sc, CommunicationProblem problemGroup, String text) {
		String groupId = UUID.randomUUID().toString();
		
		User u = sc.getUserService().findUserByUsername("daemon");

		int rid = createInquiryReceivedResponse(ib, ResponseStatus.REJECTED, null, sc);
		int sid = createInquiryReplySms(ib, text, null, u, sc);
		
		//Register response and sms via communication notes
		createInquiryCommunicationNotes(ib, groupId, rid, UserSms.class, sid, problemGroup, CommunicationSolution.RESEND_VALID_SERVICE_REQUEST_SMS, null, u, sc);
	}
	
	public void handleSuccessInquiry(Child child, String ID, InboundMessage ib, ServiceContext sc, CommunicationProblem problemGroup) {
		String groupId = UUID.randomUUID().toString();
		
		User u = sc.getUserService().findUserByUsername("daemon");

		int rid = createInquiryReceivedResponse(ib, ResponseStatus.ACCEPTED, child.getMappedId(), sc);
		
		List<Vaccination> vl = sc.getVaccinationService().findByCriteria(child.getMappedId(), null, null, 0, 100, true, new String[]{"vaccine"});
		
		String text = "";
		boolean allVaccinated = true;
		for (Vaccination v : vl) {
			if(v.getVaccinationStatus().equals(VACCINATION_STATUS.SCHEDULED)){
				allVaccinated = false;
				text += "\nVaccine: "+v.getVaccine().getName()+" due on "+WebGlobals.GLOBAL_JAVA_DATE_FORMAT.format(v.getVaccinationDuedate());
			}
			else if(v.getVaccinationStatus().equals(VACCINATION_STATUS.RETRO_DATE_MISSING)){
				text += "\nVaccine: "+v.getVaccine().getName()+" received but missing date info.";
			}
			else if(v.getVaccinationStatus().equals(VACCINATION_STATUS.RETRO)
					|| v.getVaccinationStatus().equals(VACCINATION_STATUS.VACCINATED)){
				text += "\nVaccine: "+v.getVaccine().getName()+" received on "+WebGlobals.GLOBAL_JAVA_DATE_FORMAT.format(v.getVaccinationDate());
			}
		}
		
		String childInfo = child.getFullName()+" s/o "+child.getFatherFirstName()+" with ID: "+ID;
		if(!allVaccinated){
			childInfo += " needs more vaccinations";
		}
		else {
			childInfo += " is fully immunized";
		}
		
		text = childInfo +"\nDetails:"+ text;

		int sid = createInquiryReplySms(ib, text , child.getMappedId(), u, sc);
		
		//Register response and sms via communication notes
		createInquiryCommunicationNotes(ib, groupId, rid, UserSms.class, sid, problemGroup, CommunicationSolution.IMMUNIZATION_STATUS_SMS, ID, u, sc);
	}
	
	public void handleEPICertifiacteInquiry(InboundMessage ib) {
		ServiceContext sc = Context.getServices();
		try{
			//remove all double spaces with a single space so now text would be vs(space/:/-/_)ID....
			String text = ib.getText().trim().replaceAll("  ", " ");
			String[] chunkedText = text.split(SERVICE_SEPARATOR_REGEX);
			if(chunkedText.length < 2){//text doesnot contain service and id
				handleInvalidRequest(ib, sc, CommunicationProblem.NO_ID_SPECIFIED, "EPI Certification inquiry must contain a valid Child ID with format 'VS Child ID' (without quotes). Example inquiry request 'VS 10010010001001'");
				return;
			}
			String service = chunkedText[0];
			// id must be specified as first string after service request and any text 
			// specified after id must be separated by a space or carriage return and that would be ignored
			String id = chunkedText[1].trim().split("[\\s\r\n]+")[0];
			if(!id.matches("[\\d\\w-]{5,20}")){// id can change over time but an id would never contain invalid characters
				handleInvalidRequest(ib, sc, CommunicationProblem.INVALID_ID_PROVIDED, "EPI Certification inquiry can not handle ID provided '"+id+"'. The request format is 'VS Child ID' (without quotes). Example inquiry request 'VS 10010010001001'");
				return;
			}
			
			Child child = sc.getChildService().findChildById(id, true, null);
			if(child == null){
				handleInvalidRequest(ib, sc, CommunicationProblem.NO_CHILD_FOUND_FOR_ID_SPECIFIED, "EPI Certification inquiry can not find Child with ID provided '"+id+"'. The request format is 'VS Child ID' (without quotes). Example inquiry request 'VS 10010010001001'");
				return;
			}
			
			// send success message with data required
			handleSuccessInquiry(child, id, ib, sc, CommunicationProblem.VACCINE_SCHEDULE_INQUIRY);
		}
		finally{
			sc.commitTransaction();// now commit all data to persist
			sc.closeSession();
		}
	}
}
