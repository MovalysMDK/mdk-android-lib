package com.adeuza.movalysfwk.mobile.mf4android.actiontask;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskIn;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask;

/**
 * <p>Interface of Android Action Task</p>
 *
 * <p>Copyright (c) 2014
 *
 * @author Sopra, pole mobilite (Nantes)
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
