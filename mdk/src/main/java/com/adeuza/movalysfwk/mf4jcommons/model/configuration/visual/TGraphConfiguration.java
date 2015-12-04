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
package com.adeuza.movalysfwk.mf4jcommons.model.configuration.visual;

//import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * <p>Class of all graphical configurations.</p>
 *
 *
 */
//@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public class TGraphConfiguration implements Comparable<TGraphConfiguration> {

	/**
	 * Name of this configuration
	 */
	private String name;

	/**
	 * Visibility of this configuration
	 */
	private boolean visible;

	/**
	 * Defines a new graphical component.
	 */
	public TGraphConfiguration() {
		this(null, true);
	}

	/**
	 * Defines a new graphical component using its name and its visibility.
	 *
	 * @param p_sName Name of the configuration.
	 * @param p_bVisible Visibility of this component: <code>true</code>, this component is visible.
	 */
	public TGraphConfiguration(String p_sName, boolean p_bVisible) {
		this.name = p_sName;
		this.visible = p_bVisible;
	}

	/**
	 * Returns the name of this configuration. Never null.
	 *
	 * @return a {@link java.lang.String} object : The name of this configuration. Never null.
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * Defines the name of the GraphConfiguration
	 *
	 * @param p_sName Name of this graph configuration
	 */
	public final void setName(final String p_sName) {
		this.name = p_sName;
	}

	/**
	 * Returns <code>true</code> if this element is visible, <code>false</code> otherwise.
	 *
	 * @return a<code>true</code> if this element is visible, <code>false</code> otherwise.
	 */
	public final boolean isVisible() {
		return this.visible;
	}

	/**
	 * Defines the visibility of this component.
	 *
	 * @param p_bVisible
	 * 		<code>true</code> if the component is visible, <code>false</code> otherwise.
	 */
	public final void setVisible(final boolean p_bVisible) {
		this.visible = p_bVisible;
	}

	/** {@inheritDoc} */
	@Override
	public int compareTo(final TGraphConfiguration p_oGraphConfiguration) {
		return this.name.compareTo(p_oGraphConfiguration.name);
	}

	@Override
	public boolean equals(Object p_oObj) {
		if (p_oObj instanceof TGraphConfiguration) {
			return this.name.equals(((TGraphConfiguration)p_oObj).getName());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
	    return this.name.hashCode();
	}
	
	/**
	 * Merges two configuration of the same entity.
	 *
	 * @param p_oGraphConfiguration
	 * 		An another configuration to merge with the current configuration.
	 * 		If <code>null</code> no merge has been performed.
	 * @return <code>true</code> if the configuration have been merged, <code>false</code> otherwise.
	 */
	public boolean merge(TGraphConfiguration p_oGraphConfiguration) {

		return p_oGraphConfiguration != null
				&& this.getName().equals(p_oGraphConfiguration.getName());
	}
}
