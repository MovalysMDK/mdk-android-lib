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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlKeywords;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>Condition pour faire un like</p>
 *
 * <p>SqlLikeCondition est composé :</p>
 * <ul>
 * 	 <li>Du champs</li>
 *   <li>De la valeur de comparaison</li>
 * </ul>
 *
 * <p>Pour le sql suivant :</p>
 * <pre>{@code
 * WHERE intervention1.CODE LIKE 'CODE01%'
 * }</pre>
 *
 * <p>Le code Java correspondant est le suivant :</p>
 * <pre>{@code
 * oSqlQuery.addToWhere(
 *   new SqlLikeCondition(
 *     new SqlField(InterventionField.CODE, InterventionDao.ALIAS_NAME),
 *     new SqlBindValue("CODE01%", SqlType.VARCHAR)));
 * }</pre>
 *
 * <p>Ou plus simplement :</p>
 * <pre>
 * {@code
 * oSqlQuery.addLikeConditionToWhere(InterventionField.CODE,
 *   InterventionDao.ALIAS_NAME, "CODE01%", SqlType.VARCHAR);
 * }</pre>
 * @see AbstractSqlCondition
 */
public class SqlLikeCondition extends AbstractSqlCondition implements Cloneable {

	/**
	 * Field du Like
	 */
	private SqlField field ; 
	
	/**
	 * Value bindée du Like 
	 */
	private SqlBindValue value ;

	/**
	 * Convertit les jokers '*' en jokers sql '%' ou ajoute ces derniers s'ils
	 * ne sont pas présents
	 *
	 * @param p_sValue a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected static String wildcards(String p_sValue) {
		if (p_sValue.indexOf('*') >= 0) {
			return p_sValue.replaceAll("\\*", "%");
		}

		// Par defaut, on fait un filtre sur '___%' et non plus '%___%'
		return new StringBuilder().append(p_sValue).append('%').toString();
	}

	/**
	 * Constructeur avec nom du champs
	 *
	 * @param p_oField champs
	 */
	public SqlLikeCondition( Field p_oField ) {
		this.field = new SqlField(p_oField);
	}

	/**
	 * <p>Constructor for SqlLikeCondition.</p>
	 *
	 * @param p_oField a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field} object.
	 * @param p_sAlias a {@link java.lang.String} object.
	 * @param p_sValue a {@link java.lang.String} object.
	 * @param p_oValueSqlType a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.SqlType} object.
	 */
	public SqlLikeCondition(Field p_oField, String p_sAlias, String p_sValue, SqlType p_oValueSqlType) {
		this.field = new SqlField(p_oField, p_sAlias);
		this.value = new SqlBindValue(SqlLikeCondition.wildcards(p_sValue), p_oValueSqlType);
	}

	/**
	 * <p>Constructor for SqlLikeCondition.</p>
	 *
	 * @param p_sFieldName a {@link java.lang.String} object.
	 * @param p_sAlias a {@link java.lang.String} object.
	 * @param p_sValue a {@link java.lang.String} object.
	 * @param p_oValueSqlType a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.SqlType} object.
	 */
	public SqlLikeCondition(String p_sFieldName, String p_sAlias, String p_sValue, SqlType p_oValueSqlType) {
		this.field = new SqlField(p_sFieldName, p_sAlias);
		this.value = new SqlBindValue(SqlLikeCondition.wildcards(p_sValue), p_oValueSqlType);
	}

	/**
	 * <p>Constructor for SqlLikeCondition.</p>
	 *
	 * @param p_sFieldName a {@link java.lang.String} object.
	 * @param p_sValue a {@link java.lang.String} object.
	 * @param p_oValueSqlType a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.SqlType} object.
	 */
	public SqlLikeCondition(String p_sFieldName, String p_sValue, SqlType p_oValueSqlType) {
		this.field = new SqlField(p_sFieldName);
		this.value = new SqlBindValue(SqlLikeCondition.wildcards(p_sValue), p_oValueSqlType);
	}

	/**
	 * Constructeur avec champs, valeur bindée
	 *
	 * @param p_oField champs
	 * @param p_oValue valeur bindée
	 */
	public SqlLikeCondition( SqlField p_oField, SqlBindValue p_oValue ) {
		this.field = p_oField;
		this.value = p_oValue ;
	}
			
	/**
	 * Constructeur avec champs, valeur bindlée, liaison avec la clause précédente
	 *
	 * @param p_oField champs
	 * @param p_oValue valeur bindée
	 * @param p_oSqlClauseLink liaison avec la clause précédente
	 */
	public SqlLikeCondition( SqlField p_oField, SqlBindValue p_oValue, SqlClauseLink p_oSqlClauseLink ) {
		this.field = p_oField;
		setLink(p_oSqlClauseLink);
		this.value = p_oValue ;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Bind les valeurs de la requête
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition#bindValues(com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder)
	 */
	@Override
	public void bindValues(StatementBinder p_oStatementBinder) throws SQLException {
		if ( this.value != null ) {
			p_oStatementBinder.bind(this.value.getValue(), this.value.getSqlType());
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * Clone le SqlLikeCondition
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition#clone()
	 */
	@Override
	public AbstractSqlCondition clone() {
		return new SqlEqualsValueCondition(this.field, this.value, getLink());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toSql( StringBuilder p_oSql, ToSqlContext p_oToSqlContext) {
		// Like forcémment sur la current locale pour les champs i18n
		this.field.toSql(p_oSql, p_oToSqlContext);
		p_oSql.append(' ');
		p_oSql.append(SqlKeywords.LIKE);
		p_oSql.append(" ?");
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void computeContentResolverSelection(StringBuilder p_oSql) {
		p_oSql.append(this.field.getName());
		p_oSql.append(' ');
		p_oSql.append(SqlKeywords.LIKE);
		p_oSql.append(" ?");
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
