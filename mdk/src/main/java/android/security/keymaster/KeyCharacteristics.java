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
 * @hide
 */
public class KeyCharacteristics implements Parcelable {
    public KeymasterArguments swEnforced;
    public KeymasterArguments hwEnforced;

    public static final Creator<KeyCharacteristics> CREATOR = new
            Creator<KeyCharacteristics>() {
                @Override
                public KeyCharacteristics createFromParcel(Parcel in) {
                    return new KeyCharacteristics(in);
                }

                @Override
                public KeyCharacteristics[] newArray(int length) {
                    return new KeyCharacteristics[length];
                }
            };

    public KeyCharacteristics() {}

    protected KeyCharacteristics(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        swEnforced.writeToParcel(out, flags);
        hwEnforced.writeToParcel(out, flags);
    }

    public void readFromParcel(Parcel in) {
        swEnforced = KeymasterArguments.CREATOR.createFromParcel(in);
        hwEnforced = KeymasterArguments.CREATOR.createFromParcel(in);
    }

    public Integer getInteger(int tag) {
        if (hwEnforced.containsTag(tag)) {
            return hwEnforced.getInt(tag, -1);
        } else if (swEnforced.containsTag(tag)) {
            return swEnforced.getInt(tag, -1);
        } else {
            return null;
        }
    }

    public int getInt(int tag, int defaultValue) {
        Integer result = getInteger(tag);
        return (result != null) ? result : defaultValue;
    }

    public List<Integer> getInts(int tag) {
        List<Integer> result = new ArrayList<Integer>();
        result.addAll(hwEnforced.getInts(tag));
        result.addAll(swEnforced.getInts(tag));
        return result;
    }

    public Date getDate(int tag) {
        Date result = hwEnforced.getDate(tag, null);
        if (result == null) {
            result = swEnforced.getDate(tag, null);
        }
        return result;
    }

    public Date getDate(int tag, Date defaultValue) {
        if (hwEnforced.containsTag(tag)) {
            return hwEnforced.getDate(tag, null);
        } else if (hwEnforced.containsTag(tag)) {
            return swEnforced.getDate(tag, null);
        } else {
            return defaultValue;
        }
    }

    public boolean getBoolean(KeyCharacteristics keyCharacteristics, int tag) {
        if (keyCharacteristics.hwEnforced.containsTag(tag)) {
            return keyCharacteristics.hwEnforced.getBoolean(tag, false);
        } else {
            return keyCharacteristics.swEnforced.getBoolean(tag, false);
        }
    }
}

