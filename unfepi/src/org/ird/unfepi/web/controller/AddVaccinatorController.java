package org.ird.unfepi.web.controller;

import java.util.Date;
import java.util.List;

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
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.utils.EncounterUtil;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.StringUtils;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.validator.VaccinatorValidator;
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
@RequestMapping("/addvaccinator")
public class AddVaccinatorController  extends DataEntryFormController{
	private static final FormType formType = FormType.VACCINATOR_ADD;
	private Date dateFormStart = new Date();
	
	public AddVaccinatorController() {
		super(new DataEntryForm("vaccinator", "Vaccinator (New)", SystemPermissions.ADD_VACCINATORS_DATA));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView addVaccinatorView(HttpServletRequest request, ModelAndView modelAndView){
		modelAndView.addObject("command", formBackingObject(request));
		return showForm(modelAndView, "dataForm");
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")Vaccinator vacc, BindingResult results,
									HttpServletRequest request, HttpServletResponse response,ModelAndView modelAndView) throws Exception {
		
		VaccinatorValidator valid = new VaccinatorValidator();
		String programid = null;// (request.getParameter("autogenIdPart")+request.getParameter("storekeeperIdAssigned"))
		valid.validateRegistration(programid, request.getParameter("birthdateOrAge"), request.getParameter("vaccinatoragey"), vacc, results);
		valid.validateVaccinatorCredentials(request.getParameter("usernamegiven"), request.getParameter("passwordgiven"), request.getParameter("passwordconfirm"), results);
		if(results.hasErrors()){
			return showForm(modelAndView, "dataForm");
		}
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);		
		ServiceContext sc = Context.getServices();
		try {
			String centerId = sc.getVaccinationService().findVaccinationCenterById(vacc.getVaccinationCenterId(), true, new String[]{"idMapper"}).getIdMapper().getIdentifiers().get(0).getIdentifier();
			String sql = "SELECT IFNULL(MAX(SUBSTRING(i.identifier, "+(centerId.length()+1)+",LENGTH(i.identifier)-"+(centerId.length()-1)+")),0) FROM vaccinator v JOIN idmapper id ON v.mappedId=id.mappedId JOIN identifier i ON id.mappedId=i.mappedId WHERE vaccinationCenterId IS NOT NULL AND i.identifier LIKE '"+centerId+"%'";
			List dl = sc.getCustomQueryService().getDataBySQL(sql);
			int maxidvacc= Integer.parseInt(dl.size()==0?"0":dl.get(0).toString());
			String vcProgramId = centerId+StringUtils.leftPad(""+(maxidvacc+1), 2, "0");//request.getParameter("autogenIdPart")+request.getParameter("storekeeperIdAssigned");
			String username = request.getParameter("usernamegiven");
			String pwd = request.getParameter("passwordgiven");
			ControllerUIHelper.doVaccinatorRegistration(vcProgramId, vacc, username, pwd, false, user.getUser(), sc);
			EncounterUtil.createVaccinatorRegistrationEncounter(vcProgramId, vacc, username, DataEntrySource.WEB, dateFormStart, user.getUser(), sc);
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(vacc), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));
			sc.commitTransaction();
			return new ModelAndView(new RedirectView("viewVaccinators.htm?action=search&"+SearchFilter.PROGRAM_ID.FILTER_NAME()+"="+vcProgramId));
			
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

	protected Vaccinator formBackingObject(HttpServletRequest request) {
		dateFormStart = new Date();
		return new Vaccinator();
	}
	
	@ModelAttribute
	protected void referenceData(HttpServletRequest request, Model model) throws Exception 
	{
		ServiceContext sc = Context.getServices();
		try {
			model.addAttribute("vaccinationCenters", sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"}));
			model.addAttribute("usernamegiven", request.getParameter("usernamegiven"));
			model.addAttribute("passwordgiven", request.getParameter("passwordgiven"));
			model.addAttribute("birthdateOrAge", request.getParameter("birthdateOrAge"));
			model.addAttribute("vaccinatoragey", request.getParameter("vaccinatoragey"));
		
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving vaccinator reference datas. Error message is:"+e.getMessage());
		
		} finally {
			sc.closeSession();
		}
	}
	
}
