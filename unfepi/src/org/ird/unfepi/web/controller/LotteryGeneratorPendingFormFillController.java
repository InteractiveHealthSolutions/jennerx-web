package org.ird.unfepi.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.GlobalParams.LotteryType;
import org.ird.unfepi.beans.EnrollmentWrapper;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Encounter;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.EncounterResults;
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.model.LotterySms;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.utils.EncounterUtil;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.validator.ChildValidator;
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

public class LotteryGeneratorPendingFormFillController extends SimpleFormController 
{
	private static final FormType formType = FormType.LOTTERY_GENERATOR_FORM_FILL;
	private Date dateFormStart = new Date();

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)	throws Exception 
	{
		/*LoggedInUser user=UserSessionUtils.getActiveUser(request);
		
		Encounter encounter = null;
		List<EncounterResults> encreslist = null;
		Integer uniqueID = null;
		
		if(command instanceof Vaccination){
			uniqueID = ((Vaccination)command).getChildId();
		}
		else if(command instanceof EnrollmentWrapper){
			uniqueID = ((EnrollmentWrapper)command).getChild().getMappedId();
		}
		
		encounter = (Encounter) request.getSession().getAttribute("encounter"+uniqueID);
		encreslist = (List<EncounterResults>) request.getSession().getAttribute("encounterres"+uniqueID);
		
		ServiceContext sc = Context.getServices();
		try{
			if(EncounterUtil.getElementValueFromEncResult("LOTTERY_TYPE", encreslist).equalsIgnoreCase(LotteryType.PENDING_FOLLOWUP.toString()))
			{
				Vaccination curVaccination = (Vaccination) command;
				Vaccine curVaccine = sc.getVaccinationService().findVaccineById(curVaccination.getVaccineId());
				Vaccine nextVaccine = sc.getVaccinationService().getByName(request.getParameter("nextVaccine"));
				Vaccination nextVaccination = new Vaccination();
				Boolean hasApprovedReminders = Boolean.valueOf(request.getParameter("reminderPreference"));
				String reminderCellNumber = request.getParameter("reminderCellNumber");
				Child child = (Child) request.getSession().getAttribute("child"+curVaccination.getVaccinationRecordNum());

				ControllerUIHelper.doFollowup(child, curVaccination, nextVaccine, nextVaccination, hasApprovedReminders, reminderCellNumber, user.getUser(), sc);

				encounter.setDetail("FILLED");
				sc.getCustomQueryService().update(encounter);
				EncounterResults encres = EncounterUtil.getEncResult("FORM_STATUS", encreslist);
				encres.setValue("FILLED");
				sc.getCustomQueryService().update(encres);
				
				GlobalParams.DBLOGGER.info(IRUtils.convertToString(curVaccination), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, formType, user.getUser().getUsername()));
				if(nextVaccine != null)
					GlobalParams.DBLOGGER.info(IRUtils.convertToString(nextVaccination), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, formType, user.getUser().getUsername()));

				EncounterUtil.createFollowupEncounter(curVaccination, curVaccine, (nextVaccine==null?null:nextVaccine.getName()), 
						request.getParameter("systemCalculatedDate"), hasApprovedReminders, reminderCellNumber, null, null, 
						DataEntrySource.WEB, dateFormStart, user.getUser(), sc);
				
				sc.commitTransaction();
				
				String editmessage="Child Followed-up successfully and lottery was done through Lottery-Generator";
				
				return new ModelAndView(new RedirectView("viewVaccinationRecord.htm?action=search&editOrUpdateMessage="+editmessage+"&childIdName="+child.getIdMapper().getIdentifiers().get(0).getIdentifier()));
			}
			else if(EncounterUtil.getElementValueFromEncResult("LOTTERY_TYPE", encreslist).equalsIgnoreCase(LotteryType.MISSING_ENROLLMENT.toString()))
			{
				EnrollmentWrapper ewr = (EnrollmentWrapper)command;
				
				IdMapper idm = ewr.getIdMapper();
				Child ch = ewr.getChild();
				Vaccination currVaccn = ewr.getCurrentVaccination();
				Vaccination nextVaccn = new Vaccination();
				LotterySms ls = ewr.getPreference();
				ContactNumber primCont = ewr.getContactNumber1();
				ContactNumber secCont = ewr.getContactNumber2();
				Address addr = ewr.getAddress();
				
				Vaccine currVaccine = sc.getVaccinationService().findVaccineById(currVaccn.getVaccineId());
				Vaccine nextVaccine = request.getParameter("nextVaccine")==null?null:sc.getVaccinationService().getByName(request.getParameter("nextVaccine"));
				
				ControllerUIHelper.doEnrollmentLotteryGen(ch, currVaccn, nextVaccine, nextVaccn, ls, primCont, secCont, addr, user.getUser(), sc);
					
				encounter.setDetail("FILLED");
				sc.getCustomQueryService().update(encounter);
				EncounterResults encres = EncounterUtil.getEncResult("FORM_STATUS", encreslist);
				encres.setValue("FILLED");
				sc.getCustomQueryService().update(encres);
				
				EncounterUtil.createEnrollmentEncounter(idm, currVaccn, ch, addr, currVaccine, (nextVaccine==null?null:nextVaccine.getName()), 
						request.getParameter("systemCalculatedDate"), ls.getHasApprovedReminders(), 
						primCont.getNumber(), secCont.getTelelineType(), secCont.getNumber(), 
						null, DataEntrySource.WEB, dateFormStart, user.getUser(), sc);
				
				sc.commitTransaction();

				GlobalParams.DBLOGGER.info(IRUtils.convertToString(ch), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));
				GlobalParams.DBLOGGER.info(IRUtils.convertToString(currVaccn), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));
				if(nextVaccine != null)
					GlobalParams.DBLOGGER.info(IRUtils.convertToString(nextVaccn), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));
				GlobalParams.DBLOGGER.info(IRUtils.convertToString(ls), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));
				GlobalParams.DBLOGGER.info(IRUtils.convertToString(addr), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));
				GlobalParams.DBLOGGER.info(IRUtils.convertToString(primCont), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));
				GlobalParams.DBLOGGER.info(IRUtils.convertToString(secCont), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));

				String editmessage="Child enrolled successfully and lottery was done through Lottery-Generator";
				
				return new ModelAndView(new RedirectView("viewChildren.htm?action=search&editOrUpdateMessage="+editmessage+"&childid="+idm.getProgramId()));
			}
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
		}*/
		return null;
	}
	
	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors) throws Exception 
	{
		if(command instanceof Vaccination){
			VaccinationValidator validator = (VaccinationValidator) getValidator();
			validator.validateVaccinationFillHalfForm(command, errors, request);
		}
		else if(command instanceof EnrollmentWrapper){
			ChildValidator validator = (ChildValidator) getValidator();
//TODO			validator.validateEnrollment((EnrollmentWrapper) command, request.getParameter("nextVaccine"), errors);
		}
		
		return super.processFormSubmission(request, response, command, errors);
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		dateFormStart = new Date();

		Integer encid=Integer.parseInt(request.getParameter("encid"));
		String p1=request.getParameter("p1");
		String p2=request.getParameter("p2");
		
		boolean enable = true;
		String errorMessage = "";
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		
		if(user==null){
			boolean session_expired = true;
			request.setAttribute("session_expired", session_expired);
		}
		else{
			ServiceContext sc = Context.getServices();
			try{
				Encounter encounter = sc.getEncounterService().findEncounterByCriteria(encid, p1, p2, null, null, null, null, false, 0, 1, null).get(0);
				List<EncounterResults> encreslist = sc.getCustomQueryService().getDataByHQL("from EncounterResults where id.encounterId="+encounter.getId().getEncounterId()+" and id.p1id="+encounter.getId().getP1id()+" and id.p2id="+encounter.getId().getP2id());

				if(encounter.getDetail() == null 
						|| !encounter.getDetail().toLowerCase().contains("pending")
						|| !EncounterUtil.getElementValueFromEncResult("FORM_STATUS", encreslist).toLowerCase().contains("pending"))
				{
					errorMessage +=	"Note: Form was not found PENDING, have it been filled or is Not Applicable? Conatact program vendor if flow is unexpected!";
					enable = false;
				}

				request.getSession().setAttribute("encounter"+encounter.getId().getP1id(), encounter);
				request.getSession().setAttribute("encounterres"+encounter.getId().getP1id(), encreslist);
				
				int recordId = Integer.parseInt(EncounterUtil.getElementValueFromEncResult("VACCINATION_RECORD_NUM", encreslist));
				Vaccination vacc = sc.getVaccinationService().getVaccinationRecord(recordId , false, new String[]{"vaccine"}, null);

				if(vacc == null) 
				{
					errorMessage +=	"No vaccination record found with given id.";
					enable = false;
				}
				else if(!vacc.getVaccinationStatus().equals(VACCINATION_STATUS.UNFILLED)){
					errorMessage +=	"Vaccination record status was not found UNFILLED.";
					enable = false;
				}
				
				// FOLLOW UP
				if(EncounterUtil.getElementValueFromEncResult("LOTTERY_TYPE", encreslist).equalsIgnoreCase(LotteryType.PENDING_FOLLOWUP.toString()))
				{
					try{
						if(vacc != null) {
//TODO							ControllerUIHelper.prepareFollowupDisplayObjects(request, vacc, sc);	
						}
					}
					catch (Exception e) {
						e.printStackTrace();
						GlobalParams.FILELOGGER.error(formType.name(), e);
						
						errorMessage +=	"Oops .. Some error occurred. Exception message is:"+e.getMessage()+".";
						enable = false;
					}
					
					setValidator(new VaccinationValidator());
					setCommandClass(Vaccination.class);
					setFormView("followupVaccination");
					
					return vacc==null?new Vaccination():vacc;
				}
				else if(EncounterUtil.getElementValueFromEncResult("LOTTERY_TYPE", encreslist).equalsIgnoreCase(LotteryType.MISSING_ENROLLMENT.toString()))
				{
					EnrollmentWrapper enw = new EnrollmentWrapper();
					enw.setChild(sc.getChildService().findChildById(encounter.getId().getP1id(), false, new String[]{"idMapper"}));
					
	//TODO				enw.setCurrentVaccination(vacc);
					sc.closeSession();
					
					enw.getChild().setDateEnrolled(encounter.getDateEncounterEntered());

					setValidator(new ChildValidator());
					setCommandClass(EnrollmentWrapper.class);
					setFormView("addChild");
					
					return enw;
				}
			}catch (Exception e) {
				e.printStackTrace();
				GlobalParams.FILELOGGER.error(formType.name(), e);
				
				errorMessage +=	"Oops .. Some error occurred. Exception message is:"+e.getMessage()+".";
				enable = false;
			}finally{
				sc.closeSession();
			}
		}
		
		request.setAttribute("shouldenableVaccination", enable);
		request.setAttribute("errorMessage", errorMessage);
		
		setCommandClass(Object.class);
		setFormView("addChild");
		return new Object();
	}
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA), true));
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA), true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Short.class, new CustomNumberEditor(Short.class, true));
		binder.registerCustomEditor(Float.class, new CustomNumberEditor(Float.class, true));
		binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor(WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING, WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING, true));
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,	Errors errors) throws Exception 
	{
		boolean session_expired=false;
		Map<String, Object> model=new HashMap<String, Object>();
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			session_expired=true;
			request.setAttribute("session_expired", session_expired);
		}else{
			ServiceContext sc = Context.getServices();
			try{
				Integer uniqueID = null;
				
				if(command instanceof Vaccination){
					uniqueID = ((Vaccination)command).getChildId();
				}
				else if(command instanceof EnrollmentWrapper){
					uniqueID = ((EnrollmentWrapper)command).getChild().getMappedId();
				}
				
				Encounter encounter = (Encounter) request.getSession().getAttribute("encounter"+uniqueID);
				List<EncounterResults> encreslist = (List<EncounterResults>) request.getSession().getAttribute("encounterres"+uniqueID);

				ArrayList<VaccinationCenter> centeres = new ArrayList<VaccinationCenter>();
				centeres.add(sc.getVaccinationService().findVaccinationCenterById(Integer.parseInt(encounter.getLocationId()), true, new String[]{"idMapper"}));
				
				ArrayList<Vaccinator> vtors = new ArrayList<Vaccinator>();
				vtors.add(sc.getVaccinationService().findVaccinatorById(encounter.getId().getP2id(), true, new String[]{"idMapper"}));
				
				ControllerUIHelper.prepareVaccinationReferenceData(request, model, centeres, vtors);
				
				if(command instanceof Vaccination)
				{
					Vaccination vacc = (Vaccination) command;
					model.put("vaccine", vacc.getVaccine());
					
	//TODO				ControllerUIHelper.prepareFollowupReferenceData(request, model, vacc, sc);
				}
				else if(command instanceof EnrollmentWrapper)
				{
					EnrollmentWrapper enw = (EnrollmentWrapper) command;

					List<Vaccine> vaclToShow = new ArrayList<Vaccine>();
					vaclToShow.add(sc.getVaccinationService().findVaccineById(Short.parseShort(EncounterUtil.getElementValueFromEncResult("VACCINE_ID_RECEIVED", encreslist))));
					
					model.put("vaccines", vaclToShow);

					ControllerUIHelper.prepareEnrollmentReferenceData(request, model, enw, sc);
				}
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
