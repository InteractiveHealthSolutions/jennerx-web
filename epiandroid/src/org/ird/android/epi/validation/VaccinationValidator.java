package org.ird.android.epi.validation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.ird.android.epi.VaccinationStatus;
import org.ird.android.epi.alert.VaccineScheduleRow;
import org.ird.android.epi.common.DateTimeUtils;
import org.ird.android.epi.common.EpiUtils;
import org.ird.android.epi.dal.VaccineService;
import org.ird.android.epi.model.Child;
import org.ird.android.epi.model.Vaccine;
import org.ird.android.epi.model.VaccineConstants;
import org.ird.android.epi.model.VaccineGap;
import org.ird.android.epi.model.VaccineHelper;
import org.ird.android.epi.model.VaccinePrerequisite;

import android.content.Context;

public class VaccinationValidator
{
	/***
	 * Method to check the prerequisites not given Assumption: there is only one
	 * prerequisite per vaccine
	 * 
	 * @param name
	 *            the name of the vaccine for which prerequisites are being
	 *            checked
	 * @param rows
	 *            the VaccineScheduleRow objects from the Vaccination schedule
	 *            fragment
	 * @return the List implementation containing all the missing
	 *         vaccinesprerequisites.
	 */
	public static VaccinationValidator.PrerequisiteValidationResult checkPrerequisites(String name, ArrayList<VaccineScheduleRow> rows, Context cxt)
	{

		PrerequisiteValidationResult result = new PrerequisiteValidationResult();
		Vaccine vac = VaccineService.getVaccineByName(name, cxt);
		if (vac == null)
		{
			result.addErrorVaccine("No vaccine found against given name");
			return result;
		}

		VaccinePrerequisite[] prereqs = VaccineService.getPrerequisiteVaccine(vac, cxt);

		if (prereqs.length < 1)
			return result;

		for (int i = 0; i < rows.size(); i++)
		{
			// look for the row representing the prerequisite vaccine
			if (rows.get(i).getVaccineName().equals(prereqs[0].getPrereq().getName()))
			{
				if (rows.get(i).isGiven())
				{
					return result; // the prerequisite is given
				}
			}

			// if this is the last row in the schedule then the add this
			// prerequsitie to the not given list
			if (i == (rows.size() - 1))
			{
				// add to the name of the vaccine to list of warnings
				if (!prereqs[0].isMandatory())
					result.addWarningVaccine(prereqs[0].getPrereq().getName());
				else
					result.addErrorVaccine(prereqs[0].getPrereq().getName());
			}
		}

		return result;
	}

	public static boolean checkPreviousVaccineGap(Date assignedDate, VaccineScheduleRow row, ArrayList<VaccineScheduleRow> rows, Context cxt)
	{

		// Copied from VaccineValidator
		Vaccine vaccine = VaccineService.getVaccineByName(row.getVaccineName(), cxt);
		Date prerequisiteDate = null;

		// check gap from the last vaccine
		VaccineGap previousGap = VaccineHelper.fetchGapByTypeName(vaccine, VaccineConstants.GAP_TYPE_PREVIOUS_VACCINE, cxt);

		// no gap hence no need to check anything else, all's fine :)
		if (previousGap == null)
			return true;

		VaccinePrerequisite[] reqs = VaccineService.getPrerequisiteVaccine(vaccine, cxt);
		/*
		 * ASSUMPTION: THERE IS ALWAYS ONE PREREQUISITE. WILL HAVE TO CHANGE
		 * THIS WILL HAVE TO CHANGE THIS METHOD IF THIS CHANGES
		 */

		if (reqs.length == 1 && reqs[0] != null)// get the only prerequisite
												// this vaccine has got
		{
			for (VaccineScheduleRow _row : rows)
			{
				// check if the pre-requisite vaccine has been yet
				if (_row.getVaccineName().equals(reqs[0].getPrereq().getName()) && _row.isGiven() && !(_row.getStatus().equals(VaccinationStatus.RETRO_DATE_MISSING)))
				{
					prerequisiteDate = _row.getVaccinationDate();
					break;
				}
			}

			// the prerequisite has not been given yet

			/**
			 * to me this seems to be unnecessary
			 */
			/*
			 * if(prerequisiteDate== null) { if(reqs[0].isMandatory()){ return
			 * false; } else{ return true; } }
			 */

			if (prerequisiteDate == null)
				return true;

			// get difference of days between the assigned date and the
			// prerequisite date
			int assignedDateDiff = DateTimeUtils.daysBetween(assignedDate, prerequisiteDate);

			// get the required difference in days
			Calendar calPrereq = GregorianCalendar.getInstance();
			calPrereq.setTime(prerequisiteDate);

			if (previousGap.getTimeUnit().equalsIgnoreCase(VaccineConstants.GAP_TIME_DAY))
			{
				calPrereq.add(Calendar.DAY_OF_MONTH, previousGap.getValue());
			}
			else if (previousGap.getTimeUnit().equalsIgnoreCase(VaccineConstants.GAP_TIME_WEEK))
			{
				calPrereq.add(Calendar.WEEK_OF_YEAR, previousGap.getValue());
			}
			else if (previousGap.getTimeUnit().equalsIgnoreCase(VaccineConstants.GAP_TIME_MONTH))
			{
				calPrereq.add(Calendar.MONTH, previousGap.getValue());
			}

			int requiredDateDiff = DateTimeUtils.daysBetween(calPrereq.getTime(), prerequisiteDate);

			// TODO:
			// requiredDateDiff-3 to allow grace period. Need to get grace
			// period for each vaccine from server and add it over here.
			if (assignedDateDiff >= requiredDateDiff - 3)
			{
				return true;
			}
		}
		return false;
	}



	public static boolean checkStandardGap(Date assignedDate, String vaccineName, VaccineScheduleRow[] rows, int position, Context cxt)
	{
		Vaccine vaccine;
		VaccineGap standardGap;
		Date lastVaccinatedDate;

		vaccine = VaccineService.getVaccineByName(vaccineName, cxt);
		// this vaccine does not exist
		if (vaccine == null)
			return false;
		standardGap = VaccineHelper.fetchGapByTypeName(vaccine, VaccineConstants.GAP_TYPE_STANDARD, cxt);
		if (standardGap == null)
			return true; // the gap does not exist, so this does not apply to it

		Date maxDate = null;
		Date tempDate1 = null;
		Date tempDate2 = null;

		VaccineScheduleRow tempRow1, tempRow2;

		// loop backwards in the rows from the vaccine to get the last
		// vaccinated row
		for (int i = position - 1; i >= 0; i--)
		{
			// only check date that is
			if (rows[i].isGiven())
			{
				// only for the first date check
				if (tempDate2 == null)
				{
					tempDate1 = rows[i].getVaccinationDate();
					tempDate2 = tempDate1;
					maxDate = tempDate1;
				}
				else
				{
					tempDate2 = tempDate1;
					tempDate1 = rows[i].getVaccinationDate();
					if (tempDate1.getTime() > tempDate2.getTime())
					{
						maxDate = tempDate1;
					}
				}
			}
		}
		// no last vaccine was given, so all's clear for this rule
		if (maxDate == null)
			return true;

		// get the date that should be if the standard rule is applied
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(maxDate);
		if (standardGap.getTimeUnit() == VaccineConstants.GAP_TIME_DAY)
		{
			cal.add(Calendar.DAY_OF_MONTH, standardGap.getValue());
		}
		else if (standardGap.getTimeUnit() == VaccineConstants.GAP_TIME_WEEK)
		{
			cal.add(Calendar.WEEK_OF_YEAR, standardGap.getValue());
		}
		else if (standardGap.getTimeUnit() == VaccineConstants.GAP_TIME_MONTH)
		{
			cal.add(Calendar.MONTH, standardGap.getValue());
		}
		// check the assigned date is after the required date and not before it
		if (assignedDate.getTime() >= cal.getTime().getTime())
		{
			return true;
		}
		return false;
	}

	public static boolean checkBirthdateGap(Date assignedDate, ArrayList<VaccineScheduleRow> rows, VaccineScheduleRow row, Date dob, Context cxt)
	{
		Date dueDate;
		Child ch;
		Vaccine vaccine;

		if (assignedDate == null)
			return false;

		vaccine = VaccineService.getVaccineByName(row.getVaccineName(), cxt);

		if (vaccine != null)
		{
			dueDate = VaccineHelper.getDueDatefromBirth(vaccine.getName(), dob);

			/**
			 * Allowing grace period of 3 days.
			 */

			Calendar cal = Calendar.getInstance();
			cal.setTime(dueDate);
			cal.add(Calendar.DATE, -3);
			Date dueDateBefore3Days = cal.getTime();

			// the date assigned by vaccinator can not be before the prescribed
			// time from the date of birth -3days
			if (dueDateBefore3Days.getTime() <= assignedDate.getTime())
				return true;
			else
				return false;

		}
		return false;
	}

	public static boolean checkLateVaccinationGap(VaccineScheduleRow row, Date dob, Context cxt)
	{
		Vaccine vaccine;
		VaccineGap lateGap;

		vaccine = VaccineService.getVaccineByName(row.getVaccineName(), cxt);

		if (vaccine != null)
		{
			lateGap = VaccineHelper.fetchGapByTypeName(vaccine, VaccineConstants.GAP_TYPE_LATE, cxt);

			if (lateGap != null)
			{

				Calendar cal = GregorianCalendar.getInstance();
				cal.setTime(dob);

				if (lateGap.getTimeUnit().equals(VaccineConstants.GAP_TIME_DAY))
				{
					cal.add(Calendar.DAY_OF_MONTH, lateGap.getValue());

				}
				else if (lateGap.getTimeUnit().equals(VaccineConstants.GAP_TIME_WEEK))
				{
					cal.add(Calendar.WEEK_OF_MONTH, lateGap.getValue());

				}
				else if (lateGap.getTimeUnit().equals(VaccineConstants.GAP_TIME_MONTH))
				{
					cal.add(Calendar.MONTH, lateGap.getValue());
				}
				else if (lateGap.getTimeUnit().equals(VaccineConstants.GAP_TIME_YEAR))
				{
					cal.add(Calendar.YEAR, lateGap.getValue());
				}

				if (new Date().getTime() >= cal.getTime().getTime())
				{
					return true;
				}
			}

		}
		return false;
	}

	public static boolean checkAnyVaccinesGivenToday(Context cxt, List<VaccineScheduleRow> rows)
	{
		boolean isVaccineGivenToday = false;
		int countVaccinated = 0;
		for (VaccineScheduleRow row : rows)
		{
			// The vaccine was given today
			if (row.getStatus() != null)
			{
				if (row.getStatus().equals(VaccinationStatus.VACCINATED.name().toString()) && row.isEditable())
				{
					if (DateTimeUtils.getTodaysDateWithoutTime().getTime() == row.getVaccinationDate().getTime())
					{
						countVaccinated++;
					}
				}
			}

		}
		if (countVaccinated > 0)
		{
			isVaccineGivenToday = true;
		}
		else
		{
			EpiUtils.showDismissableDialog(cxt, "You can not proceed without vaccinating at least one vaccine today", "Missing Vaccines").show();
		}
		return isVaccineGivenToday;
	}

	public static boolean checkMissingEligibleVaccines(Context cxt, List<VaccineScheduleRow> rows)
	{
		boolean isValid = true;
		StringBuilder missingVaccines = new StringBuilder();

		missingVaccines.append("Following vaccines need to be vaccinated or scheduled: \n");

		for (VaccineScheduleRow row : rows)
		{
			if (row.isEligible() && !row.isSelected() && row.isEditable() && row.isApplicable())
			{
				if (isValid)
					isValid = false;
				missingVaccines.append(row.getVaccineName() + "\n");
			}
		}

		// if eligibile vaccines were not scheduled or given, then show the
		// warning
		if (!isValid)
			EpiUtils.showDismissableDialog(cxt, missingVaccines.toString(), "Missing Vaccines").show();

		return isValid;
	}

	/**
	 * Class used to hold the result of prerequisite vaccines checked
	 * 
	 * @author Omer
	 * 
	 */

	public static class PrerequisiteValidationResult
	{
		ArrayList<String> warningVaccines = new ArrayList<String>();
		ArrayList<String> errorVaccines = new ArrayList<String>();

		public boolean hasErrors = false;
		public boolean hasWarnings = false;

		public ArrayList<String> getWarningVaccines()
		{
			return warningVaccines;
		}

		public void addWarningVaccine(String warningVaccine)
		{
			if (!hasWarnings)
				hasWarnings = true;

			this.warningVaccines.add(warningVaccine);
		}

		public ArrayList<String> getErrorVaccines()
		{
			return errorVaccines;
		}

		public void addErrorVaccine(String errorVaccine)
		{
			if (!hasErrors)
				hasErrors = true;

			this.errorVaccines.add(errorVaccine);
		}
	}
}
