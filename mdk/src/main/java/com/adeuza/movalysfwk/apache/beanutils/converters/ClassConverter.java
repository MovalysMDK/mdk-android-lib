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

/**
 * {@link org.apache.commons.beanutils.Converter} implementaion that handles conversion
 * to and from <b>java.lang.Class</b> objects.
 * <p>
 * The class will be loaded from the thread context class
 * loader (if it exists); otherwise the class loader that loaded this class
 * will be used.
 * <p>
 * Can be configured to either return a <i>default value</i> or throw a
 * <code>ConversionException</code> if a conversion error occurs.
 *
 * @author Tomas Viberg
 * @version $Revision: 690380 $ $Date: 2008-08-29 21:04:38 +0100 (Fri, 29 Aug 2008) $
 * @since 1.4
 */
public final class ClassConverter extends AbstractConverter {

    /**
     * Construct a <b>java.lang.Class</b> <i>Converter</i> that throws
     * a <code>ConversionException</code> if an error occurs.
     */
    public ClassConverter() {
        super();
    }

    /**
     * Construct a <b>java.lang.Class</b> <i>Converter</i> that returns
     * a default value if an error occurs.
     *
     * @param defaultValue The default value to be returned
     * if the value to be converted is missing or an error
     * occurs converting the value.
     */
    public ClassConverter(Object defaultValue) {
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
        return Class.class;
    }

    /**
     * <p>Convert a java.lang.Class or object into a String.</p>
     *
     * @param value The input value to be converted
     * @return the converted String value.
     * @since 1.8.0
     */
    @Override
	protected String convertToString(Object value) {
        return (value instanceof Class) ? ((Class)value).getName() : value.toString();
    }

    /**
     * <p>Convert the input object into a java.lang.Class.</p>
     *
     * @param type Data type to which this value should be converted.
     * @param value The input value to be converted.
     * @return The converted value.
     * @throws Throwable if an error occurs converting to the specified type
     * @since 1.8.0
     */
    @Override
	protected Object convertToType(Class type, Object value) throws Throwable {
        ClassLoader classLoader =
            Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            try {
                return (classLoader.loadClass(value.toString()));
            } catch (ClassNotFoundException ex) {
                // Don't fail, carry on and try this class's class loader
                // (see issue# BEANUTILS-263)
            }
        }

        // Try this class's class loader
        classLoader = ClassConverter.class.getClassLoader();
        return (classLoader.loadClass(value.toString()));
    }

}
