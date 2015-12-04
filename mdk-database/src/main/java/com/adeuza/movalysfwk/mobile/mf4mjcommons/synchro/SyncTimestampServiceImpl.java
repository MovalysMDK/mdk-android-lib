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

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoQuery;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.MParametersDao;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.MParametersField;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlDelete;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.factory.MParametersFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MParameters;

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
		Map<String, Long> r_mapSyncTimestamp = new HashMap<String, Long>();

		try {
			MParametersDao oParametersDao = BeanLoader.getInstance().getBean(MParametersDao.class);
			
			DaoQuery oDaoQuery = oParametersDao.getSelectDaoQuery();
			oDaoQuery.getSqlQuery().addLikeConditionToWhere(MParametersField.NAME, MParametersDao.ALIAS_NAME,
					SYNCHRO_TIMESTAMP_PREFIX + "%", Types.VARCHAR);
			List<MParameters> listParameters = oParametersDao.getListMParameters(oDaoQuery, p_oContext);
			for (MParameters oParameter : listParameters) {
				r_mapSyncTimestamp.put(oParameter.getName().substring(SYNCHRO_TIMESTAMP_PREFIX.length()),
						Long.parseLong(oParameter.getValue()));
			}
		} catch (DaoException e) {
			throw new SynchroException(e);
		}
		return r_mapSyncTimestamp;
	}
	
	/** {@inheritDoc} */
	@Override
	public void saveSyncTimestamps( Map<String,Long> p_mapSyncTimestamp, MContext p_oContext ) throws SynchroException {
		
		try {
			MParametersDao oParametersDao = BeanLoader.getInstance().getBean(MParametersDao.class);
			MParametersFactory oParametersFactory = BeanLoader.getInstance().getBean(MParametersFactory.class);
			
			DaoQuery oDaoQuery = oParametersDao.getSelectDaoQuery();
			oDaoQuery.getSqlQuery().addLikeConditionToWhere(
					MParametersField.NAME, MParametersDao.ALIAS_NAME, SYNCHRO_TIMESTAMP_PREFIX + "%" , Types.VARCHAR);
			
			List<MParameters> listParameters = oParametersDao.getListMParameters(oDaoQuery, p_oContext);	
			Map<String,MParameters> mapExistingParameters = new HashMap<String,MParameters>();
			for( MParameters oParameter : listParameters ) {
				mapExistingParameters.put(oParameter.getName().substring(SYNCHRO_TIMESTAMP_PREFIX.length()), oParameter);	
			}
			
			
			List<MParameters> listParametersToUpdate = new ArrayList<MParameters>();
			for( Entry<String,Long> oEntry: p_mapSyncTimestamp.entrySet()) {
				MParameters oParameter = mapExistingParameters.get(oEntry.getKey());
				if ( oParameter == null ) {
					oParameter = oParametersFactory.createInstance();
					oParameter.setName( SYNCHRO_TIMESTAMP_PREFIX + oEntry.getKey());
				}
				oParameter.setValue(Long.toString(oEntry.getValue()));
				
				Application.getInstance().getLog().error("synchro", "  " + oParameter.getName().substring(SYNCHRO_TIMESTAMP_PREFIX.length())
						+ " : " + Long.parseLong(oParameter.getValue()) + ", id = " + oParameter.getId());
				listParametersToUpdate.add(oParameter);
			}
					
			oParametersDao.saveOrUpdateListMParameters(listParametersToUpdate, p_oContext );
		} catch (DaoException e) {
			throw new SynchroException(e);
		}
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
		MParametersDao oParametersDao = BeanLoader.getInstance().getBean(MParametersDao.class);

		SqlDelete oDelete = oParametersDao.getDeleteQuery();
		oDelete.addEqualsConditionToWhere(MParametersField.NAME, SYNCHRO_TIMESTAMP_PREFIX + p_sKey, Types.VARCHAR);
		
		try {
			oParametersDao.genericDelete(oDelete, p_oContext);
		} catch (DaoException e) {
			throw new SynchroException(e);
		}
	}
}
