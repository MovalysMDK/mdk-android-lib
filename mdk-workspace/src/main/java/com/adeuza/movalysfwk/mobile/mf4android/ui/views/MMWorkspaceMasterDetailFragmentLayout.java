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
package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentGetSetAndFilled;
import com.adeuza.movalysfwk.mobile.mf4android.ui.wrappers.MMWorkspaceMasterDetailAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.VMTabConfiguration;

/**
 * Workspace component that handled master/detail flow
 */
public class MMWorkspaceMasterDetailFragmentLayout extends AbstractBaseWorkspaceFragmentLayout<VMTabConfiguration> 
	implements ComponentGetSetAndFilled<VMTabConfiguration> {

	/**
	 * Constructs a new MMLinearLayout
	 * Call super with Class = VMTabConfiguration.class
	 * @param p_oContext the context to use
	 * @see AbstractBaseWorkspaceFragmentLayout#MMBaseWorkspaceFragmentLayout(Context, Class)
	 */
	public MMWorkspaceMasterDetailFragmentLayout(Context p_oContext) {
		super(p_oContext, VMTabConfiguration.class);
	}
	
	/**
	 * Constructs a new MMLinearLayout.
	 * Call super with Class = VMTabConfiguration.class
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see AbstractBaseWorkspaceFragmentLayout#MMBaseWorkspaceFragmentLayout(Context, AttributeSet, Class)
	 */
	public MMWorkspaceMasterDetailFragmentLayout(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, VMTabConfiguration.class );
	}

	/**
	 * Unhide all detail columns
	 * @param p_bNeedRecompute true if recompute is needed
	 */
	public void unHideDetailColumns(boolean p_bNeedRecompute) {
		this.unHideAllColumns(p_bNeedRecompute);
	}

	/*************************************************************************************
	 **************************** Framework delegate callback ****************************
	 *************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentGetSetAndFilled#configurationGetValue()
	 */
	@Override
	public VMTabConfiguration configurationGetValue() {
		return null;
	}
	/**
	 * {@inheritDoc}
	 * @see ComponentGetSetAndFilled#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(VMTabConfiguration p_oObjectToSet) {
		try {
			((MMWorkspaceMasterDetailAdapter) this.getAdapter()).setTabConfiguration((VMTabConfiguration) p_oObjectToSet);
		} catch (ClassCastException e) {
			Log.v(MMWorkspaceMasterDetailFragmentLayout.class.getSimpleName(), "[configurationSetValue] " + e);
		}
	}
	/**
	 * {@inheritDoc}
	 * @see ComponentGetSetAndFilled#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return false;
	}

}
