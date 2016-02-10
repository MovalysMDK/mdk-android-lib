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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query;

import java.util.HashMap;
import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Contexte pour la transformation des Query en sql</p>
 *
 *
 */
public class ToSqlContext {

	/**
	 * Transactional context 
	 */
	private MContext txContext ;

	/**
	 * Map des champs d'alias
	 */
	private Map<String,String> fieldAliases = new HashMap<String,String>();

	/**
	 * Constructeur
	 *
	 * @param p_oTxContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.ItfTransactionalContext} object.
	 */
	public ToSqlContext(MContext p_oTxContext) {
		super();
		this.txContext = p_oTxContext;
	}

	/**
	 * Retourne l'objet txContext
	 *
	 * @return Objet txContext
	 */
	public MContext getTxContext() {
		return this.txContext;
	}

	/**
	 * Enregistre un alias de champs
	 *
	 * @param p_sFieldName nom du champs
	 * @param p_sFieldAlias alias du champs
	 */
	public void registerFieldAlias(String p_sFieldName, String p_sFieldAlias) {
		this.fieldAliases.put(p_sFieldName, p_sFieldAlias);
	}

	/**
	 * Renvoie l'alias du champs
	 *
	 * @param p_sFieldName nom du champs
	 * @return l'alias du champs
	 */
	public String getFieldAlias(String p_sFieldName) {
		return this.fieldAliases.get(p_sFieldName);
	}
}
