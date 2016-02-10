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

/**
 *
 * <p>Enumération indiquant quelle type d'opération sera réalisé dans une requête exigeant une condition</p>
 *
 *
 */
public enum OperatorCondition {
	/** Supérieur */
	SUPERIOR(" > "),
	
	/** Inférieur */
	INFERIOR(" < "),
	
	/** Equals */
	EQUALS(" = "),
	
	/** Supérieur ou égal */
	SUPERIOR_OR_EQUALS(" >= "),
	
	/** Inférieur ou égal */
	INFERIOR_OR_EQUALS(" <= "),
	
	/** Différent */
	DIFFERENT(" <> ");
	
	
	/** Valeur qui sera utilisé pour la comparaison dans la requête */
	private String symbol;
	
	/**
	 * Constructeur de l'énumération
	 * @param p_sDescription Valeur du champs Descriptor
	 */
	private OperatorCondition(String p_sDescription) {
		this.symbol = p_sDescription;
	}

	/**
	 * Renvoie la description
	 *
	 * @return La description
	 */
	public String toSql() {
		return this.symbol;
	} 
}
