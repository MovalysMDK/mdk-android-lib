package com.adeuza.movalysfwk.mobile.mf4android.configuration.loader;

import android.content.res.XmlResourceParser;

/**
 * <p>TODO Décrire la classe ConfigurationLoader</p>
 *
 * <p>Copyright (c) 2012
 * <p>Company: Adeuza
 *
 * @author emalespine
 *
 */

public interface ConfigurationLoader {
	
	public void load(XmlResourceParser p_oParser, String p_sConfigurationKey);
}
