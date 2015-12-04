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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.LayoutComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.VisualComponentDescriptorsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.Screen;

/**
 * <p>
 * A delegate for master visual component
 * </p>
 *
 */
public class MasterVisualComponentDelegate {

	/** associated model */
	private ViewModel viewModel = null;
	/** the parent of delegate */
	private MasterVisualComponent parent = null;
	/** Parameters used to compute some rules */
	private Map<String, Object> parameters = null;

	/**
	 * To store custom components by layout configuration.
	 * Use to delete custom fields of a layout
	 */
	private Map<LayoutComponentConfiguration, List<ConfigurableVisualComponent>> visualComponentsByLayoutConfiguration;

	/**
	 * Constructs a new MasterVisualComponent
	 *
	 * @param p_oParent
	 *            the parent of compoennt
	 */
	public MasterVisualComponentDelegate(final MasterVisualComponent p_oParent) {
		this.parent = p_oParent;
		this.visualComponentsByLayoutConfiguration = new HashMap<LayoutComponentConfiguration, List<ConfigurableVisualComponent>>();
	}

	/**
	 * Sets the view model
	 *
	 * @param p_oViewModel
	 *            the associated view model
	 */
	public void setViewModel(final ViewModel p_oViewModel) {
		this.viewModel = p_oViewModel;
	}

	/**
	 * Returns the associated view model
	 *
	 * @return the associated view model
	 */
	public ViewModel getViewModel() {
		return this.viewModel;
	}

	/**
	 * Modifies the current component (register in view model ...)
	 *
	 * @param p_bWriteData a boolean.
	 */
	public void inflate(boolean p_bWriteData) {
		String sName = null;
		
		if (this.parent.getComponentFwkDelegate() != null && this.parent.getComponentFwkDelegate().getName() != null) {
			sName = this.parent.getComponentFwkDelegate().getName();
		} else if (this.parent instanceof Screen) {
			sName = ((Screen)this.parent).getName();
		}
		
		if (sName != null) {
			// récupération du descripteur
			RealVisualComponentDescriptor oDescriptor = VisualComponentDescriptorsHandler.getInstance().getRealDescriptor(sName);
			oDescriptor.inflate(String.valueOf(this.hashCode()), this.parent, this.parent.getParameters(), null);

			if (p_bWriteData) {
				this.parent.getViewModel().doOnDataLoaded(null);
			}
		}
	}

	/**
	 * Modify the current component (unregister in view model ...)
	 */
	public void unInflate() {
		String sName = null;
		
		if (this.parent.getComponentFwkDelegate() != null && this.parent.getComponentFwkDelegate().getName() != null) {
			sName = this.parent.getComponentFwkDelegate().getName();
		} else if (this.parent instanceof Screen) {
			sName = ((Screen)this.parent).getName();
		}
		
		if (sName != null) {
			RealVisualComponentDescriptor oDescriptor = VisualComponentDescriptorsHandler.getInstance().getRealDescriptor(sName);
			oDescriptor.unInflate(String.valueOf(this.hashCode()), this.parent, this.parent.getParameters());
		}
	}

	/**
	 * Registers a list of custom fields displayed on a specific layout.
	 *
	 * @param p_oLayoutConfiguration Layout configuration. Mandatory.
	 * @param p_oComponents Components to register
	 */
	public void registerConfigurableVisualComponentsOf(LayoutComponentConfiguration p_oLayoutConfiguration, List<ConfigurableVisualComponent> p_oComponents) {
		if (p_oLayoutConfiguration != null) {
			this.visualComponentsByLayoutConfiguration.put(p_oLayoutConfiguration, p_oComponents);
		}
	}

	/**
	 * Unregisters components displayed on a specific layout
	 *
	 * @param p_oLayoutConfiguration Layout configuration. Mandatory.
	 */
	public void unregisterConfigurableVisualComponentsOf(LayoutComponentConfiguration p_oLayoutConfiguration) {
		if (p_oLayoutConfiguration != null) {
			this.visualComponentsByLayoutConfiguration.remove(p_oLayoutConfiguration);
		}
	}

	/**
	 * Returns the components displayed on a layout
	 *
	 * @param p_oLayoutConfiguration Layout configuration. Mandatory.
	 * @return the components displayed on a layout
	 */
	public List<ConfigurableVisualComponent> getConfigurableVisualComponentsOf(LayoutComponentConfiguration p_oLayoutConfiguration) {
		List<ConfigurableVisualComponent> r_oComponent = null;
		if (p_oLayoutConfiguration != null) {
			r_oComponent = this.visualComponentsByLayoutConfiguration.get(p_oLayoutConfiguration);
		}
		return r_oComponent;
	}
	
	/**
	 * Set parameteres used to compute some rules
	 *
	 * @param p_oParameters A map of parameters
	 */
	public void setParameters(Map<String, Object> p_oParameters) {
		this.parameters = p_oParameters;
	}
	
	/**
	 * Returns the map of the parameters used to compute some rules
	 *
	 * @return A map of parameters
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}
}
