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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.request;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * <p>RequestTime class.</p>
 *
 */
public class RequestTime {
	/**
	 * Current time in millis
	 */
	private long currentTime ;
	/**
	 * Timezone raw offset
	 */
	private long timezoneRawOffset ;
	/**
	 * Timezone display name
	 */
	private String timezoneName ;
	
	/**
	 * Definition of the mobile time
	 */
	public RequestTime() {
		this.currentTime = System.currentTimeMillis();
		TimeZone oTimeZone = Calendar.getInstance().getTimeZone();
		this.timezoneRawOffset = oTimeZone.getRawOffset() ;
		this.timezoneName = oTimeZone.getDisplayName() ;
	}
	/**
	 * Accessor of the current time in millis property
	 *
	 * @return current time in millis
	 */
	public long getCurrentTime() {
		return currentTime;
	}
	/**
	 * Accessor of the property name of the timezome
	 *
	 * @return name of the timezome
	 */
	public String getTimezoneName() {
		return timezoneName;
	}
	/**
	 * Accessor of the property raw offset of the timezome
	 *
	 * @return raw offset of the timezome
	 */
	public long getTimezoneRawOffset() {
		return timezoneRawOffset;
	}
}
