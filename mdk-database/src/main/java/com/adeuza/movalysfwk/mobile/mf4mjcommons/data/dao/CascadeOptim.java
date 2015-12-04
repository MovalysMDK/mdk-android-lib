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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.ICascade;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlInField;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlInValueCondition;

/**
 * <p>Permet d'optimiser le traitement des cascades générateur pour les listes</p>
 *
 *
 * @since 2.5
 */
public class CascadeOptim {

	/** Main entities max number */
	private static final int MAIN_ENTITIES_MAX_NUMBER = 128;

	/**
	 * Maximum size of IN, multiple queries will be processed if the IN is above that size
	 */
	public static final int IN_SIZE = 100;

	/**
	 * entities from whcich the query is built
	 */
	private Map<String, Object> mainEntities;

	/**
	 * IN primary keys list 
	 */
	private List<Object> pkValues;
	
	/**
	 * Map containing entities per cascade
	 */
	private Map<ICascade, Map<String, List<Object>>> cascadeEntities;
	
	/**
	 * Map containing the values of the foreign keys per cascade 
	 */
	private Map<ICascade, List<Object>> cascadeFkValues;
	
	/**
	 * Map containing the values of the foreign key per join classes 
	 */
	private Map<ICascade, List<Object>> cascadeJoinClassesFkValues;

	/**
	 * map containing the values of the cascades per join classes
	 */
	private Map<ICascade, Map<String, List<String>>> mapCascadeJoinClasses ;

	/**
	 * Cascades definitions
	 */
	private CascadeOptimDefinition cascadeOptimDefinition ;
	
	/**
	 * Constructor
	 *
	 * @param p_mapJoinClasses a {@link java.util.Map} object.
	 * @param p_oCascadeOptimDefinition a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.CascadeOptimDefinition} object.
	 * @param p_oCascadeEntities a {@link java.util.Map} object.
	 * @param p_mapCascadeFkValues a {@link java.util.Map} object.
	 * @param p_mapCascadeJoinClassesFkValues a {@link java.util.Map} object.
	 */
	protected CascadeOptim( CascadeOptimDefinition p_oCascadeOptimDefinition,
			Map<ICascade, Map<String, List<Object>>> p_oCascadeEntities,
			Map<ICascade, List<Object>> p_mapCascadeFkValues,
			Map<ICascade, List<Object>> p_mapCascadeJoinClassesFkValues, 
			Map<ICascade, Map<String, List<String>>> p_mapJoinClasses ) {
		
		this.cascadeOptimDefinition = p_oCascadeOptimDefinition ;
		this.cascadeEntities = p_oCascadeEntities ;
		this.cascadeFkValues = p_mapCascadeFkValues ;
		this.cascadeJoinClassesFkValues = p_mapCascadeJoinClassesFkValues ;
		this.mainEntities = new HashMap<String, Object>(MAIN_ENTITIES_MAX_NUMBER);
		this.pkValues = new ArrayList<Object>();
		this.mapCascadeJoinClasses = p_mapJoinClasses ;
	}

	/**
	 * Déclare une entity
	 *
	 * @param p_sId id de l'entity
	 * @param p_oEntity l'entity
	 * @param p_oPkValue liste des valeurs composant sa clé primaire
	 */
	public void registerEntity(String p_sId, Object p_oEntity,
			Object... p_oPkValue) {
		this.mainEntities.put(p_sId, p_oEntity);
		for (Object oPkValue : p_oPkValue) {
			this.pkValues.add(oPkValue);
		}
	}

	/**
	 * Recupère l'entity par son id
	 *
	 * @param p_sId id de l'entity
	 * @return entity d'id p_sId
	 */
	public Object getEntity(String p_sId) {
		return this.mainEntities.get(p_sId);
	}

	/**
	 * Retourne la liste des entités principales.
	 *
	 * @return la liste des entités principales
	 */
	public Collection<Object> getListMainEntity(){
		return mainEntities.values();
	}
	
	/**
	 * Enregistre une entity pour une cascadee
	 *
	 * @param p_oCascade Cascade
	 * @param p_sFkId id de l'objet cible au format string
	 * @param p_oEntity l'objet source
	 * @param p_listFkValues id de l'objet cible ( liste car l'id peut-être composé de plusieurs valeurs )
	 */
	public void registerEntityForCascade(ICascade p_oCascade, String p_sFkId,
			Object p_oEntity, Object... p_listFkValues) {
		
		Map<String, List<Object>> mapEntitiesOnCascade = this.cascadeEntities
				.get(p_oCascade);
		if ( mapEntitiesOnCascade == null ) {
			mapEntitiesOnCascade = new HashMap<String, List<Object>>();
			this.cascadeEntities.put(p_oCascade, mapEntitiesOnCascade);
		}
		
		// Recupère la liste des entités
		List<Object> listCascadeEntities = mapEntitiesOnCascade.get(p_sFkId);
		if (listCascadeEntities == null) {
			listCascadeEntities = new ArrayList<Object>();
			mapEntitiesOnCascade.put(p_sFkId, listCascadeEntities);
		}
		// ajoute les valeurs de la clé étrangère
		List<Object> listFkValue = this.cascadeFkValues.get(p_oCascade);
		for (Object oFkValue : p_listFkValues) {
			if ( !listFkValue.contains(oFkValue)) {
				listFkValue.add(oFkValue);
			}
		}
		listCascadeEntities.add(p_oEntity);
	}
	
	/**
	 * Retourne vrai s'il y a des entités enregistrées pour la cascade indiquée
	 *
	 * @param p_oCascade cascade
	 * @return vrai s'il y a des entités enregistrées pour la cascade indiquée
	 */
	public boolean hasEntityForCascade( ICascade p_oCascade ) {
		Map<String,List<Object>> listObjects = this.cascadeEntities.get(p_oCascade);
		return listObjects != null && !listObjects.isEmpty();
	}


	/**
	 * Déclare un enregistrement d'une join-table
	 *
	 * @param p_oCascade nom de la cascade many-to-many
	 * @param p_sFk1 clé source au format String
	 * @param p_sFk2 clé cible au format String
	 * @param p_oFk2Values liste des valeurs qui composent la clé cible
	 */
	public void registerJoinEntity( ICascade p_oCascade, String p_sFk1, String p_sFk2, Object... p_oFk2Values ) {
		Map<String, List<String>> mapJoinEntities = this.mapCascadeJoinClasses.get( p_oCascade );
		List<String> listFk1 = mapJoinEntities.get(p_sFk2);
		if ( listFk1 == null ) {
			listFk1 = new ArrayList<String>();
			mapJoinEntities.put(p_sFk2, listFk1 );
			List<Object> listFkValues = this.cascadeJoinClassesFkValues.get( p_oCascade);
			for( Object oPkValue : p_oFk2Values ) {
				listFkValues.add( oPkValue );
			}
		}
		listFk1.add(p_sFk1);
	}
	
	/**
	 * Recupère la liste des clés source par rapport à la clé cible
	 *
	 * @param p_oCascade nom de la cascade many-to-many
	 * @param p_sTargetId id de la clé source
	 * @return Retourne les clés source correspondant à la clé cible
	 */
	public List<String> getSourceIdsOfJoinEntitiesByTargetId( ICascade p_oCascade, String p_sTargetId ) {
		Map<String, List<String>> mapJoinEntities = this.mapCascadeJoinClasses.get( p_oCascade );
		return mapJoinEntities.get(p_sTargetId);
	}
	
	/**
	 * Recupère la liste des entity pointant vers la cascade avec la foreign key p_sFkId
	 *
	 * @param p_oCascade cascade
	 * @param p_sFkId valeur de la foreign key
	 * @return liste des entity correspondantes
	 */
	public List<? extends Object> getListEntityForCascade(ICascade p_oCascade,
			String p_sFkId) {
		Map<String, List<Object>> mapEntitiesOnCascade = this.cascadeEntities
				.get(p_oCascade);
		return mapEntitiesOnCascade.get(p_sFkId);
	}

	/**
	 * Retourne la liste des InClause correspondant à la cascade
	 *
	 * @param p_oCascade cascade
	 * @return liste des InClause correspondant à la cascade
	 */
	public List<SqlInValueCondition> getInClausesForCascade(ICascade p_oCascade) {

		List<SqlInValueCondition> r_listIn = new ArrayList<SqlInValueCondition>();

		Map<String, List<Object>> mapEntitiesOnCascade = this.cascadeEntities
				.get(p_oCascade);
		List<Object> listFkValue = this.cascadeFkValues.get(p_oCascade);
		
		if ( !listFkValue.isEmpty()) {

			List<SqlInField> listFkFields = this.cascadeOptimDefinition.getCascadeFkFields(p_oCascade);

			int iNbKey = mapEntitiesOnCascade.keySet().size();
			Iterator<Object> iterFkValue = listFkValue.iterator();
			List<Object> listValueOfIn = new ArrayList<Object>();

			// Pour chaque entité
			for (int iCpt = 1; iCpt <= iNbKey; iCpt++) {

				// ajout les valeurs de la clé primaire de l'entité
				for (int i = 0; i < listFkFields.size(); i++) {
					listValueOfIn.add(iterFkValue.next());
				}

				// si restriction atteinte ou si dernière entité
				if (iCpt % IN_SIZE == 0 || iCpt == iNbKey) {
					SqlInValueCondition oInClause = SqlInValueCondition.createSqlInValueCondition(listFkFields, listValueOfIn);
					r_listIn.add(oInClause);
					listValueOfIn = new ArrayList<Object>();
				}
			}
		}

		return r_listIn;
	}

	/**
	 * Retourne la liste des InClause de l'entity principale
	 *
	 * @return iste des InClause de l'entity principale
	 */
	public List<SqlInValueCondition> getInClausesForMainEntities() {
		List<SqlInValueCondition> r_listIn = new ArrayList<SqlInValueCondition>();

		if ( !this.pkValues.isEmpty()) {

			int iNbKey = this.mainEntities.keySet().size();
			Iterator<Object> iterPkValue = this.pkValues.iterator();
			List<Object> listValueOfIn = new ArrayList<Object>();

			// Pour chaque entité
			for (int iCpt = 1; iCpt <= iNbKey; iCpt++) {

				// ajout les valeurs de la clé primaire de l'entité
				for (int i = 0; i < this.cascadeOptimDefinition.getPkFields().size(); i++) {
					listValueOfIn.add(iterPkValue.next());
				}

				// si restriction atteinte ou si dernière entité
				if (iCpt % IN_SIZE == 0 || iCpt == iNbKey) {
					SqlInValueCondition oInClause = SqlInValueCondition.createSqlInValueCondition(this.cascadeOptimDefinition.getPkFields(), listValueOfIn);
					r_listIn.add(oInClause);
					listValueOfIn = new ArrayList<Object>();
				}
			}
		}

		return r_listIn;
	}

	
	/**
	 * Retourne la liste des InClause correspondant à une cascade vers une classe de jointure
	 *
	 * @param p_oCascade la cascade vers la classe de jointure
	 * @param p_oDestEntityPK la clé primaire de l'entity cible
	 * @param p_sAlias l'aias de l'entity cible
	 * @return la liste des InClause correspondant à une cascade vers une classe de jointure
	 */
	public List<SqlInValueCondition> getInClausesForJoinClasses(ICascade p_oCascade, FieldType[] p_oDestEntityPK,
			String p_sAlias ) {

		List<SqlInValueCondition> r_listIn = new ArrayList<SqlInValueCondition>();

		Map<String, List<String>> mapEntitiesOnCascade = this.mapCascadeJoinClasses.get(p_oCascade);
		List<Object> listFkValue = this.cascadeJoinClassesFkValues.get(p_oCascade);
		
		List<SqlInField> listSqlInFields = new ArrayList<SqlInField>();
		for( FieldType oDestEntityPK : p_oDestEntityPK) {
			listSqlInFields.add( new SqlInField(oDestEntityPK.getField().toString(), p_sAlias, oDestEntityPK.getType()));
		}
		
		if ( !listFkValue.isEmpty()) {

			int iNbKey = mapEntitiesOnCascade.keySet().size();
			Iterator<Object> iterFkValue = listFkValue.iterator();
			List<Object> listValueOfIn = new ArrayList<Object>();

			// Pour chaque entité
			for (int iCpt = 1; iCpt <= iNbKey; iCpt++) {

				// ajout les valeurs de la clé primaire de l'entité
				for (int i = 0; i < p_oDestEntityPK.length; i++) {
					Object oObject = iterFkValue.next();
					listValueOfIn.add(oObject);
				}

				// si restriction atteinte ou si dernière entité
				if (iCpt % IN_SIZE == 0 || iCpt == iNbKey) {
					SqlInValueCondition oInClause = SqlInValueCondition.createSqlInValueCondition(listSqlInFields, listValueOfIn);
					r_listIn.add(oInClause);
					listValueOfIn = new ArrayList<Object>();
				}
			}
		}

		return r_listIn;
	}
}
