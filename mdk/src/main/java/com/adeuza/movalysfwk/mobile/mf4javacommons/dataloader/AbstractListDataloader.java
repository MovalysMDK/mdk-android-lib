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
package com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * Dataloader spécialisé dans le traitement des listes
 *
 * @param <ITEMRESULT> le type de données contenu dans la liste
 * @param <IN> le type du paramètre d'entrée du chargement
 */
public abstract class AbstractListDataloader<ITEMRESULT extends MIdentifiable> extends AbstractDataloader<List<ITEMRESULT>> {

	//XXX CHANGE V3.2
	Map<String, Map<String, ITEMRESULT>> itemsById;

	/**
	 * Default constructor
	 */
	public AbstractListDataloader() {
		//XXX CHANGE V3.2
		this.itemsById = new HashMap<String, Map<String, ITEMRESULT>>();
		this.itemsById.put(DEFAULT_KEY, new HashMap<String, ITEMRESULT>());
	}

	//XXX CHANGE V3.2
	/** {@inheritDoc} */
	@Override
	public void addItemId(String p_sKey, long p_lItemId) {
		super.addItemId(p_sKey, p_lItemId);
		this.itemsById.put(p_sKey, new HashMap<String, ITEMRESULT>());
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.AbstractDataloader#reload(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, boolean)
	 */
	//XXX CHANGE V3.2
	@Override
	public void reload(MContext p_oContext, boolean p_bNotify, DataLoaderParts... p_oReload) throws DataloaderException {
		this.reload(p_oContext, DEFAULT_KEY, p_bNotify, p_oReload);
	}
	
	//XXX CHANGE V3.2
	/** {@inheritDoc} */
	@Override
	public void reload(MContext p_oContext, String p_sKey, boolean p_bNotify, DataLoaderParts... p_oReload) throws DataloaderException {
		this.itemsById.get(p_sKey).clear();
		super.reload(p_oContext, p_sKey, p_bNotify, p_oReload);
		if (this.getData(p_sKey) != null) {
			for (ITEMRESULT oItem : this.getData(p_sKey)) {
				this.itemsById.get(p_sKey).put(oItem.idToString(), oItem);
			}
		}
	}

	/**
	 * Get an item by its key.
	 * @deprecated use getItem(p_sKey, p_sItemId) instead
	 * @param p_sItemId a {@link java.lang.String} object.
	 * @return a ITEMRESULT object.
	 */
	@Deprecated
	public final ITEMRESULT getItem(final String p_sItemId) {
		//XXX CHANGE V3.2
		return this.getItem(DEFAULT_KEY, p_sItemId);
	}
	
	//XXX CHANGE V3.2
	/**
	 * <p>getItem.</p>
	 *
	 * @param p_sKey a {@link java.lang.String} object.
	 * @param p_sItemId a {@link java.lang.String} object.
	 * @return a ITEMRESULT object.
	 */
	public final ITEMRESULT getItem(String p_sKey, final String p_sItemId) {
		return this.itemsById.get(p_sKey).get(p_sItemId);
	}
	
}
