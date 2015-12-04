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

import java.sql.Date;
import java.sql.Timestamp;

import com.adeuza.movalysfwk.mobile.mf4android.jdbc.AndroidSQLitePreparedStatement;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.Enum;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.SqlType;

/**
 * <p>
 * Classe pour binder les parametres d'un prepareStatement.
 * </p>
 * <p>
 * Les parametres doivent être bindés dans l'ordre, StatementBinder gérant en interne la position.
 * </p>
 *
 */
public class StatementBinder {

	/**
	 * Position courante du binding
	 */
	private int position;
	/**
	 * PreparedStatement sur lequel il faut effectuer le binding
	 */
	private AndroidSQLitePreparedStatement statement;

	/**
	 * Instancie le StatementBinder avec un preparedStatement
	 *
	 * @param p_oStatement
	 *            prepareStatement sur lequel effectué le binding
	 */
	public StatementBinder(AndroidSQLitePreparedStatement p_oStatement) {
		this.position = 1;
		this.statement = p_oStatement;
	}

	/**
	 * Bind la valeur d'un parametre
	 *
	 * @param p_oValue
	 *            object value for binding
	 * @param p_iSqlType
	 *            type SQL
	 * @throws DaoException
	 *             echec du binding
	 */
	public void bind(Object p_oValue, @SqlType int p_iSqlType) throws DaoException {

		// Gestion du fait que p_oValue peut-être une énumération
		// Toutes les interfaces de Movalys devrait implémentée l'interface MIEnum
		// -> Récupération de la valeur du champ "baseId"
		Object oValue = p_oValue;
		if (p_oValue instanceof Enum) {
			Enum oEnum = (Enum) p_oValue;
			oValue = oEnum.getBaseId();
		}

		if (oValue != null) {
			this.statement.setObject(this.position, oValue, p_iSqlType);
		}
		else {
			this.statement.setNull(this.position, p_iSqlType);
		}
		this.position++;
	}

	/**
	 * Binding d'une chaine
	 *
	 * @param p_sValue
	 *            string value for binding
	 * @throws DaoException
	 *             binding failure
	 */
	public void bindString(String p_sValue) throws DaoException {
		this.statement.setString(this.position, p_sValue);
		this.position++;
	}

	/**
	 * Binding d'un char
	 *
	 * @param p_sValue
	 *            char value for binding
	 * @throws DaoException
	 *             binding failure
	 */
	public void bindChar(char p_sValue) throws DaoException {
		this.statement.setString(this.position, Character.toString(p_sValue));
		this.position++;
	}

	/**
	 * Binding d'un caractère
	 *
	 * @param p_oValue
	 *            Character value to bind
	 * @throws DaoException
	 *             binding failure
	 */
	public void bindChar(Character p_oValue) throws DaoException {
		bindChar(p_oValue.charValue());
	}

	/**
	 * Binding d'un long
	 *
	 * @param p_lValue
	 *            long value to bind
	 * @throws DaoException
	 *             binding failure
	 */
	public void bindLong(long p_lValue) throws DaoException {
		this.statement.setLong(this.position, p_lValue);
		this.position++;
	}
	
	/**
	 * Binding d'un Long
	 *
	 * @param p_lValue
	 *            long value to bind
	 * @throws DaoException
	 *             binding failure
	 */
	public void bindLong(Long p_lValue) throws DaoException {
		if (p_lValue == null) {
			this.bindNull(SqlType.INTEGER);
		}
		else {
			this.bindLong(p_lValue.longValue());
		}
	}

	/**
	 * Binding d'un int
	 *
	 * @param p_iValue
	 *            bind int value
	 * @throws DaoException
	 *             binding failure
	 */
	public void bindInt(int p_iValue) throws DaoException {
		this.statement.setLong(this.position, p_iValue);
		this.position++;
	}

	/**
	 * Binding d'un Integer
	 *
	 * @param p_iValue
	 *            bind int value
	 * @throws DaoException
	 *             binding failure
	 */
	public void bindInt(Integer p_iValue) throws DaoException {
		if (p_iValue == null) {
			this.bindNull(SqlType.INTEGER);
		}
		else {
			this.bindInt(p_iValue.intValue());
		}
	}

	/**
	 * Binding d'un timestamp
	 *
	 * @param p_oTimestamp
	 *            bind Timestamp value
	 * @throws DaoException
	 *             binding failure
	 */
	public void bindTimestamp(Timestamp p_oTimestamp) throws DaoException {
		this.statement.setTimestamp(this.position, p_oTimestamp);
		this.position++;
	}

	/**
	 * 
	 *
	 * @param p_oValue boolean value to bind
	 * @throws DaoException binding failure
	 */
	public void bindBoolean(Boolean p_oValue) throws DaoException {
		this.statement.setInt(this.position, p_oValue?1:0);
		this.position++;
	}

	/**
	 * Binding d'un double
	 *
	 * @throws DaoException
	 *             binding failure
	 * @param p_dDouble a double.
	 */
	public void bindDouble(double p_dDouble) throws DaoException {
		this.statement.setDouble(this.position, p_dDouble);
		this.position++;
	}

	/**
	 * Binding d'un Double
	 *
	 * @param p_oDouble
	 *            double value to bind
	 * @throws DaoException
	 *             binding failure
	 */
	public void bindDouble(Double p_oDouble) throws DaoException {
		if (p_oDouble == null) {
			this.bindNull(SqlType.DOUBLE);
		}
		else {
			this.bindDouble(p_oDouble.doubleValue());
		}
	}

	/**
	 * Binding d'un short
	 *
	 * @throws DaoException
	 *             binding failure
	 * @param p_iShort a short.
	 */
	public void bindShort(short p_iShort) throws DaoException {
		this.statement.setShort(this.position, p_iShort);
		this.position++;
	}

	/**
	 * Binding d'un Short
	 *
	 * @param p_oShort
	 *            Short value to bind
	 * @throws DaoException
	 *             binding failure
	 */
	public void bindShort(Short p_oShort) throws DaoException {
		if (p_oShort == null) {
			this.bindNull(SqlType.INTEGER);
		}
		else {
			this.bindShort(p_oShort.shortValue());
		}
	}

	/**
	 * Binding d'un byte
	 *
	 * @throws DaoException
	 *             binding failure
	 * @param p_bByte a byte.
	 */
	public void bindByte(byte p_bByte) throws DaoException {
		this.statement.setByte(this.position, p_bByte);
		this.position++;
	}

	/**
	 * Binding d'un byte
	 *
	 * @param p_oByte
	 *            byte value to bind
	 * @throws DaoException
	 *             binding failure
	 */
	public void bindByte(Byte p_oByte) throws DaoException {
		if (p_oByte == null) {
			this.bindNull(SqlType.CHAR);
		}
		else {
			this.bindByte(p_oByte.byteValue());
		}
	}

	/**
	 * Binding de la valeur null
	 *
	 * @param p_iSqlType
	 *            sql type
	 * @throws DaoException
	 *             binding failure
	 */
	public void bindNull(@SqlType int p_iSqlType) throws DaoException {
		this.statement.setNull(this.position, p_iSqlType);
		this.position++;
	}

	/**
	 * Binding d'un float
	 *
	 * @throws DaoException
	 *             binding failure
	 * @param p_fFloat a float.
	 */
	public void bindFloat(float p_fFloat) throws DaoException {
		this.statement.setFloat(this.position, p_fFloat);
		this.position++;
	}

	/**
	 * Binding d'un Float
	 *
	 * @param p_oFloat
	 *            float value to bind
	 * @throws DaoException
	 *             binding failure
	 */
	public void bindFloat(Float p_oFloat) throws DaoException {
		if (p_oFloat == null) {
			this.bindNull(SqlType.FLOAT);
		}
		else {
			this.bindFloat(p_oFloat.floatValue());
		}
	}
	
	/**
	 * Binding d'une date
	 *
	 * @param p_oDate
	 *            date value to bind
	 * @throws DaoException
	 *             binding failure
	 */
	public void bindDate(Date p_oDate) throws DaoException {
		this.statement.setDate(this.position, p_oDate);
		this.position++;
	}

	/**
	 * Binding d'un MIEnum
	 *
	 * @throws DaoException
	 *             binding failure
	 * @param p_oMEnum a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.Enum} object.
	 */
	public void bindMEnum(Enum p_oMEnum) throws DaoException {
		this.statement.setInt(this.position, p_oMEnum.getBaseId());
		this.position++;
	}

	/**
	 * Bind a clob
	 *
	 * @param p_sValue
	 *            clob value to bind
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             binding failure
	 * @throws DaoException
	 *             binding failure
	 */
	public void bindClob(String p_sValue) throws DaoException {
		this.statement.setString(this.position, p_sValue);
		this.position++;
	}

	/**
	 * Retourne la position courante du binding
	 *
	 * @return la position courante du binding
	 */
	public int getCurrentPosition() {
		return this.position;
	}
}
