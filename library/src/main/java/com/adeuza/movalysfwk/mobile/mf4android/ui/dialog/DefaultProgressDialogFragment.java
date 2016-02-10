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
