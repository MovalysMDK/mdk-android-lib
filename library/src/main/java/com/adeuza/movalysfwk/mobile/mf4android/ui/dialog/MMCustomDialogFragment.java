package com.adeuza.movalysfwk.mobile.mf4android.ui.dialog;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.AbstractMMFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationR;

/**
 * <p>
 * 	Component that represent a custom dialog box with 2 buttons and one question.
 * </p>
 * 
 * <p>
 * Copyright (c) 2011
 * </p>
 * <p>
 * Company: Adeuza
 * </p>
 * 
 * @author fbourlieux
 * @since Barcelone (24 janv. 2011)
 */
public class MMCustomDialogFragment extends MMDialogFragment{

	/** The application context */
	private Context m_oContext;
	/** The dialog title */
	private String m_sTitle;
	/** dialog message */
	private String m_sMessage;
	/** text on the positive button */
	private String m_sPositiveButtonText;
	/** text on the negative button */
	private String m_sNegativeButtonText;
	/** text on the cancel button */
	private String m_sCancelButtonText;
	/** identifier of the title icon */
	private int m_iIcon;
	/** the current view */
	private View m_uiContentView;
	/** The dialog listener */
	private DialogInterface.OnClickListener m_oPositiveButtonClickListener, m_oNegativeButtonClickListener, m_oCancelButtonClickListener;
	/** The application */
	private AndroidApplication m_oApplication = null;

	/**
	 * The tag for this dialog
	 */
	public static String CUSTOM_DIALOG_TAG = "CustomDialogTag";

	/**
	 * Creates and returns a new instance of this fragment
	 * @param p_oContext The context of this dialogFragment
	 * @param p_iTheme The theme for this dialogFragment
	 * @param p_sTitle The title of the Dialog Fragment
	 * @param p_sMessage The message of the DialogFragment
	 * @param p_sPositiveButtonText The text for the positive button
	 * @param p_sNegativeButtonText The text for the negative button
	 * @param p_sCancelButtonText The text for the cancel button
	 * @param p_iIcon The id of the icon
	 * @param p_oUiContentView The contentView of the DialogFragment
	 * @param p_oPositiveButtonClickListener The onClickListener for the positive button
	 * @param p_oNegativeButtonClickListener The onClickListener for the negative button
	 * @param p_oCancelButtonClickListener The onClickListener for the cancel button
	 * @param p_oApplication The application context
	 * @return
	 */
	public static MMCustomDialogFragment newInstance(Context p_oContext, /*int p_iTheme, */String p_sTitle, String p_sMessage, String p_sPositiveButtonText,
			String p_sNegativeButtonText,String p_sCancelButtonText, int p_iIcon, View p_oUiContentView, 
			OnClickListener p_oPositiveButtonClickListener, OnClickListener p_oNegativeButtonClickListener, OnClickListener p_oCancelButtonClickListener, 
			AndroidApplication p_oApplication) {
		MMCustomDialogFragment oFragment = new MMCustomDialogFragment();
		oFragment.m_oContext = p_oContext;
		oFragment.m_iIcon = p_iIcon;
		oFragment.m_oApplication = p_oApplication;
		oFragment.m_oNegativeButtonClickListener = p_oNegativeButtonClickListener;
		oFragment.m_oPositiveButtonClickListener = p_oPositiveButtonClickListener;
		oFragment.m_oCancelButtonClickListener = p_oCancelButtonClickListener;
		oFragment.m_sCancelButtonText = p_sCancelButtonText;
		oFragment.m_sMessage = p_sMessage;
		oFragment.m_sNegativeButtonText = p_sNegativeButtonText;
		oFragment.m_sPositiveButtonText = p_sPositiveButtonText;
		oFragment.m_sTitle = p_sTitle;
		oFragment.m_uiContentView = p_oUiContentView;
//		oFragment.setStyle(STYLE_NORMAL, p_iTheme);		oFragment.m_oDismissListener = new WeakReference<OnDismissListener>(p_oDismissListener);
		oFragment.m_sFragmentTag = String.valueOf(AbstractMMFragment.oGeneratedUniqueTag.incrementAndGet());
		oFragment.m_bUpdateComponentReference = false;
		return oFragment;
	}
	
	

	@Override
	/**
	 * {@inheritDoc}
	 */
	public Dialog onCreateDialog(Bundle p_oSavedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(m_oContext);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View oLayout = inflater.inflate(this.m_oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_custom_dialog), null);
		
		builder.setTitle(m_sTitle);
		builder.setIcon(m_iIcon);
		
		// set the positive button
		if (this.m_sPositiveButtonText != null) {
			builder.setPositiveButton(m_sPositiveButtonText, m_oPositiveButtonClickListener);
		}
		// set the negative button
		if (this.m_sNegativeButtonText != null) {
			builder.setNegativeButton(m_sNegativeButtonText, m_oNegativeButtonClickListener);
		}
		
		// set the negative button
		if (this.m_sCancelButtonText != null) {
			builder.setNeutralButton(m_sCancelButtonText, m_oCancelButtonClickListener);
		}
		
		//Setting the content view or the message
		LinearLayout contentView = ((LinearLayout) oLayout.findViewById(this.m_oApplication.getAndroidIdByRKey(AndroidApplicationR.custom_dialog_content_layout)));

		// set the content message
		if (this.m_sMessage != null) {
			builder.setMessage(this.m_sMessage);
			contentView.setVisibility(View.GONE);
		} else if (this.m_uiContentView != null) {
			// if no message set add the contentView to the dialog body
			((LinearLayout) oLayout.findViewById(this.m_oApplication.getAndroidIdByRKey(AndroidApplicationR.custom_dialog_content_layout))).addView(this.m_uiContentView, 
					new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		}
		
		//building the dialog returning it
		builder.setView(oLayout);
		return builder.create();
	}

	/**
	 * Helper class for creating a custom dialog
	 */
	public static class Builder {

		/** The application context */
		private Context context;
		/** The dialog title */
		private String title;
		/** dialog message */
		private String message;
		/** text on the positive button */
		private String positiveButtonText;
		/** text on the negative button */
		private String negativeButtonText;
		/** text on the cancel button */
		private String cancelButtonText;
		/** identifier of the title icon */
		private int icon;
		/** the current view */
		private View contentView;
		/** The dialog listener */
		private DialogInterface.OnClickListener positiveButtonClickListener, negativeButtonClickListener, cancelButtonClickListener;

		private AndroidApplication application = null;

		/**
		 * <p>
		 *  Construct an object <em>Builder</em>.
		 * </p>
		 * @param p_oContext
		 * 		The application context
		 */
		public Builder(Context p_oContext) {
			this.context = p_oContext;
			this.application = (AndroidApplication) Application.getInstance();
		}

		/**
		 * Set the Dialog message from resource
		 * 
		 * @param p_oMessage
		 * @return
		 */
		public Builder setMessage(ApplicationR p_oMessage) {
			return this.setMessage(this.application.getStringResource(p_oMessage));
		}

		/**
		 * Set the Dialog message from resources
		 * @param p_t_oMessages Resources
		 * @return
		 */
		public Builder setMessages(ApplicationR... p_t_oMessages) {
			if (p_t_oMessages != null) {
				return this.setMessages(Arrays.asList(p_t_oMessages));
			}
			return this;
		}

		/**
		 * Set the Dialog message from resources
		 * @param p_oMessages Resources
		 * @return
		 */
		public Builder setMessages(Collection<ApplicationR> p_oMessages) {
			if (p_oMessages != null) {
				final StringBuilder oMessages = new StringBuilder();

				Iterator<ApplicationR> iterMessages = p_oMessages.iterator();
				if (iterMessages.hasNext()) {
					oMessages.append (Application.getInstance().getStringResource(iterMessages.next()));
					while (iterMessages.hasNext()) {
						oMessages.append('\n');
						oMessages.append (Application.getInstance().getStringResource(iterMessages.next()));
					}
				}
				return this.setMessage(oMessages.toString());
			}
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param p_iMessage
		 * @return
		 */
		public Builder setMessage(int p_iMessage) {
			return this.setMessage(this.application.getStringResource(p_iMessage));
		}

		/**
		 * Set the Dialog message from String
		 * 
		 * @param title
		 * @return
		 */
		protected Builder setMessage(String p_sMessage) {
			this.message = p_sMessage;
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param p_oTitle
		 * @return
		 */
		public Builder setTitle(ApplicationR p_oTitle) {
			return this.setTitle(this.application.getStringResource(p_oTitle));
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param p_iTitle
		 * @return
		 */
		public Builder setTitle(int p_iTitle) {
			return this.setTitle(this.application.getStringResource(p_iTitle));
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param p_sTitle
		 * @return
		 */
		protected Builder setTitle(String p_sTitle) {
			this.title = p_sTitle;
			return this;
		}

		/**
		 * Set the Dialog icon from resource 
		 * 
		 * @param p_iIcon 
		 */
		public void setIcon(int p_iIcon) {
			this.icon = p_iIcon;
		}

		/**
		 * Set a custom content view for the Dialog. If a message is set, the contentView is not added to the Dialog...
		 * 
		 * @param p_oView
		 * @return
		 */
		public Builder setContentView(View p_oView) {
			this.contentView = p_oView;
			return this;
		}


		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param p_iPositiveButtonText
		 * @param p_oListener
		 * @return
		 */
		public Builder setPositiveButton(ApplicationR p_oPositiveButtonText, DialogInterface.OnClickListener p_oListener) {
			return this.setPositiveButton(this.application.getStringResource(p_oPositiveButtonText), p_oListener);
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param p_iPositiveButtonText
		 * @param p_oListener
		 * @return
		 */
		public Builder setPositiveButton(int p_iPositiveButtonText, DialogInterface.OnClickListener p_oListener) {
			return this.setPositiveButton(this.application.getStringResource(p_iPositiveButtonText), p_oListener);
		}

		/**
		 * Set the positive button text and it's listener
		 * 
		 * @param p_sPositiveButtonText
		 * @param p_oListener
		 * @return
		 */
		protected Builder setPositiveButton(String p_sPositiveButtonText, DialogInterface.OnClickListener p_oListener) {
			this.positiveButtonText = p_sPositiveButtonText;
			this.positiveButtonClickListener = p_oListener;
			return this;
		}

		/**
		 * Set the negative button text and it's listener
		 * 
		 * @param p_iNegativeButtonText
		 * @param p_oListener
		 * @return
		 */
		public Builder setNegativeButton(ApplicationR p_oNegativeButtonText, DialogInterface.OnClickListener p_oListener) {
			return this.setNegativeButton(this.application.getStringResource(p_oNegativeButtonText), p_oListener);
		}

		/**
		 * Set the negative button text and it's listener
		 * 
		 * @param p_iNegativeButtonText
		 * @param p_oListener
		 * @return
		 */
		public Builder setNegativeButton(int p_iNegativeButtonText, DialogInterface.OnClickListener p_oListener) {
			return this.setNegativeButton(this.application.getStringResource(p_iNegativeButtonText), p_oListener);
		} 

		/**
		 * Set the negative button text and it's listener
		 * 
		 * @param p_sNegativeButtonText
		 * @param p_oListener
		 * @return
		 */
		protected Builder setNegativeButton(String p_sNegativeButtonText, DialogInterface.OnClickListener p_oListener) {
			this.negativeButtonText = p_sNegativeButtonText;
			this.negativeButtonClickListener = p_oListener;
			return this;
		}

//		/**
//		 * Set the cancel button text
//		 * @param p_oCancelButtonText
//		 * @return
//		 */
//		public Builder setCancelButton(final ApplicationR p_oCancelButtonText) {
//			return this.setCancelButton(this.application.getStringResource(p_oCancelButtonText));
//		}
//
//		/**
//		 * Set the cancel button text
//		 * 
//		 * @param p_sCanceButtonText
//		 * @return
//		 */
//		protected Builder setCancelButton(final String p_sCancelButtonText) {
//			this.cancelButtonText = p_sCancelButtonText;
//			return this;
//		}
		
		/**
		 * Set the positive button text and it's listener
		 * 
		 * @param p_sPositiveButtonText
		 * @param p_oListener
		 * @return
		 */
		protected Builder setCancelButton(String p_sCancelButtonText, DialogInterface.OnClickListener p_oListener) {
			this.cancelButtonText = p_sCancelButtonText;
			this.cancelButtonClickListener = p_oListener;
			return this;
		}
		
		
		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param p_iPositiveButtonText
		 * @param p_oListener
		 * @return
		 */
		public Builder setCancelButton(ApplicationR p_oCancelButtonText, DialogInterface.OnClickListener p_oListener) {
			return this.setCancelButton(this.application.getStringResource(p_oCancelButtonText), p_oListener);
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param p_iPositiveButtonText
		 * @param p_oListener
		 * @return
		 */
		public Builder setCancelButton(int p_iCancelButtonText, DialogInterface.OnClickListener p_oListener) {
			return this.setPositiveButton(this.application.getStringResource(p_iCancelButtonText), p_oListener);
		}
		

		/**
		 * Create the custom dialog
		 */
		public MMCustomDialogFragment create() {
			// instantiate the dialog with the custom Theme
			final MMCustomDialogFragment oDialog = MMCustomDialogFragment.newInstance(context, /*this.application.getAndroidIdByRKey(AndroidApplicationR.ThemeCustomDialog),*/ title, message, positiveButtonText, negativeButtonText, cancelButtonText, icon, contentView, positiveButtonClickListener, negativeButtonClickListener, cancelButtonClickListener, application);
			//The builder is now the onCreateView of the DialogFragment instead of builder of Dialog.
			return oDialog;
		}
	}
	
	@Override
	public String getFragmentDialogTag() {
		return CUSTOM_DIALOG_TAG;
	}

}
