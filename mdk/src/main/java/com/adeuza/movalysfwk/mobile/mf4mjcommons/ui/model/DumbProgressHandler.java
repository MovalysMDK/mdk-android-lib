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
 * ProgressHandler that do nothing (empty implementation).
 * Used by Starter.runStandalone().
 *
 */
public class DumbProgressHandler implements ProgressHandler {

	/** {@inheritDoc} */
	@Override
	public void increaseProgress(ApplicationR p_oMessage) {
		// do nothing
	}

	/** {@inheritDoc} */
	@Override
	public void increaseProgress(int p_iLevel, ApplicationR p_oMessage) {
		// do nothing
	}

	/** {@inheritDoc} */
	@Override
	public void initProgressProcess(int p_iLevel, int p_iCurrentProgressStep, float p_fTotalStepCount) {
		// do nothing
	}

	/** {@inheritDoc} */
	@Override
	public void associateTextToProgressStep(String p_sText) {
		// do nothing
	}

	/** {@inheritDoc} */
	@Override
	public void increaseProgress(int p_iLevel, int p_iTotalStep, ApplicationR p_oMessage) {
		// Nothing to do
	}

	/** {@inheritDoc} */
	@Override
	public void increaseProgress(int p_iLevel, String p_sMessage) {
		
	}

	/** {@inheritDoc} */
	@Override
	public void increaseProgress(int p_iLevel, int p_iTotalStep, String p_oMessage) {
		
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ProgressHandler#resetProgress()
	 */
	@Override
	public void resetProgress() {
		
	}
}
