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
package com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable;

import java.util.Iterator;

import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMCheckBoxGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * Framework delegate for {@link MMCheckBoxGroup} component
 * @see AndroidConfigurableCheckBoxGroupComponentFwkDelegate
 */
public class AndroidConfigurableCheckBoxGroupComponentFwkDelegate extends
		AndroidConfigurableVisualComponentFwkDelegate {
	/**
	 * Create new instance of AndroidConfigurableCheckBoxGroupComponentFwkDelegate
	 * @param p_oCurrentView the current view
	 */
	public AndroidConfigurableCheckBoxGroupComponentFwkDelegate(ConfigurableVisualComponent p_oCurrentView) {
		super(p_oCurrentView);
	}
	
	/**
	 * Create new instance of AndroidConfigurableCheckBoxGroupComponentFwkDelegate
	 * @param p_oCurrentView the current view
	 * @param p_oAttrs the view attributes
	 */
	public AndroidConfigurableCheckBoxGroupComponentFwkDelegate(ConfigurableVisualComponent p_oCurrentView, AttributeSet p_oAttrs) {
		super(p_oCurrentView, p_oAttrs);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetChanged() {
		super.resetChanged();

		((MMCheckBoxGroup<?>) this.currentView).getInitialCheckedBoxes().clear();
		
		for (int i=0; i<((ViewGroup)this.currentView).getChildCount();i++) {
			if (((ViewGroup)this.currentView).getChildAt(i) instanceof CheckBox) {
				CheckBox oCheckBox = (CheckBox) ((ViewGroup)this.currentView).getChildAt(i);
				if (oCheckBox.isChecked()) {
					((MMCheckBoxGroup<?>) this.currentView).getInitialCheckedBoxes().add(oCheckBox);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChanged() {
		boolean r_bChanged = false;

		// 1. Une des cases précédemment cochées a-t-elle été décochée?
		Iterator<CheckBox> iterInitialCheckedCheckBoxes = ((MMCheckBoxGroup<?>) this.currentView).getInitialCheckedBoxes().iterator();
		while (iterInitialCheckedCheckBoxes.hasNext() && !r_bChanged) {
			r_bChanged = !iterInitialCheckedCheckBoxes.next().isChecked();
		}

		if (!r_bChanged) {
			// 2. Une autre case a-t-elle été cochée?
			int iCheckedCount = 0;
			for (CheckBox oCheckBox : ((MMCheckBoxGroup<?>) this.currentView).getCheckBoxByValue().values()) {
				if (oCheckBox.isChecked()) {
					iCheckedCount++;
				}
			}
			r_bChanged = iCheckedCount != ((MMCheckBoxGroup<?>) this.currentView).getInitialCheckedBoxes().size();
		}
		return r_bChanged;
	}
	
}
