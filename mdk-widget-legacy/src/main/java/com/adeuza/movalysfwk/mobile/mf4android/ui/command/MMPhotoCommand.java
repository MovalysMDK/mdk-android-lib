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
package com.adeuza.movalysfwk.mobile.mf4android.ui.command;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMSlidingDrawer;

/**
 * This photo command is an Activity that can :
 * <ul>
 * <li>Take a photo using an intent to a camera activity</li>
 * <li>Choose an existing image using galery or other activities that can handle
 * an ACTION_GET for images/* data</li>
 * <li>View and Update image metadata</li>
 * </ul>
 */
public class MMPhotoCommand extends PhotoCommand {

	private MMSlidingDrawer uiPhotoMetaSlidingDrawer;

	/**
	 * Retreive SlidingDrawer
	 */
	@Override
	public void setContentView(int p_iLayoutResID) {
		super.setContentView(p_iLayoutResID);
		this.uiPhotoMetaSlidingDrawer = (MMSlidingDrawer)this.findViewById(AndroidApplication.getInstance().getAndroidIdByRKey(AndroidApplicationR.component_photo__SlidingDrawer__group));
	}

	/**
	 * Update UI components
	 */
	@Override
	protected void updateUi() {
		super.updateUi();
		this.uiPhotoMetaSlidingDrawer.setEdit(!this.photoMetadata.isReadOnly());
	}

	/**
	 * {@inheritDoc}
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if ( this.uiPhotoMetaSlidingDrawer!=null && this.uiPhotoMetaSlidingDrawer.isOpened()){
			this.uiPhotoMetaSlidingDrawer.animateClose();
		}
		else{
			super.onBackPressed();
		}
	}
}
