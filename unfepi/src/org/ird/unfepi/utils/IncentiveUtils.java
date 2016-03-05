package org.ird.unfepi.utils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.GlobalParams.DownloadableType;
import org.ird.unfepi.GlobalParams.IncentiveWorkType;
import org.ird.unfepi.GlobalParams.VariableSettingType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.IncentiveStatus;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.model.VaccinatorIncentive;
import org.ird.unfepi.model.VaccinatorIncentiveEvent;
import org.ird.unfepi.model.VaccinatorIncentiveParticipant;
import org.ird.unfepi.model.VaccinatorIncentiveTransaction;
import org.ird.unfepi.model.VaccinatorIncentiveWorkProgress;
import org.ird.unfepi.model.VariableSetting;
import org.ird.unfepi.report.Csvee;
import org.ird.unfepi.report.CsveeRow;

public class IncentiveUtils {

	public static boolean determineLotteryWon(int probablilityPercent){
		return new Random().nextInt(100) < probablilityPercent;
	}
	
	public static boolean determineProbablityWon(int probablilityPercent){
		return new Random().nextInt(100) < probablilityPercent;
	}
	
	public static Integer determineIncentiveScheme(){
		ServiceContext sc = Context.getServices();
		Integer armId = null;

		try{
			int arm1total = Integer.parseInt(sc.getCustomQueryService().getDataBySQL("SELECT COUNT(DISTINCT v.childId) FROM childincentive i JOIN vaccination v on i.vaccinationRecordNum=v.vaccinationRecordNum WHERE i.armId=1").get(0).toString());
			int arm2total = Integer.parseInt(sc.getCustomQueryService().getDataBySQL("SELECT COUNT(DISTINCT v.childId) FROM childincentive i JOIN vaccination v on i.vaccinationRecordNum=v.vaccinationRecordNum WHERE i.armId=2").get(0).toString());;
			boolean arm1Apply = arm1total <= Integer.parseInt(Context.getSetting("arm.arm1.max-records", null));
			boolean arm2Apply = arm2total <= Integer.parseInt(Context.getSetting("arm.arm2.max-records", null));
			
			if(arm1Apply && arm2Apply){
				if(determineLotteryWon(50)){
					armId = 1;
				}
				else {
					armId = 2;
				}
			}
			else if(arm1Apply){
				armId = 1;
			}
			else if(arm2Apply){
				armId = 2;
			}
		}
		finally{
			sc.closeSession();
		}
		return armId;
	}
	
	public static String determineVerificationCode(){
		return String.valueOf(Utils.getRandomNumber(100000, 999999));
	}
	
	public static void doVaccinatorIncentivization(Date dateIncentivizationRangeLower, Date dateIncentivizationRangeUpper, User user){
		ServiceContext sc = Context.getServices();
		
		Date dataDateRangeLower = dateIncentivizationRangeLower;
		Date dataDateRangeUpper = dateIncentivizationRangeUpper;
		
		try{
			GlobalParams.INCENTIVEJOBLOGGER.info("Running Job: VaccinatorIncentivizor"/*+jxc.getJobDetail().getFullName()*//*+";prev firetime:"+jxc.getPreviousFireTime()*/);

			List<Vaccinator> vactorl= sc.getVaccinationService().getAllVaccinator(0, Integer.MAX_VALUE, true, null);
			
			GlobalParams.INCENTIVEJOBLOGGER.info("Number of Vaccinators participating in incentiviztion: "+vactorl.size());

			VaccinatorIncentiveEvent vievent = new VaccinatorIncentiveEvent();
			vievent.setDateOfEvent(new Date());
			vievent.setDataRangeDateLower(dataDateRangeLower);
			vievent.setDataRangeDateUpper(dataDateRangeUpper);
			
			Integer sieventId = Integer.parseInt(sc.getIncentiveService().saveVaccinatorIncentiveEvent(vievent).toString());
			
			Csvee csvProcess = new Csvee();
			
			CsveeRow headerrowProcess = new CsveeRow();
			headerrowProcess.addRowElement("Vaccination Center");
			headerrowProcess.addRowElement("Vaccinator ID");
			headerrowProcess.addRowElement("Name");
			headerrowProcess.addRowElement("Address");
			headerrowProcess.addRowElement("CNIC No.");
			headerrowProcess.addRowElement("Phone");
			headerrowProcess.addRowElement("EP Acc. No.");
			headerrowProcess.addRowElement("Incentivization Event Date");
			headerrowProcess.addRowElement("Incentivization Date From");
			headerrowProcess.addRowElement("Incentivization Date To");
			headerrowProcess.addRowElement("Vaccinations Done");
			headerrowProcess.addRowElement("Subtotal / Total Amount Due (Vaccinator)");
			headerrowProcess.addRowElement("EP Serv. Charges");
			headerrowProcess.addRowElement("Total");
			headerrowProcess.addRowElement("Incentive Calculation Formula");

			csvProcess.addHeader(headerrowProcess);
			
			Csvee csvAllTransactions = new Csvee();
			
			CsveeRow headerrowAllTransactions = new CsveeRow();
			headerrowAllTransactions.addRowElement("Scheme");
			headerrowAllTransactions.addRowElement("Vaccination Center UC");
			headerrowAllTransactions.addRowElement("Vaccination Center ID");
			headerrowAllTransactions.addRowElement("Vaccination Center Name");
			headerrowAllTransactions.addRowElement("Vaccinator ID");
			headerrowAllTransactions.addRowElement("Vaccinator Name");
			headerrowAllTransactions.addRowElement("Amount");
			headerrowAllTransactions.addRowElement("Vaccination ID");
			headerrowAllTransactions.addRowElement("Child ID");
			headerrowAllTransactions.addRowElement("Child Name");
			headerrowAllTransactions.addRowElement("Vaccine");
			headerrowAllTransactions.addRowElement("Vaccine Date");
			headerrowAllTransactions.addRowElement("Incentive Date");
			headerrowAllTransactions.addRowElement("Incentive Status");
			headerrowAllTransactions.addRowElement("Is Incentivized");
			headerrowAllTransactions.addRowElement("Incentive Record ID");

			csvAllTransactions.addHeader(headerrowAllTransactions);
			
			for (Vaccinator vaccinator : vactorl) {
				try{
					List<VaccinatorIncentive> vinc = sc.getIncentiveService().findVaccinatorIncentiveByCriteriaVaccinatorIncentivized(vaccinator.getMappedId(), null, IncentiveStatus.AVAILABLE, false, null);
					float totalAmount = (float) 0.0;
					int totalVaccinations = 0;
					
					for (VaccinatorIncentive vid : vinc) {
						
						totalAmount += vid.getAmount()==null?0:vid.getAmount();
						totalVaccinations += 1;
						
						vid.setIncentiveStatus(IncentiveStatus.WAITING);
						sc.getIncentiveService().updateVaccinatorIncentive(vid);
						
						CsveeRow alltrDatarowProcess = new CsveeRow();
						alltrDatarowProcess.addRowElement(vid.getArm().getArmName());
						alltrDatarowProcess.addRowElement(vid.getVaccination().getVaccinationCenter().getIdMapper().getIdentifiers().get(0).getLocation().getName());
						alltrDatarowProcess.addRowElement(vid.getVaccination().getVaccinationCenter().getIdMapper().getIdentifiers().get(0).getIdentifier());
						alltrDatarowProcess.addRowElement(vid.getVaccination().getVaccinationCenter().getName());
						alltrDatarowProcess.addRowElement(vid.getVaccinator().getIdMapper().getIdentifiers().get(0).getIdentifier());
						alltrDatarowProcess.addRowElement(vid.getVaccinator().getFullName());
						alltrDatarowProcess.addRowElement(vid.getAmount());
						alltrDatarowProcess.addRowElement(vid.getVaccination().getVaccinationRecordNum());
						alltrDatarowProcess.addRowElement(vid.getVaccination().getChild().getIdMapper().getIdentifiers().get(0).getIdentifier());
						alltrDatarowProcess.addRowElement(vid.getVaccination().getChild().getFullName());
						alltrDatarowProcess.addRowElement(vid.getVaccination().getVaccinationRecordNum());
						alltrDatarowProcess.addRowElement(vid.getVaccination().getVaccine().getName());
						alltrDatarowProcess.addRowElement(vid.getVaccination().getVaccinationDate());
						
						alltrDatarowProcess.addRowElement(vid.getIncentiveDate());
						alltrDatarowProcess.addRowElement(vid.getIncentiveStatus());
						alltrDatarowProcess.addRowElement(vid.getIsIncentivized());
						alltrDatarowProcess.addRowElement(vid.getVaccinatorIncentiveId());
						
						csvAllTransactions.addData(alltrDatarowProcess);
					}
					
					Boolean isincentivized = null;
					Float incentamount = null;
					String caluculationDescr = null;
					Integer epcharges = null;
					
					isincentivized = totalAmount > 0;
					
					List<VariableSetting> epchargesl = sc.getIRSettingService().findByCriteria(null, VariableSettingType.EP_CHARGES.name(), null, null, totalAmount, totalAmount, true, 0, 1);
					
					if(epchargesl.size() > 0){
						epcharges = Integer.parseInt(epchargesl.get(0).getValue());
						incentamount = isincentivized ? (totalAmount + epcharges) : null;
						caluculationDescr = isincentivized ?
								("(TotalAmountDue+EPCharges) " + ": ("+totalAmount+"+"+epcharges+")"):null;
					}
					else {
						caluculationDescr = "UNABLE TO GET EP CHARGES";
					}
					
					VaccinatorIncentiveParticipant siparti = new VaccinatorIncentiveParticipant();
					siparti.setCriteriaElementValue((int) totalAmount);
					siparti.setIsIncentivised(isincentivized);
					siparti.setVaccinatorId(vaccinator.getMappedId());
					siparti.setVaccinatorIncentiveEventId(sieventId);
					siparti.setVaccinatorIncentiveParamsId(null);
					siparti.setDescription(caluculationDescr);
					int sipartiId = Integer.parseInt(sc.getIncentiveService().saveVaccinatorIncentiveParticipant(siparti).toString());
					
					VaccinatorIncentiveWorkProgress siworkproge = new VaccinatorIncentiveWorkProgress();
					siworkproge.setWorkType(IncentiveWorkType.TOTAL_VACCINATIONS.name());
					siworkproge.setWorkValue(Float.toString(totalVaccinations));
					siworkproge.setVaccinatorIncentiveParticipantId(sipartiId);
					
					VaccinatorIncentiveWorkProgress siworkproge2 = new VaccinatorIncentiveWorkProgress();
					siworkproge2.setWorkType(IncentiveWorkType.TOTAL_AMOUNT_DUE.name());
					siworkproge2.setWorkValue(Float.toString(totalAmount));
					siworkproge2.setVaccinatorIncentiveParticipantId(sipartiId);
					
					sc.getIncentiveService().saveVaccinatorIncentiveWorkProgress(siworkproge);
					sc.getIncentiveService().saveVaccinatorIncentiveWorkProgress(siworkproge2);
					
					if(isincentivized != null && isincentivized){
						VaccinatorIncentiveTransaction sitrans = new VaccinatorIncentiveTransaction();
						sitrans.setAmountDue(incentamount);
						sitrans.setCreatedDate(new Date());
						sitrans.setTransactionStatus(VaccinatorIncentiveTransaction.TranscationStatus.DUE);
						sitrans.setVaccinatorId(vaccinator.getMappedId());
						sitrans.setVaccinatorIncentiveEventId(sieventId);
						sitrans.setDescription(caluculationDescr);
						
						sc.getIncentiveService().saveVaccinatorIncentiveTransaction(sitrans);
					}
					
					CsveeRow datarowProcess = new CsveeRow();
					datarowProcess.addRowElement(vaccinator.getVaccinationCenter().getIdMapper().getIdentifiers().get(0).getIdentifier());
					datarowProcess.addRowElement(vaccinator.getIdMapper().getIdentifiers().get(0).getIdentifier());
					datarowProcess.addRowElement(vaccinator.getFullName());
					datarowProcess.addRowElement("");
					datarowProcess.addRowElement(vaccinator.getNic());//nic
					List<ContactNumber> phone = sc.getDemographicDetailsService().getContactNumber(vaccinator.getMappedId(), true, null);
					datarowProcess.addRowElement(phone.size()==0?"":phone.get(0).getNumber());//phone
					datarowProcess.addRowElement(vaccinator.getEpAccountNumber());//EP acc no
					datarowProcess.addRowElement(vievent.getDateOfEvent());
					datarowProcess.addRowElement(vievent.getDataRangeDateLower());
					datarowProcess.addRowElement(vievent.getDataRangeDateUpper());
					datarowProcess.addRowElement(totalVaccinations);
					datarowProcess.addRowElement(totalAmount);
					datarowProcess.addRowElement(epcharges==null?null:epcharges);
					datarowProcess.addRowElement(incentamount);
					datarowProcess.addRowElement(caluculationDescr);
					
					csvProcess.addData(datarowProcess);
				}
				catch (Exception e) {
					e.printStackTrace();
					GlobalParams.INCENTIVEJOBLOGGER.error("Error running job for Vaccinator:"+vaccinator.getIdMapper().getIdentifiers().get(0).getIdentifier()+":"+vaccinator.getFullName() ,e);
					CsveeRow datarow = new CsveeRow();
					datarow.addRowElement("Error running job for Vaccinator:"+vaccinator.getIdMapper().getIdentifiers().get(0).getIdentifier()+":"+vaccinator.getFullName()+"---Trace:"+e.getMessage());
					csvProcess.addData(datarow);
				}
			}

			////DONOT COMMIT IF WANT TO PREVENT DB CHANGES
			/*///////////// */
			 sc.commitTransaction();
			
			FileUtils.saveDownloadable(csvProcess.getCsvStream(true), GlobalParams.VACCINATOR_INCENTIVE_DOWNLOADABLE_CSV_INITIALS+"_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".csv", GlobalParams.INCENTIVE_CSV_DIR, user, DownloadableType.INCENTIVE_REPORT);
			FileUtils.saveDownloadable(csvAllTransactions.getCsvStream(true), "VaccinatorIncentiveAllTransactionsCSV"+"_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".csv", GlobalParams.INCENTIVE_CSV_DIR, user, DownloadableType.INCENTIVE_REPORT);

			// ONLY FOR TEST to skip save saveDownloadable(csvProcess.getCsvStream(true), GlobalParams.VACCINATOR_INCENTIVE_DOWNLOADABLE_CSV_INITIALS+"_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".csv", GlobalParams.INCENTIVE_CSV_DIR, user, DownloadableType.INCENTIVE_REPORT);
			// ONLY FOR TEST to skip save saveDownloadable(csvAllTransactions.getCsvStream(true), "VaccinatorIncentiveAllTransactionsCSV"+"_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".csv", GlobalParams.INCENTIVE_CSV_DIR, user, DownloadableType.INCENTIVE_REPORT);
		}
		catch (Exception e) {
			GlobalParams.INCENTIVEJOBLOGGER.error("Error running job:" ,e);
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}

	// ONLY FOR TEST to skip save 
	/*public static void saveDownloadable(ByteArrayOutputStream outputstr, String fileUniqueName, String additionalDirPath, User creator,DownloadableType downloadableType) throws IOException{
		String finalpath = "";
		
		String dataFilePath = Context.getSetting("downloadable.report-path", System.getProperty("user.home"));
		File dfile = new File(dataFilePath);
		if(!dfile.exists()){
			dataFilePath = System.getProperty("user.home");
		}
		if(!dataFilePath.endsWith(System.getProperty("file.separator"))){
			dataFilePath += System.getProperty("file.separator");
		}
		
		finalpath += dataFilePath;
		
		if(additionalDirPath.startsWith(System.getProperty("file.separator"))){
			additionalDirPath = additionalDirPath.substring(additionalDirPath.indexOf(System.getProperty("file.separator")));
		}
		
		if(!additionalDirPath.endsWith(System.getProperty("file.separator"))){
			additionalDirPath += System.getProperty("file.separator");
		}
		
		finalpath += additionalDirPath;
		
		if(fileUniqueName.startsWith(System.getProperty("file.separator"))){
			fileUniqueName = fileUniqueName.substring(fileUniqueName.indexOf(System.getProperty("file.separator")));
		}
		
		finalpath += fileUniqueName;
		
		File file = new File(finalpath);
		if(!file.exists()){
			File par = new File(file.getParent());
			if(!par.exists()){
				par.mkdirs();
			}
			
			file.createNewFile();
		}
		
		OutputStream os = new FileOutputStream(file);
		os.write(outputstr.toByteArray());
		os.flush();
		os.close();
	}*/
}
