package org.ird.unfepi.utils;

import java.lang.reflect.Field;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ird.unfepi.GlobalParams.SmsTiming;
import org.ird.unfepi.beans.APVRRAnalysisGridRow;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Reminder;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.Response;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.model.exception.ReminderDataException;
import org.ird.unfepi.model.exception.VaccinationDataException;

public class IRUtils {
	@SuppressWarnings("deprecation")
	public static ArmDayTiming makeReminderTime(SmsTiming preferredSmsTiming, List<Reminder> reminderGroup) {
		Time time = null;

		if (preferredSmsTiming.equals(SmsTiming.MORNING)) {
			time = new Time( Utils.getRandomNumber( 8 , 10 ) , Utils
					.getRandomNumber( 0 , 55 ) , 12 );
		}
		else if(preferredSmsTiming.equals(SmsTiming.AFTERNOON)) {
			time = new Time( Utils.getRandomNumber( 12 , 14 ) , Utils
					.getRandomNumber( 0 , 55 ) , 12 );
		}
		else if (preferredSmsTiming.equals(SmsTiming.EVENING)) {
			time = new Time( Utils.getRandomNumber( 15 , 19 ) , Utils
					.getRandomNumber( 0 , 55 ) , 12 );
		}
		else {
			time = new Time(9, 45, 12);
		}
		
		ArmDayTiming timings = new ArmDayTiming();

		for (Reminder rem : reminderGroup) {
			if(rem.getIsDefaultTimeEditable()){
				timings.addDayTiming(rem.getGapEventDay(), time);
			}
			else{
				timings.addDayTiming(rem.getGapEventDay(), rem.getDefaultReminderTime());
			}
		}
		
		return timings;
	}
	
	public static String getListAsString(List<Short> collection,String separator){
		StringBuilder strb=new StringBuilder();
		int len=collection.size();
		
		int index=0;
		for (Short shor : collection) {
			if(index==len-1){
				strb.append(shor);
			}else{
				strb.append(shor+separator);
			}
			index++;
		}
		return strb.toString();
	}
	  
	public static String getListAsString(List<String> collection,String separator, String encapsulator){
		StringBuilder strb=new StringBuilder();
		int len=collection.size();
		
		int index=0;
		for (String string : collection) {
			if(encapsulator != null){
				string = encapsulator+string+encapsulator;
			}
			
			if(index==len-1){
				strb.append(string);
			}else{
				strb.append(string+separator);
			}
			index++;
		}
		return strb.toString();
	}

	public static int findStringInList(List<String> list, String string, boolean ignorecase){
		int i = 0;
		for (String s : list) {
			if((ignorecase&&s.equalsIgnoreCase(string))	|| s.equals(string)){
				return i;
			}
			
			i++;
		}
		
		return -1;
	}
	
	public static String getArrayAsString(String[] collection,String separator, String encapsulator){
		StringBuilder strb=new StringBuilder();
		int len=collection.length;
		
		int index=0;
		for (String string : collection) {
			if(encapsulator != null){
				string = encapsulator+string+encapsulator;
			}
			
			if(index==len-1){
				strb.append(string);
			}else{
				strb.append(string+separator);
			}
			index++;
		}
		return strb.toString();
	}
	
	public static Map convertEntrySetToMap(Set<Entry<Object, Object>> entrySet){
	    Map<Object, Object> mapFromSet = new HashMap<Object, Object>();
	    for(Entry<Object, Object> entry : entrySet)
	    {
	        mapFromSet.put(entry.getKey(), entry.getValue());
	    }
		return mapFromSet;
	}
	
	public static String convertToString(Object obj) {
		StringBuilder s = new StringBuilder(obj.getClass().getSimpleName());
		s.append("[");
		Field[] f = obj.getClass().getDeclaredFields();
		for (Field field : f) {
			try {
				field.setAccessible(true);
				s.append(field.getName() + "=" + field.get(obj));
				s.append(";");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int i = s.lastIndexOf(";");
		if (i != -1)
			s.deleteCharAt(i);

		s.append("]");

		return s.toString();
	}
	
	/** reminder history and child response data must be in descending order by date (latest date being first one)
	 * @throws VaccinationDataException 
	 * @throws ReminderDataException */
	public static APVRRAnalysisGridRow addRecord(Child child) throws VaccinationDataException, ReminderDataException {
		APVRRAnalysisGridRow recordGrid=new APVRRAnalysisGridRow();
		recordGrid.setChild(child);
		
		ServiceContext sc = Context.getServices();
		try{
		List<Vaccination> vaccl=sc.getVaccinationService().findVaccinationRecordByCriteria(child.getMappedId(), null, null, null, null, null, null, null, null, null, null, false, 0, 111, true, new String[]{"vaccine", "createdByUserId", "lastEditedByUserId"}, null);
		for (Vaccination pv : vaccl) {	
			List<ReminderSms> reml = sc.getReminderService().findByCriteria(null, null, false, null, pv.getVaccinationRecordNum(), true, new String[]{"reminder", "createdByUserId", "lastEditedByUserId"});
			
			if(reml==null){
				reml=new ArrayList<ReminderSms>();
			}
			
			List<Response> pr = sc.getCommunicationService().getResponseByCriteria(null, null, false, null, null, null, null, null, null, Vaccination.class, pv.getVaccinationRecordNum(), null, 0, 100, true, null, null);
			
			Vaccinator vaccinator = pv.getVaccinatorId()==null?null:sc.getVaccinationService().findVaccinatorById(pv.getVaccinatorId(), true, new String[]{"idMapper"});
			VaccinationCenter center=pv.getVaccinationCenterId()==null?null:sc.getVaccinationService().findVaccinationCenterById(pv.getVaccinationCenterId(), true, new String[]{"idMapper"});
			recordGrid.addRecord(pv, vaccinator, center, reml, pr);
		}
		}
		finally{
		sc.closeSession();
		}
		return recordGrid;
	}

	public static void main(String[] args) {
		List<String> l = new ArrayList<String>();
		l.add("abc");
		l.add("aBc");
		l.add("ac");
		l.add("c");
		l.add("bc");
		l.add("ab");
		l.add("aBBc");
		l.add("abBc");
		
		System.out.println(findStringInList(l, "aBBc", false));
	}
}
