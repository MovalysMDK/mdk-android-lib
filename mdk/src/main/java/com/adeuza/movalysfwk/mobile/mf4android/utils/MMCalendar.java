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
package com.adeuza.movalysfwk.mobile.mf4android.utils;

import java.util.Calendar;

/**
 * Calendar class
 *
 */
public class MMCalendar extends Calendar{

	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = 474295236417817759L;
	
	/** The tag for the year*/
	public static final int DATE_DEFAULT_YEAR = 2000;
	/** The tag for the year*/
	public static final int DATE_DEFAULT_MONTH = 1;
	/** The tag for the year*/
	public static final int DATE_DEFAULT_DAY = 1;
	/** The tag for the year*/
	public static final int DATE_DEFAULT_HOUR = 0;
	/** The tag for the year*/
	public static final int DATE_DEFAULT_MINUTE = 0;

	/** The tag for the year*/
	public static final String DATE_TAG_YEAR ="year";
	/** The tag for the month*/
	public static final String DATE_TAG_MONTH ="month";
	/** The tag for the day*/
	public static final String DATE_TAG_DAY ="day";
	/** The tag for the hour*/
	public static final String DATE_TAG_HOUR ="hour";
	/** The tag for the minute*/
	public static final String DATE_TAG_MINUTE ="minute";
	
	@Override
	public void add(int p_iField, int p_iAmount) {
		// Nothing to do
	}

	@Override
	protected void computeFields() {
		// Nothing to do
		
	}

	@Override
	protected void computeTime() {
		// Nothing to do
		
	}

	@Override
	public int getGreatestMinimum(int p_iField) {
		// Nothing to do
		return 0;
	}

	@Override
	public int getLeastMaximum(int p_iField) {
		// Nothing to do
		return 0;
	}

	@Override
	public int getMaximum(int p_iField) {
		// Nothing to do
		return 0;
	}

	@Override
	public int getMinimum(int p_iField) {
		// Nothing to do
		return 0;
	}

	@Override
	public void roll(int p_iField, boolean p_bUp) {
		// Nothing to do
		
	}

}
