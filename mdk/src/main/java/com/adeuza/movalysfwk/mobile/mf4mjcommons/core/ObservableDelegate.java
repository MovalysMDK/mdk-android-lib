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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.core;

import static com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ErrorDefinition.FWK_MOBILE_E_0101;
import static com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ErrorDefinition.FWK_MOBILE_E_0101_LABEL;
import static com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ErrorDefinition.FWK_MOBILE_E_0120;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ErrorDefinition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;

/**
 * <p>
 * Implements observer/observable pattern
 * </p>
 *
 * @param <LISTENER> listener of this class
 */
public class ObservableDelegate<LISTENER> implements Serializable {

	/** method to call on notification */
	private HashMap<String, Method> methodToCallOnNotify ;
	/** list of listener */
	private List<LISTENER> listeners ;
	/**
	 * Construct a new delagate
	 */
	public ObservableDelegate() {
		this.listeners = new ArrayList<>();
		this.methodToCallOnNotify = new HashMap<>();
	}

	//SMA TODO faire autrement (mais faudrait casser les signatures)
	/**
	 * <p>Getter for the field <code>listeners</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<LISTENER> getListeners() {
		return this.listeners;
	}
	
	/**
	 * Register a new listener. if listener is already register, then calling
	 * this method does not involve any action
	 *
	 * @param p_oListener the listener to register
	 */
	public void register(LISTENER p_oListener) {
		if (!this.listeners.contains(p_oListener)) {
			this.listeners.add(p_oListener);
		}
	}

	/**
	 * Register a new listener. if listener is already register, then calling
	 * this method does not involve any action
	 *
	 * @param p_oListener the listener to register
	 */
	public void registerInFirst(LISTENER p_oListener) {
		if (!this.listeners.contains(p_oListener)) {
			this.listeners.add(0, p_oListener);
		}
	}

	/**
	 * Unregister a listener, if the listener is not present, then calling this
	 * method does not involve any action
	 *
	 * @param p_oListener the listener to unregister
	 */
	public void unregister(LISTENER p_oListener) {
		if (this.listeners.contains(p_oListener)) {
			this.listeners.remove(p_oListener);
		}
	}
	
	/**
	 * <p>unregisterAll.</p>
	 */
	public void unregisterAll() {
		this.listeners.clear();
	}
	
	/**
	 * Notifiy listners by calling the method p_sMethodToLaunch
	 *
	 * @param p_sMethodToLaunch method to call
	 * @param p_oNotifier notifier
	 * @param p_oParams parameters to use
	 */
	public void doOnNotification(String p_sMethodToLaunch, Notifier p_oNotifier, Object... p_oParams) {
		String sKey = null;
		Method oMethod = null;

		final List<LISTENER> listListeners = new ArrayList<LISTENER>(this.listeners);
		for (LISTENER oListener : listListeners) {
			if (oListener != p_oNotifier) {
				sKey = StringUtils.concat(oListener.getClass().getSimpleName(), ".", p_sMethodToLaunch);
				oMethod = this.methodToCallOnNotify.get(sKey);
				if (oMethod == null) {
					for (Method oTemp : oListener.getClass().getMethods()) {
						if (oTemp.getName().equals(p_sMethodToLaunch)) {
							oMethod = oTemp;
							break;
						}
					}
					if (oMethod == null) {
						throw new MobileFwkException(FWK_MOBILE_E_0120, StringUtils.concat(
								ErrorDefinition.FWK_MOBILE_E_0120_LABEL, "-", sKey));
					}
					this.methodToCallOnNotify.put(sKey, oMethod);
				}
				try {
					oMethod.invoke(oListener, p_oParams);
				} catch (IllegalArgumentException e) {
					StringBuilder sMessage = new StringBuilder("(");
					sMessage.append("Impossible to invoke method: ");
					sMessage.append(p_sMethodToLaunch);
					sMessage.append("(");
					for (Object oObject : p_oParams) {
						if (oObject != null) {
							sMessage.append(' ');
							sMessage.append(oObject.getClass().getName());
						} else {
							sMessage.append(" null");
						}
					}
					sMessage.append(") on class: ");
					sMessage.append(oListener.getClass().getName());
					sMessage.append(" - Found method signature is : ");
					sMessage.append(oMethod.toString());

					Application.getInstance().getLog().error(FWK_MOBILE_E_0101, sMessage.toString());
					throw new MobileFwkException(FWK_MOBILE_E_0101, FWK_MOBILE_E_0101_LABEL, e);
				} catch (IllegalAccessException | InvocationTargetException e) {
					throw new MobileFwkException(FWK_MOBILE_E_0101, FWK_MOBILE_E_0101_LABEL, e);
				}
			}
		}
	}
}
