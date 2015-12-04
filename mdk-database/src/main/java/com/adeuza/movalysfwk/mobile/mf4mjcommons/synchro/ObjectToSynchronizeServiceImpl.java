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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoQuery;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.MObjectToSynchronizeDao;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.MObjectToSynchronizeDaoImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.MObjectToSynchronizeField;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MObjectToSynchronize;

/**
 * <p>MObjectToSynchronizeDaoImpl class.</p>
 *
 */
public class ObjectToSynchronizeServiceImpl implements ObjectToSynchronizeService {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, List<MObjectToSynchronize>> getMapObjectToSynchronize(MContext p_oContext)
			throws SynchroException {

		MObjectToSynchronizeDao oMObjectToSynchronizeDao = BeanLoader.getInstance().getBean(
				MObjectToSynchronizeDao.class);

		Map<String, List<MObjectToSynchronize>> r_mapCurrentObjectsToSynchronize = new HashMap<String, List<MObjectToSynchronize>>();

		List<MObjectToSynchronize> listObjectToSynchronize = null;
		
		try {
			listObjectToSynchronize = oMObjectToSynchronizeDao
					.getListMObjectToSynchronize(p_oContext);
		} catch (DaoException e) {
			throw new SynchroException(e);
		}

		// Construct map with object to synchronize (key is entity type)
		for (MObjectToSynchronize oObjectToSync : listObjectToSynchronize) {
			List<MObjectToSynchronize> listSyncObjets = r_mapCurrentObjectsToSynchronize.get(oObjectToSync
					.getObjectName());
			if (listSyncObjets == null) {
				listSyncObjets = new ArrayList<MObjectToSynchronize>();
				r_mapCurrentObjectsToSynchronize.put(oObjectToSync.getObjectName(), listSyncObjets);
			}
			listSyncObjets.add(oObjectToSync);
		}

		return r_mapCurrentObjectsToSynchronize;
	}

	
	/** {@inheritDoc} */
	@Override
	public boolean isSynchronized( String p_sObjectClass, String p_sValue, MContext p_oContext) throws SynchroException {
		
		MObjectToSynchronizeDao oMObjectToSynchronizeDao = BeanLoader.getInstance().getBean(
				MObjectToSynchronizeDao.class);
		
		DaoQuery oQuery = oMObjectToSynchronizeDao.getSelectDaoQuery();
		oQuery.getSqlQuery().addEqualsConditionToWhere(
			MObjectToSynchronizeField.OBJECTNAME, MObjectToSynchronizeDao.ALIAS_NAME, p_sObjectClass, Types.VARCHAR);
		oQuery.getSqlQuery().addEqualsConditionToWhere(
				MObjectToSynchronizeField.OBJECTID, MObjectToSynchronizeDao.ALIAS_NAME, p_sValue, Types.VARCHAR);
		
		List<MObjectToSynchronize> lObjects = null;
		
		try {
			lObjects = oMObjectToSynchronizeDao.getListMObjectToSynchronize(oQuery, p_oContext);
		} catch (DaoException e) {
			throw new SynchroException(e);
		}
		
		return lObjects.isEmpty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteObjectToSynchronize( List<MObjectToSynchronize> p_listObjectToSynchronize,
			MContext p_oContext) throws SynchroException {

		MObjectToSynchronizeDao oMObjectToSynchronizeDao = BeanLoader.getInstance().getBean(
				MObjectToSynchronizeDao.class);
		
		try {
			oMObjectToSynchronizeDao.deleteListMObjectToSynchronize(p_listObjectToSynchronize, p_oContext);
		} catch (DaoException e) {
			throw new SynchroException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteObjectToSynchronizeByObjectIdAndObjectName(List<MObjectToSynchronize> p_listObjectToSynchronize,
			MContext p_oContext) throws SynchroException {
		try {
			BeanLoader.getInstance().getBean(MObjectToSynchronizeDao.class)
					.deleteListMObjectToSynchronizeByObjectIdAndObjectName(p_listObjectToSynchronize, p_oContext);
		} catch (DaoException e) {
			throw new SynchroException(e);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean isThereDataToSynchronize(MContext p_oContext) throws SynchroException {
		List<MObjectToSynchronize> oList = null;
		try {
			oList = BeanLoader.getInstance().getBean(MObjectToSynchronizeDao.class)
					.getListMObjectToSynchronize(p_oContext);
		} catch (DaoException e) {
			throw new SynchroException(e);
		}
		return !oList.isEmpty();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.AbstractMObjectToSynchronizeDaoImpl#saveOrUpdateListMObjectToSynchronize(java.util.Collection, com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.CascadeSet, com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext)
	 */
	@Override
	public void saveOrUpdateListMObjectToSynchronize(Collection<MObjectToSynchronize> p_listMObjectToSynchronize, MContext p_oContext) throws SynchroException {

		if (p_listMObjectToSynchronize != null && !p_listMObjectToSynchronize.isEmpty()) {
			MObjectToSynchronizeDaoImpl oDao = (MObjectToSynchronizeDaoImpl) BeanLoader.getInstance().getBean(MObjectToSynchronizeDao.class);
			
			try {
				oDao.saveOrUpdateListMObjectToSynchronize(p_listMObjectToSynchronize, p_oContext);
			} catch (DaoException e) {
				throw new SynchroException(e);
			}
		}
	}
}
