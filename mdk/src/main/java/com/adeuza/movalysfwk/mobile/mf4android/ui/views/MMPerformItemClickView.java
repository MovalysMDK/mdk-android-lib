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

import android.view.View;

/**
 * Used on list with clickable items<br/>
 * Describes the method used to click an item of the list
 */
public interface MMPerformItemClickView {

	/**
	 * Performs a click on list item
	 * @param p_oView the view that was clicked
	 * @param p_iPosition the position of the view in the list
	 * @param p_iId the row id of the clicked view
	 * @return true if a click was performed
	 */
	public boolean superPerformItemClick(View p_oView, int p_iPosition, long p_iId);
}
