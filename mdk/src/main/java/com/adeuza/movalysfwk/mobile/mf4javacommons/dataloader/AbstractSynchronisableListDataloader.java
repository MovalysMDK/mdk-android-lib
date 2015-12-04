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
package com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader;

import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronisationResponseTreatmentInformation;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationListener;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.CascadeSet;

/**
 * Dataloader spécialisé dans le traitement des listes sensible à la synchronisation
 *
 * @param <ITEMRESULT> le type de données contenu dans la liste
 * @param <IN> le type du paramètre d'entrée du chargement
 */
public abstract class AbstractSynchronisableListDataloader<ITEMRESULT> extends AbstractDataloader<List<ITEMRESULT>> implements SynchronizationListener {

	/**
	 * <p>Constructor for AbstractSynchronisableListDataloader.</p>
	 */
	public AbstractSynchronisableListDataloader() {
		super();
		Application.getInstance().getController().register(this);
	}
	
	/** {@inheritDoc} */
	@Override
	public void doOnStartProcess(int p_iLevel, int p_iCurrentProgressStep, float p_fTotalStepCount) {
		//Nothing to do
	}
	
	/** {@inheritDoc} */
	@Override
	public void doOnSynchroChanged(int p_iLevel, String p_sMessage) {
		//Nothing to do
	}

	/** {@inheritDoc} */
	@Override
	public void doOnSynchroChanged2(int p_iLevel, int p_iMaxStep,
			String p_sMessage) {
		//Nothing to do
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationListener#doOnResetProgress()
	 */
	@Override
	public void doOnResetProgress() {
		//Nothing to do
	}
	
	/** {@inheritDoc} */
	@Override
	public void doOnStopProcess(MContext p_oContext, SynchronisationResponseTreatmentInformation p_oInformation) {
		if (p_oInformation != null && this.isInformationForMe(p_oInformation , this.getLoadCascadeForMe())) {
			try {
				//XXX CHANGE V3.2
				this.reloadAll(p_oContext);
			} catch (DataloaderException e) {
				//SMA faire mieux, problème de refraischissement de données
				Application.getInstance().getLog().error("synchro", "Problème lors du rechargement des données", e);
			}
		}
	}
	/**
	 * <p>isInformationForMe.</p>
	 *
	 * @deprecated replaced by {@link #isInformationForMe(SynchronisationResponseTreatmentInformation, CascadeSet)}
	 * @param p_oInformation a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronisationResponseTreatmentInformation} object.
	 * @return a boolean.
	 */
	protected boolean isInformationForMe(SynchronisationResponseTreatmentInformation p_oInformation) {
		return this.isInformationForMe( p_oInformation, this.getLoadCascadeForMe() );
	}
	
	/**
	 * <p>isInformationForMe.</p>
	 *
	 * @param p_oInformation a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronisationResponseTreatmentInformation} object.
	 * @param p_oLoadCascadeSet a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.CascadeSet} object.
	 * @return a boolean.
	 */
	protected boolean isInformationForMe(SynchronisationResponseTreatmentInformation p_oInformation, CascadeSet p_oLoadCascadeSet) {
		return p_oInformation.containsEntity(p_oLoadCascadeSet);
	}
	/**
	 * Retourne la liste des cascades pour vérifier si des données ont changées lors de la synchronisation
	 *
	 * @return la liste des cascades utilisées pour vérifier si le chargement des données
	 */
	protected abstract CascadeSet getLoadCascadeForMe();

}
