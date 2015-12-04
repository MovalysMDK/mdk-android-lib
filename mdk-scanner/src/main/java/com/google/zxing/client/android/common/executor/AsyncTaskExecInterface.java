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
package com.google.zxing.client.android.common.executor;

import android.os.AsyncTask;

/**
 * <p>AsyncTaskExecInterface interface.</p>
 *
 */
public interface AsyncTaskExecInterface {

  /**
   * <p>execute.</p>
   *
   * @param p_oTask a {@link android.os.AsyncTask} object.
   * @param p_oArgs a T object.
   * @param <T> a T object.
   */
  <T> void execute(AsyncTask<T,?,?> p_oTask, T... p_oArgs);

}
