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
 * <p>Représente un couple de valeurs</p>
 *
 *
 * @param <E>
 * @param <T>
 */
public class PairValue<E, T> {

	/**
	 * Valeur1
	 */
	private final E key;
	/**
	 * Valeur2 
	 */
	private final T value;

	/**
	 * Construit un couple de valeur à partir de p_oKey et de p_oValue
	 *
	 * @param p_oKey valeur1
	 * @param p_oValue valeur2
	 */
	public PairValue(E p_oKey, T p_oValue) {
		this.key = p_oKey;
		this.value = p_oValue;
	}

	/**
	 * Retourne la premiere valeur du couple
	 *
	 * @return la premiere valeur du couple
	 */
	public E getKey() {
		return key ;
	}

	/**
	 * Retourne la deuxieme valeur du couple
	 *
	 * @return la deuxieme valeur du couple
	 */
	public T getValue() {
		return value;
	}
}
