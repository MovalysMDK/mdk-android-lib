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
package com.adeuza.movalysfwk.apache.beanutils;



/**
 * <p>General purpose data type converter that can be registered and used
 * within the BeanUtils package to manage the conversion of objects from
 * one type to another.</p>
 *
 * <p>Converter subclasses bundled with the BeanUtils library are required
 * to be thread-safe, as users of the library may call conversion methods
 * from more than one thread simultaneously.</p>
 *
 * <p>Custom converter subclasses created by users of the library can be
 * non-thread-safe if the application using them is single-threaded. However
 * it is recommended that they be written in a thread-safe manner anyway.</p> 
 *
 * @author Craig McClanahan
 * @author Paulo Gaspar
 * @version $Revision: 555824 $ $Date: 2007-07-13 01:27:15 +0100 (Fri, 13 Jul 2007) $
 * @since 1.3
 */

public interface Converter {


    /**
     * Convert the specified input object into an output object of the
     * specified type.
     *
     * @param type Data type to which this value should be converted
     * @param value The input value to be converted
     * @return The converted value
     *
     * @exception ConversionException if conversion cannot be performed
     *  successfully
     */
    public Object convert(Class type, Object value);


}
