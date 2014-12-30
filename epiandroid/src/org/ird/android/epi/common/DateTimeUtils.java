package org.ird.android.epi.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.util.Log;

public class DateTimeUtils
{

	private final static String TAG_DATE_TIME_UTILS = "DateTimeUtils";

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(GlobalConstants.DATE_FORMAT);

	public static final String DATE_FORMAT_SHORT = "dd/MM/yyyy";

	/***
	 * Method returns the difference between two dates and always returns as a postive integer
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int daysBetween(Date d1, Date d2)
	{
		int diff = (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
		return diff > 0 ? diff : (diff * -1);
	}

	public static SimpleDateFormat getDateFormat()
	{
		return dateFormat;
	}

	/**
	 * Method to return date in String format to a java.util.Date object
	 * 
	 * @param textDate
	 *            String representing the value of the date
	 * @param format
	 *            Date format of the given text
	 * @return
	 */
	public static Date StringToDate(String textDate, String format)
	{
		if (textDate == null || "".equals(textDate.trim()))
			return null;
		Date dt = null;
		if (format == null)
			format = DATE_FORMAT_SHORT;

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try
		{
			dt = sdf.parse(textDate);
		}
		catch (ParseException e)
		{
			Log.e(TAG_DATE_TIME_UTILS, e.getMessage());
			e.printStackTrace();
		}
		// Same for any other exception
		catch (Exception ex)
		{
			Log.e(TAG_DATE_TIME_UTILS, ex.getMessage());
			ex.printStackTrace();
		}

		return dt;
	}

	public static String DateToString(Date date, String format)
	{
		if (date == null)
			return "";
		SimpleDateFormat sdf;
		if (format == null)
		{
			sdf = dateFormat;
		}
		else
		{
			try
			{
				sdf = new SimpleDateFormat(format);
			}
			catch (Exception ex)
			{
				return null;
			}
		}
		String dateString = sdf.format(date);
		return dateString;
	}

	/**
	 * Method to fetch number of months between two dates
	 * Credit: Roland Tepp @ StakOverflow
	 * 
	 * @param olderDate
	 * @param newerDate
	 * @return
	 */
	public static final int getMonthsDifference(Date olderDate, Date newerDate)
	{
		int m1 = olderDate.getYear() * 12 + olderDate.getMonth();
		int m2 = newerDate.getYear() * 12 + newerDate.getMonth();
		return m2 - m1;
	}

	public static Date getTodaysDateWithoutTime()
	{
		Date today;
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		today = cal.getTime();
		return today;
	}

}
