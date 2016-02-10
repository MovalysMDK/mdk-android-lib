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

import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlField;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlKeywords;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>Représente un count dans un Select</p>
 *
 * <p>Exemple : SELECT count( distinct INTERVENTION1.STATEID ) FROM MIT_INTERVENTION INTERVENTION1
 * <pre>
 * {@code
 * SqlQuery oQuery = new SqlQuery();
 * ...
 * SqlCountSelectPart oSqlCountSelectPart = new SqlCountSelectPart(true);
 * oSqlCountSelectPart.addField( InterventionDao.ALIAS_NAME, InterventionField.STATEID);
 * oSqlQuery.addToSelect( oSqlCountSelectPart);
 * }
 * </pre>
 *
 * <p>Exemple : SELECT count(*) FROM MIT_INTERVENTION
 * <pre>
 * {@code
 * SqlQuery oQuery = new SqlQuery();
 * oQuery.addCountToSelect();
 * }
 * </pre>
 *
 *
 * @since 2.5
 * @see AbstractSqlSelectPart
 */
public class SqlCountSelectPart extends AbstractSqlSelectPart implements Cloneable {
	
	/**
	 * Liste des champs sur lequel effectuer le count
	 */
	private List<SqlField> sqlFields = new ArrayList<SqlField>();
	
	/**
	 * Indique si le count est distinct ou pas
	 */
	private boolean distinct = false ;

	/**
	 * Constructeur vide
	 */
	public SqlCountSelectPart() {
		// nothing to do
	}
	
	/**
	 * Constructeur avec paramètre distinct
	 *
	 * @param p_bDistinct vrai si count( distinct..)
	 */
	public SqlCountSelectPart( boolean p_bDistinct ) {
		this.distinct = p_bDistinct ;
	}
	
	/**
	 * Constructeur avec distinct et liste des champs
	 *
	 * @param p_bDistinct vrai si count( distinct..)
	 * @param p_listSqlFields liste des champs sur lequel faire le count
	 */
	public SqlCountSelectPart( boolean p_bDistinct, SqlField...p_listSqlFields ) {
		this.distinct = p_bDistinct ;
		for( SqlField oSqlField: p_listSqlFields ) {
			this.sqlFields.add(oSqlField);
		}
	}
	
	/**
	 * Ajoute des champs au count
	 *
	 * @param p_sAlias alias des tables des champs
	 * @param p_sFieldNames liste des noms des champs
	 */
	public void addFields( String p_sAlias, String...p_sFieldNames ) {
		for( String sFieldName: p_sFieldNames) {
			this.sqlFields.add( new SqlField(sFieldName, p_sAlias ));
		}
	}
	
	/**
	 * Ajoute des champs au count
	 *
	 * @param p_sAlias alias de la table des champs
	 * @param p_listFields liste des champs
	 */
	public void addFields(String p_sAlias, Field... p_listFields) {
		for( Field oField : p_listFields ) {
			this.sqlFields.add( new SqlField(oField.name(), p_sAlias ));
		}
	}
	
	/**
	 * Ajoute un champs au count
	 *
	 * @param p_oSqlField champs à ajouter
	 */
	public void addField( SqlField p_oSqlField ) {
		this.sqlFields.add(p_oSqlField);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Clone le SqlCountSelectPart
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select.AbstractSqlSelectPart#clone()
	 */
	@Override
	public AbstractSqlSelectPart clone() {
		SqlCountSelectPart r_oSqlCountSelectPart = new SqlCountSelectPart(this.distinct);
		for( SqlField oSqlField: this.sqlFields) {
			r_oSqlCountSelectPart.addField(oSqlField);
		}
		return r_oSqlCountSelectPart;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select.AbstractSqlSelectPart#toSql(java.lang.StringBuilder)
	 */
	@Override
	public void toSql( StringBuilder p_oStringBuilder, ToSqlContext p_oToSqlContext ) {
		p_oStringBuilder.append(SqlKeywords.COUNT);
		p_oStringBuilder.append('(');
		if ( this.distinct ) {
			p_oStringBuilder.append('(');
		}	
		boolean bFirst = true ;
		if ( !this.sqlFields.isEmpty()) {
			for( SqlField oSqlField: this.sqlFields) {
				if ( bFirst ) {
					bFirst = false ;
				} else {
					p_oStringBuilder.append(',');
				}
				oSqlField.toSql(p_oStringBuilder, p_oToSqlContext);
			}
		}
		else {
			p_oStringBuilder.append('*'); 
		}
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
