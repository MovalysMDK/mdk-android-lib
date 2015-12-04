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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;


/**
 * <p>Describes a visual component, taking into account the configuration.</p>
 *
 *
 */
public class RealVisualComponentDescriptor {

	/** suffix to use for view model name */
	private static final String SUFFIX_VM = "VM";
	/** seperator for path, in data */
	private static final String PATH_SEPARATOR_IN = "_";
	/** separator for path, out data */
	private static final String PATH_SEPARATOR_OUT = ".";

	/** list of subcomponents */
	private Map<String, RealVisualComponentDescriptor> components = null;

	/** Visual configuration to apply. For one configuration, there are several visual component to consider. */
	private Map<String, List<RealVisualComponentDescriptor>> fieldConfigurationsToApply = null;

	/** path to bind data in view model, hierarchical representation*/
	private List<String> viewModelPath = null;
	
	/** paht to bind data in view model, flat representation */
	private String completeViewModelPath = null;
	
	/** path to retrevie component */
	private List<String> visualPath = null;
	
	/** theoritical descriptor associated */
	private TheoriticalVisualComponentDescriptor theoriticalDescriptor = null;
	
	/**
	 * Constructs a new real descriptor
	 *
	 * @param p_oTheoriticalDescriptor basic theoritical descriptor
	 */
	public RealVisualComponentDescriptor(TheoriticalVisualComponentDescriptor p_oTheoriticalDescriptor) {
		this.fieldConfigurationsToApply = new HashMap<String, List<RealVisualComponentDescriptor>>();
		this.viewModelPath = new ArrayList<String>();
		this.visualPath = new ArrayList<String>();
		this.theoriticalDescriptor = p_oTheoriticalDescriptor;

		this.components = new HashMap<String, RealVisualComponentDescriptor>();
		for(TheoriticalVisualComponentDescriptor oTheoriticalDescriptor : this.theoriticalDescriptor.getComponents()) {
			this.components.put(oTheoriticalDescriptor.getName(), new RealVisualComponentDescriptor(oTheoriticalDescriptor));
		}
	}
	
	/**
	 * Returns a list of sub components
	 *
	 * @return sub components
	 */
	public Collection<RealVisualComponentDescriptor> getChildren() {
		return this.components.values();
	}

	/**
	 * Returns all children of this descriptor that are not layout.
	 *
	 * @return All children of this descriptor that are not layout.
	 */
	public Map<String, List<RealVisualComponentDescriptor>> getFieldConfigurationsToApply() {
		return this.fieldConfigurationsToApply;
	}

	/**
	 * Returns the sub component with the given name or null if this component does not exist.
	 *
	 * @param p_sName
	 * 		Name of the searched component.
	 * @return The sub component with the given name or null if this component does not exist.
	 */
	public RealVisualComponentDescriptor getChildByName(final String p_sName) {
		return this.components.get(p_sName);
	}
	
	/**
	 * Gets path to retreive view model
	 *
	 * @return path to retreive view model
	 */
	public List<String> getViewModelPath() {
		return this.viewModelPath;
	}
	
	/**
	 * Gets path to retreive ui component.
	 *
	 * @return path to retreiv ui component.
	 */
	public List<String> getVisualPath() {
		return this.visualPath;
	}

	/**
	 * Gets the view model path
	 *
	 * @return the view model path, flat representation
	 */
	public String getCompleteViewModelPath() {
		return this.completeViewModelPath;
	}
	
	/**
	 * Gets the theoritical descriptor associated
	 *
	 * @return theoritical descriptor
	 */
	public TheoriticalVisualComponentDescriptor getTheoriticalDescriptor() {
		return this.theoriticalDescriptor;
	}
	
	/**
	 * Gets the associated configuration from VisualComponentConfigurationHandler. To choose configuration system use the model name.
	 *
	 * @return associated configuration
	 */
	public VisualComponentConfiguration getDirectConfiguration() {
		return ConfigurationsHandler.getInstance().getVisualConfiguration(this.theoriticalDescriptor.getConfigurationName());
	}

	/**
	 * Link configuration, eg compute view model path, and the visual configuration to apply
	 */
	public void linkConfiguration() {
		List<RealVisualComponentDescriptor> oParents = new ArrayList<RealVisualComponentDescriptor>();
		this.linkConfiguration(oParents);
	}
	
	/**
	 * Link configuration, eg compute view model path, and the visual configuration to apply
	 * @param p_oParents the components parents
	 */
	private void linkConfiguration(List<RealVisualComponentDescriptor> p_oParents) {
		List<RealVisualComponentDescriptor> oNewParents = new ArrayList<RealVisualComponentDescriptor>(p_oParents);
		if (this.theoriticalDescriptor.isMaster()) {
			oNewParents.add(this);
		}
		else if (this.theoriticalDescriptor.getComponents().isEmpty()){
			Iterator<RealVisualComponentDescriptor> iterParents = oNewParents.iterator();
			if (iterParents.hasNext()) {
			// on ne prend pas le premier qui correspond au view model de base
				RealVisualComponentDescriptor oParentRealDsc = iterParents.next();
				TheoriticalVisualComponentDescriptor oParentTheoDsc;
				while (iterParents.hasNext()) {
					oParentRealDsc = iterParents.next();
					oParentTheoDsc = oParentRealDsc.getTheoriticalDescriptor();
					this.visualPath.add(oParentTheoDsc.getName());
					this.viewModelPath.add(oParentTheoDsc.getModel());
				}
			}
		}

		VisualComponentConfiguration oGc = this.getDirectConfiguration();
		if (oGc != null) {
			if (!oNewParents.isEmpty()) {
				oNewParents.get(0).addGraphConfiguration(oGc.getName(), this);
			}
		}

		List<RealVisualComponentDescriptor> listChildren = new ArrayList<RealVisualComponentDescriptor>(this.getChildren());
		for(RealVisualComponentDescriptor oDescriptor : listChildren) {
			oDescriptor.linkConfiguration(oNewParents);
		}

		this.computePaths();
	}

	/**
	 * Computes the path of this descriptor.
	 * 
	 * @param p_bIsCustom
	 * 		This descriptor is a custom field.
	 */
	private void computePaths() {

		//finition des view model path
		// on ajoute son propre nom
		this.viewModelPath.add(this.theoriticalDescriptor.getModel());
		this.visualPath.add(this.theoriticalDescriptor.getName());
		List<String> oTempVMPath = new ArrayList<String>();
		StringBuilder oTempCompletePath = new StringBuilder();
		Iterator<String> oItPath = this.viewModelPath.iterator();
		int iN = 0;
		String sName = null;
		String[] oNames = null;
		while(oItPath.hasNext()) {
			sName = oItPath.next();
			if (sName.contains(PATH_SEPARATOR_IN)) {
				oNames = sName.split(PATH_SEPARATOR_IN);
				iN = 1;
				for(String sName2 : oNames) {
					oTempVMPath.add(sName2);
					if(oTempCompletePath.length()>0) {
						oTempCompletePath.append(PATH_SEPARATOR_OUT);
					}
					oTempCompletePath.append(sName2);
					iN++;
				}
			}
			else {
				if(oTempCompletePath.length()>0) {
					oTempCompletePath.append(PATH_SEPARATOR_OUT);
				}
				oTempCompletePath.append(sName);
				oTempVMPath.add(sName);
			}
		}
		// le premier morceau est inutile
		//oTempVMPath.remove(0);
		this.completeViewModelPath = oTempCompletePath.toString();
		this.viewModelPath = oTempVMPath;
	}
	
	/**
	 * Adds a configuration to apply.
	 * @param p_sConfigurationName the name of configuration
	 * @param p_oComponentTarget the component where configuration will be applied
	 */
	private void addGraphConfiguration(String p_sConfigurationName, RealVisualComponentDescriptor p_oComponentTarget) {

		Map<String, List<RealVisualComponentDescriptor>> mapVisualConfigurationsToApply;
		mapVisualConfigurationsToApply = this.fieldConfigurationsToApply;

		List<RealVisualComponentDescriptor> oTargets = mapVisualConfigurationsToApply.get(p_sConfigurationName);
		if (oTargets == null) {
			oTargets = new ArrayList<RealVisualComponentDescriptor>();
			mapVisualConfigurationsToApply.put(p_sConfigurationName, oTargets);
		}
		if (!oTargets.contains(p_oComponentTarget)) {
			oTargets.add(p_oComponentTarget);
		}
	}
	
	/**
	 * Modifies a master component, with configuration (register).
	 *
	 * @param p_sKeyView the key of view, used when several screen use the same model
	 * @param p_oComposite master component where apply configuration
	 * @param p_oParameters parameters to use to compute some rules
	 * @param p_sFragmentTag a {@link java.lang.String} object.
	 */
	public void inflate(String p_sKeyView, MasterVisualComponent p_oComposite, Map<String, Object> p_oParameters, String p_sFragmentTag) {
		this.inflateFields(p_sKeyView, p_oComposite, p_oParameters, this.fieldConfigurationsToApply, p_sFragmentTag);
	}
	
	/**
	 * Modifies a master component, with configuration (register).
	 *
	 * @param p_sKeyView the key of view, used when several screen use the same model
	 * @param p_oComposite master component where apply configuration
	 * @param p_oParameters parameters to use to compute some rules
	 */
	public void inflate(String p_sKeyView, MasterVisualComponent p_oComposite, Map<String, Object> p_oParameters) {
		this.inflateFields(p_sKeyView, p_oComposite, p_oParameters, this.fieldConfigurationsToApply, null);
	}

	/**
	 * Modifies a master component, with configuration (register).
	 * @param p_sKeyView the key of view, used when several screen use the same model
	 * @param p_oComposite master component where apply configuration
	 * @param p_oParameters parameters to use to compute some rules
	 * @param p_oDescriptorsByName the descriptors to inflate
	 * @param p_sFragmentTag fragment identifier
	 */
	private void inflateFields(final String p_sKeyView, final MasterVisualComponent p_oComposite, final Map<String, Object> p_oParameters,
			Map<String, List<RealVisualComponentDescriptor>> p_oDescriptorsByName, String p_sFragmentTag) {
		
		List<ConfigurableVisualComponent> oComponents = null;
		ConfigurableVisualComponent oComponent = null;

		for (Map.Entry<String, List<RealVisualComponentDescriptor>> oEntry : p_oDescriptorsByName.entrySet()) {
			oComponents = new ArrayList<ConfigurableVisualComponent>();
			BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) ConfigurationsHandler.getInstance().getVisualConfiguration(oEntry.getKey());
			for(RealVisualComponentDescriptor oComponentDescriptor : oEntry.getValue()) {
				oComponent = p_oComposite.retrieveConfigurableVisualComponent(oComponentDescriptor.visualPath, true);
				if (oComponent != null) {
					oComponent.getComponentFwkDelegate().setDescriptor(oComponentDescriptor);
					oComponent.getComponentFwkDelegate().setFragmentTag(p_sFragmentTag);
					oComponents.add(oComponent);
				}
			}
			if (!oComponents.isEmpty()){
				oConfiguration.applyOn(p_sKeyView, p_oComposite, oComponents, p_oParameters);
			}
		}
	}
	
	/**
	 * Modifies a master component, with configuration (unregister).
	 *
	 * @param p_sKeyView the key of view, used when several screen use the same model
	 * @param p_oComposite master component where apply configuration
	 * @param p_oParameters parameters to use to compute some rules
	 */
	public void unInflate(String p_sKeyView, MasterVisualComponent p_oComposite, Map<String, Object> p_oParameters) {
		this.unInflateFields(p_sKeyView, p_oComposite, p_oParameters, this.fieldConfigurationsToApply);
	}

	/**
	 * Modifies a master component, with provided configuration (unregister).
	 * @param p_sKeyView the key of view, used when several screen use the same model
	 * @param p_oComposite master component where apply configuration
	 * @param p_oParameters parameters to use to compute some rules
	 * @param p_oDescriptorsByName The descriptors to treat
	 */
	private void unInflateFields(final String p_sKeyView, final MasterVisualComponent p_oComposite, final Map<String, Object> p_oParameters,
			Map<String, List<RealVisualComponentDescriptor>> p_oDescriptorsByName) {

		List<ConfigurableVisualComponent> oComponents = null;
		ConfigurableVisualComponent oComponent = null;

		for (Map.Entry<String, List<RealVisualComponentDescriptor>> oEntry : p_oDescriptorsByName.entrySet()) {
			oComponents = new ArrayList<ConfigurableVisualComponent>();
			BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) ConfigurationsHandler.getInstance().getVisualConfiguration(oEntry.getKey());
		
			for(RealVisualComponentDescriptor oComponentDescriptor : oEntry.getValue()) {
				oComponent = p_oComposite.retrieveConfigurableVisualComponent(oComponentDescriptor.visualPath, false);
				if (oComponent != null) {
					oComponent.getComponentFwkDelegate().setDescriptor(oComponentDescriptor);
					oComponents.add(oComponent);
				}
			}
			if (!oComponents.isEmpty()){
				oConfiguration.unApplyOn(p_sKeyView, p_oComposite, oComponents, p_oParameters);
			}
		}
	}
}
