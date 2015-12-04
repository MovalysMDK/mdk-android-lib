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
package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.adeuza.movalysfwk.mobile.mf4android.ui.annotations.BaseComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.wrappers.ComponentsAttributesHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.base.MMBaseEditText;

/**
 * <p>EditText widget used in the Movalys Mobile product for Android</p>
 */
@BaseComponent(baseName="MMBaseEditText", baseClass="android.widget.EditText", appCompatClass="android.support.v7.widget.AppCompatEditText")
public class MMEditText extends MMBaseEditText {

	/**
	 * Constructs a new MMEditText
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see EditText#EditText(Context, AttributeSet)
	 */
	public MMEditText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			ComponentsAttributesHelper.getInstance().storeAttributesForComponent(this.getId(), p_oAttrs);
		}
	}

	/**
	 * Constructs a new MMEditText
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use
	 * @see EditText#EditText(Context, AttributeSet, int)
	 */
	public MMEditText(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			ComponentsAttributesHelper.getInstance().storeAttributesForComponent(this.getId(), p_oAttrs);
		}
	}
}
