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
package com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable;

import java.util.Locale;

import android.util.AttributeSet;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * Delegate for Enum handled type component
 *
 */
public class AndroidConfigurableEnumComponentDelegate extends
		AndroidConfigurableVisualComponentDelegate<Enum<?>> {

	/** the enum default value */
	private static final String DEFAULT_VALUE = "fwk_none";
	/** no resource value */
	public static final int NO_RESSOURCE = -1;
	/** enum prefix for resource */
	private String enumRessourceNamePrefix;
	/** does it replace resource by Enum text */
	private boolean isMissingRessourceReplacedByText = true;
	/** current Enum value as String */
	private String enumValue = DEFAULT_VALUE;

	/**
	 * Constructor
	 * @param p_oCurrentView the component (inherited from TextView)
	 * @param p_oDelegateType the type of the delegate
	 */
	public AndroidConfigurableEnumComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType) {
		super(p_oCurrentView, p_oDelegateType);
	}
	
	/**
	 * Construct new instance of AndroidConfigurableEnumComponentDelegate
	 * @param p_oCurrentView the component view
	 * @param p_oDelegateType the class type handled by component
	 * @param p_oAttrs the android view attributes (in layout XML)
	 */
	public AndroidConfigurableEnumComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType, AttributeSet p_oAttrs) {
		super(p_oCurrentView, p_oDelegateType, p_oAttrs);
		if (p_oAttrs != null) {
			String sEnumName = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "enum");
			if (sEnumName == null) {
				sEnumName = StringUtils.EMPTY;
			}else {
				sEnumName = sEnumName.toLowerCase(Locale.getDefault());
			}
			String sPrefix = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "prefix");
			if (sPrefix == null) {
				sPrefix = StringUtils.EMPTY;
			}
			
			StringBuilder imageNamePrefixBuilder = new StringBuilder();
			if(sPrefix.length() > 0) {
				imageNamePrefixBuilder.append(sPrefix).append('_');
			}
			else {
				imageNamePrefixBuilder.append("enum_");
			}
			imageNamePrefixBuilder.append(sEnumName).append('_');
			this.enumRessourceNamePrefix = imageNamePrefixBuilder.toString();
			this.isMissingRessourceReplacedByText  = p_oAttrs.getAttributeBooleanValue(Application.MOVALYSXMLNS, "imageMissingReplacement", true);
		}
	}

	/**
	 * Getter for enum prefix name
	 * @return the enum resource prefix name
	 */
	public String getEnumRessourceNamePrefix() {
		return enumRessourceNamePrefix;
	}
	/**
	 * Is missing resource replace by Enum text
	 * @return true if the resource are replace by Enum text, false otherwise
	 */
	public boolean isMissingRessourceReplacedByText() {
		return this.isMissingRessourceReplacedByText;
	}
	/**
	 * Set if the resource are replace by Enum text
	 * @param p_bReplaceByTextIfMissing true if resource are replace by text
	 */
	public void setIsMissingRessourceReplacedByText(boolean p_bReplaceByTextIfMissing) {
		this.isMissingRessourceReplacedByText = p_bReplaceByTextIfMissing;
	}
	/**
	 * Set the the Enum resource prefix
	 * @param p_sRessourcePrefix the new prefix to get resources
	 */
	public void setEnumRessourceNamePrefix(String p_sRessourcePrefix) {
		this.enumRessourceNamePrefix = p_sRessourcePrefix;
	}
	/**
	 * Set the Enum value
	 * @param p_sEnumValue the new Enum value
	 */
	public void setEnumValue(String p_sEnumValue) {
		this.enumValue = p_sEnumValue;
	}
	/**
	 * Get the Enum value
	 * @return the current Enum value
	 */
	public String getEnumValue() {
		return this.enumValue;
	}
	/**
	 * Concatenate Enum prefix resource value and the enum value
	 * @return the concatenation of the prefix and the Enum value
	 */
	public String getEnumRessourceValue() {
		return this.enumRessourceNamePrefix + this.enumValue;
	}
	/**
	 * Get the resource id
	 * @param p_oType the group to search resource id in (for example {@link ApplicationRGroup#DRAWABLE})
	 * @return the id of resource if exists, -1 otherwise
	 */
	public int getRessourceId(ApplicationRGroup p_oType) {
		return ((AndroidApplication)AndroidApplication.getInstance()).getAndroidIdByStringKey( p_oType, this.getEnumRessourceValue());
	}
	
	@Override
	public void configurationSetValue(Enum<?> p_oObjectToSet) {
		this.enumValue = p_oObjectToSet.name().toLowerCase(Locale.getDefault());
		super.configurationSetValue(p_oObjectToSet);
	}
	
}
