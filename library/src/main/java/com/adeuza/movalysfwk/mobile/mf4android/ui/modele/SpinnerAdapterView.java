package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * Processes to execute in the {@link AbstractConfigurableSpinnerAdapter} for the spinner view
 * @param <ITEM> the class of the item
 * @param <ITEMVM> the class of the item view model
 * @param <LISTVM> the class of the list view model
 */
public class SpinnerAdapterView<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>, LISTVM extends ListViewModel<ITEM, ITEMVM>>
	extends AbstractAdapterLevelTreatment<AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, LISTVM>>{

	/** true if the current view is selected */
	private boolean selected = false;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewModel getCurrentViewModel(AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, int... p_oPositions) {
		return p_oAdapter.getItem(p_oPositions[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doBeforeSetViewModelProcess(AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter,
			boolean p_bExpanded, View p_oView, ViewGroup p_oViewGroup, ViewModel p_oCurrentVM, int... p_oPositions) {
		selected = p_oAdapter.getSelectedItem() != null && p_oCurrentVM.getIdVM().equals(p_oAdapter.getSelectedItem().getIdVM());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getItemLayout(AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, int... p_oPositions) {
		return p_oAdapter.getItemLayout(p_oPositions[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableListViewHolder getViewHolder(AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, boolean p_bExpanded) {
		return p_oAdapter.createViewHolder();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterRestoreRow(AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter,
			View p_oCurrentRow, MasterVisualComponent<?> p_oCompositeComponent) {
		p_oAdapter.getComponents().remove(p_oCompositeComponent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doAfterSetViewModelProcess(AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter,
			boolean p_bExpanded, View p_oCurrentRow, ViewGroup p_oViewGroup, ViewModel p_oCurrentVM,
			MasterVisualComponent<?> p_oCompositeComponent, int... p_oPositions) {
		p_oCurrentRow.setSelected(selected);
		p_oAdapter.setSelectedPosition(p_oPositions[0]);
		p_oAdapter.getComponents().add(p_oCompositeComponent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void wrapCurrentView(AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter,
			View p_oCurrentRow, ViewModel p_oCurrentVM, boolean b_isExpanded, int... p_oPositions) {
		p_oAdapter.wrapCurrentView(p_oCurrentRow, (ITEMVM) p_oCurrentVM, p_oPositions[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View inflateOnNullViewModel(AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, 
			LayoutInflater p_oLayoutInflater, View p_oCurrentRow,
			ConfigurableListViewHolder p_oHolder, int... p_oPositions) {
		AndroidApplication oApp = ((AndroidApplication) Application.getInstance());
		View r_oView = p_oLayoutInflater.inflate(oApp.getAndroidIdByRKey(AndroidApplicationR.fwk_component__simple_spinner_emptyitem__master), null);
		p_oHolder = p_oAdapter.createViewHolder();
		r_oView.setTag(p_oHolder);
		
		return r_oView;
	}

}
