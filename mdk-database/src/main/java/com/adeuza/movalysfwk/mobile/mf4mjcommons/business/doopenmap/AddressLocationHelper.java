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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doopenmap;

import com.adeuza.movalysfwk.mobile.mf4android.database.sqlite.MDKSQLiteStatement;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ResultSetReader;

/**
 * <p>AddressLocationHelper class.</p>
 */
public class AddressLocationHelper extends BaseAddressLocationHelper {

	/**
	 * <p>bindToStatement.</p>
	 *
	 * @param p_oLocation a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doopenmap.AddressLocation} object.
	 * @param p_oStatement statement.
	 * @throws DaoException if any.
	 */
	public static void bindToStatement( AddressLocation p_oLocation, MDKSQLiteStatement p_oStatement ) throws DaoException {
		if ( p_oLocation != null ) {
			p_oStatement.bindDouble(p_oLocation.getLatitude());
			p_oStatement.bindDouble(p_oLocation.getLongitude());
			p_oStatement.bindString(p_oLocation.getCompl());
			p_oStatement.bindString(p_oLocation.getStreet());
			p_oStatement.bindString(p_oLocation.getCity());
			p_oStatement.bindString(p_oLocation.getCountry());
		}
		else {
			p_oStatement.bindNull();
			p_oStatement.bindNull();
			p_oStatement.bindNull();
			p_oStatement.bindNull();
			p_oStatement.bindNull();
			p_oStatement.bindNull();
		}
	}
	
	/**
	 * <p>readResultSet.</p>
	 *
	 * @param p_oReader a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ResultSetReader} object.
	 * @throws DaoException if any.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doopenmap.AddressLocation} object.
	 */
	public static AddressLocation readResultSet( ResultSetReader p_oReader) throws DaoException {
		AddressLocation r_oAddressLocation = null ;
		Double dLatitude = p_oReader.getDoubleObject();
		Double dLongitude = p_oReader.getDoubleObject();
		String sCompl = p_oReader.getString();
		String sStreet = p_oReader.getString();
		String sCity = p_oReader.getString();
		String sCountry = p_oReader.getString();
		
		if ( dLatitude != null || dLongitude != null ||
			sCompl != null || sStreet != null || sCity != null || sCountry != null ) {
			r_oAddressLocation = new AddressLocation(dLatitude, dLongitude, sCompl, sStreet, sCity, sCountry );
		}
		return r_oAddressLocation;
	}
}
