package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;

import android.view.View;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMCheckBox;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ExpandableViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;

/**
 * <p>
 * 	Adapter for an multi selected expandable list.
 * </p>
 * <p>
 * 	THIS ADAPTER MUST ONLY BE USED BY A MMMultiSelectedListVew COMPONENT !!
 * </p>
 * 
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
 *
 * @param <LISTVM> a model for list  
 * @param <ITEMVM> a model for item
 */
public class MultiSelectedExpandableListAdapter<
				ITEM extends MIdentifiable, 
				SUBITEM extends MIdentifiable, 
				SUBITEMVM extends ItemViewModel<SUBITEM>, 
				ITEMVM extends ExpandableViewModel<ITEM, SUBITEM, SUBITEMVM>> 
							extends AbstractConfigurableExpandableListAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> {

	private AbstractConfigurableFixedListAdapter<SUBITEM, SUBITEMVM, ListViewModel<SUBITEM,SUBITEMVM>> selectedAdapter;
	/** Application */
	private AndroidApplication application = (AndroidApplication) Application.getInstance();
	/** Layout Id du fils */
	private int childLayoutId=0;
	/** Layout configurable Id du fils */
	private int configurableChildLayoutId;
	
	/**
	 * Construit une nouvelle couche pour un Adapter de liste à plusieurs niveau.
	 * @param p_oMasterVM
	 * @param p_oP_iGroupLayout
	 * @param p_oP_iConfigurableGroupLayoutId
	 * @param p_oP_iChildLayout
	 * @param p_oP_iConfigurableChildLayoutId
	 */
	public MultiSelectedExpandableListAdapter(ListViewModel<ITEM, ITEMVM> p_oMasterVM, int p_iGroupLayout,
			int p_iConfigurableGroupLayoutId, int p_iChildLayout, int p_iConfigurableChildLayoutId) {
		super(p_oMasterVM, p_iGroupLayout, p_iConfigurableGroupLayoutId, p_iChildLayout, p_iConfigurableChildLayoutId);
		childLayoutId=p_iChildLayout;
		configurableChildLayoutId=p_iConfigurableChildLayoutId;
	}
	
	/**
	 * Renseigne l'adapter des élément sélectionnés dans l'adapter de la liste à deux niveau.
	 * @param p_oSelectedAdapter
	 */
	public void setSelectedAdapter(AbstractConfigurableFixedListAdapter<SUBITEM, SUBITEMVM, ListViewModel<SUBITEM,SUBITEMVM>> p_oSelectedAdapter){
		selectedAdapter=p_oSelectedAdapter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void wrapCoat(View p_oView,SUBITEMVM p_oCurrentViewModel) {
		super.wrapCoat(p_oView, p_oCurrentViewModel);
		if (p_oView!=null 
				&& selectedAdapter.getMasterVM()!=null 
//				&& selectedAdapter.getMasterVM().getList()!=null 
				&& p_oView.getParent()!=null ){
			boolean bIsSelected = selectedAdapter.getMasterVM().indexOf(p_oCurrentViewModel)!=-1;
			View oView  ;
			if ( p_oView.getParent().getParent()==null) {
				oView  =(View)p_oView.getParent();
			}else {
				oView = (View)p_oView.getParent().getParent();
			}
			MMCheckBox oCheck = (MMCheckBox)oView.findViewById(application.getAndroidIdByRKey(AndroidApplicationR.vmdocumentedredevablepanellistdocumentsimpl__select));
			//si c'est selected alors on décoche, et inversement
			oCheck.setChecked(bIsSelected);
		}
	}

	/**
	 * Retourne l'objet childLayoutId
	 * @return Objet childLayoutId
	 */
	public int getChildLayoutId() {
		return this.childLayoutId;
	}

	/**
	 * Retourne l'objet childConfigurableComponentId
	 * @return Objet childConfigurableComponentId
	 */
	@Override
	public int getConfigurableChildLayoutId() {
		return this.configurableChildLayoutId;
	}
	
}
