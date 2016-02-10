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
