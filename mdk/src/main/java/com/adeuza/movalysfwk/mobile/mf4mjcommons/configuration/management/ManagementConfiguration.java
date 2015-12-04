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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.AbstractConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;

/**
 * <p>Defines a management composant, like the composition of the planning/intervention detail screen</p>
 *
 *
 */
public class ManagementConfiguration extends AbstractConfiguration implements Cloneable {

	/** Version number of this class implementation. */
	private static final long serialVersionUID = 1L;

	/** list of groups (column in screen) */
	private List<ManagementGroup> groups = null;
	/** map of groups for direct access */
	private Map<String, ManagementGroup> groupsByName = null;

	private Map<String, ManagementZone> zonesByName = null;

	/**
	 * Constructs a new management configuration
	 *
	 * @param p_sName the name of configuration
	 */
	public ManagementConfiguration(String p_sName) {
		super(p_sName, ConfigurationsHandler.TYPE_MANAGEMENT);
		this.groups = new ArrayList<ManagementGroup>();
		this.groupsByName = new TreeMap<String, ManagementGroup>();
		this.zonesByName = new TreeMap<String , ManagementZone>();
	}

	/**
	 * Add agroup
	 *
	 * @param p_sGroup the name of group to add
	 * @return the new group
	 */
	public ManagementGroup addGroup(String p_sGroup) {
		ManagementGroup oGroup = this.groupsByName.get(p_sGroup);
		if (oGroup == null) {
			oGroup = new ManagementGroup(p_sGroup);
			this.groups.add(oGroup);
			this.groupsByName.put(p_sGroup, oGroup);
		}
		return oGroup;
	}
	
	/**
	 * Add a zone (a panel in screen)
	 *
	 * @param p_sGroup the name of group's zone
	 * @param p_sZone the name of zone
	 * @param p_sSource the source of zone
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone} object.
	 */
	public ManagementZone addZone(String p_sGroup, String p_sZone, String p_sSource) {
		return addZone(p_sGroup, p_sZone, p_sSource, null);
	}

	/**
	 * Add a zone (a panel in screen)
	 *
	 * @param p_sGroup the name of group's zone
	 * @param p_sZone the name of zone
	 * @param p_sSource the source of zone
	 * @param p_sTag a {@link java.lang.String} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone} object.
	 */
	public ManagementZone addZone(String p_sGroup, String p_sZone, String p_sSource, String p_sTag) {
		final ManagementGroup oGroup = this.addGroup(p_sGroup);
		ManagementZone r_oZone = oGroup.addZone(p_sZone, p_sSource, p_sTag);

		this.zonesByName.put(p_sZone, r_oZone);

		return r_oZone;
	}	
	
	/**
	 * Returns visible groups, use visual configuration to compute
	 *
	 * @return a list of visible groups
	 */
	public List<ManagementGroup> getVisibleGroups() {
		List<ManagementGroup> r_listVisibleGroups = new ArrayList<ManagementGroup>();
		for (ManagementGroup oGroup : this.groups) {
			List<ManagementZone> listZones = oGroup.getVisibleZones();
			if (!listZones.isEmpty()) {
				r_listVisibleGroups.add(oGroup);
			}
		}
		return r_listVisibleGroups;
	}

	/**
	 * <p>clone.</p>
	 *
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementConfiguration} object.
	 */
	@Override
	public ManagementConfiguration clone() {
		ManagementConfiguration r_oConfiguration = new ManagementConfiguration(this.getName());

		for (ManagementGroup oGroup : this.groups) {
			r_oConfiguration.addGroup(oGroup.clone());

			for (ManagementZone oZone : oGroup.getVisibleZones()) {
				r_oConfiguration.zonesByName.put(oZone.getName(), oZone);
			}
		}
		
		return r_oConfiguration;
	}
	/**
	 * Merges the current configuration with a new and partial version.
	 *
	 * @param p_oOverwrite a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementConfiguration} object.
	 * @return a boolean.
	 */
	public boolean merge(final ManagementConfiguration p_oOverwrite) {
		ManagementGroup oExistingGroup;
		ManagementZone oExistingZone;

		boolean r_bMergeable = false;

		// 1. Vérification que chaque zone obligatoire de la configuration initiale
		// est contenue dans un des groupes de la configuration à appliquer.
		final List<ManagementGroup> listUpdatedGroups = p_oOverwrite.getVisibleGroups();
		final List<ManagementZone> listNotExistingMandatoryZones = new ArrayList<ManagementZone>();

		Iterator<ManagementGroup> iterUpdatedGroups = null;
		boolean bMandatoryZoneExists = false;
		for (ManagementGroup oGroup : this.getVisibleGroups()) {
			for (ManagementZone oZone : oGroup.getVisibleZones()) {
				bMandatoryZoneExists = false;
				if (oZone.isMandatory() && p_oOverwrite.hasGroup(oZone.getGroup())) {
					iterUpdatedGroups = listUpdatedGroups.iterator();
					while (!bMandatoryZoneExists && iterUpdatedGroups.hasNext()) {
						bMandatoryZoneExists = iterUpdatedGroups.next().hasManagementZone(oZone);
					}

					if (!bMandatoryZoneExists) {
						listNotExistingMandatoryZones.add(oZone);
					}
				}
			}
		}

		// 2. Toutes les zones obligatoires sont présentes dans la configuration à merger
		// => Le merge peut débuter.
		if (listNotExistingMandatoryZones.isEmpty()) {
			for (ManagementGroup oNewGroup : p_oOverwrite.getVisibleGroups()) {
				oExistingGroup = this.getGroup(oNewGroup.getName());
				if (oExistingGroup == null) { // Nouvelle colonne, elle est ajoutée à la fin
					oExistingGroup = this.addGroup(oNewGroup.getName());
				}
				else { // Merge d'une colonne existante
					oExistingGroup.clear();
				}

				for (ManagementZone oNewZone : oNewGroup.getVisibleZones()) {
					oExistingZone = this.zonesByName.get(oNewZone.getName());
					if (oExistingZone == null) {
						// Impossible de rajouter une zone qui n'existait pas dans la configuration initiale
					}
					else {
						oExistingGroup.addZone(oExistingZone);
					}
				}
			}
		}
		// Au moins une zone obligatoire appartenant à un groupe surchargé n'est pas dans la configuration à merger
		// => Merge impossible. Log des zones obligatoires concernées
		else {
			
		}

		return r_bMergeable;
	}

	/**
	 * <p>addGroup.</p>
	 *
	 * @param p_oGroup a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementGroup} object.
	 */
	protected void addGroup(final ManagementGroup p_oGroup) {
		if (p_oGroup != null && !this.groupsByName.containsKey(p_oGroup.getName())) {
			this.groups.add(p_oGroup);
			this.groupsByName.put(p_oGroup.getName(), p_oGroup);
		}
	}

	/**
	 * <p>getGroup.</p>
	 *
	 * @param p_sGroup a {@link java.lang.String} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementGroup} object.
	 */
	protected final ManagementGroup getGroup(final String p_sGroup) {
		return this.groupsByName.get(p_sGroup);
	}

	/**
	 * <p>hasGroup.</p>
	 *
	 * @param p_sGroup a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	protected boolean hasGroup(final String p_sGroup) {
		return this.getGroup(p_sGroup) != null;
	}
}
