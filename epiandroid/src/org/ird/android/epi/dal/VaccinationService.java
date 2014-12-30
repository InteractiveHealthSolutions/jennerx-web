package org.ird.android.epi.dal;

import java.util.ArrayList;
import java.util.List;

import org.ird.android.epi.model.Child;
import org.ird.android.epi.model.Vaccination;

public class VaccinationService
{
	
	public static Vaccination[] getVaccinationByChild(Child child)
	{
		List<Vaccination> vaccinations = new ArrayList<Vaccination>();
		Vaccination[] array =new Vaccination[ vaccinations.size()];
		return vaccinations.toArray(array);
	}
	
	public static boolean saveVaccination(Vaccination vaccination)
	{
		return false;
	}
}
