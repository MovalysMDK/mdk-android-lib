package com.adeuza.movalysfwk.mobile.mf4android.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import android.database.Cursor;

/**
 * <p>TODO Décrire la classe AndroidSQLiteResultSetMetaData</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author emalespine
 *
 */

public class AndroidSQLiteResultSetMetaData implements ResultSetMetaData {
	/**
	 * Native android cursor.
	 */
	private Cursor androidCursor;

	/**
	 * constructor
	 * @param p_oNativeCursor native cursor
	 */
	protected AndroidSQLiteResultSetMetaData(Cursor p_oNativeCursor) {
		this.androidCursor = p_oNativeCursor;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(Class<T> p_oIface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(Class<?> p_oIface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnCount()
	 */
	@Override
	public int getColumnCount() throws SQLException {
		return this.androidCursor.getColumnCount();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isAutoIncrement(int)
	 */
	@Override
	public boolean isAutoIncrement(int p_oColumn) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isCaseSensitive(int)
	 */
	@Override
	public boolean isCaseSensitive(int p_oColumn) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isSearchable(int)
	 */
	@Override
	public boolean isSearchable(int p_oColumn) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isCurrency(int)
	 */
	@Override
	public boolean isCurrency(int p_oColumn) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isNullable(int)
	 */
	@Override
	public int isNullable(int p_oColumn) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isSigned(int)
	 */
	@Override
	public boolean isSigned(int p_oColumn) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnDisplaySize(int)
	 */
	@Override
	public int getColumnDisplaySize(int p_oColumn) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnLabel(int)
	 */
	@Override
	public String getColumnLabel(int p_iColumn) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnName(int)
	 */
	@Override
	public String getColumnName(int p_iColumn) throws SQLException {
		return this.androidCursor.getColumnName(p_iColumn - 1);
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getSchemaName(int)
	 */
	@Override
	public String getSchemaName(int p_oColumn) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getPrecision(int)
	 */
	@Override
	public int getPrecision(int p_oColumn) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getScale(int)
	 */
	@Override
	public int getScale(int p_oColumn) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getTableName(int)
	 */
	@Override
	public String getTableName(int p_oColumn) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getCatalogName(int)
	 */
	@Override
	public String getCatalogName(int p_oColumn) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnType(int)
	 */
	@Override
	public int getColumnType(int p_iColumn) throws SQLException {
		throw new UnsupportedOperationException();
//		int oldPos = this.androidCursor.getPosition();
//		boolean moved = false;
//		if (this.androidCursor.isBeforeFirst() || this.androidCursor.isAfterLast()) {
//			boolean resultSetEmpty = this.androidCursor.getCount() == 0 || this.androidCursor.isAfterLast();
//			if (resultSetEmpty) {
//				return Types.NULL;
//			}
//			this.androidCursor.moveToFirst(); 
//			moved = true;
//		}
//
//		int iAndroidType	= this.androidCursor.getType(p_iColumn - 1);
//		int iJDBCType		= Types.NULL;
//		switch (iAndroidType) {
//			case Cursor.FIELD_TYPE_NULL:
//			iJDBCType = Types.NULL;
//			break;
//		case Cursor.FIELD_TYPE_INTEGER:
//			iJDBCType = Types.INTEGER;
//			break;
//		case Cursor.FIELD_TYPE_FLOAT:
//			iJDBCType = Types.FLOAT;
//			break;
//		case Cursor.FIELD_TYPE_STRING:
//			iJDBCType = Types.VARCHAR;
//			break;
//		case Cursor.FIELD_TYPE_BLOB:
//			iJDBCType = Types.BLOB;
//			break;
//		default:
//			iJDBCType = Types.NULL;
//			break;
//		}
//		if (moved) {
//			this.androidCursor.moveToPosition(oldPos);
//		}
//		return iJDBCType;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnTypeName(int)
	 */
	@Override
	public String getColumnTypeName(int p_oColumn) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isReadOnly(int)
	 */
	@Override
	public boolean isReadOnly(int p_oColumn) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isWritable(int)
	 */
	@Override
	public boolean isWritable(int p_oColumn) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#isDefinitelyWritable(int)
	 */
	@Override
	public boolean isDefinitelyWritable(int p_oColumn) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.ResultSetMetaData#getColumnClassName(int)
	 */
	@Override
	public String getColumnClassName(int p_oColumn) throws SQLException {
		throw new UnsupportedOperationException();
	}
}
