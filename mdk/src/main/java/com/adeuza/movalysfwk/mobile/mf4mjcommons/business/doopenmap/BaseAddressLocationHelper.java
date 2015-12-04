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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.AddressLocationSVMImpl;

/**
 * <p>AddressLocationHelper class.</p>
 */
public class BaseAddressLocationHelper {
	
	/**
	 * <p>toComponent.</p>
	 *
	 * @param p_oAddressLocImpl a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.AddressLocationSVMImpl} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doopenmap.AddressLocation} object.
	 */
	public static AddressLocation toComponent( AddressLocationSVMImpl p_oAddressLocImpl ) {
		AddressLocation r_oAddressLocation = null ;
		if ( p_oAddressLocImpl != null && (p_oAddressLocImpl.getLatitude() != null || 
				p_oAddressLocImpl.getLongitude() != null || p_oAddressLocImpl.getCoutry() != null ||
				p_oAddressLocImpl.getCompl() != null || p_oAddressLocImpl.getStreet() != null ||
				p_oAddressLocImpl.getCity() != null )) {
			r_oAddressLocation = new AddressLocation();
			r_oAddressLocation.setLatitude(p_oAddressLocImpl.getLatitude());
			r_oAddressLocation.setLongitude(p_oAddressLocImpl.getLongitude());
			r_oAddressLocation.setCompl(p_oAddressLocImpl.getCompl());
			r_oAddressLocation.setCity(p_oAddressLocImpl.getCity());
			r_oAddressLocation.setStreet(p_oAddressLocImpl.getStreet());
			r_oAddressLocation.setCountry(p_oAddressLocImpl.getCoutry());
		}
		return r_oAddressLocation ;
	}
	
	/**
	 * <p>toViewModel.</p>
	 *
	 * @param p_oAddressLocImpl a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doopenmap.AddressLocation} object.
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.AddressLocationSVMImpl} object.
	 */
	public static AddressLocationSVMImpl toViewModel( AddressLocation p_oAddressLocImpl ) {
		AddressLocationSVMImpl r_oAddressLocation = new AddressLocationSVMImpl(0,0);
		if ( p_oAddressLocImpl != null ) {
			r_oAddressLocation.setGPSPosition(p_oAddressLocImpl.getLatitude(), p_oAddressLocImpl.getLongitude());
			r_oAddressLocation.setCompl(p_oAddressLocImpl.getCompl());
			r_oAddressLocation.setCity(p_oAddressLocImpl.getCity());
			r_oAddressLocation.setStreet(p_oAddressLocImpl.getStreet());
			r_oAddressLocation.setCoutry(p_oAddressLocImpl.getCountry());
		}
		return r_oAddressLocation ;
	}
}
