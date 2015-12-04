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

/**
 * <p>Class of basic graphical components that have a label.</p>
 *
 *
 */
public class TComponentGraphConfiguration extends TGraphConfiguration {

	/**
	 * Label of this component.
	 */
	private String label = null;

	/**
	 * Creates a new component. By default the name and the label are null, the component is visible.
	 */
	public TComponentGraphConfiguration() {
		this(null, null, true);
	}

	/**
	 *Creates a new component using its name, its label and its visibility
	 *
	 * @param p_sName Name of the component. Mandatory.
	 * @param p_sLabel Label of the component.
	 * @param p_bVisible <code>true</code> if the component is visible, <code>false</code> otherwise.
	 */
	public TComponentGraphConfiguration(final String p_sName, final String p_sLabel, final boolean p_bVisible) {
		super(p_sName, p_bVisible);
		this.label = p_sLabel;
	}

	/**
	 * Returns the label of this component, or null if it is not define.
	 *
	 * @return a {@link java.lang.String} object : The label of this component, or null if it is not define.
	 */
	public final String getLabel() {
		return this.label;
	}

	/**
	 * Defines the label of this component.
	 *
	 * @param p_sLabel
	 * 		The label of this component.
	 */
	public final void setLabel(final String p_sLabel) {
		this.label = p_sLabel;
	}
}
