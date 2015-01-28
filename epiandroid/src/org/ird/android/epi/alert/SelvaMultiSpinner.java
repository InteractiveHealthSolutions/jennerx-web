/**
 * This class was originally developed by the author "Selva". See this link:
 * http://stackoverflow.com/a/12312077/2931462
 */
package org.ird.android.epi.alert;

import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AbsListView.MultiChoiceModeListener;

public class SelvaMultiSpinner extends Spinner implements ISelvaMultiSpinnerListener, OnMultiChoiceClickListener, OnCancelListener 
{
	protected List<String> listitems;
    protected boolean[] checked;    
    ISelvaMultiSpinnerListener listener;
    
    private final static String DEFAULT_CAPTION="Select Option(s)";
    
    public SelvaMultiSpinner(Context context) 
    {
        super(context);
    }

    public SelvaMultiSpinner(Context arg0, AttributeSet arg1)
    {
        super(arg0, arg1);
    }

    public SelvaMultiSpinner(Context arg0, AttributeSet arg1, int arg2) 
    {
        super(arg0, arg1, arg2);
    }

    @Override
    public void onClick(DialogInterface dialog, int ans, boolean isChecked)
    {
        if (isChecked)
            checked[ans] = true;
        else
            checked[ans] = false;
    }


  @Override
    public void onCancel(DialogInterface dialog)
    {
     
      if(this.listener!=null)
      {
    	  this.listener.itemsSelected(getSelectedItems(),this);
      }
      else
      {
    	  this.itemsSelected(getSelectedItems(),this);
      }
    }
  
  	public String[] getSelectedItems()
  	{
  		List<String>selectedItems = new ArrayList<String>();
        for (int i = 0; i < listitems.size(); i++)
        {
            if (checked[i] == true)
            {
          	  selectedItems.add(listitems.get(i));        	 
            }
        }        
        String[] items = selectedItems.toArray(new String[selectedItems.size()]);
        return items;
  	}

    @Override
    public boolean performClick()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(
                  listitems.toArray(new CharSequence[listitems.size()]), checked, this);
        builder.setPositiveButton("Done",
                new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        builder.setOnCancelListener(this);
        builder.show();
        return true;
    }

    public void setItems(List<String> items, String allText, ISelvaMultiSpinnerListener listener)
    {
    	this.listener = listener;
        this.listitems = items;
        
        if(checked == null)	
        	checked = new boolean[items.size()];

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[] { allText });
        setAdapter(adapter);
    }
    
    private void addItems(SelvaMultiSpinner sp, String label)
	{
		sp.setItems(listitems, label, this);
	}
    
	@Override
	public void onItemschecked(boolean[] checked)
	{		
	}
	
	@Override
	public void itemsSelected(String[] selectedItems, SelvaMultiSpinner spinner)
	{
		String str="";
		if(selectedItems.length==0)
		{
			addItems(this, DEFAULT_CAPTION);
			return;
		}
		
 		for(int i =0; i<selectedItems.length;i++)
 		{
 			if(i == (selectedItems.length -1))
 					str = str + selectedItems[i] ;
 			else
 				str = str + selectedItems[i] + ", " ;
 		}
 		addItems(this, str);
	}
}
