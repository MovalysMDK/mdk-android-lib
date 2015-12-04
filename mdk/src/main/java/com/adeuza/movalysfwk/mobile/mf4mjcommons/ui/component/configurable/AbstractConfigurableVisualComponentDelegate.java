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

import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.delegate.DelegateDatatypeUtils;

/**
 * Abstract definition for component delegate
 * this delegate centralize some of the default behavior of a component in MDK framework
 *
 * @param <VALUE> the data type handled by the component
 */
public abstract class AbstractConfigurableVisualComponentDelegate<VALUE> implements VisualComponentDelegate<VALUE> {

	/** reference to the component managed by the delegate */
	protected ConfigurableVisualComponent currentCvComponent;
	
	/** indicates whether the component is filled */
	protected boolean filled = false;
	
	/** mandatory flag ; true if the field has a mandatory property set */
	protected boolean mandatoryFlag = false;
	
	/** parametric class for this delegate */
	protected Class<?> delegateType;
	
	/**
	 * Constructor of the component delegate
	 * @param p_oCurrentView the component managed by the delegate
	 * @param p_oDelegateType the type handled by the component
	 */
	public AbstractConfigurableVisualComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType) {
		this.currentCvComponent =  p_oCurrentView;
		this.delegateType = p_oDelegateType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VALUE configurationGetValue() {
		return null;
	}

	/**
	 * Returns the given value formatted if the formatter exists
	 * @param p_oSourceValue the source value to transform
	 * @return the formatted value if formatter exists
	 */
	@SuppressWarnings("unchecked")
	public VALUE getFormattedValue(VALUE p_oSourceValue) {
		VALUE r_oReturnValue = p_oSourceValue;
		if(this.currentCvComponent.getComponentFwkDelegate().customFormatter() != null && p_oSourceValue != null) {
			r_oReturnValue = (VALUE) this.currentCvComponent.getComponentFwkDelegate().customFormatter().format(p_oSourceValue);
		}
		return r_oReturnValue;
	}
	
	/**
	 * Returns the given value unformatted if the formatter exists
	 * @param p_oSourceValue the source value to transform
	 * @return the unformatted value if formatter exists
	 */
	@SuppressWarnings("unchecked")
	public VALUE getUnFormattedValue(VALUE p_oSourceValue) {
		VALUE r_oReturnValue = p_oSourceValue;
		if (this.currentCvComponent.getComponentFwkDelegate().customFormatter() != null && p_oSourceValue != null) {
			r_oReturnValue = (VALUE) this.currentCvComponent.getComponentFwkDelegate().customFormatter().unformat(p_oSourceValue);
		}
		return r_oReturnValue;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(VALUE p_oObject) {
		return DelegateDatatypeUtils.getNullOrEmptyComputer(this.delegateType).isNullOrEmpty(p_oObject);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.filled;
	}
	
	/**
	 * set is the current linked component is filled
	 * @param p_bFilled the component is filled
	 */
	public void setFilled(boolean p_bFilled) {
		this.filled = p_bFilled;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationRemoveMandatoryLabel()
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		this.mandatoryFlag = false;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#setConfigurationSetMandatoryLabel()
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		this.mandatoryFlag = true;
	}

	/**
	 * Return true whether mandatory label is set
	 * @return true whether mandatory label is set
	 */
	public boolean isFlagMandatory() {
		return this.mandatoryFlag;
	}

	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		return true;
	}

	@Override
	public boolean isAlwaysEnabled() {
		return this.currentCvComponent.getClass().getAnnotation(MMAlwaysEnable.class) != null;
	}
}
