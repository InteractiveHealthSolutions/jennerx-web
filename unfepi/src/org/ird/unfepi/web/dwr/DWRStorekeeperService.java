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
import org.ird.unfepi.model.StorekeeperIncentiveTransaction;
import org.ird.unfepi.model.StorekeeperIncentiveTransaction.TranscationStatus;
import org.ird.unfepi.utils.UserSessionUtils;

import com.mysql.jdbc.StringUtils;

public class DWRStorekeeperService {
	public int getStorekeepersOnCityId(String cityId){
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
			List res = sc.getCustomQueryService().getDataBySQL("select count(*) from storekeeper join idmapper using(mappedId) where programId like '"+cityId+"%'");
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
			//return "Session expired. Logout from application and login again";
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
				StorekeeperIncentiveTransaction tr = sc.getIncentiveService().findStorekeeperIncentiveTransactionById(tid, false, null);
				if(!tr.getTransactionStatus().equals(StorekeeperIncentiveTransaction.TranscationStatus.DUE)){
					return "Transaction was not found to be DUE";
				}
				
				tr.setAmountPaid(tr.getAmountDue());
				if(!StringUtils.isEmptyOrWhitespaceOnly(addonNote)){
					tr.setDescription((tr.getDescription()==null?"":tr.getDescription())+addonNote);
				}
				tr.setLastEditedByUserId(user.getUser());
				tr.setLastEditedDate(new Date());
				tr.setPaidDate(pdate);
				tr.setTransactionStatus(TranscationStatus.PAID);
				
				sc.getIncentiveService().updateStorekeeperIncentiveTransaction(tr);
				sc.commitTransaction();
				
				return "DONE: Transaction marked as PAID successfully for Storekeeper ("+tr.getStorekeeper().getIdMapper().getIdentifiers().get(0).getIdentifier()+":"+tr.getStorekeeper().getFullName()+") with date "+pdate;
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
	
	public String doIncentivizeStorekeepers(Date incentivizationDateUpper){
		throw new UnsupportedOperationException("Storkeepers are not given incentives");
	}
}
