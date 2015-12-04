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

import java.io.Serializable;
import java.util.Map;

/**
 * <p>Abstract implementation for all action parameters</p>
 *
 *
 */
public abstract class AbstractActionParameter implements Serializable {
	
	/** serial id */
	private static final long serialVersionUID = -5914696215601864000L;

	/** unique number of launched action */
	private long actionId = 0;

	/** unique number of launched action */
	private boolean actionAttachedActivity = false;
	
	private Map<String, Object> parameters;
	
	/**
	 * Disable progress dialog
	 */
	private boolean progressDialogDisabled = false ;
	
	/**
	 * Returns the unique number of laucnhed action
	 *
	 * @return a unique id
	 */
	public long getActionId() {
		return this.actionId;
	}
	
	/**
	 * Sets the unique id
	 *
	 * @param p_lId the unique id
	 */
	public void setActionId(long p_lId) {
		this.actionId = p_lId;
	}

	/**
	 * Returns the parameters usable by rules.
	 *
	 * @return the parameters usable by rules.
	 */
	public Map<String, Object> getRuleParameters() {
		return this.parameters;
	}

	/**
	 * Returns the Action Attached Activity (True/False)
	 *
	 * @return isActionAttachedActivity
	 */
	public boolean isActionAttachedActivity() {
		return this.actionAttachedActivity;
	}
	
	/**
	 * Sets the Action Attached Activity (True/False)
	 *
	 * @param p_bActionAttachedActivity
	 */
	public void setActionAttachedActivity(boolean p_bActive) {
		this.actionAttachedActivity = p_bActive;
	}
	
	/**
	 * Defines the parameters usable by rules.
	 *
	 * @param p_mapParameters the parameters usable by rules.
	 */
	public void setRuleParameters(final Map<String, Object> p_mapParameters) {
		this.parameters = p_mapParameters;
	}
	
	/**
	 * Return true if progress dialog is disabled
	 * @return true if progress dialog is disabled
	 */
	public boolean isProgressDialogDisabled() {
		return this.progressDialogDisabled;
	}
	
	/**
	 * Disable progress dialog
	 */
	public void disableProgressDialog() {
		this.progressDialogDisabled = true ;
	}
}
