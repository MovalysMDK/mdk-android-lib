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
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.mediastore.MMediaException;
import com.adeuza.movalysfwk.mobile.mf4android.mediastore.images.MImage;
import com.adeuza.movalysfwk.mobile.mf4android.mediastore.images.MMediaStoreImageService;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AndroidPhotoMetaData;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMDateTimeTextView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPositionTextView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMSlidingDrawer;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.AbstractBusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionFail;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionFailEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionSuccess;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.actiontask.listener.ListenerOnActionSuccessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
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
 * @author dmaurange
 * 
 */
public class MMPhotoCommand extends AbstractMMActivity implements OnClickListener {

	/**
	 * 
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
	 * 
	 */
	public static final String PHOTOMETADATA_PARAMETER = "photoMetadataParam";
	/**
	 * 
	 */
	public static final String TMP_PHOTO_URI_PARAMETER = "tmpPhotoUri";

	/**
	 * Return code for take photo activity
	 */
	private static final int TAKE_PHOTO_ACTIVITY = Math.abs("TAKE_PHOTO_ACTIVITY".hashCode()) & NumericConstants.HEXADECIMAL_MASK;

	/**
	 * Return code for select picture activity
	 */
	private static final int SELECT_PICTURE_ACTIVITY = Math.abs("SELECT_PICTURE_ACTIVITY".hashCode()) & NumericConstants.HEXADECIMAL_MASK;

	/**
	 * Return code for select picture activity on KITKAT and up version
	 */
	private static final int SELECT_KITKAT_PICTURE_ACTIVITY = Math.abs("SELECT_KITKAT_PICTURE_ACTIVITY".hashCode()) & NumericConstants.HEXADECIMAL_MASK;
	
	/**
	 * Application
	 */
	private AndroidApplication application ;

	/**
	 * Mediastore image service
	 */
	private MMediaStoreImageService mediaStoreImageService;

	/**
	 * Photo metadata
	 */
	private AndroidPhotoMetaData photoMetadata ;


	/** 
	 * Taille max de l'image créé 
	 */
	private static final int MAX_WIDTH_OF_BITMAP = 640 ;

	/* UI Components */
	private ImageButton uiOkButton;
	private ImageButton uiCancelButton;
	private EditText uiPhotoName;
	private EditText uiPhotoDescription;
	private MMDateTimeTextView uiPhotoDate;
	private MMPositionTextView uiPhotoPosition;
	private MMSlidingDrawer uiPhotoMetaSlidingDrawer;

	/** the photo field on the panel */
	private ImageView uiPhotoView;
	private ImageButton uiButtonCamera;
	private ImageButton uiButtonAttachment;
	private ImageButton uiButtonErase;

	/** used to know if we create a new photo or edit an old one **/
	private boolean isEmptyPhoto=false;
	/**
	 * Temporary uri of the new photo (before launching the camera activity). Will become the new uri of photoMetadata
	 * when photo will be validated in the camera activity.
	 */
	private transient Uri m_sPhotoUri ;

	/**
	 * The photo config
	 */
	private MMPhotoConfig photoConfig ;

	/**
	 * The key use to retain the uri of the current photo
	 */
	private static final String PHOTO_COMMAND_URI_KEY = "photoCommandUriKey";

	/**
	 * The key used to retain the current state of the erase button.
	 */
	private static final String PHOTO_COMMAND_ERASE_ENABLED_KEY = "photoCommandEraseEnabledKey";


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle p_oSavedInstanceState) {
		super.onCreate(p_oSavedInstanceState);

		this.application = (AndroidApplication) Application.getInstance();
		this.mediaStoreImageService = BeanLoader.getInstance().getBean(MMediaStoreImageService.class);

		setContentView(this.application.getAndroidIdByRKey(AndroidApplicationR.fwk_component_photo_photocommand_layout));
		this.uiOkButton = (ImageButton) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_photo__button_ok__button));
		this.uiCancelButton = (ImageButton) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_photo__button_cancel__button));
		this.uiPhotoName = (EditText) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_photo__name__edit));
		this.uiPhotoDescription = (EditText) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_photo__description__edit));
		this.uiPhotoPosition = (MMPositionTextView) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_photo__position__value));
		this.uiPhotoDate = (MMDateTimeTextView) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_photo__date__value));
		this.uiOkButton.setOnClickListener(this);
		this.uiCancelButton.setOnClickListener(this);
		this.uiPhotoView = (ImageView) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_photo__image_photo__value));
		this.uiButtonCamera = (ImageButton) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_photo__camera__button));
		this.uiButtonAttachment = (ImageButton) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_photo__attachment__button));
		this.uiButtonErase = (ImageButton) this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_photo__erase__button));
		this.uiPhotoMetaSlidingDrawer = (MMSlidingDrawer)this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.component_photo__SlidingDrawer__group));

		this.uiButtonCamera.setOnClickListener(this);
		this.uiButtonAttachment.setOnClickListener(this);
		this.uiButtonErase.setOnClickListener(this);

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

	@Override
	protected void onResume() {
		super.onResume();
		checkExternalState();
	}


	/**
	 * check the external state, toast an error message if not mounted mount the
	 * external if state is not mount, not shared
	 * 
	 * @return true if mounted, false if not
	 */
	private boolean checkExternalState() {
		boolean r_bResult = true ;
		if (Environment.MEDIA_SHARED.equals(Environment.getExternalStorageState())) {
			Toast.makeText(getApplicationContext(),Application.getInstance()
					.getStringResource(AndroidApplicationR.component_photo__external_shared__message),
					Toast.LENGTH_LONG).show();
			r_bResult =  false;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())) {
			Toast.makeText(getApplicationContext(),Application.getInstance()
					.getStringResource(AndroidApplicationR.component_photo__external_readonly__message),
					Toast.LENGTH_LONG).show();
			r_bResult =  false;
		}

		else if (Environment.MEDIA_UNMOUNTED.equals(Environment.getExternalStorageState())) {
			Log.e(LOG_TAG, StringUtils.concat("external storage need to be mounted, state is : ",
					Environment.getExternalStorageState()));
			Toast.makeText(getApplicationContext(),Application.getInstance()
					.getStringResource(AndroidApplicationR.component_photo__external_mounting__message),
					Toast.LENGTH_LONG).show();
			this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
			r_bResult =  false;
		} else if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			Toast.makeText(getApplicationContext(),Application.getInstance()
					.getStringResource(AndroidApplicationR.component_photo__external_notready__message),
					Toast.LENGTH_LONG).show();
			r_bResult =  false;
		} 
		return r_bResult ;
	}
	/**
	 * @param p_sPropertyName
	 * @return
	 */
	private Object getIntentExtraProperty(String p_sPropertyName) {
		Object r_oObject = null;
		if (this.getIntent().getExtras() != null) {
			r_oObject = this.getIntent().getExtras().get(p_sPropertyName);
		}
		return r_oObject;
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
	 * {@inheritDoc}
	 */
	@Override
	public void onClick(View p_oParamView) {
		try {
			if (p_oParamView.getId() == this.application
					.getAndroidIdByRKey(AndroidApplicationR.component_photo__camera__button)) {
				// prise de photo
				this.takePhoto();
			} else if (p_oParamView.getId() == this.application
					.getAndroidIdByRKey(AndroidApplicationR.component_photo__attachment__button)) {
				// choix d'une image existante
				this.choosePhoto();
			} else if (p_oParamView.getId() == this.application
					.getAndroidIdByRKey(AndroidApplicationR.component_photo__erase__button)) {
				// poubelle : on vide tout
				this.erasePhoto();
			} else if (p_oParamView.getId() == this.application
					.getAndroidIdByRKey(AndroidApplicationR.component_photo__button_ok__button)) {
				// validation de la photo
				this.validPhoto();
			} else if (p_oParamView.getId() == this.application
					.getAndroidIdByRKey(AndroidApplicationR.component_photo__button_cancel__button)) {
				// Annulation
				this.setResult(Activity.RESULT_CANCELED);
				this.finish();
			}
		} catch (MMediaException oException) {
			throw new MobileFwkException(oException);
		}
	}
	/**
	 * @throws MMediaException
	 */
	private void validPhoto() throws MMediaException {

		Log.d(LOG_TAG, "MMPhotoCommand.validPhoto() ");
		if (!isEmptyPhoto){

			// ui values to photoMetadata
			this.photoMetadata.setName( this.uiPhotoName.getText().toString());
			this.photoMetadata.setDescription( this.uiPhotoDescription.getText().toString());
			this.photoMetadata.setDateTaken(System.currentTimeMillis());

			Log.d(LOG_TAG, "  " + this.photoMetadata.toDebug());

			if ( this.photoMetadata.getImage().getLocalUri() != null ) {
				this.mediaStoreImageService.update( this.photoMetadata.getImage(), new String[] {
					Media.TITLE, Media.DESCRIPTION, Media.DATE_MODIFIED, Media.IS_PRIVATE }, getContentResolver());
			}

			Intent oPhotoResultat = new Intent();
			oPhotoResultat.putExtra( PHOTOMETADATA_PARAMETER, this.photoMetadata);
			Log.d("LOG_TAG", "  intent: " + oPhotoResultat.toString());
			this.setResult( Activity.RESULT_OK, oPhotoResultat );
		}
		else{
			this.setResult(Activity.RESULT_CANCELED);
		}
		this.finish();
	}

	/**
	 * Clear current photo
	 */
	private void erasePhoto() {

		uiButtonErase.setEnabled(false);
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
	private void updateUi() {
		this.updateMetadataToComponents();

		this.uiPhotoName.setEnabled(true);
		this.uiPhotoDescription.setEnabled(true);
		this.uiPhotoMetaSlidingDrawer.setEdit(!this.photoMetadata.isReadOnly());

		updateButtonsFromEditMode();
	}

	/**
	 * Update Buttons
	 */
	private void updateButtonsFromEditMode() {
		if ( this.photoMetadata.isReadOnly() ) 
		{
			this.uiButtonErase.setVisibility(View.GONE);
			this.uiOkButton.setVisibility(View.GONE);
			this.uiButtonCamera.setVisibility(View.GONE);
			this.uiButtonAttachment.setVisibility(View.GONE);
		}
		else {
			this.uiButtonErase.setVisibility(View.VISIBLE);
			this.uiOkButton.setVisibility(View.VISIBLE);
			this.uiButtonCamera.setVisibility(View.VISIBLE);
			this.uiButtonAttachment.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Update components with photo metadata (except photo content)
	 */
	private void updateMetadataToComponents() {
		this.updatePhoto( this.photoMetadata.getLocalUriAsString());
		this.uiPhotoName.setText( this.photoMetadata.getName());
		this.uiPhotoDescription.setText( this.photoMetadata.getDescription());
		this.uiPhotoPosition.configurationSetValue( this.photoMetadata.getPosition());
		if ( this.photoMetadata.getDateTaken() != 0 ) {
			uiPhotoDate.configurationSetValue( this.photoMetadata.getDateTaken());
		}
	}

	private void updatePhoto(String p_sImageUri) {

		AndroidApplication oApplication = (AndroidApplication)Application.getInstance();
		if (p_sImageUri == null || "".equals(p_sImageUri) ){
			this.m_sPhotoUri = null;
			this.uiButtonErase.setEnabled(false);
			this.uiPhotoView.setImageResource(oApplication.getAndroidIdByRKey(AndroidApplicationR.component__photoview__nophoto));
		}
		else{
			this.m_sPhotoUri = Uri.parse(p_sImageUri);
			this.uiPhotoView.setTag(p_sImageUri);
			this.uiPhotoView.setImageBitmap(null);
			this.uiButtonErase.setEnabled(true);
			if (p_sImageUri.startsWith("content:")) {
				Uri imageUri = Uri.parse(p_sImageUri);
				MMediaStoreImageService oMMediaStoreImageService = BeanLoader.getInstance().getBean(MMediaStoreImageService.class);
				Bitmap oImage = oMMediaStoreImageService.loadBitmap(imageUri, MAX_WIDTH_OF_BITMAP, this.getContentResolver());
				this.uiPhotoView.setImageBitmap(oImage);
			}
		}
	}

	/**
	 * launch a native activity to choose an existing photo
	 */
	private void choosePhoto() {
		Intent oIntent = null;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			oIntent = new Intent();
			oIntent.setType(MIME_IMAGE);
			oIntent.setAction(Intent.ACTION_GET_CONTENT);
			
			this.startActivityForResult(Intent.createChooser(oIntent, this.application.getStringResource(AndroidApplicationR
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
	private void takePhoto() {
		Log.d(LOG_TAG, "takePhoto");
		// clear current image to preserve memory
		// this.oUiPhoto.clearBitmap();

		if (checkExternalState()) {
			ContentValues oValues = new ContentValues();
			// le nom de l'image n'est pas défini
			// values.put(MediaStore.Images.Media.TITLE, "testlmi.jpg");
			oValues.put(MediaStore.Images.Media.DESCRIPTION, application.getStringResource(AndroidApplicationR.component_photo__default_description__value));
			oValues.put(MediaStore.Images.Media.MIME_TYPE, MIME_IMAGE_JPEG);

			this.m_sPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, oValues);

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
	private void loadLocalPhoto() {

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
						Application.getInstance().getStringResource(AndroidApplicationR.component_photo__load__notfound),
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
				this.uiPhotoView.setImageResource( this.application.getAndroidIdByRKey(AndroidApplicationR.component__photoview__nophoto));
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

					Application.getInstance().getController().publishBusinessEvent(null,
							new PhotoDownloadedEvent(this, this.photoMetadata));

				} catch (MMediaException oMMediaException) {
					Log.e(LOG_TAG, "Failed to save downloaded image", oMMediaException);
				}
			}
		}
	}


	/**
	 * Ecouteur de la fin de l'action de téléchargement d'image lorsqu'il y a un pb
	 *  
	 */
	@ListenerOnActionFail(action=ImageDownloadCommand.class)
	public void doAfterDownloadImageFail( ListenerOnActionFailEvent<BitmapParameterOUT> p_oBitmap) {
		Log.d(LOG_TAG, "MMPhotoCommand.doAfterDownloadImageFail - " + this.photoMetadata.getRemoteUri());
		Toast.makeText(getApplicationContext(),
				Application.getInstance().getStringResource(AndroidApplicationR.component_photo__load__downloadfailed),
				Toast.LENGTH_LONG).show();
		Log.i(LOG_TAG, "Remote photo not downloaded: " + this.photoMetadata.getLocalUri());
	}

	/**
	 * (non-Javadoc)
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
	 * {@inheritDoc}
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if ( this.uiPhotoMetaSlidingDrawer!=null && this.uiPhotoMetaSlidingDrawer.isOpened()){
			this.uiPhotoMetaSlidingDrawer.animateClose();
		}
		else{
			super.onBackPressed();
		}
	}

	/**
	 * @return
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
	 * @author lmichenaud
	 *
	 */
	public static class PhotoDownloadedEvent extends AbstractBusinessEvent<PhotoMetaData> {

		/**
		 * SelectedItemEvent
		 * @param p_oSource source
		 * @param p_oData data
		 */
		public PhotoDownloadedEvent(Object p_oSource, PhotoMetaData p_oPhotoMetaData) {
			super(p_oSource, p_oPhotoMetaData);
		}
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
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

	@Override
	/**
	 * {@inheritDoc}
	 */	
	protected void onRestoreInstanceState(Bundle p_oSavedInstanceState) {
		super.onRestoreInstanceState(p_oSavedInstanceState);
		this.updatePhoto(p_oSavedInstanceState.getString(PHOTO_COMMAND_URI_KEY));
		this.uiButtonErase.setEnabled(p_oSavedInstanceState.getBoolean(PHOTO_COMMAND_ERASE_ENABLED_KEY));
	}
}
