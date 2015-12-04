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
package com.adeuza.movalysfwk.mobile.mf4android.mediastore.images;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;

import com.adeuza.movalysfwk.mobile.mf4android.mediastore.MMediaException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;

/**
 * Service to query images in MediaStore
 *
 */
public interface MMediaStoreImageService {

	/**
	 * Return path of the image uri
	 * @param p_oUri uri of the image
	 * @param p_oContext context
	 * @return
	 */
	public String getPath( Uri p_oUri, MContext p_oContext );
	
	/**
	 * Load an image
	 * @param p_oUri image uri
	 * @param p_oContext context
	 * @return image
	 */
	public MImage load( Uri p_oUri, MContext p_oContext ) throws MMediaException ;
	
	/**
	 * Load an image
	 * @param p_oUri image uri
	 * @param p_oContentResolver content resolver
	 * @return image
	 */
	public MImage load(Uri p_oUri, ContentResolver p_oContentResolver) throws MMediaException ;
	
	/**
	 * @param p_oUri
	 * @param p_iMaxWidth
	 * @param p_oContext
	 * @return
	 */
	public Bitmap loadBitmap(Uri p_oUri, int p_iMaxWidth, ContentResolver p_oContentResolver);
	
	/**
	 * Insert an image
	 * @param p_oImage image
	 * @param p_oBitmap bitmap
	 * @param p_oContext context
	 * @throws MMediaException
	 */
	public void insert( MImage p_oImage, Bitmap p_oBitmap, MContext p_oContext ) throws MMediaException;
	
	/**
	 * Insert an image
	 * @param p_oImage image
	 * @param p_oBitmap bitmap
	 * @param p_oResolver content resolver
	 * @throws MMediaException
	 */
	public void insert( MImage p_oImage, Bitmap p_oBitmap, ContentResolver p_oResolver ) throws MMediaException;
	
	/**
	 * Insert an image
	 * @param p_oImage image
	 * @param p_oFile image file
	 * @param p_iMaxWidth max width of images
	 * @param p_oResolver content resolver
	 * @throws MMediaException
	 */
	public void insert( MImage p_oImage, File p_oFile, int p_iMaxWidth, ContentResolver p_oResolver ) throws MMediaException;
	
	/**
	 * Update an image
	 * @param p_oImage image
	 * @param p_oResolver content resolver
	 * @throws MMediaException
	 */
	public void update( MImage p_oImage, ContentResolver p_oResolver ) throws MMediaException;
	
	/**
	 * Update an image
	 * @param p_oImage
	 * @param p_sColumns
	 * @param p_oResolver content resolver
	 * @throws MMediaException
	 */
	public void update( MImage p_oImage, String[] p_sColumns, ContentResolver p_oResolver ) throws MMediaException;
	
	/**
	 * Search for images
	 * @param p_oBaseUri search base uri
	 * @param p_sCriteria search criteria (where)
	 * @param p_sCriteriaValues values for binding the query
	 * @param p_oContext context
	 * @return found images
	 */
	public List<MImage> search( Uri p_oBaseUri, String p_sCriteria, String[] p_sCriteriaValues, MContext p_oContext );
	
	/**
	 * Search for images
	 * @param p_oBaseUri search base uri
	 * @param p_sCriteria search criteria (where)
	 * @param p_sCriteriaValues values for binding the query
	 * @param p_sColumns columns to retrieve
	 * @param p_oContext context
	 * @return
	 */
	public List<MImage> search( Uri p_oBaseUri, String p_sCriteria, String[] p_sCriteriaValues, String[] p_sColumns, MContext p_oContext );
	
	/**
	 * Search for images
	 * @param p_oBaseUri search base uri
	 * @param p_sCriteria search criteria (where)
	 * @param p_sCriteriaValues values for binding the query
	 * @param p_oResolver content resolver
	 * @return found images
	 */
	public List<MImage> search( Uri p_oBaseUri, String p_sCriteria, String[] p_sCriteriaValues, ContentResolver p_oResolver );
	
	/**
	 * Search for images
	 * @param p_oBaseUri search base uri
	 * @param p_sCriteria search criteria (where)
	 * @param p_sCriteriaValues values for binding the query
	 * @param p_sColumns columns to retrieve
	 * @param p_oResolver content resolver
	 * @return found images
	 */
	public List<MImage> search( Uri p_oBaseUri, String p_sCriteria, String[] p_sCriteriaValues,
			String[] p_sColumns, ContentResolver p_oResolver );
	
	/**
	 * Delete image
	 * @param p_oImage image
	 * @param p_oResolver content resolver
	 */
	public void delete( MImage p_oImage, ContentResolver p_oResolver );
	
	/**
	 * Delete image
	 * @param p_oImage image
	 * @param p_oContext context
	 */
	public void delete( MImage p_oImage, MContext p_oContext );
	
	
	/**
	 * Load an image via uri and write it to an outputstream.
	 * @param p_oUri
	 * @param p_oStream
	 * @param p_oFormat
	 * @param p_iQuality
	 * @param p_iMaxWidth
	 * @param p_oContext
	 */
	public void writeResizedBitmap( Uri p_oUri, OutputStream p_oStream,
			Bitmap.CompressFormat p_oFormat, int p_iQuality, int p_iMaxWidth, MContext p_oContext );
	
	public void printImages( ContentResolver p_oContentResolver);
	
	public int computeSampleSize( byte[] p_oPhotoContent, int p_iMaxWidth );
}
