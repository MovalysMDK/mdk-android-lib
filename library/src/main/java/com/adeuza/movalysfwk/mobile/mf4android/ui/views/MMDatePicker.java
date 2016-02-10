
package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.common.AbstractUIRunnable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * component used to enter a date into a form
 * <b>Warning, this component takes up lot of space.</b>
 * MMDateTimeEditText can be used to maximize space and delegate inputs in a dialog
 * @author dmaurange
 *
 */
public class MMDatePicker extends DatePicker implements MMIdentifiableView, ConfigurableVisualComponent<Long>, OnDateChangedListener, InstanceStatable {

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<Long> aivDelegate = null;
	/** the date used by the component*/
	private Calendar calendar = null;

	private boolean writingData;

	/**
	 * @param p_oContext
	 */
	public MMDatePicker(Context p_oContext) {
		super(p_oContext);
		if (!isInEditMode()){
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this);
		}	
	}
	
	/**
	 * @param p_oContext
	 * @param attrs
	 */
	public MMDatePicker(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if (!isInEditMode()){
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
		}	
	}
	
	/**
	 * @param p_oContext
	 * @param attrs
	 * @param p_iDefStyle
	 */
	public MMDatePicker(Context p_oContext, AttributeSet p_oAttrs, int p_iDefStyle) {
		super(p_oContext, p_oAttrs, p_iDefStyle);
		if (!isInEditMode()){
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
		}	
	}
	
	/**
	 * called wen the inflator finished the job 
	 * {@inheritDoc}
	 */	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		this.calendar = Calendar.getInstance();
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
	public void configurationSetValue(Long p_oObject) {
		this.aivDelegate.configurationSetValue(p_oObject);

		if (p_oObject != null && !p_oObject.equals(-1L)) {
			this.calendar.setTimeInMillis(p_oObject);
		}

		this.writingData = true;
		this.init(this.calendar.get(Calendar.YEAR), this.calendar.get(Calendar.MONTH), this.calendar.get(Calendar.DAY_OF_MONTH), this);
		this.writingData = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDateChanged(DatePicker p_oDatePicker, int p_iYyear, int p_iMonthOfYear, int p_iDayOfMonth) {
		this.updateCalendar();
		if (!this.writingData) {
			this.aivDelegate.changed();
		}
	}

	/**
	 * Méthode privée pour instancier un nouveau Calendar avec les valeures du DatePicker courant.
	 * @return objet Calendar.
	 */
	private void updateCalendar(){
		this.calendar.set(this.getYear(), this.getMonth(), this.getDayOfMonth());
	}	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObject) {
		this.aivDelegate.configurationSetCustomValues(p_oObject);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long configurationGetValue() {

		//FwkControllerImpl oController = (FwkControllerImpl) Application.getInstance().getController();
		//MExtendedContextImpl oContext = (MExtendedContextImpl) oController.getCurrentContext();
		//boolean useTask = oContext!=null && oContext instanceof MExtendedContextImpl && (((MExtendedContextImpl) oContext).isLaunchedByActionTask());
		//permet de détecter qu'il y a eu un changement
		this.clearFocus();
		this.updateCalendar();
		return this.calendar.getTimeInMillis();
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
	public Class<?> getValueType() {
		return Long.class;
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
		configurationGetValue();
		return this.aivDelegate.isChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyValue(Long p_oObject) {
		return p_oObject==null;
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
	public void configurationPrepareHide(List<ConfigurableVisualComponent<?>> p_oComponentsToHide) {
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
		this.setEnabled(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationEnabledComponent() {
		this.aivDelegate.configurationEnabledComponent();
		this.setEnabled(true);

		//		// La modification de la valeur des champs sans passer par les boutons +/- n'aboutit pas à l'appel à onDateChanged.
		//		// Pour masquer cette anomalie, on rend les champs EditText non modifiables
		//		((ViewGroup) ((ViewGroup)this.getChildAt(0)).getChildAt(0)).getChildAt(1).setEnabled(false);
		//		((ViewGroup) ((ViewGroup)this.getChildAt(0)).getChildAt(1)).getChildAt(1).setEnabled(false);
		//		((ViewGroup) ((ViewGroup)this.getChildAt(0)).getChildAt(2)).getChildAt(1).setEnabled(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFilled() {
		return true;
	}

	/**
	 * {@inheritDoc}
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
		this.aivDelegate.configurationRemoveMandatoryLabel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		this.aivDelegate.configurationSetMandatoryLabel();
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
	 * @see android.widget.DatePicker#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		return this.aivDelegate.onSaveInstanceState(super.onSaveInstanceState());
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.DatePicker#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		this.aivDelegate.onRestoreInstanceState(p_oState);
	}


	/**
	 * Runnable to execute clearFocus() in the ui thread
	 * @author lmichenaud
	 *
	 */
	public static class ClearFocusRun extends AbstractUIRunnable {

		/**
		 * 
		 */
		private MMDatePicker datePicker = null;

		/**
		 * @param p_oMMDatePicker
		 */
		public ClearFocusRun(MMDatePicker p_oMMDatePicker) {
			this.datePicker = p_oMMDatePicker;
		}

		/**
		 * {@inheritDoc}
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void exec() {
			this.datePicker.clearFocus();
		}
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
