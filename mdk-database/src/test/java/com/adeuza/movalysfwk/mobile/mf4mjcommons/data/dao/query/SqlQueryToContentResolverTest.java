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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.contentprovider.ContentResolverQuery;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.Field;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.OrderAsc;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.OrderDesc;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.OrderSet;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.SqlType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.OperatorCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlBetweenCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlCompareFieldCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlCompareValueCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlEqualsFieldCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlGenericCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlGroupCondition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions.SqlNullCondition;

/**
 * TestCase to pass data from SqlQuery to ContentResolver
 *
 */
public class SqlQueryToContentResolverTest extends TestCase {
	
	/**
	 * @throws DaoException dao exception
	 * 
	 */
	@Test
	public void testSelectWithOrder() throws DaoException {
		
		SqlQuery oSqlQuery = createBaseQuery();
		oSqlQuery.setOrderBy(OrderSet.of(OrderAsc.of(TestField.NAME), OrderDesc.of(TestField.DESCRIPTION)));
		ContentResolverQuery oCRQuery = oSqlQuery.toContentResolverQuery();

		assertNotNull(oCRQuery.getProjection());
		assertEquals(oCRQuery.getProjection().length, 5);
		assertEquals(oCRQuery.getProjection()[0], "ID");
		assertEquals(oCRQuery.getProjection()[1], "NAME");
		assertEquals(oCRQuery.getProjection()[2], "DESCRIPTION");
		assertEquals(oCRQuery.getProjection()[3], "AGE");
		assertEquals(oCRQuery.getProjection()[4], "CREATIONDATE");
		assertEquals(oCRQuery.getOrder(), "NAME ASC,DESCRIPTION DESC");
		assertNull(oCRQuery.getSelection());
		assertNull(oCRQuery.getSelectionArgs());
	}
	
	/**
	 * @throws DaoException dao exception
	 */
	@Test
	public void testSelectWithBetween() throws DaoException {
		SqlQuery oSqlQuery = createBaseQuery();
		oSqlQuery.addToWhere( new SqlBetweenCondition(new SqlField(TestField.AGE), 16, 25, SqlType.INTEGER));
		
		ContentResolverQuery oCRQuery = oSqlQuery.toContentResolverQuery();
		assertNotNull(oCRQuery.getSelection());
		assertNotNull(oCRQuery.getSelectionArgs());
		assertEquals(oCRQuery.getSelection(), "AGE BETWEEN ? AND ?");
		assertEquals(oCRQuery.getSelectionArgs().length, 2);
		assertEquals(oCRQuery.getSelectionArgs()[0], "16");
		assertEquals(oCRQuery.getSelectionArgs()[1], "25");
	}
	
	/**
	 * @throws DaoException dao exception
	 */
	@Test
	public void testSelectWithNullCondition() throws DaoException {
		SqlQuery oSqlQuery = createBaseQuery();
		oSqlQuery.addToWhere( new SqlNullCondition(TestField.DESCRIPTION));
		
		ContentResolverQuery oCRQuery = oSqlQuery.toContentResolverQuery();
		assertNotNull(oCRQuery.getSelection());
		assertNull(oCRQuery.getSelectionArgs());
		assertEquals(oCRQuery.getSelection(), "DESCRIPTION IS NULL ");
	}

	/**
	 * @throws DaoException dao exception
	 */
	@Test
	public void testSelectWithLikeCondition() throws DaoException {
		SqlQuery oSqlQuery = createBaseQuery();
		oSqlQuery.addLikeConditionToWhere(TestField.DESCRIPTION.name(), "men", SqlType.VARCHAR);
		
		ContentResolverQuery oCRQuery = oSqlQuery.toContentResolverQuery();
		assertNotNull(oCRQuery.getSelection());
		assertNotNull(oCRQuery.getSelectionArgs());
		assertEquals(oCRQuery.getSelection(), "DESCRIPTION LIKE ?");
		assertEquals(oCRQuery.getSelectionArgs().length, 1);
		assertEquals(oCRQuery.getSelectionArgs()[0], "men%");
	}
	
	/**
	 * @throws DaoException dao exception
	 */
	@Test
	public void testSelectWithCompareFieldCondition() throws DaoException {
		SqlQuery oSqlQuery = createBaseQuery();
		oSqlQuery.addToWhere( new SqlCompareFieldCondition(
			TestField.NAME, TestField.DESCRIPTION, OperatorCondition.DIFFERENT));
		
		ContentResolverQuery oCRQuery = oSqlQuery.toContentResolverQuery();
		assertNotNull(oCRQuery.getSelection());
		assertNull(oCRQuery.getSelectionArgs());
		assertEquals(oCRQuery.getSelection(), "NAME <> DESCRIPTION");
	}
	
	/**
	 * @throws DaoException dao exception
	 */
	@Test
	public void testSelectWithCompareValueCondition() throws DaoException {
		SqlQuery oSqlQuery = createBaseQuery();
		oSqlQuery.addToWhere(
			new SqlCompareValueCondition(TestField.AGE.name(), 17, 
					SqlType.INTEGER, OperatorCondition.SUPERIOR));
		
		ContentResolverQuery oCRQuery = oSqlQuery.toContentResolverQuery();
		assertNotNull(oCRQuery.getSelection());
		assertNotNull(oCRQuery.getSelectionArgs());
		assertEquals(oCRQuery.getSelection(), "AGE > ?");
		assertEquals(oCRQuery.getSelectionArgs().length, 1);
		assertEquals(oCRQuery.getSelectionArgs()[0], "17");
	}
	
	/**
	 * @throws DaoException dao exception
	 */
	@Test
	public void testSelectWithEqualsFieldCondition() throws DaoException {
		SqlQuery oSqlQuery = createBaseQuery();
		oSqlQuery.addToWhere( new SqlEqualsFieldCondition(TestField.NAME, TestField.DESCRIPTION));
		
		ContentResolverQuery oCRQuery = oSqlQuery.toContentResolverQuery();
		assertNotNull(oCRQuery.getSelection());
		assertNull(oCRQuery.getSelectionArgs());
		assertEquals(oCRQuery.getSelection(), "NAME = DESCRIPTION");
	}
	
	/**
	 * @throws DaoException dao exception
	 */
	@Test
	public void testSelectWithEqualsValueCondition() throws DaoException {
		SqlQuery oSqlQuery = createBaseQuery();
		oSqlQuery.addEqualsConditionToWhere(TestField.NAME, null, "bugdroid", SqlType.VARCHAR);
		
		ContentResolverQuery oCRQuery = oSqlQuery.toContentResolverQuery();
		assertNotNull(oCRQuery.getSelection());
		assertNotNull(oCRQuery.getSelectionArgs());
		assertEquals(oCRQuery.getSelection(), "NAME = ?");
		assertEquals(oCRQuery.getSelectionArgs().length, 1);
		assertEquals(oCRQuery.getSelectionArgs()[0], "bugdroid");
	}
	
	/**
	 * @throws DaoException dao exception
	 */
	@Test
	public void testSelectWithGenericCondition() throws DaoException {
		SqlQuery oSqlQuery = createBaseQuery();
		oSqlQuery.addToWhere( 
			new SqlGenericCondition("AGE = ?", new SqlBindValue(50, SqlType.INTEGER)));
		
		ContentResolverQuery oCRQuery = oSqlQuery.toContentResolverQuery();
		assertNotNull(oCRQuery.getSelection());
		assertNotNull(oCRQuery.getSelectionArgs());
		assertEquals(oCRQuery.getSelection(), "AGE = ?");
		assertEquals(oCRQuery.getSelectionArgs().length, 1);
		assertEquals(oCRQuery.getSelectionArgs()[0], "50");
	}
	
	/**
	 * @throws DaoException dao exception
	 */
	@Test
	public void testSelectWithGroupCondition() throws DaoException {
		SqlQuery oSqlQuery = createBaseQuery();
		
		SqlGroupCondition oSqlGroupCondition1 = new SqlGroupCondition(
				SqlClauseLink.AND,
				SqlClauseLink.OR,
				new SqlCompareValueCondition(TestField.AGE.name(), 18, SqlType.INTEGER, OperatorCondition.INFERIOR),
				new SqlCompareValueCondition(TestField.AGE.name(), 60, SqlType.INTEGER, OperatorCondition.SUPERIOR));
		
		oSqlQuery.addToWhere(oSqlGroupCondition1);
		oSqlQuery.addLikeConditionToWhere(TestField.DESCRIPTION.name(), "women", SqlType.VARCHAR);
		
		ContentResolverQuery oCRQuery = oSqlQuery.toContentResolverQuery();
		assertNotNull(oCRQuery.getSelection());
		assertNotNull(oCRQuery.getSelectionArgs());
		assertEquals(oCRQuery.getSelection(), "(AGE < ? OR AGE > ?) AND DESCRIPTION LIKE ?");
		assertEquals(oCRQuery.getSelectionArgs().length, 3);
		assertEquals(oCRQuery.getSelectionArgs()[0], "18");
		assertEquals(oCRQuery.getSelectionArgs()[1], "60");
		assertEquals(oCRQuery.getSelectionArgs()[2], "women%");
	}
	
	/**
	 * @throws DaoException dao exception
	 */
	@Test
	public void testSelectWithInValueCondition() throws DaoException {
		SqlQuery oSqlQuery = createBaseQuery();
		List<Integer> listValues = new ArrayList<>();
		listValues.add(20);
		listValues.add(30);
		listValues.add(40);
		listValues.add(50);
		oSqlQuery.addInValueConditionToWhere(TestField.AGE, null, SqlType.INTEGER, listValues);
		
		ContentResolverQuery oCRQuery = oSqlQuery.toContentResolverQuery();
		assertNotNull(oCRQuery.getSelection());
		assertNotNull(oCRQuery.getSelectionArgs());
		assertEquals(oCRQuery.getSelection(), "AGE IN (?,?,?,?,?)");
		assertEquals(oCRQuery.getSelectionArgs().length, 5);
		assertEquals(oCRQuery.getSelectionArgs()[0], "20");
		assertEquals(oCRQuery.getSelectionArgs()[1], "30");
		assertEquals(oCRQuery.getSelectionArgs()[2], "40");
		assertEquals(oCRQuery.getSelectionArgs()[3], "50");
		assertEquals(oCRQuery.getSelectionArgs()[4], "20");
	}
	
	/**
	 * @throws DaoException dao exception
	 */
	@Test
	public void testSelectWithInValueCondition2() throws DaoException {
		SqlQuery oSqlQuery = createBaseQuery();
		List<Integer> listValues = new ArrayList<>();
		listValues.add(20);
		listValues.add(30);
		listValues.add(40);
		listValues.add(50);
		listValues.add(60);
		listValues.add(70);
		oSqlQuery.addInValueConditionToWhere(TestField.AGE, null, SqlType.INTEGER, listValues);
		
		ContentResolverQuery oCRQuery = oSqlQuery.toContentResolverQuery();
		assertNotNull(oCRQuery.getSelection());
		assertNotNull(oCRQuery.getSelectionArgs());
		assertEquals(oCRQuery.getSelection(), "(AGE IN (?,?,?,?,?) OR AGE IN (?,?,?,?,?))");
		assertEquals(oCRQuery.getSelectionArgs().length, 10);
		assertEquals(oCRQuery.getSelectionArgs()[0], "20");
		assertEquals(oCRQuery.getSelectionArgs()[1], "30");
		assertEquals(oCRQuery.getSelectionArgs()[2], "40");
		assertEquals(oCRQuery.getSelectionArgs()[3], "50");
		assertEquals(oCRQuery.getSelectionArgs()[4], "60");
		assertEquals(oCRQuery.getSelectionArgs()[5], "70");
		assertEquals(oCRQuery.getSelectionArgs()[6], "20");
		assertEquals(oCRQuery.getSelectionArgs()[7], "20");
		assertEquals(oCRQuery.getSelectionArgs()[8], "20");
		assertEquals(oCRQuery.getSelectionArgs()[9], "20");
	}
	
	/**
	 * @throws DaoException dao exception
	 */	
	@Test
	public void testSelectWithTimestamp() throws DaoException {
		SqlQuery oSqlQuery = createBaseQuery();
		
		Timestamp oCreationValue = new Timestamp(System.currentTimeMillis());
		oSqlQuery.addToWhere(
			new SqlCompareValueCondition(TestField.CREATIONDATE.name(), oCreationValue, 
					SqlType.TIMESTAMP, OperatorCondition.SUPERIOR));
		
		ContentResolverQuery oCRQuery = oSqlQuery.toContentResolverQuery();
		assertNotNull(oCRQuery.getSelection());
		assertNotNull(oCRQuery.getSelectionArgs());
		assertEquals(oCRQuery.getSelection(), "CREATIONDATE > ?");
		assertEquals(oCRQuery.getSelectionArgs().length, 1);
		assertEquals(oCRQuery.getSelectionArgs()[0], Long.toString(oCreationValue.getTime()));
	}
	
	/**
	 * Create a select with all fields and no condition
	 * @return select with all fields and no condition
	 */
	private SqlQuery createBaseQuery() {
		SqlQuery r_oSqlQuery = new SqlQuery();
		r_oSqlQuery.addFieldToSelect("a", TestField.ID, TestField.NAME, 
				TestField.DESCRIPTION, TestField.AGE, TestField.CREATIONDATE);
		r_oSqlQuery.addToFrom("T_PARAMETERS", "parameters1");
		return r_oSqlQuery;
	}
	
	private static enum TestField implements Field {
		
		ID(0),
		NAME(1),
		DESCRIPTION(2),
		AGE(3),
		CREATIONDATE(4);
		
		private int index;

		private TestField(int p_iIndex) {
			this.index = p_iIndex;
		}
		
		@Override
		public int getColumnIndex() {
			return index;
		}
	}
}
