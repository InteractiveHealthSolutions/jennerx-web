package org.ird.unfepi.rest.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Reminder;
import org.ird.unfepi.model.Storekeeper;
import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.rest.elements.ResponseStatus;
import org.ird.unfepi.service.ChildService;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class StorekeeperHelper
{
	public static String findLotteryWinnings(String childId)
	{
		//find child matching the Id
		ServiceContext sc =  Context.getServices();
		Child child = null;
		
		if("".equalsIgnoreCase(childId))
			 return ResponseBuilder.buildResponse(ResponseStatus.STATUS_DATA_ERROR, null);
		
		
		ChildService childService = sc.getChildService();
		child=childService.findChildById(childId, true, new String[]{"idMapper"});
		
		if(child==null)
			 return ResponseBuilder.buildResponse(ResponseStatus.STATUS_DATA_ERROR, null);
		
		Integer mappedId = child.getMappedId();
		String sql = "SELECT childlottery.amount, childlottery.code,childlottery.vaccinationRecordNum" +
							" FROM childlottery INNER JOIN vaccination" + 
							" ON vaccination.vaccinationRecordNum = childlottery.vaccinationRecordNum" +
							" WHERE vaccination.childId = "+ mappedId +  
							" AND childlottery.codeStatus = 'AVAILABLE';";	
		
		List<Object> result = sc.getCustomQueryService().getDataBySQL(sql);
		Map<String, Object> params =new  HashMap<String, Object>();
		if(result.size()>0)
		{
			JSONArray json = new JSONArray();
			JSONObject temp;
			for(int i =0; i < result.size(); i++)
			{
				temp =  new JSONObject();
				Object[] values =(Object[]) result.get(i);
				temp.put(RequestElements.AMOUNT_WON, values[0]);
				temp.put(RequestElements.VERIFICATION_CODE, values[1]);
				temp.put(RequestElements.VACCINATION_RECORD_NUM, values[2]);
				json.add(temp);
			}			
			params.put(RequestElements.LOTTERIES_AVAILABLE, json);
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_SUCCESS, params);
		}
		else
		{			
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_SUCCESS, null);
		}
	}

	public static String consumeLottery(JSONObject form)
	{
		/*
		 * TODO: Add following validations in this method
		 * 1.Form is null -done
		 * 2.Some exception occurs in handling data received -done
		 * 3.Exception occurs in running database update -done
		 * 4.No verification code is received -done
		 * 5.Any other exception occurs -handled in the Service class
		 */
		//Validations
		if(form== null)
			ResponseBuilder.buildResponse(ResponseStatus.STATUS_INCORRECT_DATA_FORMAT_ERROR, null);
				
		JSONArray lotteriesConsumed;
		Integer storekeeperId;
		
		try
		{
			storekeeperId = Integer.valueOf(form.get(RequestElements.LG_USERID).toString());
			lotteriesConsumed = (org.json.simple.JSONArray)form.get(RequestElements.LOTTERY_CONSUMED);
		}
		catch (NumberFormatException e)
		{
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_INCORRECT_DATA_FORMAT_ERROR, null);
		}
		catch (Exception e)
		{
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_INCORRECT_DATA_FORMAT_ERROR, null);
		}
		
		StringBuilder sqlUpdate =new StringBuilder( "update childlottery" + 
				"   set  " + 
				"	transactionDate = now()," +
				"	consumptionDate = now(), " +
				"	storekeeperId =  " + storekeeperId + "," +
				"	transactionDate = now()," +
				"	lastEditedDate = now()," +
				"	lastEditedByUserId = " + storekeeperId + "," +
				"	codeStatus = 'CONSUMED'" +
				"   where vaccinationRecordNum in( ");
		
		ArrayList<LotteryComposite> lotteries = new ArrayList<StorekeeperHelper.LotteryComposite>();
		StringBuilder recordNum;
		StringBuilder code;
		 
		StorekeeperHelper.LotteryComposite myLotteryObject;
		JSONObject temp;
		
		if(lotteriesConsumed.size()==0)
			ResponseBuilder.buildResponse(ResponseStatus.STATUS_INCORRECT_DATA_FORMAT_ERROR, null);
		//ArrayList<String> recordNumbers= new ArrayList<String>();
		
		for(int i =0; i < lotteriesConsumed.size(); i ++)
		{
			temp=(JSONObject)lotteriesConsumed.get(i);
			if(temp!=null)
			{
				myLotteryObject = new StorekeeperHelper().new LotteryComposite();
				
				//code
				code = new StringBuilder();
				code.append((String)temp.get(RequestElements.VERIFICATION_CODE));
				myLotteryObject.setLotteryCode(code.toString());
				
				//record number
				recordNum = new StringBuilder();
				recordNum.append((String)temp.get(RequestElements.VACCINATION_RECORD_NUM));
				myLotteryObject.setRecordNumber(recordNum.toString());
				//Handle three conditions: 1st element and only element, 1st and middle element & last element in the list
				if(lotteriesConsumed.size()==1 || i == lotteriesConsumed.size()-1)
				{
					
					sqlUpdate.append(recordNum.toString());
					sqlUpdate.append(");");
				}
				else
				{
					sqlUpdate.append(recordNum);
					sqlUpdate.append(",");
				}
				lotteries.add(myLotteryObject);
			}			
		}
		Session se=null;
		Transaction transaction=null; 
		try
		{
			se=Context.getNewSession();
			
			transaction= se.beginTransaction();
			SQLQuery query =  se.createSQLQuery(sqlUpdate.toString());
			query.executeUpdate();
			transaction.commit();
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_SUCCESS, null);
		}
		catch(Exception ex)
		{
			if(transaction!=null)
				transaction.rollback();
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_DATA_ERROR, null);
		}
		finally
		{
			se.close();
		}
	}
	
	private boolean sendMessage(Integer storekeeperId, ArrayList<String> recordNums)
	{
		boolean allSent=false;
		ServiceContext sc =  Context.getServices();
		Storekeeper storekeeper = sc.getStorekeeperService().findStorekeeperById(storekeeperId, false, new String[]{"idMapper"});
		TarseelServices tsc = TarseelContext.getServices();
		Reminder reminder;
		
		String lotteryWon="Congratulations, you have just successfully consumed Lottery bearing code: ";
		String lotteryWonNumber ="having record number : ";
		StringBuilder storekeeperSms;
		StringBuilder careGiverSms;
		for(String number : recordNums)
		{
			reminder = new Reminder();
			//TODO: Create SMS Text and reminder here
			/*tsc.getSmsService().createNewOutboundSms(recipient, text, duedate, priority, validityPeriod, periodType, projectId, additionalDescription)
			*/
		}
		return allSent;

	}
	
	private class LotteryComposite
	{
		String lotteryCode;
		String recordNumber;
		public String getLotteryCode()
		{
			return lotteryCode;
		}
		public void setLotteryCode(String lotteryCode)
		{
			this.lotteryCode = lotteryCode;
		}
		public String getRecordNumber()
		{
			return recordNumber;
		}
		public void setRecordNumber(String recordNumber)
		{
			this.recordNumber = recordNumber;
		}
		
	}
} 
