package com.adeuza.movalysfwk.mobile.mf4android.application;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.R;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.DefaultApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.RunInitError;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ErrorDefinition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;

/**
 * <p>TODO Décrire la classe CheckApplicationRInit</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @author emalespine
 *
 */
public class AndroidRLoader {

	private static final Class<?>[] CONSTANTS = {DefaultApplicationR.class, AndroidApplicationR.class};

	/** link between android id and text id for a visual component */
	private SparseArrayCompat<String> androidIdInt2String;

	/** link between android id and text id for a visual component */
	private Map<String, Integer> androidIdString2Int;

	private Class<?> androidRlayout;

	private String mainPackage;

	private static final int ANDROID_ID_COUNT = 2048 ;
	/**
	 * Default constructor. Initialize the attributes.
	 */
	public AndroidRLoader(String p_sMainPackage) {
		this.androidIdInt2String	= new SparseArrayCompat<>(ANDROID_ID_COUNT);
		this.androidIdString2Int	= new HashMap<>(ANDROID_ID_COUNT);
		this.mainPackage			= p_sMainPackage;
	}

	/**
	 * Loads all constants of {@link android.R} and checks that each defined ApplicationR matches an existing constant into {@link android.R}
	 */
	public void loadAndCheck() {
		this.load();
		this.check();
	}

	/**
	 * Loads all constants of {@link android.R} into maps.
	 */
	public void load() {
		this.androidIdInt2String.clear();
		this.androidIdString2Int.clear();

		try {
			Class<?> oClazzR = Class.forName(this.mainPackage + ".R");
			Class<?> oClazzToTreat = null;
			for(ApplicationRGroup oRGroup : ApplicationRGroup.class.getEnumConstants()) {
				for (Class<?> oTemp : oClazzR.getDeclaredClasses()) {
					if (oTemp.getSimpleName().equals(oRGroup.getR())) {
						oClazzToTreat = oTemp;
						break;
					}
				}
				if (oClazzToTreat!=null) {
					this.analyseClassForFindId(oRGroup, oClazzToTreat);
					if (oRGroup.equals(ApplicationRGroup.LAYOUT)) {
						this.androidRlayout = oClazzToTreat;
					}
				}
				oClazzToTreat = null;
			}
		}
		catch (ClassNotFoundException e) {
			throw new MobileFwkException(e);
		}
	}

	/**
	 * Checks that each defined ApplicationR matches an existing constant into {@link android.R}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.application.RunInit#run(com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MMContext)
	 */
	public void check() {
		boolean r_bAllIsOk = true;
		for (Class<?> oApplicationR : CONSTANTS) {
			r_bAllIsOk = this.check(oApplicationR) && r_bAllIsOk;
		}

		if (!r_bAllIsOk) {
			throw new RunInitError(); 
		}
	}

	/**
	 * Checks that each defined ApplicationR of an enumeration matches an existing constant into {@link android.R}
	 * @param p_oApplicationR An enumeration of ApplicationR
	 */
	@SuppressWarnings("unchecked")
	private boolean check(Class<?> p_oApplicationR) {
		boolean r_bAllIsOk = true;
		if (ApplicationR.class.isAssignableFrom(p_oApplicationR)) {
			StringBuilder oErrorMessage = null;
			for (ApplicationR oEnumItem : ((Class<? extends ApplicationR>) p_oApplicationR).getEnumConstants()) {
				if (!this.androidIdString2Int.containsKey(oEnumItem.getGroup().getKey().concat(oEnumItem.getKey()))) {
					r_bAllIsOk = false;

					oErrorMessage = new StringBuilder();
					oErrorMessage.append(ErrorDefinition.FWK_MOBILE_E_0004_LABEL);
					oErrorMessage.append(R.class.getName());
					oErrorMessage.append('.');
					oErrorMessage.append(oEnumItem.getGroup().getR());
					oErrorMessage.append('.');
					oErrorMessage.append(oEnumItem.getKey());

					Log.e(ErrorDefinition.FWK_MOBILE_E_0004, oErrorMessage.toString());
				}
			}
		}
		return r_bAllIsOk;
	}

	/**
	 * Analyse class for find id
	 * @param p_oClass
	 *            the class to analyse
	 * @param p_sKey key to use
	 */
	private void analyseClassForFindId(ApplicationRGroup p_sKey, Class<?> p_oClass) {
		try {
			String sKey = null;
			int iValue = 0;
			for (Field oField : p_oClass.getFields()) {
				sKey = oField.getName();
				if (oField.getType().isPrimitive()) {
					iValue = oField.getInt(null);
					this.androidIdInt2String.put(iValue, sKey); // un entier pour une seule chaîne
					this.androidIdString2Int.put(p_sKey.getKey() + sKey, iValue); // mais plusieurs chaîne pour un entier d'où la clé supplémentaire
				}
			}
		} catch (IllegalArgumentException e) {
			throw new MobileFwkException(e);
		} catch (IllegalAccessException e) {
			throw new MobileFwkException(e);
		}
	}
	public SparseArrayCompat<String> getAndroidIdInt2String() {
		return this.androidIdInt2String;
	}
	public Map<String, Integer> getAndroidIdString2Int() {
		return this.androidIdString2Int;
	}
	public Class<?> getAndroidRlayout() {
		return this.androidRlayout;
	}

}
