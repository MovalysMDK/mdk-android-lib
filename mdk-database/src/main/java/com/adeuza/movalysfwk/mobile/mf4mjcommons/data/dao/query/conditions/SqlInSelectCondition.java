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

import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.contentprovider.SelectionArgsBuilder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.SqlType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlKeywords;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlQuery;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>Condition IN dont les valeurs sont les résultats d'une requête SQL</p>
 *
 * <p>Exemple :</p>
 * <p>Pour le sql suivant : </p>
 * <pre>
 * {@code
 * select code from mit_request request1 where request1.id in
 *   ( select intervention1.requestid from mit_intervention intervention1 )
 * }</pre>
 *
 * <p>Le code Java correspondant est le suivant :</p>
 * <pre>
 * {@code
 * SqlQuery oSubQuery = new SqlQuery();
 * oSubQuery.addFieldToSelect( InterventionDao.ALIAS_NAME, InterventionField.REQUESTID);
 * oSubQuery.addToFrom(InterventionDao.TABLE_NAME, InterventionDao.ALIAS_NAME);
 *
 * SqlQuery oSqlQuery = new SqlQuery();
 * oSqlQuery.addFieldToSelect(RequestDao.ALIAS_NAME, RequestField.CODE );
 * oSqlQuery.addToFrom(RequestDao.TABLE_NAME, RequestDao.ALIAS_NAME);
 * List<SqlInField> listInFields = new ArrayList<SqlInField>();
 * listInFields.add(new SqlInField(RequestField.ID, RequestDao.ALIAS_NAME, SqlType.INTEGER));
 * oSqlQuery.addToWhere(new SqlInSelectCondition(listInFields, oSubQuery));
 * }</pre>
 */
public class SqlInSelectCondition extends AbstractSqlInCondition implements Cloneable {

	/**
	 * Requête ramenant les valeurs du IN 
	 */
	private SqlQuery sqlQuery ;
	
	/**
	 * Constructeur avec nom du champs, alias de la table du champs, type SQL du champs, requête pour les valeurs
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_iSqlType type SQL du champs
	 * @param p_oSqlQuery requête ramenant les valeurs du IN
	 */
	public SqlInSelectCondition(String p_sFieldName, String p_sAlias, @SqlType int p_iSqlType, SqlQuery p_oSqlQuery ) {
		this.setField(p_sFieldName, p_sAlias, p_iSqlType);
		this.sqlQuery = p_oSqlQuery;
	}
	
	/**
	 * Constructeur avec champs, alias de la table du champs, type SQL du champs, requête pour les valeurs
	 *
	 * @param p_oField champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_iSqlType type SQL du champs
	 * @param p_oSqlQuery requête ramenant les valeurs du IN
	 */
	public SqlInSelectCondition(Field p_oField, String p_sAlias, @SqlType int p_iSqlType, SqlQuery p_oSqlQuery ) {
		this.setField(p_oField, p_sAlias, p_iSqlType);
		this.sqlQuery = p_oSqlQuery;
	}

	/**
	 * Constructeur avec liste des champs du In, requête pour les valeurs
	 *
	 * @param p_listFields liste des champs du In
	 * @param p_oSqlQuery requête ramenant les valeurs du IN
	 */
	public SqlInSelectCondition(List<SqlInField> p_listFields, SqlQuery p_oSqlQuery) {
		setFields(p_listFields);
		this.sqlQuery = p_oSqlQuery;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Génère le SQL de la condition
	 */
	@Override
	public void toSql( StringBuilder p_oStringBuilder, ToSqlContext p_oToSqlContext) throws DaoException {

		p_oStringBuilder.append('(');
		int iNbFields = getNbFields();
		for ( SqlInField oSqlInField : getListFields()) {
			if ( oSqlInField.getAlias() != null ) {
				p_oStringBuilder.append(oSqlInField.getAlias());
				p_oStringBuilder.append('.');
			}
			p_oStringBuilder.append(oSqlInField.getFieldName());
			if (--iNbFields > 0) {
				p_oStringBuilder.append(',');
			}
		}
		p_oStringBuilder.append(") ");

		if (this.isInverse()) {
			p_oStringBuilder.append(SqlKeywords.NOT_IN);
		}
		else {
			p_oStringBuilder.append(SqlKeywords.IN);
		}

		p_oStringBuilder.append(" (");
		p_oStringBuilder.append(this.sqlQuery.toSql(p_oToSqlContext));
		p_oStringBuilder.append(')');
	}

	/**
	 * {@inheritDoc}
	 *
	 * Bind les valeurs de la requête
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition#bindValues(com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder)
	 */
	@Override
	public void bindValues(StatementBinder p_oStatementBinder) throws DaoException {
		this.sqlQuery.bindValues(p_oStatementBinder);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Clone le InSelectCondition
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition#clone()
	 */
	@Override
	public AbstractSqlCondition clone() {
		List<SqlInField> listSqlInFields = new ArrayList<SqlInField>();
		for( SqlInField oSqlInField : getListFields()) {
			listSqlInFields.add( oSqlInField.clone());
		}
		AbstractSqlInCondition r_oSqlCondition = new SqlInSelectCondition(listSqlInFields, this.sqlQuery.clone());
		r_oSqlCondition.setInverse(isInverse());
		return r_oSqlCondition ;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void computeContentResolverSelection(StringBuilder p_oSql) {
		throw new UnsupportedOperationException("value in (select...) are not supported by ContentResolver");
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void computeContentResolverSelectionArgs(SelectionArgsBuilder p_oSqlArgsBuilder) {
		throw new UnsupportedOperationException("value in (select...) are not supported by ContentResolver");
	}
}
