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
package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdelete;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.AbstractBusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.RedirectActionParameterImpl;

/**
 * Chain delete detail action interface
 * 
 */
public interface ChainDeleteDetailAction
		extends
		Action<ChainDeleteActionDetailParameter, RedirectActionParameterImpl, DefaultActionStep, Void> {

	/**
	 * chain delete detail action
	 *
	 */
	public static class DeleteEntityEvent extends AbstractBusinessEvent<MEntity> {
		/**
		 * Constructor
		 * 
		 * @param p_oSource
		 *            source event.
		 * @param p_oData
		 *            entity.
		 */
		public DeleteEntityEvent(Object p_oSource, MEntity p_oData) {
			super(p_oSource, p_oData);
		}
	}
}
