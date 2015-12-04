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
package com.adeuza.movalysfwk.apache.beanutils.converters;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * {@link DateTimeConverter} implementation that handles conversion to
 * and from <b>java.sql.Time</b> objects.
 * <p>
 * This implementation can be configured to handle conversion either
 * by using java.sql.Time's default String conversion, or by using a
 * Locale's default format or by specifying a set of format patterns.
 * See the {@link DateTimeConverter} documentation for further details.
 * <p>
 * Can be configured to either return a <i>default value</i> or throw a
 * <code>ConversionException</code> if a conversion error occurs.
 *
 * @author Craig R. McClanahan
 * @version $Revision: 690380 $ $Date: 2008-08-29 21:04:38 +0100 (Fri, 29 Aug 2008) $
 * @since 1.3
 */
public final class SqlTimeConverter extends DateTimeConverter {

    /**
     * Construct a <b>java.sql.Time</b> <i>Converter</i> that throws
     * a <code>ConversionException</code> if an error occurs.
     */
    public SqlTimeConverter() {
        super();
    }

    /**
     * Construct a <b>java.sql.Time</b> <i>Converter</i> that returns
     * a default value if an error occurs.
     *
     * @param defaultValue The default value to be returned
     * if the value to be converted is missing or an error
     * occurs converting the value.
     */
    public SqlTimeConverter(Object defaultValue) {
        super(defaultValue);
    }

    /**
     * Return the default type this <code>Converter</code> handles.
     *
     * @return The default type this <code>Converter</code> handles.
     * @since 1.8.0
     */
    @Override
	protected Class getDefaultType() {
        return Time.class;
    }

    /**
     * Return a <code>DateFormat<code> for the Locale.
     * @param locale TODO
     * @param timeZone TODO
     *
     * @return The DateFormat.
     * @since 1.8.0
     */
    @Override
	protected DateFormat getFormat(Locale locale, TimeZone timeZone) {
        DateFormat format = null;
        if (locale == null) {
            format = DateFormat.getTimeInstance(DateFormat.SHORT);
        } else {
            format = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
        }
        if (timeZone != null) {
            format.setTimeZone(timeZone);
        }
        return format;
    }

}
