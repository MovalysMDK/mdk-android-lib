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
import android.view.LayoutInflater;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;

/**
 * This view group represents a Position with latitude and longitude Text views, its label, and a map button that open the map application installed
 * on the device
 * <ul>
 * 	<li>add xmlns:movalys="http://www.adeuza.com/movalys/mm/android" on the main tag</li> 
 * 	<li>include a com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPositionTextView</li> 
 * 	<li>add label attribute to declare the @string/resource Id to use as label for the group</li> 
 * 	<li>add labelLatitude attribute to declare the @string/resource Id to use as label for the latitude field label</li>
 * 	<li>add labelLongitude attribute to declare the @string/resource Id to use as label for the longitude field label</li>
 * </ul>
 */
public class MMPositionTextView extends AbstractPositionMasterView {

	/**
	 * 
	 * Construct a new MMPositionEditText component
	 * 
	 * @param p_oContext the current context
	 */
	public MMPositionTextView(Context p_oContext) {
		super(p_oContext);
	}

	/**
	 * 
	 * Construct a new MMPositionTextView component
	 * 
	 * @param p_oContext the current context
	 * @param p_oAttrs xml attributes
	 */
	public MMPositionTextView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			inflateLayout();
		}
	}

	/**
	 * inflates the component's view
	 */
	private void inflateLayout() {
		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
		LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
		oLayoutInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_position_textview), this);
	}
}
