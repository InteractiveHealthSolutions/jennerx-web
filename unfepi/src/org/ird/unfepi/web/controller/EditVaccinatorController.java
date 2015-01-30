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
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class EditVaccinatorController extends DataEditFormController
{
	private static final FormType formType = FormType.VACCINATOR_CORRECT;
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception 
	{
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		
		Vaccinator vacc = (Vaccinator) command;
		
		ServiceContext sc = Context.getServices();
		try{
			vacc.setEditor(user.getUser());
			sc.getVaccinationService().updateVaccinator(vacc);
			
			User vuser = sc.getUserService().findUser(vacc.getMappedId());
			vuser.setFirstName(vacc.getFirstName());
			vuser.setMiddleName(vacc.getMiddleName());
			vuser.setLastName(vacc.getLastName());
			sc.getUserService().updateUser(vuser);

			sc.commitTransaction();

			GlobalParams.DBLOGGER.info(IRUtils.convertToString(vacc), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, formType, user.getUser().getUsername()));
			
			return new ModelAndView(new RedirectView("viewVaccinators.htm?action=search&vaccinatorid="+vacc.getIdMapper().getIdentifiers().get(0).getIdentifier()));
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
		Vaccinator vacc = new Vaccinator();

		String programId = request.getParameter("programId");
		ServiceContext sc = Context.getServices();
		try{
			vacc = sc.getVaccinationService().findVaccinatorById(programId, false, new String[]{"idMapper"});
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving Vaccinator reference datas. Error message is:"+e.getMessage());
		}
		finally{
			sc.closeSession();
		}
		return vacc;
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,	Errors errors) throws Exception 
	{
		Map<String, Object> model = new HashMap<String, Object>();
		ServiceContext sc = Context.getServices();
		try{
			Vaccinator cmd = (Vaccinator) command;
			model.put("vaccinationCenter", sc.getVaccinationService().findVaccinationCenterById(cmd.getVaccinationCenterId(), true, new String[]{"idMapper"}));
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving vaccinators reference datas. Error message is:"+e.getMessage());
		}
		finally{
			sc.closeSession();
		}
		
		
		return model;
	}
}
