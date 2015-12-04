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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ItfMessage;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.AbstractComponentWrapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * <p>Main interface of the View Model representation. Use this interface for form representation.</p>
 */
public interface ViewModel extends Serializable {

	/** Actions on the list view for listener ListenerOnFixedListModified */
	public static enum Action {
		/** add action */
		ADD,
		/** update action */
		UPDATE,
		/** update action */
		REMOVE
	};
	
	/**
	 * Register a component on the view model
	 *
	 * @param p_oComponent a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent} object.
	 */
	public void register(ConfigurableVisualComponent p_oComponent);
	
	/**
	 * Unregister all components for the key in all registers
	 *
	 * @param p_oComponent a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent} object.
	 */
	public void unregister(ConfigurableVisualComponent p_oComponent);
	
	/**
	 * Unregister all components for the key in all registers
	 */
	public void unregister();

	/**
	 * Register in view, the componant whose write data
	 *
	 * @param p_oComponent the component to register
	 */
	public void registerWritableDataComponents(ConfigurableVisualComponent p_oComponent);
	
	/**
	 * Register in view, the componant whose read data
	 *
	 * @param p_oComponent the component to register
	 */
	public void registerReadableDataComponents(ConfigurableVisualComponent p_oComponent);
	
	/**
	 * Register in view, the componant whose hide data
	 *
	 * @param p_oComponent the component to register
	 */
	public void registerHidableDataComponents(ConfigurableVisualComponent p_oComponent);

	
	/**
	 * Executed after an action
	 *
	 * @param p_mapParameters a {@link java.util.Map} object.
	 */
	public void doOnDataLoaded(Map<String, Object> p_mapParameters);
	
	/**
	 * Write data in component
	 *
	 * @param p_sPath a {@link java.lang.String} object.
	 */
	public void writeDataToComponent(String p_sPath);
	
	/**
	 * validate data for component is group p_sGroup
	 *
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 * @param p_oParameters a {@link java.util.Map} object.
	 * @return a boolean.
	 */
	public boolean validComponents(MContext p_oContext, Map<String, Object> p_oParameters);
	
	/**
	 * Validate the current view model
	 *
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 * @param p_oParamaters a {@link java.util.Map} object.
	 * @return true if the view model is valid, false otherwise
	 */
	public boolean validViewModel(MContext p_oContext, Map<String, Object> p_oParamaters);
	
	/**
	 * Validates the current user errors
	 *
	 * @param p_oContext context to use
	 * @return true if there are no pending user errors
	 */
	public boolean validateUserErrors(MContext p_oContext);
	
	/**
	 * Read data to the component
	 *
	 * @param p_oComponent a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent} object.
	 * @return true if its valid content for the component, false else
	 */
	public boolean readDataFromComponent(ConfigurableVisualComponent p_oComponent);
	
	/**
	 * Read data to the component
	 *
	 * @param p_oComponent a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent} object.
	 * @param p_oParameters a {@link java.util.Map} object.
	 * @return true if its valid content for the component, false else
	 */
	public boolean readDataFromComponent(ConfigurableVisualComponent p_oComponent, Map<String, Object> p_oParameters);
	
	
	/**
	 * Returns true whether view model is editable
	 *
	 * @return true whether model is editable
	 */
	public boolean isEditable();

	/**
	 * If the view model is editable then p_bEditable equals <code>true</code>.
	 *
	 * @param p_bEditable <code>true</code> if the view model is editable.
	 */
	public void setEditable(boolean p_bEditable);

	/**
	 * Define the "editable" attribute using a context and some parameters.
	 * 
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 * @param p_mapParameters a {@link java.util.Map} object.
	 */
	public void setEditable(final MContext p_oContext, final Map<String, Object> p_mapParameters);

	/**
	 * Returns a unique identifier (ids of the linked entity, separated by underscore)
	 *
	 * @return a unique identifier
	 */
	public String getIdVM();
	
	/**
	 * Gets the AbstractVM parent
	 *
	 * @return the parent abstarctVM
	 */
	public AbstractViewModel getParent() ;
	/**
	 * Affecte l'objet parent
	 *
	 * @param p_oParent Objet parent
	 */
	public void setParent(AbstractViewModel p_oParent) ;
	/**
	 * <p>
	 * 	Set the object <em>directlyModified</em>to known if the view is directly modified or not.
	 * 	This method is recursive. If the view model has a parent, it'll call the parent.setDirectlyModified(boolean)
	 * 	method with the same parameter.
	 * </p>
	 *
	 * @param p_oDirectlyModified
	 * 		True if the view is directly modified, false otherwise.
	 */
	public void setDirectlyModified(boolean p_oDirectlyModified);
	
	
	/**
	 * <p>isDirectlyModified.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isDirectlyModified();

	/**
	 * A2A_DOC _#_FBO_#_ - Décrire la méthode beforeConfigurationChanged de la classe ViewModel
	 *
	 * @return a {@link java.util.Map} object.
	 */
	// public Map<String,Object> beforeConfigurationChanged(String p_sKey);
	public Map<String,Object> beforeConfigurationChanged();
	
	/**
	 * A2A_DOC _#_FBO_#_ - Décrire la méthode afterConfigurationChanged de la classe ViewModel
	 */
	// public void afterConfigurationChanged(String p_sKey, Map<String,Object> p_oConfigurationMap);
	public void afterConfigurationChanged();

	/**
	 * Indicates whether the model is ready to changed (view has changed, but not the view model)
	 *
	 * @return true if it is ready to change
	 */
	public boolean isReadyToChanged();
	
	/**
	 * Clear view model.
	 */
	public void clear();

	/**
	 * <p>resetChangedIndicator.</p>
	 */
	public void resetChangedIndicator();
	
	/**
	 * Clone VM attributes
	 *
	 * @param p_oVmToClone the view model to clone
	 */
	public void cloneVmAttributes(ViewModel p_oVmToClone);
	
	/**
	 * Pushes an error to a list of components by key
	 *
	 * @param p_oError the error message
	 * @param p_sKey a list of component keys
	 */
	public void setUserError(ItfMessage p_oError, String ... p_sKey);

	/**
	 * Pulls an error back from a list of components by key
	 *
	 * @param p_oError the error message
	 * @param p_sKey a list of component keys
	 */
	public void unsetUserError(ItfMessage p_oError, String ... p_sKey);

	/**
	 * Get the key of the view model
	 *
	 * @return the key of the view model
	 * @since 3.1.1
	 */
	String getKey();

	/**
	 * Set the key of the view model
	 *
	 * @param key the new key
	 * @since 3.1.1
	 */
	void setKey(String key);

	/**
	 * Sets the value of a byte field and sends a notification if necessary
	 * @param p_bOldValue the old value
	 * @param p_bNewValue the new value
	 * @param p_sKeyIdIdentifier the modified field view model key
	 */
	void affectByteAndNotify(byte p_bOldValue, byte p_bNewValue, String p_sKeyIdIdentifier);
	
	/**
	 * Sets the value of a short field and sends a notification if necessary
	 * @param p_sOldValue the old value
	 * @param p_sNewValue the new value
	 * @param p_sKeyIdIdentifier the modified field view model key
	 */
	void affectShortAndNotify(short p_sOldValue, short p_sNewValue, String p_sKeyIdIdentifier);
	
	/**
	 * Sets the value of an int field and sends a notification if necessary
	 * @param p_iOldValue the old value
	 * @param p_iNewValue the new value
	 * @param p_sKeyIdIdentifier the modified field view model key
	 */
	void affectIntAndNotify(int p_iOldValue, int p_iNewValue, String p_sKeyIdIdentifier);
	
	/**
	 * Sets the value of a long field and sends a notification if necessary
	 * @param p_lOldValue the old value
	 * @param p_lNewValue the new value
	 * @param p_sKeyIdIdentifier the modified field view model key
	 */
	void affectLongAndNotify(long p_lOldValue, long p_lNewValue, String p_sKeyIdIdentifier);
	
	/**
	 * Sets the value of a float field and sends a notification if necessary
	 * @param p_fOldValue the old value
	 * @param p_fNewValue the new value
	 * @param p_sKeyIdIdentifier the modified field view model key
	 */
	void affectFloatAndNotify(float p_fOldValue, float p_fNewValue, String p_sKeyIdIdentifier);
	
	/**
	 * Sets the value of a double field and sends a notification if necessary
	 * @param p_dOldValue the old value
	 * @param p_dNewValue the new value
	 * @param p_sKeyIdIdentifier the modified field view model key
	 */
	void affectDoubleAndNotify(double p_dOldValue, double p_dNewValue, String p_sKeyIdIdentifier);
	
	/**
	 * Sets the value of a boolean field and sends a notification if necessary
	 * @param p_bOldValue the old value
	 * @param p_bNewValue the new value
	 * @param p_sKeyIdIdentifier the modified field view model key
	 */
	void affectBooleanAndNotify(boolean p_bOldValue, boolean p_bNewValue, String p_sKeyIdIdentifier);
	
	/**
	 * Sets the value of a char field and sends a notification if necessary
	 * @param p_aOldValue the old value
	 * @param p_aNewValue the new value
	 * @param p_sKeyIdIdentifier the modified field view model key
	 */
	void affectCharAndNotify(char p_aOldValue, char p_aNewValue, String p_sKeyIdIdentifier);
	
	/**
	 * Sets the value of an Object field and sends a notification if necessary
	 * @param p_oOldValue the old value
	 * @param p_oNewValue the new value
	 * @param p_sKeyIdIdentifier the modified field view model key
	 */
	void affectObjectAndNotify(Object p_oOldValue, Object p_oNewValue, String p_sKeyIdIdentifier);
	
	/**
	 * Sets the value of an Enum field and sends a notification if necessary
	 * @param p_oOldValue the old value
	 * @param p_oNewValue the new value
	 * @param p_sKeyIdIdentifier the modified field view model key
	 */
	void affectEnumAndNotify(Enum<?> p_oOldValue, Enum<?> p_oNewValue, String p_sKeyIdIdentifier);
	
	/**
	 * Returns the attributes of the delegate for the component with the given id
	 * The attributes are stored with their name and value
	 * @param p_iComponentId the component id to look for
	 * @return a map storing the the attributes of the component, or null if there were no attributes stored
	 */
	Map<String, Object> getDelegateAttributesForComponent(int p_iComponentId);

	/**
	 * Sets attributes for a component with the given id 
	 * @param p_iComponentId the id of the component linked to the attribute to be set
	 * @param p_oAttributes the attributes to set
	 */
	void setDelegateAttributesForComponent(Integer p_iComponentId, Map<String, Object> p_oAttributes);
	
	/**
	 * Returns a set of components identified by their visual path with their related wrappers
	 * @return the components and their wrappers
	 */
	Set<Entry<String, List<AbstractComponentWrapper<?>>>> getWrappers();

	/**
	 * Returns the list of wrappers found with the given visual path.
	 * @param p_sPath the visual path of the wrappers to find
	 * @return the wrappers found, null if none exist
	 */
	List<AbstractComponentWrapper<?>> getWrapperAtPath(String p_sPath);
}
