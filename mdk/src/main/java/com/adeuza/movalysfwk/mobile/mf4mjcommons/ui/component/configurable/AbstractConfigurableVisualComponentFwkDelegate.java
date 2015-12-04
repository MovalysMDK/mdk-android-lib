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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.common.AbstractNamableObject;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;


/**
 * <p>A delegate pattern for configurable visual component.</p>
 *
 *
 */
public abstract class AbstractConfigurableVisualComponentFwkDelegate extends AbstractNamableObject implements VisualComponentFwkDelegate {
	
	/** indicates whether component changed */
	protected CustomConverter customConverter = null;
	/** indicates whether component changed */
	protected CustomFormatter customFormatter = null;
	/** indicates whether component changed */
	private boolean changed = false;
	/** descriptor */
	private RealVisualComponentDescriptor descriptor = null;
	
	public AbstractConfigurableVisualComponentFwkDelegate(boolean p_bIsMaster) {
		super(p_bIsMaster);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.VisualComponentConfiguration} object.
	 */
	@Override
	public VisualComponentConfiguration getConfiguration() {
		if (ConfigurationsHandler.isLoaded() && this.getModel() != null) {
			return ConfigurationsHandler.getInstance().getVisualConfiguration(this.getConfigurationName());
		}
		else {
			return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @return a boolean.
	 */
	@Override
	public boolean isChanged() {
		return this.changed;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetChanged() {
		this.changed = false;
	}
	
	/**
	 * Set the change value, without trigger.
	 * @param p_bChanged
	 */
	public void setChanged(boolean p_bChanged) {
		this.changed = p_bChanged;
	}
	
	/**
	 * Indicates that component changed
	 */
	public void changed() {
		this.changed = true;
		this.doOnVisualComponentChanged();
	}
	
	/**
	 * Do after the currentComponant is changed
	 */
	protected abstract void doOnVisualComponentChanged();

	/** {@inheritDoc} */
	@Override
	public RealVisualComponentDescriptor getDescriptor() {
		return this.descriptor;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setDescriptor(RealVisualComponentDescriptor p_oDescriptor) {
		this.descriptor = p_oDescriptor;
	}
}
