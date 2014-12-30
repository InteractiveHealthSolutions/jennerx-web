package ord.ird.android.epi.db;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class DatbaseAsyncTask extends AsyncTask
{

	private ProgressDialog 	progressDialog;
	private final Context 		context ;
	
	public DatbaseAsyncTask (Context cxt)
	{
		this.context=cxt;
	}
	
    @Override
    protected Object doInBackground(final Object... objects)
    {
    
    	//TODO: Open and create database here
        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        new Runnable()
		{
			public void run()
			{ progressDialog = ProgressDialog.show(
		                    DatbaseAsyncTask.this.context, "Saving",
		                    "Opening/Upgrading the database, please wait", true);
		    }
		        
		};		
    }

    @Override
    protected void onPostExecute(Object object)
    {
        super.onPostExecute(object);
        progressDialog.dismiss();
    }

}
