/**
 * 
 */
package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Women;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.StringUtils;

/**
 * @author Safwan
 *
 */
public class WomenDashboardController extends DataDisplayController {

	@Override
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		Map<String, Object> model = new HashMap<String, Object>();
		LoggedInUser user = UserSessionUtils.getActiveUser(req);
		
		String programId = req.getParameter("womenId");
		ServiceContext sc = Context.getServices();
		try{
			if(!StringUtils.isEmptyOrWhitespaceOnly(programId)){
				Women women = sc.getWomenService().findById(Integer.parseInt(programId));
				if(women != null){
					String hqlvaccination = "FROM WomenVaccination v " +
							" LEFT JOIN FETCH v.vaccine vc " +
							" LEFT JOIN FETCH v.vaccinationCenter center " +
							" LEFT JOIN FETCH center.idMapper centerId " +
							" LEFT JOIN FETCH v.vaccinator vctor " +
							" LEFT JOIN FETCH vctor.idMapper vctorId " +
							" WHERE v.womenId = " + women.getMappedId() + 
							" ORDER BY IFNULL(vaccinationDate, 99999999999999) ASC, vaccinationDuedate ASC ";
					List vaccinations = sc.getCustomQueryService().getDataByHQL(hqlvaccination );
					
					/*String hqlchildlottery = "FROM ChildLottery cl " +
							" LEFT JOIN FETCH cl.vaccination v " +
							" LEFT JOIN FETCH v.vaccine vc " +
							" LEFT JOIN FETCH v.vaccinationCenter center " +
							" LEFT JOIN FETCH center.idMapper centerId " +
							" LEFT JOIN FETCH v.vaccinator vctor " +
							" LEFT JOIN FETCH vctor.idMapper vctorId " +
							" LEFT JOIN FETCH cl.storekeeper stork " + 
							" LEFT JOIN FETCH cl.storekeeper.idMapper storkId " + 
							" WHERE v.childId = " + women.getMappedId() +
							" ORDER BY v.vaccinationDate ASC";
					List childlotteries = sc.getCustomQueryService().getDataByHQL(hqlchildlottery);
					
					String hqlchildreminders = "SELECT vc.name vaccineName, " +
							" (SELECT IF(hasApprovedReminders,'true','false') FROM lotterysms WHERE mappedId="+women.getMappedId()+" AND DATE(datePreferenceChanged) <= DATE(v.vaccinationDuedate) ORDER BY datePreferenceChanged DESC LIMIT 1), " +
							" CASE WHEN sms1.reminderstatus='pending' THEN 'SCHEDULED' ELSE sms1.reminderstatus END sms1ReminderStatus, DATE_FORMAT(sms1.sentDate, '%d-%m-%Y %H:%i') sms1SentDate, " +
							" CASE WHEN sms2.reminderstatus='pending' THEN 'SCHEDULED' ELSE sms2.reminderstatus END sms2ReminderStatus, DATE_FORMAT(sms2.sentDate, '%d-%m-%Y %H:%i') sms2SentDate, " +
							" CASE WHEN sms3.reminderstatus='pending' THEN 'SCHEDULED' ELSE sms3.reminderstatus END sms3ReminderStatus, DATE_FORMAT(sms3.sentDate, '%d-%m-%Y %H:%i') sms3SentDate " +
							//" CASE WHEN smsl.reminderstatus='pending' THEN 'SCHEDULED' ELSE smsl.reminderstatus END smslReminderStatus, DATE_FORMAT(smsl.sentDate, '%d-%m-%Y %H:%i') smslSentDate " +
							" FROM vaccination v " +
							" LEFT JOIN vaccine vc ON v.vaccineid=vc.vaccineId " +
							" LEFT JOIN remindersms sms1 ON v.vaccinationRecordNum = sms1.vaccinationRecordNum AND sms1.reminderId = (SELECT reminderId FROM reminder WHERE remindername = 'REM_VACC_DAY_N1') " +
							" LEFT JOIN remindersms sms2 ON v.vaccinationRecordNum = sms2.vaccinationRecordNum AND sms2.reminderId = (SELECT reminderId FROM reminder WHERE remindername = 'REM_VACC_DAY_0') " +
							" LEFT JOIN remindersms sms3 ON v.vaccinationRecordNum = sms3.vaccinationRecordNum AND sms3.reminderId = (SELECT reminderId FROM reminder WHERE remindername = 'REM_VACC_DAY_6') " +
							//" LEFT JOIN remindersms smsl ON v.vaccinationRecordNum = smsl.vaccinationRecordNum AND smsl.reminderId = (SELECT reminderId FROM reminder WHERE remindername = 'REM_LOTTERY_WON_SMS')" +
							" WHERE v.childId = " + women.getMappedId() +
							" ORDER BY IFNULL(v.vaccinationDate, 99999999999999) ASC, v.vaccinationDuedate ASC ";
					List childreminders = sc.getCustomQueryService().getDataBySQL(hqlchildreminders);*/
					
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("child", women);
					map.put("address", sc.getDemographicDetailsService().getAddress(women.getMappedId(), true, null));
					map.put("contacts", sc.getDemographicDetailsService().getContactNumber(women.getMappedId(), true, null));
					map.put("vaccinations", vaccinations);
					/*map.put("childLotteries", childlotteries);
					map.put("childReminders", childreminders);
					*/
					addModelAttribute(model, "datalist", map);
				}
				else {
					addModelAttribute(model, "errorMessage", "No women found with given id.");
				}
			}
			
			return showForm(model);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		return null;
	}

}
