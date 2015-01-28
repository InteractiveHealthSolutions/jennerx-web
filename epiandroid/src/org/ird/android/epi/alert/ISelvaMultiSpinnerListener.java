package org.ird.android.epi.alert;

public interface ISelvaMultiSpinnerListener
{
	public void onItemschecked(boolean[] checked);
    public void itemsSelected(String[] selectedItems, SelvaMultiSpinner spinner);
}
