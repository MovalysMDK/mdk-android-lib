package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

import android.view.View;
import android.view.ViewGroup;

/**
 * Delegate for the list adapters
 */
public interface ListAdapterDelegate {

	/**
	 * returns the view in the given position for the given list level
	 * @param p_oLevel the {@link AdapterLevel} to work on
	 * @param p_bExpanded true if the element is expanded
	 * @param p_oView the view to compute
	 * @param p_oViewGroup the groupview which the view is related to 
	 * @param p_oPositions the position(s) in the list being worked on
	 * @return the view in the given position for the given list level
	 */
	public View getViewByLevel(AdapterLevel p_oLevel, boolean p_bExpanded, View p_oView, ViewGroup p_oViewGroup, int ... p_oPositions);
	
}
