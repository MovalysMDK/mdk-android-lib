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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;

/**
 * <p>
 * 	LinearLayout widget used only for planning and intervention layouting in the Movalys Mobile product for Android
 * </p>
 * 
 * 
 */
public class MMWorkspaceDetailFragmentLayout extends AbstractBaseWorkspaceFragmentLayout<NoneType> {
	
	/**
	 * Constructs a new MMLinearLayout
	 * 
	 * @param p_oContext the context to use
	 * @see AbstractBaseWorkspaceFragmentLayout#MMBaseWorkspaceFragmentLayout(Context)
	 */
	public MMWorkspaceDetailFragmentLayout(Context p_oContext) {
		super(p_oContext, NoneType.class);
	}
	
	/**
	 * Constructs a new MMLinearLayout.
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see AbstractBaseWorkspaceFragmentLayout#MMBaseWorkspaceFragmentLayout(Context)
	 */
	public MMWorkspaceDetailFragmentLayout(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, NoneType.class );
	}
	
	/**
	 * Unhide all detail columns
	 * @param p_bNeedRecompute unused param
	 */
	public void unHideDetailColumns(boolean p_bNeedRecompute) {
		// noting to do when hide columns
	}
}
