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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;

/**
 * <p>PropertyTarget class.</p>
 *
 */
public enum PropertyTarget {

	HIDDEN("hiddenBusinessCompute"),
	ENABLE("enableBusinessCompute"),
	MANDATORY("mandatoryBusinessCompute");
	
	private BusinessRuleComputeHelper computeClass;
	
	private PropertyTarget(String p_sComputeClass) {
		computeClass = (BusinessRuleComputeHelper) BeanLoader.getInstance().instantiatePrototype(p_sComputeClass);
	}

	/**
	 * <p>getComputeHelper.</p>
	 *
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.listener.businessrule.BusinessRuleComputeHelper} object.
	 */
	public BusinessRuleComputeHelper getComputeHelper() {
		return this.computeClass;
	}
	
	
}
