package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMSearchSpinner;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMSpinnerCheckedTextView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModelImpl;

/**
 * <p>Adapter de liste pour un composant de type MMSearchSpinner.</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
 * @since MF-Annapurna
 * 
 * @param <ITEM> le type d'un item présent dans la liste
 * @param <ITEMVM> le type du VM d'un item présent dans la liste
 * @param <LISTVM> le type de la liste faisant le lien entre l'item et son ViewModel
 */
public class ConfigurableSpinnerListAdapter<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>, LISTVM extends ListViewModel<ITEM,ITEMVM>> 
																					extends AbstractConfigurableListAdapter<ITEM,ITEMVM,LISTVM> implements OnItemClickListener {
	
	/** la liste complète dans le composant, sans prendre en compte le filtre de recherche */
	private List<ITEMVM> initialAdapterList = null;
	/** le listener de sélection */
	private OnClickListener listener = null;
	/** l'item sélectionné dans la liste */
	private ITEMVM selectedItem = null;
	/** l'identifiant du composant graphique dans lequel on fait la sélection */
	private int selectedComponentId = -1;
	
	private boolean useEmptyItem = true;
	
	/** View of the empty item in list. */
//	private View emptyDropDownItem = null;
	
	/**
	 * Création d'un nouvel objet de type ConfigurableSpinnerListAdapter.
	 * @param p_oSpinnerAdapter l'adapter de la liste
	 * @param p_oClickListener le listener de sélection d'un item
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ConfigurableSpinnerListAdapter(AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, LISTVM> p_oSpinnerAdapter, Class<ITEMVM> p_oClass, OnClickListener p_oClickListener, boolean p_bUseEmptyItem) {
		//transformation d'un Adapter de Spinner en Adapter de Liste
		super((LISTVM) new ListViewModelImpl((ListViewModelImpl<?,?>)p_oSpinnerAdapter.getMasterVM(), p_oClass), 
				p_oSpinnerAdapter.getSpinnerDropDowListLayoutId(), 
				p_oSpinnerAdapter.getConfigurableMasterDropDowId());
		initialAdapterList = new ArrayList<>();
		for (int i=0;i<p_oSpinnerAdapter.getCount();i++) {
			initialAdapterList.add(p_oSpinnerAdapter.getMasterVM().getCacheVMByPosition(i));
		}
//		initialAdapterList = new ArrayList<ITEMVM>(p_oSpinnerAdapter.getMasterVM().getList());
		listener = p_oClickListener;
		selectedComponentId = p_oSpinnerAdapter.getSelectedComponent();
		selectedItem = p_oSpinnerAdapter.getSelectedItem();
		this.useEmptyItem = p_bUseEmptyItem;
	}
	
	/**
	 * A2A_DOC - Décrire la méthode hasEmptyItem de la classe AbstractConfigurableSpinnerAdapter
	 * @return
	 */
	protected boolean hasEmptyItem() {
		return this.useEmptyItem;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCount() {
		int iCount = 0 ;
		if (this.hasEmptyItem()){
			iCount = 1 ;
		}
		return this.getMasterVM().getCount() +iCount;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView(int p_iParamInt, View p_oView, ViewGroup p_oViewGroup) {
		View r_oCurrentRow ;
		//Récupération du model
		if (this.getItem(p_iParamInt) == null) {
			AndroidApplication oApp = ((AndroidApplication) Application.getInstance());
			r_oCurrentRow = LayoutInflater.from(p_oViewGroup.getContext()).inflate(oApp.getAndroidIdByRKey(AndroidApplicationR.fwk_component__simple_spinner_dropdown_emptyitem__master), null);
			ConfigurableListViewHolder oHolder = this.createViewHolder();
			r_oCurrentRow.setTag(oHolder);
			this.wrapCurrentView(r_oCurrentRow, null, p_iParamInt);
		}
		else {
			r_oCurrentRow = super.getView(p_iParamInt, p_oView, p_oViewGroup);
			
		}
		return r_oCurrentRow;
	}
	
	/**
	 * Retourne l'emplacement d'un item dans l'adapter.
	 * @param p_oItem l'item à rechercher
	 * @return la position, ou null s'il n'existe pas
	 */
	public int indexOf(ITEMVM p_oItem){
		int iIndex = 0 ;
		if (this.hasEmptyItem()){
			iIndex = 1 ;
		}
		return this.getMasterVM().indexOf(p_oItem) + iIndex;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITEMVM getItem(int p_iParamInt) {
		ITEMVM r_oItem = null;

		int iIndex = p_iParamInt;
		if (this.hasEmptyItem()) {
			iIndex--;
		}

		if (iIndex > -1) {
			r_oItem = this.getMasterVM().getCacheVMByPosition(iIndex);
		}

		return r_oItem;
	}
	
	/**
	 * Définis l'item sélectionné dans la liste.
	 * @param p_oSelectedItem l'item sélectionné
	 */
	public void setSelectedItem(ITEMVM p_oSelectedItem){
		selectedItem = p_oSelectedItem;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void wrapCurrentView(View p_oView, ITEMVM p_oCurrentViewModel, int p_iParamChildPosition) {
		super.wrapCurrentView(p_oView, p_oCurrentViewModel, p_iParamChildPosition);
		
		if (selectedComponentId!=-1) {
			View oComponent = p_oView.findViewById(this.selectedComponentId);
			if (oComponent == null) {
				oComponent = p_oView.findViewById(((AndroidApplication) Application.getInstance())
						.getAndroidIdByRKey(AndroidApplicationR.component__simple_spinner_dropdown_emptyitem__value));
			}
			if (oComponent instanceof MMSpinnerCheckedTextView) {
				if (p_oCurrentViewModel == null) {
					((MMSpinnerCheckedTextView) oComponent).setChecked(this.selectedItem == null);
				}
				else {
					((MMSpinnerCheckedTextView) oComponent).setChecked(p_oCurrentViewModel.equals(this.selectedItem));
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getItemViewType(int p_oPosition) {
		int iType = 0 ;
		if (p_oPosition != 0 && this.hasEmptyItem()){
			iType = 1 ;
		}
		return  iType ;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getViewTypeCount() {
		int iType = 1 ;
		if (this.hasEmptyItem()){
			iType = 2 ;
		}
		return  iType ;
	}

	@Override
	public void onItemClick(AdapterView<?> p_oParent, View p_oView, int p_iPosition,
			long p_iId) {
		((MMSearchSpinner<?, ?>) listener).internViewClicked(p_oView);
	}
}
