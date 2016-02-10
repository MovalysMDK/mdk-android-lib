package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.SectionTitle;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * <p>
 * 	Custom EditText widget used in the Movalys Mobile product for Android
 *	This component is used as Section Title and replace</p>
 *	
 * 		style="@style/interventiondetail_sectiontitle_text" 
 * 		android:gravity="center_vertical|center_horizontal"
 * 		android:layout_gravity="center_vertical|center_horizontal"
 * 		/> 
 *
 * <p>Copyright (c) 2010
 * <p>Company: Adeuza
 *
 * @author dmaurange
 *
 */

public class MMSectionTitle extends TextView implements ConfigurableVisualComponent<SectionTitle>, MMIdentifiableView, InstanceStatable {

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<SectionTitle> aivDelegate = null;

	private String defaultValue = "";
	/** si vrai on surcharge le background , si faux non c'ets le style qui le gère */
	private boolean withImageBackground = true ;

	/**
	 * Constructs a new MMSectionTitle
	 * @param p_oContext the context to use
	 * @see TextView#TextView(Context)
	 */
	public MMSectionTitle(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this);
		}
	}

	/**
	 * Constructs a new MMSectionTitle
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see TextView#TextView(Context, AttributeSet)
	 */
	public MMSectionTitle(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.init(p_oAttrs);
		}
	}

	/**
	 * Constructs a new MMSectionTitle
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use
	 * @see TextView#TextView(Context, AttributeSet, int)
	 */
	public MMSectionTitle(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			this.init(p_oAttrs);
		}
	}

	/**
	 * Initialize the default value of this title.
	 * @param p_oAttrs 
	 */
	protected final void init(AttributeSet p_oAttrs) {
		this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
		this.withImageBackground = p_oAttrs.getAttributeBooleanValue(AndroidApplication.MOVALYSXMLNS, "withImageBackground", false);
//		TypedArray a = this.getContext().obtainStyledAttributes(p_oAttrs,  /*R.styleable.MMSectionTitle*/, p_iDefStyle, 0);
//		this.withImageBackground = a.getBoolean(R.styleable.MMSectionTitle_withImageBackground, true);
//		a.recycle();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()){
//			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<SectionTitle>(this);

			this.setBackground(((AndroidApplication) Application.getInstance()).getAndroidIdByRKey(AndroidApplicationR.section_title_default));

			BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) this.getConfiguration();
			if (oConfiguration != null && oConfiguration.getLabel() != null) {
				this.defaultValue = oConfiguration.getLabel();
			}
		}
	}
	/**
	 * Initialise the background resource of the component
	 * @param p_iResourceId the resource id
	 */
	private final void setBackground(int p_iResourceId){
		if ( this.withImageBackground ) {
			setBackgroundResource(p_iResourceId);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(SectionTitle p_oObjectToSet) {
		this.aivDelegate.configurationSetValue(p_oObjectToSet);
		if (p_oObjectToSet!=null) {
			this.setText(this.defaultValue + p_oObjectToSet.getTitle());

			if (this.withImageBackground && p_oObjectToSet.getBackground() != null) {
				this.setBackgroundResource((Integer)Application.getInstance().getImageByName(p_oObjectToSet.getBackground()));
			}
		}
		else {
			this.setText(StringUtils.EMPTY);
			if ( this.withImageBackground ) {
				this.setBackgroundResource(0);
			}
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
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMValueableView#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return String.class;
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
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getConfiguration()
	 */
	@Override
	public VisualComponentConfiguration getConfiguration() {
		return this.aivDelegate.getConfiguration();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isGroup()
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
	public SectionTitle configurationGetValue() {
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
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationSetLabel(java.lang.String)
	 */
	@Override
	public void configurationSetLabel(String p_sLabel) {
		this.aivDelegate.configurationSetLabel(p_sLabel);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isLabel()
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
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isValue()
	 */
	@Override
	public boolean isValue() {
		return this.aivDelegate.isValue();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isEdit()
	 */
	@Override
	public boolean isEdit() {
		return this.aivDelegate.isEdit();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getLocalisation()
	 */
	@Override
	public String getLocalisation() {
		return this.aivDelegate.getLocalisation();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getModel()
	 */
	@Override
	public String getModel() {
		return this.aivDelegate.getModel();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getDescriptor()
	 */
	@Override
	public RealVisualComponentDescriptor getDescriptor() {
		return this.aivDelegate.getDescriptor();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setDescriptor(com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.descriptor.RealVisualComponentDescriptor)
	 */
	@Override
	public void setDescriptor(RealVisualComponentDescriptor p_oDescriptor) {
		this.aivDelegate.setDescriptor(p_oDescriptor);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isPanel()
	 */
	@Override
	public boolean isPanel() {
		return this.aivDelegate.isPanel();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isUnknown()
	 */
	@Override
	public boolean isUnknown() {
		return this.aivDelegate.isUnknown();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getName()
	 */
	@Override
	public String getName() {
		return this.aivDelegate.getName();
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
	public boolean isMaster() {
		return this.aivDelegate.isMaster();
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
	public boolean isNullOrEmptyValue(SectionTitle p_oObject) {
		return p_oObject==null || p_oObject.getTitle() == null && (( this.withImageBackground && p_oObject.getBackground() == null) || !this.withImageBackground );
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
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		// Nothing to do
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationSetMandatoryLabel()
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
	 * @see android.widget.TextView#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		return this.aivDelegate.onSaveInstanceState(super.onSaveInstanceState());
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.TextView#onRestoreInstanceState(android.os.Parcelable)
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

	public void setWithImageBackground(boolean p_bWithImageBackground) {
		this.withImageBackground = p_bWithImageBackground;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		this.aivDelegate.setCustomConverter(p_oConverter);
	}
	
	/**
	 * {@inheritDoc}
	 */
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
