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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model;

import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.LaunchParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.ContenerDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ExtFwkErrors;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ItfMessage;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.AbstractComponentWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.clonable.NonClonableViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.listener.businessrule.PropertyTarget;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * <p>Defines a view model object. Registers all the visual components for data binding.</p>
 */
@SuppressWarnings("serial")
public abstract class AbstractViewModel implements ViewModel {
	
	/** view model data object */
	private ViewModelDataObject vmDataObject;
	
	/** view model notification delegate */
	private ViewModelNotificationDelegate vmNotificationDelegate;

	/** view model data manipulation delegate */
	private ViewModelDataManipulationDelegate vmDataManipulationDelegate;
	
	/** editable */
	private boolean editable = true;
	
	/** value to known if the view is directly modified */
	private boolean directlyModified = false;
	
	/** the parent view model */
	private AbstractViewModel parent = null;
	
	/** read current vm */
	private String readCurrentVm ;
	
	/** true if update value is needed */
	private boolean updatedValue = false ;
	
	/** rule enable / disable ViewModel */
	private ComputeRuleEditable ruleEditableFlag = null;
	
	/** key for loading and cache */
	private String key = Dataloader.DEFAULT_KEY;

	/** cache map for view model fields */
	private Map<String, Field> mapCacheFields;

	/** delegates attributes */
	private Map<Integer, Map<String, Object>> delegatesAttributes;
	
	/**
	 * <p>
	 *  Builds an <em>AbstractViewModel</em> object.
	 * </p>
	 */
	public AbstractViewModel() {
		this.mapCacheFields = new HashMap<String, Field>();
		this.vmDataObject = new ViewModelDataObject();
		this.vmNotificationDelegate = new ViewModelNotificationDelegate(this);
		this.vmNotificationDelegate.analyseClass(this.getClass());
		
		this.vmDataManipulationDelegate = new ViewModelDataManipulationDelegate(this);
		
		this.delegatesAttributes = new HashMap<Integer, Map<String, Object>>();
	}

	/**
	 * Notify the field with new content value
	 *
	 * @param p_sKey key of the field in the view model
	 * @param p_oOldVal old content of the field
	 * @param p_oNewVal new content of the field
	 */
	protected void notifyFieldChanged(String p_sKey, Object p_oOldVal, Object p_oNewVal) {
		this.notifyFieldChanged(p_sKey, p_oOldVal, p_oNewVal, this.vmDataObject.getLastKnownValidationByComponentKey(p_sKey));
	}
	
	/**
	 * Notify the field with new content value and {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ValidationState} of the component
	 *
	 * @param p_sKey key of the field in the view model
	 * @param p_oOldVal old content of the field
	 * @param p_oNewVal new content of the field
	 * @param p_oComponentValidation {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ValidationState} of the field
	 */
	protected void notifyFieldChanged(String p_sKey, Object p_oOldVal, Object p_oNewVal, ValidationState p_oComponentValidation) {
		this.vmNotificationDelegate.notifyFieldChanged(p_sKey, p_oOldVal, p_oNewVal, p_oComponentValidation);
	}

	/**
	 * Invokes the method to notify when collection has changed.
	 *
	 * @param p_oSubVm Sub view model to use for method invocation
	 * @param p_eAction action to use for method invocation
	 * @param p_iIdItem item id of the modified object to use for method invocation
	 * @param p_oNewOrCurrentOrDeletedObject object to modify to use for method invocation
	 */
	protected void notifyCollectionChanged(ViewModel p_oSubVm, Action p_eAction, int p_iIdItem, ViewModel p_oNewOrCurrentOrDeletedObject) {
		this.vmNotificationDelegate.notifyCollectionChanged(p_oSubVm, p_eAction, p_iIdItem, p_oNewOrCurrentOrDeletedObject);
	}

	/**
	 * Clear data in the sub list
	 */
	public void clearSubData() {
		this.vmDataObject.clearSubData();
	}

	/**
	 * Construct an object <em>AbstractViewModel</em> with his parent as a parameter.
	 *
	 * @param p_oParent
	 * 		The parent view model
	 */
	public AbstractViewModel(AbstractViewModel p_oParent) {
		this();
		this.parent = p_oParent;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Clone view model attributes to current object
	 */
	@Override
	public void cloneVmAttributes(ViewModel p_oVmToClone) {
		if (p_oVmToClone != null && this.getClass().equals(p_oVmToClone.getClass()) ) {
			for (Field oField : p_oVmToClone.getClass().getDeclaredFields()) {				
				this.cloneField(p_oVmToClone, oField);
			}
			if (this instanceof AbstractItemViewModelId && p_oVmToClone instanceof AbstractItemViewModelId) {
				((AbstractItemViewModelId)this).setId_id(((AbstractItemViewModelId)p_oVmToClone).getId_id());
			}
			if (this instanceof AbstractItemViewModelIdentifier && p_oVmToClone instanceof AbstractItemViewModelIdentifier) {
				((AbstractItemViewModelIdentifier)this).setId_identifier(((AbstractItemViewModelIdentifier)p_oVmToClone).getId_identifier());
			}
		} else {
			if(p_oVmToClone == null) {
				Application.getInstance().getLog().error(Application.LOG_TAG, "impossible to clone a null ViewModel");
			}
			else {
				Application.getInstance().getLog().error(Application.LOG_TAG, "Impossible to clone :"+this.getClass().getName()+" in class :"
						+ p_oVmToClone.getClass().getName() + "The source and target classes are different");
			}

		}

	}
	
	/**
	 * Clones a field
	 * @param p_oVmToClone View model behind the field
	 * @param p_oField field to clone
	 */
	private void cloneField(ViewModel p_oVmToClone, Field p_oField) {
		try {
			p_oField.setAccessible(true);
			Object oValue = p_oField.get(p_oVmToClone);
			if ( oValue != null && ( ViewModel.class.isAssignableFrom(oValue.getClass()) 
					|| ListViewModel.class.isAssignableFrom(oValue.getClass()) )) {
				try {
					Annotation oNonClonableAnnotation = p_oField.getAnnotation(NonClonableViewModel.class);
					if(oNonClonableAnnotation != null) {
						p_oField.set(this, (ViewModel) oValue);
					}
					else {								
						ViewModel oVMToSave = (ViewModel) oValue.getClass().getConstructor().newInstance();
						oVMToSave.cloneVmAttributes((ViewModel) oValue);
						p_oField.set(this, oVMToSave);
					}
	
				} catch (InstantiationException | InvocationTargetException | NoSuchMethodException e) {
					Application.getInstance().getLog().error(Application.LOG_TAG, "AbstractViewModel Exception", e);
				}
	
			} else {
				p_oField.set(this, p_oField.get(p_oVmToClone));
			}
		} catch (IllegalArgumentException | SecurityException | IllegalAccessException e1) {
			Application.getInstance().getLog().error(Application.LOG_TAG, "AbstractViewModel Exception", e1);
		}
	}

	/**
	 * Getter for is always notify
	 *
	 * @return true is the notif are active false otherwise
	 */
	public boolean isAlwaysNotify() {
		return this.vmDataObject.isAlwaysNotify();
	}

	/**
	 * Setter for notify
	 *
	 * @param p_bAlwaysNotify the value to set
	 */
	public void setAlwaysNotify(boolean p_bAlwaysNotify) {
		this.vmDataObject.setAlwaysNotify(p_bAlwaysNotify);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Set editable flag
	 */
	@Override
	public void setEditable(boolean p_bEditable) {
		this.editable = p_bEditable;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Define the "editable" attribute using a context and some parameters.
	 * This method calls {@link #computeEditableFlag(MContext, Map)} to deduce the value of the editable attribute.
	 */
	@Override
	public void setEditable(final MContext p_oContext, final Map<String, Object> p_mapParameters) {
		this.setEditable(this.computeEditableFlag(p_oContext, p_mapParameters));
	}

	/**
	 * Computes the editable attribute of the class
	 *
	 * @param p_oContext context to use
	 * @param p_mapParameters parameters map
	 * @return true if it should be editable
	 */
	protected boolean computeEditableFlag(final MContext p_oContext, final Map<String, Object> p_mapParameters) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 *
	 * register a component
	 */
	@Override
	public void register(ConfigurableVisualComponent p_oComponent) {
		this.vmDataObject.addComponentToList(p_oComponent);
		p_oComponent.getComponentFwkDelegate().registerVM(this);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Unregister a component (in writable, readable and hidable register)
	 */
	@Override
	public void unregister(ConfigurableVisualComponent p_oComponent) {
		String sKey = p_oComponent.getComponentFwkDelegate().getDescriptor().getCompleteViewModelPath();
		this.vmDataObject.removeKeyFromAllMaps(sKey, p_oComponent);
		if (this.parent!=null) {
			this.parent.vmDataObject.removeWriteSubDataComponentByKey(sKey);
		}
		this.vmDataObject.removeWrapperByKey(p_oComponent.getComponentFwkDelegate().getName());
		p_oComponent.getComponentFwkDelegate().unregisterVM();
	}

	/**
	 * {@inheritDoc}
	 *
     * Unregister a component (in writable, readable and hidable register)
     */
    @Override
    public void unregister() {
        this.vmDataObject.clear();
        if (this.parent != null) {
            this.parent.vmDataObject.clear();
        }
    }

    /**
     * {@inheritDoc}
     *
	 * Register a writable component, the parent is used in list case
	 */
	@Override
	public void registerWritableDataComponents(ConfigurableVisualComponent p_oComponent) {
		this.vmDataObject.addWriteDataComponentToList(p_oComponent);

		p_oComponent.getComponentFwkDelegate().registerVM(this);

		if (this.parent!=null) {
			this.parent.vmDataObject.addWriteSubDataComponentToList(p_oComponent);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * Register a readable component
	 */
	@Override
	public void registerReadableDataComponents(ConfigurableVisualComponent p_oComponent) {
		this.vmDataObject.addReadDataComponentToList(p_oComponent);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Register a hidable component
	 */
	@Override
	public void registerHidableDataComponents(ConfigurableVisualComponent p_oComponent) {
		this.vmDataObject.addHideComponentToList(p_oComponent);
	}

	/** {@inheritDoc} */
	@Override	
	public Map<String,Object> beforeConfigurationChanged(){
		return new HashMap<String, Object>();
	}

	/** {@inheritDoc} */
	@Override
	public void afterConfigurationChanged(){
		// Nothing to do
	}

	/**
	 * Indicates whether the model is ready to changed (view has changed, but not the view model)
	 *
	 * @return true if it is ready to change
	 */
	@Override
	public boolean isReadyToChanged() {
		return this.isDirectlyModified();
	}

	/**
	 * Reset the change indicator
	 */
	@Override
	public void resetChangedIndicator() {
		ConfigurableVisualComponent oComponent = null;
		Iterator<ConfigurableVisualComponent> iterComponents = null;
		for(Entry<String, List<ConfigurableVisualComponent>> oEntry : this.vmDataObject.getWriteDataComponents().getEntrySet()) {
			iterComponents = oEntry.getValue().iterator();
			while(iterComponents.hasNext()) {
				oComponent = iterComponents.next();
				oComponent.getComponentFwkDelegate().resetChanged();
			}
		}
		for(Entry<String, List<ConfigurableVisualComponent>> oEntry : this.vmDataObject.getWriteSubDataComponents().getEntrySet()) {
			iterComponents = oEntry.getValue().iterator();
			while(iterComponents.hasNext()) {
				oComponent = iterComponents.next();
				oComponent.getComponentFwkDelegate().resetChanged();
			}
		}
		this.directlyModified = false;
	}

	/**
	 * Validate a component
	 * @param p_oComponent the component to validate
	 * @param p_oParameters the params
	 * @return {@link ValidationState} the validation state of the component
	 */
	ValidationState validComponent(ConfigurableVisualComponent p_oComponent, Map<String, Object> p_oParameters) {
		StringBuilder builderError = new StringBuilder();
		boolean bLocalError = false;
		ValidationState r_oValidation = ValidationState.INIT;

		if (p_oComponent != null && p_oComponent.getComponentFwkDelegate().getConfiguration() instanceof BasicComponentConfiguration) {
			BasicComponentConfiguration oBasicConfiguration = (BasicComponentConfiguration) p_oComponent.getComponentFwkDelegate().getConfiguration();
			if ( (p_oComponent.getComponentFwkDelegate().getDescriptor().getTheoriticalDescriptor().isMandatory()
				&& (!p_oComponent.getComponentDelegate().isFilled() && p_oComponent.getComponentFwkDelegate().isVisible()))) {
				builderError.append(Application.getInstance().getStringResource(DefaultApplicationR.error_mandatory));
				bLocalError = true;
				r_oValidation = ValidationState.EMPTY_MANDATORY;
			}
			boolean bValid = p_oComponent.getComponentDelegate().validate(oBasicConfiguration, p_oParameters, builderError);
			if (!bValid) {
				r_oValidation = ValidationState.ERROR;
			} else if (r_oValidation == ValidationState.INIT) {
				r_oValidation = ValidationState.VALID;
			}
			bLocalError = bLocalError || !bValid;
			if (bLocalError) {
				p_oComponent.getComponentDelegate().configurationSetError(builderError.toString());
			} else if (this.getVmDataObject().fieldHasUserError(p_oComponent)){
				this.getVmDataObject().refreshUserErrorOnComponent(p_oComponent);
				r_oValidation = ValidationState.USER_ERROR;
			} else {
				p_oComponent.getComponentDelegate().configurationUnsetError();
			}
			
			vmDataObject.updateComponentValidationStatus(p_oComponent.getComponentFwkDelegate().getDescriptor().getTheoriticalDescriptor().getModel(), r_oValidation);
		}
		
		if (r_oValidation.isError()) {
			Application.getInstance().getLog().error(Application.LOG_TAG, "Le composant " + p_oComponent.getComponentFwkDelegate().getName() + " n'est pas valide");
		}
		return r_oValidation;
	}


	/**
	 * Validates all components in a container
	 * @param p_oContext context to use
	 * @param p_oParameters parameters to use
	 * @param p_oContener container to inspect
	 * @param p_sGroup name of the group that should contain the components
	 * @return true if the component is valid
	 */
	private boolean validComponents(MContext p_oContext, Map<String, Object> p_oParameters, ContenerDelegate<ConfigurableVisualComponent> p_oContener, String p_sGroup) {

		Iterator<ConfigurableVisualComponent> iterComponents = null;
		ConfigurableVisualComponent oComponent = null;
		boolean bError = false;

		// TODO : a améliorer
		boolean bUseTask = false;
		
		if (p_oContext != null) {
			try {
				Method oIsLaunchedByAction = p_oContext.getClass().getMethod("isLaunchedByActionTask");
				boolean bIsAction = (boolean) oIsLaunchedByAction.invoke(p_oContext);
				
				bUseTask = bIsAction;
			} catch (NoSuchMethodException | SecurityException
					| IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// Si on ne trouve pas, bUseTask est à false car pas originaire d'une task
				Application.getInstance().getLog().debug("validComponents", e.getMessage());
			}
		}

		if (bUseTask) {
			LaunchParameter oLaunchParameter = null;
			try {
				Method oLaunchParameterMethod = p_oContext.getClass().getMethod("getLaunchParameter");
				oLaunchParameter = (LaunchParameter) oLaunchParameterMethod.invoke(p_oContext);
			} catch (NoSuchMethodException | SecurityException
					| IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				Application.getInstance().getLog().error("validComponents", e.getMessage(), e);
			}
			
			MMActionTask<?, ?, ?, ?> oTask = oLaunchParameter.getTask();

			ViewModelValidateRunner oProgress = new ViewModelValidateRunner(this, p_oParameters);
			oTask.publishActionRunnableProgress(oProgress, true);

			// Le Thread courant est mis en attente par la MMActionTask (cf. booléen fournit en paramètre du publish)
			// et n'est réveillé que lorsque le traitement effectué par le Progress est terminé (cf. AbstractUIRunnable)
			// Il est donc possible de récupérer le résultat de l'exécution du progress.
			bError = oProgress.hasError();
		} else {
			for(Entry<String, List<ConfigurableVisualComponent>> oEntry : p_oContener.getEntrySet()) {
				iterComponents = oEntry.getValue().iterator();
				while(iterComponents.hasNext()) {
					oComponent = iterComponents.next();
					if (p_sGroup == null || oComponent.getComponentFwkDelegate().getDescriptor().getTheoriticalDescriptor().isInGroup(p_sGroup)) {
						boolean bValidComponent = this.validComponent(oComponent, p_oParameters).isError();
						bError = bError || bValidComponent;
					}
				}
			}
		}
		return bError;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Validate the data
	 */
	@Override
	public boolean validComponents(MContext p_oContext, Map<String, Object> p_oParameters) {
		return validComponents(p_oContext, p_oParameters, null);
	}

	/**
	 * validate data for components in group p_sGroup
	 *
	 * @param p_oContext context to use
	 * @param p_oParameters parameters to use
	 * @param p_sGroup group to use
	 * @return true wether data is valid
	 */
	public boolean validComponents(MContext p_oContext, Map<String, Object> p_oParameters, String p_sGroup) {
		//avant de lire les données on vérifie le caractère obligatoire des infos
		boolean bError = this.validComponents(p_oContext, p_oParameters, this.vmDataObject.getWriteDataComponents(), p_sGroup);
		bError = this.validComponents(p_oContext, p_oParameters, this.vmDataObject.getWriteSubDataComponents(), p_sGroup) || bError; // bien mettre le ou à la fin pour que la méthode 1 s'exécute

		return bError;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Valid the view model
	 */
	@Override
	public boolean validViewModel(final MContext p_oContext, final Map<String, Object> p_oParameters) {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public boolean validateUserErrors(MContext p_oContext) {
		boolean bValid = true;
		if (this.getVmDataObject().hasUserError()) {
			p_oContext.getMessages().addMessage(ExtFwkErrors.InvalidViewModelDataError);
			bValid = false;
		}
		return bValid;
	}

	/** {@inheritDoc} */
	@Override
	public boolean readDataFromComponent(ConfigurableVisualComponent p_oComponent) {
		return this.readDataFromComponent(p_oComponent, null);
	}

	/** {@inheritDoc} */
	@Override
	public boolean readDataFromComponent(ConfigurableVisualComponent p_oComponent, Map<String, Object> p_oParameters) {

		updatedValue = false;
		readCurrentVm = p_oComponent.getComponentFwkDelegate().getModel();
		ValidationState oCompValid = this.validComponent(p_oComponent, p_oParameters);

		if (oCompValid.doNotify()) {
			// erase value in error map
			
			// on suit le process donnée valide si :
			// 1/ la donnée est valide
			// 2/ si la donnée est obligatoire mais vide
			this.vmDataObject.removeFieldErrorByKey(p_oComponent.getComponentFwkDelegate().getDescriptor().getCompleteViewModelPath());
			this.vmDataManipulationDelegate.readValidDataFromComponent(p_oComponent);
		} else {
			// write data in error map
			this.vmDataManipulationDelegate.readInvalidDataFromComponent(p_oComponent);
		}

		updatedValue = false;
		readCurrentVm = null;

		return !oCompValid.isError();
	}

	/** {@inheritDoc} */
	@Override
	public void doOnDataLoaded(Map<String, Object> p_mapParameters) {

		for(Entry<String, List<ConfigurableVisualComponent>> oEntry : this.vmDataObject.getReadDataComponents().getEntrySet()) {
			if (Application.getInstance().isOnUIThread()) {
				this.writeDataToComponent(oEntry.getKey());
			}
			else{
				(Application.getInstance()).runOnUiThread(new WriteDataRunnable(this, oEntry.getKey()));
			}
		}
		
		this.vmNotificationDelegate.executeCachedListeners(this);
	}

	/** {@inheritDoc} */
	@Override
	public void writeDataToComponent(String p_sPath) {

		ConfigurableVisualComponent oComponent = null;
		if (this.vmDataObject.getReadDataComponents().get(p_sPath) != null) {
			for(ConfigurableVisualComponent lComp : this.vmDataObject.getReadDataComponents().get(p_sPath)){
				if (!lComp.getComponentFwkDelegate().isLabel())  {
					oComponent = lComp;
					// get the list of rule for this field
					Map<PropertyTarget, List<Method>> oRules = this.getVmDataObject().getBusinessRuleRegistry().getRulesWithField(p_sPath);
					if (oRules != null) {
						// get the affected components
						List<ConfigurableVisualComponent> oAffectedListComp = this.vmDataObject.getComponents().get(p_sPath);
						
						for (ConfigurableVisualComponent oAffectedCompoent : oAffectedListComp) {
							if (!oAffectedCompoent.getComponentFwkDelegate().hasRules()) {
								oAffectedCompoent.getComponentFwkDelegate().setHasRules(true);
							}
						}
						
						//
						applyRulesOnComponents(PropertyTarget.HIDDEN, oRules, oAffectedListComp);
						
						applyRulesOnComponents(PropertyTarget.MANDATORY, oRules, oAffectedListComp);
						
					}
					if (this.vmDataObject.getFieldsErrorMap().containsKey(p_sPath)) {
						//ABE case that the component is in error map

						this.writeInvalidDataForComponent(oComponent, this.vmDataObject.getFieldsErrorMap().get(p_sPath));

					} else {
						this.writeDataForComponent(p_sPath, oComponent);
					}
				}
			}
		}
	}

	/**
	 * Apply a list of rules to a list of components 
	 * @param p_oProp property target to use
	 * @param p_oRules rules to apply
	 * @param p_oAffectedListComp components list to analyse
	 */
	@SuppressWarnings("unchecked")
	void applyRulesOnComponents(PropertyTarget p_oProp, Map<PropertyTarget, List<Method>> p_oRules, List<ConfigurableVisualComponent> p_oAffectedListComp) {
		List<Method> propertyRules = p_oRules.get(p_oProp);
		if (propertyRules != null && !propertyRules.isEmpty()) {
			boolean bPropResult = false;
			for (Method method : propertyRules) {
				try {
					bPropResult = bPropResult || (Boolean) method.invoke(this);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					Application.getInstance().getLog().error(Application.LOG_TAG, "[AbstractViewModel.applyRulesOnComponents] InstantiationException !", e);
				}
			}
			for (ConfigurableVisualComponent configurableVisualComponent : p_oAffectedListComp) {
				p_oProp.getComputeHelper().applyRuleForComponent(configurableVisualComponent, bPropResult);
			}
			
		}
	}

	/**
	 * write invalid data to a component
	 *
	 * @param p_oComponent component to modify
	 * @param p_oInvalideValue valud to write
	 */
	protected void writeInvalidDataForComponent(ConfigurableVisualComponent p_oComponent, Object p_oInvalideValue) {
		this.vmDataManipulationDelegate.writeInvalidDataForComponent(p_oComponent, p_oInvalideValue);
	}

	/**
	 * Write data for component p_oComponent
	 *
	 * @param p_sPath path to look for components
	 * @param p_oComponent component to write
	 */
	protected void writeDataForComponent(String p_sPath, ConfigurableVisualComponent p_oComponent) {
		this.vmDataManipulationDelegate.writeDataForComponent(p_sPath, p_oComponent);
	}

	/**
	 * Exec rules for the specific key
	 *
	 * @param p_sKey the key to compute rules
	 * @param p_oPropertyTargets the rules to compute if empty all rules are computed
	 */
	public void execRules(String p_sKey, PropertyTarget... p_oPropertyTargets) {
		this.vmNotificationDelegate.execRules(p_sKey, p_oPropertyTargets);
	}
	
	/**
	 * Exec all rules for the target
	 *
	 * @param p_oPropertyTargets the target to exec rules if empty all rules are computed
	 */
	public void execRules(PropertyTarget... p_oPropertyTargets) {
		this.execRules(null, p_oPropertyTargets);
	}
	
	/**
	 * Returns true whether model is editable
	 *
	 * @return true whether model is editable
	 */
	@Override
	public boolean isEditable() {
		return this.editable && (this.parent == null || this.parent.isEditable());
	}

	/**
	 * <p>Return the boolean value to known if the view is directly modified or not.</p>
	 *
	 * @return true if the view is directly modified, false otherwise.
	 */
	@Override
	public boolean isDirectlyModified() {
		return this.directlyModified;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * 	Set the object <em>directlyModified</em>to known if the view is directly modified or not.
	 * 	This method is recursive. If the view model has a parent, it'll call the parent.setDirectlyModified(boolean)
	 * 	method with the same parameter.
	 * </p>
	 */
	@Override
	public void setDirectlyModified(boolean p_oDirectlyModified) {
		this.directlyModified = p_oDirectlyModified;
		if (this.parent != null){
			this.parent.setDirectlyModified(p_oDirectlyModified);
		}
	}
	
	/**
	 * Gets the AbstractVM parent
	 *
	 * @return the parent abstarctVM
	 */
	@Override
	public AbstractViewModel getParent() {
		return this.parent;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Affecte l'objet parent
	 */
	@Override
	public void setParent(AbstractViewModel p_oParent) {
		this.parent = p_oParent;
	}
	
	/**
	 * Returns true if the value was updated
	 * @return true if the value was updated
	 */
	boolean isUpdatedValue() {
		return updatedValue;
	}

	/**
	 * Sets the updated value
	 * @param p_bUpdatedValue the new updated value
	 */
	void setUpdatedValue(boolean p_bUpdatedValue) {
		this.updatedValue = p_bUpdatedValue;
	}
	
	/**
	 * Returns the currently read view model
	 * @return the currently read view model
	 */
	String getReadCurrentVm() {
		return this.readCurrentVm;
	}
	
	/**
	 * Returns the data object
	 * @return the data object
	 */
	ViewModelDataObject getVmDataObject() {
		return this.vmDataObject;
	}
	
	/**
	 * returns the notification delegate
	 * @return the notification delegate
	 */
	ViewModelNotificationDelegate getVmNotificationDelegate() {
		return this.vmNotificationDelegate;
	}
	
	/**
	 * Search in the writables containers the component with the given name
	 *
	 * @param p_sName name of searched component
	 * @return null if not found else the component
	 */
	public ConfigurableVisualComponent getWritableComponentByName(String p_sName){
		return this.vmDataObject.getWritableComponentByName(p_sName);
	}
	
	/**
	 * Clears the current object
	 */
	@Override
	public void clear() {
		this.resetChangedIndicator();
		this.vmDataObject.clearSubData();
		this.vmDataObject.getFieldsErrorMap().clear();
		this.delegatesAttributes.clear();
	}

	/**
	 * Returns the parameter type of the configurationSetValue method of the parameter class
	 * @param p_oClassOfMethod the class to analyse
	 * @return the parameter type of the configurationSetValue method
	 */
	Class<?> findConfigurationSetValueParameterType( Class<?> p_oClassOfMethod ) {
		Class<?> r_oParameterType = null ;
		for( Method oMethod: p_oClassOfMethod.getDeclaredMethods()) {
			if ( oMethod.getName().equals("configurationSetValue")) {
				String sParameterType = oMethod.getParameterTypes()[0].getName();
				if ( !sParameterType.equals(Object.class.getName())) {
					r_oParameterType = oMethod.getParameterTypes()[0];
					break;
				}
			}
		}
		if (r_oParameterType == null) {
			r_oParameterType = findConfigurationSetValueParameterType(p_oClassOfMethod.getSuperclass());
		}
		return r_oParameterType ;
	}

	/**
	 * Serialises map content to object
	 * @param p_oObjectOutputStream stream to write to
	 * @throws IOException exception thrown
	 */
	private void writeObject(ObjectOutputStream p_oObjectOutputStream) throws IOException {

		Map<String, List<ConfigurableVisualComponent>> mapComponentToUnreg = new HashMap<String, List<ConfigurableVisualComponent>>();

		unregisterComponents(mapComponentToUnreg, this.vmDataObject.getReadDataComponents());
		unregisterComponents(mapComponentToUnreg, this.vmDataObject.getWriteDataComponents());
		unregisterComponents(mapComponentToUnreg, this.vmDataObject.getWriteSubDataComponents());
		unregisterComponents(mapComponentToUnreg, this.vmDataObject.getHideComponent());
		
		for (Entry<String, List<ConfigurableVisualComponent>> oEntry : mapComponentToUnreg.entrySet()) {
			for (ConfigurableVisualComponent oComp : oEntry.getValue()) {
				this.unregister(oComp);
			}
		}
		
		this.vmNotificationDelegate.clear();
		this.mapCacheFields.clear();
		
		p_oObjectOutputStream.defaultWriteObject();
	}
	
	/**
	 * Unregisters components from list
	 * @param p_oMapComponentToUnreg unregistered components list
	 * @param p_oContener components to unregister
	 */
	private void unregisterComponents(Map<String, List<ConfigurableVisualComponent>> p_oMapComponentToUnreg, ContenerDelegate<ConfigurableVisualComponent> p_oContener) {
		if (p_oContener.getSize() != 0) {
			for (Entry<String, List<ConfigurableVisualComponent>> oEntry : p_oContener.getEntrySet()) {
				for (ConfigurableVisualComponent oComp : oEntry.getValue()) {
					if (!p_oMapComponentToUnreg.containsKey(oComp.getComponentFwkDelegate().getDescriptor().getCompleteViewModelPath())) {
						p_oMapComponentToUnreg.put(oComp.getComponentFwkDelegate().getDescriptor().getCompleteViewModelPath(), new ArrayList<ConfigurableVisualComponent>());
					}
					p_oMapComponentToUnreg.get(oComp.getComponentFwkDelegate().getDescriptor().getCompleteViewModelPath()).add(oComp);
					Application.getInstance().getLog().error("ViewModelSerialization", "componant for key "+oEntry.getKey()+" : "+oComp+" not unregister before serialization");
				}
			}
		}
	}

	/**
	 * unserialises data from stream
	 * @param p_oInputStream stream from which we deserialises
	 * @throws ClassNotFoundException exception thrown
	 * @throws IOException exception thrown
	 */
	private void readObject(ObjectInputStream p_oInputStream) throws ClassNotFoundException, IOException {
		p_oInputStream.defaultReadObject();

		this.vmNotificationDelegate.analyseClass(this.getClass());
	}

	/**
	 * Sets the runnable to use to validate the business rules
	 *
	 * @param p_oRuleEditableFlag runnable to use
	 */
	public void setCurrentRule(ComputeRuleEditable p_oRuleEditableFlag) {
		this.ruleEditableFlag =  p_oRuleEditableFlag;
	}
	
	/**
	 * Launches the computeEditableFlag method
	 * Used for compatibility with previous version (modified in 4.0.9 of MDK framework)
	 */
	protected void computeEditableFlag() {

		if ( ruleEditableFlag != null ) {
			ruleEditableFlag.run();
			ruleEditableFlag = null;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param p_oError a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ItfMessage} object.
	 * @param p_sKey a {@link java.lang.String} object.
	 */
	@Override
	public void setUserError(final ItfMessage p_oError, final String ... p_sKey) {
		if (Application.getInstance().isOnUIThread()) {
			setErrorsToMap(p_oError, p_sKey);
		} else{
			(Application.getInstance()).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					setErrorsToMap(p_oError, p_sKey);
				}
			});
		}
	}

	/**
	 * Set errors to the user map
	 * @param p_oError a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ItfMessage} object.
	 * @param p_sKey a {@link java.lang.String} object.
	 */
	private void setErrorsToMap(ItfMessage p_oError, String... p_sKey) {
		for (String sKey : p_sKey) {
			this.getVmDataObject().addUserErrorToMap(sKey, p_oError);
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @param p_oError a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ItfMessage} object.
	 * @param p_sKey a {@link java.lang.String} object.
	 */
	@Override
	public void unsetUserError(ItfMessage p_oError, final String ... p_sKey) {
		if (Application.getInstance().isOnUIThread()) {
			unsetErrorFromMap(p_sKey);
		} else{
			(Application.getInstance()).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					unsetErrorFromMap(p_sKey);
				}
			});
		}
	}

	/**
	 * Unset error from the user map
	 * @param p_sKey a {@link java.lang.String} object.
	 */
	private void unsetErrorFromMap(String... p_sKey) {
		for (String sKey : p_sKey) {
			this.getVmDataObject().removeUserErrorFromMap(sKey);
		}
	}

	/** {@inheritDoc} */
	@Override
	public String getKey() {
		return key;
	}

	/** {@inheritDoc} */
	@Override
	public void setKey(String p_sKey) {
		this.key = p_sKey;
	}
	
	/** {@inheritDoc} */
	@Override
	public void affectByteAndNotify(byte p_bOldValue, byte p_bNewValue, String p_sKeyIdIdentifier) {
		if (p_bOldValue != p_bNewValue) {
			this.affectAndCachedField(p_sKeyIdIdentifier, p_bNewValue);
			this.notifyFieldChanged(p_sKeyIdIdentifier, p_bOldValue, p_bNewValue);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void affectShortAndNotify(short p_sOldValue, short p_sNewValue, String p_sKeyIdIdentifier) {
		if (p_sOldValue != p_sNewValue) {
			this.affectAndCachedField(p_sKeyIdIdentifier, p_sNewValue);
			this.notifyFieldChanged(p_sKeyIdIdentifier, p_sOldValue, p_sNewValue);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void affectIntAndNotify(int p_iOldValue, int p_iNewValue, String p_sKeyIdIdentifier) {
		if (p_iOldValue != p_iNewValue) {
			this.affectAndCachedField(p_sKeyIdIdentifier, p_iNewValue);
			this.notifyFieldChanged(p_sKeyIdIdentifier, p_iOldValue, p_iNewValue);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void affectLongAndNotify(long p_lOldValue, long p_lNewValue, String p_sKeyIdIdentifier) {
		if (p_lOldValue != p_lNewValue) {
			this.affectAndCachedField(p_sKeyIdIdentifier, p_lNewValue);
			this.notifyFieldChanged(p_sKeyIdIdentifier, p_lOldValue, p_lNewValue);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void affectFloatAndNotify(float p_fOldValue, float p_fNewValue, String p_sKeyIdIdentifier) {
		if (p_fOldValue != p_fNewValue) {
			this.affectAndCachedField(p_sKeyIdIdentifier, p_fNewValue);
			this.notifyFieldChanged(p_sKeyIdIdentifier, p_fOldValue, p_fNewValue);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void affectDoubleAndNotify(double p_dOldValue, double p_dNewValue, String p_sKeyIdIdentifier) {
		if (p_dOldValue != p_dNewValue) {
			this.affectAndCachedField(p_sKeyIdIdentifier, p_dNewValue);
			this.notifyFieldChanged(p_sKeyIdIdentifier, p_dOldValue, p_dNewValue);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void affectBooleanAndNotify(boolean p_bOldValue, boolean p_bNewValue, String p_sKeyIdIdentifier) {
		if (p_bOldValue != p_bNewValue) {
			this.affectAndCachedField(p_sKeyIdIdentifier, p_bNewValue);
			this.notifyFieldChanged(p_sKeyIdIdentifier, p_bOldValue, p_bNewValue);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void affectCharAndNotify(char p_aOldValue, char p_aNewValue, String p_sKeyIdIdentifier) {
		if (p_aOldValue != p_aNewValue) {
			this.affectAndCachedField(p_sKeyIdIdentifier, p_aNewValue);
			this.notifyFieldChanged(p_sKeyIdIdentifier, p_aOldValue, p_aNewValue);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void affectEnumAndNotify(Enum<?> p_oOldValue, Enum<?> p_oNewValue, String p_sKeyIdIdentifier) {
		if ((p_oNewValue == null && p_oOldValue != null) || (p_oNewValue != null && !p_oNewValue.equals(p_oOldValue))) {
			this.affectAndCachedField(p_sKeyIdIdentifier, p_oNewValue);
			this.notifyFieldChanged(p_sKeyIdIdentifier, p_oOldValue, p_oNewValue);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void affectObjectAndNotify(Object p_oOldValue, Object p_oNewValue, String p_sKeyIdIdentifier) {
		if ((p_oNewValue == null && p_oOldValue != null) || (p_oNewValue != null && !p_oNewValue.equals(p_oOldValue))) {
			this.affectAndCachedField(p_sKeyIdIdentifier, p_oNewValue);
			this.notifyFieldChanged(p_sKeyIdIdentifier, p_oOldValue, p_oNewValue);
		}
	}
	
	/**
	 * affect attribute and cache field
	 * @param p_sFieldName the field name
	 * @param p_oNewValue the value to set
	 */
	private void affectAndCachedField(String p_sFieldName, Object p_oNewValue) {
		Field oField = this.mapCacheFields.get(p_sFieldName);
		try {
			if (oField == null) {
				oField = retrievFieldInHierarchy(this.getClass(), p_sFieldName);
				oField.setAccessible(true);
				this.mapCacheFields.put(p_sFieldName, oField);
			}
			oField.set(this, p_oNewValue);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			Application.getInstance().getLog().error(Application.LOG_TAG, "field not in view model :"+this.getClass().getSimpleName(), e);
		}
	}
	
	/**
	 * get the field in the class hierarchy
	 * <p>this is a recursive method</p>
	 * @param p_oClass the class to get field
	 * @param p_sFieldName the field name
	 * @return the field in the hierarchy
	 * @throws NoSuchFieldException if field is not in hierarchy
	 * @throws SecurityException if field is private
	 */
	private Field retrievFieldInHierarchy(Class<?> p_oClass, String p_sFieldName) throws NoSuchFieldException, SecurityException {
		Field r_oField = null;
		try {
			r_oField = p_oClass.getDeclaredField(p_sFieldName);
		} catch (NoSuchFieldException e) {
			if (p_oClass.getSuperclass() != null) {
				r_oField = this.retrievFieldInHierarchy(p_oClass.getSuperclass(), p_sFieldName);
			} else {
				throw new NoSuchFieldException("no field ["+p_sFieldName+"] in herarchy");
			}
		}
		
		return r_oField;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getIdVM() {
		return String.valueOf(this.hashCode());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> getDelegateAttributesForComponent(int p_iComponentId) {
		return this.delegatesAttributes.get(p_iComponentId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDelegateAttributesForComponent(Integer p_iComponentId, Map<String, Object> p_oAttributes) {
		if (!this.delegatesAttributes.containsKey(p_iComponentId)) {
			this.delegatesAttributes.put(p_iComponentId, new HashMap<String, Object>());
		}
		
		Map<String, Object> oDelegateAttributesMap = this.delegatesAttributes.get(p_iComponentId);
		
		oDelegateAttributesMap.putAll(p_oAttributes);
	}
	
	@Override
	public Set<Entry<String, List<AbstractComponentWrapper<?>>>> getWrappers() {
		return this.getVmDataObject().getWrappers().getEntrySet();
	}
	
	@Override
	public List<AbstractComponentWrapper<?>> getWrapperAtPath(String p_sPath) {
		return this.getVmDataObject().getWrappers().get(p_sPath);
	}
}
