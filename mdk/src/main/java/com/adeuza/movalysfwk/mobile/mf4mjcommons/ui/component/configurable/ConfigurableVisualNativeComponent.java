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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;

/**
 * Interface for native component
 * <p>use to keep get configuration methode in Activities</p>
 *
 */
public interface ConfigurableVisualNativeComponent {

	/**
	 * Returns the associated configuration
	 *
	 * @return a configuration
	 */
	public VisualComponentConfiguration getConfiguration();
	/**
	 * Sets the component descriptor
	 *
	 * @param p_oDescriptor the descritor to set
	 */
	public void setDescriptor(RealVisualComponentDescriptor p_oDescriptor);
	/**
	 * Returns the component descriptor
	 *
	 * @return the descriptor
	 */
	public RealVisualComponentDescriptor getDescriptor();

}
