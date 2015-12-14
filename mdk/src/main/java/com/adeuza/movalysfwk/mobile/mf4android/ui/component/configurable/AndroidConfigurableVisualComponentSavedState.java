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
package com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View.BaseSavedState;


/**
 * This class allow to save the state of MM components of the framework.
 * It is used in the terminal's rotation to save the state of each component 
 *
 */
public class AndroidConfigurableVisualComponentSavedState extends BaseSavedState {
	/** component changed */
	private boolean changed;
	/** component visibility */
	private int visibility;
	/** component is enable */
	private boolean enabled;
	/** component has linked rules */
	private boolean hasRules;
	
	/**
	 * Create instance of AndroidConfigurableVisualComponentSavedState
	 * @param p_oSuperState the parent {@link Parcelable} to build on 
	 */
	public AndroidConfigurableVisualComponentSavedState(Parcelable p_oSuperState) {
		super(p_oSuperState);
	}
	/**
	 * Create instance of AndroidConfigurableVisualComponentSavedState
	 * @param p_oInParcel the {@link Parcel} to build in
	 */
	public AndroidConfigurableVisualComponentSavedState(Parcel p_oInParcel) {
		super(p_oInParcel);
		this.changed = p_oInParcel.readInt() == 1;
		this.visibility = p_oInParcel.readInt();
		this.enabled = p_oInParcel.readInt() == 1;
		this.hasRules = p_oInParcel.readInt() == 1;
	}
	/**
	 * Is the component have changed
	 * @return true if the component have changed, false otherwise
	 */
	public boolean isChanged() {
		return changed;
	}
	/**
	 * Set if the component have changed
	 * @param p_bChanged true if the component have change, false otherwise
	 */
	public void setChanged(boolean p_bChanged) {
		this.changed = p_bChanged;
	}
	/**
	 * Get the component visibility
	 * @return the component visibility (take android values : {@link android.view.View#VISIBLE}, {@link android.view.View#INVISIBLE} or {@link android.view.View#GONE}
	 */
	public int getVisibility() {
		return this.visibility;
	}
	/**
	 * Set the component visibility
	 * @param p_iVisibility the component visibility (take android values : {@link android.view.View#VISIBLE}, {@link android.view.View#INVISIBLE} or {@link android.view.View#GONE}
	 */
	public void setVisibility(int p_iVisibility) {
		this.visibility = p_iVisibility;
	}
	/**
	 * Set if the component is enable
	 * @param p_bEnabled true if the component is enable, false otherwise
	 */
	public void setEnabled(boolean p_bEnabled) {
		this.enabled = p_bEnabled;
		Log.d("ACVCSS.setEnabled - " + p_bEnabled, Log.getStackTraceString(new Exception()));
	}
	/**
	 * Is the component enable
	 * @return true if the component is enable, false otherwise
	 */
	public boolean isEnabled() {
		return enabled;
	}
	/**
	 * Set if the component has binded rules on a view model
	 * @param p_bHasRule true if the component at least one rule, false otherwise
	 */
	public void setHasRules(boolean p_bHasRule) {
		this.hasRules = p_bHasRule;
	}
	/**
	 * Get if the component has binded rules on a view model
	 * @return true if the component at least one rule, false otherwise
	 */
	public boolean getHasRules() {
		return this.hasRules;
	}
	
	@Override
	public void writeToParcel(Parcel p_oOutParcel, int p_iFlags) {
		if(p_oOutParcel != null) {
			super.writeToParcel(p_oOutParcel, p_iFlags);

			int i = 0 ;
			if (this.changed ){
				i = 1 ;
			}
			p_oOutParcel.writeInt(i);
			p_oOutParcel.writeInt(this.visibility);
			p_oOutParcel.writeInt(this.enabled ? 1 : 0);
			p_oOutParcel.writeInt(this.hasRules ? 1 : 0);
		}
	} 
	/**
	 * Save parcelable CREATOR
	 */
	public static final Parcelable.Creator<AndroidConfigurableVisualComponentSavedState> CREATOR
	= new Parcelable.Creator<AndroidConfigurableVisualComponentSavedState>() {
		@Override
		public AndroidConfigurableVisualComponentSavedState createFromParcel(Parcel p_oInParcel) {
			return new AndroidConfigurableVisualComponentSavedState(p_oInParcel);
		}

		@Override
		public AndroidConfigurableVisualComponentSavedState[] newArray(int p_iSize) {
			return new AndroidConfigurableVisualComponentSavedState[p_iSize];
		}
	};
}
