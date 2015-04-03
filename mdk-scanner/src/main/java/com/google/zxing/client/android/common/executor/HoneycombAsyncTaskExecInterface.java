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

import android.annotation.TargetApi;
import android.os.AsyncTask;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumericConstants;

/**
 * On Honeycomb and later, {@link android.os.AsyncTask} returns to serial execution by default which is undesirable.
 * This calls Honeycomb-only APIs to request parallel execution.
 */
@TargetApi(NumericConstants.VERSION_11)
public final class HoneycombAsyncTaskExecInterface implements AsyncTaskExecInterface {

  /** {@inheritDoc} */
  @Override
  public <T> void execute(AsyncTask<T,?,?> p_oTask, T... p_oArgs) {
    p_oTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, p_oArgs);
  }

}
