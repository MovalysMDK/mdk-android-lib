package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay;

import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.genericdisplay.InDisplayParameter;

/**
 * load data for multiple display detail action parameter
 * @author lmichenaud
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
