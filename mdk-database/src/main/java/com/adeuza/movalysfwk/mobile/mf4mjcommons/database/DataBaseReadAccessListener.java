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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.database;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>A listener interested in action writing in database</p>
 *
 *
 */
public interface DataBaseReadAccessListener {

	/** name of method doOnControllerStartActionWithReadInDataBase */
	public static final String M_DO_ON_CONTROLLER_START_ACTION_WITH_READ_IN_DATABASE = "doOnControllerStartActionWithReadInDataBase";
	
	/** name of method doOnControllerStopActionWithReadInDataBase */
	public static final String M_DO_ON_CONTROLLER_STOP_ACTION_WITH_READ_IN_DATABASE = "doOnControllerStopActionWithReadInDataBase";
	
	/**
	 * This method is called by controller when it launched an action whith writing access to database
	 *
	 * @param p_oContext context to use
	 */
	public void doOnControllerStartActionWithReadInDataBase(MContext p_oContext);
	
	/**
	 * This method is called by controller when it stoped an action whith writing access to database
	 *
	 * @param p_oContext context to use
	 */
	public void doOnControllerStopActionWithReadInDataBase(MContext p_oContext);
}
