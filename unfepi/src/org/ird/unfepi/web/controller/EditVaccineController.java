package org.ird.unfepi.web.controller;

import java.util.Date;
import java.util.List;

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
import org.ird.unfepi.model.Model.VaccineEntity;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.validator.VaccineValidator;
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
@RequestMapping("/editVaccine")
public class EditVaccineController extends DataEditFormController{
	
	private static final FormType formType = FormType.VACCINE_CORRECT;
	
	EditVaccineController() {
		super(new  DataEditForm("vaccine", "Vaccine (Edit)", SystemPermissions.CORRECT_VACCINES));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView editVaccinationCenterView(HttpServletRequest request, ModelAndView modelAndView) throws Exception{
		modelAndView.addObject("command", formBackingObject(request));
		return showForm(modelAndView, "dataForm");
	}
	
	@RequestMapping(method=RequestMethod.POST)
	protected ModelAndView onSubmit(@ModelAttribute("command")Vaccine vacc, BindingResult results, HttpServletRequest request,
			HttpServletResponse response, ModelAndView modelAndView)	throws Exception {
		String message="Vaccine edited successfully.";
	
		LoggedInUser user=UserSessionUtils.getActiveUser(request);;
		ServiceContext sc = Context.getServices();
		
		new VaccineValidator().validate(vacc, results, false);
		if(results.hasErrors()){	
//			System.out.println(results.toString());
			return showForm(modelAndView, "dataForm");	
		}
		
		try{
			
			if(vacc.getVaccine_entity().equals(VaccineEntity.CHILD_COMPULSORY)){
				vacc.setSupplementary(false);
			}
			else if(vacc.getVaccine_entity().equals(VaccineEntity.CHILD_SUPPLEMENTARY)){
				vacc.setSupplementary(true);
			}
			
			vacc.setEditor(user.getUser());
			vacc.setLastEditedDate(new Date());
			
			List<Vaccine> numOfdoses = sc.getCustomQueryService().getDataByHQL("from Vaccine where shortName = '"+vacc.getShortName()+"' and vaccineId <> " + vacc.getVaccineId());
			if(numOfdoses != null && numOfdoses.size() > 0){
				for (Vaccine vac: numOfdoses) {
					vac.setNumberOfDoses(vacc.getNumberOfDoses());
					sc.getCustomQueryService().update(vac);
				}
			}

			sc.getVaccinationService().updateVaccine(vacc);
			sc.commitTransaction();
			
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(vacc), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, formType, user.getUser().getUsername()));

			
			return new ModelAndView(new RedirectView("viewVaccine.htm"));
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
	
	protected Vaccine formBackingObject(HttpServletRequest request) throws Exception {
		Vaccine vaccine = new Vaccine();
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			boolean session_expired = true;
			request.setAttribute("session_expired", session_expired);
		}
		else{
			String vaccineName = request.getParameter("editRecord");
			
//			System.out.println(vaccineName + "   vaccineNamevaccineNamevaccineNamevaccineName");
			
			ServiceContext sc = Context.getServices();
			try{
				vaccine = sc.getVaccinationService().getByName(vaccineName);
			}catch (Exception e) {
				e.printStackTrace();
				GlobalParams.FILELOGGER.error(formType.name(), e);
				request.setAttribute("errorMessage", "An error occurred while retrieving Vaccine. Error message is:"+e.getMessage());
			}finally{
				sc.closeSession();
			}
		}
		return vaccine;
	}

}
