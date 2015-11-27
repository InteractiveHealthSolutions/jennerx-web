package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEditFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.ErrorMessages;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.model.Role;
import org.ird.unfepi.model.User;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.validator.UserValidator;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class EditUserController extends DataEditFormController
{
	private static final FormType formType = FormType.USER_CORRECT;
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,	HttpServletResponse response, 
			Object command, BindException errors) throws Exception {
		final String MESSAGE_USER_EDITED_SUCCESSFULLY="User data edited successfully.";

		LoggedInUser user=UserSessionUtils.getActiveUser(request);

		String role=request.getParameter("userrole");
		User underEditUser=(User) command;
		ServiceContext sc = Context.getServices();
		try{
			IdMapper idm = sc.getIdMapperService().findIdMapper(underEditUser.getMappedId());

			if(!User.isUserDefaultAdministrator(underEditUser.getUsername(), idm.getRole().getRolename())){
				idm.setRoleId(sc.getUserService().getRole(role, false, null).getRoleId());
				sc.getIdMapperService().updateIdMapper(idm);
			}
			underEditUser.setEditor(user.getUser());
			sc.getUserService().updateUser(underEditUser);
			sc.commitTransaction();
			
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(underEditUser), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, formType, user.getUser().getUsername()));
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(idm), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, formType, user.getUser().getUsername()));
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
		
		return new ModelAndView(new RedirectView("viewUsers.htm?editOrUpdateMessage="+MESSAGE_USER_EDITED_SUCCESSFULLY+"&action=search&userid="+underEditUser.getUsername()));
	}
	
	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors) throws Exception 
	{
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			return new ModelAndView(new RedirectView("login.htm"));
		}
		UserValidator validator = (UserValidator) getValidator();
		User userUnderEdit = (User)command;
		validator.validateEdit(userUnderEdit.getIdMapper().getIdentifiers().get(0).getIdentifier(), userUnderEdit, request.getParameter("userrole"), user.getUser(), user.getUser().getIdMapper().getRole(), errors);
		return super.processFormSubmission(request, response, command, errors);
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		User iruser = new User();

		String rec=request.getParameter("editRecord");
		ServiceContext sc = Context.getServices();
		try{
			iruser=sc.getUserService().findUser(rec, false, false, false, false, new String[]{"idMapper"});
		}catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving User. Error message is:"+e.getMessage());
		}finally{
			sc.closeSession();
		}
		return iruser;
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,	Errors errors) throws Exception {
		User underEditUser=(User) command;
		Map<String, Object> model=new HashMap<String, Object>();
		ServiceContext sc = Context.getServices();
		try{
			Role userUnderEditRole = sc.getIdMapperService().findIdMapper(underEditUser.getMappedId()).getRole();
			LoggedInUser loggedinUser = UserSessionUtils.getActiveUser(request);
			Role editorRole = sc.getIdMapperService().findIdMapper(loggedinUser .getUser().getMappedId()).getRole();

			Map<String, ArrayList<Role>> rolemap = UserSessionUtils.getRolesDistinction(loggedinUser.getUser(), editorRole, sc);
			List<Role> allowedRoles = rolemap.get("ALLOWED_ROLES");
			List<Role> notAllowedRoles = rolemap.get("NOT_ALLOWED_ROLES");
			
			String editErrorMessage = "";
			boolean isUserAllowedToEdit = true;
			
			// DefaultAdmin User should only be editable by DefaultAdmin and its status and role should never be editable.
			if(User.isUserDefaultAdministrator(underEditUser.getUsername(), userUnderEditRole.getRolename())
					&& !loggedinUser.isDefaultAdministrator()){
				isUserAllowedToEdit = false;
				editErrorMessage += ErrorMessages.USER_ADMIN_EDITOR_NOT_AUTHORIZED;
			}
			
			for (Role role : notAllowedRoles) {
				if(userUnderEditRole.getRolename().equalsIgnoreCase(role.getRolename())){
					isUserAllowedToEdit = false;
					editErrorMessage += "The specified user has more privileges. Hence, can not be edited.";
				}
			}
			
			model.put("userRole", userUnderEditRole);
			model.put("allowedRoles", allowedRoles);
			model.put("notAllowedRoles", notAllowedRoles);
			model.put("isUserAllowedToEdit", isUserAllowedToEdit);
			model.put("editErrorMessage", editErrorMessage);
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving User. Error message is:"+e.getMessage());
		}finally{
			sc.closeSession();
		}
		
		
		return model;
	}
}
