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


import java.util.List;

import com.adeuza.movalysfwk.apache.beanutils.ConversionException;


/**
 * <p>Standard {@link org.apache.commons.beanutils.Converter} implementation that converts an incoming
 * String into a primitive array of long.  On a conversion failure, returns
 * a specified default value or throws a {@link ConversionException} depending
 * on how this instance is constructed.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 556229 $ $Date: 2007-07-14 07:11:19 +0100 (Sat, 14 Jul 2007) $
 * @since 1.4
 * @deprecated Replaced by the new {@link ArrayConverter} implementation
 */

public final class LongArrayConverter extends AbstractArrayConverter {


    // ----------------------------------------------------------- Constructors


    /**
     * Create a {@link org.apache.commons.beanutils.Converter} that will throw
     * a {@link ConversionException} if a conversion error occurs.
     */
    public LongArrayConverter() {

        this.defaultValue = null;
        this.useDefault = false;

    }


    /**
     * Create a {@link org.apache.commons.beanutils.Converter} that will return
     * the specified default value if a conversion error occurs.
     *
     * @param defaultValue The default value to be returned
     */
    public LongArrayConverter(Object defaultValue) {

        this.defaultValue = defaultValue;
        this.useDefault = true;

    }


    // ------------------------------------------------------- Static Variables


    /**
     * <p>Model object for type comparisons.</p>
     */
    private static final long[] MODEL = new long[0];


    // --------------------------------------------------------- Public Methods


    /**
     * Convert the specified input object into an output object of the
     * specified type.
     *
     * @param type Data type to which this value should be converted
     * @param value The input value to be converted
     * @return the converted value
     *
     * @exception ConversionException if conversion cannot be performed
     *  successfully
     */
    @Override
	public Object convert(Class type, Object value) {

        // Deal with a null value
        if (value == null) {
            if (useDefault) {
                return (defaultValue);
            } else {
                throw new ConversionException("No value specified");
            }
        }

        // Deal with the no-conversion-needed case
        if (MODEL.getClass() == value.getClass()) {
            return (value);
        }

        // Deal with input value as a String array
        if (strings.getClass() == value.getClass()) {
            try {
                String[] values = (String[]) value;
                long[] results = new long[values.length];
                for (int i = 0; i < values.length; i++) {
                    results[i] = Long.parseLong(values[i]);
                }
                return (results);
            } catch (Exception e) {
                if (useDefault) {
                    return (defaultValue);
                } else {
                    throw new ConversionException(value.toString(), e);
                }
            }
        }

        // Parse the input value as a String into elements
        // and convert to the appropriate type
        try {
            List list = parseElements(value.toString());
            long[] results = new long[list.size()];
            for (int i = 0; i < results.length; i++) {
                results[i] = Long.parseLong((String) list.get(i));
            }
            return (results);
        } catch (Exception e) {
            if (useDefault) {
                return (defaultValue);
            } else {
                throw new ConversionException(value.toString(), e);
            }
        }

    }


}
