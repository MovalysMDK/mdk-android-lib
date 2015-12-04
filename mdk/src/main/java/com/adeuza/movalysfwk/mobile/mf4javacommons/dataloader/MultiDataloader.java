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

import java.util.Map;
import java.util.Set;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>MultiDataloader interface.</p>
 *
 * @param <RESULT> le type de résultat attendu lors du chargement des données
 */
public interface MultiDataloader<RESULT> {

	/**
	 * pop the current reload arguments
	 *
	 * @param p_sKey the key to pop in the dataloader
	 * @return the current reload arguments
	 */
	public Set<DataLoaderParts> popReload(String p_sKey);

	/**
	 * Reload the dataloader for the specified key
	 * <p>this method hide to the developers the complexity of a business event send to the Activity and the action following.</p>
	 *
	 * @param p_sKey the key to reload in the dataloader
	 * @param p_oReload the reload parameters
	 */
	public void reload(String p_sKey, DataLoaderParts... p_oReload);
	
	/**
	 * Recharge les données : celles-ci sont mises à disposition par la méthode getData. Cette méthode réalise la notification des listeners.
	 *
	 * @param p_oContext the context
	 * @param p_sKey the key to reload
	 * @param p_oReload the reload parameters
	 * @throws com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataloaderException en cas de problème de chargement une com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataloaderException est lancée
	 */
	void reload(MContext p_oContext, String p_sKey, DataLoaderParts... p_oReload) throws DataloaderException;
	
	/**
	 * Recharge les données : celles-ci sont mises à disposition par la méthode getData. Cette méthode réalise la notification des listeners.
	 *
	 * @param p_oContext the context
	 * @param p_sKey the key to reload
	 * @param p_bNotify notify
	 * @param p_oReload the reload parameters
	 * @throws com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataloaderException en cas de problème de chargement une com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataloaderException est lancée
	 */
	void reload(MContext p_oContext, String p_sKey, boolean p_bNotify, DataLoaderParts... p_oReload) throws DataloaderException;
	
	/**
	 * Recharge toutes les données pour toutes les clés du dataloader
	 *
	 * @param p_oContext le context
	 * @throws com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataloaderException en cas de problème de chargement une com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataloaderException est lancée
	 */
	void reloadAll(MContext p_oContext) throws DataloaderException;
	
	/**
	 * Notify by an event of the change in data loader
	 *
	 * @param p_oContext the context
	 * @param p_sKey the key to notify
	 */
	public void notifyDataLoaderReload( MContext p_oContext, String p_sKey );
	
	/**
	 * Donne les données chargées par le loader pour la clé specifiée.
	 *
	 * @param p_sKey the key to get data
	 * @return les données chargées par le loader
	 */
	public RESULT getData(String p_sKey);

	/**
	 * Add a data to the specified key
	 *
	 * @param p_sKey the key to add data
	 * @param p_oData the data to add in the dataloader
	 */
	public void addData(String p_sKey, final RESULT p_oData);

	/**
	 * Add the item id of the specified key
	 *
	 * @param p_sKey the key to add/change id
	 * @param p_lItemId new item id
	 */
	void addItemId(String p_sKey, long p_lItemId);
	
	/**
	 * Add the options for the specified key
	 *
	 * @param p_sKey the key to specify
	 * @param p_mapOptions new options
	 */
	public void addOptions(String p_sKey, Map<String, Object> p_mapOptions);

	
}
