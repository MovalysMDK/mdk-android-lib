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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.AddressLocationSVMImpl;

/**
 * Value Object classe to manage address location across actions {@link #isAcurate()} allow you to know if this location contains Lat / Long and if
 * its different of {@link java.lang.Integer#MIN_VALUE}
 */
public class AddressLocation extends AbstractActionParameter implements ActionParameter {
	
	/** serial id */
	private static final long serialVersionUID = 8761928278312129068L;
	
	/** WGS84 Latitude*/
	private Double latitude;
	/** WGS84 Longitude*/
	private Double longitude;
	/** Address complement to use for ZA, Commercial center...*/
	private String compl;
	/** street adress contain number in the street + street name*/
	private String street;
	/** ZIP code + city name*/
	private String city;
	/** country name*/
	private String country;

	/**
	 * Construct a new address location
	 */
	public AddressLocation() {
		super();
	}
	
	/**
	 * use this constructor to create {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doopenmap.AddressLocation} when WGS coordinates exists
	 *
	 * @param p_oLatitude
	 *            WGS84 GPS latitude
	 * @param p_oLongitude
	 *            WGS84 GPS Longitude
	 */
	public AddressLocation(Double p_oLatitude, Double p_oLongitude) {
		super();
		this.latitude = p_oLatitude;
		this.longitude = p_oLongitude;
		this.street = "";
		this.compl = "";
		this.city = "";
		this.country = "";
	}

	/**
	 * Use this constructor to create {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.business.doopenmap.AddressLocation} when WGS coordinates are not known
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
	public AddressLocation(String p_oCompl, String p_oStreet, String p_oCity, String p_oCoutry) {
		super();
		this.latitude = null;
		this.longitude = null;
		this.compl = p_oCompl;
		this.street = p_oStreet;
		this.city = p_oCity;
		this.country = p_oCoutry;
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
	public AddressLocation(Double p_oLatitude, Double p_oLongitude, String p_oCompl, String p_oStreet, String p_oCity, String p_oCoutry) {
		super();
		this.latitude = p_oLatitude;
		this.longitude = p_oLongitude;
		this.compl = p_oCompl;
		this.street = p_oStreet;
		this.city = p_oCity;
		this.country = p_oCoutry;
	}

	/**
	 * Constructor using AddressLocationVMImpl
	 *
	 * @param p_oAddressLocationModel the VM of the location {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.AddressLocationSVMImpl}
	 */
	public AddressLocation(AddressLocationSVMImpl p_oAddressLocationModel) {
		this(p_oAddressLocationModel.getLatitude(), p_oAddressLocationModel.getLongitude(), p_oAddressLocationModel.getCompl(), 
				p_oAddressLocationModel.getStreet(), p_oAddressLocationModel.getCity(), p_oAddressLocationModel.getCoutry());
	}

	/**
	 *
	 * if this location contains Lat / Long and if its different of {@link java.lang.Integer#MIN_VALUE}, return true else return false
	 *
	 * @return boolean value
	 */
	public Boolean isAcurate() {
		return this.latitude != null && this.longitude != null && this.latitude != Integer.MIN_VALUE
				&& this.longitude != Integer.MIN_VALUE;
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
	 * Sets latitude
	 *
	 * @param p_oLatitude
	 *            Objet latitude
	 */
	public void setLatitude(Double p_oLatitude) {
		this.latitude = p_oLatitude;
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
	 * sets longitude
	 *
	 * @param p_oLongitude
	 *            Objet longitude
	 */
	public void setLongitude(Double p_oLongitude) {
		this.longitude = p_oLongitude;
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
	public String getCountry() {
		return this.country;
	}

	/**
	 * Affecte l'objet coutry
	 *
	 * @param p_oCoutry
	 *            Objet coutry
	 */
	public void setCountry(String p_oCoutry) {
		this.country = p_oCoutry;
	}

}
