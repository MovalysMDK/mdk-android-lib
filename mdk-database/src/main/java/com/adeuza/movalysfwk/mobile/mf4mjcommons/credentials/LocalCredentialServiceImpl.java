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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.credentials;

import java.net.HttpURLConnection;
import java.sql.Types;
import java.util.Calendar;

import com.adeuza.movalysfwk.mf4jcommons.crypt.AESUtil;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.FwkPropertyName;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.property.Property;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.CascadeSet;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.MParametersDao;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.MParametersField;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.factory.MParametersFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MParameters;

/**
 * <p>LocalCredentialServiceImpl class.</p>
 *
 */
public class LocalCredentialServiceImpl implements LocalCredentialService {

	/**
	 * Name of the {@link MParameters} that represents the login of the previous
	 * authenticated user.
	 */
	private static final String PREVIOUS_LOGIN_PARAMETER = "previous-login";

	/**
	 * Name of the {@link MParameters} that represents the resource identifier
	 * of the previous authenticated user.
	 */
	private static final String PREVIOUS_RESOURCE_PARAMETER = "resourceId";

	/**
	 * Name of the {@link MParameter} that represents the last synchronization
	 * date of the user.
	 */
	private static final String PREVIOUS_SYNC_DATE_PARAMETER = "previous-sync-date";

	/**
	 * Milli seconds in one day.
	 */
	private static final int MILLISECONDS_IN_ONE_DAY = 86400000;

	/**
	 * {@inheritDoc}
	 *
	 * (non-Javadoc)
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.credentials.LocalCredentialService#storeCredentials(java.lang.String, long, com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext)
	 */
	@Override
	public void storeCredentials(String p_sLogin, long p_lResourceId, MContext p_oContext)
			throws CredentialException {
		
		try {
			// On ne définit qu'une fois l'identifiant de la ressource, lors de la
			// première action de synchronisation.
			long lResourceId = Application.getInstance().getCurrentUserResource();
	
			MParametersDao oParameterDao = BeanLoader.getInstance().getBean(MParametersDao.class);
			MParametersFactory oParametersFactory = BeanLoader.getInstance().getBean(MParametersFactory.class);
	
			// mise à jour de la date de dernière synchronisation en base
			oParameterDao.deleteMparametersByName(p_oContext, MParametersField.NAME, Types.VARCHAR,
					PREVIOUS_SYNC_DATE_PARAMETER);
			MParameters oSyncDate = oParametersFactory.createInstance();
			oSyncDate.setName(PREVIOUS_SYNC_DATE_PARAMETER);
			oSyncDate.setValue(Long.toString(System.currentTimeMillis()));
			oParameterDao.saveOrUpdateMParameters(oSyncDate, p_oContext);
	
			if (lResourceId < 1 || p_lResourceId > 0 && p_lResourceId != lResourceId) {
				oParameterDao.deleteMparametersByName(p_oContext, MParametersField.NAME, Types.VARCHAR,
						PREVIOUS_LOGIN_PARAMETER);
				oParameterDao.deleteMparametersByName(p_oContext, MParametersField.NAME, Types.VARCHAR,
						PREVIOUS_RESOURCE_PARAMETER);
	
				MParameters oLogin = oParametersFactory.createInstance();
				oLogin.setName(PREVIOUS_LOGIN_PARAMETER);
				oLogin.setValue(p_sLogin);
				oParameterDao.saveOrUpdateMParameters(oLogin, p_oContext);
	
				MParameters oResource = oParametersFactory.createInstance();
				oResource.setName(PREVIOUS_RESOURCE_PARAMETER);
				oResource.setValue(AESUtil.encrypt(Long.toString(p_lResourceId)));
				oParameterDao.saveOrUpdateMParameters(oResource, p_oContext);
	
				Application.getInstance().setCurrentUserResource(p_lResourceId);
			}
		} catch( Exception oException ) {
			throw new CredentialException(oException);
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * (non-Javadoc)
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.credentials.LocalCredentialService#doIdentify(java.lang.String, com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext)
	 */
	@Override
	public IdentResult doIdentify( String p_sLogin, MContext p_oContext ) throws CredentialException {
		IdentResult r_oResult = IdentResult.ko;
		boolean bIdentified = false;
		try {
			if (ConfigurationsHandler.getInstance().getProperty(FwkPropertyName.sync_mock_mode).getBooleanValue()
					&& ConfigurationsHandler.getInstance().getProperty(FwkPropertyName.sync_mock_testid).getIntValue() == HttpURLConnection.HTTP_CLIENT_TIMEOUT){
				//mock pour la simulation d'un dépassement de délais de synchro
				r_oResult = IdentResult.koCauseOfDate;
			}
			else {
				MParametersDao oMParametersDao = BeanLoader.getInstance().getBean(MParametersDao.class);
				MParameters oParameter = oMParametersDao.getMParametersByName(PREVIOUS_LOGIN_PARAMETER, CascadeSet.NONE,p_oContext);
				bIdentified = oParameter != null && p_sLogin.equals(oParameter.getValue());
				
				if (bIdentified) {
					oParameter = oMParametersDao.getMParametersByName(PREVIOUS_SYNC_DATE_PARAMETER,CascadeSet.NONE,p_oContext);
					bIdentified = oParameter != null;
				}
				
				if (bIdentified){
					Calendar oLastSyncDate = Calendar.getInstance();
					oLastSyncDate.setTimeInMillis(Long.valueOf(oParameter.getValue()));
					Property oDelta = ConfigurationsHandler.getInstance().getProperty(FwkPropertyName.synchronization_max_time_without_sync);
					if (oDelta.getLongValue() > 0) {
						bIdentified = Calendar.getInstance().getTimeInMillis()<(oLastSyncDate.getTimeInMillis()+(oDelta.getLongValue()*MILLISECONDS_IN_ONE_DAY));
					}
					if (!bIdentified) {
						r_oResult = IdentResult.koCauseOfDate;
					}
				}
				
				if (bIdentified) {
					oParameter = oMParametersDao.getMParametersByName(PREVIOUS_RESOURCE_PARAMETER,CascadeSet.NONE,p_oContext);
					bIdentified = oParameter != null;
					if (bIdentified) {
						Application.getInstance().setCurrentUserResource(Long.parseLong(AESUtil.decrypt(oParameter.getValue())));
						r_oResult = IdentResult.ok;
					}
				}
				
			}
		} catch( Exception oException ) {
			throw new CredentialException(oException);
		}
		return r_oResult;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * (non-Javadoc)
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.credentials.LocalCredentialService#deleteLocalCredentials(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext)
	 */
	@Override
	public void deleteLocalCredentials( MContext p_oContext ) throws CredentialException {
		try {
			MParametersDao oDataHelper = BeanLoader.getInstance().getBean(MParametersDao.class);
			oDataHelper.deleteMparametersByName(p_oContext, MParametersField.NAME, Types.VARCHAR, PREVIOUS_LOGIN_PARAMETER);
			oDataHelper.deleteMparametersByName(p_oContext, MParametersField.NAME, Types.VARCHAR, PREVIOUS_RESOURCE_PARAMETER);
			oDataHelper.deleteMparametersByName(p_oContext, MParametersField.NAME, Types.VARCHAR, PREVIOUS_SYNC_DATE_PARAMETER);
		} catch( DaoException oException ) {
			throw new CredentialException(oException);
		}
	}
}
