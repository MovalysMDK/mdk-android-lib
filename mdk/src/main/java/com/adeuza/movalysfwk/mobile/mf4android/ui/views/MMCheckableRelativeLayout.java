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

import android.R;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;

/**
 * <p>MMCheckableRelativeLayout</p>
 */
public class MMCheckableRelativeLayout extends RelativeLayout implements Checkable {

	/** true if the android version running the application is pre-honeycomb */
	public static final boolean PRE_HONEYCOMB = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB;

	/**the checked value **/
	private boolean checked=false;
	
	/**
	 * Constructs a new MMCheckableRelativeLayout
	 * @param p_oContext the context to use
	 * @see RelativeLayout#RelativeLayout(Context)
	 */
	public MMCheckableRelativeLayout(Context p_oContext) {
		super(p_oContext);
	}

	/**
	 * Constructs a new MMCheckableRelativeLayout
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see RelativeLayout#RelativeLayout(Context, AttributeSet)
	 */
	public MMCheckableRelativeLayout(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
	}

	/** 
	 * implements checkable interface to style the selected item of a listView (workaround of activated in android > v11)
	 * @param p_bChecked desired checked state
	 * @see Checkable#setChecked(boolean)
	 */
	@Override
	public void setChecked(boolean p_bChecked) {
		if (p_bChecked != checked) {
			checked = p_bChecked;
			refreshDrawableState();
		}
	}

	@Override
	public boolean isChecked() {
		return checked;
	}

	@Override
	public void toggle() {
		setChecked(!checked);	
	}

	/**
	 * Describes the checked state of the layout drawable on post honeycomb versions
	 */
	private static final int[] CHECKED_STATE_SET = {
		R.attr.state_activated
	};
	
	/**
	 * Describes the checked state of the layout drawable on pre honeycomb versions
	 */
	private static final int[] PREHONEYCOMB_CHECKED_STATE_SET = {
		R.attr.state_checked
	};

	@Override
	protected int[] onCreateDrawableState(int p_iExtraSpace) {
		final int[] drawableState = super.onCreateDrawableState(p_iExtraSpace + 1);
		if (isChecked()) {
			if ( PRE_HONEYCOMB ) {
				mergeDrawableStates(drawableState, PREHONEYCOMB_CHECKED_STATE_SET);
			} else {
				mergeDrawableStates(drawableState, CHECKED_STATE_SET);
			}
		}
		return drawableState;
	}
}
