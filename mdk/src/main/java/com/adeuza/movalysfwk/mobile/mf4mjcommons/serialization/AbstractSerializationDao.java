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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;

/**
 * <p>Abstract class of dao used to serialize/deserialize object.</p>
 *
 *
 */
public abstract class AbstractSerializationDao {

	/**
	 * Serializes an object.
	 *
	 * @param p_oContext
	 * 		Current context. Never null.
	 * @param p_sSerializationKey
	 * 		Serialization key. Mandatory.
	 * @param p_oValue
	 * 		Object to serialize
	 */
	public void saveObject(final MContext p_oContext, final String p_sSerializationKey, final Object p_oValue) {
		try {
			if (p_oValue != null) {
				ObjectOutputStream oOutputStream = new ObjectOutputStream(this.getOutputStream(p_oContext, p_sSerializationKey));
				oOutputStream.writeObject(p_oValue);
				oOutputStream.close();
			}
		}
		catch (IOException e) {
			throw new MobileFwkException(e);
		}
	}

	/**
	 * Deserializes an object.
	 *
	 * @param p_oContext
	 * 		Current context. Never null.
	 * @param p_sSerializationKey
	 * 		Serialization key. Mandatory.
	 * @return The serialized object or null if it does not exist.
	 */
	public Object loadObject(final MContext p_oContext, final String p_sSerializationKey) {
		Object r_oSerializedObject = null;
		try {
			if (this.isObjectExist(p_oContext, p_sSerializationKey)) {
				ObjectInputStream oInputStream = new ObjectInputStream(this.getInputStream(p_oContext, p_sSerializationKey));
				r_oSerializedObject = oInputStream.readObject();
				oInputStream.close();
			}
		}
		catch(IOException e) {
			throw new MobileFwkException(e);
		}
		catch (ClassNotFoundException e) {
			throw new MobileFwkException(e);
		}

		return r_oSerializedObject;
	}

	/**
	 * <p>getOutputStream.</p>
	 *
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 * @param p_sSerializationKey a {@link java.lang.String} object.
	 * @return a {@link java.io.OutputStream} object.
	 */
	protected abstract OutputStream getOutputStream(final MContext p_oContext, final String p_sSerializationKey);

	/**
	 * <p>getInputStream.</p>
	 *
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 * @param p_sSerializationKey a {@link java.lang.String} object.
	 * @return a {@link java.io.InputStream} object.
	 */
	protected abstract InputStream getInputStream(final MContext p_oContext, final String p_sSerializationKey);

	/**
	 * <p>isObjectExist.</p>
	 *
	 * @param p_oContext a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext} object.
	 * @param p_sSerializationKey a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	protected abstract boolean isObjectExist(final MContext p_oContext, final String p_sSerializationKey);

	/**
	 * Delete an object previously serialized.
	 *
	 * @param p_oContext
	 * 		The current application context. Never null.
	 * @param p_sSerializationKey
	 * 		The serialization key used to retreive the serialized object. Mandatory.
	 */
	public abstract void delete(final MContext p_oContext, final String p_sSerializationKey);
}
