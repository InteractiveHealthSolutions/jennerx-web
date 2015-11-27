package org.ird.unfepi.web.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.constants.WebGlobals;

public class DownloadLogServlet extends HttpServlet{
	String fileloc=null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String logfile=req.getParameter("logfile");
/*try{
	fileloc=Context.getIRSetting("logs.root-path", System.getProperty("user.home"));
	File file=new File(fileloc);
	if(!file.exists()){
		fileloc=System.getProperty("user.home");
	}
	
	if(logfile.startsWith("v")){
		if(logfile.compareTo("vweblog")==0){
			fileloc+=WebGlobals.WEB_LOG_FILE_NAME;
		}
		if(logfile.compareTo("vmodemlog")==0){
			fileloc+=WebGlobals.MODEM_LOG_FILE_NAME;
		}
		if(logfile.compareTo("vunsentreminder")==0){
			fileloc+=WebGlobals.UNSENT_REMINDER_FILE_NAME;
		}
		if(logfile.compareTo("vfailedflag")==0){
			fileloc+=WebGlobals.FAILED_FLAG_FILE_NAME;
		}
		if(logfile.compareTo("vreceivedresponse")==0){
			fileloc+=WebGlobals.RECEIVED_RESPONSE_FILE_NAME;
		}
		if(logfile.compareTo("vunsavedresponse")==0){
			fileloc+=WebGlobals.UNSAVED_RESPONSE_FILE_NAME;
		}
		if(logfile.compareTo("vunsavedreminder")==0){
			fileloc+=WebGlobals.UNSAVED_REMINDER_FILE_NAME;
		}
		if(logfile.compareTo("vdailydetailedlog")==0){
			fileloc+=WebGlobals.DAILY_DETAILED_LOG_FILE_NAME;
		}
		downloadLogFile(resp);
	}
	
	if(logfile.startsWith("d")){
		if(logfile.compareTo("dweblog")==0){
			fileloc+=WebGlobals.WEB_LOG_FOLDER_PATH;
		}
		if(logfile.compareTo("dmodemlog")==0){
			fileloc+=WebGlobals.MODEM_LOG_FOLDER_PATH;
		}
		if(logfile.compareTo("dunsentreminder")==0){
			fileloc+=WebGlobals.UNSENT_REMINDER_FOLDER_PATH;
		}
		if(logfile.compareTo("dfailedflag")==0){
			fileloc+=WebGlobals.FAILED_FLAG_FOLDER_PATH;
		}
		if(logfile.compareTo("dreceivedresponse")==0){
			fileloc+=WebGlobals.RECEIVED_RESPONSE_FOLDER_PATH;
		}
		if(logfile.compareTo("dunsavedresponse")==0){
			fileloc+=WebGlobals.UNSAVED_RESPONSE_FOLDER_PATH;
		}
		if(logfile.compareTo("dunsavedreminder")==0){
			fileloc+=WebGlobals.UNSAVED_REMINDER_FOLDER_PATH;
		}
		if(logfile.compareTo("ddailydetailedlog")==0){
			fileloc+=WebGlobals.DAILY_DETAILED_LOG_FOLDER_PATH;
		}
		// TODO not working fine with zips of hierarichy of files needs to be modified 
		if(logfile.compareTo("dcompleterecord")==0){
			fileloc=WebGlobals.ALL_LOGS_FOLDER_PATH;
		}
		downloadCompleteLog(resp);
	}
	}catch (Exception e) {
			resp.getOutputStream().print("Error downloading file:"+e.getMessage());
			LoggerUtil.logIt("Error downloading file:"+ExceptionUtil.getStackTrace(e));
			return;
	}*/
}
	private void downloadCompleteLog(HttpServletResponse resp) throws IOException{

		     File inFolder=new File(fileloc);
		    // File outFolder=new File("Out.zip");
			String filename=fileloc.substring(fileloc.lastIndexOf(System.getProperty("file.separator")));
			resp.setContentType("application/octet-stream");
			resp.setHeader("Content-Disposition","attachment;filename="+filename+new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).format(new Date())+".zip");
	
			//create object of ZipOutputStream from FileOutputStream
			ZipOutputStream zout = new ZipOutputStream(resp.getOutputStream());
			//create File object from source directory
				addDirectory(zout, inFolder);
			//close the ZipOutputStream

			zout.close();
		}
		private void addDirectory(ZipOutputStream zout, File fileSource) throws IOException {
		//get sub-folder/files list
		File[] files = fileSource.listFiles();
		for(int i=0; i < files.length; i++)
		{
		//if the file is directory, call the function recursively
		if(files[i].isDirectory())
		{
		addDirectory(zout, files[i]);
		continue;
		}
		/*
		* we are here means, its file and not directory, so
		* add it to the zip file
		*/

		//create byte buffer
		byte[] buffer = new byte[1024];
		//create object of FileInputStream
		FileInputStream fin = new FileInputStream(files[i]);
		BufferedInputStream bipstr=new BufferedInputStream(fin);

		zout.putNextEntry(new ZipEntry(files[i].getName()));
		/*
		* After creating entry in the zip file, actually
		* write the file.
		*/
		int length;
		while((length = bipstr.read(buffer)) != -1)
		{
		zout.write(buffer, 0, length);
		}
		/*
		* After writing the file to ZipOutputStream, use
		*
		* void closeEntry() method of ZipOutputStream class to
		* close the current entry and position the stream to
		* write the next entry.
		*/
		zout.closeEntry();
		//close the InputStream
		fin.close();
		bipstr.close();
		}
		}
	private void downloadLogFile(HttpServletResponse resp) throws IOException {
		String filename=fileloc.substring(fileloc.lastIndexOf(System.getProperty("file.separator")));
		resp.setContentType("text/csv");
		resp.setHeader("Content-Disposition","attachment;filename="+filename);

		File file = new File(fileloc);
		FileInputStream fileIn = new FileInputStream(file);
		BufferedInputStream bipstr=new BufferedInputStream(fileIn);
		ServletOutputStream out = resp.getOutputStream();
		 
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
		out.close();
	}
}
