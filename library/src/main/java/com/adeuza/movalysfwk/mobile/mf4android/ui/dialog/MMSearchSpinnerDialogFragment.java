package com.adeuza.movalysfwk.mobile.mf4android.ui.dialog;

import java.lang.ref.WeakReference;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.AbstractMMFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ConfigurableSpinnerListAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMEditText;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMListView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMSearchSpinner;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModelFilter;

/**
 * <p>TODO Décrire la classe MMFixedListInternalDialog</p>
 *
 * <p>Copyright (c) 2012
 * <p>Company: Adeuza
 *
 * @author emalespine
 *
 */

public class MMSearchSpinnerDialogFragment extends MMDialogFragment implements OnDismissListener{

	/**
	 * The key for the viewId argument
	 */
	public static final String VIEW_ID_ARGUMENT_KEY = "searchSpinnerDialogFragmentViewIdArgumentKey";

	/**
	 * key for ITEMVM interface class
	 */
	public static final String ITEMVM_ITF_CLASS_ARGUMENT_KEY = "spinnerItemVmItf";

	
	/**
	 * The key for the theme argument
	 */
	public static String SEARCH_SPINNER_FRAGMENT_DIALOG_TAG = "spinnerDialogFragmentTag";
	
	/**
	 * The key for the theme argument
	 */
	public MMListView itemList;
	
	/**
	 * The key for the theme argument
	 */
	public MMEditText filter;
	
	/**
	 * The viewId to the contentView
	 */
	private int m_iViewId = -1;
	
	/** the application */
	private AndroidApplication application = (AndroidApplication) Application.getInstance();

	private Class mItemVmInterface;
	
	
	/**
	 * NewInstance method 
	 * @param p_oDismissListener The DismissListener of this DialogFragment. It will be stored in
	 * this object as a WeakReference
	 * @return A new instance of this fragment
	 */
	public static MMSearchSpinnerDialogFragment newInstance(OnDismissListener p_oDismissListener) {
		MMSearchSpinnerDialogFragment oFragment = new MMSearchSpinnerDialogFragment();
		oFragment.m_oDismissListener = new WeakReference<>(p_oDismissListener);
		oFragment.m_sFragmentTag = String.valueOf(AbstractMMFragment.oGeneratedUniqueTag.incrementAndGet());
		oFragment.m_bUpdateComponentReference = false;
		oFragment.componentId = ((View) p_oDismissListener).getId();
		oFragment.componentFragmentTag = ((ConfigurableVisualComponent<?>) p_oDismissListener).getFragmentTag();
		return oFragment;
	}


	/**
	 * {@inheritDoc}
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle p_oSavedInstanceState) {
		super.onCreate(p_oSavedInstanceState);
		this.setRetainInstance(false);
		Bundle oArguments = getArguments();
		if(oArguments != null) {
			m_iViewId = oArguments.getInt(this.VIEW_ID_ARGUMENT_KEY);
			mItemVmInterface = (Class) oArguments.get(this.ITEMVM_ITF_CLASS_ARGUMENT_KEY);
		}

	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	/**
	 * {@inheritDoc}
	 * @see android.app.Dialog#onCreateonCreateView(LayoutInflater inflater, ViewGroup container,
	 *		Bundle savedInstanceState) 
	 */
	public View onCreateView(LayoutInflater p_oInflater, ViewGroup p_oContainer,
			Bundle p_oSavedInstanceState) {
		super.onCreateView(p_oInflater, p_oContainer, p_oSavedInstanceState);
		View oContentView = p_oInflater.inflate(m_iViewId, p_oContainer);
		
		((MMSearchSpinner)getDismissListener()).setDialogVisible(this);
		
		itemList = (MMListView) oContentView.findViewById( this.application.getAndroidIdByRKey(AndroidApplicationR.custom_popup_list_items));
		filter = (MMEditText) oContentView.findViewById(application.getAndroidIdByRKey(AndroidApplicationR.filtre_section));
		
		this.filter.addTextChangedListener((MMSearchSpinner)getDismissListener());
		this.itemList.setAdapter(new ConfigurableSpinnerListAdapter(((MMSearchSpinner)getDismissListener()).getAdapter(), mItemVmInterface, ((MMSearchSpinner)getDismissListener()), ((MMSearchSpinner)getDismissListener()).hasEmptyValue()));
		this.filter.setText(((MMSearchSpinner)getDismissListener()).getCurrentFilter());
		return oContentView;
	}
	
	@Override
	public void onViewCreated(View p_oView, Bundle p_oSavedInstanceState) {
		super.onViewCreated(p_oView, p_oSavedInstanceState);
		
	}
	
	@Override
	public void onDismiss(DialogInterface p_oDialog) {
		getDismissListener().onDismiss(p_oDialog);
		super.onDismiss(p_oDialog);
	}

	/**
	 * Init Item Selected
	 * @param p_oSelectedItem Item selected
	 *
	 */
	public void setSelectedItem(ItemViewModel p_oSelectedItem) {
		((ConfigurableSpinnerListAdapter)itemList.getAdapter()).setSelectedItem(p_oSelectedItem);
	}
	
	/**
	 * Filter List View Model
	 * @param p_oFilter searchFieldFilter
	 *
	 */	public <ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>, LISTVM extends ListViewModel<ITEM, ITEMVM>> void filter(ListViewModelFilter<ITEM,ITEMVM,String> p_oFilter) {
		((ConfigurableSpinnerListAdapter<ITEM,ITEMVM, LISTVM>) this.itemList.getAdapter()).getMasterVM().filter(p_oFilter, this.filter.getText().toString());
		((BaseAdapter) this.itemList.getAdapter()).notifyDataSetChanged();
	}
}
