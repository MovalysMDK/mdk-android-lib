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

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
/**
 * Class to retrieve the results of the scannering action 
 */
public final class ViewfinderResultPointCallback implements ResultPointCallback {
	/**
	 * View scanning 
	 */
	private final ViewfinderViewable viewfinderView;

	public ViewfinderResultPointCallback(ViewfinderViewable p_oViewfinderView) {
		this.viewfinderView = p_oViewfinderView;
	}
	
	/**
	 * Handler of the action of found data in the scanner
	 */
	@Override
	public void foundPossibleResultPoint(ResultPoint p_oPoint) {
		viewfinderView.addPossibleResultPoint(p_oPoint);
	}
}
