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
import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MObjectToSynchronize;

/**
 * <p>ObjectToSynchronizeService interface.</p>
 *
 */
public interface ObjectToSynchronizeService {

	/**
	 * <p>getMapObjectToSynchronize.</p>
	 *
	 * @param p_oContext a com$adeuza$movalysfwk$mobile$mf4mjcommons$context$MContext object.
	 * @throws SynchroException if any.
	 * @return a {@link java.util.Map} object.
	 */
	public Map<String, List<MObjectToSynchronize>> getMapObjectToSynchronize(MContext p_oContext)
			throws SynchroException;

	/**
	 * <p>isSynchronized.</p>
	 *
	 * @param p_sObjectClass a {@link java.lang.String} object.
	 * @param p_sValue a {@link java.lang.String} object.
	 * @param p_oContext a com$adeuza$movalysfwk$mobile$mf4mjcommons$context$MContext object.
	 * @throws SynchroException if any.
	 * @return a boolean.
	 */
	public boolean isSynchronized( String p_sObjectClass, String p_sValue, MContext p_oContext) throws SynchroException;
	
	/**
	 * <p>deleteObjectToSynchronize.</p>
	 *
	 * @param p_oContext a com$adeuza$movalysfwk$mobile$mf4mjcommons$context$MContext object.
	 * @param p_listObjectToSynchronize a {@link java.util.List} object.
	 * @throws SynchroException if any.
	 */
	public void deleteObjectToSynchronize(
			List<MObjectToSynchronize> p_listObjectToSynchronize, MContext p_oContext)
			throws SynchroException;

	/**
	 * <p>deleteObjectToSynchronizeByObjectIdAndObjectName.</p>
	 *
	 * @param p_listObjectToSynchronize a {@link java.util.List} object.
	 * @param p_oContext a com$adeuza$movalysfwk$mobile$mf4mjcommons$context$MContext object.
	 * @throws SynchroException if any.
	 */
	public void deleteObjectToSynchronizeByObjectIdAndObjectName(
			List<MObjectToSynchronize> p_listObjectToSynchronize,
			MContext p_oContext) throws SynchroException;
	
	/**
	 * <p>
	 * 	Method that checks if there are changes to be synchronized with the Back Office.
	 * </p>
	 *
	 * @param p_oContext
	 * 				The application context.
	 * @return true if we must synchronize, false otherwise.
	 * @throws SynchroException if any
	 */
	public boolean isThereDataToSynchronize(MContext p_oContext) throws SynchroException ;
	
	/**
	 * Sauve ou met à jour laliste d'entité MObjectToSynchronize passée en paramètre selon leur existence en base.
	 *
	 * La cascade est CascadeSet.NONE
	 *
	 * @param p_listMObjectToSynchronize a list of MObjectToSynchronize
	 * @param p_oContext context to use
	 * @throws SynchroException if any
	 */
	public void saveOrUpdateListMObjectToSynchronize(Collection<MObjectToSynchronize> p_listMObjectToSynchronize, MContext p_oContext)
			throws SynchroException;
}
