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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select;

import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlField;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlFunction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>Ajoute une fonction à un select.</p>
 * <p>Ne gère les functions qu'avec un seul paramètre</p>
 *
 * <p>Exemple : SELECT MAX(CREATIONDATE) FROM MIT_INTERVENTION
 * <pre>
 * {@code
 * SqlQuery oQuery = new SqlQuery();
 * ...
 * oQuery.addToSelect( new SqlFunctionSelectPart( SqlFunction.MAX, InterventionField.ID, InterventionDao.ALIAS_NAME ));
 * }
 * </pre>
 *
 *
 * @since 2.5
 * @see AbstractSqlSelectPart
 */
public class SqlFunctionSelectPart extends AbstractSqlSelectPart implements Cloneable {

	/**
	 * Nom du champs
	 */
	private SqlField sqlField ;
	
	/**
	 * Sql Function
	 */
	private SqlFunction sqlFunction ;
	
	/**
	 * Constructeur
	 *
	 * @param p_oField a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field} object.
	 * @param p_sTablePrefix a {@link java.lang.String} object.
	 * @param p_oSqlFunction a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlFunction} object.
	 */
	public SqlFunctionSelectPart( SqlFunction p_oSqlFunction, Field p_oField, String p_sTablePrefix ) {
		this.sqlField = new SqlField( p_oField, p_sTablePrefix );
		this.sqlFunction = p_oSqlFunction ;
	}

	/**
	 * Constructeur
	 *
	 * @param p_sField a {@link java.lang.String} object.
	 * @param p_sTablePrefix a {@link java.lang.String} object.
	 * @param p_oSqlFunction a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlFunction} object.
	 */
	protected SqlFunctionSelectPart( SqlFunction p_oSqlFunction, String p_sField, String p_sTablePrefix ) {
		this.sqlField = new SqlField(p_sField, p_sTablePrefix);
		this.sqlFunction = p_oSqlFunction;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select.AbstractSqlSelectPart#clone()
	 */
	@Override
	public SqlFunctionSelectPart clone() {
		return new SqlFunctionSelectPart(this.sqlFunction, this.sqlField.getName(), this.sqlField.getTablePrefix());
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select.AbstractSqlSelectPart#toSql(java.lang.StringBuilder)
	 */
	@Override
	public void toSql(StringBuilder p_oStringBuilder, ToSqlContext p_oToSqlContext ) {
		p_oStringBuilder.append(sqlFunction.name());
		p_oStringBuilder.append('(');
		this.sqlField.toSql(p_oStringBuilder, p_oToSqlContext);
		p_oStringBuilder.append(')');
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void computeContentResolverProjection(List<String> p_listColumns) {
		throw new UnsupportedOperationException();
	}
}
