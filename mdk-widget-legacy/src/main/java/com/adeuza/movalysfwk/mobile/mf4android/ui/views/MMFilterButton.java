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
package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.InvocationTargetException;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractAutoBindMMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.activity.business.genericdisplay.GenericDisplayDialogImpl;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;

/**
 * <p>Filter button widget used in the Movalys Mobile product for Android to show the filter panel</p>
 *	to customise title and icon of the dialog created, add the folowing elements :
 *<ul>
 *	<li>add xmlns:movalys="http://www.adeuza.com/movalys/mm/android" on the main tag</li> 
 * 	<li>include a com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMFilterButton</li> 
 * 	<li>add title attribute to declare the @string/resource Id to use as title for the search criteria dialog</li> 
 * 	<li>add icon attribute to declare the @drawable/resource Id to use as icon for the search criteria dialog</li>
 *</ul>
 *<p>if title attribute is not set, dialog apears with no icon and no titlebar</p>
 *
 *
 *
 */

public class MMFilterButton extends MMImageButton implements MMComplexeComponent, OnClickListener, OnDismissListener {


	/**
	 * Action class when opening the search dialog
	 */
	private Class<? extends Action<?,?,?,?>> actionClass;

	/** classe du dialogue à ouvrir */
	private Class<? extends AbstractAutoBindMMDialogFragment> dialogClass;


	/**title resource declared as property of the component*/
	private int oUiTitleRessource=0;
	/**icon resource declared as property of the component*/
	private int oUiIconRessource=0;

	/** Framework application */
	private Application oApplication= Application.getInstance();

	/**
	 * Component request code
	 */
	private int requestCode = -1;
	
	/**
	 * Constructs a new MMFilterButton
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see MMImageButton#MMImageButton(Context, AttributeSet)
	 */
	public MMFilterButton(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext,p_oAttrs);
		if(!isInEditMode()) {
			this.dialogClass = this.getDialogClass("start_dialog", p_oAttrs);
			if (this.dialogClass == null) {
				this.actionClass = this.getActionClass("action", p_oAttrs);
			}

			this.setOnClickListener((OnClickListener) this);
			try {
				oUiTitleRessource=p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "title", 0);
			} catch (NotFoundException e) {
				oApplication.getLog().debug("MMFilterButton",
						StringUtils.concat("Ressource not found", String.valueOf(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "title", 0))));
			}
			try {
				oUiIconRessource=p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "icon", 0);
			} catch (NotFoundException e) {
				oApplication.getLog().debug("MMFilterButton",
						StringUtils.concat("Ressource not found", String.valueOf(p_oAttrs.getAttributeResourceValue(Application.MOVALYSXMLNS, "icon", 0))));
			}
		}
	}

	/**
	 * Return the action class
	 * <p>The component can redefine the Action class. So we verify this class</p>
	 * @param p_sParameterName the class name
	 * @param p_oAttrs the attribute set of the component
	 * @return the Action class for the current Component
	 */
	@SuppressWarnings("unchecked")
	private Class<? extends Action<?,?,?,?>> getActionClass( String p_sParameterName, AttributeSet p_oAttrs ) {
		Class<? extends Action<?,?,?,?>> r_oActionClass = null;
		String sActionClass = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, p_sParameterName);
		if (sActionClass != null && sActionClass.length() > 0) {
			try {
				r_oActionClass =  (Class<? extends Action<?,?,?,?>>) Class.forName(sActionClass);
			} catch (ClassNotFoundException e) {
				oApplication.getLog().error("MMFilterButton","Action is missing.",e);
			}
		}
		//		else {
		//			oApplication.getLog().error("MMFilterButton","Parameter 'searchAction' is missing.");
		//			try {
		//				r_oActionClass = (Class<? extends Action<?,?,?,?>>) Class.forName(this.getModel());
		//			} catch (ClassNotFoundException e) {
		//				oApplication.getLog().error("MMFilterButton","Action is missing.",e);
		//			}
		//		}
		return r_oActionClass ;
	}

	/**
	 * Return the dialog class
	 * <p>The component can redefine the Dialog class. So we verify this class</p>
	 * @param p_sParameterName the class name
	 * @param p_oAttrs the attribute set of the component
	 * @return the Dialog class for the current Component
	 */
	@SuppressWarnings("unchecked")
	private Class<? extends AbstractAutoBindMMDialogFragment> getDialogClass( String p_sParameterName, AttributeSet p_oAttrs ) {
		Class<? extends AbstractAutoBindMMDialogFragment> r_oDialogClass = null;
		String sDialogClass = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, p_sParameterName);
		if (sDialogClass != null && sDialogClass.length() > 0) {
			try {
				r_oDialogClass =  (Class<? extends AbstractAutoBindMMDialogFragment>) Class.forName(sDialogClass);
			} catch (ClassNotFoundException e) {
				// the calling method handles the fact that this class is not found
				oApplication.getLog().info("MMFilterButton","dialog is missing : " + sDialogClass );
			}
		}
		return r_oDialogClass ;
	}


	@Override
	public void onClick(View p_oParamView) {
		MMDialogFragment oDialog = createDialogFragment(null);

		Bundle oDialogArguements = new Bundle();
		oDialogArguements.putInt(MMDialogFragment.DIALOG_FRAGMENT_ARGUMENT_TITLE_KEY, this.oUiTitleRessource);
		oDialogArguements.putInt(MMDialogFragment.DIALOG_FRAGMENT_ARGUMENT_ICON_KEY, this.oUiIconRessource);

		prepareDialogFragment(oDialog, oDialogArguements);

		oDialog.show(getFragmentActivityContext().getSupportFragmentManager(), oDialog.getFragmentDialogTag());

	}

	/**
	 * Returns the fragmentActivit context
	 * 
	 * @return a fragmentActivity context
	 */
	private ActionBarActivity getFragmentActivityContext() {
		return  (ActionBarActivity) ((AndroidApplication) Application.getInstance()).getCurrentActivity();
	}

	@Override
	public void onDismiss(DialogInterface p_oDialog) {
		// nothing to do
	}

	@Override
	public MMDialogFragment createDialogFragment(Bundle p_oParameteres) {
		AbstractAutoBindMMDialogFragment r_oDialog = null;
		// launch action to load the data of the viewmodel
		try {
			// create the dialog instance
			try {
				if (this.dialogClass == null) {
					// Get the dialog action
					Application.getInstance().getController().launchAction(actionClass, null);
					GenericDisplayDialogImpl oDialogAction = (GenericDisplayDialogImpl)BeanLoader.getInstance().getBean(actionClass);
					r_oDialog = oDialogAction.getDialogClass().getConstructor(Context.class).newInstance(this.getContext());
				} else {
					r_oDialog = this.dialogClass.getConstructor(ConfigurableVisualComponent.class).newInstance(this);
				}
			} catch (IllegalArgumentException e) {
				oApplication.getLog().error("MMFilterButton","Impossible to instantiate the dialog.",e);
			} catch (SecurityException e) {
				oApplication.getLog().error("MMFilterButton","Impossible to instantiate the dialog.",e);
			} catch (InvocationTargetException e) {
				oApplication.getLog().error("MMFilterButton","Impossible to instantiate the dialog.",e);
			} catch (NoSuchMethodException e) {
				oApplication.getLog().error("MMFilterButton","Impossible to instantiate the dialog.",e);
			}
		} catch (InstantiationException oInstantiationException) {
			oApplication.getLog().error("MMFilterButton","Impossible to instantiate the dialog.",oInstantiationException);
		} catch (IllegalAccessException oIllegalAccessException) {
			oApplication.getLog().error("MMFilterButton","Impossible to instantiate the dialog.",oIllegalAccessException);
		}
		return r_oDialog;
	}


	@Override
	public void prepareDialogFragment(MMDialogFragment p_oDialog, Bundle p_oArgs) {
		p_oDialog.setArguments(p_oArgs);
		if (this.dialogClass != null && p_oDialog instanceof AbstractAutoBindMMDialogFragment) {
			((AbstractAutoBindMMDialogFragment) p_oDialog).setParentActivity(getParentActivity());
			((AbstractAutoBindMMDialogFragment) p_oDialog).analyse();
			((AbstractAutoBindMMDialogFragment) p_oDialog).doFillAction();
		}
	}

	/**
	 * Get the component parent activity
	 * @return the component parent Activity
	 */
	public AbstractMMActivity getParentActivity() {
		AbstractMMActivity oParentActivity = null;
		if(this.getContext() instanceof AbstractMMActivity) {
			oParentActivity = (AbstractMMActivity) this.getContext();
		}
		else if(this.getContext() instanceof ContextThemeWrapper) {
			oParentActivity = (AbstractMMActivity) ((ContextThemeWrapper)this.getContext()).getBaseContext();
		}
		return oParentActivity;
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Bundle r_oBundle = new Bundle();
		r_oBundle.putInt("requestCode", this.requestCode);
		return r_oBundle;
	}
	
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;
		this.requestCode = r_oBundle.getInt("requestCode");
	}
	
	@Override
	public void onActivityResult(int p_iRequestCode, int p_oResultCode,
			Intent p_oIntent) {
		// Nothing to do

	}

	@Override
	public int getRequestCode() {
		if (this.requestCode == -1) {
			this.requestCode = this.hashCode() & REQUEST_CODE_MASK;
		}
		return this.requestCode;
	}
}
