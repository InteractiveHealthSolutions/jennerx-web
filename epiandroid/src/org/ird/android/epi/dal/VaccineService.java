package org.ird.android.epi.dal;

import java.util.ArrayList;
import java.util.List;

import ord.ird.android.epi.db.LocationDbHelper;
import ord.ird.android.epi.db.VaccineDBHelper;
import ord.ird.android.epi.db.VaccineGapDBHelper;
import ord.ird.android.epi.db.VaccinePrerequisiteDBHelper;

import org.ird.android.epi.model.Vaccine;
import org.ird.android.epi.model.VaccineConstants;
import org.ird.android.epi.model.VaccineGap;
import org.ird.android.epi.model.VaccineGapType;
import org.ird.android.epi.model.VaccinePrerequisite;

import android.content.Context;

public class VaccineService
{

	private static Vaccine[] allVaccines;
	private static Vaccine[] allSupplementaryVaccines;
	private static VaccineGap[] allGaps;
	private static VaccinePrerequisite[] allPrereqs;


	public static Vaccine[] getAllCompulsoryVaccines(Context cxt)
	{
		if (allVaccines == null)
		{
			getCompulsoryVaccines(cxt);
		}

		return allVaccines;
	}

	public static Vaccine[] getAllSupplementaryVaccines(Context cxt)
	{
		if (allSupplementaryVaccines == null)
		{
			getSupplementaryVaccines(cxt);
		}
		return allSupplementaryVaccines;
	}

	public static VaccinePrerequisite[] getPrerequisiteVaccine(Vaccine targetVaccine, Context cxt)
	{
		if (allPrereqs == null)
		{
			getPrereqs(cxt);
		}

		ArrayList<VaccinePrerequisite> reqs = new ArrayList<VaccinePrerequisite>();

		for (VaccinePrerequisite p : allPrereqs)
		{
			if (p.getVaccine().equals(targetVaccine))
			{
				reqs.add(p);
			}
		}

		VaccinePrerequisite[] array = new VaccinePrerequisite[reqs.size()];
		return reqs.toArray(array);
	}

	public static VaccineGap[] getGapsForVaccine(Vaccine targetVaccine, Context cxt)
	{
		if (allGaps == null)
		{
			getGaps(cxt);
		}
		ArrayList<VaccineGap> targetGaps = new ArrayList<VaccineGap>();

		for (VaccineGap g : allGaps)
		{
			if (g.getVacine().equals(targetVaccine))
			{
				targetGaps.add(g);
			}
		}
		VaccineGap[] gapsArray = new VaccineGap[targetGaps.size()];
		return targetGaps.toArray(gapsArray);
	}

	public static VaccineGap[] getAllGaps(Context cxt)
	{
		if (allGaps == null)
		{
			getGaps(cxt);
		}
		return allGaps;
	}

	private static void getGaps(Context cxt)
	{
		// TODO: Hard code gaps here. download from server and save in db later
		if (allGaps == null)
		{
			if (allVaccines == null)
			{
				getAllCompulsoryVaccines(cxt);
			}

			VaccineGapDBHelper helper = new VaccineGapDBHelper(cxt);
			List<VaccineGap> vaccGapList = helper.getAllGaps();
			allGaps = vaccGapList.toArray(new VaccineGap[vaccGapList.size()]);


			// create gaps for each vaccine
			// ArrayList<VaccineGap> gaps = new ArrayList<VaccineGap>();
			// VaccineGap gap;
			// VaccineGapType gapType;
			// gapType = new VaccineGapType(1, VaccineConstants.GAP_TYPE_BIRTHDATE);
			//
			// gap = new VaccineGap(getVaccineByName("BCG", cxt), 1, gapType, "WEEK", 0, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("Measles1", cxt), 1, gapType, "MONTH", 9, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("Measles2", cxt), 1, gapType, "MONTH", 15, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("OPV0", cxt), 1, gapType, "WEEK", 0, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("OPV1", cxt), 1, gapType, "WEEK", 6, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("OPV2", cxt), 1, gapType, "WEEK", 10, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("OPV3", cxt), 1, gapType, "WEEK", 14, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("PCV1", cxt), 1, gapType, "WEEK", 6, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("PCV2", cxt), 1, gapType, "WEEK", 10, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("PCV3", cxt), 1, gapType, "WEEK", 14, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("Penta1", cxt), 1, gapType, "WEEK", 6, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("Penta2", cxt), 1, gapType, "WEEK", 10, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("Penta3", cxt), 1, gapType, "WEEK", 14, true);
			// gaps.add(gap);
			//
			// gapType = new VaccineGapType(2, VaccineConstants.GAP_TYPE_PREVIOUS_VACCINE);
			//
			// gap = new VaccineGap(getVaccineByName("Measles2", cxt), 1, gapType, "MONTH", 6, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("OPV1", cxt), 1, gapType, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("OPV2", cxt), 1, gapType, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("OPV3", cxt), 1, gapType, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("PCV2", cxt), 1, gapType, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("PCV3", cxt), 1, gapType, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("Penta2", cxt), 1, gapType, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("Penta3", cxt), 1, gapType, "WEEK", 4, true);
			// gaps.add(gap);
			//
			// gapType = new VaccineGapType(4, VaccineConstants.GAP_TYPE_STANDARD);
			//
			// gap = new VaccineGap(getVaccineByName("BCG", cxt), 1, gapType, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("Measles1", cxt), 1, gapType, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("Measles2", cxt), 1, gapType, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("OPV0", cxt), 1, gapType, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("OPV1", cxt), 1, gapType, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("OPV2", cxt), 1, gapType, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("OPV3", cxt), 1, gapType, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("PCV1", cxt), 1, gapType, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("PCV2", cxt), 1, gapType, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("PCV3", cxt), 1, gapType, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("Penta1", cxt), 1, gapType, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("Penta2", cxt), 1, gapType, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("Penta3", cxt), 1, gapType, "WEEK", 4, true);
			// gaps.add(gap);
			//
			// /**
			// * Gap Type LATE
			// */
			// gapType = new VaccineGapType(5, VaccineConstants.GAP_TYPE_LATE);
			//
			// gap = new VaccineGap(getVaccineByName("BCG", cxt), 1, gapType, "YEAR", 2, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("Measles1", cxt), 1, gapType, "YEAR", 2, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("Measles2", cxt), 1, gapType, "YEAR", 2, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("OPV0", cxt), 1, gapType, "DAY", 30, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("OPV1", cxt), 1, gapType, "YEAR", 2, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("OPV2", cxt), 1, gapType, "YEAR", 2, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("OPV3", cxt), 1, gapType, "YEAR", 2, true);
			// gaps.add(gap);
			//
			// // / these late gaps were commented before new expiration date
			//
			// gap = new VaccineGap(getVaccineByName("PCV1", cxt), 1, gapType, "YEAR", 2, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("PCV2", cxt), 1, gapType, "YEAR", 2, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("PCV3", cxt), 1, gapType, "YEAR", 2, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("Penta1", cxt), 1, gapType, "YEAR", 2, true);
			//

			// gaps.add(gap);
			//
			// // / end of commented code
			//
			// gap = new VaccineGap(getVaccineByName("Penta2", cxt), 1, gapType, "YEAR", 2, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("Penta3", cxt), 1, gapType, "YEAR", 2, true);
			// gaps.add(gap);
			//
			// allGaps = new VaccineGap[gaps.size()];
			// gaps.toArray(allGaps);
		}
	}

	private static void getPrereqs(Context cxt)
	{
		VaccinePrerequisiteDBHelper helper = new VaccinePrerequisiteDBHelper(cxt);
		List<VaccinePrerequisite> vaccPrereqList = helper.getAllPrerequisites();
		allPrereqs = vaccPrereqList.toArray(new VaccinePrerequisite[vaccPrereqList.size()]);

		// allPrereqs = new VaccinePrerequisite[8];
		//
		// VaccinePrerequisite prerequisite;
		//
		// prerequisite = new VaccinePrerequisite(getVaccineByName("Penta2", cxt), getVaccineByName("Penta1", cxt), true);
		// allPrereqs[0] = prerequisite;
		//
		// prerequisite = new VaccinePrerequisite(getVaccineByName("Penta3", cxt), getVaccineByName("Penta2", cxt), true);
		// allPrereqs[1] = prerequisite;
		//
		// prerequisite = new VaccinePrerequisite(getVaccineByName("Measles2", cxt), getVaccineByName("Measles1", cxt), true);
		// allPrereqs[2] = prerequisite;
		//
		// prerequisite = new VaccinePrerequisite(getVaccineByName("OPV1", cxt), getVaccineByName("OPV0", cxt), false);
		// allPrereqs[3] = prerequisite;
		//
		// prerequisite = new VaccinePrerequisite(getVaccineByName("OPV2", cxt), getVaccineByName("OPV1", cxt), true);
		// allPrereqs[4] = prerequisite;
		//
		// prerequisite = new VaccinePrerequisite(getVaccineByName("OPV3", cxt), getVaccineByName("OPV2", cxt), true);
		// allPrereqs[5] = prerequisite;
		//
		// prerequisite = new VaccinePrerequisite(getVaccineByName("PCV2", cxt), getVaccineByName("PCV1", cxt), true);
		// allPrereqs[6] = prerequisite;
		//
		// prerequisite = new VaccinePrerequisite(getVaccineByName("PCV3", cxt), getVaccineByName("PCV2", cxt), true);
		// allPrereqs[7] = prerequisite;
	}

	public static Vaccine getVaccineByName(String name, Context cxt)
	{
		if (allVaccines == null)
			getCompulsoryVaccines(cxt);

		for (Vaccine v : allVaccines)
		{
			if (v.getName().equalsIgnoreCase(name))
				return v;
		}

		return null;
	}

	public static Vaccine getVaccineById(Integer id, Context cxt)
	{
		if (allVaccines == null)
			getCompulsoryVaccines(cxt);

		for (Vaccine v : allVaccines)
		{
			if (v.getId().equals(id))
				return v;
		}

		return null;
	}

	public static Vaccine getSupplementaryVaccineByName(String name, Context cxt)
	{
		if (allSupplementaryVaccines == null)
			getSupplementaryVaccines(cxt);

		for (Vaccine v : allSupplementaryVaccines)
		{
			if (v.getName().equalsIgnoreCase(name))
				return v;
		}

		return null;
	}

	private static void getCompulsoryVaccines(Context cxt)
	{
		VaccineDBHelper helper = new VaccineDBHelper(cxt);
		List<Vaccine> vaccList = helper.getCompulsoryVaccines();
		allVaccines = vaccList.toArray(new Vaccine[vaccList.size()]);

		// allVaccines = new Vaccine[13];
		// // TODO: this is ugly, super ugly. Need to download from server and save locally in future
		// Vaccine vacTemp;
		// vacTemp = new Vaccine();
		// vacTemp.setName("BCG");
		// vacTemp.setIsSupplementary(false);
		// allVaccines[0] = vacTemp;
		//
		// vacTemp = new Vaccine();
		// vacTemp.setName("OPV0");
		// vacTemp.setIsSupplementary(false);
		// allVaccines[1] = vacTemp;
		//
		// vacTemp = new Vaccine();
		// vacTemp.setName("Penta1");
		// vacTemp.setIsSupplementary(false);
		// allVaccines[2] = vacTemp;
		//
		// vacTemp = new Vaccine();
		// vacTemp.setName("PCV1");
		// vacTemp.setIsSupplementary(false);
		// allVaccines[3] = vacTemp;
		//
		// vacTemp = new Vaccine();
		// vacTemp.setName("OPV1");
		// vacTemp.setIsSupplementary(false);
		// allVaccines[4] = vacTemp;
		//
		// vacTemp = new Vaccine();
		// vacTemp.setName("Penta2");
		// vacTemp.setIsSupplementary(false);
		// allVaccines[5] = vacTemp;
		//
		// vacTemp = new Vaccine();
		// vacTemp.setName("PCV2");
		// vacTemp.setIsSupplementary(false);
		// allVaccines[6] = vacTemp;
		//
		// vacTemp = new Vaccine();
		// vacTemp.setName("OPV2");
		// vacTemp.setIsSupplementary(false);
		// allVaccines[7] = vacTemp;
		//
		// vacTemp = new Vaccine();
		// vacTemp.setName("Penta3");
		// vacTemp.setIsSupplementary(false);
		// allVaccines[8] = vacTemp;
		//
		// vacTemp = new Vaccine();
		// vacTemp.setName("PCV3");
		// vacTemp.setIsSupplementary(false);
		// allVaccines[9] = vacTemp;
		//
		// vacTemp = new Vaccine();
		// vacTemp.setName("OPV3");
		// vacTemp.setIsSupplementary(false);
		// allVaccines[10] = vacTemp;
		//
		// vacTemp = new Vaccine();
		// vacTemp.setName("Measles1");
		// vacTemp.setIsSupplementary(false);
		// allVaccines[11] = vacTemp;
		//
		// vacTemp = new Vaccine();
		// vacTemp.setName("Measles2");
		// vacTemp.setIsSupplementary(false);
		// allVaccines[12] = vacTemp;

	}

	private static void getSupplementaryVaccines(Context cxt)
	{

		VaccineDBHelper helper = new VaccineDBHelper(cxt);
		List<Vaccine> supVaccList = helper.getSupplementaryVaccines();
		allSupplementaryVaccines = supVaccList.toArray(new Vaccine[supVaccList.size()]);

		// allSupplementaryVaccines = new Vaccine[3];
		//
		// // / Supplementary Vaccines \\\
		// // TODO: this is ugly, super ugly. Need to download from server and save locally in future
		//
		// Vaccine vacTemp;
		//
		// vacTemp = new Vaccine();
		// vacTemp.setName("Measles");
		// vacTemp.setIsSupplementary(true);
		// allSupplementaryVaccines[0] = vacTemp;
		//
		// vacTemp = new Vaccine();
		// vacTemp.setName("OPV");
		// vacTemp.setIsSupplementary(true);
		// allSupplementaryVaccines[1] = vacTemp;
		//
		// vacTemp = new Vaccine();
		// vacTemp.setName("IPV");
		// vacTemp.setIsSupplementary(true);
		// allSupplementaryVaccines[2] = vacTemp;
	}


	public static Boolean isSupplementaryVaccine(String name, Context cxt)
	{
		if (allSupplementaryVaccines == null)
		{
			getSupplementaryVaccines(cxt);
		}

		for (Vaccine v : allSupplementaryVaccines)
		{
			if (v.getName().equalsIgnoreCase(name))
			{
				if (v.isSupplementary() != null)
				{
					return v.isSupplementary();
				}
			}
		}

		if (allVaccines == null)
		{
			getCompulsoryVaccines(cxt);
		}

		for (Vaccine v : allVaccines)
		{
			if (v.getName().equalsIgnoreCase(name))
			{
				if (v.isSupplementary() != null)
				{
					return v.isSupplementary();
				}
			}
		}

		return null;
	}

}
