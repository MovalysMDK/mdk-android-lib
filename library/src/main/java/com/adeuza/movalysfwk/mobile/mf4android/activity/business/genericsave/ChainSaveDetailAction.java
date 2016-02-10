package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericsave;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.AbstractBusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.RedirectActionParameterImpl;

/**
 * Chain save detail action
 * 
 * <p>
 * Copyright (c) 2010
 * </p>
 * <p>
 * Company: Adeuza
 * </p>
 * 
 */
public interface ChainSaveDetailAction
		extends
		Action<ChainSaveActionDetailParameter, RedirectActionParameterImpl, DefaultActionStep, Void> {

	/**
	 * modify entity event
	 * @author lmichenaud
	 */
	public static class ModifyEntityEvent extends
			AbstractBusinessEvent<MEntity> {
		/**
		 * Constructor
		 * 
		 * @param p_oSource
		 *            source event.
		 * @param p_oData
		 *            entity.
		 */
		public ModifyEntityEvent(Object p_oSource, MEntity p_oData) {
			super(p_oSource, p_oData);
		}
	}

	/**
	 * add entity event
	 * @author lmichenaud
	 */
	public static class AddEntityEvent extends AbstractBusinessEvent<MEntity> {
		/**
		 * Constructor
		 * 
		 * @param p_oSource
		 *            source event.
		 * @param p_oData
		 *            entity.
		 */
		public AddEntityEvent(Object p_oSource, MEntity p_oData) {
			super(p_oSource, p_oData);
		}
	}
}
