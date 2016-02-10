package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;

/**
 * <p>TODO Décrire la classe FixedListValueObject</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author dmaurange
 *
 */

public class FixedListValueObject <ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>>{
	/** the list model */
	private ListViewModel<ITEM,ITEMVM> masterVM = null;
	/** android id of layout (the name of file) for item */
	private int layoutid = 0;
	/** android id of configurable layout for item */
	private int configurableLayout = 0;
	/** the name of model */
	private String name = null;
	
	/**
	 * Construc a new FixedListValueObject
	 * @param p_sName the name of layout
	 * @param p_oMasterVM list's view model
	 * @param p_iLayoutId id of layout (the file name)
	 * @param p_iConfigurableLayout id of configurable layout (the first component)
	 */
	public FixedListValueObject(String p_sName, ListViewModel<ITEM,ITEMVM> p_oMasterVM, int p_iLayoutId, int p_iConfigurableLayout) {
		this.name = p_sName;
		this.masterVM = p_oMasterVM;
		this.layoutid = p_iLayoutId;
		this.configurableLayout = p_iConfigurableLayout;
	}

	/**
	 * Retourne l'objet masterVM
	 * @return Objet masterVM
	 */
	public ListViewModel<ITEM,ITEMVM> getMasterVM() {
		return this.masterVM;
	}

	/**
	 * Affecte l'objet masterVM 
	 * @param p_oMasterVM Objet masterVM
	 */
	public void setMasterVM(ListViewModel<ITEM,ITEMVM> p_oMasterVM) {
		this.masterVM = p_oMasterVM;
	}

	/**
	 * Retourne l'objet layoutid
	 * @return Objet layoutid
	 */
	public int getLayoutid() {
		return this.layoutid;
	}

	/**
	 * Affecte l'objet layoutid 
	 * @param p_oLayoutid Objet layoutid
	 */
	public void setLayoutid(int p_oLayoutid) {
		this.layoutid = p_oLayoutid;
	}

	/**
	 * Retourne l'objet configurableLayout
	 * @return Objet configurableLayout
	 */
	public int getConfigurableLayout() {
		return this.configurableLayout;
	}

	/**
	 * Affecte l'objet configurableLayout 
	 * @param p_oConfigurableLayout Objet configurableLayout
	 */
	public void setConfigurableLayout(int p_oConfigurableLayout) {
		this.configurableLayout = p_oConfigurableLayout;
	}

	/**
	 * Retourne l'objet name
	 * @return Objet name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Affecte l'objet name 
	 * @param p_oName Objet name
	 */
	public void setName(String p_oName) {
		this.name = p_oName;
	}
}
