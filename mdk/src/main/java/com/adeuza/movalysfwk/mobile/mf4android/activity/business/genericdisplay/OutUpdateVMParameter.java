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
package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay;

import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * update VM OUT param
 */
public class OutUpdateVMParameter extends AbstractActionParameter implements ActionParameter {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 274849204986327815L;

	/**
	 * data loader
	 */
	private Class<? extends Dataloader<?>> dataloader ;

	/**
	 * view model class
	 */
	private Class<? extends ViewModel> viewModel;
	
	/**
	 * GETTER for data loader
	 * @return data loader
	 */
	public Class<? extends Dataloader<?>> getDataloader() {
		return dataloader;
	}

	/**
	 * SETTER of data loader
	 * @param p_sDataloader data loader
	 */
	public void setDataloader(Class<? extends Dataloader<?>> p_sDataloader) {
		this.dataloader = p_sDataloader;
	}

	/**
	 * returns the view model
	 * @return the view model
	 */
	public Class<? extends ViewModel> getViewModel() {
		return viewModel;
	}

	/**
	 * sets the view model
	 * @param p_oViewModel the view model
	 */
	public void setViewModel(Class<? extends ViewModel> p_oViewModel) {
		this.viewModel = p_oViewModel;
	}
}
