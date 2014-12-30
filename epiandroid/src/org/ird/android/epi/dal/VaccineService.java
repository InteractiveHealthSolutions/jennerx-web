package org.ird.android.epi.dal;

import java.util.ArrayList;

import org.ird.android.epi.model.Vaccine;
import org.ird.android.epi.model.VaccineConstants;
import org.ird.android.epi.model.VaccineGap;
import org.ird.android.epi.model.VaccinePrerequisite;

import android.content.Context;

public class VaccineService
{

	private static Vaccine[] allVaccines;
	private static VaccineGap[] allGaps;
	private static VaccinePrerequisite[] allPrereqs;

	public static Vaccine[] getAllVaccines()
	{
		if (allVaccines == null)
		{
			getVaccines();
		}
		return allVaccines;
	}

	public static VaccinePrerequisite[] getPrerequisiteVaccine(Vaccine targetVaccine)
	{
		if (allPrereqs == null)
		{
			getPrereqs();
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

	public static VaccineGap[] getGapsForVaccine(Vaccine targetVaccine)
	{
		if (allGaps == null)
		{
			getGaps();
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

	public static VaccineGap[] getAllGaps()
	{
		if (allGaps == null)
		{
			getGaps();
		}
		return allGaps;
	}

	private static void getGaps()
	{
		// TODO: Hard code gaps here. download from server and save in db later
		if (allGaps == null)
		{
			if (allVaccines == null)
				getAllVaccines();

			// create gaps for each vaccine
			ArrayList<VaccineGap> gaps = new ArrayList<VaccineGap>();
			VaccineGap gap;
			gap = new VaccineGap(getVaccineByName("BCG"), 1, VaccineConstants.GAP_TYPE_BIRTHDATE, "WEEK", 0, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("Measles1"), 1, VaccineConstants.GAP_TYPE_BIRTHDATE, "MONTH", 9, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("Measles2"), 1, VaccineConstants.GAP_TYPE_BIRTHDATE, "MONTH", 15, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("OPV0"), 1, VaccineConstants.GAP_TYPE_BIRTHDATE, "WEEK", 0, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("OPV1"), 1, VaccineConstants.GAP_TYPE_BIRTHDATE, "WEEK", 6, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("OPV2"), 1, VaccineConstants.GAP_TYPE_BIRTHDATE, "WEEK", 10, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("OPV3"), 1, VaccineConstants.GAP_TYPE_BIRTHDATE, "WEEK", 14, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("PCV1"), 1, VaccineConstants.GAP_TYPE_BIRTHDATE, "WEEK", 6, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("PCV2"), 1, VaccineConstants.GAP_TYPE_BIRTHDATE, "WEEK", 10, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("PCV3"), 1, VaccineConstants.GAP_TYPE_BIRTHDATE, "WEEK", 14, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("Penta1"), 1, VaccineConstants.GAP_TYPE_BIRTHDATE, "WEEK", 6, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("Penta2"), 1, VaccineConstants.GAP_TYPE_BIRTHDATE, "WEEK", 10, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("Penta3"), 1, VaccineConstants.GAP_TYPE_BIRTHDATE, "WEEK", 14, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("Measles2"), 1, VaccineConstants.GAP_TYPE_PREVIOUS_VACCINE, "MONTH", 6, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("OPV1"), 1, VaccineConstants.GAP_TYPE_PREVIOUS_VACCINE, "WEEK", 4, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("OPV2"), 1, VaccineConstants.GAP_TYPE_PREVIOUS_VACCINE, "WEEK", 4, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("OPV3"), 1, VaccineConstants.GAP_TYPE_PREVIOUS_VACCINE, "WEEK", 4, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("PCV2"), 1, VaccineConstants.GAP_TYPE_PREVIOUS_VACCINE, "WEEK", 4, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("PCV3"), 1, VaccineConstants.GAP_TYPE_PREVIOUS_VACCINE, "WEEK", 4, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("Penta2"), 1, VaccineConstants.GAP_TYPE_PREVIOUS_VACCINE, "WEEK", 4, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("Penta3"), 1, VaccineConstants.GAP_TYPE_PREVIOUS_VACCINE, "WEEK", 4, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("BCG"), 1, VaccineConstants.GAP_TYPE_STANDARD, "WEEK", 4, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("Measles1"), 1, VaccineConstants.GAP_TYPE_STANDARD, "WEEK", 4, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("Measles2"), 1, VaccineConstants.GAP_TYPE_STANDARD, "WEEK", 4, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("OPV0"), 1, VaccineConstants.GAP_TYPE_STANDARD, "WEEK", 4, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("OPV1"), 1, VaccineConstants.GAP_TYPE_STANDARD, "WEEK", 4, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("OPV2"), 1, VaccineConstants.GAP_TYPE_STANDARD, "WEEK", 4, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("OPV3"), 1, VaccineConstants.GAP_TYPE_STANDARD, "WEEK", 4, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("PCV1"), 1, VaccineConstants.GAP_TYPE_STANDARD, "WEEK", 4, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("PCV2"), 1, VaccineConstants.GAP_TYPE_STANDARD, "WEEK", 4, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("PCV3"), 1, VaccineConstants.GAP_TYPE_STANDARD, "WEEK", 4, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("Penta1"), 1, VaccineConstants.GAP_TYPE_STANDARD, "WEEK", 4, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("Penta2"), 1, VaccineConstants.GAP_TYPE_STANDARD, "WEEK", 4, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("Penta3"), 1, VaccineConstants.GAP_TYPE_STANDARD, "WEEK", 4, true);
			gaps.add(gap);

			/**
			 * Gap Type LATE
			 */

			gap = new VaccineGap(getVaccineByName("BCG"), 1, VaccineConstants.GAP_TYPE_LATE, "YEAR", 2, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("Measles1"), 1, VaccineConstants.GAP_TYPE_LATE, "YEAR", 2, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("Measles2"), 1, VaccineConstants.GAP_TYPE_LATE, "YEAR", 2, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("OPV0"), 1, VaccineConstants.GAP_TYPE_LATE, "DAY", 30, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("OPV1"), 1, VaccineConstants.GAP_TYPE_LATE, "YEAR", 2, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("OPV2"), 1, VaccineConstants.GAP_TYPE_LATE, "YEAR", 2, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("OPV3"), 1, VaccineConstants.GAP_TYPE_LATE, "YEAR", 2, true);
			gaps.add(gap);

			// / these late gaps were commented before new expiration date
			
			gap = new VaccineGap(getVaccineByName("PCV1"), 1, VaccineConstants.GAP_TYPE_LATE, "YEAR", 2, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("PCV2"), 1, VaccineConstants.GAP_TYPE_LATE, "YEAR", 2, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("PCV3"), 1, VaccineConstants.GAP_TYPE_LATE, "YEAR", 2, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("Penta1"), 1, VaccineConstants.GAP_TYPE_LATE, "YEAR", 2, true);

			// gap = new VaccineGap(getVaccineByName("PCV1"), 1, VaccineConstants.GAP_TYPE_LATE, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("PCV2"), 1, VaccineConstants.GAP_TYPE_LATE, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("PCV3"), 1, VaccineConstants.GAP_TYPE_LATE, "WEEK", 4, true);
			// gaps.add(gap);
			// gap = new VaccineGap(getVaccineByName("Penta1"), 1, VaccineConstants.GAP_TYPE_LATE, "WEEK", 4, true);
			gaps.add(gap);

			// / end of commented code

			gap = new VaccineGap(getVaccineByName("Penta2"), 1, VaccineConstants.GAP_TYPE_LATE, "YEAR", 2, true);
			gaps.add(gap);
			gap = new VaccineGap(getVaccineByName("Penta3"), 1, VaccineConstants.GAP_TYPE_LATE, "YEAR", 2, true);
			gaps.add(gap);

			allGaps = new VaccineGap[gaps.size()];
			gaps.toArray(allGaps);
		}
	}

	private static void getPrereqs()
	{
		allPrereqs = new VaccinePrerequisite[8];

		VaccinePrerequisite prerequisite;

		prerequisite = new VaccinePrerequisite(getVaccineByName("Penta2"), getVaccineByName("Penta1"), true);
		allPrereqs[0] = prerequisite;

		prerequisite = new VaccinePrerequisite(getVaccineByName("Penta3"), getVaccineByName("Penta2"), true);
		allPrereqs[1] = prerequisite;

		prerequisite = new VaccinePrerequisite(getVaccineByName("Measles2"), getVaccineByName("Measles1"), true);
		allPrereqs[2] = prerequisite;

		prerequisite = new VaccinePrerequisite(getVaccineByName("OPV1"), getVaccineByName("OPV0"), false);
		allPrereqs[3] = prerequisite;

		prerequisite = new VaccinePrerequisite(getVaccineByName("OPV2"), getVaccineByName("OPV1"), true);
		allPrereqs[4] = prerequisite;

		prerequisite = new VaccinePrerequisite(getVaccineByName("OPV3"), getVaccineByName("OPV2"), true);
		allPrereqs[5] = prerequisite;

		prerequisite = new VaccinePrerequisite(getVaccineByName("PCV2"), getVaccineByName("PCV1"), true);
		allPrereqs[6] = prerequisite;

		prerequisite = new VaccinePrerequisite(getVaccineByName("PCV3"), getVaccineByName("PCV2"), true);
		allPrereqs[7] = prerequisite;
	}

	public static Vaccine getVaccineByName(String name)
	{
		if (allVaccines == null)
			getVaccines();

		for (Vaccine v : allVaccines)
		{
			if (v.getName().equalsIgnoreCase(name))
				return v;
		}

		return null;
	}

	private static void getVaccines()
	{
		allVaccines = new Vaccine[13];
		// TODO: this is ugly, super ugly. Need to download from server and save locally in future
		Vaccine vacTemp;
		vacTemp = new Vaccine();
		vacTemp.setName("BCG");
		allVaccines[0] = vacTemp;

		vacTemp = new Vaccine();
		vacTemp.setName("OPV0");
		allVaccines[1] = vacTemp;

		vacTemp = new Vaccine();
		vacTemp.setName("Penta1");
		allVaccines[2] = vacTemp;

		vacTemp = new Vaccine();
		vacTemp.setName("PCV1");
		allVaccines[3] = vacTemp;

		vacTemp = new Vaccine();
		vacTemp.setName("OPV1");
		allVaccines[4] = vacTemp;

		vacTemp = new Vaccine();
		vacTemp.setName("Penta2");
		allVaccines[5] = vacTemp;

		vacTemp = new Vaccine();
		vacTemp.setName("PCV2");
		allVaccines[6] = vacTemp;

		vacTemp = new Vaccine();
		vacTemp.setName("OPV2");
		allVaccines[7] = vacTemp;

		vacTemp = new Vaccine();
		vacTemp.setName("Penta3");
		allVaccines[8] = vacTemp;

		vacTemp = new Vaccine();
		vacTemp.setName("PCV3");
		allVaccines[9] = vacTemp;

		vacTemp = new Vaccine();
		vacTemp.setName("OPV3");
		allVaccines[10] = vacTemp;

		vacTemp = new Vaccine();
		vacTemp.setName("Measles1");
		allVaccines[11] = vacTemp;

		vacTemp = new Vaccine();
		vacTemp.setName("Measles2");
		allVaccines[12] = vacTemp;

	}

}
