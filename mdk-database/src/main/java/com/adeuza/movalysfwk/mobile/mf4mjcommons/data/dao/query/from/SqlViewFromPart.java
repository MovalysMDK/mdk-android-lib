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

import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.joins.AbstractSqlJoin;

/**
 * <p>Partie From de type View</p>
 *
 *
 * <p>Exemple :</p>
 * <p>Pour le Sql suivant :</p>
 * <pre>
 * {@code
 * SELECT intervention1.code
 * FROM v__inters }</pre>
 *
 * <p>Le code java correspondant est le suivant : </p>
 * <pre>
 * {@code
 * SqlQuery oSqlSelect = new SqlQuery();
 * oSqlSelect.addFieldToSelect(InterventionDao.ALIAS_NAME, InterventionField.CODE);
 * oSqlSelect.addToFrom( new SqlViewFromPart("v__inters", InterventionDao.ALIAS_NAME)); }</pre>
 *
 * @since 2.5
 * @see AbstractSqlFromPart
 */
public class SqlViewFromPart extends AbstractSqlFromPart implements Cloneable {

	/**
	 * Nom de la table
	 */
	private String viewName ;
	
	/**
	 * Constructeur avec nom de la vue
	 *
	 * @param p_sViewName nom de la vue
	 */
	public SqlViewFromPart(String p_sViewName) {
		this.viewName = p_sViewName ;
	}
	
	/**
	 * Constructeur avec nom de la table, alias de la table
	 *
	 * @param p_sViewName nom de la vue
	 * @param p_sAliasName alias de la table
	 */
	public SqlViewFromPart(String p_sViewName, String p_sAliasName) {
		this.viewName = p_sViewName ;
		setAlias(p_sAliasName);
	}
	
	/** {@inheritDoc} */
	@Override
	public AbstractSqlFromPart clone() {
		AbstractSqlFromPart r_oSqlFromPart = new SqlViewFromPart(this.viewName,this.getAlias());
		for( AbstractSqlJoin oJoin : this.getJoins()) {
			r_oSqlFromPart.addSqlJoin(oJoin.clone());
		}
		return r_oSqlFromPart ;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.from.AbstractSqlFromPart#toFirstSql(com.adeuza.movalysfwk.mobile.mf4javacommons.core.historisation.map.QueryHistorisationBlocMap)
	 */
	@Override
	public String toFirstSql( ToSqlContext p_oToSqlContext) throws DaoException {

		StringBuilder sBuilder = new StringBuilder(this.viewName);
		if ( this.getAlias() != null ) {
			sBuilder.append(' ');
			sBuilder.append(this.getAlias());
		}
		this.joinToSql(sBuilder, p_oToSqlContext);

		return sBuilder.toString();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.from.AbstractSqlFromPart#toOtherSql(com.adeuza.movalysfwk.mobile.mf4javacommons.core.historisation.map.QueryHistorisationBlocMap)
	 */
	@Override
	public List<String> toOtherSql( ToSqlContext p_oToSqlContext) throws DaoException {
		throw new DaoException(" La méthode toOtherSql n'est pas supporté pour SqlViewFromPart.");
	}
}
