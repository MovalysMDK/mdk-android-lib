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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.joins.AbstractSqlJoin;

/**
 * <p>Partie d'un From.</p>
 *
 * <p>Une partie d'un From est composée de </p>
 * <ul>
 *  <li>L'élément principal (Table, sous-requête)</li>
 *  <li>L'alias de l'élément</li>
 *  <li>Liste de jointures qui partent de l'élément principal</li>
 * </ul>
 *
 * <p>Les implémentations disponibles sont :</p>
 * <ul>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.from.SqlTableFromPart SqlTableFromPart} : pour une table (from TABLE)</li>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.from.SqlSelectFromPart SqlSelectFromPart} : pour une sous-requête (from ( select ... from TABLE ))</li>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.from.SqlViewFromPart SqlViewFromPart} :  pour une vue (from VIEW)</li>
 * </ul>
 *
 * @since 2.5
 */
public abstract class AbstractSqlFromPart {

	/**
	 * Liste des jointures 
	 */
	private List<AbstractSqlJoin> joins = new ArrayList<AbstractSqlJoin>();
	
	/**
	 * Alias de la partie du From
	 */
	private String alias ;
	
	/**
	 * Ajoute une jointure
	 *
	 * @param p_oSqlJoin jointure à ajouter
	 */
	public void addSqlJoin( AbstractSqlJoin p_oSqlJoin ) {
		this.joins.add( p_oSqlJoin );
	}
	
	/**
	 * Retourne l'alias
	 *
	 * @return alias du SqlFromPart
	 */
	public String getAlias() {
		return this.alias;
	}

	/**
	 * <p>joinToSql.</p>
	 *
	 * @param p_oSqlBuilder a {@link java.lang.StringBuilder} object.
	 * @param p_oSqlContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext} object.
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException if any.
	 */
	protected void joinToSql( StringBuilder p_oSqlBuilder, ToSqlContext p_oSqlContext) throws DaoException {
		for( AbstractSqlJoin oSqlJoin: this.joins ) {
			p_oSqlBuilder.append(' ');
			oSqlJoin.toSql(p_oSqlBuilder, p_oSqlContext);
		}
	}
	
	/**
	 * Génère le Sql du premier SqlFromPart
	 *
	 * @return sql du SqlFromPart
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException échec de construction du sql
	 * @param p_oSqlContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext} object.
	 */
	public abstract String toFirstSql(ToSqlContext p_oSqlContext) throws DaoException;

	/**
	 * Génère le Sql des SqlFromPart qui ne sont pas le premier
	 *
	 * @return sql du SqlFromPart
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException  échec de construction du sql
	 * @param p_oSqlContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext} object.
	 */
	public abstract List<String> toOtherSql(ToSqlContext p_oSqlContext) throws DaoException;
	
	/**
	 * Bind les valeurs du SqlFromPart
	 *
	 * @param p_oStatementBinder StatementBinder à utiliser pour le binding
	 * @throws java.sql.SQLException Echec du Binding
	 */
	public void bindValues(StatementBinder p_oStatementBinder) throws SQLException {
		for( AbstractSqlJoin oSqlJoin: this.joins ) {
			oSqlJoin.bindValues(p_oStatementBinder);
		}
	}
	
	/**
	 * Définit l'alias du SqlFromPart
	 *
	 * @param p_sAlias alias du SqlFromPart
	 */
	protected void setAlias(String p_sAlias) {
		this.alias = p_sAlias;
	}
	
	/**
	 * Retourne la liste des jointures
	 *
	 * @return liste des jointures
	 */
	protected List<AbstractSqlJoin> getJoins() {
		return this.joins;
	}

	/**
	 * Clone le SqlFromPart
	 *
	 * @return le clone du SqlFromPart
	 * @see java.lang.Object#clone()
	 */
	@Override
	public abstract AbstractSqlFromPart clone();
}
