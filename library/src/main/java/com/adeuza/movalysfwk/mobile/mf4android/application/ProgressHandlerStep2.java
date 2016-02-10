package com.adeuza.movalysfwk.mobile.mf4android.application;

import static com.adeuza.movalysfwk.mobile.mf4android.messages.InformationDefinition.FWK_MOBILE_I_1002;
import static com.adeuza.movalysfwk.mobile.mf4android.messages.InformationDefinition.FWK_MOBILE_I_1002_LABEL_1;
import static com.adeuza.movalysfwk.mobile.mf4android.messages.InformationDefinition.FWK_MOBILE_I_1002_LABEL_2;
import static com.adeuza.movalysfwk.mobile.mf4android.messages.InformationDefinition.FWK_MOBILE_I_1002_LABEL_3;
import static com.adeuza.movalysfwk.mobile.mf4android.messages.InformationDefinition.FWK_MOBILE_I_1002_LABEL_4;
import static com.adeuza.movalysfwk.mobile.mf4android.messages.InformationDefinition.FWK_MOBILE_I_1002_LABEL_5;
import static com.adeuza.movalysfwk.mobile.mf4android.messages.InformationDefinition.SPACE;

import java.lang.ref.WeakReference;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractProgressHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ProgressHandler;

/**
 * <p>
 * 	Inner class to represent the second step of the application startup.
 * 	This object extends AsyncTask and will perform asynchronously, the application initialization.
 * 	At each step, it increments a progress bar, positioned on the splash screen of the application.
 * </p>
 * <ul>Under initialization steps:
 * 	<li>application startup</li>
 * 	<li>data synchronization</li>
 * 	<li>planning startup</li>
 * </ul>
 * @author lmichenaud
 *
 */
public class ProgressHandlerStep2 extends AbstractProgressHandler<Void> implements ProgressHandler {

	private WeakReference<ApplicationMainImpl> appMain ;
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	protected Void doInBackground(Void... p_oParams) {
		final int iInitialPriority = Thread.currentThread().getPriority();
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		
		//initialisation des étapes de progression de la barre de progress
		this.initProgress(ApplicationMainImpl.SECOND_PART_STARTUP_PROGRESS_STEP, this.appMain.get().getStepCount());
		if (!this.isCancel()) {
		this.increaseProgress(null); //+3
			if (!this.isCancel()) {
				//lancement de l'application
				long lTime = System.currentTimeMillis();
				Application.getInstance().getLog().info(FWK_MOBILE_I_1002, FWK_MOBILE_I_1002_LABEL_3);
				this.appMain.get().getStarter().runApplication(); // +3 +n
				Application.getInstance().getLog().info(FWK_MOBILE_I_1002, StringUtils.concat(FWK_MOBILE_I_1002_LABEL_4, SPACE, String.valueOf((System.currentTimeMillis()-lTime))));
		
				if (!this.isCancel()) {
					// Initialisation de la lecture des descripteurs de composants.
					lTime = System.currentTimeMillis();
					Application.getInstance().getLog().info(FWK_MOBILE_I_1002, FWK_MOBILE_I_1002_LABEL_5);
					this.appMain.get().getStarter().initVisualComponentHandler();
					Application.getInstance().getLog().info(FWK_MOBILE_I_1002, StringUtils.concat(FWK_MOBILE_I_1002_LABEL_4, SPACE, String.valueOf((System.currentTimeMillis()-lTime))));
					this.increaseProgress(null); // +4 +n
					if (!this.isCancel()) {
						//la configuration est modifiée il faut recalculer les descriptors*/
						lTime = System.currentTimeMillis();
						Application.getInstance().getLog().info(FWK_MOBILE_I_1002, FWK_MOBILE_I_1002_LABEL_1);
						// A2A_DEV sma : pas besoin on le calcul dans le initialize screens descriptor mais à ajouter à la fin de la synchro 
						Application.getInstance().getLog().info(FWK_MOBILE_I_1002, StringUtils.concat(FWK_MOBILE_I_1002_LABEL_2, SPACE, String.valueOf((System.currentTimeMillis()-lTime))));
						this.appMain.get().getStarter().loaded();
						Application.getInstance().loaded();
						this.increaseProgress(null); // +5 +n
					}
				}
			}
		}

		Thread.currentThread().setPriority(iInitialPriority);
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onPostExecute(Void p_oInutilise) {
		super.onPostExecute(p_oInutilise);		
		Application.getInstance().getController().doFirstSynchronisation(this.appMain.get());
		Application.getInstance().updateOldSetting();
	}
	
	public void attach(ApplicationMainImpl p_oApplicationMainImpl) {
		this.appMain = new WeakReference<>(p_oApplicationMainImpl);
	}
}
