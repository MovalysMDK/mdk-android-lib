/**
 * 
 */
package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.os.Parcelable;

/**
 * @author lbrunelliere
 *
 */
public interface InstanceStatable {

	public void superOnRestoreInstanceState(Parcelable p_oState);
	
	public Parcelable superOnSaveInstanceState();
	
}
