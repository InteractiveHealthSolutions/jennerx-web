package ord.ird.android.epi.db;

import java.util.ArrayList;
import java.util.List;

import org.ird.android.epi.model.Centre;
import org.ird.android.epi.model.LocationType;

import android.content.ContentValues;
import android.content.Context;

public class CentreDBHelper
{
	private Context context=null;	
	private DatabaseUtil dbUtil=null;
			
	private static final String TABLE_CENTRE="centre";
	private static final String FIELD_CENRE_ID="centreId";
	private static final String FIELD_CENTRE_NAME="name";
	
	public CentreDBHelper(Context cxt)
	{
		this.context = cxt;
		if(context!=null && dbUtil == null)
		{
			dbUtil = new DatabaseUtil(context);
		}
	}
	
	public boolean saveCentres(List<Centre> centres)
	{
		boolean isSaved=false;
		ContentValues values;
		
		boolean[] flags=new boolean[centres.size()];
		int flagCounter=0;
		
		for(Centre centre : centres)
		{
			values = new ContentValues();
			values.put(FIELD_CENRE_ID, centre.getCentreId());
			values.put(FIELD_CENTRE_NAME, centre.getName());
			
			flags[flagCounter] = dbUtil.insert(TABLE_CENTRE, values);
			flagCounter++;
		}
		
		isSaved = checkAllSaved(flags);
		return isSaved;
	}
	
	private boolean checkAllSaved(boolean[] flags)
	{
		for(boolean b : flags)
		{
			if(!b)
				return false;
		}
		return true;
	}
	
	public List<Centre> getCentres()
	{
		DatabaseUtil dbUtil = new DatabaseUtil(context);
		List<Centre> centres = new ArrayList<Centre>();
		Centre temp;
		String[][] values = dbUtil.getTableData("select * from " + TABLE_CENTRE);
		
		for(int i=0; i <values.length;i ++)
		{
			temp=new Centre();
			temp.setCentreId(Integer.valueOf(values[i][0]));
			temp.setName(values[i][1]);
			
			centres.add(temp);
		}
		
		return centres;
	}
}
