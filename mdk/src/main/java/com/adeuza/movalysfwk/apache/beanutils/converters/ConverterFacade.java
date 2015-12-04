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

import com.adeuza.movalysfwk.apache.beanutils.Converter;


/**
 * Provides a facade for {@link Converter} implementations
 * preventing access to any public API in the implementation,
 * other than that specified by {@link Converter}.
 * <p />
 * This implementation can be used to prevent registered {@link Converter}
 * implementations that provide configuration options from being
 * retrieved and modified.
 *
 * @version $Revision: 552084 $ $Date: 2007-06-30 04:04:13 +0100 (Sat, 30 Jun 2007) $
 * @since 1.8.0
 */
public final class ConverterFacade implements Converter {

    private final Converter converter;

    /**
     * Construct a converter which delegates to the specified
     * {@link Converter} implementation.
     *
     * @param converter The converter to delegate to
     */
    public ConverterFacade(Converter converter) {
        if (converter == null) {
            throw new IllegalArgumentException("Converter is missing");
        }
        this.converter = converter;
    }

    /**
     * Convert the input object into an output object of the
     * specified type by delegating to the underlying {@link Converter}
     * implementation.
     *
     * @param type Data type to which this value should be converted
     * @param value The input value to be converted
     * @return The converted value.
     */
    @Override
	public Object convert(Class type, Object value) {
        return converter.convert(type, value);
    }

    /**
     * Provide a String representation of this facade implementation
     * sand the underlying {@link Converter} it delegates to.
     *
     * @return A String representation of this facade implementation
     * sand the underlying {@link Converter} it delegates to
     */
    @Override
	public String toString() {
        return "ConverterFacade[" + converter.toString() + "]";
    }

}
