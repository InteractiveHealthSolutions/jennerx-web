package ord.ird.android.epi.db;

import java.util.ArrayList;
import java.util.List;

import org.ird.android.epi.common.EpiUtils;
import org.ird.android.epi.dal.VaccineGapTypeService;
import org.ird.android.epi.dal.VaccineService;
import org.ird.android.epi.model.Vaccine;
import org.ird.android.epi.model.VaccineGap;
import org.ird.android.epi.model.VaccineGapType;
import org.ird.android.epi.model.VaccinePrerequisite;

import android.content.ContentValues;
import android.content.Context;

public class VaccinePrerequisiteDBHelper
{
	private Context context = null;
	private DatabaseUtil dbUtil = null;

	private static final String TABLE_VACCINEPREREQUISITE = "vaccineprerequisite";

	private static final String FIELD_VACCINE_ID = "vaccineId";
	private static final String FIELD_VACCINEPREREQUISITE_ID = "vaccinePrerequisiteId";
	private static final String FIELD_VACCINEPREREQUISITE_MANDATORY = "mandatory";

	public VaccinePrerequisiteDBHelper(Context cxt)
	{
		this.context = cxt;
		if (context != null && dbUtil == null)
		{
			dbUtil = new DatabaseUtil(context);
		}
	}

	public boolean saveVaccinePrerequisite(List<VaccinePrerequisite> vaccinePrerequisites)
	{
		boolean isSaved = false;
		ContentValues values;

		boolean[] flags = new boolean[vaccinePrerequisites.size()];
		int flagCounter = 0;

		for (VaccinePrerequisite vaccinePrereq : vaccinePrerequisites)
		{
			/*
			 * Columns in the order of "vaccineId","vaccinePrerequisiteId", "mandatory"
			 */

			values = new ContentValues();
			values.put(FIELD_VACCINE_ID, vaccinePrereq.getVaccine().getId());
			values.put(FIELD_VACCINEPREREQUISITE_ID, vaccinePrereq.getPrereq().getId());
			values.put(FIELD_VACCINEPREREQUISITE_MANDATORY, vaccinePrereq.isMandatory());

			flags[flagCounter] = dbUtil.insert(TABLE_VACCINEPREREQUISITE, values);
			flagCounter++;
		}

		isSaved = checkAllSaved(flags);
		return isSaved;
	}

	private boolean checkAllSaved(boolean[] flags)
	{
		for (boolean b : flags)
		{
			if (!b)
				return false;
		}
		return true;
	}

	public List<VaccinePrerequisite> getAllPrerequisites()
	{
		DatabaseUtil dbUtil = new DatabaseUtil(context);
		List<VaccinePrerequisite> vaccinePrerequisites = new ArrayList<VaccinePrerequisite>();
		VaccinePrerequisite temp;
		String[][] values = dbUtil.getTableData("select * from " + TABLE_VACCINEPREREQUISITE);

		for (int i = 0; i < values.length; i++)
		{
			/*
			 * Columns in the order of "vaccineId","vaccinePrerequisiteId", "mandatory"
			 */
			Vaccine vaccine, preVaccine;
			Integer vaccineId, prereqVaccineId;
			Boolean isMandatory;

			vaccineId = Integer.valueOf(values[i][0]);

			// getting vaccine by id from sqlite DB
			vaccine = VaccineService.getVaccineById(vaccineId, context);

			prereqVaccineId = Integer.valueOf(values[i][1]);

			// getting prerequisite vaccine by id from sqlite DB
			preVaccine = VaccineService.getVaccineById(prereqVaccineId, context);

			isMandatory = EpiUtils.stringToBool(values[i][2]);

			temp = new VaccinePrerequisite(vaccine, preVaccine, isMandatory);

			vaccinePrerequisites.add(temp);
		}

		return vaccinePrerequisites;
	}
}
