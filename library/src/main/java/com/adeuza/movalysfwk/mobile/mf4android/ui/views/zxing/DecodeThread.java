/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adeuza.movalysfwk.mobile.mf4android.ui.views.zxing;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.ResultPointCallback;

/**
 * This thread does all the heavy lifting of decoding the images.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class DecodeThread extends Thread {

  public static final String BARCODE_BITMAP = "barcode_bitmap";
  public static final String BARCODE_SCALED_FACTOR = "barcode_scaled_factor";
  private static final String TAG = DecodeThread.class.getSimpleName();

  private final CaptureActivity activity;
  private final Map<DecodeHintType,Object> hints;
  private Handler handler;
  private final CountDownLatch handlerInitLatch;

  public DecodeThread(CaptureActivity p_oActivity,
               Collection<BarcodeFormat> p_oDecodeFormats,
               Map<DecodeHintType,?> p_oBaseHints,
               String p_sCharacterSet,
               ResultPointCallback p_oResultPointCallback) {

    this.activity = p_oActivity;
    handlerInitLatch = new CountDownLatch(1);

    hints = new EnumMap<>(DecodeHintType.class);
    if (p_oBaseHints != null) {
      hints.putAll(p_oBaseHints);
    }
    /*
    // The prefs can't change while the thread is running, so pick them up once here.
    if (decodeFormats == null || decodeFormats.isEmpty()) {
      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getContext());
      decodeFormats = EnumSet.noneOf(BarcodeFormat.class);
      if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_1D, false)) {
        decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
      }
      if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_QR, false)) {
        decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
      }
      if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_DATA_MATRIX, false)) {
        decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
      }
    }*/
    hints.put(DecodeHintType.POSSIBLE_FORMATS, p_oDecodeFormats);

    if (p_sCharacterSet != null) {
      hints.put(DecodeHintType.CHARACTER_SET, p_sCharacterSet);
    }
    hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, p_oResultPointCallback);
    Log.i("DecodeThread", "Hints: " + hints);
  }

  public Handler getHandler() {
    try {
      handlerInitLatch.await();
    } catch (InterruptedException ie) {
      // continue?
  	  Log.v(TAG, "[getHandler] "+ie);
    }
    return handler;
  }

  @Override
  public void run() {
    Looper.prepare();
    handler = new DecodeHandler(activity, hints);
    handlerInitLatch.countDown();
    Looper.loop();
  }

}
