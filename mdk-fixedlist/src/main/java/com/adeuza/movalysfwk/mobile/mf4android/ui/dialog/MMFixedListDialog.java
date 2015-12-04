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
package com.adeuza.movalysfwk.mobile.mf4android.ui.dialog;

import java.lang.ref.WeakReference;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.AbstractMMFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.WidgetWrapperHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMFixedList;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.VisualComponentDescriptorsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

public class MMFixedListDialog <ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>> extends MMDialogFragment {

	private MasterVisualComponent detail;

	/** The current ViewModel for this FixedListDialogFragment */
	private ITEMVM oCurrentViewModel = null;
	
	private boolean bHasWrittenData;

	/** The tag for this dialogFragment */
	public static String FIXED_LIST_VIEW_DIALOG_FRAGMENT_TAG = "fixedListViewDialogFragmentTag";

	public static String FIXED_LIST_VIEW_DIALOG_FRAGMENT_READ_MODE_ARGUMENT_KEY = "fixedListViewDialogFragmentReadModeArgumentKey";

	/** The current creating/consulting mode */
	private String mode;

	private boolean isReadOnly = false;

	/**
	 * NewInstance method.
	 * @param p_oDismissListener The DismissListener of this DialogFragment. It will be stored in this object as a WeakReference
	 * @return A new instance of this fragment
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static MMFixedListDialog newInstance(OnDismissListener p_oDismissListener) {
		MMFixedListDialog oFragment = new MMFixedListDialog();
		oFragment.m_oDismissListener = new WeakReference<>(p_oDismissListener);
		oFragment.m_sFragmentTag = String.valueOf(AbstractMMFragment.oGeneratedUniqueTag.incrementAndGet());
		oFragment.m_bUpdateComponentReference = false;
		oFragment.componentId = ((View) p_oDismissListener).getId();
		oFragment.componentFragmentTag = ((ConfigurableVisualComponent) p_oDismissListener).getComponentFwkDelegate().getFragmentTag();
		oFragment.bHasWrittenData = false;
		return oFragment;
	}

	@Override
	public void onCreate(Bundle p_oSavedInstanceState) {
		super.onCreate(p_oSavedInstanceState);
		setStyle(MMDialogFragment.STYLE_NO_TITLE, MMDialogFragment.THEME_DEFAULT);
		Bundle oArguments = getArguments();
		if (oArguments != null) {
			this.setMode(oArguments.getString("mode"));
			this.setIsReadOnly(oArguments
					.getBoolean(FIXED_LIST_VIEW_DIALOG_FRAGMENT_READ_MODE_ARGUMENT_KEY));
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();

		//Mise à jour des vues utilisant le OnDismissListener. Ne pas faire avant le onResume.
		int iOkButtonIdentifier = ((AndroidApplication) Application
				.getInstance())
				.getAndroidIdByRKey(AndroidApplicationR.component__fixedlist_detail_ok__button);
		int iOkButtonAndNewIdentifier = ((AndroidApplication) Application
				.getInstance())
				.getAndroidIdByRKey(AndroidApplicationR.component__fixedlist_detail_okandnext__button);
		int iCancelButtonIdentifier = ((AndroidApplication) Application
				.getInstance())
				.getAndroidIdByRKey(AndroidApplicationR.component__fixedlist_detail_cancel__button);

		// Récupération des boutons de la FixedList pour mettre à jour leur dismissListener
		View okButton = this.getView().findViewById(iOkButtonIdentifier);
		okButton.setTag(this);

		View cancelButton = this.getView().findViewById(iCancelButtonIdentifier);
		cancelButton.setTag(this);

		View okNewButton = this.getView().findViewById(iOkButtonAndNewIdentifier);
		okNewButton.setTag(this);

		@SuppressWarnings("unchecked")
		MMFixedList<ITEM, ITEMVM> oParent = (MMFixedList<ITEM, ITEMVM>) m_oDismissListener.get();

		if (this.isReadOnly) {
			okButton.setVisibility(View.GONE);
			okNewButton.setVisibility(View.GONE);
		}

		okButton.setOnClickListener(oParent);
		cancelButton.setOnClickListener(oParent);
		okNewButton.setOnClickListener(oParent);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater p_oInflater, ViewGroup p_oContainer,
			Bundle p_oSavedInstanceState) {
		super.onCreateView(p_oInflater, p_oContainer, p_oSavedInstanceState);

		//Récupération de l'id de la vue à créer.
		int iDetailLayoutIdentifier = ((AndroidApplication) Application
				.getInstance())
				.getAndroidIdByRKey(AndroidApplicationR.fwk_component__richfixedlist_detail__master);


		// Creating the globalView of the DialogFragment
		View oGlobalView = p_oInflater.inflate(iDetailLayoutIdentifier, null);
		
		//Utilisation du parent (référence à jour ou non, on utilise les mêmes données).
		MMFixedList<ITEM, ITEMVM> oParent = (MMFixedList<ITEM, ITEMVM>) m_oDismissListener.get();

		// Création et initialisation de la vue
		View oDetailView = p_oInflater.inflate(oParent.getAdapter().getAdapter().getDetailLayoutId(), null);
		this.detail = (MasterVisualComponent) WidgetWrapperHelper.getInstance().createWrapper(oDetailView, "MMMasterLayoutWrapper");
		oParent.getAdapter().getAdapter().wrapDetailView(this.detail);
		ViewGroup oSousGlobalView = (ViewGroup) oGlobalView
				.findViewById(((AndroidApplication) Application.getInstance())
						.getAndroidIdByRKey(AndroidApplicationR.component__fixedlist_detail__master_id));
		oSousGlobalView.addView(oDetailView, 2);

		return oGlobalView;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void prepareDialogData(Bundle p_oSavedInstanceState) {
		if ( m_oDismissListener != null ) {
			MMFixedList<ITEM, ITEMVM> oParent = (MMFixedList<ITEM, ITEMVM>) m_oDismissListener.get();

			if (this.oCurrentViewModel == null) {
				this.oCurrentViewModel = oParent.getAdapter().getAdapter().createEmptyVM();
			}

			if (this.mode.equals(MMFixedList.FIXED_LIST_MODE_EDIT) && !this.bHasWrittenData) {
				this.oCurrentViewModel.cloneVmAttributes((ViewModel) oParent.getAdapter().getSelectedItem());
			}
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		this.detail.setDescriptor(VisualComponentDescriptorsHandler
				.getInstance().getRealDescriptor(this.detail.getComponentFwkDelegate().getName()));
		this.detail.setViewModel(this.oCurrentViewModel);
		this.detail.getDescriptor().inflate(String.valueOf(this.hashCode()), this.detail, null, getTag());
		
		//A la rotation ne JAMAIS refaire le doOnDataLoaded
		if (!this.bHasWrittenData) {
			this.oCurrentViewModel.doOnDataLoaded(null);
			this.bHasWrittenData = true;
		}
	
	}

	@Override
	public void onStop() {
		int iOkButtonIdentifier = ((AndroidApplication) Application
				.getInstance())
				.getAndroidIdByRKey(AndroidApplicationR.component__fixedlist_detail_ok__button);
		int iOkButtonAndNewIdentifier = ((AndroidApplication) Application
				.getInstance())
				.getAndroidIdByRKey(AndroidApplicationR.component__fixedlist_detail_okandnext__button);
		int iCancelButtonIdentifier = ((AndroidApplication) Application
				.getInstance())
				.getAndroidIdByRKey(AndroidApplicationR.component__fixedlist_detail_cancel__button);
		
		View okButton = this.getView().findViewById(iOkButtonIdentifier);
		okButton.setTag(null);

		View cancelButton = this.getView().findViewById(iCancelButtonIdentifier);
		cancelButton.setTag(null);

		View okNewButton = this.getView().findViewById(iOkButtonAndNewIdentifier);
		okNewButton.setTag(null);
		
		if (this.detail != null) {
			this.detail.getDescriptor().unInflate(String.valueOf(this.hashCode()), this.detail, ((MasterVisualComponent) this.getActivity()).getParameters());
		}
		super.onStop();
	}

	@Override
	public void onDismiss(DialogInterface p_oDialogInterface) {
		if (m_oDismissListener != null) {
			m_oDismissListener.get().onDismiss(p_oDialogInterface);
		}
		super.onDismiss(p_oDialogInterface);
	}

	public String getMode() {
		return mode;
	}

	/**
	 * Get this temporary VM
	 * @return the VM for this dialog
	 */
	public ITEMVM getViewModel() {
		return this.oCurrentViewModel;
	}

	public void setMode(String p_sMode) {
		if (p_sMode != null) {
			this.mode = p_sMode;
		} else {
			this.mode = MMFixedList.FIXED_LIST_MODE_CREATE;
		}
	}
	
	public void setHasWrittenData(boolean p_bHasWrittenData) {
		this.bHasWrittenData = p_bHasWrittenData;
	}

	public MasterVisualComponent getDetail() {
		return this.detail;
	}

	private void setIsReadOnly(boolean p_bIsReadOnly) {
		this.isReadOnly = p_bIsReadOnly;
	}

	public boolean isEditing() {
		return !this.getMode().equals(MMFixedList.FIXED_LIST_MODE_CREATE) && this.getMode().equals(MMFixedList.FIXED_LIST_MODE_EDIT);
	}

	public void localDestroy() {
		if (getDialog() != null && getRetainInstance()) {
			getDialog().setOnDismissListener(null);
		}
		if (this.detail != null) {
			this.detail.getDescriptor().unInflate(null, this.detail, ((MasterVisualComponent) this.getActivity()).getParameters());
		}
	}

	@Override
	public void onDestroyView() {
		this.detail = null;
		super.onDestroyView();
	}

}
