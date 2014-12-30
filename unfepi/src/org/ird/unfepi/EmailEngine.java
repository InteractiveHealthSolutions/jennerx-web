package org.ird.unfepi;

import ird.xoutTB.emailer.emailServer.EmailServer;
import ird.xoutTB.emailer.exception.EmailException;

import java.util.Arrays;
import java.util.Properties;

import javax.mail.MessagingException;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.utils.Utils;
import org.ird.unfepi.utils.reporting.ExceptionUtil;
/**
 *  donot forget to instantiate EmailEngine by calling instantiateEmailEngine(Properties props)
 *  properties are mendatory required to get smtp host for email server
 *  
 *  mail.transport.protocol=smtp 	(for example)
 *	mail.host=smtp.gmail.com 		(for example)
 *	mail.user.username=immunization.reminder@gmail.com (for example)
 *	mail.user.password=xxxxxxxx 	(for example)
 *	mail.smtp.auth=true
 *	mail.smtp.port=465				(for example)
 *
 * call this method just only once in your application as it will make singleton instance 
 * of EmailEngine and calling it again and again will have no effect
 * 
 * @author maimoonak
 *
 */
public class EmailEngine {
	private static EmailEngine _instance;
	private static EmailServer emailer;

	private EmailEngine() {
	}
	
	public static void instantiateEmailEngine(Properties props) throws EmailException{
		if(_instance == null)
		{
			_instance = new EmailEngine();

			if(emailer==null){
				emailer=new EmailServer(props);
			}
			else
			{
				throw new InstantiationError("EmailServer have aready an instance instantiated");
			}
		}
		else
		{
			throw new InstantiationError("An instance of EmailEngine already exists");
		}
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public static EmailEngine getInstance() {
		if(_instance == null){
			throw new InstantiationError("EmailEngine not instantiated");
		}
		return _instance;
	}
	
	public EmailServer getEmailer(){
		return emailer;
	}
	
	public boolean sendSimpleMail(String[] recipients, String subject, String message){
		try {
			return getEmailer().postSimpleMail(recipients, subject, message, "unfepi@gmail.org");
		} catch (MessagingException e) {
			logUnsentEmails(recipients, subject, message, e);
		}
		return false;
	}
	
	public boolean sendHtmlMail(String[] recipients, String subject, String message){
		try {
			return getEmailer().postHtmlMail(recipients, subject, message, "unfepi@gmail.org");
		} catch (MessagingException e) {
			logUnsentEmails(recipients, subject, message,e);
		}
		return false;
	}
		
	private void logUnsentEmails(String[] recipients,String subject, String message, MessagingException e){
		GlobalParams.FILELOGGER.error(ExceptionUtil.getStackTrace(e));
		GlobalParams.EMAILSLOGGER.error(
				"*********************************************************************\n"+
				"SUBJECT 	:\t"+subject+"\n"+
				"RECIPIENTS :\t"+Utils.getListAsString(Arrays.asList(recipients), " , ")+"\n"+
				"CONTENT	:\t"+message+"\n"+
				"*********************************************************************\n");
	}
	
	public synchronized void emailErrorReportToAdmin(String subject,String message){
		String[] recipients=new String[]{Context.getSetting(GlobalParams.ADMIN_EMAIL_PROPERTY,"maimoona.kausar@irdinformatics.org")};
		try {
			GlobalParams.FILELOGGER.error("Sending error report to admin :"+message);
			getEmailer().postSimpleMail(recipients, subject, message, "unfepi@gmail.org");
		} catch (MessagingException e) {
			logUnsentEmails(recipients, subject, message,e);
		}
	}
	
	public void emailErrorReportToAdminAsASeparateThread(String subject,String message){
		final String msg=message;
		final String sub=subject;
		Runnable emailr = new Runnable() {
			@Override
			public void run() {
				emailErrorReportToAdmin(sub, msg);
			}
		};
		new Thread(emailr).start();  
	}
}
