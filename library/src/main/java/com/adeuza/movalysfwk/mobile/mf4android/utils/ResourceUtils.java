package com.adeuza.movalysfwk.mobile.mf4android.utils;

import android.content.Context;

/**
 * Ressource Utils
 * 
 */
public final class ResourceUtils {
	/**
	 * Get the string for the given resrource name
	 */
	private ResourceUtils(){
		
	}
	/**
	 * Get resource by name
	 * @param p_sResourceName the name of the ressource
	 * @param p_oContext the current context
	 * @return the string of the given ressource
	 */
	public static String getStringResourceByName(String p_sResourceName, Context p_oContext ) {
		int iResId = p_oContext.getResources().getIdentifier(p_sResourceName, "string",
						p_oContext.getPackageName());
		return p_oContext.getString(iResId);
	}
}
