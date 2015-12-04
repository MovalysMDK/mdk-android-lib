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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.invoker.okhttp;

import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import okio.BufferedSink;

/**
 * Writer to write into a BufferedSink
 */
public class BufferedSinkWriter extends Writer {

    /**
     * BufferedSink to write into
     */
    private BufferedSink bufferedSink;

    /**
     * Encoding
     */
    private String encoding;

    /**
     * Constructor
     *
     * @param p_oBufferedSink BufferedSink to write into
     * @param p_sEncoding encoding to use
     */
    public BufferedSinkWriter(BufferedSink p_oBufferedSink, String p_sEncoding) {
        this.bufferedSink = p_oBufferedSink;
        this.encoding = p_sEncoding;
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
    	this.bufferedSink.close();
    }

    /** {@inheritDoc} */
    @Override
    public void flush() throws IOException {
    	this.bufferedSink.flush();
    }

    /** {@inheritDoc} */
    @Override
    public void write(char[] p_cCharbuffer, int p_iOffset, int p_iCount) throws IOException {
        Charset latin1Charset = Charset.forName(this.encoding);
        ByteBuffer byteBuffer = latin1Charset.encode(CharBuffer.wrap(p_cCharbuffer));
        this.bufferedSink.write(byteBuffer.array(), p_iOffset, p_iCount);
    }
}

