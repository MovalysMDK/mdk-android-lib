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

import java.util.List;

import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.MDKViewConnectorWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.AbstractComponentWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.ComponentWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * Helper for wrapper on binded components
 * Use this to instantiate a wrapper on a component
 */
public class WidgetWrapperHelper {

	private static final String WRAPPER_SUFFIX = "Wrapper";

    /**
     * Used for parsing the base package of a class package name
     */
    private static final String PACKAGE_SEPARATOR = "\\.";
	
	/**
	 * Singleton instance.
	 */
	private static WidgetWrapperHelper instance = null;
	
	/**
	 * private constructor.
	 */
	private WidgetWrapperHelper() {
		// nothing to do
	}
	
	/**
	 * Return singleton instance.
	 * @return singleton instance
	 */
	public static WidgetWrapperHelper getInstance() {
		if (instance == null) {
			instance = new WidgetWrapperHelper();
		}
		return instance;
	}

	/**
	 * Looks for a wrapper implementation of the given view class and all its parent.
	 * @param p_oView the view to look a wrapper for
	 * @return the wrapper class name, null if there is none found
	 */
	public String getWrapperClassForComponent(View p_oView) {
		if (p_oView != null) {
			return this.getWrapperClassForComponent(p_oView.getClass(), false);
		}
		return null;
	}
	
	/**
	 * Looks for a wrapper implementation of the given class and all its parent.
	 * We can look for a component wrapper or connector wrapper
	 * @param p_oViewClass the class to look for
	 * @param p_bIsConnector true if we are looking for a connector, else we are looking for a component wrapper
	 * @return the wrapper name found, null otherwise
	 */
	private String getWrapperClassForComponent(Class<?> p_oViewClass, boolean p_bIsConnector) {
		StringBuilder oBeanKey = new StringBuilder();
		Class<?> oCurrentClass = p_oViewClass;
		boolean hasFoundDef = false;

		// Try to wrap the component, by bubbling into the parent class until a corresponding
		// wrapper is found.
		// Bubbling stops when the base package has changed : this is in order to prevent the
        // wrapping of legacy components
        String basePackage = oCurrentClass.getPackage().getName().split(PACKAGE_SEPARATOR)[0];
		while (!hasFoundDef && oCurrentClass != null && basePackage.equals(oCurrentClass.getPackage().getName().split(PACKAGE_SEPARATOR)[0])) {
			oBeanKey.setLength(0);

			oBeanKey.append(computeName(oCurrentClass.getSimpleName(), p_bIsConnector));

			hasFoundDef = BeanLoader.getInstance().hasDefinition(oBeanKey.toString());

			oCurrentClass = oCurrentClass.getSuperclass();
		}
		
		String r_oBeanKey = null;
		
		if (hasFoundDef) {
			r_oBeanKey = oBeanKey.toString();
		}
		
		return r_oBeanKey;
	}
	
	/**
	 * Returns true if the view with the given class can be binded to a wrapper
	 * @return true if a wrapper exists for the given view class
	 */
	public boolean componentHasWrapper(Class<?> p_oViewClass) {
		return (this.getWrapperClassForComponent(p_oViewClass, false) != null);
	}
	
	
	public ComponentWrapper getWrapperForComponent(ViewModel p_oViewModel, String p_sPath) {
		List<AbstractComponentWrapper<?>> oList = p_oViewModel.getWrapperAtPath(p_sPath);
		
		// if there are no component or more than one
		if (oList == null || oList.size() > 1) {
			return null;
		} else {
			return oList.get(0);
		}
	}
	
	/**
	 * Returns the wrapper for a component.
	 * @param p_oView the component on which a wrapper should be created
	 * @param p_sWrapperClass the class name of the wrapper
	 * @return the created wrapper
	 */
	public ComponentWrapper createWrapper(View p_oView, String p_sWrapperClass) {
		return this.createWrapper(p_oView, p_sWrapperClass, false);
	}
	
	/**
	 * Returns the wrapper for a component.
	 * @param p_oView the component on which a wrapper should be created
	 * @param p_sWrapperClass the class name of the wrapper
	 * @param p_bIsRestoring true if the component is being restored after a rotation
	 * @return the created wrapper
	 */
	public ComponentWrapper createWrapper(View p_oView, String p_sWrapperClass, boolean p_bIsRestoring) {
		ComponentWrapper r_oWrapper = null;
		
		// we instantiate the wrapper
		r_oWrapper = (ComponentWrapper) BeanLoader.getInstance().instantiatePrototypeFromConstructor(p_sWrapperClass, null, null);
		
		// we link the wrapper to the component
		r_oWrapper.setComponent(p_oView, p_bIsRestoring);
		
		return r_oWrapper;
	}
	
	/**
	 * Returns the adapter connector wrapper for a given view class.
	 * If no connector was found, an exception will be raised.
	 * @param p_oViewClass the view class on which we look for a connector
	 * @return the connector found.
	 */
	public MDKViewConnectorWrapper getConnectorWrapper(Class<? extends View> p_oViewClass) {
		MDKViewConnectorWrapper r_oWrapper;

		String oBeanKey = getWrapperClassForComponent(p_oViewClass, true);
		
		r_oWrapper = (MDKViewConnectorWrapper) BeanLoader.getInstance().instantiatePrototypeFromConstructor(oBeanKey, null, null);

		return r_oWrapper;
	}
	
	/**
	 * Computes the name of the wrapper to look for.
	 * @param p_sClassName the name of the class to look for a wrapper on
	 * @param p_bIsConnector true if we are looking for a connector, else we are looking for a component wrapper
	 * @return the computed name
	 */
	private String computeName(String p_sClassName, boolean p_bIsConnector) {
		if (p_bIsConnector) {
			return computeConnectorName(p_sClassName);
		} else {
			return computeWrapperName(p_sClassName);
		}
	}

	/**
	 * computes the name of a component wrapper with the class name given in parameter.
	 * @param p_sClassName the class name to compute a name on
	 * @return the wrapper name
	 */
	private String computeWrapperName(String p_sClassName) {
		return p_sClassName + WRAPPER_SUFFIX;
	}
	
	/**
	 * computes the name of a connector wrapper with the class name given in parameter.
	 * @param p_sClassName the class name to compute a name on
	 * @return the wrapper name
	 */
	private String computeConnectorName(String p_sClassName) {
		return "connectorwrapper_" + p_sClassName;
	}
}
