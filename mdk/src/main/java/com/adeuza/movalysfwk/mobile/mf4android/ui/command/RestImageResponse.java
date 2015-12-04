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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.RestResponse;

/**
 * <p>Response of the rest service who download a bitmap image in Base64</p> 
 */

public class RestImageResponse extends RestResponse {
	/**
	 * Content of the picture in base 64 encoding
	 */
	private String content ;
	/**
	 * Accesseur du contenu
	 * @return le contenu
	 */
	public String getContent() {
		return this.content;
	}
	/**
	 * Modifieur du contenu
	 * @param p_oContent nouveau contenu
	 */
	public void setContent(String p_oContent) {
		this.content = p_oContent;
	}
}
