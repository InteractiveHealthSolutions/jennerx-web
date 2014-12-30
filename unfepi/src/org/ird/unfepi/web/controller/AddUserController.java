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
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.model.Identifier;
import org.ird.unfepi.model.IdentifierType;
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

import com.mysql.jdbc.StringUtils;

public class AddUserController extends DataEntryFormController{
	private static final FormType formType = FormType.USER_ADD;
			
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception 
	{
		final String MESSAGE_USER_ADDED_SUCCESSFULLY="User added successfully.";
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		
		ServiceContext sc = Context.getServices();

		String role=request.getParameter("userrole");
		String userLocation = request.getParameter("userLocation");

		User irduser=(User) command;
		try{
			String urogramId = request.getParameter("projectId");
	
			IdMapper idm = new IdMapper();
			idm.setRoleId(sc.getUserService().getRole(role, false, null).getRoleId());
			idm.setMappedId(Integer.parseInt(sc.getIdMapperService().saveIdMapper(idm).toString()));

			Identifier ident = new Identifier();
			ident.setIdentifier(urogramId);
			ident.setIdentifierType((IdentifierType)sc.getCustomQueryService().getDataByHQL("FROM IdentifierType WHERE name ='"+GlobalParams.IdentifierType.USER_PROJECT_ID+"'").get(0));
			ident.setPreferred(true);
			ident.setIdMapper(idm);
			ident.setLocationId(StringUtils.isEmptyOrWhitespaceOnly(userLocation)?null:Integer.parseInt(userLocation));
			sc.getCustomQueryService().save(ident);
			
			irduser.setMappedId(idm.getMappedId());
			irduser.setCreator(user.getUser());
			
			sc.getUserService().createUser(irduser);
			sc.commitTransaction();
			
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(irduser), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(idm), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));

			String NEWLY_ADDED_ROLE_URL = "viewUsers.htm?editOrUpdateMessage="+MESSAGE_USER_ADDED_SUCCESSFULLY+"&action=search&userID="+irduser.getUsername();
			
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
	protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors) throws Exception 
	{
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		UserValidator validator = (UserValidator) getValidator();
		User userUnderEdit = (User)command;
		validator.validateRegistration(request.getParameter("projectId"), request.getParameter("cpassword"), userUnderEdit, request.getParameter("userrole"), user.getUser(), user.getUser().getIdMapper().getRole(), errors);
		return super.processFormSubmission(request, response, command, errors);
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,	Errors errors) throws Exception {
		boolean session_expired=false;
		Map<String, Object> model=new HashMap<String, Object>();

		LoggedInUser editorUser=UserSessionUtils.getActiveUser(request);
		ServiceContext sc = Context.getServices();
		try{
			Role editorUserRole = editorUser.getUser().getIdMapper().getRole();
			
			Map<String, ArrayList<Role>> rolemap = UserSessionUtils.getRolesDistinction(editorUser.getUser(), editorUserRole, sc);
			List<Role> allowedRoles = rolemap.get("ALLOWED_ROLES");
			List<Role> notAllowedRoles = rolemap.get("NOT_ALLOWED_ROLES");

			model.put("projectId", request.getParameter("projectId"));
			model.put("allowedRoles", allowedRoles);
			model.put("notAllowedRoles", notAllowedRoles);
		}catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("roleErrorMsg"	, e.getMessage());
		}
		finally{
			sc.closeSession();
		}
		
		
		return model;	
	}
}