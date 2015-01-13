package org.ird.android.epi.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.ird.android.epi.VaccinationStatus;
import org.ird.android.epi.alert.VaccineScheduleRow;
import org.ird.android.epi.common.DateTimeUtils;
import org.ird.android.epi.common.EpiUtils;
import org.ird.android.epi.dal.VaccineService;
import org.ird.android.epi.validation.VaccinationValidator;

import android.R.integer;

public class VaccineHelper
{
	static OrderedVaccine[] orderedVaccines = null;
	static Vaccine[] vaccines = null;
	static int[] vaccineDays = null;
	public static boolean isSorted = false;
	private static VaccineGap[] allGaps;

	private static void calculateDaysFromBirth()
	{
		vaccines = VaccineService.getAllVaccines();
		vaccineDays = new int[vaccines.length];
		orderedVaccines = new OrderedVaccine[vaccines.length];

		VaccineGap[] currentGaps;
		int daysFromBirth = 0;
		OrderedVaccine orderVac;

		for (int i = 0; i < vaccines.length; i++)
		{
			currentGaps = VaccineService.getGapsForVaccine(vaccines[i]);
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(new Date());
			for (VaccineGap gap : currentGaps)
			{
				if (gap.getGapType().equals(VaccineConstants.GAP_TYPE_BIRTHDATE))
				{
					if (gap.getTimeUnit().equals(VaccineConstants.GAP_TIME_DAY))
					{
						cal.add(Calendar.DAY_OF_MONTH, gap.getValue());
						// daysFromBirth = gap.getValue();
					}
					else if (gap.getTimeUnit().equals(VaccineConstants.GAP_TIME_WEEK))
					{
						cal.add(Calendar.WEEK_OF_MONTH, gap.getValue());
						// daysFromBirth = gap.getValue() * 7;
					}
					else if (gap.getTimeUnit().equals(VaccineConstants.GAP_TIME_MONTH))
					{
						cal.add(Calendar.MONTH, gap.getValue());
						// daysFromBirth = gap.getValue() * 30;
					}
					daysFromBirth = DateTimeUtils.daysBetween(new Date(), cal.getTime());
					vaccineDays[i] = daysFromBirth;
					orderVac = new OrderedVaccine(vaccines[i], daysFromBirth);
					orderedVaccines[i] = orderVac;
				}
			}
		}
		Arrays.sort(orderedVaccines);
	}

	public static Date calculateDueDate(Vaccine vaccine, Date dateOfBirth, Vaccination[] vaccinations)
	{
		Calendar cal = GregorianCalendar.getInstance();
		Date dueDate = null;
		Date prerequisiteDate = null;
		Date previousDate = null;
		Date lastVaccinationDate = null;

		// get all the gaps for the vaccine
		VaccineGap[] gaps = VaccineService.getGapsForVaccine(vaccine);

		// DATE OF BIRTH GAP

		// first obtain date with respect to birth date
		VaccineGap dobGap = fetchGap(gaps, VaccineConstants.GAP_TYPE_BIRTHDATE);
		if (dobGap != null)
		{
			dueDate = getDueDatefromBirth(vaccine.getName(), dateOfBirth);
			cal.setTime(dueDate);
		}

		// //PREVIOUS GAP////////
		// check gap from the last vaccine
		VaccineGap previousGap = fetchGap(gaps, VaccineConstants.GAP_TYPE_PREVIOUS_VACCINE);
		if (previousGap != null)
		{
			VaccinePrerequisite[] reqs = VaccineService.getPrerequisiteVaccine(vaccine);
			/*
			 * ASSUMPTION: THERE IS ALWAYS ONE PREREQUISITE. WILL HAVE TO CHANGE THIS
			 * WILL HAVE TO CHANGE THIS METHOD IF THIS CHANGES
			 */

			if (reqs.length == 1 && reqs[0] != null)// get the only prerequisite this vaccine has got
			{
				for (Vaccination vaccination : vaccinations)
				{
					// check if the pre-requisite vaccine has been yet
					if (vaccination.getGivenVaccine().equals(reqs[0].getPrereq())
							&& vaccination.isGiven())
					{
						prerequisiteDate = vaccination.getVaccinationDate();
						break;
					}
				}

				// the preqrequisite was given so lets calculate the adjusted date as per the second rule
				if (prerequisiteDate != null)
				{
					Calendar calPrereq = GregorianCalendar.getInstance();
					calPrereq.setTime(previousDate);
					if (previousGap.getTimeUnit() == VaccineConstants.GAP_TIME_DAY)
					{
						calPrereq.add(Calendar.DAY_OF_MONTH, previousGap.getValue());
					}
					else if (previousGap.getTimeUnit() == VaccineConstants.GAP_TIME_WEEK)
					{
						calPrereq.add(Calendar.WEEK_OF_YEAR, previousGap.getValue());
					}
					else if (previousGap.getTimeUnit() == VaccineConstants.GAP_TIME_MONTH)
					{
						calPrereq.add(Calendar.MONTH, previousGap.getValue());
					}
					// if the calculated is after the default dueDate then go with this gap time
					if (calPrereq.getTime().getTime() > dueDate.getTime())
					{
						dueDate = calPrereq.getTime();
					}
				}
			}

			// //////STANDARD GAP//////////

			// sort the vaccinations first
			Arrays.sort(vaccinations);

			// look for the last given vaccination
			for (int i = vaccinations.length - 1; i >= 0; i--)
			{
				if (vaccinations[i].isGiven())
				{
					lastVaccinationDate = vaccinations[i].getVaccinationDate();
					break;
				}
			}
			// the last vaccinated date was found
			if (lastVaccinationDate != null)
			{
				Calendar calStandard = GregorianCalendar.getInstance();
				calStandard.setTime(lastVaccinationDate);
				calStandard.add(Calendar.MONTH, 1);

				// if the duedate is ahead of this calculated date then let it be
				if (dueDate.getTime() < calStandard.getTime().getTime())
				{
					dueDate = calStandard.getTime();
				}
			}
		}

		return dueDate;
	}

	public static Date getDueDatefromBirth(String vaccine, Date dateOfBirth)
	{
		if (!isSorted)
			sortVaccines();
		int daysToAdd = 0;
		for (int i = 0; i < vaccines.length; i++)
		{
			if (vaccines[i].getName().equals(vaccine))
			{
				daysToAdd = vaccineDays[i];
				break;
			}
		}

		Calendar cal = GregorianCalendar.getInstance();
		// If no date of birth is passed
		if (dateOfBirth == null)
			cal.setTime(new Date());
		else
			cal.setTime(dateOfBirth);

		cal.add(Calendar.DAY_OF_MONTH, daysToAdd);
		return cal.getTime();
	}

	public static Date getDueDate(Date dateOfBirth, VaccineScheduleRow row, ArrayList<VaccineScheduleRow> rows)
	{
		Date dueDatefromDOB = VaccineHelper.getDueDatefromBirth(row.getVaccineName(), dateOfBirth != null ? dateOfBirth : null);

		// Copied from VaccineValidator
		Vaccine vaccine = VaccineService.getVaccineByName(row.getVaccineName());
		Date prerequisiteVaccinationDate = null;

		// check gap from the last vaccine
		VaccineGap previousGap = VaccineHelper.fetchGap(vaccine, VaccineConstants.GAP_TYPE_PREVIOUS_VACCINE);

		// no gap hence no need to check anything else, all's fine :)
		if (previousGap == null)
		{
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(dueDatefromDOB);

			// Initializing calender else it will not be initialized
			// Integer calInitializer = cal.get(Calendar.DAY_OF_WEEK);
			return moveDateIfSunday(cal);
		}

		VaccinePrerequisite[] reqs = VaccineService.getPrerequisiteVaccine(vaccine);
		/*
		 * ASSUMPTION: THERE IS ALWAYS ONE PREREQUISITE. WILL HAVE TO CHANGE
		 * THIS WILL HAVE TO CHANGE THIS METHOD IF THIS CHANGES
		 */
		Calendar calDOBandPrereq = null;

		if (reqs.length == 1 && reqs[0] != null)// get the only prerequisite
												// this vaccine has got
		{
			for (VaccineScheduleRow _row : rows)
			{
				if (_row.getVaccineName().equals(reqs[0].getPrereq().getName()))
				{
					// if this _row is not set to any status then set due date from DOB
					if (_row.getStatus() == null)
					{
						// this code producing 1 less day for dueDatefromDOB = 41 for penta1

						calDOBandPrereq = GregorianCalendar.getInstance();
						calDOBandPrereq.setTime(dueDatefromDOB);
						return moveDateIfSunday(calDOBandPrereq);

						// check if the pre-requisite vaccine (_row) has been given yet
						// I am in doubt that this method will be called ever
						// if (_row.isGiven() == true)
						// {
						// prerequisiteVaccinationDate = _row.getVaccinationDate();
						//
						// calDOBandPrereq = GregorianCalendar.getInstance();
						// calDOBandPrereq.setTime(prerequisiteVaccinationDate);
						// calDOBandPrereq.get(Calendar.DAY_OF_YEAR);
						//
						// if (previousGap.getTimeUnit() == VaccineConstants.GAP_TIME_DAY)
						// {
						// calDOBandPrereq.add(Calendar.DAY_OF_MONTH, previousGap.getValue());
						// }
						// else if (previousGap.getTimeUnit() == VaccineConstants.GAP_TIME_WEEK)
						// {
						// calDOBandPrereq.add(Calendar.WEEK_OF_YEAR, previousGap.getValue());
						// }
						// else if (previousGap.getTimeUnit() == VaccineConstants.GAP_TIME_MONTH)
						// {
						// calDOBandPrereq.add(Calendar.MONTH, previousGap.getValue());
						// }
						//
						// // If previousGap is not satisfying gap from DOB then return dueDatefromDOB
						// if (dueDatefromDOB.getTime() >= calDOBandPrereq.getTime().getTime())
						// {
						// Calendar _cal = GregorianCalendar.getInstance();
						// _cal.setTime(dueDatefromDOB);
						//
						// // Initializing calender else it will not be initialized
						// // Integer calInitializer = _cal.get(Calendar.DAY_OF_WEEK);
						// return moveDateIfSunday(_cal);
						// }
						//
						// return moveDateIfSunday(calDOBandPrereq);
						// }
						//
						// // if preRequisite is not given then return (dueDate from DOB)
						// else if (_row.isGiven() == false)
						// {
						// calDOBandPrereq = GregorianCalendar.getInstance();
						// calDOBandPrereq.setTime(dueDatefromDOB);
						//
						// return moveDateIfSunday(calDOBandPrereq);
						// }
					}

					// If this _row is set to any status other then Retro_Missing then set due date from DOB or previous gap
					// checking with enum name
					else if ((_row.getStatus().equals(VaccinationStatus.RETRO_DATE_MISSING.name()) == false))
					{
						// check if the pre-requisite vaccine (_row) has been given yet
						if (_row.isGiven() == true)
						{
							prerequisiteVaccinationDate = _row.getVaccinationDate();

							calDOBandPrereq = GregorianCalendar.getInstance();
							calDOBandPrereq.setTime(prerequisiteVaccinationDate);
							calDOBandPrereq.get(Calendar.DAY_OF_YEAR);

							if (previousGap.getTimeUnit() == VaccineConstants.GAP_TIME_DAY)
							{
								calDOBandPrereq.add(Calendar.DAY_OF_MONTH, previousGap.getValue());
							}
							else if (previousGap.getTimeUnit() == VaccineConstants.GAP_TIME_WEEK)
							{
								calDOBandPrereq.add(Calendar.WEEK_OF_YEAR, previousGap.getValue());
							}
							else if (previousGap.getTimeUnit() == VaccineConstants.GAP_TIME_MONTH)
							{
								calDOBandPrereq.add(Calendar.MONTH, previousGap.getValue());
							}

							// If previousGap is not satisfying gap from DOB then return dueDatefromDOB
							if (dueDatefromDOB.getTime() >= calDOBandPrereq.getTime().getTime())
							{
								Calendar _cal = GregorianCalendar.getInstance();
								_cal.setTime(dueDatefromDOB);

								// Initializing calender else it will not be initialized
								// Integer calInitializer = _cal.get(Calendar.DAY_OF_WEEK);
								return moveDateIfSunday(_cal);
							}

							return moveDateIfSunday(calDOBandPrereq);
						}
					}
				}
			}
		}

		calDOBandPrereq = GregorianCalendar.getInstance();
		calDOBandPrereq.setTime(dueDatefromDOB);
		return moveDateIfSunday(calDOBandPrereq);
	}

	/**
	 * If given calendar has week day of Sunday then it will move day to Monday
	 * 
	 * @param targetCalendar
	 * @return Date
	 */
	private static Date moveDateIfSunday(Calendar targetCalendar)
	{
		if (targetCalendar != null)
		{
			int dayOfWeek = targetCalendar.get(Calendar.DAY_OF_WEEK);

			if (dayOfWeek == 1)
			{
				targetCalendar.add(Calendar.DAY_OF_YEAR, 1);
				return targetCalendar.getTime();
			}

			return targetCalendar.getTime();
		}

		return null;
	}

	public static Vaccine[] getSortedVaccines()
	{
		// initialize the arrays of both vaccine class and the helper class objects ie OrderedVaccine
		if (vaccines == null || orderedVaccines == null)
			calculateDaysFromBirth();

		if (!isSorted)
		{
			sortVaccines();
		}
		return vaccines;
	}

	private static void sortVaccines()
	{
		for (int i = 0; i < orderedVaccines.length; i++)
		{
			vaccines[i] = orderedVaccines[i].vaccine;
		}
		isSorted = true;
	}

	/**
	 * Helper method for searching relevant gaps that apply to a vaccine
	 * 
	 * @param targetGaps
	 * @param gapType
	 *            String from the class VaccineConstants
	 * @return
	 */
	public static VaccineGap fetchGap(VaccineGap[] targetGaps, String gapType)
	{
		for (VaccineGap gap : targetGaps)
		{
			if (gap.getGapType().equalsIgnoreCase(gapType))
				return gap;
		}
		return null;
	}

	public static VaccineGap fetchGap(Vaccine vaccine, String gapType)
	{
		VaccineGap[] targetGaps = VaccineService.getGapsForVaccine(vaccine);
		for (VaccineGap gap : targetGaps)
		{
			if (gap.getGapType().equalsIgnoreCase(gapType))
				return gap;
		}
		return null;
	}

	/**
	 * This class was created to help sort vaccine class objects
	 * based on their duration from the date of birth. The ordering
	 * is needed when displaying the vaccine(s) in the vaccination
	 * schedule.
	 */
	private static class OrderedVaccine implements Comparable
	{
		Vaccine vaccine;
		int gapFromBirth;

		public OrderedVaccine(Vaccine v, int days)
		{
			vaccine = v;
			gapFromBirth = days;
		}

		public Vaccine getVaccine()
		{
			return vaccine;
		}

		public void setVaccine(Vaccine vaccine)
		{
			vaccine = vaccine;
		}

		public int getGapFromBirth()
		{
			return gapFromBirth;
		}

		public void setGapFromBirth(int gapFromBirth)
		{
			gapFromBirth = gapFromBirth;
		}

		@Override
		public int compareTo(Object otherVaccine)
		{
			if (!(otherVaccine instanceof OrderedVaccine))
				return -1;

			if (gapFromBirth == ((OrderedVaccine) otherVaccine).gapFromBirth)
				return 0;

			else if (gapFromBirth > ((OrderedVaccine) otherVaccine).gapFromBirth)
				return 1;

			else
				return -1;
		}
	}

}