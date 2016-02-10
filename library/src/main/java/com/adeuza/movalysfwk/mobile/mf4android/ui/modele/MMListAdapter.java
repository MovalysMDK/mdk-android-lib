package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

public interface MMListAdapter {

	public void setSelectedItem(String p_sItemId);

	public void resetSelectedItem();
	
	public void notifyDataSetChanged ();
}
