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
package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;

/**
 * Image component displaying a image in function of a double.
 *
 */
public class MMDoubleImageIndicator extends ImageView implements ConfigurableVisualComponent, 
ComponentReadableWrapper<Double>, ComponentWritableWrapper<Double> {

	/** there is no image */
	public static final int NO_IMAGE = -1;
	
	/** width of the image */
	public static final int IMAGE_SIZE_X = 150 ;
	
	/** height of the image */
	public static final int IMAGE_SIZE_Y = 32;
	
	/** Size of the text */
	public static final int TEXT_SIZE = 20;

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<Double> aivDelegate = null;
	
	/** configurable view delegate */
	private AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;

	/** Image name. */
	private String imageNamePrefix = null;
	
	/** True if the design replaced the image displayed by a text if the image doesn't exist , false else */
	private boolean isMissingImageReplacedByText = false ;

	/** the level list handle by the component */
	private List<Double> levelList = new ArrayList<>() ;

	/**
	 * Constructs a new MMImageView
	 * @param p_oContext the context to use
	 * @see ImageView#ImageView(Context)
	 */
	@SuppressWarnings("unchecked")
	public MMDoubleImageIndicator(Context p_oContext) {
		super(p_oContext);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Double>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Double.class, null});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{null});
		}
	}

	/**
	 * Constructs a new MMImageView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see ImageView#ImageView(Context, AttributeSet)
	 */
	@SuppressWarnings("unchecked")
	public MMDoubleImageIndicator(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext,p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Double>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Double.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this,  
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
			this.init(p_oAttrs);
		}
	}

	/**
	 * Constructs a new MMImageView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use 
	 * @see ImageView#ImageView(Context, AttributeSet, int)
	 */
	@SuppressWarnings("unchecked")
	public MMDoubleImageIndicator(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Double>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Double.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this,  
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
			this.init(p_oAttrs);
		}
		//completed by the onFinishInflate method callback
	}

	/**
	 * Initializes this component.
	 * @param p_oAttrs the android view attribute set
	 */
	private final void init(AttributeSet p_oAttrs) {
		String sPrefixImageName = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "prefix");
		if (sPrefixImageName == null) {
			sPrefixImageName = StringUtils.EMPTY;
		}else {
			sPrefixImageName = sPrefixImageName.toLowerCase(Locale.getDefault());
		}
		this.imageNamePrefix = new StringBuilder().append("doublelevel_").append(sPrefixImageName).append('_').toString();
		this.isMissingImageReplacedByText = p_oAttrs.getAttributeBooleanValue(Application.MOVALYSXMLNS, "imageMissingReplacement", true);

		String sLevelsValue = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "levels");
		if (sLevelsValue != null){
			String[] aLevels = sLevelsValue.split("#");
			for ( String sLevel : aLevels) {
				try {
					levelList.add(Double.parseDouble(sLevel));
				}catch( NumberFormatException e){
					Application.getInstance().getLog().error("MMDoubleImageIndicator", "Component parameter level is not good :"+sLevel );
				}
			}
		} else {
			levelList.add(0.0d);
		}
	}

	/**
	 * find the level to display
	 * @param p_oObjectToSet the level to display
	 * @return the index of level
	 */
	public int findLevel(Double p_oObjectToSet){
		int r_iIndexLevelFound = 0 ;
		if ( p_oObjectToSet != null){
			for ( Double level : this.levelList){
				if ( p_oObjectToSet < level ){
					break ;
				}
				r_iIndexLevelFound++ ;
			}
		}
		return r_iIndexLevelFound ;

	}

	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivFwkDelegate.setId(p_oId);
	}

	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}

	@Override
	public VisualComponentDelegate<Double> getComponentDelegate() {
		return this.aivDelegate;
	}
	
	/********************************************************************************************************
	 ************************************ Framework delegate callback ***************************************
	 ********************************************************************************************************/

	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public Double configurationGetValue() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(Double p_oObjectToSet) {
		String sIdentifier = this.imageNamePrefix + this.findLevel(p_oObjectToSet);
		Integer iImageId = ((AndroidApplication)AndroidApplication.getInstance()).getAndroidIdByStringKey(
				ApplicationRGroup.DRAWABLE, sIdentifier);

		// permet de remplacer l image si elle n'existe pas
		if ( iImageId == MMDoubleImageIndicator.NO_IMAGE ) {
			if ( this.isMissingImageReplacedByText ) {
				Log.d("MMDoubleImageIndicator", "Missing drawable: " + sIdentifier + ".png : replaced by the text");
				Activity activity = (Activity) ((AndroidApplication) Application.getInstance()).getCurrentVisibleActivity();
				DisplayMetrics oDisplayMetrics = activity.getResources().getDisplayMetrics();
				int iImageSizeX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MMDoubleImageIndicator.IMAGE_SIZE_X, oDisplayMetrics );
				int iImageSizeY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MMDoubleImageIndicator.IMAGE_SIZE_Y, oDisplayMetrics );
				int iTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MMDoubleImageIndicator.TEXT_SIZE, oDisplayMetrics );
				int iPaddingTop = iTextSize + ((iImageSizeY - iTextSize) / 2 );

				Bitmap oBitmapOverlay = Bitmap.createBitmap(iImageSizeX, iImageSizeY, Bitmap.Config.ARGB_4444);
				Canvas oCanvas = new Canvas(oBitmapOverlay);
				Paint oPaint = new Paint();
				oPaint.setColor(Color.WHITE);
				oPaint.setTextSize(iTextSize);
				oPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
				oCanvas.drawText(sIdentifier, 0, iPaddingTop, oPaint);

				this.setImageBitmap(oBitmapOverlay);
			}
			else {
				this.setImageBitmap(null);
			}
		}
		else {
			if (this.getVisibility()!= View.VISIBLE){
				this.setVisibility(View.VISIBLE);
			}
			this.setImageResource(iImageId);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return true;
	}
}
