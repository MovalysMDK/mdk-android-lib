package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

public class ListAdapterChild<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>, LISTVM extends ListViewModel<ITEM, ITEMVM>>
	extends AbstractAdapterLevelTreatment<AbstractConfigurableListAdapter<ITEM, ITEMVM, LISTVM>> {

	private ConfigurableListViewHolder viewHolder;
	private boolean selected = false;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewModel getCurrentViewModel(AbstractConfigurableListAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, int... p_oPositions) {
		return p_oAdapter.getItem(p_oPositions[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doBeforeSetViewModelProcess(AbstractConfigurableListAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, 
			boolean p_bExpanded, View p_oView, ViewGroup p_oViewGroup, ViewModel p_oCurrentVM, int... p_oPositions) {
		selected = p_oCurrentVM.getIdVM().equals(p_oAdapter.getSelectedItem());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getItemLayout(AbstractConfigurableListAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, int... p_oPositions) {
		return p_oAdapter.getItemLayout(p_oPositions[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableListViewHolder getViewHolder(AbstractConfigurableListAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, boolean p_bExpanded) {
		this.viewHolder = p_oAdapter.createViewHolder();
		
		return this.viewHolder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterRestoreRow(AbstractConfigurableListAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, 
			View p_oCurrentRow, MasterVisualComponent<?> p_oCompositeComponent) {
		p_oAdapter.getComponents().remove(p_oCompositeComponent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterSetViewModelProcess(AbstractConfigurableListAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter,
			boolean p_bExpanded, View p_oCurrentRow, ViewGroup p_oViewGroup,
			ViewModel p_oCurrentVM, MasterVisualComponent<?> p_oCompositeComponent, int... p_oPositions) {
		p_oAdapter.getComponents().add(p_oCompositeComponent);
		viewHolder.isSelected = selected;
		selected = selected || viewHolder.isSelected;
		p_oCurrentRow.setSelected(selected);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void wrapCurrentView(AbstractConfigurableListAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, View p_oCurrentRow,
			ViewModel p_oCurrentVM, boolean b_isExpanded, int... p_oPositions) {
		p_oAdapter.wrapCurrentView(p_oCurrentRow, (ITEMVM) p_oCurrentVM, p_oPositions[0]);
	}
}
