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
package com.google.zxing.client.android.camera.open;

import android.hardware.Camera;

/**
 * Provides an abstracted means to open a {@link android.hardware.Camera}. The API changes over Android API versions and
 * this allows the app to use newer API methods while retaining backwards-compatible behavior.
 */
public interface OpenCameraInterface {

  /**
   * <p>open.</p>
   *
   * @return a {@link android.hardware.Camera} object.
   */
  Camera open();

}
