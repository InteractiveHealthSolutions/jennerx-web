package org.ird.unfepi.web.controller;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.ird.unfepi.ChildIncentivization;
import org.ird.unfepi.DataEntryForm;
import org.ird.unfepi.DataEntryFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.beans.EnrollmentWrapper;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.utils.VaccinationCenterVisit;
import org.ird.unfepi.web.utils.VaccineSchedule;
import org.ird.unfepi.web.utils.VaccineSchedule.VaccineStatusType;
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
@RequestMapping("/addchild")
@SessionAttributes("command")
public class AddChildController extends DataEntryFormController{
	
	private static final FormType formType = FormType.ENROLLMENT_ADD;
	private Date dateFormStart = new Date();
	
	AddChildController(){
		super(new DataEntryForm("enrollment", "Enrollment (New)", SystemPermissions.ADD_CHILDREN_DATA));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView addChildFormView(HttpServletRequest request, ModelAndView modelAndView){
		modelAndView.addObject("command", new EnrollmentWrapper());
		return showForm(modelAndView, "dataForm");		
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")EnrollmentWrapper ewr, BindingResult results,
								 HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView,
								 HttpSession session) throws Exception {
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		String childIdentifier = ewr.getChildIdentifier();
		Child ch = ewr.getChild();
		VaccinationCenterVisit centerVisit = ewr.getCenterVisit();
		Address addr = ewr.getAddress();
		ServiceContext sc = Context.getServices();
		try{
			@SuppressWarnings("unchecked")
			List<VaccineSchedule> vaccineSchedule = (List<VaccineSchedule>) request.getSession().getAttribute(VaccinationCenterVisit.VACCINE_SCHEDULE_KEY+ewr.getCenterVisit().getUuid());
			
			Iterator<VaccineSchedule> iter = vaccineSchedule.iterator();
			while (iter.hasNext()) {
				VaccineSchedule vsh = iter.next();
				if (vsh.getStatus().equals(VaccineStatusType.SCHEDULED.name()) || vsh.getStatus().equals(VaccineStatusType.NOT_ALLOWED.name()) || (vsh.getStatus() == null?true:vsh.getStatus().length()==0)) {
					iter.remove();
				}
			}
			@SuppressWarnings("unused")
			List<ChildIncentivization> lotteryRes = ControllerUIHelper.doEnrollment(DataEntrySource.WEB, childIdentifier, ewr.getChildNamed(), ch, 
					ewr.getBirthdateOrAge(), ewr.getChildagey(), ewr.getChildagem(), ewr.getChildagew(), ewr.getChildaged(), 
					addr, centerVisit, ewr.getCompleteCourseFromCenter(), vaccineSchedule, dateFormStart, user.getUser(), sc);
			
			sc.commitTransaction();

			String editmessage="Child Enrolled successfully. ";
			
			return new ModelAndView(new RedirectView("childDashboard.htm?action=search&editOrUpdateMessage="+editmessage+"&childId="+childIdentifier));
			
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

	@ModelAttribute
	protected void referenceData(HttpServletRequest request, Model model) throws Exception 
	{
		ServiceContext sc = Context.getServices();
		try {
			List<VaccinationCenter> centeres = sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"});
			List<Vaccinator> vaccinators = sc.getVaccinationService().getAllVaccinator(0, Integer.MAX_VALUE, true, new String[]{"idMapper"});
			model.addAttribute("vaccinationCenters", centeres);
			model.addAttribute("vaccinators", vaccinators);	
			List<Vaccine> vaccinesL = sc.getCustomQueryService().getDataByHQL("FROM Vaccine where vaccine_entity like 'CHILD%' and isSupplementary = 0") ;
			model.addAttribute("vaccineList", vaccinesL);
			
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessagev", "An error occurred while retrieving reference data list. Error message is:"+e.getMessage());
		
		} finally {
			sc.closeSession();
		}	
	}
}