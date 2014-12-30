package org.ird.android.epi.alert;

import java.util.Map;

import org.ird.android.epi.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;


public class ServerResponseAlert extends DialogFragment{

	//default values
	private String messageToShow=getString(R.string.application_emptyString);
	private String positiveButtonText= getString(R.string.application_ok);
	private String negativeButtonText=getString(R.string.application_cancel);
	
	private IDialogListener reciever=null;
	

	public void setNegativeButtonText(String negativeButtonText) {
		this.negativeButtonText = negativeButtonText;
	}	

	public void setMessageToShow(String msg){
		this.messageToShow=msg;
	}
	public String getPositiveButtonText() {
		return positiveButtonText;
	}

	public void setPositiveButtonText(String positiveButtonText) {
		this.positiveButtonText = positiveButtonText;
	}

	public String getNegativeButtonText() {
		return negativeButtonText;
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(this.messageToShow)        	   
               .setPositiveButton(this.positiveButtonText, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
//                	   if(reciever != null)
//                	   {
//                		   reciever.onDialogPositiveClick((DialogFragment)ServerResponseAlert.this);
//                	   }
                   }
               })
               .setNegativeButton(this.negativeButtonText, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   if(reciever != null)
                	   {
                		   reciever.onDialogNegativeClick();
                	   }
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try
		{
			reciever = (IDialogListener)activity;
		}
		catch(Exception ex)
		{
			throw new ClassCastException("Could not find an implementation of IDialogListener in calling activity:" );
		}
	}
}
