package org.ird.unfepi.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEditForm;
import org.ird.unfepi.DataEditFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
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
@RequestMapping("/editlocation")
public class EditLocationController extends DataEditFormController{
	
	private static final FormType formType = FormType.LOCATION_CORRECT;
	
	EditLocationController(){
		super(new  DataEditForm("location", "Location (Edit)", SystemPermissions.CORRECT_LOCATIONS));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView editLocationView(HttpServletRequest request, ModelAndView modelAndView){
		modelAndView.addObject("command", formBackingObject(request));
		return showForm(modelAndView, "dataForm");
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")Location loc, BindingResult results, 
									HttpServletRequest request,	HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		
		new LocationValidator().validate(loc, results);
		if(results.hasErrors()){
			return showForm(modelAndView, "dataForm");
		}
		
		final String MESSAGE_USER_EDITED_SUCCESSFULLY="Location data edited successfully.";

		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		ServiceContext sc = Context.getServices();
		try {
			loc.setLastEditedByUserId(user.getUser());
			loc.setLastEditedDate(new Date());
			sc.getCustomQueryService().update(loc);
			sc.commitTransaction();
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(loc), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, formType, user.getUser().getUsername()));
		
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView("exception");
		
		} finally {
			sc.closeSession();
		}
		return new ModelAndView(new RedirectView("viewLocations.htm?editOrUpdateMessage="+MESSAGE_USER_EDITED_SUCCESSFULLY+"&action=search&locationid="+loc.getName()));
	}
	
	protected Location formBackingObject(HttpServletRequest request) {
		Location loc = null;
		String rec = request.getParameter("rid");
		ServiceContext sc = Context.getServices();
		try {
			loc = (Location) sc.getCustomQueryService().getDataByHQL("from Location l left join fetch l.parentLocation p left join fetch l.locationType lt where l.locationId="+rec).get(0);
		
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving Location. Error message is:"+e.getMessage());
		
		} finally {
			sc.closeSession();
		}
		return loc;
	}
	
	@ModelAttribute
	protected void referenceData(HttpServletRequest request, Model model) throws Exception {
		ServiceContext sc = Context.getServices();
		try {
			model.addAttribute("locationTypes", sc.getCustomQueryService().getDataByHQL("FROM LocationType"));
		
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving Location. Error message is:"+e.getMessage());
		
		} finally {
			sc.closeSession();
		}
	}

}
