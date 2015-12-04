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
package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.view.View;

/**
 * <p>Listener qui va écouter la liste de résultat d'un composant de type MMSearchSpinner pour récupérer l'item sélectionné.</p>
 *
 *
 * @since MF-Annapurna
 */
public interface OnSearchItemSelectedListener {

	/**
	 * M2thode déclenchée lorsqu'on sélectionne un item dans la liste d'un composant de type MMSearchSpinner.
	 * @param p_oSpinnerSelected le spinner courant
	 * @param p_oArg1 la vue dans lequel se trouve le spinner
	 * @param p_iNdexOfSelectedItem l'index de l'item sélectionné
	 */
	public void onItemSelected(MMSearchSpinner p_oSpinnerSelected, View p_oArg1, int p_iNdexOfSelectedItem);
	
}
