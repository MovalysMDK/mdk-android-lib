package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMMasterRelativeLayout;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * Processes to execute in the {@link AbstractConfigurableSpinnerAdapter} for the drop down view
 * @param <ITEM> the class of the item
 * @param <ITEMVM> the class of the item view model
 * @param <LISTVM> the class of the list view model
 */
public class SpinnerAdapterDropDown<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>, LISTVM extends ListViewModel<ITEM, ITEMVM>> 
	extends AbstractAdapterLevelTreatment<AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, LISTVM>> {

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
		selected = p_oPositions[0] == p_oAdapter.getSelectedPosition();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean needToInflateView(View p_oCurrentRow) {
		return (p_oCurrentRow == null
				|| AndroidApplicationR.fwk_component__simple_spinner_dropdown_emptyitem__master
						.toString().endsWith(((MMMasterRelativeLayout) p_oCurrentRow).getName()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getItemLayout(AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, int... p_oPositions) {
		return p_oAdapter.getSpinnerDropDowListLayoutId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigurableListViewHolder getViewHolder(AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, boolean p_bExpanded) {
		return p_oAdapter.createDropDownViewHolder();
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
	public void wrapCurrentView(AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter,
			View p_oCurrentRow, ViewModel p_oCurrentVM, boolean b_isExpanded, int... p_oPositions) {
		p_oAdapter.wrapCurrentDropDownView(p_oCurrentRow, (ITEMVM) p_oCurrentVM, p_oPositions[0], b_isExpanded);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View inflateOnNullViewModel(AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oAdapter, 
			LayoutInflater p_oLayoutInflater, View p_oCurrentRow,
			ConfigurableListViewHolder p_oHolder, int... p_oPositions) {
		AndroidApplication oApp = ((AndroidApplication) Application.getInstance());
		View r_oView = p_oLayoutInflater.inflate(
						oApp.getAndroidIdByRKey(AndroidApplicationR.fwk_component__simple_spinner_dropdown_emptyitem__master),
						null);
		p_oHolder = p_oAdapter.createDropDownViewHolder();
		r_oView.setTag(p_oHolder);
		
		p_oAdapter.wrapCurrentDropDownView(r_oView, null, p_oPositions[0], selected);
		
		return r_oView;
	}
	
}
