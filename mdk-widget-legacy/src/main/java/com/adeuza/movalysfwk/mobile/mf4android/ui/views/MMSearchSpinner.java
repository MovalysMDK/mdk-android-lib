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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractSearchAutoBindMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMRelativeLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMSpinnerAdapterHolder;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKSpinnerAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.MDKViewConnector;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.spinner.MDKSpinnerConnector;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentMandatoryLabel;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMSearchSpinnerDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ConfigurableListViewHolder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModelFilter;

/**
 * <p>Composant de type Spinner avec en plus une notion de filtre pour limiter l'affichage de la liste de résultat.</p>
 *
 * @param <ITEM> a model for item
 * @param <ITEMVM> a view model for item
 *
 * @since MF-Annapurna
 */
public class MMSearchSpinner<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>> extends AbstractMMRelativeLayout<ITEMVM> 
implements MMComplexeComponent, OnDismissListener, OnClickListener, TextWatcher, MMSpinnerAdapterHolder<ITEM, ITEMVM>, ComponentMandatoryLabel,
ComponentReadableWrapper<ITEMVM>, ComponentWritableWrapper<ITEMVM> {

	/** logging tag */
	private static final String TAG = "MMSearchSpinner";

	/**
	 * Search use empty parameter
	 */
	private static final String USE_EMPTY_ITEM_PARAMETER = "use-empty-item";

	/**
	 * Search activity to launch instead of displaying the search dialog
	 * Optional parameter.
	 */
	private static final String SEARCH_ACTIVITY_PARAMETER = "search-activity";

	/**
	 * Search activity to launch instead of displaying the search dialog
	 * Optional parameter.
	 */
	private static final String SEARCH_ACTIVITY_PORTRAIT_MODE_PARAMETER = "search-activity-portrait-mode";
	/**
	 * Search field if the filter is not custom
	 */
	private static final String SEARCH_FIELD_PARAMETER = "filter-field-name";

	/** the application */
	private AndroidApplication application = (AndroidApplication) Application.getInstance();
	/** le layout qui va récupérer le résultat pour l'afficher lorsqu'on sélectionne un item */
	private RelativeLayout contentLayout = null;
	/** l'adapter pour l'affichage de la liste de résultat correspondant au motif de recherche */
	private MDKSpinnerConnector connector = null;
	/** la vue sélectionnée */
	private View selectedView = null;
	/** permet de définir si la liste du spinner gère le cas vide */
	private boolean hasEmptyValue = true;

	/** la dialog de recherche */
	private MMSearchSpinnerDialogFragment dialog = null;
	/** le titre de la dialog de recherche */
	private String dialogTitle = null;
	/** l'item sélectionné dans la liste */
	private ITEMVM selectedItem = null;
	/** le listener de sélection d'item dans la liste */
	private OnSearchItemSelectedListener listener = null;
	/** boolean pour savoir si le speener est en cours d'utilisation (c.a.d avec la dialog ouverte)*/
	private boolean isDialogVisible = false;
	/** CurrentFilter */
	private String currentFilter;
	
	/**
	 * Search activity class
	 */
	private String searchActivityClass ;

	/**
	 * Search activity portrait mode
	 */
	private String searchActivityPortraitMode;

	/**
	 * Search spinner filter
	 */
	private ListViewModelFilter<ITEM, ITEMVM, String> mSearchSpinnerFilter = null;

	/**
	 * Search Field
	 */
	private String searchFieldFilter;

	/**
	 *  Component request code
	 */
	private int requestCode = -1;
	
	/**
	 * Création d'un nouvel objet SearchSpinner qui permet de filtrer les résultats d'un Spinner.
	 * @param p_oContext || selectedView.getParent() == null
	 */
	public MMSearchSpinner(Context p_oContext) {
		super(p_oContext, ItemViewModel.class);
		if(!isInEditMode()) {
			init();
		}
	}

	/**
	 * Création d'un nouvel objet SearchSpinner qui permet de filtrer les résultats d'un Spinner.
	 * @param p_oContext of another view hierarchy. If you really want to reuse it (I would suggest you probably dont) then you have to detach it from its parent in its existing view hierarchy.
	 * @param p_oAttrs the android view AttributeSet (in layout XML)
	 */
	public MMSearchSpinner(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, ItemViewModel.class);
		if(!isInEditMode()) {
			this.hasEmptyValue = p_oAttrs.getAttributeBooleanValue(Application.MOVALYSXMLNS, USE_EMPTY_ITEM_PARAMETER, true);
			this.searchActivityClass = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, SEARCH_ACTIVITY_PARAMETER);
			this.searchActivityPortraitMode = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, SEARCH_ACTIVITY_PORTRAIT_MODE_PARAMETER);
			this.searchFieldFilter = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, SEARCH_FIELD_PARAMETER);
			init();
		}
	}

	/**
	 * initialisation de l'objet courant.
	 */
	private final void init(){
		LayoutInflater oInflater = LayoutInflater.from(this.getContext());
		oInflater.inflate(application.getAndroidIdByRKey(AndroidApplicationR.fwk_search_spinner_item__master),this);
		this.contentLayout = (RelativeLayout) this.findViewById(application.getAndroidIdByRKey(AndroidApplicationR.search_spinner_text_value));
		
		// Préparation du titre du dialog de recherche
		setDialogTitle(application.getStringResource(AndroidApplicationR.searchSpinnerDialogTitle));
		this.setOnClickListener(this);
	}

	/**
	 * Vide le champ.
	 */
	public void cleanField(){
		if ( this.contentLayout!= null && this.contentLayout.getChildAt(0) != null){
			this.contentLayout.removeViewAt(0);
			this.selectedItem = null;
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMSpinnerAdapterHolder#setAdapter(com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractConfigurableSpinnerAdapter)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setAdapter(MDKSpinnerConnector p_oAdapter){
		this.connector = p_oAdapter;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMSpinnerAdapterHolder#getAdapter()
	 */
	@Override
	public MDKSpinnerConnector getAdapter() {
		return this.connector;
	}

	/**
	 * Est  ce que la liste dois gérer le cas vide ?
	 * @return true on propose une valeure vide, false sinon
	 */
	public boolean hasEmptyValue(){
		return this.hasEmptyValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClick(View p_oParamView) {

		if ( this.searchActivityClass != null ) {
			this.startSearchActivity();
		}
		else if ( p_oParamView == this && !this.isDialogVisible ) {
			this.showSearchDialog();
		}
		else if ( this.dialog.getDialog().isShowing() && this.isDialogVisible ) {
			this.hideSearchDialog( p_oParamView );
		}
	}

	/**
	 * hide the search dialog
	 * @param p_oParamView the clicked view
	 */
	public void internViewClicked(View p_oParamView) {
		this.hideSearchDialog( p_oParamView );
	}
	
	/**
	 * Show search dialog
	 */
	private void showSearchDialog() {
		this.dialog = (MMSearchSpinnerDialogFragment) createDialogFragment(null);

		Bundle oDialogFragmentArguments = new Bundle();
		oDialogFragmentArguments.putInt(MMSearchSpinnerDialogFragment.VIEW_ID_ARGUMENT_KEY, this.application.getAndroidIdByRKey(AndroidApplicationR.fwk_custom_popup));
		oDialogFragmentArguments.putSerializable(MMSearchSpinnerDialogFragment.ITEMVM_ITF_CLASS_ARGUMENT_KEY, this.connector.getItemVMInterface());
		oDialogFragmentArguments.putString("fragmentTag", String.valueOf(this.getId()));
		
		prepareDialogFragment(this.dialog, oDialogFragmentArguments);
		this.dialog.show(getFragmentActivityContext().getSupportFragmentManager(),this.dialog.getFragmentDialogTag());
		this.isDialogVisible=true;
	}

	/**
	 * Returns the fragmentActivit context
	 * 
	 * @return a fragmentActivity context
	 */
	private ActionBarActivity getFragmentActivityContext() {
		return  (ActionBarActivity) ((AndroidApplication) Application.getInstance()).getCurrentActivity();
	}

	/**
	 * hide search dialog
	 * @param p_oParamView clicked view
	 */
	private void hideSearchDialog( View p_oParamView ) {
		// Récupération de la position dans le Tag
		this.dialog.dismiss();
		ITEMVM oItem = null;
		this.isDialogVisible=false;

		// Le tag peut être null dans le cas d'un double clic par exemple
		if (p_oParamView !=null && p_oParamView.getTag() != null){
			oItem=this.getItemVMbyId((ConfigurableListViewHolder) p_oParamView.getTag());
			if (oItem != null) {
				this.aivDelegate.configurationSetValue(oItem);
				this.aivFwkDelegate.changed();
			}else if (selectedItem != null){
				this.aivDelegate.configurationSetValue(null);
				this.aivFwkDelegate.changed();
			}
			if (this.listener != null) {
				this.listener.onItemSelected(this, p_oParamView, this.connector.indexOf(oItem));
			}
		}
	}

	/**
	 * Get on item vm from it's id
	 * @param p_oConfHolder the configuration holder that contains the view model id
	 * @return the view model conresponding to the {@link ConfigurableListViewHolder} in param, null otherwise
	 */
	private ITEMVM getItemVMbyId(ConfigurableListViewHolder p_oConfHolder) {
		for (int i = 0; i < this.getAdapter().getCount() && p_oConfHolder.getViewModelID() != null; i++) {
			ITEMVM oItem = (ITEMVM) this.getAdapter().getItem(i);
			if ( oItem != null && p_oConfHolder.getViewModelID().equals(oItem.getIdVM())) {
				return oItem;
			}
		}
		return null;
	}
	
	/**
	 * Launch new intent to SrearchActivity
	 */
	public void startSearchActivity() {

		try {
			Class<?> oClass = Class.forName(this.searchActivityClass);

			Intent oSearchEntityIntent = new Intent(this.getContext(), oClass );

			if ( this.searchActivityPortraitMode != null ) {
				oSearchEntityIntent.putExtra(AbstractSearchAutoBindMMActivity.PORTRAIT_MODE,
						this.searchActivityPortraitMode);
			}

			Context oContext = this.getContext();
			if ( oContext instanceof ContextWrapper ) {
				oContext = ((ContextWrapper) oContext).getBaseContext();
			}

			((AbstractMMActivity) oContext).startActivityForResult(
					oSearchEntityIntent, this, getRequestCode() );
		} catch( ClassNotFoundException oException ) {
			throw new MobileFwkException(oException);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int p_iRequestCode, int p_iResultCode, Intent p_oIntent) {
		if (p_iRequestCode == getRequestCode() && p_iResultCode == Activity.RESULT_OK ) {
			String sEntity = (String) p_oIntent.getExtras().getString("ENTITY");
			ITEMVM oItemVM = (ITEMVM) this.connector.getItemVMById(sEntity);
			int iSelectedPos = this.connector.getItemVMPos(oItemVM);
			this.aivDelegate.configurationSetValue(oItemVM);
			this.connector.setSelectedPosition(iSelectedPos);
			this.aivFwkDelegate.changed();
		}
	}

	/**
	 * Return the object <em>isDialogVisible</em>.
	 * @return Objet isDialogVisible
	 */
	public boolean isDialogVisible() {
		return this.isDialogVisible;
	}

	/**
	 * Set the object <em>isDialogVisible</em>.
	 * @param p_oIsDialogVisible Objet isDialogVisible
	 */
	public void setDialogVisible(boolean p_oIsDialogVisible) {
		this.isDialogVisible = p_oIsDialogVisible;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeTextChanged(CharSequence p_oParamCharSequence, int p_oParamInt1, int p_oParamInt2, int p_oParamInt3) {
		/* Nothing to do */
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onTextChanged(CharSequence p_oParamCharSequence, int p_oParamInt1, int p_oParamInt2, int p_oParamInt3) {
		this.currentFilter = p_oParamCharSequence.toString();

		if (this.mSearchSpinnerFilter == null) {
			this.mSearchSpinnerFilter = createDefaultFilter();
		}

		if (this.dialog!=null) {
			this.dialog.filter(this.mSearchSpinnerFilter);
		}
	}
	
	/**
	 * Create the default filter for the search spinner
	 * @return the {@link ListViewModelFilter} that filter on a String specified in XML AttributeSet
	 */
	protected ListViewModelFilter<ITEM, ITEMVM, String> createDefaultFilter() {
		return new ListViewModelFilter<ITEM, ITEMVM, String>() {

			@Override
			public boolean filter(ITEM p_oItem, ITEMVM p_oItemVm, String... p_oParams) {
				boolean r_bFiltered = false;
				try {
					// Filter is active when a field is specified
					if (MMSearchSpinner.this.searchFieldFilter != null) {
						r_bFiltered = false;
						if (p_oItem != null) {

							Field oField = p_oItem.getClass().getDeclaredField(MMSearchSpinner.this.searchFieldFilter);
							oField.setAccessible(true);
							if (oField.getType().equals(String.class) && p_oParams.length == 1 ) {
								String sFilter = p_oParams[0].toLowerCase(); 
								String sValue = ((String) oField.get(p_oItem)).toLowerCase();
								if (sValue.contains(sFilter)) {
									r_bFiltered = true;
								}
							}
						}
					} else {
						r_bFiltered = true;
					}
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
					Log.w(TAG, e.getMessage());
					r_bFiltered = true;
				}
				return r_bFiltered;
			}
			
		};
	}

	/**
	 * Set the filter to use to filter ListViewModel
	 * @param p_oFilter the {@link ListViewModelFilter} to set
	 */
	public void setFilter(ListViewModelFilter<ITEM, ITEMVM, String> p_oFilter) {
		this.mSearchSpinnerFilter = p_oFilter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterTextChanged(Editable p_oParamEditable) {
		/* Nothing to do */
	}

	/**
	 * Return the object <em>dialogTitle</em>.
	 * @return Objet dialogTitle
	 */
	public String getDialogTitle() {
		return this.dialogTitle;
	}

	/**
	 * Set the object <em>dialogTitle</em>.
	 * @param p_oDialogTitle Objet dialogTitle
	 */
	public void setDialogTitle(String p_oDialogTitle) {
		this.dialogTitle = p_oDialogTitle;
	}

	/**
	 * Affectation du listener de sélection d'un item dans la liste de recherche.
	 * @param p_oListener le listener qui va écouter la sélection d'items et réagir en fonction
	 */
	public void setOnSearchItemSelectedListener(OnSearchItemSelectedListener p_oListener) {
		this.listener = p_oListener;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDismiss(DialogInterface p_oArg0) {
		isDialogVisible=false;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Parcelable onSaveInstanceState() {
		Bundle oToSave = new Bundle();
		oToSave.putParcelable("parent", super.superOnSaveInstanceState());
		oToSave.putString("class", MMSearchSpinner.class.getName());
		oToSave.putString("Filter",this.currentFilter);
		oToSave.putInt("requestCode",this.requestCode);

		if ( this.selectedItem != null ) {
			try {
				ITEMVM oVMToSave = (ITEMVM) this.selectedItem.getClass().getConstructor().newInstance();
				oVMToSave.cloneVmAttributes(this.selectedItem);
				oToSave.putSerializable("selectedItem", oVMToSave);
			} catch (IllegalArgumentException e) {
				Log.e("MMSearchSpinner", "onSaveInstanceState: selectedItem - IllegalArgumentException", e);
			} catch (SecurityException e) {
				Log.e("MMSearchSpinner", "onSaveInstanceState: selectedItem - SecurityException", e);
			} catch (InstantiationException e) {
				Log.e("MMSearchSpinner", "onSaveInstanceState: selectedItem - InstantiationException", e);
			} catch (IllegalAccessException e) {
				Log.e("MMSearchSpinner", "onSaveInstanceState: selectedItem - IllegalAccessException", e);
			} catch (InvocationTargetException e) {
				Log.e("MMSearchSpinner", "onSaveInstanceState: selectedItem - InvocationTargetException", e);
			} catch (NoSuchMethodException e) {
				Log.e("MMSearchSpinner", "onSaveInstanceState: selectedItem - NoSuchMethodException", e);
			}
		}

		return oToSave;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		if(!(p_oState instanceof Bundle)){
			super.onRestoreInstanceState(p_oState);
			return;
		}

		Bundle savedState = (Bundle) p_oState;
		String sClass = savedState.getString("class");
		if ( !MMSearchSpinner.class.getName().equals(sClass)) {
			super.onRestoreInstanceState(p_oState);
			return;
		}

		super.onRestoreInstanceState(savedState.getParcelable("parent"));
		this.currentFilter = savedState.getString("Filter");
		this.requestCode = savedState.getInt("requestCode");
		
		ITEMVM oSelectedItem = (ITEMVM) savedState.getSerializable("selectedItem");
		if ( oSelectedItem != null ) {
			MDKViewConnector oConnector = (MDKViewConnector) this.getAdapter();
			MDKSpinnerAdapter<?,?,?> oAdapter = (MDKSpinnerAdapter<?,?,?>) oConnector.getAdapter();
			
			for (int i = 0; i < oAdapter.getMasterVM().getCount(); i++) {
				ITEMVM vm = (ITEMVM) oAdapter.getMasterVM().getCacheVMByPosition(i);
				if ( vm.getIdVM().equals(oSelectedItem.getIdVM())) {
					this.selectedItem = vm;
					break;
				}
			}

			if ( this.selectedItem == null ) {
				this.selectedItem = oSelectedItem;
			}
			this.aivDelegate.configurationSetValue(this.selectedItem);
		}
	}

	@Override
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		return MMSearchSpinnerDialogFragment.newInstance(this);
	}

	@Override
	public void prepareDialogFragment(MMDialogFragment p_oDialog,
			Bundle p_oDialogFragmentArguments) {
		p_oDialog.setArguments(p_oDialogFragmentArguments);		
	}

	/**
	 * Set the visible dialog on component
	 * @param p_oSearchSpinnerDialogFragment the dialog
	 */
	public void setDialogVisible(MMSearchSpinnerDialogFragment p_oSearchSpinnerDialogFragment) {
		this.dialog = p_oSearchSpinnerDialogFragment;
		this.isDialogVisible = true;
		
	}

	/**
	 * Getter for the current filter
	 * @return the current {@link ListViewModelFilter}
	 */
	public String getCurrentFilter() {
		return this.currentFilter;
	}

	/**
	 * Get the selected item
	 * @return the selectedItem the selected item
	 */
	public ITEMVM getSelectedItem() {
		return selectedItem;
	}

	/**
	 * Set the selected item
	 * @param p_oSelectedItem the selectedItem to set
	 */
	public void setSelectedItem(ITEMVM p_oSelectedItem) {
		this.selectedItem = p_oSelectedItem;
	}

	/**
	 * Get the dialog linked to the component
	 * @return the dialog the SearchSpinnerDialogFragment
	 */
	public MMSearchSpinnerDialogFragment getDialog() {
		return dialog;
	}

	/**
	 * Get the selected view
	 * @return the selectedView the selected View
	 */
	public View getSelectedView() {
		return selectedView;
	}

	/**
	 * Set the selected view
	 * @param p_oSelectedView the selectedView to set
	 */
	public void setSelectedView(View p_oSelectedView) {
		this.selectedView = p_oSelectedView;
	}

	/************************************************************************************
	 ****************************** Framework delegate callback *************************
	 ************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public ITEMVM configurationGetValue() {
		return this.getSelectedItem();
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(ITEMVM p_oObjectToSet) {
		this.getAdapter().doOnErrorOnSelectedItem(this, null);

		if (p_oObjectToSet == null) {
			this.setSelectedItem(null);
		} else if (this.getAdapter() != null) {
			this.setSelectedItem((ITEMVM) this.getAdapter().getItemVMById(p_oObjectToSet.getIdVM()));
		}

		// Delete de la selectedView. On utilise pas l'objet selectedView pour supprimer 
		// l'instance dans le layout car dans le cadre d'une rotation, l'objet selectedView 
		// change d'id. Du coup on ne le retrouve plus et on ne sait pas le virer
		if (this.contentLayout.getChildAt(0)!= null){
			this.contentLayout.removeViewAt(0);
		}
		int iPos = 0;
		if ( p_oObjectToSet != null ){
			iPos = this.getAdapter().indexOf(this.getSelectedItem());
		}

		this.setSelectedView(this.getAdapter().getView(iPos, null, this));

		// La selected view en retour de l'adapter ne devrait pas avoir de parent
		// ne faire ça que pour le MMSearchSpinner, le MMSpinner a ca propre mécanique interne
		if (this.getSelectedView().getParent()!=null) {
			((ViewGroup) this.getSelectedView().getParent()).removeView(this.getSelectedView());
		}
		this.contentLayout.addView(this.getSelectedView());
		if ( this.getDialog() !=null ) {
			this.getDialog().setSelectedItem(this.getSelectedItem());
		}
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.getSelectedItem() != null;
	}
	
	/**
	 * {@inheritDoc}
	 * @see ComponentMandatoryLabel#setMandatoryLabel(boolean)
	 */
	@Override
	public void setMandatoryLabel(boolean p_bMandatory) {
		String sOriginalLabel = this.getDialogTitle();
		String sComputedMandatoryLabel = this.aivDelegate.computeMandatoryLabel(sOriginalLabel, p_bMandatory);
		this.setDialogTitle(sComputedMandatoryLabel);
	}

	@Override
	public int getRequestCode() {
		if (this.requestCode == -1) {
			this.requestCode = this.hashCode() & REQUEST_CODE_MASK;
		}
		return this.requestCode;
	}
}
