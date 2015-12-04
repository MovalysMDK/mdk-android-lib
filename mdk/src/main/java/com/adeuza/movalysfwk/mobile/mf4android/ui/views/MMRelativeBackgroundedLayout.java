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
import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMRelativeLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableEnumComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;

/**
 * Custom RelativeLayout with a background binded to an enumeration
 */
public class MMRelativeBackgroundedLayout  extends AbstractMMRelativeLayout<Enum<?>> 
	implements ComponentReadableWrapper<Enum<?>> {
	
	/** Tag to use for debugging */
	public static final String DEBUG_TAG = "MMRelativeBackgroundedLayout";
	
	private int backgroundResource = -1;
	
	/**
	 * Constructs a new MMRelativeBackgroundedLayout
	 * @param p_oContext The context to use
	 * @param p_oAttrs The attributes to use
	 */
	public MMRelativeBackgroundedLayout(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, Enum.class);
	}
	
	/**********************************************************************************************
	 ******************************* Framework delegate callback **********************************
	 **********************************************************************************************/
	@Override
	public void configurationSetValue(Enum<?> p_oObjectToSet) {
		int iImageId = ((AndroidConfigurableEnumComponentDelegate) this.aivDelegate).getRessourceId(ApplicationRGroup.DRAWABLE);
		if ( iImageId != AndroidConfigurableEnumComponentDelegate.NO_RESSOURCE ) {
			if (this.getVisibility()!= View.VISIBLE){
				this.setVisibility(View.VISIBLE);
			}
			this.backgroundResource = iImageId;
			this.setBackgroundResource(iImageId);
		} else {
			Log.d(MMRelativeBackgroundedLayout.DEBUG_TAG, "Background Image not found " + ((AndroidConfigurableEnumComponentDelegate) this.aivDelegate).getEnumRessourceValue() );
		}
	}
	
	/**
	 * returns the id of the background resource set on the component
	 * @return the id of the background resource set on the component
	 */
	public int getBackgroundResource() {
		return this.backgroundResource;
	}
}
