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
