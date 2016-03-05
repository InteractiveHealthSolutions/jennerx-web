package org.ird.unfepi.autosys.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.ChildIncentive;
import org.ird.unfepi.model.Reminder.ReminderType;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.utils.Utils;

public class ReminderUtils {
	//change if vaccine thing is added and arm day reminders are provided on web interface
	@SuppressWarnings("unchecked")
	public static String pickupRandomReminderText(ReminderSms reminderSms, ServiceContext sc)
	{
		String text = "";
		List<String> rt=new ArrayList<String>();
		rt.addAll(reminderSms.getReminder().getReminderText());

		List<String> names=new ArrayList<String>();
		ChildIncentive vChildIncentive = null;
		Vaccination vaccination = Utils.initializeAndUnproxy(reminderSms.getVaccination());
		Child child = Utils.initializeAndUnproxy(vaccination.getChild());
		int num = Utils.getRandomNumber(rt.size());
		
		if(reminderSms.getReminder().getReminderType().equals(ReminderType.LOTTERY_WON_REMINDER)){
			vChildIncentive = sc.getIncentiveService().findChildIncentiveByVaccination(reminderSms.getVaccinationRecordNum(), true, null).get(0);
		}
		
		try{
			text=rt.get((num>0&&num<=rt.size())?num-1:0);
		}
		catch (Exception e) {
			e.printStackTrace();
			String deftext = "";
			if(reminderSms.getReminder().getReminderType().equals(ReminderType.NEXT_VACCINATION_REMINDER)){
				deftext = Context.getSetting("vaccination-reminder.default-reminder-text",
					 "Baraey meherbani muqarrarah tareekh par apnay bachay ko tika lagwanay k liyaya EPI center laen.");
			}
			else if(reminderSms.getReminder().getReminderType().equals(ReminderType.LOTTERY_WON_REMINDER)){
				deftext = Context.getSetting("lottery-reminder.default-reminder-text",
					 "Aap nay bachay ko vaccine lagwany par innam jeeta hay. Inam hasil karnay k liyay EPI center say mazid maloomat lain.");
			}
			text = deftext;
		}
		
		Matcher matcher = Pattern.compile("\\[\\[\\w+\\.\\w+]\\]").matcher(text);
		
		String textToSend=""+text;
		try{
			while (matcher.find()) {
				names.add(text.substring(matcher.start(),matcher.end()));
			}		
			for (String nm : names) {
				if(nm.matches("\\[\\[Child\\.\\w+\\]\\]")){
					Class<Child> pcls;
					try {
						pcls = (Class<Child>) Class.forName("org.ird.unfepi.model.Child");
						try {
							String fieldname=nm.replace("[[Child.","");
							fieldname=fieldname.replace("]]", "");
							Field[] aaa = pcls.getDeclaredFields();
							for (Field field : aaa) {
								if(field.getName().equalsIgnoreCase(fieldname)){
									field.setAccessible(true);
									String nameis = field.get(child).toString();
									if(nameis.toLowerCase().contains(" name")){
										nameis = "Baby";
									}
									textToSend=textToSend.replace(nm, nameis);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							textToSend=textToSend.replace(nm, "");
						} 
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}
				}
				else if(nm.matches("\\[\\[ReminderSms\\.\\w+\\]\\]")){
					Class<ReminderSms> pcls;
					try {
						pcls = (Class<ReminderSms>) Class.forName("org.ird.unfepi.model.ReminderSms");
						try {
							String fieldname=nm.replace("[[ReminderSms.","");
							fieldname=fieldname.replace("]]", "");
							Field[] aaa = pcls.getDeclaredFields();
							for (Field field : aaa) {
								if(field.getName().equalsIgnoreCase(fieldname)){
									field.setAccessible(true);
									textToSend=textToSend.replace(nm, field.get(reminderSms).toString());
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							textToSend=textToSend.replace(nm, "-----");
						} 
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}
			  }
			  else if(nm.matches("\\[\\[Vaccination.Day]\\]")){
				  Calendar c=Calendar.getInstance();
				  c.setTime(vaccination.getVaccinationDuedate());
				  textToSend=textToSend.replace("[[Vaccination.Day]]", c.getDisplayName(Calendar.DAY_OF_WEEK,2,Locale.US));
			  }
			  else if(nm.matches("\\[\\[Vaccination.Date]\\]")){
				  Calendar c=Calendar.getInstance();
				  c.setTime(vaccination.getVaccinationDuedate());
				  textToSend=textToSend.replace("[[Vaccination.Date]]", c.getTime().toString());
			  }
			  else if(nm.matches("\\[\\[Vaccination.Vaccine]\\]")){
				  Calendar c=Calendar.getInstance();
				  c.setTime(vaccination.getVaccinationDuedate());
				  textToSend=textToSend.replace("[[Vaccination.Vaccine]]", vaccination.getVaccine().getName());
			  }
			  else if(nm.matches("\\[\\[Incentive.Amount]\\]")){
				  textToSend=textToSend.replace("[[Incentive.Amount]]", vChildIncentive.getAmount().toString());
			  }
		  }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return textToSend;
	}
}
