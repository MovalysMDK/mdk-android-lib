package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdelete;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.AbstractBusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.RedirectActionParameterImpl;

/**
 * Chain delete detail action interface
 * 
 * <p>
 * Copyright (c) 2010
 * </p>
 * <p>
 * Company: Adeuza
 * </p>
 * 
 */
public interface ChainDeleteDetailAction
		extends
		Action<ChainDeleteActionDetailParameter, RedirectActionParameterImpl, DefaultActionStep, Void> {

	/**
	 * chain delete detail action
	 * @author lmichenaud
	 *
	 */
	public static class DeleteEntityEvent extends AbstractBusinessEvent<MEntity> {
		/**
		 * Constructor
		 * 
		 * @param p_oSource
		 *            source event.
		 * @param p_oData
		 *            entity.
		 */
		public DeleteEntityEvent(Object p_oSource, MEntity p_oData) {
			super(p_oSource, p_oData);
		}
	}
}
