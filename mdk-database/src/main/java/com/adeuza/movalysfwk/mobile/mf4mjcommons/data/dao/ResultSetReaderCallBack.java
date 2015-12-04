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

import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteResultSet;

/**
 * <p>Callback pour la lecture d'un resultset.</p>
 * <p>Cette classe est utilisée dans le cadre du genericSelect de la classe AbstractDao ainsi
 * que dans le cadre du genericInsert pour lire les valeurs générées</p>
 */
public interface ResultSetReaderCallBack {

	/**
	 * Cette methode traite la lecture du ResultSet.
	 * Elle ne doit pas le fermer.
	 *
	 * @param p_oResultSet Resultset à lire
	 * @return a {@link java.lang.Object} object.
	 * @throws DaoException déclenchée si la lecture du resultset déclence une exception SQL
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si la lecture du resultset déclenche une exception technique
	 */
	public Object doRead( AndroidSQLiteResultSet p_oResultSet ) throws DaoException ; 
}
