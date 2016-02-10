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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao;

import java.sql.SQLException;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;

/**
 * <p>Interface du binder des valeurs lors de la mise à jour des relations 'one-to-many' après l'enregistrement.</p>
 *
 *
 * @since 2.5
 */
public interface StatementBinderCallBack {

	/**
	 * Permet de binder les paramètres.
	 *
	 * @param p_oStatementBinder Classe pour binder les parametres d'un prepareStatement
	 * @throws java.sql.SQLException déclenchée si l'exécution du bind déclencenche une exception SQL
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException déclenchée si l'exécution d'un Dao déclenche une exception technique
	 */
	public void doBind( StatementBinder p_oStatementBinder ) throws SQLException, DaoException ; 
	
}
