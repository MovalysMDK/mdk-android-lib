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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.messages;

/**
 *
 * <p>Contains error definition</p>
 *
 *
 */
public final class ErrorDefinition {

	/**
	 * This class has only static methods, so it is impossible to instantiate objects.
	 */
	private ErrorDefinition() {
		// NOTHING TO DO
	}
	
	/** defines a space used in message */
	public static final String SPACE = " ";
	/** defines a space used in point */
	public static final String POINT = ".";
	
	
	
	
	/* COMMON ERROR MESSAGE : Properties messages 0000 to 0099 */
	/** This error happens when the properties file is missing in classpath */
	public static final String FWK_MOBILE_E_0001 = "FWK_MOBILE_E_0001";
	/** Label of error {@link #FWK_MOBILE_E_0001} */
	public static final String FWK_MOBILE_E_0001_1_LABEL = "File properties ";
	/** Label of error {@link #FWK_MOBILE_E_0001} */
	public static final String FWK_MOBILE_E_0001_2_LABEL = " is missing in classpath.";
	
	/** this error happens when the property is not defined in properties file */
	public static final String FWK_MOBILE_E_0002 = "FWK_MOBILE_E_0002";
	/** Label of error {@link #FWK_MOBILE_E_0002} */
	public static final String FWK_MOBILE_E_0002_1_LABEL = "The property ";
	/** Label of error {@link #FWK_MOBILE_E_0002} */
	public static final String FWK_MOBILE_E_0002_2_LABEL = " is undefined in file ";

	/** This error happens when the ConfigurationsHandler is called and, it is not initialized. */
	public static final String FWK_MOBILE_E_0003 = "FWK_MOBILE_E_0003";
	/** Label of error {@link #FWK_MOBILE_E_0003} */
	public static final String FWK_MOBILE_E_0003_LABEL = "The ConfigurationsHandler has not been initialized.";

	/** This errors happens when the value of an {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR} is not defined. */
	public static final String FWK_MOBILE_E_0004 = "FWK_MOBILE_E_0004";
	/** Labell of error {@link #FWK_MOBILE_E_0004} */
	public static final String FWK_MOBILE_E_0004_LABEL = "Impossible to retreive the value of the constant named ";
	
	
	
	
	
	/* COMMON ERROR MESSAGE : Introspection messages 0100 to 0199 */
	/** this error happens when it is impossible to instantiate a class */
	public static final String FWK_MOBILE_E_0100 = "FWK_MOBILE_E_0100";
	/** Label of error {@link #FWK_MOBILE_E_0100} */
	public static final String FWK_MOBILE_E_0100_LABEL = "Impossible to instanciate class ";
	
	/** this error happens when it is impossible to instantiate a class */
	public static final String FWK_MOBILE_E_0101 = "FWK_MOBILE_E_0101";
	/** Label of error {@link #FWK_MOBILE_E_0101} */
	public static final String FWK_MOBILE_E_0101_LABEL = "Impossible to invoke method ";
	
	/** This error happens when java code attempts to access an attribute */
	public static final String FWK_MOBILE_E_0110 = "FWK_MOBILE_E_0110";
	/** Label of error {@link #FWK_MOBILE_E_0110} */
	public static final String FWK_MOBILE_E_0110_LABEL = "Impossible to access attribute";
	
	/** this error happens when it is impossible to find a method */
	public static final String FWK_MOBILE_E_0120 = "FWK_MOBILE_E_0120";
	/** Label of error {@link #FWK_MOBILE_E_0120} */
	public static final String FWK_MOBILE_E_0120_LABEL = "Impossible to find method";
	
	/** FWK-MOBILE-E-104 - The transaction does not finish correctly... */
	public static final String CODE_ENDING_TRANSACTION_ERROR = "FWK-MOBILE-E-104";
	/** label of error FWK-MOBILE-E-104<br> - The transaction does not finish correctly... */
	public static final String LABEL_ENDING_TRANSACTION_ERROR = "The transaction does not finish correctly...";
	
	/** FWK-MOBILE-E-105 - no transaction exists. Unable to close it ... */
	public static final String CODE_NO_TRANSACTION_EXIST = "FWK-MOBILE-E-105";
	/** label of error FWK-MOBILE-E-104<br> - no transaction exists. Unable to close it ... */
	public static final String LABEL_NO_TRANSACTION_EXIST = "no transaction exists. Unable to close it";
	
	
	
	
	/* COMMON ERROR MESSAGE : FREE 0200 to 0999 */


	
	
	/* COMMON ERROR MESSAGE : Starting message 1000 to 1099 */
	/** this error happens when application is not correctly initialized.*/
	public static final String FWK_MOBILE_E_1000 = "FWK_MOBILE_E_1000";
	/** Label of error {@link #FWK_MOBILE_E_0001} */
	public static final String FWK_MOBILE_E_1000_LABEL  = "Initialize must be initialized.";
	
	/** this error happens when the logger handler is not correctly initialized. */
	public static final String FWK_MOBILE_E_1001 = "FWK_MOBILE_E_1001";
	/** Label of error {@link #FWK_MOBILE_E_1001} */
	public static final String FWK_MOBILE_E_1001_LABEL = "Logger must be configured at application  starting.";

	/** this error happens when a EntityDefinition is not initialized. */
	public static final String FWK_MOBILE_E_1002 = "FWK_MOBILE_E_1002";
	/** Label of error {@link #FWK_MOBILE_E_1002} */
	public static final String FWK_MOBILE_E_1002_LABEL = "Impossible to retrieve a EntityDefinition for class: ";

	/** this error happens when the dao handler is not correcty initialized. */
	public static final String FWK_MOBILE_E_1003 = "FWK_MOBILE_E_1003";
	/** Label of error {@link #FWK_MOBILE_E_1003} */
	public static final String FWK_MOBILE_E_1003_LABEL = "Dao must be configured at application  starting.";

	
	
	
	/*  COMMON ERROR MESSAGE : FREE 1100 to 1999 */
	

	
	
	/* COMMON ERROR MESSAGE : SQL Messages 2000 to 2499 */
	/** This error happens when the execution of a SQLite query thrown an exception */
	public static final String FWK_MOBILE_E_2000 = "FWK_MOBILE_E_2000";
	/** Label of error {@link #FWK_MOBILE_E_2000} */
	public static final String FWK_MOBILE_E_2000_LABEL = "Error during the execution of SQLite query";

	
	
	
	
	/* COMMON ERROR MESSAGE : Model Messages 2500 to 2999 */	
	/**
	 * This error happens when the primary key of an customizable entity is composed of more than attribute.
	 * The database structure requires that the primary key is reduced to an integer
	 */
	public static final String FWK_MOBILE_E_2500 = "FWK_MOBILE_E_2500";
	/** Label of error {@link #FWK_MOBILE_E_2500} */
	public static final String FWK_MOBILE_E_2500_LABEL = "The primary key of a custom entity can not be composed of more than one column";
	
	/**
	 * This error happens When the number of parameters does not match the number of columns
	 * composing the primary key of an entity.
	 */
	public static final String FWK_MOBILE_E_2501 = "FWK_MOBILE_E_2501";
	/** Label of error {@link #FWK_MOBILE_E_2501} */
	public static final String FWK_MOBILE_E_2501_LABEL = "The number of columns composing the primary key doesn't match the number of provided parameters";

	/**
	 * This error happens when an entity of business model has not primary key.
	 */
	public static final String FWK_MOBILE_E_2502 = "FWK_MOBILE_E_2502";
	/** Label of error {@link #FWK_MOBILE_E_2502} */
	public static final String FWK_MOBILE_E_2502_LABEL = "An entity must have a primary key : ";
	
	
	
	
	/* COMMON ERROR MESSAGE : view model & view 3000 to 3499 */
	/** This error happens when the it's impossible to bind a view with its view model */
	public static final String FWK_MOBILE_E_30050 = "FWK_MOBILE_E_30050";
	/** Label of error {@link #FWK_MOBILE_E_30050} */
	
	public static final String FWK_MOBILE_E_3050_LABEL = "Impossible de bind a view and its view model";
	/** This error happens when the application doesn't recognize the type of a custom field. */
	public static final String FWK_MOBILE_E_3051 = "FWK_MOBILE_E_3051";
	/** Label of error {@link #FWK_MOBILE_E_3051} */
	public static final String FWK_MOBILE_E_3051_LABEL = "Unkown custom field type";
	
	/** This error happens when an unknown MPhotoState identifier is asked */
	public static final String FWK_MOBILE_E_3052 = "FWK_MOBILE_E_3052";
	/** Label of error {@link #FWK_MOBILE_E_3052} */
	public static final String FWK_MOBILE_E_3052_LABEL = " is a unknown photoState identifier";

	
	
	
	/* COMMON ERROR MESSAGE : connection & synchronization 3500 to 3999 */
	/** This error happens when its impossible to join server */
	public static final String FWK_MOBILE_E_3500 = "FWK_MOBILE_E_3500";
	/** Label of error {@link #FWK_MOBILE_E_3500} */
	public static final String FWK_MOBILE_E_3500_LABEL = "Impossible to join server";
	
	/** This error happens when server returns an error */
	public static final String FWK_MOBILE_E_3501 = "FWK_MOBILE_E_3501";
	/** Label of error {@link #FWK_MOBILE_E_3501} */
	public static final String FWK_MOBILE_E_3501_LABEL = "Server in error";

	/** This error happens when mobile is not connected */
	public static final String FWK_MOBILE_E_3502 = "FWK_MOBILE_E_3502";
	/** Label of error {@link #FWK_MOBILE_E_3502} */
	public static final String FWK_MOBILE_E_3502_LABEL = "Device not connected";
	
}
