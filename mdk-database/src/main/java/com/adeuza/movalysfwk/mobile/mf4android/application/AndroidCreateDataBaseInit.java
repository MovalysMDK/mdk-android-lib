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
package com.adeuza.movalysfwk.mobile.mf4android.application;

import java.io.IOException;
import java.io.InputStream;

import com.adeuza.movalysfwk.mobile.mf4android.context.AndroidContextImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.RunInit;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.RunInitError;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.FwkPropertyName;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.ItfTransactionalContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContextFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContextImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.AbstractSubControllerSynchronization;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.ServiceException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.DBUpdaterService;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.DBUpdaterServiceImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.serialization.SerializationDao;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.transaction.ConnectionFactory;

/**
 * <p>Initializes the database creation for android.</p>
 *
 *
 */
public class AndroidCreateDataBaseInit implements RunInit {

	/** {@inheritDoc} */
	@Override
	public void run(MContext p_oContext) throws RunInitError {
		if (p_oContext != null
				&& ConfigurationsHandler.getInstance().getProperty(FwkPropertyName.database)
						.getBooleanValue()) {

			try {

				DBUpdaterServiceImpl oDBUpdaterService = (DBUpdaterServiceImpl) BeanLoader.getInstance()
						.getBean(DBUpdaterService.class);

				// init des scripts pour la création/suppression du model du fwk
				this.addCreateDropScript(oDBUpdaterService, Application.getInstance()
						.openInputStream(p_oContext, DefaultApplicationR.sqlitecreate_fwkmodel), Application
						.getInstance().openInputStream(p_oContext, DefaultApplicationR.sqlitedrop_fwkmodel));

				// init des scripts pour la création/suppression du model des
				// users
				this.addCreateDropScript(oDBUpdaterService, Application.getInstance()
						.openInputStream(p_oContext, DefaultApplicationR.sqlitecreate_usermodel), Application
						.getInstance().openInputStream(p_oContext, DefaultApplicationR.sqlitedrop_usermodel));

				// init des scripts pour la création/suppression des data du fwk
				this.addDataScript(oDBUpdaterService, Application.getInstance()
						.openInputStream(p_oContext, DefaultApplicationR.sqlitecreate_fwkdata), Application
						.getInstance().openInputStream(p_oContext, DefaultApplicationR.sqlitedrop_fwkdata));

				// init des scripts pour la création/suppression des data des
				// users
				this.addDataScript(oDBUpdaterService, Application.getInstance()
						.openInputStream(p_oContext, DefaultApplicationR.sqlitecreate_userdata), Application
						.getInstance().openInputStream(p_oContext, DefaultApplicationR.sqlitedrop_userdata));

				int iIndex = 1;

				InputStream oInputStream = Application.getInstance().openInputStream(p_oContext,
						DefaultApplicationR.sqlitecreate_userdata, iIndex);
				while (oInputStream != null) {
					this.addDataScript(oDBUpdaterService, oInputStream, null);

					oInputStream = Application.getInstance().openInputStream(p_oContext,
							DefaultApplicationR.sqlitecreate_userdata, ++iIndex);
				}
				
				// la base est créee si la version est nouvelle ou si la base
				// n'existe pas
				if ( Application.getInstance().isNewVersion() ) {
					
					SerializationDao oDao = BeanLoader.getInstance().getBean(SerializationDao.class);
					this.onNewApplicationVersion(p_oContext, oDao.loadString(p_oContext, Application.SETTING_APPVERSION), Application.getInstance().getApplicationVersion());
					
				} else if ( !Application.getInstance().isDataBaseExist() ) {

					// Delete database
					this.deleteDatabase(p_oContext, Application.getInstance().getDatabaseName());

					BeanLoader
					.getInstance()
					.getBean(ConnectionFactory.class)
					.initialize(Application.getInstance().getDatabaseName(),
							Application.getInstance().getDataBaseVersion());

					//mise à jour du numéro de version de l'application
					this.updateAppVerion(p_oContext);
					
					this.createDataBase(p_oContext, Application.getInstance().getDatabaseName());

					Application
							.getInstance()
							.getController()
							.manageSynchronizationActions(AbstractSubControllerSynchronization.SC_NEED_FULL_SYNCHRO_ACTION);

				} else {
					// la base est déjà créer mais il faut quand même
					// initialiser la connexion
					BeanLoader
						.getInstance()
						.getBean(ConnectionFactory.class)
						.initialize(Application.getInstance().getDatabaseName(),
								Application.getInstance().getDataBaseVersion());
				}
				
				((MContextImpl) p_oContext).getConnection().beginTransactionConnection();
				
			} catch (IOException | ServiceException | DaoException e) {
				throw new RunInitError(e);
			}
		}
	}

	/**
	 * Update the app syncronisation version
	 *
	 * @param p_oContext context
	 */
	protected void updateAppVerion(MContext p_oContext) {
		//mise à jour du numéro de version de l'application
		SerializationDao oDao = BeanLoader.getInstance().getBean(SerializationDao.class);
		oDao.saveString(p_oContext, Application.SETTING_APPVERSION, Application.getInstance().getApplicationVersion());
	}

	/**
	 * Method called when the app version is upgraded
	 * if you overwrite the default implementation you MUST call this.updateAppVersion(context) in this method
	 *
	 * @param p_oContext context
	 * @param p_sOldVersion old version
	 * @param p_sNewVersion new version
	 * @throws DaoException if any.
	 */
	protected void onNewApplicationVersion(MContext p_oContext, String p_sOldVersion, String p_sNewVersion) throws DaoException {
		// del old version
		this.deleteDatabase(p_oContext, Application.getInstance().getDatabaseName());
		// get connection
		BeanLoader.getInstance().getBean(ConnectionFactory.class)
						.initialize(
								Application.getInstance().getDatabaseName(), 
								Application.getInstance().getDataBaseVersion());
		// save new version number
		this.updateAppVerion(p_oContext);
		// create database
		this.createDataBase(p_oContext, Application.getInstance().getDatabaseName());
		// App syncro
		Application
				.getInstance()
				.getController()
				.manageSynchronizationActions(AbstractSubControllerSynchronization.SC_NEED_FULL_SYNCHRO_ACTION);
	}
	
	/**
	 * <p>
	 * Méthode privée permettant le lancement des méthodes d'initialisation de
	 * la database.
	 * </p>
	 * @param p_oDBUpdaterService updater service
	 * @param p_oCreateInputStream le stream de création en base
	 * @param p_oDropInputStream le stream de suppression en base
	 * @throws ServiceException impossible de charger le bean DBTpdaterService
	 * @throws IOException Problème IO lors de l'exécution de l'action d'initialisation
	 */
	private void addCreateDropScript(DBUpdaterServiceImpl p_oDBUpdaterService, InputStream p_oCreateInputStream, InputStream p_oDropInputStream) 
			throws ServiceException, IOException {
		try {
			try {
				p_oDBUpdaterService.addCreateDropScript(p_oCreateInputStream, p_oDropInputStream);
			} finally {
				if (p_oDropInputStream != null) {
					p_oDropInputStream.close();
				}
			}
		} finally {
			p_oCreateInputStream.close();
		}
	}
	
	/**
	 * Adds a data script to the updater
	 * @param p_oDBUpdaterService updater service
	 * @param p_oCreateInputStream create input stream
	 * @param p_oDropInputStream drop input stream
	 * @throws ServiceException if any
	 * @throws IOException if any
	 */
	private void addDataScript(DBUpdaterServiceImpl p_oDBUpdaterService,
			InputStream p_oCreateInputStream, InputStream p_oDropInputStream) throws ServiceException,
			IOException {
		try {
			try {
				p_oDBUpdaterService.addDataScript(p_oCreateInputStream, p_oDropInputStream);
			} finally {
				if (p_oDropInputStream != null) {
					p_oDropInputStream.close();
				}
			}
		} finally {
			p_oCreateInputStream.close();
		}
	}
	
	//FIXME: javadoc
	protected void deleteDatabase(MContext p_oContext, String p_sDatabaseName) {
		((AndroidContextImpl) p_oContext).getAndroidNativeContext().deleteDatabase(p_sDatabaseName);
	}
	
	//FIXME: javadoc
	protected void createDataBase(MContext p_oContext, String p_sDatabaseName) throws DaoException {
		MContextFactory oCf = BeanLoader.getInstance().getBean(MContextFactory.class);
		MContext oContext = oCf.createContext();
		//retour sur la mécanique classique d'android de création au premier appel
		((ItfTransactionalContext)oContext).getConnection(); 
		oContext.endTransaction();
		oContext.close();
	}
}
