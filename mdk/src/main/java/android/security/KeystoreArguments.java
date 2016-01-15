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
package android.security;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class for handling the additional arguments to some keystore binder calls.
 * This must be kept in sync with the deserialization code in system/security/keystore.
 * @hide
 */
public class KeystoreArguments implements Parcelable {
    public byte[][] args;

    public static final Creator<KeystoreArguments> CREATOR = new
            Creator<KeystoreArguments>() {
                public KeystoreArguments createFromParcel(Parcel in) {
                    return new KeystoreArguments(in);
                }
                public KeystoreArguments[] newArray(int size) {
                    return new KeystoreArguments[size];
                }
            };

    public KeystoreArguments() {
        args = null;
    }

    public KeystoreArguments(byte[][] args) {
        this.args = args;
    }

    private KeystoreArguments(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        if (args == null) {
            out.writeInt(0);
        } else {
            out.writeInt(args.length);
            for (byte[] arg : args) {
                out.writeByteArray(arg);
            }
        }
    }

    private void readFromParcel(Parcel in) {
        int length = in.readInt();
        args = new byte[length][];
        for (int i = 0; i < length; i++) {
            args[i] = in.createByteArray();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
