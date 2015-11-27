package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEditFormController;
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
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class EditRoleController extends DataEditFormController {
	private static final FormType formType = FormType.ROLE_CORRECT;
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		 String message="Role edited successfully.";
	
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		String[] perms=request.getParameterValues("selectedPerms");
		List<Permission> permissions=new ArrayList<Permission>();
		Role role=(Role) command;
		role.getPermissions().clear();
		
		ServiceContext sc = Context.getServices();
		try{
			for (String p : perms) {
				permissions.add(sc.getUserService().getPermission(p,false));
			}
			role.addPermissions(permissions);
			role.setEditor(user.getUser());
			sc.getUserService().updateRole(role);
			sc.commitTransaction();
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(role), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, formType, user.getUser().getUsername()));

			String NEWLY_ADDED_ROLE_URL = "viewRoles.htm?editOrUpdateMessage="+message+"&action=search&roleName="+role.getRolename();
			
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
		Role role = new Role();

		String rec=request.getParameter("editRecord");
		ServiceContext sc = Context.getServices();
		try{
			role=sc.getUserService().getRole(rec, false, new String[]{"permissions"});
		}catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving Role. Error message is:"+e.getMessage());
		}finally{
			sc.closeSession();
		}
		return role;
	}
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Role role=(Role) command;
		Map<String, Object> model=new HashMap<String, Object>();
		List<Permission> perms=new ArrayList<Permission>();

		ServiceContext sc = Context.getServices();
		try{
			perms=sc.getUserService().getAllPermissions(true);
			
			for (Permission p : role.getPermissions()) {
				for (int i = 0; i < perms.size(); i++) {
					if(perms.get(i).getPermissionId()==p.getPermissionId()){
						perms.remove(i);
						i--;
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving Permission list. Error message is:"+e.getMessage());
		}
		finally{
			sc.closeSession();
		}
		model.put("rolePermissions", role.getPermissions());
		model.put("remainingPermissions", perms);
		
		
		return model;
	}

}
