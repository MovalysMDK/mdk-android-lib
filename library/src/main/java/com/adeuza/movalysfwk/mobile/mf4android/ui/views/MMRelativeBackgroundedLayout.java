package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMRelativeLayout;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;

/**
 * Custom RelativeLayout with a background binded to an enumeration
 */
public class MMRelativeBackgroundedLayout  extends AbstractMMRelativeLayout<Enum<?>>{
	/** there is no image */
	public static final int NO_IMAGE = -1;
	/** Enum name. */
	private String imageNamePrefix = null;
	/** Suffix of the default image. */
	private static final String DEFAULT_IMG_SUFFIX = "fwk_none";
	/** Tag to use for debugging */
	private static final String DEBUG_TAG = "MMRelativeBackgroundedLayout";
	/**
	 * Constructs a new MMLinearLayout
	 * @param p_oContext 
	 * 		The context to use
	 * @param p_oAttrs 
	 * 		The attributes to use
	 */
	public MMRelativeBackgroundedLayout(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.init(p_oAttrs);
		}
	}
	/**
	 * Constructs a new MMLinearLayout
	 * @param p_oContext 
	 * 		The context to use
	 */
	public MMRelativeBackgroundedLayout(Context p_oContext) {
		super(p_oContext);
	}
	/**
	 * Initializes this component.
	 * @param p_oAttrs attributes of the layout
	 */
	private final void init(AttributeSet p_oAttrs) {
		String sEnumName = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "enum");
		if (sEnumName == null) {
			sEnumName = StringUtils.EMPTY;
		}else {
			sEnumName = sEnumName.toLowerCase(Locale.getDefault());
		}
		String sPrefix = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "prefix");
		if (sPrefix == null) {
			sPrefix = StringUtils.EMPTY;
		}		
		StringBuilder imageNamePrefixBuilder = new StringBuilder();
		if(sPrefix.length() > 0) {
			imageNamePrefixBuilder.append(sPrefix).append('_');
		}
		else {
			imageNamePrefixBuilder.append("enum_");
		}
		imageNamePrefixBuilder.append(sEnumName).append('_');
		
		this.imageNamePrefix = imageNamePrefixBuilder.toString();
	}
	/** 
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.MMValueableView#setValue(java.lang.Object)
	 */
	@Override
	public void configurationSetValue(Enum<?> p_iImageId) {
		super.configurationSetValue(p_iImageId);

		String sImgSuffix = DEFAULT_IMG_SUFFIX; 
		if (p_iImageId != null) {
			sImgSuffix = p_iImageId.name().toLowerCase(Locale.getDefault());
		}
		String sIdentifier = this.imageNamePrefix + sImgSuffix;

		Integer iImageId = ((AndroidApplication)AndroidApplication.getInstance()).getAndroidIdByStringKey(
				ApplicationRGroup.DRAWABLE, sIdentifier);

		if ( iImageId != NO_IMAGE ) {
			if (this.getVisibility()!= VISIBLE){
				this.setVisibility(VISIBLE);
			}
			this.setBackgroundResource(iImageId);
		} else {
			Log.d(DEBUG_TAG, "Background Image not found " + sIdentifier );
		}
	}	
	/**
	 * {@inheritDoc}
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		// NOTHING TO DO
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enum<?> configurationGetValue() {
		return null;
	}
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return null;
	}
	/** 
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.interventionproducts.mm.android.customviews.MMValueableView#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return Integer.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomConverter customConverter() {
		return this.aivDelegate.customConverter();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomFormatter customFormatter() {
		return this.aivDelegate.customFormatter();
	}

	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		this.aivDelegate.setCustomConverter(p_oConverter);
	}
	
	@Override
	public void setCustomFormatter(CustomFormatter p_oFormatter) {
		this.aivDelegate.setCustomFormatter(p_oFormatter);
	}
	
	/**
	 * Sets the custom converter
	 * @param p_oCustomConverter custom converter to set
	 * @param p_oAttributeSet attributeSet for the converter
	 */
	public void setCustomConverter(String p_oCustomConverter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomConverter(p_oCustomConverter,p_oAttributeSet);		
	}
	
	/**
	 * Sets the custom formatter
	 * @param p_oCustomFormatter custom formatter to set
	 * @param p_oAttributeSet attributeSet for the formatter
	 */
	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomFormatter(p_oCustomFormatter,p_oAttributeSet);		
	}
}
