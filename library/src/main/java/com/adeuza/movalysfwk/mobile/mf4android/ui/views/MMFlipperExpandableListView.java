package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractConfigurableFlipperExpandableListAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.MMListAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.FlipperExpandableListConnector.GroupMetadata;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.DataSetObserver;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ExpandableViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModelListener;

/**
 * <p>Adapter for list</p>
 *
 * <p>Copyright (c) 2010
 * <p>Company: Adeuza
 *
 * @author smaitre
 * @param <ITEMVM>
 *
 * @param <LISTVM> a model for list  
 * @param <ITEMVM> a model for item
 */
public class MMFlipperExpandableListView<
ITEM extends MIdentifiable,
SUBITEM extends MIdentifiable,
SUBSUBITEM extends MIdentifiable,
SUBSUBITEMVM extends ItemViewModel<SUBSUBITEM>,
SUBITEMVM extends ExpandableViewModel<SUBITEM, SUBSUBITEM, SUBSUBITEMVM>,
ITEMVM extends ExpandableViewModel<ITEM, SUBITEM, SUBITEMVM>> extends AbstractMMLinearLayout<ListViewModel<ITEM,ITEMVM>> 
implements ListViewModelListener, OnClickListener, MMAdaptableListView {

	/** the adapter for the component*/
	private FlipperExpandableListAdapter currentAdapter=null;

	/** local VM values to deal with changes in a transaction for the save of this component*/
	private ListViewModel<ITEM,ITEMVM> currentList=null;

	/** the next Button on the UI*/
	private Button oUINextButton=null;
	/** the previous Button on the UI*/
	private Button oUIPreviousButton=null;
	/** the title layout in the center of arrows*/
	private ViewGroup oUiTitleLayout=null;
	/** the ViewFlipper component in the layout*/
	private ExpandableListView oUIExpListView=null;
	/** the application */
	private AndroidApplication oApplication=(AndroidApplication)Application.getInstance();


	/** DataSetObserver */
	private DataSetObserver mDataSetObserver;

	/** Current Item */
	private int currentTitle;

	/** Adapter */
	FlipperExpandableListConnector oFlipperExpandableListConnector = null;



	/**
	 * <p>
	 *  Construct an object <em>MMFixedListView</em>.
	 * </p>
	 * @param p_oContext
	 * 		The application context
	 */
	public MMFlipperExpandableListView(Context p_oContext) {
		this(p_oContext,null);
	}

	/**
	 * <p>
	 *  Construct an object <em>MMFixedListView</em>.
	 * </p>
	 * @param p_oContext
	 * 		The application context
	 * @param p_oAttrs
	 * 		parameter to configurate the <em>MMFixedListView</em> object.
	 */
	public MMFlipperExpandableListView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			LayoutInflater oInflater = LayoutInflater.from(this.getContext());
			oInflater.inflate(oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component__flipper_expandable_list__panel), this);
			oUINextButton = (Button) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.flipper_expandable_list__btn_next__button));
			oUINextButton.setOnClickListener(this);
			oUIPreviousButton = (Button) this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.flipper_expandable_list__btn_previous__button));
			oUIPreviousButton.setOnClickListener(this);
			oUIExpListView=(ExpandableListView)this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.flipper_expandable_list_item__list__master_id));
			oUiTitleLayout=(ViewGroup)this.findViewById(oApplication.getAndroidIdByRKey(AndroidApplicationR.flipper_expandable_list__title__layout));
		}
	}

	/**
	 * Define adapter for the flipper and show current child
	 * @param p_oAdapter new adpater
	 */
	public void setAdapter(FlipperExpandableListAdapter p_oAdapter){
		if (this.currentAdapter != null) {
			this.currentAdapter.unregisterDataSetObserver(this.mDataSetObserver);
		}

		if (p_oAdapter != null) {
			this.mDataSetObserver = new AdapterDataSetObserver();
			p_oAdapter.registerDataSetObserver(this.mDataSetObserver);
		}

		this.currentAdapter=p_oAdapter;


		this.oFlipperExpandableListConnector = new FlipperExpandableListConnector(this.currentAdapter ,0);
		oUIExpListView.setAdapter(this.oFlipperExpandableListConnector);
		if (this.currentAdapter instanceof OnChildClickListener) {
			oUIExpListView.setOnChildClickListener((OnChildClickListener) this.currentAdapter);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setListAdapter(MMListAdapter p_oAdapter) {
		this.setAdapter((AbstractConfigurableFlipperExpandableListAdapter<ITEM,SUBITEM,SUBSUBITEM,SUBSUBITEMVM, SUBITEMVM, ITEMVM>) p_oAdapter);
	}

	/**
	 * Get List Adapter
	 *
	 */
	@Override
	public MMListAdapter getListAdapter() {
		return (MMListAdapter) this.currentAdapter;
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable oSuperState = this.aivDelegate.onSaveInstanceState(super.onSaveInstanceState());
		Bundle oState = new Bundle();
		oState.putParcelable("superState", oSuperState);
		oState.putInt("currentIndex", this.currentTitle);

		oState.putSerializable("expandedData", (Serializable) this.oFlipperExpandableListConnector.getTitleExpandableList());
		return oState;
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		Bundle oBundle = (Bundle) p_oState;
		Parcelable oSuperState = oBundle.getParcelable("superState");
		super.onRestoreInstanceState(((BaseSavedState) oSuperState).getSuperState());
		this.aivDelegate.onRestoreInstanceState(oSuperState);
		this.currentTitle = (oBundle.getInt("currentIndex"));
		showCurrentChild();	

		this.oFlipperExpandableListConnector.setTitleExpandableList((Map<Integer, ArrayList<GroupMetadata>>) ((Bundle) p_oState).getSerializable("expandedData"));
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View p_oArg0) {
		if(p_oArg0.getId()==oApplication.getAndroidIdByRKey(AndroidApplicationR.flipper_expandable_list__btn_next__button)){
			this.showNext();
		}
		else if(p_oArg0.getId()==oApplication.getAndroidIdByRKey(AndroidApplicationR.flipper_expandable_list__btn_previous__button)){
			this.showPrevious();
		}

	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.ViewAnimator#showNext()
	 */
	public void showNext() {
		if (hasNext()) {
			this.currentTitle++;
			showCurrentChild();
			this.restoreGroupPosition();
		}
	}

	/**
	 * Has Next
	 *
	 */
	private boolean hasNext() {
		return ((this.currentTitle+1) < this.currentAdapter.getTitleCount());
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.ViewAnimator#showPrevious()
	 */
	public void showPrevious() {
		if (hasPrevious()) {
			this.currentTitle--;
			showCurrentChild();
			this.restoreGroupPosition();
		}
	}

	/**
	 * has Previous
	 *
	 */
	private boolean hasPrevious() {
		return ((this.currentTitle-1) >= 0);
	}

	/**
	 * {@inheritDoc} 
	 * @see android.widget.ViewAnimator#showPrevious()
	 */
	private void restoreGroupPosition(){
		ArrayList<GroupMetadata> listGroupPosition = this.oFlipperExpandableListConnector.getGoupExpandableList(this.currentTitle);
		if (listGroupPosition != null && !listGroupPosition.isEmpty()) {
			for(GroupMetadata positionGroup : listGroupPosition) {
				oUIExpListView.expandGroup(positionGroup.getCurrentID());
			}
		}
	}

	/**
	 * to display the current pane 
	 */
	public void showCurrentChild() {
		updateButtonState();
		createTitleView();
		displayCurrentTitle();

	}

	/**
	 * Set the correct state for previous and next button
	 */
	private void updateButtonState() {
		if (this.hasNext()){
			oUINextButton.setVisibility(View.VISIBLE);
		}
		else{
			oUINextButton.setVisibility(View.INVISIBLE);
		}
		if (this.hasPrevious()){
			oUIPreviousButton.setVisibility(View.VISIBLE);
		}
		else{
			oUIPreviousButton.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * <p>
	 * remove the inflated views and create new ones based on the current ViewModel
	 * </p>
	 */
	protected void displayCurrentTitle(){
		this.oFlipperExpandableListConnector.setCurrentTitleID(this.currentTitle, true);
	}

	/**
	 * This method can be overriden to wrap the components in the
	 * MasterVisualCommponent of the master list
	 * 
	 * @param p_oView
	 *            the current view
	 * @param p_oCurrentViewModel
	 *            The view Model of this item
	 * @param p_iTitlePosition
	 *            The position of the first level of the triple list
	 */
	protected void wrapMasterView(View p_oCurrentRow, ITEMVM p_oCurrentViewModel, int p_iTitlePosition) {
		// nothing to do in this implementation, the automatic binding is used

	}

	/**
	 * create Title View
	 */
	private void createTitleView(){
		//récupère la titleView de l'adapter
		View oTitle=currentAdapter.getTitleView(this.currentTitle, true, oUiTitleLayout.getChildAt(0), oUiTitleLayout);
		if (oTitle.getParent()==null){
			oUiTitleLayout.addView(oTitle);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uimodel.ListViewModelListener#doOnListVMChanged()
	 */
	@Override
	public void doOnListVMChanged() {
		this.displayCurrentTitle();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(ListViewModel<ITEM,ITEMVM> p_oObject) {
		//Dans le cas d'une liste on ne fait rien
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationDisabledComponent() {
		this.aivDelegate.configurationDisabledComponent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationEnabledComponent() {
		this.aivDelegate.configurationEnabledComponent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAlwaysEnabled() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEdit() {
		return this.aivDelegate.isEdit();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFilled() {
		return this.aivDelegate.isFilled();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return this.isShown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationRemoveMandatoryLabel() {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return ExpandableViewModel.class;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationGetValue()
	 */
	@Override
	public ListViewModel<ITEM,ITEMVM> configurationGetValue() {
		return this.currentList;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#configurationGetCustomValues()
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return this.aivDelegate.configurationGetCustomValues();
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(ListViewModel<ITEM,ITEMVM> p_oObject) {
		return p_oObject == null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnsetError() {
		this.aivDelegate.configurationUnsetError();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetError(String p_oError) {
		this.aivDelegate.configurationSetError(p_oError);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {
		return this.aivDelegate.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomConverter customConverter() {
		return this.aivDelegate.customConverter();
	}

	/**
	 * Set Custom Converter
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomFormatter customFormatter() {
		return this.aivDelegate.customFormatter();
	}

	/**
	 * Set Custom Formatter
	 */	
	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomFormatter(p_oCustomFormatter,p_oAttributeSet);		
	}

	/**
	 * Custom Adapter DataSetObserver
	 */		
	class AdapterDataSetObserver extends DataSetObserver {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onChanged() {
			super.onChanged();
			MMFlipperExpandableListView.this.showCurrentChild();
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onInvalidated() {
			super.onInvalidated();
			MMFlipperExpandableListView.this.showCurrentChild();
		}
	}
}
