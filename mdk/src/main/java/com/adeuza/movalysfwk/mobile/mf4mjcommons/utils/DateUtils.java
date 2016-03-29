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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.utils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * <p>Classe utilitaire pour les dates</p>
 *
 */
public final class DateUtils {
	/**
	 * Returns <code>true</code> if <code>p_oDate</code> is not null and it not January 1, 1970, 00:00:00 GMT.
	 *
	 * @param p_oDate A date to check.
	 * @return <code>true</code> if <code>p_oDate</code> is not null and it not January 1, 1970, 00:00:00 GMT.
	 */
	public static boolean isDefined(final Date p_oDate) {
		return p_oDate != null && p_oDate.getTime() > 0;
	}

	/** Private constructor. */
	private DateUtils() {
		// Nothing
	}
	
	/**
	 * <p>getTimestampCurrentDay.</p>
	 *
	 * @return a {@link java.sql.Timestamp} object.
	 */
	public static Timestamp getTimestampCurrentDay() {
		return new Timestamp(nullifyDate(Calendar.getInstance()).getTime().getTime());
	}
	
	/**
	 * <p>getTimeMillisCurrentDay.</p>
	 *
	 * @return a long.
	 */
	public static long getTimeMillisCurrentDay() {
		return nullifyDate(Calendar.getInstance()).getTime().getTime();
	}
	
	/**
	 * Return the date in parameter with hour, minute,second and millisecond to zero
	 *
	 * @param p_oTimestamp  base computing date
	 * @return Return the date in parameter with hour, minute,second and millisecond to zero
	 */
	public static Calendar nullifyDate( final Timestamp p_oTimestamp ) {
		Calendar oGregorianCalendar = new GregorianCalendar();
		oGregorianCalendar.setTimeInMillis(p_oTimestamp.getTime());
		return nullifyDate(oGregorianCalendar) ; 
	}	
	
	/**
	 * Return the date in parameter with hour, minute,second and millisecond to zero
	 *
	 * @return Return the date in parameter with hour, minute,second and millisecond to zero
	 * @param p_oCalendar a {@link java.util.Calendar} object.
	 */
	public static Calendar nullifyDate( final Calendar p_oCalendar ) {
		p_oCalendar.clear(Calendar.HOUR_OF_DAY);
		p_oCalendar.set(Calendar.HOUR_OF_DAY, 0);
		p_oCalendar.clear(Calendar.MINUTE);
		p_oCalendar.set(Calendar.MINUTE, 0);
		p_oCalendar.clear(Calendar.SECOND);
		p_oCalendar.set(Calendar.SECOND, 0);
		p_oCalendar.clear(Calendar.MILLISECOND);
		p_oCalendar.set(Calendar.MILLISECOND, 0);
		return p_oCalendar ; 
	}
	
	/**
	 * Return the date in parameter with hour, minute,second and millisecond to zero
	 *
	 * @param p_oTimestamp  base computing date
	 * @return Return the date in parameter with hour, minute,second and millisecond to zero
	 * @see DateUtils#nullifyDate(Timestamp)
	 */
	public static Calendar nullifyDate( final long p_oTimestamp ) {
		return nullifyDate(new Timestamp(p_oTimestamp));
	}
	/**
	 * Return the date in parameter with hour, minute,second and millisecond to maximum values possible
	 *
	 * @param p_oTimestamp  base computing date
	 * @return Return the date in parameter with hour, minute,second and millisecond to maximum values possible
	 */
	public static Calendar modifyDateToEndOfDay( final Timestamp p_oTimestamp ) {
		Calendar oGregorianCalendar = new GregorianCalendar();
		oGregorianCalendar.setTimeInMillis(p_oTimestamp.getTime());
		oGregorianCalendar.set(Calendar.HOUR_OF_DAY, oGregorianCalendar.getMaximum(Calendar.HOUR_OF_DAY));
		oGregorianCalendar.clear(Calendar.MINUTE);
		oGregorianCalendar.set(Calendar.MINUTE, oGregorianCalendar.getMaximum(Calendar.MINUTE));
		oGregorianCalendar.clear(Calendar.SECOND);
		oGregorianCalendar.set(Calendar.SECOND, oGregorianCalendar.getMaximum(Calendar.SECOND));
		oGregorianCalendar.clear(Calendar.MILLISECOND);
		oGregorianCalendar.set(Calendar.MILLISECOND, oGregorianCalendar.getMaximum(Calendar.MILLISECOND));
		return oGregorianCalendar ; 
	}
	
	/**
	 * <p>getTime.</p>
	 *
	 * @param p_oTimestampToConvert a java$sql$Timestamp object.
	 * @return a {@link java.lang.Long} object.
	 */
	public static Long getTime(final java.sql.Timestamp p_oTimestampToConvert){
		if (p_oTimestampToConvert!=null){
			return p_oTimestampToConvert.getTime();
		}
		else{
			return null;
		}
	}
	
	/**
	 * @param p_oTimestampToConvert
	 * @return
	 */
	public static java.sql.Date getDate(final java.sql.Timestamp p_oTimestampToConvert){
		if (p_oTimestampToConvert!=null){
			return new java.sql.Date(p_oTimestampToConvert.getTime());
		}
		else{
			return null;
		}
	}
	
	/**
	 * <p>getTime.</p>
	 *
	 * @param p_oLong a {@link java.lang.Long} object.
	 * @return Timestamp object.
	 */
	public static java.sql.Timestamp getTime(Long p_oLong){
		if (p_oLong!=null){
			return new java.sql.Timestamp(p_oLong);
		}
		else{
			return null;
		}
	}

	/**
	 * <p>getTime.</p>
	 *
	 * @param p_oDate a {@link java.lang.Long} object.
	 * @return Timestamp object.
	 */
	public static java.sql.Timestamp getTime(Date p_oDate){
		if (p_oDate!=null){
			return new java.sql.Timestamp(p_oDate.getTime());
		}
		else{
			return null;
		}
	}

	/**
	 * Return the date in parameter with hour, minute,second and millisecond to maximum values possible
	 *
	 * @param p_oTimestamp  base computing date
	 * @return Return the date in parameter with hour, minute,second and millisecond to maximum values possible
	 * @see DateUtils#modifyDateToEndOfDay(Timestamp)
	 */
	public static Calendar modifyDateToEndOfDay( final long p_oTimestamp ) {
		return modifyDateToEndOfDay(new Timestamp(p_oTimestamp));
	}
	/**
	 * Add The decimal hour to the date
	 *
	 * @param p_oDate date to add
	 * @param p_dHourInDecimal number of hours to add like 7.8
	 * @param p_iPrecision asked precision for the date Calendar.HOUR_OF_DAY or Calendar.MINUTE or Calendar.SECOND
	 * ex 7,33 is 7h20mn0s with Calendar.MINUTE 7,33 is 7h19mn40s with Calendar.SECOND , 7,33 is 7h0mn0s with Calendar.HOUR_OF_DAY
	 * @return the new date with the number of hours added
	 */
	public static Calendar addHourInDecimalToDate( final Calendar p_oDate , final  double p_dHourInDecimal , final int p_iPrecision ){
		Calendar r_oCal = new GregorianCalendar();
		r_oCal.setTime(p_oDate.getTime());
		if ( p_iPrecision == Calendar.HOUR_OF_DAY ) {
			// on arrrondi à l'entier le plus proche
			r_oCal.set(Calendar.HOUR_OF_DAY, (int)Math.round(p_dHourInDecimal) );
		} else {
			r_oCal.set(Calendar.HOUR_OF_DAY, (int) Math.floor(p_dHourInDecimal) ); 
			if ( p_iPrecision == Calendar.MINUTE || p_iPrecision ==  Calendar.SECOND) {
				double dNbMinuteInDecimal = (p_dHourInDecimal - Math.floor(p_dHourInDecimal) ) * DurationUtils.MINUTES_IN_ONE_HOUR ;
				if ( dNbMinuteInDecimal > 0.0d ) {
					if ( p_iPrecision == Calendar.MINUTE ) {
						r_oCal.set(Calendar.MINUTE , (int)Math.round(dNbMinuteInDecimal));
					}else {
						r_oCal.set(Calendar.MINUTE , (int)Math.floor(dNbMinuteInDecimal));
						if ( p_iPrecision ==  Calendar.SECOND) {
							double dNbMinuteInSecond = (dNbMinuteInDecimal - Math.floor(dNbMinuteInDecimal) ) * DurationUtils.SECONDS_IN_ONE_MINUTE ;
							if ( dNbMinuteInSecond > 0.0d ) {
								r_oCal.set(Calendar.SECOND ,(int)Math.floor(dNbMinuteInSecond));
							}
						}
					}
				}
			}
		}
		return r_oCal ; 
	}
	/**
	 * Retourne true si les 2 dates sont au même jour, mois, année, ère, false sinon
	 *
	 * @param oFirstCal a {@link java.util.Calendar} object.
	 * @param oSecondCal a {@link java.util.Calendar} object.
	 * @return a boolean.
	 */
	public static boolean isSameDayMonthYearEra(Calendar oFirstCal , Calendar oSecondCal ){
		boolean r_bIsSameDay = true ;
		if (oFirstCal.get(Calendar.DATE) != oSecondCal.get(Calendar.DATE) ) {
			r_bIsSameDay = false;
		} else  if (oFirstCal.get(Calendar.MONTH) != oSecondCal.get(Calendar.MONTH) ) {
			r_bIsSameDay = false;
		}else  if (oFirstCal.get(Calendar.YEAR) != oSecondCal.get(Calendar.YEAR) ) {
			r_bIsSameDay = false;
		}else  if (oFirstCal.get(Calendar.ERA) != oSecondCal.get(Calendar.ERA) ) {
			r_bIsSameDay = false;
		}
		return r_bIsSameDay ;
	}
}
