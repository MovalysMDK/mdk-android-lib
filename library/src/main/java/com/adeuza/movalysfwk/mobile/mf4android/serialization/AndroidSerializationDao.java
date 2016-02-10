package com.adeuza.movalysfwk.mobile.mf4android.serialization;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.prefs.Preferences;

import android.content.Context;

import com.adeuza.movalysfwk.mobile.mf4android.context.AndroidMMContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.serialization.AbstractSerializationDao;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.serialization.SerializationDao;

/**
 * <p>Android DAO used to serialize/deserialize an object.</p>
 *
 * <p>Copyright (c) 2010
 * <p>Company: Adeuza
 *
 * @author emalespine
 */
public class AndroidSerializationDao extends AbstractSerializationDao implements SerializationDao {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveString(final MContext p_oContext, final String p_sSerializationKey, final String p_sValue) {
		if (p_sValue == null || p_sValue.length() > Preferences.MAX_VALUE_LENGTH) {
			this.saveObject(p_oContext, p_sSerializationKey, p_sValue);
		}
		else {
			((AndroidMMContext) p_oContext).getSharedPreferences().edit().putString(p_sSerializationKey, p_sValue).commit();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String loadString(final MContext p_oContext, final String p_sSerializationKey) {
		String r_sValue = null;
		if(this.isSharedPreferenceExist(p_oContext, p_sSerializationKey)) {
			r_sValue = ((AndroidMMContext) p_oContext).getSharedPreferences().getString(p_sSerializationKey, null);
		}
		else if (this.isObjectExist(p_oContext, p_sSerializationKey)) {
			Object oValue = this.loadObject(p_oContext, p_sSerializationKey);
			if (String.class.isAssignableFrom(oValue.getClass())) {
				r_sValue = (String) oValue;
			}
		}

		return r_sValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected OutputStream getOutputStream(final MContext p_oContext, final String p_sSerializationKey) {
		try {
			return ((AndroidMMContext) p_oContext).getAndroidNativeContext().openFileOutput(p_sSerializationKey, Context.MODE_PRIVATE);
		}
		catch (FileNotFoundException e) {
			throw new MobileFwkException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected InputStream getInputStream(final MContext p_oContext, final String p_sSerializationKey) {
		try {
			return ((AndroidMMContext) p_oContext).getAndroidNativeContext().openFileInput(p_sSerializationKey);
		}
		catch (FileNotFoundException e) {
			throw new MobileFwkException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isObjectExist(final MContext p_oContext, final String p_sSerializationKey) {
		final Context oNativeContext = ((AndroidMMContext) p_oContext).getAndroidNativeContext();

		for (String sFileName : oNativeContext.fileList()) {
			if (sFileName.equals(p_sSerializationKey)) {
				return true;
			}
		}
		return false;
	}

	private boolean isSharedPreferenceExist(final MContext p_oContext, final String p_sSerializationKey) {
		return ((AndroidMMContext) p_oContext).getSharedPreferences().contains(p_sSerializationKey);
	}
	
//	/**
//	 * Stores a string. This string is associated to a key that allows reloading.
//	 * In this implementation, the android shared preferences are used to store the string value.
//	 * 
//	 * @param p_oContext
//	 * 		The current application context. Never null.
//	 * 
//	 * @param p_sSerializationKey
//	 * 		The key used to store and reload the string. Mandatory.
//	 * 
//	 * @param p_sValue
//	 * 		The value to store.
//	 * 
//	 * @see com.adeuza.movalys.fwk.mobile.javacommons.dao.serialization.AbstractSerializationDao#save(java.lang.String, java.lang.String)
//	 */
//	@Override
//	public void save(final MMContext p_oContext, final String p_sSerializationKey, final String p_sValue) {
//		((AndroidMMContext) p_oContext).getSharedPreferences().edit().putString(p_sSerializationKey, p_sValue).commit();
//	}
//
//	/**
//	 * Loads a string value using its serialization key.
//	 * In this implementation, the serialized value is loaded from the android shared preferences.
//	 * 
//	 * @param p_oContext
//	 * 		The current application context. Never null.
//	 * 
//	 * @param p_sSerializationKey
//	 * 		The key used to serialize the string.
//	 * 
//	 * @return
//	 * 		The saved string.
//	 * 
//	 * @see com.adeuza.movalys.fwk.mobile.javacommons.dao.serialization.AbstractSerializationDao#load(java.lang.String)
//	 */
//	@Override
//	public String load(final MMContext p_oContext, String p_sSerializationKey) {
//		return ((AndroidMMContext) p_oContext).getSharedPreferences().getString(p_sSerializationKey, null);
//	}

	/**
	 * Delete an object previously serialized in the shared preferences of the android application.
	 * 
	 * @param p_oContext
	 * 		The current application context. Never null.
	 * 
	 * @param p_sSerializationKey
	 * 		The serialization key used to retreive the serialized object. Mandatory.
	 *
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.dao.serialization.AbstractSerializationDao#delete(com.adeuza.MContext.fwk.mobile.javacommons.application.MMContext, java.lang.String)
	 */
	@Override
	public void delete(final MContext p_oContext, String p_sSerializationKey) {
		if (p_sSerializationKey != null) {
			AndroidMMContext oContext = ((AndroidMMContext) p_oContext);

			// If the setting is stored in shared preferences.
			oContext.getSharedPreferences().edit().remove(p_sSerializationKey).commit();

			// If the setting is stored in file.
			oContext.getAndroidNativeContext().deleteFile(p_sSerializationKey);
		}
	}
}
