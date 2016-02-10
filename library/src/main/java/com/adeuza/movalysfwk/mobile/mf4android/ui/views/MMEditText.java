package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.entity.AbstractEntityFieldConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.IDateFormatValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.IFormFieldValidator;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * <p>EditText widget used in the Movalys Mobile product for Android</p>
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 * @author dmaurange
 */

public class MMEditText extends EditText implements ConfigurableVisualComponent<String>, MMIdentifiableView, InstanceStatable {

	private static final int UPDATEVM_DELAY = 200;
	private static final int MESSAGE_TEXT_CHANGED = 0;

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<String> aivDelegate = null;
	/** Indique si on est en train de modifier le champ */
	private boolean writingData;
	/** Objet de validation de la valeur selon les paramètres liés au type de données */
	private IFormFieldValidator oValidator ;
	/** Id of the type conforms to */
	private AbstractEntityFieldConfiguration.DataType dataType = AbstractEntityFieldConfiguration.DataType.TYPE_STRING;
	/** Liste des surveillants d'écriture */
	private List<TextWatcher> textWatchers = null;

	private Handler mDelayTextChangedHandler = new Handler() {
		@Override
		public void handleMessage(Message p_oMsg) {
			if (p_oMsg.what == MESSAGE_TEXT_CHANGED) {
				MMEditText.this.aivDelegate.changed();
			}
		}
	};

	/**
	 * Constructs a new MMEditText
	 * @param p_oContext the context to use
	 * @see EditText#EditText(Context)
	 */
	public MMEditText(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this);
			this.writingData = false;
			this.defineParameters(null);
		}
	}	
	/**
	 * Constructs a new MMEditText
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see EditText#EditText(Context, AttributeSet)
	 */
	public MMEditText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
			this.writingData = false;
			this.defineParameters(p_oAttrs);
		}
	}

	/**
	 * Constructs a new MMEditText
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use
	 * @see EditText#EditText(Context, AttributeSet, int)
	 */
	public MMEditText(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			this.writingData = false;
			this.aivDelegate = new AndroidConfigurableVisualComponentDelegate<>(this, p_oAttrs);
			this.defineParameters(p_oAttrs);
		}
	}

	/**
	 * Récupère et conserve les données du paramètrage XML du composant
	 * Spécifie le type de données lié et son validateur
	 * @param p_oAttrs paramétrage XML du composant
	 */
	private void defineParameters( AttributeSet p_oAttrs ){
		this.aivDelegate.defineParameters(p_oAttrs);
		String sType = this.aivDelegate.getAttributes().get(ConfigurableVisualComponent.Attribute.DATA_TYPE_ATTRIBUTE) ;
		if (sType != null && sType.length() > 0) {
			this.dataType = AbstractEntityFieldConfiguration.DataType.valueOf( sType.toUpperCase(Locale.getDefault()) );
		}
		if ( this.dataType != null && !isInEditMode()){
			oValidator = BeanLoader.getInstance().getBean( this.dataType.getValidatorClass() ) ;
		}
	}

	/**
	 * called wen the inflator finished the job 
	 */	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()) {
			if (oValidator!= null){
				oValidator.addParametersToConfiguration( this.aivDelegate.getAttributes() , (BasicComponentConfiguration)this.getConfiguration() );
			}
		}
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
	 * Modifie le type paramétré et récupère les attribus adéquat pour le validateur
	 * @param p_iDataType nouvelle valeur du type paramétré
	 */
	public void setDataType(AbstractEntityFieldConfiguration.DataType p_iDataType) {
		this.dataType = p_iDataType;
		oValidator = BeanLoader.getInstance().getBean( this.dataType.getValidatorClass() ) ;
		oValidator.addParametersToConfiguration( this.aivDelegate.getAttributes() , (BasicComponentConfiguration)this.getConfiguration() );
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
	public void configurationSetValue(String p_oObject) {
		if(
				p_oObject != null && !p_oObject.equals(this.configurationGetValue()) 
				|| p_oObject == null && this.configurationGetValue() != null) {
			this.aivDelegate.configurationSetValue(p_oObject);
			this.writingData = true;
			if (p_oObject!=null) {
				int iOldStart = this.getSelectionStart();
				int iOldStop = this.getSelectionEnd();
				iOldStart = Math.min(iOldStart, p_oObject.length());
				iOldStop = Math.min(iOldStop, p_oObject.length());
				p_oObject = this.aivDelegate.getFormattedValue(p_oObject);
				this.setText(p_oObject);
				setEditTextSelection(this, iOldStart, iOldStop);
			}
			else {
				this.setText(StringUtils.EMPTY);
			}
			this.writingData = false;
		}
	}
	
	private void setEditTextSelection(EditText p_oEditText, int p_iStart, int p_iStop) {
		if (p_iStart != -1 && p_iStop != -1) {
			p_oEditText.setSelection(p_iStart, p_iStop);
		}
	}
	
	/**
	 * {@inheritDoc} 
	 */
	@Override
	public void configurationSetCustomValues(String[] p_oObject) {
		this.aivDelegate.configurationSetCustomValues(p_oObject);

		if (p_oObject == null || p_oObject.length == 0) {
			this.setText(StringUtils.EMPTY);
		} else {
			String sFirstValue = p_oObject[0];
			if (sFirstValue == null) {
				sFirstValue = StringUtils.EMPTY ;
			} else if ( sFirstValue.equalsIgnoreCase("0") && this.dataType.isDateFormatType() ){
				// définir la bonne date qui est relative
				sFirstValue =  this.computeDefaultDate((BasicComponentConfiguration)this.getConfiguration());
			}
			this.setText(sFirstValue);
		}
	}	
	/**
	 * Définit une date calculé selon la date du jour
	 * On incrémente ou décrémente la date selon le paramétrage et on prend l'heure et les minutes définies dans le paramétrage
	 * @param p_oBasicComponentConfiguration configuration du composant
	 * @return retourne la date par défaut calculée
	 */
	private String computeDefaultDate( BasicComponentConfiguration p_oBasicComponentConfiguration ) {
		Calendar oDefautltDate = Calendar.getInstance() ;
		// define relative day
		Object oDefaultIncrDay =  p_oBasicComponentConfiguration.getParameter(
				ConfigurableVisualComponent.Attribute.DEFAULT_INCR_DAY_ATTRIBUTE.getName());
		if ( oDefaultIncrDay != null ){
			try {
				oDefautltDate.add(Calendar.DATE, Integer.parseInt(oDefaultIncrDay.toString())) ;
			} catch (NumberFormatException oEx){
				Log.e(Application.LOG_TAG, oDefaultIncrDay.toString() 
						+" n'est pas un entier : le paramètre du chp perso '"
						+ ConfigurableVisualComponent.Attribute.DEFAULT_INCR_DAY_ATTRIBUTE.getName() + "' a une valeur erronée");
			}
		}
		// define hours
		Object oDefaultHours =  p_oBasicComponentConfiguration.getParameter(
				ConfigurableVisualComponent.Attribute.DEFAULT_HOURS_ATTRIBUTE.getName());
		if ( oDefaultHours != null ){
			try {
				oDefautltDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(oDefaultHours.toString())) ;
			} catch (NumberFormatException oEx){
				Log.e(Application.LOG_TAG, oDefaultHours.toString() 
						+" n'est pas un entier : le paramètre '" + ConfigurableVisualComponent.Attribute.DEFAULT_HOURS_ATTRIBUTE.getName() 
						+"' du chp perso '"+ this.aivDelegate.getConfigurationName() +"' a une valeur erronée");
			}
		}
		// define minutes
		Object oDefaultMinutes =  p_oBasicComponentConfiguration.getParameter(
				ConfigurableVisualComponent.Attribute.DEFAULT_MINUTES_ATTRIBUTE.getName());
		if ( oDefaultMinutes != null ){
			try {
				oDefautltDate.set(Calendar.MINUTE, Integer.parseInt(oDefaultMinutes.toString())) ;
			} catch (NumberFormatException oEx){
				Log.e(Application.LOG_TAG, oDefaultMinutes.toString() 
						+" n'est pas un entier : le paramètre "	+ ConfigurableVisualComponent.Attribute.DEFAULT_MINUTES_ATTRIBUTE.getName() 
						+"' du chp perso '"+ this.aivDelegate.getConfigurationName() +"' a une valeur erronée");
			}
		}
		DateFormat oFormat = ((IDateFormatValidator) oValidator).getDateFormat(p_oBasicComponentConfiguration); 
		if ( oFormat != null ) {
			return oFormat.format( oDefautltDate.getTime() );
		}
		return null ;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String configurationGetValue() {
		String oReturnValue = this.getText().toString().trim();
		if (oReturnValue.length() == 0) {
			oReturnValue = null;
		}
		if(oReturnValue != null && this.customFormatter() != null) {
			oReturnValue = (String) this.customFormatter().unformat(oReturnValue);
		}
		return oReturnValue;
	}
	/**
	 * {@inheritDoc}
	 * @see android.widget.EditText#getText()
	 */
	@Override
	public Editable getText() {
		// Dans le cas des nombres décimaux, android autorise la saisie ".1234" mais l'appel à Double.parseDouble(".1234") provoque une exception.
		// On corrige ce point au niveau composant, android ne gérant pas correctement le séparateur décimal en fonction de la local.
		Editable r_oEditable = super.getText();
		if (r_oEditable != null && r_oEditable.length() > 0 
				&& (this.getInputType() | InputType.TYPE_CLASS_NUMBER) != 0 
				&& (this.getInputType() | InputType.TYPE_NUMBER_FLAG_DECIMAL) != 0 
				&& r_oEditable.charAt(0) == '.' ) {
			r_oEditable.insert(0, "0");
		}
		return r_oEditable;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return new String[] {this.getText().toString()};
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
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return String.class;
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
		return this.aivDelegate.isChanged();
	}	
	/**
	 * {@inheritDoc}
	 * @see android.widget.TextView#onTextChanged(java.lang.CharSequence, int, int, int)
	 */
	@Override
	protected void onTextChanged(CharSequence p_oText, int p_oStart, int p_oBefore, int p_oAfter) {
		super.onTextChanged(p_oText, p_oStart, p_oBefore, p_oAfter);
		if (!this.writingData && this.aivDelegate!=null) {
			mDelayTextChangedHandler.removeMessages(MESSAGE_TEXT_CHANGED);
			final Message msg = Message.obtain(mDelayTextChangedHandler, MESSAGE_TEXT_CHANGED, p_oText);
			mDelayTextChangedHandler.sendMessageDelayed(msg, UPDATEVM_DELAY);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(String p_oObject) {
		return p_oObject==null || p_oObject.length() == 0;
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
		this.setFocusable(false);
		this.setFocusableInTouchMode(false);
	}	
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		this.aivDelegate.configurationEnabledComponent();
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnsetError() {
		if(this.getError() != null) {
			this.setError(null);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetError(String p_oError) {
		this.setError(p_oError);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		if (this.aivDelegate.getDescriptor().getTheoriticalDescriptor().getMinsize()!=0 
				&& this.isFilled()
				&& this.configurationGetValue() != null
				&& this.configurationGetValue().length()<this.aivDelegate.getDescriptor().getTheoriticalDescriptor().getMinsize() ) {
			if (p_oErrorBuilder.length()>0) {
				p_oErrorBuilder.append("\n");
			}
			p_oErrorBuilder.append(Application.getInstance().getStringResource(DefaultApplicationR.error_minsize1)).append(' ');
			p_oErrorBuilder.append(this.aivDelegate.getDescriptor().getTheoriticalDescriptor().getMinsize()).append(' ');
			p_oErrorBuilder.append(Application.getInstance().getStringResource(DefaultApplicationR.error_minsize2));
			return false;
		}

		return oValidator.validate(this, p_oConfiguration, p_oErrorBuilder);		
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		// !!! isEmpty() n'est pas implémentée sur android. 
		//Compile au niveau fwk mais NoSuchMethodException à l'exécution.
		return this.getText().toString().length()>0;
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
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		// Nothing to do
	}
	
	/**
	 * {@inheritDoc}
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

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
		//Nothing to do
	}


//	@Override
//	public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
//		AbstractMMActivity currentActivity = (AbstractMMActivity) ((AndroidApplication)AndroidApplication.getInstance()).getCurrentActivity();
//		if(this.getParentFragmentTags() != null && currentActivity != null &&
//				currentActivity instanceof AbstractWorkspaceMasterDetailMMFragmentActivity) {
//			AbstractWorkspaceMasterDetailMMFragmentActivity workspaceActivity = (AbstractWorkspaceMasterDetailMMFragmentActivity)currentActivity;
//			int componentPageIndex = 0;
//			//1. Récupérer le fragment dans lequel se trouve la vue via les tags
//			if(getParentFragmentTags() != null && getParentFragmentTags().size() >0) {
//				//2. Récupérer l'index du fragment dans le ViewPager
//				AbstractMMFragment pageFragment = (AbstractMMFragment) currentActivity.getSupportFragmentManager().findFragmentByTag(getParentFragmentTags().get(0));
//				if(pageFragment != null) {
//					componentPageIndex = workspaceActivity.getWadapter().getItemPositionByTag(pageFragment.getFragmentTag());
//				}
//			}
//			//3. Mettre le viewPager a la bonne page
//			workspaceActivity.getWlayout().setCurrentItem(componentPageIndex);
//		}
//		return super.requestFocus(direction, previouslyFocusedRect);
//	}


	@Override
	/**
	 * {@inheritDoc}
	 */
	public Parcelable superOnSaveInstanceState() {

		Parcelable superParcel = super.onSaveInstanceState();
		Bundle r_oBundle = new Bundle();

		r_oBundle.putParcelable("parentParcelable", superParcel);
		String valueToSave = this.getText().toString();
		if(this.customFormatter() != null) {
			valueToSave = (String) customFormatter().unformat(valueToSave);
		}
		r_oBundle.putString("edittext", valueToSave);

		return r_oBundle;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;

		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.onRestoreInstanceState(oParentParcelable);
		configurationSetValue(r_oBundle.getString("edittext"));
		
		this.writingData  = false;

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

		this.writingData = true;

		// On désactive les watchers, sinon leur méthode afterTextChanged sera invoquée
		// et ils seront marqués comme modifiés alors que nous sommes en mode restauration.
		List<TextWatcher> listBackupWatchers = null ;
		if ( this.textWatchers != null ) {
			listBackupWatchers = new ArrayList<>(this.textWatchers);
			this.clearTextChangedListeners();
		}

		//super.onRestoreInstanceState(((BaseSavedState) p_oState).getSuperState());
		this.aivDelegate.onRestoreInstanceState(p_oState);

		// Réactivation des watchers
		if ( listBackupWatchers != null ) {
			for( TextWatcher oWatcher: listBackupWatchers ) {
				this.addTextChangedListener(oWatcher);
			}
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
	 * @see android.widget.TextView#setInputType(int)
	 */
	@Override
	public void setInputType(int p_oType) {
		// Le setInputType provoque un changement sur le composant.
		// S'il n'y en avait pas avant, on reset le flag de changement
		final boolean bChanged = this.isChanged();
		super.setInputType(p_oType);
		if (!bChanged) {
			this.resetChanged();
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 * @see android.widget.TextView#addTextChangedListener(android.text.TextWatcher)
	 */
	@Override
	public void addTextChangedListener(TextWatcher p_oWatcher)
	{       
		if (textWatchers == null) 
		{
			textWatchers = new ArrayList<>();
		}
		textWatchers.add(p_oWatcher);

		super.addTextChangedListener(p_oWatcher);
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see android.widget.TextView#removeTextChangedListener(android.text.TextWatcher)
	 */
	@Override
	public void removeTextChangedListener(TextWatcher p_oWatcher)
	{       
		if (textWatchers != null) {
			textWatchers.remove(p_oWatcher);
		}
		super.removeTextChangedListener(p_oWatcher);
	}

	/**
	 *  Nettoie les inspecteurs de texte
	 */
	public void clearTextChangedListeners() {
		if(textWatchers != null)
		{
			for(TextWatcher oWatcher : textWatchers)
			{
				super.removeTextChangedListener(oWatcher);
			}

			textWatchers.clear();
			textWatchers = null;
		}
	}

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
