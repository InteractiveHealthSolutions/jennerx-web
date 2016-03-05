package org.ird.unfepi.web.dwr;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import org.directwebremoting.WebContextFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.ChildIncentive;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.utils.IMRUtils;
import org.ird.unfepi.web.validator.ValidatorOutput;
import org.ird.unfepi.web.validator.ValidatorOutput.ValidatorStatus;
import org.ird.unfepi.web.validator.ValidatorUtils;

public class DWRChildService {

	public int getChildrenEnrolledCountOnDay(int vaccinationCenterId, Date enrollmentDate){
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
		try{
			List res = sc.getCustomQueryService().getDataBySQL("select v.childId from vaccination v where vaccinationCenterId="+vaccinationCenterId+" and date(vaccinationDate) = '"+new SimpleDateFormat("yyyy-MM-dd").format(enrollmentDate)+"' and "+GlobalParams.SQL_ENROLLMENT_FILTER_v);
			return res.size();
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		return -1;
	}
	
	public String changeEpiNumber(String childId, String vaccinationCenter, String epiNumber){
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
		Session ss = Context.getNewSession();
		try{
			if(!UserSessionUtils.hasActiveUserPermission(SystemPermissions.CORRECT_VACCINATIONS, WebContextFactory.get().getHttpServletRequest())){
				return "You donot have permissions to carry out this task";
			}
			
			int centerId = sc.getVaccinationService().findVaccinationCenterById(vaccinationCenter, true, null).getMappedId();
			ValidatorOutput vepi = ValidatorUtils.validateEpiNumber(epiNumber, centerId, sc.getChildService().findChildById(childId, true, null).getMappedId(), false, true);
			if(!vepi.STATUS().equals(ValidatorStatus.OK)){
				return vepi.MESSAGE();
			}
			
			Transaction tx = ss.beginTransaction();
			SQLQuery qu = ss.createSQLQuery("update vaccination set epiNumber = '"+epiNumber+"' " +
					" where vaccinationStatus <> 'SCHEDULED' " +
					" and vaccinationCenterId="+centerId +
					" and childId=(select mappedId from idmapper where programId='"+childId+"') ");
			qu.executeUpdate();
			tx.commit();
			
			GlobalParams.DBLOGGER.info("programId="+childId+";epiNumber="+epiNumber+";vaccinationCenter="+vaccinationCenter, LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, FormType.EPI_NUMBER_CHANGE, user.getUser().getUsername()));
			
			return "ok";
		}catch (Exception e) {
			e.printStackTrace();
			return "Error : "+e.getMessage();
		}
		finally{
			try { ss.close(); } catch (Exception e2) {e2.printStackTrace();}
			sc.closeSession();
		}
	}
	
	public static String consumeLottery(String childId, short amount, String verificationCode, 
			int storekeeperId, Date transactionDate){
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
			if(!user.hasPermission(SystemPermissions.DO_LOTTERY_CONSUMPTION.name())){
				return "You donot have permissions to mark lottery consumed";
			}
			
			Child ch = sc.getChildService().findChildById(childId, true, new String[]{"idMapper"});
			if(ch == null){
				return "ERROR: No child found with given ID";
			}
			
			List<ChildIncentive> listChildIncentive = sc.getIncentiveService().findChildIncentiveByCriteria(/*verificationCode,*/ ch.getMappedId(), null, true, null, null, null, null, /*null, null, null, null,*/ null, null, null, 0, 2, false, new String[]{"vaccination"});

			if(listChildIncentive.size() == 0){
				return "ERROR: No winning found for given ID and code.";
			}
			else if(listChildIncentive.size() > 1){
				return "ERROR: Multiple winnings found for given ID and code.";
			}
			
			ChildIncentive childIncentive = listChildIncentive.get(0);
			if(childIncentive.getAmount().intValue() != amount){
				return "ERROR: Given amount not conform with actual winning for given ID and code.";
			}
			/*else if(!childIncentive.getCodeStatus().equals(CodeStatus.AVAILABLE)){
				return "ERROR: Given winning is "+childIncentive.getCodeStatus()+".";
			}*/
			
			if(!childIncentive.getVaccination().getVaccinationStatus().equals(VACCINATION_STATUS.VACCINATED)){
				return "ERROR: No record found VACCINATED for given ID and lottery vaccine.";
			}
			
			/*childIncentive.setCodeStatus(CodeStatus.CONSUMED);*/
			childIncentive.setEditor(user.getUser());
		//	childIncentive.setConsumptionDate(new Date());
		//	childIncentive.setStorekeeperId(storekeeperId);
			childIncentive.setTransactionDate(transactionDate);
			
			sc.getIncentiveService().updateChildIncentive(childIncentive);
			
		//	EncounterUtil.createLotteryConsumerEncounter(ch, childIncentive, DataEntrySource.WEB, new Date(), user.getUser(), sc);
			
			List<ReminderSms> smsl = IMRUtils.createLotteryConsumedReminderSms(childIncentive.getVaccinationRecordNum(), childIncentive.getTransactionDate(), user.getUser(), null, sc);
			
			for (ReminderSms reminderSms : smsl) {
				sc.getReminderService().addReminderSmsRecord(reminderSms);
			}
			
			sc.commitTransaction();
			
			return "DONE: Lottery ws successfully marked as consumed.";
		}
		catch (Exception e){
			return "Error while lottery updation :"+e.getMessage();
		}
		finally {
			sc.closeSession();
		}
	}
}
