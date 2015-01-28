package org.ird.android.epi;

import java.util.Map;

import org.ird.android.epi.alert.IDialogListener;
import org.ird.android.epi.common.EpiUtils;
import org.ird.android.epi.nfc.EPINfcTags;
import org.ird.android.epi.nfc.NFCReader;
import org.ird.android.epi.nfc.NFCWriter;
import org.ird.android.epi.validation.ValidatorResult;
import org.ird.android.epi.validation.ValidatorUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class NfcWriterActivity extends Activity implements IDialogListener
{
	//For NFC
	NfcAdapter adapter;
	PendingIntent pendingIntent;
	IntentFilter tagFilters[];
	Tag nfcTag;
	private boolean isWritten = false;	
	private static final String LOGCAT_TAG_NFC_WRITER="NFCWriter";
	private static String CHILD_ID;
	public static final String IS_WRITTEN ="IsWritten";
	String[][] techListsArray;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle b =getIntent().getExtras();
		if(b!=null)
		{
			CHILD_ID = b.getString(EPINfcTags.CHILD.getTagIdentifier());		
		}
		setContentView(R.layout.nfcwriter_layout);
		
		adapter = NfcAdapter.getDefaultAdapter(this);
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent (this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter ndef = new IntentFilter (NfcAdapter.ACTION_NDEF_DISCOVERED);
		ndef.addCategory(Intent.CATEGORY_DEFAULT);
		try
		{
			ndef.addDataType("*/*");
		}
		catch (MalformedMimeTypeException e1)
		{
			e1.printStackTrace();
		}
		
		String[][] techListsArray = new String[][] { new String[] { Ndef.class.getName() } };
		ndef.addCategory(Intent.ACTION_DEFAULT);
		tagFilters = new IntentFilter[]{ndef};		
		EditText txtID= (EditText)findViewById(R.id.edtTxtNFCData);
		if(txtID !=  null)
		{
			txtID.setText(CHILD_ID);
		}			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nfc_writer, menu);
		return true;
	}

	public void onClick(View control)
	{
		write();
	}
	
	private void write()
	{
		if(nfcTag==null)
		{
			EpiUtils.showAlert(this, "No tag in contact", "", this).show();
			return;
		}	
		
		ValidatorUtil validator = new ValidatorUtil(this);
		ValidatorResult result;
		result = validator.validateChildId(CHILD_ID);
		
		if(!result.isValid())
		{
			EpiUtils.showAlert(this, "Incorrect Data to write to tag:" + result.getMessage(), "Error", this).show();
			return;
		}
			
		String[][] payload = {{EPINfcTags.CHILD.getTagIdentifier(),CHILD_ID}};
		try
		{
			NFCWriter.write(nfcTag, payload);
			isWritten=true;
			AlertDialog.Builder builder =new AlertDialog.Builder(this);
			builder.setMessage("Written to tag successfully");
			builder.setTitle("Success");
			builder.setPositiveButton("Ok", new AlertDialog.OnClickListener()
			{	@Override
				public void onClick(DialogInterface dialog, int which)
				{					
					dialog.dismiss();
					finish();
				}
			});
			builder.create().show();
		}
		catch (Exception e)
		{
			handleNFCException(e);
		}	
	}
	
	private void handleNFCException(Exception ex)
	{
		ex.printStackTrace();
		EpiUtils.showAlert(this, getResources().getString(R.string.nfc_write_error), "Error", this).show();
		Log.e(LOGCAT_TAG_NFC_WRITER, "error occurred while NFC operation:\n" + ex.getMessage());
	}
	
	@Override
	public void onNewIntent(Intent intent)
	{
		if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()))
		{	
			read(null,intent);
		}
	}
	
	public void read(View control, Intent intent)
	{
		try
		{
			nfcTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			if(nfcTag!=null)
			{
				String valueWritten =  NFCReader.readTag(intent, EPINfcTags.CHILD.getTagIdentifier());
				Toast.makeText(this,getResources().getString(R.string.alert_tag_in_contact) + valueWritten, Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(this, "No tag in contact", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		catch (Exception e)
		{
			handleNFCException(e);
		}
	}
	
	@Override
	public void onPause(){
		super.onPause();
		adapter.disableForegroundDispatch(this);
	}
	
	@Override 
	public void finish()
	{
		Intent intent = new Intent();
		intent.putExtra(IS_WRITTEN, isWritten);
		setResult(RESULT_OK, intent);
		super.finish();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		adapter.enableForegroundDispatch(this, pendingIntent, tagFilters, null);
	}

	@Override
	public void onDialogPositiveClick(Map... o)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogNegativeClick(Map... o)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogNeutralClick(Map... o)
	{
		// TODO Auto-generated method stub
		
	}
}
