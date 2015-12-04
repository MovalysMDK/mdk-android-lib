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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query;

import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.contentprovider.ContentResolverQuery;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.GroupBy;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Limit;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.OrderSet;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.SqlType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlClauseLinks;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlEqualsValueCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlInValueCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlLikeCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.from.AbstractSqlFromPart;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.from.SqlFrom;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.from.SqlTableFromPart;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.from.SqlViewFromPart;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select.AbstractSqlSelectPart;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select.SqlCountSelectPart;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select.SqlFunctionSelectPart;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select.SqlSelect;

/**
 * <p>Classe pour la construction d'un Select</p>
 *
 * <p>Le SqlQuery est composé en 3 parties : </p>
 * <ul>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.select.SqlSelect SqlSelect} pour la sélection des champs</li>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.from.SqlFrom SqlFrom} pour la construction du from et des jointures</li>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlWhere SqlWhere} pour la construction des critères de sélection</li>
 * </ul>
 *
 * <p>Exemple de création d'une requête from scratch :</p>
 * <pre>
 * {@code
 * SelectQuery oQuery = new SqlQuery();
 * oQuery.addFieldToSelect(InterventionDao.ALIAS_NAME, InterventionField.ID, InterventionField.CODE);
 * oQuery.addFromPart(InterventionDao.TABLE_NAME, InterventionDao.ALIAS_NAME);
 * oQuery.addEqualsConditionToWhere(InterventionField.REQUESTID, InterventionDao.ALIAS_NAME, 1, SqlType.INTEGER);
 * oQuery.setOrderBy(OrderSet.of(OrderAsc.of(InterventionField.CODE, InterventionDao.ALIAS_NAME)));
 * }
 * </pre>
 */
public class SqlQuery implements Cloneable {
	
	/**
	 * default fetch size
	 */
	private static final int DEFAULT_FETCH_SIZE = 10;

	/**
	 * Select du query 
	 */
	private SqlSelect sqlSelect = new SqlSelect();
	
	/**
	 * From du query
	 */
	private SqlFrom sqlFrom = new SqlFrom();
	
	/**
	 * Where du query
	 */
	private SqlWhere sqlWhere = new SqlWhere();

	/**
	 * Group By du query
	 */

	private GroupBy groupBy;

	/**
	 * Order By du query
	 */
	private OrderSet orderBy ;

	/**
	 * Limit
	 */
	private Limit limit;
	
	/**
	 * Nombre d'union dans la requête
	 */
	private int nbUnion = 0;
	
	/**
	 * Fetch Size
	 */
	private int fetchSize = DEFAULT_FETCH_SIZE ;
	


	/**
	 * Constructeur vide
	 */
	public SqlQuery() {
		this.groupBy = GroupBy.NONE ;
		this.orderBy = OrderSet.NONE ;
	}

	/**
	 * Définit le tri du select
	 *
	 * @param p_oGroupBy OrderSet représentant le tri
	 */
	public void setGroupBy(GroupBy p_oGroupBy) {
		this.groupBy = p_oGroupBy;
	}

	/**
	 * Cree une clause "Having" Une première condition est obligatoire
	 *
	 * @param p_oSqlCondition condition à rajouter
	 */
	public void setHavingToGroupBy(AbstractSqlCondition p_oSqlCondition) {
		this.groupBy.setHavingToGroupBy(p_oSqlCondition);
	}
	
	/**
	 * Ajoute une condition au Having
	 *
	 * @param p_oSqlCondition condition à rajouter
	 */
	public void addToHavingOfGroupBy( AbstractSqlCondition p_oSqlCondition) {
		this.groupBy.addToHavingOfGroupBy(p_oSqlCondition);
	}
	
	/**
	 * Définit le tri du select
	 *
	 * @param p_oOrderBy OrderSet représentant le tri
	 */
	public void setOrderBy(OrderSet p_oOrderBy) {
		this.orderBy = p_oOrderBy;
	}

	/**
	 * Defines the limit parameter to apply to the query
	 *
	 * @param p_oLimit the limit to set
	 */
	public void setLimit(Limit p_oLimit) {
		this.limit = p_oLimit;
	}

	/**
	 * Ajoute un ou plusieurs champs dans la partie Select
	 *
	 * @param p_sAlias alias de la table des champs ajoutés
	 * @param p_sField la liste des champs à ajouter
	 */
	public void addFieldToSelect(String p_sAlias, String... p_sField ) {
		this.sqlSelect.addFields(p_sAlias, p_sField);
	}
	
	/**
	 * Ajoute un ou plusieurs champs dans la partie Select
	 *
	 * @param p_sAlias alias de la table des champs ajoutés
	 * @param p_listFields la liste des champs à ajouter
	 */
	public void addFieldToSelect(String p_sAlias, Field... p_listFields ) {
		for( Field oField : p_listFields ) {
			this.sqlSelect.addFields(p_sAlias, oField );
		}
	}
	
	/**
	 * Ajoute un count(*) au select
	 */
	public void addCountToSelect() {
		this.sqlSelect.addSelectPart(new SqlCountSelectPart(false));
	}

	/**
	 * Ajoute un count([alias.]fieldName) au select
	 *
	 * @param p_oField champs à compter
	 * @param p_sAlias alias de la table du champs
	 */
	public void addCountToSelect( Field p_oField, String p_sAlias ) {
		this.sqlSelect.addSelectPart(new SqlCountSelectPart(false, new SqlField(p_oField, p_sAlias)));
	}	
	
	/**
	 * Ajoute un count([distinct] alias.fieldName) au select
	 *
	 * @param p_oField champs à compter
	 * @param p_bDistinct compte distinct ou non
	 */
	public void addCountToSelect( SqlField p_oField, boolean p_bDistinct ) {
		this.sqlSelect.addSelectPart(new SqlCountSelectPart(p_bDistinct, p_oField));
	}
	
	/**
	 * Ajoute une fonction dans le select
	 *
	 * @param p_oSqlFunctionSelectPart la function à ajouter
	 */
	public void addFunctionToSelect( SqlFunctionSelectPart p_oSqlFunctionSelectPart ) {
		this.sqlSelect.addSelectPart(p_oSqlFunctionSelectPart);
	}
	
	/**
	 * Ajoute une partie dans le select
	 *
	 * @param p_oSqlSelectPart SqlSelectPart à rajouter au select
	 */
	public void addToSelect( AbstractSqlSelectPart p_oSqlSelectPart ) {
		this.sqlSelect.addSelectPart(p_oSqlSelectPart);
	}
	
	/**
	 * (Dés)Active le distinct sur le select
	 *
	 * @param p_bDistinct true pour avoir le distinct sur le select
	 */
	public void setSelectDistinct( boolean p_bDistinct ) {
		this.sqlSelect.setDistinct(true);
	}
	
	/**
	 * Ajoute une table dans le From
	 *
	 * @param p_sTableName nom de la table
	 * @param p_sAlias alias de la table
	 */
	public void addToFrom( String p_sTableName, String p_sAlias ) {
		this.sqlFrom.addFromPart(new SqlTableFromPart(p_sTableName, p_sAlias));
	}
	
	/**
	 * Ajoute une vue dans le From
	 *
	 * @param p_sViewName nom de la vue
	 * @param p_sAlias alias de la vue
	 */
	public void addViewToFrom( String p_sViewName, String p_sAlias ) {
		this.sqlFrom.addFromPart(new SqlViewFromPart(p_sViewName, p_sAlias));
	}
	
	/**
	 * Complete le From
	 *
	 * @param p_oFromPart partie à ajouter au From
	 */
	public void addToFrom( AbstractSqlFromPart p_oFromPart ) {
		this.sqlFrom.addFromPart(p_oFromPart);
	}
	
	/**
	 * Renvoie la premiere partie du from
	 *
	 * @return la premiere partie du from
	 */
	public AbstractSqlFromPart getFirstFromPart() {
		return this.sqlFrom.getFirstFromPart();
	}
	
	/**
	 * Renvoie la partie du from en position i
	 *
	 * @param p_iIndex index de la partie du from à retourner
	 * @return Renvoie la partie du from en position i
	 */
	public AbstractSqlFromPart getFromPart( int p_iIndex ) {
		return this.sqlFrom.getSqlFromPart(p_iIndex);
	}

	/**
	 * Ajoute une condition d'égalité au Where
	 *
	 * @param p_oField nom du champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_oObjectValue valeur du champs
	 * @param p_iValueSqlType type du champs
	 */
	public void addEqualsConditionToWhere( Field p_oField, String p_sAlias, Object p_oObjectValue, @SqlType int p_iValueSqlType ) {
		this.sqlWhere.addSqlCondition(
			new SqlEqualsValueCondition( new SqlField(p_oField,p_sAlias), new SqlBindValue(p_oObjectValue, p_iValueSqlType)));
	}
		
	/**
	 * Ajoute une condition d'égalité au Where
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_oObjectValue valeur du champs
	 * @param p_iValueSqlType type du champs
	 */
	public void addEqualsConditionToWhere( String p_sFieldName, String p_sAlias, Object p_oObjectValue, @SqlType int p_iValueSqlType ) {
		this.sqlWhere.addSqlCondition(
			new SqlEqualsValueCondition( new SqlField(p_sFieldName,p_sAlias), new SqlBindValue(p_oObjectValue, p_iValueSqlType)));
	}

	/**
	 * Ajoute une condition d'égalité au Where
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_oObjectValue valeur du champs
	 * @param p_oValueSqlType type du champs
	 */
	public void addEqualsConditionToWhere( String p_sFieldName, Object p_oObjectValue, @SqlType int p_iValueSqlType ) {
		this.sqlWhere.addSqlCondition(
			new SqlEqualsValueCondition( new SqlField(p_sFieldName), new SqlBindValue(p_oObjectValue, p_iValueSqlType)));
	}
	
	/**
	 * Ajoute une condition Like au Where
	 *
	 * @param p_oField nom du champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_oObjectValue valeur du champs
	 * @param p_iValueSqlType type du champs
	 */
	public void addLikeConditionToWhere( Field p_oField, String p_sAlias, Object p_oObjectValue, @SqlType int p_iValueSqlType ) {
		this.sqlWhere.addSqlCondition(
			new SqlLikeCondition( new SqlField(p_oField,p_sAlias), new SqlBindValue(p_oObjectValue, p_iValueSqlType)));
	}
		
	/**
	 * Ajoute une condition Like au Where
	 *
	 * @param p_oField nom du champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_sStringValue valeur du champs
	 * @param p_iValueSqlType type du champs
	 */
	public void addLikeConditionToWhere( Field p_oField, String p_sAlias, String p_sStringValue, @SqlType int p_iValueSqlType ) {
		this.sqlWhere.addSqlCondition(
			new SqlLikeCondition( p_oField, p_sAlias, p_sStringValue, p_iValueSqlType));
	}

	/**
	 * Ajoute une condition Like au Where
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_oObjectValue valeur du champs
	 * @param p_iValueSqlType type du champs
	 */
	public void addLikeConditionToWhere( String p_sFieldName, Object p_oObjectValue, @SqlType int p_iValueSqlType ) {
		this.sqlWhere.addSqlCondition(
			new SqlLikeCondition( new SqlField(p_sFieldName), new SqlBindValue(p_oObjectValue, p_iValueSqlType)));
	}
	
	/**
	 * Ajoute une condition Like au Where
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_sStringValue valeur du champs
	 * @param p_iValueSqlType type du champs
	 */
	public void addLikeConditionToWhere( String p_sFieldName, String p_sStringValue, @SqlType int p_oValueSqlType ) {
		this.sqlWhere.addSqlCondition(new SqlLikeCondition( p_sFieldName, p_sStringValue, p_oValueSqlType));
	}

	/**
	 * Ajoute une condition Like au Where
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_oObjectValue valeur du champs
	 * @param p_iValueSqlType type du champs
	 */
	public void addLikeConditionToWhere( String p_sFieldName, String p_sAlias, Object p_oObjectValue, @SqlType int p_iValueSqlType ) {
		this.sqlWhere.addSqlCondition(
			new SqlLikeCondition( new SqlField(p_sFieldName,p_sAlias), new SqlBindValue(p_oObjectValue, p_iValueSqlType)));
	}
	
	/**
	 * Ajoute une condition Like au Where
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_sStringValue valeur du champs
	 * @param p_iValueSqlType type du champs
	 */
	public void addLikeConditionToWhere( String p_sFieldName, String p_sAlias, String p_sStringValue, @SqlType int p_iValueSqlType ) {
		this.sqlWhere.addSqlCondition(new SqlLikeCondition(p_sFieldName, p_sAlias, p_sStringValue, p_iValueSqlType));
	}

	/**
	 * Ajoute une condition InValue au Where
	 *
	 * @param p_oField champs du IN
	 * @param p_sAlias alias de la table du champs
	 * @param p_iSqlType type du champs
	 * @param p_listInValues liste des valeurs du IN
	 */
	public void addInValueConditionToWhere( Field p_oField, String p_sAlias, @SqlType int p_iSqlType, List<?> p_listInValues ) {
		this.sqlWhere.addSqlCondition(new SqlInValueCondition( p_oField, p_sAlias, p_iSqlType, p_listInValues ));
	}
	
	/**
	 * Ajoute une condition au Where
	 *
	 * @param p_oSqlCondition condition à rajouter
	 */
	public void addToWhere( AbstractSqlCondition p_oSqlCondition) {
		this.sqlWhere.addSqlCondition(p_oSqlCondition);
	}
	
	/**
	 * 
	 */
	public void and() {
		this.sqlWhere.addSqlCondition(SqlClauseLinks.AND);
	}
	
	/**
	 * 
	 */
	public void or() {
		this.sqlWhere.addSqlCondition(SqlClauseLinks.OR);
	}
	
	/**
	 * Génère la requete au format SQL
	 *
	 * @param p_oToSqlContext sql context
	 * @return chaine de la requête SQL
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException echec de creation de la requete
	 */
	public String toSql( ToSqlContext p_oToSqlContext ) throws DaoException {
		StringBuilder sBuilder = new StringBuilder();
		
		String sSelect = this.sqlSelect.toSql(p_oToSqlContext);
		StringBuilder sAfterFromBuilder = new StringBuilder();
		this.sqlWhere.toSql(sAfterFromBuilder, p_oToSqlContext);
		
		this.nbUnion = 0;
		boolean bIsFirst = true;
		for (StringBuilder oStringBuilder : this.sqlFrom.toSql(p_oToSqlContext)) {
			this.nbUnion = this.nbUnion +1;
			if(bIsFirst){
				bIsFirst = false;
			}else{
				sBuilder.append(' ').append(SqlKeywords.UNION_ALL).append(' ');
			}
			sBuilder.append(sSelect);
			sBuilder.append(oStringBuilder);
			sBuilder.append(sAfterFromBuilder);
		}

		if ( this.groupBy != null) {
			this.groupBy.appendToSql(sBuilder, p_oToSqlContext);
		}
		
		if ( this.orderBy != null ) {
			this.orderBy.appendToSql(sBuilder, p_oToSqlContext);
		}
		
		if (this.limit != null) {
			this.limit.appendToSql(sBuilder, p_oToSqlContext);
		}
		
		return sBuilder.toString();
	}
	
	/**
	 * Bind les valeurs de la requête
	 *
	 * @param p_oStatementBinder StatementBinder à utiliser pour le binding
	 * @throws DaoException échec du binding
	 */
	public void bindValues( StatementBinder p_oStatementBinder ) throws DaoException {
		for (int i = 1; i <= this.nbUnion; i++) {
			this.sqlFrom.bindValues(p_oStatementBinder);
			this.sqlWhere.bindValues(p_oStatementBinder);
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Clone la requête SQL
	 * @see java.lang.Object#clone()
	 */
	@Override
	public SqlQuery clone() {
		SqlQuery r_oSqlQuery = new SqlQuery();
		r_oSqlQuery.setSelect(this.sqlSelect.clone());
		r_oSqlQuery.setFrom(this.sqlFrom.clone());
		r_oSqlQuery.setWhere(this.sqlWhere.clone());
		r_oSqlQuery.setOrderBy( this.orderBy.clone());
		return r_oSqlQuery ;
	}
	
	/**
	 * Définit la partie Select de la requête
	 * Méthode private car utilisée uniquement par le clone 
	 * @param p_oSqlSelect nouveau select de la requête
	 */
	private void setSelect( SqlSelect p_oSqlSelect ) {
		this.sqlSelect = p_oSqlSelect ;
	}
	
	/**
	 * Définit la partie From de la requête
	 * Méthode private car utilisée uniquement par le clone
	 * @param p_oSqlFrom nouveau From de la requête
	 */
	private void setFrom( SqlFrom p_oSqlFrom ) {
		this.sqlFrom = p_oSqlFrom ;
	}
	
	/**
	 * Définit la partie Where de la requête
	 * Méthode private car utilisée uniquement par le clone
	 * @param p_oSqlWhere nouveau Where de la requête
	 */
	private void setWhere( SqlWhere p_oSqlWhere ) {
		this.sqlWhere = p_oSqlWhere ;
	}

	/**
	 * Retourne l'objet fetchSize
	 *
	 * @return Objet fetchSize
	 */
	public int getFetchSize() {
		return this.fetchSize;
	}

	/**
	 * Affecte l'objet fetchSize
	 *
	 * @param p_iFetchSize Objet fetchSize
	 */
	public void setFetchSize(int p_iFetchSize) {
		this.fetchSize = p_iFetchSize;
	}

	/**
	 * Compute content resolver query
	 * @return content resolver query
	 */
	public ContentResolverQuery toContentResolverQuery() {
		
		if ( this.sqlFrom.countFromParts() != 1 ) {
			throw new UnsupportedOperationException("Only SqlQuery instances with one SqlFromPart, can be converted to ContentResolverQuery ");
		}
		
		ContentResolverQuery r_oContentResolverQuery = new ContentResolverQuery();
		r_oContentResolverQuery.setProjection( this.sqlSelect.computeContentResolverProjection());
		r_oContentResolverQuery.setSelection(this.sqlWhere.computeContentResolverSelection());
		r_oContentResolverQuery.setSelectionArgs(this.sqlWhere.computeContentResolverSelectionArgs());
		r_oContentResolverQuery.setOrder( this.orderBy.computeContentResolverOrderBy());
		
		return r_oContentResolverQuery;
	}
}
