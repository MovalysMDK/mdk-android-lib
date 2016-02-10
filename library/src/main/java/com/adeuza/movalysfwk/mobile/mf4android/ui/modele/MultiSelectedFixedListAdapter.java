package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.generic.InParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModelImpl;

/**
 * <p>Generic adapter for a simple <code>FixedList</code> that only display some items, not initiate actions.</p>
 *
 * <p>Copyright (c) 2012</p>
 * <p>Company: Adeuza</p>
 *
 * @param <SUBITEM> model entity
 * @param <SUBITEMVM> a view model for list  
 * 
 * @author fbourlieux
 * @since MF-Baltoro
 */
public class MultiSelectedFixedListAdapter<SUBITEM extends MIdentifiable, SUBITEMVM extends ItemViewModel<SUBITEM>> 
				extends AbstractConfigurableFixedListAdapter<SUBITEM,SUBITEMVM,ListViewModel<SUBITEM, SUBITEMVM>> {

	/**
	 * Construct a new Adapter for all selected items in <code>MMMultiSelectedExpandableListView</code> component. 
	 */
	public MultiSelectedFixedListAdapter(int p_iLayoutId, int p_iComponentId, Class<SUBITEMVM> p_oClass) {
		super(new ListViewModelImpl<>(p_oClass, false),p_iLayoutId,p_iComponentId,-1);//pour désactiver l'action lors du clic sur l'item
	}
	
	/**
	 * {@inheritDoc}
	 * <p>DONT USE THIS METHOD, IN OUR CASE THIS METHOD WILL ALLWAYS RETURN NULL !!</p>
	 */
	@Override
	public SUBITEMVM createEmptyVM() {
		/* Nothing to do */
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>DONT USE THIS METHOD, IN OUR CASE THIS METHOD WILL ALLWAYS RETURN NULL !!</p>
	 */
	@Override
	public Class<? extends Action<InParameter, ?, ?, ?>> getDoAfterDeleteAction() {
		/* Nothing to do */
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>DONT USE THIS METHOD, IN OUR CASE THIS METHOD WILL ALLWAYS RETURN NULL !!</p>
	 */
	@Override
	public Class<? extends Action<InParameter, ?, ?, ?>> getDoBeforeAddAction() {
		/* Nothing to do */
		return null;
	}

}
