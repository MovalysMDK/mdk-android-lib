package com.adeuza.movalysfwk.mobile.mf4android.ui.views.preferences;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;

/**
 * <p>TODO Décrire la classe MMListPreference</p>
 *
 * <p>Copyright (c) 2012
 * <p>Company: Adeuza
 *
 * @author emalespine
 *
 */

public class MMListPreference extends ListPreference {
	/**
	 * TODO Décrire le constructeur MMPreferenceCategory
	 * @param p_oContext
	 */
	public MMListPreference(Context p_oContext) {
		super(p_oContext);
	}

	/**
	 * TODO Décrire le constructeur MMPreferenceCategory
	 * @param p_oContext
	 * @param p_oAttrs
	 */
	public MMListPreference(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
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
	}
}
