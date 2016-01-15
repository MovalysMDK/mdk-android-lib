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
import android.os.ParcelFormatException;

/**
 * Base class for the Java side of a Keymaster tagged argument.
 * <p>
 * Serialization code for this and subclasses must be kept in sync with system/security/keystore
 * and with hardware/libhardware/include/hardware/keymaster_defs.h
 * @hide
 */
abstract class KeymasterArgument implements Parcelable {
    public final int tag;

    public static final Creator<KeymasterArgument> CREATOR = new
            Creator<KeymasterArgument>() {
                public KeymasterArgument createFromParcel(Parcel in) {
                    final int pos = in.dataPosition();
                    final int tag = in.readInt();
                    switch (KeymasterDefs.getTagType(tag)) {
                        case KeymasterDefs.KM_ENUM:
                        case KeymasterDefs.KM_ENUM_REP:
                        case KeymasterDefs.KM_INT:
                        case KeymasterDefs.KM_INT_REP:
                            return new KeymasterIntArgument(tag, in);
                        case KeymasterDefs.KM_LONG:
                        case KeymasterDefs.KM_LONG_REP:
                            return new KeymasterLongArgument(tag, in);
                        case KeymasterDefs.KM_DATE:
                            return new KeymasterDateArgument(tag, in);
                        case KeymasterDefs.KM_BYTES:
                        case KeymasterDefs.KM_BIGNUM:
                            return new KeymasterBlobArgument(tag, in);
                        case KeymasterDefs.KM_BOOL:
                            return new KeymasterBooleanArgument(tag, in);
                        default:
                            throw new ParcelFormatException("Bad tag: " + tag + " at " + pos);
                    }
                }
                public KeymasterArgument[] newArray(int size) {
                    return new KeymasterArgument[size];
                }
            };

    protected KeymasterArgument(int tag) {
        this.tag = tag;
    }

    /**
     * Writes the value of this argument, if any, to the provided parcel.
     */
    public abstract void writeValue(Parcel out);

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(tag);
        writeValue(out);
    }
}
