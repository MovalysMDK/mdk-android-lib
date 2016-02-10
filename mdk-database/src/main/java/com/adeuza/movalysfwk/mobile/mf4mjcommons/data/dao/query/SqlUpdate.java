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

import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.SqlType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlEqualsValueCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlLikeCondition;

/**
 * <p>
 * Requête d'update
 * </p>
 * <p>Le SqlUpdate est construit à partir : </p>
 * <ul>
 *   <li>Du nom de la table à updater.</li>
 *   <li>Une liste de {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field Field} : les champs à updater.</li>
 *   <li>D'un {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlWhere SqlWhere} pour la sélection des lignes à mettre à jour.</li>
 * </ul>
 *
 * <p>Exemple de création d'une requête d'update :</p>
 * <pre>
 * {@code
 * SqlUpdate oUpdateQuery = new SqlUpdate(InterventionDao.TABLE_NAME);
 * oUpdateQuery.addBindedField(InterventionField.CODE);
 * SqlEqualsValueCondition oSqlEqualsValueCondition = oUpdateQuery.addEqualsConditionToWhere(InterventionField.ID, SqlType.INTEGER);
 * oSqlEqualsValueCondition.setValue(12);
 * }
 * </pre>
 *
 * @since 2.5
 */
public class SqlUpdate implements Cloneable {

	/**
	 * Nom de la table
	 */
	private String tableName;

	/**
	 * Liste des champs du Set de l'update
	 */
	private List<Field> listFields = new ArrayList<Field>();

	/**
	 * Where de l'update
	 */
	private SqlWhere sqlWhere = new SqlWhere();

	/**
	 * Constructeur vide
	 */
	public SqlUpdate() {
		// nothing to do
	}

	/**
	 * Constructeur avec le nom de la table
	 *
	 * @param p_sTableName
	 *            nom de la table
	 */
	public SqlUpdate(String p_sTableName) {
		this.tableName = p_sTableName;
	}

	/**
	 * Ajoute un field au Set
	 *
	 * @param p_oField
	 *            field à ajouter
	 */
	public void addBindedField(Field p_oField) {
		this.listFields.add(p_oField);
	}

	/**
	 * Définit le nom de la table pour l'update
	 *
	 * @param p_sTableName
	 *            nom de la table
	 */
	public void setTableName(String p_sTableName) {
		this.tableName = p_sTableName;
	}

	/**
	 * Ajoute une condition d'égalité au Where
	 *
	 * @param p_oField
	 *            nom du champs
	 * @param p_oValueSqlType
	 *            type de la valeur
	 * @return la condition d'égalitée ajouté au where
	 */
	public SqlEqualsValueCondition addEqualsConditionToWhere(Field p_oField, SqlType p_oValueSqlType) {
		SqlEqualsValueCondition r_oSqlEqualsValueCondition = new SqlEqualsValueCondition(new SqlField(p_oField), new SqlBindValue(null,
				p_oValueSqlType));
		this.sqlWhere.addSqlCondition(r_oSqlEqualsValueCondition);
		return r_oSqlEqualsValueCondition;
	}

	/**
	 * Ajoute une condition d'égalité au where
	 *
	 * @param p_sFieldName
	 *            nom du champs
	 * @param p_oValueSqlType
	 *            type de la valeur
	 * @return la condition d'égalitée ajouté au where
	 */
	public SqlEqualsValueCondition addEqualsConditionToWhere(String p_sFieldName, SqlType p_oValueSqlType) {
		SqlEqualsValueCondition r_oSqlEqualsValueCondition = new SqlEqualsValueCondition(new SqlField(p_sFieldName), new SqlBindValue(null,
				p_oValueSqlType));
		this.sqlWhere.addSqlCondition(r_oSqlEqualsValueCondition);
		return r_oSqlEqualsValueCondition;
	}

	/**
	 * Ajoute une condition Like au where
	 *
	 * @param p_sFieldName
	 *            nom du champs
	 * @param p_oValueSqlType
	 *            type de la valeur
	 * @return la condition Like ajoutée au where
	 */
	public SqlLikeCondition addLikeConditionToWhere(String p_sFieldName, SqlType p_oValueSqlType) {
		SqlLikeCondition r_oSqlLikeCondition = new SqlLikeCondition(new SqlField(p_sFieldName), new SqlBindValue(null, p_oValueSqlType));
		this.sqlWhere.addSqlCondition(r_oSqlLikeCondition);
		return r_oSqlLikeCondition;
	}

	/**
	 * Ajoute une condition au where
	 *
	 * @param p_oSqlCondition
	 *            la condition
	 */
	public void addToWhere(AbstractSqlCondition p_oSqlCondition) {
		this.sqlWhere.addSqlCondition(p_oSqlCondition);
	}

	/**
	 * Définit le Where de l'update En private car utilisé uniquement par le clone()
	 * 
	 * @param p_oSqlWhere
	 *            nouveau where de la requête
	 */
	private void setWhere(SqlWhere p_oSqlWhere) {
		this.sqlWhere = p_oSqlWhere;
	}

	/**
	 * Génère le SQL de l'update
	 *
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.ItfTransactionalContext} object.
	 * @return SQL de l'update
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             echec de creation de la requete
	 */
	public String toSql( MContext p_oContext ) throws DaoException {
		StringBuilder r_oSqlUpdate = new StringBuilder(SqlKeywords.UPDATE.toString());
		r_oSqlUpdate.append(' ');
		r_oSqlUpdate.append(this.tableName);
		r_oSqlUpdate.append(' ');
		r_oSqlUpdate.append(SqlKeywords.SET);
		r_oSqlUpdate.append(' ');
		boolean bFirst = true;
		for (Field oField : this.listFields) {
			if (bFirst) {
				bFirst = false;
			} else {
				r_oSqlUpdate.append(", ");
			}
			r_oSqlUpdate.append(oField.name());
			r_oSqlUpdate.append(" = ?");
		}
		this.sqlWhere.toSql(r_oSqlUpdate, new ToSqlContext(p_oContext));
		return r_oSqlUpdate.toString();
	}

	/**
	 * {@inheritDoc}
	 *
	 * Clone l'update
	 * @see java.lang.Object#clone()
	 */
	@Override
	public SqlUpdate clone() {
		SqlUpdate r_oSqlUpdate = new SqlUpdate(this.tableName);
		for (Field oField : this.listFields) {
			r_oSqlUpdate.addBindedField(oField);
		}
		r_oSqlUpdate.setWhere(this.sqlWhere.clone());
		return r_oSqlUpdate;
	}
}
