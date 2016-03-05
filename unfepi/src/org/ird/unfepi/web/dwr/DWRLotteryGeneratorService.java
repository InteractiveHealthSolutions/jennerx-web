package org.ird.unfepi.web.dwr;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import org.directwebremoting.WebContextFactory;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.utils.UserSessionUtils;

public class DWRLotteryGeneratorService {

	public boolean isIdExists(String programId){
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){
			//return "Session expired. Logout from application and login again";
			try {
				WebContextFactory.get().forwardToString("login.htm");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ServiceContext sc = Context.getServices();
		try {
			List resl = sc.getCustomQueryService().getDataBySQL("select count(*) from child c join idmapper i on c.mappedId=i.mappedId where i.programId='"+programId+"'");
			return resl.size() == 0 ? false: Integer.parseInt(resl.get(0).toString())>0;
		}
		finally {
			sc.closeSession();
		}
	}
	
	/*public String verifyParams(String epiNumber, Integer vaccinationCenterId, Integer vaccineId, String programId, Integer timeliness, String lotteryType){
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){
			try {
				WebContextFactory.get().forwardToString("login.htm");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ServiceContext sc = Context.getServices();
		try {
			IdMapper chl = sc.getIdMapperService().findIdMapper(programId);

			if(!lotteryType.equalsIgnoreCase(GlobalParams.LotteryType.MISSING_ENROLLMENT.toString())
					&& chl == null)
			{
					return "No enrollment found for ID "+programId;
			}
			else if(lotteryType.equalsIgnoreCase(GlobalParams.LotteryType.MISSING_ENROLLMENT.toString())
					&& chl != null)
			{
				return "An enrollment already exists on ID "+programId;
			}
			else if(lotteryType.equalsIgnoreCase(GlobalParams.LotteryType.MISSING_ENROLLMENT.toString())
					 && sc.getCustomQueryService().getDataBySQL("select epiNumber from vaccination where vaccinationCenterId="+vaccinationCenterId+" and epiNumber='"+epiNumber+"'").size()>0){
				return "EPI number already occupied";
			}

			if(chl != null){
				List pendvaccl = sc.getCustomQueryService().getDataBySQL("select vaccinationDuedate, vaccinationStatus, hasApprovedLottery, vaccinationRecordNum from vaccination where vaccineId="+vaccineId+" and childId="+chl.getMappedId()+" order by vaccinationDuedate desc");
	
				if(lotteryType.equalsIgnoreCase(GlobalParams.LotteryType.PENDING_FOLLOWUP.toString()))
				{
					// cannot check for pendvaccl.size()==0 as P1 followup may be done with Enrollment so may not be existing in DB
					//if vaccine is penta1 check any BCG form is pending/unfilled
					if(pendvaccl.size() == 0){
						if(vaccineId == Integer.parseInt(sc.getCustomQueryService().getDataBySQL("select vaccineId from vaccine where (name like 'penta1%' OR name like 'mealses1%') ").get(0).toString())){
							List p1FupUnenr = sc.getCustomQueryService().getDataBySQL("select dateEncounterEntered from encounter join encounterresults using (p1id,p2id,encounterid) where p1id="+chl.getMappedId()+" and encounterType='"+EncounterType.LOTTERY_GEN.toString()+"' and element='VACCINE_NAME_RECEIVED' and (value like 'bcg%' OR value like 'penta1%') ");
							if(p1FupUnenr.size()==0){
								return "Neither any PENDING Vaccination record found for ID "+programId+" for given vaccine nor any unfilled form found.";
							}
						}
						else {
							return "NO PENDING vaccination was not found for given vaccine for ID "+programId;
						}
					}
					
					if(pendvaccl.size() > 0){
							Object[] arr = (Object[]) pendvaccl.get(0);
							if(arr[1].toString().equalsIgnoreCase(VACCINATION_STATUS.VACCINATED.toString())){
								return "Vaccine found to be VACCINATED for ID "+programId;
							}
							
							Calendar cal=Calendar.getInstance();
							cal.add(Calendar.DATE, 4);
							if(arr[1].toString().equalsIgnoreCase(VACCINATION_STATUS.PENDING.toString())
									&& !DateUtils.truncateDatetoDate((Date)arr[0]).before(cal.getTime())){
								return "Vaccine`s due date found to be PENDING for ID "+programId+" on "+(Date)arr[0]+" and minimum acceptable gap is 3 days before due date";
							}
							
							int actualinterv = DateUtils.differenceBetweenIntervals(new Date(), ((Date)arr[0]), TIME_INTERVAL.DATE);
							if(actualinterv != timeliness){
								return "Actual timeliness value calculated by system is "+actualinterv+" with vaccination duedate "+(Date)arr[0];
							}
					}
				}
				
				if(lotteryType.equalsIgnoreCase(GlobalParams.LotteryType.EXISTING.toString()))
				{
					if(pendvaccl.size() == 0){
						return "Vaccination Event record not found for ID "+programId;
					}
					else {
						Object[] arr = (Object[]) pendvaccl.get(0);
						if(!arr[1].toString().equalsIgnoreCase(VACCINATION_STATUS.VACCINATED.toString())){
							return "Vaccine was not found to be VACCINATED for ID "+programId;
						}
						// Lottery MUST have been approved
						else if(arr[2] == null || !arr[2].toString().equalsIgnoreCase("true")){
							return "Lottery not found to be approved for given Vaccine for ID "+programId;
						}
					}
				}
				
				if(lotteryType.equalsIgnoreCase(GlobalParams.LotteryType.PENDING_FOLLOWUP.toString()))
				{
					List<Vaccination> vaccl = sc.getVaccinationService().findVaccinationRecordByCriteria(chl.getMappedId(), null, null, vaccinationCenterId, null, null, null, null, null, null, VACCINATION_STATUS.VACCINATED, false, 0, 1, true, null, null);
					if(vaccl.size() > 0
							&& !StringUtils.isEmptyOrWhitespaceOnly(vaccl.get(0).getEpiNumber())
							&& !vaccl.get(0).getEpiNumber().equalsIgnoreCase(epiNumber)){
						return "ID "+programId+" on its last vaccine at given center was assigned EPI number "+vaccl.get(0).getEpiNumber();
					}
				}
				
				if(!lotteryType.equalsIgnoreCase(GlobalParams.LotteryType.EXISTING.toString()))
				{
					Object vcl = null;
					vcl = sc.getCustomQueryService().getDataBySQL("select count(*) from vaccination where epiNumber='"+epiNumber+"' and vaccinationCenterId="+vaccinationCenterId+" and childId <> "+chl.getMappedId()).get(0);
					if(Integer.parseInt(vcl.toString()) > 0){
						return "EPI number already occupied";
					}
				}
				
				if(ChildLottery.lotteryPreviouslyDone(vtnl.get(0).getVaccinationRecordNum(), sc)){
					return "ID "+programId+" already have performed lottery for given vaccine";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "SERVER ERROR OCCURRED:"+e.getMessage();
		}
		finally {
			sc.closeSession();
		}
		
		return "OK";
	}*/
}
