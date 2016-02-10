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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContextImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.CascadeSet;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlDelete;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MParameters;


/**
 *
 * <p>Classe de DAO : MParametersDaoImpl</p>
 *
 */
public class MParametersDaoImpl extends AbstractMParametersDaoImpl implements MParametersDao {
	
	/** {@inheritDoc} */
	@Override
	public void deleteMparametersByName(MContext p_oContext, Field p_oField, SqlType p_oType,String p_sValue) throws DaoException {
		try {
			SqlDelete oSqlDelete = getDeleteQuery();
			oSqlDelete.addEqualsConditionToWhere(p_oField, p_sValue, p_oType);
			Connection oConnection = ((MContextImpl) p_oContext).getTransaction().getConnection();
			PreparedStatement oStatement = oConnection.prepareStatement(oSqlDelete.toSql(p_oContext));
			try {
				oSqlDelete.bindValues(oStatement);
				oStatement.executeUpdate();
			} finally {
				oStatement.close();
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<MParameters> getListMParametersByField(MContext p_oContext, Field p_oField, SqlType p_oType, Object p_oValue, CascadeSet p_oCascadeSet)
			throws DaoException {
		
		DaoQuery oQuery = this.getSelectDaoQuery();
		oQuery.getSqlQuery().addEqualsConditionToWhere(p_oField.name(), p_oValue, p_oType);
		return this.getListMParameters(oQuery, p_oCascadeSet, new DaoSession(), p_oContext);
	}
	
	/** {@inheritDoc} */
	@Override
	public MParameters getMParametersByName(String p_sName, CascadeSet p_oCascadeSet, MContext p_oContext)
			throws DaoException {
		
		DaoQuery oSelect = this.getSelectDaoQuery();
		oSelect.getSqlQuery().addEqualsConditionToWhere(MParametersField.NAME, MParametersDao.ALIAS_NAME, p_sName, SqlType.VARCHAR);

		List<MParameters> listParameters = this.getListMParameters(oSelect, p_oCascadeSet, p_oContext);
		if (listParameters == null || listParameters.isEmpty()) {
			return null;
		}
		else {
			return listParameters.iterator().next();
		}
	}
}
