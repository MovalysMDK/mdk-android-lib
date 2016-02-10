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

//@non-generated-start[imports]
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContextImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.CascadeSet;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ResultSetReader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlClauseLink;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlDelete;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.AbstractSqlCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlEqualsValueCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlGroupCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MObjectToSynchronize;

//@non-generated-end

/**
 *
 * <p>Classe de DAO : MObjectToSynchronizeDaoImpl</p>
 *
 */
public class MObjectToSynchronizeDaoImpl extends AbstractMObjectToSynchronizeDaoImpl implements MObjectToSynchronizeDao {

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.MObjectToSynchronizeDao#deleteObjectToSynchronizeByObjectIdAndObjectName(java.util.List, com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext)
	 */
	@Override
	public void deleteListMObjectToSynchronizeByObjectIdAndObjectName(List<MObjectToSynchronize> p_listObjectToSynchronize, MContext p_oContext) throws DaoException {
		if (!p_listObjectToSynchronize.isEmpty()) {
			try {
				SqlDelete oSqlDelete = getDeleteQuery();
				SqlEqualsValueCondition oObjectIdCondition = oSqlDelete.addEqualsConditionToWhere(MObjectToSynchronizeField.OBJECTID,
						SqlType.INTEGER);

				SqlEqualsValueCondition oObjectNameCondition = oSqlDelete.addEqualsConditionToWhere(MObjectToSynchronizeField.OBJECTNAME,
						SqlType.VARCHAR);

				Connection oConnection = ((MContextImpl) p_oContext).getTransaction().getConnection();
				PreparedStatement oStatement = oConnection.prepareStatement(oSqlDelete.toSql(p_oContext));
				try {
					for (MObjectToSynchronize oEntity : p_listObjectToSynchronize) {
						oObjectIdCondition.setValue(oEntity.getId());
						oObjectNameCondition.setValue(oEntity.getObjectName());
						oSqlDelete.bindValues(oStatement);
						oStatement.addBatch();
					}

					oStatement.executeBatch();
				} finally {
					oStatement.close();
				}

			} catch (SQLException e) {
				throw new DaoException(e);
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean isThereDataToSynchronize(MContext p_oContext) {
		List<MObjectToSynchronize> oList = new ArrayList<MObjectToSynchronize>();
		try {
			oList = super.getListMObjectToSynchronize(p_oContext);
		} catch (DaoException e) {
			throw new MobileFwkException(e);
		}
		return !oList.isEmpty();
	}
	
	/**
	 * <p>
	 * 	If there is an object in the database, with the same objectId and objectName, then this object will be deleted before the insert.
	 * </p>
	 * {@inheritDoc}
	 */
	@Override
	public void saveOrUpdateMObjectToSynchronize(MObjectToSynchronize p_oObject, MContext p_oContext) throws DaoException {
		
		List<MObjectToSynchronize> listObjects = this.getListMObjectToSynchronizeByObjectId(p_oObject.getObjectId(), p_oContext);
		for(MObjectToSynchronize oTemp : listObjects) {
			if (oTemp.getObjectName().equals(p_oObject.getObjectName())) {
				this.deleteMObjectToSynchronize(oTemp, p_oContext);
			}
		}
		super.saveOrUpdateMObjectToSynchronize(p_oObject,p_oContext);
	}

	/** {@inheritDoc} */
	@Override
	public List<MObjectToSynchronize> getListMObjectToSynchronizeByObjectId(long p_lObjectId,MContext p_oContext) throws DaoException {
		List<MObjectToSynchronize> r_oReturnList = new ArrayList<MObjectToSynchronize>();
		try {
			
			DaoQuery oDaoQuery = this.getSelectDaoQuery();
			oDaoQuery.getSqlQuery().addEqualsConditionToWhere(MObjectToSynchronizeField.OBJECTID,MObjectToSynchronizeDao.ALIAS_NAME, p_lObjectId,SqlType.INTEGER);
			oDaoQuery.getSqlQuery().setOrderBy(OrderSet.of(OrderAsc.of(MObjectToSynchronizeField.OBJECTID)));
			
			PreparedStatement oStatement = oDaoQuery.prepareStatement(p_oContext);
			try {
				oDaoQuery.bindValues(oStatement);
				ResultSetReader oResultSetReader = new ResultSetReader(oStatement.executeQuery());
				try {
						
					while (oResultSetReader.next()) {
						r_oReturnList.add(this.valueObject(oResultSetReader, oDaoQuery, new DaoSession(), CascadeSet.NONE, p_oContext));
					}
				} finally {
					oResultSetReader.close();
				}
			} finally {
				oStatement.close();
			}
		
		} catch (SQLException e) {
			throw new DaoException(e);
		} catch (IOException e) {
			throw new DaoException(e);
		}
		
		return r_oReturnList;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.AbstractMObjectToSynchronizeDaoImpl#saveOrUpdateListMObjectToSynchronize(java.util.Collection, com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.CascadeSet, com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext)
	 */
	@Override
	public void saveOrUpdateListMObjectToSynchronize(Collection<MObjectToSynchronize> p_listMObjectToSynchronize, MContext p_oContext) throws DaoException {

		if (p_listMObjectToSynchronize != null && !p_listMObjectToSynchronize.isEmpty()) {
			List<AbstractSqlCondition> listConditions = new ArrayList<AbstractSqlCondition>();
			for (MObjectToSynchronize oMObjectToSynchronize : p_listMObjectToSynchronize) {
				listConditions.add(new SqlGroupCondition(SqlClauseLink.AND, SqlClauseLink.AND,
						new SqlEqualsValueCondition(MObjectToSynchronizeField.OBJECTID, MObjectToSynchronizeDao.TABLE_NAME, oMObjectToSynchronize.getObjectId(), SqlType.INTEGER),
						new SqlEqualsValueCondition(MObjectToSynchronizeField.OBJECTNAME, MObjectToSynchronizeDao.TABLE_NAME, oMObjectToSynchronize.getObjectName(), SqlType.VARCHAR)));

				if (oMObjectToSynchronize.getId() < 0L) {
					oMObjectToSynchronize.setId(this.nextId());
				}
			}

			SqlDelete oDelete = this.getDeleteQuery();
			oDelete.addToWhere(new SqlGroupCondition(listConditions, SqlClauseLink.AND, SqlClauseLink.OR));
			this.genericDelete(oDelete, p_oContext);

			super.saveListMObjectToSynchronize(p_listMObjectToSynchronize, CascadeSet.NONE, p_oContext);
		}
	}
}
