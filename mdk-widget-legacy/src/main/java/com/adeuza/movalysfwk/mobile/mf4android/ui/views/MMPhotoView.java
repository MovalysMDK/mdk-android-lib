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
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.mediastore.images.MMediaStoreImageService;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentBindDestroy;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;

/**
 * <p>Photo view widget used in the Movalys Mobile product for Android</p>
 * this component use the photo URI as String 
 */
public class MMPhotoView extends ImageView implements ConfigurableVisualComponent, InstanceStatable, ComponentBindDestroy, 
	ComponentReadableWrapper<String>, ComponentWritableWrapper<String> {

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<String> aivDelegate = null;

	/** the uri of this image*/
	private Uri imageUri=null;
	
	/** max width of the bitmap */
	public static final int MAX_WIDTH_OF_BITMAP = 640 ;

	/** loggin tag */
	private static final String TAG = "MMPhotoView";

	/**
	 * Constructs a new MMImageView
	 * @param p_oContext the context to use
	 * @see ImageView#ImageView(Context)
	 */
	@SuppressWarnings("unchecked")
	public MMPhotoView(Context p_oContext) {
		super(p_oContext);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<String>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{String.class, null});
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
	public MMPhotoView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext,p_oAttrs);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<String>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{String.class, p_oAttrs});
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
	public MMPhotoView(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<String>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{String.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
		}
	}
	
	/**
	 * Updates the component with a new photo from its url
	 * @param p_oPhotoUrl the url of the photo to set
	 */
	public void updatePhoto(String p_oPhotoUrl){
		this.aivDelegate.configurationSetValue(p_oPhotoUrl);
		this.aivFwkDelegate.changed();	
	}

	/**
	 * Called when the component value changes
	 */
	protected void clearBitmap() {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#setId(int)
	 */
	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivFwkDelegate.setId(p_oId);
	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.onSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);

		if (this.aivDelegate.configurationGetValue()!=null) {
			r_oBundle.putString("imageUri", this.aivDelegate.configurationGetValue());
		} else {
			r_oBundle.putString("imageUri", "");
		}

		Log.d(TAG,"[superOnSaveInstanceState] photoUri : " + r_oBundle.getString("imageUri"));
		return r_oBundle;
	}

	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;
		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.onRestoreInstanceState(oParentParcelable);

		if (this.aivDelegate.configurationGetValue() == null) {
			this.aivDelegate.configurationSetValue(r_oBundle.getString("imageUri"));
		}

		Log.d(TAG,"[superOnRestoreInstanceState] photoUri : " + r_oBundle.getString("imageUri"));
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		return this.aivFwkDelegate.onSaveInstanceState(super.onSaveInstanceState());
	}
	
	/**
	 * {@inheritDoc}
	 * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		this.aivFwkDelegate.onRestoreInstanceState(p_oState);
	}
	
	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}
	
	@Override
	public VisualComponentDelegate<String> getComponentDelegate() {
		return this.aivDelegate;
	}
	
	/****************************************************************************************************
	 *********************************** Framework delegate callback ************************************
	 ****************************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentBindDestroy#doOnPostAutoBind()
	 */
	@Override
	public void doOnPostAutoBind() {
		if (this.imageUri != null) {
			MMediaStoreImageService oMMediaStoreImageService = BeanLoader.getInstance().getBean(MMediaStoreImageService.class);
			Bitmap oImage = oMMediaStoreImageService.loadBitmap(this.imageUri, MMPhotoView.MAX_WIDTH_OF_BITMAP, this.getContext().getContentResolver());
			this.setImageBitmap(oImage);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentBindDestroy#destroy()
	 */
	@Override
	public void destroy() {
		this.clearBitmap();
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public String configurationGetValue() {
		if (this.imageUri == null) {
			return null;
		} else {
			return this.imageUri.toString();
		}
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(String p_oObjectToSet) {
		if (p_oObjectToSet == null || "".equals(p_oObjectToSet) ){
			AndroidApplication oApplication = (AndroidApplication) AndroidApplication.getInstance();
			this.setImageResource(oApplication.getAndroidIdByRKey(AndroidApplicationR.component__photoview__nophoto));
		}else{
			this.setTag(p_oObjectToSet);
			this.clearBitmap();
			if (((String) p_oObjectToSet).startsWith("content:")) {
				this.imageUri=Uri.parse( (String)p_oObjectToSet);
				MMediaStoreImageService oMMediaStoreImageService = BeanLoader.getInstance().getBean(MMediaStoreImageService.class);
				Bitmap oImage = oMMediaStoreImageService.loadBitmap(this.imageUri, MMPhotoView.MAX_WIDTH_OF_BITMAP, this.getContext().getContentResolver());
				this.setImageBitmap(oImage);
			}
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
