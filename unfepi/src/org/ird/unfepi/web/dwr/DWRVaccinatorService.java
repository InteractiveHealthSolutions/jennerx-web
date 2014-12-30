package org.ird.unfepi.web.dwr;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import org.directwebremoting.WebContextFactory;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.VaccinatorIncentiveTransaction;
import org.ird.unfepi.utils.EncounterUtil;
import org.ird.unfepi.utils.IncentiveUtils;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.utils.date.DateUtils;
import org.ird.unfepi.utils.date.DateUtils.TIME_INTERVAL;

import com.mysql.jdbc.StringUtils;

public class DWRVaccinatorService 
{
	public int getVaccinatorsCountOnCenter(int vaccinationCenterId){
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){
			//return "Session expired. Logout from application and login again";
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
			List res = sc.getCustomQueryService().getDataBySQL("select count(*) from vaccinator where vaccinationCenterId="+vaccinationCenterId);
			return res.size()==0?0:Integer.parseInt(res.get(0).toString());
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		return -1;
	}
	
	public boolean isIdExists(String programId){
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){
			//return "Session expired. Logout from application and login again";
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
			List resl = sc.getCustomQueryService().getDataBySQL("select count(*) from idmapper where programId='"+programId+"'");
			return resl.size() == 0 ? false: Integer.parseInt(resl.get(0).toString())>0;
		}
		finally {
			sc.closeSession();
		}

	}
	
	public String markPaid(Integer tid,String addonNote,Date pdate){
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
		if(user.hasPermission(SystemPermissions.UPDATE_FINANCIAL_TRANSACTION.toString())){
			ServiceContext sc = Context.getServices();
			try{
				VaccinatorIncentiveTransaction tr = sc.getIncentiveService().findVaccinatorIncentiveTransactionById(tid, false, null);
				if(!tr.getTransactionStatus().equals(VaccinatorIncentiveTransaction.TranscationStatus.DUE)){
					return "Transaction was not found to be DUE";
				}
				
				tr.setAmountPaid(tr.getAmountDue());
				if(!StringUtils.isEmptyOrWhitespaceOnly(addonNote)){
					tr.setDescription((tr.getDescription()==null?"":tr.getDescription())+addonNote);
				}
				tr.setLastEditedByUserId(user.getUser());
				tr.setLastEditedDate(new Date());
				tr.setPaidDate(pdate);
				tr.setTransactionStatus(VaccinatorIncentiveTransaction.TranscationStatus.PAID);
				
				sc.getIncentiveService().updateVaccinatorIncentiveTransaction(tr);
				sc.commitTransaction();
				
				return "DONE: Transaction marked as PAID successfully for Vaccinator ("+tr.getVaccinator().getIdMapper().getIdentifiers().get(0).getIdentifier()+":"+tr.getVaccinator().getFullName()+") with date "+pdate;
			}
			catch (Exception e) {
				e.printStackTrace();
				return "Error while updating transaction: Trace is:"+e.getMessage();
			}
			finally{
				sc.closeSession();
			}
		}
		else{
			return "You donot have permissions to carry out requested task. Contact your System Administrator";
		}
	}
	
	public String doIncentivizeVaccinators(Date incentivizationDateUpper){
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
			if(!UserSessionUtils.hasActiveUserPermission(SystemPermissions.DO_VACCINATOR_INCENTIVIZATION, WebContextFactory.get().getHttpServletRequest())){
				return "You donot have permissions to incentivize vaccinators";
			}
			Date prevfireTime = null;
			try{
				prevfireTime = sc.getIncentiveService().getAllVaccinatorIncentiveEvent(0, 1, true, null).get(0).getDataRangeDateUpper();
				prevfireTime = new Date(prevfireTime.getTime() + 1000);//adding 1 sec to ensure date range dont coincide
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			prevfireTime = prevfireTime==null?new Date(new Date().getTime()-1000*60*60*24*60L):prevfireTime;
			incentivizationDateUpper = DateUtils.roundoffDatetoDate(incentivizationDateUpper);
			
			if(incentivizationDateUpper.before(prevfireTime)){
				return "Incentivization date upper range cannot be before last event`s date "+prevfireTime;
			}
			if(incentivizationDateUpper.after(new Date())){
				return "Incentivization date upper range cannot be after todays date";
			}
			if(DateUtils.differenceBetweenIntervals(new Date(incentivizationDateUpper.getTime()), 
					new Date(prevfireTime.getTime()), TIME_INTERVAL.DATE) < 14){
				return "Incentivization date upper range must have minimum gap of 14 days from last event`s date."+prevfireTime;
			}
			
			EncounterUtil.createVaccinatorIncentiveEncounter(new Date(), prevfireTime, incentivizationDateUpper, DataEntrySource.WEB, new Date(), user.getUser(), sc);
			
			sc.commitTransaction();
			sc.closeSession();

			IncentiveUtils.doVaccinatorIncentivization(prevfireTime, incentivizationDateUpper, user.getUser());
			
			return "Incentivization have taken place. Refresh page to view new records. A csv file is available in downloadable reports under category Incentives.";
		}
		catch (Exception e){
			return "Error while incentivization :"+e.getMessage();
		}
		finally {
			sc.closeSession();
		}
	}
	
}
