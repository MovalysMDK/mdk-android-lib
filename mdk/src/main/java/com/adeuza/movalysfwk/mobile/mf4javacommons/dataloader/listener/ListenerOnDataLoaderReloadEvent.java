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
package com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.listener;

import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader;

/**
 * <pType a utiliser lors de la définition de méthode de callback de listener de dataloader</p>
 *
 *
 */
public class ListenerOnDataLoaderReloadEvent<DL extends Dataloader<?>> {

	/** le dataloder appelant la méthode de callback */
	private DL dataLoader;
	
	//XXX CHANGE V3.2
	private String key = Dataloader.DEFAULT_KEY;
	
	/**
	 * Construit un nouveau value object
	 *
	 * @param p_oDataLoader le dataloader appelant la méthode de callback
	 */
	public ListenerOnDataLoaderReloadEvent(DL p_oDataLoader) {
		this.dataLoader = p_oDataLoader;
	}
	
	/**
	 * Construit un nouveau value object
	 *
	 * @param p_oDataLoader le dataloader appelant la méthode de callback
	 * @param p_sKey a {@link java.lang.String} object.
	 */
	//XXX CHANGE V3.2
	public ListenerOnDataLoaderReloadEvent(DL p_oDataLoader, String p_sKey) {
		this.dataLoader = p_oDataLoader;
		this.key  = p_sKey;
	}
	
	/**
	 * Donne le dataloader associé
	 *
	 * @return le dataloader associé
	 */
	public DL getDataLoader() {
		return this.dataLoader;
	}
	
	//XXX CHANGE V3.2
	/**
	 * <p>Getter for the field <code>key</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getKey() {
		return this.key;
	}
}
