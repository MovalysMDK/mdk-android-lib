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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>ActionRule</p>
 *
 * <p>Interface des règles de gestion centralisées sur les actions.</p>
 *
 *
 */
public interface ActionRule {

	/**
	 * <p>Vérifie une règle de gestion.</p>
	 * <p>Cette méthode met à jour l'état du contexte suivant le résultat de la vérification.</p>
	 *
	 * @param p_oContext Contexte
	 * @param p_oRuleContext Contexte de la règle
	 */
	public void compute(MContext p_oContext, ActionRuleContext p_oRuleContext);
	
	/**
	 * <p>Permet d'envoyer une notification.</p>
	 *
	 * @param p_oContext Contexte
	 * @param p_oRuleContext Contexte de la règle
	 */
	public void notifyUser(MContext p_oContext, ActionRuleContext p_oRuleContext);
	
	/**
	 * <p>Traitement invoqué pour l'état <code>ActionRuleContext.DO_NOTHING_STATE_VALUE</code></p>
	 *
	 * @param p_oContext Contexte
	 * @param p_oRuleContext Contexte de la règle
	 */
	public void doOnDoNothingState(MContext p_oContext, ActionRuleContext p_oRuleContext);
	
	/**
	 * <p>Traitement invoqué pour l'état <code>ActionRuleContext.CONTINUE_CONTROLLER_STATE_VALUE</code></p>
	 *
	 * @param p_oContext Contexte
	 * @param p_oRuleContext Contexte de la règle
	 */
	public void doOnContinueControllerState(MContext p_oContext, ActionRuleContext p_oRuleContext);
	
	/**
	 * <p>Traitement invoqué pour l'état <code>ActionRuleContext.INTERRUPT_CONTROLLER_STATE_VALUE</code></p>
	 *
	 * @param p_oContext Contexte
	 * @param p_oRuleContext Contexte de la règle
	 */
	public void doOnInterruptControllerState(MContext p_oContext, ActionRuleContext p_oRuleContext);
}
