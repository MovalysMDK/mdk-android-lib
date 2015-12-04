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
package com.adeuza.movalysfwk.mobile.mf4android.utils;

import android.content.Context;

/**
 * Resource utility class
 * 
 */
public final class ResourceUtils {
	/**
	 * Get the string for the given resource name
	 */
	private ResourceUtils(){
		
	}
	/**
	 * Get resource by name
	 * @param p_sResourceName the name of the resource
	 * @param p_oContext the current context
	 * @return the string of the given resource
	 */
	public static String getStringResourceByName(String p_sResourceName, Context p_oContext ) {
		int iResId = p_oContext.getResources().getIdentifier(p_sResourceName, "string",
						p_oContext.getPackageName());
		return p_oContext.getString(iResId);
	}
}
