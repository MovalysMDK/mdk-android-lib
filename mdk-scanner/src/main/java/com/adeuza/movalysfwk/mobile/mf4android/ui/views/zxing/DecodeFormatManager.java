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
package com.adeuza.movalysfwk.mobile.mf4android.ui.views.zxing;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Pattern;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.android.Intents;

final class DecodeFormatManager {

  private static final Pattern COMMA_PATTERN = Pattern.compile(",");

  static final Collection<BarcodeFormat> PRODUCT_FORMATS;
  static final Collection<BarcodeFormat> ONE_D_FORMATS;
  static final Collection<BarcodeFormat> QR_CODE_FORMATS = EnumSet.of(BarcodeFormat.QR_CODE);
  static final Collection<BarcodeFormat> DATA_MATRIX_FORMATS = EnumSet.of(BarcodeFormat.DATA_MATRIX);
  static {
    PRODUCT_FORMATS = EnumSet.of(BarcodeFormat.UPC_A,
                                 BarcodeFormat.UPC_E,
                                 BarcodeFormat.EAN_13,
                                 BarcodeFormat.EAN_8,
                                 BarcodeFormat.RSS_14,
                                 BarcodeFormat.RSS_EXPANDED);
    ONE_D_FORMATS = EnumSet.of(BarcodeFormat.CODE_39,
                               BarcodeFormat.CODE_93,
                               BarcodeFormat.CODE_128,
                               BarcodeFormat.ITF,
                               BarcodeFormat.CODABAR);
    ONE_D_FORMATS.addAll(PRODUCT_FORMATS);
  }

  private DecodeFormatManager() {}
	private static final String TAG = DecodeFormatManager.class.getSimpleName();

  static Collection<BarcodeFormat> parseDecodeFormats(Intent p_oIntent) {
    List<String> scanFormats = null;
    String scanFormatsString = p_oIntent.getStringExtra(Intents.Scan.FORMATS);
    if (scanFormatsString != null) {
      scanFormats = Arrays.asList(COMMA_PATTERN.split(scanFormatsString));
    }
    return parseDecodeFormats(scanFormats, p_oIntent.getStringExtra(Intents.Scan.MODE));
  }

  static Collection<BarcodeFormat> parseDecodeFormats(Uri p_oInputUri) {
    List<String> formats = p_oInputUri.getQueryParameters(Intents.Scan.FORMATS);
    if (formats != null && formats.size() == 1 && formats.get(0) != null){
      formats = Arrays.asList(COMMA_PATTERN.split(formats.get(0)));
    }
    return parseDecodeFormats(formats, p_oInputUri.getQueryParameter(Intents.Scan.MODE));
  }

	private static Collection<BarcodeFormat> parseDecodeFormats(Iterable<String> p_sScanFormats, String p_sDecodeMode) {
		Collection<BarcodeFormat> result = null;
		if (p_sScanFormats != null) {
			result = EnumSet.noneOf(BarcodeFormat.class);
			try {
				for (String format : p_sScanFormats) {
					result.add(BarcodeFormat.valueOf(format));
				}
			} catch (IllegalArgumentException iae) {
				// ignore it then
				Log.v(TAG, "[parseDecodeFormats] "+iae);
			}
		}
	    else if (p_sDecodeMode != null) {
			if (Intents.Scan.PRODUCT_MODE.equals(p_sDecodeMode)) {
				result= PRODUCT_FORMATS;
			}
			if (Intents.Scan.QR_CODE_MODE.equals(p_sDecodeMode)) {
				result= QR_CODE_FORMATS;
			}
			if (Intents.Scan.DATA_MATRIX_MODE.equals(p_sDecodeMode)) {
				result= DATA_MATRIX_FORMATS;
			}
			if (Intents.Scan.ONE_D_MODE.equals(p_sDecodeMode)) {
				result = ONE_D_FORMATS;
			}
		}
		return result;
	}

}