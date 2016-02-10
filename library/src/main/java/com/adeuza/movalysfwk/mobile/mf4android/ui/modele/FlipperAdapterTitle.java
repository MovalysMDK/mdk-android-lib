package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

import android.view.View;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ExpandableViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * Processes to execute in the {@link AbstractConfigurableFlipperExpandableListAdapter} for the title view
 * @param <ITEM> the class of the item
 * @param <SUBITEM> the class of the group item
 * @param <SUBSUBITEM> the class of the child item
 * @param <SUBSUBITEMVM> the class of the view model of the child
 * @param <SUBITEMVM> the class of the view model of the group
 * @param <ITEMVM> the class of the view model of the title
 */
public class FlipperAdapterTitle<
		ITEM extends MIdentifiable, 
		SUBITEM extends MIdentifiable, 
		SUBSUBITEM extends MIdentifiable, 
		SUBSUBITEMVM extends ItemViewModel<SUBSUBITEM>, 
		SUBITEMVM extends ExpandableViewModel<SUBITEM, SUBSUBITEM, SUBSUBITEMVM>, 
		ITEMVM extends ExpandableViewModel<ITEM, SUBITEM, SUBITEMVM>> 
	extends AbstractAdapterLevelTreatment<AbstractConfigurableFlipperExpandableListAdapter<ITEM, SUBITEM, SUBSUBITEM, SUBSUBITEMVM, SUBITEMVM, ITEMVM>> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewModel getCurrentViewModel(
			AbstractConfigurableFlipperExpandableListAdapter<ITEM, SUBITEM, SUBSUBITEM, SUBSUBITEMVM, SUBITEMVM, ITEMVM> p_oAdapter,
			int... p_oPositions) {
		return p_oAdapter.getTitle(p_oPositions[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getItemLayout(
			AbstractConfigurableFlipperExpandableListAdapter<ITEM, SUBITEM, SUBSUBITEM, SUBSUBITEMVM, SUBITEMVM, ITEMVM> p_oAdapter,
			int... p_oPositions) {
		return p_oAdapter.getTitleLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableListViewHolder getViewHolder(
			AbstractConfigurableFlipperExpandableListAdapter<ITEM, SUBITEM, SUBSUBITEM, SUBSUBITEMVM, SUBITEMVM, ITEMVM> p_oAdapter,
			boolean p_bExpanded) {
		return p_oAdapter.createViewHolderTitleItem(p_bExpanded);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MasterVisualComponent<?> getCompositeComponent(
			AbstractConfigurableFlipperExpandableListAdapter<ITEM, SUBITEM, SUBSUBITEM, SUBSUBITEMVM, SUBITEMVM, ITEMVM> p_oAdapter,
			View p_oCurrentRow) {
		int iLayout = -1;
		
		iLayout = p_oAdapter.getConfigurableTitleLayoutId();
		
		MasterVisualComponent<?> p_oCompositeComponent = (MasterVisualComponent<?>) p_oCurrentRow.findViewById(iLayout);
		
		return p_oCompositeComponent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void wrapCurrentView(
			AbstractConfigurableFlipperExpandableListAdapter<ITEM, SUBITEM, SUBSUBITEM, SUBSUBITEMVM, SUBITEMVM, ITEMVM> p_oAdapter,
			View p_oCurrentRow, ViewModel p_oCurrentVM, boolean b_isExpanded, int... p_oPositions) {
		p_oAdapter.wrapTitleView(p_oCurrentRow, (ITEMVM) p_oCurrentVM, p_oPositions[0]);
	}
}
