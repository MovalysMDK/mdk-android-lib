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

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.CascadeSet;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ResultSetReader;

/**
 * <p>Permet de compléter la lecture d'un resultset par rapport au valueObject du dao</p>
 *
 *
 */
public interface EntityRowReaderCallBack {

	/**
	 * <p>Lance la lecture de la ligne du resultset</p>
	 * <p>Ne doit pas appeler le next() sur le resultset !</p>
	 *
	 * @param p_oEntity l'entité de la ligne correspondante
	 * @param p_oDaoSession la dao session
	 * @param p_oCascadeSet la cascade
	 * @throws DaoException echec de lecture de la ligne
	 * @param p_oResultSetReader a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ResultSetReader} object.
	 */
	public void doReadRow( MEntity p_oEntity, ResultSetReader p_oResultSetReader, DaoSession p_oDaoSession, CascadeSet p_oCascadeSet )
		throws DaoException ;
}
