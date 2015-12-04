/**
 * Copyright (C) 2010 Sopra Steria Group (movalys.support@soprasteria.com)
 *
 * This file is part of Movalys MDK.
 * Movalys MDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Movalys MDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Movalys MDK. If not, see <http://www.gnu.org/licenses/>.
 */
package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.WidgetWrapperHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.VisualComponentDescriptorsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * List adapters delegate
 * @param <ADAPTER> : the delegated adapter class
 */
public class ListAdapterDelegateImpl<ADAPTER> implements ListAdapterDelegate {

	/** Master adapter */
	protected ADAPTER masterAdapter;
	
	/**
	 * Constructor
	 * @param p_oAdapter the linked adapter
	 */
	public ListAdapterDelegateImpl(ADAPTER p_oAdapter) {
		this.masterAdapter = p_oAdapter;
	}
	
	@Override
	public View getViewByLevel(AdapterLevel p_oLevel, boolean p_bExpanded, View p_oView, ViewGroup p_oViewGroup, 
			int p_iViewType, int... p_iPositions) {
		
		ConfigurableListViewHolder oHolder = null;
		
		if ( p_oView != null ) {
			oHolder = (ConfigurableListViewHolder) p_oView.getTag();
		}
		
		if ( oHolder == null || p_oLevel.getLevel().needToInflateView(p_oView, p_iViewType)) {

			// no previous existing viewholder, create a new one.
			oHolder = this.onCreateViewHolder(p_oLevel, p_bExpanded, p_oViewGroup, p_iViewType);
			
			// set viewholder into view's tag.
			oHolder.getItemView().setTag(oHolder);
		}
		
		// Set data into viewholder
		this.onBindViewHolder(p_oLevel, p_bExpanded, oHolder, p_iPositions);
		
		return oHolder.getItemView();
	}
	
	/**
	 * {@inheritDoc}
	 * @deprecated USE getViewByLevel instead
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public View getLegacyViewByLevel(AdapterLevel p_oLevel, boolean p_bExpanded, View p_oView, ViewGroup p_oViewGroup, int p_iViewType, int ... p_oPositions) {
		// Création du ViewHolder
		ConfigurableListViewHolder oHolder = null;

		// Vue à renvoyer
		View oCurrentRow = p_oView;

		MasterVisualComponent oCompositeComponent = null;
		
		LayoutInflater oLayoutInflater = LayoutInflater.from(p_oViewGroup.getContext());
		
		// Récupération du model
		ViewModel oCurrentVM = p_oLevel.getLevel().getCurrentViewModel(masterAdapter, p_oPositions);
		
		if (oCurrentVM != null) {
		
			// Récupération du wrapper
			// on ne peut pas optimiser en ne recréant la vue que si le tag n'est plus l'id du view model
			// Android commence par une phase de destruction du lien des vues qui met à jour le tag MDK
			// quand les vues sont reconstruites, le tag est correct et n'est plus topé comme faux par MDK
			if (p_oLevel.getLevel().needToInflateView(oCurrentRow, p_iViewType)) {
				// Création de la vue
				oCurrentRow = oLayoutInflater.inflate(p_oLevel.getLevel().getItemLayout(masterAdapter, p_iViewType), null);
				
				// On retient le nécessaire dans le ViewHolder
				oHolder = p_oLevel.getLevel().getViewHolder(masterAdapter, oCurrentRow, p_bExpanded);

				// add wrapper to view
				oCompositeComponent = (MasterVisualComponent) WidgetWrapperHelper.getInstance().createWrapper(oHolder.getItemView(), "MMMasterLayoutWrapper");
				
				oHolder.setMaster(oCompositeComponent);
				
				oCurrentRow.setTag(oHolder);
				
				p_oLevel.getLevel().postInflate(masterAdapter, oCurrentRow, p_bExpanded);
			} else {
				oHolder = (ConfigurableListViewHolder) oCurrentRow.getTag();

				oCompositeComponent = oHolder.getMaster();
				
				if (oCompositeComponent == null) {
					// on doit creer le MasterVisualComponent
					oCompositeComponent = (MasterVisualComponent) WidgetWrapperHelper.getInstance().createWrapper(oCurrentRow, "MMMasterLayoutWrapper");
					
					// on le stocke dans le view holder
					oHolder.setMaster(oCompositeComponent);
				} else {
					oCompositeComponent.getDescriptor().unInflate(
							String.valueOf(p_oViewGroup.hashCode()),
							oCompositeComponent, null);
				}
				
				p_oLevel.getLevel().doAfterRestoreRow(masterAdapter, oCurrentRow, oCompositeComponent);
			}
			
			// Mise a jour du ViewModel dans le holder
			oHolder.setViewModelID(oCurrentVM.getIdVM());
			
//			if (oCompositeComponent == null) {
//				oCompositeComponent = p_oLevel.getLevel().getCompositeComponent(masterAdapter, oCurrentRow);
//			}
			
			oCompositeComponent.setDescriptor(
				VisualComponentDescriptorsHandler.getInstance().getRealDescriptor(oCompositeComponent.getComponentFwkDelegate().getName()));
			oCompositeComponent.setViewModel(oCurrentVM);
			
			// On donne le ViewHolder à l'inflate pour qu'il récupère les
			// composants déja référencés si possible.
			oCompositeComponent.getDescriptor().inflate(
					String.valueOf(p_oViewGroup.hashCode()),
					oCompositeComponent, p_oLevel.getLevel().getInflateParameters(oHolder));
			
			p_oLevel.getLevel().doAfterSetViewModelProcess(masterAdapter, p_bExpanded, oHolder, oCurrentVM, oCompositeComponent, p_oPositions);
			
			p_oLevel.getLevel().postBind(masterAdapter, oHolder.getItemView(), oCurrentVM, p_bExpanded, p_oPositions);
			
			oCurrentVM.doOnDataLoaded(null);
			p_oLevel.getLevel().doAfterOnDataLoaded(masterAdapter, oHolder);
		} else {
			// empty element on spinner
			oCurrentRow = p_oLevel.getLevel().inflateOnNullViewModel(masterAdapter, oLayoutInflater, oCurrentRow, oHolder, p_oPositions);
		}
		
		return p_oLevel.getLevel().getViewToReturn(masterAdapter, oCurrentRow, oCurrentVM);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ConfigurableListViewHolder onCreateViewHolder(AdapterLevel p_oLevel, boolean p_bExpanded, ViewGroup p_oViewGroup, int p_iViewType) {
		
		// Create inflater
		LayoutInflater oLayoutInflater = LayoutInflater.from(p_oViewGroup.getContext());
		
		// Inflate item layout
		View oCurrentRow = oLayoutInflater.inflate(p_oLevel.getLevel().getItemLayout(masterAdapter, p_iViewType), null);
		
		// Specific stuff after inflating
		p_oLevel.getLevel().doAfterInflateRow(masterAdapter, oLayoutInflater, oCurrentRow, null);
		
		// Method to customization view after inflating row
		p_oLevel.getLevel().postInflate(masterAdapter, oCurrentRow, p_bExpanded);
		
		// Create view holder
		ConfigurableListViewHolder r_oVH = p_oLevel.getLevel().getViewHolder(masterAdapter, oCurrentRow, p_bExpanded);
		r_oVH.setViewGroupHashCode(String.valueOf(p_oViewGroup.hashCode()));
		
		// add master wrapper to view holder
		r_oVH.setMaster((MasterVisualComponent) WidgetWrapperHelper.getInstance().createWrapper(r_oVH.getItemView(), "MMMasterLayoutWrapper"));
		
		// set params on MasterVisualComponent
		r_oVH.getMaster().setParameters(p_oLevel.getLevel().getInflateParameters(r_oVH));
		
		return r_oVH;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onBindViewHolder(AdapterLevel p_oLevel, boolean p_bExpanded, ConfigurableListViewHolder p_oViewholder, int... p_iPositions) {
		
		ViewModel oCurrentVM = p_oLevel.getLevel().getCurrentViewModel(masterAdapter, p_iPositions);
		
		if (oCurrentVM != null) {
		
			p_oLevel.getLevel().doBeforeSetViewModelProcess(masterAdapter, p_bExpanded, p_oViewholder.getItemView(), oCurrentVM, p_iPositions);
			
			// Uninflate
			MasterVisualComponent oCompositeComponent = p_oViewholder.getMaster();			
			
			// Descriptor is not defined if it's a new view. In the case of a recycled view, the descriptor is still there
			// and uninflate must be called.
			if ( oCompositeComponent.getDescriptor() != null ) {
				oCompositeComponent.getDescriptor().unInflate(p_oViewholder.getViewGroupHashCode(), oCompositeComponent, null);
			}
			
			// After clean up
			p_oLevel.getLevel().doAfterRestoreRow(masterAdapter, p_oViewholder.getItemView(), oCompositeComponent);
			
			//
			oCompositeComponent.setDescriptor(
					VisualComponentDescriptorsHandler.getInstance().getRealDescriptor(oCompositeComponent.getComponentFwkDelegate().getName()));
			oCompositeComponent.setViewModel(oCurrentVM);
			
			// Inflate
			oCurrentVM.unregister();
			oCompositeComponent.getDescriptor().inflate( p_oViewholder.getViewGroupHashCode(),
				oCompositeComponent, p_oLevel.getLevel().getInflateParameters(p_oViewholder));
			
			// After inflate
			p_oLevel.getLevel().doAfterSetViewModelProcess(masterAdapter, p_bExpanded, 
				p_oViewholder, oCurrentVM, oCompositeComponent, p_iPositions);
			
			// Mise a jour du ViewModel dans le holder
			p_oViewholder.setViewModelID(oCurrentVM.getIdVM());
			
			// Method to update data in view after binding row
			p_oLevel.getLevel().postBind(masterAdapter, p_oViewholder.getItemView(), oCurrentVM, p_bExpanded, p_iPositions);
			
			//
			oCurrentVM.doOnDataLoaded(null);
			
			//
			p_oLevel.getLevel().doAfterOnDataLoaded(masterAdapter, p_oViewholder);
		}
	}
}
