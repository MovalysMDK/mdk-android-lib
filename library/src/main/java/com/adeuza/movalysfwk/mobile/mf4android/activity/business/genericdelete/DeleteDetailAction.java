package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdelete;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.EntityActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * delete detail action interface
 * @author lmichenaud
 *
 * @param <ITEM> entity
 * @param <STATE> action step
 * @param <PROGRESS> progress obj
 */
public interface DeleteDetailAction<ITEM extends MEntity,
	STATE extends ActionStep, 
	PROGRESS extends Object> 
	extends Action<NullActionParameterImpl, EntityActionParameterImpl<ITEM>, STATE, PROGRESS> {

	/**
	 * Save the entity.
	 * @param p_oContext context
	 * @param p_oInParameter NULL action IN param
	 * @return item
	 * @throws ActionException error
	 */
	public ITEM deleteData(MContext p_oContext, NullActionParameterImpl p_oInParameter) throws ActionException;

}
