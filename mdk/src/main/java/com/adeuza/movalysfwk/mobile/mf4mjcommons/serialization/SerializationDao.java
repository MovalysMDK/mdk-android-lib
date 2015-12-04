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

import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * <p>Interface of all DAO used to serialize/deserialize object.</p>
 *
 *
 */
public interface SerializationDao {

	/**
	 * Serializes an object identified by an key and store it.
	 *
	 * @param p_oContext
	 * 		The current application context. Never null.
	 * @param p_sSerializationKey
	 * 		The serialization key. Mandatory.
	 * @param p_oValue
	 * 		The object to serialize.
	 */
	public void saveObject(final MContext p_oContext, final String p_sSerializationKey, final Object p_oValue);

	/**
	 * Stores a string identified by an key.
	 *
	 * @param p_oContext
	 * 		The current application context. Never null.
	 * @param p_sSerializationKey
	 * 		The serialization key. Mandatory.
	 * @param p_sValue
	 * 		The string to store.
	 */
	public void saveString(final MContext p_oContext, final String p_sSerializationKey, final String p_sValue);

	/**
	 * Loads an object, previously serialized, identified by the provided serialization key.
	 * This method returns an null value if the serialization key is not associated
	 * to a serialized object.
	 *
	 * <p>
	 * 	This method throws a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException} when the specified type does
	 * 	not matched the type of the serialized object.
	 * </p>
	 *
	 * @param p_oContext
	 * 		The current application context. Never null.
	 * @param p_sSerializationKey
	 * 		The serialization key used to retreive the serialized object. Mandatory.
	 * @return The deserialized object or null if the serialization is not associated to serialized object.
	 */
	public Object loadObject(final MContext p_oContext, final String p_sSerializationKey);

	/**
	 * Loads a string, previously serialized, identified by the provided serialization key.
	 * This method returns an null value if the serialization key is not associated
	 * to a serialized object.
	 *
	 * <p>
	 * 	This method throws a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException} when the specified type does
	 * 	not matched the type of the serialized object.
	 * </p>
	 *
	 * @param p_oContext
	 * 		The current application context. Never null.
	 * @param p_sSerializationKey
	 * 		The serialization key used to retreive the serialized object. Mandatory.
	 * @return The deserialized object or null if the serialization is not associated to serialized object.
	 */
	public String loadString(final MContext p_oContext, final String p_sSerializationKey);

	/**
	 * Delete an object previously serialized.
	 *
	 * @param p_oContext
	 * 		The current application context. Never null.
	 * @param p_sSerializationKey
	 * 		The serialization key used to retreive the serialized object. Mandatory.
	 */
	public void delete(final MContext p_oContext, final String p_sSerializationKey);
}
