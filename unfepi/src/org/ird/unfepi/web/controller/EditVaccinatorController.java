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
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UnfepiUtils;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.validator.VaccinatorValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


@Controller
@SessionAttributes("command")
@RequestMapping("/editvaccinator")
public class EditVaccinatorController extends DataEditFormController{
	
	private static final FormType formType = FormType.VACCINATOR_CORRECT;
	
	EditVaccinatorController(){
		super(new  DataEditForm("vaccinator", "Vaccinator (Edit)", SystemPermissions.CORRECT_VACCINATORS_DATA));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView editVaccinationCenterView(HttpServletRequest request, ModelAndView modelAndView){
		modelAndView.addObject("command", formBackingObject(request));
		ServiceContext sc = Context.getServices();
		modelAndView.addObject("vaccinationCenters", sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"}));
		sc.closeSession();
		return showForm(modelAndView, "dataForm");
	}
	@RequestMapping(method=RequestMethod.POST)
	protected ModelAndView onSubmit(@ModelAttribute("command")Vaccinator vacc, BindingResult results,
			HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		
		new VaccinatorValidator().validate(vacc, results);
		if (results.hasErrors()) {
			return showForm(modelAndView, "dataForm");
		}
	
		LoggedInUser user = UserSessionUtils.getActiveUser(request);		
		ServiceContext sc = Context.getServices();
		try {
			vacc.setEditor(user.getUser());
			sc.getVaccinationService().updateVaccinator(vacc);
			
			User vuser = sc.getUserService().findUser(vacc.getMappedId());
			vuser.setFirstName(vacc.getFirstName());
			vuser.setMiddleName(vacc.getMiddleName());
			vuser.setLastName(vacc.getLastName());
			
			sc.getUserService().updateUser(vuser);
			sc.commitTransaction();

			GlobalParams.DBLOGGER.info(IRUtils.convertToString(vacc), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, formType, user.getUser().getUsername()));
			return new ModelAndView(new RedirectView("viewVaccinators.htm?action=search&vaccinatorid="+vacc.getIdMapper().getIdentifiers().get(0).getIdentifier()));
			
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView("exception");
		
		} finally {
			sc.closeSession();
		}
	}
	
	protected Vaccinator formBackingObject(HttpServletRequest request) {
		
		Vaccinator vacc = new Vaccinator();
		String programId = request.getParameter("programId");
		ServiceContext sc = Context.getServices();
		try {
			vacc = sc.getVaccinationService().findVaccinatorById(programId, false, new String[]{"idMapper"});
		
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving Vaccinator reference datas. Error message is:"+e.getMessage());
		
		} finally {
			sc.closeSession();
		}
		return vacc;
	}
}
