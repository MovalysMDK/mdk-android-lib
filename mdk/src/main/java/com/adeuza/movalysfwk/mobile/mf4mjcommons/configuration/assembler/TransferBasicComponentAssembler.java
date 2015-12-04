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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.assembler;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.visual.TComponentGraphConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;

/**
 * <p>Provides conversion of remote configuration of fields into local configuration</p>
 *
 *
 */
public class TransferBasicComponentAssembler
		extends AbstractTransferConfigEltAssembler<BasicComponentConfiguration, TComponentGraphConfiguration>
		implements TransferConfigEltAssembler<BasicComponentConfiguration, TComponentGraphConfiguration> {

	/**
	 * Default constructor: only memory allocation.
	 */
	public TransferBasicComponentAssembler() {
		// NOTHING TO DO
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>Transforms an element of the remote configuration to matching element of the local configuration.</p>
	 */
	@Override
	public BasicComponentConfiguration toBusinessObject(final TComponentGraphConfiguration p_oTransferObject) {
		return new BasicComponentConfiguration(p_oTransferObject.getName(), p_oTransferObject.getLabel());
	}

	/**
	 * {@inheritDoc}
	 *
	 * Merges a remote configuration with the local matching  configuration.
	 */
	@Override
	public void merge(TComponentGraphConfiguration p_oSource, BasicComponentConfiguration p_oTarget) {
		p_oTarget.setVisible(p_oSource.isVisible());
		p_oTarget.setLabel(p_oSource.getLabel());
	}
}
