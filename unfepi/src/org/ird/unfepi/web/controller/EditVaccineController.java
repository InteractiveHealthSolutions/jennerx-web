package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class EditVaccineController extends SimpleFormController{
	private static final FormType formType = FormType.VACCINE_CORRECT;
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {
		String message="Vaccine edited successfully.";
	
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		
		Vaccine vacc=(Vaccine) command;
		ServiceContext sc = Context.getServices();
		try{
			vacc.setEditor(user.getUser());

			sc.getVaccinationService().updateVaccine(vacc);
			sc.commitTransaction();
			
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(vacc), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, formType, user.getUser().getUsername()));

			String NEWLY_ADDED_ROLE_URL = "viewVaccines.htm?editOrUpdateMessage="+message+"&action=search&vaccineName="+vacc.getName();
			
			return new ModelAndView(new RedirectView(NEWLY_ADDED_ROLE_URL));
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
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Vaccine vacc = new Vaccine();

		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			boolean session_expired = true;
			request.setAttribute("session_expired", session_expired);
		}
		else{
			String rec=request.getParameter("editRecord");
			ServiceContext sc = Context.getServices();
			try{
				vacc=sc.getVaccinationService().getByName(rec);
			}catch (Exception e) {
				e.printStackTrace();
				GlobalParams.FILELOGGER.error(formType.name(), e);
				request.setAttribute("errorMessage", "An error occurred while retrieving Vaccine. Error message is:"+e.getMessage());
			}finally{
				sc.closeSession();
			}
		}
		return vacc;
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		
		return new HashMap<String, Object>();
	}
}
