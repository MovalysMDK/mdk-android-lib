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
package com.adeuza.movalysfwk.mobile.mf4android.ui.component.wrappers;

import android.os.Parcelable;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.ComponentWrapper;

/**
 * Defines the mandatory methods for an android component wrapper
 */
public interface AndroidComponentWrapper extends ConfigurableVisualComponent, ComponentWrapper {

	/**
	 * called when the screen rotates or the activity goes to pause
	 * @return the data to save
	 */
	public Parcelable onSaveInstanceState();
	
	/**
	 * called when the activity is restored
	 * @param p_oState the data to restore
	 */
	public void onRestoreInstanceState(Parcelable p_oState);
	
}
