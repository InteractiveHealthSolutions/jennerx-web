package org.ird.android.epi.nfc;

import java.util.HashMap;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

public class NFCReader
{
	private static String TAG_NFC_READER="NfcReader";
	public static HashMap<String, String> readTagOnly(Intent intent, HashMap<String, String> keyValueMap) 
	{		
		// tag.getId(); //get the universally unique id of the tag
		NdefMessage[] ndefMsgs;
		
		// getting raw data in Parcelable array
		Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		
		/*Converting the raw data in Parcelable array to NDEF format 
		  and storing in NdefMessage array*/
		if (rawMsgs != null) 
		{
			ndefMsgs = new NdefMessage[rawMsgs.length];
			if(ndefMsgs.length ==1)
			{
				ndefMsgs[0] = (NdefMessage) rawMsgs[0];
				NdefRecord[] ndr = ndefMsgs[0].getRecords();
				
				String key;
				for (int j=0;j<ndr.length;j++) 
				{					
					
					//get the id of name value pair
					byte[] id = ndr[j].getId();
					key = new String(id);
					if(keyValueMap.containsKey(key))
					{
						keyValueMap.remove(key);
						keyValueMap.put(key, ndr[j].getPayload().toString());
					}					
				}
				return keyValueMap;
			}
		}	
		return null;

	}
	
	public static String readTag(Intent intent, String keyToSearch)
	{
		// tag.getId(); //get the universally unique id of the tag
		try
		{					
			NdefMessage[] ndefMsgs;
			
			// getting raw data in Parcelable array
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			
			/*Converting the raw data in Parcelable array to NDEF format 
			  and storing in NdefMessage array*/
			if (rawMsgs != null) 
			{
				ndefMsgs = new NdefMessage[rawMsgs.length];
				if(ndefMsgs.length ==1)
				{
					ndefMsgs[0] = (NdefMessage) rawMsgs[0];
					NdefRecord[] ndr = ndefMsgs[0].getRecords();
					
					StringBuilder key=null;
					for (int j=0;j<ndr.length;j++) 
					{	
						//get the id of name value pair
						byte[] id = ndr[j].getId();
						key = new StringBuilder();
						key.append(new String (id));
						if(keyToSearch.equalsIgnoreCase(key.toString()))
						{
							//return value associated with this id. Convert bytes to String first
							String payload = new String(ndr[j].getPayload());
							return payload;
						}					
					}
					return null;
				}
			}	
			return null;
		}
		catch (Exception e)
		{
			Log.e(TAG_NFC_READER, e.getMessage());
			e.printStackTrace();
			return null;
		}
 	}
	
	public static String readTag(Intent intent)
	{
		// tag.getId(); //get the universally unique id of the tag
		try
		{			
			NdefMessage[] ndefMsgs;
			
			// getting raw data in Parcelable array
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			
			/*Converting the raw data in Parcelable array to NDEF format 
			  and storing in NdefMessage array*/
			if (rawMsgs != null) 
			{
				ndefMsgs = new NdefMessage[rawMsgs.length];
				if(ndefMsgs.length ==1)
				{
					ndefMsgs[0] = (NdefMessage) rawMsgs[0];
					NdefRecord[] ndr = ndefMsgs[0].getRecords();
					
					StringBuilder key=null;
					String payload =null;
					for (int j=0;j<ndr.length;j++) 
					{	
						//return value associated with this id. Convert bytes to String first
						payload = new String(ndr[j].getPayload());
					}
					return payload;
				}
			}	
			return null;
		}
		catch (Exception e)
		{
			Log.e(TAG_NFC_READER, e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
