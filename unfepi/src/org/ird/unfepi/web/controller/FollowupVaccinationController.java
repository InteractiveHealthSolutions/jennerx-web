package org.ird.unfepi.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEntryFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.LotterySms;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.utils.VaccinationCenterVisit;
import org.ird.unfepi.web.utils.VaccineSchedule;
import org.ird.unfepi.web.utils.VaccineSchedule.VaccineStatusType;
import org.ird.unfepi.web.validator.VaccinationValidator;
import org.ird.unfepi.web.validator.ValidatorUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class FollowupVaccinationController extends DataEntryFormController
{
	private static final FormType formType = FormType.FOLLOWUP_ADD;
	private Date dateFormStart = new Date();

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception
	{
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		
		ServiceContext sc = Context.getServices();
		try{
			VaccinationCenterVisit centerVisit = (VaccinationCenterVisit) command;
			List<VaccineSchedule> vaccineSchedule = (List<VaccineSchedule>) request.getSession().getAttribute(VaccinationCenterVisit.VACCINE_SCHEDULE_KEY+centerVisit.getUuid());
			Child child = (Child) request.getSession().getAttribute("childfollowup");
			
			if(!StringUtils.isEmptyOrWhitespaceOnly(request.getParameter("cnic"))){
				Child c = sc.getChildService().findChildById(child.getMappedId(), false, null);
				c.setNic(request.getParameter("cnic"));
				sc.getChildService().updateChild(c);
			}
			
			Iterator<VaccineSchedule> iter = vaccineSchedule.iterator();
			while (iter.hasNext()) {
				VaccineSchedule vsh = iter.next();
				if (vsh.getStatus().equals(VaccineStatusType.SCHEDULED.name()) || vsh.getStatus().equals(VaccineStatusType.NOT_ALLOWED.name()) || (vsh.getStatus() == null?true:vsh.getStatus().length()==0)) {
					iter.remove();
				}
			}
			
			/*List<ChildLotteryRunner> lotteryRes = */ControllerUIHelper.doFollowup(DataEntrySource.WEB, centerVisit, vaccineSchedule, dateFormStart, user.getUser(), sc);
			
			sc.commitTransaction();

			String editmessage="Child Followed up successfully. ";
			
			return new ModelAndView(new RedirectView("childDashboard.htm?action=search&editOrUpdateMessage="+editmessage+"&childId="+child.getIdMapper().getIdentifiers().get(0).getIdentifier()));
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
	
	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors) throws Exception 
	{
		VaccinationCenterVisit centerVisit = (VaccinationCenterVisit) command;
		VaccinationValidator validator = (VaccinationValidator) getValidator();
		List<VaccineSchedule> vaccineSchedule = (List<VaccineSchedule>) request.getSession().getAttribute(VaccinationCenterVisit.VACCINE_SCHEDULE_KEY+centerVisit.getUuid());
		validator.validateVaccinationForm(centerVisit, vaccineSchedule, errors, request);
		ServiceContext sc = Context.getServices();
		try{
			if(!StringUtils.isEmptyOrWhitespaceOnly("showcnic")){
				ValidatorUtils.validateChildNIC(DataEntrySource.WEB, centerVisit.getChildId(), request.getParameter("cnic"), false, null, errors, false, sc);
			}
		}
		finally{
			sc.closeSession();
		}
		return super.processFormSubmission(request, response, command, errors);
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		dateFormStart = new Date();
		
		String child_id=request.getParameter("child_id");//child-program-id
		VaccinationCenterVisit vcv = new VaccinationCenterVisit();
		Child child = new Child();
		boolean isIncentivized = false;
		Vaccination previousVaccination = new Vaccination();
		
		ServiceContext sc = Context.getServices();
		LotterySms prf = null;
		try{
			child = sc.getChildService().findChildById(Integer.parseInt(child_id), true, new String[]{"idMapper"});
//			isIncentivized = sc.getIncentiveService().findChildIncentiveByCriteria(child.getMappedId(), null, null, null, null, null, null, null, null, null, 0, 2, true, null).size()>0;
			ControllerUIHelper.prepareFollowupDisplayObjects(request, child, sc); //childfollowup
			previousVaccination = ControllerUIHelper.getPreviousVaccination(child.getMappedId(), sc);
//			prf = sc.getChildService().findLotterySmsByChild(child.getMappedId(), false, 0, 10, null).get(0);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "Oops .. Some error occurred. Exception message is:"+e.getMessage());
			request.setAttribute("shouldenableVaccination", false);
		}finally{
			sc.closeSession();
		}

//		vcv.setPreference(prf);
		System.out.println("\n\n"+previousVaccination.getVaccine().getName() +" --------- \n\n");
		vcv.setVaccinationCenterId(previousVaccination.getVaccinationCenterId());
		vcv.setVaccinatorId(previousVaccination.getVaccinatorId());
//		vcv.setEpiNumber(previousVaccination.getEpiNumber());
		vcv.setChildId(child.getMappedId());
		
//		if(StringUtils.isEmptyOrWhitespaceOnly(child.getNic())&&isIncentivized){
//		request.setAttribute("showcnic", true);
//		}

		return vcv;
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,	Errors errors) throws Exception 
	{
		Map<String, Object> model = new HashMap<String, Object>();
			ServiceContext sc = Context.getServices();
			try{
				VaccinationCenterVisit vacc = (VaccinationCenterVisit) command;
				
				ControllerUIHelper.prepareFollowupReferenceData(request, model, vacc, sc);
				
				ControllerUIHelper.prepareVaccinationReferenceData(request, model, 
						sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"}), 
						sc.getVaccinationService().getAllVaccinator(0, Integer.MAX_VALUE, true, new String[]{"idMapper"}));

				List<Vaccine> vaccinesL = sc.getCustomQueryService().getDataByHQL("FROM Vaccine where vaccine_entity like 'CHILD%' and isSupplementary = 0") ;
				model.put("vaccineList", vaccinesL);
								
//				model.put("cnic", request.getParameter("cnic"));
//				model.put("showcnic", request.getAttribute("showcnic")!=null?request.getAttribute("showcnic"):request.getParameter("showcnic"));
			}
			catch (Exception e) {
				e.printStackTrace();
				GlobalParams.FILELOGGER.error(formType.name(), e);
				request.setAttribute("errorMessagev", "An error occurred while retrieving reference data list. Error message is:"+e.getMessage());
			}
			finally{
				sc.closeSession();
			}

		
		return model;
	}
}
