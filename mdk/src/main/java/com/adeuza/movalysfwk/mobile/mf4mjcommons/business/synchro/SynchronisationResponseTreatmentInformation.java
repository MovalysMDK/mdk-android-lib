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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro;

import java.util.HashSet;
import java.util.Set;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.CascadeSet;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.ICascade;

/**
 * Contient les informations de traitement de résultat.
 * Cette classe sera donnée au classe listener de la synchronisation
 *
 */
public class SynchronisationResponseTreatmentInformation {

	/** ensemble des types de données impactées par la synchro */
	private Set<Class<? extends MEntity>> impactedClasses = null;
	
	/**
	 * Constructeur
	 */
	public SynchronisationResponseTreatmentInformation() {
		this.impactedClasses = new HashSet<Class<? extends MEntity>>();
	}
	
	/**
	 * Add a class
	 *
	 * @param p_oClass a {@link java.lang.Class} object.
	 */
	public void addEntity(Class<? extends MEntity>...p_oClass) {
		for(Class<? extends MEntity> oClass : p_oClass) {
			this.impactedClasses.add(oClass);
		}
	}
	
	/**
	 * <p>containsOne.</p>
	 *
	 * @param p_oClass a {@link java.lang.Class} object.
	 * @return a boolean.
	 */
	public boolean containsOne(Class<? extends MEntity>...p_oClass) {
		boolean bContains = false;
		for(Class<? extends MEntity> oClass : p_oClass) {
			bContains = this.impactedClasses.contains(oClass);
			if (bContains) {
				break;
			}
		}
		return bContains;
	}

	/**
	 * <p>clear.</p>
	 */
	public void clear() {
		this.impactedClasses.clear();
	}

	/**
	 * TODO Décrire la méthode complete de la classe SynchronisationResponseTreatmentInformation
	 *
	 * @param p_oInformations a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronisationResponseTreatmentInformation} object.
	 */
	public void complete(SynchronisationResponseTreatmentInformation p_oInformations) {
		this.impactedClasses.addAll(p_oInformations.impactedClasses);
	}
	/**
	 * Retourne vrai si la cascade vise une classe impactée par la synchronisation
	 *
	 * @param p_oCascadeSet cascades dans lequel on cherche les types visées par la cascade
	 * @return a boolean.
	 */
	public boolean containsEntity( CascadeSet p_oCascadeSet){
		boolean bContains = false;
		
		for( ICascade oCascade : p_oCascadeSet.cascades()) {
			Class<? extends MEntity> oClass = oCascade.getResultType(); 
			bContains = oClass != null && this.impactedClasses.contains(oClass);
			if (bContains) {
				break;
			}
		}
		return bContains; 
	}
}
