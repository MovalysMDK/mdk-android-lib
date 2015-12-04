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

import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.FieldType;

/**
 * <p>Condition NOT IN avec les valeurs indiquées</p>
 *
 * <p>SqlNotInValueCondition est composée</p>
 * <ul>
 *   <li>De la liste des champs pour le NOT IN</li>
 *   <li>De la liste des valeurs</li>
 * </ul>
 *
 * <p>Par défaut, le NOT IN est découpé en bloc de 5 valeurs comme ceci : </p>
 * <pre>
 * {@code
 * CHAMPS1 NOT IN (val1,val2,val3,val4,val5) AND CHAMPS1 NOT (val6,val7,val8,val9,val10)...
 * }</pre>
 *
 * <p>Exemple :</p>
 * <p>Pour le sql suivant : </p>
 * <pre>
 * {@code
 * select intervention1.id from mit_intervention intervention1
 * where (intervention1.inventiontypeid, intervention1.code)
 * 	not in ((1,'CODE001'),(2,'CODE002'),(3,'CODE003'))
 * }</pre>
 *
 * <p>Le code Java correspondant est le suivant :</p>
 * <pre>
 * {@code
 * List<SqlInField> listInFields = new ArrayList<SqlInField>();
 * listInFields.add(new SqlInField(InterventionField.INTERVENTIONTYPEID, InterventionDao.ALIAS_NAME, SqlType.INTEGER));
 * listInFields.add(new SqlInField(InterventionField.CODE, InterventionDao.ALIAS_NAME, SqlType.VARCHAR));
 *
 * List<Object> listValues = new ArrayList<Object>();
 * listValues.add(1);
 * listValues.add("CODE001");
 * listValues.add(2);
 * listValues.add("CODE002");
 * listValues.add(3);
 * listValues.add("CODE003");
 *
 * oSqlQuery.addFieldToSelect(InterventionDao.ALIAS_NAME, InterventionField.ID );
 * oSqlQuery.addToFrom(InterventionDao.TABLE_NAME, InterventionDao.ALIAS_NAME);
 * oSqlQuery.addToWhere(new SqlNotInValueCondition(listInFields, listValues));
 * }
 * </pre>
 *
 *
 * @see SqlInValueCondition
 * @since 2.5
 */
public class SqlNotInValueCondition extends SqlInValueCondition {

	/**
	 * Constructeur avec liste des champs, alias de la table du champs, liste des valeurs
	 *
	 * @param p_listFields tableau de couples Champs/Type
	 * @param p_sAlias alias de la table des champs
	 * @param p_listValues liste des valeurs
	 */
	public SqlNotInValueCondition(FieldType[] p_listFields, String p_sAlias, List<?> p_listValues){
		super(p_listFields, p_sAlias, p_listValues);
		this.setInverse(true);
	}

	/**
	 * Constructeur avec liste des champs, liste des valeurs
	 *
	 * @param p_listFields tableau de couples Champs/Type
	 * @param p_listValues liste des valeurs
	 */
	public SqlNotInValueCondition(FieldType[] p_listFields, List<?> p_listValues){
		super(p_listFields, p_listValues);
		this.setInverse(true);
	}	
	
}
