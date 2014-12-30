package org.ird.unfepi.web.dwr;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import org.directwebremoting.WebContextFactory;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.DailySummary;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.utils.date.DateUtils;

public class DWRDailySummaryService {

	public boolean isDailySummaryExists(int vaccinationCenterId, Date summaryDate){
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
			List<DailySummary> res = sc.getReportService().findDailySummaryByCriteria(vaccinationCenterId, null, DateUtils.truncateDatetoDate(summaryDate), DateUtils.roundoffDatetoDate(summaryDate), true, 0, 10, null);
			return res.size()>0;
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		return false;
	}
	public boolean isAnyOtherDailySummaryExists(int dsId, int vaccinationCenterId, Date summaryDate){
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
			List<DailySummary> res = sc.getReportService().findDailySummaryByCriteria(vaccinationCenterId, null, DateUtils.truncateDatetoDate(summaryDate), DateUtils.roundoffDatetoDate(summaryDate), true, 0, 10, null);
			return (res.size()>0 && res.get(0).getSerialNumber()!=dsId);
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		return false;
	}
}
