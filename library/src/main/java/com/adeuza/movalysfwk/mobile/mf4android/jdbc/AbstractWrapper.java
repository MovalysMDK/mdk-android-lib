/**
 * 
 */
package com.adeuza.movalysfwk.mobile.mf4android.jdbc;

import java.sql.SQLException;
import java.sql.Wrapper;

/**
 * <p>Abstract wrapper.</p>
 *
 * <p>Copyright (c) 2014
 * <p>Company: Adeuza
 *
 * @author pedubreuil
 *
 */
public abstract class AbstractWrapper implements Wrapper {

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


}
