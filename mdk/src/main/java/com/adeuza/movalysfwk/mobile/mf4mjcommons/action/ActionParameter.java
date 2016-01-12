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
 * <p>Interface for all action parameters</p>
 */
public interface ActionParameter extends Serializable {
	
	/**
	 * Returns the unique number of laucnhed action
	 *
	 * @return a unique id
	 */
	public long getActionId();
	
	/**
	 * Sets the unique id
	 *
	 * @param p_lId the unique id
	 */
	public void setActionId(long p_lId);

	/**
	 * Returns the Action Attached Activity (True/False)
	 *
	 * @return isActionAttachedActivity
	 */
	public boolean isActionAttachedActivity();
	
	/**
	 * Sets the Action Attached Activity (True/False)
	 *
	 * @param p_bActive
	 */
	public void setActionAttachedActivity(boolean p_bActive);
	
	
	/**
	 * Returns the parameters usable by rules.
	 *
	 * @return the parameters usable by rules.
	 */
	public Map<String, Object> getRuleParameters();

	/**
	 * Defines the parameters usable by rules.
	 *
	 * @param p_mapParameters the parameters usable by rules.
	 */
	public void setRuleParameters(final Map<String, Object> p_mapParameters);
	
	/**
	 * Return true if progress dialog is disabled
	 * @return true if progress dialog is disabled
	 */
	public boolean isProgressDialogDisabled();
	
	/**
	 * Disable progress dialog
	 */
	public void disableProgressDialog();
}
