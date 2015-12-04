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
package com.adeuza.movalysfwk.mobile.mf4android.actiontask;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskIn;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask;

/**
 * <p>Interface of Android Action Task</p>
 *
 *
 * @param <IN> IN action parameter
 * @param <OUT> OUT action parameter
 * @param <STATE> action step
 * @param <PROGRESS> action progress
 *
 */
public interface MMAndroidActionTask<IN extends ActionParameter, OUT extends ActionParameter,STATE extends ActionStep, PROGRESS extends Object>
		extends MMActionTask<IN, OUT, STATE, PROGRESS> {

	/**
	 * Execute Action
	 * @param p_oParameterIn action task input
	 */
	public void execAction(ActionTaskIn<IN> p_oParameterIn);
}
