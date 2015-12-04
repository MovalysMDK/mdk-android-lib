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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;

/**
 * <p>TODO Décrire la classe ManageSynchronizationParameter</p>
 *
 *
 */
public class ManageSynchronizationParameter extends AbstractActionParameter implements ActionParameter {

	/**
	 * serial version ID
	 */
	private static final long serialVersionUID = 4152367045528084186L;
	/**
	 * Indique s'il y a des éléments dans la table T_MOBJECTTOSYNCHRONIZE
	 */
	boolean mmToBo;

	/**
	 *
	 * TODO Décrire le constructeur ManageSynchronizationParameter
	 *
	 * @param p_bMmToBo a boolean.
	 */
	public ManageSynchronizationParameter(final boolean p_bMmToBo) {
		this.mmToBo = p_bMmToBo;
	}

	/**
	 * <p>isMmToBo.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isMmToBo() {
		return this.mmToBo;
	}
}
