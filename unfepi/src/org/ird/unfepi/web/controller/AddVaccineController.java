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
import org.ird.unfepi.model.Model.VaccineEntity;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.validator.VaccineValidator;
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
@RequestMapping("/addVaccine")
public class AddVaccineController extends DataEntryFormController{
	
	private static final FormType formType = FormType.VACCINE_ADD;
	private Date dateFormStart = new Date();
	
	public AddVaccineController() {
		super(new DataEntryForm("vaccine", "Vaccine (New)", SystemPermissions.ADD_VACCINE_SCHEDULE));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView addVaccineView(HttpServletRequest request, ModelAndView modelAndView){
		modelAndView.addObject("command", formBackingObject(request));
		return showForm(modelAndView, "dataForm");		
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")Vaccine vaccine/*VaccineRegistrationWrapper wrapper*/, BindingResult results,
								 HttpServletRequest request, HttpServletResponse response,ModelAndView modelAndView) throws Exception {
		
		new VaccineValidator().validate(vaccine, results, true);
		if(results.hasErrors()){	
			System.out.println(results.toString());
			return showForm(modelAndView, "dataForm");	
		}
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);		
		ServiceContext sc = Context.getServices();
		try {
			
			Short id =  (Short) sc.getCustomQueryService().getDataBySQL(
					"SELECT vaccineId FROM vaccine where vaccineId REGEXP '[^9{3,}]' order by vaccineId desc limit 1;").get(0);
			
			vaccine.setVaccineId((short)(id+1));
			vaccine.setCreatedDate(new Date());
			vaccine.setCreatedByUserId(user.getUser());
			
			if(vaccine.getVaccine_entity().equals(VaccineEntity.CHILD_COMPULSORY)){
				vaccine.setSupplementary(false);
			}
			else if(vaccine.getVaccine_entity().equals(VaccineEntity.CHILD_SUPPLEMENTARY)){
				vaccine.setSupplementary(true);
			}
			
			sc.getVaccinationService().addVaccine(vaccine);
			sc.commitTransaction();
			
//			EncounterUtil.createVaccineRegistrationEncounter(vaccine, DataEntrySource.WEB, dateFormStart, user.getUser(), sc);
//			GlobalParams.DBLOGGER.info(IRUtils.convertToString(vaccine), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));
			
			return new ModelAndView(new RedirectView("viewVaccine.htm"));
			
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
	
	protected /*VaccineRegistrationWrapper*/Vaccine formBackingObject(HttpServletRequest request)
	{
		dateFormStart = new Date();
		return new /*VaccineRegistrationWrapper()*/Vaccine();
	}
	
	@ModelAttribute
	protected void referenceData(HttpServletRequest request, Model model) throws Exception 
	{
		ServiceContext sc = Context.getServices();
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessagev", "An error occurred while retrieving reference data list. Error message is:"+e.getMessage());
		} finally {
			sc.closeSession();
		}	
	}
}
