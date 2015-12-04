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
package com.adeuza.movalysfwk.mobile.mf4android.ui.views;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractAutoBindMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentSavedState;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.WidgetWrapperHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractConfigurableFixedListAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.generic.InParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.Notifier;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.ObservableDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.listener.ConfigurationListener;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.VisualComponentDescriptorsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.AbstractViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListItemRemovable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModelImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModelListener;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * <p>Abstract class defining a fixed list</p>
 *
 *
 * @param <ITEM> a model for item
 * @param <ITEMVM> a model for itemVM
 */
public abstract class AbstractFixedListView<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>>
		extends AbstractMMLinearLayout<ListViewModel<ITEM,ITEMVM>>
		implements ListViewModelListener, OnClickListener, ConfigurationListener, InstanceStatable, MMAdaptableFixedListView<ITEM, ITEMVM> {

	/** Log Tag */
	private static final String TAG = "AbstractFixedListView";

	/** Attr display detail */
	public static final String ATTR_DISPLAY_DETAIL = "displayDetail";

	/** Default display detail */
	public static final boolean DEFAULT_DISPLAY_DETAIL = true;

	/** adapter */
	private AbstractConfigurableFixedListAdapter<ITEM, ITEMVM, ListViewModel<ITEM,ITEMVM>> adapter;

	/** fixed list add button id */
	private int uiAddButtonId;

	/** fixed list delete button id */
	private int uiDeleteButtonId;

	/** fixed list add button view */
	private View uiAddButton;

	/** fixed list title view */
	private TextView titleView;

	/** fixed list title */
	private String specifiedTitle;

	/** fixed list selected VM */
	private ITEMVM selectedVM;

	/** application instance */
	private AndroidApplication androidApplication = (AndroidApplication) Application.getInstance();

	/** delegate */
	private ObservableDelegate<MMFixedListViewListener> odMMFixedListViewListener;

	/** notifier */
	private Notifier oNotifier = null;

	/** used to load the movalys:titlemode attribute that disable the title and + button**/
	private boolean bTitleMode=true;

	/** title View Group */
	private ViewGroup uiTitleViewGroup=null;

	/** Max items in the fixed list, -1 for unlimited */
	private int maxItems = Integer.MAX_VALUE ;

	/** boolean display detail */
	protected boolean displayDetail;

	/** master component map */
	private Map<View, MasterVisualComponent> mastersMap;
	
	/**
	 * <p>
	 *  Construct an object <em>MMFixedListView</em>.
	 * </p>
	 * @param p_oContext The application context
	 * @param p_oAttrs parameter to configure the <em>MMFixedListView</em> object.
	 */
	public AbstractFixedListView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, ListViewModel.class);
		
		int iText = p_oAttrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "text", 0);
		if (iText!=0){
			this.specifiedTitle = this.getResources().getString(iText);
		}
		else{
			this.specifiedTitle = "";
		}
		
		this.bTitleMode = p_oAttrs.getAttributeBooleanValue(Application.MOVALYSXMLNS, "titleMode", true);
		this.maxItems = p_oAttrs.getAttributeIntValue(Application.MOVALYSXMLNS, "maxItems", Integer.MAX_VALUE);
		this.displayDetail = p_oAttrs.getAttributeBooleanValue(Application.MOVALYSXMLNS, ATTR_DISPLAY_DETAIL, DEFAULT_DISPLAY_DETAIL);
		
		mastersMap = new HashMap<View, MasterVisualComponent>();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()){
			final BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) this.aivFwkDelegate.getConfiguration();
			if (oConfiguration != null && oConfiguration.getLabel() != null) {
				this.specifiedTitle = oConfiguration.getLabel();
			}

			this.odMMFixedListViewListener = new ObservableDelegate<>();
			this.oNotifier = new Notifier();

			this.setOrientation(VERTICAL);
			
			this.linkChildren();
			this.titleMode();
		}
	}

	/**
	 * Inflate component layout
	 */
	private void linkChildren() {
		this.uiAddButtonId = androidApplication.getAndroidIdByRKey(AndroidApplicationR.component__fixedlist_addButton__button);
		this.uiDeleteButtonId = androidApplication.getAndroidIdByRKey(AndroidApplicationR.component__fixedlist_item_deleteButton__button);
		
		LayoutInflater oInflater = LayoutInflater.from(this.getContext());
		oInflater.inflate(this.getMasterLayout(), this);
		
		this.uiAddButton = this.findViewById(this.uiAddButtonId);
		this.titleView = (TextView) this.findViewById(androidApplication.getAndroidIdByRKey(AndroidApplicationR.component__fixedlist_title__value));
		//this.uiListView = (ViewGroup) this.findViewById(androidApplication.getAndroidIdByRKey(AndroidApplicationR.component__fixedlist__edit));
		this.uiTitleViewGroup = (ViewGroup) this.findViewById(androidApplication.getAndroidIdByRKey(AndroidApplicationR.component__fixedlist_title__group));
	}
	
	/**
	 * Change title mode
	 */
	private void titleMode() {
		if (this.bTitleMode){
			this.uiAddButton.setOnClickListener(this);
			this.uiTitleViewGroup.setVisibility(View.VISIBLE);
			this.uiAddButton.setVisibility(View.VISIBLE);
			this.titleView.setVisibility(View.VISIBLE);
		}
		else{
			this.uiTitleViewGroup.setVisibility(View.GONE);
			this.uiAddButton.setVisibility(View.GONE);
			this.titleView.setVisibility(View.GONE);
		}
	}
	
	/**
	 * Getter for selected VM
	 * @return the selected VM
	 */
	public ITEMVM getSelectedVM() {
		return this.selectedVM;
	}
	
	/**
	 * Getter application
	 * @return the Application instance
	 */
	public AndroidApplication getAndroidApplication() {
		return this.androidApplication;
	}
	/**
	 * Getter delete button id
	 * @return the delete button id
	 */
	public int getUiDeleteButtonId() {
		return this.uiDeleteButtonId;
	}
	
	/**
	 * setter selected VM
	 * @param p_oSelectedVM the ITEMVM to set
	 */
	protected void setSelectedVM(ITEMVM p_oSelectedVM) {
		this.selectedVM = p_oSelectedVM;
	}

	/**
	 * Setter adapter
	 * @param p_oAdapter the adapter to set
	 */
	@Override
	public void setAdapter(AbstractConfigurableFixedListAdapter<ITEM, ITEMVM, ListViewModel<ITEM,ITEMVM>> p_oAdapter) {
		this.adapter = p_oAdapter;
	}

	/**
	 * Getter adapter
	 * @return the adapter
	 */
	@Override
	public AbstractConfigurableFixedListAdapter<ITEM, ITEMVM, ListViewModel<ITEM,ITEMVM>> getAdapter() {
		return this.adapter;
	}

	/**
	 * Returns the number of items in the list
	 * @return the number of items in the list
	 */
	public int getCount() {
		return this.adapter.getMasterVM().getCount();
	}

	/**
	 * Returns the item found at a given position
	 * @param p_iParamInt the position to look an item for
	 * @return the item found
	 */
	public ViewModel getItem(int p_iParamInt) {
		return this.adapter.getMasterVM().getCacheVMByPosition(p_iParamInt);
	}

	/**
	 * Returns the item with the given identifier
	 * @param p_iParamInt the identifier of the item looked for
	 * @return the identifier of the found item
	 */
	public long getItemId(int p_iParamInt) {
		return p_iParamInt;
	}

	/**
	 * Get title mode
	 * @return true if title is shown, false otherwise
	 */
	public boolean getTitleMode() {
		return bTitleMode;
	}
	
	/**
	 * Get display detail
	 * @return true if detail is display, false otherwise
	 */
	public boolean getDisplayDetail() {
		return this.displayDetail;
	}
	
	/**
	 * 
	 * This method can be overriden to wrap the components in the MasterVisualCommponent
	 * @param p_oView the current view
	 * @param p_oCurrentViewModel The view Model of this item
	 */
	protected void wrapCurrentView(View p_oView, ITEMVM p_oCurrentViewModel) {
		if (p_oCurrentViewModel != null) {
			View oSupprButton = p_oView.findViewById(this.uiDeleteButtonId);
			
			boolean bRemovable = true ;
			if ( p_oCurrentViewModel instanceof ListItemRemovable) {
				ListItemRemovable oListItemRemovable = (ListItemRemovable) p_oCurrentViewModel ;
				bRemovable = oListItemRemovable.canBeRemoveFromList();
			}
			
			if (this.isEnabled() && bRemovable ) {
				oSupprButton.setTag(p_oCurrentViewModel);
				oSupprButton.setOnClickListener(this);
				oSupprButton.setVisibility(View.VISIBLE);
			} else {
				oSupprButton.setVisibility(View.GONE);
			}

			if (this.displayDetail) {
				p_oView.setOnClickListener(this);
			}
		}
	}

	/**
	 * 
	 * This method can be overriden to wrap the components in the MasterVisualCommponent
	 * @param p_oView the current view
	 * @param p_oObject the current ITEMVM
	 */
	protected void unWrapCurrentView(View p_oView, ITEMVM p_oObject){
		//nothing to do in this implementation, the automatic mapping is used
	}

	/**
	 * <p>
	 * 	A2A_DOC - Describe method <em>createView</em> of class <em>MMFixedListView</em>.
	 * </p>
	 * @param p_oViewModel the VM to create associate View
	 * @return the view corresponding to the VM
	 */
	public View createView(ITEMVM p_oViewModel) {
		AbstractAutoBindMMActivity oActivity = (AbstractAutoBindMMActivity) this.getContext();

		LayoutInflater oInflater = LayoutInflater.from(this.getContext());
		
		ViewGroup oCurrentRow = (ViewGroup) oInflater.inflate(this.adapter.getLayout(p_oViewModel), null);
		ViewGroup oItem = (ViewGroup) oInflater.inflate(androidApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component__fixedlist_item__master), oCurrentRow , true );

		//on bloque le système de sauvegarde des onSaveInstanceState et onRestoreInstanceState des fils
		//car la fixedList sauvegarde tout le ViewModel dans son #onSaveInstanceState 
		// (sinon, les composant ayant plusieurs fois le même id s'écrasent mutuellement les valeurs dans le parcelable)
		setSaveEnableRecursive(oItem,false);
		//Peut-être remplacé par la méthode setSaveFromParentEnabled(false) à partir de l'API 11 version Android 4.1.1.4

		View oRoot = oItem.findViewById(this.adapter.getConfigurableLayout(p_oViewModel));
		MasterVisualComponent oCompositeComponent = (MasterVisualComponent) WidgetWrapperHelper.getInstance().createWrapper(oRoot, "MMMasterLayoutWrapper");
		oCompositeComponent.getComponentFwkDelegate().setFragmentTag(this.aivFwkDelegate.getFragmentTag());
		oCompositeComponent.setDescriptor(VisualComponentDescriptorsHandler.getInstance().getRealDescriptor(oCompositeComponent.getComponentFwkDelegate().getName()));
		oCompositeComponent.setViewModel(p_oViewModel);		
		oCompositeComponent.getDescriptor().inflate(String.valueOf(oItem.hashCode()),oCompositeComponent, null,oCompositeComponent.getComponentFwkDelegate().getFragmentTag());

		mastersMap.put(oCurrentRow, oCompositeComponent);
		
		this.wrapCurrentView(oItem, p_oViewModel);
//ABE		p_oViewModel.writeData(null, oActivity.getParameters());
		p_oViewModel.doOnDataLoaded(oActivity.getParameters());

		return oCurrentRow;
	}	

	/**
	 * Enable recursive Save
	 * @param p_oView the view to save
	 * @param p_oState the state
	 */
	public void setSaveEnableRecursive(View p_oView, boolean p_oState){
		p_oView.setSaveEnabled(false);
		if (p_oView instanceof ViewGroup){
			int i=0;
			while (i<((ViewGroup)p_oView).getChildCount()){
				setSaveEnableRecursive(((ViewGroup)p_oView).getChildAt(i), p_oState);
				i++;
			}
		}
	}

	/**
	 * Destroys the view passed in parameter
	 * @param p_oCurrentRow the view to destroy
	 */
	public void destroyView(View p_oCurrentRow) {
		// il faut récupérer le noeud principal
		@SuppressWarnings("unchecked")
		ITEMVM oViewModel = (ITEMVM) p_oCurrentRow.getTag();
		MasterVisualComponent oCompositeComponent = mastersMap.get(p_oCurrentRow);
		oCompositeComponent.getDescriptor().unInflate(String.valueOf(p_oCurrentRow.hashCode()), oCompositeComponent, null);
		unWrapCurrentView(p_oCurrentRow, oViewModel);
	}

	/**
	 * <p>
	 * 	A2A_DOC DMA Describe method <em>update</em> of class <em>MMFixedListView</em>.
	 * </p>
	 */
	public void update() {
		int iNbChilds = this.getItemCount();

		View oChildView; // on ne supprime plus le 1er qui est le titre
		for (int i=0; i < iNbChilds; i++) {
			oChildView = this.getItemAt(i);
			this.destroyView(oChildView);
		}
		this.removeItems();

		if (this.adapter.getMasterVM() != null) {
			
			synchronized (this.adapter.getMasterVM()) {
				
				// TODO optimze ???
				for (int i = 0; i < this.adapter.getMasterVM().getCount(); i++) {
					oChildView=this.createView(this.adapter.getMasterVM().getCacheVMByPosition(i));
					oChildView.setTag(this.adapter.getMasterVM().getCacheVMByPosition(i));
					this.addItem(oChildView);
				}
			}
			
		}
		
		this.updateTitle();
		this.updateAddButton();
		AbstractMMActivity oActivity = (AbstractMMActivity) this.getContext();
		oActivity.replayOnActivityResults();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnListVMChanged() {
		this.update();
		
	}

	/**
	 * Action on click add button
	 * @param p_oItemVM the ITEMVM clicked
	 */
	protected void doOnClickAddButton( ITEMVM p_oItemVM ) {
		if ( p_oItemVM == null ) {
			this.selectedVM = this.adapter.createEmptyVM();
		}
		else {
			this.selectedVM = p_oItemVM ;
		}
	}
	
	/**
	 * Suppression d'un détail
	 * @param p_oView the clicked view
	 */
	protected void doOnClickDeleteButtonOnItem(View p_oView) {
		ITEMVM oItemVM = (ITEMVM) p_oView.getTag();
		if (oItemVM != null) {
			//this.masterVM.unregister(p_sKey)unregister(oItemVM);
			// des composants à supprimée
			this.adapter.remove(oItemVM);
			((AbstractViewModel) this.adapter.getMasterVM()).setDirectlyModified(true);
			//TODO SMA : à revoir en contradiction avec la notion de listener
			this.doOnListVMChanged();

			InParameter oActionParameters = new InParameter();
			oActionParameters.vm = oItemVM;
			
			this.odMMFixedListViewListener.doOnNotification("onRemove", this.oNotifier, this, this.adapter.getMasterVM().getCount());
			this.updateTitle();
			this.updateAddButton();
		}
	}

	/**
	 * Action on cancel
	 */
	protected void doCancelItem() {
		this.selectedVM = null;
	}
	
	/**
	 * Valid a item of the FixedList
	 * @param p_bIsEditing is the item is in edit mode
	 * @param p_iIndexOfItem the index of the item
	 * @param p_oViewModel the ITEMVM to valid
	 * @return true if the item is valid
	 */
	protected boolean doValidItem(boolean p_bIsEditing, int p_iIndexOfItem, ITEMVM p_oViewModel) {
		Map<String, Object> mapParameters = new HashMap<>();
		mapParameters.put(AbstractConfigurableFixedListAdapter.SELECTED_VM, this.selectedVM);
		
		final boolean bValidItem = !p_oViewModel.validComponents(null,mapParameters);

		if (bValidItem) {
			this.setSelectedVM(p_oViewModel);

			// nouvel item
			if (!p_bIsEditing) {
				this.adapter.add(this.selectedVM);
			}
			else { // modifiation item
				
				if(p_iIndexOfItem == -1) {
					Log.e("MMFixedListView","The item has not been updated because it was not found in the adapter");
					return false;
				}
				
				this.adapter.getMasterVM().update(p_iIndexOfItem, getSelectedVM());
			}
			((AbstractViewModel) this.adapter.getMasterVM()).setDirectlyModified(true);

			this.updateTitle();
			this.updateAddButton();

			InParameter oActionParameters = new InParameter();
			oActionParameters.vm = this.selectedVM;
			
			this.doOnListVMChanged();
			
			this.odMMFixedListViewListener.doOnNotification("onAdd", this.oNotifier, this, this.adapter.getMasterVM().getCount());
		}
		return bValidItem;
	}

	/**
	 * Compare list with ITEMVM param
	 * @param p_oViewModel the ITEMVM to compare
	 * @return true if the adapter contain a ITEMVM with the param id
	 */
	public boolean containsVMWithId(ITEMVM p_oViewModel) {
		int index = this.adapter.getMasterVM().indexOf(p_oViewModel);
		return index !=-1;
	}

	/**
	 * Action on selection
	 * @param p_oView the selected view
	 */
	protected void doOnClickSelectItem(View p_oView) {
		ITEMVM oItemVM = (ITEMVM) p_oView.getTag();
		if (oItemVM != null) {
			this.doOnClickSelectItem(oItemVM);
		}
	}

	/**
	 * Action on select
	 * @param p_oItemVM the selected ITEMVM
	 */
	protected void doOnClickSelectItem(ITEMVM p_oItemVM) {
		this.selectedVM = p_oItemVM;
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View p_oView) {
		if (p_oView != null) {
			this.readAllItems();

			int iViewId = p_oView.getId();
			if (this.uiDeleteButtonId == iViewId) { // Suppression
				this.doOnClickDeleteButtonOnItem(p_oView);
			} else if (this.uiAddButtonId == iViewId) { // Ajout
				this.doOnClickAddButton(null);
			} else if (treatCustomButton(p_oView)) {
				//Nothing to do : le traitement se trouve dans la méthode treatCustomButton
				Application.getInstance().getLog().debug(TAG,"onClick treatCustomButton ");
			} else {
				this.doOnClickSelectItem(p_oView);
			}
		}
	}
	
	/**
	 * Customize custom button
	 * @param p_oView the view of the button
	 * @return true if the custom button is treated
	 */
	public abstract boolean treatCustomButton(View p_oView);
	
	/**
	 * Register a listener
	 * @param p_oMMFixedListViewListener the listener to register
	 */
	public void register(MMFixedListViewListener p_oMMFixedListViewListener) {
		this.odMMFixedListViewListener.register(p_oMMFixedListViewListener);

		if (this.aivDelegate.isFilled()) {
			this.odMMFixedListViewListener.doOnNotification("onAdd", this.oNotifier, this, this.adapter.getMasterVM().getCount());
		}
	}

	/**
	 * Unregister a listener
	 * @param p_oMMFixedListViewListener the listener to unregister
	 */
	public void unregister(MMFixedListViewListener p_oMMFixedListViewListener) {
		this.odMMFixedListViewListener.unregister(p_oMMFixedListViewListener);
	}


	/**
	 * Get the notifier
	 * @return the notifier.
	 */
	public Notifier getNotifier() {
		return this.oNotifier;
	}

	/**
	 * Get the FixedListListeners
	 * @return the listeners
	 */
	public ObservableDelegate<MMFixedListViewListener> getFixedListViewListeners() {
		return this.odMMFixedListViewListener;
	}

	/**
	 * Update the title
	 */
	public void updateTitle() {
		if (this.titleView != null) {
			int iPos ; 
			if (this.adapter == null || this.adapter.getMasterVM() == null){
				iPos = 0 ;
			}else{
				iPos = this.adapter.getMasterVM().getCount() ;
			}			
			this.titleView.setText(this.computeTitle(this.specifiedTitle, iPos));
		}
	}

	/**
	 * Construct the title
	 * @param p_sDefaultTitle the default title
	 * @param p_iCount the item count
	 * @return the updated title
	 */
	protected String computeTitle(String p_sDefaultTitle, int p_iCount) {
		return new StringBuilder(p_sDefaultTitle).append(" (").append(p_iCount).append(')').toString();
	}

	/**
	 * update the add button
	 */
	protected void updateAddButton() {
		if ( bTitleMode && this.adapter != null) {
			// Hide the add button if max items is reached.
			if (this.adapter.getMasterVM() != null  && this.adapter.getMasterVM().getCount() >= this.maxItems) {
				this.uiAddButton.setVisibility(View.INVISIBLE);
			}else if (!this.isEnabled()) {
				this.uiAddButton.setVisibility(View.GONE);
			}else {
				this.uiAddButton.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * Retourne l'objet titleView
	 * @return Objet titleView
	 */
	public TextView getTitleView() {
		return this.titleView;
	}
	/**
	 * Affecte l'objet titleView 
	 * @param p_oTitleView Objet titleView
	 */
	protected void setTitleView(TextView p_oTitleView) {
		this.titleView = p_oTitleView;
	}

	/**
	 * Getter of item at index
	 * @param p_iIndex the index to get item
	 * @return the view at the specified index
	 */
	public View getItemAt(final int p_iIndex) {
		return this.getChildAt(p_iIndex + 1);
	}

	/**
	 * count items
	 * @return the number of item
	 */
	public int getItemCount() {
		return this.getChildCount() - 1;
	}

	/**
	 * Remove an item
	 */
	public void removeItems() {
		this.removeViews(1, this.getItemCount());
	}

	/**
	 * Add an item
	 * @param p_oView the view to add
	 */
	protected void addItem(View p_oView) {
		this.addView(p_oView);
	}

	/**
	 * Getter master layout
	 * @return the layout id
	 */
	protected int getMasterLayout() {
		return this.androidApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component__fixedlist__master);
	}
	/**
	 * Setter ui add button id
	 * @param p_iUiAddButtonId the add button id
	 */
	public void setUiAddButtonId(int p_iUiAddButtonId) {
		this.uiAddButtonId = p_iUiAddButtonId;
	}
	/**
	 * Setter ui add button view
	 * @param p_oUiAddButton the add button view
	 */
	public void setUiAddButton(View p_oUiAddButton) {
		this.uiAddButton = p_oUiAddButton;
	}

	///////////////////////////////////////////////////
	///// Gestion de sauvegarde et restauration ///////
	///////////////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		Log.d(TAG, "[onSaveInstanceState]");
		
		Bundle oToSave = new Bundle();
		oToSave.putParcelable("parent", super.superOnSaveInstanceState());
		oToSave.putString("class", AbstractFixedListView.class.getName());
		
		if ( this.adapter != null && this.adapter.getMasterVM() != null){

			// ICI en crééant un nouveau ListViewModel, on casse l'affiliation au parent.
			// Donc l'instance VM parent existe toujours avec l'ancienne référence et 
			// n'est jamais liée à cette nouvelle référence.
			// Ceci est normal tans que la vue n'est pas restaurée.
			ListViewModel<ITEM, ITEMVM> oSaveMasterVM = new ListViewModelImpl<>(this.adapter.getItemVMInterface(), false);

			// Store the parent VM to bind the new ListViewModel if a restore is triggered
			if (this.adapter.getMasterVM().getParent() != null) {
//				Class oFirstInterface = this.adapter.getMasterVM().getParent().getClass().getInterfaces()[0];
//				ViewModel oPVM = Application.getInstance().getViewModelCreator().getViewModel(oFirstInterface);
				
				oToSave.putString("masterVMParentClass", this.adapter.getMasterVM().getParent().getClass().getName());

				for(Method oMethod : this.adapter.getMasterVM().getParent().getClass().getDeclaredMethods() ) {
					try {
						if ( oMethod!=null 
								&& !oMethod.getReturnType().equals(void.class) 
								&& !oMethod.getReturnType().equals(Void.class) 
								&& this.adapter.getMasterVM().equals(oMethod.invoke(this.adapter.getMasterVM().getParent()))) {
							
							oToSave.putString("masterVMParentClassMethodName", oMethod.getName());
							break;
						}
					} catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e) {
						Log.e(TAG, e.getMessage());
					}
				}
			}
			// TODO optimize ??? (not nessecary)
			for (int i = 0; i < this.adapter.getMasterVM().getCount(); i++) {
				try {
					ITEMVM oItemVM = this.adapter.getMasterVM().getCacheVMByPosition(i);
					ITEMVM oVMToSave = (ITEMVM) oItemVM.getClass().getConstructor().newInstance();
					oVMToSave.cloneVmAttributes(oItemVM);
					oSaveMasterVM.add(oVMToSave, true);
				} catch (IllegalArgumentException|SecurityException|InstantiationException|IllegalAccessException|InvocationTargetException|NoSuchMethodException e) {
					Log.e(TAG, "onSaveInstanceState: MasterVM ", e);
				} 
			}
			oToSave.putSerializable("listVM", oSaveMasterVM);
			oToSave.putBoolean("changed", this.adapter.getMasterVM().isDirectlyModified());

			if ( this.selectedVM != null ) {
				ITEMVM oVMToSave = this.adapter.createEmptyVM();
				oVMToSave.cloneVmAttributes(this.selectedVM);
				int pos = this.adapter.getMasterVM().indexOf(this.selectedVM);
				oToSave.putInt("selectedVMPosition", pos);
				oToSave.putSerializable("selectedVM", oVMToSave);
			}
			
		} else {
			Log.e(TAG, this.getClass().getSimpleName()+"@"+this.getId()+" -> onSaveInstanceState : adapter.getMasterVM est null !!!");
		}
		//les fils ne doivent pas sauvegarder leur état par le #onSaveInstanceState 
		//car la fixed list sauvegarde tout le viewModel
		//le create view fait un setSaveFromParentEnabled(false) sur chaque item de la liste

		return oToSave;
	}
	/**
	  * Mise à jour des VM avec les données de l'UI pour éviter de perdre des données lors de la rotation
	  */
	private void readAllItems(){
		// TODO broked something??
//		ListViewModel<ITEM, ITEMVM> oMasterVM = this.adapter.getMasterVM();
//		if ( oMasterVM != null &&  oMasterVM.isEditable() && oMasterVM.getList() != null ){
//			Map<String, Object> mapParameters ;
//			for ( ITEMVM oItem : oMasterVM.getList() ){
//				mapParameters = new HashMap<String, Object>();
//				mapParameters.put(AbstractConfigurableFixedListAdapter.SELECTED_VM, oItem );
//				// useless with sync VM
////				oItem.readData(null, mapParameters);
//			}
//		}
	}

	/**
	 * {@inheritDoc}onRestoreInstanceState
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		Log.d(TAG, "[onRestoreInstanceState] state :"+p_oState);

		if(!(p_oState instanceof Bundle)){
			super.onRestoreInstanceState(p_oState);
			return;
		}
		Bundle oSavedState = (Bundle) p_oState;
		String sClass = oSavedState.getString("class");
		if ( !AbstractFixedListView.class.getName().equals(sClass)) {
			super.onRestoreInstanceState(p_oState);
			return;
		}

		super.onRestoreInstanceState(oSavedState.getParcelable("parent"));

		if (this.adapter != null) {
			this.adapter.setMasterVM((ListViewModel<ITEM, ITEMVM>) oSavedState.getSerializable("listVM"));
			// link the restores masterVM to is parent class
			try {
				if (oSavedState.getString("masterVMParentClass") != null) {
					Class oParentVMClass = Class.forName(oSavedState.getString("masterVMParentClass"));
					if (oParentVMClass != null) {
						Method oMethod = null;
						// Risky...
						oMethod = oParentVMClass.getDeclaredMethod(oSavedState.getString("masterVMParentClassMethodName").replace("get", "set"), this.adapter.getMasterVM().getClass().getInterfaces()[0]);
	
						ViewModel oParent = null;
						for(Class<? extends ViewModel> oInterface : oParentVMClass.getInterfaces()) {
							oParent = Application.getInstance().getViewModelCreator().getViewModel(oInterface);
							if (oParent !=null) {
								break;
							}
						}
						this.adapter.getMasterVM().setParent((AbstractViewModel) oParent);
						if (oMethod !=null) {
							oMethod.invoke(oParent, this.adapter.getMasterVM());
						}
					}
				}
			} catch (ClassNotFoundException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				Log.e(TAG, "parentVM class not found : "+e);
			}
		}

		if (this.adapter != null && this.adapter.getMasterVM() != null) {
			this.aivDelegate.configurationSetValue(this.adapter.getMasterVM());

			if (Boolean.TRUE.equals(oSavedState.getBoolean("changed"))) {
				this.adapter.getMasterVM().setDirectlyModified(true);
			}

			int iPos = oSavedState.getInt("selectedVMPosition", -1);
			ITEMVM oSelectedVm = (ITEMVM) oSavedState.getSerializable("selectedVM");
			if ( oSelectedVm != null ) {
				// if pos is -1 it's a new item (not in list)
				if (iPos == -1) {
					this.setSelectedVM(oSelectedVm);
				} else {
					// the position is always correct (if not, it's a bug)
					ITEMVM oVm = this.getAdapter().getMasterVM().getCacheVMByPosition(iPos);
					if ( oVm != null && oVm.getIdVM().equals(oSelectedVm.getIdVM())) {
						this.setSelectedVM(oVm);
					} else {
						throw new RuntimeException("[AbstractFixedList] item to restore is not in the list");
					}
				}
			}
		}
	}
	
	/**
	 * SavedState object
	 * @param <ITEMVM> the item type
	 */
	public static class FixedListViewSavedState<ITEMVM> extends AndroidConfigurableVisualComponentSavedState{
		/** list of ITEMVM to save */
		List <ITEMVM> savedListVM;
		
		/**
		 * Getter savedListVM
		 * @return the savedListVM
		 */
		public List<ITEMVM> getSavedListVM() {
			return savedListVM;
		}

		/**
		 * Setter savedListVm
		 * @param p_oSavedListVM the savedListVM to set
		 */
		public void setSavedListVM(List<ITEMVM> p_oSavedListVM) {
			this.savedListVM = p_oSavedListVM;
		}

		/**
		 * Constructor
		 * @param p_oSuperState the Parceleable build
		 */
		public FixedListViewSavedState(Parcelable p_oSuperState) {
			super(p_oSuperState);
		}

		/**
		 * Constructor
		 * @param p_oInParcel the Parcel to build
		 */
		public FixedListViewSavedState(Parcel p_oInParcel) {
			super(p_oInParcel);
		}
		
		@Override
		public void writeToParcel(Parcel p_oOutParcel, int p_iFlags) {
			super.writeToParcel(p_oOutParcel, p_iFlags);
		} 

		/**
		 * Creator
		 */
		public static final Parcelable.Creator<FixedListViewSavedState> CREATOR
				= new Parcelable.Creator<FixedListViewSavedState>() {
						@Override
						public FixedListViewSavedState createFromParcel(Parcel p_oInParcel) {
							return new FixedListViewSavedState(p_oInParcel);
						}

						@Override
						public FixedListViewSavedState[] newArray(int p_iSize) {
							return new AbstractFixedListView.FixedListViewSavedState[p_iSize];
						}
				};
	}

	/************************************************************************************************************************
	 ******************************************* Framework delegate callback ************************************************
	 ************************************************************************************************************************/
}
