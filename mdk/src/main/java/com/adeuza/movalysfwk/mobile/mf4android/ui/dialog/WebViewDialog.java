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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.webkit.WebView;

/**
 * A generic WebViewDialogFragment with a object used for callback
 *
 *
 * @since Cotopaxi
 */

public class WebViewDialog extends DialogFragment {

	/**
	 * NewInstance method 
	 * @param p_oDismissListener The DismissListener of this DialogFragment. It will be stored in
	 * this object as a WeakReference
	 * @return A new instance of this fragment
	 */
	public static WebViewDialog newInstance(String p_sTitleDialog, String p_sURL, String p_sCancelButton) {
		WebViewDialog oFragment = new WebViewDialog();
        Bundle args = new Bundle();
        args.putString("titleDialog", p_sTitleDialog);
        args.putString("urlAboutScreen", p_sURL);
        args.putString("cancelTitleButton", p_sCancelButton);
        oFragment.setArguments(args);
        return oFragment;
	}

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		String sTitleDialog = getArguments().getString("titleDialog");
        String sURL = getArguments().getString("urlAboutScreen");
        String sCloseButton = getArguments().getString("cancelTitleButton");

        final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(sTitleDialog);
        this.setCancelable(true);

        final WebView oWebView = new WebView(this.getActivity());
        oWebView.loadUrl(sURL);
        builder.setView(oWebView);
        builder.setNeutralButton(sCloseButton,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
