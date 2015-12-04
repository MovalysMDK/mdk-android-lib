/**
 * Copyright (C) 2010 Sopra Steria Group (movalys.support@soprasteria.com)
 *
 * This file is part of Movalys MDK.
 * Movalys MDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Movalys MDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Movalys MDK. If not, see <http://www.gnu.org/licenses/>.
 */
package com.adeuza.movalysfwk.mobile.mf4android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;

/**
 * Class use to create a panel
 *
 */
public abstract class AbstractInflateMMFragment extends AbstractMMFragment {

	/** identifier */
	protected static final String IDENTIFIER_CACHE_KEY = "id";
	
	/** the layout */
	protected ViewGroup layout;
	
	/** get the id of the layout from ressources */
	public abstract int getLayoutId();
	
	protected boolean isRotated = false;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater p_oInflater, ViewGroup p_oContainer, Bundle p_oSavedInstanceState) {
		Application.getInstance().addActiveDisplayList(this);
		this.layout = (ViewGroup) p_oInflater.inflate(getLayoutId(), p_oContainer, false);
		
		doAfterInflate(this.layout);
		
		return this.layout;
	}
	
	@Override
	public void onActivityCreated(Bundle p_oSavedInstanceState) {
		super.onActivityCreated(p_oSavedInstanceState);
		
		this.isRotated = (p_oSavedInstanceState != null && p_oSavedInstanceState.getBoolean("ROTATED"));
		
		if (!this.isRotated) {
			doFillAction();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		isRotated = false;
	}
	
	@Override
	public void onSaveInstanceState(Bundle p_oOutState) {
		p_oOutState.putBoolean("ROTATED", true);
	}
	
	/**
	 * Method to call to populate the panel (this method needs to be overridden to do a action)
	 */
	public abstract void doFillAction();
	
	/**
	 * Call after inflating the view in this.layout
	 * Override this
	 * @param p_oRoot  view group
	 */
	protected void doAfterInflate(ViewGroup p_oRoot) {
		// nothing by default
	}
}
