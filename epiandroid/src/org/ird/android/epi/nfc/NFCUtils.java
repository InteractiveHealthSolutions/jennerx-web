package org.ird.android.epi.nfc;

import java.io.IOException;

import org.ird.android.epi.nfc.NFCWriter.EPINFCException;

import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

public class NFCUtils
{
	private static final String TAG_NFC="NFCUtils";
	
	public boolean readLotteryTag(Intent intent)
	{		
		String result = NFCReader.readTag(intent, EPINfcTags.LOTTERY.toString());
		
		if(result!=null)
			return true;
		else
			return false;
	}	
	
	public static boolean writeChildID(Tag tag, String childId)
	{
		//Data is sent in a multidimensional String Array of ""<id1>","<payload1>","<id2>","<payload2>"..." format
		String[][] record = new String[][]{{EPINfcTags.CHILD.toString(),childId}}; 
		
		try
		{
			NFCWriter.write(tag, record);
			return true;
		}
		catch (Exception ex)
		{
			return handleException(ex);			
		}
	}
	
	private static boolean handleException(Exception ex)
	{
		ex.printStackTrace();		
		Log.e(TAG_NFC, ex.getMessage());
		return false;
	}
	
}
