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
package com.adeuza.movalysfwk.mobile.mf4android.ui.views.zxing.camera;

import android.content.SharedPreferences;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.zxing.PreferencesActivity;

/**
 * Enumerates settings of the prefernce controlling the front light.
 */
public enum FrontLightMode {

  /** Always on. */
  ON,
  /** On only when ambient light is low. */
  AUTO,
  /** Always off. */
  OFF;

  private static FrontLightMode parse(String p_sModeString) {
	  if(p_sModeString == null){
		  return OFF;
	  }
	  else {
		  return valueOf(p_sModeString);
	  }
  }

  public static FrontLightMode readPref(SharedPreferences p_oSharedPrefs) {
    return parse(p_oSharedPrefs.getString(PreferencesActivity.KEY_FRONT_LIGHT_MODE, null));
  }

}
