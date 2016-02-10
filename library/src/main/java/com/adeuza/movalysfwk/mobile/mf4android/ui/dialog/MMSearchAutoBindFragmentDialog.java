package com.adeuza.movalysfwk.mobile.mf4android.ui.dialog;

import java.lang.ref.WeakReference;

import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractSearchAutoBindMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.AbstractMMFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * <p>TODO Décrire la classe MMFixedListInternalDialog</p>
 *
 * <p>Copyright (c) 2012
 * <p>Company: Adeuza
 *
 * @author emalespine
 *
 */

public class MMSearchAutoBindFragmentDialog extends MMDialogFragment {

	/**
	 * The key for the viewId argument
	 */
	public static String VIEW_ID_ARGUMENT_KEY = "searchAutoBindFragmentDialogViewIdArgumentKey";
	
	
	/**
	 * The key for the theme argument
	 */
	public static String SEARCH_AUTO_BIND_FRAGMENT_DIALOG_TAG = "searchAutoBindDialogFragmentTag";
	
	
	/**
	 * The viewId to the contentView
	 */
	private int m_iViewId = -1;
	
	/**
	 * NewInstance method 
	 * @param p_oDismissListener The DismissListener of this DialogFragment. It will be stored in
	 * this object as a WeakReference
	 * @return A new instance of this fragment
	 */
	public static MMSearchAutoBindFragmentDialog newInstance(OnDismissListener p_oDismissListener) {
		MMSearchAutoBindFragmentDialog oFragment = new MMSearchAutoBindFragmentDialog();
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

		Bundle oArguments = getArguments();
		if(oArguments != null) {
			m_iViewId = oArguments.getInt(VIEW_ID_ARGUMENT_KEY);
		}

	}


	@Override
	/**
	 * {@inheritDoc}
	 * @see android.app.Dialog#onCreateonCreateView(LayoutInflater inflater, ViewGroup container,
	 *		Bundle savedInstanceState) 
	 */
	public View onCreateView(LayoutInflater p_oInflater, ViewGroup p_oContainer,
			Bundle p_oSavedInstanceState) {
		super.onCreateView(p_oInflater, p_oContainer, p_oSavedInstanceState);
		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
		
		View oContentView = p_oInflater.inflate(m_iViewId, p_oContainer);
		this.getDialog().setTitle(oApplication.getStringResource(AndroidApplicationR.component_search_dialog_title));
		return oContentView;
	}
	
	@Override
	public void onViewCreated(View p_oView, Bundle p_oSavedInstanceState) {
		super.onViewCreated(p_oView, p_oSavedInstanceState);
		((AbstractSearchAutoBindMMActivity<?,?>)this.m_oDismissListener.get()).afterDialogFragmentShown();
	}

	/**
	 * {@inheritDoc}
	 */
	protected void prepareDialogData() {

	}

}
