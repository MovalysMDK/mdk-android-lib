package com.adeuza.movalysfwk.mobile.mf4android.activity.business.resetdatabase;

import static org.acra.ACRA.LOG_TAG;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.context.AndroidContextImpl;
import com.adeuza.movalysfwk.mobile.mf4android.database.DBImportExportHelper;
import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidConnectionFactoryImpl;
import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteConnection;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.DataloaderException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.DefaultActionStep;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.resetdatabase.ResetDataBaseAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContextFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.controller.SubControllerSynchronization;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.NotifierLauncher;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.serialization.SerializationDao;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.transaction.ConnectionFactory;

/**
 * <p>ResetDataBaseAction implementation</p>
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 * @author smaitre
 */
public class ResetDataBaseActionImpl extends AbstractTaskableAction<NullActionParameterImpl, NullActionParameterImpl, DefaultActionStep, Void> implements ResetDataBaseAction, NotifierLauncher {

	/** serial id */
	private static final long serialVersionUID = 7377232787023190863L;
	/** notifier*/
	private Notifier notifier = null;
	/** export datetime format */
	private static final String DATETIME_FORMAT = "yyyyMMdd-HH'h'mm'm'ss";
	/**
	 * Construct a new action
	 */
	public ResetDataBaseActionImpl() {
		this.notifier = new Notifier();
	}	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl getEmptyInParameter() {
		return new NullActionParameterImpl();
	}	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl doAction(MContext p_oContext, NullActionParameterImpl p_oParameterIn) {

		//Log.d("ResetDataBaseAction", "doAction");
		try {
			// Export préalable de la base
			this.doExportDatabase();
			
			// Fermeture de l'ancienne connection
			((AndroidContextImpl)p_oContext).endTransaction();
			((AndroidContextImpl)p_oContext).close();
			
			// Delete database (doesnot work: database file is deleted but data are still available)
			//Log.e("AVIRER", "ResetDataBaseAction.delete database: " + Application.getInstance().getDatabaseName());
			//if (((AbstractMMActivity)Application.getInstance().getMainScreen()).deleteDatabase(Application.getInstance().getDatabaseName())) {
				
			//création du nouveau context après la suppression de la base
			AndroidConnectionFactoryImpl oConnectionFactory = (AndroidConnectionFactoryImpl) BeanLoader.getInstance().getBean(ConnectionFactory.class);
			
			AndroidSQLiteConnection oConnection = (AndroidSQLiteConnection) oConnectionFactory.getConnectionDisableFK();
			try {
				oConnection.resetDatabase();				
				//Mantis 18625 - lorsqu'on fait un reset de la base, on ne change pas son numéro de version. 
				SerializationDao oDao = BeanLoader.getInstance().getBean(SerializationDao.class);
				oDao.saveString(p_oContext, Application.SETTING_APPVERSION, Application.getInstance().getApplicationVersion());
				
				oConnection.commit();
			} finally {
				oConnection.close();
			}			
		}catch( java.sql.SQLException oException ) {
			Log.e("ResetDataBaseAction", "Failed to reset database", oException);
		}
		return null;
	}	
	/**
	 * Export de la base
	 */
	private void doExportDatabase() {
		SimpleDateFormat oSdf = new SimpleDateFormat(DATETIME_FORMAT,
				Locale.US);
		
		StringBuilder oPath = new StringBuilder("export/database_");
		String sLogin = Application.getInstance().getStringSetting(Application.SETTING_LOGIN);
		if (sLogin != null && sLogin.length() != 0) {
			oPath.append(sLogin).append('_');
		}
		oPath.append(oSdf.format(System.currentTimeMillis()));
		
		Context oContext = ((AndroidApplication) Application.getInstance()).getApplicationContext();
		File oReportDir = oContext.getExternalFilesDir(oPath.toString());
		try {
			DBImportExportHelper.exportDatabase(oReportDir, oContext);
			Log.i(LOG_TAG, "Database successfully exported : " + oReportDir.getAbsolutePath());
		}
		catch (Throwable oException) {
			Log.e(LOG_TAG, "Database export failed : ", oException);
		}
	}

	/**
	 * reload data loader
	 * @param p_oContext context
	 */
	private void reloadDataloader(MContext p_oContext ) {
		//il faut recharger les data loader pour mettre à jour l'interface
		for(Dataloader<?> oLoader : BeanLoader.getInstance().getSingletons(Dataloader.class)) {
			try {
				oLoader.reload(p_oContext);
			} catch (DataloaderException oException) {
				Log.e(LOG_TAG, "Dataloader reload failed : ", oException);
			}
		}
				
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnSuccess(MContext p_oContext, NullActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		//Nothing to do
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public NullActionParameterImpl doOnError(MContext p_oContext, NullActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oResultOut) {
		// nothing to do
		return p_oResultOut;
	}	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPreExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPreExecute(NullActionParameterImpl p_oParameterIn, MContext p_oContext) throws ActionException {
		//Nothing do do
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPostExecute(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter)
	 */
	@Override
	public void doPostExecute(MContext p_oContext, NullActionParameterImpl p_oParameterIn, NullActionParameterImpl p_oParameterOut)
			throws ActionException {
		MContext oLocalContext = BeanLoader.getInstance().getBean(MContextFactory.class).createContext();
		try {
			oLocalContext.beginTransaction();
			try {
				// refresh the dataloader to refresjh the views
				this.reloadDataloader( oLocalContext);
				
				Log.d("ResetDataBaseAction", "before manageSynchronizationActions SC_NEED_FULL_SYNCHRO_ACTION");
				Application.getInstance().getController().manageSynchronizationActions(SubControllerSynchronization.SC_NEED_FULL_SYNCHRO_ACTION);
			} finally {
				oLocalContext.endTransaction();
			}
		} finally {
			oLocalContext.close();
		}
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action#doPublishProgress(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext, com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionStep, PROGRESS[])
	 */
	@Override
	public void doPublishProgress(MContext p_oContext, DefaultActionStep p_oState, Void... p_oProgressInformations) {
		//Nothing to do
	}
	/**
	 * ResetDataBaseAction use allways database access.
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDataBaseAccessAction() {
		return true;
	}
	/**
	 * {@inheritDoc}
	 * ResetDataBaseAction use allways database access.
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
	 * {@inheritDoc}
	 */
	@Override
	public Notifier getNotifier() {
		return this.notifier;
	}
}
