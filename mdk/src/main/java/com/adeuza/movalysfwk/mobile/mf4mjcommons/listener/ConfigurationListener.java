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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.listener;

import java.util.Map;

/**
 * <p>Used to save an object configuration on device rotation</p>
 *
 *
 * @since MF-Annapurna
 */
public interface ConfigurationListener {

	/**
	 * Before rotating, we push to the map to be saved all the objects that we need to maintain
	 *
	 * @param p_oConfigurationMap the map used to save objects
	 */
	public void beforeConfigurationChanged(Map<String,Object> p_oConfigurationMap);
	
	/**
	 * After rotating, we retrieve the objects to restore from the map
	 *
	 * @param p_oConfigurationMap the map used to save objects
	 */
	public void afterConfigurationChanged(Map<String,Object> p_oConfigurationMap);
}
