package com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews;

import android.content.Intent;
import android.os.Bundle;

import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;

/**
 * <p>
 * All composite MMComponents used in Movalys Mobile Layouts extends MMViewGroup<br/>
 * let's see
 * <ul>
 * 
 * <li>Phone number with call button {@link MMPhoneTextViewGroup},</li>
 * <li>Scan edit text {@link MMScanEditTextGroup},</li>
 * <li>GPS Edit text {@link MMPositionEditText},</li>
 * <li>...</li>
 * </ul>
 * 
 * {@link MMValueableView} implements {@link MMView} This interface do the same job for composite components
 * </p>
 * 
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 * 
 * @author dmaurange
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
	 * This method is called if the component can show a {@link MMDialogFragment}<br>
	 * <br>
	 * @return A MMDialogFragment object
	 */
	public MMDialogFragment createDialogFragment(Bundle p_oParameters);

	/**
	 * This method is called if the component can show a {@link MMDialogFragment}<br>
	 * <br>
	 * @param p_iDialogId the dialogFragment
	 * @param p_oArgs the {@link Bundle} arguments to pass to he DialogFragment
	 */
	public void prepareDialogFragment(MMDialogFragment p_oDialog, Bundle p_oDialogFragmentArguments);
	
	/**
	 * @param p_iRequestCode The request code
	 * @param p_oResultCode The result code
	 * @param p_oIntent The intent
	 */
	public void onActivityResult(int p_iRequestCode, int p_oResultCode, Intent p_oIntent);
}
