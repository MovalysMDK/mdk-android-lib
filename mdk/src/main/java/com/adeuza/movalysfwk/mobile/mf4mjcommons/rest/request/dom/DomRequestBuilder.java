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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.dom;

import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterIN;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request.RestRequest;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.SynchroException;

/**
 * <p>Classe dédiée à la récupération de données que l'on souhaite synchroniser avec le serveur.</p>
 *
 *
 * @param <R> la request à builder
 */
public interface DomRequestBuilder<R extends RestRequest> {

	/**
	 * <p>Récupère un ensemble d'objets à synchroniser et modifie la requête à envoyer au serveur en conséquence.</p>
	 *
	 * @param p_oSyncRequest
	 * 		La requête qui va être envoyée au serveur. Ne doit jamais être null. Cet objet est modifié par effet de bord.
	 * @param p_oContext
	 * 		Contexte transactionnel
	 *
	 * @param p_oContext
	 * 		Contexte transactionnel
	 * @param p_oSynchronizationActionParameterIN a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.synchro.SynchronizationActionParameterIN} object.
	 * @param p_mapSyncTimestamp
	 * 		Map contenant la date de la dernière synchronisation réussi d'entité référentielle
	 * @param p_oContext
	 * 		Contexte transactionnel
	 *
	 * @param p_oContext
	 * 		Contexte transactionnel
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.synchro.SynchroException if any.
	 * @param p_bfirstGoToServer a boolean.
	 */
	public void buildRequest(boolean p_bfirstGoToServer, R p_oSyncRequest, 
			SynchronizationActionParameterIN p_oSynchronizationActionParameterIN,
			Map<String, Long> p_mapSyncTimestamp, MContext p_oContext ) throws SynchroException;
}
