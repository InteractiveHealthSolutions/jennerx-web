package ord.ird.android.epi.db;

import java.util.ArrayList;
import java.util.List;

import org.ird.android.epi.dal.VaccineService;
import org.ird.android.epi.model.Vaccine;
import org.ird.android.epi.model.VaccineGap;
import org.ird.android.epi.model.VaccineGapType;

import android.content.ContentValues;
import android.content.Context;

public class VaccineGapTypeDBHelper
{
	private Context context = null;
	private DatabaseUtil dbUtil = null;

	private static final String TABLE_VACCINEGAPTYPE = "vaccinegaptype";

	private static final String FIELD_VACCINEGAP_VACCINEGAPTYPEID = "vaccineGapTypeId";
	public static final String FIELD_VACCINEGAPTYPE_NAME = "name";

	public VaccineGapTypeDBHelper(Context cxt)
	{
		this.context = cxt;
		if (context != null && dbUtil == null)
		{
			dbUtil = new DatabaseUtil(context);
		}
	}

	public boolean saveVaccineGapTypes(List<VaccineGapType> vaccineGapTypes)
	{
		boolean isSaved = false;
		ContentValues values;

		boolean[] flags = new boolean[vaccineGapTypes.size()];
		int flagCounter = 0;

		for (VaccineGapType vaccineGapType : vaccineGapTypes)
		{
			/**
			 * Columns in the order of "vaccineGapTypeId","name"
			 */

			values = new ContentValues();
			values.put(FIELD_VACCINEGAP_VACCINEGAPTYPEID, vaccineGapType.getId());

			values.put(FIELD_VACCINEGAPTYPE_NAME, vaccineGapType.getName());

			flags[flagCounter] = dbUtil.insert(TABLE_VACCINEGAPTYPE, values);
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

	public List<VaccineGapType> getAllGapTypes()
	{
		DatabaseUtil dbUtil = new DatabaseUtil(context);
		List<VaccineGapType> vaccineGapType = new ArrayList<VaccineGapType>();
		VaccineGapType temp;
		String[][] values = dbUtil.getTableData("select * from " + TABLE_VACCINEGAPTYPE);

		for (int i = 0; i < values.length; i++)
		{
			/**
			 * Columns in the order of "vaccineGapTypeId","name"
			 */

			Integer vacGapTypeId;
			String name;

			vacGapTypeId = Integer.valueOf(values[i][0]);
			name = String.valueOf(values[i][1]);

			temp = new VaccineGapType(vacGapTypeId, name);

			vaccineGapType.add(temp);
		}

		return vaccineGapType;
	}
}
