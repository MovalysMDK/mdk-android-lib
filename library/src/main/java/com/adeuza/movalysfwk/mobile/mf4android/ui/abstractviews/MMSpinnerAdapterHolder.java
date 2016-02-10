package com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractConfigurableSpinnerAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;

/**
 * @author lmichenaud
 *
 * @param <ITEM>
 * @param <ITEMVM>
 */
public interface MMSpinnerAdapterHolder<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>> {

	/**
	 * @return
	 */
	public AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, ListViewModel<ITEM,ITEMVM>> getAdapter();
	
	/**
	 * @param p_oAdapter
	 */
	public void setAdapter(AbstractConfigurableSpinnerAdapter p_oAdapter);
}
