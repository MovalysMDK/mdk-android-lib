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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * <p>NumberUtils class.</p>
 *
 */
public class NumberUtils {

	/**
	 * <p>parseDouble.</p>
	 *
	 * @param p_sDouble a {@link java.lang.String} object.
	 * @return a {@link java.lang.Double} object.
	 * @throws java.lang.NumberFormatException if any.
	 */
	public static final Double parseDouble( String p_sDouble ) throws NumberFormatException {
		return parseDouble(p_sDouble, null);
	}
	
	/**
	 * <p>parseDouble.</p>
	 *
	 * @param p_sDouble a {@link java.lang.String} object.
	 * @param p_dDefault a {@link java.lang.Double} object.
	 * @return a {@link java.lang.Double} object.
	 * @throws java.lang.NumberFormatException if any.
	 */
	public static final Double parseDouble( String p_sDouble, Double p_dDefault ) throws NumberFormatException {
		Double r_oDouble = p_dDefault ;
		if ( p_sDouble != null && p_sDouble.length() > 0 ) {
			r_oDouble = Double.parseDouble(p_sDouble);
		}
		return r_oDouble ;
	}

	/**
	 * Conversion d'une chaine de caractère en entier.
	 *
	 * @param p_sInt La chaine de caractère à convertir.
	 * @throws java.lang.NumberFormatException if any.
	 * @return a int.
	 */
	public static final int parseInt(final String p_sInt) throws NumberFormatException {
		return parseInt(p_sInt, 0);
	}

	/**
	 * <p>parseInt.</p>
	 *
	 * @param p_sInteger Chaine de caractères à parser.
	 * @param p_iDefault Valeur par défaut
	 * @return a int.
	 * @throws java.lang.NumberFormatException if any.
	 */
	public static final int parseInt(final String p_sInteger, int p_iDefault ) throws NumberFormatException {
		return parseInteger(p_sInteger, p_iDefault);
	}

	
	/**
	 * <p>parseInteger.</p>
	 *
	 * @param p_sInteger a {@link java.lang.String} object.
	 * @return a {@link java.lang.Integer} object.
	 * @throws java.lang.NumberFormatException if any.
	 */
	public static final Integer parseInteger( String p_sInteger ) throws NumberFormatException {
		return parseInteger(p_sInteger, null);
	}
	
	/**
	 * <p>parseInteger.</p>
	 *
	 * @param p_sInteger a {@link java.lang.String} object.
	 * @param p_dDefault a {@link java.lang.Integer} object.
	 * @return a {@link java.lang.Integer} object.
	 * @throws java.lang.NumberFormatException if any.
	 */
	public static final Integer parseInteger( String p_sInteger, Integer p_dDefault ) throws NumberFormatException {
		Integer r_oInteger = p_dDefault ;
		if ( p_sInteger != null && p_sInteger.length() > 0 ) {
			r_oInteger = Integer.parseInt(p_sInteger);
		}
		return r_oInteger ;
	}
	
	/**
	 * <p>parseLong.</p>
	 *
	 * @param p_sLong a {@link java.lang.String} object.
	 * @return a {@link java.lang.Long} object.
	 * @throws java.lang.NumberFormatException if any.
	 */
	public static final Long parseLong( String p_sLong ) throws NumberFormatException {
		return parseLong(p_sLong, null);
	}
	
	/**
	 * <p>parseLong.</p>
	 *
	 * @param p_sLong a {@link java.lang.String} object.
	 * @param p_dDefault a {@link java.lang.Long} object.
	 * @return a {@link java.lang.Long} object.
	 * @throws java.lang.NumberFormatException if any.
	 */
	public static final Long parseLong( String p_sLong, Long p_dDefault ) throws NumberFormatException {
		Long r_oLong = p_dDefault ;
		if ( p_sLong != null && p_sLong.length() > 0 ) {
			r_oLong = Long.parseLong(p_sLong);
		}
		return r_oLong ;
	}
	/**
	 * Cree un tableau d'entier contenant la partie entière et la partie décimale du double
	 *
	 * @param p_oDouble double à découper avec un point comme séparateur
	 * @return un tableau d'entier contenant la partie entière et la partie décimale du double
	 */
	public static int[] cutDouble(Double p_oDouble) {
		DecimalFormatSymbols oSymbols = new DecimalFormatSymbols();
		oSymbols.setDecimalSeparator('.');
		DecimalFormat oFormater = new DecimalFormat("0.0", oSymbols);
		String sDouble = oFormater.format(p_oDouble);
		int iPosPoint = sDouble.indexOf(".");
		if (iPosPoint > 0 ){
			return new int[] {
					Integer.parseInt(sDouble.substring(0, iPosPoint)),
					Integer.parseInt(sDouble.substring(iPosPoint+1))
			} ;
		} else if (iPosPoint == 0){
			return new int[] {
					0,
					Integer.parseInt(sDouble.substring(1))
			};
		} else {
			return new int[] {Integer.parseInt(sDouble),0} ;
		}		
	}
	/**
	 * Vérifie que l'on peut bien convertir le long en int sans modifier la valeur
	 *
	 * @param p_lValue valeur en long
	 * @return la même valeur en int ou une exception si le long est trop grand ou trop petit
	 */
	public static int safeLongToInt(long p_lValue) {
	    if (p_lValue < Integer.MIN_VALUE || p_lValue > Integer.MAX_VALUE) {
	        throw new IllegalArgumentException
	            (p_lValue + " cannot be cast to int without changing its value.");
	    }
	    return (int) p_lValue;
	}
}
