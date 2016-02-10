package com.adeuza.movalysfwk.mobile.mf4android.ui.views.preferences;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;

/**
 * <p>TODO Décrire la classe MMEditTextPreference</p>
 *
 * <p>Copyright (c) 2012
 * <p>Company: Adeuza
 *
 * @author emalespine
 *
 */

public class MMEditTextPreference extends EditTextPreference{
	/**
	 * TODO Décrire le constructeur MMPreferenceCategory
	 * @param p_oContext
	 */
	public MMEditTextPreference(Context p_oContext) {
		super(p_oContext);
	}

	/**
	 * TODO Décrire le constructeur MMPreferenceCategory
	 * @param p_oContext
	 * @param p_oAttrs
	 */
	public MMEditTextPreference(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		this.init(p_oAttrs);
	}

	/**
	 * TODO Décrire le constructeur MMPreferenceCategory
	 * @param p_oContext
	 * @param p_oAttrs
	 * @param p_oDefStyle
	 */
	public MMEditTextPreference(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		this.init(p_oAttrs);
	}

	protected final void init(final AttributeSet p_oAttrs) {
		// Dans le cas des MMTextView inclut dans un xml ne faisant pas l'objet d'un binding,
		// il faut écraser le libellé définit dans le xml par l'éventuel libellé présent dans le ConfigurationsHandler (propriété)
		final int iTitleId = p_oAttrs.getAttributeResourceValue(AndroidApplication.ANDROID_XML_NAMESPACE, "title", 0);
		if (iTitleId != 0) {
			this.setTitle(((AndroidApplication)Application.getInstance()).getStringResource(iTitleId));
		}

		final int iSummaryId = p_oAttrs.getAttributeResourceValue(AndroidApplication.ANDROID_XML_NAMESPACE, "summary", 0);
		if (iSummaryId != 0) {
			this.setSummary(((AndroidApplication)Application.getInstance()).getStringResource(iSummaryId));
		}

		final int iDialogTitle = p_oAttrs.getAttributeResourceValue(AndroidApplication.ANDROID_XML_NAMESPACE, "dialogTitle", 0);
		if (iDialogTitle != 0) {
			this.setDialogTitle(((AndroidApplication)Application.getInstance()).getStringResource(iDialogTitle));
		}

		final int iDialogMessage = p_oAttrs.getAttributeResourceValue(AndroidApplication.ANDROID_XML_NAMESPACE, "dialogMessage", 0);
		if (iDialogMessage != 0) {
			this.setDialogMessage(((AndroidApplication)Application.getInstance()).getStringResource(iDialogMessage));
		}
	}

	/**
	 * {@inheritDoc}
	 * @see android.preference.EditTextPreference#onAddEditTextToDialogView(android.view.View, android.widget.EditText)
	 */
	@Override
	// MANTIS 20183 : Curseur à la fin du texte.
	protected void onAddEditTextToDialogView(View p_oDialogView, EditText p_oEditText) {
		super.onAddEditTextToDialogView(p_oDialogView, p_oEditText);
		p_oEditText.setSelection(p_oEditText.getText().length());
	}
}
