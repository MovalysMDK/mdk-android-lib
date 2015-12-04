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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;

/**
 * <p>Defines a managed zone (like a panel in a column)</p>
 *
 *
 */
public class ManagementZone extends ManagementGroup implements Serializable {

	/**
	 * Version number of this class implementation.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Zone source
	 */
	private String source;

	/**
	 * Tag
	 */
	private String tag ;

	/**
	 * Group
	 */
	private String group;

	/**
	 * Default constructor: Creates a new zone without name and source.
	 */
	protected ManagementZone() {
		this.source = null;
		this.tag = null ;
		this.group = null;
	}
	
	/**
	 * Constructs a new zone
	 *
	 * @param p_sName the name of zone
	 * @param p_sSource the source of zone
	 * @param p_sGroup a {@link java.lang.String} object.
	 */
	public ManagementZone(final String p_sGroup, final String p_sName, final String p_sSource) {
		super(p_sName);
		this.group = p_sGroup;
		this.source = p_sSource;
	}
	
	/**
	 * Constructs a new zone
	 *
	 * @param p_sName the name of zone
	 * @param p_sSource the source of zone
	 * @param p_sTag tag
	 * @param p_sGroup a {@link java.lang.String} object.
	 */
	public ManagementZone(final String p_sGroup, String p_sName, String p_sSource, String p_sTag ) {
		this(p_sGroup, p_sName, p_sSource);
		this.tag = p_sTag;
	}

	/**
	 * Returns the source
	 *
	 * @return the source of management zone
	 */
	public final String getSource() {
		return this.source;
	}

	/**
	 * Returns the group
	 *
	 * @return the group where the zone has been included.
	 */
	public final String getGroup() {
		return this.group;
	}
	
	/**
	 * Defines the source of this zone. Only used to deserialize (jackson) a zone.
	 *
	 * @param p_sSource
	 * 		The source of the zone.
	 */
	protected final void setSource(final String p_sSource) {
		this.source = p_sSource;
	}

	/**
	 * <p>Getter for the field <code>tag</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTag() {
		return this.tag;
	}

	/**
	 * Returns true if this zone is mandatory
	 *
	 * @return true if this zone is mandatory
	 */
	public boolean isMandatory() {
		boolean r_bMandatory = BeanLoader.getInstance().getBean(ManagementZoneMandatoryRule.class).isMandatory(this);
		if (!r_bMandatory) { // La zone est non obligatoire, si une zone fille est obligatoire, alors le parent est obligatoire
			for (ManagementZone oChildZone : this.getVisibleZones()) {
				if (oChildZone.isMandatory()) {
					r_bMandatory = true;
					break;
				}
			}
		}
		return r_bMandatory;
	}

	/**
	 * Returns a copy of this zone.
	 *
	 * @returns a copy of this zone.
	 */
	@Override
	public ManagementZone clone() {
		ManagementZone r_oZone = (ManagementZone) super.clone();

		r_oZone.group = this.getGroup();
		r_oZone.source = this.getSource();
		r_oZone.tag = this.getTag();

		return r_oZone;
	}
}
