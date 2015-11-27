package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEntryFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.GlobalParams.SearchFilter;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.LocationType;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class AddLocationController  extends DataEntryFormController{

	private static final FormType formType = FormType.LOCATION_ADD;
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception 
	{
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		
		Location loc = (Location) command;
		
		ServiceContext sc = Context.getServices();
		try{
			ControllerUIHelper.doLocationRegistration(loc, user.getUser(), sc);
			
			sc.commitTransaction();
			
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(loc), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));

			return new ModelAndView(new RedirectView("viewLocations.htm?action=search&"+SearchFilter.NAME_PART.FILTER_NAME()+"="+loc.getName()));
		}
		catch (Exception e) {
			sc.rollbackTransaction();
			
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
	protected Object formBackingObject(HttpServletRequest request)	throws Exception {
		Location l = new Location();
		l.setParentLocation(new Location());
		l.setLocationType(new LocationType());
		return l;
	}
	
	/*@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception {
		LocationValidator v = (LocationValidator) getValidator();
		v.validate(command, errors);
		System.out.println(errors.getAllErrors());
		return processFormSubmission(request, response, command, errors);
	}*/
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,	Errors errors) throws Exception 
	{
		Map<String, Object> model = new HashMap<String, Object>();
		ServiceContext sc = Context.getServices();;
		try{
			model.put("locationTypes", sc.getCustomQueryService().getDataByHQL("FROM LocationType"));
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving location reference datas. Error message is:"+e.getMessage());
		}
		finally{
			sc.closeSession();
		}
		
		return model;
	}
}
