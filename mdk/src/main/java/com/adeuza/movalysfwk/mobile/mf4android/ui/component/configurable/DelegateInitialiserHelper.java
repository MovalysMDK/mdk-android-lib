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

import java.lang.reflect.Array;

import android.util.AttributeSet;
import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.ComponentWrapper;

/**
 * Helper class used to initialize the MDK components delegates
 */
public class DelegateInitialiserHelper {
	
	/**
	 * static method used to initialize the components delegates
	 * @param p_oComponent the component on which the delegate should be initialized
	 * @param p_oParamsClasses an array of classes describing the parameters to send to the beanloader
	 * @param p_oParamsObjects an array of objects describing the parameters to send to the beanloader
	 * @param <DELEGATE> the type returned by the delegate
	 * @return an instantiated delegate
	 */
	public static <DELEGATE> Object initDelegate(ConfigurableVisualComponent p_oComponent, Class<?>[] p_oParamsClasses, Object[] p_oParamsObjects) {
		return initOneDelegate(p_oComponent, false, p_oParamsClasses, p_oParamsObjects);
	}
	
	/**
	 * static method used to initialize the framework delegates
	 * @param p_oComponent the component on which the delegate should be initialized
	 * @param p_oParamsClasses an array of classes describing the parameters to send to the beanloader
	 * @param p_oParamsObjects an array of objects describing the parameters to send to the beanloader
	 * @param <DELEGATE> the type returned by the delegate
	 * @return an instantiated delegate
	 */
	public static <DELEGATE> Object initFwkDelegate(ConfigurableVisualComponent p_oComponent, Class<?>[] p_oParamsClasses, Object[] p_oParamsObjects) {
		return initOneDelegate(p_oComponent, true, p_oParamsClasses, p_oParamsObjects);
	}
	
	/**
	 * static method used to initialize the components delegates
	 * @param p_oComponent the component on which the delegate should be initialized
	 * @param p_oDefaultDelegateClass the default delegate, should the one linked to the component not be found
	 * @param p_bIsFwk true if the desired delegate is a framework delegate
	 * @param p_oParamsClasses an array of classes describing the parameters to send to the beanloader
	 * @param p_oParamsObjects an array of objects describing the parameters to send to the beanloader
	 * @param <DELEGATE> the type returned by the delegate
	 * @return an instantiated delegate
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <DELEGATE> Object initOneDelegate(ConfigurableVisualComponent p_oComponent, boolean p_bIsFwk, Class<?>[] p_oParamsClasses, Object[] p_oParamsObjects) {
		
		DELEGATE r_oDelegate = null;
		
		BeanLoader oBeanLoader = BeanLoader.getInstance();
		StringBuilder oBeanKey = new StringBuilder();
		
		Class<?> oCurrentClass = null;
		
		if (p_oComponent instanceof ComponentWrapper) {
			oCurrentClass = ((ComponentWrapper) p_oComponent).getComponent().getClass();
		} else {
			oCurrentClass = p_oComponent.getClass();
		}
		
		boolean hasFoundDef = false;
		boolean isComponent = (p_oComponent instanceof View);
		boolean keepLooking = oCurrentClass != null || (isComponent && p_oComponent instanceof ConfigurableVisualComponent);
		
		while (!hasFoundDef && keepLooking) { 
			oBeanKey.setLength(0);
			
			oBeanKey.append(oCurrentClass.getSimpleName());
			if (p_bIsFwk) {
				oBeanKey.append("Fwk");
			}
			oBeanKey.append("Delegate");
			
			hasFoundDef = oBeanLoader.hasDefinition(oBeanKey.toString());
			
			oCurrentClass = oCurrentClass.getSuperclass();
			
			keepLooking = oCurrentClass != null;
			
			if (isComponent) {
				keepLooking &= ConfigurableVisualComponent.class.isAssignableFrom(oCurrentClass);
			}
		}
		
		if (hasFoundDef) {
			Class<?>[] oParamsClasses = concatenate(new Class[]{ConfigurableVisualComponent.class}, p_oParamsClasses);
			Object[] oParamsObjects = concatenate(new Object[]{(ConfigurableVisualComponent)p_oComponent}, p_oParamsObjects);
			r_oDelegate = oBeanLoader.instantiatePrototypeFromConstructor(oBeanKey.toString(), oParamsClasses, oParamsObjects);
		} else {
			
			if (p_bIsFwk) {
				r_oDelegate = (DELEGATE) new AndroidConfigurableVisualComponentFwkDelegate(p_oComponent, (AttributeSet) p_oParamsObjects[0]);
			} else {
				AttributeSet param3 = null; 
				
				if (p_oParamsObjects.length > 1) {
					param3 = (AttributeSet) p_oParamsObjects[1];
				}
				
				r_oDelegate = (DELEGATE) new AndroidConfigurableVisualComponentDelegate((ConfigurableVisualComponent) p_oComponent, (Class<?>) p_oParamsObjects[0], param3);
			}
		}
		
		return r_oDelegate;
	}
	
	/**
	 * static method used to concatenate two arrays
	 * @param p_oFirstArray the first array
	 * @param p_oSecondArray the second array
	 * @param <T> arrays type
	 * @return an array composed of the two passed in parameters
	 */
	private static <T> T[] concatenate (T[] p_oFirstArray, T[] p_oSecondArray) {
	    int iFirstLength = p_oFirstArray.length;
	    int iSecondLength = p_oSecondArray.length;

	    @SuppressWarnings("unchecked")
	    T[] r_oConcatArrays = (T[]) Array.newInstance(p_oFirstArray.getClass().getComponentType(), iFirstLength+iSecondLength);
	    System.arraycopy(p_oFirstArray, 0, r_oConcatArrays, 0, iFirstLength);
	    System.arraycopy(p_oSecondArray, 0, r_oConcatArrays, iFirstLength, iSecondLength);

	    return r_oConcatArrays;
	}
	
}
