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
import org.ird.unfepi.model.Storekeeper;
import org.ird.unfepi.model.User;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class EditStorekeeperController extends DataEditFormController
{
	private static final FormType formType = FormType.STOREKEEPER_CORRRECT;
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception 
	{
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		
		Storekeeper stork = (Storekeeper) command;
		
		ServiceContext sc = Context.getServices();
		try{
			stork.setEditor(user.getUser());
			sc.getStorekeeperService().updateStorekeeper(stork);
			
			User suser = sc.getUserService().findUser(stork.getMappedId());
			suser.setFirstName(stork.getFirstName());
			suser.setMiddleName(stork.getMiddleName());
			suser.setLastName(stork.getLastName());
			sc.getUserService().updateUser(suser);

			sc.commitTransaction();

			GlobalParams.DBLOGGER.info(IRUtils.convertToString(stork), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, formType, user.getUser().getUsername()));
			
			return new ModelAndView(new RedirectView("viewStorekeepers.htm?action=search&storekeeperid="+stork.getIdMapper().getIdentifiers().get(0).getIdentifier()));
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
	
/*	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		System.out.println(errors.getAllErrors());
		return null;
	}*/
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)	throws Exception {
		Storekeeper stork = new Storekeeper();

		String programId = request.getParameter("programId");
		ServiceContext sc = Context.getServices();
		try{
			stork = sc.getStorekeeperService().findStorekeeperById(programId, false, new String[]{"idMapper"});
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving Storekeeper reference datas. Error message is:"+e.getMessage());
		}
		finally{
			sc.closeSession();
		}
		return stork;
	}

	@Override
	protected Map referenceData(HttpServletRequest request, Object command,	Errors errors) throws Exception 
	{
		Map<String, Object> model = new HashMap<String, Object>();
		ServiceContext sc = Context.getServices();;
		try{
			model.put("vaccinationCenters", sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"}));
			//model.put("relationships", sc.getDemographicDetailsService().getAllRelationship());
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving storekeepers reference datas. Error message is:"+e.getMessage());
		}
		finally{
			sc.closeSession();
		}
		
		return model;
	}
}
