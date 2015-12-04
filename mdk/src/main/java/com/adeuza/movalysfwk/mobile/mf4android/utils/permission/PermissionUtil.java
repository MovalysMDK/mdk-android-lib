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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * based on library : https://github.com/kayvannj/PermissionUtil
 * Created by kayvan on 10/26/15.
 */
public class PermissionUtil {

	public static PermissionRequestObject with(Context oContext) {
        return new PermissionRequestObject(oContext);
    }

    static public class PermissionRequestObject {

        private static final String TAG = PermissionRequestObject.class.getSimpleName();

        private ArrayList<SinglePermission> mPermissionsWeDontHave;
        private int mRequestCode;
        private FuncGrant mGrantFunc;
        private FuncDeny mDenyFunc;
        private Func2 mResultFunc;
        private Func3 mRationalFunc;
        private String[] mPermissionNames;
        
        private WeakReference<Context> mContext;
        
        public PermissionRequestObject(Context context) {
        	mContext = new WeakReference<Context>(context);
        }
        
        public PermissionRequestObject request(String permissionName) {
            return this.request(new String[]{permissionName});
        }

        public PermissionRequestObject request(String... permissionNames) {
        	mPermissionNames = permissionNames;
        	return this;
        }

        /**
         * Execute the permission request with the given Request Code
         *
         * @param reqCode
         *         a unique request code in your activity
         */
        public PermissionRequestObject ask(int reqCode) {
            mRequestCode = reqCode;
            int length = mPermissionNames.length;
            mPermissionsWeDontHave = new ArrayList<>(length);
            for (String mPermissionName : mPermissionNames) {
                mPermissionsWeDontHave.add(new SinglePermission(mPermissionName));
            }

            if (needToAsk()) {
                Log.i(TAG, "Asking for permission");

                if (mContext != null)  {
                	Context oContext = mContext.get();
	                if (oContext != null && oContext instanceof Activity)
	                	ActivityCompat.requestPermissions((Activity)oContext, mPermissionNames, reqCode);
                }
            } else {
                Log.i(TAG, "No need to ask for permission");
                if (mGrantFunc != null) mGrantFunc.call();
            }
            return this;
        }

        private boolean needToAsk() {
            ArrayList<SinglePermission> neededPermissions = new ArrayList<>(mPermissionsWeDontHave);
            for (int i = 0; i < mPermissionsWeDontHave.size(); i++) {
                SinglePermission perm = mPermissionsWeDontHave.get(i);
                int checkRes = 0;
                if (mContext != null) {
                	Context oContext = mContext.get();
                	if (oContext != null)
                		checkRes = ContextCompat.checkSelfPermission(mContext.get(), perm.getPermissionName());
                }
               	
                if (checkRes == PackageManager.PERMISSION_GRANTED) {
                    neededPermissions.remove(perm);
                } else {
                	if (mContext != null)  {
                    	Context oContext = mContext.get();
    	                if (oContext != null && oContext instanceof Activity) {
    	                	 if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)oContext, perm.getPermissionName())) {
    	                         perm.setRationalNeeded(true);
    	                     }
    	                }
                    }
                }
            }
            mPermissionsWeDontHave = neededPermissions;
            mPermissionNames = new String[mPermissionsWeDontHave.size()];
            for (int i = 0; i < mPermissionsWeDontHave.size(); i++) {
                mPermissionNames[i] = mPermissionsWeDontHave.get(i).getPermissionName();
            }
            return mPermissionsWeDontHave.size() != 0;
        }

        /**
         * Called for the first denied permission if there is need to show the rational
         */
        public PermissionRequestObject onRational(Func3 rationalFunc) {
            mRationalFunc = rationalFunc;
            return this;
        }

        /**
         * Called if all the permissions were granted
         */
        public PermissionRequestObject onAllGranted(FuncGrant grantFunc) {
            mGrantFunc = grantFunc;
            return this;
        }

        /**
         * Called if there is at least one denied permission
         */
        public PermissionRequestObject onAnyDenied(FuncDeny denyFunc) {
            mDenyFunc = denyFunc;
            return this;
        }

        /**
         * Called with the original operands from {@link AppCompatActivity#onRequestPermissionsResult(int, String[], int[])
         * onRequestPermissionsResult} for any result
         */
        public PermissionRequestObject onResult(Func2 resultFunc) {
            mResultFunc = resultFunc;
            return this;
        }

        /**
         * This Method should be called from {@link AppCompatActivity#onRequestPermissionsResult(int, String[], int[])
         * onRequestPermissionsResult} with all the same incoming operands
         * <pre>
         * {@code
         *
         * public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
         *      if (mStoragePermissionRequest != null)
         *          mStoragePermissionRequest.onRequestPermissionsResult(requestCode, permissions,grantResults);
         * }
         * }
         * </pre>
         */
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            Log.i(TAG, String.format("ReqCode: %d, ResCode: %d, PermissionName: %s", requestCode, grantResults[0], permissions[0]));

            if (mRequestCode == requestCode) {
                if (mResultFunc != null) {
                    Log.i(TAG, "Calling Results Func");
                    mResultFunc.call(requestCode, permissions, grantResults);
                    return;
                }
                List<String> deniedPermissions = new ArrayList<>();
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        if (mPermissionsWeDontHave.get(i).isRationalNeeded()) {
                            if (mRationalFunc != null) {
                                Log.i(TAG, "Calling Rational Func");
                                mRationalFunc.call(mPermissionsWeDontHave.get(i).getPermissionName());
                            }
                        }
                        deniedPermissions.add(mPermissionsWeDontHave.get(i).getPermissionName());
                        
                    }
                }
                if (mDenyFunc != null) {
                    Log.i(TAG, "Calling Deny Func");
                    String[] strings = new String[] {};
                    mDenyFunc.call(deniedPermissions.toArray(strings));
                    return;
                } else Log.e(TAG, "NUll DENY FUNCTIONS");


                // there has not been any deny
                if (mGrantFunc != null) {
                    Log.i(TAG, "Calling Grant Func");
                    mGrantFunc.call();
                } else Log.e(TAG, "NUll GRANT FUNCTIONS");
            }
        }
    }
}
