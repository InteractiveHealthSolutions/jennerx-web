package org.ird.android.epi.dal;

import java.util.List;

import ord.ird.android.epi.db.VaccineDBHelper;
import ord.ird.android.epi.db.VaccineGapTypeDBHelper;

import org.ird.android.epi.model.Vaccine;
import org.ird.android.epi.model.VaccineGap;
import org.ird.android.epi.model.VaccineGapType;

import android.content.Context;

public class VaccineGapTypeService
{
	private static VaccineGapType[] allGapTypes;

	public static VaccineGapType[] getAllVaccineGapTypes(Context cxt)
	{
		if (allGapTypes == null)
		{
			// getVaccines();
			VaccineGapTypeDBHelper helper = new VaccineGapTypeDBHelper(cxt);
			List<VaccineGapType> vaccList = helper.getAllGapTypes();
			allGapTypes = vaccList.toArray(new VaccineGapType[vaccList.size()]);
		}

		return allGapTypes;
	}

	public static VaccineGapType getVaccineGapTypeByName(String name, Context cxt)
	{
		if (allGapTypes == null)
			getAllVaccineGapTypes(cxt);

		for (VaccineGapType v : allGapTypes)
		{
			if (v.getName().equalsIgnoreCase(name))
				return v;
		}

		return null;
	}

	public static VaccineGapType getVaccineGapTypeById(Integer id, Context cxt)
	{
		if (allGapTypes == null)
			getAllVaccineGapTypes(cxt);

		for (VaccineGapType v : allGapTypes)
		{
			if (v.getId().equals(id))
			{
				return v;
			}
		}

		return null;
	}

}
