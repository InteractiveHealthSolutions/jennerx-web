package org.ird.unfepi.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEditFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class EditLocationController extends DataEditFormController
{
	private static final FormType formType = FormType.LOCATION_CORRECT;
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,	HttpServletResponse response, 
			Object command, BindException errors) throws Exception {
		final String MESSAGE_USER_EDITED_SUCCESSFULLY="Location data edited successfully.";

		LoggedInUser user=UserSessionUtils.getActiveUser(request);

		Location loc = (Location) command;
		ServiceContext sc = Context.getServices();
		try{
			loc.setLastEditedByUserId(user.getUser());
			loc.setLastEditedDate(new Date());
			sc.getCustomQueryService().update(loc);
			sc.commitTransaction();
			
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(loc), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, formType, user.getUser().getUsername()));
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
		
		return new ModelAndView(new RedirectView("viewLocations.htm?editOrUpdateMessage="+MESSAGE_USER_EDITED_SUCCESSFULLY+"&action=search&locationid="+loc.getName()));
	}
	
	/*@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors) throws Exception 
	{
		return super.processFormSubmission(request, response, command, errors);
	}*/
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		Location loc = null;
		String rec = request.getParameter("rid");
		ServiceContext sc = Context.getServices();
		try{
			loc = (Location) sc.getCustomQueryService().getDataByHQL("from Location l left join fetch l.parentLocation p left join fetch l.locationType lt where l.locationId="+rec).get(0);
		}catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving Location. Error message is:"+e.getMessage());
		}finally{
			sc.closeSession();
		}
		return loc;
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,	Errors errors) throws Exception {
		Map<String, Object> model=new HashMap<String, Object>();
		ServiceContext sc = Context.getServices();
		try{
			model.put("locationTypes", sc.getCustomQueryService().getDataByHQL("FROM LocationType"));
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving Location. Error message is:"+e.getMessage());
		}finally{
			sc.closeSession();
		}

		return model;
	}
}
