package org.ird.android.epi.dal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.ird.android.epi.model.Child;

public class ChildService
{
	private static ArrayList<Child> allChildren;
	
	public static Child getChildById(String id)
	{
		getChildren();
		
		for(Child ch : allChildren)
		{
			if(ch.getProjectId()!= null && ch.getProjectId().equalsIgnoreCase(id))
			{
				return ch;
			}
		}
		return null;
	}
	
	private static void getChildren()
	{
		if(allChildren== null || allChildren.size()==0)
		{
			allChildren = new ArrayList<Child>();			
			
			Child ch = new Child();
			ch.setProjectId("123456");
			ch.setChildFirstName("Baby");
			//ch.setChildLastName("Test");
			ch.setFatherFirstName("Ali");
			//ch.setFatherLastName("Test");
			Calendar cal = GregorianCalendar.getInstance();
			//0-based index, so 2 for months means 3rd month ie March
			cal.set(2014, 2, 10);
			ch.setDateOfBirht(cal.getTime());
			ch.setEstimated(false);
			
			allChildren.add(ch);			
		}
	}
	
	public static ArrayList<Child> getAllChildren()
	{		
		getChildren();
		return allChildren;
	}
}
