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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.conditions;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.contentprovider.SelectionArgsBuilder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.SqlClauseLink;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.StatementBinder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ToSqlContext;

/**
 * Links between sql clause.
 *
 */
public class SqlClauseLinks {

	/**
	 * "And" link.
	 */
	public static final AbstractSqlCondition AND = new ClauseLinkCondition(SqlClauseLink.AND); 
	
	/**
	 * "Or" link.
	 */
	public static final AbstractSqlCondition OR = new ClauseLinkCondition(SqlClauseLink.OR);
	
	/**
	 * Clause link.
	 *
	 */
	private static class ClauseLinkCondition extends AbstractSqlCondition {
		
		/**
		 * Sql clause link.
		 */
		private @SqlClauseLink String link ;

		/**
		 * @param link
		 */
		private ClauseLinkCondition(String p_sLink) {
			this.link = p_sLink;
		}
		
		@Override
		public void toSql(StringBuilder p_oStringBuilder, ToSqlContext p_oToSqlContext) throws DaoException {
			p_oStringBuilder.append(' ');
			p_oStringBuilder.append(this.link);
			p_oStringBuilder.append(' ');
		}
		
		@Override
		public void computeContentResolverSelectionArgs(SelectionArgsBuilder p_oSqlArgsBuilder) {
			//nothing to do
		}
		
		@Override
		public void computeContentResolverSelection(StringBuilder p_oSql) {
			p_oSql.append(' ');
			p_oSql.append(this.link);
			p_oSql.append(' ');
		}
		
		@Override
		public AbstractSqlCondition clone() {
			return this;
		}
		
		@Override
		public void bindValues(StatementBinder p_oStatementBinder) throws DaoException {
			//nothing to do
		}
	}
}
