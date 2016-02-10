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

/**
 * <p>Field d'une table (utilisé pour les enumerations des Fields dans les Dao)</p>
 *
 *
 */
public interface Field {

	/**
	 * Retourne le nom du champs
	 *
	 * @return nom du champs
	 */
	public String name();
	
	/**
	 * Index de la colonne
	 *
	 * @return index de la colonne
	 */
	public int getColumnIndex();
}
