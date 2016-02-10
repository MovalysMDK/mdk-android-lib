package com.adeuza.movalysfwk.mobile.mf4android.context;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContextFactory;

/**
 * <p>Factory pour la création d'un nouveau context android.</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
 * @since MF-Annapurna
 */
public class AndroidContextFactoryImpl implements MContextFactory {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MContext createContext() {
		return new AndroidContextImpl(((AndroidApplication)Application.getInstance()).getApplication());
	}
}
