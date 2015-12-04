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
package com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews;

import android.content.Intent;
import android.os.Bundle;

import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;

/**
 * Used on components which can display dialogs<br/>
 * This interface will be used to communicate the opening and result of a dialog to the view from the activity.
 */
public interface MMComplexeComponent {
	
	/**
	 * The key used to retrieve the component id
	 */
	public static final String COMPLEXE_COMPONENT_ID_KEY = "COMPLEXE_COMPONENT_ID_KEY";

	/**
	 * The key used to retrieve the fragment tag
	 */
	public static final String COMPLEXE_COMPONENT_FRAGMENT_TAG_KEY = "COMPLEXE_COMPONENT_FRAGMENT_TAG_KEY"; 

	/**
	 * The key used to retrieve the fragment tag
	 */
	public static final String COMPLEXE_COMPONENT_REQUEST_CODE = "COMPLEXE_COMPONENT_REQUEST_CODE";
	
	/** 
	 * mask for result code on startActivityForResult
	 */
	public static final int REQUEST_CODE_MASK = 0x0000ffff;
	
	/**
	 * This method is called if the component can show a {@link MMDialogFragment}<br>
	 * <br>
	 * @param p_oParameters parameters
	 * @return A MMDialogFragment object
	 */
	public MMDialogFragment createDialogFragment(Bundle p_oParameters);

	/**
	 * This method is called to prepare the {@link MMDialogFragment} before it is shown<br>
	 * <br>
	 * @param p_oDialog the dialogFragment
	 * @param p_oDialogFragmentArguments the {@link Bundle} arguments to pass to he DialogFragment
	 */
	public void prepareDialogFragment(MMDialogFragment p_oDialog, Bundle p_oDialogFragmentArguments);
	
	/**
	 * This method is called on activity result (callback from system)
	 * @param p_iRequestCode The request code
	 * @param p_oResultCode The result code
	 * @param p_oIntent The intent
	 */
	public void onActivityResult(int p_iRequestCode, int p_oResultCode, Intent p_oIntent);
	
	/**
	 * Returns the unique request code of the component used to identify the result of the activity.
	 * @return the unique request code
	 */
	public int getRequestCode();
}
