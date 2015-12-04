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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.action;

import android.Manifest;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.ActionTaskProgress;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;


/**
 * <p>Classe abstraite à utiliser obligatoirement pour avoir des tâches qui peuvent se lancer par ActionTask</p>
 */
public abstract class AbstractTaskableAction<IN extends ActionParameter, OUT extends ActionParameter, PROGRESS extends ActionStep, PROGRESSPARAMETER extends Object> 
implements Action<IN,OUT,PROGRESS,PROGRESSPARAMETER> {

	/** la tâche qui lance l'action */
	private MMActionTask<IN, OUT, PROGRESS, PROGRESSPARAMETER> handler = null;

	/**
	 * <p>hasPreExecuteDialog.</p>
	 *
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 * @param p_oParameterIn a IN object.
	 * @return a boolean.
	 */
	public boolean hasPreExecuteDialog(MContext p_oContext, IN p_oParameterIn) {
		return false;
	}

	/**
	 * Affecte le handler de l'action (Async task)
	 *
	 * @param p_oHandler le handler de la tâche
	 */
	public void setCurrentHandler(MMActionTask<IN, OUT, PROGRESS, PROGRESSPARAMETER> p_oHandler) {
		this.handler = p_oHandler;
	}
	
	/**
	 * Get handler
	 * @return handler
	 */
	public MMActionTask<IN, OUT, PROGRESS, PROGRESSPARAMETER> getHandler() {
		return this.handler;
	}
	
	/**
	 * Permet à l'action de publier un avancement en publiant sur le handler
	 * {@inheritDoc}
	 * @param p_oContext a com$adeuza$movalysfwk$mobile$mf4mjcommons$context$MContext object.
	 * @param p_oState a PROGRESS object.
	 * @param p_oProgressInformations a PROGRESSPARAMETER object.
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPublishProgress(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep, PROGRESS[])	 */
	@Override
	public void doPublishProgress(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext p_oContext, PROGRESS p_oState, PROGRESSPARAMETER...p_oProgressInformations) {
		ActionTaskProgress<PROGRESS, PROGRESSPARAMETER> atp = new ActionTaskProgress<PROGRESS, PROGRESSPARAMETER>();
		atp.setStep(p_oState);
		atp.setValue(p_oProgressInformations);
		this.handler.publishActionProgress(atp);
	}

	public String[] getRequieredPermissions() {
		return new String[] {Manifest.permission.WAKE_LOCK};
	}
}
