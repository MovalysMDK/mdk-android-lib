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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.contentprovider;

/**
 * Bean that aggregates the different parameters needed to execute a query on a ContentResolver
 */
public class ContentResolverQuery {

	/**
	 * projections array
	 */
	private String[] projection;
	
	/**
	 * selection
	 */
	private String selection;
	
	/**
	 * selection arguments
	 */
	private String[] selectionArgs;
	
	/**
	 * order
	 */
	private String order;

	/**
	 * Return projection
	 * @return projection
	 */
	public String[] getProjection() {
		return this.projection;
	}

	/**
	 * Return selection
	 * @return selection
	 */
	public String getSelection() {
		return this.selection;
	}

	/**
	 * Return selection args
	 * @return selection args
	 */
	public String[] getSelectionArgs() {
		return this.selectionArgs;
	}

	/**
	 * Return order
	 * @return order
	 */
	public String getOrder() {
		return this.order;
	}

	/**
	 * Set projection value
	 * @param p_oProjection projection
	 */
	public void setProjection(String[] p_oProjection) {
		this.projection = p_oProjection;
	}

	/**
	 * Set selection value
	 * @param p_sSelection selection
	 */
	public void setSelection(String p_sSelection) {
		this.selection = p_sSelection;
	}

	/**
	 * Set selection arguments
	 * @param p_oSelectionArgs selection arguments
	 */
	public void setSelectionArgs(String[] p_oSelectionArgs) {
		this.selectionArgs = p_oSelectionArgs;
	}

	/**
	 * Set order
	 * @param p_sOrder order
	 */
	public void setOrder(String p_sOrder) {
		this.order = p_sOrder;
	}
	
	
}
