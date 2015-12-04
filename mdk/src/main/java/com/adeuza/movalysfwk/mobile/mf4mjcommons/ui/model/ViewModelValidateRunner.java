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

import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.common.AbstractUIRunnable;

/**
 * Runnable for view model validation in UI thread
 */
public class ViewModelValidateRunner extends AbstractUIRunnable {
	/** view model to modify */
	private AbstractViewModel viewmodel;
	
	/** parameters to use */
	private Map<String, Object> parameters;
	
	/** true if there is an error */
	private boolean error;

	/**
	 * Constructor
	 *
	 * @param p_oViewModel to analyse
	 * @param p_mapParameters parameters to use
	 */
	public ViewModelValidateRunner(AbstractViewModel p_oViewModel, Map<String, Object> p_mapParameters) {
		this.viewmodel = p_oViewModel;
		this.parameters = p_mapParameters;
		this.error = false;
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void exec() {
		this.error = this.viewmodel.validComponents(null, this.parameters);
	}

	/**
	 * Wether there is an error
	 *
	 * @return true if there is an error
	 */
	public final boolean hasError() {
		return this.error;
	}
}
