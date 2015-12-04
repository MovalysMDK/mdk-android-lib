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
package com.adeuza.movalysfwk.mobile.mf4android.ui.component.wrappers;

import java.util.HashMap;
import java.util.Map;

import android.util.AttributeSet;
import android.util.SparseArray;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * Helper for the xml attributes on binded components
 * Use this to store the attributes set on the component and make them available for the parsing engine
 */
public class ComponentsAttributesHelper {

	/** the unique instance of the class */
	private static ComponentsAttributesHelper instance;
	
	/** string resources prefix */
	private static final String STRING_RESOURCES_PREFIX = "@string/";
	
	/** the map storing attributes for a component by its identifier */
	private SparseArray<Map<ConfigurableVisualComponent.Attribute,String>> attributes;
	
	/**
	 * private constructor
	 */
	private ComponentsAttributesHelper() {
		attributes = new SparseArray<Map<ConfigurableVisualComponent.Attribute,String>>();
	}
	
	/**
	 * returns the unique instance of the class
	 * @return the unique instance of the class
	 */
	public static ComponentsAttributesHelper getInstance() {
		if (instance == null) {
			instance = new ComponentsAttributesHelper();
		}
		
		return instance;
	}
	
	/**
	 * stores the xml attributes of the given component
	 * @param p_iComponentId component identifier
	 * @param p_oAttrs attributes to store
	 */
	public void storeAttributesForComponent( int p_iComponentId, AttributeSet p_oAttrs ){
		if ( p_oAttrs!= null ){
			
			if (this.attributes.indexOfKey(p_iComponentId) < 0) {
				this.attributes.put(p_iComponentId, new HashMap<ConfigurableVisualComponent.Attribute,String>());
			}
			
			Map<ConfigurableVisualComponent.Attribute,String> oComponentAttributes = this.attributes.get(p_iComponentId);
			
			for (ConfigurableVisualComponent.Attribute oAttribute : ConfigurableVisualComponent.Attribute.values() ){
				String sValue = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, oAttribute.getName()) ;
				if (sValue != null){
					oComponentAttributes.put(oAttribute, convertParameterValue(sValue));
				}
			}
		}
	}
	
	/**
	 * returns true if the helper has attributes stored for the component with given identifier
	 * @param p_iComponentId component identifier to look for
	 * @return true if the helper has attributes for this component
	 */
	public boolean hasAttributesForComponent(int p_iComponentId) {
		return this.attributes.indexOfKey(p_iComponentId) > 0;
	}
	
	/**
	 * returns the attributes stored for a view with given id, null if no attributes were stored
	 * @param p_iComponentId component identifier to look for
	 * @return attributes stored for a the given view
	 */
	public Map<ConfigurableVisualComponent.Attribute,String> getAttributesForComponent(int p_iComponentId) {
		return this.attributes.get(p_iComponentId);
	}
	
	/**
	 * Tries to grab a resource in android strings from a given attribute
	 * @param p_sValue the attribute value
	 * @return the computed value
	 */
	private String convertParameterValue(String p_sValue) {
		AndroidApplication application = (AndroidApplication) Application.getInstance();
		String r_sValue = p_sValue;
		if (r_sValue.startsWith(STRING_RESOURCES_PREFIX)) {
			r_sValue = r_sValue.substring(STRING_RESOURCES_PREFIX.length());
			r_sValue = application.getStringResource(application.getAndroidIdByStringKey(ApplicationRGroup.STRING, r_sValue));
		}
		if(r_sValue == null){
			return p_sValue;
		}
		else {
			return r_sValue;
		}
	}
}
