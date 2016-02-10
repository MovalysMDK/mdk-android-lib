package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.view.View;

/**
 * <p>Listener sur les MMFixedListView</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author ktilhou
 *
 */

public interface MMFixedListViewListener {
	
	
	/**
	 * <p>
	 * 	Evènement déclenché lors de l'ajout d'un élément à la liste.
	 * </p>
	 * @param p_oMMFixedListView Liste d'éléments
	 * @param p_iNbElem Nombre d'éléments de la liste
	 */
	public void onAdd(MMFixedListView<?,?> p_oMMFixedListView, int p_iNbElem);
	
	/**
	 * <p>
	 * 	Evènement déclenché lors de la suppression d'un élément à la liste.
	 * </p>
	 * @param p_oMMFixedListView Liste d'éléments
	 * @param iNbElem Nombre d'éléments de la liste
	 */
	public void onRemove(MMFixedListView<?,?> p_oMMFixedListView, int p_iNbElem);

	/**
	 * Launch an update of view after the writeData
	 * @param p_oMMFixedListView
	 * 		The fixed list
	 * 
	 * @param p_oView
	 * 		The view to update.
	 */
	public void updateViewDialogAfterWriteData(MMFixedListView<?,?> p_oMMFixedListView, View p_oView);
}
