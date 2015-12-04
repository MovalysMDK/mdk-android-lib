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
package com.adeuza.movalysfwk.mobile.mf4android.serialization;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.SparseArrayCompat;

public class SparseArrayIntArrayParcelable extends SparseArrayCompat<int[]>
		implements Parcelable {

	public static Parcelable.Creator<SparseArrayIntArrayParcelable> CREATOR = new Parcelable.Creator<SparseArrayIntArrayParcelable>() {
		@Override
		public SparseArrayIntArrayParcelable createFromParcel(Parcel source) {

			// read size
			int size = source.readInt();

			// read keys
			int[] keys = new int[size];
			source.readIntArray(keys);

			// read values
			int[][] values = new int[size][];

			for (int i = 0; i < size; i++) {
				int subValueSize = source.readInt();
				int[] subValue = new int[subValueSize];
				source.readIntArray(subValue);
				values[i] = subValue;
			}

			// set value in sparse array
			SparseArrayIntArrayParcelable read = new SparseArrayIntArrayParcelable();			
			for (int i = 0; i < size; i++) {
				read.put(keys[i], values[i]);
			}

			return read;
		}

		@Override
		public SparseArrayIntArrayParcelable[] newArray(int size) {
			return new SparseArrayIntArrayParcelable[size];
		}
	};

	public SparseArrayIntArrayParcelable() {

	}

	public SparseArrayIntArrayParcelable(
			SparseArrayIntArrayParcelable sparseBooleanArray) {
		for (int i = 0; i < sparseBooleanArray.size(); i++) {
			this.put(sparseBooleanArray.keyAt(0), sparseBooleanArray.valueAt(0));
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		int[] keys = new int[size()];
		for (int i = 0; i < size(); i++) {
			keys[i] = keyAt(i);
		}

		// Write number of keys
		dest.writeInt(size());
		
		// Write keys
		dest.writeIntArray(keys);
		
		for( int i = 0 ; i < size(); i++) {
			dest.writeInt(valueAt(i).length);
			dest.writeIntArray(valueAt(i));
		}
	}
}
