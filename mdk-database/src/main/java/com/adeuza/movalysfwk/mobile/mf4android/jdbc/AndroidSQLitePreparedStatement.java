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
package com.adeuza.movalysfwk.mobile.mf4android.jdbc;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;

/**
 * <p>Implements "PreparedStatement" of JDBC API for android devices.</p>
 */
public class AndroidSQLitePreparedStatement extends AndroidSQLiteStatement {

	/**
	 * All binded values (Object format)
	 */
	private List<Object> bindedObjects;

	/**
	 * All binded values (String format)
	 */
	private List<String> bindedStrings;

	/**
	 * Previous binded values stored is this object when {@link #addBatch()} is called.
	 */
	private Queue<List<Object>> batchBindedObjects;

	/**
	 * True if the sql instructions is an insert.
	 */
	private boolean select;

	/**
	 * Creates a new instance of this class using a connection and a sql instruction.
	 * @param p_oConnection A database connection
	 * @param p_sQuery a sql instruction
	 */
	public AndroidSQLitePreparedStatement(AndroidSQLiteConnection p_oConnection, String p_sQuery) {
		super(p_oConnection);

		this.sql = p_sQuery;
		this.select			= isSelect(this.sql);
		this.bindedObjects	= new ArrayList<>();
		this.bindedStrings	= new ArrayList<>();

		this.batchBindedObjects = new LinkedList<>();
	}

	//FIXME: Javadoc
	public AndroidSQLiteResultSet executeQuery() throws DaoException {
		if(!this.select) {
			throw new DaoException("impossible to call \"executeQuery\" if the sql instruction is not a \"select\".");
		}

//		final long lStart = System.currentTimeMillis();
//		Log.d("SQL", "executeQuery, sql: "  + this.sql + ", binded values: " + this.bindedStrings.toString());

		AndroidSQLiteResultSet r_oResultSet = new AndroidSQLiteResultSet(this, this.getConnection().getAndroidDatabase().rawQuery(this.sql,
				this.bindedStrings.toArray(new String[this.bindedStrings.size()])));

//		Log.d("SQL", "executeQuery, tps: " + (System.currentTimeMillis() - lStart) + " ms");

		return r_oResultSet; 
	}

	//FIXME: Javadoc
	public int executeUpdate() throws DaoException {
		if (this.select) {
			throw new DaoException("Impossible to call \"executeUpdate\" if the sql instruction is not an update intruction.");
		}
		try {
			Log.d("SQL", "executeUpdate, sql: "  + this.sql + ", binded values: " + this.bindedObjects.toString());
			this.getConnection().getAndroidDatabase().execSQL(this.sql, this.bindedObjects.toArray());
		} catch(DaoException oDaoException) {
			Log.e("SQL", "executeUpdate failed ! sql: "  + this.sql + ", binded values: " + this.bindedObjects.toString());
			throw oDaoException ;
		}
		return 0;
	}

	//FIXME: Javadoc
	public void setNull(int p_iParameterIndex, int p_oSqlType) throws DaoException {
		this.setBindedValue(p_iParameterIndex, null, null);
	}

	//FIXME: Javadoc
	public void setBoolean(int p_iParameterIndex, boolean p_bValue) throws DaoException {
		if (p_bValue){
			this.setInt(p_iParameterIndex, 1 );
		}else{
			this.setInt(p_iParameterIndex, 0 );
		}
	}

	//FIXME: Javadoc
	public void setByte(int p_iParameterIndex, byte p_bValue) throws DaoException {
		this.setBindedValue(p_iParameterIndex, p_bValue, Short.toString(p_bValue));
	}

	//FIXME: Javadoc
	public void setShort(int p_iParameterIndex, short p_sValue) throws DaoException {
		this.setBindedValue(p_iParameterIndex, p_sValue, Short.toString(p_sValue));
	}

	//FIXME: Javadoc
	public void setInt(int p_iParameterIndex, int p_iValue) throws DaoException {
		this.setBindedValue(p_iParameterIndex, p_iValue, Integer.toString(p_iValue));
	}

	//FIXME: Javadoc
	public void setLong(int p_iParameterIndex, long p_lValue) throws DaoException {
		this.setBindedValue(p_iParameterIndex, p_lValue, Long.toString(p_lValue));
	}

	//FIXME: Javadoc
	public void setFloat(int p_iParameterIndex, float p_fValue) throws DaoException {
		this.setBindedValue(p_iParameterIndex, p_fValue, Float.toString(p_fValue));
	}

	//FIXME: Javadoc
	public void setDouble(int p_iParameterIndex, double p_dValue) throws DaoException {
		this.setBindedValue(p_iParameterIndex, p_dValue, Double.toString(p_dValue));
	}

	//FIXME: Javadoc
	public void setString(int p_iParameterIndex, String p_sValue) throws DaoException {
		this.setBindedValue(p_iParameterIndex, p_sValue, p_sValue);
	}

	//FIXME: Javadoc
	public void setDate(int p_iParameterIndex, Date p_oValue) throws DaoException {
		if (p_oValue == null) {
			this.setNull(p_iParameterIndex, Types.DATE);
		}
		else {
			this.setLong(p_iParameterIndex, p_oValue.getTime());
		}
	}

	//FIXME: Javadoc
	public void setTime(int p_iParameterIndex, Time p_oValue) throws DaoException {
		if (p_oValue == null) {
			this.setNull(p_iParameterIndex, Types.TIME);
		}
		else {
			this.setLong(p_iParameterIndex, p_oValue.getTime());
		}
	}

	//FIXME: Javadoc
	public void setTimestamp(int p_iParameterIndex, Timestamp p_oValue) throws DaoException {
		if (p_oValue == null) {
			this.setNull(p_iParameterIndex, Types.TIMESTAMP);
		}
		else {
			this.setLong(p_iParameterIndex, p_oValue.getTime());
		}
	}

	//FIXME: Javadoc
	public void clearParameters() throws DaoException {
		this.bindedObjects.clear();
		this.bindedStrings.clear();
	}

	//FIXME: Javadoc
	public void setObject(int p_iParameterIndex, Object p_oValue, int p_iTargetSqlType) throws DaoException {
		
		int v_iTargetSqlType = p_iTargetSqlType;
		if(p_iTargetSqlType == Types.NUMERIC && !Long.class.isAssignableFrom(p_oValue.getClass())){
			v_iTargetSqlType = Types.INTEGER; 
		}
		
		if(p_iTargetSqlType == Types.DECIMAL){
			v_iTargetSqlType = Types.FLOAT;
		}
		
		switch (v_iTargetSqlType) {
			case Types.NUMERIC:
				this.setLong(p_iParameterIndex, (Long) p_oValue);
				break;
			case Types.FLOAT:
				this.setFloat(p_iParameterIndex, (Float) p_oValue);
				break;
			case Types.DOUBLE:
				this.setDouble(p_iParameterIndex, (Double) p_oValue);
				break;
			case Types.VARCHAR:
				this.setString(p_iParameterIndex, (String) p_oValue);
				break;
			case Types.DATE:
				this.setDate(p_iParameterIndex, (Date) p_oValue);
				break;
			case Types.TIME:
				this.setTime(p_iParameterIndex, (Time) p_oValue);
				break;
			case Types.TIMESTAMP:
				this.setTimestamp(p_iParameterIndex, (Timestamp) p_oValue);
				break;
			case Types.BOOLEAN:
				this.setBoolean(p_iParameterIndex, (Boolean) p_oValue);
				break;
			default:
				this.setObject(p_iParameterIndex, p_oValue);
		}
	}

	//FIXME: Javadoc
	public void setObject(int p_iParameterIndex, Object p_oValue) throws DaoException {
		String sValue = null;
		if (p_oValue != null) {
			sValue = p_oValue.toString();
		}
		this.setBindedValue(p_iParameterIndex, p_oValue, sValue);
	}

	//FIXME: javadoc
	public boolean execute() throws DaoException {
		try { 
			//Log.d("AndroidSQLitePreparedStatment.execute()", this.sql);
			this.getConnection().getAndroidDatabase().execSQL(this.sql, this.bindedObjects.toArray());
		} catch( DaoException oDaoException ) {
			Log.e("SQL", "execute failed ! sql: "  + this.sql + ", binded values: " + this.bindedObjects.toString());
			throw oDaoException ;
		}
		return true;
	}

	//FIXME: javadoc
	public void addBatch() throws DaoException {
		if (this.select) {
			throw new DaoException("Impossible to call \"addBatch\" method for a select.");
		}

		this.batchBindedObjects.offer(this.bindedObjects);

		this.bindedObjects = new ArrayList<>();
		this.bindedStrings.clear();
	}

	//FIXME: javadoc
	public int[] executeBatch() throws DaoException {
		if (this.select) {
			throw new DaoException("Impossible to call \"executeBatch\" method for a select.");
		}

		int[] r_t_iUpdates = new int[this.batchBindedObjects.size()];

		int iIndex = 0;
		for (List<Object> listBindedObjects : this.batchBindedObjects) {
			this.bindedObjects = listBindedObjects;
			r_t_iUpdates[iIndex] = this.executeUpdate();
		}
		return r_t_iUpdates;
	}

	//FIXME: javadoc
	public void clearBatch() throws DaoException {
		this.batchBindedObjects.clear();
	}

	//FIXME: Javadoc
	public void setDate(int p_iParameterIndex, Date p_oValue, Calendar p_oCal) throws DaoException {
		this.setDate(p_iParameterIndex, p_oValue);
	}

	//FIXME: Javadoc
	public void setTime(int p_iParameterIndex, Time p_oValue, Calendar p_oCal) throws DaoException {
		this.setTime(p_iParameterIndex, p_oValue);
	}

	//FIXME: Javadoc
	public void setTimestamp(int p_iParameterIndex, Timestamp p_oValue, Calendar p_oCal) throws DaoException {
		this.setTimestamp(p_iParameterIndex, p_oValue);
	}

	//FIXME: Javadoc
	public void setNull(int p_iParameterIndex, int p_oSqlType, String p_oTypeName) throws DaoException {
		this.setBindedValue(p_iParameterIndex, null, null);
	}

	//FIXME: Javadoc
	public void setURL(int p_iParameterIndex, URL p_oValue) throws DaoException {
		if (p_oValue == null) {
			this.setNull(p_iParameterIndex, Types.VARCHAR);
		}
		else {
			this.setString(p_iParameterIndex, p_oValue.toString());
		}
	}

	/**
	 * TODO Décrire la méthode setBindedValue de la classe AndroidSQLitePreparedStatement
	 * @param p_iParameterIndex index of the parameter
	 * @param p_oValue object to bind
	 * @param p_sValue string to bind
	 */
	private void setBindedValue(int p_iParameterIndex, Object p_oValue, String p_sValue) {
		while (this.bindedObjects.size() < p_iParameterIndex) {
			this.bindedObjects.add(null);
			this.bindedStrings.add(null);
		}

		int iAndroidIndex = p_iParameterIndex - 1;

		this.bindedObjects.set(iAndroidIndex, p_oValue);
		this.bindedStrings.set(iAndroidIndex, p_sValue);
	}
	
	
	public boolean isSelect() {
		return this.select;
	}
}
