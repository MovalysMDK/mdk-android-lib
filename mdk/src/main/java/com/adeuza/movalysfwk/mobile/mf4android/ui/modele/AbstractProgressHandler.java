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
package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

import java.util.List;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ProgressHandler;

/**
 * <p>Abstract class for managing the progress Handlers of the application initialization.</p>
 *
 *
 * @since Barcelone (24 nov. 2010)
 * 
 * @param <RESULT> type of assicated action
 */
public abstract class AbstractProgressHandler<RESULT> extends AsyncTask<Void, ProgressDefinition, RESULT> implements ProgressHandler {

	/**
	 * The maximum number of progress bar for one action.
	 */
	private static final int PROGRESS_BAR_MAX_NUMBER = 5;
	/**
	 * Static variable to represent the per cent value.
	 */
	private static final int PER_CENT = 100;
	
	/**
	 * Table of Progress Handler definition
	 */
	private ProgressDefinition[] oProgressDefinitionTable;
	
	/** cancel the async task */
	private boolean cancel = false;
	
	/** list of progress bar to use */
	private List<ProgressBar> progressBarList = null;
	/** list des texts bar associées */
	private List<TextView> textViewList = null;
	/** Priorité initiale */
	private int initialPriority;

	/**
	 * Set the parent and the list of progressbar to use
	 * @param p_oApplication the parent
	 * @param p_oProgressBarList the progress bar to use
	 */
	public void init( List<ProgressBar> p_oProgressBarList, List<TextView> p_oTextviewList) {
		this.progressBarList = p_oProgressBarList;
		this.textViewList = p_oTextviewList;
	}
	
	/**
	 * Method that increase a progressBar.
	 */
	@Override
	public void increaseProgress(ApplicationR p_oMessage) {
		this.increaseProgress(0, p_oMessage);
	}
	
	/**
	 * Returns true whether task is cancelled
	 * @param true whether task is cancelled
	 */
	public boolean isCancel() {
		return this.cancel;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCancelled() {
		super.onCancelled();
		this.cancel = true;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void increaseProgress(int p_iLevel, String p_sMessage){
				
		//traitement sur le float de la barre à incrémenter
		ProgressDefinition oProgressDef = this.oProgressDefinitionTable[p_iLevel];
		//mise à jour et sauvegarde du ProgressDefinition
		oProgressDef.setProgressStepCount(oProgressDef.getProgressStepCount()+1, p_sMessage);
		
		this.publishProgress(this.oProgressDefinitionTable);	
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.modelui.ProgressHandler#increaseProgress(int)
	 */
	@Override
	public void increaseProgress(int p_iLevel, ApplicationR p_oMessage){
		this.increaseProgress(p_iLevel, this.getMessage(p_oMessage));
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ProgressHandler#increaseProgress(int, int, com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR)
	 */
	@Override
	public void increaseProgress(int p_iLevel, int p_iTotalStep, ApplicationR p_oMessage){
		this.increaseProgress(p_iLevel, p_iTotalStep, this.getMessage(p_oMessage));
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ProgressHandler#increaseProgress(int, int, java.lang.String)
	 */
	@Override
	public void increaseProgress(int p_iLevel, int p_iTotalStep, String p_oMessage){
		//traitement sur le float de la barre à incrémenter
		ProgressDefinition oProgressDef = this.oProgressDefinitionTable[p_iLevel];
		oProgressDef.setTotalProgressStepCount(p_iTotalStep);
		this.increaseProgress(p_iLevel, p_oMessage);
	}
	
	/**
	 * Lance la progression dans la barre de défilement.
	 */
	public void loadProgress() {
		this.publishProgress(this.oProgressDefinitionTable);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void associateTextToProgressStep(String p_sText){
		//A2A_DEV _#_FBO_#_ - à terminer des que SMA à implémenté la récupération d'objets dans le R depuis un nom d'entité.
	}
	
	/**
	 * <p>
	 * 	Method that initialize the progress bar.
	 * </p>
	 * 
	 * @param p_iProgressStep 
	 * 				The first step of the bar
	 * 
	 * @param p_iTotalProgressStep 
	 * 				The number of step represent by the progress bar
	 */
	public void initProgress(int p_iProgressStep, float p_iTotalProgressStep) {
		this.initProgressProcess(0, p_iProgressStep,p_iTotalProgressStep);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initProgressProcess(int p_iLevel, int p_iCurrentProgressStep, float p_fTotalStepCount){
		if (this.oProgressDefinitionTable == null){
			//on aura jammais plus de 5 barres pour représenter le même process...
			this.oProgressDefinitionTable = new ProgressDefinition[PROGRESS_BAR_MAX_NUMBER];
		}
		ProgressDefinition oProgressDef = new ProgressDefinition(p_iCurrentProgressStep,p_fTotalStepCount);
		this.oProgressDefinitionTable[p_iLevel] = oProgressDef;
	}
	
	@Override
	public void resetProgress() {
		for(ProgressDefinition  oProgressDef : this.oProgressDefinitionTable) {
			if (oProgressDef!=null) {
				oProgressDef.setProgressStepCount(0, "");
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onProgressUpdate(ProgressDefinition... p_oValues) {
		ProgressDefinition oValue = null;
		if ( this.progressBarList != null ) {
			for(int i = 0 ;i<p_oValues.length; i++) {
				oValue = p_oValues[i];
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
	
	
	protected String getMessage( ApplicationR p_oMessage ) {
		String sMessage = "";
		if ( p_oMessage != null ) {
			sMessage = Application.getInstance().getStringResource(p_oMessage);
		}
		return sMessage ;
	}

	/**
	 * {@inheritDoc}
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		this.initialPriority = Thread.currentThread().getPriority();
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
	}
	/**
	 * 
	 * {@inheritDoc}
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(RESULT p_oResult) {
		Thread.currentThread().setPriority(this.initialPriority);
		super.onPostExecute(p_oResult);
	}
}
