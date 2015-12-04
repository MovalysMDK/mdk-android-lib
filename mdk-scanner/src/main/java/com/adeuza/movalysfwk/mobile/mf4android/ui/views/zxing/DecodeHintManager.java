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

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.DecodeHintType;

/**
 * @author Lachezar Dobrev
 */
final class DecodeHintManager {
  
  private static final String TAG = DecodeHintManager.class.getSimpleName();

  // This pattern is used in decoding integer arrays.
  private static final Pattern COMMA = Pattern.compile(",");

  private DecodeHintManager() {}

  /**
   * <p>Split a query string into a list of name-value pairs.</p>
   * 
   * <p>This is an alternative to the {@link Uri#getQueryParameterNames()} and
   * {@link Uri#getQueryParameters(String)}, which are quirky and not suitable
   * for exist-only Uri parameters.</p>
   * 
   * <p>This method ignores multiple parameters with the same name and returns the
   * first one only. This is technically incorrect, but should be acceptable due
   * to the method of processing Hints: no multiple values for a hint.</p>
   * 
   * @param p_sQuery query to split
   * @return name-value pairs
   */
  private static Map<String,String> splitQuery(String p_sQuery) {
    Map<String,String> map = new HashMap<>();
    int pos = 0;
    while (pos < p_sQuery.length()) {
      if (p_sQuery.charAt(pos) == '&') {
        // Skip consecutive ampersand separators.
        pos ++;
        continue;
      }
      int amp = p_sQuery.indexOf('&', pos);
      int equ = p_sQuery.indexOf('=', pos);
      if (amp < 0) {
        // This is the last element in the query, no more ampersand elements.
        String name;
        String text;
        if (equ < 0) {
          // No equal sign
          name = p_sQuery.substring(pos);
          name = name.replace('+', ' '); // Preemptively decode +
          name = Uri.decode(name);
          text = "";
        } else {
          // Split name and text.
          name = p_sQuery.substring(pos, equ);
          name = name.replace('+', ' '); // Preemptively decode +
          name = Uri.decode(name);
          text = p_sQuery.substring(equ + 1);
          text = text.replace('+', ' '); // Preemptively decode +
          text = Uri.decode(text);
        }
        if (!map.containsKey(name)) {
          map.put(name, text);
        }
        break;
      }
      if (equ < 0 || equ > amp) {
        // No equal sign until the &: this is a simple parameter with no value.
        String name = p_sQuery.substring(pos, amp);
        name = name.replace('+', ' '); // Preemptively decode +
        name = Uri.decode(name);
        if (!map.containsKey(name)) {
          map.put(name, "");
        }
        pos = amp + 1;
        continue;
      }
      String name = p_sQuery.substring(pos, equ);
      name = name.replace('+', ' '); // Preemptively decode +
      name = Uri.decode(name);
      String text = p_sQuery.substring(equ+1, amp);
      text = text.replace('+', ' '); // Preemptively decode +
      text = Uri.decode(text);
      if (!map.containsKey(name)) {
        map.put(name, text);
      }
      pos = amp + 1;
    }
    return map;
  }

  static Map<DecodeHintType,?> parseDecodeHints(Uri p_oInputUri) {
    String query = p_oInputUri.getEncodedQuery();
    if (query == null || query.length() == 0) {
      return null;
    }

    // Extract parameters
    Map<String, String> parameters = splitQuery(query);

    Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);

    for (DecodeHintType hintType: DecodeHintType.values()) {

      if (
    		  hintType == DecodeHintType.CHARACTER_SET 
    		  || hintType == DecodeHintType.NEED_RESULT_POINT_CALLBACK 
    		  || hintType == DecodeHintType.POSSIBLE_FORMATS
    	) {
        continue; // This hint is specified in another way
      }

      String parameterName = hintType.name();
      String parameterText = parameters.get(parameterName);
      if (parameterText == null) {
        continue;
      }
      if (hintType.getValueType().equals(Object.class)) {
        // This is an unspecified type of hint content. Use the value as is.
        // TODO: Can we make a different assumption on this?
        hints.put(hintType, parameterText);
        continue;
      }
      if (hintType.getValueType().equals(Void.class)) {
        // Void hints are just flags: use the constant specified by DecodeHintType
        hints.put(hintType, Boolean.TRUE);
        continue;
      }
      if (hintType.getValueType().equals(String.class)) {
        // A string hint: use the decoded value.
        hints.put(hintType, parameterText);
        continue;
      }
      if (hintType.getValueType().equals(Boolean.class)) {
        // A boolean hint: a few values for false, everything else is true.
        // An empty parameter is simply a flag-style parameter, assuming true
        if (parameterText.length() == 0) {
          hints.put(hintType, Boolean.TRUE);
        } else if (
        		"0".equals(parameterText) 
        		|| "false".equalsIgnoreCase(parameterText) 
        		|| "no".equalsIgnoreCase(parameterText)) {
          hints.put(hintType, Boolean.FALSE);
        } else {
          hints.put(hintType, Boolean.TRUE);
        }

        continue;
      }
      if (hintType.getValueType().equals(int[].class)) {
        // An integer array. Used to specify valid lengths.
        // Strip a trailing comma as in Java style array initialisers.
        if (parameterText.length() > 0 && parameterText.charAt(parameterText.length() - 1) == ',') {
          parameterText = parameterText.substring(0, parameterText.length() - 1);
        }
        String[] values = COMMA.split(parameterText);
        int[] array = new int[values.length];
        for (int i = 0; i < values.length; i++) {
          try {
            array[i] = Integer.parseInt(values[i]);
          } catch (NumberFormatException ignored) {
            Log.w(TAG, "Skipping array of integers hint " + hintType + " due to invalid numeric value: '" + values[i] + '\'');
            array = null;
            break;
          }
        }
        if (array != null) {
          hints.put(hintType, array);
        }
        continue;
      } 
      Log.w(TAG, "Unsupported hint type '" + hintType + "' of type " + hintType.getValueType());
    }

    Log.i(TAG, "Hints from the URI: " + hints);
    return hints;
  }

  static Map<DecodeHintType, Object> parseDecodeHints(Intent p_oIntent) {
    Bundle extras = p_oIntent.getExtras();
    if (extras == null || extras.isEmpty()) {
      return null;
    }
    Map<DecodeHintType,Object> hints = new EnumMap<>(DecodeHintType.class);

    for (DecodeHintType hintType: DecodeHintType.values()) {

      if (hintType == DecodeHintType.CHARACTER_SET 
		  || hintType == DecodeHintType.NEED_RESULT_POINT_CALLBACK 
		  || hintType == DecodeHintType.POSSIBLE_FORMATS) {
        continue; // This hint is specified in another way
      }

      String hintName = hintType.name();
      if (extras.containsKey(hintName)) {
        if (hintType.getValueType().equals(Void.class)) {
          // Void hints are just flags: use the constant specified by the DecodeHintType
          hints.put(hintType, Boolean.TRUE);
        } else {
          Object hintData = extras.get(hintName);
          if (hintType.getValueType().isInstance(hintData)) {
            hints.put(hintType, hintData);
          } else {
            Log.w(TAG, "Ignoring hint " + hintType + " because it is not assignable from " + hintData);
          }
        }
      }
    }

    Log.i(TAG, "Hints from the Intent: " + hints);
    return hints;
  }

}
