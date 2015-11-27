/*
 * 
 */
package org.ird.unfepi.utils.date.test;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.ird.unfepi.utils.date.DateUtils;
import org.ird.unfepi.utils.date.DateUtils.TIME_INTERVAL;
import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * The Class DateUtilsTest.
 */
public class DateUtilsTest extends TestCase {

	/**
	 * Test datesqual.
	 */
	@Test
	public void testDatesqual(){
		Calendar date1=Calendar.getInstance();
		date1.set(Calendar.HOUR_OF_DAY, 00);
		date1.set(Calendar.MINUTE, 00);
		date1.set(Calendar.SECOND, 00);

		Calendar date2=Calendar.getInstance();
		date2.set(Calendar.HOUR_OF_DAY, 23);
		date2.set(Calendar.MINUTE, 59);
		date2.set(Calendar.SECOND, 59);
		
		assertTrue("dates not equal at first step: "+date1.getTime()+"::::"+date2.getTime()
				, DateUtils.datesEqual(date1.getTime(), date2.getTime()));
		//Checking hours
		for (int i=0;i<23;i++) {
			date1.add(Calendar.HOUR_OF_DAY, 1);
			date2.add(Calendar.HOUR_OF_DAY, -1);
			
			System.out.println(i+" : Dates: "+date1.getTime()+"  ::::  "+date2.getTime());
			assertTrue("dates not equal at i: "+i+":Dates:"+date1.getTime()+"::::"+date2.getTime()
					, DateUtils.datesEqual(date1.getTime(), date2.getTime()));
		}
				
		//just meeting of two dates
		date1=Calendar.getInstance();
		date1.set(Calendar.HOUR_OF_DAY, 23);
		date1.set(Calendar.MINUTE, 59);
		date1.set(Calendar.SECOND, 59);

		date2=Calendar.getInstance();
		date2.add(Calendar.DATE, 1);
		date2.set(Calendar.HOUR_OF_DAY, 00);
		date2.set(Calendar.MINUTE, 00);
		date2.set(Calendar.SECOND, 00);
		
		assertFalse("dates became equal while hours were just one second different: "+date1.getTime()+"::::"+date2.getTime()
				, DateUtils.datesEqual(date1.getTime(), date2.getTime()));
		
		date1=Calendar.getInstance();
		date1.set(Calendar.YEAR	, 2012);
		
		System.out.println("Date: "+date1.getTime());
		assertFalse("dats was of next year but shown equal"+date1.getTime()
				, DateUtils.datesEqual(date1.getTime(),new Date()));
		
		date1=Calendar.getInstance();
		date1.set(Calendar.YEAR	, 2010);
		

		System.out.println("Date: "+date1.getTime());
		assertFalse("date was of prev year but shown equal "+date1.getTime()
				, DateUtils.datesEqual(date1.getTime(),new Date()));
	}
	
	/**
	 * Test after todays date.
	 */
	@Test
	public void testAfterTodaysDate(){
		Calendar date1=Calendar.getInstance();
		date1.set(Calendar.HOUR_OF_DAY, 00);
		date1.set(Calendar.MINUTE, 00);
		date1.set(Calendar.SECOND, 00);

		for (int i=0;i<23;i++) {
			date1.add(Calendar.HOUR_OF_DAY, 1);
			
			System.out.println(i+" : Date: "+date1.getTime());
			assertFalse("date was not after todays date at i: "+i+":Dates:"+date1.getTime()
					, DateUtils.afterTodaysDate(date1.getTime()));
		}
		
		date1=Calendar.getInstance();
		date1.set(Calendar.YEAR	, 2012);
		
		System.out.println("Date: "+date1.getTime());
		assertTrue("date was of next year "+date1.getTime()
				, DateUtils.afterTodaysDate(date1.getTime()));
		
		date1=Calendar.getInstance();
		date1.set(Calendar.YEAR	, 2010);
		

		System.out.println("Date: "+date1.getTime());
		assertFalse("date was of prev year "+date1.getTime()
				, DateUtils.afterTodaysDate(date1.getTime()));
				
		//just meeting of two dates
		date1=Calendar.getInstance();
		date1.set(Calendar.YEAR, 2010);
		date1.set(Calendar.MONTH, Calendar.SEPTEMBER);
		date1.set(Calendar.HOUR_OF_DAY, 23);
		date1.set(Calendar.MINUTE, 59);
		date1.set(Calendar.SECOND, 59);
		
		assertFalse("date was of previous year",DateUtils.afterTodaysDate(date1.getTime()));
	}
	
	/**
	 * Test get days passed.
	 */
	@Test
	public void testGetDaysPassed(){
		System.out.println("current date test till 23 hour addition");
		Calendar date1=Calendar.getInstance();
		date1.set(Calendar.HOUR_OF_DAY, 1);
		date1.set(Calendar.MINUTE, 15);
		date1.set(Calendar.SECOND, 00);
		
		for (int i=0 ;i<22 ;i++) {
			date1.add(Calendar.HOUR_OF_DAY, 1);
			System.out.println("i = "+i+" ; Date = "+date1.getTime());
			assertEquals("not equal at i="+i+" :Date="+date1.getTime(),
					0, DateUtils.getDaysPassed(date1.getTime(), new Date()));
		}
		
		System.out.println("next day date test till 23 hour addition");
		date1=Calendar.getInstance();
		date1.add(Calendar.DATE, 1);
		date1.set(Calendar.HOUR_OF_DAY, 00);
		date1.set(Calendar.MINUTE, 00);
		date1.set(Calendar.SECOND, 00);
		
		for (int i=0 ;i<22 ;i++) {
			date1.add(Calendar.HOUR_OF_DAY, 1);
			System.out.println("i = "+i+" ; Date = "+date1.getTime());
			assertEquals("not equal at i="+i+" :Date="+date1.getTime(),
					1, DateUtils.getDaysPassed(date1.getTime(), new Date()));
		}
		
		System.out.println("prev day date test till 23 hour addition");
		date1=Calendar.getInstance();
		date1.add(Calendar.DATE, -1);
		date1.set(Calendar.HOUR_OF_DAY, 00);
		date1.set(Calendar.MINUTE, 00);
		date1.set(Calendar.SECOND, 00);
		
		for (int i=0 ;i<22 ;i++) {
			date1.add(Calendar.HOUR_OF_DAY, 1);
			System.out.println("i = "+i+" ; Date = "+date1.getTime());
			assertEquals(
					"not equal at i="+i+" :Date="+date1.getTime(),
					1, 
					DateUtils.getDaysPassed( new Date(),date1.getTime()));
		}
		System.out.println("complete tests");

		int day=-6;
		while(day <= Math.abs( -6 )){
			date1=Calendar.getInstance();
			date1.add(Calendar.DATE, day);
			date1.set(Calendar.HOUR_OF_DAY, 00);
			date1.set(Calendar.MINUTE, 00);
			date1.set(Calendar.SECOND, 00);
			
			System.out.println("for day :"+day);

			for (int i = 0; i < 23; i++) {
				date1.add(Calendar.HOUR_OF_DAY, 1);

				System.out.println("i = "+i+" ; Day = "+day+" ; Date = "+date1.getTime());
				assertEquals(
						"not equal at i="+i+"  :Date="+date1.getTime(),
						day, 
						DateUtils.getDaysPassed(date1.getTime() , new Date()));
			}
			day++;
		}
	}
	
	/**
	 * Test difference between interval.
	 */
	@Test
	public void testDifferenceBetweenInterval(){
		System.out.println("TEST DifferenceBetweenInterval");
		
		Calendar date1;
		
		int diff=-1;
		while(diff <= Math.abs( -1 )){
			date1=Calendar.getInstance();
			date1.add(Calendar.YEAR, diff);
			date1.set(Calendar.MONTH, Calendar.JANUARY);
			date1.set(Calendar.HOUR_OF_DAY, 00);
			date1.set(Calendar.MINUTE, 00);
			date1.set(Calendar.SECOND, 00);
			
			System.out.println("for year :"+diff);

			for (int i = 0; i < 6; i++) {
				date1.add(Calendar.MONTH, 2);

				long msDiff = date1.getTime().getTime() -DateUtils.truncateDatetoDate( new Date()).getTime();
				double yearDiff = (msDiff / 1000 / 60 / 60 / 24 / 365.25);

				//System.out.println("i = "+i+" ; Diff = "+diff+" ; yearDiff = "+yearDiff+" ; Date = "+date1.getTime());

				System.out.println("i = "+i
						+" ; Diff = "+diff
						+" ; yearDiff = "+yearDiff
						+" result unround= "+DateUtils.differenceBetweenIntervals(date1.getTime(), new Date(), TIME_INTERVAL.YEAR)
						+" result round= "+DateUtils.differenceBetweenIntervals(date1.getTime(), new Date(), TIME_INTERVAL.YEAR)
						+" ; Date = "+date1.getTime());
				
				/*assertEquals(
						"not equal at i="+i+"  : Date="+date1.getTime(),
						yearDiff, 
						DateUtils.differenceBetweenIntervals(date1.getTime(), new Date(), TIME_INTERVAL.YEAR));
*/			}
			diff++;
		}
		
		diff=-1;
		while(diff <= Math.abs( -1 )){
			date1=Calendar.getInstance();
			date1.add(Calendar.YEAR, diff);
			date1.set(Calendar.MONTH, Calendar.JANUARY);
			date1.set(Calendar.HOUR_OF_DAY, 00);
			date1.set(Calendar.MINUTE, 00);
			date1.set(Calendar.SECOND, 00);
			
			System.out.println("for year :"+diff);

			for (int i = 0; i < 6; i++) {
				date1.add(Calendar.MONTH, 2);

				long msDiff = date1.getTime().getTime() -DateUtils.truncateDatetoDate( new Date()).getTime();
				double yearDiff = (msDiff*2 / 1000 / 60 / 60 / 24 / 365.25);

				//System.out.println("i = "+i+" ; Diff = "+diff+" ; yearDiff = "+yearDiff+" ; Date = "+date1.getTime());

				System.out.println("i = "+i
						+" ; Diff = "+diff
						+" ; yearDiff = "+yearDiff
						+" result unround= "+DateUtils.differenceBetweenIntervals(date1.getTime(), new Date(), TIME_INTERVAL.HALF_YEAR)
						+" result round= "+DateUtils.differenceBetweenIntervals(date1.getTime(), new Date(), TIME_INTERVAL.HALF_YEAR)
						+" ; Date = "+date1.getTime());
				
				/*assertEquals(
						"not equal at i="+i+"  : Date="+date1.getTime(),
						yearDiff, 
						DateUtils.differenceBetweenIntervals(date1.getTime(), new Date(), TIME_INTERVAL.YEAR));
*/			}
			diff++;
		}
		
		diff=-1;
		while(diff <= Math.abs( -1 )){
			date1=Calendar.getInstance();
			date1.add(Calendar.YEAR, diff);
			date1.set(Calendar.MONTH, Calendar.JANUARY);
			date1.set(Calendar.HOUR_OF_DAY, 00);
			date1.set(Calendar.MINUTE, 00);
			date1.set(Calendar.SECOND, 00);
			
			System.out.println("for year :"+diff);

			for (int i = 0; i < 6; i++) {
				date1.add(Calendar.MONTH, 2);

				long msDiff = date1.getTime().getTime() -DateUtils.truncateDatetoDate( new Date()).getTime();
				double yearDiff = (msDiff*4 / 1000 / 60 / 60 / 24 / 365.25);

				//System.out.println("i = "+i+" ; Diff = "+diff+" ; yearDiff = "+yearDiff+" ; Date = "+date1.getTime());

				System.out.println("i = "+i
						+" ; Diff = "+diff
						+" ; yearDiff = "+yearDiff
						+" result unround= "+DateUtils.differenceBetweenIntervals(date1.getTime(), new Date(), TIME_INTERVAL.QUARTER_YEAR)
						+" result round= "+DateUtils.differenceBetweenIntervals(date1.getTime(), new Date(), TIME_INTERVAL.QUARTER_YEAR)
						+" ; Date = "+date1.getTime());
				
				/*assertEquals(
						"not equal at i="+i+"  : Date="+date1.getTime(),
						yearDiff, 
						DateUtils.differenceBetweenIntervals(date1.getTime(), new Date(), TIME_INTERVAL.YEAR));
*/			}
			diff++;
		}

		diff=-1;
		while(diff <= Math.abs( -1 )){
			date1=Calendar.getInstance();
			date1.add(Calendar.YEAR, diff);
			date1.set(Calendar.MONTH, Calendar.JANUARY);
			date1.set(Calendar.HOUR_OF_DAY, 00);
			date1.set(Calendar.MINUTE, 00);
			date1.set(Calendar.SECOND, 00);
			
			System.out.println("for year :"+diff);

			for (int i = 0; i < 12; i++) {
				date1.add(Calendar.MONTH, 1);

				long msDiff = date1.getTime().getTime() -DateUtils.truncateDatetoDate( new Date()).getTime();
				double yearDiff = (msDiff / 1000 / 60 / 60 / 24 / 30);

				//System.out.println("i = "+i+" ; Diff = "+diff+" ; yearDiff = "+yearDiff+" ; Date = "+date1.getTime());

				System.out.println("i = "+i
						+" ; Diff = "+diff
						+" ; yearDiff = "+yearDiff
						+" result unround= "+DateUtils.differenceBetweenIntervals(date1.getTime(), new Date(), TIME_INTERVAL.MONTH)
						+" result round= "+DateUtils.differenceBetweenIntervals(date1.getTime(), new Date(), TIME_INTERVAL.MONTH)
						+" ; Date = "+date1.getTime());
				
				/*assertEquals(
						"not equal at i="+i+"  : Date="+date1.getTime(),
						yearDiff, 
						DateUtils.differenceBetweenIntervals(date1.getTime(), new Date(), TIME_INTERVAL.YEAR));
*/			}
			diff++;
		}
		
		
		diff=-12;
		for (TIME_INTERVAL timeInterval : TIME_INTERVAL.values()) {
			
			switch (timeInterval) {
			case SECOND:
				break;
				
			case MINUTE:
				break;
				
			case HOUR:
				break;
				
			case DATE:
			case DAY:
				diff=-12;//run from -12 years to 12 years
				while(diff <= Math.abs( -12 )){
					date1=Calendar.getInstance();
					date1.add(Calendar.YEAR, diff);
					date1.set(Calendar.MONTH, Calendar.JANUARY);
					
					for (int i = 0; i < 12; i++) {// loop through each month
						date1.set(Calendar.MONTH, i);
						for (int j = 1; j < 30; j++) {//day by day avg 30 days in a month
						date1.set(Calendar.DATE, j);
						
						Date now=new Date();
						
						long msDiff = date1.getTime().getTime() - now.getTime();
						int yearDiff = (int) (msDiff / 1000 / 60 / 60 / 24);
						System.out.println("i = "+i
								+" ; j = "+j
								+" ; Diff = "+diff
								+" ; dayDiff = "+yearDiff
								+" ; Date = "+date1.getTime());
						assertEquals(
						"DAY not equal at i="+i+" ; j="+j+"  : Date="+date1.getTime(),
						yearDiff, 
						DateUtils.differenceBetweenIntervals(date1.getTime(), now, timeInterval));
						}
					}
					diff++;
				}
				break;
				
			case WEEK:
				diff=-12;//run from -12 years to 12 years
				while(diff <= Math.abs( -12 )){
					date1=Calendar.getInstance();
					date1.add(Calendar.YEAR, diff);
					date1.set(Calendar.MONTH, Calendar.JANUARY);
					
					for (int i = 0; i < 12; i++) {// loop through each month
						date1.set(Calendar.MONTH, i);
						for (int j = 1; j < 30; j++) {//day by day avg 30 days in a month
						date1.set(Calendar.DATE, j);
						
						Date now=new Date();
						
						long msDiff = date1.getTime().getTime() - now.getTime();
						int yearDiff = (int) (msDiff / 1000 / 60 / 60 / 24/7);
						System.out.println("i = "+i
								+" ; j = "+j
								+" ; Diff = "+diff
								+" ; weekDiff = "+yearDiff
								+" ; Date = "+date1.getTime());
						assertEquals(
						"WEEK not equal at i="+i+" ; j="+j+"  : Date="+date1.getTime(),
						yearDiff, 
						DateUtils.differenceBetweenIntervals(date1.getTime(), now, timeInterval));
						}
					}
					diff++;
				}
				break;		
			
			case  MONTH://////NOT PASSING TEST
			diff=-12;//run from -12 years to 12 years
				while(diff <= Math.abs( -12 )){
					date1=Calendar.getInstance();
					date1.add(Calendar.YEAR, diff);
					date1.set(Calendar.MONTH, Calendar.JANUARY);
					
					for (int i = 0; i < 12; i++) {// loop through each month
						date1.set(Calendar.MONTH, i);
						for (int j = 1; j < 30; j++) {//day by day 
						date1.set(Calendar.DATE, j);
						
						Date now=new Date();
						
						long msDiff = DateUtils.truncateDatetoDate(date1.getTime()).getTime() - 
										DateUtils.roundoffDatetoDate(now).getTime();					
						//if y1 and m1 are the year and month of the first date, 
						//and y2 and m2 are the year and month of the second
						//(y2 - y1) * 12 + (m2 - m1) + 1;
						//int yearDiff=((date1.getTime().getYear() - now.getYear()) * 12 )+ (date1.getTime().getMonth() - now.getMonth()) + 1;

						
						double yearDiff = (msDiff*12 / 1000 / 60 / 60 / 24 / 365.2);
						System.out.println("i = "+i
								+" ; j = "+j
								+" ; Diff = "+diff
								+" ; monthDiff = "+yearDiff
								+" ; Date = "+date1.getTime());
						assertEquals(
						"MONTH not equal at i="+i+" ; j="+j+"  : Date="+date1.getTime(),
						(int)(yearDiff-0.018), 
						DateUtils.differenceBetweenIntervals(date1.getTime(), now, timeInterval));
						}
					}
					diff++;
				}
				break;			
			case QUARTER_YEAR:
				diff=-12;
				while(diff <= Math.abs( -12 )){
					date1=Calendar.getInstance();
					date1.add(Calendar.YEAR, diff);
					date1.set(Calendar.MONTH, Calendar.JANUARY);
					
					for (int i = 0; i < 12; i++) {// loop through each month
						date1.set(Calendar.MONTH, i);
						for (int j = 1; j < 30; j=j+8) {//day by day avg 30 days in a month
						date1.set(Calendar.DATE, j);
						
						Date now=new Date();
						
						long msDiff = DateUtils.truncateDatetoDate(date1.getTime()).getTime() - 
											DateUtils.roundoffDatetoDate(now).getTime();
						double yearDiff =  (msDiff / 1000 / 60 / 60 / 24 / 365.25);
						System.out.println("i = "+i
								+" ; j = "+j
								+" ; Diff = "+diff
								+" ; quarterYearDiff = "+(int)((yearDiff-0.01) *4)+"  : "+(yearDiff-0.01)*4
								+" ; result = "+DateUtils.differenceBetweenIntervals(date1.getTime(), now, timeInterval)
								+" ; Date = "+date1.getTime());
						assertEquals(
						"QUARTER_YEAR not equal at i="+i+" ; j="+j+"  : Date="+date1.getTime(),
						(int)((yearDiff-0.01) * 4), 
						DateUtils.differenceBetweenIntervals(date1.getTime(), now, timeInterval));
						}
					}
					diff++;
				}
				break;
				
			case HALF_YEAR:
				diff=-12;
				while(diff <= Math.abs( -12)){
					date1=Calendar.getInstance();
					date1.add(Calendar.YEAR, diff);
					date1.set(Calendar.MONTH, Calendar.JANUARY);
					
					for (int i = 0; i < 12; i++) {// loop through each month
						date1.set(Calendar.MONTH, i);
						for (int j = 1; j < 30; j=j+8) {//day by day avg 30 days in a month
						date1.set(Calendar.DATE, j);
						
						Date now=new Date();
						
						long msDiff = date1.getTime().getTime() - now.getTime();
						double yearDiff =  (msDiff / 1000 / 60 / 60 / 24 / 365.2425);
						System.out.println("i = "+i
								+" ; j = "+j
								+" ; Diff = "+diff
								+" ; halfYearDiff = "+(int)((yearDiff-0.01) *2)+"  : "+(yearDiff-0.01) *2
								+" ; result = "+DateUtils.differenceBetweenIntervals(date1.getTime(), now, timeInterval)
								+" ; Date = "+date1.getTime());
						assertEquals(
						"HALF_YEAR not equal at i="+i+" ; j="+j+"  : Date="+date1.getTime(),
						(int)((yearDiff-0.01) * 2), 
						DateUtils.differenceBetweenIntervals(date1.getTime(), now, timeInterval));
						}
					}
					diff++;
				}
				break;
				
			case YEAR:
				diff=-12;
				while(diff <= Math.abs( -12 )){
					date1=Calendar.getInstance();
					date1.add(Calendar.YEAR, diff);
					date1.set(Calendar.MONTH, Calendar.JANUARY);
					
					for (int i = 0; i < 12; i++) {// loop through each month
						date1.set(Calendar.MONTH, i);
						for (int j = 1; j < 30; j=j+8) {//day by day avg 30 days in a month
						date1.set(Calendar.DATE, j);
						
						Date now=new Date();
						
						long msDiff = date1.getTime().getTime() - now.getTime();
						double yearDiff =  (msDiff / 1000 / 60 / 60 / 24 / 365.2425);
						System.out.println("i = "+i
								+" ; j = "+j
								+" ; Diff = "+diff
								+" ; YearDiff = "+(int)((yearDiff-0.01))+"  : "+(yearDiff-0.01)
								+" ; result = "+DateUtils.differenceBetweenIntervals(date1.getTime(), now, timeInterval)
								+" ; Date = "+date1.getTime());
						assertEquals(
						"YEAR not equal at i="+i+" ; j="+j+"  : Date="+date1.getTime(),
						(int)((yearDiff-0.01)), 
						DateUtils.differenceBetweenIntervals(date1.getTime(), now, timeInterval));
						}
					}
					diff++;
				}
				break;
			}
			date1=Calendar.getInstance();
			date1.add(Calendar.YEAR, diff);
			date1.set(Calendar.MONTH, Calendar.JANUARY);
			date1.set(Calendar.HOUR_OF_DAY, 00);
			date1.set(Calendar.MINUTE, 00);
			date1.set(Calendar.SECOND, 00);
			
			System.out.println("for year :"+diff);

		}
		System.out.println("complete tests");
	}
/*	public static final int getMonthsDifference(Date date1, Date date2) {
	    int m1 = date1.getYear() * 12 + date1.getMonth();
	    int m2 = date2.getYear() * 12 + date2.getMonth();
	    return m1 - m2 ;
	}
	
	public int getNumberOfMonths(Date fromDate, Date toDate){
		int monthCount=0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(fromDate);
		int c1date=cal.get(Calendar.DATE);
		int c1month=cal.get(Calendar.MONTH);
		int c1year=cal.get(Calendar.YEAR);
		cal.setTime(toDate);
		int c2date=cal.get(Calendar.DATE);
		int c2month=cal.get(Calendar.MONTH);
		int c2year=cal.get(Calendar.YEAR);

		monthCount = ((c2year-c1year)*12) + (c2month-c1month) + ((c2date>=c1date)?1:0);

		return monthCount;
	}*/

}