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

import java.util.Collection;

/**
 * <p>Interface of classes that provides conversion of remote configuration into local configuration.</p>
 *
 * @param <IN> Type of the local configuration managed by this assembler.
 * @param <OUT> Type of the remote configuration manager by this assembler.
 */
public interface TransferConfigEltAssembler<IN, OUT> {

	/**
	 * <p>Transforms elements of the remote configuration to matching elements of the local configuration.</p>
	 *
	 * <p>
	 * 	This method returns:
	 * </p>
	 *
	 * <ul>
	 * 	<li><code>null</code>, if <em>p_oTransferObjects</em> is null,</li>
	 * 	<li>An empty collection if <em>p_oTransferObjets</em> is empty,</li>
	 * 	<li>A collection where each element is the local representation of an element of the remote configuration (at the same index).
	 * </ul>
	 *
	 * @param p_oTransferObjects
	 * 		The remote elements to transforms.
	 * @return 	null if <em>p_oTransferObjects</em> is null, an empty collection if <em>p_oTransferObjets</em> is empty,
	 * 	a collection where each element is the local representation of an element of the remote configuration (at the same index).
	 */
	public Collection<IN> toBusinessObject(Collection<OUT> p_oTransferObjects);

	/**
	 * <p>Transforms an element of the remote configuration to matching element of the local configuration.</p>
	 *
	 * @param p_oTransferObject The remote element to transform. This parameter must not be <code>null</code>.
	 * @return The local representation of the remote configuration. Never <code>null</code>.
	 */
	public IN toBusinessObject(OUT p_oTransferObject);

	/**
	 * Merges a remote configuration with the local matching  configuration.
	 *
	 * @param p_oSource The remote version of the configuration. Never null.
	 * @param p_oTarget The local version of the configuration. Never null.
	 */
	public void merge(OUT p_oSource, IN p_oTarget);
}
