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
package com.adeuza.movalysfwk.apache.codec;

/**
 * <p>Provides the highest level of abstraction for Decoders.
 * This is the sister interface of {@link Encoder}.  All
 * Decoders implement this common generic interface.</p>
 * 
 * <p>Allows a user to pass a generic Object to any Decoder 
 * implementation in the codec package.</p>
 * 
 * <p>One of the two interfaces at the center of the codec package.</p>
 * 
 * @author Apache Software Foundation
 * @version $Id: Decoder.java,v 1.9 2004/02/29 04:08:31 tobrien Exp $
 */
public interface Decoder {

    /**
     * Decodes an "encoded" Object and returns a "decoded"
     * Object.  Note that the implementation of this
     * interface will try to cast the Object parameter
     * to the specific type expected by a particular Decoder
     * implementation.  If a {@link java.lang.ClassCastException} occurs
     * this decode method will throw a DecoderException.
     * 
     * @param pObject an object to "decode"
     * 
     * @return a 'decoded" object
     * 
     * @throws DecoderException a decoder exception can
     * be thrown for any number of reasons.  Some good
     * candidates are that the parameter passed to this
     * method is null, a param cannot be cast to the
     * appropriate type for a specific encoder.
     */
    Object decode(Object pObject) throws DecoderException;
}  

