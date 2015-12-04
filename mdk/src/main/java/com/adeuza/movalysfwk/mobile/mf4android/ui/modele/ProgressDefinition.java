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
package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;


/**
 * <p>
 * 	Object to centralize the information for calculating the progression of a treatment on Movalys.
 * 	The two variables are:
 * 	<ul>
 * 		<li>the number of steps throughout treatment.</li>
 * 		<li>the number of the step at which we arrived.</li>
 * 	</ul> 
 * </p>
 *
 *
 * @since Barcelone (26 nov. 2010)
 */
public class ProgressDefinition {
	
	/**
	 * The actual step of a progress bar.
	 */
	private int progressStepCount = 0;
	/**
	 * The number of step of a progress bar.
	 */
	private float totalProgressStepCount = 0;
	
	private String message = null;
	
	/** 
	 * <p>
	 *  Construct an object <em>ProgressDefinition</em>.
	 * </p>
	 */
	public ProgressDefinition() {
		//Nothing to do
	}

	/** 
	 * <p>
	 *  Construct an object <em>ProgressDefinition</em> with parameters.
	 * </p>
	 * 
	 * @param p_oProgressStepCount
	 * 				The actual step of a progress bar.
	 * 
	 * @param p_oTotalProgressStepCount
	 * 				The number of step of a progress bar.
	 */
	public ProgressDefinition(int p_oProgressStepCount, float p_oTotalProgressStepCount) {
		super();
		this.progressStepCount = p_oProgressStepCount;
		this.totalProgressStepCount = p_oTotalProgressStepCount;
	}

	/**
	 * <p>Return the object <em>progressStepCount</em></p>
	 * @return Objet progressStepCount
	 */
	public int getProgressStepCount() {
		return this.progressStepCount;
	}

	/**
	 * <p>Set the object <em>progressStepCount</em></p>. 
	 * @param p_oProgressStepCount Objet progressStepCount
	 */
	public void setProgressStepCount(int p_oProgressStepCount, String p_sMessage) {
		this.progressStepCount = p_oProgressStepCount;
		this.message = p_sMessage;
	}

	/**
	 * <p>Return the object <em>totalProgressStepCount</em></p>
	 * @return Objet totalProgressStepCount
	 */
	public float getTotalProgressStepCount() {
		return this.totalProgressStepCount;
	}
	
	public String getMessage() {
		if (this.message != null) {
			return this.message;
		}
		else {
			return "";
		}
	}
	
	public float getValue() {
		int iStep = this.getProgressStepCount();
		if (iStep == Integer.MAX_VALUE) {
			float fProgress = Float.MAX_VALUE;
			return fProgress;
		}
		else {
			float fProgress = iStep / this.getTotalProgressStepCount();
			return fProgress;
		}
	}

	/**
	 * <p>Set the object <em>totalProgressStepCount</em></p>. 
	 * @param p_oTotalProgressStepCount Objet totalProgressStepCount
	 */
	public void setTotalProgressStepCount(float p_oTotalProgressStepCount) {
		this.totalProgressStepCount = p_oTotalProgressStepCount;
	}
	
}
