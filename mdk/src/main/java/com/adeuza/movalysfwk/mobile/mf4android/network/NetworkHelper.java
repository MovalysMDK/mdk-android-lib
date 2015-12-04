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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.network.ConnectionType;

/**
 * <p>Classe helper pour la récupération des informations sur la connection du device.</p>
 *
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
