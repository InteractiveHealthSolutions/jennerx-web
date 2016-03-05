package org.ird.unfepi.web.servlet;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.DownloadableReport;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.utils.reporting.ExceptionUtil;

public class DownloadReportServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			response.sendRedirect(request.getContextPath() +"/login.htm");
			return;
		}
	
		String recordId=request.getParameter("record");
		 
		ServiceContext sc = Context.getServices();

		ServletOutputStream out = response.getOutputStream();
		try {
			DownloadableReport dobl = sc.getReportService().getDownloadableReportById(Integer.parseInt(recordId), true, null);
			
			if(dobl.getDownloadableName().toLowerCase().endsWith(".pdf")){
				response.setContentType("application/pdf"); 
			}
			else if(dobl.getDownloadableName().toLowerCase().endsWith(".csv")){
				response.setContentType("text/csv"); 
			}
			else if(dobl.getDownloadableName().toLowerCase().endsWith(".txt")){
				response.setContentType("text/csv"); 
			}
			else if(dobl.getDownloadableName().toLowerCase().endsWith(".zip")){
				response.setContentType("application/zip"); 
			}
			else{
				response.setContentType("application/zip"); 
			}
			response.setHeader("Content-Disposition", "attachment; filename="+dobl.getDownloadableName());
			
			FileInputStream fileIn = new FileInputStream(dobl.getDownloadablePath());
			BufferedInputStream bipstr=new BufferedInputStream(fileIn);
			 
			byte[] outputByte = new byte[4096];
			//copy binary contect to output stream
			int length;
			while((length=bipstr.read(outputByte)) != -1)
			{
				out.write(outputByte, 0, length);
			}
			fileIn.close();
			bipstr.close();
			out.flush();
			
			GlobalParams.DBLOGGER.info(dobl.getDownloadableName(), LoggerUtils.getLoggerParams(LogType.DATA_EXPORT, null, user.getUser().getUsername()));
		} catch (Exception e) {
			e.printStackTrace();
			out.write(("Error occurred while downloading report ."+ExceptionUtil.getStackTrace(e)).getBytes());
		}finally{
			sc.closeSession();
			out.close();
		}
	}
}
