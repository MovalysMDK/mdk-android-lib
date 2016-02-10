package com.adeuza.movalysfwk.mobile.mf4android.ui.command;

import java.io.Serializable;

import android.util.AttributeSet;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;

/**
 * @author lmichenaud
 *
 */
public class MMPhotoConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6339735987636266814L;

	/**
	 * 
	 */
	public static final String THUMBNAIL_MAX_WIDTH_PARAMETER = "thumbnailMaxWidth";
	
	/**
	 * 
	 */
	public static final String PARAMETER_NAME = "MMPhotoCommandConfig";
	
	/**
	 * Image default width : default value
	 */
	public static final int IMAGE_DEFAULT_WIDTH_DEFAULTVALUE = 1280 ;
	
	/**
	 * Downloaded image max width : default value
	 */
	public static final int DOWNLOADED_IMAGE_MAX_WIDTH_DEFAULTVALUE = 1024 ;
	
	/**
	 * Image default width parameter value
	 */
	private int imageDefaultWidth = IMAGE_DEFAULT_WIDTH_DEFAULTVALUE ;
	
	/**
	 * Downloaded image max width parameter value
	 */
	private int downloadImageMaxWidth = DOWNLOADED_IMAGE_MAX_WIDTH_DEFAULTVALUE ;

	/**
	 * Thumbnail max width
	 */
	private int thumbnailMaxWidth = -1 ;
	
	/**
	 * 
	 */
	private boolean removeEnabled = true ;
	
	/**
	 * 
	 */
	public MMPhotoConfig( boolean p_bRemoveEnabled, AttributeSet p_oAttrs) {
		this.imageDefaultWidth = p_oAttrs.getAttributeIntValue(Application.MOVALYSXMLNS, "imageDefaultWidth",
				IMAGE_DEFAULT_WIDTH_DEFAULTVALUE);
		this.downloadImageMaxWidth = p_oAttrs.getAttributeIntValue(Application.MOVALYSXMLNS, "downloadImageMaxWidth",
				DOWNLOADED_IMAGE_MAX_WIDTH_DEFAULTVALUE);
		this.thumbnailMaxWidth = p_oAttrs.getAttributeIntValue(Application.MOVALYSXMLNS, THUMBNAIL_MAX_WIDTH_PARAMETER, -1);
		this.removeEnabled = p_bRemoveEnabled ;
	}

	/**
	 * @return
	 */
	public int getImageDefaultWidth() {
		return imageDefaultWidth;
	}

	/**
	 * @param p_iImageDefaultWidth
	 */
	public void setImageDefaultWidth(int p_iImageDefaultWidth) {
		this.imageDefaultWidth = p_iImageDefaultWidth;
	}

	/**
	 * @return
	 */
	public int getDownloadImageMaxWidth() {
		return downloadImageMaxWidth;
	}

	/**
	 * @param p_iDownloadImageMaxWidth
	 */
	public void setDownloadImageMaxWidth(int p_iDownloadImageMaxWidth) {
		this.downloadImageMaxWidth = p_iDownloadImageMaxWidth;
	}

	/**
	 * Thumbnail max width
	 * @return
	 */
	public int getThumbnailMaxWidth() {
		return this.thumbnailMaxWidth;
	}

	/**
	 * @param p_iThumbnailMaxWidth
	 */
	public void setThumbnailMaxWidth(int p_iThumbnailMaxWidth) {
		this.thumbnailMaxWidth = p_iThumbnailMaxWidth;
	}

	/**
	 * @return
	 */
	public boolean isRemoveEnabled() {
		return removeEnabled;
	}

	/**
	 * @param p_bRemoveEnabled
	 */
	public void setRemoveEnabled(boolean p_bRemoveEnabled) {
		this.removeEnabled = p_bRemoveEnabled;
	}
}
