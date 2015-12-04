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
package com.adeuza.movalysfwk.mobile.mf4android.ui.views.preferences;

import android.content.Context;
import android.preference.RingtonePreference;
import android.util.AttributeSet;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;

/**
 * <p>TODO Décrire la classe MMRingtonePreference</p>
 *
 *
 *
 */

public class MMRingtonePreference extends RingtonePreference {
	/**
	 * TODO Décrire le constructeur MMPreferenceCategory
	 * @param p_oContext
	 */
	public MMRingtonePreference(Context p_oContext) {
		super(p_oContext);
	}

	/**
	 * TODO Décrire le constructeur MMPreferenceCategory
	 * @param p_oContext
	 * @param p_oAttrs
	 */
	public MMRingtonePreference(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		this.init(p_oAttrs);
	}

	/**
	 * TODO Décrire le constructeur MMPreferenceCategory
	 * @param p_oContext
	 * @param p_oAttrs
	 * @param p_oDefStyle
	 */
	public MMRingtonePreference(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
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
	}
}
