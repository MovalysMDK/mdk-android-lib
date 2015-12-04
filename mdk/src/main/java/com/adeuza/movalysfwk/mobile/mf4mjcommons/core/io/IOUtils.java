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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * <p>IOUtils</p>
 * Convert InputStream to String
 *
 */
public final class IOUtils {
	
	/** buffer size */
	private static final int BUFFER_SIZE = 4096;

	/** Util constructor */
	private IOUtils() {
	}
	
	/**
	 * Read String inside Input stream
	 *
	 * @param p_oInputStream a {@link java.io.InputStream} object.
	 * @return String value of input stream
	 * @throws java.io.IOException if any.
	 */
	public static String toString( InputStream p_oInputStream ) throws IOException {
		String r_sValue = null ;
		InputStreamReader oInputStreamReader = new InputStreamReader(p_oInputStream);
		try {
			StringWriter oStringWriter = new StringWriter();
			char[] buffer = new char[BUFFER_SIZE];
			int n = 0;
			while (-1 != (n = oInputStreamReader.read(buffer))) {
				oStringWriter.write(buffer, 0, n);
			}
			r_sValue = oStringWriter.toString();
		} finally {
			oInputStreamReader.close();
		} 
		return r_sValue ;
	}
}
