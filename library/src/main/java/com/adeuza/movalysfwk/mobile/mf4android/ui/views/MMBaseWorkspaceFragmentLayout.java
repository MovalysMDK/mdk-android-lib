package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.MMViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableLayoutComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.wrappers.MMWorkspaceAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableLayoutComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * @author lmichenaud
 * 
 * @param <VALUE>
 */
public abstract class MMBaseWorkspaceFragmentLayout<VALUE> extends MMViewPager
implements ConfigurableLayoutComponent<VALUE>, MMIdentifiableView, InstanceStatable {

	/**
	 * name of the MMWorkspaceView
	 */
	public static final String MAIN_MMWORKSPACEVIEW_NAME = "Main_MMWorkspaceView";

	/**
	 * the application to access to centralized resources
	 */
	private AndroidApplication application = (AndroidApplication) Application
			.getInstance();

	/**
	 * configurable view delegate
	 */
	private AndroidConfigurableLayoutComponentDelegate<VALUE> aivDelegate;

	/**
	 * Background resource id
	 */
	private int backgroundResId;

	/**
	 * 
	 */
	private Class<VALUE> valueClass;

	/**
	 * @param p_oContext
	 */
	public MMBaseWorkspaceFragmentLayout(Context p_oContext) {
		super(p_oContext);
		this.aivDelegate = new AndroidConfigurableLayoutComponentDelegate<>(
				this);

		init();
	}
	
	@Override
	public void requestChildFocus(View p_oChild, View p_oFocused) {
		super.requestChildFocus(p_oChild, p_oFocused);
	}

	/**
	 * @param p_oContext
	 * @param p_oAttrs
	 */
	public MMBaseWorkspaceFragmentLayout(Context p_oContext,
			AttributeSet p_oAttrs, Class<VALUE> p_oValueClass) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableLayoutComponentDelegate<>(
					this);
			this.backgroundResId = p_oAttrs.getAttributeResourceValue(
					AndroidApplication.MOVALYSXMLNS, "background", 0);
			this.valueClass = p_oValueClass;
			init();
		}
	}

	/**
	 * 
	 */
	public abstract void customInit();

	/**
	 * 
	 */
	public final void init() {

		this.initBackground();

		this.customInit();

	}

	/**
	 * @return
	 */
	public boolean isFirstScreenSelected() {
		return this.getCurrentItem() == 0;
	}

	/**
	 * 
	 */
	protected void initBackground() {
		if (this.backgroundResId > 0) {
			this.setBackgroundResource(this.backgroundResId);
		}
	}

	/**
	 * @return
	 */
	public AndroidApplication getAndroidApplication() {
		return this.application;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return this.isShown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
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
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyValue(VALUE p_oObject) {
		return p_oObject == null;
	}

	/**
	 * Hide the detail columns
	 * 
	 * @param p_iColumn
	 *            the number of the detail column to hide
	 * @param p_bNeedRecompute
	 *            if true, this method launch recomputeDisplay
	 */
	public void hideColumn(int p_iColumnIndex, boolean p_bNeedRecompute) {
		((MMWorkspaceAdapter) this.getAdapter()).hideColumn(p_iColumnIndex);
	}

	/**
	 * Unhide all detail columns
	 */
	public void unHideAllColumns(boolean p_bNeedRecompute) {
		((MMWorkspaceAdapter) this.getAdapter()).unHideAllColumns();
	}

	/**
	 * Hide all detail columns
	 */
	public void hideAllColumns(boolean p_bNeedRecompute) {

		((MMWorkspaceAdapter) this.getAdapter()).hideAllColumns();
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
	 */
	@Override
	public void hide() {
		this.aivDelegate.hide();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unHide() {
		this.aivDelegate.unHide();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationPrepareHide(
			List<ConfigurableVisualComponent<?>> p_oComponentsToHide) {
		this.aivDelegate.configurationPrepareHide(p_oComponentsToHide);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Method getConfigurationSetValueMethod() {
		return this.aivDelegate.getConfigurationSetValueMethod();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationDisabledComponent() {
		this.aivDelegate.configurationDisabledComponent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationEnabledComponent() {
		this.aivDelegate.configurationEnabledComponent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFilled() {
		return this.aivDelegate.isFilled();
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
	 */
	@Override
	public void configurationUnsetError() {
		this.aivDelegate.configurationUnsetError();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetError(String p_oError) {
		this.aivDelegate.configurationSetError(p_oError);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration,
			Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		return this.aivDelegate.validate(p_oConfiguration, p_mapParameters,
				p_oErrorBuilder);
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
	 */
	@Override
	public final String getModel() {
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
	public String[] configurationGetCustomValues() {
		return this.aivDelegate.configurationGetCustomValues();
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
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomDateTime(
			BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate
				.configurationAppendCustomDateTime(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(VALUE p_oVALUE) {
		this.aivDelegate.configurationSetValue(p_oVALUE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObjectsToSet) {
		this.aivDelegate.configurationSetCustomValues(p_oObjectsToSet);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VALUE configurationGetValue() {
		return this.aivDelegate.configurationGetValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomLabel(
			final BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate
				.configurationAppendCustomLabel(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomTextField(
			BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate
				.configurationAppendCustomTextField(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomCheckBoxes(
			BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate
				.configurationAppendCustomCheckBoxes(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomRadioButtons(
			BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate
				.configurationAppendCustomRadioButtons(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomComboBox(
			BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate
				.configurationAppendCustomComboBox(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomURL(
			BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate
				.configurationAppendCustomURL(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomEmail(
			BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate
				.configurationAppendCustomEmail(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomInteger(
			BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate
				.configurationAppendCustomInteger(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomDuration(
			BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate
				.configurationAppendCustomDuration(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomDate(
			BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate
				.configurationAppendCustomDate(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomTime(
			BasicComponentConfiguration p_oFieldConfiguration) {
		return this.aivDelegate
				.configurationAppendCustomTime(p_oFieldConfiguration);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableLayoutComponent#configurationAppendCustomPhone(com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration)
	 */
	@Override
	public ConfigurableVisualComponent<?> configurationAppendCustomPhone(
			BasicComponentConfiguration p_oConfiguration) {
		return this.aivDelegate
				.configurationAppendCustomPhone(p_oConfiguration);
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
	public void removeChild(ConfigurableVisualComponent<?> p_oChildToDelete) {
		this.aivDelegate.removeChild(p_oChildToDelete);
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
	public Class<?> getValueType() {
		return this.valueClass;
	}
	
	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.onSaveInstanceState();
		Bundle r_oBundle = new Bundle();

		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putInt("CurrentPage", this.getCurrentItem());

		return r_oBundle;
	}
	
	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		
		Bundle r_oBundle = (Bundle) p_oState ;

		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.onRestoreInstanceState(oParentParcelable);
		if (r_oBundle.getInt("CurrentPage") != 0 ) {
			this.unHideAllColumns(true);
			int page = this.getAdapter().getCount() / this.application.getScreenColumnNumber();
			if ( page -1 < r_oBundle.getInt("CurrentPage") ) {
				this.setCurrentItem(page-1);
			} else {
				this.setCurrentItem(r_oBundle.getInt("CurrentPage"));
			}
		}
		
	}
	
	@Override
	public Parcelable onSaveInstanceState() {
		return this.aivDelegate.onSaveInstanceState(super.onSaveInstanceState());
	}
	
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		this.aivDelegate.onRestoreInstanceState(p_oState);
	}
	
	/**
	 * {@inheritDoc}
	 * 
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
