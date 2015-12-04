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
package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericsave;

import java.util.Arrays;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;

/**
 * chain save action detail parameter
 *
 */
public class ChainSaveActionDetailParameter extends AbstractActionParameter implements ActionParameter {

	/**
	 * serializable
	 */
	private static final long serialVersionUID = 9019264544831171952L;

	/**
	 * save detail actions
	 */
	private List<Class<? extends SaveDetailAction<?,?,?>>> saveDetailActions ;

	/**
	 * Paramètre à fournir aux actions de sauvegarde. 
	 */
	private NullActionParameterImpl saveDetailActionParameterIn;
	
	/**
	 * exit mode
	 */
	private boolean exitMode = false;

	/**
	 * constructor
	 * @param p_oAction action
	 */
	public ChainSaveActionDetailParameter(Class<? extends SaveDetailAction<?,?,?>>... p_oAction) {
		this.saveDetailActions = Arrays.asList(p_oAction);
	}
	
	/**
	 * constructor
	 * @param p_oParameterIn IN parameter
	 * @param p_oAction save detail action
	 */
	public ChainSaveActionDetailParameter(NullActionParameterImpl p_oParameterIn, Class<? extends SaveDetailAction<?,?,?>>... p_oAction) {
		this(p_oAction);
		this.saveDetailActionParameterIn = p_oParameterIn;
	}

	/**
	 * getter
	 * @return save detail actions
	 */
	public List<Class<? extends SaveDetailAction<?,?,?>>> getSaveDetailActions() {
		return this.saveDetailActions;
	}

	/**
	 * GETTER
	 * @return save detail action IN parameters
	 */
	public NullActionParameterImpl getSaveDetailActionsParameterIn() {
		return this.saveDetailActionParameterIn;
	}

	/**
	 * GET the exit mode
	 * @return true if this action is launched before to exit
	 */
	public boolean isExitMode() {
		return this.exitMode;
	}

	/**
	 * SET the exit mode
	 * @param p_bExitMode true if the action is launched beafore exiting
	 */
	public void setExitMode(boolean p_bExitMode) {
		this.exitMode = p_bExitMode;
	}
}
