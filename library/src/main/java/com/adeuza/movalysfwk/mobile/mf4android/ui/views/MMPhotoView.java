package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

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
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * <p>Photo view widget used in the Movalys Mobile product for Android</p>
 * this component use the photo URI as String 
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 * @author dmaurange
 */
public class MMPhotoView extends ImageView implements ConfigurableVisualComponent<String>, MMIdentifiableView, InstanceStatable {
	/**resources*/
	private AndroidApplication oApplication = (AndroidApplication)Application.getInstance();

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<String> aivDelegate = null;

	/** the uri of this image*/
	private Uri imageUri=null;
	/** Taille max de l'image créé */
	private static final int MAX_WIDTH_OF_BITMAP = 640 ;

	private static final String TAG = "MMPhotoView";

	/**
	 * Constructs a new MMImageView
	 * @param p_oContext the context to use
	 * @see ImageView#ImageView(Context)
	 */
	public MMPhotoView(Context p_oContext) {
		super(p_oContext);
		if (!isInEditMode()){
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this);
		}
	}
	/**
	 * Constructs a new MMImageView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see ImageView#ImageView(Context, AttributeSet)
	 */
	public MMPhotoView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext,p_oAttrs);
		if (!isInEditMode()){
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
		}
	}
	/**
	 * Constructs a new MMImageView
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use 
	 * @see ImageView#ImageView(Context, AttributeSet, int)
	 */
	public MMPhotoView(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if (!isInEditMode()){
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
		}
	}
	/**
	 * Called wen the inflator finished the job 
	 * {@inheritDoc}
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

	}	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationSetValue(java.lang.Object)
	 */
	@Override
	public void configurationSetValue(String p_sImageUri) {
		this.aivDelegate.configurationSetValue(p_sImageUri);

		Log.d(TAG,"[configurationSetValue] photoUri : " + p_sImageUri);
		this.aivDelegate.configurationSetValue(p_sImageUri);
		if (p_sImageUri == null || "".equals(p_sImageUri) ){
			this.setImageResource(oApplication.getAndroidIdByRKey(AndroidApplicationR.component__photoview__nophoto));
		}else{
			this.setTag(p_sImageUri);
			this.clearBitmap();
			if (p_sImageUri.startsWith("content:")) {
				this.imageUri=Uri.parse(p_sImageUri);
				MMediaStoreImageService oMMediaStoreImageService = BeanLoader.getInstance().getBean(MMediaStoreImageService.class);
				Bitmap oImage = oMMediaStoreImageService.loadBitmap(this.imageUri, MAX_WIDTH_OF_BITMAP, getContext().getContentResolver());
				this.setImageBitmap(oImage);
			}
		}
	}
	/**
	 * Mise à jour de la photo avec un objet complet
	 * @param p_oPhoto
	 */
	public void updatePhoto(String p_oPhotoUrl){
		//if ( photoUri == null ) {
		this.configurationSetValue(p_oPhotoUrl);
		this.aivDelegate.changed();
		//} 		
	}
	/*
	public void clear() {
		this.clearBitmap();
		this.photo=null;
		this.setImageResource(oApplication.getAndroidIdByRKey(AndroidApplicationR.component__photoview__nophoto));
		this.resetChanged();
	}*/

	/**
	 * 
	 */
	public void clearBitmap() {
		//optim to preserve memory
		/* SPA plante au 2eme affichage du + 
		BitmapDrawable oBitmapDrawable = ((BitmapDrawable)this.getDrawable());
		if ( oBitmapDrawable != null && oBitmapDrawable.getBitmap() != null ) {
			oBitmapDrawable.getBitmap().recycle();
		}
		 */
	}	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationSetCustomValues(java.lang.String[])
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		// NOTHING TO DO
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#setId(int)
	 */
	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivDelegate.setId(p_oId);
	}

	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#getConfiguration()
	 */
	@Override
	public VisualComponentConfiguration getConfiguration() {
		return this.aivDelegate.getConfiguration();
	}

	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isGroup()
	 */
	@Override
	public boolean isGroup() {
		return this.aivDelegate.isGroup();
	}

	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationHide(boolean)
	 */
	@Override
	public void configurationHide(boolean p_bLockModifier) {
		this.aivDelegate.configurationHide(p_bLockModifier);
	}

	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationUnHide(boolean)
	 */
	@Override
	public void configurationUnHide(boolean p_bLockModifier) {
		this.aivDelegate.configurationUnHide(p_bLockModifier);
	}

	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationGetValue()
	 */
	@Override
	public String configurationGetValue() {
		return imageUri.toString();
	}

	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationGetCustomValues()
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return this.aivDelegate.configurationGetCustomValues();
	}

	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationSetLabel(java.lang.String)
	 */
	@Override
	public void configurationSetLabel(String p_sLabel) {
		this.aivDelegate.configurationSetLabel(p_sLabel);
	}

	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isLabel()
	 */
	@Override
	public boolean isLabel() {
		return this.aivDelegate.isLabel();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isAlwaysEnabled()
	 */
	@Override
	public boolean isAlwaysEnabled() {
		return false;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isValue()
	 */
	@Override
	public boolean isValue() {
		return this.aivDelegate.isValue();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isEdit()
	 */
	@Override
	public boolean isEdit() {
		return this.aivDelegate.isEdit();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#getLocalisation()
	 */
	@Override
	public String getLocalisation() {
		return this.aivDelegate.getLocalisation();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#getModel()
	 */
	@Override
	public String getModel() {
		return this.aivDelegate.getModel();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#getDescriptor()
	 */
	@Override
	public RealVisualComponentDescriptor getDescriptor() {
		return this.aivDelegate.getDescriptor();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#setDescriptor(com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor)
	 */
	@Override
	public void setDescriptor(RealVisualComponentDescriptor p_oDescriptor) {
		this.aivDelegate.setDescriptor(p_oDescriptor);
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isPanel()
	 */
	@Override
	public boolean isPanel() {
		return this.aivDelegate.isPanel();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isUnknown()
	 */
	@Override
	public boolean isUnknown() {
		return this.aivDelegate.isUnknown();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#getName()
	 */
	@Override
	public String getName() {
		return this.aivDelegate.getName();
	}
	/** 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMValueableView#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return String.class;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isMaster()
	 */
	@Override
	public boolean isMaster() {
		return this.aivDelegate.isMaster();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#resetChanged()
	 */
	@Override
	public void resetChanged() {
		this.aivDelegate.resetChanged();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isChanged()
	 */
	@Override
	public boolean isChanged() {
		return this.aivDelegate.isChanged();
	}	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(String p_oObject) {
		return p_oObject==null ;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyCustomValues(String[] p_oObjects) {
		return this.aivDelegate.isNullOrEmptyCustomValues(p_oObjects);
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#hide()
	 */
	@Override
	public void hide() {
		this.aivDelegate.hide();
	}	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#unHide()
	 */
	@Override
	public void unHide() {
		this.aivDelegate.unHide();
	}	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationPrepareHide(java.util.List)
	 */
	@Override
	public void configurationPrepareHide(List<ConfigurableVisualComponent<?>> p_oComponentsToHide) {
		this.aivDelegate.configurationPrepareHide(p_oComponentsToHide);
	}	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getConfigurationSetValueMethod()
	 */
	@Override
	public Method getConfigurationSetValueMethod() {
		return this.aivDelegate.getConfigurationSetValueMethod();
	}	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationDisabledComponent()
	 */
	@Override
	public void configurationDisabledComponent() {
		this.aivDelegate.configurationDisabledComponent();
	}	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		this.aivDelegate.configurationEnabledComponent();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return imageUri!=null;
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return this.isShown();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		// Nothing to do
	}	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationSetMandatoryLabel()
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		// Nothing to do
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnPostAutoBind() {
		if (this.imageUri != null) {
			MMediaStoreImageService oMMediaStoreImageService = BeanLoader.getInstance().getBean(MMediaStoreImageService.class);
			Bitmap oImage = oMMediaStoreImageService.loadBitmap(this.imageUri, MAX_WIDTH_OF_BITMAP, getContext().getContentResolver());
			this.setImageBitmap(oImage);
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#destroy()
	 */
	@Override
	public void destroy() {
		this.clearBitmap();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationUnsetError()
	 */
	@Override
	public void configurationUnsetError() {
		this.aivDelegate.configurationUnsetError();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationSetError(java.lang.String)
	 */
	@Override
	public void configurationSetError(String p_oError) {
		this.aivDelegate.configurationSetError(p_oError);
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#validate(com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration, java.lang.StringBuilder)
	 */
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		return this.aivDelegate.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder);
	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.onSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);

		if (this.imageUri!=null) {
			r_oBundle.putString("imageUri", this.imageUri.toString());
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

		if (this.imageUri == null) {
			configurationSetValue(r_oBundle.getString("imageUri"));
		}

		Log.d(TAG,"[superOnRestoreInstanceState] photoUri : " + r_oBundle.getString("imageUri"));
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		return this.aivDelegate.onSaveInstanceState(super.onSaveInstanceState());
	}
	/**
	 * {@inheritDoc}
	 * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		this.aivDelegate.onRestoreInstanceState(p_oState);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String p_sNewName) {
		this.aivDelegate.setName(p_sNewName);
	}


	/**
	 * {@inheritDoc}
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	public void registerVM(ViewModel p_oViewModel) {
		this.aivDelegate.registerVM(p_oViewModel);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#unregisterVM()
	 */
	@Override
	public void unregisterVM() {
		this.aivDelegate.unregisterVM();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomConverter customConverter() {
		return this.aivDelegate.customConverter();
	}

	public void setCustomConverter(String p_oCustomConverter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomConverter(p_oCustomConverter,p_oAttributeSet);		
	}

	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		this.aivDelegate.setCustomConverter(p_oConverter);
	}
	
	@Override
	public void setCustomFormatter(CustomFormatter p_oFormatter) {
		this.aivDelegate.setCustomFormatter(p_oFormatter);
	}
	
	@Override
	public CustomFormatter customFormatter() {
		return this.aivDelegate.customFormatter();
	}
	
	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomFormatter(p_oCustomFormatter,p_oAttributeSet);		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFragmentTag() {
		return this.aivDelegate.getFragmentTag();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFragmentTag(String p_sFragmentTag) {
		this.aivDelegate.setFragmentTag(p_sFragmentTag);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasRules() {
		return this.aivDelegate.hasRules();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHasRules(boolean p_bHasRules) {
		this.aivDelegate.setHasRules(p_bHasRules);
	}
}
