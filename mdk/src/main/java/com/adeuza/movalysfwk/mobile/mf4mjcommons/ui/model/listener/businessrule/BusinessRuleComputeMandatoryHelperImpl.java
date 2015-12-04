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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * <p>BusinessRuleComputeMandatoryHelperImpl class.</p>
 *
 */
public class BusinessRuleComputeMandatoryHelperImpl implements
		BusinessRuleComputeHelper<Boolean> {

	/** {@inheritDoc} */
	@Override
	public void applyRuleForComponent(ConfigurableVisualComponent p_oComponent, Boolean p_oRuleResult) {
		if (Application.getInstance().isOnUIThread()) {
			p_oComponent.getComponentFwkDelegate().getDescriptor().getTheoriticalDescriptor().setMandatory(p_oRuleResult);
			if (p_oRuleResult) {
				p_oComponent.getComponentDelegate().configurationSetMandatoryLabel();
			} else {
				p_oComponent.getComponentDelegate().configurationRemoveMandatoryLabel();
			}
		} else {
			Application.getInstance().runOnUiThread(new BusinessRuleComputeDataRunnable(PropertyTarget.MANDATORY, p_oComponent, p_oRuleResult));
		}
	}

}
