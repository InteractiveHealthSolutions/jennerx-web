package org.ird.unfepi.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;

public class LogoutServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		String username = "";
		try{
			username = UserSessionUtils.getActiveUser(req).getUser().getUsername();
			UserSessionUtils.logout(username, req, resp);
			GlobalParams.DBLOGGER.info("User "+username+" logged out", LoggerUtils.getLoggerParams(LogType.LOGOUT, null, null));
		}
		catch (Exception e) {
			GlobalParams.DBLOGGER.error("User "+username+" had logout failure. Error message:"+e.getMessage(), LoggerUtils.getLoggerParams(LogType.LOGOUT, null, null));
			e.printStackTrace();
		}
		resp.sendRedirect(req.getContextPath() + "/login.htm");
	}
}
