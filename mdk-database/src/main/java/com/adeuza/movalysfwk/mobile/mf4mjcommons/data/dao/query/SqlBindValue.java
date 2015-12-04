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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.SqlType;

/**
 * <p>Représente le bind d'une variable d'un PreparedStatement</p>
 *
 *
 */
public class SqlBindValue {

	/**
	 * Valeur pour le bind
	 */
	private Object value ;
	/**
	 * Type de la valeur 
	 */
	private @SqlType int sqlType ;

	/**
	 * Constructeur avec la valeur et le type
	 *
	 * @param p_oValue valeur du paramètre
	 * @param p_iSqlType type du paramètre
	 */
	public SqlBindValue(Object p_oValue, @SqlType int p_iSqlType) {
		this.value = p_oValue;
		this.sqlType = p_iSqlType;
	}

	/**
	 * Clone l'objet
	 *
	 * @return clone de l'instance
	 */
	public Object getValue() {
		return this.value ;
	}
	
	/**
	 * Affecte l'objet value
	 *
	 * @param p_oValue Objet value
	 */
	public void setValue(Object p_oValue) {
		this.value = p_oValue;
	}

	/**
	 * Retourne le type Sql du paramètre
	 *
	 * @return le type Sql de la valeur
	 */
	public @SqlType int getSqlType() {
		return sqlType;
	}
}
