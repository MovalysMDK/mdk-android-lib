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

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.CascadeSet;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ResultSetReader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlQuery;

/**
 * <p>Interface des requêtes pour la couche Dao avec gestion des spécificités Movalys : blocs histo + références</p>
 *
 *
 * @since 2.5
 */
public interface DaoQuery {

	/**
	 * Construit le preparedStatement de la requête
	 *
	 * @param p_oContext
	 *            contexte transactionnel
	 * @return le preparedStatement de la requête
	 * @throws java.sql.SQLException
	 *             echec de creation du preparedStatement
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             echec de creation du preparedStatement
	 */
	public PreparedStatement prepareStatement(MContext p_oContext) throws DaoException, SQLException ;

	/**
	 * Bind les valeurs de la requête SQL
	 *
	 * @param p_oStatement
	 *            preparedStatement de la requete SQL
	 * @throws java.sql.SQLException
	 *             echec du binding
	 */
	public void bindValues(PreparedStatement p_oStatement) throws SQLException ;

	/**
	 * Retourne la SqlQuery
	 *
	 * @return SqlQuery
	 */
	public SqlQuery getSqlQuery() ;

	/**
	 * Value l'attribut rowReaderCallBack
	 *
	 * @param p_oRowReaderCallBack Nouvelle valeur pour l'attribut rowReaderCallBack
	 */
	public void addRowReaderCallBack(EntityRowReaderCallBack p_oRowReaderCallBack) ;
	
	/**
	 * <p>Lance la lecture spécifique du resultset</p>
	 * <p>L'entité est déjà lue et passé en paramètre de cette méthode.</p>
	 * <p>Cette méthode permet de lire les champs SQL qui auraient été rajoutés à la main</p>
	 *
	 * @param p_oEntity entité correspondant à la ligne en cours du resultset
	 * @param p_oResultSet le resultset
	 * @param p_oDaoSession la session dao
	 * @param p_oCascadeSet la cascade
	 * @throws java.sql.SQLException  échec SQL
	 */
	public void doResultSetCustomRead( MEntity p_oEntity, ResultSetReader p_oResultSet, DaoSession p_oDaoSession, CascadeSet p_oCascadeSet ) throws SQLException ;

	/**
	 * Retourne l'objet fetchSize
	 *
	 * @return Objet fetchSize
	 */
	public int getFetchSize();

	/**
	 * Affecte l'objet fetchSize
	 *
	 * @param p_iFetchSize a int.
	 */
	public void setFetchSize(int p_iFetchSize);
}
