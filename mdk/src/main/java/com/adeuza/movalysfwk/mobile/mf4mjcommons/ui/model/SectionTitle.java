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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model;

/**
 * <p>TODO Décrire la classe SectionTitle</p>
 *
 *
 */
public class SectionTitle {
	private String title = "";

	private String background;

	/**
	 * <p>Constructor for SectionTitle.</p>
	 */
	public SectionTitle() {
	}

	/**
	 * <p>Constructor for SectionTitle.</p>
	 *
	 * @param p_sTitle a {@link java.lang.String} object.
	 */
	public SectionTitle(String p_sTitle) {
		this.title = p_sTitle;
	}

	/**
	 * <p>Constructor for SectionTitle.</p>
	 *
	 * @param p_sTitle a {@link java.lang.String} object.
	 * @param p_sBackground a {@link java.lang.String} object.
	 */
	public SectionTitle(String p_sTitle, String p_sBackground) {
		this.background = p_sBackground;
		this.title = p_sTitle;
	}

	/**
	 * <p>Getter for the field <code>title</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * <p>Getter for the field <code>background</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getBackground() {
		return this.background;
	}

	/**
	 * <p>Setter for the field <code>title</code>.</p>
	 *
	 * @param p_sTitle a {@link java.lang.String} object.
	 */
	public void setTitle(String p_sTitle) {
		this.title = p_sTitle;
	}

	/**
	 * <p>Setter for the field <code>background</code>.</p>
	 *
	 * @param p_sBackground a {@link java.lang.String} object.
	 */
	public void setBackground(String p_sBackground) {
		this.background = p_sBackground;
	}
}
