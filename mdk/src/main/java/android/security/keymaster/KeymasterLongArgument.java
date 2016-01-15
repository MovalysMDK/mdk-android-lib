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
class KeymasterLongArgument extends KeymasterArgument {
    public final long value;

    public KeymasterLongArgument(int tag, long value) {
        super(tag);
        switch (KeymasterDefs.getTagType(tag)) {
            case KeymasterDefs.KM_LONG:
            case KeymasterDefs.KM_LONG_REP:
                break; // OK.
            default:
                throw new IllegalArgumentException("Bad long tag " + tag);
        }
        this.value = value;
    }

    public KeymasterLongArgument(int tag, Parcel in) {
        super(tag);
        value = in.readLong();
    }

    @Override
    public void writeValue(Parcel out) {
        out.writeLong(value);
    }
}
