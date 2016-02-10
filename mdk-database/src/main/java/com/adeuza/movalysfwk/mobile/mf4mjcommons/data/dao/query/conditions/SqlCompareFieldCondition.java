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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlClauseLink;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlField;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>Condition de comparaison entre 2 champs</p>
 *
 * <p>Le SqlCompareFieldCondition est composé de</p>
 * <ul>
 *   <li>Des deux champs comparés</li>
 *   <li>Opérateur de la comparaison : {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.OperatorCondition OperatorCondition}</li>
 * </ul>
 *
 * <p>Pour le sql suivant :</p>
 * <pre>
 * {@code
 *  WHERE intervention1.REQUESTID = request1.ID
 * }</pre>
 * <p>Le code Java correspondant est :</p>
 * <pre>
 * {@code
 * oSqlQuery.addToWhere( new SqlCompareFieldCondition(
 * 	  InterventionDao.InterventionField.INTERVENTIONSTATEID, InterventionDao.ALIAS_NAME,
 *    RequestDao.RequestField.ID, RequestDao.ALIAS_NAME
 * ));
 * }
 * </pre>
 */
public class SqlCompareFieldCondition extends AbstractSqlCondition implements Cloneable {

	/**
	 * Premier champs pour l'égalité
	 */
	private SqlField field1 ; 
	
	/**
	 * Deuxième champs pour l'égalité 
	 */
	private SqlField field2 ; 
	
	/**
	 * Enumération indiquant l'opération à effectuer
	 */
	private OperatorCondition operator ;
	
	/**
	 * Constructeur avec champs1, champs2, opérateur =
	 *
	 * @param p_oField1 champs1
	 * @param p_oField2 champs2
	 */
	public SqlCompareFieldCondition( Field p_oField1, Field p_oField2) {
		this(p_oField1, p_oField2, OperatorCondition.EQUALS);
	}
	
	/**
	 * Constructeur avec champs1, champs2 et opérateur
	 *
	 * @param p_oField1 champs1
	 * @param p_oField2 champs2
	 * @param p_operator Opérateur de calcul
	 */
	public SqlCompareFieldCondition( Field p_oField1, Field p_oField2, OperatorCondition p_operator) {
		this.field1 = new SqlField(p_oField1);
		this.field2 = new SqlField(p_oField2);
		this.operator = p_operator;
	}
	
	/**
	 * Constructeur avec champs1, champs2, opérateur =
	 *
	 * @param p_oField1 champs1
	 * @param p_oField2 champs2
	 */
	public SqlCompareFieldCondition( SqlField p_oField1, SqlField p_oField2) {
		this(p_oField1, p_oField2, OperatorCondition.EQUALS);
	}
	
	/**
	 * Constructeur avec champs1, champs2
	 *
	 * @param p_oField1 champs1
	 * @param p_oField2 champs2
	 * @param p_operator Opérateur de calcul
	 */
	public SqlCompareFieldCondition( SqlField p_oField1, SqlField p_oField2, OperatorCondition p_operator ) {
		this.field1 = p_oField1;
		this.field2 = p_oField2;
		this.operator = p_operator;
	}
	
	/**
	 * Constructeur avec alias1.champs1, alias2.champs2 et opérateur =
	 *
	 * @param p_sFieldName1 nom du champs1
	 * @param p_sAlias1 alias de la table du champs 1
	 * @param p_sFieldName2 nom du champs2
	 * @param p_sAlias2 alias de la table du champs 2
	 */
	public SqlCompareFieldCondition( String p_sFieldName1, String p_sAlias1, String p_sFieldName2, String p_sAlias2) {
		this(p_sFieldName1, p_sAlias1, p_sFieldName2, p_sAlias2, OperatorCondition.EQUALS);
	}

	/**
	 * Constructeur avec alias1.champs1, alias2.champs2
	 *
	 * @param p_sFieldName1 nom du champs1
	 * @param p_sAlias1 alias de la table du champs 1
	 * @param p_sFieldName2 nom du champs2
	 * @param p_sAlias2 alias de la table du champs 2
	 * @param p_operator Opérateur de calcul
	 */
	public SqlCompareFieldCondition( String p_sFieldName1, String p_sAlias1, String p_sFieldName2, String p_sAlias2, OperatorCondition p_operator ) {
		this.field1 = new SqlField(p_sFieldName1, p_sAlias1);
		this.field2 = new SqlField(p_sFieldName2, p_sAlias2);
		this.operator = p_operator;
	}
	
	/**
	 * Constructeur avec alias1.champs1, alias2.champs2 et opérateur =
	 *
	 * @param p_sFieldName1 nom du champs1
	 * @param p_sAlias1 alias de la table du champs 1
	 * @param p_sFieldName2 nom du champs2
	 * @param p_sAlias2 alias de la table du champs 2
	 */
	public SqlCompareFieldCondition( Field p_sFieldName1, String p_sAlias1, Field p_sFieldName2, String p_sAlias2) {
		this(p_sFieldName1, p_sAlias1, p_sFieldName2, p_sAlias2, OperatorCondition.EQUALS);
	}
			
	/**
	 * Constructeur avec alias1.champs1, alias2.champs2
	 *
	 * @param p_sFieldName1 nom du champs1
	 * @param p_sAlias1 alias de la table du champs 1
	 * @param p_sFieldName2 nom du champs2
	 * @param p_sAlias2 alias de la table du champs 2
	 * @param p_operator Opérateur de calcul
	 */
	public SqlCompareFieldCondition( Field p_sFieldName1, String p_sAlias1, Field p_sFieldName2, String p_sAlias2, OperatorCondition p_operator ) {
		this.field1 = new SqlField(p_sFieldName1, p_sAlias1);
		this.field2 = new SqlField(p_sFieldName2, p_sAlias2);
		this.operator = p_operator;
	}
	
	/**
	 * Constructeur avec champs1, champs2 et opérateur =
	 *
	 * @param p_oField1 champs1
	 * @param p_oField2 champs2
	 * @param p_oSqlClauseLink liaison avec la condition précédente
	 */
	public SqlCompareFieldCondition( SqlField p_oField1, SqlField p_oField2, SqlClauseLink p_oSqlClauseLink) {
		this(p_oField1, p_oField2, p_oSqlClauseLink, OperatorCondition.EQUALS);
	}
			
	/**
	 * Constructeur avec champs1, champs2
	 *
	 * @param p_oField1 champs1
	 * @param p_oField2 champs2
	 * @param p_oSqlClauseLink liaison avec la condition précédente
	 * @param p_operator Opérateur de calcul
	 */
	public SqlCompareFieldCondition( SqlField p_oField1, SqlField p_oField2, SqlClauseLink p_oSqlClauseLink, OperatorCondition p_operator ) {
		this.field1 = p_oField1;
		this.field2 = p_oField2;
		this.setLink(p_oSqlClauseLink);
		this.operator = p_operator;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Bind les valeurs
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition#bindValues(com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder)
	 */
	@Override
	public void bindValues(StatementBinder p_oStatementBinder) throws SQLException {
		//Nothing to do
	}

	/**
	 * {@inheritDoc}
	 *
	 * Clone le SqlEqualsFieldCondition
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition#clone()
	 */
	@Override
	public AbstractSqlCondition clone() {
		return new SqlCompareFieldCondition(this.field1, this.field2, getLink(), this.operator);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Génère le Sql de la condition d'égalité
	 */
	@Override
	public void toSql( StringBuilder p_oSql, ToSqlContext p_oToSqlContext) {
		// Comparaison forcémment sur la current locale pour les champs i18n
		this.field1.toSql(p_oSql, p_oToSqlContext);
		p_oSql.append(this.operator.toSql());
		this.field2.toSql(p_oSql, p_oToSqlContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void computeContentResolverSelection(StringBuilder p_oSql) {
		p_oSql.append(this.field1.getName());
		p_oSql.append(this.operator.toSql());
		p_oSql.append(this.field2.getName());
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void computeContentResolverSelectionArgs(SelectionArgsBuilder p_oSqlArgsBuilder) {
		//Nothing to do
	}
}
