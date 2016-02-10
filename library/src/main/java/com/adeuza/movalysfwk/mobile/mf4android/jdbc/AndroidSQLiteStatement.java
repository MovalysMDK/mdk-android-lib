package com.adeuza.movalysfwk.mobile.mf4android.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AndroidSQLiteStatement
 *
 */
public class AndroidSQLiteStatement implements Statement {

	/**
	 * Pattern used to detect insert.
	 */
	private static final Pattern INSERT_REGEX = Pattern.compile("^\\s*INSERT\\s+INTO\\s+([a-zA-Z0-9_]+)\\s+.*$", Pattern.CASE_INSENSITIVE);

	/**
	 * Pattern used to deted select.
	 */
	private static final Pattern SELECT_REGEX = Pattern.compile("^\\s*SELECT\\s+.*$", Pattern.CASE_INSENSITIVE);
	
	/**
	 * The connection.
	 */
	private AndroidSQLiteConnection connection;

	/**
	 * The sql instruction.
	 */
	protected String sql;

	/**
	 * The ResultSet.
	 */
	private AndroidSQLiteResultSet resultSet;

	/**
	 * return the table where insert is performed
	 * @param p_sQuery the query
	 * @return the table name
	 */
	protected static String getTableWherePerformInsert(String p_sQuery) {
		String r_sTableName = null;
		if (p_sQuery != null) {
			Matcher oMatch = INSERT_REGEX.matcher(p_sQuery);
			if (oMatch.find()) {
				r_sTableName = oMatch.group(1);
			}
		}
		return r_sTableName;
	}

	/**
	 * return true if the query is a insert query
	 * @param p_sQuery the query to test
	 * @return true if the is a insert query, false otherwise
	 */
	protected static boolean isInsert(String p_sQuery) {
		return INSERT_REGEX.matcher(p_sQuery).find();
	}

	/**
	 * Return <em>true</em> when <em>p_sSql</em> represents a "select" sql instruction.
	 * @param p_sSql A SQL instruction
	 * @return <em>true</em> when <em>p_sSql</em> represents a "select" sql instruction, false otherwise.
	 */
	protected static boolean isSelect(String p_sSql) {
		return p_sSql != null && SELECT_REGEX.matcher(p_sSql).find();
	}
	
	/**
	 * construcor
	 * @param p_oConnection JDBC connection
	 */
	public AndroidSQLiteStatement(AndroidSQLiteConnection p_oConnection) {
		this.connection		= p_oConnection;
		this.resultSet		= null;
		this.sql			= null;
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
	 * @see java.sql.Statement#executeQuery(java.lang.String)
	 */
	@Override
	public ResultSet executeQuery(String p_sSql) throws SQLException {
		if (this.resultSet != null && !this.resultSet.isClosed()) {
			throw new SQLException("Previous ResultSet is not closed.");
		}
		this.sql		= p_sSql;
		this.resultSet	= new AndroidSQLiteResultSet(this, this.connection.getAndroidDatabase().rawQuery(p_sSql, null));

		return this.resultSet;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#executeUpdate(java.lang.String)
	 */
	@Override
	public int executeUpdate(String p_sSql) throws SQLException {
		this.sql = p_sSql;
		this.connection.getAndroidDatabase().execSQL(p_sSql);
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#close()
	 */
	@Override
	public void close() throws SQLException {
		if (this.resultSet != null) {
			if (this.resultSet.isClosed()) {
				this.resultSet = null;
			}
			else {
				throw new SQLException("Impossible to close a Statement if a Resulset is not closed.");
			}
		}
		
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#getMaxFieldSize()
	 */
	@Override
	public int getMaxFieldSize() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#setMaxFieldSize(int)
	 */
	@Override
	public void setMaxFieldSize(int p_oMax) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#getMaxRows()
	 */
	@Override
	public int getMaxRows() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#setMaxRows(int)
	 */
	@Override
	public void setMaxRows(int p_oMax) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#setEscapeProcessing(boolean)
	 */
	@Override
	public void setEscapeProcessing(boolean p_oEnable) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#getQueryTimeout()
	 */
	@Override
	public int getQueryTimeout() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#setQueryTimeout(int)
	 */
	@Override
	public void setQueryTimeout(int p_oSeconds) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#cancel()
	 */
	@Override
	public void cancel() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#getWarnings()
	 */
	@Override
	public SQLWarning getWarnings() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#clearWarnings()
	 */
	@Override
	public void clearWarnings() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#setCursorName(java.lang.String)
	 */
	@Override
	public void setCursorName(String p_oName) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#execute(java.lang.String)
	 */
	@Override
	public boolean execute(String p_sSql) throws SQLException {
		this.connection.getAndroidDatabase().execSQL(p_sSql);
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#getResultSet()
	 */
	@Override
	public ResultSet getResultSet() throws SQLException {
		return this.resultSet;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#getUpdateCount()
	 */
	@Override
	public int getUpdateCount() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#getMoreResults()
	 */
	@Override
	public boolean getMoreResults() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#setFetchDirection(int)
	 */
	@Override
	public void setFetchDirection(int p_oDirection) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#getFetchDirection()
	 */
	@Override
	public int getFetchDirection() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#setFetchSize(int)
	 */
	@Override
	public void setFetchSize(int p_oRows) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#getFetchSize()
	 */
	@Override
	public int getFetchSize() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#getResultSetConcurrency()
	 */
	@Override
	public int getResultSetConcurrency() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#getResultSetType()
	 */
	@Override
	public int getResultSetType() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#addBatch(java.lang.String)
	 */
	@Override
	public void addBatch(String p_oSql) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#clearBatch()
	 */
	@Override
	public void clearBatch() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#executeBatch()
	 */
	@Override
	public int[] executeBatch() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#getConnection()
	 */
	@Override
	public AndroidSQLiteConnection getConnection() throws SQLException {
		return this.connection;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#getMoreResults(int)
	 */
	@Override
	public boolean getMoreResults(int p_oCurrent) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#getGeneratedKeys()
	 */
	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		ResultSet result;
		Statement oStatement = this.connection.createStatement();
		try {
			result = oStatement.executeQuery("select last_insert_rowid()");
		} 
		finally {
			oStatement.close();
		}
		
		return result;
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int)
	 */
	@Override
	public int executeUpdate(String p_oSql, int p_oAutoGeneratedKeys) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
	 */
	@Override
	public int executeUpdate(String p_oSql, int[] p_oColumnIndexes) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#executeUpdate(java.lang.String, java.lang.String[])
	 */
	@Override
	public int executeUpdate(String p_oSql, String[] p_oColumnNames) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#execute(java.lang.String, int)
	 */
	@Override
	public boolean execute(String p_oSql, int p_oAutoGeneratedKeys) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#execute(java.lang.String, int[])
	 */
	@Override
	public boolean execute(String p_oSql, int[] p_oColumnIndexes) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean execute(String p_oSql, String[] p_oColumnNames) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#getResultSetHoldability()
	 */
	@Override
	public int getResultSetHoldability() throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#isClosed()
	 */
	@Override
	public boolean isClosed() throws SQLException {
		return this.resultSet == null || this.resultSet.isClosed();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#setPoolable(boolean)
	 */
	@Override
	public void setPoolable(boolean p_oPoolable) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#isPoolable()
	 */
	@Override
	public boolean isPoolable() throws SQLException {
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#closeOnCompletion()
	 */
	//@Override
	public void closeOnCompletion() throws SQLException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.sql.Statement#isCloseOnCompletion()
	 */
	//@Override
	public boolean isCloseOnCompletion() throws SQLException {
		throw new UnsupportedOperationException();
	}
}
