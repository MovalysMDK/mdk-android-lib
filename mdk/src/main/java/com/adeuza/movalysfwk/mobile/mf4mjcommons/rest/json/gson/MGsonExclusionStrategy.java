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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.json.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * <p>MGsonExclusionStrategy class.</p>
 *
 */
public class MGsonExclusionStrategy implements ExclusionStrategy {

	/** {@inheritDoc} */
	@Override
	public boolean shouldSkipClass(Class<?> p_oClass) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean shouldSkipField(FieldAttributes p_oField) {
		return p_oField.getAnnotation(Unexposed.class) != null;
	}
}
