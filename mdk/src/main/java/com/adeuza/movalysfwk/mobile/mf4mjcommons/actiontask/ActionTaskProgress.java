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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep;

/**
 * <p>Value object permettant de définir la progression d'une ActionTask</p>
 *
 *
 */
public class ActionTaskProgress<STATE extends ActionStep, PROGRESS extends Object> {
	
	/** l'état d'avancement de la tâche */
	private STATE step = null;
	/** les informations relatives à l'avancement de la tâche */
	private PROGRESS[] value = null;
	/**
	 * Retourne l'objet step
	 *
	 * @return Objet step
	 */
	public STATE getStep() {
		return this.step;
	}
	/**
	 * Affecte l'objet step
	 *
	 * @param p_oStep Objet step
	 */
	public void setStep(STATE p_oStep) {
		this.step = p_oStep;
	}
	/**
	 * Retourne l'objet value
	 *
	 * @return Objet value
	 */
	public PROGRESS[] getValue() {
		return this.value;
	}
	/**
	 * Affecte l'objet value
	 *
	 * @param p_oValue Objet value
	 */
	public void setValue(PROGRESS[] p_oValue) {
		this.value = p_oValue;
	}
}
