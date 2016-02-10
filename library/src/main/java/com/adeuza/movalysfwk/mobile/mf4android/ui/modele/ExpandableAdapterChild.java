package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ExpandableViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * Processes to execute in the {@link AbstractConfigurableExpandableListAdapter} for the child view
 * @param <ITEM> the class of the group item
 * @param <SUBITEM> the class of the child item
 * @param <SUBITEMVM> the class of the view model of the child
 * @param <ITEMVM> the class of the view model of the group
 */
public class ExpandableAdapterChild<ITEM extends MIdentifiable, SUBITEM extends MIdentifiable, SUBITEMVM extends ItemViewModel<SUBITEM>, ITEMVM extends ExpandableViewModel<ITEM, SUBITEM, SUBITEMVM>>
	extends AbstractAdapterLevelTreatment<AbstractConfigurableExpandableListAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM>> {

	/** the coat for the view */
	private View currentCoatRow;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewModel getCurrentViewModel(AbstractConfigurableExpandableListAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> p_oAdapter, int... p_oPositions) {
		return p_oAdapter.getChild(p_oPositions[0], p_oPositions[1]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableListViewHolder getViewHolder(AbstractConfigurableExpandableListAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> p_oAdapter, boolean p_bExpanded) {
		return p_oAdapter.createViewHolder();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getItemLayout(AbstractConfigurableExpandableListAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> p_oAdapter, int ... p_oPositions) {
		return p_oAdapter.getChildLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void wrapCurrentView(AbstractConfigurableExpandableListAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> p_oAdapter, View p_oCurrentRow, ViewModel p_oCurrentVM, boolean b_isExpanded, int ... p_oPositions) {
		p_oAdapter.wrapCurrentChildView(p_oCurrentRow, (SUBITEMVM) p_oCurrentVM, p_oPositions[0], p_oPositions[1]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterInflateRow(AbstractConfigurableExpandableListAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> p_oAdapter, LayoutInflater p_oLayoutInflater, View p_oCurrentRow, View p_oView) {
		currentCoatRow = p_oView;
		ViewGroup oContentView;
		if (p_oAdapter.getChildCoatLayout()!=0 
				&& p_oAdapter.getChildCoatContentLayoutId()!=0){
			currentCoatRow = p_oLayoutInflater.inflate(p_oAdapter.getChildCoatLayout(), null);
			oContentView = (ViewGroup) currentCoatRow.findViewById(p_oAdapter.getChildCoatContentLayoutId());
			if (p_oCurrentRow.getParent()==null){
				oContentView.addView(p_oCurrentRow);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterRestoreRow(AbstractConfigurableExpandableListAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> p_oAdapter, View p_oCurrentRow, MasterVisualComponent<?> p_oCompositeComponent) {
		if (p_oAdapter.getChildCoatLayout()!=0 
				&& p_oAdapter.getChildCoatContentLayoutId()!=0){
			p_oCurrentRow = p_oCurrentRow.findViewById(p_oAdapter.getChildCoatContentLayoutId());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getViewToReturn(AbstractConfigurableExpandableListAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> p_oAdapter, View p_oCurrentRow, ViewModel p_oCurrentVM) {
		if (p_oAdapter.getChildCoatLayout() != 0 && p_oAdapter.getChildCoatContentLayoutId() != 0){
			p_oAdapter.wrapCoat(p_oCurrentRow, (SUBITEMVM) p_oCurrentVM);
			return currentCoatRow;
		} else {
			return p_oCurrentRow;
		}
	}
}