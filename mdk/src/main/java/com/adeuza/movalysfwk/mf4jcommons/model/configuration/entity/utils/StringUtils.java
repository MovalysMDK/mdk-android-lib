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
package com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils;

/**
 * <p>Service to manipulate String.</p>
 *
 *
 */
public final class StringUtils {

	/**
	 * To retreive all non ascii char in a string.
	 */
	public static final String NOT_ASCII_REGEX = "[^\\p{ASCII}]";

	/** the empty string */
	public static final String EMPTY = "";

	/**
	 * This class has only static methods, so it is impossible to instantiate objects.
	 */
	private StringUtils() {
		//Nothing To Do
	}

	/**
	 * Concatenates string using a StringBuilder.
	 *
	 * @param p_oStrings string to concatenate
	 * @return the concatened string.
	 */
	public static String concat(String...p_oStrings) {
		StringBuilder oBuilder = new StringBuilder();
		for(String sString : p_oStrings) {
			if (sString!=null) {
			oBuilder.append(sString);
		}
		}
		return oBuilder.toString();
	}

	/**
	 * <p>
	 * 	Concatenates string using a StringBuilder.
	 * </p>
	 *
	 * @param p_sACharacter the first caracter to concatenate.
	 * @param p_oStrings all the other string values tio concatenate.
	 * @return the concatenate string.
	 */
	public static String concat(char p_sACharacter, String...p_oStrings) {
		StringBuilder oBuilder = new StringBuilder();

		oBuilder.append(p_sACharacter);

		for(String sString : p_oStrings) {
			oBuilder.append(sString);
		}
		return oBuilder.toString();
	}

	/**
	 * <p>
	 * 	Concatenates string using a StringBuilder.
	 * </p>
	 *
	 * @param p_s1stString the first string to concatenate.
	 * @param p_sACharacter a caracter to concatenate.
	 * @param p_oStrings a list of string values to concatenate.
	 * @return the concatenate string.
	 */
	public static String concat(String p_s1stString, char p_sACharacter, String...p_oStrings) {
		StringBuilder oBuilder = new StringBuilder();

		oBuilder.append(p_s1stString);
		oBuilder.append(p_sACharacter);

		for(String sString : p_oStrings) {
			oBuilder.append(sString);
		}
		return oBuilder.toString();
	}

	/**
	 * <p>
	 * 	Concatenates string using a StringBuilder.
	 * </p>
	 *
	 * @param p_s1stString the first string to concatenate.
	 * @param p_s2ndString the seccond string to concatenate.
	 * @param p_sACharacter a caracter to concatenate.
	 * @param p_oStrings a list of string values to concatenate.
	 * @return the concatenate string.
	 */
	public static String concat(String p_s1stString, String p_s2ndString, char p_sACharacter, String...p_oStrings) {
		StringBuilder oBuilder = new StringBuilder();

		oBuilder.append(p_s1stString);
		oBuilder.append(p_s2ndString);
		oBuilder.append(p_sACharacter);

		for(String sString : p_oStrings) {
			oBuilder.append(sString);
		}
		return oBuilder.toString();
	}

	/**
	 * Removes from a String accented letters and replace them by their regular ASCII equivalent.
	 *
	 * @param p_sOneString The string to treat
	 * @return A string without accent.
	 */
	public static String normalize(String p_sOneString) {
		return java.text.Normalizer.normalize(p_sOneString, java.text.Normalizer.Form.NFD)
					.replaceAll(NOT_ASCII_REGEX, StringUtils.EMPTY);
	}

	/**
	 * Splits a string to get an array of String having a fixed length.
	 *
	 * @param p_sAString
	 * 		The string to split
	 * @param p_iMaxLength
	 * 		The max length of a String in the result. The integer must be greater than 0.
	 * @return an array of {@link java.lang.String} objects : The length of each String is smaller than p_iMaxLength or equal to p_iMaxLength
	 */
	public static String[] split(String p_sAString, int p_iMaxLength) {
		String[] r_t_sSplittedString = null;

		if (p_sAString != null && p_iMaxLength > 0) {
			int iArraySize = p_sAString.length() / p_iMaxLength;
			if (p_sAString.length() % p_iMaxLength > 0) {
				iArraySize++;
			}

			r_t_sSplittedString = new String[iArraySize];

			for (int i = 0; i < iArraySize; i++) {
				r_t_sSplittedString[i] = p_sAString.substring(i * p_iMaxLength,
						Math.min((i +1) * p_iMaxLength, p_sAString.length()));
			}
		}
		return r_t_sSplittedString;
	}
	

    /**
     * <p>Capitalizes all the whitespace separated words in a String.
     * Only the first letter of each word is changed. To convert the
     * rest of each word to lowercase at the same time,
     * use {@link #capitalizeFully(String)}.</p>
     *
     * <p>Whitespace is defined by {@link java.lang.Character#isWhitespace(char)}.
     * A <code>null</code> input String returns <code>null</code>.
     * Capitalization uses the unicode title case, normally equivalent to
     * upper case.</p>
     *
     * <pre>
     * StringUtils.capitalize(null)        = null
     * StringUtils.capitalize("")          = ""
     * StringUtils.capitalize("i am FINE") = "I Am FINE"
     * </pre>
     *
     * @param p_sStr  the String to capitalize, may be null
     * @return capitalized String, <code>null</code> if null String input
     */
    public static String capitalize(String p_sStr) {
        return capitalize(p_sStr, null);
    }

    /**
     * <p>Capitalizes all the delimiter separated words in a String.
     * Only the first letter of each word is changed. To convert the
     * rest of each word to lowercase at the same time,
     * use {@link #capitalizeFully(String, char[])}.</p>
     *
     * <p>The delimiters represent a set of characters understood to separate words.
     * The first string character and the first non-delimiter character after a
     * delimiter will be capitalized. </p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.
     * Capitalization uses the unicode title case, normally equivalent to
     * upper case.</p>
     *
     * <pre>
     * StringUtils.capitalize(null, *)            = null
     * StringUtils.capitalize("", *)              = ""
     * StringUtils.capitalize(*, new char[0])     = *
     * StringUtils.capitalize("i am fine", null)  = "I Am Fine"
     * StringUtils.capitalize("i aM.fine", {'.'}) = "I aM.Fine"
     * </pre>
     *
     * @param p_sStr  the String to capitalize, may be null
     * @param p_sDelimiters  set of characters to determine capitalization, null means whitespace
     * @return capitalized String, <code>null</code> if null String input
     */
    public static String capitalize(String p_sStr, char[] p_sDelimiters) {
    	int iDelimLen = -1;
    	if (p_sDelimiters != null){
        	iDelimLen = p_sDelimiters.length;
        }
        if (p_sStr == null || p_sStr.length() == 0 || iDelimLen == 0) {
            return p_sStr;
        }
        int iStrLen = p_sStr.length();
        StringBuilder oBuffer = new StringBuilder(iStrLen);
        boolean bCapitalizeNext = true;
        for (int i = 0; i < iStrLen; i++) {
            char sCh = p_sStr.charAt(i);

            if (isDelimiter(sCh, p_sDelimiters)) {
                oBuffer.append(sCh);
                bCapitalizeNext = true;
            } else if (bCapitalizeNext) {
                oBuffer.append(Character.toTitleCase(sCh));
                bCapitalizeNext = false;
            } else {
                oBuffer.append(sCh);
            }
        }
        return oBuffer.toString();
    }
    
    /**
     * <p>Uncapitalizes all the whitespace separated words in a String.
     * Only the first letter of each word is changed.</p>
     *
     * <p>Whitespace is defined by {@link java.lang.Character#isWhitespace(char)}.
     * A <code>null</code> input String returns <code>null</code>.</p>
     *
     * <pre>
     * StringUtils.uncapitalize(null)        = null
     * StringUtils.uncapitalize("")          = ""
     * StringUtils.uncapitalize("I Am FINE") = "i am fINE"
     * </pre>
     *
     * @param p_sStr  the String to uncapitalize, may be null
     * @return uncapitalized String, <code>null</code> if null String input
     * @see #capitalize(String)
     */
    public static String uncapitalize(String p_sStr) {
        return uncapitalize(p_sStr, null);
    }

    /**
     * <p>Uncapitalizes all the whitespace separated words in a String.
     * Only the first letter of each word is changed.</p>
     *
     * <p>The delimiters represent a set of characters understood to separate words.
     * The first string character and the first non-delimiter character after a
     * delimiter will be uncapitalized. </p>
     *
     * <p>Whitespace is defined by {@link java.lang.Character#isWhitespace(char)}.
     * A <code>null</code> input String returns <code>null</code>.</p>
     *
     * <pre>
     * StringUtils.uncapitalize(null, *)            = null
     * StringUtils.uncapitalize("", *)              = ""
     * StringUtils.uncapitalize(*, null)            = *
     * StringUtils.uncapitalize(*, new char[0])     = *
     * StringUtils.uncapitalize("I AM.FINE", {'.'}) = "i AM.fINE"
     * </pre>
     *
     * @param p_sStr  the String to uncapitalize, may be null
     * @param p_sDelimiters  set of characters to determine uncapitalization, null means whitespace
     * @return uncapitalized String, <code>null</code> if null String input
     * @see #capitalize(String)
     */
    public static String uncapitalize(String p_sStr, char[] p_sDelimiters) {
        int iDelimLen = -1;
        if (p_sDelimiters != null ){
        	iDelimLen = p_sDelimiters.length;
        }
        if (p_sStr == null || p_sStr.length() == 0 || iDelimLen == 0) {
            return p_sStr;
        }
        int iStrLen = p_sStr.length();
        StringBuilder oBuffer = new StringBuilder(iStrLen);
        boolean bUncapitalizeNext = true;
        for (int i = 0; i < iStrLen; i++) {
            char sCh = p_sStr.charAt(i);

            if (isDelimiter(sCh, p_sDelimiters)) {
                oBuffer.append(sCh);
                bUncapitalizeNext = true;
            } else if (bUncapitalizeNext) {
                oBuffer.append(Character.toLowerCase(sCh));
                bUncapitalizeNext = false;
            } else {
                oBuffer.append(sCh);
            }
        }
        return oBuffer.toString();
    }
    
    /**
     * <p>Is the character a delimiter.</p>
     * @param p_sCh  the character to check
     * @param p_sDelimiters  the delimiters
     * @return true if it is a delimiter
     */
    private static boolean isDelimiter(char p_sCh, char[] p_sDelimiters) {
        boolean r_bResult = false;
    	if (p_sDelimiters == null) {
    		r_bResult = Character.isWhitespace(p_sCh);
        }else{
	        for (int i = 0, iSize = p_sDelimiters.length; i < iSize; i++) {
	            if (p_sCh == p_sDelimiters[i]) {
	                r_bResult = true;
	            }
	        }
        }
        return r_bResult;
    }

}
