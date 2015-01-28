package org.ird.unfepi.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.beans.DailySummaryWrapper;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.DailySummary;
import org.ird.unfepi.model.DailySummaryVaccineGiven;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class EditDailySummaryController extends SimpleFormController
{
	private static final FormType formType = FormType.DAILY_SUMMARY_CORRECT;
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception 
	{
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		
		DailySummaryWrapper dsw = (DailySummaryWrapper) command;
		DailySummary ds = dsw.getDailySummary();
		ServiceContext sc = Context.getServices();

		try{
			ds.setEditor(user.getUser());
			sc.getReportService().updateDailySummary(ds);
			
			for (DailySummaryVaccineGiven dsvg : dsw.getDsvgList()) {
				sc.getReportService().updateDailySummaryVaccineGiven(dsvg);
			}
			sc.commitTransaction();
			
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(ds), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, formType, user.getUser().getUsername()));
			for (DailySummaryVaccineGiven dsvg : dsw.getDsvgList()) {
				GlobalParams.DBLOGGER.info(ds.getSerialNumber()+";"+IRUtils.convertToString(dsvg), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, formType, user.getUser().getUsername()));
			}
			String editmessage = "updated successfully";
			return new ModelAndView(new RedirectView("viewDailySummaries.htm?action=search&editOrUpdateMessage="+editmessage ));
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}
		finally{
			sc.closeSession();
		}
	}
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception 
	{
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA), true));
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA), true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Short.class, new CustomNumberEditor(Short.class, true));
		binder.registerCustomEditor(Float.class, new CustomNumberEditor(Float.class, true));
		binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor(WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING, WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING, true));
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)	throws Exception {
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		DailySummary ds = new DailySummary();
		List<DailySummaryVaccineGiven> dsvgl = new ArrayList<DailySummaryVaccineGiven>();
		if(user==null){
			boolean session_expired = true;
			request.setAttribute("session_expired", session_expired);
		}
		else{
			String dsId = request.getParameter("dsId");
			ServiceContext sc = Context.getServices();
			try{
				ds = sc.getReportService().findDailySummaryBySerialId(Integer.parseInt(dsId), false, null);
				dsvgl = sc.getReportService().findDailySummaryVaccineGivenByCriteria(null, null, ds.getSerialNumber(), null, false, 0, Integer.MAX_VALUE, null);
			}
			catch (Exception e) {
				e.printStackTrace();
				GlobalParams.FILELOGGER.error(formType.name(), e);
				request.setAttribute("errorMessage", "An error occurred while retrieving Daily Summary. Error message is:"+e.getMessage());
			}finally{
				sc.closeSession();
			}
		}
		
		DailySummaryWrapper dsw= new DailySummaryWrapper(ds, dsvgl);

		return dsw;
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception 
	{
		boolean session_expired=false;
		Map<String, Object> model=new HashMap<String, Object>();
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			session_expired=true;
			request.setAttribute("session_expired", session_expired);
		}else{
			ServiceContext sc = Context.getServices();
			try{
				model.put("vaccinationCenters", sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"}));
				model.put("vaccinators", sc.getVaccinationService().getAllVaccinator(0, Integer.MAX_VALUE, true, new String[]{"idMapper"}));
			}catch (Exception e) {
				e.printStackTrace();
				GlobalParams.FILELOGGER.error(formType.name(), e);
				request.setAttribute("errorMessagev", "An error occurred while reference data list. Error message is:"+e.getMessage());
			}
			finally{
				sc.closeSession();
			}
		}
		
		
		return model;
	}
}
