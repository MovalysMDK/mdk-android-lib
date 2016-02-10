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


import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.apache.beanutils.ConversionException;
import com.adeuza.movalysfwk.apache.beanutils.Converter;




/**
 * <p>Convenience base class for converters that translate the String
 * representation of an array into a corresponding array of primitives
 * object.  This class encapsulates the functionality required to parse
 * the String into a list of String elements that can later be
 * individually converted to the appropriate primitive type.</p>
 *
 * <p>The input syntax accepted by the <code>parseElements()</code> method
 * is designed to be compatible with the syntax used to initialize arrays
 * in a Java source program, except that only String literal values are
 * supported.  For maximum flexibility, the surrounding '{' and '}'
 * characters are optional, and individual elements may be separated by
 * any combination of whitespace and comma characters.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 690380 $ $Date: 2008-08-29 21:04:38 +0100 (Fri, 29 Aug 2008) $
 * @since 1.4
 * @deprecated Replaced by the new {@link ArrayConverter} implementation
 */

public abstract class AbstractArrayConverter implements Converter {


    // ----------------------------------------------------------- Constructors


    /**
     * Create a {@link Converter} that will throw a {@link ConversionException}
     * if a conversion error occurs.
     */
    public AbstractArrayConverter() {

        this.defaultValue = null;
        this.useDefault = false;

    }

    /**
     * Create a {@link Converter} that will return the specified default value
     * if a conversion error occurs.
     *
     * @param defaultValue The default value to be returned
     * @since 1.8.0
     */
    public AbstractArrayConverter(Object defaultValue) {

        if (defaultValue == NO_DEFAULT) {
            this.useDefault = false;
        } else {
            this.defaultValue = defaultValue;
            this.useDefault = true;
        }

    }

    // ------------------------------------------------------- Static Variables

    /**
     * This is a special reference that can be passed as the "default object"
     * to the constructor to indicate that no default is desired. Note that
     * the value 'null' cannot be used for this purpose, as the caller may
     * want a null to be returned as the default.
     * @since 1.8.0
     */
    public static final Object NO_DEFAULT = new Object();

    // ----------------------------------------------------- Instance Variables


    /**
     * <p>Model object for string arrays.</p>
     */
    protected static String[] strings = new String[0];


    /**
     * The default value specified to our Constructor, if any.
     */
    protected Object defaultValue = null;


    /**
     * Should we return the default value on conversion errors?
     */
    protected boolean useDefault = true;


    // --------------------------------------------------------- Public Methods


    /**
     * Convert the specified input object into an output object of the
     * specified type.  This method must be implemented by a concrete
     * subclass.
     *
     * @param type Data type to which this value should be converted
     * @param value The input value to be converted
     * @return The converted value
     *
     * @exception ConversionException if conversion cannot be performed
     *  successfully
     */
    @Override
	public abstract Object convert(Class type, Object value);


    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Parse an incoming String of the form similar to an array initializer
     * in the Java language into a <code>List</code> individual Strings
     * for each element, according to the following rules.</p>
     * <ul>
     * <li>The string is expected to be a comma-separated list of values.</li>
     * <li>The string may optionally have matching '{' and '}' delimiters
     *   around the list.</li>
     * <li>Whitespace before and after each element is stripped.</li>
     * <li>Elements in the list may be delimited by single or double quotes.
     *  Within a quoted elements, the normal Java escape sequences are valid.</li>
     * </ul>
     *
     * @param svalue String value to be parsed
     * @return The parsed list of String values
     *
     * @exception ConversionException if the syntax of <code>svalue</code>
     *  is not syntactically valid
     * @exception NullPointerException if <code>svalue</code>
     *  is <code>null</code>
     */
    protected List parseElements(String svalue) {

        // Validate the passed argument
        if (svalue == null) {
            throw new NullPointerException();
        }

        // Trim any matching '{' and '}' delimiters
        svalue = svalue.trim();
        if (svalue.startsWith("{") && svalue.endsWith("}")) {
            svalue = svalue.substring(1, svalue.length() - 1);
        }

        try {

            // Set up a StreamTokenizer on the characters in this String
            StreamTokenizer st =
                new StreamTokenizer(new StringReader(svalue));
            st.whitespaceChars(',',','); // Commas are delimiters
            st.ordinaryChars('0', '9');  // Needed to turn off numeric flag
            st.ordinaryChars('.', '.');
            st.ordinaryChars('-', '-');
            st.wordChars('0', '9');      // Needed to make part of tokens
            st.wordChars('.', '.');
            st.wordChars('-', '-');

            // Split comma-delimited tokens into a List
            ArrayList list = new ArrayList();
            while (true) {
                int ttype = st.nextToken();
                if ((ttype == StreamTokenizer.TT_WORD) ||
                    (ttype > 0)) {
                    list.add(st.sval);
                } else if (ttype == StreamTokenizer.TT_EOF) {
                    break;
                } else {
                    throw new ConversionException
                        ("Encountered token of type " + ttype);
                }
            }

            // Return the completed list
            return (list);

        } catch (IOException e) {

            throw new ConversionException(e);

        }



    }


}
