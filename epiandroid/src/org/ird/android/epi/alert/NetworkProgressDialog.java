package org.ird.android.epi.alert;

import android.app.ProgressDialog;
import android.content.Context;
 
public class NetworkProgressDialog extends ProgressDialog implements Runnable{
 
        public NetworkProgressDialog(Context context) {
                super(context);
                setCanceledOnTouchOutside(false);
                setCancelable(true);                
                setMessage("Please wait...");
                setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

		@Override
		public void run()
		{
			this.show();
		}
 
}
