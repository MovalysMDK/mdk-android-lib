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
package com.adeuza.movalysfwk.mobile.mf4javacommons.event.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.adeuza.movalysfwk.mobile.mf4javacommons.event.BusinessEvent;

/**
 * Annotation which allows to listen to a business event send by the controller
 * @param Class&lt;? extends BusinessEvent&lt;?&gt;&gt; Class of the BusinessEvent
 * @param Class&lt;?&gt;[] Classes to filter 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ListenerOnBusinessNotification {

	/**
	 * <p>value.</p>
	 *
	 * @return a {@link java.lang.Class} object.
	 */
	public Class<? extends BusinessEvent<?>> value();
	
	/**
	 * Class listener filter
	 */
	public Class<?>[] classFilters() default {};
}
