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
package com.adeuza.movalysfwk.mobile.mf4android.utils.permission;

/**
 * based on library : https://github.com/kayvannj/PermissionUtil
 * Created by kayvan on 10/27/15.
 */
public class SinglePermission {

    private String mPermissionName;
    private boolean mRationalNeeded = false;
    private String mReason;

    public SinglePermission(String permissionName) {
        mPermissionName = permissionName;
    }

    public SinglePermission(String permissionName, String reason) {
        mPermissionName = permissionName;
        mReason = reason;
    }

    public boolean isRationalNeeded() {
        return mRationalNeeded;
    }

    public void setRationalNeeded(boolean rationalNeeded) {
        mRationalNeeded = rationalNeeded;
    }

    public String getReason() {
        return mReason == null ? "" : mReason;
    }

    public void setReason(String reason) {
        mReason = reason;
    }

    public String getPermissionName() {
        return mPermissionName;
    }

    public void setPermissionName(String permissionName) {
        mPermissionName = permissionName;
    }


}
