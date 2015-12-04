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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.EntityFactory;

/**
 * <p>EntityHelper class.</p>
 *
 */
public abstract class AbstractEntityHelper {
	
	/**
	 * Map for the known entity factories
	 */
	protected Map<Class<MEntity>, Class<EntityFactory<MEntity>>> 
		mapEntityFactories = new HashMap<Class<MEntity>, Class<EntityFactory<MEntity>>>();
	
	/**
	 * Returns an instance of the EntityHelper class
	 * @return an instance of the EntityHelper class
	 */
	public static AbstractEntityHelper getInstance() {
		AbstractEntityHelper oHelper = null;
		
		try {
			Class<?> oEntityHelper = Class.forName("com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.EntityHelper");
			
			Method oMethod = oEntityHelper.getMethod("getInstance");
			
			oHelper = (AbstractEntityHelper) oMethod.invoke(null);
			
		} catch (ClassNotFoundException | NoSuchMethodException
				| SecurityException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| BeanLoaderError e) {
			oHelper = new AbstractEntityHelper() {
				@Override
				protected void initialize(String p_sKey, String p_sValue) {
					// nothing to do
				}
			};
		}
		
		return oHelper;
	}
	
	/**
	 * <p>getEntityClasses.</p>
	 *
	 * @return a {@link java.util.Collection} object.
	 */
	public Collection<Class<MEntity>> getEntityClasses() {
		return this.mapEntityFactories.keySet();
	}
	
	/**
	 * <p>getFactoryForEntity.</p>
	 *
	 * @param p_oEntityClass a {@link java.lang.Class} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.EntityFactory} object.
	 * @param <T> a T object.
	 */
	public <T extends MEntity> EntityFactory<T> getFactoryForEntity( Class<T> p_oEntityClass ) {
		Class<EntityFactory<MEntity>> oFactoryClass = this.mapEntityFactories.get(p_oEntityClass);
		return (EntityFactory<T>) BeanLoader.getInstance().getBean(oFactoryClass);
	}

	/**
	 * <p>initialize.</p>
	 *
	 * @param p_oInputStream a {@link java.io.InputStream} object.
	 */
	public void initialize(InputStream p_oInputStream) {

		PropertyResourceBundle oPropertyResourceBundle;
		try {
			oPropertyResourceBundle = new PropertyResourceBundle(p_oInputStream);
			Enumeration<String> enumKeys = oPropertyResourceBundle.getKeys();

			String sKey = null;
			while (enumKeys.hasMoreElements()) {
				sKey = enumKeys.nextElement();
				this.initialize(sKey, oPropertyResourceBundle.getString(sKey));
			}
		}
		catch (IOException oIOException ) {
			throw new BeanLoaderError("EntityFactoryUtils: failed reading input stream", oIOException);
		}
	}
	
	/**
	 * Initialize value
	 * @param p_sKey class of the MEntity
	 * @param p_sValue class of the factory/EntityDao
	 */
	protected abstract void initialize(String p_sKey, String p_sValue);
}
