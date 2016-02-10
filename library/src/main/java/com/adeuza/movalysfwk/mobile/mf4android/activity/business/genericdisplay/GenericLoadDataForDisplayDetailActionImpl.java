package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.genericdisplay.InDisplayParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Implémentation générique d'une action d'affichage du détail d'une entité.</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @since MF-Annapurna
 */
//XXX CHANGE V3.2
public class GenericLoadDataForDisplayDetailActionImpl extends AbstractGenericLoadActionImpl<InDisplayParameter, NullActionParameterImpl, DefaultActionStep, Void> implements GenericLoadDataForDisplayDetailAction{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 707095331470866739L;

	/** 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doAction(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public NullActionParameterImpl doAction(MContext p_oContext, InDisplayParameter p_oParam) throws ActionException {
		//XXX CHANGE V3.2
		reloadDataLoader(p_oContext, p_oParam);
		return new NullActionParameterImpl();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#getEmptyInParameter()
	 */
	@Override
	public InDisplayParameter getEmptyInParameter() {
		return new InDisplayParameter();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay.AbstractGenericLoadActionImpl.doNotify()
	 */
	@Override
	protected boolean doNotify() {
		return true;
	}
}
