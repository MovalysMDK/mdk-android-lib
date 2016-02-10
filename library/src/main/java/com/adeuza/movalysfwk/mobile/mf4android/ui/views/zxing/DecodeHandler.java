/*
 * Copyright (C) 2010 ZXing authors
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

import java.io.ByteArrayOutputStream;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumericConstants;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

final class DecodeHandler extends Handler {

  private static final String TAG = DecodeHandler.class.getSimpleName();

  private final CaptureActivity activity;
  private final MultiFormatReader multiFormatReader;
  private boolean running = true;

  DecodeHandler(CaptureActivity p_oActivity, Map<DecodeHintType,Object> p_oHints) {
    multiFormatReader = new MultiFormatReader();
    multiFormatReader.setHints(p_oHints);
    this.activity = p_oActivity;
  }

  @Override
  public void handleMessage(Message p_oMessage) {
    if (!running) {
      return;
    }
    if (p_oMessage.what == R.id.decode) {
        decode((byte[]) p_oMessage.obj, p_oMessage.arg1, p_oMessage.arg2);
    } else if (p_oMessage.what == R.id.quit) {
        running = false;
        Looper.myLooper().quit();
    }
  }

  /**
   * Decode the data within the viewfinder rectangle, and time how long it took. For efficiency,
   * reuse the same reader objects from one decode to the next.
   *
   * @param p_oData   The YUV preview frame.
   * @param p_iWidth  The width of the preview frame.
   * @param p_iHeight The height of the preview frame.
   */
  private void decode(byte[] p_oData, int p_iWidth, int p_iHeight) {
    long start = System.currentTimeMillis();
    Result rawResult = null;
    PlanarYUVLuminanceSource source = activity.getCameraManager().buildLuminanceSource(p_oData, p_iWidth, p_iHeight);
    if (source != null) {
      BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
      try {
        rawResult = multiFormatReader.decodeWithState(bitmap);
      } catch (ReaderException re) {
        // continue
    	  Log.v(TAG, "[decode] "+re);
      } finally {
        multiFormatReader.reset();
      }
    }

    Handler handler = activity.getHandler();
    if (rawResult != null) {
      // Don't log the barcode contents for security.
      long end = System.currentTimeMillis();
      Log.d(TAG, "Found barcode in " + (end - start) + " ms");
      if (handler != null) {
        Message message = Message.obtain(handler, R.id.decode_succeeded, rawResult);
        Bundle bundle = new Bundle();
        bundleThumbnail(source, bundle);        
        message.setData(bundle);
        message.sendToTarget();
      }
    } else {
      if (handler != null) {
        Message message = Message.obtain(handler, R.id.decode_failed);
        message.sendToTarget();
      }
    }
  }

  private static void bundleThumbnail(PlanarYUVLuminanceSource p_oSource, Bundle p_oBundle) {
    int[] pixels = p_oSource.renderThumbnail();
    int width = p_oSource.getThumbnailWidth();
    int height = p_oSource.getThumbnailHeight();
    Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
    ByteArrayOutputStream out = new ByteArrayOutputStream();    
    bitmap.compress(Bitmap.CompressFormat.JPEG, NumericConstants.PERCENT_50, out);
    p_oBundle.putByteArray(DecodeThread.BARCODE_BITMAP, out.toByteArray());
    p_oBundle.putFloat(DecodeThread.BARCODE_SCALED_FACTOR, (float) width / p_oSource.getWidth());
  }
}
