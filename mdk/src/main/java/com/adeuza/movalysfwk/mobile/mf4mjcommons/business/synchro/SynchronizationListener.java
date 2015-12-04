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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>
 * 	Listener for the sync.
 * 	This interface defines methods to dispatch start, progress and stop event.
 * </p>
 *
 *
 * @since Barcelone (26 nov. 2010)
 */
public interface SynchronizationListener {

	/** the name of the method doOnStartProcess */
	public static final String M_DO_ON_START_PROCESS = "doOnStartProcess";
	/** the name of the method doOnSynchroChanged */
	public static final String M_DO_ON_SYNCHRO_CHANGED = "doOnSynchroChanged";
	/** Constant <code>M_DO_ON_SYNCHRO_CHANGED2="doOnSynchroChanged2"</code> */
	public static final String M_DO_ON_SYNCHRO_CHANGED2 = "doOnSynchroChanged2";
	/** Constant <code>M_DO_ON_SYNCHRO_RESETPROGRESS="doOnResetProgress"</code> */
	public static final String M_DO_ON_SYNCHRO_RESETPROGRESS = "doOnResetProgress";
	/** the name of the method doOnStopProcess */
	public static final String M_DO_ON_STOP_PROCESS = "doOnStopProcess";
	
	/**
	 * <p>
	 * 	This event is displayed when a new synchro Process start.
	 * </p>
	 *
	 * @param p_iLevel
	 * 				The level of the progress bar widget. '0' is the top
	 * @param p_iCurrentProgressStep
	 * 				The current progress step. It will be often at 0.
	 * @param p_fTotalStepCount
	 * 				The number of step to progress.
	 */
	public void doOnStartProcess(int p_iLevel, int p_iCurrentProgressStep, float p_fTotalStepCount);
	
	/**
	 * <p>doOnSynchroChanged.</p>
	 *
	 * @param p_iLevel a int.
	 * @param p_sMessage a {@link java.lang.String} object.
	 */
	public void doOnSynchroChanged(int p_iLevel, String p_sMessage);
		
	/**
	 * <p>
	 * 	This event is displayed when the step of a synchro's progress bar changed.
	 * </p>
	 *
	 * @param p_iLevel
	 * 				The level of the progress bar widget. '0' is the top
	 * @param p_iTotalStep a int.
	 * @param p_sMessage a {@link java.lang.String} object.
	 */
	public void doOnSynchroChanged2(int p_iLevel, int p_iTotalStep, String p_sMessage);
	
	/**
	 * <p>
	 * 	This event is displayed when a synchro is finnish.
	 * </p>
	 *
	 * @param p_oContext a com$adeuza$movalysfwk$mobile$mf4mjcommons$context$MContext object.
	 * @param p_oInformation a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronisationResponseTreatmentInformation} object.
	 */
	public void doOnStopProcess(MContext p_oContext, SynchronisationResponseTreatmentInformation p_oInformation);
	
	/**
	 * <p>doOnResetProgress.</p>
	 */
	public void doOnResetProgress();
}
