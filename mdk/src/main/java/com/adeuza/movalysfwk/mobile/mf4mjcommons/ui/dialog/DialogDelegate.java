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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog;

import java.util.Map;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.common.AbstractNamableObject;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * Delegate of Dialog
 *
 */
public class DialogDelegate extends AbstractNamableObject implements VisualComponentFwkDelegate {
	/**
	 * Constructs a new delegate
	 */
	public DialogDelegate() {
		super(true);
	}
	
	@Override
	public VisualComponentConfiguration getConfiguration() {
		return ConfigurationsHandler.getInstance().getVisualConfiguration(this.getConfigurationName());
	}

	@Override
	public void setDescriptor(RealVisualComponentDescriptor p_oDescriptor) {
		// nothing ??
	}

	@Override
	public RealVisualComponentDescriptor getDescriptor() {
		return null;
	}

	@Override
	public void registerVM(ViewModel p_oViewModel) {
		// nothing
	}

	@Override
	public void unregisterVM() {
		// nothing
	}

	@Override
	public String getFragmentTag() {
		return null;
	}

	@Override
	public void setFragmentTag(String p_sFragmentTag) {
		// nothing
	}

	@Override
	public boolean hasRules() {
		return false;
	}

	@Override
	public void setHasRules(boolean p_bHasRules) {
		// nothing
	}

	@Override
	public void configurationSetLabel(String p_sLabel) {
		// nothing
	}

	@Override
	public CustomConverter customConverter() {
		return null;
	}

	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		// nothing
	}

	@Override
	public CustomFormatter customFormatter() {
		return null;
	}

	@Override
	public void setCustomFormatter(CustomFormatter p_oFormatter) {
		// nothing
	}

	@Override
	public void resetChanged() {
		// nothing to do
	}

	@Override
	public boolean isChanged() {
		return false;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public Map<String, Object> instanceAttributes() {
		return null;
	}

	@Override
	public void restoreInstanceAttributes() {
		// Nothing to do
	}

	@Override
	public void saveInstanceAttributes() {
		// Nothing to do
	}
	
}
