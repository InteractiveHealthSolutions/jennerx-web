package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEditForm;
import org.ird.unfepi.DataEditFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Permission;
import org.ird.unfepi.model.Role;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.validator.RoleValidator;
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
@RequestMapping("/editrole")
public class EditRoleController extends DataEditFormController {
	private static final FormType formType = FormType.ROLE_CORRECT;

	EditRoleController() {
		super(new DataEditForm("role", "Role (Edit)", SystemPermissions.CORRECT_USER_ROLES));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView EditRoleView(HttpServletRequest request, ModelAndView modelAndView) {
		modelAndView.addObject("command", backingObject(request, modelAndView.getModelMap()));
		return showForm(modelAndView, "dataForm");
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command") Role role,BindingResult results, 
			HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {

		new RoleValidator().validate(role, results);
		if (results.hasErrors()) {
			return showForm(modelAndView, "dataForm");
		}

		String message = "Role edited successfully.";

		LoggedInUser user = UserSessionUtils.getActiveUser(request);
		String[] perms = request.getParameterValues("selectedPerms");
		List<Permission> permissions = new ArrayList<Permission>();

		role.getPermissions().clear();

		ServiceContext sc = Context.getServices();
		try {
			for (String p : perms) {
				permissions.add(sc.getUserService().getPermission(p, false));
			}
			role.addPermissions(permissions);
			role.setEditor(user.getUser());
			sc.getUserService().updateRole(role);
			sc.commitTransaction();
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(role),LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, formType, user.getUser().getUsername()));

			String NEWLY_ADDED_ROLE_URL = "viewRoles.htm?editOrUpdateMessage="+message+"&action=search&roleName="+role.getRolename();
			return new ModelAndView(new RedirectView(NEWLY_ADDED_ROLE_URL));
		
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView("exception");
		
		} finally {
			sc.closeSession();
		}
	}

	public Role backingObject(HttpServletRequest request, ModelMap model) {

		Role role = new Role();
		String rec = request.getParameter("editRecord");
		List<Permission> perms = new ArrayList<Permission>();
		ServiceContext sc = Context.getServices();
		try {
			role = sc.getUserService().getRole(rec, false, new String[] { "permissions" });
			perms = sc.getUserService().getAllPermissions(true);

			for (Permission p : role.getPermissions()) {
				for (int i = 0; i < perms.size(); i++) {
					if (perms.get(i).getPermissionId() == p.getPermissionId()) {
						perms.remove(i);
						i--;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving Role. Error message is:" + e.getMessage());
		
		} finally {
			sc.closeSession();
		}
		
		model.addAttribute("rolePermissions", role.getPermissions());
		model.addAttribute("remainingPermissions", perms);
		return role;
	}
}
