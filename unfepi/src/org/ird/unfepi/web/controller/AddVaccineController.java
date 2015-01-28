package org.ird.unfepi.web.controller;

import java.util.Date;

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

public class AddVaccineController extends SimpleFormController {
	
	private static final FormType formType = FormType.VACCINE_ADD;
	private Date dateFormStart = new Date();

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception 
	{
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		
		Vaccine vaccine = (Vaccine) command;
		ServiceContext sc = Context.getServices();
		try{
			vaccine.setCreator(user.getUser());
			sc.getVaccinationService().addVaccine(vaccine);
			
			sc.commitTransaction();
			
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(vaccine), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));

			String editmessage = "Vaccine added successfully";
			return new ModelAndView(new RedirectView("viewVaccines.htm?editOrUpdateMessage="+editmessage));
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
		
		return new Vaccine();
	}
}