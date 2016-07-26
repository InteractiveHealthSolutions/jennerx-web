package org.ird.unfepi.web.controller;

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
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UnfepiUtils;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.validator.ContactNumberValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@SessionAttributes("command")
@RequestMapping("/editcontactNumber")
public class EditContactNumberController extends DataEditFormController{
	private static final FormType formType = FormType.CONTACT_NUMBER_CORRECT;
	
	EditContactNumberController(){
		super(new  DataEditForm("contact_number", "Contact Number (Edit)", SystemPermissions.CORRECT_CHILD_CONTACT_NUMBER));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView editChildView(HttpServletRequest request, ModelAndView modelAndView){
		modelAndView.addObject("command", formBackingObject(request, modelAndView.getModelMap()));
		return showForm(modelAndView, "dataForm");
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")ContactNumber con, BindingResult results, 
			HttpServletRequest request,	HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		ServiceContext sc = Context.getServices();
		try {		
			new ContactNumberValidator().validate(con,results);		
			if(results.hasErrors()){
				return showForm(modelAndView, "dataForm");
			}

			con.setEditor(user.getUser());
			sc.getDemographicDetailsService().updateContactNumber(con);
			sc.commitTransaction();
			
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(con), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, formType, user.getUser().getUsername()));
			String editmessage = "updated successfully";
			String entityRole = request.getParameter("entityRole");
			
			if(entityRole.equalsIgnoreCase("child")){
				return new ModelAndView(new RedirectView("viewChildren.htm?action=search&editOrUpdateMessage="+editmessage +"&childid="+con.getIdMapper().getIdentifiers().get(0).getIdentifier()));
			}
			else if(entityRole.equalsIgnoreCase("vaccinator")){
				return new ModelAndView(new RedirectView("viewVaccinators.htm?action=search&editOrUpdateMessage="+editmessage +"&vaccinatorid="+con.getIdMapper().getIdentifiers().get(0).getIdentifier()));
			}
			
			return new ModelAndView(new RedirectView("viewChildren.htm?action=search&editOrUpdateMessage="+editmessage +"&childid="+con.getIdMapper().getIdentifiers().get(0).getIdentifier()));
		
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView("exception");
		
		} finally {
			sc.closeSession();
		}
	}
	
	protected ContactNumber formBackingObject(HttpServletRequest request, ModelMap model)
	{
		String conId=request.getParameter("coNum");
		ContactNumber con = null;
		ServiceContext sc = Context.getServices();
		try {
			con = sc.getDemographicDetailsService().getContactNumberById(Integer.parseInt(conId), false, new String[]{"idMapper"});
		
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessagev", "An error occurred while retrieving contact number. Error message is:"+e.getMessage());
		
		} finally {
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
