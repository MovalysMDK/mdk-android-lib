package com.adeuza.movalysfwk.mobile.mf4android.ui.command;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.RestResponse;

/**
 * <p>Response of the rest service who download a bitmap image in Base64</p> 
 * <p>Copyright (c) 2012</p>
 * <p>Company: Adeuza</p>
 * @author spacreau
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
