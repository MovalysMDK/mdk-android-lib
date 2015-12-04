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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.ICascade;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlInField;

/**
 * <p>CascadeOptimDefinition</p>
 *
 *
 */
public class CascadeOptimDefinition {

	/**
	 * Maximum number of foreign keys
	 */
	private static final int MAX_FK_NUMBER = 64;
	
	/**
	 * List of the primary key fields with their type on the entity
	 */
	private List<SqlInField> pkFields;
		
	/**
	 * Map containing the foreign keys of the cascades
	 */
	private Map<ICascade, List<SqlInField>> cascadeFkFields;
		
	/**
	 * List of joins Cascade
	 */
	private List<ICascade> joinCascades ;
	
	/**
	 * Constructor
	 *
	 * @param p_oPkFields an array of {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.PairValue} objects.
	 * @param p_sAlias a {@link java.lang.String} object.
	 */
	public CascadeOptimDefinition( FieldType[] p_oPkFields, String p_sAlias ) {
		
		this.cascadeFkFields = new HashMap<ICascade, List<SqlInField>>(MAX_FK_NUMBER);		
		this.pkFields = new ArrayList<SqlInField>();
		for( FieldType oPkField : p_oPkFields ) {
			this.pkFields.add(new SqlInField(oPkField.getField().toString(),p_sAlias,oPkField.getType()));
		}
		this.pkFields = Collections.unmodifiableList(pkFields);
		this.joinCascades = new ArrayList<ICascade>();
	}
	
	/**
	 * Declares a cascade of type n..1, 1..1
	 *
	 * @param p_oCascade cascade to declare
	 * @param p_oFkFields liste fields in the foreign key
	 * @param p_sAlias alias of the destination table 
	 */
	public void registerCascade(ICascade p_oCascade,
			FieldType[] p_oFkFields, String p_sAlias) {
		
		List<SqlInField> listFkInFields = new ArrayList<SqlInField>();
		for( FieldType oFkField : p_oFkFields ) {
			listFkInFields.add(new SqlInField(oFkField.getField().toString(),p_sAlias,oFkField.getType()));
		}
		this.cascadeFkFields.put(p_oCascade, listFkInFields);
	}
	
	/**
	 * Declares a cascade on a join class
	 *
	 * @param p_oCascade cascade to the join class
	 */
	public void registerJoinClass( ICascade p_oCascade ) {
		this.joinCascades.add(p_oCascade);
	}
	
	/**
	 * Creates an empty cascade
	 *
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.CascadeOptim} object.
	 */
	public CascadeOptim createCascadeOptim() {		
		Map<ICascade, Map<String, List<Object>>> mapCascadeEntities = new TreeMap<ICascade, Map<String, List<Object>>>();
		Map<ICascade, List<Object>> mapCascadeFkValues = new HashMap<ICascade, List<Object>>();
		Map<ICascade, List<Object>> mapCascadeJoinClassesFkValues = new HashMap<ICascade, List<Object>>();
		Map<ICascade, Map<String, List<String>>> mapJoinClasses = new HashMap<ICascade, Map<String, List<String>>>();
		for( ICascade oCascade : this.cascadeFkFields.keySet()) {
			mapCascadeFkValues.put(oCascade, new ArrayList<Object>());
			mapCascadeEntities.put(oCascade, new HashMap<String, List<Object>>());
		}
		for( ICascade oCascade : this.joinCascades ) {
			mapCascadeJoinClassesFkValues.put(oCascade, new ArrayList<Object>());
			mapJoinClasses.put(oCascade, new HashMap<String, List<String>>());
		}
		return new CascadeOptim(this, mapCascadeEntities, mapCascadeFkValues, mapCascadeJoinClassesFkValues, mapJoinClasses ) ;
	}

	/**
	 * locks the cascade
	 */
	public void lock() {
		this.pkFields = Collections.unmodifiableList(this.pkFields);
		this.cascadeFkFields = Collections.unmodifiableMap(this.cascadeFkFields);
		this.joinCascades = Collections.unmodifiableList(this.joinCascades);
	}
	
	/**
	 * returns the fields of the primary key
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<SqlInField> getPkFields() {
		return this.pkFields ;
	}

	/**
	 * returns the fields of the foreignkey for the given cascade
	 *
	 * @param p_oCascade a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.ICascade} object.
	 * @return a {@link java.util.List} object.
	 */
	public List<SqlInField> getCascadeFkFields(ICascade p_oCascade) {
		return this.cascadeFkFields.get(p_oCascade);
	}
}
