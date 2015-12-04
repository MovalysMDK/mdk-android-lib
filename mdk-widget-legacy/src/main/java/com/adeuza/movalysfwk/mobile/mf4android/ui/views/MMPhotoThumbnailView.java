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

import java.sql.Timestamp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.mediastore.images.MImage;
import com.adeuza.movalysfwk.mobile.mf4android.mediastore.images.MMediaStoreImageService;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMRelativeLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.command.MMPhotoConfig;
import com.adeuza.movalysfwk.mobile.mf4android.ui.command.PhotoCommand;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentBindDestroy;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentNullOrEmpty;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AndroidPhotoMetaData;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.MPhotoVOHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.MPhotoVO;

/**
 * <p>Photo view widget used in the Movalys Mobile product for Android</p>
 * this component use the photo URI as String to display a thumbnail of this image
 *
 */
public class MMPhotoThumbnailView extends AbstractMMRelativeLayout<MPhotoVO> implements View.OnClickListener, MMComplexeComponent, 
	ComponentBindDestroy, ComponentNullOrEmpty<MPhotoVO>, ComponentEnable,
	ComponentReadableWrapper<MPhotoVO>, ComponentWritableWrapper<MPhotoVO> {

	/** the tag used to retain photoVM on orientation changes */
	private static final String PHOTO_THUMBNAIL_VM_KEY = "photoThumbnailVMKey";
	/** the tag used to retain photoConfig on orientation changes */
	private static final String PHOTO_THUMBNAIL_CONFIG_KEY= "photoThumbnailConfigKey";
	
	/**
	 * Component request code
	 */
	private int requestCode = -1;

	/**
	 * Component view model
	 */
	private MPhotoVO photoVM ;

	/**
	 * Image display 
	 */
	private ImageView uiImageView;

	/**
	 * Name text field 
	 */
	private TextView uiImageName ;

	/**
	 * Description text field
	 */
	private TextView uiImageDescription ;

	/**
	 * Date text field
	 */
	private MMDateTimeTextView uiImageDate ;

	/**
	 * Application instance
	 */
	private AndroidApplication application = (AndroidApplication) Application.getInstance();

	/**
	 * 
	 */
	private MMPhotoConfig photoConfig ;

	/**
	 * Constructor
	 * inherited from android.view.View
	 * @param p_oContext android context
	 */
	public MMPhotoThumbnailView(Context p_oContext) {
		this(p_oContext, null);
		if(!isInEditMode()) {
			this.photoVM = new MPhotoVO();
		}
	}

	/**
	 * Constructor
	 * inherited from android.view.View
	 * @param p_oContext android context
	 * @param p_oAttrs the View AttributeSet
	 */
	public MMPhotoThumbnailView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs, MPhotoVO.class);
		if(!isInEditMode()) {
			this.photoConfig = new MMPhotoConfig(true, p_oAttrs);
			this.linkChildrens();
			this.photoVM = new MPhotoVO();
		}
	}	

	/**
	 * Configure listener on this component
	 * @param p_oListener the component listener
	 */
	public void configureListeners(OnClickListener p_oListener) {
		this.setOnClickListener(p_oListener);
		this.uiImageView.setOnClickListener(p_oListener); 
		this.uiImageName.setOnClickListener(p_oListener); 
		this.uiImageDescription.setOnClickListener(p_oListener); 
		this.uiImageDate.setOnClickListener(p_oListener); 
	}

	/**
	 * Set the photo vm
	 * <p>DO NOT USE<p>
	 * <p>This method is not private for the framework to work, but it's not to
	 * use in integration</p>
	 * @param p_oPhoto the photo view model
	 */
	public void setPhotoVM(MPhotoVO p_oPhoto) {
		this.photoVM = p_oPhoto;
	}
	
	/**
	 * Get the photo view mode
	 * <p>DO NOT USE<p>
	 * <p>This method is not private for the framework to work, but it's not to
	 * use in integration</p>
	 * @return the photo view model
	 */
	public MPhotoVO getPhotoVM() {
		return this.photoVM;
	}

	/**
	 * 
	 */
	protected void refreshThumbnail() {
		if ( this.photoVM.getLocalUri() != null ) {
			this.uiImageView.setImageBitmap(BeanLoader.getInstance().getBean(MMediaStoreImageService.class)
					.loadBitmap(Uri.parse(this.photoVM.getLocalUri()), this.getThumbnailMaxWidth(), this.getContext().getContentResolver()));
		}
		else {
			this.uiImageView.setImageResource(application.getAndroidIdByRKey(AndroidApplicationR.component__photoview__nophoto));
		}

		this.uiImageDate.getComponentDelegate().configurationSetValue(this.photoVM.getDate().getTime());
		this.uiImageName.setText(this.photoVM.getName());
		this.uiImageDescription.setText(this.photoVM.getDescription());
	}

	/**
	 * link the child views with custom attributes and add button onClickListener
	 */
	private final void linkChildrens() {

		LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
		oLayoutInflater.inflate(this.application.getAndroidIdByRKey(AndroidApplicationR.fwk_component_photo_thumbnail_layout), this);

		this.uiImageView = (ImageView) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_photo_thumbnail__image_photo__value));
		this.uiImageName = (TextView) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_photo_thumbnail__name__value));
		this.uiImageDescription = (TextView) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_photo_thumbnail__description__value));
		this.uiImageDate = (MMDateTimeTextView) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_photo_thumbnail__date__value));
	}

	/**
	 * update component user interface
	 */
	public void updateUI() {
		try {

			if(this.photoVM == null) {
				this.uiImageDate.getComponentDelegate().configurationSetValue(null);
				this.uiImageName.setText(null);
				this.uiImageDescription.setText(null);
				this.uiImageView.setImageResource(application.getAndroidIdByRKey(AndroidApplicationR.component__photoview__nophoto));
			}
			else {
				this.setTag( this.photoVM );

				if ( this.photoVM.getLocalUri() == null ) {
					this.uiImageView.setImageResource( application.getAndroidIdByRKey(AndroidApplicationR.component__photoview__nophoto));
				}
				else {
					Uri oImageUri = Uri.parse(this.photoVM.getLocalUri());
					ContentResolver oContentResolver = getContext().getContentResolver();

					this.uiImageView.setImageBitmap(BeanLoader.getInstance().getBean(MMediaStoreImageService.class)
							.loadBitmap(oImageUri, this.getThumbnailMaxWidth(), oContentResolver));

					if ( this.photoVM.isUseComponentForMetadata()) {
						MMediaStoreImageService oMediaStoreImageService = BeanLoader.getInstance().getBean(MMediaStoreImageService.class);
						MImage oImageMetaData = oMediaStoreImageService.load(oImageUri, oContentResolver);
						this.photoVM.setDate(new Timestamp(oImageMetaData.getDateTaken()));
						this.photoVM.setDescription(oImageMetaData.getDescription());
						this.photoVM.setName(oImageMetaData.getTitle());
					}
				}
			}

			if ( this.photoVM !=null && this.photoVM.getDate() != null ) {
				this.uiImageDate.getComponentDelegate().configurationSetValue(this.photoVM.getDate().getTime());
			}
			else {
				this.uiImageDate.getComponentDelegate().configurationSetValue(null);
			}
			if (this.photoVM != null) {
				this.uiImageName.setText(this.photoVM.getName());
				this.uiImageDescription.setText(this.photoVM.getDescription());
			}

		} catch ( Exception oException) {
			Log.e(Application.LOG_TAG,"Imposible to get Thumbnail with no image id", oException);
			this.uiImageView.setImageResource(application.getAndroidIdByRKey(AndroidApplicationR.component__photoview__nophoto));
		}
	}


	@Override
	public boolean hasFocus() {
		if(!isInEditMode()) {
			return uiImageName.hasFocus() || uiImageView.hasFocus()
					|| uiImageDescription.hasFocus() || uiImageDate.hasFocus();
		}
		return false;
	}

	/**
	 * Clear the image view to the default empty image
	 */
	public void clear() {
		this.uiImageView.setImageResource( this.application.getAndroidIdByRKey(AndroidApplicationR.component__photoview__nophoto));
	}

	@Override
	public void onClick(View p_oView) {
//		View oRecursiveParent = this;
//		while(oRecursiveParent.getParent() != null) {
//			oRecursiveParent = (View) oRecursiveParent.getParent();
//			if(oRecursiveParent instanceof MMPhotoFixedListView) {
//				MMPhotoFixedListView oPhotoFixedList = (MMPhotoFixedListView) oRecursiveParent;
//				for(int index = 0 ; index < oPhotoFixedList.getAdapter().getMasterVM().getList().size(); index++) {
//					VMWithPhoto oVMWithPhoto = (VMWithPhoto) oPhotoFixedList.getAdapter().getMasterVM().getList().get(index);
//					if(oVMWithPhoto.getPhoto().equals(this.photoVM)) {
//						View oViewItemClicked = oPhotoFixedList.getChildAt(index);
//						((MMPhotoFixedListView) oPhotoFixedList).doOnClickSelectItem(oViewItemClicked);
//					}
//				}
//				return;
//			}
//		}

		if (p_oView != null) {
			Intent oPhotoDetailEdit = new Intent(this.getContext(), 
				BeanLoader.getInstance().getBeanClassByType("photoCommand"));
			oPhotoDetailEdit.putExtra(MMPhotoConfig.PARAMETER_NAME, this.photoConfig);

			AndroidPhotoMetaData oPhotoMetaData = new AndroidPhotoMetaData();
			updateToPhotoMetaData(oPhotoMetaData);
			oPhotoDetailEdit.putExtra(PhotoCommand.PHOTOMETADATA_PARAMETER, oPhotoMetaData );

			AbstractMMActivity oActivity = (AbstractMMActivity) ((AndroidApplication) Application.getInstance()).getCurrentActivity();
			oActivity.startActivityForResult(oPhotoDetailEdit, this, getRequestCode());			
		}		
	}


	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int p_iRequestCode, int p_iResultCode, Intent p_oIntent) {

		Log.d(Application.LOG_TAG, "MMPhotoThumbnailView.onActivityResult, requestCode: " + p_iRequestCode + ", resultCode: " +  p_iResultCode);
		if (p_iRequestCode == getRequestCode() && p_iResultCode == Activity.RESULT_OK ) {
			Log.d(Application.LOG_TAG, "  intent: " + p_oIntent.toString());
			AndroidPhotoMetaData oPmd = (AndroidPhotoMetaData) p_oIntent.getSerializableExtra(PhotoCommand.PHOTOMETADATA_PARAMETER);
			Log.d(Application.LOG_TAG, "  back from MMPhotoCommand: " + oPmd.toDebug());
			if ( updateFromPhotoMetaData(oPmd)) { 
				this.aivFwkDelegate.changed();
			}
		}
	}

	/**
	 * Update metadata from photo
	 * @param p_oPhotoMetaData Android Photo metadata
	 */
	private void updateToPhotoMetaData(AndroidPhotoMetaData p_oPhotoMetaData) {
		MPhotoVOHelper.updateToPhotoMetaData(this.photoVM, p_oPhotoMetaData);
		p_oPhotoMetaData.setReadOnly(!this.aivFwkDelegate.isEdit());
	}

	/**
	 * update photo from metadata
	 * @param p_oPmd Android Photo metadata
	 * @return true if changed, false otherwise
	 */
	private boolean updateFromPhotoMetaData(AndroidPhotoMetaData p_oPmd) {
		boolean r_bChanged = MPhotoVOHelper.updateToPhotoVO(p_oPmd, this.photoVM);
		refreshThumbnail();
		return r_bChanged ;
	}

	@Override
	public Parcelable onSaveInstanceState() {
		//return super.onSaveInstanceState();
		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putSerializable(PHOTO_THUMBNAIL_CONFIG_KEY, this.photoConfig);
		r_oBundle.putSerializable(PHOTO_THUMBNAIL_VM_KEY, this.photoVM);
		r_oBundle.putInt("requestCode", this.requestCode);
		return r_oBundle;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Parcelable superOnSaveInstanceState() {
		Log.d(Application.LOG_TAG, "[superOnSaveInstanceState]");

		Parcelable superParcel = super.superOnSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putSerializable(PHOTO_THUMBNAIL_CONFIG_KEY, this.photoConfig);
		r_oBundle.putSerializable(PHOTO_THUMBNAIL_VM_KEY, this.photoVM);
		return r_oBundle;
	}

	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
//		super.onRestoreInstanceState(p_oState);
		Bundle r_oBundle = (Bundle) p_oState ;
		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);

		this.photoVM= (MPhotoVO) r_oBundle.getSerializable(PHOTO_THUMBNAIL_VM_KEY);
		this.photoConfig = (MMPhotoConfig) r_oBundle.getSerializable(PHOTO_THUMBNAIL_CONFIG_KEY);
		this.requestCode = r_oBundle.getInt("requestCode");
		this.updateUI();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Log.d(Application.LOG_TAG, "[onRestoreInstanceState] state :"+p_oState);
		Bundle r_oBundle = (Bundle) p_oState ;
		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.superOnRestoreInstanceState(oParentParcelable);

		this.photoVM= (MPhotoVO) r_oBundle.getSerializable(PHOTO_THUMBNAIL_VM_KEY);
		this.photoConfig = (MMPhotoConfig) r_oBundle.getSerializable(PHOTO_THUMBNAIL_CONFIG_KEY);
		this.updateUI();
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
	
	/****************************************************************************************************
	 *********************************** Framework delegate callback ************************************
	 ****************************************************************************************************/

	/**
	 * {@inheritDoc}
	 * @see ComponentBindDestroy#doOnPostAutoBind()
	 */
	@Override
	public void doOnPostAutoBind() {
		this.configureListeners(this);
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentBindDestroy#destroy()
	 */
	@Override
	public void destroy() {
		this.uiImageView.setOnClickListener(null);
		this.uiImageName.setOnClickListener(null);
		this.uiImageDescription.setOnClickListener(null);
		this.uiImageDate.setOnClickListener(null);
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public MPhotoVO configurationGetValue() {
		return this.getPhotoVM();
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(MPhotoVO p_oObjectToSet) {
		if(p_oObjectToSet != null) {
			this.setPhotoVM(p_oObjectToSet);
			this.updateUI();
		}
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.getPhotoVM() != null 
				&& (this.getPhotoVM().getLocalUri() != null 
				|| this.getPhotoVM().getRemoteUri() != null);
	}

	@Override
	public boolean isNullOrEmptyValue(MPhotoVO p_oObject) {
		return p_oObject == null || (p_oObject.getLocalUri() == null && p_oObject.getRemoteUri() == null);
	}

	@Override
	public void enableComponent(boolean p_bEnable) {
		this.uiImageView.setEnabled(p_bEnable);
	}

	/**
	 * Return the max thumbnail width in pixels
	 * @return the max thumbnail width in pixels
	 */
	protected int getThumbnailMaxWidth() {
		return (int) (this.photoConfig.getThumbnailMaxWidth() * this.getResources().getDisplayMetrics().density);
	}
	
	@Override
	public int getRequestCode() {
		if (this.requestCode == -1) {
			this.requestCode = this.hashCode() & REQUEST_CODE_MASK;
		}
		return this.requestCode;
	}
}
