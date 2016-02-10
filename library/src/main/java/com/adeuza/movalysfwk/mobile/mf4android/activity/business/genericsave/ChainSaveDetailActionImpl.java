package com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericsave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4android.context.AndroidMMContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.EntityActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.RedirectActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ExtFwkErrors;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;

/**
 * chain save detail action
 * @author lmichenaud
 * 
 */
public class ChainSaveDetailActionImpl
		extends
		AbstractTaskableAction<ChainSaveActionDetailParameter, RedirectActionParameterImpl, DefaultActionStep, Void>
		implements ChainSaveDetailAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5608245764179468600L;

	/**
	 * notifier
	 */
	private Notifier notifier = new Notifier();

	/**
	 * Actions to launch
	 */
	private List<SaveDetailAction<MEntity, ?, ?>> actions = new ArrayList<>();

	/**
	 * action results
	 */
	private Map<SaveDetailAction, EntityActionParameterImpl> actionResults = new HashMap<>();
	
	/**
	 * DO action
	 * @param p_oContext context
	 * @param p_oParameterIn chain save action detail IN parameter
	 * @return redirect action parameter
	 */
	@Override
	public RedirectActionParameterImpl doAction(MContext p_oContext, ChainSaveActionDetailParameter p_oParameterIn) {
		try {
			if (p_oParameterIn != null) {
				NullActionParameterImpl saveInParameter = p_oParameterIn.getSaveDetailActionsParameterIn();
				boolean bValidationSuccess = validateData(p_oContext, saveInParameter);

				if (bValidationSuccess) {
					// Les étapes doivent être effectuées les unes après les autres pour que les entités traitées soient les bonnes
					preSave(p_oContext, p_oParameterIn);
					
					save(p_oContext, p_oParameterIn);
					
					postSave(p_oContext, p_oParameterIn);
					
					// create the events
					p_oContext.setEventsExitMode(p_oParameterIn.isExitMode());
					((AndroidMMContext)p_oContext).addBusinessEventFromFirstEvent(p_oParameterIn.isExitMode());
				}
				else {
					p_oContext.getMessages().addMessage(ExtFwkErrors.InvalidViewModelDataError);
				}
			}

		} catch (ActionException oException) {
			Log.e(this.getClass().getSimpleName(),
					"Erreur in ChainSaveDetailAction", oException);
			p_oContext.getMessages().addMessage(ExtFwkErrors.ActionError);
		}

		return new RedirectActionParameterImpl();
	}

	/**
	 * Execute validateData on each action
	 * @param p_oContext context to use
	 * @param p_oSaveInParameter save parameters to use
	 * @return true if the actions returned true
	 * @throws ActionException exception
	 */
	private boolean validateData(MContext p_oContext, NullActionParameterImpl p_oSaveInParameter) throws ActionException {
		boolean r_bValidationSuccess = true;
		
		for (SaveDetailAction<?, ?, ?> oSaveAction : this.actions) {
			if (!oSaveAction.validateData(p_oSaveInParameter, p_oContext)) {
				r_bValidationSuccess = false ;
			}
		}
		
		return r_bValidationSuccess;
	}

	/**
	 * Execute preSaveData on each action
	 * @param p_oContext context to use
	 * @param p_oParameterIn action parameters
	 * @throws ActionException exception
	 */
	private void preSave(MContext p_oContext, ChainSaveActionDetailParameter p_oParameterIn) throws ActionException {
		for (SaveDetailAction<MEntity, ?, ?> oSaveAction : this.actions) {
			MEntity oEntity = oSaveAction.preSaveData(p_oParameterIn.getSaveDetailActionsParameterIn(), p_oContext );
			
			this.actionResults.put(oSaveAction, new EntityActionParameterImpl(oEntity));
		}
	}

	/**
	 * Execute saveData on each action
	 * @param p_oContext context to use
	 * @param p_oParameterIn action parameters
	 * @throws ActionException exception
	 */
	private void save(MContext p_oContext, ChainSaveActionDetailParameter p_oParameterIn) throws ActionException {
		for (SaveDetailAction<MEntity, ?, ?> oSaveAction : this.actions) {
			MEntity oEntity = oSaveAction.saveData(
				(MEntity) this.actionResults.get(oSaveAction).getEntity(),
				p_oParameterIn.getSaveDetailActionsParameterIn(), p_oContext);	
			this.actionResults.put(oSaveAction, new EntityActionParameterImpl(oEntity));
		}
	}
	
	/**
	 * Execute postSaveData on each action
	 * @param p_oContext context to use
	 * @param p_oParameterIn action parameters
	 * @throws ActionException exception
	 */
	private void postSave(MContext p_oContext, ChainSaveActionDetailParameter p_oParameterIn) throws ActionException {
		for (SaveDetailAction<MEntity, ?, ?> oSaveAction : this.actions) {
			oSaveAction.postSaveData(
				this.actionResults.get(oSaveAction).getEntity(),
				p_oParameterIn.getSaveDetailActionsParameterIn(), p_oContext );
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChainSaveActionDetailParameter getEmptyInParameter() {
		return new ChainSaveActionDetailParameter();
	}

	/**
	 * publish progress
	 * @param p_oContext context
	 * @param p_oState state
	 * @param p_oParameterIn action IN parameter
	 * @param p_oProgressInformations progress IN informations
	 */
	public void doPublishProgress(ActionParameter p_oParameterIn,
			MContext p_oContext, DefaultActionStep p_oState,
			Void... p_oProgressInformations) {
		//do nothing
	}

	/**
	 * pre-execute
	 * @param p_oContext context
	 * @param p_oParameterIn chain save action detail IN parameter
	 * @throws ActionException error
	 */
	@Override
	public void doPreExecute(ChainSaveActionDetailParameter p_oParameterIn,
			MContext p_oContext) throws ActionException {

		ChainSaveActionDetailParameter oParameterIn = (ChainSaveActionDetailParameter) p_oParameterIn;

		for (Class<? extends SaveDetailAction<?, ?, ?>> oSaveActionClass : oParameterIn.getSaveDetailActions()) {
			
			SaveDetailAction<MEntity, ?, ?> oAction = (SaveDetailAction<MEntity, ?, ?>) BeanLoader.getInstance().getBean(oSaveActionClass);
			this.actions.add(oAction);
			try {
				oAction.doPreExecute(p_oParameterIn.getSaveDetailActionsParameterIn(), p_oContext);
			} catch (ActionException oActionException) {
				throw new MobileFwkException(oActionException);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doPostExecute(MContext p_oContext,
			ChainSaveActionDetailParameter p_oParameterIn,
			RedirectActionParameterImpl p_oParameterOut) throws ActionException {
		
		for (SaveDetailAction<?, ?, ?> oSaveAction : this.actions) {
			oSaveAction.doPostExecute(p_oContext, p_oParameterIn.getSaveDetailActionsParameterIn(), this.actionResults.get(oSaveAction));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnSuccess(MContext p_oContext,
			ChainSaveActionDetailParameter p_oParameterIn,
			RedirectActionParameterImpl p_oResultOut) throws ActionException {
		
		for (SaveDetailAction<?, ?, ?> oSaveAction : this.actions) {
			oSaveAction.doOnSuccess(p_oContext, p_oParameterIn.getSaveDetailActionsParameterIn(), this.actionResults.get(oSaveAction));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RedirectActionParameterImpl doOnError(MContext p_oContext,
			ChainSaveActionDetailParameter p_oParameterIn,
			RedirectActionParameterImpl p_oResultOut) throws ActionException {
		
		for (SaveDetailAction<?, ?, ?> oSaveAction : this.actions) {
			oSaveAction.doOnError(p_oContext, p_oParameterIn.getSaveDetailActionsParameterIn(), this.actionResults.get(oSaveAction));
		}		
		
		return p_oResultOut;
	}

	/**
	 * do nothing
	 */
	public void doNotify() {
		// does nothing
	}

	/**
	 * ResetDataBaseAction use always database access. 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDataBaseAccessAction() {
		return true;
	}

	/**
	 * {@inheritDoc} ResetDataBaseAction use always database access.
	 */
	@Override
	public boolean isWritableDataBaseAccessAction() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isConcurrentAction() {
		return false;
	}

	/**
	 * GETTER
	 * @return notifier
	 */
	public Notifier getNotifier() {
		return this.notifier;
	}
}
