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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * <p>Defines a contener delegate, a contener stocks object of type TYPE and group them by a key</p>
 *
 *
 * @param <TYPE> the type of object to stock
 */
public class ContenerDelegate<TYPE> implements Serializable {

	/** map to stocks object */
	private HashMap<String, List<TYPE>> contener = null;
	
	/**
	 * Constructs a new contener delegate
	 */
	public ContenerDelegate() {
		contener = new HashMap<String, List<TYPE>>();
	}
	
	/**
	 * Clear the current container
	 */
	public void clear() {
		this.contener.clear();
	}
	
	/**
	 * Add an object and group by key
	 *
	 * @param p_sKey the key to use to group object
	 * @param p_oComponent the object to add
	 */
	public void add(String p_sKey, TYPE p_oComponent) {
		List<TYPE> oList = this.contener.get(p_sKey);
		if (oList ==null) {
			oList = new ArrayList<TYPE>();
			this.contener.put(p_sKey, oList);
		}
		if (!oList.contains(p_oComponent)) {
			oList.add(p_oComponent);
		}
	}
	
	/**
	 * Remove an object
	 *
	 * @param p_sKey the key to use to group object
	 * @param p_oComponent the object to remove
	 * @return a boolean.
	 */
	public boolean remove(String p_sKey, TYPE p_oComponent) {
		boolean r_bRemoved = false ;
		List<TYPE> oList = this.contener.get(p_sKey);
		if (oList!=null) {
			r_bRemoved = oList.remove(p_oComponent);
			if (oList.isEmpty()){
				r_bRemoved = this.contener.remove(p_sKey) != null;
			}
		}
		return r_bRemoved ;
	}
	
	/**
	 * Remove all component of the group
	 *
	 * @param p_sKey the key to use to group object
	 */
	public void remove(String p_sKey) {
		this.contener.remove(p_sKey);
	}
	
	/**
	 * Get an object
	 *
	 * @param p_sKey the key to use to find object
	 * @return the object, or null if there is no entry for that key
	 */
	public List<TYPE> get(String p_sKey) {
		return this.contener.get(p_sKey);
	}
	
	/**
	 * Gets all objects
	 *
	 * @return data in map
	 */
	public  Set<Entry<String, List<TYPE>>> getEntrySet() {
		return this.contener.entrySet();
	}	
	
	/**
	 * return size of the contener
	 *
	 * @return integer
	 */
	public int getSize(){
		return this.contener.size();
	}
}
