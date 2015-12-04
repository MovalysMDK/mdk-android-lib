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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.application;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;


/**
 *
 * <p>Classes implementing this interface are executed at application startup.
 * Warning, unlike the back office it is necessary to add the classes manually to the application.
 * AbstractInitializerApplication.adInit(p_oClass)</p>
 * <p>Implementation of this classes must be a singleton.</p>
 *
 *
 */
// les classes implémentant cette interface doivent être un singleton
public interface RunInit {

	/**
	 * contains code to launch at startup
	 *
	 * @param p_oContext the context to use
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.application.RunInitError if any.
	 */
	public void run(MContext p_oContext) throws RunInitError;
}
