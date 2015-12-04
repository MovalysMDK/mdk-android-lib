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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.insert.AbstractSqlInsertField;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.insert.SqlInsertAutoIncrementField;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.insert.SqlInsertBindedField;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.insert.SqlInsertNullField;

/**
 * <p>Représente une requête d'insertion.</p>
 *
 * <p>Le SqlInsert est construit à partir : </p>
 * <ul>
 *   <li>Du nom de la table dans laquelle insérer</li>
 *   <li>Une liste de {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.insert.AbstractSqlInsertField AbstractSqlInsertField}</li>
 * </ul>
 *
 * <p>Exemple de création d'une requête d'insertion :</p>
 * <pre>
 * {@code
 * SqlInsert oInsert = new SqlInsert( InterventionDao.TABLE_NAME);
 * oInsert.addAutoIncrementField(InterventionField.ID);
 * oInsert.addBindedField(InterventionField.CODE);
 * oInsert.addBindedField(InterventionField.CREATIONDATE);
 * }
 * </pre>
 *
 * @since 2.5
 */
public class SqlInsert implements Cloneable {

	/**
	 * Nom de la table pour l'insertion
	 */
	private String tableName ;
	
	/**
	 * Liste des champs faisant partie du INSERT 
	 */
	private List<AbstractSqlInsertField> listFields = new ArrayList<AbstractSqlInsertField>();
	
	/**
	 * Constructeur vide
	 */
	public SqlInsert() {
		// nothing to do
	}

	/**
	 * Instancie un SqlInsert avec le nom de la table
	 *
	 * @param p_sTableName nom de la table
	 */
	public SqlInsert( String p_sTableName ) {
		this.tableName = p_sTableName ;
	}
	
	/**
	 * Ajoute un field avec valeur bindée
	 *
	 * @param p_oField nom du champs
	 */
	public void addBindedField( Field p_oField ) {
		this.listFields.add(new SqlInsertBindedField(p_oField));
	}
	
	/**
	 * Ajoute un field avec valeur venant d'une séquence
	 *
	 * @param p_oField nom du champs
	 */
	public void addAutoIncrementField( Field p_oField ) {
		this.listFields.add(new SqlInsertAutoIncrementField(p_oField));
	}
	
	/**
	 * Ajoute en position i un field avec valeur venant d'une séquence
	 *
	 * @param p_iIndex position du field à ajouter
	 * @param p_oField nom du champs
	 */
	public void addAutoIncrementField( int p_iIndex, Field p_oField ) {
		this.listFields.add(p_iIndex, new SqlInsertAutoIncrementField(p_oField));
	}

	/**
	 * Ajoute un Field avec une valeur NULL
	 *
	 * @param p_oField nom du champs
	 */
	public void addNullField( Field p_oField ) {
		this.listFields.add(new SqlInsertNullField(p_oField));
	}
	
	/**
	 * Ajoute un field dans l'insert
	 * private car utilisé uniquement par le clone
	 * @param p_oSqlInsertField field to insert
	 */
	private void addSqlInsertField( AbstractSqlInsertField p_oSqlInsertField ) {
		this.listFields.add(p_oSqlInsertField);
	}
	
	/**
	 * Définit le nom de la table de l'insert
	 *
	 * @param p_sTableName nom de la table
	 */
	public void setTableName(String p_sTableName) {
		this.tableName = p_sTableName;
	}

	/**
	 * Génère le SQL de la requête d'insertion
	 *
	 * @return la requête SQL en chaîne de caractère
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.ItfTransactionalContext} object.
	 */
	public String toSql( MContext p_oContext ) {
		StringBuilder r_oSqlInsert = new StringBuilder(SqlKeywords.INSERT.toString());
		r_oSqlInsert.append(' ');
		r_oSqlInsert.append(SqlKeywords.INTO);
		r_oSqlInsert.append(' ');
		r_oSqlInsert.append(this.tableName);
		r_oSqlInsert.append('(');
		boolean bFirst = true ;
		for( AbstractSqlInsertField oSqlInsertField: this.listFields ) {
			if ( bFirst ) {
				bFirst = false ;
			} else {
				r_oSqlInsert.append(',');
			}
			oSqlInsertField.appendToColumnClause(r_oSqlInsert, p_oContext);
		}
		r_oSqlInsert.append(") ");
		r_oSqlInsert.append(SqlKeywords.VALUES);
		r_oSqlInsert.append('(');
		bFirst = true ;
		for( AbstractSqlInsertField oSqlInsertField: this.listFields ) {
			if ( bFirst ) {
				bFirst = false ;
			} else {
				r_oSqlInsert.append(',');
			}
			oSqlInsertField.appendToValueClause(r_oSqlInsert, p_oContext);
		}
		r_oSqlInsert.append(')');
		return r_oSqlInsert.toString();
	}
	
	/**
	 * Clone de la requête d'insertion
	 *
	 * @see java.lang.Object#clone()
	 * @return clone de la requête d'insertion
	 */
	@Override
	public SqlInsert clone() {
		SqlInsert r_oSqlInsert = new SqlInsert(this.tableName);
		for( AbstractSqlInsertField oSqlInsertField : this.listFields ) {
			r_oSqlInsert.addSqlInsertField(oSqlInsertField.clone());
		}
		return r_oSqlInsert;
	}
}
