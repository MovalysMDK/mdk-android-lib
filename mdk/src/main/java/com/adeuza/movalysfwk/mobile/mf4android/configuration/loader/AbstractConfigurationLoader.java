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
package com.adeuza.movalysfwk.mobile.mf4android.configuration.loader;

import android.content.res.XmlResourceParser;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Initializable;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;

/**
 * <p>TODO Décrire la classe AbstractConfigurationLoader</p>
 *
 *
 *
 */

public abstract class AbstractConfigurationLoader implements Initializable {

	private AndroidApplication androidApplication;
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mf4jcommons.core.beans.Initializable#initialize()
	 */
	@Override
	public void initialize() {
		this.androidApplication = (AndroidApplication) Application.getInstance();
		
	}

	protected final AndroidApplication getAndroidApplication() {
		return this.androidApplication;
	}

	/**
	 * Read a parameter in xml parser
	 * @param p_sId the parameter name
	 * @param p_oParser the parser to use
	 * @return the parameter value, if parameter is an android id then it's converted in literal parameter
	 */
	protected String getStringParameter(String p_sId, XmlResourceParser p_oParser) {
		String sName = null;
		String r_sValue = null;
		// la méthode du framework android p_oParser.getAttributeValue("android", p_sId)
		// semble ne pas fonctionner, on garde le code pas optimum pour le moment ???
		// A2A_DEV à retester problème de lié à la chaine pour android c'est pas une chaîne ???
		for (int a = 0; a < p_oParser.getAttributeCount(); a++) {
			sName = p_oParser.getAttributeName(a);
			if (sName.equals(p_sId) || sName.equals("android:" + p_sId) || sName.equals("movalys:" + p_sId)) {
				r_sValue = p_oParser.getAttributeValue(a);
				if (r_sValue.contains("@")) {
					r_sValue = r_sValue.substring(r_sValue.indexOf('@') + 1);
					try {
						int iValue = Integer.valueOf(r_sValue);
						r_sValue = this.getAndroidApplication().getAndroidIdStringByIntKey(iValue);
					}
					catch(NumberFormatException e) {
						// Nothing To Do
						Application.getInstance().getLog().error(NumberFormatException.class.getSimpleName(), e.toString());
					}
				}
				break;
			}
		}
		return r_sValue;
	}
}
