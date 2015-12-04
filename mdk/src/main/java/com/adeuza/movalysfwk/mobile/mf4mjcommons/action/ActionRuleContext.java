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

import java.util.HashMap;
import java.util.Map;

/**
 * <p>ActionRuleContext</p>
 *
 * <p>Contexte pour les règles de gestion centralisées sur les actions.</p>
 *
 *
 */
public class ActionRuleContext {
	
	/** DO_NOTHING_STATE_VALUE */
	public static final int DO_NOTHING_STATE_VALUE = 0;
	
	/** CONTINUE_CONTROLLER_STATE_VALUE */
	public static final int CONTINUE_CONTROLLER_STATE_VALUE = 1;
	
	/** INTERRUPT_CONTROLLER_STATE_VALUE */
	public static final int INTERRUPT_CONTROLLER_STATE_VALUE = 2;
	
	/**
	 * Etat résultat de la vérification de la règle
	 */
	private int state;

	/**
	 * Map de paramètres
	 */
	private Map<String, Object> parameters;
	

	/**
	 * Constructors
	 */
	public ActionRuleContext() {
		this.state = ActionRuleContext.DO_NOTHING_STATE_VALUE;
		this.parameters = new HashMap<String, Object>();
	}

	/**
	 * <p>Constructor for ActionRuleContext.</p>
	 *
	 * @param p_oState Etat résultat de la vérification de la règle
	 * @param p_oParameters Map de paramètres
	 */
	public ActionRuleContext(int p_oState, Map<String, Object> p_oParameters) {
		this.state = p_oState;
		this.parameters = p_oParameters;
	}

	/**
	 * Retourne l'objet state
	 *
	 * @return Objet state
	 */
	public int getState() {
		return this.state;
	}

	/**
	 * Affecte l'objet state
	 *
	 * @param p_oState Objet state
	 */
	public void setState(int p_oState) {
		this.state = p_oState;
	}

	/**
	 * Retourne l'objet parameters
	 *
	 * @return Objet parameters
	 */
	public Map<String, Object> getParameters() {
		return this.parameters;
	}

	/**
	 * Affecte l'objet parameters
	 *
	 * @param p_oParameters Objet parameters
	 */
	public void setParameters(Map<String, Object> p_oParameters) {
		this.parameters = p_oParameters;
	}

}
