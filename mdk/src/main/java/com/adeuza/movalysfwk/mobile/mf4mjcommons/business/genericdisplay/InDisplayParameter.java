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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.business.genericdisplay;

import java.util.HashMap;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataLoaderParts;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.MMDataloader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;

/**
 * <p>InDisplayParameter class.</p>
 *
 */
public class InDisplayParameter extends AbstractActionParameter implements ActionParameter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1499713852088844269L;
	
	/** l'id de l'élément à afficher */
	private String id = "-1";

	private Map<String, Object> options = new HashMap<String, Object>();
	
	/** le dataloader à utiliser */
	//XXX CHANGE V3.2
	private Class<? extends MMDataloader<?>> dataLoader = null;
	
	/** reload params */
	private DataLoaderParts[] reloadParams = null;
	
	//XXX CHANGE V3.2
	private String sKey = Dataloader.DEFAULT_KEY;
	
	/**
	 * <p>Constructor for InDisplayParameter.</p>
	 */
	public InDisplayParameter() {
		super();
	}
	
	
	//XXX CHANGE V3.2
	/**
	 * <p>Constructor for InDisplayParameter.</p>
	 *
	 * @param p_sId a {@link java.lang.String} object.
	 * @param p_oDataLoader a {@link java.lang.Class} object.
	 */
	public InDisplayParameter( String p_sId, Class<? extends MMDataloader<?>> p_oDataLoader ) {
		super();
		this.id = p_sId ;
		this.dataLoader = p_oDataLoader;
	}
	
	/**
	 * Modifieur de l'id
	 *
	 * @param p_oId new id
	 */
	public void setId(String p_oId) {
		this.id = p_oId;
	}
	/**
	 * Modifieur du data loader
	 *
	 * @param p_oDataLoader new data loader
	 */
	//XXX CHANGE V3.2
	public void setDataLoader(Class<? extends MMDataloader<?>> p_oDataLoader) {
		this.dataLoader = p_oDataLoader;
	}
	/**
	 * Modifier des options
	 *
	 * @param p_oOptions new options
	 */
	public void setOptions(Map<String, Object> p_oOptions) {
		this.options = p_oOptions;
	}

	/**
	 * Modifier for reload options
	 *
	 * @param p_oReload new reload options
	 */
	public void setReload(DataLoaderParts... p_oReload) {
		this.reloadParams = p_oReload.clone();
	}
	
	/**
	 * Accesseur de l'id
	 *
	 * @return identifiant
	 */
	public String getId() {
		return this.id;
	}
	/**
	 * Accesseur du dataloader
	 *
	 * @return le data loader
	 */
	//XXX CHANGE V3.2
	public Class<? extends MMDataloader<?>> getDataLoader() {
		return this.dataLoader;
	}
	/**
	 * Accesseur des options
	 *
	 * @return options
	 */
	public Map<String, Object> getOptions() {
		return this.options;
	}
	/**
	 * Accesseur de l'id
	 *
	 * @param p_sTring a {@link java.lang.String} object.
	 * @param p_oBject a {@link java.lang.Object} object.
	 */
	public void addOption( String p_sTring,Object p_oBject){
		this.options.put(p_sTring, p_oBject);
	}
	
	/**
	 * Getter for reload options
	 *
	 * @return an array of {@link com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataLoaderParts} objects.
	 */
	public DataLoaderParts[] getReload() {
		return this.reloadParams;
	}

	//XXX CHANGE V3.2
	/**
	 * <p>getKey.</p>
	 *
	 * @return the lKey
	 */
	public String getKey() {
		return this.sKey;
	}

	/**
	 * <p>setKey.</p>
	 *
	 * @param p_sKey a {@link java.lang.String} object.
	 */
	public void setKey(String p_sKey) {
		this.sKey = p_sKey;
	}

}
