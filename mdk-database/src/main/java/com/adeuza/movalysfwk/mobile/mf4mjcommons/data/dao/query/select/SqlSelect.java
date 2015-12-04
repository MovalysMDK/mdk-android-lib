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
 * <p>Représente le Select d'une requête</p>
 * <p>Un SqlSelect est une liste de {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select.AbstractSqlSelectPart AbstractSqlSelectPart} qui peuvent être :
 * <ul>
 * 	<li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select.SqlFieldSelectPart SqlFieldSelectPart} : sélection d'un champs</li>
 *  <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select.SqlCountSelectPart SqlCountSelectPart} : comptage</li>
 *  <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select.SqlFunctionSelectPart SqlFunctionSelectPart} : utilisation d'une fonction SQL (Max, Min, Sum)</li>
 *  <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select.SqlGenericSelectPart SqlGenericSelectPart} : libre</li>
 * </ul>
 *
 * @since 2.5
 */
public class SqlSelect implements Cloneable {
	
	/**
	 * Liste du partie du Select
	 */
	private List<AbstractSqlSelectPart> listSelectParts = new ArrayList<AbstractSqlSelectPart>();
		
	/**
	 * Vrai si select avec distinct
	 */
	private boolean distinct = false ;
	
	/**
	 * Ajoute des champs au Select
	 *
	 * @param p_sAlias alias de la table des champs
	 * @param p_sFieldNames nom des champs
	 */
	public void addFields( String p_sAlias, String...p_sFieldNames ) {
		for( String sFieldName: p_sFieldNames) {
			this.listSelectParts.add( new SqlFieldSelectPart(sFieldName, p_sAlias ));
		}
	}
	
	/**
	 * Ajoute des champs au select
	 *
	 * @param p_sAlias alias de la table des champs
	 * @param p_listFields nom des champs
	 */
	public void addFields(String p_sAlias, Field... p_listFields) {
		for( Field oField : p_listFields ) {
			this.listSelectParts.add( new SqlFieldSelectPart(oField, p_sAlias));
		}
	}
	
	/**
	 * Ajoute une SelectPart au Select
	 *
	 * @param p_oSqlSelectPart SqlSelectPart à ajouter
	 */
	public void addSelectPart( AbstractSqlSelectPart p_oSqlSelectPart ) {
		this.listSelectParts.add(p_oSqlSelectPart);
	}
	
	/**
	 * Définit si le select est distinct
	 *
	 * @param p_bDistinct vrai si le select est distinct
	 */
	public void setDistinct(boolean p_bDistinct) {
		this.distinct = p_bDistinct;
	}

	/**
	 * Ajoute un champs au Select
	 *
	 * @param p_oField le champs à ajouter
	 */
	public void addField( SqlField p_oField ) {
		this.listSelectParts.add( new SqlFieldSelectPart(p_oField));
	}
	
	/**
	 * Génère le sql du Select
	 *
	 * @return sql du select
	 * @param p_oToSqlContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext} object.
	 */
	public String toSql( ToSqlContext p_oToSqlContext ) {
		StringBuilder r_sSelect = new StringBuilder(SqlKeywords.SELECT.toString());
		r_sSelect.append(' ');
		if ( distinct ) {
			r_sSelect.append(' ');
			r_sSelect.append(SqlKeywords.DISTINCT);
			r_sSelect.append(' ');
		}
		boolean bFirst = true ;
		for( AbstractSqlSelectPart oSqlSelectPart : this.listSelectParts ) {
			if ( bFirst ) {
				bFirst = false ;
			} else {
				r_sSelect.append(", ");
			}
			oSqlSelectPart.toSql(r_sSelect, p_oToSqlContext );
		}
		return r_sSelect.toString();
	}
	
	/**
	 * Compute Content Resolver projection
	 * @return projection for ContentResolver
	 */
	public String[] computeContentResolverProjection() {
		
		List<String> listColumns = new ArrayList<>();
		for( AbstractSqlSelectPart oSqlSelectPart : this.listSelectParts ) {
			oSqlSelectPart.computeContentResolverProjection(listColumns);
		}
		String[] r_t_listColumns = new String[listColumns.size()];
		listColumns.toArray(r_t_listColumns);
		return r_t_listColumns;
	}
	
	/**
	 * Clone le select
	 *
	 * @return clone du Select
	 * @see java.lang.Object#clone()
	 */
	@Override
	public SqlSelect clone() {
		SqlSelect r_oSqlSelect = new SqlSelect();
		r_oSqlSelect.setDistinct(this.distinct);
		for( AbstractSqlSelectPart oSqlSelectPart : this.listSelectParts ) {
			r_oSqlSelect.addSelectPart(oSqlSelectPart.clone());
		}
		return r_oSqlSelect ;
	}
}
