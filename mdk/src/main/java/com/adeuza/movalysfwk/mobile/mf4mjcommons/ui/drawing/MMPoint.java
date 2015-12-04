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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.drawing;

/**
 * <p>
 * 	A point representing a location in (x,y) coordinate space, specified in float precision.
 * </p>
 *
 *
 * @since Barcelone (8 déc. 2010)
 */
public class MMPoint {

	/** The X coordinate of this Point. */
	private float x;
	/** The Y coordinate of this Point. */
	private float y;
	
	/**
	 * <p>
	 *  Constructs and initializes a point at the origin (0, 0) of the coordinate space.
	 * </p>
	 */
	public MMPoint() {
		super();
		this.x = 0;
		this.y = 0;
	}
	
	/**
	 * <p>
	 *  Constructs and initializes a point at the specified (x,y) location in the coordinate space.
	 * </p>
	 *
	 * @param p_oX
	 * 			the X coordinate of the newly constructed Point
	 * @param p_oY
	 * 			the Y coordinate of the newly constructed Point
	 */
	public MMPoint(float p_oX, float p_oY) {
		super();
		this.x = p_oX;
		this.y = p_oY;
	}
	
	/**
	 * <p>
	 *  Constructs and initializes a point with the same location as the specified Point object.
	 * </p>
	 *
	 * @param p_oPoint
	 * 			a point.
	 */
	public MMPoint(MMPoint p_oPoint) {
		super();
		this.x = p_oPoint.getX();
		this.y = p_oPoint.getY();
	}
	
	/**
	 * <p>
	 * 	Returns the X coordinate of this <em>MMPoint</em> in float precision.
	 * </p>
	 *
	 * @return the X coordinate of this <em>MMPoint</em>.
	 */
	public float getX() {
		return this.x;
	}
	
	/**
	 * <p>
	 * 	Returns the Y coordinate of this <em>MMPoint</em> in float precision.
	 * </p>
	 *
	 * @return the Y coordinate of this <em>MMPoint</em>.
	 */
	public float getY() {
		return this.y;
	}
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder("P(").append(this.x).append(',').append(this.y).append(") ").toString() ;
	}	
}
