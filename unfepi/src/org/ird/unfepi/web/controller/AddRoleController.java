package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEntryForm;
import org.ird.unfepi.DataEntryFormController;
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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@SessionAttributes("command")
@RequestMapping("/addrole")
public class AddRoleController extends DataEntryFormController{
	
	private static final FormType formType = FormType.ROLE_ADD;
	
	AddRoleController(){
		super(new DataEntryForm("role", "Role (New)", SystemPermissions.ADD_ROLES));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView addRoleFormView(HttpServletRequest request, ModelAndView modelAndView){
		modelAndView.addObject("command", new Role());
		return showForm(modelAndView, "dataForm");
	}
	
	@RequestMapping(method=RequestMethod.POST)
	protected ModelAndView onSubmit(@ModelAttribute("command")Role role, BindingResult results,
			HttpServletRequest request,HttpServletResponse response, ModelAndView modelAndView) throws Exception {
			
		final String MESSAGE_ROLE_ADDED_SUCCESSFULLY="Role added successfully.";			
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		
		new RoleValidator().validate(role, results);
		if(results.hasErrors()){
			return showForm(modelAndView, "dataForm");
		}
		
		ServiceContext sc = Context.getServices();
		String[] perms=request.getParameterValues("selectedPerms");			
		List<Permission> permissions=new ArrayList<Permission>();
		try {
			for (String p : perms) {
				permissions.add(sc.getUserService().getPermission(p, false));
			}				
			role.addPermissions(permissions);			
			role.setCreator(user.getUser());				
			sc.getUserService().addRole(role);			
			sc.commitTransaction();
			
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(role), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));
			String NEWLY_ADDED_ROLE_URL = "viewRoles.htm?editOrUpdateMessage="+MESSAGE_ROLE_ADDED_SUCCESSFULLY+"&action=search&roleName="+role.getRolename();
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
	
	@ModelAttribute
	protected void referenceData(HttpServletRequest request, Model model) throws Exception {
		ServiceContext sc = Context.getServices();
		try {
			List<Permission> perms=sc.getUserService().getAllPermissions(true);
			model.addAttribute("permissions", perms);
		
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("permissionErrorMsg", e.getMessage());
		
		} finally {
			sc.closeSession();
		}
	}
}
