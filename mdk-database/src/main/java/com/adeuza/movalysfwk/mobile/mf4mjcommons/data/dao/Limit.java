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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * Defines a limit parameter to apply to a query
 *
 * @since 3.1.1
 */
public class Limit implements Cloneable {

	/** the minimum value of the limit */
	private int min = -1;
	
	/** the maximum value of the limit */
	private int max = -1;
	
	/**
	 * Default constructor
	 */
	public Limit() {
		// empty constructor
	}
	
	/**
	 * Constructor
	 *
	 * @param p_iMax max value for the limit
	 */
	public Limit(int p_iMax) {
		this.max = p_iMax;
	}

	/**
	 * Constructor
	 *
	 * @param p_iMin min value for the limit
	 * @param p_iMax max value for the limit
	 */
	public Limit(int p_iMin, int p_iMax) {
		this.min = p_iMin;
		this.max = p_iMax;
	}

	/**
	 * Returns the min value
	 *
	 * @return the min value
	 */
	public int getMin() {
		return min;
	}

	/**
	 * Returns the max value
	 *
	 * @return the max value
	 */
	public int getMax() {
		return max;
	}
	
	/**
	 * Generates the sql for the limit
	 *
	 * @param p_oSql the sql to feed
	 * @param p_oToSqlContext the context to use
	 * @return the sql of the limit
	 */
	public StringBuilder appendToSql( StringBuilder p_oSql, ToSqlContext p_oToSqlContext ) {
		if (this.max != -1) {
			p_oSql.append(" LIMIT ");
			
			if (this.min != -1) {
				p_oSql.append(Integer.valueOf(this.min));
				p_oSql.append(" ");
			}
			
			p_oSql.append(Integer.valueOf(this.max));
		}
		return p_oSql ;
	}
	
	/**
	 * Clones a GroupedField
	 *
	 * @return the cloned GroupedField
	 */
	@Override
	public Limit clone() {
		return new Limit(this.min, this.max);
	}
	
}
