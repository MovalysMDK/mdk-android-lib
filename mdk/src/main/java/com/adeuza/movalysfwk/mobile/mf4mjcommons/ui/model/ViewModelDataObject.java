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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.ContenerDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ItfMessage;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.AbstractComponentWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.listener.businessrule.BusinessRuleRegistry;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.listener.businessrule.PropertyTarget;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *  Object to store view models data
 */
@SuppressWarnings("serial")
public class ViewModelDataObject implements Serializable {

	/** all components of view model */
	private ContenerDelegate<ConfigurableVisualComponent> components;
	
	/** components to write data, key is active screen */
	private ContenerDelegate<ConfigurableVisualComponent> writeDataComponents;
	
	/** components to read data, key is active screen */
	private ContenerDelegate<ConfigurableVisualComponent> readDataComponents;
	
	/** components to write data for sub list, key is active screen */
	private ContenerDelegate<ConfigurableVisualComponent> writeSubDataComponents;
	
	/** components to hide, key is active screen */
	private ContenerDelegate<ConfigurableVisualComponent> hideComponents;
	
	/** Map storing the last known ValidationState of components */
	private Map<String, ValidationState> lastKnownValidationByComponent;
	
	/** Method to notify when o change occurred */
	private Map<String,List<Method>> notifyMethodFields;
	
	/** Method to notify when Collection o change occurred */
	private Map<String,List<Method>> notifyMethodCollection;
	
	/** the register that store business rules */
	private BusinessRuleRegistry businessRuleRegistry;
	
	/** always notify */
	private boolean alwaysNotify = true;
	
	/** Field in error */
	private Map<String, Object> fieldsErrorMap;
	
	/** User errors on fields map */
	private Map<String, ItfMessage> userFieldsErrorMap;
	
	/** components and wrapper */
	private ContenerDelegate<AbstractComponentWrapper<?>> componentsToWrapperMap;
	
	/**
	 * Constructor
	 */
	public ViewModelDataObject() {
		this.components = new ContenerDelegate<ConfigurableVisualComponent>();
		this.writeDataComponents = new ContenerDelegate<ConfigurableVisualComponent>();
		this.readDataComponents = new ContenerDelegate<ConfigurableVisualComponent>();
		this.hideComponents = new ContenerDelegate<ConfigurableVisualComponent>();
		this.writeSubDataComponents = new ContenerDelegate<ConfigurableVisualComponent>();
		this.notifyMethodFields = new HashMap<String, List<Method>>();
		this.notifyMethodCollection = new HashMap<String, List<Method>>();
		this.businessRuleRegistry = new BusinessRuleRegistry();
		this.fieldsErrorMap = new HashMap<String,Object>();
		this.lastKnownValidationByComponent = new HashMap<String, ValidationState>();
		this.userFieldsErrorMap = new HashMap<String, ItfMessage>();
		this.componentsToWrapperMap = new ContenerDelegate<AbstractComponentWrapper<?>>(); 
	}
	
	/**
	 * Returns the components list
	 * @return the components list
	 */
	public ContenerDelegate<ConfigurableVisualComponent> getComponents() {
		return components;
	}
	
	/**
	 * Removes a component from the list
	 * @param p_oComponent the components to set
	 */
	public void addComponentToList(ConfigurableVisualComponent p_oComponent) {
		this.components.add(p_oComponent.getComponentFwkDelegate().getDescriptor().getCompleteViewModelPath(), p_oComponent);
		
		if (p_oComponent instanceof AbstractComponentWrapper) {
			this.componentsToWrapperMap.add(p_oComponent.getComponentFwkDelegate().getName(), (AbstractComponentWrapper<?>) p_oComponent);
		}
	}
	
	/**
	 * Returns the wrappers list
	 * @return the wrappers list
	 */
	public ContenerDelegate<AbstractComponentWrapper<?>> getWrappers() {
		return componentsToWrapperMap;
	}
	
	/**
	 * Removes the wrappers with the given key.
	 * @param p_sKey the key to remove
	 */
	public void removeWrapperByKey(String p_sKey) {
		this.componentsToWrapperMap.remove(p_sKey);
	}
	
	/**
	 * Returns the list of write data components
	 * @return the writeDataComponents
	 */
	public ContenerDelegate<ConfigurableVisualComponent> getWriteDataComponents() {
		return writeDataComponents;
	}

	/**
	 * Adds a component to the write data list
	 * @param p_oComponent the components to set
	 */
	public void addWriteDataComponentToList(ConfigurableVisualComponent p_oComponent) {
		this.writeDataComponents.add(p_oComponent.getComponentFwkDelegate().getDescriptor().getCompleteViewModelPath(), p_oComponent);
	}
	
	/**
	 * Returns the list of components in the read data list
	 * @return the readDataComponents
	 */
	public ContenerDelegate<ConfigurableVisualComponent> getReadDataComponents() {
		return readDataComponents;
	}

	/**
	 * Add a component to the read data list
	 * @param p_oComponent the components to set
	 */
	public void addReadDataComponentToList(ConfigurableVisualComponent p_oComponent) {
		this.readDataComponents.add(p_oComponent.getComponentFwkDelegate().getDescriptor().getCompleteViewModelPath(), p_oComponent);
	}
	
	/**
	 * Returns the list of write sub data components
	 * @return the writeSubDataComponents
	 */
	public ContenerDelegate<ConfigurableVisualComponent> getWriteSubDataComponents() {
		return writeSubDataComponents;
	}

	/**
	 * Adds a component to the write sub data list
	 * @param p_oComponent the components to set
	 */
	public void addWriteSubDataComponentToList(ConfigurableVisualComponent p_oComponent) {
		this.writeSubDataComponents.add(p_oComponent.getComponentFwkDelegate().getDescriptor().getCompleteViewModelPath(), p_oComponent);
	}
	
	/**
	 * Removes a component from the write sub data list by its key
	 * @param p_sKey key of the component to remove
	 */
	public void removeWriteSubDataComponentByKey(String p_sKey) {
		this.writeSubDataComponents.remove(p_sKey);
	}
	
	/**
	 * returns the list of hidden component
	 * @return the list of hidden component
	 */
	public ContenerDelegate<ConfigurableVisualComponent> getHideComponent() {
		return this.hideComponents;
	}
	
	/**
	 * Adds a component to the list of hidden component
	 * @param p_oComponent the components to set
	 */
	public void addHideComponentToList(ConfigurableVisualComponent p_oComponent) {
		this.hideComponents.add(p_oComponent.getComponentFwkDelegate().getDescriptor().getCompleteViewModelPath(), p_oComponent);
	}
	
	/**
	 * Returns the last known validation state of a given field
	 * @param p_sKey the key of the field
	 * @return the validation state of the field
	 */
	public ValidationState getLastKnownValidationByComponentKey(String p_sKey) {
		return lastKnownValidationByComponent.get(p_sKey);
	}
	
	/**
	 * Update the last {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ValidationState} of a component
	 * @param p_sKey the key of the component
	 * @param p_oValue the new {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ValidationState}
	 */
	public void updateComponentValidationStatus(String p_sKey, ValidationState p_oValue) {
		this.lastKnownValidationByComponent.put(p_sKey, p_oValue);
	}
	
	/**
	 * Returns the list of fields in error
	 * @return the fieldsErrorMap
	 */
	public Map<String, Object> getFieldsErrorMap() {
		return fieldsErrorMap;
	}

	/**
	 * Removes a component from the fields in error list by its key
	 * @param p_sKey key of the component to remove
	 */
	public void removeFieldErrorByKey(String p_sKey) {
		this.fieldsErrorMap.remove(p_sKey);
	}
	
	/**
	 * Removes a set of key and component from a bunch of maps
	 * @param p_sKey the key to remove
	 * @param p_oComponent the component to remove
	 */
	public void removeKeyFromAllMaps(String p_sKey, ConfigurableVisualComponent p_oComponent) {
		this.components.remove(p_sKey, p_oComponent);
		this.writeDataComponents.remove(p_sKey, p_oComponent);
		this.readDataComponents.remove(p_sKey, p_oComponent);
		this.hideComponents.remove(p_sKey, p_oComponent);
		if (p_oComponent instanceof AbstractComponentWrapper) {
			this.componentsToWrapperMap.remove(p_sKey, (AbstractComponentWrapper<?>) p_oComponent);
		}
	}
	
	/**
	 *  Clear the VMDataObject.
	 */
	public void clear() {
		for(Entry<String,List<ConfigurableVisualComponent>> entry : this.components.getEntrySet()){
			for(ConfigurableVisualComponent configurableVisualComponent : entry.getValue()){
				configurableVisualComponent.getComponentFwkDelegate().unregisterVM();
			}
		}
		this.components.clear();
		this.writeDataComponents.clear();
		this.readDataComponents.clear();
		this.hideComponents.clear();
		this.componentsToWrapperMap.clear();
	}

	/**
	 * Returns the list of methods with a notify annotation
	 * @return the notifyMethodFields
	 */
	public Map<String, List<Method>> getNotifyMethodFields() {
		return notifyMethodFields;
	}

	/**
	 * Sets the list of notifying methods
	 * @param p_oNotifyMethodFields the notifyMethodFields to set
	 */
	public void setNotifyMethodFields(Map<String, List<Method>> p_oNotifyMethodFields) {
		this.notifyMethodFields = p_oNotifyMethodFields;
	}

	/**
	 * Returns the list of methods with the notify collection annotation
	 * @return the notifyMethodCollection
	 */
	public Map<String, List<Method>> getNotifyMethodCollection() {
		return notifyMethodCollection;
	}

	/**
	 * Sets the list of methods with a notify collection annotation
	 * @param p_oNotifyMethodCollection the notifyMethodCollection to set
	 */
	public void setNotifyMethodCollection(Map<String, List<Method>> p_oNotifyMethodCollection) {
		this.notifyMethodCollection = p_oNotifyMethodCollection;
	}

	/**
	 * Returns the business rules registry
	 * @return the businessRuleRegistry
	 */
	public BusinessRuleRegistry getBusinessRuleRegistry() {
		return businessRuleRegistry;
	}

	/**
	 * Sets the business rules registry
	 *
	 * @param p_oBusinessRuleRegistry the businessRuleRegistry to set
	 */
	public void setBusinessRuleRegistry(BusinessRuleRegistry p_oBusinessRuleRegistry) {
		this.businessRuleRegistry = p_oBusinessRuleRegistry;
	}

	/**
	 * Adds a business rule to the registry with the given parameters
	 * @param p_oMethod the method to trigger
	 * @param p_oPropertyTarget the property target
	 * @param p_sKey the field key
	 * @param p_oFields the known fields
	 * @param p_oTriggers the knwon triggers
	 */
	public void addBusinessRuleRegistry(Method p_oMethod, PropertyTarget p_oPropertyTarget, String p_sKey, String[] p_oFields, String[] p_oTriggers) {
		this.businessRuleRegistry.add(p_oMethod, p_oPropertyTarget, p_sKey, p_oFields, p_oTriggers);
	}
	
	/**
	 * Returns whether the modified fields should notify
	 * @return the alwaysNotify
	 */
	public boolean isAlwaysNotify() {
		return alwaysNotify;
	}

	/**
	 * Set whether the view model should always notify field changes
	 * @param p_bAlwaysNotify the alwaysNotify to set
	 */
	public void setAlwaysNotify(boolean p_bAlwaysNotify) {
		this.alwaysNotify = p_bAlwaysNotify;
	}

	/**
	 * Clear data in the sub list
	 */
	public void clearSubData() {
		this.lastKnownValidationByComponent.clear();
		this.writeSubDataComponents.clear();
	}
	
	/**
	 * Search in the writables containers the component with the given name
	 * @param p_sName name of searched component
	 * @return null if not found else the component
	 */
	public ConfigurableVisualComponent getWritableComponentByName(String p_sName){
		ConfigurableVisualComponent r_oComponent = this.getComponentByName(p_sName,  writeDataComponents);
		if (r_oComponent==null){
			r_oComponent = this.getComponentByName(p_sName,  writeSubDataComponents);
		}
		return r_oComponent ;
	}
	
	/**
	 * Search  in the container the component with the given name 
	 * @param p_sName name of searched component
	 * @param p_oContener container of component 
	 * @return null if not found else the component  
	 */
	private ConfigurableVisualComponent getComponentByName(String p_sName , ContenerDelegate<ConfigurableVisualComponent> p_oContener ){
		Iterator<ConfigurableVisualComponent> iterComponents = null;
		ConfigurableVisualComponent r_oComponent = null;
		String sComponent = new StringBuilder("__").append(p_sName).append("__").toString();
		for(Entry<String, List<ConfigurableVisualComponent>> oEntry : p_oContener .getEntrySet()) {
			iterComponents = oEntry.getValue().iterator();
			while(iterComponents.hasNext()) {
				r_oComponent = iterComponents.next();
				if (r_oComponent.getComponentFwkDelegate().getName().contains(sComponent)){
					return r_oComponent ;
				}
			}
		}
		return null ;
	}
	
	/**
	 * Add a method to notify when fields are modified
	 * @param p_oMethod method to call on modification
	 * @param p_oFields fields to listen to
	 */
	public void addMethodFieldNotifier(Method p_oMethod, String[] p_oFields) {
		for (String sKey : p_oFields) {
			List<Method> list = this.notifyMethodFields.get(sKey);
			if (list == null) {
				list = new ArrayList<Method>();
				this.notifyMethodFields.put(sKey, list);
			}
			list.add(p_oMethod);
		}

	}

	/**
	 * Add a method to notify when collection is modified
	 * @param p_oMethod method to call on modification
	 * @param p_oFields fields to listen to
	 */
	public void addMethodCollectionNotifier(Method p_oMethod, String[] p_oFields) {
		for (String sKey : p_oFields) {
			List<Method> list = this.notifyMethodCollection.get(sKey);
			if (list == null) {
				list = new ArrayList<Method>();
				this.notifyMethodCollection.put(sKey, list);
			}
			list.add(p_oMethod);
		}

	}
	
	/**
	 * Serialises map content to object
	 * @param p_oObjectOutputStream stream to write to
	 * @throws IOException exception thrown
	 */
	private void writeObject(ObjectOutputStream p_oObjectOutputStream) throws IOException {
		Map<String, ValidationState> oLastKnownValidationByComponentBackup = lastKnownValidationByComponent;
		Map<String,List<Method>> oNotifyMethodFieldsBackup = notifyMethodFields;
		Map<String,List<Method>> oNotifyMethodCollectionBackup = notifyMethodCollection;
		BusinessRuleRegistry oBusinessRuleRegistryBackup = businessRuleRegistry;
		
		lastKnownValidationByComponent = null;
		notifyMethodFields = null;
		notifyMethodCollection = null;
		businessRuleRegistry = null;
		
		p_oObjectOutputStream.defaultWriteObject();
		
		lastKnownValidationByComponent = oLastKnownValidationByComponentBackup;
		notifyMethodFields = oNotifyMethodFieldsBackup;
		notifyMethodCollection = oNotifyMethodCollectionBackup;
		businessRuleRegistry = oBusinessRuleRegistryBackup;
	}	

	/**
	 * unserialises data from stream
	 * @param p_oInputStream stream from which we deserialises
	 * @throws ClassNotFoundException exception thrown
	 * @throws IOException exception thrown
	 */
	private void readObject(ObjectInputStream p_oInputStream) throws ClassNotFoundException, IOException {
		p_oInputStream.defaultReadObject();
	}

	/**
	 * Adds a user error to the map
	 * @param p_sKey the component key
	 * @param p_oError the error
	 */
	public void addUserErrorToMap(String p_sKey, ItfMessage p_oError) {
		ConfigurableVisualComponent oComponent = this.getWritableComponentByName(p_sKey);
		String sCompletePath = oComponent.getComponentFwkDelegate().getDescriptor().getCompleteViewModelPath();
		String sError = Application.getInstance().getStringResource(p_oError.getResource());
		
		oComponent.getComponentDelegate().configurationSetError(sError);
		
		this.fieldsErrorMap.put(sCompletePath, oComponent.getComponentDelegate().configurationGetValue());
		this.userFieldsErrorMap.put(p_sKey, p_oError);
	}
	
	/**
	 * <p>refreshUserErrorOnComponent.</p>
	 * @param p_oComponent a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent} object.
	 */
	public void refreshUserErrorOnComponent(ConfigurableVisualComponent p_oComponent) {
		String sKey = p_oComponent.getComponentFwkDelegate().getDescriptor().getCompleteViewModelPath();
		if (this.userFieldsErrorMap.containsKey(sKey)) {
			String sError = Application.getInstance().getStringResource(this.userFieldsErrorMap.get(sKey).getResource());
			p_oComponent.getComponentDelegate().configurationSetError(sError);
		}
	}

	/**
	 * Removes a user error from the map
	 * @param p_sKey the component key
	 */
	public void removeUserErrorFromMap(String p_sKey) {
		ConfigurableVisualComponent oComponent = this.getWritableComponentByName(p_sKey);
		String sCompletePath = oComponent.getComponentFwkDelegate().getDescriptor().getCompleteViewModelPath();
		
		oComponent.getComponentDelegate().configurationUnsetError();
		
		this.fieldsErrorMap.remove(sCompletePath);
		this.userFieldsErrorMap.remove(p_sKey);
	}

	/**
	 * <p>hasUserError.</p>
	 * @return a boolean.
	 */
	public boolean hasUserError() {
		return this.userFieldsErrorMap.size() > 0;
	}

	/**
	 * <p>fieldHasUserError.</p>
	 * @param p_oComponent a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent} object.
	 * @return a boolean.
	 */
	public boolean fieldHasUserError(ConfigurableVisualComponent p_oComponent) {
		Object oKey = p_oComponent.getComponentFwkDelegate().getDescriptor().getCompleteViewModelPath();
		if (this.userFieldsErrorMap.containsKey(oKey)) {
			return true;
		}
		return false;
	}

	/**
	 * <p>Getter for the field <code>userFieldsErrorMap</code>.</p>
	 * @return a {@link java.util.Map} object.
	 */
	public Map<String, ItfMessage> getUserFieldsErrorMap() {
		return this.userFieldsErrorMap;
	}

}
