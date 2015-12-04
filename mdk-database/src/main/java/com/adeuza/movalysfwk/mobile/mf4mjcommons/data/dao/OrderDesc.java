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
 * <p>Représente un tri descendant sur une colonne</p>
 *
 *
 */
public final class OrderDesc extends AbstractOrder implements Cloneable {

	/**
	 * Mot clé du tri descendant
	 */
	private static final String DESC = " DESC";
	
	/**
	 * TODO Décrire le constructeur OrderDesc
	 *
	 * @param p_sOrder a {@link java.lang.String} object.
	 */
	public OrderDesc(String p_sOrder) {
		super(p_sOrder);
	}

	/**
	 * Constructor
	 *
	 * @param p_sOrder order
	 * @param p_oCollate collate
	 * @since 3.1.1
	 */
	public OrderDesc(String p_sOrder, Collate p_oCollate ) {
		super(p_sOrder, p_oCollate);
	}
	
	/**
	 * TODO Décrire le constructeur OrderDesc
	 *
	 * @param p_oField a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field} object.
	 */
	public OrderDesc(Field p_oField) {
		super(p_oField);
	}

	/**
	 * Constructor
	 *
	 * @param p_oField field
	 * @param p_oCollate collate
	 * @since 3.1.1
	 */
	public OrderDesc(Field p_oField, Collate p_oCollate) {
		super(p_oField, p_oCollate);
	}
	
	/**
	 * TODO Décrire le constructeur OrderDesc
	 *
	 * @param p_oField a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field} object.
	 * @param p_sTableAliasName a {@link java.lang.String} object.
	 */
	public OrderDesc(Field p_oField, String p_sTableAliasName) {
		super(p_oField, p_sTableAliasName);
	}

	/**
	 * Constructor
	 *
	 * @param p_oField field
	 * @param p_sTableAliasName alias
	 * @param p_oCollate collate
	 * @since 3.1.1
	 */
	public OrderDesc(Field p_oField, String p_sTableAliasName, Collate p_oCollate) {
		super(p_oField, p_sTableAliasName, p_oCollate);
	}
	
	/**
	 * TODO Décrire le constructeur OrderDesc
	 *
	 * @param p_sOrder a {@link java.lang.String} object.
	 * @param p_sTableAliasName a {@link java.lang.String} object.
	 */
	public OrderDesc(String p_sOrder, String p_sTableAliasName) {
		super(p_sOrder, p_sTableAliasName);
	}

	/**
	 * Constructor
	 *
	 * @param p_sOrder order
	 * @param p_sTableAliasName alias
	 * @param p_oCollate collate
	 * @since 3.1.1
	 */
	public OrderDesc(String p_sOrder, String p_sTableAliasName, Collate p_oCollate) {
		super(p_sOrder, p_sTableAliasName, p_oCollate);
	}
	
	/**
	 * Retourne l'Order correspondant au tri ascendant sur un field
	 *
	 * @param p_iColIndex index de la colonne
	 * @return l'Order correspondant au tri ascendant sur un field
	 */
	public static Order of( int p_iColIndex ) {
		return new OrderDesc(Integer.toString(p_iColIndex));
	}

	/**
	 * Returns an order built with the given parameters
	 *
	 * @param p_iColIndex index
	 * @param p_oCollate collate
	 * @return an order built with the given parameters
	 * @since 3.1.1
	 */
	public static Order of( int p_iColIndex, Collate p_oCollate ) {
		return new OrderDesc(Integer.toString(p_iColIndex), p_oCollate);
	}
	
	/**
	 * Retourne l'Order correspondant au tri descendant sur un field
	 *
	 * @param p_oField field sur lequel porte le tri
	 * @return l'Order correspondant au tri descendant sur un field
	 */
	public static Order of( Field p_oField ) {
		return new OrderDesc(p_oField);
	}
	
	/**
	 * Returns an order built with the given parameters
	 *
	 * @param p_oField field
	 * @param p_oCollate collate
	 * @return an order built with the given parameters
	 * @since 3.1.1
	 */
	public static Order of( Field p_oField, Collate p_oCollate ) {
		return new OrderDesc(p_oField, p_oCollate);
	}
	
	/**
	 * Retourne l'Order correspondant au tri descendant sur un field
	 *
	 * @param p_oField field sur lequel porte le tri
	 * @return l'Order correspondant au tri descendant sur un field
	 * @param p_sTableAliasName a {@link java.lang.String} object.
	 */
	public static Order of( Field p_oField, String p_sTableAliasName ) {
		return new OrderDesc(p_oField, p_sTableAliasName);
	}
	
	/**
	 * Returns an order built with the given parameters
	 *
	 * @param p_oField field
	 * @param p_sTableAliasName alias
	 * @param p_oCollate collate
	 * @return an order built with the given parameters
	 * @since 3.1.1
	 */
	public static Order of( Field p_oField, String p_sTableAliasName, Collate p_oCollate ) {
		return new OrderDesc(p_oField, p_sTableAliasName, p_oCollate);
	}
	
	/**
	 * Clone l'objet OrderDesc
	 *
	 * @return le clone de OrderDesc
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Order clone() {
		return new OrderDesc(getOrder(), getTableAliasName(), getCollate());
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.data.dao.AbstractOrder#getSort()
	 */
	@Override
	protected String getSort() {
		return DESC;
	}
}
