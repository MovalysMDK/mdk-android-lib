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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.joins;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlKeywords;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition;

/**
 * <p>Représente un left outer join avec une table</p>
 *
 * <p>Exemple : <p>
 * <p>Pour la requête sql suivante :</p>
 * <pre>
 * {@code
 * SELECT request1.CODE, intervention1.CODE
 * FROM MIT_REQUEST request1
 * LEFT OUTER JOIN MIT_INTERVENTION intervention1
 * ON intervention1.requestid = request1.id ;
 * }</pre>
 * <p>Le code Java correspondant est : </p>
 * <pre>
 * {@code
 * SqlQuery oSqlSelect = new SqlQuery();
 * oSqlSelect.addFieldToSelect(RequestDao.ALIAS_NAME, RequestDao.RequestField.CODE);
 * oSqlSelect.addFieldToSelect(InterventionDao.ALIAS_NAME, InterventionDao.InterventionField.CODE);
 * oSqlSelect.addToFrom(RequestDao.TABLE_NAME, RequestDao.ALIAS_NAME);
 * oSqlSelect.getFirstFromPart().addSqlJoin(
 * new SqlTableLeftOuterJoin( InterventionDao.TABLE_NAME, InterventionDao.ALIAS_NAME, RequestDao.TABLE_NAME,
 *   new SqlEqualsFieldCondition(InterventionDao.InterventionField.REQUESTID, InterventionDao.ALIAS_NAME,
 *   RequestDao.RequestField.ID, RequestDao.ALIAS_NAME)));
 * }</pre>
 *
 *
 * @since 2.5
 * @see AbstractSqlJoin
 */
public class SqlTableLeftOuterJoin extends AbstractSqlTableJoin implements Cloneable {
	
	/**
	 * Nom de la table 
	 */
	private String tableName ;
	
	/**
	 * Constructeur avec nom de la table, alias, liste des conditions de jointures
	 *
	 * @param p_sTableName nom de la table
	 * @param p_sAlias alias de la table
	 * @param p_oSqlConditions conditions de jointure
	 * @param p_sMasterTableName a {@link java.lang.String} object.
	 */
	public SqlTableLeftOuterJoin( String p_sTableName, String p_sAlias, String p_sMasterTableName, AbstractSqlCondition... p_oSqlConditions ) {
		this.tableName = p_sTableName ;
		setAlias( p_sAlias );
		this.setMasterTableName(p_sMasterTableName);
		for( AbstractSqlCondition oSqlCondition: p_oSqlConditions) {
			this.addOnCondition(oSqlCondition);
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Retourne le type de jointure
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.joins.AbstractSqlJoin#getJoinType()
	 */
	@Override
	public String getJoinType() {
		return SqlKeywords.LEFT_OUTER_JOIN.toString();
	}

	/**
	 * {@inheritDoc}
	 *
	 * Genere le Sql de la partie jointure (sans les conditions)
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.joins.AbstractSqlJoin#getJoinWith(QueryHistorisationBlocMap)
	 */
	@Override
	public String getJoinWith(ToSqlContext p_oToSqlContext) {
		return this.tableName;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Clone du SqlTableLeftOuterJoin
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.joins.AbstractSqlJoin#clone()
	 */
	@Override
	public AbstractSqlJoin clone() {
		AbstractSqlJoin r_oSqlJoin = new SqlTableLeftOuterJoin(this.tableName, this.getAlias(), this.getMasterTableName());
		for( AbstractSqlCondition oCondition : this.getConditions()) {
			r_oSqlJoin.addOnCondition(oCondition.clone());
		}
		
		return r_oSqlJoin ;
	}
}
