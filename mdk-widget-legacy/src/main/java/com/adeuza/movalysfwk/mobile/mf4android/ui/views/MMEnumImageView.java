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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableEnumComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;

/**
 * MMEnumImageView
 *<p>Show an image in function of an Enum</p>
 */
public class MMEnumImageView extends ImageView implements ConfigurableVisualComponent, InstanceStatable, ComponentReadableWrapper<Enum<?>> {

	/** Suffix of the default image. */
	public static final String DEFAULT_IMG_SUFFIX = "fwk_none";
	
	/** width of the image */
	public static final int IMAGE_SIZE_X = 150 ;
	
	/** height of the image */
	public static final int IMAGE_SIZE_Y = 32;
	
	/** Size of the text */
	public static final int TEXT_SIZE = 20;

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate;
	
	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<Enum<?>> aivDelegate;

	/**
	 * Constructs a new MMImageView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see ImageView#ImageView(Context, AttributeSet)
	 */
	@SuppressWarnings("unchecked")
	public MMEnumImageView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext,p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Enum<?>>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Enum.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
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
	public MMEnumImageView(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Enum<?>>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Enum.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
		}
		//completed by the onFinishInflate method callback
	}

	/**
	 * Compute the display for the current image
	 */
	protected void computeDisplay() {

		Integer iImageId = ((AndroidConfigurableEnumComponentDelegate) this.aivDelegate).getRessourceId(ApplicationRGroup.DRAWABLE);

		if ( iImageId == AndroidConfigurableEnumComponentDelegate.NO_RESSOURCE ) {
			if ( ((AndroidConfigurableEnumComponentDelegate) this.aivDelegate).isMissingRessourceReplacedByText() ) {
				Log.d(Application.LOG_TAG, "Missing drawable: " + ((AndroidConfigurableEnumComponentDelegate) this.aivDelegate).getEnumRessourceValue() + ".png : replaced by the text");
				DisplayMetrics oDisplayMetrics = this.getContext().getResources().getDisplayMetrics();
				int iImageSizeX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, IMAGE_SIZE_X, oDisplayMetrics );
				int iImageSizeY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, IMAGE_SIZE_Y, oDisplayMetrics );
				int iTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE, oDisplayMetrics );
				int iPaddingTop = iTextSize + ((iImageSizeY - iTextSize) / 2 );

				Bitmap oBitmapOverlay = Bitmap.createBitmap(iImageSizeX, iImageSizeY, Bitmap.Config.ARGB_4444);
				Canvas oCanvas = new Canvas(oBitmapOverlay);
				Paint oPaint = new Paint();
				oPaint.setColor(Color.WHITE);
				oPaint.setTextSize(iTextSize);
				oPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
				oCanvas.drawText(((AndroidConfigurableEnumComponentDelegate) this.aivDelegate).getEnumValue(), 0, iPaddingTop, oPaint);

				this.setImageBitmap(oBitmapOverlay);
			}
			else {
				this.setImageBitmap(null);
			}
		}
		else {
			if (this.getVisibility()!= VISIBLE){
				this.setVisibility(VISIBLE);
			}
			this.setImageResource(iImageId);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivFwkDelegate.setId(p_oId);
	}

	/**
	 *  {@inheritDoc}
	 *  @see InstanceStatable#superOnRestoreInstanceState(Parcelable)
	 */
	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		super.onRestoreInstanceState(p_oState);
	}
	
	@Override
	public Parcelable superOnSaveInstanceState() {
		return super.onSaveInstanceState();
	}
	
	/**
	 * {@inheritDoc}
	 * Keep the value when state changed
	 * @see android.view.View#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		if ( this.aivDelegate != null){
			Bundle oToSave = new Bundle();
			oToSave.putParcelable("parent", this.aivFwkDelegate.onSaveInstanceState( super.onSaveInstanceState() ) );
			oToSave.putString("imageNamePrefix", ((AndroidConfigurableEnumComponentDelegate) this.aivDelegate).getEnumRessourceNamePrefix());
			oToSave.putString("imageNameSuffix", ((AndroidConfigurableEnumComponentDelegate) this.aivDelegate).getEnumValue());
			oToSave.putBoolean("isMissingImageReplacedByText", ((AndroidConfigurableEnumComponentDelegate) this.aivDelegate).isMissingRessourceReplacedByText());
			return oToSave;
		}
		return super.onSaveInstanceState();
	}

	/**
	 * {@inheritDoc}
	 * Reset the value when state changed
     * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		if(!(p_oState instanceof Bundle)){
			super.onRestoreInstanceState(p_oState);
			return;
		}

		Bundle savedState = (Bundle) p_oState;
		this.aivFwkDelegate.onRestoreInstanceState(savedState.getParcelable("parent"));
		((AndroidConfigurableEnumComponentDelegate) this.aivDelegate).setEnumRessourceNamePrefix(savedState.getString("imageNamePrefix"));
		((AndroidConfigurableEnumComponentDelegate) this.aivDelegate).setEnumValue(savedState.getString("imageNameSuffix"));
		((AndroidConfigurableEnumComponentDelegate) this.aivDelegate).setIsMissingRessourceReplacedByText(savedState.getBoolean("isMissingImageReplacedByText"));
		this.aivDelegate.setFilled(true);
		
		this.computeDisplay();
	}

	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}

	@Override
	public VisualComponentDelegate<Enum<?>> getComponentDelegate() {
		return this.aivDelegate;
	}

	/************************************************************************************************
	 ******************************** Framework delegate callback ***********************************
	 ************************************************************************************************/

	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(Enum<?> p_oObjectToSet) {
		this.aivDelegate.setFilled(p_oObjectToSet != null);
		computeDisplay();
	}
}
