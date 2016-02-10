package com.adeuza.movalysfwk.mobile.mf4android.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.adeuza.movalysfwk.mobile.mf4android.activity.lifecyclecallbacks.app.LifecycleDispatchFragmentActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.application.Starter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
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
public class ParameterDialogActivity extends LifecycleDispatchFragmentActivity implements
		OnClickListener, Screen {

	/**
	 * Request code to the activity.
	 */
	public static final int REDIRECT_TO_PARAMETER_ACTIVITY_REQUEST_CODE = Math
			.abs(ParameterDialogActivity.class.hashCode()) & NumericConstants.HEXADECIMAL_MASK;
	/**
	 * The 'Parameter' button of the activity
	 */
	private Button uiParamButton;
	/**
	 * The 'Quit' button of the activity
	 */
	private Button uiStopButton;
	private AndroidApplication androidApplication;
	private ScreenDelegate screenDelegate;

	public ParameterDialogActivity() {
		super();
		this.screenDelegate = new ScreenDelegate();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle p_oSavedInstanceState) {
		super.onCreate(p_oSavedInstanceState);

		// Redémarrage Application suite arret process
		if (!this.getClass().getName().contains("ApplicationMainImpl")
				&& !this.getClass().getName()
						.contains("ParameterDialogActivity")) {
			if (Application.getInstance() == null
					|| ConfigurationsHandler.getInstance() == null
					|| BeanLoader.getInstance() == null) {

				Starter starter = new Starter(this);
				starter.runStandalone();
			}
		}

		this.androidApplication = (AndroidApplication) Application
				.getInstance();

		this.setContentView(this.androidApplication.getAndroidIdByRKey(
				AndroidApplicationR.fwk_screen_redirect_to_parameter_main));

		this.uiParamButton = (Button) this
				.findViewById(this
						.androidApplication
						.getAndroidIdByRKey(
								AndroidApplicationR.screen_redirect_to_parameter_param_button));
		this.uiParamButton.setOnClickListener(this);

		this.uiStopButton = (Button) this
				.findViewById(this
						.androidApplication
						.getAndroidIdByRKey(
								AndroidApplicationR.screen_redirect_to_parameter_stop_button));
		this.uiStopButton.setOnClickListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClick(View p_oView) {
		if (p_oView == this.uiParamButton) {
			// affichage de l'activity PreferenceActivity
			this.setResult(RESULT_OK);
			this.finish();
		} else if (p_oView == this.uiStopButton) {
			// Close the application
			this.setResult(RESULT_CANCELED);
			this.finish();
		}
	}

	@Override
	public String getName() {
		return this.screenDelegate.getName();
	}

}
