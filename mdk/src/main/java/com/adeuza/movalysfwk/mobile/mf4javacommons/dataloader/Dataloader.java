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
 * Permet le chargement de données et la notification d'élément utilisant ces données
 *
 * @param <RESULT> le type de résultat attendu lors du chargement des données
 */
public interface Dataloader<RESULT> {
	//XXX CHANGE V3.2
	/** the default key in case none was specified */
	public static final String DEFAULT_KEY = "DefaultKey";
	
	/**
	 * pop the current reload arguments
	 *
	 * @return the current reload arguments
	 * @deprecated use popReload(String p_sKey) instead. This implementation use popReload(String p_sKey) with a default key
	 */
	//XXX CHANGE V3.2
	@Deprecated
	public Set<DataLoaderParts> popReload();

	/**
	 * Reload the dataloader
	 * <p>this method hide to the developers the complexity of a business event send to the Activity and the action following.</p>
	 *
	 * @param p_oReload the reload parameters
	 * @deprecated use reload(String p_sKey, DataLoaderParts... p_oReload) instead. This implementation use reload(String p_sKey, DataLoaderParts... p_oReload) with a default key
	 */
	//XXX CHANGE V3.2
	@Deprecated
	public void reload(DataLoaderParts... p_oReload);
	
	/**
	 * Recharge les données : celles-ci sont mises à disposition par la méthode getData. Cette méthode réalise la notification des listeners.
	 *
	 * @deprecated use reload(MContext p_oContext, String p_sKey, DataLoaderParts... p_oReload) instead. This implementation use reload(MContext p_oContext, String p_sKey, DataLoaderParts... p_oReload) with a default key
	 * @param p_oContext context
	 * @param p_oReload a {@link com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataLoaderParts} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataloaderException en cas de problème de chargement une com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataloaderException est lancée
	 */
	@Deprecated
	void reload(MContext p_oContext, DataLoaderParts... p_oReload) throws DataloaderException;
	
	/**
	 * Recharge les données : celles-ci sont mises à disposition par la méthode getData. Cette méthode réalise la notification des listeners.
	 *
	 * @param p_bNotify notify
	 * @param p_oContext context.
	 * @param p_oReload a {@link com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataLoaderParts} object.
	 * @deprecated use reload(MContext p_oContext, String p_sKey, boolean p_bNotify, DataLoaderParts... p_oReload) instead. This implementation use (MContext p_oContext, String p_sKey, boolean p_bNotify, DataLoaderParts... p_oReload) with a default key
	 * @throws com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataloaderException en cas de problème de chargement une com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataloaderException est lancée
	 */
	//XXX CHANGE V3.2
	@Deprecated
	void reload(MContext p_oContext, boolean p_bNotify, DataLoaderParts... p_oReload) throws DataloaderException;
	
	/**
	 * Notify by an event of the change in data loader
	 *
	 * @deprecated use notifyDataLoaderReload( MContext p_oContext, String p_sKey ) instead. This implementation use notifyDataLoaderReload( MContext p_oContext, String p_sKey ) with a default key
	 * @param p_oContext context
	 */
	//XXX CHANGE V3.2
	@Deprecated
	public void notifyDataLoaderReload( MContext p_oContext );
	
	/**
	 * Donne les données chargées par le loader
	 *
	 * @return les données chargées par le loader
	 * @deprecated use getData(String p_sKey) instead. This implementation use getData(String p_sKey) with a default key
	 */
	//XXX CHANGE V3.2
	@Deprecated
	public RESULT getData();

	/**
	 * Set the data in this dataloader
	 *
	 * @param p_oData a RESULT object.
	 * @deprecated use addData(String p_sKey, final RESULT p_oData) instead. This implementation use addData(String p_sKey, final RESULT p_oData) with a default key
	 */
	//XXX CHANGE V3.2
	@Deprecated
	public void setData(final RESULT p_oData);

	/**
	 * Modify the item id
	 *
	 * @param p_lItemId new item id
	 * @deprecated use addItemId(String p_sKey, final long p_lItemId) instead. This implementation use addItemId(String p_sKey, final long p_lItemId) with a default key
	 */
	//XXX CHANGE V3.2
	@Deprecated
	public void setItemId(final long p_lItemId);
	
	/**
	 * Modify the options
	 *
	 * @param p_mapOptions new options
	 * @deprecated use addOptions(String p_sKey, Map<String, Object> p_mapOptions) instead. This implementation use addOptions(String p_sKey, Map<String, Object> p_mapOptions) with a default key
	 */
	//XXX CHANGE V3.2
	@Deprecated
	public void setOptions(Map<String, Object> p_mapOptions);

}
