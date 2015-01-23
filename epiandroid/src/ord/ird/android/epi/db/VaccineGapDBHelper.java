package ord.ird.android.epi.db;

import java.util.ArrayList;
import java.util.List;

import org.ird.android.epi.common.EpiUtils;
import org.ird.android.epi.dal.VaccineGapTypeService;
import org.ird.android.epi.dal.VaccineService;
import org.ird.android.epi.model.Vaccine;
import org.ird.android.epi.model.VaccineGap;
import org.ird.android.epi.model.VaccineGapType;
import org.ird.android.epi.model.VaccineHelper;

import android.content.ContentValues;
import android.content.Context;

public class VaccineGapDBHelper
{
	private Context context = null;
	private DatabaseUtil dbUtil = null;

	private static final String TABLE_VACCINEGAP = "vaccinegap";

	private static final String FIELD_VACCINE_ID = "vaccineId";
	private static final String FIELD_VACCINEGAP_VACCINEGAPTYPEID = "vaccineGapTypeId";
	private static final String FIELD_VACCINEGAP_GAPTIMEUNIT = "gapTimeUnit";
	private static final String FIELD_VACCINEGAP_VALUE = "value";

	public VaccineGapDBHelper(Context cxt)
	{
		this.context = cxt;
		if (context != null && dbUtil == null)
		{
			dbUtil = new DatabaseUtil(context);
		}
	}

	public boolean saveVaccineGaps(List<VaccineGap> vaccineGaps)
	{
		boolean isSaved = false;
		ContentValues values;

		boolean[] flags = new boolean[vaccineGaps.size()];
		int flagCounter = 0;

		for (VaccineGap vaccineGap : vaccineGaps)
		{
			/*
			 * Columns in the order of "vaccineGapTypeId","vaccineId", "gapTimeUnit", "value"
			 */

			values = new ContentValues();
			values.put(FIELD_VACCINEGAP_VACCINEGAPTYPEID, vaccineGap.getGapType().getId());
			values.put(FIELD_VACCINE_ID, vaccineGap.getVacine().getId());
			values.put(FIELD_VACCINEGAP_GAPTIMEUNIT, vaccineGap.getTimeUnit());
			values.put(FIELD_VACCINEGAP_VALUE, vaccineGap.getValue());

			flags[flagCounter] = dbUtil.insert(TABLE_VACCINEGAP, values);
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

	public List<VaccineGap> getAllGaps()
	{
		DatabaseUtil dbUtil = new DatabaseUtil(context);
		List<VaccineGap> vaccineGap = new ArrayList<VaccineGap>();
		VaccineGap temp;
		String[][] values = dbUtil.getTableData("select * from " + TABLE_VACCINEGAP);

		for (int i = 0; i < values.length; i++)
		{
			/*
			 * Columns in the order of "vaccineGapTypeId","vaccineId", "gapTimeUnit", "value"
			 */
			Vaccine tempVaccine;
			Integer vaccineId, value, vacGapTypeId;
			String gapTimeUnit;

			vacGapTypeId = Integer.valueOf(values[i][0]);

			// getting vacGapType by id from sqlite DB
			VaccineGapType vacGapType = VaccineGapTypeService.getVaccineGapTypeById(vacGapTypeId, context);

			vaccineId = Integer.valueOf(values[i][1]);
			
			// getting vaccine by id from sqlite DB
			tempVaccine = VaccineService.getVaccineById(vaccineId, context);


			gapTimeUnit = String.valueOf(values[i][2]);
			value = Integer.valueOf(values[i][3]);

			temp = new VaccineGap(tempVaccine, vacGapType, gapTimeUnit, value);		

			vaccineGap.add(temp);
		}

		return vaccineGap;
	}
}
