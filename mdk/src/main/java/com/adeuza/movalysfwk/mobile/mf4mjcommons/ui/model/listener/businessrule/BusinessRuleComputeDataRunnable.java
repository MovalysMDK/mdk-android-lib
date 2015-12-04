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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.listener.businessrule;

import java.lang.ref.WeakReference;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * This runnable inner class is used when a "applyRuleForComponent" should be
 * called in a task out of the ui thread.
 * 
 */
class BusinessRuleComputeDataRunnable implements Runnable {

	/**
	 * The PropertyTarget
	 */
	private PropertyTarget propertyTarget;

	/**
	 * A Weakreference to the ViewModel used to retrieve the data
	 */
	private WeakReference<ConfigurableVisualComponent> component;

	/**
	 * The key used to identify the component to update
	 */
	private Boolean ruleResult;

	/**
	 * The public constructor of the runnable
	 *
	 * @param p_oPropertyTarget a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.listener.businessrule.PropertyTarget} object.
	 * @param p_oComponent a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent} object.
	 * @param p_oRuleResult a {@link java.lang.Boolean} object.
	 */
	public BusinessRuleComputeDataRunnable(PropertyTarget p_oPropertyTarget, ConfigurableVisualComponent p_oComponent, Boolean p_oRuleResult) {
		this.propertyTarget = p_oPropertyTarget;
		this.component = new WeakReference<ConfigurableVisualComponent> (p_oComponent);
		this.ruleResult = p_oRuleResult;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if (this.ruleResult == null || this.ruleResult) {
			if (this.propertyTarget == PropertyTarget.HIDDEN) {
				this.component.get().getComponentDelegate().hide();
			} else if (this.propertyTarget == PropertyTarget.ENABLE) {
				this.component.get().getComponentDelegate().configurationEnabledComponent();
			} else if (this.propertyTarget == PropertyTarget.MANDATORY) {
				this.component.get().getComponentFwkDelegate().getDescriptor().getTheoriticalDescriptor().setMandatory(this.ruleResult);
				this.component.get().getComponentDelegate().configurationSetMandatoryLabel();
			}
		} else {
			if (this.propertyTarget == PropertyTarget.HIDDEN) {
				this.component.get().getComponentDelegate().unHide();
			} else if (this.propertyTarget == PropertyTarget.ENABLE) {
				this.component.get().getComponentDelegate().configurationDisabledComponent();
			} else if (this.propertyTarget == PropertyTarget.MANDATORY) {
				this.component.get().getComponentFwkDelegate().getDescriptor().getTheoriticalDescriptor().setMandatory(this.ruleResult);
				this.component.get().getComponentDelegate().configurationRemoveMandatoryLabel();
			}
		}
	}
}
