package org.ird.unfepi.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEntryForm;
import org.ird.unfepi.DataEntryFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.utils.EncounterUtil;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UnfepiUtils;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.validator.ContactNumberValidator;
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
@RequestMapping("/addcontactNumber")
public class AddContactNumberController extends DataEntryFormController{
	
	private static final FormType formType = FormType.CONTACT_NUMBER_ADD;
	private Date dateFormStart = new Date();
	
	AddContactNumberController(){
		super(new DataEntryForm("contact_number", "Contact Number (New)", SystemPermissions.ADD_CHILD_CONTACT_NUMBER));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView addContactNumberView(HttpServletRequest request, ModelAndView modelAndView){
		modelAndView.addObject("command", formBackingObject(request));
		return showForm(modelAndView, "dataForm");		
	}
	
	@RequestMapping(method=RequestMethod.POST)
	protected ModelAndView onSubmit(@ModelAttribute("command")ContactNumber con, BindingResult results, 
			HttpServletRequest request, HttpServletResponse response,ModelAndView modelAndView)throws Exception {		
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		ServiceContext sc = Context.getServices();
		
		new ContactNumberValidator().validate(con,results);		
		if(results.hasErrors()){
			return showForm(modelAndView, "dataForm");
		}
		try {
			IdMapper entity = sc.getIdMapperService().findIdMapper(con.getMappedId());
			
			con.setCreator(user.getUser());
			sc.getDemographicDetailsService().saveContactNumber(con);
			
			EncounterUtil.createContactNumberEncounter(entity, con, DataEntrySource.WEB, dateFormStart, user.getUser(), sc);
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(con), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));
			
			sc.commitTransaction();

			String editmessage = "updated successfully";
			String entityRole = request.getParameter("entityRole");

			if(entityRole.equalsIgnoreCase("child")){
				return new ModelAndView(new RedirectView("viewChildren.htm?action=search&editOrUpdateMessage="+editmessage +"&childid="+request.getParameter("programId")));
			}
			else if(entityRole.equalsIgnoreCase("storekeeper")){
				return new ModelAndView(new RedirectView("viewStorekeepers.htm?action=search&editOrUpdateMessage="+editmessage +"&storekeeperid="+request.getParameter("programId")));
			}
			else if(entityRole.equalsIgnoreCase("vaccinator")){
				return new ModelAndView(new RedirectView("viewVaccinators.htm?action=search&editOrUpdateMessage="+editmessage +"&vaccinatorid="+request.getParameter("programId")));
			}
			
			return new ModelAndView(new RedirectView("viewChildren.htm?action=search&editOrUpdateMessage="+editmessage +"&childid="+request.getParameter("programId")));
		
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
	
	protected ContactNumber formBackingObject(HttpServletRequest request) 
	{
		dateFormStart = new Date();
		ContactNumber con = new ContactNumber();
		
		ServiceContext sc = Context.getServices();
		try{
			String programId = request.getParameter("programId");
			IdMapper idm = sc.getIdMapperService().findIdMapper(programId);
			con.setMappedId(idm.getMappedId());
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessagev", "An error occurred while reference data list. Error message is:"+e.getMessage());
		}
		finally{
			sc.closeSession();
		}
		return con;
	}
	
	@ModelAttribute
	protected void referenceData(HttpServletRequest request, Model model) throws Exception 
	{		
		ServiceContext sc = Context.getServices();
		try {
			String programId = request.getParameter("programId");
			model.addAttribute("programId", programId);			
			String entityRole = request.getParameter("entityRole");
			if(entityRole == null){
				entityRole = sc.getIdMapperService().findIdMapper(programId).getRole().getRolename();
			}
			model.addAttribute("entityRole", entityRole);
		
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessagev", "An error occurred while reference data list. Error message is:"+e.getMessage());
		
		} finally {
			sc.closeSession();
		}
	}
	
}
