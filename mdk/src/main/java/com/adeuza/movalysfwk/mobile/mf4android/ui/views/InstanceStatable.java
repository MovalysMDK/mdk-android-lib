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
/**
 * 
 */
package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.os.Parcelable;

/**
 * Used to save and restore instance state on the view.<br/>
 * This is usually called when rotating the screen.
 *
 */
public interface InstanceStatable {

	/**
	 * Restore the instance state on the view
	 * @param p_oState state of the view
	 */
	public void superOnRestoreInstanceState(Parcelable p_oState);
	
	/**
	 * Save the instance state on the view
	 * @return the instance state to save
	 */
	public Parcelable superOnSaveInstanceState();
	
}
