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
package com.adeuza.movalysfwk.mobile.mf4android.context;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericsave.ChainSaveDetailAction.AddEntityEvent;
import com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericsave.ChainSaveDetailAction.ModifyEntityEvent;
import com.adeuza.movalysfwk.mobile.mf4android.application.MFAndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4javacommons.context.MExtendedContextImpl;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.CUDEvent;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.CUDType;

/**
 * <p>
 * 	Implementation of the Movalys Android context.
 * 	This owner's version has an instance of an Android context.
 * 	This object is an MMContext and extends the AbstractMMContext class. 
 * </p>
 *
 *
 * @since Barcelone (28 sept. 2010)
 */
public class AndroidContextImpl extends MExtendedContextImpl implements AndroidMMContext {

	/**
	 * The Android context
	 */
	private Context context;
	
	/** 
	 * Construct an object <em>AndroidContextImpl</em>.
	 * @param p_oContext The context that provides database access. This parameter can not be null.
	 */
	protected AndroidContextImpl(Context p_oContext) {
		super();
		this.context = p_oContext;
	}

	/**
	 * Returns the shared preferences of the main screen.
	 * 
	 * @return
	 * 		The shared preferences of the main screen. Never null.
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.android.application.AndroidMMContext#getSharedPreferences()
	 */
	@Override
	public SharedPreferences getSharedPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(this.context);
	}

	/**
	 * <p>Return the object <em>context</em></p>
	 * @return Objet context
	 */
	@Override
	public Context getAndroidNativeContext() {
		return this.context;
	}
	
	/**
	 * GETTER
	 * @return android application
	 */
	public MFAndroidApplication getAndroidApplication() {
		return (MFAndroidApplication) this.context.getApplicationContext();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addBusinessEventFromFirstEvent(boolean p_bExitMode) {
		if ( !this.getEvents().isEmpty()) {
			CUDEvent<?> oEvent = (CUDEvent<?>) this.getEvents().get(0);
			if ( CUDEvent.class.isAssignableFrom(oEvent.getClass())) {
				if (CUDType.CREATE.compareTo(oEvent.getType()) == 0) {
					AddEntityEvent oAddEntityEvent = new AddEntityEvent(oEvent.getSource(), oEvent.getData());
					oAddEntityEvent.setExitMode(p_bExitMode);
					this.registerEvent(oAddEntityEvent);
				}
				else if ( CUDType.UPDATE.compareTo(oEvent.getType()) == 0) {
					ModifyEntityEvent oModifyEntityEvent = new ModifyEntityEvent(oEvent.getSource(), oEvent.getData());
					oModifyEntityEvent.setExitMode(p_bExitMode);
					this.registerEvent(oModifyEntityEvent);
				}
			}
		}
		
	}

}
