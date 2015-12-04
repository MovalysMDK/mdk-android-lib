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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.joins;


/**
 * <p>Représente une jointure SQL</p>
 *
 *
 * @since 2.5
 */
public abstract class AbstractSqlTableJoin extends AbstractSqlJoin {
	
	/**
	 * table maitre
	 */
	private String masterTableName = null;
	
	/**
	 * Définit le nom de la table maître
	 *
	 * @param p_sMaster a {@link java.lang.String} object.
	 */
	protected void setMasterTableName(String p_sMaster) {
		this.masterTableName = p_sMaster;
	}
	
	/**
	 * Retourne le nom de la table maître
	 *
	 * @return a {@link java.lang.String} object.
	 */
	protected String getMasterTableName() {
		return this.masterTableName;
	}
}
