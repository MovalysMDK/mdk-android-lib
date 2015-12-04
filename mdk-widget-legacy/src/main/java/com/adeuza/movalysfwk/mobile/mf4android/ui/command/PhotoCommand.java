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
package com.adeuza.movalysfwk.mobile.mf4android.ui.command;

import java.util.Map;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.mediastore.MMediaException;
import com.adeuza.movalysfwk.mobile.mf4android.mediastore.images.MImage;
import com.adeuza.movalysfwk.mobile.mf4android.mediastore.images.MMediaStoreImageService;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AndroidPhotoMetaData;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMDateTimeTextView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPositionTextView;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.AbstractBusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionFail;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionFailEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionSuccess;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionSuccessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.PhotoMetaData;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumericConstants;

/**
 * This photo command is an Activity that can :
 * <ul>
 * <li>Take a photo using an intent to a camera activity</li>
 * <li>Choose an existing image using galery or other activities that can handle
 * an ACTION_GET for images/* data</li>
 * <li>View and Update image metadata</li>
 * </ul>
 * 
 * this activity wait for 2 informations in intent :
 * <ul>
 * <li>idPhoto for the photo id as string</li>
 * <li>photoMetadata the {@link PhotoMetaData} containing existing photo data</li>
 * </ul>
 * 
 * 
 */
public class PhotoCommand extends AbstractMMActivity {

	/**
	 * Tag used by logs.
	 */
	private static final String LOG_TAG = "MFWKPHOTO";

	/**
	 * Jpeg content type
	 */
	private static final String MIME_IMAGE = "image/*";

	/**
	 * Jpeg content type
	 */
	private static final String MIME_IMAGE_JPEG = "image/jpeg";

	/**
	 * URI content:
	 */
	private static final String URI_CONTENT = "content:";

	/**
	 * parameter used to retreive metadata into {@link Intent}
	 */
	public static final String PHOTOMETADATA_PARAMETER = "photoMetadataParam";
	/**
	 * parameter used to retrieve the photo uri
	 */
	public static final String TMP_PHOTO_URI_PARAMETER = "tmpPhotoUri";

	/**
	 * The key use to retain the uri of the current photo
	 */
	private static final String PHOTO_COMMAND_URI_KEY = "photoCommandUriKey";

	/**
	 * The key used to retain the current state of the erase button.
	 */
	private static final String PHOTO_COMMAND_ERASE_ENABLED_KEY = "photoCommandEraseEnabledKey";

	/**
	 * Return code for take photo activity
	 */
	private static final int TAKE_PHOTO_ACTIVITY = "TAKE_PHOTO_ACTIVITY".hashCode() & NumericConstants.HEXADECIMAL_MASK;

	/**
	 * Return code for select picture activity
	 */
	private static final int SELECT_PICTURE_ACTIVITY = "SELECT_PICTURE_ACTIVITY".hashCode() & NumericConstants.HEXADECIMAL_MASK;

	/**
	 * Return code for select picture activity on KITKAT and up version
	 */
	private static final int SELECT_KITKAT_PICTURE_ACTIVITY = "SELECT_KITKAT_PICTURE_ACTIVITY".hashCode() & NumericConstants.HEXADECIMAL_MASK;
	/** 
	 * Taille max de l'image créé 
	 */
	private static final int MAX_WIDTH_OF_BITMAP = 640 ;

	/**
	 * Mediastore image service
	 */
	private MMediaStoreImageService mediaStoreImageService;

	/**
	 * Photo metadata
	 */
	protected AndroidPhotoMetaData photoMetadata ;

	/* UI Components */
	/** Button "OK". */
	private View uiOkButton;

	/** Button "Cancel". */
	private View uiCancelButton;

	/** Button "Camera". */
	private View uiButtonCamera;

	/** Button "Attachment". */
	private View uiButtonAttachment;

	/** Button "Erase". */
	private View uiButtonErase;

	/** Photo name */
	private TextView uiPhotoName;

	/** Photo description */
	private TextView uiPhotoDescription;

	/** Photo date. */
	private MMDateTimeTextView uiPhotoDate;

	/** Photo location. */
	private MMPositionTextView uiPhotoPosition;

	/** the photo field on the panel */
	private ImageView uiPhotoView;

	/** used to know if we create a new photo or edit an old one **/
	private boolean isEmptyPhoto=false;

	/**
	 * Temporary uri of the new photo (before launching the camera activity). Will become the new uri of photoMetadata
	 * when photo will be validated in the camera activity.
	 */
	private transient Uri m_sPhotoUri;

	/**
	 * The photo config
	 */
	private MMPhotoConfig photoConfig;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle p_oSavedInstanceState) {
		super.onCreate(p_oSavedInstanceState);

		AndroidApplication mApplication = AndroidApplication.getInstance();

		this.setContentView(mApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_photo_photocommand_layout));

		this.mediaStoreImageService = BeanLoader.getInstance().getBean(MMediaStoreImageService.class);

		// Validation button - Mandatory
		this.uiOkButton = this.findViewById(mApplication.getAndroidIdByRKey(AndroidApplicationR.component_photo__button_ok__button));
		this.uiOkButton.setOnClickListener(new OnClickOkListener());

		// Cancel button - Mandatory
		this.uiCancelButton = this.findViewById(mApplication.getAndroidIdByRKey(AndroidApplicationR.component_photo__button_cancel__button));
		this.uiCancelButton.setOnClickListener(new OnClickCancelListener());

		this.uiButtonCamera = this.findViewById(mApplication.getAndroidIdByRKey(AndroidApplicationR.component_photo__camera__button));
		if (this.uiButtonCamera != null) {
			this.uiButtonCamera.setOnClickListener(new OnClickCameraListener());
		}

		this.uiButtonAttachment = this.findViewById(mApplication.getAndroidIdByRKey(AndroidApplicationR.component_photo__attachment__button));
		if (this.uiButtonAttachment != null) {
			this.uiButtonAttachment.setOnClickListener(new OnClickAttachmentListener());
		}

		this.uiButtonErase = this.findViewById(mApplication.getAndroidIdByRKey(AndroidApplicationR.component_photo__erase__button));
		if (this.uiButtonErase != null) {
			this.uiButtonErase.setOnClickListener(new OnClickEraseListener());
		}

		this.uiPhotoName = (TextView) this.findViewById(mApplication.getAndroidIdByRKey(AndroidApplicationR.component_photo__name__edit));
		this.uiPhotoDescription = (TextView) this.findViewById(mApplication.getAndroidIdByRKey(AndroidApplicationR.component_photo__description__edit));
		this.uiPhotoPosition = (MMPositionTextView) this.findViewById(mApplication.getAndroidIdByRKey(AndroidApplicationR.component_photo__position__value));
		this.uiPhotoDate = (MMDateTimeTextView) this.findViewById(mApplication.getAndroidIdByRKey(AndroidApplicationR.component_photo__date__value));

		this.uiPhotoView = (ImageView) this.findViewById(mApplication.getAndroidIdByRKey(AndroidApplicationR.component_photo__image_photo__value));

		this.photoMetadata = (AndroidPhotoMetaData) this.getIntentExtraProperty(PHOTOMETADATA_PARAMETER);

		Map<String, Object> oNonConfigurationInstance = (Map<String, Object>) this.getLastCustomNonConfigurationInstance();

		// restore state (ex: after a rotate)
		if (oNonConfigurationInstance != null){
			this.m_sPhotoUri = (Uri) oNonConfigurationInstance.get(TMP_PHOTO_URI_PARAMETER);
		}
		if (oNonConfigurationInstance != null && oNonConfigurationInstance.get(PHOTOMETADATA_PARAMETER) != null ) {
			this.photoMetadata = (AndroidPhotoMetaData) oNonConfigurationInstance.get(PHOTOMETADATA_PARAMETER);
			this.photoConfig = (MMPhotoConfig) oNonConfigurationInstance.get(MMPhotoConfig.PARAMETER_NAME);
			this.updateUi();
		} else {
			this.photoConfig = (MMPhotoConfig) this.getIntentExtraProperty(MMPhotoConfig.PARAMETER_NAME);

			// 3 cases: 
			// - existing local photo
			// - existing remote photo
			// - new photo 

			// Instantiate if null (should be null when adding photo)
			if ( this.photoMetadata == null ) {
				this.photoMetadata = new AndroidPhotoMetaData();
				this.photoMetadata.setId(this.getSequenceIdNextval());
				isEmptyPhoto=true;
				uiButtonErase.setEnabled(false);
				// le champs nom est désactivé tant que les métadonnées d'image
				// ne sont pas remontées
				this.uiPhotoName.setEnabled(false);
				this.uiPhotoDescription.setEnabled(false);
				this.uiPhotoPosition.setEnabled(false);
			}
			else // local uri valued when local photo
				if ( this.photoMetadata.getLocalUri() != null ) {
					this.loadLocalPhoto();
				}
				else // remote uri valued when remote photo
					if ( !TextUtils.isEmpty(this.photoMetadata.getRemoteUri())) {
						this.loadRemotePhoto();
					}
		}

		if ( !this.photoConfig.isRemoveEnabled()) {
			this.uiButtonErase.setVisibility(View.GONE);
		}
		this.updateUi();
	}

	/**
	 * Check external storage state
	 */
	@Override
	protected void onResume() {
		super.onResume();
		this.checkExternalState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onDestroy() {
		this.uiPhotoView.setImageBitmap(null);
		super.onDestroy();
	}

	/**
	 * @param p_sPropertyName name of an extra
	 * @return The value associated to p_sPropertyName
	 */
	protected Object getIntentExtraProperty(String p_sPropertyName) {
		Object r_oObject = null;
		if (this.getIntent().getExtras() != null) {
			r_oObject = this.getIntent().getExtras().get(p_sPropertyName);
		}
		return r_oObject;
	}

	/**
	 * check the external state, toast an error message if not mounted mount the
	 * external if state is not mount, not shared
	 * 
	 * @return true if mounted, false if not
	 */
	protected boolean checkExternalState() {
		final String sState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(sState)) {
			return true;
		}

		ApplicationR oErrorMessage = AndroidApplicationR.component_photo__external_notready__message;
		if (Environment.MEDIA_SHARED.equals(sState)) {
			oErrorMessage = AndroidApplicationR.component_photo__external_shared__message;
		}
		else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(sState)) {
			oErrorMessage = AndroidApplicationR.component_photo__external_readonly__message;
		}
		else if (Environment.MEDIA_UNMOUNTED.equals(sState)) {
			oErrorMessage = AndroidApplicationR.component_photo__external_mounting__message;
			this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
		}

		Toast.makeText(this, AndroidApplication.getInstance().getStringResource(oErrorMessage),
				Toast.LENGTH_LONG).show();

		return false;
	}

	/**
	 * @throws MMediaException thrown by {link {@link MMediaStoreImageService#update(MImage, String[], android.content.ContentResolver)}
	 */
	protected void validPhoto() throws MMediaException {
		Log.d(LOG_TAG, "MMPhotoCommand.validPhoto() ");
		if (this.isEmptyPhoto) {
			this.setResult(Activity.RESULT_CANCELED);
		}
		else {

			// ui values to photoMetadata
			this.photoMetadata.setName( this.uiPhotoName.getText().toString());
			this.photoMetadata.setDescription( this.uiPhotoDescription.getText().toString());
			this.photoMetadata.setDateTaken(System.currentTimeMillis());

			Log.d(LOG_TAG, "  " + this.photoMetadata.toDebug());

			if ( this.photoMetadata.getImage().getLocalUri() != null ) {
				this.mediaStoreImageService.update( this.photoMetadata.getImage(), new String[] {
					MediaStore.Images.Media.TITLE, MediaStore.Images.Media.DESCRIPTION, MediaStore.Images.Media.DATE_MODIFIED,
					MediaStore.Images.Media.IS_PRIVATE }, getContentResolver());
			}

			Intent oPhotoResultat = new Intent();
			oPhotoResultat.putExtra( PHOTOMETADATA_PARAMETER, this.photoMetadata);
			Log.d("LOG_TAG", "  intent: " + oPhotoResultat.toString());
			this.setResult( Activity.RESULT_OK, oPhotoResultat );
		}
		this.finish();
	}

	/**
	 * Clear current photo
	 */
	protected void erasePhoto() {
		this.uiButtonErase.setEnabled(false);
		Log.d(LOG_TAG, "erasePhoto()");
		Log.d(LOG_TAG, this.photoMetadata.toDebug());

		this.photoMetadata.clear();
		Log.d(LOG_TAG, this.photoMetadata.toDebug());

		this.updateUi();
		// le champs nom est désactivé tant que les métadonnées d'image ne sont
		// pas remontées
		this.uiPhotoName.setEnabled(false);
	}

	/**
	 * Update UI components
	 */
	protected void updateUi() {
		this.updateMetadataToComponents();

		this.uiPhotoName.setEnabled(true);
		this.uiPhotoDescription.setEnabled(true);

		this.updateButtonsFromEditMode();
	}

	/**
	 * Update Buttons
	 */
	protected final void updateButtonsFromEditMode() {
		this.updateButtonsFromEditMode(this.photoMetadata.isReadOnly());
	}

	/**
	 * Update Buttons
	 * 
	 * @param p_bReadOnly metadatas are read only.
	 */
	protected void updateButtonsFromEditMode(boolean p_bReadOnly) {
		int iVisibility = View.VISIBLE;
		if (p_bReadOnly) {
			iVisibility = View.GONE;
		}
		this.updateButtonsFromEditMode(iVisibility);
	}

	/**
	 * Update Buttons
	 * 
	 * @param p_iVisibility View.GONE or View.VISIBLE
	 * @see View
	 */
	protected void updateButtonsFromEditMode(int p_iVisibility) {
		this.uiButtonErase.setVisibility(p_iVisibility);
		this.uiOkButton.setVisibility(p_iVisibility);
		this.uiButtonCamera.setVisibility(p_iVisibility);
		this.uiButtonAttachment.setVisibility(p_iVisibility);
	}

	/**
	 * Update components with photo metadata (except photo content)
	 */
	protected void updateMetadataToComponents() {
		this.updatePhoto( this.photoMetadata.getLocalUriAsString());
		this.uiPhotoName.setText( this.photoMetadata.getName());
		this.uiPhotoDescription.setText( this.photoMetadata.getDescription());
		this.uiPhotoPosition.getComponentDelegate().configurationSetValue( this.photoMetadata.getPosition());
		if ( this.photoMetadata.getDateTaken() != 0 ) {
			uiPhotoDate.getComponentDelegate().configurationSetValue( this.photoMetadata.getDateTaken());
		}
	}

	/**
	 * Update the photo using an URI.
	 * @param p_sImageUri URI of the photo
	 */
	protected void updatePhoto(String p_sImageUri) {
		if (p_sImageUri == null || p_sImageUri.length() == 0) {
			this.m_sPhotoUri = null;
			this.uiButtonErase.setEnabled(false);
			this.uiPhotoView.setImageResource(AndroidApplication.getInstance().getAndroidIdByRKey(AndroidApplicationR.component__photoview__nophoto));
		}
		else {
			this.m_sPhotoUri = Uri.parse(p_sImageUri);
			this.uiPhotoView.setTag(p_sImageUri);
			this.uiPhotoView.setImageBitmap(null);
			this.uiButtonErase.setEnabled(true);
			if (p_sImageUri.startsWith(URI_CONTENT)) {
				MMediaStoreImageService oMMediaStoreImageService = BeanLoader.getInstance().getBean(MMediaStoreImageService.class);
				Bitmap oImage = oMMediaStoreImageService.loadBitmap(this.m_sPhotoUri, MAX_WIDTH_OF_BITMAP, this.getContentResolver());
				this.uiPhotoView.setImageBitmap(oImage);
			}
		}
	}

	/**
	 * launch a native activity to choose an existing photo
	 */
	protected void choosePhoto() {
		Intent oIntent = null;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			oIntent = new Intent();
			oIntent.setType(MIME_IMAGE);
			oIntent.setAction(Intent.ACTION_GET_CONTENT);

			this.startActivityForResult(Intent.createChooser(oIntent, AndroidApplication.getInstance()
					.getStringResource(AndroidApplicationR
					.component_photo__choose_photo__title)), SELECT_PICTURE_ACTIVITY);
		} else {
			oIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			oIntent.addCategory(Intent.CATEGORY_OPENABLE);
			oIntent.setType("image/*");
			startActivityForResult(oIntent, SELECT_KITKAT_PICTURE_ACTIVITY);
		}
	}

	/**
	 * launch the native activity to take a photo
	 */
	protected void takePhoto() {
		Log.d(LOG_TAG, "takePhoto");
		// clear current image to preserve memory
		// this.oUiPhoto.clearBitmap();

		if (this.checkExternalState()) {
			ContentValues oValues = new ContentValues();
			// le nom de l'image n'est pas défini
			// values.put(MediaStore.Images.Media.TITLE, "testlmi.jpg");
			oValues.put(MediaStore.Images.Media.DESCRIPTION, AndroidApplication.getInstance().getStringResource(AndroidApplicationR.component_photo__default_description__value));
			oValues.put(MediaStore.Images.Media.MIME_TYPE, MIME_IMAGE_JPEG);

			this.m_sPhotoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, oValues);

			// Log.d(LOG_TAG, "takePhoto - photo uri : " + oPhotoUri);
			Intent oIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			oIntent.putExtra(MediaStore.EXTRA_OUTPUT, this.m_sPhotoUri);
			this.startActivityForResult(oIntent, TAKE_PHOTO_ACTIVITY);
		}
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int p_iRequestCode, int p_iResultCode,Intent p_oIntent) {
		if (p_iResultCode == Activity.RESULT_OK) {
			uiButtonErase.setEnabled(true);
			if (p_iRequestCode == TAKE_PHOTO_ACTIVITY) {
				Log.d(LOG_TAG, "Photo taken: " + this.m_sPhotoUri.toString());
				this.photoMetadata.setPhotoAsShooted();
				this.photoMetadata.setLocalUri(this.m_sPhotoUri);
				this.loadLocalPhoto();
			}
			else if (p_iRequestCode == SELECT_PICTURE_ACTIVITY || p_iRequestCode == SELECT_KITKAT_PICTURE_ACTIVITY) {
				Log.d(LOG_TAG, "Photo selected: " + p_oIntent.getData().toString());
				Uri oOriginalUri = p_oIntent.getData();
				// TODO change to use DocumentAPI
				// ce correctif est un paliatif au probleme de l'API Document de KITKAT
				// dans android KITKAT on converti l'URI document en URI local
				if (p_iRequestCode == SELECT_KITKAT_PICTURE_ACTIVITY) {
					
					String sWholeID = DocumentsContract.getDocumentId(oOriginalUri);
					Log.d(LOG_TAG, "sWholeID :"+sWholeID);
					String sId = sWholeID.split(":")[1];
//					String[] column = { MediaStore.Images.Media.DATA };
//					Log.d(LOG_TAG, "sId :"+sId);
//					// where id is equal to
//					String sel = MediaStore.Images.Media._ID + "=?";
//					Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{ sId }, null);
//					String filePath = "";
//					int columnIndex = cursor.getColumnIndex(column[0]);
//					if (cursor.moveToFirst()) {
//						filePath = cursor.getString(columnIndex);
//					}
//					cursor.close();
//					Log.d(LOG_TAG, "filePath :"+filePath);
//					oOriginalUri = Uri.fromFile(new File(filePath));
					
					oOriginalUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, sId);
				}
				this.photoMetadata.setPhotoAsSelected();
				this.photoMetadata.setLocalUri(oOriginalUri);
				this.loadLocalPhoto();
			}
			isEmptyPhoto=false;
		}
		//		else {
		//			if (photoMetadata.getPhotoUri() != null) {
		//				this.uiPhotoView.updatePhoto(photoMetadata.getPhotoUri());
		//			}
		//		}
	}
	/**
	 * init the curentViewModel from metadata of the image
	 * @param selectedImageUri
	 */
	protected void loadLocalPhoto() {
		try {
			Log.i(LOG_TAG, "loadLocalPhoto() : " + this.photoMetadata.toDebug());

			MImage oImage = this.mediaStoreImageService.load( this.photoMetadata.getLocalUri(), this.getContentResolver());

			if ( oImage != null) {

				this.photoMetadata.setPosition( oImage.getLatitude(), oImage.getLongitude());
				long lDate ; 
				if ( oImage.getDateTaken() > 0) {
					lDate = oImage.getDateTaken() ;
				}else if(oImage.getDateModified() > 0){
					lDate = oImage.getDateModified() ;
				} else {
					lDate = System.currentTimeMillis() ;
				}				
				this.photoMetadata.setDateTaken( lDate );
				this.photoMetadata.setPrivate( oImage.isPriv());	
				this.updateUi();

			} else {
				Toast.makeText(getApplicationContext(),
						AndroidApplication.getInstance().getStringResource(AndroidApplicationR.component_photo__load__notfound),
						Toast.LENGTH_LONG).show();
				Log.i(LOG_TAG, "photo not found: " + this.photoMetadata.getLocalUri());
			}
		} catch( MMediaException oException ) {
			throw new MobileFwkException(oException);
		}
	}

	/**
	 * Load a remote photo
	 */
	public void loadRemotePhoto() {
		//TODO affichage d'une image d'attente ?
		this.updateMetadataToComponents();

		ImageDownloadParameterIn oParam = new ImageDownloadParameterIn();
		oParam.setId( photoMetadata.getRemoteUri());
		oParam.setMaxWidth (this.photoConfig.getDownloadImageMaxWidth());
		this.launchAction(ImageDownloadCommand.class, oParam, this);

		//		RelativeLayout.LayoutParams oCancelLayoutParams = (RelativeLayout.LayoutParams) this.uiCancelButton.getLayoutParams();
		//		oCancelLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		updateButtonsFromEditMode();
	}

	/**
	 * Ecouteur de la fin de l'action de téléchargement de l'image
	 * @param p_oBitmap résulat de l'action de téléchargement
	 */
	@ListenerOnActionSuccess(action=ImageDownloadCommand.class)
	public void doAfterDownloadImage( ListenerOnActionSuccessEvent<BitmapParameterOUT> p_oBitmap) {
		Log.d(LOG_TAG, "MMPhotoCommand.doAfterDownloadImage - " + this.photoMetadata.getRemoteUri());
		if ( p_oBitmap.getSource() == this ) {
			if ( p_oBitmap.getActionResult() == null ) {
				Log.d(LOG_TAG, "MMPhotoCommand.doAfterDownloadImage - action result is null");
				this.uiPhotoView.setImageResource(AndroidApplication.getInstance().getAndroidIdByRKey(AndroidApplicationR.component__photoview__nophoto));
			} else {

				try {
					Log.d(LOG_TAG, "MMPhotoCommand.doAfterDownloadImage - insert downloaded photo");

					this.mediaStoreImageService.insert(
							this.photoMetadata.getImage(), p_oBitmap.getActionResult().getBitmapImage(), this.getContentResolver());
					Log.d(LOG_TAG, "MMPhotoCommand.doAfterDownloadImage - photo local uri: " + this.photoMetadata.getLocalUri());

					this.uiPhotoView.setImageURI(this.photoMetadata.getLocalUri()) ;
					this.updatePhoto(this.photoMetadata.getLocalUri().toString());
					this.photoMetadata.setPhotoAsDownloaded();

					updateButtonsFromEditMode();
					RelativeLayout.LayoutParams oCancelLayoutParams = (RelativeLayout.LayoutParams) this.uiCancelButton.getLayoutParams();
					oCancelLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
					oCancelLayoutParams.addRule(RelativeLayout.LEFT_OF, this.uiOkButton.getId());

					AndroidApplication.getInstance().getController().publishBusinessEvent(null,
							new PhotoDownloadedEvent(this, this.photoMetadata));

				} catch (MMediaException oMMediaException) {
					Log.e(LOG_TAG, "Failed to save downloaded image", oMMediaException);
				}
			}
		}
	}


	/**
	 * Ecouteur de la fin de l'action de téléchargement d'image lorsqu'il y a un pb
	 * @param p_oBitmap résulat de l'action de téléchargement
	 */
	@ListenerOnActionFail(action=ImageDownloadCommand.class)
	public void doAfterDownloadImageFail( ListenerOnActionFailEvent<BitmapParameterOUT> p_oBitmap) {
		Log.d(LOG_TAG, "MMPhotoCommand.doAfterDownloadImageFail - " + this.photoMetadata.getRemoteUri());
		Toast.makeText(getApplicationContext(),
				AndroidApplication.getInstance().getStringResource(AndroidApplicationR.component_photo__load__downloadfailed),
				Toast.LENGTH_LONG).show();
		Log.i(LOG_TAG, "Remote photo not downloaded: " + this.photoMetadata.getLocalUri());
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity#onRetainCustomNonConfigurationInstance()
	 */
	@Override
	public Map<String, Object> onRetainCustomNonConfigurationInstance() {
		Map<String, Object> r_oMap = (Map<String, Object>) super.onRetainCustomNonConfigurationInstance();
		r_oMap.put(PHOTOMETADATA_PARAMETER, this.photoMetadata);
		r_oMap.put(MMPhotoConfig.PARAMETER_NAME, this.photoConfig);
		r_oMap.put(TMP_PHOTO_URI_PARAMETER, this.m_sPhotoUri);
		return r_oMap;
	}

	/**
	 * Return next photo id
	 * @return next photo id
	 */
	private String getSequenceIdNextval() {
		SharedPreferences oPref = this.getSharedPreferences("photos", Activity.MODE_PRIVATE);
		long lId = oPref.getLong("photoIdSequence",0)-1;
		SharedPreferences.Editor oEditor = oPref.edit();
		oEditor.putLong("photoIdSequence", lId);
		oEditor.commit();
		return Long.toString(lId);
	}

	/**
	 * Return metadata
	 * @return metadata
	 */
	protected final AndroidPhotoMetaData getPhotoMetadata() {
		return this.photoMetadata;
	}

	/**
	 * Return true if there is a photo, false otherwise
	 * @return true if there is a photo, false otherwise
	 */
	protected final boolean hasPhoto() {
		return !this.isEmptyPhoto;
	}

	/**
	 * Get button "ok"
	 * @return button "ok" or null it doesn't exist.
	 */
	protected final View getOkButton() {
		return this.uiOkButton;
	}

	/**
	 * Get button "cancel"
	 * @return button "cancel" or null it doesn't exist.
	 */
	protected final View getCancelButton() {
		return this.uiCancelButton;
	}

	/**
	 * Get button "delete"
	 * @return button "delete" or null it doesn't exist.
	 */
	protected final View getEraseButton() {
		return uiButtonErase;
	}

	/**
	 * Get button "camera"
	 * @return button "camera" or null it doesn't exist.
	 */
	protected final View getCameraButton() {
		return uiButtonCamera;
	}

	/**
	 * Get button "attachment"
	 * @return button "attachment" or null it doesn't exist.
	 */
	protected final View getAttachmentButton() {
		return uiButtonAttachment;
	}

	/**
	 * Get view used for photo name
	 * @return view "name" or null it doesn't exist.
	 */
	protected final TextView getNameView() {
		return uiPhotoName;
	}

	/**
	 * Get view used for photo description
	 * @return view "description" or null it doesn't exist.
	 */
	protected final TextView getDescriptionView() {
		return uiPhotoDescription;
	}

	/**
	 * Get view used for photo date
	 * @return view "date" or null it doesn't exist.
	 */
	protected final MMDateTimeTextView getDateView() {
		return uiPhotoDate;
	}

	/**
	 * Get view used for photo position
	 * @return view "position" or null it doesn't exist.
	 */
	protected final MMPositionTextView getPositionView() {
		return uiPhotoPosition;
	}

	/**
	 * Get view used for photo
	 * @return view "photo" or null it doesn't exist.
	 */
	protected final ImageView getPhotoView() {
		return uiPhotoView;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSaveInstanceState(Bundle p_oSavedInstanceState) {
		super.onSaveInstanceState(p_oSavedInstanceState);
		if(this.m_sPhotoUri == null) {
			p_oSavedInstanceState.putString(PHOTO_COMMAND_URI_KEY, null);
		}
		else {
			p_oSavedInstanceState.putString(PHOTO_COMMAND_URI_KEY, this.m_sPhotoUri.toString());
		}
		p_oSavedInstanceState.putBoolean(PHOTO_COMMAND_ERASE_ENABLED_KEY, this.uiButtonErase.isEnabled());
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	protected void onRestoreInstanceState(Bundle p_oSavedInstanceState) {
		super.onRestoreInstanceState(p_oSavedInstanceState);
		this.updatePhoto(p_oSavedInstanceState.getString(PHOTO_COMMAND_URI_KEY));
		this.uiButtonErase.setEnabled(p_oSavedInstanceState.getBoolean(PHOTO_COMMAND_ERASE_ENABLED_KEY));
	}

	/**
	 * Launched after download
	 */
	public static class PhotoDownloadedEvent extends AbstractBusinessEvent<PhotoMetaData> {
		/**
		 * SelectedItemEvent
		 * @param p_oSource source
		 * @param p_oPhotoMetaData metadata
		 */
		public PhotoDownloadedEvent(Object p_oSource, PhotoMetaData p_oPhotoMetaData) {
			super(p_oSource, p_oPhotoMetaData);
		}
	}

	/**
	 * Listener called by "ok" button 
	 *
	 */
	private class OnClickOkListener implements View.OnClickListener {
		/**
		 * Call method {@link PhotoCommand#validPhoto()}
		 * @param p_oView The clicked view
		 * @thows MMediaException thrown by {@link PhotoCommand#validPhoto()}
		 */
		@Override
		public void onClick(View p_oView) {
			try {
				PhotoCommand.this.validPhoto();
			}
			catch (MMediaException e) {
				throw new MobileFwkException(e);
			}
		}
	}

	/**
	 * Listener called by "cancel" button 
	 */
	private class OnClickCancelListener implements View.OnClickListener {
		/**
		 * Close the activity
		 * @param p_oView The clicked view
		 */
		@Override
		public void onClick(View p_oView) {
			PhotoCommand.this.setResult(Activity.RESULT_CANCELED);
			PhotoCommand.this.finish();
		}
	}

	/**
	 * Listener called by "camera" button 
	 */
	private class OnClickCameraListener implements View.OnClickListener {
		/**
		 * Call method {@link PhotoCommand#takePhoto()}
		 * @param p_oView The clicked view
		 */
		@Override
		public void onClick(View p_oView) {
			PhotoCommand.this.takePhoto();
		}
	}

	/**
	 * Listener called by "attachment" button 
	 */
	private class OnClickAttachmentListener implements View.OnClickListener {
		/**
		 * Call method {@link PhotoCommand#choosePhoto()}
		 * @param p_oView The clicked view
		 */
		@Override
		public void onClick(View p_oView) {
			PhotoCommand.this.choosePhoto();
			
		}
	}

	/**
	 * Listener called by "erase" button 
	 */
	private class OnClickEraseListener implements View.OnClickListener {
		/**
		 * Call method {@link PhotoCommand#erasePhoto()}
		 * @param p_oView The clicked view
		 */
		@Override
		public void onClick(View p_oView) {
			PhotoCommand.this.erasePhoto();
		}
	}
}
