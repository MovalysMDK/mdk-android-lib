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

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.entity.AbstractEntityFieldConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;
/**
 * <p>Defines configuration for basic component</p>
 *
 */
public class BasicComponentConfiguration extends VisualComponentConfiguration {
	/**
	 * Version number of this class implementation.
	 */
	private static final long serialVersionUID = 1L;
	/** label component */
	private String label = null;
	/** hide if value is null and component not edit type */
	private boolean hideEmpty = false;
	/** field's parameter */
	private Map<String, Object> parameters = new TreeMap<String, Object>();
	/** associated entity field in read mode */
	private AbstractEntityFieldConfiguration entityFieldConfigurationIn = null;
	/** true if the field is disabled. */
	private boolean disabled = false;
	/**
	 * Type of this UI Component
	 */
	private int type;
	/**
	 * Constructs a new BasicComponentConfiguration
	 *
	 * @param p_sName the configuration's name
	 */
	public BasicComponentConfiguration(String p_sName) {
		this(p_sName, (String)null);
	}
	/**
	 * Constructs a new BasicComponentConfiguration
	 *
	 * @param p_sName the configuration's name
	 * @param p_sLabel the label to display
	 */
	public BasicComponentConfiguration(String p_sName, String p_sLabel) {
		super(p_sName);
		this.label = p_sLabel;
		this.parameters = new TreeMap<String, Object>();
	}
	/**
	 * Constructs a new custom field configuration
	 *
	 * @param p_sName the custom field's name
	 * @param p_oEntityFieldConfiguration the associated entity field
	 */
	public BasicComponentConfiguration(String p_sName, AbstractEntityFieldConfiguration p_oEntityFieldConfiguration) {
		this(p_sName, null, p_oEntityFieldConfiguration);
	}	
	/**
	 * Constructs a new custom field configuration
	 *
	 * @param p_sName the custom field's name
	 * @param p_sLabel the custom field's label
	 * @param p_oEntityFieldConfiguration the associated entity field
	 */
	public BasicComponentConfiguration(String p_sName, String p_sLabel, AbstractEntityFieldConfiguration p_oEntityFieldConfiguration) {
		this(p_sName, p_sLabel);
		this.entityFieldConfigurationIn = p_oEntityFieldConfiguration;
	}
	/**
	 * {@inheritDoc}
	 * <li>processing label treatment</li>
	 * <li>processing autobind treatment</li>
	 */
	@Override
	public void applyOn(final String p_sKeyView, final MasterVisualComponent p_oComposite, final List<ConfigurableVisualComponent> p_oComponents,
			final Map<String, Object> p_mapRuleParameters) {

		super.applyOn(p_sKeyView, p_oComposite, p_oComponents, p_mapRuleParameters);

		this.treatLabelProperty(p_sKeyView, p_oComposite, p_oComponents, p_mapRuleParameters);
		this.treatAutobindProperty(p_sKeyView, p_oComposite, p_oComponents);
	}

	/**
	 * {@inheritDoc}
	 * <li>processing autobind treatment</li>
	 */
	@Override
	public void unApplyOn(String p_sKeyView, final MasterVisualComponent p_oComposite, List<ConfigurableVisualComponent> p_oComponents, Map<String, Object> p_oRuleResults) {
		super.unApplyOn(p_sKeyView, p_oComposite, p_oComponents, p_oRuleResults);
		
		this.unTreatLabelProperty(p_sKeyView, p_oComposite, p_oComponents);
		this.unTreatAutobindProperty(p_sKeyView, p_oComposite, p_oComponents);
	}	
	
	/**
	 * Sets to true the use of rule hide if empty
	 */
	public void hideEmpty() {
		this.hideEmpty = true;
	}
	
	/**
	 * Returns label
	 *
	 * @return label
	 */
	public final String getLabel() {
		return this.label;
	}
	
	/**
	 * Overrides the current label of this component.
	 *
	 * @param p_sLabel
	 * 		The new label of this component.
	 */
	public final void setLabel(final String p_sLabel) {
		this.label = p_sLabel;
	}
	
	/**
	 * Returns an integer that represents the type of this component (textbox, combobox, etc.).
	 *
	 * @return An integer that represents the type of this component (textbox, combobox, etc.).
	 */
	public final int getType() {
		return this.type;
	}
	
	/**
	 * Defines the type of this component (textbox, combobox, etc.).
	 *
	 * @param p_iType The type of this component.
	 */
	protected final void setType(final int p_iType) {
		this.type = p_iType;
	}
	
	/**
	 * Returns <code>true</code> if the field is disabled, <code>false</code> otherwise.
	 *
	 * @return <code>true</code> if the field is disabled, <code>false</code> otherwise.
	 */
	public final boolean isDisabled() {
		return this.disabled;
	}
	
	/**
	 * Define the "disabled" property of this field.
	 *
	 * @param p_bDisabled
	 * 		<code>true</code> if the field is disabled, <code>false</code> otherwise.
	 */
	public final void setDisabled(final boolean p_bDisabled) {
		this.disabled = p_bDisabled;
	}

	/**
	 * Returns entity field configuration
	 *
	 * @return associated entity field configuration
	 */
	public AbstractEntityFieldConfiguration getEntityFieldConfiguration() {
		return this.entityFieldConfigurationIn;
	}

	/**
	 * Defines the entity field configuration associated to this component.
	 *
	 * @param p_oEntityConfiguration
	 * 		The field configuration associated to this compoment.
	 */
	public final void setEntityFieldConfiguration(final AbstractEntityFieldConfiguration p_oEntityConfiguration) {
		this.entityFieldConfigurationIn = p_oEntityConfiguration;
	}

	/**
	 * Gets the value of parameter named p_sKey
	 *
	 * @param p_sKey the key to find
	 * @return the value for associated key
	 */
	public Object getParameter(String p_sKey) {
		if (this.parameters == null ) {
			this.parameters =  new TreeMap<String, Object>();
		}
		return this.parameters.get(p_sKey);
	}

	/**
	 * Add a parameter
	 *
	 * @param p_sKey the name of parameter
	 * @param p_oValue the value of parameter
	 */
	public void addParameter(String p_sKey, Object p_oValue) {
		if (this.parameters == null ) {
			this.parameters =  new TreeMap<String, Object>();
		}
		this.parameters.put(p_sKey, p_oValue);
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
		
		// on recherche un composant dans le nom se termine par value
		// si la valeur est vide ou null alors on masque les éléments
		for(ConfigurableVisualComponent oComponent : p_oComponents) {
			
			oViewModel.register(oComponent);
			
			if (oComponent.getComponentFwkDelegate().isEdit()) {
				oViewModel.registerReadableDataComponents(oComponent);
				oViewModel.registerWritableDataComponents(oComponent);

			}
			else if (oComponent.getComponentFwkDelegate().isValue()) {
				oViewModel.registerReadableDataComponents(oComponent);
			}
			
			oComponent.getComponentDelegate().doOnPostAutoBind();
			
		}

	}

	/**
	 * Processing the unautobind configuration
	 *
	 * @param p_sKeyView the key of view, used when several screen use the same model
	 * @param p_oComposite master component where apply configuration
	 * @param p_oComponents the list of components to treat
	 */
	public void unTreatAutobindProperty(final String p_sKeyView, final MasterVisualComponent p_oComposite, final List<ConfigurableVisualComponent> p_oComponents) {
		for(ConfigurableVisualComponent oComponent : p_oComponents) {
			this.unTreatAutobindProperty(p_sKeyView, p_oComposite, oComponent);
		}
	}

	/**
	 * Processing the unautobind configuration
	 *
	 * @param p_sKeyView the key of view, used when several screen use the same model
	 * @param p_oComposite master component where apply configuration
	 * @param p_oComponent a component to treat
	 */
	public void unTreatAutobindProperty(final String p_sKeyView, final MasterVisualComponent p_oComposite, final ConfigurableVisualComponent p_oComponent) {
		p_oComposite.getViewModel().unregister(p_oComponent);
		p_oComponent.getComponentDelegate().destroy();
	}

	/**
	 * Processing label property treatment
	 * @param p_sKeyView the key of view, used when several screen use the same model
	 * @param p_oComposite master component where apply configuration
	 * @param p_oComponents the list of components to treat
	 */
	private void treatLabelProperty(final String p_sKeyView, final MasterVisualComponent p_oComposite, final List<ConfigurableVisualComponent> p_oComponents, Map<String, Object> p_mapRuleParameters) {
		boolean bMandatory = false;
		boolean bEditable = false;
		for(ConfigurableVisualComponent oComponent : p_oComponents) {
			if (oComponent.getComponentFwkDelegate().getDescriptor().getTheoriticalDescriptor().isMandatory()) {
				bMandatory = true;
			}

			if (oComponent.getComponentFwkDelegate().getDescriptor().getTheoriticalDescriptor().isEdit()) {
				bEditable = true;
			}
		}

		if (this.label!=null || bMandatory) {
			// modifier le libellé
			for(ConfigurableVisualComponent oComponent : p_oComponents) {
				if (this.label!=null)  {
					oComponent.getComponentFwkDelegate().configurationSetLabel(this.label);
				}
				if (bEditable && bMandatory) {

					oComponent.getComponentDelegate().configurationSetMandatoryLabel();
				}
				else {
					oComponent.getComponentDelegate().configurationRemoveMandatoryLabel();
				}
			}
		}
	}
	
	private void unTreatLabelProperty(final String p_sKeyView, final MasterVisualComponent p_oComposite, final List<ConfigurableVisualComponent> p_oComponents) {
		
		for(ConfigurableVisualComponent oComponent : p_oComponents) {
			if (oComponent.getComponentFwkDelegate().getDescriptor().getTheoriticalDescriptor().isMandatory()) {
				oComponent.getComponentDelegate().configurationRemoveMandatoryLabel();
			}
		}
		
	}
	
}
