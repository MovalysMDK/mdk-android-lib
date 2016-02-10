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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.SqlType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlBindValue;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlClauseLink;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlField;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>Condition d'égalité avec une valeur</p>
 *
 * <p>Le SqlEqualsValueCondition est composé de</p>
 * <ul>
 *   <li>Du champs comparé</li>
 *   <li>De la valeur de comparaison</li>
 * </ul>
 *
 * <p>Pour le sql suivant :</p>
 * <pre>
 * {@code
 * WHERE intervention1.INTERVENTIONSTATEID = 5
 * }</pre>
 * <p>le code Java correspondant est :</p>
 * <pre>
 * {@code
 * oSqlQuery.addToWhere( new SqlEqualsValueCondition(
 *   InterventionDao.InterventionField.INTERVENTIONSTATEID, InterventionDao.ALIAS_NAME,
 * 	 5, SqlType.INTEGER
 * ));
 * }
 * </pre>
 * <p>Ou plus simplement :</p>
 * <pre>{@code
 * oSqlQuery.addEqualsConditionToWhere( InterventionDao.InterventionField.INTERVENTIONSTATEID,
 *   InterventionDao.ALIAS_NAME, 5, SqlType.INTEGER);
 * }</pre>
 * @see AbstractSqlCondition
 */
public class SqlEqualsValueCondition extends AbstractSqlCondition implements Cloneable {

	/**
	 * Champs de la condition d'égalité
	 */
	private SqlField field ; 
	
	/**
	 * Valeur de la condition d'égalité 
	 */
	private SqlBindValue value ;
	
	/**
	 * Constructeur avec champs
	 *
	 * @param p_oField champs pour l'égalité
	 */
	public SqlEqualsValueCondition( Field p_oField ) {
		this.field = new SqlField(p_oField);
	}
	
	/**
	 * Constructeur avec champs, valeur
	 *
	 * @param p_oField champs
	 * @param p_oValue Valeur bindée
	 */
	public SqlEqualsValueCondition( SqlField p_oField, SqlBindValue p_oValue ) {
		this.field = p_oField;
		this.value = p_oValue ;
	}
	
	/**
	 *Constructeur avec champs, alias, valeur
	 *
	 * @param p_oField le champs
	 * @param p_sAliasName l'alias de la table
	 * @param p_oSqlBindValue la valeur à binder
	 */
	public SqlEqualsValueCondition( Field p_oField, String p_sAliasName, SqlBindValue p_oSqlBindValue) {
		this.field = new SqlField(p_oField, p_sAliasName);
		this.value = p_oSqlBindValue ;
	}
	
	/**
	 * Constructeur avec champs, type de la valeur
	 *
	 * @param p_oField champs
	 * @param p_oSqlType type de la valeur
	 */
	public SqlEqualsValueCondition( SqlField p_oField, SqlType p_oSqlType ) {
		this.field = p_oField;
		this.value = new SqlBindValue(null, p_oSqlType) ;
	}
	
	/**
	 * Constructeur avec nom du champs, type de la valeur
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_oSqlType type de la valeur
	 */
	public SqlEqualsValueCondition( String p_sFieldName, SqlType p_oSqlType ) {
		this.field = new SqlField(p_sFieldName);
		this.value = new SqlBindValue(null, p_oSqlType) ;
	}
	
	/**
	 * Constructeur avec nom du champs, type de la valeur, type de liaison
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_oSqlType type SQL de la valeur
	 * @param p_oSqlClauseLink liaison avec l'ancienne clause
	 */
	public SqlEqualsValueCondition( String p_sFieldName, SqlType p_oSqlType, SqlClauseLink p_oSqlClauseLink ) {
		this.field = new SqlField(p_sFieldName);
		setLink(p_oSqlClauseLink);
		this.value = new SqlBindValue(null, p_oSqlType) ;
	}
	
	/**
	 * Constructeur avec nom du champs, valeur, type de la valeur
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_oValue valeur
	 * @param p_oSqlType type de la valeur
	 */
	public SqlEqualsValueCondition( String p_sFieldName, Object p_oValue, SqlType p_oSqlType ) {
		this.field = new SqlField(p_sFieldName);
		this.setLink(SqlClauseLink.AND) ;
		this.value = new SqlBindValue(p_oValue, p_oSqlType) ;
	}
	
	/**
	 * Constructeur avec nom du champs, valeur, type de la valeur et liaison avec l'ancienne clause
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_oValue valeur pour l'égalité
	 * @param p_oSqlType type SQL du champs
	 * @param p_oSqlClauseLink liaison avec l'ancienne clause
	 */
	public SqlEqualsValueCondition( String p_sFieldName, Object p_oValue, SqlType p_oSqlType, SqlClauseLink p_oSqlClauseLink ) {
		this.field = new SqlField(p_sFieldName);
		this.value = new SqlBindValue(p_oValue, p_oSqlType) ;
		setLink(p_oSqlClauseLink);
	}
	
	/**
	 * Constructeur avec nom du champs, alias du champs, valeur, type de la valeur
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_oSqlType type SQL du champs
	 * @param p_oValue valeur
	 */
	public SqlEqualsValueCondition( String p_sFieldName, String p_sAlias, Object p_oValue, SqlType p_oSqlType ) {
		this.field = new SqlField(p_sFieldName, p_sAlias);
		this.value = new SqlBindValue(p_oValue, p_oSqlType) ;
	}
	
	/**
	 * Constructeur avec nom du champs, alias de la table du champs, valeur, type de la valeur et liaison avec l'ancienne clause
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_oSqlType type SQL du champs
	 * @param p_oSqlClauseLink liaison avec l'ancienne clause
	 * @param p_oValue valeur
	 */
	public SqlEqualsValueCondition( String p_sFieldName, String p_sAlias, Object p_oValue, SqlType p_oSqlType, SqlClauseLink p_oSqlClauseLink ) {
		this.field = new SqlField(p_sFieldName, p_sAlias);
		setLink(p_oSqlClauseLink);
		this.value = new SqlBindValue(p_oValue, p_oSqlType) ;
	}
	
	/**
	 * Constructeur avec nom du champs, alias du champs, type de la valeur
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_oSqlType type SQL du champs
	 */
	public SqlEqualsValueCondition( String p_sFieldName, String p_sAlias, SqlType p_oSqlType ) {
		this.field = new SqlField(p_sFieldName, p_sAlias);
		this.value = new SqlBindValue(null, p_oSqlType) ;
	}
	
	/**
	 * Constructeur avec nom du champs, alias de la table du champs, type de la valeur et liaison avec l'ancienne clause
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_oSqlType type SQL du champs
	 * @param p_oSqlClauseLink liaison avec l'ancienne clause
	 */
	public SqlEqualsValueCondition( String p_sFieldName, String p_sAlias, SqlType p_oSqlType, SqlClauseLink p_oSqlClauseLink ) {
		this.field = new SqlField(p_sFieldName, p_sAlias);
		setLink(p_oSqlClauseLink);
		this.value = new SqlBindValue(null, p_oSqlType) ;
	}
	
	/**
	 * Constructeur avec nom du champs, alias de la table du champs, type de la valeur
	 *
	 * @param p_oField champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_oSqlType type SQL du champs
	 */
	public SqlEqualsValueCondition( Field p_oField, String p_sAlias, SqlType p_oSqlType ) {
		this.field = new SqlField(p_oField, p_sAlias);
		this.value = new SqlBindValue(null, p_oSqlType) ;
	}
	
	/**
	 * Constructeur avec champs, alias de la table du champs, type de la valeur et liaison avec l'ancienne clause
	 *
	 * @param p_oField champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_oSqlType type SQL du champs
	 * @param p_oSqlClauseLink liaison avec l'ancienne clause
	 */
	public SqlEqualsValueCondition( Field p_oField, String p_sAlias, SqlType p_oSqlType, SqlClauseLink p_oSqlClauseLink ) {
		this.field = new SqlField(p_oField, p_sAlias);
		setLink(p_oSqlClauseLink);
		this.value = new SqlBindValue(null, p_oSqlType) ;
	}
	
	/**
	 * Constructeur avec champs, alias de la table du champs, valeur pour l'égalité et type de la valeur
	 *
	 * @param p_oField champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_oValue valeur pour l'égalité
	 * @param p_oSqlType type SQL du champs
	 */
	public SqlEqualsValueCondition( Field p_oField, String p_sAlias, Object p_oValue, SqlType p_oSqlType ) {
		this.field = new SqlField(p_oField, p_sAlias);
		this.value = new SqlBindValue(p_oValue, p_oSqlType) ;
	}
	
	/**
	 * Constructeur avec champs, valeur pour l'égalité et type de la valeur
	 *
	 * @param p_oField champs
	 * @param p_oValue valeur pour l'égalité
	 * @param p_oSqlType type SQL du champs
	 */
	public SqlEqualsValueCondition( Field p_oField, Object p_oValue, SqlType p_oSqlType ) {
		this.field = new SqlField(p_oField);
		this.value = new SqlBindValue(p_oValue, p_oSqlType) ;
	}
	
	/**
	 * Constructeur avec champs, alias de la table du champs, valeur pour l'égalité, type de la valeur et liaison avec l'ancienne clause
	 *
	 * @param p_oField champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_oValue valeur pour l'égalité
	 * @param p_oSqlType type SQL du champs
	 * @param p_oSqlClauseLink liaison avec l'ancienne clause
	 */
	public SqlEqualsValueCondition( Field p_oField, String p_sAlias, Object p_oValue, SqlType p_oSqlType, SqlClauseLink p_oSqlClauseLink) {
		this.field = new SqlField(p_oField, p_sAlias);
		setLink(p_oSqlClauseLink);
		this.value = new SqlBindValue(p_oValue, p_oSqlType) ;
	}
	
	/**
	 * Constructeur avec champs, valeur bindée et liaison avec l'ancienne clause
	 *
	 * @param p_oField Field
	 * @param p_oValue Valeur bindée pour l'égalité
	 * @param p_oSqlClauseLink liaison avec l'ancienne clause
	 */
	public SqlEqualsValueCondition( SqlField p_oField, SqlBindValue p_oValue, SqlClauseLink p_oSqlClauseLink ) {
		this.field = p_oField;
		setLink(p_oSqlClauseLink);
		this.value = p_oValue ;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Bind les valeurs de la condition d'égalité
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition#bindValues(com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder)
	 */
	@Override
	public void bindValues(StatementBinder p_oStatementBinder) throws SQLException {
		if ( this.value != null ) {
			p_oStatementBinder.bind(this.value.getValue(), this.value.getSqlType());
		}
	}

	/**
	 * Définit la valeur pour l'égalité
	 *
	 * @param p_oValue Objet value
	 */
	public void setValue(Object p_oValue) {
		this.value.setValue(p_oValue);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Clone la condition d'égalité
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition#clone()
	 */
	@Override
	public AbstractSqlCondition clone() {
		return new SqlEqualsValueCondition(this.field, this.value, getLink());
	}

	/**
	 * {@inheritDoc}
	 *
	 * Génère le SQL de la requête
	 */
	@Override
	public void toSql(StringBuilder p_oSql, ToSqlContext p_oToSqlContext) {
		// Equals forcémment sur la current locale pour les champs i18n
		this.field.toSql(p_oSql, p_oToSqlContext);
		p_oSql.append(" = ?");
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void computeContentResolverSelection(StringBuilder p_oSql) {
		p_oSql.append(this.field.getName());
		p_oSql.append(" = ?");
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void computeContentResolverSelectionArgs(SelectionArgsBuilder p_oSqlArgsBuilder) {
		if ( this.value != null ) {
			p_oSqlArgsBuilder.addValue(this.value.getValue(), this.value.getSqlType());
		}
	}
}
