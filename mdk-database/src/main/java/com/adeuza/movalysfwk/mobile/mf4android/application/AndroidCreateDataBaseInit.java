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

import java.sql.SQLException;
import java.util.Properties;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

import com.adeuza.movalysfwk.mobile.mf4android.context.AndroidContextImpl;
import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteDriver;
import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLiteParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.AbstractCreateDataBaseInit;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.ItfTransactionalContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContextFactory;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;

/**
 * <p>Initializes the database creation for android.</p>
 *
 *
 */
public class AndroidCreateDataBaseInit extends AbstractCreateDataBaseInit {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void deleteDatabase(MContext p_oContext, String p_sDatabaseName) {
		((AndroidContextImpl) p_oContext).getAndroidNativeContext().deleteDatabase(p_sDatabaseName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createDataBase(MContext p_oContext, String p_sDatabaseName) throws SQLException {
		MContextFactory oCf = BeanLoader.getInstance().getBean(MContextFactory.class);
		MContext oContext = oCf.createContext();
		((ItfTransactionalContext)oContext).getTransaction().getConnection(); //retour sur la mécanique classique d'android de création au premier appel
		oContext.endTransaction();
		oContext.close();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Properties resolveConnectionProperties(MContext p_oContext) {
		try {
			Context oNativeContext = ((AndroidContextImpl) p_oContext).getAndroidNativeContext();

			// Retrieve Application Version
			int iVersionCode = oNativeContext.getPackageManager().getPackageInfo(oNativeContext.getPackageName(), 0).versionCode;
			Properties r_oPropertiesInfo = new Properties();
	
			r_oPropertiesInfo.put(AndroidSQLiteParameter.ANDROID_CONTEXT_PARAMETER, oNativeContext);
			r_oPropertiesInfo.put(AndroidSQLiteParameter.DB_VERSION_PARAMETER, iVersionCode );
	
			return r_oPropertiesInfo;
		}
		catch (NameNotFoundException e) {
			throw new MobileFwkException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String resolveDatabaseURL(MContext p_oContext, String p_sDataBaseName) {
		return new StringBuilder()
				.append(AndroidSQLiteDriver.URL_PREFIX)
				.append(((AndroidContextImpl) p_oContext).getAndroidNativeContext().getDatabasePath(p_sDataBaseName))
				.toString();
	}
}
