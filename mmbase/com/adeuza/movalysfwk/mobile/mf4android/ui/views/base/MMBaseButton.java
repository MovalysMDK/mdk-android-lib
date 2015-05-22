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
package com.adeuza.movalysfwk.mobile.mf4android.ui.views.base;

import android.content.Context;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatButton;

/**
 * <p>Button widget used in the Movalys Mobile product for Android</p>
 *
 *
 *
 */

public class MMBaseButton extends AppCompatButton {

	/**
	 * Constructs a new MMBaseButton
	 * @param p_oContext the context to use
	 * @see AppCompatButton#AppCompatButton(Context)
	 */
	public MMBaseButton(Context p_oContext) {
		super(p_oContext);
	}

	/**
	 * Constructs a new MMBaseButton
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see AppCompatButton#AppCompatButton(Context, AttributeSet)
	 */
	public MMBaseButton(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
	}

	/**
	 * Constructs a new MMBaseButton
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use
	 * @see AppCompatButton#AppCompatButton(Context, AttributeSet, int)
	 */
	public MMBaseButton(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
	}
}
