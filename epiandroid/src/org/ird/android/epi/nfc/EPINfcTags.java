package org.ird.android.epi.nfc;

public enum EPINfcTags
{
	LOTTERY ("lottery"),
	CHILD ("childID");
	
	private String tagIdentifier;
	
	EPINfcTags (String str)
	{
		this.tagIdentifier = str;
	}
	
	@Override
	public String toString()
	{
		return this.tagIdentifier;
	}

	public String getTagIdentifier()
	{
		return tagIdentifier;
	}

	public void setTagIdentifier(String tagIdentifier)
	{
		this.tagIdentifier = tagIdentifier;
	}
	
}
