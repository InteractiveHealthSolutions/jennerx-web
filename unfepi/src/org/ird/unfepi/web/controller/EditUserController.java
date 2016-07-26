package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEditForm;
import org.ird.unfepi.DataEditFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.ErrorMessages;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.SystemPermissions;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@SessionAttributes("command")
@RequestMapping("/edituser")
public class EditUserController extends DataEditFormController {
	
	private static final FormType formType = FormType.USER_CORRECT;
	
	EditUserController(){
		super(new DataEditForm("user", "User (Edit)", SystemPermissions.CORRECT_USERS));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView editUserView(HttpServletRequest request, ModelAndView modelAndView){
		modelAndView.addObject("command", backingObject(request, modelAndView.getModelMap()));
		return showForm(modelAndView, "dataForm");
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")User underEditUser, BindingResult results,
								 HttpServletRequest request, HttpServletResponse response,
								 ModelAndView modelAndView){
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			return new ModelAndView(new RedirectView("login.htm"));
		}

		new UserValidator().validateEdit(underEditUser.getIdMapper().getIdentifiers().get(0).getIdentifier(), 
										 underEditUser, request.getParameter("userrole"), 
										 user.getUser(), user.getUser().getIdMapper().getRole(), results);
		
		if(results.hasErrors()){
			return showForm(modelAndView, "dataForm");
		}
		
		final String MESSAGE_USER_EDITED_SUCCESSFULLY="User data edited successfully.";
		String role = request.getParameter("userrole");
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
			return new ModelAndView(new RedirectView("viewUsers.htm?editOrUpdateMessage="+MESSAGE_USER_EDITED_SUCCESSFULLY+"&action=search&userid="+underEditUser.getUsername()));
			
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
	
	public User backingObject(HttpServletRequest request, ModelMap model){
		
		User iruser = new User();
		ServiceContext sc = Context.getServices();
		try{
			iruser=sc.getUserService().findUser(request.getParameter("editRecord") , false, false, false, false, new String[]{"idMapper"});
			
			Role userUnderEditRole = sc.getIdMapperService().findIdMapper(iruser.getMappedId()).getRole();
			LoggedInUser loggedinUser = UserSessionUtils.getActiveUser(request);
			Role editorRole = sc.getIdMapperService().findIdMapper(loggedinUser .getUser().getMappedId()).getRole();

			Map<String, ArrayList<Role>> rolemap = UserSessionUtils.getRolesDistinction(loggedinUser.getUser(), editorRole, sc);
			List<Role> allowedRoles = rolemap.get("ALLOWED_ROLES");
			List<Role> notAllowedRoles = rolemap.get("NOT_ALLOWED_ROLES");
			
			String editErrorMessage = "";
			boolean isUserAllowedToEdit = true;
			
			// DefaultAdmin User should only be editable by DefaultAdmin and its status and role should never be editable.
			if(User.isUserDefaultAdministrator(iruser.getUsername(), userUnderEditRole.getRolename()) && !loggedinUser.isDefaultAdministrator()){
				isUserAllowedToEdit = false;
				editErrorMessage += ErrorMessages.USER_ADMIN_EDITOR_NOT_AUTHORIZED;
			}
			
			for (Role role : notAllowedRoles) {
				if(userUnderEditRole.getRolename().equalsIgnoreCase(role.getRolename())){
					isUserAllowedToEdit = false;
					editErrorMessage += "The specified user has more privileges. Hence, can not be edited.";
				}
			}
			
			model.addAttribute("userRole", userUnderEditRole);
			model.addAttribute("allowedRoles", allowedRoles);
			model.addAttribute("notAllowedRoles", notAllowedRoles);
			model.addAttribute("isUserAllowedToEdit", isUserAllowedToEdit);
			model.addAttribute("editErrorMessage", editErrorMessage);
			
		}catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving User. Error message is:" + e.getMessage());
		}finally{
			sc.closeSession();
		}
		return iruser;	
	}

}
