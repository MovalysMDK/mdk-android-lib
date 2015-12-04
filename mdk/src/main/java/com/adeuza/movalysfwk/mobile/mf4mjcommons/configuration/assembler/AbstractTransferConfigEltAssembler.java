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

import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>Abstract class of objects that transform an element of a remote configuration to an element of the local configuration.</p>
 *
 *
 * @param <IN> Type of some elements of the local configuration
 * @param <OUT> Type of some elements of the remote configuration
 */
public abstract class AbstractTransferConfigEltAssembler<IN, OUT> {

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
	 * @return <code>null</code>, if <em>p_oTransferObjects</em> is null, An empty collection if <em>p_oTransferObjets</em> is empty,
	 *  a collection where each element is the local representation of an element of the remote configuration (at the same index). 
	 */
	public Collection<IN> toBusinessObject(final Collection<OUT> p_oTransferObjects) {
		Collection<IN> r_oBusinessObjects = null;
		if (p_oTransferObjects != null) {
			r_oBusinessObjects = new ArrayList<IN>();
			for (OUT oTransferObject : p_oTransferObjects) {
				r_oBusinessObjects.add(this.toBusinessObject(oTransferObject));
			}
		}
		return r_oBusinessObjects;
	}

	/**
	 * <p>Transforms an element of the remote configuration to matching element of the local configuration.</p>
	 *
	 * @param p_oTransferObject
	 * 		The remote element to transform. This parameter must not be <code>null</code>.
	 * @return The local representation of the remote configuration. Never <code>null</code>.
	 */
	public abstract IN toBusinessObject(final OUT p_oTransferObject);
}
