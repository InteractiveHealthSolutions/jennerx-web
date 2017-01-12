package org.ird.unfepi.web.dwr;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;

import org.directwebremoting.WebContextFactory;
import org.ird.unfepi.EmailEngine;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.User;
import org.ird.unfepi.service.exception.UserServiceException;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;

public class DWRUserService {

	public String changePassword(String oldPwd,String newPwd,String confirmPwd) {
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){
			return "Your session has expired . Please login again.";
		}
		if(newPwd.length()<6){
			return "password must contain a minimum length of 6 characters";
		}
		ServiceContext sc = Context.getServices();
		try {
			sc.getUserService().changePassword(user.getUser().getUsername(), oldPwd, newPwd, confirmPwd);
			sc.commitTransaction();
		} catch (UserServiceException e) {
			return e.getMessage();
		} catch (Exception e) {
			return "An error occurred while processing request. Error message is :"+e.getMessage();
		}finally{
			sc.closeSession();
		}
		return "password changed successfully";
	}
	
	public String resetPassword( String username,String newPwd , boolean sendEmail ) {
		
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){
			try {
				WebContextFactory.get().forwardToString("login.htm");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			if(!UserSessionUtils.hasActiveUserPermission(SystemPermissions.RESET_USER_PASSWORD, WebContextFactory.get().getHttpServletRequest())){
				return "You donot have permissions to reset password";
			}
		} catch (UserServiceException e1) {
			e1.printStackTrace();
			return "Error while authenticating action "+e1.getMessage();
		}
		
		ServiceContext sc = Context.getServices();
		try {
			User u=sc.getUserService().findUserByUsername(username);
			u.setClearTextPassword(newPwd);
			u.setLastEditedDate(new Date());
			u.setLastEditedByUserId(user.getUser());
			
			sc.getUserService().updateUser(u);
			sc.commitTransaction();
			
			GlobalParams.DBLOGGER.info("User:"+username+";Password:"+u.getPassword(), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, FormType.RESET_PASSWORD, user.getUser().getUsername()));

			String errorMsg = "";
			if(sendEmail){
				try{
					EmailEngine.getInstance().getEmailer().postSimpleMail(new String[]{u.getEmail()}, "UNFEPI : Password Reset", "\n\nDear user, your password has been reset to \n"+u.getClearTextPassword()+"\nYou can now access you account with the password given.\nIt is strongly recommended that you change the password as soon as possible\n\nBest Regards,\nadmin-unfepi", "admin.unfepi@gmail.com");
				}
				catch (Exception e) {
					e.printStackTrace();
					errorMsg = "Error while sending email : "+e.getMessage();
				}
			}
			
			return "User "+username+" password was reset successfully. "+(sendEmail?"An email has been sent on user email address "+u.getEmail():"")+". "+errorMsg;
		} 
		catch (UserServiceException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return "An error occurred while processing request. Error message is :"+e.getMessage();
		}finally{
			sc.closeSession();
		}
	}
	
	public boolean isIdMapperExists(String programId) {
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){
			//return "Session expired. Logout from application and login again";
			try {
				WebContextFactory.get().forwardToString("login.htm");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ServiceContext sc = Context.getServices();
		try {
			return sc.getIdMapperService().findIdMapper(programId) != null;
		} 
		finally{
			sc.closeSession();
		}
	}
	public boolean isUsernameExists(String username) {
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){
			//return "Session expired. Logout from application and login again";
			try {
				WebContextFactory.get().forwardToString("login.htm");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ServiceContext sc = Context.getServices();
		try {
			return sc.getUserService().findUserByUsername(username) != null;
		} 
		finally{
			sc.closeSession();
		}
	}
}
