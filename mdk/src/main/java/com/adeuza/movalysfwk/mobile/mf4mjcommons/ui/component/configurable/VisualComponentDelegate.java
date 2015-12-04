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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable;

import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;

/**
 * VisualComponentDelegate
 * 
 * <p>Interface of Component delegate.</p>
 * <p>All methods to interact with component are define in this delegate.</p>
 * <p>To override the default component behaviors override delegate methods</p>
 *
 * @param <VALUE> the parameter of get/set value delegate
 */
@Scope(ScopePolicy.PROTOTYPE)
public interface VisualComponentDelegate<VALUE> {

	/**
	 * This method is called to set the value of a "complex" MMComponent
	 * using the auto-bind system based on the name of the attribute that finish by __value
	 *
	 * @param p_oObjectToSet
	 * 				the object to set in the component
	 */
	public void configurationSetValue(VALUE p_oObjectToSet);
	/**
	 * Return the type of VALUE
	 *
	 * @return the type of VALUE
	 */
	public Class<?> getValueType();
	/**
	 * Get value
	 *
	 * @return the value of component
	 */
	public VALUE configurationGetValue();
	/**
	 * Whether the component considers that the component must not be displayed.
	 *
	 * @param p_oObject the object to analyse
	 * @return true wether value is null
	 */
	public boolean isNullOrEmptyValue(VALUE p_oObject);
	/**
	 * Indicates whether component is filled
	 *
	 * @return true whether component is filled
	 */
	public boolean isFilled();
	/**
	 * Initializes the component. Called after autobinding
	 *
	 * @since 3.1.1
	 */
	public void doOnPostAutoBind();
	/**
	 * Destroy the component
	 */
	public void destroy();
	/**
	 * Indicates if component must be always enabled (simple list, expandable list, flipper expandabvle, workspace etc...)
	 * even if its child components are disabled
	 *
	 * @return true if component is a always enabled, false else
	 */
	public boolean isAlwaysEnabled();
	/**
	 * Set the mandatory label
	 */
	public void configurationSetMandatoryLabel();
	/**
	 * Remove the mandatory label
	 */
	public void configurationRemoveMandatoryLabel();

	/**
	 * Enabled component
	 */
	public void configurationEnabledComponent();

	/**
	 * Disable component
	 */
	public void configurationDisabledComponent();
	
	/**
	 * do when component is filled
	 *
	 * @param p_oError a {@link java.lang.String} object.
	 */
	public void configurationSetError(String p_oError);
	/**
	 * do when component is not filled
	 */
	public void configurationUnsetError();

	/**
	 * Prepare the component to hide a set of component
	 *
	 * @param p_oComponentsToHide the component to hide
	 */
	public void configurationPrepareHide(List<ConfigurableVisualComponent> p_oComponentsToHide);
	
	/**
	 * Mask component
	 *
	 * @param p_bLockModifier true if we want to lock component
	 */
	public void configurationHide(boolean p_bLockModifier);
	
	/**
	 * UnMask component
	 *
	 * @param p_bLockModifier true if we want to lock component
	 */
	public void configurationUnHide(boolean p_bLockModifier);

	/**
	 * Hide a single component
	 */
	public void hide();

	/**
	 * Show a single component
	 */
	public void unHide();
	/**
	 * Validate the data of component
	 *
	 * @param p_oConfiguration a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration} object.
	 * @param p_mapParameter a {@link java.util.Map} object.
	 * @param p_oErrorBuilder a {@link java.lang.StringBuilder} object.
	 * @return true if data is valid
	 */
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameter, StringBuilder p_oErrorBuilder);
	
}
