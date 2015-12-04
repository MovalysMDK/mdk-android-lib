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

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.EntityFactory;

/**
 * <p>EntityHelper class.</p>
 *
 */
public class EntityHelper extends AbstractEntityHelper {

	/**
	 * factory type
	 */
	private static final Object FACTORY_TYPE = "factory";

	/**
	 * dao type
	 */
	private static final Object DAO_TYPE = "dao";

	/**
	 * Singleton instance
	 */
	private static EntityHelper instance = new EntityHelper();
	
	/**
	 * Return singleton instance
	 *
	 * @return singleton instance
	 */
	public static EntityHelper getInstance() {
		return instance;
	}
	
	/**
	 * Initializes the instance of the class
	 * @param p_sKey the class to add
	 * @param p_sValue the linked factory 
	 * @throws BeanLoaderError if any
	 */
	@Override
	protected void initialize(String p_sKey, String p_sValue) throws BeanLoaderError {
		try {
			
			String[] t_sKey = p_sKey.split("\\|");
			String sEntityClassName = t_sKey[0];
			String sType = t_sKey[1];
						
			Class<MEntity> oEntityClass = (Class<MEntity>) Class.forName(sEntityClassName);
			
			if ( FACTORY_TYPE.equals(sType)) {
				Class<EntityFactory<MEntity>> oFactoryClass = (Class<EntityFactory<MEntity>>) Class.forName(p_sValue);
				this.mapEntityFactories.put(oEntityClass, oFactoryClass);
			}
			
		}
		catch (ClassNotFoundException oClassNotFoundException) {
			throw new BeanLoaderError(new StringBuilder()
					.append("EntityHelper: failed to initialize entity helper, key: '")
					.append(p_sKey)
					.append("\', value: '")
					.append(p_sValue)
					.append('\'')
					.toString(), oClassNotFoundException);
		}
	}
}
