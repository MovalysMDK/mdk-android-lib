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

import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLitePreparedStatement;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.SqlType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlEqualsValueCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlLikeCondition;

/**
 * <p>Requête de suppression</p>
 * <p>Le SqlDelete est construit à partir : </p>
 * <ul>
 *   <li>Du nom de la table dans laquelle faire la suppression.</li>
 *   <li>D'un {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlWhere SqlWhere} pour la sélection des lignes à supprimer.</li>
 * </ul>
 *
 * <p>Exemple de création d'une requête d'update :</p>
 * <pre>
 * {@code
 * SqlDelete oDeleteQuery = new SqlDelete(InterventionDao.TABLE_NAME);
 * SqlEqualsValueCondition oSqlEqualsValueCondition = oDeleteQuery.addEqualsConditionToWhere(InterventionField.ID, SqlType.INTEGER);
 * oSqlEqualsValueCondition.setValue(12);
 * }
 * </pre>
 */
public class SqlDelete implements Cloneable {

	/**
	 * Nom de la table pour la suppression
	 */
	private String tableName ;
	
	/**
	 * Where du delete
	 */
	private SqlWhere sqlWhere ;

	/**
	 * Constructeur vide
	 */
	public SqlDelete() {
		// nothing to do
	}

	/**
	 * Instancie un SqlDelete avec le nom de la table
	 *
	 * @param p_sTableName nom de la table
	 */
	public SqlDelete( String p_sTableName ) {
		this.tableName = p_sTableName ;
		this.sqlWhere = new SqlWhere();
	}

	/**
	 * Définit le nom de la table
	 *
	 * @param p_sTableName nom de la table
	 */
	public void setTableName(String p_sTableName) {
		this.tableName = p_sTableName;
	}

	/**
	 * Définit le where du delete
	 * En private car utilisé uniquement par le Clone
	 * @param p_oSqlWhere nouveau where du delete
	 */
	private void setWhere(SqlWhere p_oSqlWhere) {
		this.sqlWhere = p_oSqlWhere;
	}
	
	/**
	 * Ajoute une condition d'égalité au Where
	 *
	 * @param p_oField champs
	 * @param p_oObjectValue valeur du champs
	 * @param p_iValueSqlType type du champs
	 * @return retourne la condition ajoutée
	 */
	public SqlEqualsValueCondition addEqualsConditionToWhere( Field p_oField, Object p_oObjectValue, @SqlType int p_iValueSqlType ) {
		SqlEqualsValueCondition r_oSqlEqualsValueCondition =
			new SqlEqualsValueCondition( new SqlField(p_oField), new SqlBindValue(p_oObjectValue, p_iValueSqlType));
		this.sqlWhere.addSqlCondition(r_oSqlEqualsValueCondition);
		return r_oSqlEqualsValueCondition ;
	}
	
	/**
	 * Ajoute une condition d'égalité au Where
	 *
	 * @param p_oField champs
	 * @param p_iValueSqlType type du champs
	 * @return retourne la condition ajoutée
	 */
	public SqlEqualsValueCondition addEqualsConditionToWhere( Field p_oField, @SqlType int p_iValueSqlType ) {
		SqlEqualsValueCondition r_oSqlEqualsValueCondition =
			new SqlEqualsValueCondition( new SqlField(p_oField), new SqlBindValue(null, p_iValueSqlType));
		this.sqlWhere.addSqlCondition(r_oSqlEqualsValueCondition);
		return r_oSqlEqualsValueCondition ;
	}
	
	/**
	 * Ajoute une condition d'égalité au Where
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_oObjectValue valeur du champs
	 * @param p_iValueSqlType type du champs
	 * @return retourne la condition ajoutée
	 */
	public SqlEqualsValueCondition addEqualsConditionToWhere( String p_sFieldName, Object p_oObjectValue, @SqlType int p_iValueSqlType ) {
		SqlEqualsValueCondition r_oSqlEqualsValueCondition =
			new SqlEqualsValueCondition( new SqlField(p_sFieldName), new SqlBindValue(p_oObjectValue, p_iValueSqlType));
		this.sqlWhere.addSqlCondition(r_oSqlEqualsValueCondition);
		return r_oSqlEqualsValueCondition ;
	}
	
	/**
	 * Ajoute une condition d'égalité au Where
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_iValueSqlType type du champs
	 * @return retourne la condition ajoutée
	 */
	public SqlEqualsValueCondition addEqualsConditionToWhere( String p_sFieldName, @SqlType int p_iValueSqlType ) {
		SqlEqualsValueCondition r_oSqlEqualsValueCondition =
			new SqlEqualsValueCondition( new SqlField(p_sFieldName), new SqlBindValue(null, p_iValueSqlType));
		this.sqlWhere.addSqlCondition(r_oSqlEqualsValueCondition);
		return r_oSqlEqualsValueCondition ;
	}
	
	/**
	 * Ajoute une condition Like au Where
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_oObjectValue valeur du champs
	 * @param p_iValueSqlType type du champs
	 * @return retourne la condition ajoutée
	 */
	public SqlLikeCondition addLikeConditionToWhere( String p_sFieldName, Object p_oObjectValue, @SqlType int p_iValueSqlType ) {
		SqlLikeCondition r_oSqlLikeCondition = new SqlLikeCondition( new SqlField(p_sFieldName), new SqlBindValue(p_oObjectValue, p_iValueSqlType));
		this.sqlWhere.addSqlCondition(r_oSqlLikeCondition);
		return r_oSqlLikeCondition ;
	}
	
	/**
	 * Ajoute une condition Like au Where
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_iValueSqlType type du champs
	 * @return retourne la condition ajoutée
	 */
	public SqlLikeCondition addLikeConditionToWhere( String p_sFieldName, @SqlType int p_iValueSqlType ) {
		SqlLikeCondition r_oSqlLikeCondition = new SqlLikeCondition( new SqlField(p_sFieldName), new SqlBindValue(null, p_iValueSqlType));
		this.sqlWhere.addSqlCondition(r_oSqlLikeCondition);
		return r_oSqlLikeCondition ;
	}

	/**
	 * Ajoute une condition au Where
	 *
	 * @param p_oSqlCondition condition à ajouter
	 */
	public void addToWhere( AbstractSqlCondition p_oSqlCondition) {
		this.sqlWhere.addSqlCondition(p_oSqlCondition);
	}
	
	/**
	 * Génère le Sql de la requête Delete
	 *
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.ItfTransactionalContext} object.
	 * @return le Sql de la requête Delete
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException echec de creation de la requete
	 */
	public String toSql( MContext p_oContext ) throws DaoException {
		StringBuilder r_oSqlDelete = new StringBuilder(SqlKeywords.DELETE.toString());
		r_oSqlDelete.append(' ');
		r_oSqlDelete.append(SqlKeywords.FROM);
		r_oSqlDelete.append(' ');
		r_oSqlDelete.append(this.tableName);
		r_oSqlDelete.append(' ');

		this.sqlWhere.toSql(r_oSqlDelete, new ToSqlContext(p_oContext));
		return r_oSqlDelete.toString();
	}

	/**
	 * Clone le Delete
	 *
	 * @see java.lang.Object#clone()
	 * @return clone de l'instance
	 */
	@Override
	public SqlDelete clone() {
		SqlDelete r_oSqlDelete = new SqlDelete(this.tableName);
		if ( this.sqlWhere != null ) {
			r_oSqlDelete.setWhere(this.sqlWhere.clone());
		}
		return r_oSqlDelete;
	}

	/**
	 * Bind les values du Delete
	 *
	 * @param p_oStatement preparedStatement à utiliser pour le binding
	 * @throws DaoException échec du binding
	 */
	public void bindValues(AndroidSQLitePreparedStatement p_oStatement) throws DaoException {
		this.sqlWhere.bindValues(new StatementBinder(p_oStatement));
	}
}
