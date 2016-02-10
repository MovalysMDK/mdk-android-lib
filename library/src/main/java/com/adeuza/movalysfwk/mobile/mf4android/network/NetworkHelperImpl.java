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
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
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
		WifiInfo oWifiInf = oWifiMan.getConnectionInfo();
		return oWifiInf.getMacAddress();
	}
}
