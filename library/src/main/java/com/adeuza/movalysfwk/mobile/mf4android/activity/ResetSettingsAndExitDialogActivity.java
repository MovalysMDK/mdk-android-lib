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
 * <p>
 * Copyright (c) 2010
 * </p>
 * <p>
 * Company: Adeuza
 * </p>
 * 
 * @author fbourlieux
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

	@Override
	public String getName() {
		return this.screenDelegate.getName();
	}

}
