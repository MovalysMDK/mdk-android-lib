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

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;


final class R {
  private static final AndroidApplication APPLI = (AndroidApplication) Application.getInstance();

  public static final class color {
    public static final int viewfinder_mask = APPLI.getAndroidIdByRKey(AndroidApplicationR.component_barcodescanner_color_viewfinder_mask);
    public static final int result_view = APPLI.getAndroidIdByRKey(AndroidApplicationR.component_barcodescanner_color_result_view);
    public static final int viewfinder_laser = APPLI.getAndroidIdByRKey(AndroidApplicationR.component_barcodescanner_color_viewfinder_laser);
    public static final int possible_result_points = APPLI.getAndroidIdByRKey(AndroidApplicationR.component_barcodescanner_color_possible_result_points);
    public static final int result_points = APPLI.getAndroidIdByRKey(AndroidApplicationR.component_barcodescanner_color_result_points);
  }

  public static final class id {
    public static final int decode = APPLI.getAndroidIdByRKey(AndroidApplicationR.component_barcodescanner_message_decode);
    public static final int decode_failed = APPLI.getAndroidIdByRKey(AndroidApplicationR.component_barcodescanner_message_decode_failed);
    public static final int decode_succeeded = APPLI.getAndroidIdByRKey(AndroidApplicationR.component_barcodescanner_message_decode_succeeded);
    public static final int launch_product_query = APPLI.getAndroidIdByRKey(AndroidApplicationR.component_barcodescanner_message_launch_product_query);
    public static final int preview_view = APPLI.getAndroidIdByRKey(AndroidApplicationR.component_barcodescanner_id_preview_view);
    public static final int restart_preview = APPLI.getAndroidIdByRKey(AndroidApplicationR.component_barcodescanner_message_restart_preview);
    public static final int viewfinder_view = APPLI.getAndroidIdByRKey(AndroidApplicationR.component_barcodescanner_id_viewfinder_view);
    public static final int return_scan_result =  APPLI.getAndroidIdByRKey(AndroidApplicationR.component_barcodescanner_message_return_scan_result);
    public static final int quit = APPLI.getAndroidIdByRKey(AndroidApplicationR.component_barcodescanner_message_quit);
  }

  public static final class layout {
    public static final int fwk_component__barcodescanner = APPLI.getAndroidIdByRKey(AndroidApplicationR.component_barcodescanner_layout);
  }
  
  public static final class xml {
  	public static final int preferences = APPLI.getAndroidIdByRKey(AndroidApplicationR.component_barcodescanner_preferences);
  }
}