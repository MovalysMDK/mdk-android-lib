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
 * Standard {@link org.apache.commons.beanutils.Converter} implementation that converts an incoming
 * String into an array of String objects. On a conversion failure, returns
 * a specified default value or throws a {@link ConversionException} depending
 * on how this instance is constructed.
 * <p>
 * There is also some special handling where the input is of type int[].
 * See method convert for more details.  
 *
 * @author Craig R. McClanahan
 * @version $Revision: 556229 $ $Date: 2007-07-14 07:11:19 +0100 (Sat, 14 Jul 2007) $
 * @since 1.4
 * @deprecated Replaced by the new {@link ArrayConverter} implementation
 */

public final class StringArrayConverter extends AbstractArrayConverter {


    // ----------------------------------------------------------- Constructors


    /**
     * Create a {@link org.apache.commons.beanutils.Converter} that will throw
     * a {@link ConversionException} if a conversion error occurs.
     */
    public StringArrayConverter() {

        this.defaultValue = null;
        this.useDefault = false;

    }


    /**
     * Create a {@link org.apache.commons.beanutils.Converter} that will return
     * the specified default value if a conversion error occurs.
     *
     * @param defaultValue The default value to be returned
     */
    public StringArrayConverter(Object defaultValue) {

        this.defaultValue = defaultValue;
        this.useDefault = true;

    }


    // ------------------------------------------------------- Static Variables


    /**
     * <p>Model object for type comparisons.</p>
     */
    private static final String[] MODEL = new String[0];
    
    /**
     * <p> Model object for int arrays.</p>
     */
    private static final int[] INT_MODEL = new int[0];



    // --------------------------------------------------------- Public Methods


    /**
     * Convert the specified input object into an output object of the
     * specified type.
     * <p>
     * If the value is already of type String[] then it is simply returned
     * unaltered.
     * <p>
     * If the value is of type int[], then a String[] is returned where each
     * element in the string array is the result of calling Integer.toString
     * on the corresponding element of the int array. This was added as a
     * result of bugzilla request #18297 though there is not complete
     * agreement that this feature should have been added. 
     * <p>
     * In all other cases, this method calls toString on the input object, then
     * assumes the result is a comma-separated list of values. The values are 
     * split apart into the individual items and returned as the elements of an
     * array. See class AbstractArrayConverter for the exact input formats
     * supported.
     * 
     * @param type is the data type to which this value should be converted.
     * It is expected to be the class for type String[] (though this parameter
     * is actually ignored by this method).
     * 
     * @param value is the input value to be converted. If null then the
     * default value is returned or an exception thrown if no default value
     * exists.
     * @return the converted value
     *
     * @exception ConversionException if conversion cannot be performed
     * successfully, or the input is null and there is no default value set
     * for this object.
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

        // Deal with the input value as an int array
        if (INT_MODEL.getClass() == value.getClass())
        {
            int[] values = (int[]) value;
            String[] results = new String[values.length];
            for (int i = 0; i < values.length; i++)
            {
                results[i] = Integer.toString(values[i]);
            }

            return (results);
        }

        // Parse the input value as a String into elements
        // and convert to the appropriate type
        try {
            List list = parseElements(value.toString());
            String[] results = new String[list.size()];
            for (int i = 0; i < results.length; i++) {
                results[i] = (String) list.get(i);
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
