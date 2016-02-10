package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractConfigurableExpandableListAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;

/**
 * <p>
 * 	Simplistic implementation of an <code>FixedList</code>, only to obtain a list of items without action on it (opening popup, delete item, etc.).
 * 	There is no title bar too.
 * </p>
 *
 * <p>Copyright (c) 2012</p>
 * <p>Company: Adeuza</p>
 *
 * @param <LISTVM> a model for list  
 * @param <ITEMVM> a model for item
 * 
 * @author fbourlieux
 * @since MF-Baltoro
 */
public class MMMultiSelectedFixedListView<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>> extends AbstractFixedListView<ITEM, ITEMVM> {

	/** adapter of the expendableList component of the <code>MMMultiSelectedListView</code> */
	@SuppressWarnings("rawtypes")
	private AbstractConfigurableExpandableListAdapter expandableAdapter;
	
	/**
	 * <p>Construct an object <em>MMMultiSelectedFixedListView</em>.</p>
	 * @param p_oContext the application context
	 */
	public MMMultiSelectedFixedListView(Context p_oContext) {
		this(p_oContext,null);
	}
	
	/**
	 * <p>Construct an object <em>MMMultiSelectedFixedListView</em> with parameters.</p>
	 * @param p_oContext the application context
	 * @param p_oAttrs parameter to configure the <em>MMMultiSelectedFixedListView</em> object.
	 */
	public MMMultiSelectedFixedListView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean treatCustomButton(View p_oView) {
		// Nothing to do
		return false;
	}
	
	/**
	 * Retourne l'id de la ressource pour la suppression de l'item dans la <code>FixedList</code>.
	 * @return identifiant de la ressource pour la suppression de l'item dans la liste.
	 */
	public int getDeleteId(){
		return this.getUiDeleteButtonId();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doOnClickDeleteButtonOnItem(View p_oView) {
		super.doOnClickDeleteButtonOnItem(p_oView);
		if (expandableAdapter!=null){
			Log.i(getClass().getSimpleName(),"notify the MMExpendableListView changed");
			expandableAdapter.notifyDataSetChanged();
		}
	}
	
	
	/**
	 * Define the Adapter of the <code>ExpendableListView</code> of the <code>MMMUltiSelectedListView</code> component.
	 * @param p_oAdapter the adapter to set
	 */
	public void setExpandableAdapter(@SuppressWarnings("rawtypes") AbstractConfigurableExpandableListAdapter p_oAdapter){
		expandableAdapter=p_oAdapter;
	}

	/**
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.listener.ConfigurationListener#beforeConfigurationChanged(java.util.Map)
	 */
	@Override
	public void beforeConfigurationChanged(Map<String, Object> p_oConfigurationMap) {
		// nothing to do
	}

	/**
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.listener.ConfigurationListener#afterConfigurationChanged(java.util.Map)
	 */
	@Override
	public void afterConfigurationChanged(Map<String, Object> p_oConfigurationMap) {
		// nothing to do
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String p_sNewName) {
		this.aivDelegate.setName(p_sNewName);
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomConverter customConverter() {
		return this.aivDelegate.customConverter();
	}

	public void setCustomConverter(String p_oCustomConverter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomConverter(p_oCustomConverter,p_oAttributeSet);		
	}

	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		this.aivDelegate.setCustomConverter(p_oConverter);
	}
	
	@Override
	public void setCustomFormatter(CustomFormatter p_oFormatter) {
		this.aivDelegate.setCustomFormatter(p_oFormatter);
	}
	
	@Override
	public CustomFormatter customFormatter() {
		return this.aivDelegate.customFormatter();
	}
	
	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomFormatter(p_oCustomFormatter,p_oAttributeSet);		
	}
}
