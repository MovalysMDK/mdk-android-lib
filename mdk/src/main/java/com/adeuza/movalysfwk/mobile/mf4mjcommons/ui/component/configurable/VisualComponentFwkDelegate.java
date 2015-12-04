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

import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * Component Delegate for Framework
 * This is used to centralize methods that are defined on all components with the same 
 * implementation.
 * This defines the MDK inner behavior of a component and should never be overridden without caution.
 */
public interface VisualComponentFwkDelegate {

	/**
	 * Returns the name of object, i.e. the identifier string of the component in the Android layout
	 * @return the name of the component ; should never be null
	 */
	public String getName();

	/**
	 * Modify the name of the component. 
	 * Use with caution, a mistaken value will break the binding.
	 * @param p_sNewName a {@link java.lang.String} object ; should never be null
	 * @hide
	 */
	public void setName( String p_sNewName );

	/**
	 * Returns the associated {@link VisualComponentConfiguration} configuration.
	 * @return the configuration of the component ; should never be null
	 */
	public VisualComponentConfiguration getConfiguration();
	
	/**
	 * Sets the component descriptor {@link RealVisualComponentDescriptor}
	 * @param p_oDescriptor the descriptor to set
	 * @hide
	 */
	public void setDescriptor(RealVisualComponentDescriptor p_oDescriptor);
	
	/**
	 * Returns the component descriptor
	 * @return the descriptor
	 */
	public RealVisualComponentDescriptor getDescriptor();
	
	/**
	 * Indicates if component is label
	 *
	 * @return true if component is label
	 */
	public boolean isLabel();
	
	/**
	 * Indicates if component is value
	 *
	 * @return true if component is value
	 */
	public boolean isValue();
	
	/**
	 * Indicates if component is edit
	 *
	 * @return true if component is edit
	 */
	public boolean isEdit();

	/**
	 * Indicates whether the component has no value
	 * If it is, then there is no writing done by the view model on that component
	 * @return true if the component has no value
	 */
	public boolean hasNoValue();
	
	/**
	 * Returns the model
	 *
	 * @return the model
	 */
	public String getModel();

	/**
	 * Register the current VM on the Component
	 *
	 * @param p_oViewModel a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel} object.
	 */
	public void registerVM(ViewModel p_oViewModel);
	
	/**
	 * Unregister viewmodel
	 */
	public void unregisterVM();
	
	/**
	 * Get the tag of the fragment that holds this component, if exists
	 *
	 * @return A tag as a String or null
	 */
	public String getFragmentTag();
	
	/**
	 * Set the tag of the fragments thats holds this component
	 *
	 * @param p_sFragmentTag The tag of the fragment as a String
	 */
	public void setFragmentTag(String p_sFragmentTag);
	
	/**
	 * Return true if the component have rules
	 *
	 * @return true if has at least one rule, false otherwise
	 */
	public boolean hasRules();
	
	/**
	 * Set if the component have rules
	 *
	 * @param p_bHasRules true if at least one rule
	 */
	public void setHasRules(boolean p_bHasRules);
	
	/**
	 * Set label
	 *
	 * @param p_sLabel the label to set
	 */
	public void configurationSetLabel(String p_sLabel);
	
	/**
	 * Get the converter for this component
	 *
	 * @return A converter for this component ; can be null
	 */
	public CustomConverter customConverter();
	
	/**
	 * <p>set the converter for this component.</p>
	 *
	 * @param p_oConverter a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter} object or null.
	 */
	public void setCustomConverter(CustomConverter p_oConverter);

	/**
	 * Get the formatter for this component
	 *
	 * @return A formatter for this component.
	 */
	public CustomFormatter customFormatter();

	/**
	 * <p>Sets a formatter on the component.</p>
	 *
	 * @param p_oFormatter a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter} object or null.
	 */
	public void setCustomFormatter(CustomFormatter p_oFormatter);
	
	/**
	 * Reset the change indicator
	 */
	public void resetChanged();
	
	/**
	 * Indicates whether the component has changed
	 *
	 * @return true if the component has changed
	 */
	public boolean isChanged();
	
	/**
	 * Indicates whether the component is visible
	 *
	 * @return true if the component is visible
	 */
	public boolean isVisible();
	
	/**
	 * returns a map of the attributes to be saved to view model on view save / restore
	 * the attributes should be stored with their name / value
	 * @return a map of the attributes to save
	 */
	public Map<String, Object> instanceAttributes();
	
	/**
	 * restores the attributes saved to view model on the delegate
	 * the attributes should be stored with their name / value
	 */
	public void restoreInstanceAttributes();
	
	/**
	 * Saves the attributes of the delegate to the view model
	 * the attributes should be stored with their name / value
	 */
	public void saveInstanceAttributes();
	
}
