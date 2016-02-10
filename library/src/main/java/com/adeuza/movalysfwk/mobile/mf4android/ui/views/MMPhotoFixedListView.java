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
import com.adeuza.movalysfwk.mobile.mf4android.ui.command.MMPhotoCommand;
import com.adeuza.movalysfwk.mobile.mf4android.ui.command.MMPhotoConfig;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AndroidPhotoMetaData;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.MPhotoVOHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
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
 * <p>
 * Copyright (c) 2011
 * </p>
 * <p>
 * Company: Adeuza
 * </p>
 * 
 * @author smaitre
 * @param <ITEM>
 *            the model of the photo
 * @param <ITEMVM>
 *            the viewmodel of the photo
 * 
 */
public class MMPhotoFixedListView<ITEM extends MEntity, ITEMVM extends ItemViewModel<ITEM>>
extends AbstractFixedListView<ITEM, ITEMVM> implements MMComplexeComponent{

	private static final String TAG = "MMPhotoFixedListView";

	/**
	 * 
	 */
	private MMPhotoConfig photoCommandConfig;

	/**
	 * @param p_oContext
	 * @param p_oAttrs
	 */
	public MMPhotoFixedListView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.photoCommandConfig = new MMPhotoConfig(false, p_oAttrs);
		}
	}

	public MMPhotoFixedListView(Context p_oContext) {
		super(p_oContext);
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
		Intent oPhotoDetailAdd = new Intent(this.getContext(), MMPhotoCommand.class);
		oPhotoDetailAdd.putExtra(MMPhotoConfig.PARAMETER_NAME, this.photoCommandConfig);
		((AbstractMMActivity) this.getContext()).startActivityForResult(oPhotoDetailAdd, this, MMPhotoThumbnailView.ADD_PHOTO_REQUEST_CODE);

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
			Intent oPhotoDetailEdit = new Intent(this.getContext(), MMPhotoCommand.class);

			oPhotoDetailEdit.putExtra(MMPhotoConfig.PARAMETER_NAME, this.photoCommandConfig);
			AndroidPhotoMetaData oPhotoMetaData = new AndroidPhotoMetaData();
			oPhotoMetaData.setReadOnly(true);
			oPhotoMetaData.setId(oSelectedViewModel.getIdVM());
			VMWithPhoto oPhotoVM = (VMWithPhoto) oSelectedViewModel;
			MPhotoVO oPhotoVO = oPhotoVM.getPhoto();
			Log.v("doOnClickSelectItem", "view tag" + oSelectedViewModel);

			MPhotoVOHelper.updateToPhotoMetaData(oPhotoVO, oPhotoMetaData);
			oPhotoDetailEdit.putExtra(MMPhotoCommand.PHOTOMETADATA_PARAMETER, oPhotoMetaData);

			Log.d("doOnClickSelectItem", "linked activity: " + this.getContext().getClass().getName());
			((AbstractMMActivity) this.getContext()).startActivityForResult(oPhotoDetailEdit, this, MMPhotoThumbnailView.ADD_PHOTO_REQUEST_CODE);
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
		if (p_iRequestCode == MMPhotoThumbnailView.ADD_PHOTO_REQUEST_CODE
				&& p_iResultCode == Activity.RESULT_OK) {
			AndroidPhotoMetaData oPmd = (AndroidPhotoMetaData) p_oIntent
					.getSerializableExtra(MMPhotoCommand.PHOTOMETADATA_PARAMETER);
			oPmd.setReadOnly(true);
			ITEMVM oPhotoTakenVM = this.getSelectedVM();
			MPhotoVO oPhotoVO = ((VMWithPhoto) oPhotoTakenVM).getPhoto();
			boolean bDm = MPhotoVOHelper.updateToPhotoVO(oPmd, oPhotoVO); // le
			// parent
			// n'est
			// pas
			// encore
			// connu,
			if (bDm) {
				oPhotoTakenVM.setDirectlyModified(true); // pour notifier le
				// parent du
				// changement
			}

//			int indexOfModifiedItem = this.getAdapter().getMasterVM().indexOf(oPhotoTakenVM);
			boolean bAdd = doValidItem(false,-1, oPhotoTakenVM);
			if (bAdd) {
				oPhotoTakenVM.setDirectlyModified(true); // pour notifier le
				// parent du
				// changement
			}
		}
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Log.d(TAG, "[onSaveInstanceState]");
		return super.onSaveInstanceState();
	}

	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		super.onRestoreInstanceState(p_oState);
		Log.d(TAG, "[onRestoreInstanceState] state :"+p_oState);
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
	 * 
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.AbstractFixedListView#createView(com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel)
	 */
	@Override
	public View createView(ITEMVM p_oViewModel) {
		ViewGroup oReturnView =  (ViewGroup) super.createView(p_oViewModel);
		//		//Surcharge du onclick du MMPhotoThumbnaiView dans le cas du MMPhotoFixedList
		//		for(int index = 0 ; index < oReturnView.getChildCount() ; index++) {
		//			if(oReturnView.getChildAt(index) instanceof MMPhotoThumbnailView) {
		//				oReturnView.getChildAt(index).setOnClickListener(new OnClickListener() {
		//				
		//					@Override
		//					public void onClick(View v) {
		//						MMPhotoFixedListView.this.doOnClickSelectItem(oReturnView)
		//					}
		//				});
		//			}
		//		}
		return oReturnView;
	}

	/**
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.listener.ConfigurationListener#beforeConfigurationChanged(java.util.Map)
	 */
	@Override
	public void beforeConfigurationChanged(
			Map<String, Object> p_oConfigurationMap) {
		// nothing to do
	}

	/**
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.listener.ConfigurationListener#afterConfigurationChanged(java.util.Map)
	 */
	@Override
	public void afterConfigurationChanged(
			Map<String, Object> p_oConfigurationMap) {
		// nothing to do
	}

	@Override
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		return null;
	}

	@Override
	public void prepareDialogFragment(MMDialogFragment p_oDialog,
			Bundle p_oDialogFragmentArguments) {
		// Nothing to do

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
}
