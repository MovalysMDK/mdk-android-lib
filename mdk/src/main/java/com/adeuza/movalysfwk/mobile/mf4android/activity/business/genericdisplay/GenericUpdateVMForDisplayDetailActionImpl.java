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

import java.util.HashMap;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.AbstractViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ComputeRuleEditable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.UpdatableFromDataloader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * <p>TODO Décrire la classe GenericUpdateVMForDisplayDetailActionImpl</p>
 *
 *
 *
 */

public class GenericUpdateVMForDisplayDetailActionImpl extends AbstractTaskableAction<InUpdateVMParameter, OutUpdateVMParameter, DefaultActionStep, Void> implements GenericUpdateVMForDisplayDetailAction {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1609824071539537079L;

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPreExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext)
	 */
	@Override
	public void doPreExecute(InUpdateVMParameter p_oInUpdateVMParameter, MContext p_oContext) throws ActionException {
		//		do nothing
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doAction(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public OutUpdateVMParameter doAction(MContext p_oContext, InUpdateVMParameter p_oParam) throws ActionException {
		OutUpdateVMParameter out = new OutUpdateVMParameter();
		out.setDataloader(p_oParam.getDataLoader());
		out.setViewModel(p_oParam.getVm());
		Map<String, Object> t = new HashMap<>();
		t.put("in", p_oParam);
		out.setRuleParameters(t);
		return out;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPostExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPostExecute(MContext p_oContext, InUpdateVMParameter p_oParameterIn, OutUpdateVMParameter p_oParameterOut) throws ActionException {
		//XXX CHANGE V3.2
		UpdatableFromDataloader oVM = (UpdatableFromDataloader) p_oParameterIn.getViewModel();
		if (oVM == null) {
			oVM = (UpdatableFromDataloader) Application.getInstance().getViewModelCreator().getViewModel(p_oParameterIn.getVm());
		}
		// Rule computeEditableFlag
		ComputeRuleEditable r = new MyRunnable((ViewModel)oVM, p_oContext, p_oParameterIn);
		((AbstractViewModel) oVM).setCurrentRule(r);
	
		// Viewmodel can be binded with ui components. So, the updateFromDataLoader must be executed in the UI thread.
		if ( p_oParameterIn.getDataLoader() != null ) {
			Dataloader<?> oDtl = BeanLoader.getInstance().getBean(p_oParameterIn.getDataLoader() );
			if (oDtl != null) {
				oVM.updateFromDataloader( oDtl);
			}
		}

		p_oParameterIn.notifyAdapters();
	}
	
	/**
	 * 
	 * Run Rule computeEditableFlag
	 *
	 */
	public static class MyRunnable implements ComputeRuleEditable {
	
		/** ViewModel */
		private ViewModel vm;
		/** Context Android */
		private MContext context;
		/** Parameter */
		private InUpdateVMParameter in;
		
		/**
		 * Constructor
		 * @param p_oVm view model to use
		 * @param p_oContext context to use
		 * @param p_oParameterIn parameters to use
		 */
		public MyRunnable (ViewModel p_oVm, MContext p_oContext, InUpdateVMParameter p_oParameterIn) {
			this.vm = p_oVm;
			this.context = p_oContext;
			this.in = p_oParameterIn;
		}

		@Override
		public void run() {
			((ViewModel) vm).setEditable(context, in.getEditableVMParameters());
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doOnSuccess(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, InUpdateVMParameter p_oParameterIn, OutUpdateVMParameter p_oResultOut) throws ActionException {
		//		do nothing
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doOnError(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public OutUpdateVMParameter doOnError(MContext p_oContext, InUpdateVMParameter p_oParameterIn, OutUpdateVMParameter p_oResultOut)
			throws ActionException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#isConcurrentAction()
	 */
	@Override
	public int getConcurrentAction() {
		return Action.DEFAULT_QUEUE;
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
