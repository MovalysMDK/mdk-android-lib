package com.adeuza.movalysfwk.mobile.mf4android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Class use to create a panel
 * @author abelliard
 *
 */
public abstract class AbstractInflateMMFragment extends AbstractMMFragment {

	/** the layout */
	protected ViewGroup layout;
	
	/** get the id of the layout from ressources */
	public abstract int getLayoutId();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater p_oInflater, ViewGroup p_oContainer, Bundle p_oSavedInstanceState) {
		
		this.layout = (ViewGroup) p_oInflater.inflate(getLayoutId(), p_oContainer, false);
		
		doAfterInflate(this.layout);
		
		return this.layout;
	}

	/**
	 * Call after inflating the view in this.layout
	 * Override this
	 * @param p_oRoot  view group
	 */
	protected void doAfterInflate(ViewGroup p_oRoot) {
		// nothing by default
	}
	

	
}
