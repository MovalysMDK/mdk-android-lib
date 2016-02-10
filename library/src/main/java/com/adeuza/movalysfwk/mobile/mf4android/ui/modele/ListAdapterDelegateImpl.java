package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
	 * @param p_oAdapter
	 */
	public ListAdapterDelegateImpl(ADAPTER p_oAdapter) {
		this.masterAdapter = p_oAdapter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public View getViewByLevel(AdapterLevel p_oLevel, boolean p_bExpanded, View p_oView, ViewGroup p_oViewGroup, int ... p_oPositions) {
		// Création du ViewHolder
		ConfigurableListViewHolder oHolder = null;

		// Vue à renvoyer
		View oCurrentRow = p_oView;

		MasterVisualComponent<?> oCompositeComponent = null;
		
		LayoutInflater oLayoutInflater = LayoutInflater.from(p_oViewGroup.getContext());
		
		// Récupération du model
		ViewModel oCurrentVM = p_oLevel.getLevel().getCurrentViewModel(masterAdapter, p_oPositions);
		
		if (oCurrentVM != null) {
		
			p_oLevel.getLevel().doBeforeSetViewModelProcess(masterAdapter, p_bExpanded, oCurrentRow, p_oViewGroup, oCurrentVM, p_oPositions);
			
			// Récupération du wrapper
			// on ne peut pas optimiser en ne recréant la vue que si le tag n'est plus l'id du view model
			// Android commence par une phase de destruction du lien des vues qui met à jour le tag MDK
			// quand les vues sont reconstruites, le tag est correct et n'est plus topé comme faux par MDK
			if (p_oLevel.getLevel().needToInflateView(oCurrentRow)) {
				// Création de la vue
				oCurrentRow = oLayoutInflater.inflate(p_oLevel.getLevel().getItemLayout(masterAdapter, p_oPositions), null);
				
				// On retient le nécessaire dans le ViewHolder
				oHolder = p_oLevel.getLevel().getViewHolder(masterAdapter, p_bExpanded);
				oCurrentRow.setTag(oHolder);
				
				p_oLevel.getLevel().doAfterInflateRow(masterAdapter, oLayoutInflater, oCurrentRow, p_oView);
			} else {
				
				oCompositeComponent = p_oLevel.getLevel().getCompositeComponent(masterAdapter, oCurrentRow);
				oCompositeComponent.getDescriptor().unInflate(
						String.valueOf(p_oViewGroup.hashCode()),
						oCompositeComponent, null);
				
				oHolder = (ConfigurableListViewHolder) oCurrentRow.getTag();
				
				p_oLevel.getLevel().doAfterRestoreRow(masterAdapter, oCurrentRow, oCompositeComponent);
			}
			
			// Mise a jour du ViewModel dans le holder
			oHolder.viewModelID = oCurrentVM.getIdVM();
			
			if (oCompositeComponent == null) {
				oCompositeComponent = p_oLevel.getLevel().getCompositeComponent(masterAdapter, oCurrentRow);
			}
			
			oCompositeComponent.setDescriptor(
					VisualComponentDescriptorsHandler.getInstance().getRealDescriptor(oCompositeComponent.getName()));
			oCompositeComponent.setViewModel(oCurrentVM);
			
			// On donne le ViewHolder à l'inflate pour qu'il récupère les
			// composants déja référencés si possible.
			oCompositeComponent.getDescriptor().inflate(
					String.valueOf(p_oViewGroup.hashCode()),
					oCompositeComponent, p_oLevel.getLevel().getInflateParameters(oHolder));
			
			p_oLevel.getLevel().doAfterSetViewModelProcess(masterAdapter, p_bExpanded, oCurrentRow, p_oViewGroup, oCurrentVM, oCompositeComponent, p_oPositions);
			
			p_oLevel.getLevel().wrapCurrentView(masterAdapter, oCurrentRow, oCurrentVM, p_bExpanded, p_oPositions);
			
			oCurrentVM.doOnDataLoaded(null);
		} else {
			// empty element on spinner
			oCurrentRow = p_oLevel.getLevel().inflateOnNullViewModel(masterAdapter, oLayoutInflater, oCurrentRow, oHolder, p_oPositions);
		}
		
		return p_oLevel.getLevel().getViewToReturn(masterAdapter, oCurrentRow, oCurrentVM);
	}
}