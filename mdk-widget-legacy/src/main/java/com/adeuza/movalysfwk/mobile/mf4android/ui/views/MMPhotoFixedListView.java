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

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MEntity;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.command.MMPhotoConfig;
import com.adeuza.movalysfwk.mobile.mf4android.ui.command.PhotoCommand;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AndroidPhotoMetaData;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.MPhotoVOHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.MPhotoVO;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.VMWithPhoto;

/**
 * <p>
 * Fixed list component
 * </p>
 * <p>
 * you can customize this component :<br/>
 * <ul>
 * <li>add xmlns:movalys="http://www.adeuza.com/movalys/mm/android" on the main
 * tag</li>
 * <li>include a
 * com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPositionEditText</li>
 * <li>add titleMode attribute with "false" value to hidde the title and +
 * button (default is true)</li>
 * </ul>
 * </p>
 * 
 * @param <ITEM>
 *            the model of the photo
 * @param <ITEMVM>
 *            the viewmodel of the photo
 * 
 */
public class MMPhotoFixedListView<ITEM extends MEntity, ITEMVM extends ItemViewModel<ITEM>>
extends AbstractFixedListView<ITEM, ITEMVM> implements MMComplexeComponent{

	/** logging tag */
	private static final String TAG = "MMPhotoFixedListView";
	
	/** photo configuration */
	private MMPhotoConfig photoCommandConfig;

	/** Component request code */
	private int requestCode = -1;
	
	/**
	 * Create new MMPhotoFixedListView
	 * @param p_oContext the android context
	 * @param p_oAttrs the android view AttributeSet (in layout XML)
	 */
	public MMPhotoFixedListView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.photoCommandConfig = new MMPhotoConfig(false, p_oAttrs);
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.AbstractFixedListView#doOnClickAddButton()
	 */
	@Override
	protected void doOnClickAddButton(ITEMVM p_oItemvm) {
		super.doOnClickAddButton(p_oItemvm);

		// ajout d'une photo
		
		Intent oPhotoDetailAdd = new Intent(this.getContext(), 
			BeanLoader.getInstance().getBeanClassByType("photoCommand"));
		oPhotoDetailAdd.putExtra(MMPhotoConfig.PARAMETER_NAME, this.photoCommandConfig);
		((AbstractMMActivity) this.getContext()).startActivityForResult(oPhotoDetailAdd, this, getRequestCode());

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.AbstractFixedListView#doOnClickSelectItem(android.view.View)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doOnClickSelectItem(View p_oView) {
		super.doOnClickSelectItem(p_oView);
		ITEMVM oSelectedViewModel = null;
		if (p_oView.getTag() != null) {

			if(p_oView.getTag() instanceof ItemViewModel<?>) {
				oSelectedViewModel = (ITEMVM) p_oView.getTag();
//				setSelectedVM((ITEMVM) p_oView.getTag());
			} 
			else {
				Log.e("MMPhotoFixedList", "The selected item cannot be updated because its viewModel hasn't be found");
				return;
			}

			// si je suis sur une modif d'une photo existante
			Intent oPhotoDetailEdit = new Intent(this.getContext(), 
					BeanLoader.getInstance().getBeanClassByType("photoCommand"));

			oPhotoDetailEdit.putExtra(MMPhotoConfig.PARAMETER_NAME, this.photoCommandConfig);
			AndroidPhotoMetaData oPhotoMetaData = new AndroidPhotoMetaData();
			oPhotoMetaData.setReadOnly(true);
			oPhotoMetaData.setId(oSelectedViewModel.getIdVM());
			VMWithPhoto oPhotoVM = (VMWithPhoto) oSelectedViewModel;
			MPhotoVO oPhotoVO = oPhotoVM.getPhoto();
			Log.v("doOnClickSelectItem", "view tag" + oSelectedViewModel);

			MPhotoVOHelper.updateToPhotoMetaData(oPhotoVO, oPhotoMetaData);
			oPhotoDetailEdit.putExtra(PhotoCommand.PHOTOMETADATA_PARAMETER, oPhotoMetaData);

			Log.d("doOnClickSelectItem", "linked activity: " + this.getContext().getClass().getName());
			((AbstractMMActivity) this.getContext()).startActivityForResult(oPhotoDetailEdit, this, getRequestCode());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.AbstractFixedListView#onActivityResult(int,
	 *      int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int p_iRequestCode, int p_iResultCode,
			Intent p_oIntent) {
		if (p_iRequestCode == getRequestCode() && p_iResultCode == Activity.RESULT_OK) {
			AndroidPhotoMetaData oPmd = (AndroidPhotoMetaData) p_oIntent
					.getSerializableExtra(PhotoCommand.PHOTOMETADATA_PARAMETER);
			oPmd.setReadOnly(true);
			ITEMVM oPhotoTakenVM = this.getSelectedVM();
			MPhotoVO oPhotoVO = ((VMWithPhoto) oPhotoTakenVM).getPhoto();
			boolean bDm = MPhotoVOHelper.updateToPhotoVO(oPmd, oPhotoVO); // le
			// parent n'est pas encore connu,
			if (bDm) {
				oPhotoTakenVM.setDirectlyModified(true); // pour notifier le parent du changement
			}

//			int indexOfModifiedItem = this.getAdapter().getMasterVM().indexOf(oPhotoTakenVM);
			boolean bAdd = doValidItem(false,-1, oPhotoTakenVM);
			if (bAdd) {
				oPhotoTakenVM.setDirectlyModified(true); // pour notifier le parent du changement
			}
		}
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Log.d(TAG, "[onSaveInstanceState]");
		Parcelable superParcel = super.onSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putInt("requestCode", this.requestCode);
		return r_oBundle;
	}

	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		Log.d(TAG, "[onRestoreInstanceState] state :"+p_oState);
		Bundle r_oBundle = (Bundle) p_oState ;
		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.onRestoreInstanceState(oParentParcelable);
		this.requestCode = r_oBundle.getInt("requestCode");
	}

	/**
	 * Nothing to do {@inheritDoc}
	 * 
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.AbstractFixedListView#treatCustomButton(android.view.View)
	 */
	@Override
	public boolean treatCustomButton(View p_oView) {
		// Nothing to do
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.listener.ConfigurationListener#beforeConfigurationChanged(java.util.Map)
	 */
	@Override
	public void beforeConfigurationChanged(Map<String, Object> p_oConfigurationMap) {
		// nothing to do
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.listener.ConfigurationListener#afterConfigurationChanged(java.util.Map)
	 */
	@Override
	public void afterConfigurationChanged(Map<String, Object> p_oConfigurationMap) {
		// nothing to do
	}

	@Override
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		return null;
	}

	@Override
	public void prepareDialogFragment(MMDialogFragment p_oDialog, Bundle p_oDialogFragmentArguments) {
		// Nothing to do

	}

	@Override
	protected void wrapCurrentView(final View p_oView, ITEMVM p_oCurrentViewModel) {
		super.wrapCurrentView(p_oView, p_oCurrentViewModel);
		ViewGroup oViewGroup = (ViewGroup) p_oView;
		for(int index = 0 ; index < oViewGroup.getChildCount() ; index++) {
			if(oViewGroup.getChildAt(index) instanceof MMPhotoThumbnailView) {
				MMPhotoThumbnailView tv = (MMPhotoThumbnailView) oViewGroup.getChildAt(index);
				tv.configureListeners(new OnClickListener() {
					@Override
					public void onClick(View p_oV) {
						MMPhotoFixedListView.this.doOnClickSelectItem(p_oView);
					}
				});
			}	
		}
	}

	@Override
	public int getRequestCode() {
		if (this.requestCode == -1) {
			this.requestCode = this.hashCode() & REQUEST_CODE_MASK;
		}
		return this.requestCode;
	}
}
