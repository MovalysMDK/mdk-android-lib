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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.from;

import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.joins.AbstractSqlJoin;

/**
 * <p>
 * Partie From de type Table
 * </p>
 * <p>
 * syntaxe: SELECT... FROM TABLE alias...
 * </p>
 * <p>Exemple :</p>
 * <p>Pour le Sql suivant :</p>
 * <pre>
 * {@code
 * SELECT intervention1.code
 * FROM intervention1 }</pre>
 *
 * <p>Le code java correspondant est le suivant : </p>
 * <pre>
 * {@code
 * SqlQuery oSqlSelect = new SqlQuery();
 * oSqlSelect.addFieldToSelect(InterventionDao.ALIAS_NAME, InterventionField.CODE);
 * oSqlSelect.addToFrom( new SqlTableFromPart(oFromQuery, InterventionDao.ALIAS_NAME)); }</pre>
 *
 * <p>Ou plus simplement : </p>
 * <pre>
 * {@code
 * SqlQuery oSqlSelect = new SqlQuery();
 * oSqlSelect.addFieldToSelect(InterventionDao.ALIAS_NAME, InterventionField.CODE);
 * oFromQuery.addToFrom(InterventionDao.TABLE_NAME, InterventionDao.ALIAS_NAME);
 * }</pre>
 *
 * @since 2.5
 * @see AbstractSqlFromPart
 */
public class SqlTableFromPart extends AbstractSqlFromPart implements Cloneable {

	/**
	 * Nom de la table
	 */
	private String tableName;

	/**
	 * Constructeur avec nom de la table, alias de la table
	 *
	 * @param p_sTableName
	 *            nom de la table
	 * @param p_sAliasName
	 *            alias de la table
	 */
	public SqlTableFromPart(String p_sTableName, String p_sAliasName) {
		this.tableName = p_sTableName;
		setAlias(p_sAliasName);
	}

	/**
	 * Returns the table name.
	 *
	 * @return The table name
	 */
	public String getTableName() {
		return this.tableName;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.from.AbstractSqlFromPart#toFirstSql(com.adeuza.movalysfwk.mobile.mf4javacommons.core.historisation.map.QueryHistorisationBlocMap)
	 */
	@Override
	public String toFirstSql(ToSqlContext p_oSqlContext) throws DaoException {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(this.tableName);
		if (this.getAlias() != null) {
			sBuilder.append(' ');
			sBuilder.append(this.getAlias());
		}

		this.joinToSql(sBuilder, p_oSqlContext);

		return sBuilder.toString();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.from.AbstractSqlFromPart#toOtherSql(QueryHistorisationBlocMap)
	 */
	@Override
	public List<String> toOtherSql(ToSqlContext p_oSqlContext) throws DaoException {
		List<String> r_list = new ArrayList<String>();

		StringBuilder sBuilder = new StringBuilder( );
		sBuilder.append(this.tableName);
		if (this.getAlias() != null) {
			sBuilder.append(' ');
			sBuilder.append(this.getAlias());
		}
		this.joinToSql(sBuilder, p_oSqlContext);
		r_list.add(sBuilder.toString());

		return r_list;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Clone le SqlTableFromPart
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.from.AbstractSqlFromPart#clone()
	 */
	@Override
	public AbstractSqlFromPart clone() {
		AbstractSqlFromPart r_oSqlFromPart = new SqlTableFromPart(this.tableName, this.getAlias());
		for (AbstractSqlJoin oJoin : this.getJoins()) {
			r_oSqlFromPart.addSqlJoin(oJoin.clone());
		}
		return r_oSqlFromPart;
	}
}
