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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.SqlType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlField;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlKeywords;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>Condition Between : la valeur d'un champs est comprise entre deux valeurs</p>
 *
 * <p>Pour le sql suivant :</p>
 * <pre>
 * {@code
 * WHERE intervention1.INTERVENTIONSTATEID BETWEEN 2 AND 4
 * }</pre>
 * <p>Le code java correspondant est le suivant :</p>
 * <pre>
 * {@code
 * oSqlQuery.addToWhere( new SqlBetweenCondition(
 *   new SqlField(InterventionDao.InterventionField.INTERVENTIONSTATEID, InterventionDao.ALIAS_NAME),
 *   2, 4, SqlType.INTEGER));
 * }</pre>
 */
public class SqlBetweenCondition extends AbstractSqlCondition implements Cloneable {

	/**
	 * Champs de la condition
	 */
	private SqlField field ; 
	
	/**
	 * Type Sql du champs
	 */
	private SqlType sqlType ;
	
	/**
	 * Valeur basse
	 */
	private Object lowerValue ;
	
	/**
	 * Valeur haute
	 */
	private Object upperValue ;
		
	/**
	 * Constructeur
	 *
	 * @param p_oField champs du between
	 * @param p_oLowerValue valeur basse
	 * @param p_oUpperValue valeur haute
	 * @param p_oSqlType type SQL
	 */
	public SqlBetweenCondition(SqlField p_oField, Object p_oLowerValue, Object p_oUpperValue, SqlType p_oSqlType) {
		this.field = p_oField ;
		this.sqlType = p_oSqlType ;
		this.lowerValue = p_oLowerValue ;
		this.upperValue = p_oUpperValue ;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Binde les valeurs du between
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition#bindValues(com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder)
	 */
	@Override
	public void bindValues(StatementBinder p_oStatementBinder) throws SQLException {
		if ( this.lowerValue != null && this.upperValue != null && this.sqlType != null) {
			p_oStatementBinder.bind(this.lowerValue, this.sqlType);
			p_oStatementBinder.bind(this.upperValue, this.sqlType);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * Clone le between
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition#clone()
	 */
	@Override
	public AbstractSqlCondition clone() {
		return new SqlBetweenCondition(this.field, this.lowerValue, this.upperValue, this.sqlType );
	}

	/**
	 * {@inheritDoc}
	 *
	 * Génère le SQL du Between
	 */
	@Override
	public void toSql( StringBuilder p_oSql, ToSqlContext p_oToSqlContext ) {
		// Between forcémment sur la current locale pour les champs i18n
		this.field.toSql(p_oSql, p_oToSqlContext);
		p_oSql.append(' ');
		p_oSql.append(SqlKeywords.BETWEEN);
		p_oSql.append(" ? AND ?");
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void computeContentResolverSelection(StringBuilder p_oSql) {
		p_oSql.append(this.field.getName());
		p_oSql.append(' ');
		p_oSql.append(SqlKeywords.BETWEEN);
		p_oSql.append(" ? AND ?");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void computeContentResolverSelectionArgs(SelectionArgsBuilder p_oSqlArgsBuilder) {
		p_oSqlArgsBuilder.addValue(this.lowerValue, this.sqlType);
		p_oSqlArgsBuilder.addValue(this.upperValue, this.sqlType);
	}
}
