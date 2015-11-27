package org.ird.unfepi.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jmatrix.eproperties.EProperties;

import org.ird.unfepi.EmailEngine;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.autosys.ReportingSystem;
import org.ird.unfepi.autosys.SmserSystem;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.utils.IRUtils;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class StarterServlet extends HttpServlet
{
	private static final long	serialVersionUID	= 1L;
	private static Scheduler scheduler;
	
	public static void startScheduler() throws SchedulerException{
		scheduler.start();
	}
	
	public static void pauseScheduler() throws SchedulerException{
		scheduler.standby();
	}


	@Override
	public void init() throws ServletException {
		try{
			System.out.println(">>>>LOADING SYSTEM PROPERTIES...");
			InputStream f = Thread.currentThread().getContextClassLoader().getResourceAsStream("unfepi.properties");
			// Java Properties donot seem to support substitutions hence EProperties are used to accomplish the task
			EProperties root = new EProperties();
			root.load(f);

			// Java Properties to send to context and other APIs for configuration
			Properties prop = new Properties();
			prop.putAll(IRUtils.convertEntrySetToMap(root.entrySet()));

			Context.instantiate(prop);
			GlobalParams.UNFEPI_PROPERTIES = prop;
			
//			TarseelContext.instantiate(prop, "smstarseel.cfg.xml");
//			
//			System.out.println("......PROPERTIES LOADED SUCCESSFULLY......");
//		
//			//setup email server
//			System.out.println(">>>>INSTANTIATING EMAIL ENGINE......");
//			
//			EmailEngine.instantiateEmailEngine(prop);
//			
//			System.out.println("......EMAIL ENGINE STARTED SUCCESSFULLY......".toLowerCase());
//				
//			// start smser and notifiers etc
//			System.out.println(">>>>INSTANTIATING SCHEDULERS......");
//
//			SchedulerFactory schedFact = new StdSchedulerFactory(prop);
//			scheduler = schedFact.getScheduler();
//			SmserSystem.instantiateSmserSystem(scheduler);
//			ReportingSystem.instantiateReportingSystem(scheduler);
//		
//			startScheduler();
//		
//			System.out.println("......QUARTZ SCHEDULER STARTED SUCCESSFULLY......".toLowerCase());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException 
	{
		String url = req.getContextPath() + "/login.htm";
		resp.sendRedirect(url);
		return;
	}
	
	private void verifyProperties(Properties props) throws IOException, ParseException{
		/*String pVal=props.getProperty("application.log.log-files-location");

		if(pVal != null){
			File file=new File(pVal);
			if(!file.exists()){
				throw new IOException("Property value 'application.log.log-files-location' in unfepi.properties doesnot exists");
			}
			if(!file.isDirectory()){
				throw new IOException("Property value 'application.log.log-files-location' in unfepi.properties is not a directory");
			}
			if(pVal.endsWith("/")||pVal.endsWith("\\")){
				props.setProperty("application.log.log-files-location", pVal.substring(0, pVal.length() -1 ));
			}
		}else {
			throw new IOException("Property 'application.log.log-files-location' not found in unfepi.properties");
		}*/
		
	}
}
