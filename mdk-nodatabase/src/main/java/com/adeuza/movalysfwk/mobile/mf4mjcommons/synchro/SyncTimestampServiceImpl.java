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
/**
 * Copyright (C) 2010 Sopra (support_movalys@sopra.com)
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


import java.util.HashMap;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>SyncTimestampServiceImpl class.</p>
 *
 */
public class SyncTimestampServiceImpl implements SyncTimestampService {

	/**
	 * prefix for timestamp in database
	 */
	public static final String SYNCHRO_TIMESTAMP_PREFIX = "synchro_timestamp_";
	
	/** {@inheritDoc} */
	@Override
	public Map<String, Long> getSyncTimestamps(MContext p_oContext) throws SynchroException {
		return new HashMap<String, Long>();
	}
	
	/** {@inheritDoc} */
	@Override
	public void saveSyncTimestamps( Map<String,Long> p_mapSyncTimestamp, MContext p_oContext ) throws SynchroException {
		// nothing to do
	}

	/**
	 * <p>deleteSyncTimestamp.</p>
	 *
	 * @param p_sKey a {@link java.lang.String} object.
	 * @param p_oContext context
	 * @throws SynchroException if any.
	 */
	@Override
	public void deleteSyncTimestamp(String p_sKey, MContext p_oContext) throws SynchroException {
		// nothing to do
	}
}
