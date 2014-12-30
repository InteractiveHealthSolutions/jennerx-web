package org.ird.android.epi.model;

import java.io.Serializable;
import java.util.Date;

public class Child implements Serializable {
	String projectId;
	String epiNumber;
	String fatherFirstName;
	//String fatherLastName;
	String childFirstName;
	//String childLastName;
	String gender;
	Date dateOfBirth;
	boolean isEstimated;
	boolean isNamed;
	int month;
	int year;
	int week;
	int days;
	
	@Override
	public boolean equals(Object o)
	{
		if((!(o instanceof Child)) || o == null)
			return false;
		
		
		if (((Child)o).getProjectId()!=null)
		{
			if (this.projectId.equals(((Child) o).projectId))
				return true;
		}
		return false;
	}
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getEpiNumber() {
		return epiNumber;
	}
	public void setEpiNumber(String epiNumber) {
		this.epiNumber = epiNumber;
	}
	public String getFatherFirstName() {
		return fatherFirstName;
	}
	public void setFatherFirstName(String fatherFirstName) {
		this.fatherFirstName = fatherFirstName;
	}
	
	public boolean isNamed()
	{
		return isNamed;
	}

	public void setNamed(boolean isNamed)
	{
		this.isNamed = isNamed;
	}

	public String getChildFirstName() {
		return childFirstName;
	}
	public void setChildFirstName(String childFirstName) {
		this.childFirstName = childFirstName;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirht(Date dateOfBirht) {
		this.dateOfBirth = dateOfBirht;
	}
	
	public String getGender()
	{
		return gender;
	}

	public void setGender(String gender)
	{
		this.gender = gender;
	}

	public void setDateOfBirth(Date dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}

	public boolean isEstimated() {
		return isEstimated;
	}
	public void setEstimated(boolean isEstimated) {
		this.isEstimated = isEstimated;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	
	
}
