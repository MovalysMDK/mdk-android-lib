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
package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractAutoBindMMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;

/**
 * <p>Implémentation générique d'une action d'affichage de dialog.</p>
 *
 *
 * @since MF-Annapurna
 */
public abstract class GenericDisplayDialogImpl {

	/** notifier*/
	private Notifier notifier = null;

	/**
	 * Construit une nouvelle action de type <em>GenericDisplayDialogImpl</em>.
	 */
	public GenericDisplayDialogImpl() {
		this.notifier = new Notifier();
	}

	/**
	 * Méthode abstraite regroupant les chargements de données en base liée à l'action courante.
	 * @param p_oContext context applicatif pour établir la connection à la base
	 * @param p_oParameterIn paramétrage suplémentaire
	 * @return l'identifiant de l'entité chargée en base sous la forme d'une chaîne de caractère
	 */
	protected abstract String loadData(MContext p_oContext, NullActionParameterImpl p_oParameterIn);
	
	/**
	 * Get the dialog class
	 * @return bind MM dialog fragment
	 */
	public abstract Class<? extends AbstractAutoBindMMDialogFragment> getDialogClass();
		
	/**
	 * GETTER
	 * @return NULL action parameter
	 */
	public NullActionParameterImpl getEmptyInParameter() {
		return new NullActionParameterImpl();
	}
	
	/**
	 * Do action
	 * @param p_oContext context
	 * @param p_oParameterIn IN parameter
	 * @return Null
	 */
	public NullActionParameterImpl doAction(MContext p_oContext, NullActionParameterImpl p_oParameterIn) {
		this.loadData(p_oContext, p_oParameterIn);
		return null;
	}
	
	/**
	 * publish progress
	 * @param p_oContext context
	 * @param p_oState default action step
	 * @param p_oProgressInformations progress informations
	 */
	public void doPublishProgress(MContext p_oContext, DefaultActionStep p_oState, Void...p_oProgressInformations) {
		//Nothing to do;
	}
	
	/**
	 * pre-execute
	 * @param p_oContext context
	 * @throws ActionException error
	 */
	public void doPreExecute(MContext p_oContext) throws ActionException {
		//Nothing to do;
	}
	
	/**
	 * post-execute
	 * @param p_oContext context
	 * @param p_oParameterIn IN parameter
	 * @param p_oParameterOut OUT parameter
	 * @throws ActionException error
	 */
	public void doPostExecute(MContext p_oContext, NullActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oParameterOut) throws ActionException {
		//Nothing to do;
	}
	
	/**
	 * on success
	 * @param p_oContext context
	 * @param p_oParameterIn IN parameter
	 * @param p_oResultOut null action OUT parameter
	 */
	public void doOnSuccess(MContext p_oContext, NullActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		//Nothing to do
	}

	/**
	 * on error
	 * @param p_oContext context
	 * @param p_oParameterIn null action IN parameter
	 * @param p_oResultOut null action OUT parameter
	 * @return null action returned parameter
	 */
	public NullActionParameterImpl doOnError(MContext p_oContext, NullActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		//Nothing to do;
		return p_oResultOut;
	}
	
	/**
	 * notify
	 */
	public void doNotify() {
		// nothing to notify
	}

	/**
	 * concurrent action
	 * @return true if action is concurrent
	 */
	public int getConcurrentAction() {
		return Action.DEFAULT_QUEUE;
	}

	/**
	 * GETTER
	 * @return notifier
	 */
	public Notifier getNotifier() {
		return this.notifier;
	}
}
