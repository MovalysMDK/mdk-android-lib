package com.adeuza.movalysfwk.mobile.mf4android.application;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import android.content.Context;

import com.adeuza.movalysfwk.mobile.mf4android.context.AndroidContextImpl;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMButton;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMCheckBox;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMChronometer;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMEditText;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMEmailTextViewGroup;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMImageButton;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMImageView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMListView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMMasterRelativeLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMMultiAutoCompleteEditText;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMMultiAutoCompleteEditTextGroup;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMNumberPicker;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMNumberPickerButton;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPhoneTextViewGroup;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPositionEditText;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMProgressBar;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMRadioButton;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMRadioGroup;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMRelativeLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMScanEditTextGroup;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMScrollView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMSectionTitle;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMSeparator;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMSignature;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMTableLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMTableRow;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMTextView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMWorkspaceView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.RunInit;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.context.MContext;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;



/**
 * <p>Create component to initiate cache.</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author smaitre
 */
public class ComponentInit implements RunInit {

	/** classes to instanciate to create cache */
	public static final Class<?>[] COMPONENTS = {
		MMButton.class, 
		MMCheckBox.class, MMChronometer.class,
		MMEditText.class, MMEmailTextViewGroup.class,
		MMImageButton.class, MMImageView.class,
		MMLinearLayout.class, MMListView.class,
		MMMasterRelativeLayout.class, MMMultiAutoCompleteEditText.class, MMMultiAutoCompleteEditTextGroup.class,
		MMNumberPicker.class,
		MMNumberPickerButton.class,
		MMPhoneTextViewGroup.class, 
		MMPositionEditText.class, MMProgressBar.class,
		MMRadioButton.class, MMRadioGroup.class, MMRelativeLayout.class, 
		MMScanEditTextGroup.class, MMScrollView.class, MMSectionTitle.class, MMSeparator.class, MMSignature.class,
		MMTableLayout.class, MMTableRow.class, MMTextView.class, MMView.class, 
		MMWorkspaceView.class
		}; 
	
	/**
	 * Create component to initiate cache
	 * 
	 * @param p_oContext
	 * 		Current context. Never null.
	 * 
	 * @see com.adeuza.movalys.fwk.mobile.javacommons.application.RunInit#run(com.adeuza.MContext.fwk.mobile.javacommons.application.MMContext)
	 */
	@Override
	public void run(MContext p_oContext) {
		AndroidContextImpl oAc = (AndroidContextImpl) p_oContext;
		Constructor<?> oConstructor = null;
		for(Class<?> oClass : COMPONENTS) {
			try {
				oConstructor = oClass.getConstructor(Context.class);
				oConstructor.newInstance(oAc.getAndroidNativeContext());
			} catch (SecurityException e) {
				throw new MobileFwkException(e);
			} catch (NoSuchMethodException e) {
				throw new MobileFwkException(e);
			} catch (IllegalArgumentException e) {
				throw new MobileFwkException(e);
			} catch (InstantiationException e) {
				throw new MobileFwkException(e);
			} catch (IllegalAccessException e) {
				throw new MobileFwkException(e);
			} catch (InvocationTargetException e) {
				throw new MobileFwkException(e);
			}
			
		}
	}
}
