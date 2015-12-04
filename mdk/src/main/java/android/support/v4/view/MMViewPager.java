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
package android.support.v4.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * <p>MMViewPager</p>
 *
 * <p>Copyright (c) 2014
 */
public class MMViewPager extends ViewPager{

	/**
	 * Indicates that if the ViewPager should has focus
	 */
	boolean shouldHasFocus;

	/**
	 * Saves the state of the focus
	 */
	boolean hadFocus;

	/**
	 * constructor
	 *
	 * @param p_oContext context
	 * @param p_oAttrs attributes set
	 */
	public MMViewPager(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
	}

	/**
	 * constructor
	 *
	 * @param p_oContext context
	 */
	public MMViewPager(Context p_oContext) {
		super(p_oContext);
	}

	@Override
	void populate() {
		//ViewPager should not has focus to call super.populate()
		this.shouldHasFocus = false;
		super.populate();
		this.shouldHasFocus = this.hadFocus;
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasFocus() {
		//If the ViewPager shouldn't has focus, false is returned and the true fucs state is saved.
		//Otherwise, the super focus state is returned.
		if(!this.shouldHasFocus) {
			this.hadFocus = super.hasFocus();
			return false;
		}
		else  {
			return super.hasFocus();
		}
	}

}
