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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>Représente la selection d'un champs dans un select</p>
 *
 * <p>Exemple : SELECT INTERVENTION1.ID, INTERVENTION1.CODE FROM MIT_INTERVENTION INTERVENTION1
 * <pre>
 * {@code
 * SqlQuery oQuery = new SqlQuery();
 * ...
 * oQuery.addToSelect( new SqlFieldSelectPart( InterventionField.ID, InterventionDao.ALIAS_NAME ));
 * oQuery.addToSelect( new SqlFieldSelectPart( InterventionField.CODE, InterventionDao.ALIAS_NAME ));
 * }
 * </pre>
 *
 * <p>Ou plus simplement :</p>
 * <pre>
 * {@code
 * SqlQuery oQuery = new SqlQuery();
 * ...
 * oQuery.addFieldToSelect( InterventionDao.ALIAS_NAME, InterventionField.ID, InterventionField.CODE );
 * }
 * </pre>
 *
 *
 * @since 2.5
 * @see AbstractSqlSelectPart
 */
public class SqlFieldSelectPart extends AbstractSqlSelectPart implements Cloneable {

	/**
	 * Nom du champs
	 */
	private SqlField sqlField ;
	
	/**
	 * Constructeur avec nom du champs, nom de l'alias
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_sTablePrefix prefix de la table
	 */
	public SqlFieldSelectPart(String p_sFieldName, String p_sTablePrefix) {
		this.sqlField = new SqlField(p_sFieldName, p_sTablePrefix);
	}
	
	/**
	 * Constructeur avec nom du champs, nom de l'alias
	 *
	 * @param p_sTablePrefix prefix de la table
	 * @param p_oField a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field} object.
	 */
	public SqlFieldSelectPart(Field p_oField, String p_sTablePrefix) {
		this.sqlField = new SqlField(p_oField, p_sTablePrefix);
	}
	
	/**
	 * Constructeur avec le champs
	 *
	 * @param p_oSqlField champs
	 */
	public SqlFieldSelectPart(SqlField p_oSqlField) {
		this.sqlField = p_oSqlField ;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Clone le SqlFieldSelectPart
	 */
	@Override
	public AbstractSqlSelectPart clone() {
		return new SqlFieldSelectPart(this.sqlField);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toSql( StringBuilder p_oStringBuilder, ToSqlContext p_oToSqlContext ) {
		this.sqlField.toSqlWithFieldAlias(p_oStringBuilder, p_oToSqlContext );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void computeContentResolverProjection(List<String> p_listColumns) {
		p_listColumns.add(this.sqlField.getName());
	}
}
