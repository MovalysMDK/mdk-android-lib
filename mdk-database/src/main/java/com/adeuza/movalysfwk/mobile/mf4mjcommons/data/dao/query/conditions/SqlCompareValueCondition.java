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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.contentprovider.SelectionArgsBuilder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.SqlType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlBindValue;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlField;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>Condition de comparaison avec une valeur</p>
 *
 * <p>Le SqlCompareValueCondition est composé de</p>
 * <ul>
 *   <li>Du champs comparé</li>
 *   <li>De la valeur bindée</li>
 *   <li>Opérateur de la comparaison : {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.OperatorCondition OperatorCondition}</li>
 * </ul>
 *
 * <p>Pour le sql suivant :</p>
 * <pre>
 * {@code
 * WHERE intervention1.INTERVENTIONSTATEID <= 5
 * }</pre>
 * <p>le code Java correspondant est :</p>
 * <pre>
 * {@code
 * oSqlQuery.addToWhere( new SqlCompareValueCondition(
 *   InterventionDao.InterventionField.INTERVENTIONSTATEID, InterventionDao.ALIAS_NAME,
 * 	 5, SqlType.INTEGER, OperatorCondition.INFERIOR_OR_EQUALS
 * ));
 * }
 * </pre>
 */
public class SqlCompareValueCondition extends AbstractSqlCondition implements Cloneable {

	/**
	 * Champs de la condition d'égalité
	 */
	private SqlField field ; 
	
	/**
	 * Valeur de la condition d'égalité 
	 */
	private SqlBindValue value ;
	
	/**
	 * Enumération indiquant l'opération à effectuer
	 */
	private @OperatorCondition String operator ;
	
	/**
	 * Constructeur avec champs et opérateur =
	 *
	 * @param p_oField champs pour l'égalité
	 */
	public SqlCompareValueCondition( Field p_oField) {
		this(p_oField, OperatorCondition.EQUALS);
	}
	
	/**
	 * Constructeur avec champs
	 *
	 * @param p_oField champs pour l'égalité
	 * @param p_sOperator a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.OperatorCondition} object.
	 */
	public SqlCompareValueCondition( Field p_oField, @OperatorCondition String p_sOperator ) {
		this.field = new SqlField(p_oField);
		this.operator = p_sOperator;
	}
	
	/**
	 * Constructeur avec champs, valeur et opérateur =
	 *
	 * @param p_oField champs
	 * @param p_oValue Valeur bindée
	 */
	public SqlCompareValueCondition( SqlField p_oField, SqlBindValue p_oValue) {
		this(p_oField, p_oValue, OperatorCondition.EQUALS);
	}
		
	/**
	 * Constructeur avec champs, valeur
	 *
	 * @param p_oField champs
	 * @param p_oValue Valeur bindée
	 * @param p_sOperator a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.OperatorCondition} object.
	 */
	public SqlCompareValueCondition( SqlField p_oField, SqlBindValue p_oValue, @OperatorCondition String p_sOperator ) {
		this.field = p_oField;
		this.value = p_oValue ;
		this.operator = p_sOperator;
	}
		
	/**
	 * Constructeur avec champs, type de la valeur et opérateur =
	 *
	 * @param p_oField champs
	 * @param p_iSqlType type de la valeur
	 */
	public SqlCompareValueCondition( SqlField p_oField, @SqlType int p_iSqlType) {
		this(p_oField, p_iSqlType, OperatorCondition.EQUALS);
	}
	
	/**
	 * Constructeur avec champs, type de la valeur
	 *
	 * @param p_oField champs
	 * @param p_iSqlType type de la valeur
	 * @param p_sOperator a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.OperatorCondition} object.
	 */
	public SqlCompareValueCondition( SqlField p_oField, @SqlType int p_iSqlType, @OperatorCondition String p_sOperator ) {
		this.field = p_oField;
		this.value = new SqlBindValue(null, p_iSqlType) ;
		this.operator = p_sOperator;
	}
	
	/**
	 * Constructeur avec nom du champs, type de la valeur et opérateur =
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_iSqlType type de la valeur
	 */
	public SqlCompareValueCondition( String p_sFieldName, @SqlType int p_iSqlType) {
		this(p_sFieldName, p_iSqlType, OperatorCondition.EQUALS);
	}
	
	/**
	 * Constructeur avec nom du champs, type de la valeur
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_iSqlType type de la valeur
	 * @param p_sOperator a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.OperatorCondition} object.
	 */
	public SqlCompareValueCondition( String p_sFieldName, @SqlType int p_iSqlType, @OperatorCondition String p_sOperator ) {
		this.field = new SqlField(p_sFieldName);
		this.value = new SqlBindValue(null, p_iSqlType) ;
		this.operator = p_sOperator;
	}
	
	/**
	 * Constructeur avec nom du champs, valeur, type de la valeur et opérateur =
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_oValue valeur
	 * @param p_iSqlType type de la valeur
	 */
	public SqlCompareValueCondition( String p_sFieldName, Object p_oValue, @SqlType int p_iSqlType) {
		this(p_sFieldName, p_oValue, p_iSqlType, OperatorCondition.EQUALS);
	}
	
	/**
	 * Constructeur avec nom du champs, valeur, type de la valeur
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_oValue valeur
	 * @param p_iSqlType type de la valeur
	 * @param p_sOperator a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.OperatorCondition} object.
	 */
	public SqlCompareValueCondition( String p_sFieldName, Object p_oValue, @SqlType int p_iSqlType, @OperatorCondition String p_sOperator ) {
		this.field = new SqlField(p_sFieldName);
		this.value = new SqlBindValue(p_oValue, p_iSqlType) ;
		this.operator = p_sOperator;
	}
	
	/**
	 * Constructeur avec nom du champs, alias du champs, type de la valeur et opérateur =
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_iSqlType type SQL du champs
	 * @param p_oValue a {@link java.lang.Object} object.
	 */
	public SqlCompareValueCondition( String p_sFieldName, String p_sAlias, Object p_oValue, @SqlType int p_iSqlType) {
		this(p_sFieldName, p_sAlias, p_oValue, p_iSqlType, OperatorCondition.EQUALS);
	}
	
	/**
	 * Constructeur avec nom du champs, alias du champs, type de la valeur
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_iSqlType type SQL du champs
	 * @param p_oValue a {@link java.lang.Object} object.
	 * @param p_sOperator a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.OperatorCondition} object.
	 */
	public SqlCompareValueCondition( String p_sFieldName, String p_sAlias, Object p_oValue, @SqlType int p_iSqlType, 
			@OperatorCondition String p_sOperator ) {
		this.field = new SqlField(p_sFieldName, p_sAlias);
		this.value = new SqlBindValue(p_oValue, p_iSqlType) ;
		this.operator = p_sOperator;
	}
	
	/**
	 * Constructeur avec nom du champs, alias du champs, type de la valeur et opérateur =
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_iSqlType type SQL du champs
	 */
	public SqlCompareValueCondition( String p_sFieldName, String p_sAlias, @SqlType int p_iSqlType) {
		this(p_sFieldName, p_sAlias, p_iSqlType, OperatorCondition.EQUALS);
	}
	
	/**
	 * Constructeur avec nom du champs, alias du champs, type de la valeur
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_iSqlType type SQL du champs
	 * @param p_sOperator a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.OperatorCondition} object.
	 */
	public SqlCompareValueCondition( String p_sFieldName, String p_sAlias, @SqlType int p_iSqlType, @OperatorCondition String p_sOperator ) {
		this.field = new SqlField(p_sFieldName, p_sAlias);
		this.value = new SqlBindValue(null, p_iSqlType) ;
		this.operator = p_sOperator;
	}
	
	/**
	 * Constructeur avec nom du champs, alias de la table du champs, type de la valeur et opérateur =
	 *
	 * @param p_oField champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_iSqlType type SQL du champs
	 */
	public SqlCompareValueCondition( Field p_oField, String p_sAlias, @SqlType int p_iSqlType) {
		this(p_oField, p_sAlias, p_iSqlType, OperatorCondition.EQUALS);
	}
	
	/**
	 * Constructeur avec nom du champs, alias de la table du champs, type de la valeur
	 *
	 * @param p_oField champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_iSqlType type SQL du champs
	 * @param p_sOperator a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.OperatorCondition} object.
	 */
	public SqlCompareValueCondition( Field p_oField, String p_sAlias, @SqlType int p_iSqlType, @OperatorCondition String p_sOperator ) {
		this.field = new SqlField(p_oField, p_sAlias);
		this.value = new SqlBindValue(null, p_iSqlType) ;
		this.operator = p_sOperator;
	}
	
	/**
	 * Constructeur avec champs, alias de la table du champs, valeur pour l'égalité et type de la valeur et opérateur =
	 *
	 * @param p_oField champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_oValue valeur pour l'égalité
	 * @param p_iSqlType type SQL du champs
	 */
	public SqlCompareValueCondition( Field p_oField, String p_sAlias, Object p_oValue, @SqlType int p_iSqlType) {
		this(p_oField, p_sAlias, p_oValue, p_iSqlType, OperatorCondition.EQUALS);
	}
	
	/**
	 * Constructeur avec champs, alias de la table du champs, valeur pour l'égalité et type de la valeur
	 *
	 * @param p_oField champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_oValue valeur pour l'égalité
	 * @param p_iSqlType type SQL du champs
	 * @param p_sOperator a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.OperatorCondition} object.
	 */
	public SqlCompareValueCondition( Field p_oField, String p_sAlias, Object p_oValue, @SqlType int p_iSqlType, @OperatorCondition String p_sOperator ) {
		this.field = new SqlField(p_oField, p_sAlias);
		this.value = new SqlBindValue(p_oValue, p_iSqlType) ;
		this.operator = p_sOperator;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Bind les valeurs de la condition d'égalité
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition#bindValues(com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder)
	 */
	@Override
	public void bindValues(StatementBinder p_oStatementBinder) throws DaoException {
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
		return new SqlCompareValueCondition(this.field, this.value, this.operator);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Génère le SQL de la requête
	 */
	@Override
	public void toSql( StringBuilder p_oSql, ToSqlContext p_oToSqlContext) {
		// Between forcémment sur la current locale pour les champs i18n
		this.field.toSql(p_oSql, p_oToSqlContext);
		p_oSql.append(this.operator);
		p_oSql.append('?');
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void computeContentResolverSelection(StringBuilder p_oSql) {
		p_oSql.append(this.field.getName());
		p_oSql.append(this.operator);
		p_oSql.append('?');
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
