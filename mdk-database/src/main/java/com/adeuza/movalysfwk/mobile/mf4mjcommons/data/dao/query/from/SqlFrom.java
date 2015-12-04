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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlKeywords;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>Partie From d'un SqlQuery.</p>
 *
 * <p>Un From est composé d'une liste de {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.from.AbstractSqlFromPart AbstractSqlFromPart}. Les
 * AbstractSqlFromPart sont séparés par une virgule dans la syntaxe Sql.</p>
 */
public class SqlFrom implements Cloneable {

	/**
	 * Liste des parties du From
	 */
	private List<AbstractSqlFromPart> fromParts = new ArrayList<AbstractSqlFromPart>();
	
	/**
	 * Ajoute un SqlFromPart au From
	 *
	 * @param p_oPart SqlFromPart à ajouter
	 */
	public void addFromPart( AbstractSqlFromPart p_oPart) {
		this.fromParts.add( p_oPart );
	}
	
	/**
	 * Génère le sql du From
	 *
	 * @param p_oSqlContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext} object.
	 * @return sql du From
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException échec de construction du sql
	 */
	public List<StringBuilder> toSql(ToSqlContext p_oSqlContext) throws DaoException {
		List<StringBuilder> listSQL = new ArrayList<StringBuilder>();
		boolean bIsFirst = true;
		for( AbstractSqlFromPart oFromPart : fromParts ) {
			if (bIsFirst) {
				StringBuilder oSQLStringBuilder = new StringBuilder();
				oSQLStringBuilder.append(' ');
				oSQLStringBuilder.append(SqlKeywords.FROM);
				oSQLStringBuilder.append(' ');
				oSQLStringBuilder.append(oFromPart.toFirstSql(p_oSqlContext));
				oSQLStringBuilder.append(' ');
				listSQL.add(oSQLStringBuilder);
				bIsFirst = false;
			}
			else {
				for (StringBuilder oListStringBuilder : listSQL) {
					for (String sTable : oFromPart.toOtherSql(p_oSqlContext)) {
						StringBuilder oSQLStringBuilder = new StringBuilder();
						oSQLStringBuilder.append(' ');
						oSQLStringBuilder.append(SqlKeywords.INNER_JOIN);
						oSQLStringBuilder.append(' ');
						oSQLStringBuilder.append(sTable);
						oSQLStringBuilder.append(' ');
						oListStringBuilder.append(oSQLStringBuilder);
					}
				}
			}
		}
		return listSQL;
	}

	/**
	 * Retourne la première partie du From
	 *
	 * @return premiere partie du From
	 */
	public AbstractSqlFromPart getFirstFromPart() {
		return this.getSqlFromPart(0);
	}
	
	/**
	 * Retourne le SqlFromPart à l'index p_iIndex
	 *
	 * @param p_iIndex index du SqlFromPart à retourner
	 * @return SqlFromPart à l'index p_iIndex
	 */
	public AbstractSqlFromPart getSqlFromPart( int p_iIndex ) {
		return this.fromParts.get(p_iIndex);
	}
	
	/**
	 * Bind les valeurs du From
	 *
	 * @param p_oStatementBinder StatementBinder à utiliser
	 * @throws DaoException échec du binding
	 */
	public void bindValues(StatementBinder p_oStatementBinder) throws DaoException {
		for( AbstractSqlFromPart oFromPart : fromParts ) {
			oFromPart.bindValues(p_oStatementBinder);
		}
	}
	
	/**
	 * Clone le From
	 *
	 * @return le clone du From
	 * @see java.lang.Object#clone()
	 */
	@Override
	public SqlFrom clone() {
		SqlFrom r_oSqlFrom = new SqlFrom();
		for( AbstractSqlFromPart oSqlFromPart: this.fromParts ) {
			r_oSqlFrom.addFromPart(oSqlFromPart.clone());
		}
		return r_oSqlFrom ;
	}

	/**
	 * Count number of parts in from
	 * @return number of parts in from
	 */
	public int countFromParts() {
		return this.fromParts.size();
	}
}
