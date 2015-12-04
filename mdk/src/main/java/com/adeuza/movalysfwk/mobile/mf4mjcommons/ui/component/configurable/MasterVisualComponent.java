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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * <p>A MasterVisualComponent is a component containing other components
 * which can not be contained within another component
 * in the sense configuration (e.g. a frame, a list item).
 * A component of this type has a model</p>
 *
 *
 */
public interface MasterVisualComponent extends ConfigurableVisualComponent, ConfigurableVisualNativeComponent {

	/**
	 * Returns the associated model
	 * @return the associated model
	 */
	public ViewModel getViewModel();
	
	/**
	 * Sets the current view model
	 *
	 * @param p_oModel the current view model
	 */
	public void setViewModel(ViewModel p_oModel);
	
	/**
	 * Returns a sub component of name "p_oKey"
	 * @param p_oPath the path of component
	 * @param p_bIsInflating true if the layout is being inflated
	 * @return a visual component
	 */
	public ConfigurableVisualComponent retrieveConfigurableVisualComponent(List<String> p_oPath, boolean p_bIsInflating);

	/**
	 * Parameters used to compute some rules
	 * @return a map of parameters
	 */
	public Map<String, Object> getParameters();
	
	/**
	 * Parameters used to compute some rules
	 * @param p_oParameters a map of parameters
	 */
	public void setParameters(Map<String, Object> p_oParameters);
	
	/**
	 * Launch the modification by code of the current component
	 * @param p_bWriteData a boolean.
	 */
	public void inflate(boolean p_bWriteData);
}
