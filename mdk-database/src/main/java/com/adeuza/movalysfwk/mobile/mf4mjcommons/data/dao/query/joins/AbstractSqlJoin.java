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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.joins;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlKeywords;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlConditionList;

/**
 * <p>Représente une jointure SQL</p>
 *
 * <p>Un AbstractSqlJoin est composé des éléments suivants : </p>
 * <ul>
 * 	<li>Le type de jointure : retourné par la méthode abstraite getJoinType()</li>
 *  <li>L'ensemble de données sur lequel la jointure est faite : c'est à dire le nom de la table si c'est une jointure avec une
 *  table, ou le sql si c'est une jointure avec un résultat de requête sql. Cette information est renvoyée par la méthode abstraite getJoinWith()</li>
 *  <li>L'alias : alias que l'on donne à l'ensemble de données.</li>
 *  <li>Les conditions de jointure : la liste des {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition AbstractSqlCondition} représentant les conditions de jointure.</li>
 * </ul>
 *
 * @since 2.5
 */
public abstract class AbstractSqlJoin {
	
	/**
	 * Alias de la jointure
	 */
	private String alias ;
	
	/**
	 * Liste des conditions de jointures 
	 */
	protected SqlConditionList onConditions = new SqlConditionList();

	/**
	 * Retourne le type de jointure (inner join, outer join, etc...)
	 *
	 * @return type de jointure
	 */
	protected abstract String getJoinType();

	/**
	 * Retourne l'ensemble Sql sur lequel la jointure est faite (ex: une table, une sous-requête sql, etc...)
	 *
	 * @param p_oToSqlContext context sql
	 * @return sql
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException échec
	 */
	protected abstract String getJoinWith(ToSqlContext p_oToSqlContext) throws DaoException ;

	/**
	 * Ajoute une condition de jointure
	 *
	 * @param p_oSqlCondition condition de jointure
	 */
	public void addOnCondition( AbstractSqlCondition p_oSqlCondition ) {
		this.onConditions.addSqlCondition(p_oSqlCondition);
	}
	
	/**
	 * Génère le sql de la jointure
	 *
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException echec de construction du sql
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.joins.AbstractSqlJoin#toSql(QueryHistorisationBlocMap)
	 * @param p_oSql a {@link java.lang.StringBuilder} object.
	 * @param p_oToSqlContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext} object.
	 */
	public void toSql( StringBuilder p_oSql, ToSqlContext p_oToSqlContext) throws DaoException {
		p_oSql.append(' ');
		
		StringBuilder sConditionBuilder = new StringBuilder();
		sConditionBuilder.append(' ');
		if ( !onConditions.isEmpty()) {
			sConditionBuilder.append(' ');
			sConditionBuilder.append(SqlKeywords.ON);
			sConditionBuilder.append(' ');
			this.onConditions.toSql(p_oSql, p_oToSqlContext);
		}
		
		p_oSql.append( this.getJoinType()).append(' ');

		String sTableName = this.getJoinWith(p_oToSqlContext);
		p_oSql.append(sTableName).append(' ').append(this.getAlias());
		p_oSql.append(sConditionBuilder);
	}

	/**
	 * Bind les valeurs de la jointure
	 *
	 * @param p_oStatementBinder StatementBinder à utiliser
	 * @throws DaoException echec du binding des valeurs
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.joins.AbstractSqlJoin#bindValues(com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder)
	 */
	public void bindValues(StatementBinder p_oStatementBinder) throws DaoException {
		this.onConditions.bindValues(p_oStatementBinder);
	}
	
	/**
	 * Clone du SqlJoin
	 *
	 * @return SqlJoin
	 * @see java.lang.Object#clone()
	 */
	@Override
	public abstract AbstractSqlJoin clone();

	/**
	 * Retourne l'alias de la jointure
	 *
	 * @return alias de la jointure
	 */
	public String getAlias() {
		return this.alias;
	}

	/**
	 * Définit l'alias de la jointure
	 *
	 * @param p_sAlias nom de l'alias
	 */
	protected void setAlias(String p_sAlias) {
		this.alias = p_sAlias;
	}
}
