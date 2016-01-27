/**
 * 
 */
package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Women;
import org.ird.unfepi.model.WomenVaccination;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.utils.VaccinationCenterVisit;
import org.ird.unfepi.web.utils.VaccineSchedule;
import org.ird.unfepi.web.utils.WomenVaccinationCenterVisit;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Safwan
 *
 */
public class FollowupWomenVaccinationController extends DataEntryFormController{
	
	private static final FormType formType = FormType.FOLLOWUP_ADD;
	private Date dateFormStart = new Date();
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception
	{
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		
		ServiceContext sc = Context.getServices();
		try{
			WomenVaccinationCenterVisit centerVisit = (WomenVaccinationCenterVisit) command;
			Women women = (Women) request.getSession().getAttribute("womenfollowup");
			List<WomenVaccination> vaccines = sc.getWomenVaccinationService().findByWomenId(women.getMappedId());
			ControllerUIHelper.doWomenFollowup(DataEntrySource.WEB, centerVisit, vaccines,  dateFormStart, women, centerVisit.getAddress(), user.getUser(), sc);
			
			sc.commitTransaction();

			String editmessage="Women Enrolled successfully. ";
			
			//return new ModelAndView(new RedirectView("childDashboard.htm?action=search&editOrUpdateMessage="+editmessage+"&childId="+child.getIdMapper().getIdentifiers().get(0).getIdentifier()));
			return new ModelAndView(new RedirectView("viewWomen.htm"));
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
		WomenVaccinationCenterVisit centerVisit = (WomenVaccinationCenterVisit) command;
		List<WomenVaccination> vaccineSchedule = (List<WomenVaccination>) request.getSession().getAttribute(WomenVaccinationCenterVisit.WOMEN_VACCINE_SCHEDULE_KEY+centerVisit.getUuid());
		return super.processFormSubmission(request, response, command, errors);
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		dateFormStart = new Date();
		
		
		String women_id = request.getParameter("women_id");
		WomenVaccinationCenterVisit vcv = new WomenVaccinationCenterVisit();
		Women women = new Women();
		WomenVaccination previousVaccination = new WomenVaccination();
		int womenId;
		List<WomenVaccination> vaccinationList = new ArrayList<WomenVaccination>();
		Address address = null;
		
		ServiceContext sc = Context.getServices();
		try {
			womenId = Integer.parseInt(women_id);
			women = sc.getWomenService().findById(womenId);
			ControllerUIHelper.prepareWomenFollowupDisplayObjects(request, women, sc);
			previousVaccination = ControllerUIHelper.getPreviousWomenVaccination(womenId, sc);
			address = sc.getDemographicDetailsService().getAddress(women.getMappedId(), false, new String[] {"idMapper"}).get(0);
			vaccinationList = sc.getWomenVaccinationService().findByWomenId(womenId);
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "Oops .. Some error occurred. Exception message is:"+e.getMessage());
			request.setAttribute("shouldenableVaccination", false);
		} finally {
			sc.closeSession();
		}
		
		vcv.setWomenId(women.getMappedId());
		vcv.setVaccinationCenterId(previousVaccination.getVaccinationCenterId());
		vcv.setVaccinatorId(previousVaccination.getVaccinatorId());
		vcv.setAddress(address);
		
		for( int i = 0; i < vaccinationList.size(); i++){
			if(i == 0)
				vcv.setTt1(vaccinationList.get(0));
			if(i == 1)
				vcv.setTt2(vaccinationList.get(1));
			if(i == 2)
				vcv.setTt3(vaccinationList.get(2));
			if(i == 3)
				vcv.setTt4(vaccinationList.get(3));
			if(i == 4)
				vcv.setTt5(vaccinationList.get(4));
		}
		
		return vcv;
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,	Errors errors) throws Exception 
	{
		Map<String, Object> model = new HashMap<String, Object>();
			ServiceContext sc = Context.getServices();
			try{
				WomenVaccinationCenterVisit vacc = (WomenVaccinationCenterVisit) command;
				
				ControllerUIHelper.prepareWomenFollowupReferenceData(request, model, vacc, sc);
				
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

		
		return model;
	}

}
