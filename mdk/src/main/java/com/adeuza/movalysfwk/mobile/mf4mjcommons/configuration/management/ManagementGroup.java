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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.LayoutComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;

/**
 * <p>Defines a ManagementGroup (like a column in screen)</p>
 *
 *
 */
public class ManagementGroup implements Serializable {

	/** Version number of this class implementation. */
	private static final long serialVersionUID = 1L;

	/** the name of group */
	private String name = null;
	/** list of zones (like a panel in screen) */
	private List<ManagementZone> zones = null;

	private Map<String, ManagementZone> zonesByName = null;

	/**
	 * Default constructor: Create a new group without name.
	 */
	protected ManagementGroup() {
		this.zones = new ArrayList<ManagementZone>();
		this.zonesByName = new TreeMap<String , ManagementZone>();
	}

	/**
	 * Constructs a new management group
	 *
	 * @param p_sName the group's name
	 */
	public ManagementGroup(String p_sName) {
		this();
		this.name = p_sName;
	}

	/**
	 * Returns the name of this group.
	 *
	 * @return The name of this group or null if it is not defined.
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * Defines the name of this group. Only used by deserialization (jaclson).
	 *
	 * @param p_sName
	 * 		Name of this group.
	 */
	protected final void setName(final String p_sName) {
		this.name = p_sName;
	}

	/**
	 * <p>clear.</p>
	 */
	protected void clear() {
		this.zones.clear();
	}
	
	/**
	 * Add a zone (a panel in screen)
	 *
	 * @param p_sZoneName the name of zone
	 * @param p_sZoneSource the source of zone
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone} object.
	 */
	public ManagementZone addZone(String p_sZoneName, String p_sZoneSource) {
		ManagementZone r_oManagementZone = new ManagementZone(this.getName(), p_sZoneName, p_sZoneSource);
		this.addZone(r_oManagementZone);
		return r_oManagementZone;
	}

	/**
	 * Add a zone (a panel in screen)
	 *
	 * @param p_sZoneName the name of zone
	 * @param p_sZoneSource the source of zone
	 * @param p_sTag a {@link java.lang.String} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone} object.
	 */
	public ManagementZone addZone(String p_sZoneName, String p_sZoneSource, String p_sTag) {
		ManagementZone r_oManagementZone = new ManagementZone(this.getName(), p_sZoneName, p_sZoneSource, p_sTag);
		this.addZone(r_oManagementZone);
		return r_oManagementZone;
	}
	
	/**
	 * <p>addZone.</p>
	 *
	 * @param p_oZone a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone} object.
	 */
	protected void addZone(final ManagementZone p_oZone) {
		this.zones.add(p_oZone);
		this.zonesByName.put(p_oZone.getName(), p_oZone);
	}

	/**
	 * <p>hasManagementZone.</p>
	 *
	 * @param p_sName a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean hasManagementZone(final String p_sName) {
		return this.zonesByName.get(p_sName) != null;
	}

	/**
	 * <p>hasManagementZone.</p>
	 *
	 * @param p_oZone a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone} object.
	 * @return a boolean.
	 */
	public boolean hasManagementZone(final ManagementZone p_oZone) {
		return p_oZone != null && this.hasManagementZone(p_oZone.getName());
	}
	
	/**
	 * Returns visible zones, use visual configuration to compute
	 *
	 * @return a list of visible zones
	 */
	public List<ManagementZone> getVisibleZones() {
		List<ManagementZone> r_listVisibleZones = new ArrayList<ManagementZone>();
		for(ManagementZone sZone : this.zones) {
			LayoutComponentConfiguration oConfiguration = (LayoutComponentConfiguration) ConfigurationsHandler.getInstance().getVisualConfiguration(this.name);
			if (oConfiguration == null || oConfiguration.isVisible()) {
				r_listVisibleZones.add(sZone);
			}
		}
		return r_listVisibleZones;
	}

	/**
	 * <p>getZoneByTag.</p>
	 *
	 * @param p_sTag a {@link java.lang.String} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone} object.
	 */
	public ManagementZone getZoneByTag( String p_sTag ) {
		ManagementZone r_oManagementZone = null ;
		for(ManagementZone oZone : this.zones) {
			if ( p_sTag.equals(oZone.getTag())) {
				r_oManagementZone = oZone ;
				break;
			}
		}
		return r_oManagementZone ;
	}

	/**
	 * <p>hasChild.</p>
	 *
	 * @return a boolean.
	 */
	public boolean hasChild() {
		return !this.zones.isEmpty();
	}

	/**
	 * <p>clone.</p>
	 *
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementGroup} object.
	 */
	@Override
	public ManagementGroup clone() {
		try {
			ManagementGroup r_oGroup = this.getClass().newInstance();
			r_oGroup.setName(this.getName());

			for (ManagementZone oZone : this.zones) {
				r_oGroup.addZone(oZone.clone());
			}

			return r_oGroup;
		}
		catch (InstantiationException e) {
			throw new MobileFwkException(e);
		}
		catch (IllegalAccessException e) {
			throw new MobileFwkException(e);
		}
	}
}
