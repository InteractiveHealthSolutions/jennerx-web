/**
 * 
 */
package org.ird.unfepi.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import org.ird.unfepi.model.WomenVaccination.WOMEN_VACCINATION_STATUS;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.utils.WomenVaccinationCenterVisit;
import org.ird.unfepi.web.validator.AddWomenValidator;
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

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,	HttpServletResponse response, Object command, BindException errors)	throws Exception {

		LoggedInUser user = UserSessionUtils.getActiveUser(request);
		EnrollmentWrapperWomen ewr = (EnrollmentWrapperWomen)command;
		
		Women women = ewr.getWomen();
		WomenVaccinationCenterVisit centerVisit = ewr.getCenterVisit();
		Address addr = ewr.getAddress();
		String projectId = ewr.getProjectId();
		ServiceContext sc = Context.getServices();
		List<WomenVaccination> vaccines = new ArrayList<WomenVaccination>();

		String enrollmentVaccine = "";
		Date enrollmentDate = null;
		if(centerVisit.getTt1().getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.VACCINATED)){
			enrollmentVaccine = "tt1";
			enrollmentDate = centerVisit.getTt1().getVaccinationDate();
		} else if (centerVisit.getTt2().getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.VACCINATED)){
			enrollmentVaccine = "tt2";
			enrollmentDate = centerVisit.getTt2().getVaccinationDate();
		} else if (centerVisit.getTt3().getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.VACCINATED)){
			enrollmentVaccine = "tt3";
			enrollmentDate = centerVisit.getTt3().getVaccinationDate();
		} else if (centerVisit.getTt4().getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.VACCINATED)){
			enrollmentVaccine = "tt4";
			enrollmentDate = centerVisit.getTt4().getVaccinationDate();
		} else if (centerVisit.getTt5().getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.VACCINATED)){
			enrollmentVaccine = "tt5";
			enrollmentDate = centerVisit.getTt5().getVaccinationDate();
		}
		
		vaccines.add(centerVisit.getTt1());
		vaccines.add(centerVisit.getTt2());
		vaccines.add(centerVisit.getTt3());
		vaccines.add(centerVisit.getTt4());
		vaccines.add(centerVisit.getTt5());
		
		for(int i = 0; i < vaccines.size(); i++){
			vaccines.get(i).setVaccineId(sc.getVaccinationService().getByName("TT" + (i+1)).getVaccineId());
		}
		
		
		ControllerUIHelper.doWomenEnrollment(DataEntrySource.WEB, projectId, women, 
				ewr.getBirthdateOrAge(), ewr.getwomenagey(), ewr.getwomenagem(), ewr.getwomenagew(), ewr.getwomenaged(), 
				addr, centerVisit, dateFormStart, user.getUser(), enrollmentVaccine, enrollmentDate, vaccines, sc);
		
		
		sc.commitTransaction();
		
		
		
			return new ModelAndView(new RedirectView("addWomen.htm"));
			//return new ModelAndView(new RedirectView("childDashboard.htm?action=search&editOrUpdateMessage="+editmessage+"&childId="+projectId));
		} 
		
	
	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors) throws Exception 
	{
		AddWomenValidator validator = (AddWomenValidator) getValidator();
		EnrollmentWrapperWomen enrw = (EnrollmentWrapperWomen) command;
		//List<WomenVaccination> womenVaccination = (List<WomenVaccination>) request.getSession().getAttribute(WomenVaccinationCenterVisit.WOMEN_VACCINE_SCHEDULE_KEY + enrw.getCenterVisit().getUuid());
		enrw.getWomen().setDateEnrolled(enrw.getCenterVisit().getVisitDate());
		validator.validateEnrollment((EnrollmentWrapperWomen) command, errors);
		return super.processFormSubmission(request, response, command, errors);
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		dateFormStart = new Date();
		
		EnrollmentWrapperWomen enw = new EnrollmentWrapperWomen();
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

			ControllerUIHelper.prepareWomenEnrollmentReferenceData(request, model, (EnrollmentWrapperWomen) command, sc);
			
		}catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessagev", "An error occurred while retrieving reference data list. Error message is:"+e.getMessage());
		}
		finally{
			sc.closeSession();
		}

		
		return model;
	}

}
