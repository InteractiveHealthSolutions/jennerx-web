package org.ird.unfepi.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.utils.UnfepiUtils;
import org.ird.unfepi.utils.UserSessionUtils;

public class ForceUpdateDumpServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			resp.sendRedirect(req.getContextPath() + "/login.htm");
			return;
		}
		
		if(!user.hasPermission(SystemPermissions.DO_FORCE_UPDATE_IMMUNIZATION_RECORDS.name())){
			throw new IllegalAccessError("You are not allowed for requested operation. Contact program vendor.");
		}
		
		ServiceContext sc = Context.getServices();
		
		try{
			UnfepiUtils.executeDump("DMP_MasterEPIData");
			
			resp.sendRedirect(req.getHeader("Referer").toString());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		
	}
}
