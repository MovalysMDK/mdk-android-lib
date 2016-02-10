/**
	 * to overwrite
 * 
 */
package com.adeuza.movalysfwk.mobile.mf4android.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Struct;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * <p>Implements "Connection" of JDBC API .</p>
 *
 * <p>Copyright (c) 2014</p>
 * <p>Company: Sopra</p>
 *
 */

public abstract class AbstractConnection implements Connection {

	/**
	 * to overwrite
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 * @param <T> Interface to implement
     * @param p_oIface A Class defining an interface that the result must implement.
     * @return an object that implements the interface. May be a proxy for the actual implementing object.
     * @throws java.sql.SQLException If no object found that implements the interface
	 */
	@Override
	public <T> T unwrap(Class<T> p_oIface) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
     * @param p_oIface a Class defining an interface.
     * @return true if this implements the interface or directly or indirectly wraps an object that does.
     * @throws java.sql.SQLException  if an error occurs while determining whether this is a wrapper
     * for an object with the given interface.
	 */
	@Override
	public boolean isWrapperFor(Class<?> p_oIface) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#prepareCall(java.lang.String)
     * @param p_sSql an SQL statement that may contain one or more '?'
     * parameter placeholders
     * @return the native form of this statement
     * @throws SQLException if a database access error occurs
     * or this method is called on a closed connection
	 */
	@Override
	public CallableStatement prepareCall(String p_sSql) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#nativeSQL(java.lang.String)
     * @param p_sSql an SQL statement that may contain one or more '?'
     * parameter placeholders
     * @return the native form of this statement
     * @throws SQLException if a database access error occurs
     * or this method is called on a closed connection
	 */
	@Override
	public String nativeSQL(String p_sSql) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}



	/**
	 * to overwrite
	 * @see java.sql.Connection#getMetaData()
     * @return a <code>DatabaseMetaData</code> object for this
     *         <code>Connection</code> object
     * @throws  SQLException if a database access error occurs
     * or this method is called on a closed connection
	 */
	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}


	/**
	 * to overwrite
	 * @see java.sql.Connection#setCatalog(java.lang.String)
     * @param p_sCatalog the name of a catalog (subspace in this
     *        <code>Connection</code> object's database) in which to work
     * @throws SQLException if a database access error occurs
     * or this method is called on a closed connection
	 */
	@Override
	public void setCatalog(String p_sCatalog) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#getCatalog()
     * @return the current catalog name or <code>null</code> if there is none
     * @throws SQLException if a database access error occurs
     * or this method is called on a closed connection
	 */
	@Override
	public String getCatalog() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#setTransactionIsolation(int)
     * @param p_iLevel one of the following <code>Connection</code> constants:
     *        <code>Connection.TRANSACTION_READ_UNCOMMITTED</code>,
     *        <code>Connection.TRANSACTION_READ_COMMITTED</code>,
     *        <code>Connection.TRANSACTION_REPEATABLE_READ</code>, or
     *        <code>Connection.TRANSACTION_SERIALIZABLE</code>.
     *        (Note that <code>Connection.TRANSACTION_NONE</code> cannot be used
     *        because it specifies that transactions are not supported.)
     * @throws SQLException if a database access error occurs, this
     * method is called on a closed connection
     *            or the given parameter is not one of the <code>Connection</code>
     *            constants
	 */
	@Override
	public void setTransactionIsolation(int p_iLevel) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#getTransactionIsolation()
     * @return the current transaction isolation level, which will be one
     *         of the following constants:
     *        <code>Connection.TRANSACTION_READ_UNCOMMITTED</code>,
     *        <code>Connection.TRANSACTION_READ_COMMITTED</code>,
     *        <code>Connection.TRANSACTION_REPEATABLE_READ</code>,
     *        <code>Connection.TRANSACTION_SERIALIZABLE</code>, or
     *        <code>Connection.TRANSACTION_NONE</code>.
     * @throws SQLException if a database access error occurs
     * or this method is called on a closed connection
	 */
	@Override
	public int getTransactionIsolation() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#getWarnings()
     * @return the first <code>SQLWarning</code> object or <code>null</code>
     *         if there are none
     * @throws SQLException if a database access error occurs or
     *            this method is called on a closed connection
	 */
	@Override
	public SQLWarning getWarnings() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#clearWarnings()
     * @throws SQLException SQLException if a database access error occurs
     * or this method is called on a closed connection
	 */
	@Override
	public void clearWarnings() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}



	/**
	 * to overwrite
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
     * @param p_sSql a <code>String</code> object that is the SQL statement to
     *            be sent to the database; may contain on or more '?' parameters
     * @param p_iResultSetType a result set type; one of
     *         <code>ResultSet.TYPE_FORWARD_ONLY</code>,
     *         <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
     *         <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @param p_iResultSetConcurrency a concurrency type; one of
     *         <code>ResultSet.CONCUR_READ_ONLY</code> or
     *         <code>ResultSet.CONCUR_UPDATABLE</code>
     * @return a new <code>CallableStatement</code> object containing the
     * pre-compiled SQL statement that will produce <code>ResultSet</code>
     * objects with the given type and concurrency
     * @throws SQLException if a database access error occurs, this method
     * is called on a closed connection
     *         or the given parameters are not <code>ResultSet</code>
     *         constants indicating type and concurrency
     
     * this method or this method is not supported for the specified result
     * set type and result set concurrency.
	 */
	@Override
	public CallableStatement prepareCall(String p_sSql, int p_iResultSetType,
			int p_iResultSetConcurrency) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#getTypeMap()
     * @return the <code>java.util.Map</code> object associated
     *         with this <code>Connection</code> object
     * @throws SQLException if a database access error occurs
     * or this method is called on a closed connection
     
     * this method
	 */
	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#setTypeMap(java.util.Map)
     * @param p_oMap the <code>java.util.Map</code> object to install
     *        as the replacement for this <code>Connection</code>
     *        object's default type map
     * @throws SQLException if a database access error occurs, this
     * method is called on a closed connection or
     *        the given parameter is not a <code>java.util.Map</code>
     *        object
     
     * this method
	 */
	@Override
	public void setTypeMap(Map<String, Class<?>> p_oMap) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#setHoldability(int)
     * @param p_iHoldability a <code>ResultSet</code> holdability constant; one of
     *        <code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
     *        <code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
     * @throws SQLException if a database access occurs, this method is called
     * on a closed connection, or the given parameter
     *         is not a <code>ResultSet</code> constant indicating holdability
	 */
	@Override
	public void setHoldability(int p_iHoldability) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#getHoldability()
     * @return the holdability, one of
     *        <code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
     *        <code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
     * @throws SQLException if a database access error occurs
     * or this method is called on a closed connection
	 */
	@Override
	public int getHoldability() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#setSavepoint()
     * @return the new <code>Savepoint</code> object
     * @throws SQLException if a database access error occurs,
     * this method is called while participating in a distributed transaction,
     * this method is called on a closed connection
     *            or this <code>Connection</code> object is currently in
     *            auto-commit mode
     
     * this method
	 */
	@Override
	public Savepoint setSavepoint() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#setSavepoint(java.lang.String)
     * @param p_sName a <code>String</code> containing the name of the savepoint
     * @return the new <code>Savepoint</code> object
     * @throws SQLException if a database access error occurs,
          * this method is called while participating in a distributed transaction,
     * this method is called on a closed connection
     *            or this <code>Connection</code> object is currently in
     *            auto-commit mode
     
     * this method
	 */
	@Override
	public Savepoint setSavepoint(String p_sName) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}


	/**
	 * to overwrite
	 * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
     * @param p_oSavepoint the <code>Savepoint</code> object to be removed
     * @throws SQLException if a database access error occurs, this
     *  method is called on a closed connection or
     *            the given <code>Savepoint</code> object is not a valid
     *            savepoint in the current transaction
     
     * this method
	 */
	@Override
	public void releaseSavepoint(Savepoint p_oSavepoint) throws SQLException {
		throw new SQLFeatureNotSupportedException();

	}





	/**
	 * to overwrite
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
     * @param p_sSql a <code>String</code> object that is the SQL statement to
     *            be sent to the database; may contain on or more '?' parameters
     * @param p_iResultSetType one of the following <code>ResultSet</code>
     *        constants:
     *         <code>ResultSet.TYPE_FORWARD_ONLY</code>,
     *         <code>ResultSet.TYPE_SCROLL_INSENSITIVE</code>, or
     *         <code>ResultSet.TYPE_SCROLL_SENSITIVE</code>
     * @param p_iResultSetConcurrency one of the following <code>ResultSet</code>
     *        constants:
     *         <code>ResultSet.CONCUR_READ_ONLY</code> or
     *         <code>ResultSet.CONCUR_UPDATABLE</code>
     * @param p_iResultSetHoldability one of the following <code>ResultSet</code>
     *        constants:
     *         <code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
     *         <code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
     * @return a new <code>CallableStatement</code> object, containing the
     *         pre-compiled SQL statement, that will generate
     *         <code>ResultSet</code> objects with the given type,
     *         concurrency, and holdability
     * @throws SQLException if a database access error occurs, this
     * method is called on a closed connection
     *            or the given parameters are not <code>ResultSet</code>
     *            constants indicating type, concurrency, and holdability
      
     * this method or this method is not supported for the specified result
     * set type, result set holdability and result set concurrency.
	 * 
	 */
	@Override
	public CallableStatement prepareCall(String p_sSql, int p_iResultSetType,
			int p_iResultSetConcurrency, int p_iResultSetHoldability)
			throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int)
     * @param p_sSql an SQL statement that may contain one or more '?' IN
     *        parameter placeholders
     * @param p_iAutoGeneratedKeys a flag indicating whether auto-generated keys
     *        should be returned; one of
     *        <code>Statement.RETURN_GENERATED_KEYS</code> or
     *        <code>Statement.NO_GENERATED_KEYS</code>
     * @return a new <code>PreparedStatement</code> object, containing the
     *         pre-compiled SQL statement, that will have the capability of
     *         returning auto-generated keys
     * @throws SQLException if a database access error occurs, this
     *  method is called on a closed connection
     *         or the given parameter is not a <code>Statement</code>
     *         constant indicating whether auto-generated keys should be
     *         returned
     
     * this method with a constant of Statement.RETURN_GENERATED_KEYS
	 */
	@Override
	public PreparedStatement prepareStatement(String p_sSql, int p_iAutoGeneratedKeys)
			throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
     * @param p_sSql an SQL statement that may contain one or more '?' IN
     *        parameter placeholders
     * @param p_iColumnIndexes an array of column indexes indicating the columns
     *        that should be returned from the inserted row or rows
     * @return a new <code>PreparedStatement</code> object, containing the
     *         pre-compiled statement, that is capable of returning the
     *         auto-generated keys designated by the given array of column
     *         indexes
     * @throws SQLException if a database access error occurs
     * or this method is called on a closed connection
	 */
	@Override
	public PreparedStatement prepareStatement(String p_sSql, int[] p_iColumnIndexes)
			throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
     * @param p_sSql an SQL statement that may contain one or more '?' IN
     *        parameter placeholders
     * @param p_sColumnNames an array of column names indicating the columns
     *        that should be returned from the inserted row or rows
     * @return a new <code>PreparedStatement</code> object, containing the
     *         pre-compiled statement, that is capable of returning the
     *         auto-generated keys designated by the given array of column
     *         names
     * @throws SQLException if a database access error occurs
     * or this method is called on a closed connection
     
     * this method
	 * 
	 */
	@Override
	public PreparedStatement prepareStatement(String p_sSql, String[] p_sColumnNames)
			throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#createClob()
     * @return An object that implements the <code>Clob</code> interface
     * @throws SQLException if an object that implements the
     * <code>Clob</code> interface can not be constructed, this method is
     * called on a closed connection or a database access error occurs.
     
     * this data type
	 */
	@Override
	public Clob createClob() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#createBlob()
     * @return  An object that implements the <code>Blob</code> interface
     * @throws SQLException if an object that implements the
     * <code>Blob</code> interface can not be constructed, this method is
     * called on a closed connection or a database access error occurs.
     
     * this data type
	 */
	@Override
	public Blob createBlob() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#createNClob()
     * @return An object that implements the <code>NClob</code> interface
     * @throws SQLException if an object that implements the
     * <code>NClob</code> interface can not be constructed, this method is
     * called on a closed connection or a database access error occurs.
     
     * this data type
	 */
	@Override
	public NClob createNClob() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#createSQLXML()
     * @return An object that implements the <code>SQLXML</code> interface
     * @throws SQLException if an object that implements the <code>SQLXML</code> interface can not
     * be constructed, this method is
     * called on a closed connection or a database access error occurs.
     
     * this data type
	 */
	@Override
	public SQLXML createSQLXML() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#isValid(int)
         * @param p_iTimeout -             The time in seconds to wait for the database operation
         *                                              used to validate the connection to complete.  If
         *                                              the timeout period expires before the operation
         *                                              completes, this method returns false.  A value of
         *                                              0 indicates a timeout is not applied to the
         *                                              database operation.
         * <p>
         * @return true if the connection is valid, false otherwise
         * @throws SQLException if the value supplied for <code>timeout</code>
         * is less then 0
	 */
	@Override
	public boolean isValid(int p_iTimeout) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}



	/**
	 * to overwrite
	 * @see java.sql.Connection#createArrayOf(java.lang.String, java.lang.Object[])
  * @param p_sTypeName the SQL name of the type the elements of the array map to. The typeName is a
  * database-specific name which may be the name of a built-in type, a user-defined type or a standard  SQL type supported by this database. This
  *  is the value returned by <code>Array.getBaseTypeName</code>
  * @param p_oElements the elements that populate the returned object
  * @return an Array object whose elements map to the specified SQL type
  * @throws SQLException if a database error occurs, the JDBC type is not
  *  appropriate for the typeName and the conversion is not supported, the typeName is null or this method is called on a closed connection
	 */
	@Override
	public Array createArrayOf(String p_sTypeName, Object[] p_oElements)
			throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#createStruct(java.lang.String, java.lang.Object[])
  * @param p_sTypeName the SQL type name of the SQL structured type that this <code>Struct</code>
  * object maps to. The typeName is the name of  a user-defined type that
  * has been defined for this database. It is the value returned by
  * <code>Struct.getSQLTypeName</code>.

  * @param p_oAttributes the attributes that populate the returned object
  *  @return a Struct object that maps to the given SQL type and is populated with the given attributes
  * @throws SQLException if a database error occurs, the typeName is null or this method is called on a closed connection
	 */
	@Override
	public Struct createStruct(String p_sTypeName, Object[] p_oAttributes)
			throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#setSchema(java.lang.String)
    * @param p_sSchema the name of a schema  in which to work
    * @throws SQLException if a database access error occurs
    * or this method is called on a closed connection
	 */
	//@Override
	public void setSchema(String p_sSchema) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#getSchema()
     * @return the current schema name or <code>null</code> if there is none
     * @throws SQLException if a database access error occurs
     * or this method is called on a closed connection
	 */
	//@Override
	public String getSchema() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#abort(java.util.concurrent.Executor)
     * @param p_oExecutor  The <code>Executor</code>  implementation which will
     * be used by <code>abort</code>.
     * @throws java.sql.SQLException if a database access error occurs or
     * the {@code executor} is {@code null},
	 */
	//@Override
	public void abort(Executor p_oExecutor) throws SQLException {
		throw new SQLFeatureNotSupportedException();

	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#setNetworkTimeout(java.util.concurrent.Executor, int)
     * @param p_oExecutor  The <code>Executor</code>  implementation which will
     * be used by <code>setNetworkTimeout</code>.
     * @param p_iMilliseconds The time in milliseconds to wait for the database
     * operation
     *  to complete.  If the JDBC driver does not support milliseconds, the
     * JDBC driver will round the value up to the nearest second.  If the
     * timeout period expires before the operation
     * completes, a SQLException will be thrown.
     * A value of 0 indicates that there is not timeout for database operations.
     * @throws java.sql.SQLException if a database access error occurs, this
     * method is called on a closed connection,
     * the {@code executor} is {@code null},
     * or the value specified for <code>seconds</code> is less than 0.
	 */
	//@Override
	public void setNetworkTimeout(Executor p_oExecutor, int p_iMilliseconds)
			throws SQLException {
		throw new SQLFeatureNotSupportedException();

	}

	/**
	 * to overwrite
	 * @see java.sql.Connection#getNetworkTimeout()
     * @return the current timeout limit in milliseconds; zero means there is
     *         no limit
     * @throws SQLException if a database access error occurs or
     * this method is called on a closed <code>Connection</code>
	 */
	//@Override
	public int getNetworkTimeout() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

}
