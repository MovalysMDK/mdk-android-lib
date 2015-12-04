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
package com.adeuza.movalysfwk.mobile.mf4android.ui.command;


/**
 * interface to implements to provide a formated text to the bluetooth printer
 *
 */
public interface PrintableViewModel {

	/**
	 * Method to implements to return the formated text to be print
	 * @return the formated text with the escape characters conform to the printer 
	 */
	public String[] getEscTexttoPrint();
	
	/**
	 * Method to implements to return the formated text to be print
	 * @return the formated text with the escape characters conform to the printer 
	 */
	public String getEscLinetoPrint(int p_iLineToPrint);
	
	
	/**
	 * Method to implement to return the reference of object printed
	 * this reference is displayed in the print dialog for the end user
	 * @return the reference to display
	 */
	public String getReference();
}
