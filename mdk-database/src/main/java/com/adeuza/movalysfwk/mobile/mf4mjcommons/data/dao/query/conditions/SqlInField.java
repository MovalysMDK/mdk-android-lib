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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.SqlType;

/**
 * <p>Représente un champs IN d'une SqlInCondition</p>
 *
 * <p>Il est définit par :</p>
 * <ul>
 * 	<li>Le nom du champs</li>
 *  <li>L'alias du champs</li>
 *  <li>Son {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.SqlType SqlType}</li>
 * </ul>
 *
 */
public class SqlInField implements Cloneable {

	/**
	 * Nom du champs
	 */
	private String fieldName ;
	/**
	 * Alias de la table du champs 
	 */
	private String alias ;
	/**
	 * Type du champs SQL 
	 */
	private SqlType sqlType ;
	
	/**
	 * Constructeur avec nom du champs, alias de la table du champs, type du champs
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_oSqlType type du champs
	 */
	public SqlInField( String p_sFieldName, String p_sAlias, SqlType p_oSqlType ) {
		this.fieldName = p_sFieldName ;
		this.alias = p_sAlias ;
		this.sqlType = p_oSqlType ;
	}
	
	/**
	 * Constructeur avec nom du champs, alias de la table du champs, type du champs
	 *
	 * @param p_oField champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_oSqlType type du champs
	 */
	public SqlInField( Field p_oField, String p_sAlias, SqlType p_oSqlType ) {
		this.fieldName = p_oField.name();
		this.alias = p_sAlias ;
		this.sqlType = p_oSqlType ;
	}
	
	/**
	 * Constructeur avec nom du champs, type du champs
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_oSqlType type du champs
	 */
	public SqlInField( String p_sFieldName, SqlType p_oSqlType ) {
		this.fieldName = p_sFieldName ;
		this.sqlType = p_oSqlType ;
	}

	/**
	 * Retourne le nom du champs
	 *
	 * @return nom du champs
	 */
	public String getFieldName() {
		return this.fieldName;
	}

	/**
	 * Retourne l'alias de la table du champs
	 *
	 * @return alias de la table du champs
	 */
	public String getAlias() {
		return this.alias;
	}

	/**
	 * Retourne le type SQL du champs
	 *
	 * @return type SQL du champs
	 */
	public SqlType getSqlType() {
		return this.sqlType;
	}
	
	/**
	 * Clone le SqlInField
	 *
	 * @return clone du SqlInField
	 * @see java.lang.Object#clone()
	 */
	@Override
	public SqlInField clone() {
		return new SqlInField( this.fieldName, this.alias, this.sqlType );
	}
}
