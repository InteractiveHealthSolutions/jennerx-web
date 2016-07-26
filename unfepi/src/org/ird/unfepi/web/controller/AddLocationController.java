package org.ird.unfepi.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEntryForm;
import org.ird.unfepi.DataEntryFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.GlobalParams.SearchFilter;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.LocationType;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UnfepiUtils;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.validator.LocationValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@SessionAttributes("command")
@RequestMapping("/addlocation")
public class AddLocationController  extends DataEntryFormController {
	
	private static final FormType formType = FormType.LOCATION_ADD;
	
	public AddLocationController() {
		super(new DataEntryForm("location", "Location (New)", SystemPermissions.ADD_LOCATIONS));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView addLocationView(HttpServletRequest request, ModelAndView modelAndView){
		modelAndView.addObject("command", formBackingObject());
		return showForm(modelAndView, "dataForm");	
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")Location loc, BindingResult results,
									HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		new LocationValidator().validate(loc, results);
		if(results.hasErrors()){
			return showForm(modelAndView, "dataForm");
		}
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		ServiceContext sc = Context.getServices();
		try {
			ControllerUIHelper.doLocationRegistration(loc, user.getUser(), sc);
			sc.commitTransaction();
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(loc), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));
			return new ModelAndView(new RedirectView("viewLocations.htm?action=search&"+SearchFilter.NAME_PART.FILTER_NAME()+"="+loc.getName()));
			
		} catch (Exception e) {
			sc.rollbackTransaction();
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView("exception");
		
		} finally {
			sc.closeSession();
		}
	}
	
	protected Location formBackingObject() {
		Location location = new Location();
		location.setParentLocation(new Location());
		location.setLocationType(new LocationType());
		return location;
	}
	
	@ModelAttribute
	protected void referenceData(HttpServletRequest request, Model model) throws Exception 
	{
		ServiceContext sc = Context.getServices();
		try {
			model.addAttribute("locationTypes", sc.getCustomQueryService().getDataByHQL("FROM LocationType"));
		
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving location reference datas. Error message is:"+e.getMessage());
		
		} finally{
			sc.closeSession();
		}
	}
}
