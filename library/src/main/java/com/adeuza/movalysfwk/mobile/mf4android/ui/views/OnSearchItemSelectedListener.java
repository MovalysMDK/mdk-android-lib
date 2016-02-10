package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.view.View;

/**
 * <p>Listener qui va écouter la liste de résultat d'un composant de type MMSearchSpinner pour récupérer l'item sélectionné.</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
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
