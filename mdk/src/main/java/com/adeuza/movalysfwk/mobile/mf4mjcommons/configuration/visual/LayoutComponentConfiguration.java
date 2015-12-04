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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * <p>Defines configuration for layout</p>
 *
 *
 */
public class LayoutComponentConfiguration extends VisualComponentConfiguration{
	/**
	 * Version number of this class implementation.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Custom fields.
	 */
	private Collection<BasicComponentConfiguration> fields = null;

	/** sets of custom fields */
	private Map<String, BasicComponentConfiguration> fieldsByName = null;

	/**
	 * Constructs a new configuration for a layout
	 *
	 * @param p_sName the configuration's name
	 */
	public LayoutComponentConfiguration(String p_sName) {
		super(p_sName);
		this.fields			= new ArrayList<BasicComponentConfiguration>();
		this.fieldsByName	= new HashMap<String, BasicComponentConfiguration>();
	}

	/**
	 * Returns all fields in this layout. Caution: Any change in the returned collection will affect the configuration of the layout
	 *
	 * @return All fields in this layout. Possibly an empty collection.
	 */
	public final Collection<BasicComponentConfiguration> getFieldConfigurations() {
		return this.fields;
	}

	/**
	 * Returns the field named <em>p_sFieldName</em> or null if this fields does not exist.
	 *
	 * @param p_sFieldName
	 * 		Name of the searched field. Mandatory.
	 * @return The field named <em>p_sFieldName</em> or null if this fields does not exist.
	 */
	public final BasicComponentConfiguration getFieldConfiguration(final String p_sFieldName) {
		return this.fieldsByName.get(p_sFieldName);
	}

	/**
	 * Defines the fields in this layout. All existing field are dropped to this layout before add the add.
	 *
	 * @param p_oFields
	 * 		The fiels in this layout.
	 */
	public void setFieldConfigurations(Collection<BasicComponentConfiguration> p_oFields) {
		this.fields.clear();
		this.fieldsByName.clear();
		if (p_oFields != null) {
			for (BasicComponentConfiguration oField : p_oFields) {
				this.addFieldConfiguration(oField);
			}
		}
	}

	/**
	 * Adds a field configuration to the current configuration
	 *
	 * @param p_oConfiguration the configuration to add
	 */
	public void addFieldConfiguration(BasicComponentConfiguration p_oConfiguration) {
		if (p_oConfiguration != null) {
			if (this.fieldsByName.containsKey(p_oConfiguration.getName())) {
				this.removeField(p_oConfiguration.getName());
			}
			this.fields.add(p_oConfiguration);
			this.fieldsByName.put(p_oConfiguration.getName(), p_oConfiguration);
		}
	}

	/**
	 * Removes a field.
	 *
	 * @param p_oConfiguration
	 * 		The field to remove.
	 */
	public void removeField(BasicComponentConfiguration p_oConfiguration) {
		if (p_oConfiguration != null) {
			this.removeField(p_oConfiguration.getName());
		}
	}

	/**
	 * Removes a field using its name.
	 *
	 * @param p_sFieldName
	 * 		The name of the field to remove.
	 */
	public void removeField(String p_sFieldName) {
		BasicComponentConfiguration oField = this.fieldsByName.get(p_sFieldName);
		if (oField != null) {
			this.fields.remove(oField);
			this.fieldsByName.remove(p_sFieldName);
		}
	}

	/**
	 * {@inheritDoc}
	 * <li>processing custom fields</li>
	 */
	@Override
	public void applyOn(final String p_sKeyView, final MasterVisualComponent p_oComposite, final List<ConfigurableVisualComponent> p_oComponents,
			final Map<String, Object> p_oRuleResults) {

		super.applyOn(p_sKeyView, p_oComposite, p_oComponents, p_oRuleResults);
		this.treatAutobindProperty(p_sKeyView, p_oComposite, p_oComponents);
	}

	/**
	 * Processing the autobind configuration
	 *
	 * @param p_sKeyView the key of view, used when several screen use the same model
	 * @param p_oComposite master component where apply configuration
	 * @param p_oComponents the list of components to treat
	 */
	public void treatAutobindProperty(final String p_sKeyView, final MasterVisualComponent p_oComposite, final List<ConfigurableVisualComponent> p_oComponents) {
		final ViewModel oViewModel = p_oComposite.getViewModel();

		for(ConfigurableVisualComponent oComponent : p_oComponents) {
			if (oComponent.getComponentFwkDelegate().isValue()) {
				oViewModel.registerReadableDataComponents(oComponent);
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public void unApplyOn(String p_sKeyView,
			MasterVisualComponent p_oComposite,
			List<ConfigurableVisualComponent> p_oComponents,
			Map<String, Object> p_oRuleResults) {
		super.unApplyOn(p_sKeyView, p_oComposite, p_oComponents, p_oRuleResults);

		this.untreatAutobindProperty(p_sKeyView, p_oComposite, p_oComponents);
	}

	/**
	 * untreat autobind properties
	 * @param p_sKeyView the view key
	 * @param p_oComposite the master component
	 * @param p_oComponents the configurable components
	 */
	private void untreatAutobindProperty(String p_sKeyView,
			MasterVisualComponent p_oComposite,
			List<ConfigurableVisualComponent> p_oComponents) {

		final ViewModel oViewModel = p_oComposite.getViewModel();

		for(ConfigurableVisualComponent oComponent : p_oComponents) {
			if (oComponent.getComponentFwkDelegate().isValue()) {
				oViewModel.unregister(oComponent);
			}
		}
	}

}
