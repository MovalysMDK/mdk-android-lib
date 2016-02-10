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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>Partie d'un select (générique)</p>
 * 
 * @see AbstractSqlSelectPart
 */
public class SqlGenericSelectPart extends AbstractSqlSelectPart implements Cloneable {

	/**
	 * Chaine sql
	 */
	private String sql ;
	
	/**
	 * Constructeur
	 *
	 * @param p_sSql chaine sql de la partie du select
	 */
	public SqlGenericSelectPart( String p_sSql ) {
		this.sql = p_sSql ;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select.AbstractSqlSelectPart#clone()
	 */
	@Override
	public AbstractSqlSelectPart clone() {
		return new SqlGenericSelectPart( this.sql );
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select.AbstractSqlSelectPart#toSql(java.lang.StringBuilder)
	 */
	@Override
	public void toSql(StringBuilder p_oStringBuilder, ToSqlContext p_oToSqlContext) {
		p_oStringBuilder.append(this.sql);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void computeContentResolverProjection(List<String> p_listColumns) {
		throw new UnsupportedOperationException();
	}
}
