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

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractSearchAutoBindMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMRelativeLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMSpinnerAdapterHolder;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMSearchSpinnerDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractConfigurableSpinnerAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ConfigurableListViewHolder;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModelFilter;

/**
 * <p>Composant de type Spinner avec en plus une notion de filtre pour limiter l'affichage de la liste de résultat.</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 *
 * @author fbourlieux
 * @since MF-Annapurna
 */
public class MMSearchSpinner<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>> extends AbstractMMRelativeLayout<ITEMVM> 
implements MMComplexeComponent, OnDismissListener, OnClickListener, TextWatcher, MMSpinnerAdapterHolder<ITEM, ITEMVM> {

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
	/**
	 * Search entity code
	 */
	private static final int SEARCH_ENTITY_CODE = Math.abs("SEARCH_ENTITY_CODE".hashCode());

	/** the application */
	private AndroidApplication application = (AndroidApplication) Application.getInstance();
	/** le layout qui va récupérer le résultat pour l'afficher lorsqu'on sélectionne un item */
	private MMRelativeLayout contentLayout = null;
	/** l'adapter pour l'affichage de la liste de résultat correspondant au motif de recherche */
	private AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, ListViewModel<ITEM,ITEMVM>> adapter = null;
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
	/** boolean pour savoir si rotation est en cours */
	private boolean isOnRestoreInstanceState = false;
	
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
	 * Création d'un nouvel objet SearchSpinner qui permet de filtrer les résultats d'un Spinner.
	 * @param p_oContext || selectedView.getParent() == null
	 */
	public MMSearchSpinner(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			init();
		}
	}

	/**
	 * Création d'un nouvel objet SearchSpinner qui permet de filtrer les résultats d'un Spinner.
	 * @param p_oContextt of another view hierarchy. If you really want to reuse it (I would suggest you probably dont) then you have to detach it from its parent in its existing view hierarchy.
	 * @param p_oAttrs
	 */
	public MMSearchSpinner(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
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
		this.contentLayout = (MMRelativeLayout) this.findViewById(application.getAndroidIdByRKey(AndroidApplicationR.search_spinner_text_value));
		
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
	public void setAdapter(AbstractConfigurableSpinnerAdapter p_oAdapter){
		this.adapter = p_oAdapter;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMSpinnerAdapterHolder#getAdapter()
	 */
	@Override
	public AbstractConfigurableSpinnerAdapter<ITEM, ITEMVM, ListViewModel<ITEM, ITEMVM>> getAdapter() {
		return this.adapter;
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

	public void internViewClicked(View p_oParamView) {
		this.hideSearchDialog( p_oParamView );
	}
	
	/**
	 * Show search dialog
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	private void showSearchDialog() {
		this.dialog = (MMSearchSpinnerDialogFragment) createDialogFragment(null);

		Bundle oDialogFragmentArguments = new Bundle();
		oDialogFragmentArguments.putInt(MMSearchSpinnerDialogFragment.VIEW_ID_ARGUMENT_KEY, this.application.getAndroidIdByRKey(AndroidApplicationR.fwk_custom_popup));
		oDialogFragmentArguments.putSerializable(MMSearchSpinnerDialogFragment.ITEMVM_ITF_CLASS_ARGUMENT_KEY, this.adapter.getItemVMInterface());
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
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void hideSearchDialog( View p_oParamView ) {
		// Récupération de la position dans le Tag
		this.dialog.dismiss();
		ITEMVM oItem = null;
		this.isDialogVisible=false;

		// Le tag peut être null dans le cas d'un double clic par exemple
		if (p_oParamView !=null && p_oParamView.getTag() != null){
			oItem=this.getItemVMbyId((ConfigurableListViewHolder) p_oParamView.getTag());
			if (oItem != null) {
				this.configurationSetValue(oItem);
				this.aivDelegate.changed();
			}else if (selectedItem != null){
				this.configurationSetValue(null);
				this.aivDelegate.changed();
			}
			if (this.listener != null) {
				this.listener.onItemSelected(this, p_oParamView, this.adapter.indexOf(oItem));
			}
		}
	}

	private ITEMVM getItemVMbyId(ConfigurableListViewHolder p_oConfHolder) {
		for (int i = 0; i < this.getAdapter().getCount() && p_oConfHolder.viewModelID != null; i++) {
			ITEMVM oItem = (ITEMVM) this.getAdapter().getItem(i);
			if ( oItem != null && p_oConfHolder.viewModelID.equals(oItem.getIdVM())) {
				return oItem;
			}
		}
		return null;
	}
	
	/**
	 * 
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
					oSearchEntityIntent, this, SEARCH_ENTITY_CODE );
		} catch( ClassNotFoundException oException ) {
			throw new MobileFwkException(oException);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMOldComplexeComponent#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int p_iRequestCode, int p_iResultCode, Intent p_oIntent) {
		if (p_iRequestCode == SEARCH_ENTITY_CODE && p_iResultCode == Activity.RESULT_OK ) {
			String sEntity = (String) p_oIntent.getExtras().getString("ENTITY");
			ITEMVM oItemVM = this.adapter.getItemVMById(sEntity);
			int iSelectedPos = this.adapter.getItemVMPos(oItemVM);
			this.configurationSetValue(oItemVM);
			this.adapter.setSelectedPosition(iSelectedPos);
			this.aivDelegate.changed();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetCustomValues(String[] p_aValues) {
		/* Nothing to do */
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
	public Class<?> getValueType() {
		return NoneType.class;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITEMVM configurationGetValue() {
		return this.selectedItem;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return null;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullOrEmptyValue(ITEMVM p_oObject) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override @SuppressWarnings({ "unchecked", "rawtypes" })
	public void configurationSetValue(ITEMVM p_oObject) {
		super.configurationSetValue(p_oObject);
		((AbstractConfigurableSpinnerAdapter<?,?,?>)this.getAdapter()).doOnNoErrorOnSelectedItem(this);

		if (p_oObject == null) {
			this.selectedItem = null;
		} else if (this.adapter != null) {
			this.selectedItem = this.adapter.getItemVMById(p_oObject.getIdVM());
		}

		// Delete de la selectedView. On utilise pas l'objet selectedView pour supprimer 
		// l'instance dans le layout car dans le cadre d'une rotation, l'objet selectedView 
		// change d'id. Du coup on ne le retrouve plus et on ne sait pas le virer
		if (this.contentLayout.getChildAt(0)!= null){
			this.contentLayout.removeViewAt(0);
		}
		int iPos = 0;
		if ( p_oObject != null ){
			iPos = this.adapter.indexOf(this.selectedItem);
		}

		this.selectedView = this.adapter.getView(iPos, null, this);

		// La selected view en retour de l'adapter ne devrait pas avoir de parent
		// ne faire ça que pour le MMSearchSpinner, le MMSpinner a ca propre mécanique interne
		if (this.selectedView.getParent()!=null) {
			((ViewGroup) this.selectedView.getParent()).removeView(this.selectedView);
		}
		this.contentLayout.addView(selectedView);
		if ( this.dialog !=null ) {
			this.dialog.setSelectedItem(this.selectedItem);
		}
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
							if (oField.getType().equals(String.class) ) {
								if (p_oParams.length == 1 ) {
									String sFilter = p_oParams[0].toLowerCase(); 
									String sValue = ((String) oField.get(p_oItem)).toLowerCase();
									if (sValue.contains(sFilter)) {
										r_bFiltered = true;
									}
								}
							}
						}
					}
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
					r_bFiltered = true;
				}
				return r_bFiltered;
			}
			
		};
	}

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
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetMandatoryLabel() {
		if (!this.aivDelegate.isFlagMandatory()) {
			this.aivDelegate.configurationSetMandatoryLabel();

			StringBuilder oTitleBuilder = new StringBuilder();
			CharSequence sPrompt = this.getDialogTitle();
			if (sPrompt != null) {
				oTitleBuilder.append(sPrompt);
			}
			if ( !oTitleBuilder.toString().endsWith("(*)") ) {
				oTitleBuilder.append("(*)");
			}

			this.setDialogTitle(oTitleBuilder.toString());
		}
	}

	/**
	 * Affectation du listener de sélection d'un item dans la liste de recherche.
	 * @param listener le listener qui va écouter la sélection d'items et réagir en fonction
	 */
	public void setOnSearchItemSelectedListener(OnSearchItemSelectedListener p_oListener) {
		this.listener = p_oListener;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFilled() {
		return this.selectedItem != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnsetError() {
		((AbstractConfigurableSpinnerAdapter<?,?,?>)this.getAdapter()).doOnNoErrorOnSelectedItem(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetError(String p_oError) {
		((AbstractConfigurableSpinnerAdapter<?,?,?>)this.getAdapter()).doOnErrorOnSelectedItem(this, p_oError);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDismiss(DialogInterface p_oArg0) {
		isDialogVisible=false;
	}


	@Override
	public Parcelable onSaveInstanceState() {
		Bundle oToSave = new Bundle();
		oToSave.putParcelable("parent", super.superOnSaveInstanceState());
		oToSave.putString("class", MMSearchSpinner.class.getName());
		oToSave.putString("Filter",this.currentFilter);

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
		
		ITEMVM oSelectedItem = (ITEMVM) savedState.getSerializable("selectedItem");
		if ( oSelectedItem != null ) {
			for (int i = 0; i < this.getAdapter().getMasterVM().getCount(); i++) {
				ITEMVM vm = this.getAdapter().getMasterVM().getCacheVMByPosition(i);
				if ( vm.getIdVM().equals(oSelectedItem.getIdVM())) {
					this.selectedItem = vm;
					break;
				}
			}

			if ( this.selectedItem == null ) {
				this.selectedItem = oSelectedItem;
			}

			this.isOnRestoreInstanceState = true;
			configurationSetValue(this.selectedItem);
			this.isOnRestoreInstanceState = false;
		}
	}

	@Override
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		MMDialogFragment oDialogFragment = MMSearchSpinnerDialogFragment.newInstance(this);
		return oDialogFragment;
	}

	@Override
	public void prepareDialogFragment(MMDialogFragment p_oDialog,
			Bundle p_oDialogFragmentArguments) {
		p_oDialog.setArguments(p_oDialogFragmentArguments);		
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

	public void setDialogVisible(MMSearchSpinnerDialogFragment mmSearchSpinnerDialogFragment) {
		this.dialog = mmSearchSpinnerDialogFragment;
		this.isDialogVisible = true;
		
	}

	public String getCurrentFilter() {
		return this.currentFilter;
	}
}
