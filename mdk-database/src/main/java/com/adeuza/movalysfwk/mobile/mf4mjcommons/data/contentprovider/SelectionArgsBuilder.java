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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.data.contentprovider;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.SqlType;

/**
 * Builder of SelectionArgs for ContentResolver
 */
public class SelectionArgsBuilder {

	/** selection args */
	private List<String> selectionArgs = new ArrayList<>();

	/**
	 * Add a value
	 * @param p_oValue value
	 * @param p_oSqlType value type
	 */
	public void addValue( Object p_oValue, SqlType p_oSqlType ) {
		int iTargetSqlType = p_oSqlType.intValue();
		if(p_oSqlType.intValue() == Types.NUMERIC && !Long.class.isAssignableFrom(p_oValue.getClass())){
			iTargetSqlType = Types.INTEGER; 
		}
		
		if(p_oSqlType.intValue() == Types.DECIMAL){
			iTargetSqlType = Types.FLOAT;
		}
		
		switch (iTargetSqlType) {
			case Types.NUMERIC:
				selectionArgs.add( Long.toString((Long)p_oValue));
				break;
			case Types.FLOAT:
				selectionArgs.add( Float.toString((Float)p_oValue));
				break;
			case Types.DOUBLE:
				selectionArgs.add( Double.toString((Double)p_oValue));
				break;
			case Types.VARCHAR:
				selectionArgs.add((String)p_oValue);
				break;
			case Types.DATE:
				Date oDate = (Date)p_oValue;
				selectionArgs.add(Long.toString(oDate.getTime()));
				break;
			case Types.TIME:
				Time oTime = (Time)p_oValue;
				selectionArgs.add(Long.toString(oTime.getTime()));
				break;
			case Types.TIMESTAMP:
				Timestamp oTimetamp = (Timestamp) p_oValue;
				selectionArgs.add(Long.toString(oTimetamp.getTime()));
				break;
			case Types.BOOLEAN:
				selectionArgs.add(Boolean.toString((Boolean)p_oValue));
				break;
			default:
				selectionArgs.add(p_oValue.toString());
		}
	}


	/**
	 * Return selection args
	 * @return selection args
	 */
	public String[] getSelectionArgs() {
		String[] r_t_sResult = null;
		if (!this.selectionArgs.isEmpty()) {
			r_t_sResult = new String[this.selectionArgs.size()];
			this.selectionArgs.toArray(r_t_sResult);
		}
		return r_t_sResult;
	}
}
