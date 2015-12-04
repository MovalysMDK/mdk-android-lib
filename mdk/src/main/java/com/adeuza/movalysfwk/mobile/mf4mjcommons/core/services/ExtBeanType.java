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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services;


/**
 * <p>Enumération permettant au BeanLoader de charger un bean en fonction d'un mot clé.</p>
 *
 *
 * @since Annapurna
 */
public enum ExtBeanType implements BeanType {

	/** mot-clé pour le chargement d'un bean application */
	Application("application"),
	/** mot-clé pour le chargement d'un bean controller */
	Controller("controller"),
	/** key for load view model creator */
	ViewModelCreator("viewmodelcreator"),
	/** mot-clé pour le chargement d'un bean transfer */
	Transfer("transfer"),
	/** mot-clé pour le chargement d'un bean d'action de synchronisation */
	SynchronizationCall("synchronizationCall"),
	/** mot-clé pour le chargement d'un bean lié aux champs personnalisés */
	CustomField("customFields"),
	/** mot-clé utilisé pour déterminer la MMActionTask dédiée aux lancements synchrones d'actions */
	SyncAction("SyncAction"),
	/** mot-clé utilisé pour déterminer la MMActionTask dédiée aux lancements asynchrones d'actions */
	ASyncAction("ASyncAction");
	
	/** mot-clé identifiant le bean à charger. */
	private String name = null;

	/**
	 * Construit un objet <em>ExtBeanType</em>.
	 * @param p_sName le mot-clé identifiant le bean à charger.
	 */
	private ExtBeanType(String p_sName) {
		this.name = p_sName;
	}
	
	/** {@inheritDoc} */
	@Override
	public String getName() {
		return this.name;
	}
}
