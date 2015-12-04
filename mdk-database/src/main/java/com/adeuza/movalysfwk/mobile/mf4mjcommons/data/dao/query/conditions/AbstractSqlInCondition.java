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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions;

import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.FieldType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.SqlType;

/**
 * <p>Condition IN : classe abstraite pour la gestion du IN.</p>
 *
 * <p>En plus des éléments hérités, AbstractSqlInCondition est composée de :</p>
 * <ul>
 *   <li>liste de SqlInField : la liste des champs qui composent le IN.</li>
 *   <li>boolean Inverse : si true, utilisation du NOT IN</li>
 * </ul>
 *
 * <p>Deux implémentations sont disponibles :</p>
 * <ul>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlInSelectCondition SqlInSelectCondition} : la valeur du champs est comprise dans le résultat d'une sous-requête</li>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlInValueCondition SqlInValueCondition} : la valeur du champs est comprise dans une liste de valeurs données</li>
 *   <li>{@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlInValueCondition SqlNotInValueCondition} : la valeur du champs n'est pas comprise dans une liste de valeurs données</li>
 * </ul>
 *
 *
 * @see AbstractSqlCondition
 */
public abstract class AbstractSqlInCondition extends AbstractSqlCondition {
	
	/**
	 * Liste des champs du IN
	 */
	private List<SqlInField> listFields = new ArrayList<>();

	/**
	 * In ou NOT IN 
	 */
	private boolean inverse = false ;

	/**
	 * Vrai si la condition est un NOT IN
	 *
	 * @return true si NOT IN
	 */
	public boolean isInverse() {
		return this.inverse;
	}

	/**
	 * Définit si la condition est un NOT IN
	 *
	 * @param p_bInverse true si NOT IN
	 */
	public void setInverse(boolean p_bInverse) {
		this.inverse = p_bInverse;
	}
	
	/**
	 * Définit le champs unique du IN
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_iSqlType type du champs
	 */
	public void setField( String p_sFieldName, String p_sAlias, @SqlType int p_iSqlType ) {
		this.listFields.clear();
		this.listFields.add( new SqlInField(p_sFieldName, p_sAlias, p_iSqlType));
	}
	
	/**
	 * Définit le champs unique du IN
	 *
	 * @param p_oField champs
	 * @param p_sAlias alias de la table du champs
	 * @param p_iSqlType type du champs
	 */
	public void setField( Field p_oField, String p_sAlias, @SqlType int p_iSqlType ) {
		listFields.clear();
		this.listFields.add( new SqlInField(p_oField.name(), p_sAlias, p_iSqlType));
	}
	
	/**
	 * Définit la liste des champs du IN
	 *
	 * @param p_listFields liste des champs
	 */
	public void setFields( List<SqlInField> p_listFields ) {
		this.listFields.clear();
		this.listFields.addAll(p_listFields);
	}
	
	/**
	 * Définit la liste des champs du IN
	 *
	 * @param p_listFields tableau de couples Field/Type de champs
	 * @param p_sAliasName alias de la table des champs
	 */
	public void setFields( FieldType[] p_listFields, String p_sAliasName ) {
		this.listFields.clear();
		for( FieldType oPairValue : p_listFields ) {
			this.listFields.add( new SqlInField(oPairValue.getField(), p_sAliasName, oPairValue.getType()));
		}
	}

	/**
	 * Retourne la liste des champs du IN
	 *
	 * @return la liste des champs du IN
	 */
	public List<SqlInField> getListFields() {
		return this.listFields;
	}
	
	/**
	 * Retourne le nombre de champs du IN
	 *
	 * @return le nombre de champs du IN
	 */
	public int getNbFields() {
		return this.listFields.size();
	}
}
