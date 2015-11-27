package org.ird.unfepi.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.EmailEngine;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.User;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.utils.reporting.ExceptionUtil;
import org.ird.unfepi.utils.reporting.LoggerUtil;

public class ResetUserPwdServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			resp.sendRedirect(req.getContextPath() + "/login.htm");
			return;
		}
		
		ServiceContext sc = Context.getServices();

		String id=req.getParameter("UserId");

		try{
		User u=sc.getUserService().findUserByUsername(id);
		u.setClearTextPassword(req.getParameter("paswrd"));
		
		sc.getUserService().updateUser(u);
		sc.commitTransaction();
		
		LoggerUtil.logIt("Password of User '"+id+"' was reset by '"+user.getUser().getUsername()+":"+user.getUser().getFullName()+"'");
		req.getSession().setAttribute("resetUserPwdMsg", "User "+id+" password was reset successfully. An email has been sent on user email address "+u.getEmail());
		
		try{
			EmailEngine.getInstance().getEmailer().postSimpleMail(new String[]{u.getEmail()}, "UNFEPI : Password Reset", "\n\nDear user, your password has been reset to \n"+u.getClearTextPassword()+"\nYou can now access you account with the password given.\nIt is strongly recommended that you change the password as soon as possible\n\nBest Regards,\nadmin-unfepi", "admin.unfepi@gmail.com");
		}
		catch (Exception e) {
			LoggerUtil.logIt("\nError while resetting pwd:"+ExceptionUtil.getStackTrace(e));
		}
		}
		catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("resetUserPwdMsg", "User password was not reset.");
			LoggerUtil.logIt("An attempt to reset password of User '"+id+"' to be reset by '"+user.getUser().getUsername()+":"+user.getUser().getFullName()+"' was failed with error \n"+e.getMessage());
			LoggerUtil.logIt("\nError while resetting pwd:"+ExceptionUtil.getStackTrace(e));
		}
		finally{
				sc.closeSession();
		}
		resp.sendRedirect(req.getContextPath() + "/viewUsers.htm");
	}
	/*String resetPwd(){
		String str="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_|:;!@#%*(){}[]";
		Random rand=new Random();
		rand.nextInt(str.length());
		String pstr="";
		for (int i = 0; i < 8+rand.nextInt(5); i++) {
			pstr+=str.charAt(rand.nextInt(str.length()));
		}
		return pstr;
	}*/
}