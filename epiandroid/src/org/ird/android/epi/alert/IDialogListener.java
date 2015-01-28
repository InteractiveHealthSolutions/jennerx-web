package org.ird.android.epi.alert;

import java.util.Map;

public interface IDialogListener {

	 public void onDialogPositiveClick(Map...o);
     public void onDialogNegativeClick(Map...o);
     public void onDialogNeutralClick(Map...o);
     
}
