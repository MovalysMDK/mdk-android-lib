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
package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdelete;

import java.util.Arrays;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;

/**
 * Chain delete action detail parameter
 *
 */
public class ChainDeleteActionDetailParameter extends AbstractActionParameter implements ActionParameter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9019264544831171953L;

	/**
	 * 
	 */
	private List<Class<? extends DeleteDetailAction<?,?,?>>> deleteDetailActions ;

	/**
	 * Paramètre à fournir aux actions de sauvegarde. 
	 */
	private NullActionParameterImpl deleteDetailActionParameterIn;
	
	/**
	 * exit mode
	 */
	private boolean exitMode = false;

	/**
	 * constructor
	 * @param p_oAction delete detail action
	 */
	public ChainDeleteActionDetailParameter(Class<? extends DeleteDetailAction<?,?,?>>... p_oAction) {
		this.deleteDetailActions = Arrays.asList(p_oAction);
	}
	
	/**
	 * chain delete action detail parameter
	 * @param p_oParameterIn IN parameter
	 * @param p_oAction delete detail action
	 */
	public ChainDeleteActionDetailParameter(NullActionParameterImpl p_oParameterIn, Class<? extends DeleteDetailAction<?,?,?>>... p_oAction) {
		this(p_oAction);
		this.deleteDetailActionParameterIn = p_oParameterIn;
	}

	/**
	 * get delete detail actions
	 * @return list of class
	 */
	public List<Class<? extends DeleteDetailAction<?,?,?>>> getDeleteDetailActions() {
		return this.deleteDetailActions;
	}

	/**
	 * get delete detail actions parameter IN
	 * @return deleteDetailActionParameterIn
	 */
	public NullActionParameterImpl getDeleteDetailActionsParameterIn() {
		return this.deleteDetailActionParameterIn;
	}

	/**
	 * Set the exit mode
	 * @return true if this action is launched before to exit
	 */
	public boolean isExitMode() {
		return exitMode;
	}

	/**
	 * Returns the exit mode
	 * @param p_bExitMode true if the action is launched beafore exiting
	 */
	public void setExitMode(boolean p_bExitMode) {
		this.exitMode = p_bExitMode;
	}
}
