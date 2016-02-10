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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select;

import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>Représente une partie d'un Select</p>
 */
public abstract class AbstractSqlSelectPart {

	/**
	 * Génère le SQL de la partie du Select
	 *
	 * @param p_oStringBuilder génère le sql dans le string builder
	 * @param p_oToSqlContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext} object.
	 */
	public abstract void toSql( StringBuilder p_oStringBuilder, ToSqlContext p_oToSqlContext );

	/**
	 * Clone la partie du Select
	 *
	 * @return le clone de la partie du Select
	 * @see java.lang.Object#clone()
	 */
	@Override
	public abstract AbstractSqlSelectPart clone();

	/**
	 * Compute projection for ContentResolver
	 * @param p_listColumns list containing columns 
	 */
	public abstract void computeContentResolverProjection(List<String> p_listColumns);
}
