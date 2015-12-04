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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask;

import java.lang.ref.WeakReference;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;

/**
 * <p>Value object permettant de passer des paramètres d'entrée entre l'ActionTask et l'Action</p>
 *
 *
 */
public class ActionTaskIn<IN extends ActionParameter> {
	
	/** le paramètre d'entrée */
	private IN in ;

	private WeakReference<Object> source ;
	
	/**
	 * Retourne l'objet in
	 *
	 * @return Objet in
	 */
	public IN getIn() {
		return this.in;
	}

	/**
	 * Affecte l'objet in
	 *
	 * @param p_oIn Objet in
	 */
	public void setIn(IN p_oIn) {
		this.in = p_oIn;
	}

	/**
	 * Retourne l'objet source
	 *
	 * @return Objet source
	 */
	public Object getSource() {
		Object r_oObject = null;
		if ( this.source != null ) {
			r_oObject = this.source.get();
		}
		return r_oObject;
	}

	/**
	 * Affecte l'objet source
	 *
	 * @param p_oSource Objet source
	 */
	public void setSource(Object p_oSource) {
		this.source = new WeakReference<>(p_oSource);
	}
}
