/*import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.ird.unfepi.model.ArmDayReminder;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.CsvUpload;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.model.ReminderSms.REMINDER_STATUS;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.utils.Utils;

public class ScriptToIncMeasles {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		SessionFactory sf = new Configuration().configure()
			.buildSessionFactory();
		Session ss = sf.openSession();
		Transaction t = ss.beginTransaction();
		// TODO add commit transaction before final running of script
		List<Child> chl = ss.createQuery("from Child").list();
		for (Child child : chl) {
			if (child.getBirthdate() != null) {
				Vaccination mv = new Vaccination();
				mv.setChild(child);
				mv.setDescription("Measles1 vaccination added by script.");
				mv.setCreatedDate(new Date());
				mv.setCreatedByUserId("script");
				mv.setCreatedByUserName("manual");
				Calendar c = Calendar.getInstance();
				c.setTime(child.getBirthdate());
				c.add(Calendar.MONTH, 9);
				mv.setVaccinationDuedate(c.getTime());
				mv.setVaccinationStatus(VACCINATION_STATUS.PENDING);
				mv.setVaccine((Vaccine) ss
					.createQuery("from Vaccine where name='Measles1'")
					.list().get(0));
				Serializable mid = ss.save(mv);
				for (ArmDayReminder armday : child.getArm().getArmday()) {
					ReminderSms remsms = new ReminderSms();
					remsms.setCreatedDate(new Date());
					remsms.setCreatedByUserId("script");
					remsms.setCreatedByUserName("manual");
					remsms.setDayNumber(armday.getId().getDayNumber());
					remsms.setDueTime(armday.getDefaultReminderTime());
					Calendar cal = Calendar.getInstance();
					cal.setTime(mv.getVaccinationDuedate());
					cal.set(Calendar.HOUR_OF_DAY, remsms.getDueTime()
						.getHours());
					cal.set(Calendar.MINUTE, remsms.getDueTime().getMinutes());
					cal.set(Calendar.SECOND, remsms.getDueTime().getSeconds());
					cal.add(Calendar.DATE, armday.getId().getDayNumber());
					remsms.setDueDate(cal.getTime());
					remsms.setChild(child);
					if (remsms.getDueDate().before(new Date())) {
						remsms.setStatus(REMINDER_STATUS.NA);
						remsms.setSmsCancelReason("Sms not sent because feature was not included in study");
					}
					else {
						remsms.setStatus(REMINDER_STATUS.PENDING);
					}
					remsms.setVaccinationRecordNum(Long.parseLong(mid
						.toString()));
					ss.save(remsms);
				}
			}
			else {
				System.out.println("ID:" + child.getChildId()
					+ " have DOB null");
			}
		}
		t.commit();
		ss.close();
		
		List oll = ss.createSQLQuery("select record_number, date_uploaded, csv_file,upload_report from csv_upload").list();
		for (Object object : oll) {
			Object[] record = (Object[]) object;
			Date uploaddate = (Date) record[1];
			String csvFilePath = "/usr/share/tomcat6/imrscsvfiles/datafiles/csv"+uploaddate.getTime()+".csv";
			String reportFilePath = "/usr/share/tomcat6/imrscsvfiles/uploadreport/report"+uploaddate.getTime()+".txt";
			try{
				File file = new File(csvFilePath);
				if(!file.exists()){
					File par = new File(file.getParent());
					if(!par.exists()){
						par.mkdirs();
					}
				}
				
				OutputStream os = new FileOutputStream(file);
				os.write(record[2].toString().getBytes());
				os.flush();
				os.close();
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("CSV file not created for record number "+record[0]);
			}
			try{
				File file = new File(reportFilePath);
				if(!file.exists()){
					File par = new File(file.getParent());
					if(!par.exists()){
						par.mkdirs();
					}
				}
				
				OutputStream os = new FileOutputStream(file);
				os.write(record[3].toString().getBytes());
				os.flush();
				os.close();
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("report file not created for record number "+record[0]);
			}
			
			ss.createSQLQuery("update csv_upload set csv_file='"+csvFilePath+"' where record_number="+record[0]).executeUpdate();
			ss.createSQLQuery("update csv_upload set upload_report='"+reportFilePath+"' where record_number="+record[0]).executeUpdate();

		}
	}
}
*/