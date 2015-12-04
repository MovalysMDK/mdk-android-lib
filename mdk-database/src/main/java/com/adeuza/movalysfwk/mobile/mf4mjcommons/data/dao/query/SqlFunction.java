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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.support.annotation.StringDef;

/**
 * <p>Fonction SQL</p>
 *
 *
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({SqlFunction.MIN, SqlFunction.MAX, SqlFunction.SUM})
public @interface SqlFunction {
	
	/**
	 * Min.
	 */
	public static final String MIN = "MIN";
	
	/**
	 * Max.
	 */
	public static final String MAX = "MAX";
	
	/**
	 * Sum.
	 */
	public static final String SUM = "SUM";
}
