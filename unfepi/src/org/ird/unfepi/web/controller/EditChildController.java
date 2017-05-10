package org.ird.unfepi.web.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEditForm;
import org.ird.unfepi.DataEditFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.beans.ChildDataBean;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Child.STATUS;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.Model.ContactType;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.validator.ValidatorUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

@Controller
@SessionAttributes("command")
@RequestMapping("/editchild")
public class EditChildController extends DataEditFormController {
	
	public EditChildController() {
		super(new DataEditForm("child", "Child (Edit)", SystemPermissions.CORRECT_CHILDREN_DATA));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView editChildView(HttpServletRequest request, ModelAndView modelAndView) {
		modelAndView.addObject("command", formBackingObject(request));
		return showForm(modelAndView, "dataForm");
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")ChildDataBean cb, BindingResult results, 
								 HttpServletRequest request, HttpServletResponse response, 
								 ModelAndView modelAndView) throws Exception {

		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		ServiceContext sc = Context.getServices();
		
		try {
			String editSection = request.getParameter("editSection");
			if (editSection.equalsIgnoreCase("biographic")) {
				ValidatorUtils.validateBiographics(DataEntrySource.WEB, cb.getChildNamed(), cb.getChild(), false, cb.getBirthdateOrAge(), cb.getChildagey(), cb.getChildagem(), cb.getChildagew(), cb.getChildaged(), null, results, sc, true);
			
			} else if (editSection.equalsIgnoreCase("address")) {
				ValidatorUtils.validateAddress(DataEntrySource.WEB, cb.getAddress(), null, results, true);
			
			} else if (editSection.equalsIgnoreCase("program")) {
//				ValidatorUtils.validateChildNIC(DataEntrySource.WEB, cb.getChild().getMappedId(), cb.getChild().getNic(), false, null, results, true, sc);
				ValidatorUtils.validateChildStatus(DataEntrySource.WEB, cb.getChild(), null, results, true);
				ValidatorUtils.validateReminderAndContactInfo(DataEntrySource.WEB, cb.getPreference(), cb.getContactPrimary(), cb.getContactSecondary(), null, results, sc, false);
			
			} else if (editSection.equalsIgnoreCase("vaccination")) {
				for (Vaccination v : cb.getVaccinations()) {
					ValidatorUtils.validateVaccination(DataEntrySource.WEB, cb.getChild(), v, null, results, sc, true);
				}
			}
		
		} finally {
//			sc.closeSession();
		}
		
		if(results.hasErrors()){
			return showForm(modelAndView, "dataForm");
		}

		String editSection = request.getParameter("editSection");
		try {
			if (!StringUtils.isEmptyOrWhitespaceOnly(editSection)) {
				if (editSection.equalsIgnoreCase("biographic")) {
					Child child= cb.getChild();
					
					if (StringUtils.isEmptyOrWhitespaceOnly(child.getFirstName())) {
						child.setFirstName("NO NAME");
					}
					child.setEditor(user.getUser());
					sc.getChildService().updateChild(child);
					GlobalParams.DBLOGGER.info(IRUtils.convertToString(child), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, FormType.CHILD_BIOGRAPHIC_CORRECT, user.getUser().getUsername()));
				
				} else if (editSection.equalsIgnoreCase("address")) {
					Address add = cb.getAddress();
					add.setEditor(user.getUser());
					sc.getDemographicDetailsService().updateAddress(add);
					
					GlobalParams.DBLOGGER.info(IRUtils.convertToString(add), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, FormType.ADDRESS_CORRECT, user.getUser().getUsername()));
				
				} else if (editSection.equalsIgnoreCase("program")) {
					
					Child child = cb.getChild();
					STATUS uneditedChildStatus = (STATUS) request.getSession().getAttribute("uneditedChildStatus"+child.getMappedId());
					child.setEditor(user.getUser());

					if (uneditedChildStatus.name().equalsIgnoreCase(STATUS.FOLLOW_UP.name())
							&& child.getStatus().name().equalsIgnoreCase(STATUS.TERMINATED.name())) {

						
						GlobalParams.DBLOGGER.info(IRUtils.convertToString(child), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, FormType.CHILD_BIOGRAPHIC_CORRECT, user.getUser().getUsername()));
					}
					
					sc.getChildService().updateChild(child);
					
					ControllerUIHelper.handleNonEnrollmentContactInfo(cb.getPreference(), cb.getContactPrimary(), cb.getContactSecondary(), user.getUser(), sc);
					GlobalParams.DBLOGGER.info(IRUtils.convertToString(cb), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, FormType.CONTACT_NUMBER_CORRECT, user.getUser().getUsername()));
				
				} else if (editSection.equalsIgnoreCase("vaccination")) {
					
					for (Vaccination ved : cb.getVaccinations()) {
						ved.setEditor(user.getUser());
						sc.getVaccinationService().updateVaccinationRecord(ved);
						
						GlobalParams.DBLOGGER.info(IRUtils.convertToString(ved), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, FormType.FOLLOWUP_CORRECT, user.getUser().getUsername()));
					}
				}
				sc.commitTransaction();
			}
			String editmessage="Child Edited Successfully";
			return new ModelAndView(new RedirectView("childDashboard.htm?action=search&editOrUpdateMessage="+editmessage+"&childId="+request.getParameter("programId")));
			
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView("exception");
		
		} finally {
			sc.closeSession();
		}
	}
	
	protected ChildDataBean formBackingObject(HttpServletRequest request) {
		String programId=request.getParameter("programId");
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		ChildDataBean ew = new ChildDataBean();
		if (user == null) {
			boolean session_expired = true;
			request.setAttribute("session_expired", session_expired);
		
		} else {
			ServiceContext sc = Context.getServices();
			Child p;
			try {
				p = sc.getChildService().findChildById(programId, false, new String[]{"idMapper", "enrollmentVaccine"});
				if (p == null) {
					request.setAttribute("errorMessage", "Oops .. Some error occurred. Child was not found.");
				}
				ew.setChild(p);
				List<Address> addl = sc.getDemographicDetailsService().getAddress(p.getMappedId(), false, null);
				ew.setAddress(addl.size()>0?addl.get(0):new Address());
//				ew.setPreference(sc.getChildService().findLotterySmsByChild(p.getMappedId(), false, 0, 10, null).get(0));
				List<ContactNumber> conl = sc.getDemographicDetailsService().getContactNumber(p.getMappedId(), true, null);
				for (ContactNumber cn : conl) {
					if (cn.getNumberType().name().equalsIgnoreCase(ContactType.PRIMARY.name())) {
						ew.setContactPrimary(cn.getNumber());
					
					} else if (cn.getNumberType().name().equalsIgnoreCase(ContactType.SECONDARY.name())) {
						ew.setContactSecondary(cn.getNumber());
					}
				}
				List<Vaccination> vl = sc.getVaccinationService().findByCriteria(p.getMappedId(), null, null, 0, 20, false, new String[]{"vaccine"});
				Collections.reverse(vl);
				ew.setVaccinations(vl);
			
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("errorMessage", "Oops .. Some exception was thrown. Error message is:"+e.getMessage());
			
			} finally {
				sc.closeSession();
			}
		
			request.getSession().setAttribute("uneditedChildStatus"+ew.getChild().getMappedId(), ew.getChild().getStatus());
			request.getSession().setAttribute("uneditedChildReminderPreference"+ew.getChild().getMappedId(), ew.getPreference().getHasApprovedReminders());
			request.getSession().setAttribute("uneditedChildIncentivePreference"+ew.getChild().getMappedId(), ew.getPreference().getHasApprovedLottery());	
		}
		return ew;
	}
	
	@ModelAttribute
	protected void referenceData(HttpServletRequest request, Object command, Errors errors, Model model) throws Exception {
		ServiceContext sc = Context.getServices();
		try {
			model.addAttribute("editSection", request.getParameter("editSection"));
			model.addAttribute("vaccinationCenters", sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"}));
			model.addAttribute("vaccinators", sc.getVaccinationService().getAllVaccinator(0, 100, true, new String[]{"idMapper"}));
			
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessagev", "An error occurred while retrieving reference data list. Error message is:"+e.getMessage());
		
		} finally {
			sc.closeSession();
		}
	}

}
