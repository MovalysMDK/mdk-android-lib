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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlField;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>Conditions de nullité ou de non nullité</p>
 *
 * <p>Exemple 1 :</p>
 * <p>Pour le sql suivant : </p>
 * <pre>
 * {@code
 * WHERE intervention1.CODE IS NULL;
 * }</pre>
 *
 * <p>Le code Java correspondant est le suivant :</p>
 * <pre>
 * {@code
 * oSqlQuery.addToWhere( new SqlNullCondition(
 *			InterventionDao.InterventionField.CODE, InterventionDao.ALIAS_NAME ));
 * }</pre>
 *
 * <p>Exemple 2 :</p>
 * <p>Pour le sql suivant : </p>
 * <pre>
 * {@code
 * WHERE intervention1.CODE IS NOT NULL;
 * }</pre>
 *
 * <p>Le code Java correspondant est le suivant :</p>
 * <pre>
 * {@code
 * oSqlQuery.addToWhere( new SqlNullCondition(
 *			InterventionDao.InterventionField.CODE, InterventionDao.ALIAS_NAME, false ));
 * }</pre>
 */
public class SqlNullCondition extends AbstractSqlCondition implements Cloneable {

	/**
	 * Mot clé IS NOT NULL
	 */
	private static final String IS_NOT_NULL = " IS NOT NULL ";

	/**
	 * Mot clé IS NULL 
	 */
	private static final String IS_NULL = " IS NULL ";

	/**
	 * Champs de la condition de nullité
	 */
	private SqlField field ; 
	
	/**
	 * Vrai si on veut faire un test de nullité, faux si c'est un test de non nullité
	 */
	private boolean isNull = false;
	
	/**
	 * Constructeur avec champ, null
	 *
	 * @param p_oField a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field} object.
	 */
	public SqlNullCondition(Field p_oField) {
		this(p_oField, true);
	}
	
	/**
	 * Constructeur avec champ, null
	 *
	 * @param p_oField a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlField} object.
	 */
	public SqlNullCondition(SqlField p_oField) {
		this(p_oField, true);
	}

	/**
	 * Constructeur avec champ, alias, null
	 *
	 * @param p_oField a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field} object.
	 * @param p_sAlias a {@link java.lang.String} object.
	 */
	public SqlNullCondition(Field p_oField, String p_sAlias) {
		this(p_oField, p_sAlias, true);
	}
	
	/**
	 * Constructeur avec nom de champ, null
	 *
	 * @param p_sFieldName a {@link java.lang.String} object.
	 */
	public SqlNullCondition(String p_sFieldName) {
		this(p_sFieldName, true);
	}
	
	/**
	 * Constructeur avec nom de champ, alias, null
	 *
	 * @param p_sFieldName a {@link java.lang.String} object.
	 * @param p_sAlias a {@link java.lang.String} object.
	 */
	public SqlNullCondition(String p_sFieldName, String p_sAlias) {
		this(p_sFieldName, p_sAlias, true);
	}
	
	/**
	 * Constructeur avec champ, alias, indicateur de nullité
	 *
	 * @param p_oField a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field} object.
	 * @param p_bIsNull a boolean.
	 * @param p_sAlias a {@link java.lang.String} object.
	 */
	public SqlNullCondition(Field p_oField, String p_sAlias, boolean p_bIsNull) {
		this.field = new SqlField(p_oField, p_sAlias);
		this.isNull = p_bIsNull;
	}
	
	/**
	 * Constructeur avec champ, indicateur de nullité
	 *
	 * @param p_oField a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field} object.
	 * @param p_bIsNull a boolean.
	 */
	public SqlNullCondition(Field p_oField, boolean p_bIsNull) {
		this.field = new SqlField(p_oField);
		this.isNull = p_bIsNull;
	}
	
	/**
	 * Constructeur avec champ, indicateur de nullité
	 *
	 * @param p_oField a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlField} object.
	 * @param p_bIsNull a boolean.
	 */
	public SqlNullCondition(SqlField p_oField, boolean p_bIsNull) {
		this.field = p_oField;
		this.isNull = p_bIsNull;
	}
	
	/**
	 * Constructeur avec nom de champ, indicateur de nullité
	 *
	 * @param p_bIsNull a boolean.
	 * @param p_sFieldName a {@link java.lang.String} object.
	 */
	public SqlNullCondition(String p_sFieldName, boolean p_bIsNull) {
		this.field = new SqlField(p_sFieldName);
		this.isNull = p_bIsNull;
	}
	
	/**
	 * Constructeur avec nom de champ, alias, indicateur de nullité
	 *
	 * @param p_bIsNull a boolean.
	 * @param p_sFieldName a {@link java.lang.String} object.
	 * @param p_sAlias a {@link java.lang.String} object.
	 */
	public SqlNullCondition(String p_sFieldName, String p_sAlias, boolean p_bIsNull) {
		this.field = new SqlField(p_sFieldName, p_sAlias);
		this.isNull = p_bIsNull;
	}
	
	/** {@inheritDoc} */
	@Override
	public void bindValues(StatementBinder p_oStatementBinder) throws DaoException {
		// nothing to do
	}

	/** {@inheritDoc} */
	@Override
	public AbstractSqlCondition clone() {
		return new SqlNullCondition(field, isNull);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toSql( StringBuilder p_oSql, ToSqlContext p_oToSqlContext) {
		// Null forcémment sur la current locale pour les champs i18n
		this.field.toSql(p_oSql, p_oToSqlContext);
		if (!this.isNull) {
			p_oSql.append(IS_NOT_NULL);
		}
		else {
			p_oSql.append(IS_NULL);	
		}
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void computeContentResolverSelection(StringBuilder p_oSql) {
		p_oSql.append(this.field.getName());
		if (!this.isNull) {
			p_oSql.append(IS_NOT_NULL);
		}
		else {
			p_oSql.append(IS_NULL);	
		}
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void computeContentResolverSelectionArgs(SelectionArgsBuilder p_oSqlArgsBuilder) {
		// nothing to do
	}
}
