/**
 * 
 */
package org.ird.unfepi.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEntryFormController;
import org.ird.unfepi.beans.EnrollmentWrapperWomen;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.VaccinationStatusDate;
import org.ird.unfepi.model.Women;
import org.ird.unfepi.model.WomenVaccination;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.utils.WomenVaccinationCenterVisit;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Safwan
 *
 */
public class AddWomenController extends DataEntryFormController {
	
	private static final FormType formType = FormType.WOMEN_ADD;
	private Date dateFormStart = new Date();

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,	HttpServletResponse response, Object command, BindException errors)	throws Exception {

		LoggedInUser user = UserSessionUtils.getActiveUser(request);
		EnrollmentWrapperWomen ewr = (EnrollmentWrapperWomen)command;
		
		Women women = ewr.getWomen();
		WomenVaccinationCenterVisit centerVisit = ewr.getCenterVisit();
		Address addr = ewr.getAddress();
		String projectId = ewr.getProjectId();
		ServiceContext sc = Context.getServices();
		//String vaccine = request.getParameter("shortName");
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date tt1Date = request.getParameter("tt1Date").isEmpty() ? null : formatter.parse(request.getParameter("tt1Date"));
		Date tt2Date = request.getParameter("tt2Date").isEmpty() ? null : formatter.parse(request.getParameter("tt2Date"));
		Date tt3Date = request.getParameter("tt3Date").isEmpty() ? null : formatter.parse(request.getParameter("tt3Date"));
		Date tt4Date = request.getParameter("tt4Date").isEmpty() ? null : formatter.parse(request.getParameter("tt4Date"));
		Date tt5Date = request.getParameter("tt5Date").isEmpty() ? null : formatter.parse(request.getParameter("tt5Date"));
		
		HashMap<String,VaccinationStatusDate> vaccines = new HashMap<String,VaccinationStatusDate>();
		vaccines.put("TT1", new VaccinationStatusDate(request.getParameter("tt1Status"), tt1Date));
		vaccines.put("TT2", new VaccinationStatusDate(request.getParameter("tt2Status"), tt2Date));
		vaccines.put("TT3", new VaccinationStatusDate(request.getParameter("tt3Status"), tt3Date));
		vaccines.put("TT4", new VaccinationStatusDate(request.getParameter("tt4Status"), tt4Date));
		vaccines.put("TT5", new VaccinationStatusDate(request.getParameter("tt5Status"), tt5Date));
		String enrollmentVaccine = "";
		Date enrollmentDate = null;
	
		for (String s : vaccines.keySet()) {
			if(vaccines.get(s).getName().equalsIgnoreCase(WomenVaccination.WOMEN_VACCINATION_STATUS.VACCINATED.toString()))
			{
				enrollmentVaccine = s;
				enrollmentDate = vaccines.get(s).getDate();
			}

		}
		ControllerUIHelper.doWomenEnrollment(DataEntrySource.WEB, projectId, women, 
				ewr.getBirthdateOrAge(), ewr.getwomenagey(), ewr.getwomenagem(), ewr.getwomenagew(), ewr.getwomenaged(), 
				addr, centerVisit, dateFormStart, user.getUser(), enrollmentVaccine, enrollmentDate, vaccines, sc);
		
		
		sc.commitTransaction();
		
		
		/*String projectId = ewr.getProjectId();
		Child ch = ewr.getChild();
		VaccinationCenterVisit centerVisit = ewr.getCenterVisit();
		Address addr = ewr.getAddress();
		ServiceContext sc = Context.getServices();
		try{
			List<VaccineSchedule> vaccineSchedule = (List<VaccineSchedule>) request.getSession().getAttribute(VaccinationCenterVisit.VACCINE_SCHEDULE_KEY+ewr.getCenterVisit().getUuid());

			/*List<ChildLotteryRunner> lotteryRes = *//*ControllerUIHelper.doEnrollment(DataEntrySource.WEB, projectId, ewr.getChildNamed(), ch, 
					ewr.getBirthdateOrAge(), ewr.getChildagey(), ewr.getChildagem(), ewr.getChildagew(), ewr.getChildaged(), 
					addr, centerVisit, ewr.getCompleteCourseFromCenter(), vaccineSchedule, dateFormStart, user.getUser(), sc);
			
			sc.commitTransaction();

			String editmessage="Child Enrolled successfully. ";*///\n Lottery Runner information : ";
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

			return new ModelAndView(new RedirectView("addWomen.htm"));
			//return new ModelAndView(new RedirectView("childDashboard.htm?action=search&editOrUpdateMessage="+editmessage+"&childId="+projectId));
		} 
		/*catch (Exception e) {
			sc.rollbackTransaction();
			
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}
		finally{
			sc.closeSession();
		}
	}*/
	
	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors) throws Exception 
	{
		/*ChildValidator validator = (ChildValidator) getValidator();
		EnrollmentWrapper enrw = (EnrollmentWrapper) command;
		List<VaccineSchedule> vaccineSchedule = (List<VaccineSchedule>) request.getSession().getAttribute(VaccinationCenterVisit.VACCINE_SCHEDULE_KEY+enrw.getCenterVisit().getUuid());
		enrw.getChild().setDateEnrolled(enrw.getCenterVisit().getVisitDate());
		validator.validateEnrollment((EnrollmentWrapper) command, vaccineSchedule, errors);*/
		EnrollmentWrapperWomen enrw = (EnrollmentWrapperWomen) command;
		enrw.getWomen().setDateEnrolled(enrw.getCenterVisit().getVisitDate());
		return super.processFormSubmission(request, response, command, errors);
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		dateFormStart = new Date();
		
		EnrollmentWrapperWomen enw = new EnrollmentWrapperWomen();
		//Women women = new Women();
		return enw;
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,	Errors errors) throws Exception 
	{
		Map<String, Object> model = new HashMap<String, Object>();
		ServiceContext sc = Context.getServices();
		/*try{
			ControllerUIHelper.prepareVaccinationReferenceData(request, model, 
					sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"}), 
					sc.getVaccinationService().getAllVaccinator(0, Integer.MAX_VALUE, true, new String[]{"idMapper"}));

			ControllerUIHelper.prepareEnrollmentReferenceData(request, model, (EnrollmentWrapper) command, sc);
			
		}catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessagev", "An error occurred while retrieving reference data list. Error message is:"+e.getMessage());
		}
		finally{
			sc.closeSession();
		}*/

		
		return model;
	}

}
