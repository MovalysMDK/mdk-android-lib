package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;


/**
 * This is a classic ViewHolder implementation used to retain some references
 * of the components of the items of a ListView
 * @author quentinlagarde
 */
public class ConfigurableExpandedListViewHolder extends ConfigurableListViewHolder {
	
	
	private boolean isExpanded = false;
	
	
	/**
	 * Constructeur
	 */
	public ConfigurableExpandedListViewHolder(boolean p_bIsExpanded) {
		super();
		this.isExpanded = p_bIsExpanded;
	}


	public boolean isExpanded() {
		return isExpanded;
	}


	public void setExpanded(boolean p_bIsExpanded) {
		this.isExpanded = p_bIsExpanded;
	}
	
	
}
