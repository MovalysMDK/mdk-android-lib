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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;

/**
 * <p>Représente une suite de cascades les MIEntity</p>
 *
 *
 */
public final class CascadeSet {

	/**
	 * CascadeSet indiquant l'absence de cascade
	 */
	public static final CascadeSet NONE = new CascadeSet().setUnmodifiable(); 

	/**
	 * CascadeSet indiquant que toutes les cascades doivent être prises en compte
	 */
	public static final CascadeSet ALL = CascadeSet.of(GenericCascade.ALL).setUnmodifiable();
	
	/**
	 * CascadeSet indiquant que toutes les cascades doivent être prises en compte, sauf les champs dynamiques
	 */
	//public static final CascadeSet ALL_AND_NOT_ALL_DYN = CascadeSet.of(GenericCascade.ALL,GenericCascade.NOT_ALL_DYN); 
	
	/**
	 * Enumération de la cascade
	 */
	public enum GenericCascade implements ICascade {
		/**
		 * 
		 */
		ALL,
		/**
		 * 
		 */
		CUSTOM_FIELDS,
		/**
		 * 
		 */
		ALL_REFERENCES;

		/**
		 * {@inheritDoc}
		 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.ICascade#getResultType()
		 */
		@Override
		public Class<? extends MEntity> getResultType() {
			return null;
		}
	}
	
	/** ensembles des cascades */
	private List<ICascade> cascade = null;

	/**
	 * Permet de définir un ensemble de cascade
	 * @param p_oCascade les cascades définissant les CascadesSet
	 */
	private CascadeSet(List<ICascade> p_oCascade) {
		cascade = p_oCascade;
	}

	/**
	 * Définit un CascadeSet ne contenant aucune cascade
	 */
	private CascadeSet() {
		this.cascade = new ArrayList<ICascade>();
	}

	/**
	 * Rend la liste des cascades non modifiable.
	 * @return l'objet lui-même.
	 */
	private CascadeSet setUnmodifiable() {
		this.cascade = Collections.unmodifiableList(this.cascade);
		return this;
	}

	/**
	 * Créer un cascadeset initialisé avec un ensemble de cascades
	 *
	 * @param p_oCascades les cascades à ajouter
	 * @return un CascadeSet
	 */
	public static CascadeSet of(ICascade... p_oCascades) {
		List<ICascade> listCascades = new ArrayList<ICascade>();
		for (ICascade oCascade : p_oCascades) {
			if (oCascade != null)  {
				listCascades.add(oCascade);
			}
		}
		
		return new CascadeSet(listCascades);
	}

	/**
	 * Permet d'ajouter n cascades
	 *
	 * @param p_oCascades les cascades à ajouter
	 */
	public void add(ICascade... p_oCascades) {
		for (ICascade oCascade : p_oCascades) {
			if (oCascade != null)  {
				this.cascade.add(oCascade);
			}
		}
	}
		
	/**
	 * Permet de compléter le cascadeSet courrant avec les cascades de p_oCascadeToAdd.
	 * Les cascades sont ajoutées à l'objet courrant si elles ne sont pas présentes pour le moment.
	 *
	 * @param p_oCascadesToAdd l'ensemble des cascades à ajouter
	 */
	public void completeWith(CascadeSet p_oCascadesToAdd) {
		for(ICascade oCascade : p_oCascadesToAdd.cascades()) {
			if (!this.contains(oCascade)) {
				this.add(oCascade);
			}
		}
	}
		
	/**
	 * Indique si le CascadeSet courrant contient la cascade p_oCascade
	 *
	 * @param p_oCascade la cascade recherchée
	 * @return true si le CascadeSet contient la cascade recherchée false dans le cas contraire
	 */
	public boolean contains( ICascade p_oCascade ) {
		return this.cascade.contains(p_oCascade);
	}
	
	/**
	 * Récupération de la liste des ICascade de ce CascadeSet
	 *
	 * @return liste des ICascade de ce CascadeSet
	 */
	public List<ICascade> cascades() {
		return this.cascade;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder("CascadeSet[");
		if ( !this.cascade.isEmpty()) {
			for( ICascade oCascade : this.cascade) {
				sBuilder.append(oCascade.toString());
				sBuilder.append(',');
			}
			sBuilder.deleteCharAt(sBuilder.length()-1);
		}
		else {
			sBuilder.append("NONE");
		}
		sBuilder.append(']');
		return sBuilder.toString();
	}
	
	
}
