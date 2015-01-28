package org.ird.unfepi.web.dwr;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.directwebremoting.WebContextFactory;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.WebGlobals.DGUserSmsFieldNames;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.CommunicationNote;
import org.ird.unfepi.model.CommunicationNote.CommunicationEventType;
import org.ird.unfepi.model.Model.SmsStatus;
import org.ird.unfepi.model.UserSms;
import org.ird.unfepi.utils.UserSessionUtils;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.OutboundMessage.PeriodType;
import org.irdresearch.smstarseel.data.OutboundMessage.Priority;

public class DWRCommunicationService {

	public Map<String, Object> getCommunicationNotes(int probeId, String probeClass){
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
		
		Map<String, Object> res = new HashMap<String, Object>();
		ServiceContext sc = Context.getServices();
		try {
			res.put("notes", sc.getCustomQueryService().getDataByHQL("FROM CommunicationNote WHERE probeId = "+probeId+" AND probeClass LIKE '%"+probeClass+"%'"));
			res.put("SUCCESS", true);
		} 
		catch (Exception e) {
			e.printStackTrace();
			res.put("ERROR", "Error: "+e.getMessage());
		}
		finally {
			sc.closeSession();
		}
		return res;
		
	}
	
	public String addNote(String receiver, String source, String subject, String problem, String problemGroup, 
			String solution, String solutionGroup, Date eventDate, String probeId, String probeClass, 
			String communicationEventType, String description) {
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
		
		CommunicationNote cn = new CommunicationNote();
		cn.setCommunicationEventType(CommunicationEventType.valueOf(communicationEventType));
		cn.setCreator(user.getUser());
		cn.setDescription(description);
		cn.setEventDate(eventDate);
		
		if(!probeClass.endsWith(".class")){
			probeClass = probeClass+".class";
		}
		
		cn.setProbeClass(probeClass);
		cn.setProbeId(probeId);
		cn.setProblem(problemGroup);
		cn.setProblemGroup(problemGroup);
		cn.setReceiver(receiver);
		cn.setSolution(solutionGroup);
		cn.setSolutionGroup(solutionGroup);
		cn.setSource(source);
		cn.setSubject(subject);

		ServiceContext sc = Context.getServices();
		try {
			sc.getCustomQueryService().save(cn);
			sc.commitTransaction();
			return "SUCCESS";
		} 
		catch (Exception e) {
			e.printStackTrace();
			return "ERROR: " + e.getMessage();
		}
		finally {
			sc.closeSession();
		}
		
	}
	
	public String postReply(Integer recipientMappedId, String receiver, String subject, String problem, String problemGroup, 
			String solution, String solutionGroup, String probeId, String probeClass, 
			String communicationEventType, String description) {
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
		try {
			String refnum = addUserSms(recipientMappedId, receiver, solution, null, new Date(), Priority.HIGH, 5, user);
			
			CommunicationNote cn = new CommunicationNote();
			cn.setCommunicationEventType(CommunicationEventType.valueOf(communicationEventType));
			cn.setCreator(user.getUser());
			cn.setDescription(description);
			cn.setEventDate(new Date());
			
			if(!probeClass.endsWith(".class")){
				probeClass = probeClass+".class";
			}
			
			cn.setProbeClass(probeClass);
			cn.setProbeId(probeId);
			cn.setProblem(problemGroup);
			cn.setProblemGroup(problemGroup);
			cn.setReceiver(receiver);
			cn.setSolution(solutionGroup);
			cn.setSolutionGroup(solutionGroup);
			cn.setSource(refnum);
			cn.setSubject(subject);
			
			sc.getCustomQueryService().save(cn);
			sc.commitTransaction();
			return "SUCCESS";
		} 
		catch (Exception e) {
			e.printStackTrace();
			return "ERROR: " + e.getMessage();
		}
		finally {
			sc.closeSession();
		}
		
	}
	
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

				addUserSms(recipientId, recipient, smsText, smsDescription, duedate, priority, validityPeriod, user);
				
			}

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
	
	private String addUserSms(Integer recipientMappedId, String recipient, String smsText, String smsDescription, Date duedate, Priority priority, int validityPeriod, LoggedInUser user){
		ServiceContext sc = Context.getServices();
		TarseelServices tsc = TarseelContext.getServices();
		try{
			String referenceNum = tsc.getSmsService().createNewOutboundSms(recipient, smsText, duedate, priority, validityPeriod, PeriodType.HOUR, GlobalParams.SMS_TARSEEL_PROJECT_ID, null);
	
			UserSms sms = new UserSms();
			sms.setCreator(user.getUser());
			sms.setDescription(smsDescription);
			sms.setDueDate(duedate);
			sms.setRecipient(recipient);
			sms.setRecipientId(recipientMappedId);
			sms.setReferenceNumber(referenceNum);
			sms.setSmsStatus(SmsStatus.PENDING);
			sms.setText(smsText);
			
			sc.getUserSmsService().saveUserSms(sms);
			
			tsc.commitTransaction();
			sc.commitTransaction();
			
			return referenceNum;
		}
		finally{
			tsc.closeSession();
			sc.closeSession();
		}
	}
}
