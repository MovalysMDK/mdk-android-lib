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
import java.lang.reflect.Method;
import java.util.Map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMComplexeComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentBindDestroy;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMFixedListDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;

/**
 * <p>
 * Fixed list component
 * </p>
 * <p>
 * This component takes the following xml attributes for customization purpose:<br/>
 * <ul>
 * 	<li>"titleMode": put to "false" to hide the title and button (default is true)</li>
 * 	<li>"maxItems" : defines the maximum number of items displayed in the list</li>
 * </ul>
 * </p>
 * 
 * 
 * @param <ITEM> the class of items displayed in the list
 * @param <ITEMVM> the class of view model processing the items displayed
 */
public class MMFixedListView<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>> 
	extends AbstractFixedListView<ITEM, ITEMVM> 
	implements OnDismissListener, MMComplexeComponent, ComponentBindDestroy {

	/** The static value for creating mode */
	public static final String FIXED_LIST_MODE_CREATE = "create";

	/** The static value for editing mode */
	public static final String FIXED_LIST_MODE_EDIT = "edit";

	/** The static value for the dialog of this FixedList */
	public static final int DETAIL_DIALOG_ID = 1;

	/** Tag used for debugging */
	private static final String TAG = "MMFixedListView";

	/** the custom dialog class used to display the details of a row */
	private String dialogClassName;

	/** the dialog used to display the details of a row */
	private MMFixedListDialogFragment<ITEM, ITEMVM> dialog;

	/** the cancel button id */
	private int uiCancelButtonId;

	/** the validation button id */
	private int uiOkButtonId;

	/** the "validate and add new" button id */
	private int uiOkAndNextButtonId;

	/** true if a new item is being added */
	private boolean addNewItem;
	
	/** stores the row currently being edited, -1 if no row is being edited (used for screen rotation) */
	private int iLastSelectedItem;

	/** Component request code */
	private int requestCode = -1;
	
	/**
	 * <p>
	 * Construct an object <em>MMFixedListView</em>.
	 * </p>
	 * 
	 * @param p_oContext
	 *            The application context
	 * @param p_oAttrs
	 *            parameter to configure the <em>MMFixedListView</em> object.
	 */
	public MMFixedListView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {

			this.uiCancelButtonId = this
					.getAndroidApplication()
					.getAndroidIdByRKey(
							AndroidApplicationR.component__fixedlist_detail_cancel__button);
			this.uiOkButtonId = this.getAndroidApplication().getAndroidIdByRKey(
					AndroidApplicationR.component__fixedlist_detail_ok__button);
			this.uiOkAndNextButtonId = this
					.getAndroidApplication()
					.getAndroidIdByRKey(
							AndroidApplicationR.component__fixedlist_detail_okandnext__button);
			this.dialogClassName = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "dialogClass");
		}
	}
	
	/**
	 * Returns the dialog used to display the details of a row
	 * @return the details dialog
	 */
	@SuppressWarnings("rawtypes")
	public MMFixedListDialogFragment getDialog() {
		return this.dialog;
	}

	/**
	 * Returns the {@link MasterVisualComponent} hosted by the details dialog
	 * @return the view hosted by the details dialog
	 */
	public MasterVisualComponent getDetail() {
		return this.dialog.getDetail();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean treatCustomButton(View p_oView) {
		boolean r_bTreat = false;

		//Si le tag de la vue est du type MMDialogFragment,
		//alors on récupère le Dialog Fragment qui a demaandé la fermeture.
		//Sinon, c'est un ViewModel et il s'agit de du VM de l'item qui a été cliqué.
		if( p_oView.getTag() instanceof MMDialogFragment) {
			this.dialog = (MMFixedListDialogFragment<ITEM, ITEMVM>) p_oView.getTag();

			int iViewId = p_oView.getId();

			if (this.uiCancelButtonId == iViewId) { // Bouton annulation du détail
				this.doOnClickCancelButtonOnItem(p_oView);
				r_bTreat = true;
			} else if (this.uiOkButtonId == iViewId) { // Bouton validation du
				// détail
				this.doOnClickValidButtonOnItem(p_oView);
				r_bTreat = true;
			} else if (this.uiOkAndNextButtonId == iViewId) {
				this.doOnClickValidAndNewButtonOnItem(p_oView);
				r_bTreat = true;
			}
		}
		return r_bTreat;
	}

	/**
	 * Called when the cancel button is clicked
	 * @param p_oView the view of the clicked button
	 */
	protected void doOnClickCancelButtonOnItem(View p_oView) {
		super.doCancelItem();
		this.dialog.dismiss();
		this.dialog = null;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void doOnClickAddButton(ITEMVM p_oItemVm) {
		super.doOnClickAddButton(p_oItemVm);
		Bundle oBundle = new Bundle();
		oBundle.putString("mode", FIXED_LIST_MODE_CREATE);
		oBundle.putBoolean(
				MMFixedListDialogFragment.FIXED_LIST_VIEW_DIALOG_FRAGMENT_READ_MODE_ARGUMENT_KEY,
				!this.aivFwkDelegate.isEdit());
		this.dialog = (MMFixedListDialogFragment<ITEM, ITEMVM>) createDialogFragment(oBundle);
		this.dialog.show(getFragmentActivityContext()
				.getSupportFragmentManager(), this.dialog
				.getFragmentDialogTag());
	}

	/**
	 * Returns the fragmentActivit context
	 * 
	 * @return a fragmentActivity context
	 */
	private ActionBarActivity getFragmentActivityContext() {
		return  (ActionBarActivity) ((AndroidApplication) Application.getInstance()).getCurrentActivity();
	}

	/**
	 * Called when the valid button is clicked.
	 * @param p_oView the view of the clicked button
	 * @return true if the item was validated regarding the view model
	 */
	@SuppressWarnings("unchecked")
	protected boolean doOnClickValidButtonOnItem(View p_oView) {
		ITEMVM fragmentVMCopy = (ITEMVM) this.dialog.getDetail().getViewModel();
		boolean bValid = doValidItem(this.dialog.isEditing(), this.iLastSelectedItem, fragmentVMCopy);
		if (bValid) {
			this.iLastSelectedItem = -1;
			this.dialog.dismiss();
			this.dialog.getDetail().getDescriptor().unInflate(String.valueOf(this.dialog.hashCode()),
					this.dialog.getDetail(), null);
			this.dialog = null;
			this.updateTitle();
		}
		return bValid;
	}

	/**
	 * Called when the button validating and switching to a new item is clicked.
	 * @param p_oView the view of the button
	 */
	protected void doOnClickValidAndNewButtonOnItem(View p_oView) {
		if (this.doOnClickValidButtonOnItem(p_oView)) {
			this.addNewItem = true;
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.AbstractFixedListView#doOnClickSelectItem(com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doOnClickSelectItem(ITEMVM p_oItemVM) {
		super.doOnClickSelectItem(p_oItemVM);
		this.setSelectedVM(p_oItemVM);
		this.iLastSelectedItem = this.getAdapter().getMasterVM().indexOf(p_oItemVM);
		Bundle oBundle = new Bundle();
		oBundle.putString("mode", FIXED_LIST_MODE_EDIT);
		oBundle.putBoolean(
				MMFixedListDialogFragment.FIXED_LIST_VIEW_DIALOG_FRAGMENT_READ_MODE_ARGUMENT_KEY,
				!this.aivFwkDelegate.isEdit());

		this.dialog = (MMFixedListDialogFragment<ITEM, ITEMVM>) createDialogFragment(oBundle);

		this.dialog.show(getFragmentActivityContext()
				.getSupportFragmentManager(), this.dialog
				.getFragmentDialogTag());
	}

	@Override
	public void onDismiss(DialogInterface p_oDialog) {
		if (this.addNewItem) {
			this.doOnClickAddButton(null);
			this.addNewItem = false;
		}
	}

	@Override
	public void beforeConfigurationChanged(Map<String, Object> p_oConfigurationMap) {
		// nothing to do
	}

	@Override
	public void afterConfigurationChanged(Map<String, Object> p_oConfigurationMap) {
		update();
	}

	/**
	 * Set the id of the validating button
	 * @param p_iUiOkButtonId the new id to set
	 */
	public void setUiOkButtonId(int p_iUiOkButtonId) {
		this.uiOkButtonId = p_iUiOkButtonId;
	}

	@Override
	public Parcelable onSaveInstanceState() {

		Log.d(TAG, "onSaveInstanceState");
		Bundle oToSave = new Bundle();
		oToSave.putParcelable("parent", super.onSaveInstanceState()); // superOnSaveInstanceState
		oToSave.putString("class", MMFixedListView.class.getName());
		oToSave.putInt("lastSelectedItem", this.iLastSelectedItem);
		oToSave.putInt("requestCode", this.requestCode);
		return oToSave;
	}

	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {

		Log.d(TAG, "onRestoreInstanceState: " + p_oState.getClass().getName());
		if (!(p_oState instanceof Bundle)) {
			super.onRestoreInstanceState(p_oState);
			return;
		}
		Bundle savedState = (Bundle) p_oState;
		String sClass = savedState.getString("class");
		if (!MMFixedListView.class.getName().equals(sClass)) {
			super.onRestoreInstanceState(p_oState);
			return;
		}

		super.onRestoreInstanceState(savedState.getParcelable("parent"));

		Boolean bDialogShowing = savedState.getBoolean("dialogShowing");
		if (bDialogShowing != null && bDialogShowing.equals(Boolean.TRUE)) {
			String sMode = (String) savedState.getString("dialogMode");
			if ("modify".equals(sMode)) {
				doOnClickSelectItem(getSelectedVM());
			} else {
				doOnClickAddButton(getSelectedVM());
			}
		}
		this.iLastSelectedItem = savedState.getInt("lastSelectedItem");
		this.requestCode = savedState.getInt("requestCode");
		AbstractMMActivity activity = (AbstractMMActivity) this.getContext();
		activity.replayOnActivityResults();
	}

	@SuppressWarnings("unchecked")
	@Override
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		try {
			if (this.dialogClassName == null) {
				this.dialog = MMFixedListDialogFragment.newInstance(this);
				prepareDialogFragment(this.dialog, p_oParameters);
			} else {
				Class<?> oDialogClass = Class.forName(this.dialogClassName);
				Method m = oDialogClass.getMethod("newInstance", OnDismissListener.class);
				this.dialog = (MMFixedListDialogFragment<ITEM, ITEMVM>) m.invoke(null, this);
				this.dialog.setMode(p_oParameters.getString("mode"));
			}
		} catch (ClassNotFoundException oException) {
			throw new MobileFwkException(oException);
		} catch (NoSuchMethodException oException) {
			throw new MobileFwkException(oException);
		} catch (InvocationTargetException oException) {
			throw new MobileFwkException(oException);
		} catch (IllegalAccessException oException) {
			throw new MobileFwkException(oException);
		}
		return this.dialog;
	}

	@Override
	public void prepareDialogFragment(MMDialogFragment p_oDialog,
			Bundle p_oDialogFragmentArguments) {
		p_oDialog.setArguments(p_oDialogFragmentArguments);
	}

	@Override
	public void onActivityResult(int p_iRequestCode, int p_oResultCode,
			Intent p_oIntent) {
		// Nothing to do
	}
	
	/************************************************************************************************************************
	 ********************************************* Framework delegate callback **********************************************
	 ************************************************************************************************************************/
	
	@Override
	public void doOnPostAutoBind() {
		// nothing
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
		if (this.getDialog() != null && this.getDialog().isVisible()) {
			this.getDialog().localDestroy();
		}
	}

	@Override
	public int getRequestCode() {
		if (this.requestCode == -1) {
			this.requestCode = this.hashCode() & REQUEST_CODE_MASK;
		}
		return this.requestCode;
	}
	
}
