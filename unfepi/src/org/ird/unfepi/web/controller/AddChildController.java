package org.ird.unfepi.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.ChildIncentivization;
import org.ird.unfepi.DataEntryFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.beans.EnrollmentWrapper;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.utils.VaccinationCenterVisit;
import org.ird.unfepi.web.utils.VaccineSchedule;
import org.ird.unfepi.web.validator.ChildValidator;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class AddChildController extends DataEntryFormController
{
	private static final FormType formType = FormType.ENROLLMENT_ADD;
	private Date dateFormStart = new Date();

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,	HttpServletResponse response, Object command, BindException errors)	throws Exception {

		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		EnrollmentWrapper ewr = (EnrollmentWrapper)command;
		
		String projectId = ewr.getProjectId();
		Child ch = ewr.getChild();
		VaccinationCenterVisit centerVisit = ewr.getCenterVisit();
		Address addr = ewr.getAddress();
		ServiceContext sc = Context.getServices();
		try{
			@SuppressWarnings("unchecked")
			List<VaccineSchedule> vaccineSchedule = (List<VaccineSchedule>) request.getSession().getAttribute(VaccinationCenterVisit.VACCINE_SCHEDULE_KEY+ewr.getCenterVisit().getUuid());

			@SuppressWarnings("unused")
			List<ChildIncentivization> lotteryRes = ControllerUIHelper.doEnrollment(DataEntrySource.WEB, projectId, ewr.getChildNamed(), ch, 
					ewr.getBirthdateOrAge(), ewr.getChildagey(), ewr.getChildagem(), ewr.getChildagew(), ewr.getChildaged(), 
					addr, centerVisit, ewr.getCompleteCourseFromCenter(), vaccineSchedule, dateFormStart, user.getUser(), sc);
			
			sc.commitTransaction();

			String editmessage="Child Enrolled successfully. ";//\n Lottery Runner information : ";
			/*for (ChildLotteryRunner childLotteryRunner : lotteryRes) 
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
			}*/
			
			return new ModelAndView(new RedirectView("childDashboard.htm?action=search&editOrUpdateMessage="+editmessage+"&childId="+projectId));
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
	protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors) throws Exception 
	{
		ChildValidator validator = (ChildValidator) getValidator();
		EnrollmentWrapper enrw = (EnrollmentWrapper) command;
		List<VaccineSchedule> vaccineSchedule = (List<VaccineSchedule>) request.getSession().getAttribute(VaccinationCenterVisit.VACCINE_SCHEDULE_KEY+enrw.getCenterVisit().getUuid());
		enrw.getChild().setDateEnrolled(enrw.getCenterVisit().getVisitDate());
		validator.validateEnrollment((EnrollmentWrapper) command, vaccineSchedule, errors);
		return super.processFormSubmission(request, response, command, errors);
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		dateFormStart = new Date();
		
		EnrollmentWrapper enw = new EnrollmentWrapper();
		return enw;
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,	Errors errors) throws Exception 
	{
		Map<String, Object> model = new HashMap<String, Object>();
		ServiceContext sc = Context.getServices();
		try{
			ControllerUIHelper.prepareVaccinationReferenceData(request, model, 
					sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"}), 
					sc.getVaccinationService().getAllVaccinator(0, Integer.MAX_VALUE, true, new String[]{"idMapper"}));
		}catch (Exception e) {
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
