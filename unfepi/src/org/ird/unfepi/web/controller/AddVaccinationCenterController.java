package org.ird.unfepi.web.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEntryForm;
import org.ird.unfepi.DataEntryFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.GlobalParams.SearchFilter;
import org.ird.unfepi.beans.VCenterRegistrationWrapper;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.utils.EncounterUtil;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.StringUtils;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.validator.VaccinationCenterValidator;
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
@RequestMapping("/addvaccination_center")
public class AddVaccinationCenterController extends DataEntryFormController {
	
	private static final FormType formType = FormType.VACCINATIONCENTER_ADD;
	private Date dateFormStart = new Date();
	
	public AddVaccinationCenterController() {
		super(new DataEntryForm("vaccination_center", "Site (New)", SystemPermissions.ADD_VACCINATION_CENTERS));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView addVaccinationCenterView(HttpServletRequest request, ModelAndView modelAndView){
		modelAndView.addObject("command", formBackingObject(request));
		return showForm(modelAndView, "dataForm");		
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")VCenterRegistrationWrapper vaccw, BindingResult results,
								 HttpServletRequest request, HttpServletResponse response,ModelAndView modelAndView) throws Exception {
		
		VaccinationCenterValidator validator = new VaccinationCenterValidator();
		validator.validate(vaccw, results);
		if (com.mysql.jdbc.StringUtils.isEmptyOrWhitespaceOnly(request.getParameter("cityId")) || 
		    com.mysql.jdbc.StringUtils.isEmptyOrWhitespaceOnly(request.getParameter("centerLocation"))) {
			results.reject("", "City and Site Location MUST be specified for center");
		}
		if (results.hasErrors()) {
			return showForm(modelAndView,  "dataForm");
		}
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);		
		ServiceContext sc = Context.getServices();
		try {
			String cityId = request.getParameter("cityId");
			String centerLocation = request.getParameter("centerLocation");
			
//			String cityIdParam = request.getParameter("cityId");
//			String cityId = sc.getCustomQueryService().getDataByHQL("SELECT otherIdentifier FROM Location WHERE locationId="+cityIdParam).get(0).toString();
			
			String sql = "SELECT IFNULL(MAX(SUBSTRING(i.identifier, "+(cityId.length()+1)+",LENGTH(i.identifier)-"+(cityId.length()-1)+")),0) FROM vaccinationcenter v JOIN idmapper id ON v.mappedId=id.mappedId JOIN identifier i ON id.mappedId=i.mappedId WHERE i.identifier NOT LIKE '%999' AND i.identifier LIKE '"+cityId+"%'";
			int maxidstk = Integer.parseInt(sc.getCustomQueryService().getDataBySQL(sql ).get(0).toString());
			String vcProgramId = cityId+StringUtils.leftPad(""+(maxidstk+1), 3, "0");//request.getParameter("autogenIdPart")+request.getParameter("storekeeperIdAssigned");
			ControllerUIHelper.doVaccinationCenterRegistration(vcProgramId, centerLocation==null?null:Integer.parseInt(centerLocation), vaccw.getVaccinationCenter(), vaccw.getVaccineDayMapList(), vaccw.getCalendarDays(), user.getUser(), sc);
			EncounterUtil.createVaccinationCenterRegistrationEncounter(Short.parseShort(cityId), vcProgramId, vaccw.getVaccinationCenter(), vaccw.getVaccineDayMapList(), DataEntrySource.WEB, dateFormStart, user.getUser(), sc);		
			sc.commitTransaction();
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(vaccw.getVaccinationCenter()), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));
//			for (Map<String, Object> vdml : vaccw.getVaccineDayMapList()) {
//				String[] strarr = (String[]) vdml.get("daylist");
//				Vaccine vaccine = (Vaccine)vdml.get("vaccine");
//				GlobalParams.DBLOGGER.info("Vaccine="+vaccine.getName()+";Days="+Arrays.toString(strarr), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));
//			}
			return new ModelAndView(new RedirectView("viewVaccinationCenters.htm?action=search&"+SearchFilter.PROGRAM_ID.FILTER_NAME()+"="+vcProgramId));
			
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
	
	protected VCenterRegistrationWrapper formBackingObject(HttpServletRequest request) {
		dateFormStart = new Date();
		return new VCenterRegistrationWrapper();
	}
	
	@ModelAttribute
	protected void referenceData(HttpServletRequest request, Model model) throws Exception 
	{
		ServiceContext sc = Context.getServices();
		try{			
			model.addAttribute("cityIdselected", request.getParameter("cityId"));
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving vaccination center reference datas. Error message is:"+e.getMessage());
		}
		finally{
			sc.closeSession();
		}
	}
}
