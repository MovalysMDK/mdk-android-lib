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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Utility class for the java side of user specified Keymaster arguments.
 * <p>
 * Serialization code for this and subclasses must be kept in sync with system/security/keystore
 * @hide
 */
public class KeymasterArguments implements Parcelable {
    List<KeymasterArgument> mArguments;

    public static final Creator<KeymasterArguments> CREATOR = new
            Creator<KeymasterArguments>() {
                @Override
                public KeymasterArguments createFromParcel(Parcel in) {
                    return new KeymasterArguments(in);
                }

                @Override
                public KeymasterArguments[] newArray(int size) {
                    return new KeymasterArguments[size];
                }
            };

    public KeymasterArguments() {
        mArguments = new ArrayList<KeymasterArgument>();
    }

    private KeymasterArguments(Parcel in) {
        mArguments = in.createTypedArrayList(KeymasterArgument.CREATOR);
    }

    public void addInt(int tag, int value) {
        mArguments.add(new KeymasterIntArgument(tag, value));
    }

    public void addInts(int tag, int... values) {
        for (int value : values) {
            addInt(tag, value);
        }
    }

    public void addLongs(int tag, long... values) {
        for (long value : values) {
            addLong(tag, value);
        }
    }

    public void addBoolean(int tag) {
        mArguments.add(new KeymasterBooleanArgument(tag));
    }

    public void addLong(int tag, long value) {
        mArguments.add(new KeymasterLongArgument(tag, value));
    }

    public void addBlob(int tag, byte[] value) {
        mArguments.add(new KeymasterBlobArgument(tag, value));
    }

    public void addDate(int tag, Date value) {
        mArguments.add(new KeymasterDateArgument(tag, value));
    }

    private KeymasterArgument getArgumentByTag(int tag) {
        for (KeymasterArgument arg : mArguments) {
            if (arg.tag == tag) {
                return arg;
            }
        }
        return null;
    }

    public boolean containsTag(int tag) {
        return getArgumentByTag(tag) != null;
    }

    public int getInt(int tag, int defaultValue) {
        switch (KeymasterDefs.getTagType(tag)) {
            case KeymasterDefs.KM_ENUM:
            case KeymasterDefs.KM_INT:
                break; // Accepted types
            case KeymasterDefs.KM_INT_REP:
            case KeymasterDefs.KM_ENUM_REP:
                throw new IllegalArgumentException("Repeatable tags must use getInts: " + tag);
            default:
                throw new IllegalArgumentException("Tag is not an int type: " + tag);
        }
        KeymasterArgument arg = getArgumentByTag(tag);
        if (arg == null) {
            return defaultValue;
        }
        return ((KeymasterIntArgument) arg).value;
    }

    public long getLong(int tag, long defaultValue) {
        switch (KeymasterDefs.getTagType(tag)) {
            case KeymasterDefs.KM_LONG:
                break; // Accepted type
            case KeymasterDefs.KM_LONG_REP:
                throw new IllegalArgumentException("Repeatable tags must use getLongs: " + tag);
            default:
                throw new IllegalArgumentException("Tag is not a long type: " + tag);
        }
        KeymasterArgument arg = getArgumentByTag(tag);
        if (arg == null) {
            return defaultValue;
        }
        return ((KeymasterLongArgument) arg).value;
    }

    public Date getDate(int tag, Date defaultValue) {
        if (KeymasterDefs.getTagType(tag) != KeymasterDefs.KM_DATE) {
            throw new IllegalArgumentException("Tag is not a date type: " + tag);
        }
        KeymasterArgument arg = getArgumentByTag(tag);
        if (arg == null) {
            return defaultValue;
        }
        return ((KeymasterDateArgument) arg).date;
    }

    public boolean getBoolean(int tag, boolean defaultValue) {
        if (KeymasterDefs.getTagType(tag) != KeymasterDefs.KM_BOOL) {
            throw new IllegalArgumentException("Tag is not a boolean type: " + tag);
        }
        KeymasterArgument arg = getArgumentByTag(tag);
        if (arg == null) {
            return defaultValue;
        }
        return true;
    }

    public byte[] getBlob(int tag, byte[] defaultValue) {
        switch (KeymasterDefs.getTagType(tag)) {
            case KeymasterDefs.KM_BYTES:
            case KeymasterDefs.KM_BIGNUM:
                break; // Allowed types.
            default:
                throw new IllegalArgumentException("Tag is not a blob type: " + tag);
        }
        KeymasterArgument arg = getArgumentByTag(tag);
        if (arg == null) {
            return defaultValue;
        }
        return ((KeymasterBlobArgument) arg).blob;
    }

    public List<Integer> getInts(int tag) {
        switch (KeymasterDefs.getTagType(tag)) {
            case KeymasterDefs.KM_INT_REP:
            case KeymasterDefs.KM_ENUM_REP:
                break; // Allowed types.
            default:
                throw new IllegalArgumentException("Tag is not a repeating type: " + tag);
        }
        List<Integer> values = new ArrayList<Integer>();
        for (KeymasterArgument arg : mArguments) {
            if (arg.tag == tag) {
                values.add(((KeymasterIntArgument) arg).value);
            }
        }
        return values;
    }

    public List<Long> getLongs(int tag) {
        if (KeymasterDefs.getTagType(tag) != KeymasterDefs.KM_LONG_REP) {
            throw new IllegalArgumentException("Tag is not a repeating long: " + tag);
        }
        List<Long> values = new ArrayList<Long>();
        for (KeymasterArgument arg : mArguments) {
            if (arg.tag == tag) {
                values.add(((KeymasterLongArgument) arg).value);
            }
        }
        return values;
    }

    public int size() {
        return mArguments.size();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(mArguments);
    }

    public void readFromParcel(Parcel in) {
        in.readTypedList(mArguments, KeymasterArgument.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
