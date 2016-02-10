package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Implémentation générique d'une action d'affichage du détail d'une entité.</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @since MF-Annapurna
 */
public abstract class AbstractUpdateVMForDisplayDetailActionImpl extends AbstractTaskableAction<InUpdateVMParameter, NullActionParameterImpl, DefaultActionStep, Void>{

	/**
	 * update ViewModel
	 * @param p_oContext context
	 * @param p_oParam update VM IN param
	 */
	protected abstract void updateVM(MContext p_oContext, InUpdateVMParameter p_oParam);
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPreExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext)
	 */
	@Override
	public void doPreExecute(InUpdateVMParameter p_oInUpdateVMParameter, MContext p_oContext) throws ActionException {
		//
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doAction(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public NullActionParameterImpl doAction(MContext p_oContext, InUpdateVMParameter p_oParam) throws ActionException {
		this.updateVM(p_oContext,p_oParam);
		return new NullActionParameterImpl();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doOnError(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public NullActionParameterImpl doOnError(MContext p_oContext, InUpdateVMParameter p_oParameterIn, NullActionParameterImpl p_oResultOut)
			throws ActionException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#isDataBaseAccessAction()
	 */
	@Override
	public boolean isDataBaseAccessAction() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#isWritableDataBaseAccessAction()
	 */
	@Override
	public boolean isWritableDataBaseAccessAction() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#isConcurrentAction()
	 */
	@Override
	public boolean isConcurrentAction() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#getEmptyInParameter()
	 */
	@Override
	public InUpdateVMParameter getEmptyInParameter() {
		return new InUpdateVMParameter();
	}
}
