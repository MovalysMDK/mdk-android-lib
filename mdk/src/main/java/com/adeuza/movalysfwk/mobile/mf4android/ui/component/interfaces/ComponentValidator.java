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
package com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.validator.IFormFieldValidator;

/**
 * Use in component to retrieve it's validator.
 * <p>
 * Each component that contains a validator should implement this interface. The interface 
 * method is call as a callback from the component delegate to get the form validator of a
 * component.
 * </p>
 * <p>This implementation has been choose to limit the methods count in android compiled
 * file</p>
 *
 */
public interface ComponentValidator {

	/**
	 * Retrieve the component validator
	 * @return form validator
	 */
	IFormFieldValidator getValidator();

}
