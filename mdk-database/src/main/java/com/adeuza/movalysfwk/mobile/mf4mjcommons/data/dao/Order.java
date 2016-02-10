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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>Représente un tri sur une colonne</p>
 *
 *
 */
public interface Order {

	/**
	 * Renvoie le tri au format SQL
	 *
	 * @param p_oSql a {@link java.lang.StringBuilder} object.
	 * @param p_oToSqlContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext} object.
	 */
	public void appendToSQL( StringBuilder p_oSql, ToSqlContext p_oToSqlContext );
	
	/**
	 * Clone l'Order
	 *
	 * @return l'order
	 */
	public Order clone();

	/**
	 * Compute order by for ContentResolver
	 * @param p_oSql result is added to this StringBuilder
	 */
	public void computeContentResolverOrderBy( StringBuilder p_oSql );
}
