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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

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
	private PreparedStatement statement;

	/**
	 * Instancie le StatementBinder avec un preparedStatement
	 *
	 * @param p_oStatement
	 *            prepareStatement sur lequel effectué le binding
	 */
	public StatementBinder(PreparedStatement p_oStatement) {
		this.position = 1;
		this.statement = p_oStatement;
	}

	/**
	 * Bind la valeur d'un parametre
	 *
	 * @param p_oValue
	 *            object value for binding
	 * @param p_oSqlType
	 *            type SQL
	 * @throws java.sql.SQLException
	 *             echec du binding
	 */
	public void bind(Object p_oValue, SqlType p_oSqlType) throws SQLException {

		// Gestion du fait que p_oValue peut-être une énumération
		// Toutes les interfaces de Movalys devrait implémentée l'interface MIEnum
		// -> Récupération de la valeur du champ "baseId"
		Object oValue = p_oValue;
		if (p_oValue instanceof Enum) {
			Enum oEnum = (Enum) p_oValue;
			oValue = oEnum.getBaseId();
		}

		if (oValue != null) {
			this.statement.setObject(this.position, oValue, p_oSqlType.intValue());
		}
		else {
			this.statement.setNull(this.position, p_oSqlType.intValue());
		}
		this.position++;
	}

	/**
	 * Binding d'une chaine
	 *
	 * @param p_sValue
	 *            string value for binding
	 * @throws java.sql.SQLException
	 *             binding failure
	 */
	public void bindString(String p_sValue) throws SQLException {
		this.statement.setString(this.position, p_sValue);
		this.position++;
	}

	/**
	 * Binding d'un char
	 *
	 * @param p_sValue
	 *            char value for binding
	 * @throws java.sql.SQLException
	 *             binding failure
	 */
	public void bindChar(char p_sValue) throws SQLException {
		this.statement.setString(this.position, Character.toString(p_sValue));
		this.position++;
	}

	/**
	 * Binding d'un caractère
	 *
	 * @param p_oValue
	 *            Character value to bind
	 * @throws java.sql.SQLException
	 *             binding failure
	 */
	public void bindChar(Character p_oValue) throws SQLException {
		bindChar(p_oValue.charValue());
	}

	/**
	 * Binding d'un long
	 *
	 * @param p_lValue
	 *            long value to bind
	 * @throws java.sql.SQLException
	 *             binding failure
	 */
	public void bindLong(long p_lValue) throws SQLException {
		this.statement.setLong(this.position, p_lValue);
		this.position++;
	}
	
	/**
	 * Binding d'un Long
	 *
	 * @param p_lValue
	 *            long value to bind
	 * @throws java.sql.SQLException
	 *             binding failure
	 */
	public void bindLong(Long p_lValue) throws SQLException {
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
	 * @throws java.sql.SQLException
	 *             binding failure
	 */
	public void bindInt(int p_iValue) throws SQLException {
		this.statement.setLong(this.position, p_iValue);
		this.position++;
	}

	/**
	 * Binding d'un Integer
	 *
	 * @param p_iValue
	 *            bind int value
	 * @throws java.sql.SQLException
	 *             binding failure
	 */
	public void bindInt(Integer p_iValue) throws SQLException {
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
	 * @throws java.sql.SQLException
	 *             binding failure
	 */
	public void bindTimestamp(Timestamp p_oTimestamp) throws SQLException {
		this.statement.setTimestamp(this.position, p_oTimestamp);
		this.position++;
	}

	/**
	 * Binding d'un boolean
	 *
	 * @param p_oBoolean
	 *            boolean value to bind
	 * @throws java.sql.SQLException
	 *             binding failure
	 */
	public void bindBoolean(Boolean p_oBoolean) throws SQLException {
		this.statement.setBoolean(this.position, p_oBoolean);
		this.position++;
	}

	/**
	 * Binding d'un double
	 *
	 * @throws java.sql.SQLException
	 *             binding failure
	 * @param p_dDouble a double.
	 */
	public void bindDouble(double p_dDouble) throws SQLException {
		this.statement.setDouble(this.position, p_dDouble);
		this.position++;
	}

	/**
	 * Binding d'un Double
	 *
	 * @param p_oDouble
	 *            double value to bind
	 * @throws java.sql.SQLException
	 *             binding failure
	 */
	public void bindDouble(Double p_oDouble) throws SQLException {
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
	 * @throws java.sql.SQLException
	 *             binding failure
	 * @param p_iShort a short.
	 */
	public void bindShort(short p_iShort) throws SQLException {
		this.statement.setShort(this.position, p_iShort);
		this.position++;
	}

	/**
	 * Binding d'un Short
	 *
	 * @param p_oShort
	 *            Short value to bind
	 * @throws java.sql.SQLException
	 *             binding failure
	 */
	public void bindShort(Short p_oShort) throws SQLException {
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
	 * @throws java.sql.SQLException
	 *             binding failure
	 * @param p_bByte a byte.
	 */
	public void bindByte(byte p_bByte) throws SQLException {
		this.statement.setByte(this.position, p_bByte);
		this.position++;
	}

	/**
	 * Binding d'un byte
	 *
	 * @param p_oByte
	 *            byte value to bind
	 * @throws java.sql.SQLException
	 *             binding failure
	 */
	public void bindByte(Byte p_oByte) throws SQLException {
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
	 * @param p_oSqlType
	 *            sql type
	 * @throws java.sql.SQLException
	 *             binding failure
	 */
	public void bindNull(SqlType p_oSqlType) throws SQLException {
		this.statement.setNull(this.position, p_oSqlType.intValue());
		this.position++;
	}

	/**
	 * Binding d'un float
	 *
	 * @throws java.sql.SQLException
	 *             binding failure
	 * @param p_fFloat a float.
	 */
	public void bindFloat(float p_fFloat) throws SQLException {
		this.statement.setFloat(this.position, p_fFloat);
		this.position++;
	}

	/**
	 * Binding d'un Float
	 *
	 * @param p_oFloat
	 *            float value to bind
	 * @throws java.sql.SQLException
	 *             binding failure
	 */
	public void bindFloat(Float p_oFloat) throws SQLException {
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
	 * @throws java.sql.SQLException
	 *             binding failure
	 */
	public void bindDate(Date p_oDate) throws SQLException {
		this.statement.setDate(this.position, p_oDate);
		this.position++;
	}

	/**
	 * Binding d'un MIEnum
	 *
	 * @throws java.sql.SQLException
	 *             binding failure
	 * @param p_oMEnum a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.core.beans.Enum} object.
	 */
	public void bindMEnum(Enum p_oMEnum) throws SQLException {
		this.statement.setInt(this.position, p_oMEnum.getBaseId());
		this.position++;
	}

//	/**
//	 * Binding d'un I18nValue
//	 * 
//	 * @param p_oI18nValue
//	 *            I18nValue value to bind
//	 * @throws SQLException
//	 *             binding failure
//	 * @throws DaoException
//	 *             binding failure
//	 */
//	public void bindI18nValue(I18nValue p_oI18nValue) throws DaoException, SQLException {
//		// bind null if I118nValue is empty
//		if (p_oI18nValue.getNbLocales() == 0) {
//			for ( int iCpt = 0 ; iCpt < I18nLocaleHelper.getInstance().getNbLocales(); iCpt++ ) {
//				if (log.isTraceEnabled()) {
//					log.trace("binding parameter '{}', type='I18nValue', with value='null'", this.position );
//				}
//				this.statement.setNull(this.position++, SqlType.VARCHAR.intValue());
//			}
//		} else {
//			// if not empty, check all locales are filled
//			if (p_oI18nValue.getNbLocales() == I18nLocaleHelper.getInstance().getNbLocales()) {
//				for (I18nLocale oLocale : I18nLocaleHelper.getInstance().getEnabledLocales()) {
//					String sValue = p_oI18nValue.getValueForLocale(oLocale);
//					if (log.isTraceEnabled()) {
//						log.trace("binding parameter '{}', type='I18nValue', with value='{}'", this.position, sValue);
//					}
//					this.statement.setString(this.position++, sValue);
//				}
//			} else {
//				throw new DaoException("Values must be defined for all locales");
//			}
//		}
//	}

	/**
	 * Bind a clob
	 *
	 * @param p_sValue
	 *            clob value to bind
	 * @throws com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException
	 *             binding failure
	 * @throws java.sql.SQLException
	 *             binding failure
	 */
	public void bindClob(String p_sValue) throws DaoException, SQLException {
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
