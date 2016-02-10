package com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View.BaseSavedState;


/**
 * This class allow to save the state of MM components of the framework.
 * It is used in the terminal's rotation to save the state of each component 
 * @author dmaurange
 *
 */
public class AndroidConfigurableVisualComponentSavedState extends BaseSavedState {
	private boolean changed;
	private int visibility;
	private boolean enabled;
	private boolean hasRules;
	
	public AndroidConfigurableVisualComponentSavedState(Parcelable p_oSuperState) {
		super(p_oSuperState);
	}

	public AndroidConfigurableVisualComponentSavedState(Parcel p_oInParcel) {
		super(p_oInParcel);
		this.changed = p_oInParcel.readInt() == 1;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean p_bChanged) {
		this.changed = p_bChanged;
	}
	
	public int getVisibility() {
		return this.visibility;
	}
	
	public void setVisibility(int p_iVisibility) {
		this.visibility = p_iVisibility;
	}
	
	public void setEnabled(boolean p_bEnabled) {
		this.enabled = p_bEnabled;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setHasRules(boolean p_bHasRule) {
		this.hasRules = p_bHasRule;
	}
	
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
		}
	} 

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