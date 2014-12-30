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
import org.ird.unfepi.model.Storekeeper;
import org.ird.unfepi.model.StorekeeperIncentiveEvent;
import org.ird.unfepi.model.StorekeeperIncentiveParams;
import org.ird.unfepi.model.StorekeeperIncentiveParticipant;
import org.ird.unfepi.model.StorekeeperIncentiveTransaction;
import org.ird.unfepi.model.StorekeeperIncentiveTransaction.TranscationStatus;
import org.ird.unfepi.model.StorekeeperIncentiveWorkProgress;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.Vaccination.TimelinessStatus;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.model.VaccinatorIncentiveEvent;
import org.ird.unfepi.model.VaccinatorIncentiveParams;
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
	
	public static String determineVerificationCode(){
		return String.valueOf(Utils.getRandomNumber(100000, 999999));
	}
	
	public static void doStorekeeperIncentivization(Date dateIncentivizationRangeLower, Date dateIncentivizationRangeUpper, User user){
		ServiceContext sc = Context.getServices();
		
		/////////////////////////////////////////////////////
		Date dataDateRangeLower = dateIncentivizationRangeLower;
		Date dataDateRangeUpper = dateIncentivizationRangeUpper;
		try{
			GlobalParams.INCENTIVEJOBLOGGER.info("Running Job: "/*+jxc.getJobDetail().getFullName()*/+";prev firetime:"/*+jxc.getPreviousFireTime()*/);

			List<Storekeeper> storkl = sc.getStorekeeperService().getAllStorekeeper(true, null);
			
			GlobalParams.INCENTIVEJOBLOGGER.info("Number of storekeepers participating in incentiviztion: "+storkl.size());

			StorekeeperIncentiveEvent sievent = new StorekeeperIncentiveEvent();
			sievent.setDateOfEvent(new Date());
			sievent.setDataRangeDateLower(dataDateRangeLower);
			sievent.setDataRangeDateUpper(dataDateRangeUpper);
			
			Integer sieventId = Integer.parseInt(sc.getIncentiveService().saveStorekeeperIncentiveEvent(sievent).toString());
			
			Csvee csv = new Csvee();
			
			CsveeRow headerrow = new CsveeRow();
			headerrow.addRowElement("Sr. No.");
			headerrow.addRowElement("Storekeeper ID");
			headerrow.addRowElement("Name");
			headerrow.addRowElement("Address");
			headerrow.addRowElement("CNIC No.");
			headerrow.addRowElement("Phone");
			headerrow.addRowElement("EP Acc. No.");
			headerrow.addRowElement("Incentivization Event Date");
			headerrow.addRowElement("Incentivization Date From");
			headerrow.addRowElement("Incentivization Date To");
			headerrow.addRowElement("Transactions Done");
			headerrow.addRowElement("Amount Transferred");
			headerrow.addRowElement("Commission Rate");
			headerrow.addRowElement("Commission Amount");
			headerrow.addRowElement("Subtotal");
			headerrow.addRowElement("EP Serv. Charges");
			headerrow.addRowElement("Total");
			headerrow.addRowElement("Incentive Calculation Formula");

			csv.addHeader(headerrow);
			
			int i=1;
			for (Storekeeper storekeeper : storkl) 
			{
				try{
					String sqlskin = 
							"select count(shopkeeperId), sum(amount) , " +
							" (select group_concat('--',numberType,':',number) from contactnumber where mappedid=t.shopkeeperId) contactnumbersofshopkeeper, " +
							" (select group_concat('H.No:',a.addHouseNumber,' Street:',a.addStreet,' Sector:',a.addSector,' Colony:',a.addColony,' Town:',a.addtown,' UC:',a.addUc,' LMARK:',a.addLandmark,' CityID:',CAST(a.cityId AS char(2))) from address a where a.mappedid=t.shopkeeperId) addressofshopkeeper " +
							"from transaction t where shopkeeperId="+storekeeper.getMappedId()+" " +
							"  and transactionDate between '"+GlobalParams.SQL_DATE_FORMAT.format(dataDateRangeLower)+"' and '"+GlobalParams.SQL_DATE_FORMAT.format(dataDateRangeUpper)+"' ";
					System.out.println(sqlskin);

					List list = sc.getCustomQueryService().getDataBySQL(sqlskin);
					
					Object[] cols = (Object[]) list.get(0);
					
					float totaltransactions = Integer.parseInt(cols[0].toString());
					float totalamounttransferred = Float.parseFloat(cols[1] == null?"0":cols[1].toString());
					
					List<StorekeeperIncentiveParams> incentparaml = sc.getIncentiveService().findStorekeeperIncentiveParamsByCriteria(null, null, (int)totalamounttransferred, (int)totalamounttransferred, 0, 1, true, null);
					StorekeeperIncentiveParams incentparam = null;
					Boolean isincentivized = null;
					Float incentamount = null;
					String caluculationDescr = null;
					Float commissionamout = null;
					Float commissionPlusTotalTransferred = null;
					if(incentparaml.size() > 0){
						incentparam = incentparaml.get(0);
						
						isincentivized = totaltransactions > 0;
						commissionamout = incentparam.getCommission() * totalamounttransferred;
						commissionPlusTotalTransferred = commissionamout+totalamounttransferred;
						incentamount = isincentivized ? (commissionPlusTotalTransferred+incentparam.getEasypaisaCharges()) : null;
						caluculationDescr = isincentivized ?
								("(commission*TotalAmountTransferred)+TotalAmountTransferred+EPCharges " +
								": ("+incentparam.getCommission()+"*"+totalamounttransferred+")+"+totalamounttransferred+"+"+incentparam.getEasypaisaCharges())
								:null;
					}
					
					StorekeeperIncentiveParticipant siparti = new StorekeeperIncentiveParticipant();
					siparti.setCriteriaElementValue(totalamounttransferred);
					siparti.setIsIncentivised(isincentivized);
					siparti.setStorekeeperId(storekeeper.getMappedId());
					siparti.setStorekeeperIncentiveEventId(sieventId);
					siparti.setStorekeeperIncentiveParamsId(incentparam == null ? null : incentparam.getStorekeeperIncentiveParamsId());
					siparti.setDescription(caluculationDescr);
					int sipartiId = Integer.parseInt(sc.getIncentiveService().saveStorekeeperIncentiveParticipant(siparti).toString());
					
					StorekeeperIncentiveWorkProgress siworkproge = new StorekeeperIncentiveWorkProgress();
					siworkproge.setTransactions((int)totaltransactions);
					siworkproge.setTotalTransactionsAmount(totalamounttransferred);
					siworkproge.setStorekeeperIncentiveParticipantId(sipartiId);
					
					sc.getIncentiveService().saveStorekeeperIncentiveWorkProgress(siworkproge);
					
					if(isincentivized != null && isincentivized){
						StorekeeperIncentiveTransaction sitrans = new StorekeeperIncentiveTransaction();
						sitrans.setAmountDue(incentamount);
						sitrans.setCreatedDate(new Date());
						sitrans.setTransactionStatus(TranscationStatus.DUE);
						sitrans.setStorekeeperId(storekeeper.getMappedId());
						sitrans.setStorekeeperIncentiveEventId(sieventId);
						sitrans.setDescription(caluculationDescr);
						
						sc.getIncentiveService().saveStorekeeperIncentiveTransaction(sitrans);
					}
					
					sc.commitTransaction();
					 
					CsveeRow datarow = new CsveeRow();
					datarow.addRowElement(i);
					datarow.addRowElement(storekeeper.getIdMapper().getIdentifiers().get(0).getIdentifier());
					datarow.addRowElement(storekeeper.getFullName());
					datarow.addRowElement(cols[3]);//address
					datarow.addRowElement(storekeeper.getNic());//nic
					datarow.addRowElement(cols[2]);//phone
					datarow.addRowElement(storekeeper.getEpAccountNumber());//EP acc no
					datarow.addRowElement(sievent.getDateOfEvent());
					datarow.addRowElement(sievent.getDataRangeDateLower());
					datarow.addRowElement(sievent.getDataRangeDateUpper());
					datarow.addRowElement(totaltransactions);
					datarow.addRowElement(totalamounttransferred);
					datarow.addRowElement(incentparam==null?null:incentparam.getCommission());
					datarow.addRowElement((isincentivized!=null && isincentivized) ? commissionamout: null);
					datarow.addRowElement((isincentivized!=null && isincentivized) ? commissionPlusTotalTransferred :null);
					datarow.addRowElement(incentparam==null?null:incentparam.getEasypaisaCharges());
					datarow.addRowElement(incentamount);
					datarow.addRowElement((isincentivized!=null && isincentivized) ? caluculationDescr: null);
					
					csv.addData(datarow);
				}
				catch (Exception e) {
					e.printStackTrace();
					GlobalParams.INCENTIVEJOBLOGGER.error("Error running job for storekeeper:"+storekeeper.getIdMapper().getIdentifiers().get(0).getIdentifier()+":"+storekeeper.getFullName() ,e);
					CsveeRow datarow = new CsveeRow();
					datarow.addRowElement("Error running job for storekeeper:"+storekeeper.getIdMapper().getIdentifiers().get(0).getIdentifier()+":"+storekeeper.getFullName()+"---Trace:"+e.getMessage());
					csv.addData(datarow);
				}
				
				i++;
			}
			
			FileUtils.saveDownloadable(csv.getCsvStream(true), GlobalParams.STOREKEEPER_INCENTIVE_DOWNLOADABLE_CSV_INITIALS+"_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".csv", GlobalParams.INCENTIVE_CSV_DIR, user, DownloadableType.INCENTIVE_REPORT);
		}
		catch (Exception e) {
			GlobalParams.INCENTIVEJOBLOGGER.error("Error running job:" ,e);
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}
	
	public static void doVaccinatorIncentivization(Date dateIncentivizationRangeLower, Date dateIncentivizationRangeUpper, User user){
		ServiceContext sc = Context.getServices();
		
		/////////////////////////////////////////////////////
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
			headerrowProcess.addRowElement("Sr. No.");
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
			headerrowProcess.addRowElement("Lotteries Done / Total Winnings");
			headerrowProcess.addRowElement("Total Amount Won (Child)");
			headerrowProcess.addRowElement("Commission Rate");
			headerrowProcess.addRowElement("Subtotal / Total Amount Won (Vaccinator)");
			headerrowProcess.addRowElement("EP Serv. Charges");
			headerrowProcess.addRowElement("Total");
			headerrowProcess.addRowElement("Incentive Calculation Formula");

			csvProcess.addHeader(headerrowProcess);
			
			int i=1;
			for (Vaccinator vaccinator : vactorl) {
				try{
					String sqlvcin = "SELECT substr(cenid.programid,1,2) 'Area ID'" +
							", cenid.programid 'Vaccination Center'" +
							", vaccid.programid 'Vaccinator'" +
							", count(transactionid) 'Total Winnings'" +
							", count(distinct t.childid) 'Total Mothers'" +
							", sum(t.amount) 'Total Amount won(Mothers)'" +
							//", sum(t.amount)*2/5 'Amount won(Vaccinator)'" +
							", '"+GlobalParams.SQL_DATE_FORMAT.format(dataDateRangeLower)+" - "+GlobalParams.SQL_DATE_FORMAT.format(dataDateRangeUpper)+"' dateRange " +
							", (select group_concat('--',numberType,':',number) from contactnumber where mappedid=v.vaccinatorId) contactnumbersofvaccinator " +
							", (select group_concat('H.No:',a.addHouseNumber,' Street:',a.addStreet,' Sector:',a.addSector,' Colony:',a.addColony,' Town:',a.addtown,' UC:',a.addUc,' LMARK:',a.addLandmark,' CityID:',CAST(a.cityId AS char(2))) from address a where a.mappedid=v.vaccinatorId) addressofvaccinatorId " +
							" FROM transaction t" +
							" left join vaccination v on t.childid=v.childid and t.vaccineid=v.vaccineid" +
							" left join idmapper cenid on v.vaccinationcenterid = cenid.mappedid" +
							" left join idmapper vaccid on v.vaccinatorid= vaccid.mappedid" +
							" left join address ad on t.childid=ad.mappedid" +
							" where vaccinatorId="+vaccinator.getMappedId()+" " +
							" and v.vaccinationstatus = '"+VACCINATION_STATUS.VACCINATED+"' " +
							" and v.timelinessStatus <> '"+TimelinessStatus.EARLY+"' " +
							" and t.winningDate between '"+GlobalParams.SQL_DATE_FORMAT.format(dataDateRangeLower)+"' and '"+GlobalParams.SQL_DATE_FORMAT.format(dataDateRangeUpper)+"' ";
							
					System.out.println(sqlvcin);

					List list = sc.getCustomQueryService().getDataBySQL(sqlvcin);
					
					Object[] cols = (Object[]) list.get(0);
					
					float totaltransactions = Integer.parseInt(cols[3].toString());
					float totalamountWonByMother = Float.parseFloat(cols[5] == null?"0":cols[5].toString());
					Float totalamountWonByVaccinator = null;

					List<VaccinatorIncentiveParams> incentparaml = sc.getIncentiveService().findVaccinatorIncentiveParamsByCriteria(null, null, null, null, 0, 1, true, null);
					VaccinatorIncentiveParams incentparam = null;
					Boolean isincentivized = null;
					Float incentamount = null;
					String caluculationDescr = null;
					Integer epcharges = null;
					
					if(incentparaml.size() > 0){
						incentparam = incentparaml.get(0);
						
						isincentivized = totaltransactions > 0;
						
						totalamountWonByVaccinator = incentparam.getCommission() * totalamountWonByMother;
						
						List<VariableSetting> epchargesl = sc.getIRSettingService().findByCriteria(null, VariableSettingType.EP_CHARGES.name(), null, null, totalamountWonByVaccinator, totalamountWonByVaccinator, true, 0, 1);
						
						if(epchargesl.size() > 0){
							epcharges = Integer.parseInt(epchargesl.get(0).getValue());
							incentamount = isincentivized ? (totalamountWonByVaccinator + epcharges) : null;
							caluculationDescr = isincentivized ?
									("(commission*TotalAmountWonByMothers)+EPCharges " +
									": ("+incentparam.getCommission()+"*"+totalamountWonByMother+")+"+epcharges)
									:null;
						}
						else {
							caluculationDescr = "UNABLE TO GET EP CHARGES";
						}
						
					}
					else {
						caluculationDescr = "UNABLE TO GET COMMISSION RATE";
					}
					
					VaccinatorIncentiveParticipant siparti = new VaccinatorIncentiveParticipant();
					siparti.setCriteriaElementValue((int) totalamountWonByMother);
					siparti.setIsIncentivised(isincentivized);
					siparti.setVaccinatorId(vaccinator.getMappedId());
					siparti.setVaccinatorIncentiveEventId(sieventId);
					siparti.setVaccinatorIncentiveParamsId(incentparam == null ? null : incentparam.getVaccinatorIncentiveParamsId());
					siparti.setDescription(caluculationDescr);
					int sipartiId = Integer.parseInt(sc.getIncentiveService().saveVaccinatorIncentiveParticipant(siparti).toString());
					
					VaccinatorIncentiveWorkProgress siworkproge = new VaccinatorIncentiveWorkProgress();
					siworkproge.setWorkType(IncentiveWorkType.CHILD_LOTTERY_WINNINGS.name());
					siworkproge.setWorkValue(Float.toString(totaltransactions));
					siworkproge.setVaccinatorIncentiveParticipantId(sipartiId);
					
					VaccinatorIncentiveWorkProgress siworkproge1 = new VaccinatorIncentiveWorkProgress();
					siworkproge1.setWorkType(IncentiveWorkType.CHILD_LOTTERY_DISTINCT_CHILDREN.name());
					siworkproge1.setWorkValue(cols[4] == null ? null : cols[4].toString());
					siworkproge1.setVaccinatorIncentiveParticipantId(sipartiId);
					
					VaccinatorIncentiveWorkProgress siworkproge2 = new VaccinatorIncentiveWorkProgress();
					siworkproge2.setWorkType(IncentiveWorkType.CHILD_LOTTERY_WON_AMOUNT.name());
					siworkproge2.setWorkValue(Float.toString(totalamountWonByMother));
					siworkproge2.setVaccinatorIncentiveParticipantId(sipartiId);
					
					sc.getIncentiveService().saveVaccinatorIncentiveWorkProgress(siworkproge);
					sc.getIncentiveService().saveVaccinatorIncentiveWorkProgress(siworkproge1);
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
					
					sc.commitTransaction();
					 
					CsveeRow datarowProcess = new CsveeRow();
					datarowProcess.addRowElement(i);
					datarowProcess.addRowElement(vaccinator.getVaccinationCenter()==null?"":vaccinator.getVaccinationCenter().getIdMapper().getIdentifiers().get(0).getIdentifier());
					datarowProcess.addRowElement(vaccinator.getIdMapper().getIdentifiers().get(0).getIdentifier());
					datarowProcess.addRowElement(vaccinator.getFullName());
					datarowProcess.addRowElement(cols[8]);//address
					datarowProcess.addRowElement(vaccinator.getNic());//nic
					datarowProcess.addRowElement(cols[7]);//phone
					datarowProcess.addRowElement(vaccinator.getEpAccountNumber());//EP acc no
					datarowProcess.addRowElement(vievent.getDateOfEvent());
					datarowProcess.addRowElement(vievent.getDataRangeDateLower());
					datarowProcess.addRowElement(vievent.getDataRangeDateUpper());
					datarowProcess.addRowElement(totaltransactions);
					datarowProcess.addRowElement(totalamountWonByMother);
					datarowProcess.addRowElement(incentparam==null?null:incentparam.getCommission());
					datarowProcess.addRowElement((isincentivized != null && isincentivized) ? totalamountWonByVaccinator: null);
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
				
				i++;
			}
			
			Csvee csvSummary = new Csvee();
			
			CsveeRow headerrowSummary = new CsveeRow();
			headerrowSummary.addRowElement("Sr. No.");
			headerrowSummary.addRowElement("Area ID");
			headerrowSummary.addRowElement("Vaccination Center");
			headerrowSummary.addRowElement("Vaccinator");
			headerrowSummary.addRowElement("Total Winnings");
			headerrowSummary.addRowElement("Total Mothers");
			headerrowSummary.addRowElement("Total Amount Won (Mothers)");
			headerrowSummary.addRowElement("Total Amount Won (Vaccinator)");
			headerrowSummary.addRowElement("Date Range");

			csvSummary.addHeader(headerrowSummary);
			
			String sqlvcinSummary = "SELECT substr(cenid.programid,1,2) 'Area ID'" +
					", cenid.programid 'Vaccination Center'" +
					", vaccid.programid 'Vaccinator'" +
					", count(transactionid) 'Total Winnings'" +
					", count(distinct t.childid) 'Total Mothers'" +
					", sum(t.amount) 'Total Amount won(Mothers)'" +
					", sum(t.amount)*2/5 'Amount won(Vaccinator)'" +
					", '"+GlobalParams.SQL_DATE_FORMAT.format(dataDateRangeLower)+" - "+GlobalParams.SQL_DATE_FORMAT.format(dataDateRangeUpper)+"' dateRange" +
					" FROM transaction t" +
					" left join vaccination v on t.childid=v.childid and t.vaccineid=v.vaccineid" +
					" left join idmapper cenid on v.vaccinationcenterid = cenid.mappedid" +
					" left join idmapper vaccid on v.vaccinatorid= vaccid.mappedid" +
					" left join address ad on t.childid=ad.mappedid" +
					" where v.vaccinationstatus='vaccinated' " +
					" and t.winningDate between '"+GlobalParams.SQL_DATE_FORMAT.format(dataDateRangeLower)+"' and '"+GlobalParams.SQL_DATE_FORMAT.format(dataDateRangeUpper)+"' " +
							" group by vaccinator";
					
			List listSummary = sc.getCustomQueryService().getDataBySQL(sqlvcinSummary);
			
			csvSummary.populateCsvFromList(listSummary, true);
			
			
			Csvee csvAllTransactions = new Csvee();
			
			CsveeRow headerrowAllTransactions = new CsveeRow();
			headerrowAllTransactions.addRowElement("Sr. No.");
			headerrowAllTransactions.addRowElement("Area ID");
			headerrowAllTransactions.addRowElement("Vaccination Center");
			headerrowAllTransactions.addRowElement("Vaccinator");
			headerrowAllTransactions.addRowElement("Transaction ID");
			headerrowAllTransactions.addRowElement("Amount Won (Mother)");
			headerrowAllTransactions.addRowElement("Child ID");
			headerrowAllTransactions.addRowElement("Vaccine");
			headerrowAllTransactions.addRowElement("Winning Date");
			headerrowAllTransactions.addRowElement("Transaction Date(Consumption)");
			headerrowAllTransactions.addRowElement("Transaction Status");
			headerrowAllTransactions.addRowElement("Date Range");

			csvAllTransactions.addHeader(headerrowAllTransactions);
			
			String sqlvcinAllTransactions = "SELECT substr(cenid.programid,1,2) 'Area ID'" +
					", cenid.programid 'Vaccination Center'" +
					", vaccid.programid 'Vaccinator'" +
					", t.transactionid 'Transaction ID'" +
					", t.amount 'Amount Won(Mother)'" +
					", chlid.programid 'Child ID'" +
					", vac.name 'Vaccine'" +
					", t.winningdate 'Winning Date' " +
					", t.transactiondate 'Transaction Date(Consumption)' " +
					", t.status 'Transaction Status'" +
					", '"+GlobalParams.SQL_DATE_FORMAT.format(dataDateRangeLower)+" - "+GlobalParams.SQL_DATE_FORMAT.format(dataDateRangeUpper)+"' dateRange" +
					" FROM transaction t " +
					" left join vaccination v on t.childid=v.childid and t.vaccineid=v.vaccineid " +
					" left join idmapper cenid on v.vaccinationcenterid = cenid.mappedid " +
					" left join idmapper vaccid on v.vaccinatorid= vaccid.mappedid " +
					" left join idmapper chlid on t.childid=chlid.mappedid" +
					" left join vaccine vac on t.vaccineid=vac.vaccineid" +
					" where v.vaccinationstatus='vaccinated'" +
					"  and t.winningDate between '"+GlobalParams.SQL_DATE_FORMAT.format(dataDateRangeLower)+"' and '"+GlobalParams.SQL_DATE_FORMAT.format(dataDateRangeUpper)+"' " +
					"  order by vaccinator;";
					
			List listAllTransaction = sc.getCustomQueryService().getDataBySQL(sqlvcinAllTransactions);
			
			csvAllTransactions.populateCsvFromList(listAllTransaction, true);
			
			FileUtils.saveDownloadable(csvProcess.getCsvStream(true), GlobalParams.VACCINATOR_INCENTIVE_DOWNLOADABLE_CSV_INITIALS+"_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".csv", GlobalParams.INCENTIVE_CSV_DIR, user, DownloadableType.INCENTIVE_REPORT);
			FileUtils.saveDownloadable(csvSummary.getCsvStream(true), "VaccinatorIncentiveSummaryCSV"+"_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".csv", GlobalParams.INCENTIVE_CSV_DIR, user, DownloadableType.INCENTIVE_REPORT);
			FileUtils.saveDownloadable(csvAllTransactions.getCsvStream(true), "VaccinatorIncentiveAllTransactionsCSV"+"_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".csv", GlobalParams.INCENTIVE_CSV_DIR, user, DownloadableType.INCENTIVE_REPORT);
		}
		catch (Exception e) {
			GlobalParams.INCENTIVEJOBLOGGER.error("Error running job:" ,e);
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}
	
	public static void main(String[] args) {
		
		for (int i = 0; i < 20; i++) 
		{
			int count = 0;
			for (int i1 = 0; i1 < 10000; i1++) {
				if(determineLotteryWon(20))
				count++;
			}
			System.out.println("count1:"+count);
			
			int count2=0;
			for (int i1 = 0; i1 < 10000; i1++) {
				if((new Random().nextInt(100)) < 20){
					count2++;
				}
			}
			System.out.println("count2:"+count2);
		}

	}
}
