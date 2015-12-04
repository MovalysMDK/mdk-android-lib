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
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>Utility class used to manipulate duration.</p>
 *
 *
 */
public final class DurationUtils {

	/** Separator used by {@link #format(long)} */
	public static final char SEPARATOR = ':';

	
	/** Number of seconds in one minute. */
	public static final int SECONDS_IN_ONE_MINUTE = 60;
	
	/** Number of minutes in one hour. */
	public static final int MINUTES_IN_ONE_HOUR = 60;

	/** Number of hours in one day. */
	public static final int HOURS_IN_ONE_DAY = 24;

	/** Number of milliseconds in one second. */
	public static final int MILLISECONDS_IN_ONE_SECOND = 1000;

	/** Number of milliseconds in one minute. */
	public static final int MILLISECONDS_IN_ONE_MINUTE = MILLISECONDS_IN_ONE_SECOND * SECONDS_IN_ONE_MINUTE;
	
	/** Number of milliseconds in one hour. */
	public static final int MILLISECONDS_IN_ONE_HOUR = MILLISECONDS_IN_ONE_MINUTE * MINUTES_IN_ONE_HOUR;
	
	/** Number of seconds in one hour. */
	public static final int SECONDS_IN_ONE_HOUR = MINUTES_IN_ONE_HOUR * SECONDS_IN_ONE_MINUTE;

	/** Number of milliseconds in one day. */
	public static final int MILLISECONDS_IN_ONE_DAY = MILLISECONDS_IN_ONE_HOUR * HOURS_IN_ONE_DAY ;
	/** 
	 * Singleton pattern : Bill Pugh method.
	 * 
	 *
	 *
	 */
	private static final class DurationUtilsHolder {
		/** Single instance of {@link DurationUtils}. */
		public static final DurationUtils INSTANCE = new DurationUtils();

		/** Default constructor. */
		private DurationUtilsHolder () {
			// NOTHING
		}
	}

	/**
	 * Returns the single instance of this class.
	 *
	 * @return the single instance of this class.
	 */
	public static DurationUtils getInstance() {
		return DurationUtilsHolder.INSTANCE;
	}

	/**
	 * The {@link NumberFormat} used to format each piece of the string representation.
	 */
	private NumberFormat numberFormatter;
	
	/**
	 * Singleton pattern: private constructor.
	 */
	private DurationUtils() {
		this.numberFormatter = DateFormat.getInstance().getNumberFormat();
		this.numberFormatter.setMinimumIntegerDigits(2);
	}

	/**
	 * Transforms a duration (in seconds) to its string representation.
	 *
	 * @param p_lDuration a duration in seconds (a positive or null number)
	 * @return The string representation of this duration (HH:MM)
	 */
	public String format(long p_lDuration) {
		StringBuilder oDuration = new StringBuilder();

		oDuration.append(this.numberFormatter.format(p_lDuration / SECONDS_IN_ONE_HOUR));
		oDuration.append(DurationUtils.SEPARATOR);
		oDuration.append(this.numberFormatter.format((p_lDuration % SECONDS_IN_ONE_HOUR) / SECONDS_IN_ONE_MINUTE));

		return oDuration.toString();
	}
	/**
	 * <p>convertStringToTimestamp.</p>
	 *
	 * @param p_sValue a {@link java.lang.String} object.
	 * @return Timestamp object.
	 */
	public static Timestamp convertStringToTimestamp(String p_sValue) {
		Timestamp r = null;
		if ( p_sValue != null ) {
			try {
				r = new Timestamp(
						(new SimpleDateFormat("dd/MM/yyyy HH:mm")).parse(p_sValue).getTime());
			} catch (ParseException e) {
				
			}
		}
		return r;
	}
	
	/**
	 * <p>convertTimestampToString.</p>
	 *
	 * @param p_oValue Timestamp object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String convertTimestampToString(final Timestamp p_oValue) {
		String r_sDate = null ;
		if ( p_oValue != null ) {
			r_sDate = (new SimpleDateFormat("dd/MM/yyyy HH:mm")).format(new Date(p_oValue.getTime()));
		}
		return r_sDate;
	}
	/**
	 * Compute the duration between the 2 Dates in calendar
	 *
	 * @param p_oTimeBegin smaller date
	 * @param p_oTimeEnd biggest date
	 * @param p_iUniteOfTheResult unite like Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.DATE
	 * @return the duration between the 2 Dates in calendar
	 */
	public static double computeDurationBetweenDates( final Calendar p_oTimeBegin ,final  Calendar p_oTimeEnd , final int p_iUniteOfTheResult ){
		double r_dValue = computeDurationBetweenDates( new Timestamp(p_oTimeBegin.getTime().getTime()) , new Timestamp(p_oTimeEnd.getTime().getTime()) , p_iUniteOfTheResult );
		return r_dValue  ;
	}
	/**
	 * Return the duration in seconds between the 2 dates in parameter
	 *
	 * @param p_oTimeBegin first date
	 * @param p_oTimeEnd second date to compare
	 * @param p_iUniteOfTheResult unite of the result : int from Calendar like Calendar.HOUR_OF_DAY, Calendar.MINUTE , Calendar.SECOND  , Calendar.MILLISECOND , Calendar.DATE
	 * @return the duration in seconds between the 2 dates in parameter
	 */
	public static double computeDurationBetweenDates( final Timestamp p_oTimeBegin , final Timestamp p_oTimeEnd , final int p_iUniteOfTheResult ){
		double lDiffInMilliseconds = (p_oTimeEnd.getTime() - p_oTimeBegin.getTime());
		double lDiviseur ; 
		switch ( p_iUniteOfTheResult ) {
			case Calendar.HOUR_OF_DAY :
				lDiviseur = MILLISECONDS_IN_ONE_HOUR ;
				break ;
			case Calendar.MINUTE :
				lDiviseur = MILLISECONDS_IN_ONE_MINUTE;
				break ;
			case Calendar.SECOND :
				lDiviseur = MILLISECONDS_IN_ONE_SECOND ;
				break ;
			case Calendar.DATE :
				lDiviseur = MILLISECONDS_IN_ONE_DAY ;
				break ;
			default :
				lDiviseur = 1 ;
		}
		double r_dValue =  lDiffInMilliseconds / lDiviseur ;
		return r_dValue ; 
	}	
	/**
	 * Convertit une chaine contenant un entier décrivant un nombre de minutes en une chaine
	 * décrivant une durée en HH:mm
	 *
	 * @param p_sValue entier en minutes
	 * @return une chainedécrivant une durée en HH:mm
	 */
	public static StringBuilder convertMinutesToDuration(String p_sValue){
		StringBuilder r_oStrBui = new StringBuilder();
		int iNbHours =(int) Math.floor( (Float.parseFloat(p_sValue) / 60 ) );
		int iNbMinutes = (int)( Long.parseLong(p_sValue) - (60 * iNbHours) );
		if ( iNbHours < 10 ){
			r_oStrBui.append('0').append(iNbHours).append(':') ; 
		}else {
			r_oStrBui.append(iNbHours).append(':') ;
		}			
		if ( iNbMinutes < 10 ){
			r_oStrBui.append('0').append(iNbMinutes) ; 
		}else {
			r_oStrBui.append(iNbMinutes) ;
		}
		return r_oStrBui ;
	}
	/**
	 * Convertit une chaine contenant un entier décrivant un nombre de minutes en une chaine
	 * décrivant une durée en HH:mm
	 *
	 * @param p_sValue entier en minutes
	 * @return une chaine décrivant une durée en HH:mm
	 */
	public static String convertMinutesToDuration(Long p_sValue){
		return convertMinutesToDuration(String.valueOf(p_sValue).trim()).toString();
	}
	/**
	 * Convertit une chaine de la forme hh:mm  en une durée en minutes
	 *
	 * @param p_sValue a {@link java.lang.String} object.
	 * @return a long containing the number of minutes in the duration time
	 */
	public static Long convertTimeToDurationInMinutes(String p_sValue){
		String[] oCutTime = p_sValue.trim().split(":");
		Long r_value = 0L;
		if (oCutTime.length == 2){
			if (oCutTime[0] != null && oCutTime[0].length() > 0) {
				r_value += Long.valueOf(oCutTime[0]) * MINUTES_IN_ONE_HOUR;
			}
			if (oCutTime[1] != null && oCutTime[1].length() > 0) {
				r_value += Long.valueOf(oCutTime[1]);
			}
			return r_value;
		} 
		if (oCutTime[0] != null && oCutTime[0].length() > 0) {
			r_value += Long.valueOf(oCutTime[0]);
		}
		return r_value;
	}
}
