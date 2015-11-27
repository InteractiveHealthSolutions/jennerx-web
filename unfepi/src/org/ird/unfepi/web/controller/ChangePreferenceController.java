package org.ird.unfepi.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.LotterySms;
import org.ird.unfepi.utils.EncounterUtil;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class ChangePreferenceController extends SimpleFormController
{
	private static final FormType formType = FormType.CHANGE_PREFERENCE;
	private Date dateFormStart = new Date();

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,	HttpServletResponse response, Object command, 
			BindException errors) throws Exception 
	{
		LoggedInUser user=UserSessionUtils.getActiveUser(request);

		LotterySms ls = (LotterySms) command;
		ServiceContext sc = Context.getServices();
		try{
			String reminderCellNumber = request.getParameter("reminderCellNumber");
			
	//TODO		ControllerUIHelper.doChangePreference(ls, reminderCellNumber, user.getUser(), sc);
			
			EncounterUtil.createChangePreferenceEncounter(ls, reminderCellNumber, DataEntrySource.WEB, dateFormStart, user.getUser(), sc);
			
			sc.commitTransaction();
			
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(ls), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));

			String editmessage="Preference Updated Successfully";
			return new ModelAndView(new RedirectView("viewChildren.htm?action=search&editOrUpdateMessage="+editmessage+"&childid="+request.getParameter("programId")));
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
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		LotterySms lsms = new LotterySms();
			String sid = request.getParameter("sid");
			ServiceContext sc = Context.getServices();
			try{
				LotterySms prfprev = sc.getChildService().findLotterySmsBySerialNumber(Integer.parseInt(sid), true, new String[]{"idMapper"});
				
				lsms.setHasApprovedLottery(prfprev.getHasApprovedLottery());
				lsms.setHasApprovedReminders(prfprev.getHasApprovedReminders());
				lsms.setMappedId(prfprev.getMappedId());
				lsms.setPreferredSmsTiming(prfprev.getPreferredSmsTiming());
				lsms.setReasonLotteryRejection(prfprev.getReasonLotteryRejection());
				lsms.setReasonRemindersRejection(prfprev.getReasonRemindersRejection());
			}catch (Exception e) {
				GlobalParams.FILELOGGER.error(formType.name(), e);
				request.setAttribute("errorMessage", "An error occurred while retrieving Preference history. Error message is:"+e.getMessage());
			}finally{
				sc.closeSession();
			}
		return lsms;
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,	Errors errors) throws Exception 
	{
		boolean session_expired=false;
		Map<String, Object> model=new HashMap<String, Object>();
		
			ServiceContext sc = Context.getServices();
			LotterySms lsms = (LotterySms) command;
			try{
				String programId = request.getParameter("programId");
				if(programId == null){
//TODO					programId = sc.getIdMapperService().findIdMapper(lsms.getMappedId()).getProgramId();
				}
				
				model.put("programId", programId);
				
				String cellnumber = request.getParameter("reminderCellNumber");
				if(cellnumber == null){
					ContactNumber pcont = sc.getDemographicDetailsService().getUniquePrimaryContactNumber(lsms.getMappedId(), true, null);
					cellnumber = pcont == null ? null : pcont.getNumber();
				}
				
				model.put("reminderCellNumber", cellnumber);
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
