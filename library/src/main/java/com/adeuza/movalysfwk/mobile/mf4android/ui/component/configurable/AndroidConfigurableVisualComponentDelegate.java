package com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.InstanceStatable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.AbstractConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;


/**
 * <p>
 * Delegate for ConfigurableVisualComponent
 * </p>
 * 
 * <p>
 * Copyright (c) 2010
 * <p>
 * Company: Adeuza
 * 
 * @author smaitre
 * 
 */

public class AndroidConfigurableVisualComponentDelegate<VALUE> extends AbstractConfigurableVisualComponentDelegate<VALUE> {
	/** Nb méthodes max in cache */
	private static final int NB_MAX_MEDTHO_IN_CACHE = 1024 ;
	/** string resources prefix */
	private static final String STRING_RESOURCES_PREFIX = "@string/";
	/** method in cache */
	private static Map<String, Method> setMethod = new HashMap<>(NB_MAX_MEDTHO_IN_CACHE);

	/** indicates whether component is filled */
	private boolean filled = false;
	/** the current view */
	private View currentView = null;
	/** the current view cast in ConfigurableVisualComponent */
	private ConfigurableVisualComponent<VALUE> currentCvComponent = null;
	/**
	 * Indicates the holder fragment of this component. If this property is
	 * null, this component is not held by a fragment.
	 */
	protected String fragmentTag;
	/**
	 * component is lock when its configuration hide the component. It's
	 * impossible do display (unhide) component when it'is lock
	 */
	private List<ConfigurableVisualComponent<?>> componentsToHide = null;

	/** component is lock when its configuration hide the component. It's impossible do display (unhide) component when it'is lock */
	private boolean lock = false;

	/** mandatory flag */
	private boolean mandatoryFlag = false;
	/** Application */
	private AndroidApplication application = null;

	/**
	 * A weak reference to the ViewModel
	 */
	private WeakReference<ViewModel> vm;

	/** Attributs définis par XML */
	private Map<ConfigurableVisualComponent.Attribute,String> attributes = new HashMap<>();
	
	/** the component has rules */
	private boolean bHasRule = false;
	
	/**
	 * Constructs a new delegate
	 * 
	 * @param p_oCurrentView
	 *            the current view
	 */
	public AndroidConfigurableVisualComponentDelegate(View p_oCurrentView) {
		this.application = (AndroidApplication) Application.getInstance();
		this.setId(p_oCurrentView.getId());
		this.currentView = p_oCurrentView;
		this.currentCvComponent = (ConfigurableVisualComponent<VALUE>) p_oCurrentView;
		if (!setMethod.containsKey(this.currentView.getClass().getName())) {
			Method[] oMethods = p_oCurrentView.getClass().getMethods();
			Method oMethod = null;
			int i = oMethods.length - 1;
			while (i > 0) {
				oMethod = oMethods[i];
				if (oMethod.getName().equals("configurationSetValue")) {
					setMethod.put(this.currentView.getClass().getName(),
							oMethod);
					break;
				}
				i--;

			}
		}
		
		/*ClassAnalyse oAnalyse = Application.getInstance().getClassAnalyser().analyseScreen(this.currentView);
		List<Class<? extends Dataloader<?>>> oDataLoaders = oAnalyse.getDataLoaderForObject(this.currentView.getClass());
		if (oDataLoaders!=null) {
			for(Class<? extends Dataloader<?>> dataLoaderClass : oDataLoaders) {
				Application.getInstance().addDataLoaderListener(dataLoaderClass, this);
			}
		}
		List<Class<? extends BusinessEvent<?>>> oBusinessEvents = oAnalyse.getBusinessEventForObject(this.currentView.getClass());
		if (oBusinessEvents!=null) {
			for(Class<? extends BusinessEvent<?>> businessEventClass : oBusinessEvents) {
				Application.getInstance().addBusinessEventListener(businessEventClass, this);
			}
		}*/

	}

	public AndroidConfigurableVisualComponentDelegate(View p_oCurrentView, AttributeSet p_oAttrs) {
		this(p_oCurrentView);
		setCustomConverter(p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, Attribute.CONVERTER_NAME_ATTRIBUTE.getName()), p_oAttrs);
		setCustomFormatter(p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, Attribute.FORMATTER_NAME_ATTRIBUTE.getName()), p_oAttrs);
	}
	
	public AndroidApplication getAndroidApplication() {
		return this.application;
	}

	/**
	 * Set id of component
	 * 
	 * @param p_iId
	 *            the component's id
	 */
	public final void setId(int p_iId) {
		if (p_iId > -1) {
			String sId = this.application.getAndroidIdStringByIntKey(p_iId);
			this.setName(sId);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	// allow to use generics that can be auto cast
	@Override
	public void configurationSetValue(VALUE p_oObject) {
		this.currentCvComponent.configurationUnsetError();
		if (!this.currentCvComponent.hasRules()) {
			if (!this.isEdit() || !this.currentView.isEnabled()) {
				if (this.currentCvComponent.isNullOrEmptyValue(p_oObject)) {
					this.currentCvComponent.configurationHide(false);
				} else {
					this.currentCvComponent.configurationUnHide(false);
				}
			} else {
				this.currentCvComponent.configurationUnHide(false);
			}
		}
	}
	
	public VALUE getFormattedValue(VALUE p_oSourceValue) {
		if(this.customFormatter() != null && p_oSourceValue != null) {
			p_oSourceValue = (VALUE) this.customFormatter().format(p_oSourceValue);
		}
		return p_oSourceValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_t_sValues) {
		if (!this.isEdit() || !this.currentView.isEnabled()) {
			if (this.currentCvComponent.isNullOrEmptyCustomValues(p_t_sValues)) {
				this.currentCvComponent.configurationHide(false);
			} else {
				this.currentCvComponent.configurationUnHide(false);
			}
		} else {
			this.currentCvComponent.configurationUnHide(false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VALUE configurationGetValue() {
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
	public Class<?> getValueType() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetLabel(String p_sLabel) {
		if (this.currentView instanceof TextView && this.isLabel()) {
			((TextView) this.currentView).setText(p_sLabel);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationHide(boolean p_bLockModifier) {
		if (this.getConfiguration()==null || this.getConfiguration().isVisible()) {
			if (p_bLockModifier) {
				this.lock = true;
			}
			if (this.componentsToHide != null && this.isValue()) {
				for (ConfigurableVisualComponent<?> oComponent : this.componentsToHide) {
					oComponent.configurationUnsetError();
					((View) oComponent).setVisibility(View.GONE);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void hide() {
		if (this.getConfiguration() ==null || this.getConfiguration().isVisible()) {
			this.currentView.setVisibility(View.GONE);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnHide(boolean p_bLockModifier) {
		if (this.getConfiguration() ==null || this.getConfiguration().isVisible()) {
			if (p_bLockModifier) {
				this.lock = false;
			}
			if (!this.lock && this.componentsToHide != null && this.isValue()) {
				for (ConfigurableVisualComponent<?> oComponent : this.componentsToHide) {
					((View) oComponent).setVisibility(View.VISIBLE);
					oComponent.configurationUnsetError();
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unHide() {
		if (this.getConfiguration()==null || this.getConfiguration().isVisible()) {
			this.currentView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationPrepareHide(java.util.List)
	 */
	@Override
	public void configurationPrepareHide(
			List<ConfigurableVisualComponent<?>> p_oComponentsToHide) {
		this.componentsToHide = p_oComponentsToHide;
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getConfigurationSetValueMethod()
	 */
	@Override
	public Method getConfigurationSetValueMethod() {
		return setMethod.get(this.currentView.getClass().getName());
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationDisabledComponent()
	 */
	@Override
	public void configurationDisabledComponent() {
		this.currentView.setEnabled(false);
		this.currentView.setClickable(false);
		this.currentView.clearFocus();
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationEnabledComponent()
	 */
	@Override
	public void configurationEnabledComponent() {
		this.currentView.setEnabled(true);
		this.currentView.setClickable(true);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(VALUE p_oObject) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyCustomValues(String[] p_oObjects) {
		boolean r_bNullOrEmpty = p_oObjects == null || p_oObjects.length == 0;

		if (!r_bNullOrEmpty) {
			r_bNullOrEmpty = true;
			for (int iIndex = 0; r_bNullOrEmpty && iIndex < p_oObjects.length; iIndex++) {
				r_bNullOrEmpty = p_oObjects[iIndex] == null
						|| p_oObjects[iIndex].length() == 0;
			}
		}

		return r_bNullOrEmpty;
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.filled;
	}

	public void setFilled(boolean p_bFilled) {
		this.filled = p_bFilled;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return true;
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		this.mandatoryFlag = false;
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationSetMandatoryLabel()
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		this.mandatoryFlag = true;
	}

	/**
	 * Return true whether mandatory label is set
	 * @return true whether mandatory label is set
	 */
	public boolean isFlagMandatory() {
		return this.mandatoryFlag;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnPostAutoBind() {
		this.application.analyzeClassOf(this.currentView);
	}
	
	@Override
	public void destroy() {
		Application.getInstance().removeListenerFromDataLoader(this);
		Application.getInstance().removeListenerFromEventManager(this);
		Application.getInstance().removeListenerFromAction(this);
	}

	@Override
	public void configurationUnsetError() {
		
	}

	@Override
	public void configurationSetError(String p_oError) {
		// Nothing to do
	}

	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		return true;
	}

	/**
	 * 
	 * @param p_oInheritState
	 * 		le résultat de l'appel à super.onSaveInstanceState() exécuté à partir du composant.
	 * 
	 * @return un objet parcelable contenant la valeur de la propriété changed
	 */
	public Parcelable onSaveInstanceState(Parcelable p_oInheritState) {
		if (currentView instanceof InstanceStatable){
			Parcelable superState=((InstanceStatable)currentView).superOnSaveInstanceState();
			AndroidConfigurableVisualComponentSavedState oState=new AndroidConfigurableVisualComponentSavedState(superState);
			oState.setChanged(this.isChanged());
			oState.setVisibility(this.currentView.getVisibility());
			oState.setEnabled(this.currentView.isEnabled());
			oState.setHasRules(this.bHasRule);

			return oState;
		}
		else {
			Log.d(Application.LOG_TAG,"[onSaveInstanceState] "+currentView.getClass().getName()+" doit implémenter InstanceStatable !!!");
			return p_oInheritState;
		}
	}

	/**
	 * @param p_oState
	 * 		Le précédent état du composant courant.
	 */
	public void onRestoreInstanceState(Parcelable p_oState) {
		//Log.d("LBR","[AndroidConfigurableVisualComponentDelegate.onRestoreInstanceState] "+currentView.getClass().getName());

		if (currentView instanceof InstanceStatable){
			//Log.d("LBR","[AndroidConfigurableVisualComponentDelegate.onRestoreInstanceState] "+currentView.getClass().getName()+" implément InstanceStatable.");

			if(!(p_oState instanceof AndroidConfigurableVisualComponentSavedState)){
				//Log.d("LBR","[AndroidConfigurableVisualComponentDelegate.onRestoreInstanceState] "+currentView.getClass().getName()+" appel InstanceStatable.superOnRestoreInstanceState.");

				((InstanceStatable)currentView).superOnRestoreInstanceState(p_oState);
				return;
			}

			//Log.d("LBR","[AndroidConfigurableVisualComponentDelegate.onRestoreInstanceState] "+currentView.getClass().getName()+" implément AndroidConfigurableVisualComponentSavedState.");
			AndroidConfigurableVisualComponentSavedState savedState = (AndroidConfigurableVisualComponentSavedState) p_oState;

			// set the rules before calling super because super call configurationSetValue
			this.setHasRules(savedState.getHasRules());
			
			//Log.d("LBR","[AndroidConfigurableVisualComponentDelegate.onRestoreInstanceState] "+currentView.getClass().getName()+" appel AndroidConfigurableVisualComponentSavedState.superOnRestoreInstanceState.");
			// Etat sauvegardé par la classe mère
			((InstanceStatable)currentView).superOnRestoreInstanceState(savedState.getSuperState());

			//on repose l'état
			if (savedState.isChanged()) {
				this.changed();
			}
			else {
				this.resetChanged();
			}
			
			this.currentView.setVisibility(savedState.getVisibility());
			this.currentView.setEnabled(savedState.isEnabled());
		}
		else {
			//Log.d("LBR","[AndroidConfigurableVisualComponentDelegate.onRestoreInstanceState] "+currentView.getClass().getName()+" doit implémenter InstanceStatable !!!");

			Log.v("AndroidConfigurableVisualComponentSavedState","[onRestoreInstanceState] "+currentView.getClass().getName()+"doit implémenter InstanceStatable !!!");
		}
	}

	@Override
	protected void doOnVisualComponentChanged() {
		// ABE stuff to save value on VM
		if (this.vm != null && this.vm.get() != null) {
			ViewModel oVm = this.vm.get();
			oVm.readDataFromComponent(this.currentCvComponent);
			oVm.setDirectlyModified(true);
		} else {
			Log.w(Application.LOG_TAG, "doOnVisualComponentChanged() cannot write data for component "
				+ this.getName()+" with context : "+this.getCurrentView().getContext().getClass().getSimpleName());
		}
	}

	/**
	 * Retourne l'objet currentView
	 * @return Objet currentView
	 */
	public View getCurrentView() {
		return this.currentView;
	}

	@Override
	public void registerVM(ViewModel p_oViewModel) {
		this.vm = new WeakReference<>(p_oViewModel);
	}
	
	/**
	 * Unregister viewmodel
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#unregisterVM()
	 */
	@Override
	public void unregisterVM() {
		this.vm = null;
	}

	@Override
	public CustomConverter customConverter() {
		return this.customConverter;
	}

	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		// Nothing to do
	}

	public void setCustomConverter(String p_oCustomConverter, AttributeSet p_oAttributeValues) {
		if(p_oCustomConverter != null) {
			this.customConverter = (CustomConverter) BeanLoader.getInstance().getBeanByType(p_oCustomConverter);
		}
		if(this.customConverter != null) {
			String[] oConverterParameters = setAttributes(p_oAttributeValues, customConverter.getAttributesName());
			customConverter.setAttributes(oConverterParameters);
		}
	}

	/**
	 * sets attributes array values from AttributeSet on a given range of keys
	 * @param p_oAttributeValues AttributeSet to go through
	 * @param p_oAttributesNames attributes keys to find
	 * @return the array of values found
	 */
	private String[] setAttributes(AttributeSet p_oAttributeValues, String[] p_oAttributesNames) {
		String[] r_oParameters = new String[p_oAttributesNames.length];
		int index = 0;
		for(String sAttributeName : p_oAttributesNames) {
			String sParameterValue = p_oAttributeValues.getAttributeValue(Application.MOVALYSXMLNS, sAttributeName);
			if(sParameterValue != null) {
				r_oParameters[index] = convertParameterValue(sParameterValue);
			}
			index++;
		}
		return r_oParameters;
	}
	
	/**
	 * Tries to grab a resource in android strings from a given attribute
	 * @param p_sValue the attribute value
	 * @return the computed value
	 */
	private String convertParameterValue(String p_sValue) {
		String r_sValue = p_sValue;
		if (r_sValue.startsWith(STRING_RESOURCES_PREFIX)) {
			r_sValue = r_sValue.substring(STRING_RESOURCES_PREFIX.length());
			r_sValue = this.application.getStringResource(this.application.getAndroidIdByStringKey(ApplicationRGroup.STRING, r_sValue));
		}
		if(r_sValue == null){
			return p_sValue;
		}
		else {
			return r_sValue;
		}
	}
	
	@Override
	public CustomFormatter customFormatter() {
		return this.customFormatter;
	}

	@Override
	public void setCustomFormatter(CustomFormatter p_oFormatter) {
		// Nothing to do		
	}
	
	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeValues) {
		if(p_oCustomFormatter != null) {
			this.customFormatter = (CustomFormatter) BeanLoader.getInstance().instantiatePrototype(p_oCustomFormatter);
		}
		if(this.customFormatter != null) {
			String[] oConverterParameters = setAttributes(p_oAttributeValues, customFormatter.getAttributesName());
			customFormatter.setAttributes(oConverterParameters);
		}
	}
	
	/**
	 * The tag of the fragment container of this component, if exists.
	 * @return a tag or null
	 */
	@Override
	public String getFragmentTag() {
		return fragmentTag;
	}
	
	/**
	 * The tag of the fragment container of this component, if exists.
	 * @param fragmentTag a tag or null
	 */
	@Override
	public void setFragmentTag(String p_sFragmentTag) {
		this.fragmentTag = p_sFragmentTag;
	}
	
	/**
	 * Récupère et conserve les données du paramètrage XML du composant
	 * Spécifie le type de données lié et son validateur
	 * @param p_oAttrs paramétrage XML du composant
	 */
	public void defineParameters( AttributeSet p_oAttrs ){
		this.attributes = new HashMap<>();
		if ( p_oAttrs!= null ){
			for (ConfigurableVisualComponent.Attribute oAttribute : ConfigurableVisualComponent.Attribute.values() ){
				String sValue = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, oAttribute.getName()) ;
				if (sValue != null){
					this.attributes.put(oAttribute, convertParameterValue(sValue));
				}
			}
		}
	}
	
	public Map<ConfigurableVisualComponent.Attribute,String> getAttributes() {
		return this.attributes;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#hasRules()
	 */
	@Override
	public boolean hasRules() {
		return this.bHasRule;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#setHasRules()
	 */
	@Override
	public void setHasRules(boolean p_bHasRules) {
		this.bHasRule = p_bHasRules;
	}

}
