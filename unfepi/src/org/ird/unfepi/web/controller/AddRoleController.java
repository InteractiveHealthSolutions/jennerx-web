package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEntryFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Permission;
import org.ird.unfepi.model.Role;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class AddRoleController extends DataEntryFormController{

	private static final FormType formType = FormType.ROLE_ADD;
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {
		
			final String MESSAGE_ROLE_ADDED_SUCCESSFULLY="Role added successfully.";
			
			LoggedInUser user=UserSessionUtils.getActiveUser(request);
			
			ServiceContext sc = Context.getServices();

			String[] perms=request.getParameterValues("selectedPerms");
			List<Permission> permissions=new ArrayList<Permission>();
			Role role=(Role) command;
			try{
				for (String p : perms) {
					permissions.add(sc.getUserService().getPermission(p,false));
				}
				
				role.addPermissions(permissions);
				role.setCreator(user.getUser());
				
				sc.getUserService().addRole(role);
				sc.commitTransaction();
				
				GlobalParams.DBLOGGER.info(IRUtils.convertToString(role), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));

				String NEWLY_ADDED_ROLE_URL = "viewRoles.htm?editOrUpdateMessage="+MESSAGE_ROLE_ADDED_SUCCESSFULLY+"&action=search&roleName="+role.getRolename();
				
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
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map<String, Object> model=new HashMap<String, Object>();
		ServiceContext sc = Context.getServices();
		try{
			List<Permission> perms=sc.getUserService().getAllPermissions(true);
			model.put("permissions", perms);
		}catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("permissionErrorMsg", e.getMessage());
		}
		finally{
			sc.closeSession();
		}
		
		
		return model;
	}
}
