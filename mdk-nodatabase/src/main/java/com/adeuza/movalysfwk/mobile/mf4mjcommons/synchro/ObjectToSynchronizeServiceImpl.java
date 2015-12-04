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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
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

		return new HashMap<String, List<MObjectToSynchronize>>();
	}

	
	/** {@inheritDoc} */
	@Override
	public boolean isSynchronized( String p_sObjectClass, String p_sValue, MContext p_oContext) throws SynchroException {
		
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteObjectToSynchronize( List<MObjectToSynchronize> p_listObjectToSynchronize,
			MContext p_oContext) throws SynchroException {
		// nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteObjectToSynchronizeByObjectIdAndObjectName(List<MObjectToSynchronize> p_listObjectToSynchronize,
			MContext p_oContext) throws SynchroException {
		// nothing to do
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean isThereDataToSynchronize(MContext p_oContext) throws SynchroException {
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.AbstractMObjectToSynchronizeDaoImpl#saveOrUpdateListMObjectToSynchronize(java.util.Collection, com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.CascadeSet, com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext)
	 */
	@Override
	public void saveOrUpdateListMObjectToSynchronize(Collection<MObjectToSynchronize> p_listMObjectToSynchronize, MContext p_oContext) throws SynchroException {
		// nothing to do
	}
}
