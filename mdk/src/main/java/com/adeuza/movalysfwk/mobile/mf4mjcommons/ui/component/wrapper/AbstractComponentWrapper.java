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

import java.lang.ref.WeakReference;

import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * Abstract class describing a component wrapper
 */
public abstract class AbstractComponentWrapper<T extends View> implements ConfigurableVisualComponent, ComponentWrapper {
	
	/** the component that the wrapper takes charge of */
	protected WeakReference<T> component;
	
	/** framework is writing data */
	protected boolean writingData = false;
	
	/**
	 * Constructor
	 */
	public AbstractComponentWrapper() {
		// nothing to do
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setComponent(View p_oComponent, boolean p_bIsRestoring) {
		this.component = new WeakReference<T>((T) p_oComponent);
		this.writingData = p_bIsRestoring;
	}
	
	@Override
	public View getComponent() {
		return this.component.get();
	}

	/**
	 * Indicates to the wrapper whether the component is being restored after a rotation.
	 * @param p_bWritingData true if the component is being restored after a rotation
	 */
	public void setWritingData(boolean p_bWritingData) {
		this.writingData = p_bWritingData;
	}
}
