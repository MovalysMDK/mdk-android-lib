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
package com.adeuza.movalysfwk.mobile.mf4android.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.network.ConnectionType;

/**
 * <p>Implémentation du helper pour la récupération des informations de la connection courante du device.</p>
 *
 *
 * @since MF-Annapurna
 */
public class NetworkHelperImpl implements NetworkHelper {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConnectionType getConnectionType(Context p_oContext) {

		ConnectionType r_oConnectionType = ConnectionType.NONE;

		ConnectivityManager oConnectivityManager = (ConnectivityManager) p_oContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager oTelephonyManager = (TelephonyManager) p_oContext
				.getSystemService(Context.TELEPHONY_SERVICE);

		//TODO PERM ACCESS_NETWORK_STATE
		NetworkInfo oNetWorkInfo = oConnectivityManager.getActiveNetworkInfo();

		if (oNetWorkInfo != null && oNetWorkInfo.isConnected()) {
			int iNetType = oNetWorkInfo.getType(); // Mobile or Wifi
			int iNetSubtype = oNetWorkInfo.getSubtype();

			if (iNetType == ConnectivityManager.TYPE_WIFI) {
				r_oConnectionType = ConnectionType.WIFI;
			} else if (iNetType == ConnectivityManager.TYPE_MOBILE
					&& iNetSubtype == TelephonyManager.NETWORK_TYPE_UMTS
					&& !oTelephonyManager.isNetworkRoaming()) {
				r_oConnectionType = ConnectionType.UMTS;
			}
		}

		return r_oConnectionType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMacAddress(Context p_oContext) {
		WifiManager oWifiMan = (WifiManager) p_oContext.getSystemService(Context.WIFI_SERVICE);
		//TODO PERM ACCESS_WIFI_STATE
		WifiInfo oWifiInf = oWifiMan.getConnectionInfo();
		return oWifiInf.getMacAddress();
	}
}
