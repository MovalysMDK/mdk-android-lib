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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>Class into the different descriptors of components.</p>
 *
 *
 */
public final class VisualComponentDescriptorsHandler {

	/** instance of handler */
	private static VisualComponentDescriptorsHandler instance = null;	
	
	/**
	 * Returns an instance of handler
	 *
	 * @return a unique instance
	 */
	public static VisualComponentDescriptorsHandler getInstance() {
		if (instance == null) {
			instance = new VisualComponentDescriptorsHandler();
		}
		return instance;
	}
	
	/** map of theoritical descriptors */
	private Map<String, TheoriticalVisualComponentDescriptor> theoriticalDescriptors = null;
	/** map of real descriptors */
	private Map<String, RealVisualComponentDescriptor> realDescriptors = null;
	
	/**
	 * Constructs a new handler
	 */
	private VisualComponentDescriptorsHandler() {
		this.theoriticalDescriptors = new HashMap<String, TheoriticalVisualComponentDescriptor>();
		this.realDescriptors = new HashMap<String, RealVisualComponentDescriptor>();
	}
	
	/**
	 * Add a theoritical descriptor
	 *
	 * @param p_sName the name of descritor
	 * @param p_oDescriptor the descriptor to add
	 */
	public void addTheoriticalDescriptor(String p_sName, TheoriticalVisualComponentDescriptor p_oDescriptor) {
		this.theoriticalDescriptors.put(p_sName, p_oDescriptor);
	}
	
	/**
	 * Returns a theoritical descriptor
	 *
	 * @param p_sKey the name of theoritical descriptor to find
	 * @return a theoritical descriptor
	 */
	protected TheoriticalVisualComponentDescriptor getTheoriticalDescriptor(String p_sKey) {
		return this.theoriticalDescriptors.get(p_sKey);
	}
	
	/**
	 * Returns a real descriptor
	 *
	 * @param p_sKey the name of real descriptor to find
	 * @return a real descriptor
	 */
	public RealVisualComponentDescriptor getRealDescriptor(String p_sKey) {
		return this.realDescriptors.get(p_sKey);
	}

	/**
	 * Unlink all theoritical descriptors
	 */
	public void unlink() {
		// "deliaison des elements"
		// attention les deux boucles ne peuvent être fait en même temps
		Iterator<Entry<String, TheoriticalVisualComponentDescriptor>> oIt = this.theoriticalDescriptors.entrySet().iterator();
		while(oIt.hasNext()) {
			Entry<String, TheoriticalVisualComponentDescriptor> oEntry = oIt.next();
			oEntry.getValue().unmanager();
		}
		oIt = this.theoriticalDescriptors.entrySet().iterator();
		while(oIt.hasNext()) {
			Entry<String, TheoriticalVisualComponentDescriptor> oEntry = oIt.next();
			oEntry.getValue().unlink();
		}
		
	}
	
	/**
	 * Initializes and recalculate all real descriptors, each configuration change must start this method
	 */
	public void resetAndComputeRealDescriptors() {
		Iterator<Entry<String, TheoriticalVisualComponentDescriptor>> oIt = this.theoriticalDescriptors.entrySet().iterator();
		realDescriptors.clear();
		RealVisualComponentDescriptor oRcd = null;
		Entry<String, TheoriticalVisualComponentDescriptor> oEntry = null;
		while(oIt.hasNext()) {
			oEntry = oIt.next();
			if (oEntry.getValue().isMaster()) {
				oRcd = new RealVisualComponentDescriptor(oEntry.getValue());
				oRcd.linkConfiguration();
				realDescriptors.put(oEntry.getKey(), oRcd);
				// le nom du composant est normalement le même que le nom du layout
				// si ce n'est pas le cas, on ajoute également une entrée avec le nom du composant
				// (cas des item de spinner sans layout)
				if (!oEntry.getValue().getName().equals(oEntry.getValue().getLayoutFile())) {
					realDescriptors.put(oEntry.getValue().getName(), oRcd);
				}
			}
		}
	}
}
