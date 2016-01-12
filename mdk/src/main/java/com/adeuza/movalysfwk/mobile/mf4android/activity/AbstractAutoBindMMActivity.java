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
package com.adeuza.movalysfwk.mobile.mf4android.activity;

import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentSavedState;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.WidgetWrapperHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMCustomDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.screen.AutoBindScreen;

/**
 * <p>
 * Android implementation of screen.
 * </p>
 * 
 */
public abstract class AbstractAutoBindMMActivity extends AbstractMvcMMActivity implements AutoBindScreen {

	/**
	 * identifier cache key
	 */
	public static final String IDENTIFIER_CACHE_KEY = "id";

	/** has been try to save */
	private boolean bAsTrySave = false;

	/** onRestoreSave*/
	protected boolean restoreData = false;
	/**
	 * save instance state
	 */
	protected Bundle saveInstanceState = null;

	/** write data en cours ? */
	private boolean writingData = false;

	/**
	 * Constructs a new activity
	 */
	public AbstractAutoBindMMActivity() {
		super();
	}

	/**
	 * GETTER
	 * @return as try save
	 */
	public boolean asTrySave() {
		return this.bAsTrySave;
	}

	
	/**
	 * check if view model is modified
	 */
	protected void checkIsViewModelIsModified() {
		if (this.isViewModelModified()) {
			this.doOnViewModelModified(new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface p_oDialog, int p_iId) {
					p_oDialog.cancel();
					AbstractAutoBindMMActivity.this
					.doOnKeepModifications(p_oDialog);
				}
			},

			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface p_oDialog, int p_iId) {
					p_oDialog.cancel();
					AbstractAutoBindMMActivity.this.getViewModel()
					.doOnDataLoaded(
							AbstractAutoBindMMActivity.this
							.getParameters());
					AbstractAutoBindMMActivity.this.exit();
					AbstractAutoBindMMActivity.this.getViewModel().resetChangedIndicator();
				}
			},
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface p_oDialog, int p_iId) {
					//Nothing to do
				}
			});
		} else {
			this.exit();
		}
	}

	/**
	 * do nothing
	 */
	protected void completeOnRestoreInstanceState() {
		//do nothing
	}

	/**
	 * création d'un nouveau View Model pour l'activité courante. Attention
	 * n'est pas appelé dans le cas d'une rotation. Ne doit pas gérer la mise en
	 * cache
	 * 
	 * @return le nouveau ViewModel
	 */
	protected abstract ViewModel createViewModel();



	/**
	 * set the content view from the getViewId
	 */
	protected void defineContentView() {
		super.setContentView(this.getViewId());
	}

	/**
	 * Lance l'action de remplissage
	 */
	public void doFillAction() {
		// par défaut rien à faire
	}

	/**
	 * do on create
	 * @param p_oSavedInstanceState bundle saved instance state
	 */
	protected void doOnCreate(Bundle p_oSavedInstanceState) {
		// Nothing to do
	}



	/**
	 * Called when the user wants keep modifications.
	 * @param p_oDialog dialog interface
	 */
	protected void doOnKeepModifications(DialogInterface p_oDialog) {

		this.bAsTrySave = true;
		// NOTHING TO DO
	}

	/**
	 * Creates a custom dialog to interact with the user: lose or keep
	 * modifications.
	 * @param p_oLoseListener lose listener
	 * @param p_oKeepListener keep listener
	 * @param p_oCancelListener cancel listner
	 */
	protected void doOnViewModelModified(
			DialogInterface.OnClickListener p_oLoseListener,
			DialogInterface.OnClickListener p_oKeepListener,
			DialogInterface.OnClickListener p_oCancelListener) {
		// lancement de la fenêtre de dialog pour valider ou non la fermeture de
		// l'application
		MMCustomDialogFragment.Builder oBuilder = new MMCustomDialogFragment.Builder(this);

		oBuilder.setTitle(AndroidApplicationR.lose_form_modifications_title);
		oBuilder.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		oBuilder.setMessage(AndroidApplicationR.lose_form_modifications_text);

		oBuilder.setPositiveButton(
				AndroidApplicationR.lose_form_modifications_ok_button,
				p_oLoseListener);
		oBuilder.setNegativeButton(
				AndroidApplicationR.lose_form_modifications_ko_button,
				p_oKeepListener);
		oBuilder.setCancelButton(
				AndroidApplicationR.lose_form_modifications_cancel_button, 
				p_oCancelListener);

		MMCustomDialogFragment oAlert = oBuilder.create();
		oAlert.setCancelable(false);
		oAlert.show(getSupportFragmentManager(), oAlert.getFragmentDialogTag());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override	
	public void exit() {
		super.exit();
	}



	/**
	 * get view ID
	 * @return view ID
	 */
	public abstract int getViewId();

	/**
	 * Returns <code>true</code> when the view model has been modified,
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> when the view model has been modified,
	 *         <code>false</code> otherwise.
	 */
	protected boolean isViewModelModified() {
		ViewModel oViewModel = this.getViewModel();
		return oViewModel != null && oViewModel.isEditable() && oViewModel.isReadyToChanged();
	}

	/**
	 * is writing or restore data?
	 * @return true if system is writing data
	 */
	public boolean isWritingOrRestoreData() {
		return this.writingData || this.restoreData;
	}

	/**
	 * Lance une action depuis l'écran courant
	 * 
	 * @param p_oAction  instance de l'action à lancer
	 * @param p_oInterfaceClass  l'action à lancer (le nom de l'interface)
	 * @param p_oParameterIn  les paramètres d'entrée de l'action
	 * @param p_oSource action source
	 */
	@Override
	public void launchAction(
			Action<?, ?, ?, ?> p_oAction,
			Class<? extends Action<?, ?, ?, ?>> p_oInterfaceClass,
			ActionParameter p_oParameterIn, 
			Object p_oSource) {

		if (p_oParameterIn != null
				&& p_oParameterIn.getRuleParameters() == null) {
			p_oParameterIn.setRuleParameters(this.getParameters());
		}

		super.launchAction(p_oAction, p_oInterfaceClass, p_oParameterIn,
				p_oSource);
	}

	/**
	 * Lance une action depuis l'écran courant
	 * 
	 * @param p_oActionClass l'action à lancer (le nom de l'interface)
	 * @param p_oParameterIn  les paramètres d'entrée de l'action
	 * @param p_oSource source
	 */
	@Override
	public void launchAction(
			Class<? extends Action<?, ?, ?, ?>> p_oActionClass,
					ActionParameter p_oParameterIn, Object p_oSource) {
		if (p_oParameterIn != null
				&& p_oParameterIn.getRuleParameters() == null) {
			p_oParameterIn.setRuleParameters(this.getParameters());
		}

		super.launchAction(p_oActionClass, p_oParameterIn, p_oSource);
	}




	/**
	 * {@inheritDoc}
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if (this.getSupportFragmentManager() == null) {
			checkIsViewModelIsModified();
		} else if (!this.getSupportFragmentManager().popBackStackImmediate()){
			checkIsViewModelIsModified();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void onCreate(Bundle p_oSavedInstanceState) {

		// Test if this is the intent to exit application
		if ( this.shouldActivityBeStopped()) {
			super.onCreate(p_oSavedInstanceState);
			return;
		}

		Object oConfig = this.getLastCustomNonConfigurationInstance();

		//DMA/SMA : suite erreur sur les restauration en cas de besoin de ressources.
		//stackoverflow n°13997550
		if (p_oSavedInstanceState!=null){
			p_oSavedInstanceState.setClassLoader(AndroidConfigurableVisualComponentSavedState.class.getClassLoader());	
		}

		super.onCreate(p_oSavedInstanceState);

		ViewModel oVm = null;
		if (oConfig == null) {
			oVm = this.createViewModel();
		} else {
			// cas de la rotation, récupérer le view model dans le cache
			oVm = (ViewModel) ((Map<String, Object>) oConfig).get("vm");
		}
		this.setViewModel(oVm);

		this.defineContentView();

		this.writingData = true;

		this.inflate(writeDataAtConstruction());

		// gestion de la rotation et de la sauvegarde des paramètres des
		// composants
		if (oConfig == null) {
			Application.getInstance().cleanCacheContextConfigurationData(
					this.getViewId());
		}

		this.doOnCreate(p_oSavedInstanceState);
		this.writingData = false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 */
	@Override
	protected void onPostCreate(Bundle p_oSavedInstanceState) {
		super.onPostCreate(p_oSavedInstanceState);

		Object oConfig = this.getLastCustomNonConfigurationInstance();
		if (oConfig==null && p_oSavedInstanceState==null) {
			this.doFillAction();
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle p_oSavedInstanceState) {
		this.restoreData = true;
		//DMA/SMA : suite erreur sur les restauration en cas de besoin de ressources.
		//stackoverflow n°13997550 bug 6822 chez Google
		p_oSavedInstanceState.setClassLoader(AndroidConfigurableVisualComponentSavedState.class.getClassLoader());
		if (this.getLastCustomNonConfigurationInstance() == null) {
			// lorsque l'appli a été détruite par le système, on doit tout
			// reconstruire,
			// dans le cas d'une rotation, pas besoin car le onretain
			// sauvegarde les éléments
			this.saveInstanceState = p_oSavedInstanceState;

			doFillAction();

			// La variable restoreData est init à false
			// Lors de l'appel de la méthode superOnRestoreInstanceState
			// dans la méthode reloadDetail
		} else {
			super.onRestoreInstanceState(p_oSavedInstanceState);
			this.restoreData = false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> onRetainCustomNonConfigurationInstance() {
		Map<String, Object> r_oMap = (Map<String, Object>) super.onRetainCustomNonConfigurationInstance();
		r_oMap.put("vm", this.getViewModel());
		return r_oMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableVisualComponent retrieveConfigurableVisualComponent(List<String> p_oPath, boolean p_bIsInflating) {
		ConfigurableVisualComponent r_oView = this;
		View oTmpView;
		int iId = 0;
		int iCpt = 0;

		for (String sPath : p_oPath) {
			iId = this.getAndroidApplication().getAndroidIdByStringKey(ApplicationRGroup.ID, sPath);
			if (iCpt == 0) {
				oTmpView = this.findViewById(iId);
			} else {
				oTmpView = ((View) r_oView).findViewById(iId);
			}
			if (oTmpView == null) {
				break;
			} else {
				// get wrapper if it exists, or return view
				String sWrapperClass = WidgetWrapperHelper.getInstance().getWrapperClassForComponent(oTmpView);
				
				if (sWrapperClass != null) {
					if (p_bIsInflating) {
						r_oView = WidgetWrapperHelper.getInstance().createWrapper(oTmpView, sWrapperClass, this.restoreData);
					} else {
						r_oView = WidgetWrapperHelper.getInstance().getWrapperForComponent(this.getViewModel(), sPath);
					}
				} else {
					r_oView = (ConfigurableVisualComponent) oTmpView;
				}
			}
			iCpt++;
		}
		return r_oView;
	}



	/**
	 * set custom converter
	 * @param p_oCustomConverter custom converter
	 * @param p_oAttributeSet attribute set
	 */
	public void setCustomConverter(String p_oCustomConverter, AttributeSet p_oAttributeSet) {
		//Nothing to do
	}

	/**
	 * SETTER custom formatter
	 * @param p_oCustomFormatter custom formatter
	 * @param p_oAttributeSet attributes set
	 */
	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeSet) {
		//Nothing to do
	}


	/**
	 * call super.onRestoreInstanceState
	 */
	protected void superOnRestoreInstanceState() {
		super.onRestoreInstanceState(this.saveInstanceState);
		this.restoreData = false;
	}

	/**
	 * write data at construction
	 * @return false
	 */
	public boolean writeDataAtConstruction() {
		return false;
	}
}
