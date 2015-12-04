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
package com.adeuza.movalysfwk.mobile.mf4android.security;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.utils.security.AbstractLocalAuthHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;

/**
 * Local authentication helper
 * <p>Simplify access to authentication methods</p>
 *
 */
public class LocalAuthHelperImpl extends AbstractLocalAuthHelper {

	/** singleton */
	private static final LocalAuthHelperImpl mLocalAuthHelper = new LocalAuthHelperImpl();
	
	/**
	 * Application instance
	 */
	private AndroidApplication application = (AndroidApplication) Application.getInstance();
	
	/**
	 * Singleton getter
	 * @return this
	 */
	public static LocalAuthHelperImpl getInstance() {
		return mLocalAuthHelper;
	}
	
	@Override
	public String decrypt(String p_sEncryptedString) {
		return super.decrypt(application.getApplicationContext(), p_sEncryptedString);
	}

	@Override
	public String encrypt(String p_sPassphrase) {
		return super.encrypt(application.getApplicationContext(), p_sPassphrase);
	}

}
