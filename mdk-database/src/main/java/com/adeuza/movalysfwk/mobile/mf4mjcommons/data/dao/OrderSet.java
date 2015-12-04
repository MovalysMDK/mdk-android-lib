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
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * <p>Tri pour les objets Query</p>
 *
 *
 */
public final class OrderSet implements Cloneable {

	/**
	 * Cascade vide
	 */
	public static final OrderSet NONE = new OrderSet();

	/**
	 * Liste des order
	 */
	private List<Order> orders;

	/**
	 * Constructeur
	 * @param p_oCascade Liste des tris
	 */
	private OrderSet(List<Order> p_oCascade) {
		orders = p_oCascade;
	}

	/**
	 * Constructeur vide
	 * <p>Constructeur private car l'utilisateur doit passer par la méthode of pour l'instancier</p>
	 */
	private OrderSet() {
		this.orders = new ArrayList<Order>();
	}

	/**
	 * Retourne l'OrderSet correspondant à la liste des tris spécifiés
	 *
	 * @param p_oOrder liste des tris
	 * @return OrderSet correspondant à la liste des tris spécifiés
	 */
	public static OrderSet of(Order... p_oOrder) {
		List<Order> listOrders = new ArrayList<Order>();
		for (Order oOrder : p_oOrder) {
			listOrders.add(oOrder);
		}

		return new OrderSet(listOrders);
	}

	/**
	 * Genère le sql du tri
	 *
	 * @return tri au format sql
	 * @param p_oSql a {@link java.lang.StringBuilder} object.
	 * @param p_oToSqlContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext} object.
	 */
	public StringBuilder appendToSql( StringBuilder p_oSql, ToSqlContext p_oToSqlContext ) {
		if (!this.orders.isEmpty()) {
			p_oSql.append(" ORDER BY");
			char sSeparator = ' ';
			for (Order oOrder : this.orders) {
				p_oSql.append(sSeparator);
				oOrder.appendToSQL(p_oSql, p_oToSqlContext);
				sSeparator = ',';
			}
		}
		return p_oSql ;
	}
	
	/**
	 * Compute order by for ContentResolver
	 * @return  order by for ContentResolver
	 */
	public String computeContentResolverOrderBy() {
		String r_sResult = null;
		if (!this.orders.isEmpty()) {
			StringBuilder sOrderBy = new StringBuilder();
			boolean bFirst = true ;
			for (Order oOrder : this.orders) {
				if ( !bFirst ) {
					sOrderBy.append(',');
				}
				else {
					bFirst = false;
				}
				oOrder.computeContentResolverOrderBy(sOrderBy);
			}
			r_sResult = sOrderBy.toString();
		}
		return r_sResult;
	}
	
	/**
	 * Clone l'objet OrderSet
	 *
	 * @return le clone de l'objet
	 * @see java.lang.Object#clone()
	 */
	@Override
	public OrderSet clone() {
		List<Order> listOrders = new ArrayList<Order>();
		for( Order oOrder : this.orders ) {
			listOrders.add(oOrder.clone());
		}
		return new OrderSet(listOrders);
	}
	
	/**
	 * Returns true if the orders list is empty
	 *
	 * @return true if the orders list is empty
	 */
	public boolean isEmpty() {
		return this.orders.isEmpty();
	}	
}
