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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.support.annotation.StringDef;

/**
 *
 * <p>Enumération indiquant quelle type d'opération sera réalisé dans une requête exigeant une condition</p>
 *
 *
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({OperatorCondition.SUPERIOR, OperatorCondition.INFERIOR, OperatorCondition.EQUALS, OperatorCondition.SUPERIOR_OR_EQUALS, 
	OperatorCondition.INFERIOR_OR_EQUALS, OperatorCondition.DIFFERENT })
public @interface OperatorCondition {
	
	/**
	 * Superior.
	 */
	public static final String SUPERIOR = " > ";
	
	/**
	 * Inferior.
	 */
	public static final String INFERIOR = " < ";
	
	/**
	 * Equals.
	 */
	public static final String EQUALS = " = ";
	
	/**
	 * Superior or equals.
	 */
	public static final String SUPERIOR_OR_EQUALS = " >= ";
	
	/**
	 * Inferior or equals.
	 */
	public static final String INFERIOR_OR_EQUALS = " <= ";
	
	/**
	 * Different.
	 */
	public static final String DIFFERENT = " <> ";
}

