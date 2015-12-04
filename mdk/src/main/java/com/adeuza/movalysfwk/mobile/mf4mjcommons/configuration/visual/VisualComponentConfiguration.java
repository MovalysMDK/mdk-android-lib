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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.AbstractConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;

/**
 * <p>This class represents a visual configuration applicable to a configurable component.</p>
 *
 *
 */
public class VisualComponentConfiguration extends AbstractConfiguration {
	/**
	 * Version number of this class implementation.
	 */
	private static final long serialVersionUID = 1L;

	/** indicates the behavior to use when processing the visibility */
	private boolean visible = true;

	/**
	 * Constructs a new visual configuration
	 *
	 * @param p_sName the configuration's name
	 */
	public VisualComponentConfiguration(final String p_sName) {
		super(p_sName, ConfigurationsHandler.TYPE_VISUAL);
	}
	
	/**
	 * Indicates whether the associated visual components is visible
	 *
	 * @return true whether is visible
	 */
	public final boolean isVisible() {
		return this.visible;
	}

	/**
	 * Defines the visibility of this component
	 *
	 * @param p_bVisible
	 * 		<code>true</code> if this component is visible, <code>false</code> otherwise.
	 */
	public final void setVisible(final boolean p_bVisible) {
		this.visible = p_bVisible;
	}
	
	/**
	 * Applies the configuration in the list of configurable components p_oComponents.
	 * The treatments applied are:<br/>
	 * <li>processing of visibility</li>
	 *
	 * @param p_sKeyView the key of view, used when several screen use the same model
	 * @param p_oComposite master component where apply configuration
	 * @param p_oComponents components on which to apply the configuration
	 * @param p_oRuleResults result of rule to compute treatment
	 */
	public void applyOn(final String p_sKeyView, final MasterVisualComponent p_oComposite, final List<ConfigurableVisualComponent> p_oComponents,
			final Map<String ,Object> p_oRuleResults) {

		this.treatVisibleProperty(p_sKeyView, p_oComposite, p_oComponents);
	}
	
	/**
	 * Unapplies the configuration in the list of configurable components p_oComponents.
	 *
	 * @param p_sKeyView the key of view, used when several screen use the same model
	 * @param p_oComposite master component where apply configuration
	 * @param p_oComponents components on which to apply the configuration
	 * @param p_oRuleResults result of rule to compute treatment
	 */
	public void unApplyOn(final String p_sKeyView, final MasterVisualComponent p_oComposite, final List<ConfigurableVisualComponent> p_oComponents, 
			final Map<String ,Object> p_oRuleResults) {
		//Nothing To Do
	}
	
	/**
	 * Processes the visibility treatment. 
	 * If a component is a "group" is the only one to be hidden.
	 * @param p_sKeyView the key of view, used when several screen use the same model
	 * @param p_oComposite master component where apply configuration
	 * @param p_oComponents components on which to apply the configuration
	 */
	private void treatVisibleProperty(final String p_sKeyView, final MasterVisualComponent p_oComposite, final List<ConfigurableVisualComponent> p_oComponents) {
		if (!this.visible) {
			// on rend invisible les éléments
			for(ConfigurableVisualComponent oComponent : p_oComponents) {
				oComponent.getComponentDelegate().configurationHide(true); // la mise à jour est prioritaire donc on passe true en paramètre pour locker/delocker le hide
			}
		}
		else {
			// on rend visible les éléments
			for(ConfigurableVisualComponent oComponent : p_oComponents) {
				oComponent.getComponentDelegate().configurationUnHide(true); // la mise à jour est prioritaire donc on passe true en paramètre pour locker/delocker le hide
			}
		}
	}
}
