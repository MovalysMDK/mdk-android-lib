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
package com.adeuza.movalysfwk.mobile.mf4javacommons.context;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContextImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.LaunchParameter;

/**
 * <p>Context étdendu</p>
 *
 *
 */
public class MExtendedContextImpl extends MContextImpl {

	/** information sur le lancement de l'action */
	private LaunchParameter launchParameter = null;
	
	/**
	 * affecte la configuration de lancement de l'action
	 *
	 * @param p_oParameter le paramètre de lancement
	 */
	public void setLaunchParameter(LaunchParameter p_oParameter) {
		this.launchParameter = p_oParameter;
	}
	
	/**
	 * Indique si l'action est lancée par une tâche
	 *
	 * @return true si l'action est lancée par une tâche
	 */
	public boolean isLaunchedByActionTask() {
		
		if (this.launchParameter == null) {
			return false;
		}
		return this.launchParameter.isUseActionTask();
	}
	
	/**
	 * Donne les paramètres de lancement
	 *
	 * @return les paramètres de lancement
	 */
	public LaunchParameter getLaunchParameter() {
		return this.launchParameter;
	}
}
