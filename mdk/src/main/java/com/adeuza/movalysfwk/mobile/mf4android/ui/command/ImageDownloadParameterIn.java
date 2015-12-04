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
package com.adeuza.movalysfwk.mobile.mf4android.ui.command;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
/**
 * Paramètre de l'action téléchargement d'image
 */
public class ImageDownloadParameterIn extends AbstractActionParameter implements ActionParameter {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4794413887514784024L;
	/** the id */
	private String id = "-1";
	/** the maximum width */
	private int maxWidth = -1 ;
	/**
	 * Modify the id
	 * @param p_oMaxWidth id
	 */
	public void setId(String p_oId) {
		this.id = p_oId;
	}
	/**
	 * Modify the maximum width
	 * @param p_oMaxWidth the maximum width
	 */
	public void setMaxWidth(int p_oMaxWidth) {
		this.maxWidth = p_oMaxWidth;
	}
	/**
	 * Return the id
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}
	/**
	 * Return the maximum width
	 * @return the maximum width
	 */
	public int getMaxWidth() {
		return this.maxWidth;
	}
	
}
