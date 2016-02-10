package com.adeuza.movalysfwk.mobile.mf4android.mediastore.images;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.context.AndroidContextImpl;
import com.adeuza.movalysfwk.mobile.mf4android.mediastore.MMediaException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * @author lmichenaud
 * 
 */
public class MMediaStoreImageServiceImpl implements MMediaStoreImageService {

	/**
	 * 
	 */
	private static final int DEFAULT_QUALITY = 90 ;
	
	private static final int ORIENTATION_90 = 90 ;
	private static final int ORIENTATION_180 = 180 ;
	private static final int ORIENTATION_270 = -90 ;
	
	/**
	 * Default retrieved columns
	 */
	private static final String[] DEFAULT_COLUMNS = {
			MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME,
			MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.SIZE,
			MediaStore.Images.Media.TITLE,
			MediaStore.Images.Media.DATE_MODIFIED,
			MediaStore.Images.Media.DATE_TAKEN,
			MediaStore.Images.Media.DESCRIPTION,
			MediaStore.Images.Media.IS_PRIVATE,
			MediaStore.Images.Media.LATITUDE,
			MediaStore.Images.Media.LONGITUDE,
			MediaStore.Images.Media.ORIENTATION };

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insert(MImage p_oImage, Bitmap p_oBitmap,
			ContentResolver p_oResolver) throws MMediaException {

		ContentValues oValues = new ContentValues();
		oValues.put(Media.DISPLAY_NAME, p_oImage.getDisplayName());
		oValues.put(Media.MIME_TYPE, p_oImage.getContentType());
		oValues.put(Media.TITLE, p_oImage.getTitle());
		oValues.put(Media.DATE_TAKEN, p_oImage.getDateTaken());
		oValues.put(Media.DESCRIPTION, p_oImage.getDescription());
		oValues.put(Media.IS_PRIVATE, p_oImage.isPriv());
		oValues.put(Media.LATITUDE, p_oImage.getLatitude());
		oValues.put(Media.LONGITUDE, p_oImage.getLongitude());

		Uri oUri = p_oResolver.insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, oValues);
		Log.d(Application.LOG_TAG, "MMediaStoreImageServiceImp.insert() " + oUri.toString());
		
		p_oImage.setLocalUri(oUri);

		try {
			OutputStream oOutstream = p_oResolver.openOutputStream(oUri);
			Log.d(Application.LOG_TAG, "outstream: " + oOutstream);
			try {
				p_oBitmap.compress(Bitmap.CompressFormat.JPEG, DEFAULT_QUALITY, oOutstream);
			} finally {
				oOutstream.close();
			}
		} catch (FileNotFoundException oException) {
			throw new MMediaException(oException);
		} catch (IOException oException) {
			throw new MMediaException(oException);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(MImage p_oImage, ContentResolver p_oResolver)
			throws MMediaException {
		this.update(p_oImage, DEFAULT_COLUMNS, p_oResolver);
	}

	/**
	 * @param p_oImage
	 * @param p_sColumns
	 * @param p_oResolver
	 * @throws MMediaException
	 */
	@Override
	public void update(MImage p_oImage, String[] p_sColumns,
			ContentResolver p_oResolver) throws MMediaException {
		ContentValues oContentValues = bindToContentValues(p_oImage, p_sColumns);
		p_oResolver.update(p_oImage.getLocalUri(), oContentValues, null, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insert(MImage p_oImage, Bitmap p_oBitmap, MContext p_oContext)
			throws MMediaException {
		this.insert(p_oImage, p_oBitmap, getContentResolver(p_oContext));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Bitmap loadBitmap(Uri p_oUri, int p_iMaxWidth,
			ContentResolver p_oContentResolver) {
		String sPath = this.getPath(p_oUri, p_oContentResolver);
		if (sPath != null) {
			try {
				final ExifInterface oExif = new ExifInterface(sPath);

				int iOrientation = oExif.getAttributeInt(
						ExifInterface.TAG_ORIENTATION,
						ExifInterface.ORIENTATION_NORMAL);
				int iRotate = 0;
				switch (iOrientation) {
					case ExifInterface.ORIENTATION_ROTATE_270:
						iRotate = ORIENTATION_270;
						break ;
					case ExifInterface.ORIENTATION_ROTATE_180:
						iRotate = ORIENTATION_180;
						break ;
					case ExifInterface.ORIENTATION_ROTATE_90:
						iRotate = ORIENTATION_90;
						break ;
					default:
						iRotate = 0;						
				}

				int iSampleSize = this.computeSampleSize(sPath, p_iMaxWidth);
				Bitmap oResizedBitmap = this.resizeBitmap(sPath, iSampleSize);
				if (iRotate != 0) {
					oResizedBitmap = this.rotate(oResizedBitmap, iRotate);
				}
				return oResizedBitmap;
			} catch (IOException e) {
				Log.e(Application.LOG_TAG, "Can't load Photo Exif metadata " + p_oUri,e);
			}
		}
		return null;
	}

	/**
	 * @param p_oUri
	 * @param p_oContext
	 * @return
	 */
	@Override
	public MImage load(Uri p_oUri, MContext p_oContext) throws MMediaException {
		return load(p_oUri, getContentResolver(p_oContext));
	}

	/**
	 * @param p_oUri
	 * @param p_oContentResolver
	 * @return
	 */
	@Override
	public MImage load(Uri p_oUri, ContentResolver p_oContentResolver)
			throws MMediaException {
		MImage r_oImage = null;

		try {
			Cursor oCursor = MediaStore.Images.Media.query(p_oContentResolver, p_oUri, DEFAULT_COLUMNS);
			if (oCursor == null) {
				oCursor = p_oContentResolver.query(p_oUri, DEFAULT_COLUMNS,null, null, null);
			}
			if (oCursor != null) {
				try {
					if (oCursor.moveToFirst()) {
						r_oImage = new MImage();
						r_oImage.setLocalUri(p_oUri);
						this.bindResult(r_oImage, oCursor);
					}
				} finally {
					oCursor.close();
				}
			}

			this.loadExifData(this.getPath(p_oUri, p_oContentResolver),	r_oImage);
		} catch (IOException oException) {
			throw new MMediaException(oException);
		} catch (ParseException oException) {
			throw new MMediaException(oException);
		}

		return r_oImage;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MImage> search(Uri p_oBaseUri, String p_sCriteria, String[] p_sCriteriaValues, ContentResolver p_oResolver) {
		return search(p_oBaseUri, p_sCriteria, p_sCriteriaValues,DEFAULT_COLUMNS, p_oResolver);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MImage> search(Uri p_oBaseUri, String p_sCriteria,
			String[] p_sCriteriaValues, String[] p_sColumns,
			ContentResolver p_oResolver) {
		List<MImage> r_listImages = new ArrayList<>();
		Cursor oCursor = p_oResolver.query(p_oBaseUri, p_sColumns, p_sCriteria,
				p_sCriteriaValues, null);
		if (oCursor != null) {
			try {
				if (oCursor.moveToFirst()) {
					MImage oImage ;
					do {
						oImage = new MImage();
						this.bindResult(oImage, oCursor);
						if (oImage.getId() != null) {
							oImage.setLocalUri(Uri.withAppendedPath(p_oBaseUri,
									oImage.getId()));
						}
						r_listImages.add(oImage);
					} while (oCursor.moveToNext());
				}
			} finally {
				oCursor.close();
			}
		}
		return r_listImages;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MImage> search(Uri p_oBaseUri, String p_sCriteria,
			String[] p_sCriteriaValues, MContext p_oContext) {
		return this.search(p_oBaseUri, p_sCriteria, p_sCriteriaValues,
				getContentResolver(p_oContext));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MImage> search(Uri p_oBaseUri, String p_sCriteria,
			String[] p_sCriteriaValues, String[] p_sColumns, MContext p_oContext) {
		return this.search(p_oBaseUri, p_sCriteria, p_sCriteriaValues,
				p_sColumns, getContentResolver(p_oContext));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(MImage p_oImage, ContentResolver p_oResolver) {
		Log.d(Application.LOG_TAG, "delete photo: " + p_oImage.getLocalUri());

		p_oResolver.delete(p_oImage.getLocalUri(), null, null);
		this.deleteThumbnailsForImage(p_oImage);

		// this.printThumbnails(p_oResolver);
	}

	/**
	 * 
	 */
	public void deleteAllThumbnails() {
		Log.d(Application.LOG_TAG, "deleteAllThumbnails");

		Uri oUri = Thumbnails.EXTERNAL_CONTENT_URI;

		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
		ContentResolver oContentResolver = oApplication.getCurrentVisibleActivity().getContentResolver();

		Cursor oCursor = oContentResolver.query(oUri,
				new String[] { Thumbnails.DATA }, null, null, null);
		if (oCursor != null) {
			try {
				while (oCursor.moveToNext()) {
					int iColIndex = oCursor
							.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
					String sFile = oCursor.getString(iColIndex);
					Log.d(Application.LOG_TAG, "thumbnail file: " + sFile);
					boolean bOk = new File(sFile).delete();
					Log.d(Application.LOG_TAG, "thumbnail deleted file success: " + bOk);
				}
			} finally {
				oCursor.close();
			}
		}

		oContentResolver.delete(oUri, null, null);
	}

	/**
	 * @param p_oImage
	 */
	private void deleteThumbnailsForImage(MImage p_oImage) {

		Log.d(Application.LOG_TAG, "deleteThumbnailsForImage");
		Log.d(Application.LOG_TAG, "  image uri: " + p_oImage.getLocalUri().toString());

		// Uri oUri = Thumbnails.getContentUri("external");
		Uri oUri = Thumbnails.EXTERNAL_CONTENT_URI;

		String sFilter = Thumbnails.IMAGE_ID + " = ?";
		String[] t_sFilterValues = new String[] { p_oImage.getId() };

		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
		ContentResolver oContentResolver = oApplication.getCurrentVisibleActivity().getContentResolver();
		Cursor oCursor = oContentResolver.query(oUri,
				new String[] { Thumbnails.DATA }, sFilter, t_sFilterValues,
				null);
		if (oCursor != null) {
			try {
				while (oCursor.moveToNext()) {
					int iColIndex = oCursor
							.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
					String sFile = oCursor.getString(iColIndex);
					Log.d(Application.LOG_TAG, "thumbnail file: " + sFile);
					boolean bOk = new File(sFile).delete();
					Log.d(Application.LOG_TAG, "thumbnail deleted file success: " + bOk);
				}
			} finally {
				oCursor.close();
			}
		}

		oContentResolver.delete(oUri, sFilter, t_sFilterValues);
	}

	/**
	 * @param p_oContentResolver
	 */
	protected void printThumbnails(ContentResolver p_oContentResolver) {

		Log.d(Application.LOG_TAG, "printThumbnails");

		Uri oUri = MediaStore.Images.Thumbnails.getContentUri("external");
		Log.d(Application.LOG_TAG, "uri:" + oUri.toString());

		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
		Cursor oCursor = MediaStore.Images.Thumbnails.queryMiniThumbnails(
				oApplication.getCurrentVisibleActivity().getContentResolver(), oUri,
				MediaStore.Images.Thumbnails.MINI_KIND, null);

		if (oCursor != null) {
			try {
				int iCount = oCursor.getColumnCount();
				StringBuilder oHeader = new StringBuilder();
				for (int i = 0; i < iCount; i++) {
					oHeader.append(oCursor.getColumnName(i));
					oHeader.append(',');
				}
				Log.d(Application.LOG_TAG, oHeader.toString());
				Log.d(Application.LOG_TAG, "");

				while (oCursor.moveToNext()) {
					StringBuilder oLine = new StringBuilder();
					for (int i = 0; i < iCount; i++) {
						oLine.append(oCursor.getString(i));
						oLine.append(',');
					}
					Log.d(Application.LOG_TAG, oLine.toString());
				}
			} finally {
				oCursor.close();
			}
		} else {
			Log.d(Application.LOG_TAG, "no thumbnails");
		}
	}

	/**
	 * @param p_oContentResolver
	 */
	@Override
	public void printImages(ContentResolver p_oContentResolver) {

		Log.d(Application.LOG_TAG, "printImages");

		AndroidApplication oApplication = (AndroidApplication) Application.getInstance();
		Cursor oCursor = MediaStore.Images.Media.query(oApplication.getCurrentVisibleActivity()
				.getContentResolver(),
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, DEFAULT_COLUMNS,
				null, MediaStore.Images.Media._ID);

		if (oCursor != null) {
			try {
				int iCount = oCursor.getColumnCount();
				StringBuilder oHeader = new StringBuilder();
				for (int i = 0; i < iCount; i++) {
					oHeader.append(oCursor.getColumnName(i));
					oHeader.append(',');
				}
				Log.d(Application.LOG_TAG, oHeader.toString());
				Log.d(Application.LOG_TAG, "");

				while (oCursor.moveToNext()) {
					StringBuilder oLine = new StringBuilder();
					for (int i = 0; i < iCount; i++) {
						oLine.append(oCursor.getString(i));
						oLine.append(',');
					}
					Log.d(Application.LOG_TAG, oLine.toString());
				}
			} finally {
				oCursor.close();
			}
		} else {
			Log.d(Application.LOG_TAG, "no images");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(MImage p_oImage, MContext p_oContext) {
		this.delete(p_oImage, getContentResolver(p_oContext));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath(Uri p_oUri, MContext p_oContext) {
		ContentResolver oResolver = getContentResolver(p_oContext);
		return this.getPath(p_oUri, oResolver);
	}

	/**
	 * @param p_oUri
	 * @param p_oContentResolver
	 * @return
	 */
	private String getPath(Uri p_oUri, ContentResolver p_oContentResolver) {
		String r_sPath = null;
		String[] t_sProjection = { MediaStore.Images.Media.DATA };
		Cursor oCursor = p_oContentResolver.query(p_oUri, t_sProjection, null,
				null, null);
		if (oCursor != null && !oCursor.isAfterLast()) {
			try {
				int iColumnIndex = oCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				oCursor.moveToFirst();
				r_sPath = oCursor.getString(iColumnIndex);
			} finally {
				oCursor.close();
			}
		}
		return r_sPath;
	}

	/**
	 * @param p_oImage
	 * @return
	 */
	protected ContentValues bindToContentValues(MImage p_oImage,String[] p_sColumns) {
		ContentValues r_oContentValues = new ContentValues();

		List<String> listColumns = Arrays.asList(p_sColumns);

		if (listColumns.contains(Media.DISPLAY_NAME)) {
			r_oContentValues.put(Media.DISPLAY_NAME, p_oImage.getDisplayName());
		}
		if (listColumns.contains(Media.MIME_TYPE)) {
			r_oContentValues.put(Media.MIME_TYPE, p_oImage.getContentType());
		}
		if (listColumns.contains(Media.TITLE)) {
			r_oContentValues.put(Media.TITLE, p_oImage.getTitle());
		}
		if (listColumns.contains(Media.DATE_TAKEN)) {
			r_oContentValues.put(Media.DATE_TAKEN, p_oImage.getDateTaken());
		}
		if (listColumns.contains(Media.DATE_MODIFIED)) {
			r_oContentValues.put(Media.DATE_MODIFIED, p_oImage.getDateTaken());
		}
		if (listColumns.contains(Media.DESCRIPTION)) {
			r_oContentValues.put(Media.DESCRIPTION, p_oImage.getDescription());
		}
		if (listColumns.contains(Media.IS_PRIVATE)) {
			r_oContentValues.put(Media.IS_PRIVATE, p_oImage.isPriv());
		}
		if (listColumns.contains(Media.LATITUDE)) {
			r_oContentValues.put(Media.LATITUDE, p_oImage.getLatitude());
		}
		if (listColumns.contains(Media.LONGITUDE)) {
			r_oContentValues.put(Media.LONGITUDE, p_oImage.getLongitude());
		}

		return r_oContentValues;
	}

	/**
	 * @param p_oImage
	 * @param p_oCursor
	 */
	protected void bindResult(MImage p_oImage, Cursor p_oCursor) {

		int iColumnIndex = p_oCursor.getColumnIndex(MediaStore.Images.Media._ID);
		if (iColumnIndex != -1) {
			p_oImage.setId(p_oCursor.getString(iColumnIndex));
		}

		iColumnIndex = p_oCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
		if (iColumnIndex != -1) {
			p_oImage.setDisplayName(p_oCursor.getString(iColumnIndex));
		}

		iColumnIndex = p_oCursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE);
		if (iColumnIndex != -1) {
			p_oImage.setContentType(p_oCursor.getString(iColumnIndex));
		}

		iColumnIndex = p_oCursor.getColumnIndex(MediaStore.Images.Media.TITLE);
		if (iColumnIndex != -1) {
			p_oImage.setTitle(p_oCursor.getString(iColumnIndex));
		}

		iColumnIndex = p_oCursor.getColumnIndex(MediaStore.Images.Media.SIZE);
		if (iColumnIndex != -1) {
			p_oImage.setSize(p_oCursor.getLong(iColumnIndex));
		}

		iColumnIndex = p_oCursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);
		if (iColumnIndex != -1) {
			p_oImage.setDateModified(p_oCursor.getLong(iColumnIndex));
		}

		iColumnIndex = p_oCursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
		if (iColumnIndex != -1) {
			p_oImage.setDateTaken(p_oCursor.getLong(iColumnIndex));
		}

		iColumnIndex = p_oCursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION);
		if (iColumnIndex != -1) {
			p_oImage.setDescription(p_oCursor.getString(iColumnIndex));
		}

		iColumnIndex = p_oCursor.getColumnIndex(MediaStore.Images.Media.IS_PRIVATE);
		if (iColumnIndex != -1) {
			p_oImage.setPriv(Boolean.parseBoolean(p_oCursor.getString(iColumnIndex)));
		}

		iColumnIndex = p_oCursor.getColumnIndex(MediaStore.Images.Media.LATITUDE);
		if (iColumnIndex != -1) {
			p_oImage.setLatitude(p_oCursor.getDouble(iColumnIndex));
		}

		iColumnIndex = p_oCursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE);
		if (iColumnIndex != -1) {
			p_oImage.setLongitude(p_oCursor.getDouble(iColumnIndex));
		}

		iColumnIndex = p_oCursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);
		if (iColumnIndex != -1) {
			p_oImage.setOrientation(p_oCursor.getDouble(iColumnIndex));
		}
	}

	/**
	 * @param p_oContext
	 * @return
	 */
	protected ContentResolver getContentResolver(MContext p_oContext) {		
		return (((AndroidContextImpl) p_oContext).getAndroidNativeContext()).getContentResolver();
	}

	/**
	 * (non-Javadoc)
	 * 
	 */
	@Override
	public void writeResizedBitmap(Uri p_oUri, OutputStream p_oStream,
			CompressFormat p_oFormat, int p_iQuality, int p_iMaxWidth,
			MContext p_oContext) {

		String sImagePath = this.getPath(p_oUri, p_oContext);
		if (sImagePath != null) {
			Bitmap oBitmap = this.resizeBitmap(sImagePath,
					this.computeSampleSize(sImagePath, p_iMaxWidth));

			// oBitmap est nulle si l'image n'existe ni sur le mobile ni sur le serveur
			// (Supprimée manuellement sur le mobile)
			if (oBitmap != null) {
				// Write image in the output stream
				oBitmap.compress(p_oFormat, p_iQuality, p_oStream);
				oBitmap.recycle();
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 */
	@Override
	public void insert(MImage p_oImage, File p_oFile, int p_iMaxWidth,
			ContentResolver p_oResolver) throws MMediaException {

		try {
			ExifInterface oExif = new ExifInterface(p_oFile.getAbsolutePath());
			int iOrientation = oExif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			p_oImage.setOrientation(iOrientation);
			int iRotate = 0;
			switch (iOrientation) {
				case ExifInterface.ORIENTATION_ROTATE_270:
					iRotate= ORIENTATION_270;
					break ;
				case ExifInterface.ORIENTATION_ROTATE_180:
					iRotate = ORIENTATION_180;
					break ;
				case ExifInterface.ORIENTATION_ROTATE_90:
					iRotate = ORIENTATION_90;
					break ;
				default :
					iRotate = 0;
			}

			int iSampleSize = this.computeSampleSize(p_oFile.getAbsolutePath(), p_iMaxWidth);
			Bitmap oResizedBitmap = this.resizeBitmap(p_oFile.getAbsolutePath(), iSampleSize);
			if (iRotate != 0) {
				oResizedBitmap = this.rotate(oResizedBitmap, iRotate);
			}
			this.insert(p_oImage, oResizedBitmap, p_oResolver);
			oResizedBitmap.recycle();
		} catch (IOException oException) {
			throw new MMediaException(oException);
		}
	}

	/**
	 * @param p_oPhotoContent
	 * @param p_iMaxWidth
	 */
	@Override
	public int computeSampleSize( byte[] p_oPhotoContent, int p_iMaxWidth ) {
		
		int r_iSampleSize = 1;
		BitmapFactory.Options oBounds = new BitmapFactory.Options();
		oBounds.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(p_oPhotoContent, 0, p_oPhotoContent.length, oBounds);
		
		if (oBounds.outWidth != -1 && oBounds.outWidth > p_iMaxWidth) {
			r_iSampleSize = Math.round(  (float) oBounds.outWidth / (float) p_iMaxWidth );
		}

		return r_iSampleSize;
	}
	
	/**
	 * @param p_sImagePath
	 * @param p_iMaxWidth
	 * @return
	 */
	private int computeSampleSize(String p_sImagePath, int p_iMaxWidth) {
		int r_iSampleSize = 1;

		BitmapFactory.Options oBounds = new BitmapFactory.Options();
		// avec ce parametre, ne charge pas l'image. Le but est de juste
		// récupérer ses dimensions pour déterminer le sampleSize.
		oBounds.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(p_sImagePath, oBounds);

		Log.d(Application.LOG_TAG, "computeSampleSize, width: " + oBounds.outWidth);
		Log.d(Application.LOG_TAG, "computeSampleSize, height: " + oBounds.outHeight);

		// then, we compute sampleSize for resizing
		if (oBounds.outWidth != -1 && oBounds.outWidth > p_iMaxWidth) {
			r_iSampleSize = Math.round(  (float) oBounds.outWidth / (float) p_iMaxWidth  );
		}

		return r_iSampleSize;
	}

	/**
	 * @param p_sImagePath
	 * @param p_iSampleSize
	 * @return
	 */
	private Bitmap resizeBitmap(String p_sImagePath, int p_iSampleSize) {
		BitmapFactory.Options oBitmapOptions = new BitmapFactory.Options();
		// Load the bitmap with the new size
		oBitmapOptions.inSampleSize = p_iSampleSize;
		return BitmapFactory.decodeFile(p_sImagePath, oBitmapOptions);
	}

	/**
	 * @param p_oBitmap
	 * @param p_iAngle
	 * @return
	 */
	private Bitmap rotate(Bitmap p_oBitmap, int p_iAngle) {

		Bitmap r_oBitmap = p_oBitmap;
		int iTempW = r_oBitmap.getWidth();
		int iTempH = r_oBitmap.getHeight();

		if (iTempW > iTempH) {
			Matrix oMatrix = new Matrix();
			oMatrix.postRotate(p_iAngle);
			r_oBitmap = Bitmap.createBitmap(r_oBitmap, 0, 0, iTempW, iTempH,oMatrix, true);
		}
		return r_oBitmap;
	}

	/**
	 * @param p_sFile
	 * @param p_oImage
	 * @throws ParseException
	 */
	protected void loadExifData(String p_sFile, MImage p_oImage)
			throws IOException, ParseException {

		if (p_sFile != null) {
			final ExifInterface oExif = new ExifInterface(p_sFile);
			int iOrientation = oExif.getAttributeInt(ExifInterface.TAG_ORIENTATION,	ExifInterface.ORIENTATION_NORMAL);
			int iRotate = 0;
			switch (iOrientation) {
				case ExifInterface.ORIENTATION_ROTATE_270:
					iRotate = ORIENTATION_270;
					break ;
				case ExifInterface.ORIENTATION_ROTATE_180:
					iRotate = ORIENTATION_180;
					break ;
				case ExifInterface.ORIENTATION_ROTATE_90:
					iRotate = ORIENTATION_90;
					break ;
				default :
					iRotate = 0 ;
			}
			p_oImage.setOrientation(iRotate);

			// Coordinate in format degree/mn/s
			// String dLatitude =
			// oExif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
			// String dLongitude =
			// oExif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);

			// Only date (notime)
			// String lGpsDate =
			// oExif.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
			// Only time
			// String lGpsTimestamp =
			// oExif.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);

			// Both date and time in
			String sDate = oExif.getAttribute(ExifInterface.TAG_DATETIME);
			if (sDate != null) {
				SimpleDateFormat oSdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss" , Locale.getDefault() );
				long lDate = oSdf.parse(sDate).getTime();
				p_oImage.setDateModified(lDate);
				p_oImage.setDateTaken(lDate);
			}

			float[] t_sCoords = new float[2];
			if (oExif.getLatLong(t_sCoords)) {
				p_oImage.setLatitude(t_sCoords[0]);
				p_oImage.setLongitude(t_sCoords[1]);
			}
		}
	}
}
