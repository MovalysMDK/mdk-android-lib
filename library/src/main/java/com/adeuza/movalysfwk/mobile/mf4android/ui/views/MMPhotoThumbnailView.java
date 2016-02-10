package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.sql.Timestamp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore.Images.Thumbnails;
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
import com.adeuza.movalysfwk.mobile.mf4android.ui.command.MMPhotoCommand;
import com.adeuza.movalysfwk.mobile.mf4android.ui.command.MMPhotoConfig;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AndroidPhotoMetaData;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.MPhotoVOHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.MPhotoVO;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumericConstants;

/**
 * <p>Photo view widget used in the Movalys Mobile product for Android</p>
 * this component use the photo URI as String to display a thumbnail of this image
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author dmaurange
 *
 */
public class MMPhotoThumbnailView extends AbstractMMRelativeLayout<MPhotoVO> implements View.OnClickListener, MMComplexeComponent{

	/** the tag used to retain photoVM on orientation changes */
	private static final String PHOTO_THUMBNAIL_VM_KEY = "photoThumbnailVMKey";
	/** the tag used to retain photoConfig on orientation changes */
	private static final String PHOTO_THUMBNAIL_CONFIG_KEY= "photoThumbnailConfigKey";

	/**
	 * 
	 */

	public static final int ADD_PHOTO_REQUEST_CODE = Math.abs("ADD_PHOTO_REQUEST_CODE".hashCode()) & NumericConstants.HEXADECIMAL_MASK;

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
	 * @param p_oContext
	 */
	public MMPhotoThumbnailView(Context p_oContext) {
		this(p_oContext, null);
		if(!isInEditMode()) {
			this.photoVM = new MPhotoVO();
		}
	}

	/**
	 * @param p_oContext
	 * @param p_oAttrs
	 */
	public MMPhotoThumbnailView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.photoConfig = new MMPhotoConfig(true, p_oAttrs);
			this.linkChildrens(p_oAttrs);
			this.photoVM = new MPhotoVO();
		}
	}	

	/**
	 * 
	 */
	public void configureListeners(OnClickListener p_oListener) {
		/*View oRecursiveParent = this;
		while(oRecursiveParent.getParent() != null) {
			oRecursiveParent = (View) oRecursiveParent.getParent();
			if(oRecursiveParent instanceof MMPhotoFixedListView) {
				return;
			}
		}*/
		
		this.setOnClickListener(p_oListener);
		this.uiImageView.setOnClickListener(p_oListener); 
		this.uiImageName.setOnClickListener(p_oListener); 
		this.uiImageDescription.setOnClickListener(p_oListener); 
		this.uiImageDate.setOnClickListener(p_oListener); 
	}

	/**
	 * 
	 */
	protected void refreshThumbnail() {
		if ( this.photoVM.getLocalUri() != null ) {
			Uri oImageUri = Uri.parse(this.photoVM.getLocalUri());
			int iOriginalImageId = Integer.parseInt( oImageUri.getLastPathSegment());
			ContentResolver oContentResolver = this.getContext().getContentResolver();
			//Attention bug android 8125 : crash lors de la reconnection en usb stockage de masse
			//http://code.google.com/p/android/issues/detail?id=8125
			Bitmap oImage= Thumbnails.getThumbnail(oContentResolver, iOriginalImageId, Thumbnails.MINI_KIND, null);
			this.uiImageView.setImageBitmap(oImage);
		}
		else {
			this.uiImageView.setImageResource(application.getAndroidIdByRKey(AndroidApplicationR.component__photoview__nophoto));
		}

		this.uiImageDate.configurationSetValue(this.photoVM.getDate().getTime());
		this.uiImageName.setText(this.photoVM.getName());
		this.uiImageDescription.setText(this.photoVM.getDescription());
	}

	/**
	 * link the child views with custom attributes and add button onClickListener
	 * @param p_oAttrs  attributes of XML Component component_phone_textview
	 */
	private final void linkChildrens(AttributeSet p_oAttrs) {

		LayoutInflater oLayoutInflater = LayoutInflater.from(this.getContext());
		oLayoutInflater.inflate(this.application.getAndroidIdByRKey(AndroidApplicationR.fwk_component_photo_thumbnail_layout), this);

		this.uiImageView = (ImageView) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_photo_thumbnail__image_photo__value));
		this.uiImageName = (TextView) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_photo_thumbnail__name__value));
		this.uiImageDescription = (TextView) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_photo_thumbnail__description__value));
		this.uiImageDate = (MMDateTimeTextView) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_photo_thumbnail__date__value));

		if ( this.photoConfig.getThumbnailMaxWidth() != -1 ) {
			this.uiImageView.setMaxWidth(this.photoConfig.getThumbnailMaxWidth());
		}
	}


	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.AbstractMMRelativeLayout#configurationSetValue(java.lang.Object)
	 */
	@Override
	public void configurationSetValue(MPhotoVO p_oPhotoThumbnailViewValueObject) {
		if(p_oPhotoThumbnailViewValueObject != null) {
			this.photoVM = p_oPhotoThumbnailViewValueObject;
			this.updateUI();
		}
	}

	/**
	 * @param p_oPhotoThumbnailViewValueObject
	 * @throws BeanLoaderError
	 */
	private void updateUI() {
		try {

			if(this.photoVM == null) {
				this.uiImageDate.configurationSetValue(null);
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
					int iOriginalImageId = Integer.parseInt( oImageUri.getLastPathSegment());
					ContentResolver oContentResolver = getContext().getContentResolver();
					//Attention bug android 8125 : crash lors de la reconnection en usb stockage de masse
					//http://code.google.com/p/android/issues/detail?id=8125
					Bitmap oImage= Thumbnails.getThumbnail(oContentResolver, iOriginalImageId, Thumbnails.MINI_KIND, null);
					this.uiImageView.setImageBitmap(oImage);

					if ( this.photoVM.isUseComponentForMetadata()) {
						MMediaStoreImageService oMediaStoreImageService = BeanLoader.getInstance().getBean(MMediaStoreImageService.class);
						MImage oImageMetaData = oMediaStoreImageService.load(oImageUri, oContentResolver);
						this.photoVM.setDate(new Timestamp(oImageMetaData.getDateTaken()));
						this.photoVM.setDescription(oImageMetaData.getDescription());
						this.photoVM.setName(oImageMetaData.getTitle());
					}
				}
			}	

//			Log.v(TAG, "  use mediastore: " + this.photoVM.isUseComponentForMetadata());
//			Log.v(TAG, "  name: " + this.photoVM.getName());
//			Log.v(TAG, "  date: " + this.photoVM.getDate());
//			Log.v(TAG, "  desc: " + this.photoVM.getDescription());			

			if ( this.photoVM.getDate() != null ) {
				this.uiImageDate.configurationSetValue(this.photoVM.getDate().getTime());
			}
			else {
				this.uiImageDate.configurationSetValue(null);
			}
			this.uiImageName.setText(this.photoVM.getName());
			this.uiImageDescription.setText(this.photoVM.getDescription());

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

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationSetCustomValues(java.lang.String[])
	 */
	@Override
	public void configurationSetCustomValues(String[] p_t_sValues) {
		this.aivDelegate.configurationSetCustomValues(p_t_sValues);
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#getValueType()
	 */
	@Override
	public Class<?> getValueType() {
		return MPhotoVO.class;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationGetValue()
	 */
	@Override
	public MPhotoVO configurationGetValue() {
		return this.photoVM;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#configurationGetCustomValues()
	 */
	@Override
	public String[] configurationGetCustomValues() {
		return aivDelegate.configurationGetCustomValues();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doOnPostAutoBind() {
		this.configureListeners(this);		
		
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#destroy()
	 */
	@Override
	public void destroy() {
		this.uiImageView.setOnClickListener(null); 
		this.uiImageName.setOnClickListener(null); 
		this.uiImageDescription.setOnClickListener(null); 
		this.uiImageDate.setOnClickListener(null); 
		aivDelegate.destroy();
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
			Intent oPhotoDetailEdit = new Intent(this.getContext(), MMPhotoCommand.class);
			oPhotoDetailEdit.putExtra(MMPhotoConfig.PARAMETER_NAME, this.photoConfig);

			AndroidPhotoMetaData oPhotoMetaData = new AndroidPhotoMetaData();					
			updateToPhotoMetaData(oPhotoMetaData);
			oPhotoDetailEdit.putExtra(MMPhotoCommand.PHOTOMETADATA_PARAMETER, oPhotoMetaData );

			AbstractMMActivity oActivity = (AbstractMMActivity) ((AndroidApplication) Application.getInstance()).getCurrentActivity();
			oActivity.startActivityForResult(oPhotoDetailEdit, this, ADD_PHOTO_REQUEST_CODE);			
		}		
	}


	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.AbstractFixedListView#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int p_iRequestCode, int p_iResultCode, Intent p_oIntent) {

		Log.d(Application.LOG_TAG, "MMPhotoThumbnailView.onActivityResult, requestCode: " + p_iRequestCode + ", resultCode: " +  p_iResultCode);
		if (p_iRequestCode == ADD_PHOTO_REQUEST_CODE && p_iResultCode == Activity.RESULT_OK ) {
			Log.d(Application.LOG_TAG, "  intent: " + p_oIntent.toString());
			AndroidPhotoMetaData oPmd = (AndroidPhotoMetaData) p_oIntent.getSerializableExtra(MMPhotoCommand.PHOTOMETADATA_PARAMETER);
			Log.d(Application.LOG_TAG, "  back from MMPhotoCommand: " + oPmd.toDebug());
			if ( updateFromPhotoMetaData(oPmd)) { 
				this.aivDelegate.changed();
			}
		}
	}

	/**
	 * @param p_oPhotoMetaData
	 */
	private void updateToPhotoMetaData(AndroidPhotoMetaData p_oPhotoMetaData) {
		MPhotoVOHelper.updateToPhotoMetaData(this.photoVM, p_oPhotoMetaData);
		p_oPhotoMetaData.setReadOnly(!this.isEdit());
	}

	/**
	 * @param p_oPmd
	 */
	private boolean updateFromPhotoMetaData(AndroidPhotoMetaData p_oPmd) {
		boolean r_bChanged = MPhotoVOHelper.updateToPhotoVO(p_oPmd, this.photoVM);
		refreshThumbnail();
		return r_bChanged ;
	}

	/**
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent#isNullOrEmptyValue(java.lang.Object)
	 */
	@Override
	public boolean isNullOrEmptyValue(MPhotoVO p_oObject) {
		return p_oObject == null || (p_oObject.getLocalUri() == null && p_oObject.getRemoteUri() == null);
	}

	/**
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.uicomponent.configurable.ConfigurableVisualComponent#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return this.photoVM != null && (this.photoVM.getLocalUri() != null || this.photoVM.getRemoteUri() != null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChanged() {
		return this.aivDelegate.isChanged();
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
}
