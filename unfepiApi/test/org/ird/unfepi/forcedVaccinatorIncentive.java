package org.ird.unfepi;


public class forcedVaccinatorIncentive {

	/*public void forcedIncentivization(){
		ServiceContext sc = Context.getServices();
		Date prevfireTime = null;
		try{
			prevfireTime = sc.getIncentiveService().getAllVaccinatorIncentiveEvent(0, 1, true, null).get(0).getDateOfEvent();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Date dataDateRangeLower = prevfireTime==null?new Date(new Date().getTime()-1000*60*60*24*60L):prevfireTime;
		Date dataDateRangeUpper = DateUtils.truncateDatetoDate(DateUtils.subtractInterval(new Date(), 1, TIME_INTERVAL.DAY));
		try{
			//GlobalParams.INCENTIVEJOBLOGGER.info("Running Job: "+jxc.getJobDetail().getFullName()+";prev firetime:"+jxc.getPreviousFireTime());

			List<Vaccinator> vaccl = sc.getVaccinationService().getAllVaccinator(true, null);
			
			//GlobalParams.INCENTIVEJOBLOGGER.info("Number of vaccinators participating in lottery: "+vaccl.size());

			VaccinatorIncentiveEvent vlevent = new VaccinatorIncentiveEvent();
			vlevent.setDateOfEvent(new Date());
			vlevent.setDataRangeDateLower(dataDateRangeLower);
			vlevent.setDataRangeDateUpper(dataDateRangeUpper);
			
			Integer vleventId = Integer.parseInt(sc.getIncentiveService().saveVaccinatorIncentiveEvent(vlevent).toString());
			
			for (Vaccinator vaccinator : vaccl) 
			{
				try{
					String sqlenr = "select v.childid from vaccination as v " +
							"where v.vaccinatorId="+vaccinator.getMappedId()+
							" and (v.vaccineid = 1 " +
							"or v.vaccinationrecordnum in " +
							"  (select v2.vaccinationrecordnum from vaccination v2 " +
							"  where v2.childId=v.childId " +
							"  and v2.vaccineid = 2 " +
							"  and v2.vaccinationdate is not null " +
							"  and not exists (select childid from vaccination where childid=v2.childid and vaccineid =1)))" +
							(prevfireTime == null ? "" : "  and v.vaccinationdate between '"+GlobalParams.SQL_DATE_FORMAT.format(dataDateRangeLower)+"' and '"+GlobalParams.SQL_DATE_FORMAT.format(dataDateRangeUpper)+"' ");
					//System.out.println(sqlenr);
					int enrollmentFormCount = sc.getCustomQueryService().getDataBySQL(sqlenr).size();
					
					String sqlfup = "select v.childid, v.vaccineid, v.vaccinationrecordnum from vaccination as v " +
							"where v.vaccinatorId="+vaccinator.getMappedId()+
							" and v.vaccineid <> 1 " +
							"and v.vaccinationrecordnum not in " +
							"  (select v2.vaccinationrecordnum from vaccination v2 " +
							"  where v2.childId=v.childId " +
							"  and v2.vaccineid = 2 " +
							"  and v2.vaccinationdate is not null " +
							"  and not exists (select childid from vaccination where childid=v2.childid and vaccineid =1)) " +
							(prevfireTime == null ? "" : "  and v.vaccinationdate between '"+GlobalParams.SQL_DATE_FORMAT.format(dataDateRangeLower)+"' and '"+GlobalParams.SQL_DATE_FORMAT.format(dataDateRangeUpper)+"' ") +
							"and v.vaccinationstatus <> 'pending'";
					//System.out.println(sqlfup);

					int followupFormCount = sc.getCustomQueryService().getDataBySQL(sqlfup).size();
					
					int totalFormsFilled = screeningFormCount + enrollmentFormCount	+ followupFormCount;
					
					List<VaccinatorIncentiveParams> probabilityparaml = sc.getIncentiveService().findVaccinatorIncentiveParamsByCriteria(null, null, totalFormsFilled, totalFormsFilled, 0, 10, true, null);
					VaccinatorIncentiveParams probabilityparam = null;
					Boolean hasWon = null;
					Float amount = null;
					
					if(probabilityparaml.size() > 0){
						probabilityparam = probabilityparaml.get(0);
						
						hasWon = IncentiveUtils.determineIncentiveWon(probabilityparam.getProbability().intValue());
						amount = hasWon ? probabilityparam.getDenomination() : null;
					}
					
					VaccinatorIncentiveParticipant vlparti = new VaccinatorIncentiveParticipant();
					vlparti.setCriteriaElementValue(null);
					vlparti.setIsIncentivised(hasWon);
					vlparti.setVaccinatorId(vaccinator.getMappedId());
					vlparti.setVaccinatorIncentiveEventId(vleventId);
					vlparti.setVaccinatorIncentiveParamsId(probabilityparam == null ? null : probabilityparam.getVaccinatorIncentiveParamsId());
					
					int vlpartiId = Integer.parseInt(sc.getIncentiveService().saveVaccinatorIncentiveParticipant(vlparti).toString());
					
					VaccinatorIncentiveWorkProgress vlworkproge = new VaccinatorIncentiveWorkProgress();
					vlworkproge.setFormOrTableType(FormType.ENROLLMENT_ADD.name());
					vlworkproge.setWorkAmount(enrollmentFormCount);
					vlworkproge.setVaccinatorIncentiveParticipantId(vlpartiId);
					
					VaccinatorIncentiveWorkProgress vlworkprogf = new VaccinatorIncentiveWorkProgress();
					vlworkprogf.setFormOrTableType(FormType.FOLLOWUP_ADD.name());
					vlworkprogf.setWorkAmount(followupFormCount);
					vlworkprogf.setVaccinatorIncentiveParticipantId(vlpartiId);
					
					VaccinatorIncentiveWorkProgress vlworkprogs = new VaccinatorIncentiveWorkProgress();
					vlworkprogs.setFormOrTableType(FormType.SCREENING_ADD.name());
					vlworkprogs.setWorkAmount(screeningFormCount);
					vlworkprogs.setVaccinatorIncentiveParticipantId(vlpartiId);
					
					sc.getIncentiveService().saveVaccinatorIncentiveWorkProgress(vlworkprogs);
					sc.getIncentiveService().saveVaccinatorIncentiveWorkProgress(vlworkproge);
					sc.getIncentiveService().saveVaccinatorIncentiveWorkProgress(vlworkprogf);
					
					if(hasWon != null && hasWon){
						VaccinatorIncentiveTransaction vltrans = new VaccinatorIncentiveTransaction();
						vltrans.setAmountDue(amount);
						vltrans.setCreatedDate(new Date());
						vltrans.setTransactionStatus(TranscationStatus.DUE);
						vltrans.setVaccinatorId(vaccinator.getMappedId());
						vltrans.setVaccinatorIncentiveEventId(vleventId);
						
						sc.getIncentiveService().saveVaccinatorIncentiveTransaction(vltrans);
					}
					
					sc.commitTransaction();
				}
				catch (Exception e) {
					e.printStackTrace();
					GlobalParams.INCENTIVEJOBLOGGER.error("Error running job for vaccinator:"+vaccinator.getMappedId()+":"+vaccinator.getFullName() ,e);
				}
			}
			
			// reschedule if cron changed
			String latestInterval = Context.getIRSetting(SmserGlobals.VACCINATOR_LOTTERY_INTERVAL_SETTING, SmserGlobals.VACCINATOR_LOTTERY_DEFAULT_INTERVAL);
			String latestTime = Context.getIRSetting(SmserGlobals.VACCINATOR_LOTTERY_TIME_SETTING, SmserGlobals.VACCINATOR_LOTTERY_DEFAULT_TIME);
			DateIntervalTrigger currentTrigger = (DateIntervalTrigger)jxc.getTrigger();
			int currentInterval = currentTrigger.getRepeatInterval();
			String currentTime = new SimpleDateFormat("HH:mm:ss").format(currentTrigger.getStartTime());
			
			GlobalParams.INCENTIVEJOBLOGGER.info(jxc.getJobDetail().getFullName()+"--latestInterval:"+latestInterval+",latestTime:"+latestTime+"--currentInterval:"+currentInterval+",currentTime:"+currentTime);

			if(currentInterval != Integer.parseInt(latestInterval) 
					|| !latestTime.equalsIgnoreCase(currentTime)) {
				try 
				{
					String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
					Date startDate = null;
					try{
						startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(todayDate+" "+latestTime);
						startDate = new Date(startDate.getTime()+1000*60*60*24L*Integer.parseInt(latestInterval));
					}
					catch (Exception e) {
						e.printStackTrace();
					}

					currentTrigger.setRepeatInterval(Integer.parseInt(latestInterval));
					currentTrigger.setStartTime(startDate);
					
					GlobalParams.INCENTIVEJOBLOGGER.info("next fire time: "+jxc.getScheduler().rescheduleJob(SmserGlobals.VACCINATOR_LOTTERY_JOBNAME, SmserGlobals.VACCINATOR_LOTTERY_JOBGROUP, currentTrigger));
					GlobalParams.INCENTIVEJOBLOGGER.info("next fire times(5)"+TriggerUtils.computeFireTimes(jxc.getScheduler().getTrigger(SmserGlobals.VACCINATOR_LOTTERY_TRIGGERNAME, SmserGlobals.VACCINATOR_LOTTERY_TRIGGERGROUP), null, 5));

				} 
				catch (SchedulerException e) {
					GlobalParams.INCENTIVEJOBLOGGER.error(jxc.getJobDetail().getFullName() ,e);
					e.printStackTrace();
				}
			}
		}
		catch (Exception e) {
			GlobalParams.INCENTIVEJOBLOGGER.error("Error running job:" ,e);
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}
*/}
