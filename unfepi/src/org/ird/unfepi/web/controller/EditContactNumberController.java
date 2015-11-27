package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEditFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class EditContactNumberController extends DataEditFormController
{
	private static final FormType formType = FormType.CONTACT_NUMBER_CORRECT;
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception 
	{
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		
		ContactNumber con = (ContactNumber) command;
		ServiceContext sc = Context.getServices();

		try{
			con.setEditor(user.getUser());
			sc.getDemographicDetailsService().updateContactNumber(con);
			sc.commitTransaction();
			
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(con), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, formType, user.getUser().getUsername()));

			String editmessage = "updated successfully";
			
			String entityRole = request.getParameter("entityRole");

			if(entityRole.equalsIgnoreCase("child")){
				return new ModelAndView(new RedirectView("viewChildren.htm?action=search&editOrUpdateMessage="+editmessage +"&childid="+con.getIdMapper().getIdentifiers().get(0).getIdentifier()));
			}
			else if(entityRole.equalsIgnoreCase("storekeeper")){
				return new ModelAndView(new RedirectView("viewStorekeepers.htm?action=search&editOrUpdateMessage="+editmessage +"&storekeeperid="+con.getIdMapper().getIdentifiers().get(0).getIdentifier()));
			}
			else if(entityRole.equalsIgnoreCase("vaccinator")){
				return new ModelAndView(new RedirectView("viewVaccinators.htm?action=search&editOrUpdateMessage="+editmessage +"&vaccinatorid="+con.getIdMapper().getIdentifiers().get(0).getIdentifier()));
			}
			
			return new ModelAndView(new RedirectView("viewChildren.htm?action=search&editOrUpdateMessage="+editmessage +"&childid="+con.getIdMapper().getIdentifiers().get(0).getIdentifier()));
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
	protected Object formBackingObject(HttpServletRequest request) throws Exception 
	{
		String conId=request.getParameter("coNum");
		ContactNumber con = null;
		ServiceContext sc = Context.getServices();
		try{
			con = sc.getDemographicDetailsService().getContactNumberById(Integer.parseInt(conId), false, new String[]{"idMapper"});
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessagev", "An error occurred while retrieving contact number. Error message is:"+e.getMessage());
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
		
		ContactNumber con = (ContactNumber) command;
		ServiceContext sc = Context.getServices();
		try{
			model.put("programId", con.getIdMapper().getIdentifiers().get(0).getIdentifier());
			
			String entityRole = request.getParameter("entityRole");
			if(entityRole == null){
				entityRole = sc.getIdMapperService().findIdMapper(con.getMappedId()).getRole().getRolename();
			}
			model.put("entityRole", entityRole);
		}catch (Exception e) {
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
