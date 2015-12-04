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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.adeuza.movalysfwk.mobile.mf4javacommons.event.AbstractBusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * Class outil de dataloader
 *
 * <p>Par défaut, les méthodes de DataLoader qui sont dans MultiDataLoader (avec une clé en
 * paramètre) appellent ces méthodes avec la valeur DEFAULT_KEY.</p>
 * 
 * <p>Dans les méthodes, le paramètre d'entrée "p_sKey" permet de gérer les données métiers en fonction de cette clé.</p>
 *
 * @param <RESULT> le type de résultat attendu lors du chargement des données
 */
public abstract class AbstractDataloader<RESULT> implements MMDataloader<RESULT> {
	
	/** Map Item id */
	private Map<String, Long> itemIds;
	
	/** Options parameters */
	private Map<String, Map<String, Object>> mOptions;
	
	/** Le résultat du loader */
	private Map<String, RESULT> mDatas = null;

	/** Reload */
	private Map<String,Set<DataLoaderParts>> mReload = null;
	
	public static class ReloadLoaderBusinessEvent extends AbstractBusinessEvent<DataLoaderParts[]> {

		private Class<? extends MMDataloader<?>> oDataLoaderClass;
		private String key = DEFAULT_KEY;

		public ReloadLoaderBusinessEvent(Object p_oSource, DataLoaderParts[] p_oReload) {
			super(p_oSource, p_oReload);
		}

		public Class<? extends MMDataloader<?>> getDataLoaderClass() {
			return this.oDataLoaderClass;
		}

		public void setDataLoaderClass(Class<? extends MMDataloader<?>> p_oDataLoaderClass) {
			this.oDataLoaderClass = p_oDataLoaderClass;
		}
		
		public String getKey() {
			return this.key;
		}
		
		public void setKey(String p_sKey) {
			this.key = p_sKey;
		}
	}
	
	/**
	 * Construction du loader
	 */
	public AbstractDataloader() {
		this.itemIds = new HashMap<String, Long>();
		this.mOptions = new HashMap<String, Map<String, Object>>();
		this.mDatas = new HashMap<String, RESULT>();
		this.mReload = new HashMap<String, Set<DataLoaderParts>>();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @deprecated use {@link #popReload(String p_sKey)} instead. This implementation use popReload(String p_sKey) with a default key
	 */
	@Deprecated
	@Override
	public Set<DataLoaderParts> popReload() {
		return this.popReload(DEFAULT_KEY);
	}

	/** {@inheritDoc} */
	@Override
	public Set<DataLoaderParts> popReload(String p_sKey) {
		// this is on propose :
		// we need to clear the current attribute instance and send its old value
		HashSet<DataLoaderParts> r_oSet = new HashSet<>(this.mReload.get(p_sKey));
		this.mReload.get(p_sKey).clear();
		return r_oSet;
	}
	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated use {@link #getData(String p_sKey)} instead. This implementation use getData(String p_sKey) with a default key
	 */
	@Deprecated
	@Override
	public RESULT getData() {
		return this.getData(DEFAULT_KEY);
	}
	
	/** {@inheritDoc} */
	@Override
	public RESULT getData(String p_sKey) {
		return this.mDatas.get(p_sKey);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader#setData(java.lang.Object)
	 * 
	 * @deprecated use {@link #addData(String p_sKey, final RESULT p_oData)} instead. This implementation use addData(String p_sKey, final RESULT p_oData) with a default key
	 */
	@Deprecated
	@Override
	public final void setData(final RESULT p_oData) {
		this.addData(DEFAULT_KEY, p_oData);
	}
	
	/** {@inheritDoc} */
	@Override
	public void addData(String p_sKey, RESULT p_oData) {
		this.mDatas.put(p_sKey, p_oData);
	}
	
	/** {@inheritDoc} 
	 * 
	 * @deprecated use {@link #reload(String p_sKey, DataLoaderParts... p_oReload)} instead. This implementation use reload(String p_sKey, DataLoaderParts... p_oReload) with a default key
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Deprecated
	@Override
	public void reload(final DataLoaderParts... p_oReload) {
		reload(DEFAULT_KEY, p_oReload);
	}
	
	/** {@inheritDoc} */
	@Override
	public void reload(String p_sKey, DataLoaderParts... p_oReload) {
		ReloadLoaderBusinessEvent oEvent = new ReloadLoaderBusinessEvent(this, p_oReload);
		// get interface of DataLoaderImpl
		for (Class oInterface: this.getClass().getInterfaces()) {
			if (Dataloader.class.isAssignableFrom(oInterface) || MultiDataloader.class.isAssignableFrom(oInterface)) {
				oEvent.setDataLoaderClass((Class<? extends MMDataloader<?>>) oInterface);
				oEvent.setKey(p_sKey);
				break;
			}
		}
		Application.getInstance().getController().publishBusinessEvent(null, oEvent);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated use {@link #reload(MContext p_oContext, String p_sKey, DataLoaderParts... p_oReload)} instead. This implementation use reload(MContext p_oContext, String p_sKey, DataLoaderParts... p_oReload) with a default key
	 */
	@Deprecated
	@Override
	public void reload(MContext p_oContext, DataLoaderParts... p_oReload) throws DataloaderException {
		this.reload(p_oContext, true, p_oReload);
	}
	
	/** {@inheritDoc} */
	@Override
	public void reload(MContext p_oContext, String p_sKey, DataLoaderParts... p_oReload) throws DataloaderException {
		this.reload(p_oContext,p_sKey, true, p_oReload);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @deprecated use {@link #reload(MContext p_oContext, String p_sKey, boolean p_bNotify, DataLoaderParts... p_oReload)} instead. This implementation use (MContext p_oContext, String p_sKey, boolean p_bNotify, DataLoaderParts... p_oReload) with a default key
	 */
	@Deprecated
	@Override
	public void reload(MContext p_oContext, boolean p_bNotify, DataLoaderParts... p_oReload) throws DataloaderException {
		this.reload(p_oContext, DEFAULT_KEY, p_bNotify, p_oReload);
	}
	
	/** {@inheritDoc} */
	@Override
	public void reload(MContext p_oContext, String p_sKey, boolean p_bNotify, DataLoaderParts... p_oReload) throws DataloaderException {
		if (this.mReload.get(p_sKey) == null) {
			this.addItemId(p_sKey, -1);
		}
		if (p_oReload == null || p_oReload.length == 0) {
			this.mReload.get(p_sKey).addAll(Arrays.asList(getAllReload()));
		} else {
			this.mReload.get(p_sKey).addAll(Arrays.asList(p_oReload));
		}
		this.mDatas.put(p_sKey, this.load(p_oContext, p_sKey, this.mReload.get(p_sKey)));
		
		if ( p_bNotify ) {
			this.notifyDataLoaderReload(p_oContext, p_sKey);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void reloadAll(MContext p_oContext) throws DataloaderException {
		for (String key : mDatas.keySet()) {
			this.reload(p_oContext, key);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader#notifyDataLoaderReload(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext)
	 * 
	 * @deprecated use {@link #notifyDataLoaderReload( MContext p_oContext, String p_sKey )} instead. This implementation use notifyDataLoaderReload( MContext p_oContext, String p_sKey ) with a default key
	 */
	@Deprecated
	@Override
	public void notifyDataLoaderReload( MContext p_oContext) {
		this.notifyDataLoaderReload(p_oContext, DEFAULT_KEY);
	}
	
	/** {@inheritDoc} */
	@Override
	public void notifyDataLoaderReload(MContext p_oContext, String p_sKey) {
		Application.getInstance().getController().notifyDataLoaderReload(p_oContext, p_sKey, this);
	}
	
	/**
	 * <p>getAllReload.</p>
	 *
	 * @return an array of {@link com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataLoaderParts} objects.
	 */
	protected abstract DataLoaderParts[] getAllReload();
	
	/**
	 * Charge les données.
	 *
	 * @param p_oContext le context à utiliser
	 * @param p_lKey a {@link java.lang.String} object.
	 * @param p_oReload a {@link java.util.Set} object.
	 * @return les données chargées
	 * @throws com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataloaderException if any.
	 */
	protected abstract RESULT load(MContext p_oContext, String p_lKey, Set<DataLoaderParts> p_oReload) throws DataloaderException;
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader#setItemId(long)
	 * 
	 * @deprecated use {@link #addItemId(String p_sKey, final long p_lItemId)} instead. This implementation use addItemId(String p_sKey, final long p_lItemId) with a default key
	 */
	@Deprecated
	@Override
	public void setItemId(final long p_lItemId) {
		this.addItemId(DEFAULT_KEY, p_lItemId);
	}
	
	/** {@inheritDoc} */
	@Override
	public void addItemId(String p_sKey, long p_lItemId) {
		this.itemIds.put(p_sKey, p_lItemId);
		// add all maps linked to the id
		if (!this.mDatas.containsKey(p_sKey)) {
			this.mDatas.put(p_sKey, null);
		}
		if (!this.mOptions.containsKey(p_sKey)) {
			this.mOptions.put(p_sKey, null);
		}
		if (!this.mReload.containsKey(p_sKey)) {
			this.mReload.put(p_sKey, new HashSet<DataLoaderParts>());
		}
	}
	
	/**
	 * Return item id
	 * 
	 * @deprecated use {@link #getItemId(String p_sKey)} instead. This implementation use getItemId(String p_sKey) with a default key 
	 * @return item id
	 */
	@Deprecated
	protected long getItemId() {
		return this.getItemId(DEFAULT_KEY);
	}
	
	/**
	 * Return item id
	 *
	 * @return item id
	 * @param p_sKey a {@link java.lang.String} object.
	 */
	protected long getItemId(String p_sKey) {
		return this.itemIds.get(p_sKey);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader#setOptions(java.util.Map)
	 * 
	 * @deprecated use {@link #addOptions(String p_sKey, Map<String, Object> p_mapOptions)} instead. This implementation use addOptions(String p_sKey, Map<String, Object> p_mapOptions) with a default key
	 */
	@Deprecated
	@Override
	public void setOptions(Map<String, Object> p_mapOptions) {
		this.addOptions(DEFAULT_KEY, p_mapOptions);
	}
	
	/** {@inheritDoc} */
	@Override
	public void addOptions(String p_sKey, Map<String, Object> p_mapOptions) {
		this.mOptions.put(p_sKey, p_mapOptions);
	}
	
	/**
	 * Retrieve option by the key or the default value
	 *
	 * @deprecated use {@link #getOption(String p_sKey, String p_sOptionKey, T p_oDefaultValue)} instead. This implementation use getOption(String p_sKey, String p_sOptionKey, T p_oDefaultValue) with a default key
	 * 
	 * @param p_sOptionKey searched key
	 * @param p_oDefaultValue valeur returned if no data for the key
	 * @param <T> a T object.
	 * @return a T object.
	 */
	@Deprecated
	protected <T> T getOption(String p_sOptionKey, T p_oDefaultValue) {
		return getOption(DEFAULT_KEY, p_sOptionKey, p_oDefaultValue);
	}
	
	/**
	 * <p>getOption.</p>
	 *
	 * @param p_sKey a {@link java.lang.String} object.
	 * @param p_sOptionKey a {@link java.lang.String} object.
	 * @param p_oDefaultValue a T object.
	 * @param <T> a T object.
	 * @return a T object.
	 */
	protected <T> T getOption(String p_sKey, String p_sOptionKey, T p_oDefaultValue) {
		Object oOptionValue = null ;
		if (this.mOptions.get(p_sKey) != null){
			oOptionValue = this.mOptions.get(p_sKey).get(p_sOptionKey);
		}
		if (oOptionValue == null) {
			return p_oDefaultValue;
		}
		else {
			return (T) oOptionValue;
		}
	}
}
