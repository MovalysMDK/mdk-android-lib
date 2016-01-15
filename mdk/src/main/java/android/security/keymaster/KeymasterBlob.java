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
package android.security.keymaster;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @hide
 */
public class KeymasterBlob implements Parcelable {
    public byte[] blob;

    public KeymasterBlob(byte[] blob) {
        this.blob = blob;
    }
    public static final Creator<KeymasterBlob> CREATOR = new
            Creator<KeymasterBlob>() {
                public KeymasterBlob createFromParcel(Parcel in) {
                    return new KeymasterBlob(in);
                }

                public KeymasterBlob[] newArray(int length) {
                    return new KeymasterBlob[length];
                }
            };

    protected KeymasterBlob(Parcel in) {
        blob = in.createByteArray();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeByteArray(blob);
    }
}
