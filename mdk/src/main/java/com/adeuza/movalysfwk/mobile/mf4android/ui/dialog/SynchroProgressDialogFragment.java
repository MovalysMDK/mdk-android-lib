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
package com.adeuza.movalysfwk.mobile.mf4android.ui.dialog;
 
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.AbstractMMFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ProgressDefinition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronisationResponseTreatmentInformation;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationListener;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.ProgressDialog;

public class SynchroProgressDialogFragment extends MMDialogFragment implements ProgressDialog, SynchronizationListener {

	/**
	 * NewInstance method 
	 * @param p_oDismissListener The DismissListener of this DialogFragment. It will be stored in
	 * this object as a WeakReference
	 * @return A new instance of this fragment
	 */
	public static SynchroProgressDialogFragment newInstance(OnDismissListener p_oDismissListener) {
		SynchroProgressDialogFragment oFragment = new SynchroProgressDialogFragment();
		oFragment.m_oDismissListener = new WeakReference<>(p_oDismissListener);
		oFragment.m_sFragmentTag = String.valueOf(AbstractMMFragment.oGeneratedUniqueTag.incrementAndGet());
		oFragment.m_bUpdateComponentReference = false;
		oFragment.componentId = -1;
		oFragment.componentFragmentTag = null;
		Application.getInstance().getController().register(oFragment);
		return oFragment;
	}
	

	public static final String SYNCHRO_PROGRESS_DIALOG_FRAGMENT_TAG = "synchroProgressDialogFragmentTag";
	/**
	 * The maximum number of progress bar for one action.
	 */
	private static final int PROGRESS_BAR_MAX_NUMBER = 5;

	/**
	 * Static variable to represent the per cent value.
	 */
	private static final int PER_CENT = 100;

	/**
	 * The progressBar list to update
	 */
	private List<ProgressBar> progressBarList ;

	/**
	 * 
	 */
	private List<TextView> textViewList ;
	
	/**
	 * Table of Progress Handler definition
	 */
	private ProgressDefinition[] progressDefinitionTable;
    
    @Override
    public void onDestroy() {
		Application.getInstance().getController().unregister(this);
    	super.onDestroy();
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(Application.getInstance().getStringResource(AndroidApplicationR.dialog_synchronization_title));
		
		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_screen_synchronisation_state), null);
		builder.setView(view);
	    this.setCancelable(false);	
		
	    ProgressBar oUiGlobalProgressBar = (ProgressBar) view.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.screen_synchro_global_progress));
		oUiGlobalProgressBar.setProgress(0);
		
//		récupération de la barre de progression détaillée
		ProgressBar oUiDetailProgressBar = (ProgressBar) view.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.screen_synchro_detail_progress));
		oUiDetailProgressBar.setProgress(0);

//		initialisation de la liste des progress bar à incrémenter
		this.progressBarList = new ArrayList<>();
		this.progressBarList.add(oUiGlobalProgressBar);
		this.progressBarList.add(oUiDetailProgressBar);
		
		this.textViewList = new ArrayList<>();
		this.textViewList.add((TextView) view.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.screen_synchro_detail_progress_text1)));
		this.textViewList.add((TextView) view.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.screen_synchro_detail_progress_text2)));
		
		this.updateProgressStatus();
		
		return builder.create();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.Dialog#getName()
	 */
	@Override
	public String getName() {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.Dialog#close()
	 */
	@Override
	public void close() {
		this.dismissAllowingStateLoss();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationListener#doOnStopProcess(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronisationResponseTreatmentInformation)
	 */
	@Override
	public void doOnStopProcess(MContext p_oContext, SynchronisationResponseTreatmentInformation p_oInformation) {
		this.finishSynchro();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationListener#doOnResetProgress()
	 */
	@Override
	public void doOnResetProgress() {
		for(ProgressDefinition  oProgressDef : this.progressDefinitionTable) {
			if (oProgressDef!=null) {
				oProgressDef.setProgressStepCount(0, "");
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationListener#doOnStartProcess(int, int, float)
	 */
	@Override
	public void doOnStartProcess(int p_iLevel, int p_iCurrentProgressStep, float p_fTotalStepCount) {
		if (this.progressDefinitionTable == null){
			//on aura jammais plus de 5 barres pour représenter le même process...
			this.progressDefinitionTable = new ProgressDefinition[PROGRESS_BAR_MAX_NUMBER];
		}
		ProgressDefinition oProgressDef = new ProgressDefinition(p_iCurrentProgressStep,p_fTotalStepCount);
		this.progressDefinitionTable[p_iLevel] = oProgressDef;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationListener#doOnSynchroChanged(int, java.lang.String)
	 */
	@Override
	public void doOnSynchroChanged(int p_iLevel, String p_sMessage) {
		//traitement sur le float de la barre à incrémenter
		ProgressDefinition oProgressDef = this.progressDefinitionTable[p_iLevel];
		//mise à jour et sauvegarde du ProgressDefinition
		oProgressDef.setProgressStepCount(oProgressDef.getProgressStepCount()+1, p_sMessage);

//		this.publishProgress(this.oProgressDefinitionTable);
		ProgressDefinition oValue = null;
		if ( this.progressBarList != null ) {
			for(int i = 0 ;i<this.progressDefinitionTable.length; i++) {
				oValue = this.progressDefinitionTable[i];
				if (oValue!=null) {
					if (oValue.getTotalProgressStepCount() == Float.MAX_VALUE) {
						progressBarList.get(i).setIndeterminate(true);
					}
					else {
						progressBarList.get(i).setIndeterminate(false);
					}
					
					int iProgress = (int) (oValue.getValue()*PER_CENT);
					progressBarList.get(i).setProgress(iProgress);
					if (textViewList !=null) {
						textViewList.get(i).setText(oValue.getMessage());
					
					}
				}
			}
		}
	}
	
	private void updateProgressStatus() {
		if ( progressDefinitionTable != null ) {
			for(int i = 0 ; i< this.progressDefinitionTable.length; i++) {
				ProgressDefinition oValue = this.progressDefinitionTable[i];
				if (oValue!=null) {
					if (oValue.getTotalProgressStepCount() == Float.MAX_VALUE) {
						this.progressBarList.get(i).setIndeterminate(true);
					}
					else {
						this.progressBarList.get(i).setIndeterminate(false);
					}
					
					int iProgress = (int) (oValue.getValue()*PER_CENT);
					this.progressBarList.get(i).setProgress(iProgress);
					if ( this.textViewList != null ) {
						this.textViewList.get(i).setText(oValue.getMessage());
					
					}
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationListener#doOnSynchroChanged2(int, int, java.lang.String)
	 */
	@Override
	public void doOnSynchroChanged2(int p_iLevel, int p_iTotalStep, String p_sMessage) {
		ProgressDefinition oProgressDef = this.progressDefinitionTable[p_iLevel];
		oProgressDef.setTotalProgressStepCount(p_iTotalStep);
		this.doOnSynchroChanged(p_iLevel, p_sMessage);
	}
	
	/**
	 * Close the activity.
	 */
	protected void finishSynchro(){
		Application.getInstance().getController().unregister(this);
	}
		
    @Override
    public void onDestroyView() 
    {
    	// necessary for restoring the dialog
        if (getDialog() != null && getRetainInstance()) {
            //getDialog().setOnDismissListener(null);
        	getDialog().setDismissMessage(null);
        }

        super.onDestroyView();
    }
}
