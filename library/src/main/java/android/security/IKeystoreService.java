/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.security;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/**
 * This must be kept manually in sync with system/security/keystore until AIDL
 * can generate both Java and C++ bindings.
 * 
 */
public interface IKeystoreService extends IInterface {
	
	/**
	 * Abstract Stub binder
	 */
	public static abstract class AbstractStub extends Binder implements IKeystoreService {

		/** proxy */
		private static class Proxy implements IKeystoreService {
			/** binder */
			private final IBinder mRemote;

			/**
			 * Constructor
			 * @param p_oRemote binder
			 */
			Proxy(IBinder p_oRemote) {
				mRemote = p_oRemote;
			}

			/**
			 * return binder
			 * @return binder
			 */
			@Override
			public IBinder asBinder() {
				return mRemote;
			}

			/**
			 * get Interface descriptor
			 * @return the descriptor
			 */
			public String getInterfaceDescriptor() {
				return DESCRIPTOR;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int test() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(AbstractStub.TRANSACTION_test, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public byte[] get(String p_sName) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				byte[] _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(p_sName);
					mRemote.transact(AbstractStub.TRANSACTION_get, _data, _reply, 0);
					_reply.readException();
					_result = _reply.createByteArray();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int insert(String p_sName, byte[] p_bItem, int p_iUid, int p_iFlags) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(p_sName);
					_data.writeByteArray(p_bItem);
					_data.writeInt(p_iUid);
					_data.writeInt(p_iFlags);
					mRemote.transact(AbstractStub.TRANSACTION_insert, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int del(String p_sName, int p_iUid) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(p_sName);
					_data.writeInt(p_iUid);
					mRemote.transact(AbstractStub.TRANSACTION_del, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int exist(String p_sName, int p_iUid) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(p_sName);
					_data.writeInt(p_iUid);
					mRemote.transact(AbstractStub.TRANSACTION_exist, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public String[] saw(String p_sName, int p_iUid) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				String[] _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(p_sName);
					_data.writeInt(p_iUid);
					mRemote.transact(AbstractStub.TRANSACTION_saw, _data, _reply, 0);
					_reply.readException();
					int size = _reply.readInt();
					_result = new String[size];
					for (int i = 0; i < size; i++) {
						_result[i] = _reply.readString();
					}
					int _ret = _reply.readInt();
					if (_ret != 1) {
						return null;
					}
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int reset() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(AbstractStub.TRANSACTION_reset, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int password(String p_sPassword) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(p_sPassword);
					mRemote.transact(AbstractStub.TRANSACTION_password, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int lock() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(AbstractStub.TRANSACTION_lock, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int unlock(String p_sPassword) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(p_sPassword);
					mRemote.transact(AbstractStub.TRANSACTION_unlock, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int zero() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(AbstractStub.TRANSACTION_zero, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			// 4.3 version
			public int generate(String p_sName, int p_iUid, int p_iFlags) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(p_sName);
					_data.writeInt(p_iUid);
					_data.writeInt(p_iFlags);
					mRemote.transact(AbstractStub.TRANSACTION_generate, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int generate(String p_sName, int p_iUid, int p_iKeyType, int p_iKeySize, int p_iFlags,
					byte[][] p_bArgs) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(p_sName);
					_data.writeInt(p_iUid);
					_data.writeInt(p_iKeyType);
					_data.writeInt(p_iKeySize);
					_data.writeInt(p_iFlags);
					if (p_bArgs == null) {
						_data.writeInt(0);
					} else {
						_data.writeInt(p_bArgs.length);
						for (int i = 0; i < p_bArgs.length; i++) {
							_data.writeByteArray(p_bArgs[i]);
						}
					}
					mRemote.transact(AbstractStub.TRANSACTION_generate, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int importKey(String p_sName, byte[] p_bData, int p_iUid, int p_iFlags)
					throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(p_sName);
					_data.writeByteArray(p_bData);
					_data.writeInt(p_iUid);
					_data.writeInt(p_iFlags);
					mRemote.transact(AbstractStub.TRANSACTION_import, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public byte[] sign(String p_sName, byte[] p_bData) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				byte[] _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(p_sName);
					_data.writeByteArray(p_bData);
					mRemote.transact(AbstractStub.TRANSACTION_sign, _data, _reply, 0);
					_reply.readException();
					_result = _reply.createByteArray();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int verify(String p_sName, byte[] p_bData, byte[] p_bSignature) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(p_sName);
					_data.writeByteArray(p_bData);
					_data.writeByteArray(p_bSignature);
					mRemote.transact(AbstractStub.TRANSACTION_verify, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public byte[] getPubkey(String p_sName) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				byte[] _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(p_sName);
					mRemote.transact(AbstractStub.TRANSACTION_get_pubkey, _data, _reply, 0);
					_reply.readException();
					_result = _reply.createByteArray();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int delKey(String p_sName, int p_iUid) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(p_sName);
					_data.writeInt(p_iUid);
					mRemote.transact(AbstractStub.TRANSACTION_del_key, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int grant(String p_sName, int p_iGranteeUid) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(p_sName);
					_data.writeInt(p_iGranteeUid);
					mRemote.transact(AbstractStub.TRANSACTION_grant, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int ungrant(String p_sName, int p_iGranteeUid) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(p_sName);
					_data.writeInt(p_iGranteeUid);
					mRemote.transact(AbstractStub.TRANSACTION_ungrant, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public long getmtime(String p_sName) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				long _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(p_sName);
					mRemote.transact(AbstractStub.TRANSACTION_getmtime, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readLong();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int duplicate(String p_sSrcKey, int p_sSrcUid, String p_sDestKey, int p_iDestUid)
					throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(p_sSrcKey);
					_data.writeInt(p_sSrcUid);
					_data.writeString(p_sDestKey);
					_data.writeInt(p_iDestUid);
					mRemote.transact(AbstractStub.TRANSACTION_duplicate, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			// 4.3 version
			@Override
			public int isHardwareBacked() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(AbstractStub.TRANSACTION_is_hardware_backed, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int isHardwareBacked(String p_sKeyType) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(p_sKeyType);
					mRemote.transact(AbstractStub.TRANSACTION_is_hardware_backed, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int clearUid(long p_iUid) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeLong(p_iUid);
					mRemote.transact(AbstractStub.TRANSACTION_clear_uid, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}
		}

		/** descriptor */
		private static final String DESCRIPTOR = "android.security.keystore";

		/** transaction test */
		static final int TRANSACTION_test = IBinder.FIRST_CALL_TRANSACTION + 0;
		/** transaction get */
		static final int TRANSACTION_get = IBinder.FIRST_CALL_TRANSACTION + 1;
		/** transaction insert */
		static final int TRANSACTION_insert = IBinder.FIRST_CALL_TRANSACTION + 2;
		/** transaction del */
		static final int TRANSACTION_del = IBinder.FIRST_CALL_TRANSACTION + 3;
		/** transaction exist */
		static final int TRANSACTION_exist = IBinder.FIRST_CALL_TRANSACTION + 4;
		/** transaction saw */
		static final int TRANSACTION_saw = IBinder.FIRST_CALL_TRANSACTION + 5;
		/** transaction reset */
		static final int TRANSACTION_reset = IBinder.FIRST_CALL_TRANSACTION + 6;
		/** transaction password */
		static final int TRANSACTION_password = IBinder.FIRST_CALL_TRANSACTION + 7;
		/** transaction lock */
		static final int TRANSACTION_lock = IBinder.FIRST_CALL_TRANSACTION + 8;
		/** transaction unlock */
		static final int TRANSACTION_unlock = IBinder.FIRST_CALL_TRANSACTION + 9;
		/** transaction zero */
		static final int TRANSACTION_zero = IBinder.FIRST_CALL_TRANSACTION + 10;
		/** transaction generate */
		static final int TRANSACTION_generate = IBinder.FIRST_CALL_TRANSACTION + 11;
		/** transaction import */
		static final int TRANSACTION_import = IBinder.FIRST_CALL_TRANSACTION + 12;
		/** transaction sign */
		static final int TRANSACTION_sign = IBinder.FIRST_CALL_TRANSACTION + 13;
		/** transaction verify */
		static final int TRANSACTION_verify = IBinder.FIRST_CALL_TRANSACTION + 14;
		/** transaction get pubkey */
		static final int TRANSACTION_get_pubkey = IBinder.FIRST_CALL_TRANSACTION + 15;
		/** transaction del key */
		static final int TRANSACTION_del_key = IBinder.FIRST_CALL_TRANSACTION + 16;
		/** transaction grant */
		static final int TRANSACTION_grant = IBinder.FIRST_CALL_TRANSACTION + 17;
		/** transaction ungrant */
		static final int TRANSACTION_ungrant = IBinder.FIRST_CALL_TRANSACTION + 18;
		/** transaction getmtime */
		static final int TRANSACTION_getmtime = IBinder.FIRST_CALL_TRANSACTION + 19;
		/** transaction duplicate */
		static final int TRANSACTION_duplicate = IBinder.FIRST_CALL_TRANSACTION + 20;
		/** transaction is hardware backed */
		static final int TRANSACTION_is_hardware_backed = IBinder.FIRST_CALL_TRANSACTION + 21;
		/** transaction clear uid */
		static final int TRANSACTION_clear_uid = IBinder.FIRST_CALL_TRANSACTION + 22;

		/**
		 * Cast an IBinder object into an IKeystoreService interface, generating
		 * a proxy if needed.
		 * @param p_oObj binder
		 * @return keystore service interface
		 */
		public static IKeystoreService asInterface(final IBinder p_oObj) {
			IKeystoreService r_oKeystore = null;
			if (p_oObj != null) {
				IInterface iin = p_oObj.queryLocalInterface(DESCRIPTOR);
				if (iin != null && iin instanceof IKeystoreService) {
					r_oKeystore = (IKeystoreService) iin;
				} else {
					r_oKeystore = new IKeystoreService.AbstractStub.Proxy(p_oObj);
				}
			}
			return r_oKeystore;
		}

		/** Construct the stub at attach it to the interface. */
		public AbstractStub() {
			attachInterface(this, DESCRIPTOR);
		}

		/**
		 * return binder
		 * @return the binder interface
		 */
		@Override
		public IBinder asBinder() {
			return this;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean onTransact(int p_iCode, Parcel p_oData, Parcel p_oReply, int p_iFlags) throws RemoteException {
			boolean r_bResult = false;
			switch (p_iCode) {
			case INTERFACE_TRANSACTION: 
				p_oReply.writeString(DESCRIPTOR);
				r_bResult = true;
				break;
			case TRANSACTION_test: 
				p_oData.enforceInterface(DESCRIPTOR);
				int resultCode = test();
				p_oReply.writeNoException();
				p_oReply.writeInt(resultCode);
				r_bResult = true;
				break;
			default:
				r_bResult = super.onTransact(p_iCode, p_oData, p_oReply, p_iFlags);
				break;
			}
			return r_bResult;
		}
	}
	
	/**
	 * test method
	 * @return result
	 * @throws RemoteException remote error
	 */
	public int test() throws RemoteException;
	
	/**
	 * get the return 
	 * @param p_sName query
	 * @return byte array
	 * @throws RemoteException remote error
	 */
	public byte[] get(String p_sName) throws RemoteException;

	/**
	 * insert
	 * @param p_sName query
	 * @param p_bItem byte array
	 * @param p_iUid identifier
	 * @param p_iFlags flag
	 * @return result
	 * @throws RemoteException remote error
	 */
	public int insert(String p_sName, byte[] p_bItem, int p_iUid, int p_iFlags) throws RemoteException;

	/**
	 * del
	 * @param p_sName query
	 * @param p_iUid identifier
	 * @return result
	 * @throws RemoteException remote error
	 */
	public int del(String p_sName, int p_iUid) throws RemoteException;

	/**
	 * exist
	 * @param p_sName query
	 * @param p_iUid identifier
	 * @return result
	 * @throws RemoteException remote error
	 */
	public int exist(String p_sName, int p_iUid) throws RemoteException;
	
	/**
	 * saw
	 * @param p_sName query
	 * @param p_iUid identifier
	 * @return string array
	 * @throws RemoteException remote error
	 */
	public String[] saw(String p_sName, int p_iUid) throws RemoteException;

	/**
	 * reset
	 * @return result code
	 * @throws RemoteException remote error
	 */
	public int reset() throws RemoteException;

	/**
	 * password
	 * @param p_sPassword password
	 * @return result
	 * @throws RemoteException remote error
	 */
	public int password(String p_sPassword) throws RemoteException;
	
	/**
	 * lock
	 * @return lock
	 * @throws RemoteException remote error
	 */
	public int lock() throws RemoteException;

	/**
	 * unlock
	 * @param p_sPassword password
	 * @return result
	 * @throws RemoteException remote error
	 */
	public int unlock(String p_sPassword) throws RemoteException;

	/**
	 * zero
	 * @return result
	 * @throws RemoteException remote error
	 */
	public int zero() throws RemoteException;

	/**
	 * generate
	 * @param p_sName query
	 * @param p_iUid id
	 * @param p_iFlags flags
	 * @return result code
	 * @throws RemoteException remote error
	 */
	public int generate(String p_sName, int p_iUid, int p_iFlags) throws RemoteException;

	/**
	 * generate
	 * @param p_sName query
	 * @param p_iUid id
	 * @param p_iKeyType key type
	 * @param p_iKeySize key size
	 * @param p_iFlags flags
	 * @param p_oArgs arguments
	 * @return result code
	 * @throws RemoteException remote error
	 */
	public int generate(String p_sName, int p_iUid, int p_iKeyType, int p_iKeySize, int p_iFlags, byte[][] p_oArgs) throws RemoteException;

	/**
	 * importKey
	 * @param p_sName query
	 * @param p_oData datas
	 * @param p_iUid id
	 * @param p_iFlags flags
	 * @return result code
	 * @throws RemoteException remote error
	 */
	public int importKey(String p_sName, byte[] p_oData, int p_iUid, int p_iFlags) throws RemoteException;

	/**
	 * sign
	 * @param p_sName query
	 * @param p_oData datas
	 * @return byte array
	 * @throws RemoteException remote error
	 */
	public byte[] sign(String p_sName, byte[] p_oData) throws RemoteException;

	/**
	 * verify
	 * @param p_sName query
	 * @param p_oData datas
	 * @param p_oSignature signature
	 * @return result code
	 * @throws RemoteException remote error
	 */
	public int verify(String p_sName, byte[] p_oData, byte[] p_oSignature) throws RemoteException;

	/**
	 * get public key
	 * @param p_sName query
	 * @return byte array for the key
	 * @throws RemoteException remote error
	 */
	public byte[] getPubkey(String p_sName) throws RemoteException;

	/**
	 * delete key
	 * @param p_sName query
	 * @param p_iUid id
	 * @return result code
	 * @throws RemoteException remote error
	 */
	public int delKey(String p_sName, int p_iUid) throws RemoteException;

	/**
	 * grant access
	 * @param p_sName query
	 * @param p_iGranteeUid access id
	 * @return result code
	 * @throws RemoteException remote error
	 */
	public int grant(String p_sName, int p_iGranteeUid) throws RemoteException;

	/**
	 * remove grant access
	 * @param p_sName query
	 * @param p_iGranteeUid access id
	 * @return result code
	 * @throws RemoteException remote error
	 */
	public int ungrant(String p_sName, int p_iGranteeUid) throws RemoteException;

	/**
	 * get time
	 * @param p_sName query
	 * @return time
	 * @throws RemoteException remote error
	 */
	public long getmtime(String p_sName) throws RemoteException;

	/**
	 * duplicate
	 * @param p_sSrcKey source key
	 * @param p_iUid id
	 * @param p_sDestKey dest key
	 * @param p_iDestUid dest id
	 * @return result code
	 * @throws RemoteException remote error
	 */
	public int duplicate(String p_sSrcKey, int p_iUid, String p_sDestKey, int p_iDestUid) throws RemoteException;

	/**
	 * is hardware backed
	 * @return result code
	 * @throws RemoteException remote error
	 */
	public int isHardwareBacked() throws RemoteException;

	/**
	 * is hardware backed
	 * @param p_sString string
	 * @return result code
	 * @throws RemoteException remote error
	 */
	public int isHardwareBacked(String p_sString) throws RemoteException;

	/**
	 * clear unique id
	 * @param p_iUid id
	 * @return result code
	 * @throws RemoteException remote error
	 */
	public int clearUid(long p_iUid) throws RemoteException;
}