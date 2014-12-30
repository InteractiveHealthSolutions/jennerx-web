package org.ird.unfepi.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEntryFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.utils.EncounterUtil;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class AddContactNumberController extends DataEntryFormController
{
	private static final FormType formType = FormType.CONTACT_NUMBER_ADD;
	private Date dateFormStart = new Date();

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception 
	{
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		
		ServiceContext sc = Context.getServices();
		ContactNumber con = (ContactNumber) command;
		
		try{
			IdMapper entity = sc.getIdMapperService().findIdMapper(con.getMappedId());
			
			con.setCreator(user.getUser());
			sc.getDemographicDetailsService().saveContactNumber(con);
			
			EncounterUtil.createContactNumberEncounter(entity, con, DataEntrySource.WEB, dateFormStart, user.getUser(), sc);
			
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(con), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));

			sc.commitTransaction();

			String editmessage = "updated successfully";

			String entityRole = request.getParameter("entityRole");

			if(entityRole.equalsIgnoreCase("child")){
				return new ModelAndView(new RedirectView("viewChildren.htm?action=search&editOrUpdateMessage="+editmessage +"&childid="+request.getParameter("programId")));
			}
			else if(entityRole.equalsIgnoreCase("storekeeper")){
				return new ModelAndView(new RedirectView("viewStorekeepers.htm?action=search&editOrUpdateMessage="+editmessage +"&storekeeperid="+request.getParameter("programId")));
			}
			else if(entityRole.equalsIgnoreCase("vaccinator")){
				return new ModelAndView(new RedirectView("viewVaccinators.htm?action=search&editOrUpdateMessage="+editmessage +"&vaccinatorid="+request.getParameter("programId")));
			}
			
			return new ModelAndView(new RedirectView("viewChildren.htm?action=search&editOrUpdateMessage="+editmessage +"&childid="+request.getParameter("programId")));
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
	protected Object formBackingObject(HttpServletRequest request) throws Exception 
	{
		dateFormStart = new Date();
		ContactNumber con = new ContactNumber();
		
		ServiceContext sc = Context.getServices();
		try{
			String programId = request.getParameter("programId");
			IdMapper idm = sc.getIdMapperService().findIdMapper(programId);
			con.setMappedId(idm.getMappedId());
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessagev", "An error occurred while reference data list. Error message is:"+e.getMessage());
		}
		finally{
			sc.closeSession();
		}
		
		return con;
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,	Errors errors) throws Exception 
	{
		Map<String, Object> model=new HashMap<String, Object>();
		
		ServiceContext sc = Context.getServices();
		try{
			String programId = request.getParameter("programId");
			IdMapper idm = sc.getIdMapperService().findIdMapper(programId);
			
			model.put("programId", programId);
			
			String entityRole = request.getParameter("entityRole");
			if(entityRole == null){
				entityRole = sc.getIdMapperService().findIdMapper(programId).getRole().getRolename();
			}
			model.put("entityRole", entityRole);
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessagev", "An error occurred while reference data list. Error message is:"+e.getMessage());
		}
		finally{
			sc.closeSession();
		}
		
		
		return model;
	}
}
