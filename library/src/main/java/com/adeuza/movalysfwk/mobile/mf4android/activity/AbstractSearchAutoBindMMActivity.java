package com.adeuza.movalysfwk.mobile.mf4android.activity;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay.GenericLoadDataForDisplayDetailAction;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMSearchAutoBindFragmentDialog;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMListView;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.Dataloader;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.ListDataloader;
import com.adeuza.movalysfwk.mobile.mf4javacommons.dataloader.MMDataloader;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.BusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.genericdisplay.InDisplayParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.VisualComponentDescriptorsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;

/**
 * @author lmichenaud
 * 
 */
public abstract class AbstractSearchAutoBindMMActivity<CRITERIAITEM extends MIdentifiable, RESULTITEM extends MIdentifiable> extends
		AbstractAutoBindMMActivity implements OnClickListener, OnDismissListener {

	/**
	 * Portrait mode.
	 * When smartphone is in portrait mode, layout can be either:
	 * <ul>
	 * <li>A single panel with a button to open the criteria dialog.</li>
	 * <li>A dual panel: top panel contains the search criterias, the bottom panel contains the results</li>
	 * </ul>
	 * Default is single panel
	 */
	public static final String PORTRAIT_MODE = "portraitMode";
	public static final String PORTRAIT_MODE_DUAL = "dual";
	
	private static final int CRITERIA_DIALOG_ID = 0;

	/**
	 * 
	 */
	private AndroidApplication application = (AndroidApplication) Application.getInstance();
	
	/**
	 * 
	 */
	private MMListView listView = null;
	
	/**
	 * List adapter
	 */
	private ListAdapter listAdapter ;
	
	/**
	 * 
	 */
	private boolean criteriaAsDialog ;
	
	/**
	 * 
	 */
	private MMSearchAutoBindFragmentDialog criteriaDialog ;
	
	/**
	 * 
	 */
	private boolean isCriteriaDialogVisible = false;
	
	/**
	 * Cancel search button id
	 */
	private int cancelSearchButtonId;
	
	/**
	 * Reset search button id 
	 */
	private int resetSearchButtonId;
	
	/**
	 * Do search button id 
	 */
	private int doSearchButtonId;
	
	/**
	 * Show criteria button id
	 */
	private int showCriteriaButtonId ;

	/**
	 * View Model for criterias
	 */
	private ItemViewModel<CRITERIAITEM> criteriaViewModel ;
	
	/**
	 * Criteria data loader
	 */
	private Dataloader<CRITERIAITEM> criteriaDataLoader;
	
	/**
	 * Master component of the criteria layout
	 */
	private MasterVisualComponent<?> criteriaComponent;
	
	/**
	 * Create the adapter of the list of the results
	 * 
	 * @return
	 */
	protected abstract ListAdapter createListAdapter();
	
	/**
	 * @return
	 */
	protected abstract ItemViewModel<CRITERIAITEM> createCriteriaViewModel(); 
	
	/**
	 * Return the layout id for criterias
	 * 
	 * @return
	 */
	protected abstract int getCriteriaLayoutId();

	/**
	 * Return the layout id for the results
	 * 
	 * @return
	 */
	protected abstract int getResultLayoutId();

	/**
	 * Return the listview id for the result
	 * @return
	 */
	protected abstract int getListViewId();
	
	/**
	 * Return the id of the cancel button
	 * @return
	 */
	protected abstract int getCancelSearchButtonId();
	
	/**
	 * Return the id of the reset button
	 * @return
	 */
	protected abstract int getResetSearchButtonId();
	
	/**
	 * Return the id of the search button
	 * @return
	 */
	protected abstract int getDoSearchButtonId();
	
	/**
	 * @return
	 */
	protected abstract int getCriteriaComponentId();
	
	/**
	 * Return the button view id which opens the criteria dialog
	 * 
	 * @return
	 */
	protected abstract int getShowCriteriaButtonId();
	
	/**
	 * @return
	 */
	protected abstract Class<? extends MMDataloader<CRITERIAITEM>> getCriteriaLoaderClass();
	
	/**
	 * @return
	 */
	protected abstract Class<? extends ListDataloader<RESULTITEM>> getResultLoaderClass();
	
	/**
	 * Method to initialize components of the criteria panel (like spinners, adapters).
	 */
	protected abstract void customInitCriteriaPanel( View p_oCriteriaView);
	
	/**
	 * Method to initialize components of the criteria dialog (like spinners, adapters).
	 * @param p_oCriteriaDialog
	 */
	protected abstract void customInitCriteriaDialog(Dialog p_oCriteriaDialog);
	
	/**
	 * Return the event class for selected item event
	 * @return
	 */
	protected abstract Class<? extends BusinessEvent<?>> getSelectedItemEventClass();
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractAutoBindMMActivity#doOnCreate(android.os.Bundle)
	 */
	@Override
	protected void doOnCreate(Bundle p_oSavedInstanceState) {
		super.doOnCreate(p_oSavedInstanceState);
		
		Object oConfig = this.getLastCustomNonConfigurationInstance();
		
		// Search the view container for the results
		ViewGroup oResultContainerView = (ViewGroup) this
			.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.fwk_searchactivity_resultpanel__master));
		
		// Inflate the list layout inside the result container
		LayoutInflater oInflater = this.getLayoutInflater();
		oInflater.inflate(this.getResultLayoutId(), oResultContainerView);

		// Initialize the list, it adapter and listener on it
		this.listView = (MMListView) oResultContainerView.findViewById(this.getListViewId());
				
		this.listAdapter = this.createListAdapter();
		this.listView.setAdapter(this.listAdapter);

		// Add listener for the selected item
		this.initializeEventListeners();
		
		// Initialize resource id for buttons
		this.cancelSearchButtonId = this.getCancelSearchButtonId();
		this.resetSearchButtonId = this.getResetSearchButtonId();
		this.doSearchButtonId = this.getDoSearchButtonId();
		this.showCriteriaButtonId = this.getShowCriteriaButtonId();
				
		// Create view model for criteria
		if ( oConfig == null ) {
			this.criteriaViewModel = this.createCriteriaViewModel();
		} else {
			this.criteriaViewModel = (ItemViewModel<CRITERIAITEM>) ((Map<String, Object>) oConfig).get("criteriavm");
		}
		
		// Get data loaders
		this.criteriaDataLoader = BeanLoader.getInstance().getBean(this.getCriteriaLoaderClass());
		
		// Search the view containing the criteria (only available in landscape
		// mode)
		ViewGroup oCriteriaContainerView = (ViewGroup) this
				.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.fwk_searchactivity_criteriapanel__master));
		
		// si criteria panel found, inflate the criteria layout
		if (oCriteriaContainerView != null) {
			this.criteriaAsDialog = false ;
			
			View oCriteriaView = oInflater.inflate(this.getCriteriaLayoutId(), oCriteriaContainerView);
			this.initializeSearchButtons(oCriteriaContainerView);
			this.customInitCriteriaPanel( oCriteriaView );
			this.initCriteriaComponent(oCriteriaView.findViewById(this.getCriteriaComponentId()), this.hashCode());
			this.hideCriteriaButton(oResultContainerView);
			this.hideCancelButton(oCriteriaView);
		}
		// Else, find the button that opens the criteria dialog
		else {
			this.criteriaAsDialog = true ;
			View oShowCriteriaButton = oResultContainerView.findViewById(this.showCriteriaButtonId);
			oShowCriteriaButton.setOnClickListener(this);
		}
	}

	/**
	 * @param p_oCriteriaView
	 */
	protected void initCriteriaComponent( View p_oCriteriaView, int p_iKeyView ) {
		this.criteriaComponent = (MasterVisualComponent<?>) p_oCriteriaView ;
		this.criteriaComponent.setDescriptor(VisualComponentDescriptorsHandler.getInstance().getRealDescriptor(
			this.criteriaComponent.getName()));
		this.criteriaComponent.setViewModel(this.criteriaViewModel);
		this.criteriaComponent.getDescriptor().inflate(String.valueOf(p_iKeyView), this.criteriaComponent, null);
		this.criteriaViewModel.doOnDataLoaded(this.getParameters());
	}
	
	/**
	 * @param p_oCriteriaContainerView
	 */
	private void initializeSearchButtons(View p_oCriteriaContainerView) {
		View oButton = null;
		oButton = (View) p_oCriteriaContainerView
				.findViewById(this.cancelSearchButtonId);
		if ( oButton != null ) {
			oButton.setOnClickListener(this);
		}

		oButton = (View) p_oCriteriaContainerView
				.findViewById(this.resetSearchButtonId);
		if ( oButton != null ) {
			oButton.setOnClickListener(this);
		}

		oButton = (View) p_oCriteriaContainerView
				.findViewById(this.doSearchButtonId);
		if ( oButton != null ) {
			oButton.setOnClickListener(this);
		}
	}
	

	/**
	 * @param p_oCriteriaContainerView
	 */
	private void initializeSearchButtons(Dialog p_oDialog) {
		View oButton = null;
		oButton = (View) p_oDialog
				.findViewById(this.cancelSearchButtonId);
		if ( oButton != null ) {
			oButton.setOnClickListener(this);
		}

		oButton = (View) p_oDialog
				.findViewById(this.resetSearchButtonId);
		if ( oButton != null ) {
			oButton.setOnClickListener(this);
		}

		oButton = (View) p_oDialog
				.findViewById(this.doSearchButtonId);
		if ( oButton != null ) {
			oButton.setOnClickListener(this);
		}
	}
	
	/**
	 * Initialize event listeners
	 */
	private void initializeEventListeners() {
		try {
			Method oOnNewSelectedItemMethod = this.getClass().getMethod("onNewSelectedItem", BusinessEvent.class);
//			Method oOnReloadCriteriaMethod = this.getClass().getMethod("doOnReloadCriterias", ListenerOnDataLoaderReloadEvent.class);
//			Method oOnReloadResultsMethod = this.getClass().getMethod("doOnReloadResults", ListenerOnDataLoaderReloadEvent.class);
			
			this.getAndroidApplication().addBusinessEventListener(this.getSelectedItemEventClass(), this);
			this.getAndroidApplication().addDataLoaderListener(this.getCriteriaLoaderClass(), this);
			this.getAndroidApplication().addDataLoaderListener(this.getResultLoaderClass(), this);
			
			this.getAndroidApplication().getClassAnalyser().getClassAnalyse(this).addBusinessEventListener(
					this.getSelectedItemEventClass(), this.getClass(), oOnNewSelectedItemMethod );
			
//			this.androidApplication.getClassAnalyser().getClassAnalyse(this).addDataLoaderListener(this.getCriteriaLoaderClass(), 
//					this.getClass(), oOnReloadCriteriaMethod);
//			this.androidApplication.getClassAnalyser().getClassAnalyse(this).addDataLoaderListener(this.getResultLoaderClass(), 
//					this.getClass(), oOnReloadResultsMethod);
			
		} catch (NoSuchMethodException oNoSuchMethodException) {
			throw new MobileFwkException(oNoSuchMethodException);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View p_oView) {

		if ( p_oView.getId() == this.cancelSearchButtonId ) {
			this.cancelSearch();
		}
		else
		if ( p_oView.getId() == this.doSearchButtonId ) {
			this.doSearch();
		}
		else
		if ( p_oView.getId() == this.resetSearchButtonId ) {
			this.doReset();
		}
		else
		if ( p_oView.getId() == this.showCriteriaButtonId ) {
			this.showCriteriaDialog();
		}
	}

	/**
	 * Open the criteria dialog 
	 */
	private void showCriteriaDialog() {
		this.criteriaDialog = (MMSearchAutoBindFragmentDialog) createDialogFragment(null);
		
		Bundle oDialogFragmentArguments = new Bundle();
		oDialogFragmentArguments.putInt(MMSearchAutoBindFragmentDialog.VIEW_ID_ARGUMENT_KEY, this.getCriteriaLayoutId());
		prepareDialogFragment(this.criteriaDialog, oDialogFragmentArguments);
		this.criteriaDialog.show(getSupportFragmentManager(), this.criteriaDialog.getFragmentDialogTag());
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity#onCreateDialog(int)
	 */
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		MMDialogFragment oDialogFragment = MMSearchAutoBindFragmentDialog.newInstance((OnDismissListener) this);
		return oDialogFragment;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity#onPrepareDialog(int, android.app.Dialog)
	 */
	protected void prepareDialogFragment(MMDialogFragment p_oDialog, Bundle p_oArguments) {
		p_oDialog.setArguments(p_oArguments);
		this.customInitCriteriaDialog(this.criteriaDialog.getDialog());
	}
	
	public void afterDialogFragmentShown() {
		View oCriteriaView = this.criteriaDialog.getView().findViewById(this.getCriteriaComponentId());
		this.initCriteriaComponent(oCriteriaView, this.criteriaDialog.hashCode());
		this.isCriteriaDialogVisible = true ;
		this.initializeSearchButtons(this.criteriaDialog.getView());
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractAutoBindMMDialogFragment#doFillAction()
	 */
	@Override
	public void doFillAction() {		
		InDisplayParameter oInDisplayParameter = new InDisplayParameter();
		oInDisplayParameter.setDataLoader( this.getResultLoaderClass());
		oInDisplayParameter.setId( this.getIntent().getStringExtra(IDENTIFIER_CACHE_KEY) );
		this.launchAction(GenericLoadDataForDisplayDetailAction.class, oInDisplayParameter);
		
		this.doCriteriaFillAction();
	}
	
	/**
	 * 
	 */
	public void doCriteriaFillAction() {		
		InDisplayParameter oInDisplayParameter = new InDisplayParameter();
		oInDisplayParameter.setDataLoader( this.getCriteriaLoaderClass() );
		this.launchAction(GenericLoadDataForDisplayDetailAction.class, oInDisplayParameter);
	}

	/**
	 * 
	 */
	protected void dismissCriteriaDialog() {
		if ( this.isCriteriaDialogVisible && this.criteriaDialog.getDialog().isShowing()) {
			this.criteriaDialog.dismiss();
			this.isCriteriaDialogVisible = false ;
			this.criteriaComponent.getDescriptor().unInflate(String.valueOf(this.criteriaDialog.hashCode()), this.criteriaComponent, null);
		}
	}
	
	/**
	 * 
	 */
	protected void doReset() {
		if (this.criteriaViewModel != null) {
			this.criteriaViewModel.clear();
//ABE			this.criteriaViewModel.writeData(null, this.getParameters());
			this.criteriaViewModel.doOnDataLoaded(this.getParameters());
			this.criteriaViewModel.modifyToIdentifiable(this.criteriaDataLoader.getData());
		}
		
		if ( this.isCriteriaDialogVisible && this.criteriaDialog.getDialog().isShowing()) {
			this.criteriaDialog.dismiss();
		}		
		this.doFillAction();
	}
	
	/**
	 * 
	 */
	protected final void doSearch() {	
		this.readValuesInCriteriaDialog();
		this.dismissCriteriaDialog();
		this.doFillAction();
	}
	
	/**
	 * 
	 */
	protected void readValuesInCriteriaDialog() {
		if ( this.criteriaViewModel != null) {
			Map<String, Object> map = new HashMap<>();
			this.criteriaViewModel.modifyToIdentifiable(this.criteriaDataLoader.getData());			
		}
	}
	
	/**
	 * Cancel search
	 */
	protected void cancelSearch() {		
		if ( this.isCriteriaDialogVisible && this.criteriaDialog.getDialog().isShowing()) {
			this.criteriaDialog.getDialog().cancel();
			this.isCriteriaDialogVisible = false ;
		}
	}
	
	/**
	 * Hide the "open criteria dialog" button of the result view
	 */
	private void hideCriteriaButton( View p_oResultView) {
		View oShowCriteriaButton = p_oResultView.findViewById(this.showCriteriaButtonId);
		if ( oShowCriteriaButton != null ) {
			oShowCriteriaButton.setVisibility(View.GONE);
		}
	}
	
	/**
	 * Hide the cancel button of the criteria view
	 * @param p_oCriteriaView
	 */
	private void hideCancelButton( View p_oCriteriaView) {
		View oCancelButton = p_oCriteriaView.findViewById(this.cancelSearchButtonId);
		if ( oCancelButton != null ) {
			oCancelButton.setVisibility(View.INVISIBLE);						
		}
	}
	
	/**
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractAutoBindMMActivity#getViewId()
	 */
	@Override
	public int getViewId() {
		
		String sParam = "" ;
		Bundle oExtras = getIntent().getExtras();
		if ( oExtras !=null && oExtras.getString(PORTRAIT_MODE).equals(PORTRAIT_MODE_DUAL)) {
			sParam = "dual";
		}
		
		return this.application.getAndroidIdByStringKey(ApplicationRGroup.LAYOUT, 
				"fwksearchactivity" + sParam + "__screendetail__master");
	}

	/**
	 * @return
	 */
	public ItemViewModel<CRITERIAITEM> getCriteriaViewModel() {
		return this.criteriaViewModel;
	}

	/**
	 * @return
	 */
	protected boolean isCriteriaAsDialog() {
		return this.criteriaAsDialog;
	}
	
	/**
	 * 
	 * @param p_oBusinessEvent
	 */
	public void onNewSelectedItem( BusinessEvent<String> p_oBusinessEvent ) {
		Intent oIntent = this.getIntent();
		oIntent.putExtra("ENTITY", p_oBusinessEvent.getData());
		this.setResult(RESULT_OK, oIntent);
		this.finish();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> onRetainCustomNonConfigurationInstance() {
		Map<String,Object> r_oMap =  (Map<String, Object>) super.onRetainCustomNonConfigurationInstance();
		r_oMap.put("criteriavm", this.criteriaViewModel);
		return r_oMap;
	}
}
