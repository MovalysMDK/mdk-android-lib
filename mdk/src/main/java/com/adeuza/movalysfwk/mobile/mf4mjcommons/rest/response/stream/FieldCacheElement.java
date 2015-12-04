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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.rest.response.stream;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p>
 * 	Classe utilisée lors de la réception du flux Json pour regrouper dans le cache
 * 	les <em>fields</em> et <em>setters</em> à invoquer pour réaliser le mapping suite à la synchronisation.
 * </p>
 *
 */
public class FieldCacheElement {
	/** <code>Field</code> retourné par la synchronisation. */
	private Field field ;
	/** La méthode à invoquer pour binder le <code>Field</code> au bon endroit sur le mobile. */
	private Method setter ;

	/**
	 * <p>Construit un nouvel objet <code>FieldCacheElement</code> pour binder les values sur le mobile</p>.
	 *
	 * @param p_oField le <code>Field</code> reçut lors de la zynchro à binder sur le mobile.
	 * @param p_oSetter La <code>Method</code> à invoquer pour conder correctement le <code>Field</code> envoyé par la synchro.
	 */
	public FieldCacheElement(Field p_oField, Method p_oSetter) {
		super();
		this.field = p_oField;
		this.setter = p_oSetter;
	}

	/**
	 * Retourne le <code>Field</code>.
	 *
	 * @return <code>Field</code>
	 */
	public Field getField() {
		return field;
	}

	/**
	 * Retourne la <code>Method</code> à invooquer pour binder le <code>Field</code> au bon endroit sur le mobile.
	 *
	 * @return <code>Field</code>
	 */
	public Method getSetter() {
		return setter;
	}
}
