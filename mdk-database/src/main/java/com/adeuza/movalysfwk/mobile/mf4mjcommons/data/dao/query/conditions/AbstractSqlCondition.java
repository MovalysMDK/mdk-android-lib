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

import java.sql.SQLException;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.contentprovider.SelectionArgsBuilder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlClauseLink;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>Condition SQL</p>
 *
 * <p>AbstractSqlCondition est composée de : </p>
 * <ul>
 * 	<li>de la condition elle-même</li>
 *  <li>du lien avec la précédente : {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlClauseLink SqlClauseLink}</li>
 * </ul>
 *
 * <p>Les implémentations disponibles sont les suivantes :</p>
 * <ul>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlBetweenCondition SqlBetweenCondition} : la valeur du champs est comprise entre deux valeurs.</li>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlCompareFieldCondition SqlCompareFieldCondition} : comparaison entre deux champs</li>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlCompareValueCondition SqlCompareValueCondition} : comparaison avec une valeur</li>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlEqualsFieldCondition SqlEqualsFieldCondition} : égalité entre deux champs</li>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlEqualsValueCondition SqlEqualsValueCondition} : égalité avec une valeur</li>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlGenericCondition SqlGenericCondition} : sql générique</li>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlGroupCondition SqlGroupCondition} : pour grouper des conditions</li>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlInSelectCondition SqlInSelectCondition} : la valeur du champs est comprise dans le résultat d'une sous-requête</li>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlInValueCondition SqlInValueCondition} : la valeur du champs est comprise dans une liste de valeurs données</li>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlLikeCondition SqlLikeCondition} : comparaison avec le mot clé LIKE</li>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlNotInValueCondition SqlNotInValueCondition} : la valeur du champs n'est pas comprise dans la liste de valeurs données</li>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlNullCondition SqlNullCondition} : la valeur du champs est nulle.</li>
 * </ul>
 */
public abstract class AbstractSqlCondition {

	/**
	 * Lien avec la clause précédente, par défaut: AND 
	 */
	private SqlClauseLink link = SqlClauseLink.AND ;
	
	/**
	 * Retourne le type de liaison
	 *
	 * @return le type de liaison
	 */
	public SqlClauseLink getLink() {
		return this.link;
	}
	
	/**
	 * DÃ©finit le type de liaison avec la condition précédente
	 *
	 * @param p_oSqlClauseLink définit la liaison avec la condition précédente
	 */
	protected void setLink(SqlClauseLink p_oSqlClauseLink) {
		this.link = p_oSqlClauseLink;
	}

	/**
	 * Génère le SQL de la condition
	 *
	 * @param p_oStringBuilder a {@link java.lang.StringBuilder} object.
	 * @param p_oToSqlContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException echec de calcul du nom de la vue
	 */
	public abstract void toSql( StringBuilder p_oStringBuilder, ToSqlContext p_oToSqlContext ) throws DaoException;

	/**
	 * Compute selection of ContentResolver and append result to p_oSelectBuilder
	 * @param p_oSql result is appended to this StringBuilder
	 */
	public abstract void computeContentResolverSelection(StringBuilder p_oSql);
	
	/**
	 * Bind les valeurs de la condition
	 *
	 * @param p_oStatementBinder StatementBinder à utiliser pour le binding
	 * @throws java.sql.SQLException échec du binding
	 */
	public abstract void bindValues(StatementBinder p_oStatementBinder) throws SQLException;
	
	/**
	 * Compute selection args for ContentResolver
	 * @param p_oSqlArgsBuilder selection args builder
	 */
	public abstract void computeContentResolverSelectionArgs(SelectionArgsBuilder p_oSqlArgsBuilder);
	
	/**
	 * Clone la condition SQL
	 *
	 * @see java.lang.Object#clone()
	 * @return clone de la condition
	 */
	@Override
	public abstract AbstractSqlCondition clone();
}
