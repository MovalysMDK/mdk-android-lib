package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

import java.util.HashMap;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * This is a classic ViewHolder implementation used to retain some references
 * of the components of the items of a ListView
 * @author quentinlagarde
 */
public class ConfigurableListViewHolder {
	
	
	/**
	 * The key used to identify this object as a parameter
	 */
	public static final String CONFIGURABLE_LIST_VIEW_HOLDER_KEY = "CONFIGURABLE_LIST_VIEW_HOLDER_KEY";
	
	
	public boolean isSelected;
	/**
	 * The viewModelID
	 */
	public String viewModelID;
	
	/**
	 * A map that hold components by path
	 */
	private  HashMap<String, ConfigurableVisualComponent<?>> componentsByPath = new HashMap<>();
	
	/**
	 * A map that hold components by Id
	 */
	private  HashMap<Integer, ConfigurableVisualComponent<?>> componentsById = new HashMap<>();
	
	
	/**
	 * Constructeur
	 */
	public ConfigurableListViewHolder() {
		this.componentsById = new HashMap<>();
		this.componentsByPath = new HashMap<>();
		this.isSelected = false;
	}
	
	
	
	/**
	 * Get the map of components indexed by ID
	 * @return a Map of component indexed by ID
	 */
	public HashMap<Integer, ConfigurableVisualComponent<?>> getComponentsById() {
		return componentsById;
	}
	
	/**
	 * Get the map of components indexed by path
	 * @return a Map of component indexed by path
	 */
	public HashMap<String, ConfigurableVisualComponent<?>> getComponentsByPath() {
		return componentsByPath;
	}
	
	/**
	 * Add a component by id to the holder	
	 * @param p_lId the Id of the component	to add
	 * @param p_oComponent the component to add
	 */
	public void addComponentById(int p_lId, ConfigurableVisualComponent<?> p_oComponent) {
		this.componentsById.put(p_lId, p_oComponent);
	}
	
	/**
	 * Add a component by path to the holder	
	 * @param p_lId the Id of the component	to add
	 * @param p_oComponent the component to add
	 */
	public void addComponentByPath(String p_sPath, ConfigurableVisualComponent<?> p_oComponent) {
		this.componentsByPath.put(p_sPath, p_oComponent);
	}
	
	/**
	 * Removes a component by Id from the holder
	 * @param p_lId the Id of the component to remove
	 */
	public void removeComponentById(int p_lId) {
		this.componentsById.remove(p_lId);
	}
	
	/**
	 * Returns a component hold by the ViewHolder 
	 * @param p_lId The id of the component to get
	 * @return A ConfigurableVisualComponent or null
	 */
	public ConfigurableVisualComponent<?> getComponentById(int p_lId) {
		return this.componentsById.get(p_lId);
	}
	
	/**
	 * Returns a component hold by the ViewHolder 
	 * @param p_sPath The path of the component to get
	 * @return A ConfigurableVisualComponent or null
	 */
	public ConfigurableVisualComponent<?> getComponentByPath(String p_sPath) {
		return this.componentsByPath.get(p_sPath);
	}
	
	
}
