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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;


/**
 * <p>Interface for asynchronous task that manage some progress bar.</p>
 *
 *
 * @since Barcelone (18 nov. 2010)
 */
public interface ProgressHandler {
	
	/**
	 * <p>
	 * 	Method to increment the progress of a progress bar.
	 * </p>
	 *
	 * @param p_oMessage a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR} object.
	 */
	public void increaseProgress(ApplicationR p_oMessage);
	/**
	 * <p>
	 * 	Method to increment the progress of a progress bar.
	 * 	If you pass "0" in parameter, this corresponds to the call of the doIncreaseProgress() method.
	 * 	If you pass "1" in parameter, it means that the progressBar to increase is an under-representation of another progress bar.
	 * 	In that case, the second bar will be increase instead of the first one.
	 * </p>
	 *
	 * @param p_iLevel
	 * 			level of a progress bar widget. 0 is the top level.
	 * @param p_oMessage a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR} object.
	 */
	public void increaseProgress(int p_iLevel, ApplicationR p_oMessage);
	
	/**
	 * Method to increment the progress of a progress bar
	 *
	 * @param p_iLevel bar level to update
	 * @param p_iTotalStep total step of bar update
	 * @param p_oMessage associated message
	 */
	public void increaseProgress(int p_iLevel, int p_iTotalStep, ApplicationR p_oMessage);
	/**
	 * <p>
	 * 	Method to initialize the progress of a progress bar.
	 * 	If you pass "0" in parameter, this corresponds to the call of the doIncreaseProgress() method.
	 * 	If you pass "1" in parameter, it means that the progressBar to increase is an under-representation of another progress bar.
	 * 	In that case, the second bar will be increase instead of the first one.
	 * </p>
	 *
	 * @param p_iLevel
	 * 				The level of the progress bar widget. '0' is the top
	 * @param p_iCurrentProgressStep
	 * 				The current progress step. It will be often at 0.
	 * @param p_fTotalStepCount
	 * 				The number of step to progress.
	 */
	public void initProgressProcess(int p_iLevel, int p_iCurrentProgressStep, float p_fTotalStepCount);
	/**
	 * <p>
	 * 	This Method associate a text to a working step.
	 * 	This definition explain the goal of this step.
	 * </p>
	 *
	 * @param p_sText
	 * 				Meaning of a progress step.
	 */
	public void associateTextToProgressStep(String p_sText);
	
	/**
	 * <p>increaseProgress.</p>
	 *
	 * @param p_iLevel a int.
	 * @param p_sMessage a {@link java.lang.String} object.
	 */
	public void increaseProgress(int p_iLevel, String p_sMessage);
	
	/**
	 * <p>increaseProgress.</p>
	 *
	 * @param p_iLevel a int.
	 * @param p_iTotalStep a int.
	 * @param p_oMessage a {@link java.lang.String} object.
	 */
	public void increaseProgress(int p_iLevel, int p_iTotalStep, String p_oMessage);
	
	/**
	 * <p>resetProgress.</p>
	 */
	public void resetProgress();
}
