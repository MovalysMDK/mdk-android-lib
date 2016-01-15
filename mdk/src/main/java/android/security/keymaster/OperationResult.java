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

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Class for handling the parceling of return values from keymaster crypto operations
 * (begin/update/finish).
 * @hide
 */
public class OperationResult implements Parcelable {
    public final int resultCode;
    public final IBinder token;
    public final long operationHandle;
    public final int inputConsumed;
    public final byte[] output;

    public static final Creator<OperationResult> CREATOR = new
            Creator<OperationResult>() {
                public OperationResult createFromParcel(Parcel in) {
                    return new OperationResult(in);
                }

                public OperationResult[] newArray(int length) {
                    return new OperationResult[length];
                }
            };

    protected OperationResult(Parcel in) {
        resultCode = in.readInt();
        token = in.readStrongBinder();
        operationHandle = in.readLong();
        inputConsumed = in.readInt();
        output = in.createByteArray();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(resultCode);
        out.writeStrongBinder(token);
        out.writeLong(operationHandle);
        out.writeInt(inputConsumed);
        out.writeByteArray(output);
    }
}
