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
package com.adeuza.movalysfwk.mobile.mf4android.utils.security;

public class NativeCryptoConstants {

    public static final int EVP_PKEY_RSA = 6; // NID_rsaEcnryption
    public static final int EVP_PKEY_DSA = 116; // NID_dsa
    public static final int EVP_PKEY_DH = 28; // NID_dhKeyAgreement
    public static final int EVP_PKEY_EC = 408; // NID_X9_62_id_ecPublicKey
    public static final int EVP_PKEY_HMAC = 855; // NID_hmac
    public static final int EVP_PKEY_CMAC = 894; // NID_cmac


    private NativeCryptoConstants() {
    }
}
