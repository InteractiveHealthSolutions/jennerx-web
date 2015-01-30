package org.ird.android.epi.model;

public class LotteryWon
{
	String verrificationCode;
	String amount;
	boolean isSelected;
	String vaccinationRecordNum;
	
	public String getVerrificationCode()
	{
		return verrificationCode;
	}
	public void setVerrificationCode(String verrificationCode)
	{
		this.verrificationCode = verrificationCode;
	}
	public String getAmount()
	{
		return amount;
	}
	public void setAmount(String amount)
	{
		this.amount = amount;
	}
	public boolean isSelected()
	{
		return isSelected;
	}
	public void setSelected(boolean isSelected)
	{
		this.isSelected = isSelected;
	}
	public String getVaccinationRecordNum()
	{
		return vaccinationRecordNum;
	}
	public void setVaccinationRecordNum(String vaccinationRecordNum)
	{
		this.vaccinationRecordNum = vaccinationRecordNum;
	}
}
