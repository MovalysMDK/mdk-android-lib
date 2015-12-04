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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro;

import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>SyncTimestampService interface.</p>
 *
 */
public interface SyncTimestampService {

	/**
	 * Return map of synchronization dates for referentiel entities
	 *
	 * @param p_oContext a com$adeuza$movalysfwk$mobile$mf4mjcommons$context$MContext object.
	 * @return a {@link java.util.Map} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException if any.
	 */
	public Map<String, Long> getSyncTimestamps(MContext p_oContext) throws SynchroException;

	/**
	 * Update synchronisation date for referentiel entities
	 *
	 * @param p_mapSyncTimestamp a {@link java.util.Map} object.
	 * @param p_oContext a com$adeuza$movalysfwk$mobile$mf4mjcommons$context$MContext object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException if any.
	 */
	public void saveSyncTimestamps(Map<String, Long> p_mapSyncTimestamp, MContext p_oContext)
			throws SynchroException;

	/**
	 * Delete synchronisation date for a referential entity.
	 *
	 * @param p_sKey Referential entity's name.
	 * @param p_oContext Transactional context.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException if any.
	 */
	public void deleteSyncTimestamp(String p_sKey, MContext p_oContext) throws SynchroException;
}
