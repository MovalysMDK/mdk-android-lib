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
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
/**
 * This class exists purely to cancel long click events.
 */
public class MMNumberPickerButton extends MMImageButton {
	/** Picker lié sur lequel le bouton agit*/
	private MMNumberPicker mNumberPicker;
	/**
	 * Construct a MMNumberPickerButton
	 * @param p_oContext
	 *            the context
	 * @param p_oAttrs
	 *            the xml attributes
	 * @param p_iDefStyle
	 * 			style du thème
	 */
	public MMNumberPickerButton(Context p_oContext, AttributeSet p_oAttrs, int p_iDefStyle) {
		super(p_oContext, p_oAttrs, p_iDefStyle);
	}
	/**
	 * Construct a MMNumberPickerButton
	 * @param p_oContext
	 *            the context
	 * @param p_oAttrs
	 *            the xml attributes
	 */
	public MMNumberPickerButton(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
	}
	/**
	 * Construct a MMNumberPickerButton
	 * @param p_oContext           the context
	 */
	public MMNumberPickerButton(Context p_oContext) {
		super(p_oContext);
	}
	/**
	 * Modify the linked picker
	 * @param p_oLinkedNumberPicker new linked picker
	 */
	public void setNumberPicker(MMNumberPicker p_oLinkedNumberPicker) {
		mNumberPicker = p_oLinkedNumberPicker;
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent p_oEvent) {
		this.cancelLongpressIfRequired(p_oEvent);
		return super.onTouchEvent(p_oEvent);
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see android.view.View#onTrackballEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTrackballEvent(MotionEvent p_oEvent) {
		this.cancelLongpressIfRequired(p_oEvent);
		return super.onTrackballEvent(p_oEvent);
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see android.view.View#onKeyUp(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyUp(int p_iKeycode, KeyEvent p_oEvent) {
		if ((p_iKeycode == KeyEvent.KEYCODE_DPAD_CENTER) || (p_iKeycode == KeyEvent.KEYCODE_ENTER)) {
			this.cancelLongpress();
		}
		return super.onKeyUp(p_iKeycode, p_oEvent);
	}
	/**
	 * Verify the event 
	 * @param p_oEvent event launched
	 */
	private void cancelLongpressIfRequired(MotionEvent p_oEvent) {
		if ((p_oEvent.getAction() == MotionEvent.ACTION_CANCEL) || (p_oEvent.getAction() == MotionEvent.ACTION_UP)) {
			this.cancelLongpress();
		}
	}
	/**
	 * Cancel the next action on button
	 */
	private void cancelLongpress() {
		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
		if (oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__increment__button) == getId()) {
			mNumberPicker.cancelIncrement();
		} else if (oApplication.getAndroidIdByRKey(AndroidApplicationR.component_numberpicker__decrement__button) == getId()) {
			mNumberPicker.cancelDecrement();
		}
	}
}
