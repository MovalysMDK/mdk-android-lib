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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper;

import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * root interface of a component wrapper
 */
public interface ComponentWrapper extends ConfigurableVisualComponent {

	/**
	 * Sets the component to wrap by the current instance
	 * @param p_oComponent the component being wrapper
	 * @param p_bIsRestoring true if the component is being restored after a rotation
	 */
	public void setComponent(View p_oComponent, boolean p_bIsRestoring);
	
	/**
	 * returns the component wrapped by the current instance
	 * @return the component wrapped
	 */
	public View getComponent();
	
	/**
	 * Unsets the component. Used to destroy the link between the wrapper and its component, ie remove all listeners...
	 */
	public void unsetComponent();
}
