package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMRelativeLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMIdentifiableView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractConfigurableFixedListAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.MultiSelectedExpandableListAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.MultiSelectedFixedListAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.FwkPropertyName;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.SelectedComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ExpandableViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModelImpl;

/**
 * <p>
 * 	This component allow multiple selection in a list with two levels.
 * 	In this component we also find a summary of the selected items in a section defined at the top of the screen.
 * 	This summary is defined by using an <code>MMMultiSelectedFixedListView</code> class that extend an <code>AbstractFixedListView</code>.
 * 	The list is defined by using an  <code>MMMultiSelectedExpandableListView</code> class that extends an <code>MMExpandableListView</code>.
 * </p>
 *
 * <p>Copyright (c) 2012</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
 * @since Baltoro
 */
public class MMMultiSelectedListView<
ITEM extends MIdentifiable, 
SUBITEM extends MIdentifiable, 
SUBITEMVM extends ItemViewModel<SUBITEM>,
ITEMVM extends ExpandableViewModel<ITEM, SUBITEM, SUBITEMVM>> 
extends AbstractMMRelativeLayout<List<SUBITEMVM>> 
implements MMIdentifiableView, OnChildClickListener, SelectedComponent {


	/** the maximum number of selectable item */
	private int maxNumberOfSelectableItem = -1;

	private AndroidApplication application = (AndroidApplication) Application.getInstance();

	private MMMultiSelectedFixedListView<SUBITEM, SUBITEMVM> selected;
	private AbstractConfigurableFixedListAdapter<SUBITEM, SUBITEMVM, ListViewModel<SUBITEM,SUBITEMVM>> selectedAdapter;

	private MMExpandableListView<ITEM,SUBITEM, SUBITEMVM, ITEMVM> expandable;
	private MultiSelectedExpandableListAdapter<ITEM, SUBITEM, SUBITEMVM, ExpandableViewModel<ITEM,SUBITEM,SUBITEMVM>> expandableAdapter;

	private Class<ITEMVM> classItemVm;

	private Class<SUBITEMVM> classSubItemVm;

	/**
	 * Construct a new <code>MMMultiSelectedExpandableListView</code> component.
	 * @param p_oContext 
	 * 		The current context
	 */
	public MMMultiSelectedListView(Context p_oContext) {
		super(p_oContext);
	}

	/**
	 * Construct a new <code>MMMultiSelectedExpandableListView</code> component.
	 * @param p_oContext 
	 * 		The current context
	 * @param p_oAttrs
	 *      xml attributes
	 */
	public MMMultiSelectedListView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext,p_oAttrs);
		if(!isInEditMode()) {
			maxNumberOfSelectableItem=p_oAttrs.getAttributeIntValue(Application.MOVALYSXMLNS, "selectableMax",-1);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()){

			LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
			oLayoutInflater.inflate(application.getAndroidIdByRKey(AndroidApplicationR.fwk_component__multi_selected_expandable_list__panel), this);

			if (maxNumberOfSelectableItem==-1){
				maxNumberOfSelectableItem = ConfigurationsHandler.getInstance().getProperty(FwkPropertyName.default_maxNumberSelectableItem).getIntValue();
			}
			expandable = (MMExpandableListView<ITEM,SUBITEM, SUBITEMVM, ITEMVM>) this.findViewById(application.getAndroidIdByRKey(AndroidApplicationR.component__multi_selected_expandable_list_view__panel));
			selected = (MMMultiSelectedFixedListView) this.findViewById(application.getAndroidIdByRKey(AndroidApplicationR.component__multi_selected_expandable_list_selected_item_view__panel));
			expandable.setOnChildClickListener(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(List<SUBITEMVM> p_oListItem) {
		selectedAdapter.getMasterVM().clear();
		selectedAdapter.getMasterVM().addAll(p_oListItem);
//		selectedAdapter.getMasterVM().setList(p_oListItem);
		selected.configurationSetValue(selectedAdapter.getMasterVM());
		expandableAdapter.notifyDataSetChanged();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_sValues) {
		//Nothing to do 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getValueType() {
		return NoneType.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SUBITEMVM> configurationGetValue() {
		List<SUBITEMVM> r_oList = new ArrayList<>();
		for (int i = 0; i < selectedAdapter.getMasterVM().getCount(); i++) {
			r_oList.add(selectedAdapter.getMasterVM().getCacheVMByPosition(i));
		}
		return r_oList;
//		return selectedAdapter.getMasterVM().getList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] configurationGetCustomValues() {
		//Nothing to do 
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyValue(List<SUBITEMVM> p_oSelectedList) {
		return p_oSelectedList==null||p_oSelectedList.isEmpty();
	}

	/**
	 * Retourne l'adapter de l'<code>ExpandableListView</code>.
	 * @return Objet adapter
	 */
	public MultiSelectedExpandableListAdapter<ITEM, SUBITEM, SUBITEMVM, ExpandableViewModel<ITEM,SUBITEM,SUBITEMVM>> getAdapter() {
		return expandableAdapter;
	}

	/**
	 * Define adapters for the <code>MMMultiSelectedListView</code> component.
	 * In that component we must create two adapters based on the p_oAdapter Object.
	 * One for the selected FixedList and an other for the expandable list.
	 * @param p_oAdapter Adapter for data representation.
	 */
	public void setAdapter(MultiSelectedExpandableListAdapter<ITEM, SUBITEM, SUBITEMVM, ExpandableViewModel<ITEM,SUBITEM,SUBITEMVM>> p_oAdapter, Class<ITEMVM> p_oClassVm, Class<SUBITEMVM> p_oClassSubVm) {
		expandableAdapter=p_oAdapter;
		classItemVm = p_oClassVm;
		classSubItemVm = p_oClassSubVm;
		expandable.setAdapter(expandableAdapter);
		selected.setExpandableAdapter(p_oAdapter);
		selectedAdapter = new MultiSelectedFixedListAdapter<>(p_oAdapter.getChildLayoutId(),p_oAdapter.getConfigurableChildLayoutId(), p_oClassSubVm);
		selected.setAdapter(selectedAdapter);
		expandableAdapter.setSelectedAdapter(selectedAdapter);
	}

	/**
	 * Return the adapter of the <code>FixedListView</code> of selected items.
	 * @param p_oAdapter the Adapter
	 */
	public AbstractConfigurableFixedListAdapter<SUBITEM, SUBITEMVM, ListViewModel<SUBITEM,SUBITEMVM>> getSelectedAdapter() {
		return selected.getAdapter();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onChildClick(ExpandableListView p_oExpandableListView, View p_oClickedView, int p_iGroupPosition, int p_iChildPosition, long p_oParamLong) {
		SUBITEMVM oClickedVM = expandableAdapter.getChild(p_iGroupPosition, p_iChildPosition);
		if (selectedAdapter.getMasterVM()==null){
			selectedAdapter.setMasterVM(new ListViewModelImpl<>(classSubItemVm));
		}
		List<SUBITEMVM> oSelectedList = new ArrayList<>();
		for (int i = 0; i < selectedAdapter.getMasterVM().getCount(); i++) {
			oSelectedList.add(selectedAdapter.getMasterVM().getCacheVMByPosition(i));
		}
//		List<SUBITEMVM> oSelectedList = selectedAdapter.getMasterVM().getList();
		if (oSelectedList.contains(oClickedVM)){
			selectedAdapter.getMasterVM().remove(oClickedVM);
			selected.configurationSetValue(selectedAdapter.getMasterVM());
			expandableAdapter.notifyDataSetChanged();
		}
		else if (oSelectedList.size()<maxNumberOfSelectableItem){
			selectedAdapter.getMasterVM().add(oClickedVM);
			selected.configurationSetValue(selectedAdapter.getMasterVM());
			expandableAdapter.notifyDataSetChanged();
		}
		return false;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomConverter customConverter() {
		return this.aivDelegate.customConverter();
	}

	public void setCustomConverter(String p_oCustomConverter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomConverter(p_oCustomConverter,p_oAttributeSet);		
	}

	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		this.aivDelegate.setCustomConverter(p_oConverter);
	}
	
	@Override
	public void setCustomFormatter(CustomFormatter p_oFormatter) {
		this.aivDelegate.setCustomFormatter(p_oFormatter);
	}
	
	@Override
	public CustomFormatter customFormatter() {
		return this.aivDelegate.customFormatter();
	}
	
	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomFormatter(p_oCustomFormatter,p_oAttributeSet);		
	}
}
