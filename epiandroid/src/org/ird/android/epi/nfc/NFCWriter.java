package org.ird.android.epi.nfc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;

public class NFCWriter
{
	private static NdefRecord createRecord(String id, String text) throws UnsupportedEncodingException 
	{
		byte[] idBytes = id.getBytes();
		byte[] payloadBytes = text.getBytes();
		
		NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, idBytes, payloadBytes);
		return recordNFC;
	}
	
	public static void write( Tag tag, String[][] text) throws Exception
	{
		NdefRecord[] records = new NdefRecord[text.length];
		
		for(int i =0; i < text.length; i ++)
		{
			if(text[i].length!=2)
				throw new EPINFCException("Invalid String message format ");
			
			records[i]= createRecord(text[i][0], text[i][1]);
		}		 
		
		NdefMessage  message = new NdefMessage(records);
		try 
		{
			NdefFormatable ndef = NdefFormatable.get(tag);
			ndef.connect();
			ndef.format(message);
			ndef.close();
		} 
		catch (Exception e) 
		{
			try 
			{
				Ndef ndef = Ndef.get(tag);
				ndef.connect();
				ndef.writeNdefMessage(message);
				ndef.close();		
			} 
			catch (TagLostException tle) 
			{
				throw tle;
			} 
			catch (Exception ee)
			{
				throw ee;
			}
		}
	}
	
	public static class EPINFCException extends Exception 
	{
		public EPINFCException (String message)
		{
			super(message);			
		}
	}	

}


