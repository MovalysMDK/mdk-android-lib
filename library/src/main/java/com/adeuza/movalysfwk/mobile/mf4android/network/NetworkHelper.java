package com.adeuza.movalysfwk.mobile.mf4android.network;

import android.content.Context;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.network.ConnectionType;

/**
 * <p>Classe helper pour la récupération des informations sur la connection du device.</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @since MF-Annapurna
 */
public interface NetworkHelper {

	/**
	 * Récupération du type de connection courante sur le device.
	 * @param p_oContext context applicatif.
	 * @return le type de connection.
	 */
	public ConnectionType getConnectionType(Context p_oContext);

	/**
	 * Retourne l'identification du device courant sous la forme d'une Mac adresse.
	 * @param p_oContext le context applicatif
	 * @return la mac adresse sous la forme d'une chaine de caractère
	 */
	public String getMacAddress(Context p_oContext);
}
