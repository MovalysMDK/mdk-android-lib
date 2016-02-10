package com.adeuza.movalysfwk.mobile.mf4android.ui.dialog;

import java.lang.ref.WeakReference;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.AbstractMMFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * A generic DialogFragment with a object used for callback
 *
 * <p>Copyright (c) 2013</p>
 * <p>Company: Sopra Group</p>
 *
 * @since Cotopaxi
 * @author qlagarde
 */

public class MMDialogFragment extends DialogFragment {

	/**
	 * The value for default theme
	 */
	public static String DIALOG_FRAGMENT_ARGUMENT_TITLE_KEY = "MMDialogFragment_Argument_Title_Key";

	/**
	 * The value for default theme
	 */
	public static String DIALOG_FRAGMENT_ARGUMENT_ICON_KEY = "MMDialogFragment_Argument_Icon_Key";

	/**
	 * The value for default theme
	 */
	public static int THEME_DEFAULT = 0;

	/**
	 * The current tag of this fragment
	 */
	protected String m_sFragmentTag = null;

	/**
	 * The target object on which the onDismiss method will be called
	 */
	protected WeakReference<OnDismissListener> m_oDismissListener = null;

	/**
	 * Indicates if the weak reference to the component should be updated
	 */
	protected boolean m_bUpdateComponentReference;

	protected String componentFragmentTag ;

	protected int componentId = 0 ;

	/**
	 * NewInstance method 
	 * @param p_oDismissListener The DismissListener of this DialogFragment. It will be stored in
	 * this object as a WeakReference
	 * @return A new instance of this fragment
	 */
	public static MMDialogFragment newInstance(OnDismissListener p_oDismissListener) {
		MMDialogFragment oFragment = new MMDialogFragment();
		oFragment.m_oDismissListener = new WeakReference<>(p_oDismissListener);
		oFragment.m_sFragmentTag = String.valueOf(AbstractMMFragment.oGeneratedUniqueTag.incrementAndGet());
		oFragment.m_bUpdateComponentReference = false;
		oFragment.componentId = ((View) p_oDismissListener).getId();
		oFragment.componentFragmentTag = ((ConfigurableVisualComponent<?>) p_oDismissListener).getFragmentTag();
		return oFragment;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle p_oSavedInstanceState) {
		super.onCreate(p_oSavedInstanceState);
		this.setRetainInstance(true);
		if ( p_oSavedInstanceState != null ) {
			if ( m_sFragmentTag == null ) {
				m_sFragmentTag = p_oSavedInstanceState.getString("fragmentTag");
			}
			if ( componentId == 0 ) {
				componentId = p_oSavedInstanceState.getInt("componentId");
			}
			if ( componentFragmentTag == null ) {
				componentFragmentTag = p_oSavedInstanceState.getString("componentFragmentTag");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater p_oInflater, ViewGroup p_oContainer, Bundle p_oSavedInstanceState) {

		if ( this.m_oDismissListener == null ) {
			AbstractMMActivity oMainActivity = (AbstractMMActivity) getActivity();
			this.m_oDismissListener = new WeakReference<>((OnDismissListener) oMainActivity.findComponent(this.componentId, this.componentFragmentTag));
		}

		this.prepareDialogData(p_oSavedInstanceState);
		return super.onCreateView(p_oInflater, p_oContainer, p_oSavedInstanceState);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onViewCreated(View p_oView, Bundle p_oSavedInstanceState) {
		setTagFragmentOnView(p_oView);
		super.onViewCreated(p_oView, p_oSavedInstanceState);
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void onSaveInstanceState(Bundle p_oOutState) {
		super.onSaveInstanceState(p_oOutState);
		p_oOutState.putString("fragmentTag", this.m_sFragmentTag);
		p_oOutState.putInt("componentId", this.componentId);
		p_oOutState.putString("componentFragmentTag", this.componentFragmentTag);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onResume() {
		//Update component if it has disappeared due to a changed orientation
		if( m_bUpdateComponentReference) {
			AbstractMMActivity oMainActivity = (AbstractMMActivity) getActivity();
			this.m_oDismissListener = new WeakReference<>((OnDismissListener) oMainActivity.findComponent(this.componentId, this.componentFragmentTag));
		}
		else if(!m_bUpdateComponentReference) {
			m_bUpdateComponentReference = true;
		}
		
		
		super.onResume();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDismiss(DialogInterface p_oDialog) {
		m_oDismissListener = null;
		super.onDismiss(p_oDialog);
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void onDestroyView() {
		if (getDialog() != null && getRetainInstance())
			getDialog().setDismissMessage(null);
		super.onDestroyView();
	}

	/**
	 * Returns the tag of this dialogFragment
	 * @return a tag identifying this dialogFagment
	 */
	public String getFragmentDialogTag() {
		return this.m_sFragmentTag;
	}

	@Override 
	public void show(FragmentManager p_oManager, String p_sTag) {
		if ( !isDetached()) {
			FragmentTransaction ft = p_oManager.beginTransaction();
			ft.add(this, p_sTag);
			ft.commitAllowingStateLoss();
		}
	}

	/**
	 * This method is called when this DialogFragment needs to do some
	 * treatments before to be displayed.
	 */
	protected void prepareDialogData(Bundle p_oSavedInstanceState) {
		//Method should be implemented in custom DialogFragment
	}

	protected DialogInterface.OnDismissListener getDismissListener() {
		return this.m_oDismissListener.get();
	}

	@SuppressWarnings("rawtypes")
	private void setTagFragmentOnView(View p_oView) {
		if(p_oView instanceof ConfigurableVisualComponent) {
		if(((ConfigurableVisualComponent)p_oView).getConfiguration() !=null) {
				((ConfigurableVisualComponent)p_oView).setFragmentTag(getFragmentDialogTag());
			}
		}

		if(p_oView instanceof ViewGroup) {
			ViewGroup oViewGroup = (ViewGroup)p_oView;
			for(int iViewIndex = 0 ; iViewIndex < oViewGroup.getChildCount() ; iViewIndex++) {
				View childView = oViewGroup.getChildAt(iViewIndex);
				setTagFragmentOnView(childView);
			}
		}
	}

	
	public void setFragmentTag(String p_sFragmentTag) {
		this.m_sFragmentTag = p_sFragmentTag;
	}

	public void setDismissListener(
			WeakReference<OnDismissListener> p_oDismissListener) {
		this.m_oDismissListener = p_oDismissListener;
	}

	public void setUpdateComponentReference(boolean p_bUpdateComponentReference) {
		this.m_bUpdateComponentReference = p_bUpdateComponentReference;
	}

	public void setComponentFragmentTag(String p_sComponentFragmentTag) {
		this.componentFragmentTag = p_sComponentFragmentTag;
	}

	public void setComponentId(int p_iComponentId) {
		this.componentId = p_iComponentId;
	}
	
	
}