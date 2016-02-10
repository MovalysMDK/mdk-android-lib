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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.insert;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Représente un champs d'une requête d'insertion</p>
 *
 * <p>Les implémentations disponibles sont :</p>
 * <ul>
 *  <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.insert.SqlInsertAutoIncrementField SqlInsertAutoIncrementField} : pour une colonne dont la valeur est générée (auto-incrément ou séquence)</li>
 *  <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.insert.SqlInsertBindedField SqlInsertBindedField} : pour une colonne avec valeur bindée</li>
 *  <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.insert.SqlInsertNullField SqlInsertNullField} : pour une colonne avec une valeur nulle</li>
 * </ul>
 *
 * @since 2.5
 */
public abstract class AbstractSqlInsertField {

	/**
	 * Nom duchamps
	 */
	private String fieldName ;
	
	/**
	 * Clone le SqlInsertField
	 *
	 * @return clone du SqlInsertField
	 * @see java.lang.Object#clone()
	 */
	@Override
	public abstract AbstractSqlInsertField clone();
	
	/**
	 * Génère le sql de la valeur du champs inséré
	 *
	 * @param p_oSql a {@link java.lang.StringBuilder} object.
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.ItfTransactionalContext} object.
	 */
	public abstract void appendToValueClause(StringBuilder p_oSql, MContext p_oContext );

	/**
	 * Retourne l'objet fieldName
	 *
	 * @return Objet fieldName
	 */
	public String getFieldName() {
		return this.fieldName;
	}

	/**
	 * TODO Décrire la méthode toSql de la classe AbstractSqlInsertField
	 *
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.ItfTransactionalContext} object.
	 * @param p_oSql a {@link java.lang.StringBuilder} object.
	 */
	public void appendToColumnClause( StringBuilder p_oSql, MContext p_oContext ) {
		p_oSql.append(this.fieldName);
	}
	
	/**
	 * Définit le nom du champs
	 *
	 * @param p_sFieldName nom du champs
	 */
	protected final void setFieldName(String p_sFieldName) {
		this.fieldName = p_sFieldName;
	}
}
