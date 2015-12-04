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
package com.adeuza.movalysfwk.mobile.mf4mjcommons.model;

import java.sql.Timestamp;

import com.adeuza.movalysfwk.mobile.mf4android.database.sqlite.MDKSQLiteStatement;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.DaoException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ResultSetReader;

/**
 * Helper for the component MPhoto
 *
 */
public class MPhotoHelper extends BasePhotoHelper {

    /**
     * Bind a MPhoto to a Statement using a StatementBinder
     *
     * @param p_oStatement statement.
     * @param p_oMPhoto a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhoto} object.
     * @throws DaoException if any.
     */
    public static void bindToStatement(MPhoto p_oMPhoto, MDKSQLiteStatement p_oStatement) throws DaoException {
        if (p_oMPhoto != null) {
            p_oStatement.bindString(p_oMPhoto.getName());
            p_oStatement.bindString(p_oMPhoto.getUri());
            p_oStatement.bindString(p_oMPhoto.getSvg());
            p_oStatement.bindTimestamp(p_oMPhoto.getDate());
            p_oStatement.bindString(p_oMPhoto.getDescription());
            p_oStatement.bindMEnum(p_oMPhoto.getPhotoState());
        } else {
            p_oStatement.bindNull();
            p_oStatement.bindNull();
            p_oStatement.bindNull();
            p_oStatement.bindNull();
            p_oStatement.bindNull();
            p_oStatement.bindNull();
        }
    }

    /**
     * <p>readResultSet.</p>
     *
     * @param p_oReader a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.data.dao.query.ResultSetReader} object.
     * @throws DaoException if any.
     * @return a {@link com.adeuza.movalysfwk.mobile.mf4mjcommons.model.MPhoto} object.
     */
    public static MPhoto readResultSet(ResultSetReader p_oReader) throws DaoException {
        MPhoto r_oPhoto = null;
        String sPhotoName = p_oReader.getString();
        String sPhotoUri = p_oReader.getString();
        String sPhotoSvg = p_oReader.getString();
        Timestamp oPhotoDate = p_oReader.getTimestamp();
        String sPhotoComment = p_oReader.getString();
        Integer iPhotoState = p_oReader.getInteger();

        if (sPhotoName != null || sPhotoUri != null || sPhotoComment != null || oPhotoDate != null || iPhotoState != null || sPhotoSvg != null) {
            r_oPhoto = new MPhoto();
            r_oPhoto.setName(sPhotoName);
            r_oPhoto.setUri(sPhotoUri);
            r_oPhoto.setSvg(sPhotoSvg);
            r_oPhoto.setDate(oPhotoDate);
            r_oPhoto.setDescription(sPhotoComment);

            if (iPhotoState == null) {
                iPhotoState = MPhotoState.SELECTED.getBaseId();
            }

            r_oPhoto.setPhotoState(MPhotoState.valueOf(iPhotoState));
        }
        return r_oPhoto;
    }
}
