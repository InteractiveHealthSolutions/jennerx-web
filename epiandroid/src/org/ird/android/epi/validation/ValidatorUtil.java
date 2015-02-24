package org.ird.android.epi.validation;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.ird.android.epi.R;
import org.ird.android.epi.common.DateTimeUtils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;


public class ValidatorUtil extends Activity
{
	private static String TAG_VALIDATOR_UTIL = "ValidatorUtils";
	private static Resources res;
	private Context _context;
	String[] vaccines;

	// indices for vaccines
	private static short VACCINE_BCG = 0;
	private static short VACCINE_P1 = 1;
	private static short VACCINE_P2 = 2;
	private static short VACCINE_P3 = 3;
	private static short VACCINE_M1 = 4;
	private static short VACCINE_M2 = 5;

	private final Integer NAME_REQUIRED_LENGTH = 3;

	// hiding default constructor
	private ValidatorUtil()
	{
	}

	public ValidatorUtil(Context cxt)
	{
		this._context = cxt;
		vaccines = _context.getResources().getStringArray(R.array.array_vaccine);
	}

	/**
	 * The method is called to highlight a View to show that it has failed the validation
	 * 
	 * @param control
	 *            The UI element ie View that has failed validation
	 * @param activity
	 *            the activity making the request
	 * @param result
	 *            the ValidatorResult object which contains the error mesage and whether this
	 *            is a warning or an error
	 * @return
	 */
	public boolean setError(View control, Context activity, ValidatorResult result)
	{
		try
		{
			control.setTag(result.getMessage());
			if (control instanceof TextView)
			{
				((TextView) control).setTextColor(_context.getResources().getColor(R.color.Red));
			}
			else if (control instanceof Spinner)
			{
				control.setBackgroundColor(_context.getResources().getColor(R.color.Red));

			}
			else if (control instanceof CheckBox)
			{
				((CheckBox) control).setTextColor(_context.getResources().getColor(R.color.Red));
			}
			control.setTag(result.getMessage());
		}
		catch (Exception e)
		{
			Log.e(TAG_VALIDATOR_UTIL, e.getMessage());
			e.printStackTrace();
			// error could not be set on the control.
			return false;
		}

		// successfully applied error status to this control
		return true;
	}

	public ValidatorResult validateChildId(String id)
	{
		if (res == null)
		{
			res = this._context.getResources();
		}

		if ((id == null) || !id.matches(Regex.CHILD_ID.toString()) || !id.matches(Regex.NUMERIC.toString()))
		{
			return fillError(this._context.getString(R.string.ru_validation_error_childid_invalid));
		}

		return sendSuccess();
	}

	public ValidatorResult validateEpiNo(String no)
	{
		if (no.length() != 8)
		{
			return fillError(this._context.getString(R.string.ru_validation_error_epino_empty));
		}

		if (!no.startsWith("201"))
		{
			return fillError(this._context.getString(R.string.ru_validation_error_epino_201));
		}

		if (!no.matches("^201[0-9]{5}"))
		{
			return fillError(this._context.getString(R.string.ru_validation_error_epino_empty));
		}

		return sendSuccess();
	}


	/**
	 * Validates name against 2 rules
	 * 1) Name can't contain special characters and numbers
	 * 2) Minimum length for Name is 3
	 * 
	 * @param name
	 * @return
	 */
	public ValidatorResult validateName(String name)
	{
		if (TextUtils.isEmpty(name) || name.length() < NAME_REQUIRED_LENGTH)
		{
			return fillError(_context.getString(R.string.ru_validation_error_name_missing_length));
		}

		else if (!name.matches(Regex.WHITESPACE_ALPHA.toString()))
		{
			return fillError(_context.getString(R.string.ru_validation_error_name));
		}

		return sendSuccess();
	}

	/*
	 * TODO: Make the following separate methods for vaccine validations
	 * 1.Overage
	 * 2.Under age
	 * 3.Sequence
	 * 4.Date difference
	 */


	private ValidatorResult validateUnderageVaccine(String vaccineName, Date dateOfBirth, Date currentVaccinationDate)
	{
		if (vaccineName.equals(_context.getString(VACCINE_BCG)))
		{
			return sendSuccess();
		}

		else if (vaccineName.equals(_context.getString(VACCINE_P1)))
		{
			// make 6 weeks gap
			if (DateTimeUtils.daysBetween(dateOfBirth, currentVaccinationDate) < 6 * 7)
			{
				return fillWarning(_context.getString(R.string.ru_validation_error_vaccination_under_age));
			}

		}
		else if (vaccineName.equals(_context.getString(VACCINE_P2)))
		{
			// make 10 weeks gap
			if (DateTimeUtils.daysBetween(dateOfBirth, currentVaccinationDate) < 10 * 7)
			{
				return fillWarning(_context.getString(R.string.ru_validation_error_vaccination_under_age));
			}
		}
		else if (vaccineName.equals(_context.getString(VACCINE_P3)))
		{
			// make 14 weeks gap
			if (DateTimeUtils.daysBetween(dateOfBirth, currentVaccinationDate) < 14 * 7)
			{
				return fillWarning(_context.getString(R.string.ru_validation_error_vaccination_under_age));
			}
		}
		else if (vaccineName.equals(_context.getString(VACCINE_M1)))
		{
			// make 9 months gap
			if (DateTimeUtils.daysBetween(dateOfBirth, currentVaccinationDate) < 36 * 7)
			{
				return fillWarning(_context.getString(R.string.ru_validation_error_vaccination_under_age));
			}
		}
		else if (vaccineName.equals(_context.getString(VACCINE_M2)))
		{
			// make 15 months gap
			if (DateTimeUtils.daysBetween(dateOfBirth, currentVaccinationDate) < 60 * 7)
			{
				return fillWarning(_context.getString(R.string.ru_validation_error_vaccination_under_age));
			}
		}

		return sendSuccess();
	}

	/**
	 * Method to validate the child is not overaged for this vaccine
	 * 
	 * @param vaccineString
	 *            the same of the vaccine to be given
	 * @param dateOfBirth
	 *            child's date of birth
	 * @param currentVaccinationDate
	 *            the date this vaccine is to be administered
	 * @return
	 */
	private ValidatorResult validateOverageVaccine(String vaccineString, Date dateOfBirth, Date currentVaccinationDate)
	{
		if (vaccineString.equals(_context.getString(VACCINE_BCG)))
		{
			// should not be given to children over 1 years old
			if (DateTimeUtils.daysBetween(dateOfBirth, currentVaccinationDate) > 365)
			{
				return fillWarning(_context.getString(R.string.ru_validation_error_vaccination_over_1_year_BCG));
			}
		}

		else if (vaccineString.equals(_context.getString(VACCINE_P1)))
		{
			// should not be given to children over 2 years old
			if (DateTimeUtils.daysBetween(dateOfBirth, currentVaccinationDate) > 365 * 2)
			{
				return fillWarning(_context.getString(R.string.ru_validation_error_vaccination_over_2_years_Pentavalent));
			}
		}
		else if (vaccineString.equals(_context.getString(VACCINE_P2)))
		{
			// Should not be greater than 2 years
			if (DateTimeUtils.daysBetween(dateOfBirth, currentVaccinationDate) > 365 * 2)
			{
				return fillWarning(_context.getString(R.string.ru_validation_error_vaccination_over_2_years_Pentavalent));
			}
		}
		else if (vaccineString.equals(_context.getString(VACCINE_P3)))
		{
			// Should not be greater than 2 years
			if (DateTimeUtils.daysBetween(dateOfBirth, currentVaccinationDate) > 365 * 2)
			{
				return fillWarning(_context.getString(R.string.ru_validation_error_vaccination_over_2_years_Pentavalent));
			}
		}
		else if (vaccineString.equals(_context.getString(VACCINE_M1)))
		{
			// Should not be greater than 5 years
			if (DateTimeUtils.daysBetween(dateOfBirth, currentVaccinationDate) > 365 * 5)
			{
				return fillWarning(_context.getString(R.string.ru_validation_error_vaccination_over_5_years_Measles));
			}
		}
		else if (vaccineString.equals(_context.getString(VACCINE_M2)))
		{
			// Should not be greater than 5 years
			if (DateTimeUtils.daysBetween(dateOfBirth, currentVaccinationDate) > 365 * 5)
			{
				return fillWarning(_context.getString(R.string.ru_validation_error_vaccination_over_5_years_Measles));
			}
		}
		return sendSuccess();
	}

	/***
	 * Method to insure the two vaccine are given in order with the guidelines. Method works for combinations of
	 * both (1)last vaccine and current vaccine and (2) current vaccine and next vaccine
	 * 
	 * @param vaccine1
	 *            vaccine to be given
	 * @param vaccine2
	 *            last vaccine give
	 * @return ValidatorResult object with message and outcome of the validation
	 */
	private ValidatorResult validateVaccineSequence(String vaccine1, String vaccine2)
	{
		boolean isValid = true;
		if (vaccine1.equals(_context.getString(R.string.BCG)))
		{
			isValid = true;
		}

		else if (vaccine2.equals(getVaccineName(VACCINE_BCG)) && (!vaccine1.equals(getVaccineName(VACCINE_P1))))
		{
			isValid = false;
		}
		else if (vaccine2.equals(getVaccineName(VACCINE_P1)) && (!vaccine1.equals(getVaccineName(VACCINE_P2))))
		{
			isValid = false;
		}
		else if (vaccine2.equals(getVaccineName(VACCINE_P2)) && (!vaccine1.equals(getVaccineName(VACCINE_P3))))
		{
			isValid = false;
		}
		else if (vaccine2.equals(getVaccineName(VACCINE_P3)) && (!vaccine1.equals(getVaccineName(VACCINE_M1))))
		{
			isValid = false;
		}
		else if (vaccine2.equals(getVaccineName(VACCINE_M1)) && (!vaccine1.equals(getVaccineName(VACCINE_M2))))
		{
			isValid = false;
		}

		return isValid == true ? sendSuccess() : fillWarning(_context.getString(R.string.ru_validation_error_current_vaccination_out_of_order));
	}


	public ValidatorResult validateAddressCity(String city)
	{
		if (city == null || !city.matches(Regex.WHITESPACE_WORD.toString()))
		{
			return fillError(_context.getResources().getString(R.string.ru_validation_error_city_empty));
		}
		else
			return sendSuccess();
	}

	public ValidatorResult validateAddressTown(String town)
	{
		if (town == null || !town.matches(Regex.WHITESPACE_WORD.toString()))
		{
			return fillError(_context.getResources().getString(R.string.ru_validation_error_town_empty));
		}
		else
			return sendSuccess();
	}


	/**
	 * Validates the date of birth of the child
	 * 
	 * @param birthdate
	 * @return
	 */
	public ValidatorResult validateDateOfBirth(Date birthdate)
	{
		if (birthdate == null)
		{
			return fillError(_context.getString(R.string.ru_validation_error_dob_age_empty));
		}
		if (birthdate.getTime() > new Date().getTime())
		{
			return fillError(_context.getString(R.string.ru_validation_error_dob_future));
		}

		// child can not be more than 5 years old
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, -5);
		if (birthdate.getTime() < cal.getTime().getTime())
		{
			return fillError(_context.getResources().getString(R.string.ru_validation_error_child_more_than_5_years_old));
		}

		return sendSuccess();
	}

	public ValidatorResult validateNextVaccination(String currentVaccine, Date currentDate, String nextVaccine, Date nextDate, Date dateOfBirth)
	{
		ValidatorResult result = null;
		result = validateUnderageVaccine(currentVaccine, dateOfBirth, currentDate);
		if (!result.isValid())
			return fillWarning(result.getMessage());

		result = validateOverageVaccine(currentVaccine, dateOfBirth, currentDate);
		if (!result.isValid())
			return fillWarning(result.getMessage());

		result = validateVaccineSequence(currentVaccine, nextVaccine);
		if (!result.isValid())
			return fillWarning(result.getMessage());

		return sendSuccess();
	}

	private ValidatorResult validateVaccineGap(String currentVaccine, Date vaccinaionDate, String nextVaccine, Date nextVaccinationDate)
	{
		if (currentVaccine.equals(getVaccineName(VACCINE_P1)) && nextVaccine.equalsIgnoreCase(getVaccineName(VACCINE_P2)))
		{
			if (DateTimeUtils.daysBetween(vaccinaionDate, nextVaccinationDate) < 30)
			{
				return fillError(_context.getString(R.string.ru_validation_error_next_vaccination_P1_P2_gap));
			}
		}
		else if (currentVaccine.equals(getVaccineName(VACCINE_P2)) && nextVaccine.equalsIgnoreCase(getVaccineName(VACCINE_P3)))
		{
			if (DateTimeUtils.daysBetween(vaccinaionDate, nextVaccinationDate) < 30)
			{
				return fillError(_context.getString(R.string.ru_validation_error_next_vaccination_P2_P3_gap));
			}
		}
		else if (currentVaccine.equals(getVaccineName(VACCINE_M1)) && nextVaccine.equalsIgnoreCase(getVaccineName(VACCINE_M2)))
		{
			if (DateTimeUtils.daysBetween(vaccinaionDate, nextVaccinationDate) < 30)
			{
				return fillError(_context.getString(R.string.ru_validation_error_next_vaccination_M1_M2_gap));
			}
		}

		return sendSuccess();
	}


	/**
	 * Method for making sure that the vaccination conforms the the age restrictions and the sequence from
	 * the last given vaccine
	 * 
	 * @param isEnrollment
	 *            flag to ignore the last vaccination if this is the first captured vaccination
	 *            in the system
	 * @param dateOfBirth
	 *            needed for determinging the eligibility for a vaccine
	 * @param lastDate
	 *            the date on which the child was last vaccinated
	 * @param lastVaccine
	 *            last vaccine the child recieved
	 * @param currentDate
	 *            date for the current vaccination
	 * @param currentVaccine
	 *            the vaccine being administered
	 * @return
	 */
	public ValidatorResult validateCurrentVaccination(Boolean isEnrollment, Date dateOfBirth, Date lastDate, String lastVaccine, Date currentDate, String currentVaccine)
	{

		// vaccinationDate can not be set for past
		if (currentDate.getTime() < new Date().getTime())
		{
			return fillError(_context.getString(R.string.ru_validation_error_vaccination_date_past));
		}

		// check vaccination
		if (!isEnrollment)
		{
			if (lastVaccine.equals(currentVaccine))
			{
				return fillError(_context.getString(R.string.ru_validation_error_vaccine_given));
			}
		}

		if (dateOfBirth == null)
		{
			return fillError(_context.getString(R.string.ru_validation_error_dob_age_empty));
		}

		ValidatorResult result = null;
		result = validateUnderageVaccine(currentVaccine, dateOfBirth, currentDate);
		if (!result.isValid())
			return fillWarning(result.getMessage());

		result = validateOverageVaccine(currentVaccine, dateOfBirth, currentDate);
		if (!result.isValid())
			return fillWarning(result.getMessage());

		result = validateVaccineSequence(currentVaccine, lastVaccine);
		if (!result.isValid())
			return fillWarning(result.getMessage());

		return sendSuccess();
	}


	public ValidatorResult validateLandlineOrMobile(String no)
	{
		if (!(no.matches(Regex.CELL_NUMBER.toString())) || ((no.matches(Regex.PTCL_LANDLINE_NUMBER.toString())))
				|| ((no.matches(Regex.PTCL_WIRELESS_NUMBER.toString()))))
		{
			String s = getResources().getString(R.string.ru_validation_error_unknown_number_type);
			return fillError(s);
		}
		return sendSuccess();
	}

	public ValidatorResult validateMobile(String no)
	{
		if (!no.matches(Regex.CELL_NUMBER.toString()))
		{
			return fillError(_context.getString(R.string.ru_validation_error_mobile));
		}

		return sendSuccess();
	}

	public ValidatorResult validateLandline(String no)
	{
		if (!no.matches(Regex.PTCL_LANDLINE_NUMBER.toString()))
		{
			return fillError(_context.getString(R.string.ru_validation_error_landline_area_code));
		}

		return sendSuccess();
	}

	private ValidatorResult sendSuccess()
	{
		ValidatorResult vRes = new ValidatorResult();
		vRes.setMessage(this._context.getString(R.string.ru_validation_success));
		vRes.setIsValid(true);
		return vRes;
	}

	private ValidatorResult fillError(String error)
	{
		ValidatorResult result = new ValidatorResult();
		result.setMessage(error);
		result.setIsValid(false);
		result.setDismissable(false);
		return result;
	}

	private ValidatorResult fillWarning(String warning)
	{
		ValidatorResult result = new ValidatorResult();
		result.setMessage(warning);
		result.setIsValid(false);
		result.setDismissable(true);
		return result;
	}


	private String getVaccineName(short index)
	{
		if (vaccines.length == 0)
			vaccines = _context.getResources().getStringArray(R.array.array_vaccine);
		String vacc = null;
		try
		{
			vacc = vaccines[index];
			return vacc;
		}
		catch (ArrayIndexOutOfBoundsException ex)
		{
			VaccineException vex = new VaccineException(VaccineException.VACCINE_NOT_FOUND);
			vex.printStackTrace();
			return "";
		}
	}

	public class VaccineException extends Exception
	{
		public static final String VACCINE_NOT_FOUND = "No matching vaccine could be found";

		public VaccineException(String s)
		{
			super(s);
		}
	}

	public ValidatorResult validateUserName(String userName)
	{
		if (userName == null || userName.trim().equals(""))
		{
			return fillError(_context.getResources().getString(R.string.ru_validation_error_username_empty));
		}

		else if (!userName.matches(Regex.ALPHA_NUMERIC.toString()))
		{
			return fillError(_context.getResources().getString(R.string.ru_validation_error_username));
		}

		return sendSuccess();
	}

	public ValidatorResult validatePassword(String password)
	{

		if (password == null || password.trim().equals(""))
		{
			return fillError(_context.getResources().getString(R.string.ru_validation_error_password_empty));
		}

		else if (!password.matches(Regex.ALPHA_NUMERIC.toString()))
		{
			return fillError(_context.getResources().getString(R.string.ru_validation_error_password));
		}

		return sendSuccess();
	}

}
