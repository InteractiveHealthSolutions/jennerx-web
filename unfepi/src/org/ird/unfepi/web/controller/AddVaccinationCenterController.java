package org.ird.unfepi.web.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEntryFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.GlobalParams.SearchFilter;
import org.ird.unfepi.beans.VCenterRegistrationWrapper;
import org.ird.unfepi.constants.FormType;
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
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class AddVaccinationCenterController  extends DataEntryFormController{

	private static final FormType formType = FormType.VACCINATIONCENTER_ADD;
	private Date dateFormStart = new Date();
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception 
	{
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		
		VCenterRegistrationWrapper vaccw = (VCenterRegistrationWrapper) command;
		
		ServiceContext sc = Context.getServices();
		try{
			String cityId = request.getParameter("cityId");
			String centerLocation = request.getParameter("centerLocation");

			String sql = "SELECT IFNULL(MAX(SUBSTRING(i.identifier, "+(cityId.length()+1)+",LENGTH(i.identifier)-"+(cityId.length()-1)+")),0) FROM vaccinationcenter v JOIN idmapper id ON v.mappedId=id.mappedId JOIN identifier i ON id.mappedId=i.mappedId WHERE name NOT LIKE 'other%' AND i.identifier NOT LIKE '%999' AND i.identifier LIKE '"+cityId+"%'";
			int maxidstk = Integer.parseInt(sc.getCustomQueryService().getDataBySQL(sql ).get(0).toString());
			String vcProgramId = cityId+StringUtils.leftPad(""+(maxidstk+1), 3, "0");//request.getParameter("autogenIdPart")+request.getParameter("storekeeperIdAssigned");
	
			ControllerUIHelper.doVaccinationCenterRegistration(vcProgramId, centerLocation==null?null:Integer.parseInt(centerLocation), vaccw.getVaccinationCenter(), vaccw.getVaccineDayMapList(), vaccw.getCalendarDays(), user.getUser(), sc);
			
			EncounterUtil.createVaccinationCenterRegistrationEncounter(Short.parseShort(cityId), vcProgramId, vaccw.getVaccinationCenter(), vaccw.getVaccineDayMapList(), DataEntrySource.WEB, dateFormStart, user.getUser(), sc);
					
			sc.commitTransaction();
			
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(vaccw.getVaccinationCenter()), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));
			for (Map<String, Object> vdml : vaccw.getVaccineDayMapList()) {
				String[] strarr = (String[]) vdml.get("daylist");
				Vaccine vaccine = (Vaccine)vdml.get("vaccine");
				GlobalParams.DBLOGGER.info("Vaccine="+vaccine.getName()+";Days="+Arrays.toString(strarr), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));
			}

			return new ModelAndView(new RedirectView("viewVaccinationCenters.htm?action=search&"+SearchFilter.PROGRAM_ID.FILTER_NAME()+"="+vcProgramId));
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
		dateFormStart = new Date();
		
		return new VCenterRegistrationWrapper();
	}
/*	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		System.out.println(errors.getAllErrors());
		return null;
	}*/
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,	Errors errors) throws Exception 
	{
		Map<String, Object> model = new HashMap<String, Object>();
		ServiceContext sc = Context.getServices();;
		try{
			//model.put("relationships", sc.getDemographicDetailsService().getAllRelationship());
			
//			String vaccinationCenterIdAssigned = request.getParameter("vaccinationCenterIdAssigned");
//			model.put("vaccinationCenterIdAssigned", vaccinationCenterIdAssigned);
//			
//			String autogenIdPart = request.getParameter("autogenIdPart");
//			model.put("autogenIdPart", autogenIdPart);
			
			model.put("cityIdselected", request.getParameter("cityId"));

		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving vaccination center reference datas. Error message is:"+e.getMessage());
		}
		finally{
			sc.closeSession();
		}
		
		
		return model;
	}
}
