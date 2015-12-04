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
package com.adeuza.movalysfwk.mobile.mf4android.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.adeuza.movalysfwk.mobile.mf4android.activity.lifecyclecallbacks.app.LifecycleDispatchFragmentActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.application.MFApplicationHolder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.ScreenDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumericConstants;

/**
 * <p>
 * Activity for displaying an information message that asks the user to
 * configure the Movalys application.
 * </p>
 * 
 * @since Barcelone (19 nov. 2010)
 */
public class ResetSettingsAndExitDialogActivity extends LifecycleDispatchFragmentActivity implements
OnClickListener, Screen {

	/**
	 * Request code to the activity.
	 */
	public static final int REDIRECT_TO_PARAMETER_ACTIVITY_REQUEST_CODE = Math
			.abs(ResetSettingsAndExitDialogActivity.class.hashCode()) & NumericConstants.HEXADECIMAL_MASK;
	/**
	 * The 'Parameter' button of the activity
	 */
	private Button uiOkButton;

	private AndroidApplication androidApplication;
	private ScreenDelegate screenDelegate;

	public ResetSettingsAndExitDialogActivity() {
		super();
		this.screenDelegate = new ScreenDelegate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle p_oSavedInstanceState) {
		super.onCreate(p_oSavedInstanceState);

		this.androidApplication = (AndroidApplication) Application
				.getInstance();

		// Make us non-modal, so that others can receive touch events.
		getWindow().setFlags(LayoutParams.FLAG_NOT_TOUCH_MODAL, LayoutParams.FLAG_NOT_TOUCH_MODAL);

		// ...but notify us that it happened.
		getWindow().setFlags(LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);



		this.setContentView(this.androidApplication.getAndroidIdByRKey(
				AndroidApplicationR.fwk_screen_reset_settings_and_exit));

		this.uiOkButton = (Button) this
				.findViewById(this
						.androidApplication
						.getAndroidIdByRKey(
								AndroidApplicationR.screen_reset_settings_and_exit_ok_button));
		this.uiOkButton.setOnClickListener(this);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		onClick(this.uiOkButton);
		return true;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClick(View p_oView) {
		if (p_oView == this.uiOkButton) {
			MFApplicationHolder.getInstance().getApplication().launchStopApplication();
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return this.screenDelegate.getName();
	}
}
