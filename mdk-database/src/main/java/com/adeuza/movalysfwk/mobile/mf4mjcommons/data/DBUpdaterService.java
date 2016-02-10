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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data;

import java.util.List;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.Scope;
import com.adeuza.movalysfwk.mf4jcommons.core.beans.ScopePolicy;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.ServiceException;

/**
 * <p>Database Updater Service</p>
 *
 *
 */
@Scope(ScopePolicy.SINGLETON)
public interface DBUpdaterService {

	/**
	 * Create database
	 *
	 * @param p_oContext transactional context
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.ServiceException service exception
	 */
	public void createDatabase(MContext p_oContext ) throws ServiceException;
	
	/**
	 * Drop database
	 *
	 * @param p_oContext transactional context
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.ServiceException service exception
	 */
	public void dropDatabase(MContext p_oContext ) throws ServiceException;
	
	/**
	 * Upgrade database
	 *
	 * @param p_oContext transactional context
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.ServiceException service exception
	 */
	public void upgradeDatabase(MContext p_oContext ) throws ServiceException;
	
	/**
	 * Get Create Instructions
	 *
	 * @return create sql instructions
	 */
	public List<String> getCreateInstructions();
	
	/**
	 * Get Data Create Instructions
	 *
	 * @return create sql instructions
	 */
	public List<String> getCreateDataInstructions();
	
	/**
	 * Get Drop Instructions
	 *
	 * @return drop sql instructions
	 */
	public List<String> getDropInstructions();
	
	/**
	 *  Get Drop data Instructions
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<String> getDropDataInstructions();
}
