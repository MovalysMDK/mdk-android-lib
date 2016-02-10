package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMTableLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.dowritemail.EMail;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;

public abstract class AbstractEmailTextView<T> extends
AbstractMMTableLayout<T> implements MMIdentifiableView,
OnClickListener {

	private static final String TAG = "AbstractEmailTextView";

	/** the email address field */
	protected TextView oUiEmail;
	/** the label above the email */
	protected TextView oUiLabel;
	/** the email button */
	protected View oUiEmailButton;
	/** The key to retain the the enable state of the mail client button */
	private static final String MAIL_CLIENT_BUTTON_ENABLED_STATE_KEY = "mailClientButtonEnabledStateKey";
	/** The key to retain the the hidden state of the mail client button */
	private static final String MAIL_CLIENT_BUTTON_HIDDEN_STATE_KEY = "mailClientButtonHiddenStateKey";
	/** The key to retain the the hidden state of the mail client button */
	private static final String MAIL_VALUE_KEY = "mailValueKey";
	/** Indique si on est en train de modifier le champ */
	protected boolean writingData;

	protected T oArgType;

	public AbstractEmailTextView(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.linkLayout();
		}
	}

	public AbstractEmailTextView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.linkLayout();
			linkChildrens(p_oAttrs);
		}
	}

	private void linkLayout(){
		LayoutInflater oInflater = LayoutInflater.from(this.getContext());
		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

		oInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_email_simpletextview), this);
		oUiLabel = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_email__email__label));
		oUiEmail = (TextView) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_email__email__value));
		oUiEmailButton = this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.component_email__email__button));

	}

	/**
	 * 
	 * link the child views with custom attributes and add button onClickListener
	 * 
	 * @param p_oAttrs
	 *            attributes of XML Component component_phone_textview
	 */
	private void linkChildrens(AttributeSet p_oAttrs) {

		int iValue = p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "label", 0);
		if ( iValue != 0 ) {
			oUiLabel.setText(this.getResources().getString(iValue));
		}

		if ( ((AndroidApplication)Application.getInstance()).isMailAvailable()) {
			oUiEmailButton.setOnClickListener(this);
		} else {
			oUiEmailButton.setVisibility(View.GONE);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()) {
			final BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) this.getConfiguration();
			if (oConfiguration != null && oConfiguration.getLabel() != null) {
				this.oUiLabel.setText(oConfiguration.getLabel());
			}

			if (this.oUiLabel.getText() == null || this.oUiLabel.getText().length() == 0) {
				this.oUiLabel.setVisibility(GONE);
			}
		}
	}
	/**
	 * {@inheritDoc}
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	public void onClick(View p_oView) {
		String sEmailAddress = oUiEmail.getText().toString();
		if (sEmailAddress != null && sEmailAddress.length() > 0) {
			Application.getInstance().getController().doWriteEmail(composeMail(oArgType));
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract Class<?> getValueType();

	/**
	 * Specify the action to realise
	 * @param mail
	 */
	protected abstract EMail composeMail(T p_oMail);

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	public abstract void configurationSetValue(T p_oObjectToSet);

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	public abstract boolean isNullOrEmptyValue(T p_oObject);


	/**
	 * Set the state of the client mail button (enable/disable)
	 * @param p_bIsEnabled The new state of the button
	 */
	protected void setMailClientButtonEnabled(boolean p_bIsEnabled) {
		this.oUiEmailButton.setEnabled(p_bIsEnabled);
	}

	/**
	 * Set the visibility of the client mail button (gone/visible)
	 * @param p_bIsEnabled The new visibility of the button
	 */
	protected void setMailClientButtonHidden(boolean p_bIsHidden) {
		if(p_bIsHidden) {
			this.oUiEmailButton.setVisibility(View.GONE);
		}
		else {
			this.oUiEmailButton.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putString(MAIL_VALUE_KEY, this.oUiEmail.getText().toString());
		r_oBundle.putBoolean(MAIL_CLIENT_BUTTON_ENABLED_STATE_KEY, this.oUiEmailButton.isEnabled());
		r_oBundle.putBoolean(MAIL_CLIENT_BUTTON_HIDDEN_STATE_KEY, this.oUiEmailButton.getVisibility() == View.GONE);
		return r_oBundle;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;
		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);


		boolean enabled = r_oBundle.getBoolean(MAIL_CLIENT_BUTTON_ENABLED_STATE_KEY);
		boolean hidden = r_oBundle.getBoolean(MAIL_CLIENT_BUTTON_HIDDEN_STATE_KEY);
		String value = r_oBundle.getString(MAIL_VALUE_KEY);
		if(value != null) {
			this.oUiEmail.setText(value);
		}

		setMailClientButtonEnabled(enabled);
		setMailClientButtonHidden(hidden);
	}


}
