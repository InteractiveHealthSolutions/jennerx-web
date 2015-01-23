package ord.ird.android.epi.db;

import java.util.ArrayList;
import java.util.List;

import org.ird.android.epi.common.EpiUtils;
import org.ird.android.epi.model.Centre;
import org.ird.android.epi.model.Vaccine;

import android.content.ContentValues;
import android.content.Context;

public class VaccineDBHelper
{
	private Context context = null;
	private DatabaseUtil dbUtil = null;

	private static final String TABLE_VACCINE = "vaccine";
	private static final String FIELD_VACCINE_ID = "vaccineId";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_ISSUPPLEMENTARY = "isSupplementary";

	public VaccineDBHelper(Context cxt)
	{
		this.context = cxt;
		if (context != null && dbUtil == null)
		{
			dbUtil = new DatabaseUtil(context);
		}
	}

	public boolean saveVaccines(List<Vaccine> vaccines)
	{
		boolean isSaved = false;
		ContentValues values;

		boolean[] flags = new boolean[vaccines.size()];
		int flagCounter = 0;

		for (Vaccine vaccine : vaccines)
		{
			values = new ContentValues();
			values.put(FIELD_VACCINE_ID, vaccine.getId());
			values.put(FIELD_NAME, vaccine.getName());
			values.put(FIELD_ISSUPPLEMENTARY, vaccine.isSupplementary());

			flags[flagCounter] = dbUtil.insert(TABLE_VACCINE, values);
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

	public List<Vaccine> getCompulsoryVaccines()
	{
		DatabaseUtil dbUtil = new DatabaseUtil(context);
		List<Vaccine> vaccines = new ArrayList<Vaccine>();
		Vaccine temp;
		String[][] values = dbUtil.getTableData("select * from " + TABLE_VACCINE);

		for (int i = 0; i < values.length; i++)
		{
			// if vaccine is supplementary then don't add
			if (EpiUtils.stringToBool((values[i][2])) == false)
			{
				temp = new Vaccine();
				temp.setId(Integer.valueOf(values[i][0]));
				temp.setName(String.valueOf(values[i][1]));
				temp.setIsSupplementary(EpiUtils.stringToBool((values[i][2])));

				vaccines.add(temp);
			}

		}

		return vaccines;
	}

	public List<Vaccine> getSupplementaryVaccines()
	{
		DatabaseUtil dbUtil = new DatabaseUtil(context);
		List<Vaccine> vaccines = new ArrayList<Vaccine>();
		Vaccine temp;
		String[][] values = dbUtil.getTableData("select * from " + TABLE_VACCINE);

		for (int i = 0; i < values.length; i++)
		{
			// if vaccine is supplementary then add it
			if (EpiUtils.stringToBool((values[i][2])) == true)
			{
				temp = new Vaccine();
				temp.setId(Integer.valueOf(values[i][0]));
				temp.setName(String.valueOf(values[i][1]));
				temp.setIsSupplementary(EpiUtils.stringToBool((values[i][2])));

				vaccines.add(temp);
			}

		}

		return vaccines;
	}
}
