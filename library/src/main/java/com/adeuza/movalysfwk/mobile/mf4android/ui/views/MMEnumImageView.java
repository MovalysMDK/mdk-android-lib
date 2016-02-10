package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

public class MMEnumImageView extends ImageView implements ConfigurableVisualComponent<Enum<?>>, MMIdentifiableView, InstanceStatable, Serializable {

	/** there is no image */
	public static final int NO_IMAGE = -1; 

	/**
	 * Suffix of the default image.
	 */
	private static final String DEFAULT_IMG_SUFFIX = "fwk_none";
	/**
	 * Abscisse de l'image
	 */
	private static final int IMAGE_SIZE_X = 150 ;
	/**
	 * Ordonnée de l'image
	 */
	private static final int IMAGE_SIZE_Y = 32;
	/**
	 * Taille du texte
	 */
	private static final int TEXT_SIZE = 20;

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<Enum<?>> aivDelegate;

	/** Enum name. */
	private String imageNamePrefix;
	private String imageNameSuffix = DEFAULT_IMG_SUFFIX;
	
	/** True if the design replaced the image displayed by a text if the image doesn't exist , false else */
	private boolean isMissingImageReplacedByText = false ;

	/**
	 * Constructs a new MMImageView
	 * @param p_oContext the context to use
	 * @see ImageView#ImageView(Context)
	 */
	public MMEnumImageView(Context p_oContext) {
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
	public MMEnumImageView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext,p_oAttrs);
		if(!isInEditMode()) {
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
	public MMEnumImageView(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			this.init(p_oAttrs);
		}
		//completed by th onFinishInflate method callback
	}

	/**
	 * Initializes this component.
	 * @param p_oAttrs
	 */
	private final void init(AttributeSet p_oAttrs) {
		this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);

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
		this.isMissingImageReplacedByText = p_oAttrs.getAttributeBooleanValue(Application.MOVALYSXMLNS, "imageMissingReplacement", true);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(Enum<?> p_oObjectToSet) {
		this.aivDelegate.configurationSetValue(p_oObjectToSet);
		if (p_oObjectToSet != null) {
			this.imageNameSuffix = p_oObjectToSet.name().toLowerCase(Locale.getDefault());
			this.aivDelegate.setFilled(true);
		}
		else {
			this.aivDelegate.setFilled(false);
		}
		this.computeDisplay();
	}

	
	protected void computeDisplay() {
		String sIdentifier = this.imageNamePrefix + this.imageNameSuffix;

		Integer iImageId = ((AndroidApplication)AndroidApplication.getInstance()).getAndroidIdByStringKey(
				ApplicationRGroup.DRAWABLE, sIdentifier);

		if ( iImageId == NO_IMAGE ) {
			if ( this.isMissingImageReplacedByText ) {
				Log.d(Application.LOG_TAG, "Missing drawable: " + sIdentifier + ".png : replaced by the text");
				//Activity oActivity = (Activity) ((AndroidApplication) Application.getInstance()).getCurrentActivity();
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
				oCanvas.drawText(this.imageNameSuffix, 0, iPaddingTop, oPaint);

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
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		// NOTHING TO DO
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivDelegate.setId(p_oId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VisualComponentConfiguration getConfiguration() {
		return this.aivDelegate.getConfiguration();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isGroup() {
		return this.aivDelegate.isGroup();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationHide(boolean p_bLockModifier) {
		this.aivDelegate.configurationHide(p_bLockModifier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnHide(boolean p_bLockModifier) {
		this.aivDelegate.configurationUnHide(p_bLockModifier);
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
	 */
	@Override
	public void configurationSetLabel(String p_sLabel) {
		this.aivDelegate.configurationSetLabel(p_sLabel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLabel() {
		return this.aivDelegate.isLabel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAlwaysEnabled() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValue() {
		return this.aivDelegate.isValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEdit() {
		return this.aivDelegate.isEdit();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLocalisation() {
		return this.aivDelegate.getLocalisation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getModel() {
		return this.aivDelegate.getModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RealVisualComponentDescriptor getDescriptor() {
		return this.aivDelegate.getDescriptor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDescriptor(RealVisualComponentDescriptor p_oDescriptor) {
		this.aivDelegate.setDescriptor(p_oDescriptor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPanel() {
		return this.aivDelegate.isPanel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isUnknown() {
		return this.aivDelegate.isUnknown();
	}

	/**
	 * {@inheritDoc}
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
		return Integer.class;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMaster() {
		return this.aivDelegate.isMaster();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetChanged() {
		this.aivDelegate.resetChanged();
	}

	/**
	 * {@inheritDoc}
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
	public boolean isNullOrEmptyValue(Enum<?> p_oObject) {
		return p_oObject == null || !this.isMissingImageReplacedByText
				&& NO_IMAGE == ((AndroidApplication)AndroidApplication.getInstance()).getAndroidIdByStringKey(
						ApplicationRGroup.DRAWABLE, this.imageNamePrefix + p_oObject.name().toLowerCase(Locale.getDefault()));
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
		return this.aivDelegate.isFilled();
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
		//Nothing to do
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
		//Nothing to do
	}

	@Override
	public void configurationUnsetError() {
		this.aivDelegate.configurationUnsetError();
	}

	@Override
	public void configurationSetError(String p_oError) {
		this.aivDelegate.configurationSetError(p_oError);
	}

	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		return this.aivDelegate.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder);
	}

	/**
	 *  {@inheritDoc}
	 *  @see InstanceStatable#superOnRestoreInstanceState(Parcelable)
	 */
	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		super.onRestoreInstanceState(p_oState);
	}
	/**
	 *  {@inheritDoc}
	 *  @see InstanceStatable#superOnSaveInstanceState(Parcelable)
	 */
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
			oToSave.putParcelable("parent", this.aivDelegate.onSaveInstanceState( super.onSaveInstanceState() ) );
			oToSave.putString("imageNamePrefix", this.imageNamePrefix);
			oToSave.putString("imageNameSuffix", this.imageNameSuffix);
			oToSave.putBoolean("isMissingImageReplacedByText", this.isMissingImageReplacedByText);
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
		this.aivDelegate.onRestoreInstanceState(savedState.getParcelable("parent"));
		this.imageNamePrefix = savedState.getString("imageNamePrefix");
		this.imageNameSuffix = savedState.getString("imageNameSuffix");
		this.isMissingImageReplacedByText = savedState.getBoolean("isMissingImageReplacedByText");
		this.aivDelegate.setFilled(true);
		
		this.computeDisplay();
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
