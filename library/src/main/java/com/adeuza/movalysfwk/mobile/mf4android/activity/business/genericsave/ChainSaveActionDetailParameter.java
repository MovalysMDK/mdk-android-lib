package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericsave;

import java.util.Arrays;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;

/**
 * chain save action detail parameter
 * @author lmichenaud
 *
 */
public class ChainSaveActionDetailParameter extends AbstractActionParameter implements ActionParameter {

	/**
	 * serializable
	 */
	private static final long serialVersionUID = 9019264544831171952L;

	/**
	 * save detail actions
	 */
	private List<Class<? extends SaveDetailAction<?,?,?>>> saveDetailActions ;

	/**
	 * Paramètre à fournir aux actions de sauvegarde. 
	 */
	private NullActionParameterImpl saveDetailActionParameterIn;
	
	/**
	 * exit mode
	 */
	private boolean exitMode = false;

	/**
	 * constructor
	 * @param p_oAction action
	 */
	public ChainSaveActionDetailParameter(Class<? extends SaveDetailAction<?,?,?>>... p_oAction) {
		this.saveDetailActions = Arrays.asList(p_oAction);
	}
	
	/**
	 * constructor
	 * @param p_oParameterIn IN parameter
	 * @param p_oAction save detail action
	 */
	public ChainSaveActionDetailParameter(NullActionParameterImpl p_oParameterIn, Class<? extends SaveDetailAction<?,?,?>>... p_oAction) {
		this(p_oAction);
		this.saveDetailActionParameterIn = p_oParameterIn;
	}

	/**
	 * getter
	 * @return save detail actions
	 */
	public List<Class<? extends SaveDetailAction<?,?,?>>> getSaveDetailActions() {
		return this.saveDetailActions;
	}

	/**
	 * GETTER
	 * @return save detail action IN parameters
	 */
	public NullActionParameterImpl getSaveDetailActionsParameterIn() {
		return this.saveDetailActionParameterIn;
	}

	/**
	 * GET the exit mode
	 * @return true if this action is launched before to exit
	 */
	public boolean isExitMode() {
		return this.exitMode;
	}

	/**
	 * SET the exit mode
	 * @param p_bExitMode true if the action is launched beafore exiting
	 */
	public void setExitMode(boolean p_bExitMode) {
		this.exitMode = p_bExitMode;
	}
}
