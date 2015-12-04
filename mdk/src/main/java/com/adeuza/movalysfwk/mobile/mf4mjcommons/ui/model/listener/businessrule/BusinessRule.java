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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.listener.businessrule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>BusinessRule class.</p>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface BusinessRule {
	
	/**
	 * <p>
	 * The name ("key") of the business rule. 
	 * </p>
	 * @deprecated @see #name() attribute (replace key)
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@Deprecated
	public String key() default "";
	
	/**
	 * <p>
	 * The name of the business rule. 
	 * </p>
	 * 
	 * @return a {@link java.lang.String} representing the name of the BusinessRule.
	 */
	public String name() default "";
	/**
	 * <p>propertyTarget.</p>
	 *
	 * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.listener.businessrule.PropertyTarget} object.
	 */
	public PropertyTarget propertyTarget();
	/**
	 * <p>fields.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] fields();
	/**
	 * <p>triggers.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] triggers() default {};
}
