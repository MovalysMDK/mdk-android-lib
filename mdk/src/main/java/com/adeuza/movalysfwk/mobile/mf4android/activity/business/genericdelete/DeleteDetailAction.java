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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.EntityActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * delete detail action interface
 *
 * @param <ITEM> entity
 * @param <STATE> action step
 * @param <PROGRESS> progress obj
 */
public interface DeleteDetailAction<ITEM extends MEntity,
	STATE extends ActionStep, 
	PROGRESS extends Object> 
	extends Action<NullActionParameterImpl, EntityActionParameterImpl<ITEM>, STATE, PROGRESS> {

	/**
	 * Save the entity.
	 * @param p_oContext context
	 * @param p_oInParameter NULL action IN param
	 * @return item
	 * @throws ActionException error
	 */
	public ITEM deleteData(MContext p_oContext, NullActionParameterImpl p_oInParameter) throws ActionException;

}
