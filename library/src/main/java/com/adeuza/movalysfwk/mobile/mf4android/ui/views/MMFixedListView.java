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
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMFixedListDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;

/**
 * <p>
 * Fixed list component
 * </p>
 * <p>
 * you can customize this component :<br/>
 * <ul>
 * <li>add xmlns:movalys="http://www.adeuza.com/movalys/mm/android" on the main
 * tag</li>
 * <li>include a
 * com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPositionEditText</li>
 * <li>add titleMode attribute with "false" value to hidde the title and +
 * button (default is true)</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Copyright (c) 2010
 * </p>
 * <p>
 * Company: Adeuza
 * </p>
 * 
 * @author smaitre
 * 
 * @param <LISTVM>
 *            a model for list
 * @param <ITEMVM>
 *            a model for item
 */
public class MMFixedListView<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>>
extends AbstractFixedListView<ITEM, ITEMVM> implements
OnDismissListener, MMComplexeComponent {

	/**
	 * The static value for creating mode
	 */
	public static String FIXED_LIST_MODE_CREATE = "create";

	/**
	 * The static value for editing mode
	 */
	public static String FIXED_LIST_MODE_EDIT = "edit";

	/**
	 * The static value for the dialog of this FixedList
	 */
	public static final int DETAIL_DIALOG_ID = 1;

	private static final String TAG = "MMFixedListView";

	private String dialogClassName;

	private MMFixedListDialogFragment<ITEM, ITEMVM> dialog;

	private int uiCancelButtonId;

	private int uiOkButtonId;

	private int uiOkAndNextButtonId;

	private boolean addNewItem;
	
	private int iLastSelectedItem;

	/**
	 * <p>
	 * Construct an object <em>MMFixedListView</em>.
	 * </p>
	 * 
	 * @param p_oContext
	 *            The application context
	 */
	public MMFixedListView(Context p_oContext) {
		this(p_oContext, null);
	}

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
			this.dialogClassName = p_oAttrs.getAttributeValue(
					Application.MOVALYSXMLNS, "dialogClass");
		}
	}

	/**
	 * TODO Décrire la méthode getDetail de la classe MMFixedListView
	 * 
	 * @return
	 */
	public MasterVisualComponent<?> getDetail() {
		return this.dialog.getDetail();
	}

	/**
	 * {@inheritDoc}
	 */
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

	protected void doOnClickCancelButtonOnItem(View p_oView) {
		super.doCancelItem();
		this.dialog.dismiss();
		this.dialog
		.getDetail()
		.getDescriptor()
		.unInflate(
				String.valueOf(String.valueOf(this.dialog.hashCode())),
				this.dialog.getDetail(), null);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void doOnClickAddButton(ITEMVM p_oItemVm) {
		super.doOnClickAddButton(p_oItemVm);
		Bundle oBundle = new Bundle();
		oBundle.putString("mode", FIXED_LIST_MODE_CREATE);
		oBundle.putBoolean(
				MMFixedListDialogFragment.FIXED_LIST_VIEW_DIALOG_FRAGMENT_READ_MODE_ARGUMENT_KEY,
				!this.isEdit());
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

	@SuppressWarnings("unchecked")
	protected boolean doOnClickValidButtonOnItem(View p_oView) {
		ITEMVM fragmentVMCopy = (ITEMVM) this.dialog.getDetail().getViewModel();
		boolean bValid = doValidItem(this.dialog.isEditing(), this.iLastSelectedItem, fragmentVMCopy);
		if (bValid) {
			this.iLastSelectedItem = -1;
			this.dialog.dismiss();
			this.dialog
			.getDetail()
			.getDescriptor()
			.unInflate(String.valueOf(this.dialog.hashCode()),
					this.dialog.getDetail(), null);
			this.updateTitle();
		}
		return bValid;
	}

	/**
	 * 
	 * @param p_oView
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
				!this.isEdit());

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
	public void destroy() {
		
		if (this.dialog != null && this.dialog.isVisible()) {
			this.dialog.localDestroy();
		}
		
		super.destroy();
	}
	
	/**
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.listener.ConfigurationListener#beforeConfigurationChanged(java.util.Map)
	 */
	@Override
	public void beforeConfigurationChanged(
			Map<String, Object> p_oConfigurationMap) {
		// nothing to do
	}

	/**
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.listener.ConfigurationListener#afterConfigurationChanged(java.util.Map)
	 */
	@Override
	public void afterConfigurationChanged(
			Map<String, Object> p_oConfigurationMap) {
		update();
	}

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
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomConverter customConverter() {
		return this.aivDelegate.customConverter();
	}

	public void setCustomConverter(String p_oCustomConverter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomConverter(p_oCustomConverter,p_oAttributeSet);		
	}

	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		this.aivDelegate.setCustomConverter(p_oConverter);
	}
	
	@Override
	public void setCustomFormatter(CustomFormatter p_oFormatter) {
		this.aivDelegate.setCustomFormatter(p_oFormatter);
	}
	
	@Override
	public CustomFormatter customFormatter() {
		return this.aivDelegate.customFormatter();
	}

	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeSet) {
		this.aivDelegate.setCustomFormatter(p_oCustomFormatter,p_oAttributeSet);		
	}
}
