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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model;

import java.io.Serializable;

/**
 * Value Object classe to manage address location across actions {@link #isAcurate()} allow you to know if this location contains Lat / Long and if
 * its different of {@link java.lang.Integer#MIN_VALUE}
 *
 */
public class AddressLocationSVMImpl implements Serializable {
	/** Acuracy of the location : true if lat/long are used and not Integer.MIN_VALUE */
	private boolean isAcurate = false;
	/** WGS84 Latitude */
	private Double latitude;
	/** WGS84 Longitude */
	private Double longitude;
	/** Address complement to use for ZA, Commercial center... */
	private String compl;
	/** street adress contain number in the street + street name */
	private String street;
	/** ZIP code + city name */
	private String city;
	/** country name */
	private String coutry;

	/**
	 * Constructor used by the bean loader.
	 */
	public AddressLocationSVMImpl() {
		super();
		this.isAcurate = false;
		this.latitude = null;
		this.longitude = null;
		this.compl = "";
		this.street = "";
		this.city = "";
		this.coutry = "";
	}

	/**
	 * use this constructor to create {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.AddressLocationSVMImpl} when WGS coordinates exists
	 *
	 * @param p_oLatitude
	 *            WGS84 GPS latitude
	 * @param p_oLongitude
	 *            WGS84 GPS Longitude
	 */
	public AddressLocationSVMImpl(double p_oLatitude, double p_oLongitude) {
		super();
		isAcurate = true;
		this.latitude = p_oLatitude;
		this.longitude = p_oLongitude;
		this.street = "";
		this.compl = "";
		this.city = "";
		this.coutry = "";
	}

	/**
	 * Use this constructor to create {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.AddressLocationSVMImpl} when WGS coordinates are not known
	 *
	 * @param p_oCompl
	 *            complementary information like ZI, Commercial center ...
	 * @param p_oStreet
	 *            Street name including number in the street, street type (ie str, av, rue, chemin...) and street name
	 * @param p_oCity
	 *            ZipCode + City name
	 * @param p_oCoutry
	 *            Country
	 */
	public AddressLocationSVMImpl(String p_oCompl, String p_oStreet, String p_oCity, String p_oCoutry) {
		super();
		isAcurate = false;
		this.latitude = null;
		this.longitude = null;
		this.compl = p_oCompl;
		this.street = p_oStreet;
		this.city = p_oCity;
		this.coutry = p_oCoutry;
	}

	/**
	 * Constructor using all fields
	 *
	 * @param p_oLatitude
	 *            WGS84 GPS latitude
	 * @param p_oLongitude
	 *            WGS84 GPS Longitude
	 * @param p_oCompl
	 *            complementary information like ZI, Commercial center ...
	 * @param p_oStreet
	 *            Street name including number in the street, street type (ie str, av, rue, chemin...) and street name
	 * @param p_oCity
	 *            ZIP Code + City name
	 * @param p_oCoutry
	 *            Country
	 */
	public AddressLocationSVMImpl(Double p_oLatitude, Double p_oLongitude, String p_oCompl, String p_oStreet, String p_oCity, String p_oCoutry) {
		super();
		if (p_oLatitude != Integer.MIN_VALUE && p_oLongitude != Integer.MIN_VALUE) {
			isAcurate = true;
		}
		this.latitude = p_oLatitude;
		this.longitude = p_oLongitude;
		this.compl = p_oCompl;
		this.street = p_oStreet;
		this.city = p_oCity;
		this.coutry = p_oCoutry;
	}

	/**
	 * Constructor using AddressLocationSVMImpl
	 *
	 * @param p_oAddress a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.AddressLocationSVMImpl} object.
	 */
	public AddressLocationSVMImpl(AddressLocationSVMImpl p_oAddress) {
		super();
		this.isAcurate = true;
		if(p_oAddress != null) {
			setGPSPosition(p_oAddress.latitude, p_oAddress.longitude);
			this.compl = p_oAddress.compl;
			this.street = p_oAddress.street;
			this.city = p_oAddress.city;
			this.coutry = p_oAddress.coutry;
		}
	}

	/**
	 *
	 * if this location contains Lat / Long and if its different of {@link java.lang.Integer#MIN_VALUE}, return true else return false
	 *
	 * @return boolean value
	 */
	public Boolean isAcurate() {
		return isAcurate;
	}

	/**
	 * Gets latitude
	 *
	 * @return Objet latitude
	 */
	public Double getLatitude() {
		return this.latitude;
	}

	/**
	 * Sets the GPS Position
	 *
	 * @param p_oLatitude
	 *            Objet latitude if null, you must set it to Interger.MIN_VALUE
	 * @param p_oLongitude
	 *            Objet longitude if null, you must set it to Interger.MIN_VALUE
	 */
	public void setGPSPosition(Double p_oLatitude, Double p_oLongitude) {
		this.latitude = p_oLatitude;
		this.longitude = p_oLongitude;
		if (p_oLatitude == null || p_oLongitude == null ) {
			this.isAcurate = false;
		}
	}

	/**
	 * Sets the GPS  Position
	 *
	 * @param p_oLatitude
	 *            Objet latitude as string
	 * @param p_oLongitude
	 *            Objet longitude as string
	 */
	public void setGPSPosition(String p_oLatitude, String p_oLongitude) {
		Double latitude = p_oLatitude != null && !"".equals(p_oLatitude) ? 
				Double.valueOf(p_oLatitude)
				: null;
				Double longitude = p_oLongitude != null && !"".equals(p_oLongitude) ? 
						Double.valueOf(p_oLongitude)
						: null;
						this.setGPSPosition(latitude, longitude);
	}

	/**
	 * gets longitude
	 *
	 * @return Objet longitude
	 */
	public Double getLongitude() {
		return this.longitude;
	}

	/**
	 * get the address complement
	 *
	 * @return Objet compl
	 */
	public String getCompl() {
		return this.compl;
	}

	/**
	 * Affecte l'objet compl
	 *
	 * @param p_oCompl
	 *            Objet compl
	 */
	public void setCompl(String p_oCompl) {
		this.compl = p_oCompl;
	}

	/**
	 * Retourne l'objet street
	 *
	 * @return Objet street
	 */
	public String getStreet() {
		return this.street;
	}

	/**
	 * Affecte l'objet street
	 *
	 * @param p_oStreet
	 *            Objet street
	 */
	public void setStreet(String p_oStreet) {
		this.street = p_oStreet;
	}

	/**
	 * Retourne l'objet city
	 *
	 * @return Objet city
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * Affecte l'objet city
	 *
	 * @param p_oCity
	 *            Objet city
	 */
	public void setCity(String p_oCity) {
		this.city = p_oCity;
	}

	/**
	 * Retourne l'objet coutry
	 *
	 * @return Objet coutry
	 */
	public String getCoutry() {
		return this.coutry;
	}

	/**
	 * Affecte l'objet coutry
	 *
	 * @param p_oCoutry
	 *            Objet coutry
	 */
	public void setCoutry(String p_oCoutry) {
		this.coutry = p_oCoutry;
	}

	/**
	 * <p>clear.</p>
	 */
	public void clear() {
		this.latitude = 0d;
		this.longitude = 0d;
		this.street = "";
		this.compl = "";
		this.city = "";
		this.coutry = "";
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		boolean r_bResult = obj != null && obj instanceof AddressLocationSVMImpl; 
		if (r_bResult) {
			AddressLocationSVMImpl address = (AddressLocationSVMImpl) obj;
			if (address.latitude != null && address.longitude != null) {
				r_bResult = address.latitude.equals(this.latitude) && address.longitude.equals(this.longitude);
			} else {
				if (address.latitude == null && address.longitude == null) {
					r_bResult = this.latitude == null && this.longitude == null;
				} else if (address.latitude == null) {
					r_bResult = this.latitude == null && address.longitude.equals(this.longitude);
				} else {
					r_bResult = this.longitude == null && address.latitude.equals(this.latitude);
				}
			}
		}
		return r_bResult;
	}

}
