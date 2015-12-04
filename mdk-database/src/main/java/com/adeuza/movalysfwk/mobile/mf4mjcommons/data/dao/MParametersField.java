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
 * Enumération des champs
 */
public enum MParametersField implements Field {
	/**
	 * Field ID
	 * type=INTEGER not-null=true
	 */
	ID(1),
	/**
	 * Field NAME
	 * type=TEXT not-null=true
	 */
	NAME(2),
	/**
	 * Field VALUE
	 * type=TEXT not-null=true
	 */
	VALUE(3);
	/**
	 * Index de la column
	 */
	private int columnIndex;

	/**
	 * Constructeur
	 * @param p_iColumnIndex index de la column
	 */
	private MParametersField(int p_iColumnIndex) {
		this.columnIndex = p_iColumnIndex;
	}

	/**
	 * Retourne l'index de la colonne
	 *
	 * @return index de la colonne
	 */
	@Override
	public int getColumnIndex() {
		return this.columnIndex;
	}
}
