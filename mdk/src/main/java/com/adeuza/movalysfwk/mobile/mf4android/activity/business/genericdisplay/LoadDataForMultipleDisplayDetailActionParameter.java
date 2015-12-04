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

import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.genericdisplay.InDisplayParameter;

/**
 * load data for multiple display detail action parameter
 *
 */
public class LoadDataForMultipleDisplayDetailActionParameter extends AbstractActionParameter implements ActionParameter {

	/**
	 * Serial UID 
	 */
	private static final long serialVersionUID = 3766460163865711965L;
	
	/**
	 * 
	 */
	private List<InDisplayParameter> displayInParameters ;
	
	/**
	 * constructor
	 */
	public LoadDataForMultipleDisplayDetailActionParameter() {
		this.displayInParameters = new ArrayList<>();
	}
	
	/**
	 * load data for multiple display detail action parameter
	 * @param p_listDisplayInParameters list of display IN parameters
	 */
	public LoadDataForMultipleDisplayDetailActionParameter( List<InDisplayParameter> p_listDisplayInParameters) {
		this.displayInParameters = p_listDisplayInParameters ;
	}
	
	/**
	 * add display parameter
	 * @param p_oDisplayParameter display parameter
	 */
	public void addDisplayParameter( InDisplayParameter p_oDisplayParameter ) {
		this.displayInParameters.add(p_oDisplayParameter);
	}

	/**
	 * GETTER of display IN params
	 * @return list of IN params
	 */
	public List<InDisplayParameter> getDisplayInParameters() {
		return displayInParameters;
	}
}
