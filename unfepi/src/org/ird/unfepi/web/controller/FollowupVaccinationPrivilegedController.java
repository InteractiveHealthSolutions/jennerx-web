package org.ird.unfepi.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.ChildIncentivization;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.utils.VaccinationCenterVisit;
import org.ird.unfepi.web.validator.VaccinationValidator;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class FollowupVaccinationPrivilegedController extends SimpleFormController
{
	private static final FormType formType = FormType.FOLLOWUP_ADD_ADMIN;
	private Date dateFormStart = new Date();

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception
	{
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		ServiceContext sc = Context.getServices();
		try{
			List<ChildIncentivization> lotteryRes = null;//ControllerUIHelper.doFollowup(DataEntrySource.WEB, (VaccinationCenterVisit) command, dateFormStart, user.getUser(), sc);
			
			sc.commitTransaction();

			String editmessage="Child Enrolled successfully. \n Lottery Runner information : ";
			for (ChildIncentivization childLotteryRunner : lotteryRes) 
			{
				editmessage += "\n"+childLotteryRunner.VACCINE_NAME + " lottery ";
				if(childLotteryRunner.HAS_WON==null){
					editmessage+=" errors ("+childLotteryRunner.LOTTERY_STATUS_ERRORS+")";
				}
				else if(childLotteryRunner.HAS_WON){
					editmessage+=" won, CODE="+childLotteryRunner.VERIFICATION_CODE+":AMOUNT="+childLotteryRunner.AMOUNT.toString();
				}
				else {
					editmessage+=" performed and not won";
				}
			}
			
//TODO			return new ModelAndView(new RedirectView("viewVaccinationRecord.htm?action=search&editOrUpdateMessage="+editmessage+"&childIdName="+child.getIdMapper().getIdentifiers().get(0).getIdentifier()));
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
		return null;
	}
	
	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors) throws Exception 
	{
		VaccinationValidator validator = (VaccinationValidator) getValidator();
		validator.validateVaccinationPrivilegedForm(command, errors, request);
		return super.processFormSubmission(request, response, command, errors);
	}
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception 
	{
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA), true));
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA), true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Short.class, new CustomNumberEditor(Short.class, true));
		binder.registerCustomEditor(Float.class, new CustomNumberEditor(Float.class, true));
		binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor(WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING, WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING, true));
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		dateFormStart = new Date();
		
		String pid=request.getParameter("pid");//child-program-id
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		Vaccination vacc=new Vaccination();
		int uniqVaccRecNum = (int) System.currentTimeMillis();
		Child child = new Child();
		ArrayList<Map<String, Object>> pvl = new ArrayList<Map<String, Object>>();
		if(user==null){
			boolean session_expired = true;
			request.setAttribute("session_expired", session_expired);
		}
		else{
			ServiceContext sc = Context.getServices();
			try{
				child = sc.getChildService().findChildById(pid, true, new String[]{"idMapper"});
				uniqVaccRecNum = child.getMappedId();
				request.getSession().setAttribute("child"+uniqVaccRecNum, child);

				List<Vaccination> pvaccl = sc.getVaccinationService().findVaccinationRecordByCriteria(child.getMappedId(), null, null, null, null, null, null, null, null, null, null, false, 0, 100, true, new String[]{"vaccine"}, null);
				
				for (Vaccination vaccination : pvaccl) 
				{
					VaccinationCenter pvc = null;
					Vaccinator pvaccinator = null;

					Map<String, Object> prevvacclmap = new HashMap<String, Object>();
				
					if(vaccination.getVaccinationCenterId() != null){
						pvc = sc.getVaccinationService().findVaccinationCenterById(vaccination.getVaccinationCenterId(), true, new String[]{"idMapper"});
					}
					
					if(vaccination.getVaccinatorId() != null){
						pvaccinator = sc.getVaccinationService().findVaccinatorById(vaccination.getVaccinatorId(), true, new String[]{"idMapper"});
					}
					
					prevvacclmap.put("previous_vaccination", vaccination);
					prevvacclmap.put("previous_vaccination_vcenter", pvc);
					prevvacclmap.put("previous_vaccination_vaccinator", pvaccinator);
					pvl.add(prevvacclmap);
				}
				
				request.getSession().setAttribute("prevVaccList"+pid, pvl);
			}
			catch (Exception e) {
				e.printStackTrace();
				GlobalParams.FILELOGGER.error(formType.name(), e);
				request.setAttribute("errorMessage", "Oops .. Some error occurred. Exception message is:"+e.getMessage());
				request.setAttribute("shouldenableVaccination", false);
			}finally{
				sc.closeSession();
			}
		}
		
		Collections.reverse(pvl);
		vacc.setChildId(child.getMappedId());
		vacc.setVaccinationRecordNum(uniqVaccRecNum);
		
		return vacc;
	}
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,	Errors errors) throws Exception {
		boolean session_expired=false;
		Map<String, Object> model=new HashMap<String, Object>();
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			session_expired=true;
			request.setAttribute("session_expired", session_expired);
		}else{
			ServiceContext sc = Context.getServices();
			try{
				Vaccination vacc = (Vaccination) command;
//	TODO			model.put("vaccines", sc.getVaccinationService().getAll(true));
//
//				ControllerUIHelper.prepareFollowupReferenceData(request, model, vacc, sc);
				
				ControllerUIHelper.prepareVaccinationReferenceData(request, model, 
						sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"}), 
						sc.getVaccinationService().getAllVaccinator(0, Integer.MAX_VALUE, true, new String[]{"idMapper"}));
			}
			catch (Exception e) {
				e.printStackTrace();
				GlobalParams.FILELOGGER.error(formType.name(), e);
				request.setAttribute("errorMessagev", "An error occurred while retrieving reference data list. Error message is:"+e.getMessage());
			}
			finally{
				sc.closeSession();
			}
		}
		
		
		return model;
	}
}
