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
package com.adeuza.movalysfwk.mobile.mf4android.ui.dialog;

import java.lang.ref.WeakReference;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.AbstractMMFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.ProgressDialog;

/**
 * <p>Default progress dialog for actions.</p>
 *
 */
public class DefaultProgressDialogFragment extends MMDialogFragment implements ProgressDialog {

	/**
	 * NewInstance method 
	 * @param p_oDismissListener The DismissListener of this DialogFragment. It will be stored in
	 * this object as a WeakReference
	 * @return A new instance of this fragment
	 */
	public static DefaultProgressDialogFragment newInstance(OnDismissListener p_oDismissListener) {
		DefaultProgressDialogFragment oFragment = new DefaultProgressDialogFragment();
		oFragment.m_oDismissListener = new WeakReference<>(p_oDismissListener);
		oFragment.m_sFragmentTag = String.valueOf(AbstractMMFragment.oGeneratedUniqueTag.incrementAndGet());
		return oFragment;
	}
    
	@Override
	public Dialog onCreateDialog(Bundle p_oSavedInstanceState) {
		DefaultProgressDialog oDialog = new DefaultProgressDialog(this.getActivity());
		oDialog.setCancelable(false);
		oDialog.setIndeterminate(true);
		oDialog.setCanceledOnTouchOutside(false);
		return oDialog;
	}

	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.Dialog#getName()
	 */
	@Override
	public String getName() {
		return null;
	}

	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.Dialog#close()
	 */
	@Override
	public void close() {
		this.dismissAllowingStateLoss();
	}
	
	
	private static class DefaultProgressDialog extends android.app.ProgressDialog implements ProgressDialog {

		public DefaultProgressDialog(Context p_oContext) {
			super(p_oContext);
		}


		/**
		 * 
		 * {@inheritDoc}
		 * @see android.app.ProgressDialog#onCreate(android.os.Bundle)
		 */
		@Override
		protected void onCreate(Bundle p_oSavedInstanceState) {
			super.onCreate(p_oSavedInstanceState);
			this.setContentView(((AndroidApplication)Application.getInstance()).getAndroidIdByRKey(
					AndroidApplicationR.fwk_action_loader_dialog));
		}

		/**
		 * {@inheritDoc}
		 * @see android.app.Dialog#onBackPressed()
		 */
		@Override
		public void onBackPressed() {
			//nothing to do
		}
		/**
		 * 
		 * {@inheritDoc}
		 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.Dialog#getName()
		 */
		@Override
		public String getName() {
			return null;
		}
		/**
		 * 
		 * {@inheritDoc}
		 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.dialog.Dialog#close()
		 */
		@Override
		public void close() {
			this.dismiss();
		}
	}
}
