package com.adeuza.movalysfwk.mobile.mf4android.ui.command;


/**
 * interface to implements to provide a formated text to the bluetooth printer
 * @author dmaurange
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
