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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.auth;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.adeuza.movalysfwk.apache.codec.binary.Hex;
import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.RestException;

/**
 * <p>HMACRestAuth class.</p>
 *
 */
public class HMACRestAuth implements RestAuth {

	/**
	 * algorithm to use to encode token
	 */
	private static final String HMAC_ALGORITHM = "HmacSHA256";
	
	/**
	 * {@inheritDoc}
	 *
	 * (non-Javadoc)
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.auth.RestAuth#getAuthHeaders(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String,String> getAuthHeaders( String p_sLogin, String p_sPassword, String p_sUrl, String p_sEntryPoint ) throws AuthRestException {
		Map<String,String> r_oAuthTokens = new HashMap<String, String>();
		String sValue = p_sLogin + ':' + this.computeToken(p_sLogin, p_sPassword, p_sUrl, p_sEntryPoint);
		r_oAuthTokens.put(HEADER_TOKEN, sValue);
		return r_oAuthTokens ;
	}
	
	/**
	 * Compute token to use
	 * @param p_sUrl the url to use
	 * @return the token = use + : + encoded data
	 * @throws RestException 
	 */
	private String computeToken( String p_sLogin, String p_sPassword, String p_sUrl, String p_sEntryPoint) throws AuthRestException {
		return this.encodeData(this.computeMessage(p_sUrl, p_sEntryPoint), this.computePrivateKey(p_sLogin, p_sPassword));
	}
	
	/** 
	 * compute the message with current url
	 * @param p_sUrl url to compute
	 * @return the message use to construct token
	 */
	private String computeMessage(String p_sUrl, String p_sEntryPoint) {
		return p_sUrl.substring(p_sUrl.indexOf(p_sEntryPoint));
	}
	
	/**
	 * Encode data with private key
	 * @param p_sData data to encode
	 * @param p_sPrivateKey private key to use
	 * @return encoded data
	 * @throws RestException 
	 */
	private String encodeData(String p_sData, String p_sPrivateKey) throws AuthRestException {
		String r_sResult = null;
		try {
			byte[] oKeyBytes = p_sPrivateKey.getBytes();
			SecretKeySpec oSigningKey = new SecretKeySpec(oKeyBytes, HMAC_ALGORITHM);
			Mac oMac = Mac.getInstance(HMAC_ALGORITHM);
			oMac.init(oSigningKey);
			byte[] oRawMac = oMac.doFinal(p_sData.getBytes());
			byte[] oHexBytes = new Hex().encode(oRawMac);
			r_sResult =  new String(oHexBytes, "UTF-8");
		} catch (NoSuchAlgorithmException e) {
			throw new AuthRestException(e);
		} catch (IllegalStateException e) {
			throw new AuthRestException(e);
		} catch (UnsupportedEncodingException e) {
			throw new AuthRestException(e);
		} catch (InvalidKeyException e) {
			throw new AuthRestException(e);
		}
		return r_sResult;
	}
	
	/**
	 * Compute the private key to use
	 * @return the private key use for send token
	 */
	private String computePrivateKey( String p_sLogin, String p_sPassword ) {
		return StringUtils.concat(p_sLogin,  "__##__", p_sPassword);
	}
}
