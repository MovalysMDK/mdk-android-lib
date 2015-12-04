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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.controller;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.MMActionTask;

/**
 *
 * <p>Définition des paramètres de lancement</p>
 *
 *
 */
public class LaunchParameter {
	
	/** Indique si le postexecute doit être exécutée */
	private boolean launchPostExecute = true;
	
	/** indique si l'action est lancée par une tâche */
	private boolean useActionTask = false;
	
	/** donne le lanceur de tâche */
	private MMActionTask<?, ?, ?, ?> task = null;
	
	/** indique si l'action est la première d'une chaîne d'action */
	private boolean firstAction = true;

	/**
	 * Retourne l'objet launchPostExecute
	 *
	 * @return Objet launchPostExecute
	 */
	public boolean isLaunchPostExecute() {
		return this.launchPostExecute;
	}

	/**
	 * Affecte l'objet launchPostExecute
	 *
	 * @param p_oLaunchPostExecute Objet launchPostExecute
	 */
	public void setLaunchPostExecute(boolean p_oLaunchPostExecute) {
		this.launchPostExecute = p_oLaunchPostExecute;
	}

	/**
	 * Retourne l'objet useActionTask
	 *
	 * @return Objet useActionTask
	 */
	public boolean isUseActionTask() {
		return this.useActionTask;
	}

	/**
	 * Affecte l'objet useActionTask
	 *
	 * @param p_oUseActionTask Objet useActionTask
	 */
	public void setUseActionTask(boolean p_oUseActionTask) {
		this.useActionTask = p_oUseActionTask;
	}

	/**
	 * Retourne l'objet task
	 *
	 * @return Objet task
	 */
	public MMActionTask<?, ?, ?, ?> getTask() {
		return this.task;
	}

	/**
	 * Affecte l'objet task
	 *
	 * @param p_oTask Objet task
	 */
	public void setTask(MMActionTask<?, ?, ?, ?> p_oTask) {
		this.task = p_oTask;
	}

	/**
	 * Retourne l'objet firstAction
	 *
	 * @return Objet firstAction
	 */
	public boolean isFirstAction() {
		return this.firstAction;
	}

	/**
	 * Affecte l'objet firstAction
	 *
	 * @param p_oFirstAction Objet firstAction
	 */
	public void setFirstAction(boolean p_oFirstAction) {
		this.firstAction = p_oFirstAction;
	}
	
	
}
