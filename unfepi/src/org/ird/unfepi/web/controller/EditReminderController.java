package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEditFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Reminder;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class EditReminderController extends DataEditFormController {

	private static final FormType formType = FormType.REMINDER_CORRECT;
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		String message = "Reminder data edited successfully.";

		LoggedInUser user = UserSessionUtils.getActiveUser( request );

		String[] rt = request.getParameterValues( "remText" );
		Set<String> remt = new HashSet<String>();
		for ( String s : rt ) {
			remt.add( s );
		}
		
		Reminder rem = (Reminder) command;
		ServiceContext sc = Context.getServices();
		try {
			rem.getReminderText().clear();
			rem.setReminderText( remt );
			rem.setEditor( user.getUser() );
			
			sc.getReminderService().updateReminder( rem );

			sc.commitTransaction();
			
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(rem), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, formType, user.getUser().getUsername()));
		}
		catch ( Exception e ) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.getSession().setAttribute( "exceptionTrace" , e );
			return new ModelAndView( new RedirectView( "exception.htm" ) );
		}
		finally {
			sc.closeSession();
		}
		return new ModelAndView(new RedirectView("viewReminders.htm?action=search&editOrUpdateMessage="+message+"&reminderName="+rem.getRemindername()));
	}
	@Override
	protected Object formBackingObject(HttpServletRequest request)	throws Exception {
		Reminder rem=new Reminder();

		String rec=request.getParameter("editRecord");
		ServiceContext sc = Context.getServices();
		try{
			rem=sc.getReminderService().getReminderById(Short.parseShort(rec), false, new String[]{"reminderText"});
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", e.getMessage());
		}
		finally{
			sc.closeSession();
		}

		return rem;
	}
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,	Errors errors) throws Exception {
		Reminder rem = (Reminder) command;
		Map<String , Object> model = new HashMap<String , Object>();
		model.put( "reminderText" , rem.getReminderText() );
		
		
		return model;
	}
}
