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

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.abstractviews.MMRecyclableList;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.fixedlist.MDKFixedListConnector;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableFixedListComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMDialogFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.dialog.MMFixedListDialog;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AbstractConfigurableFixedListAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.generic.InParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.NoneType;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;
import com.soprasteria.movalysmdk.widget.fixedlist.FixedListAddListener;
import com.soprasteria.movalysmdk.widget.fixedlist.FixedListItemClickListener;
import com.soprasteria.movalysmdk.widget.fixedlist.FixedListRemoveListener;
import com.soprasteria.movalysmdk.widget.fixedlist.MDKRichFixedList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MMFixedList<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>> extends MDKRichFixedList 
	implements ConfigurableVisualComponent, OnClickListener, OnDismissListener, MMRecyclableList,
		FixedListAddListener, FixedListItemClickListener, FixedListRemoveListener {
	
	/** The static value for creating mode */
	public static final String FIXED_LIST_MODE_CREATE = "create";

	/** The static value for editing mode */
	public static final String FIXED_LIST_MODE_EDIT = "edit";

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/** configurable view delegate */
	private AndroidConfigurableFixedListComponentDelegate<ListViewModel<ITEM,ITEMVM>> aivDelegate = null;
	
	/** fixed list title */
	private String specifiedTitle;
	
	/** the custom dialog class used to display the details of a row */
	private String dialogClassName;
	
	/** the dialog used to display the details of a row */
	private MMFixedListDialog<ITEM, ITEMVM> dialog;

	/** the cancel button id */
	private int uiCancelButtonId;

	/** the validation button id */
	private int uiOkButtonId;

	/** the "validate and add new" button id */
	private int uiOkAndNextButtonId;
	
	/** true if a new item is being added */
	private boolean addNewItem;

	/** Component request code */
	private int requestCode = -1;

	/** true if the process of displaying a dialog is pending. */
	private boolean isDisplayingDialog = false;
	
	@SuppressWarnings("unchecked")
	public MMFixedList(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if (!isInEditMode()){
			
			this.aivDelegate = (AndroidConfigurableFixedListComponentDelegate<ListViewModel<ITEM, ITEMVM>>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{NoneType.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
			
			this.specifiedTitle = this.getInnerWidget().getMDKWidgetDelegate().getLabel().toString();
			
			AndroidApplication androidApplication = (AndroidApplication) Application.getInstance();
			
			this.uiCancelButtonId = androidApplication
					.getAndroidIdByRKey(
							AndroidApplicationR.component__fixedlist_detail_cancel__button);
			this.uiOkButtonId = androidApplication.getAndroidIdByRKey(
					AndroidApplicationR.component__fixedlist_detail_ok__button);
			this.uiOkAndNextButtonId = androidApplication
					.getAndroidIdByRKey(
							AndroidApplicationR.component__fixedlist_detail_okandnext__button);
			this.dialogClassName = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "dialogClass");
		}
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()){
			BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) this.aivFwkDelegate.getConfiguration();
			if (oConfiguration != null && oConfiguration.getLabel() != null) {
				this.specifiedTitle = oConfiguration.getLabel();
			}
		}
	}

	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}

	@Override
	public AndroidConfigurableFixedListComponentDelegate<ListViewModel<ITEM,ITEMVM>> getComponentDelegate() {
		return this.aivDelegate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivFwkDelegate.setId(p_oId);
	}

	@Override
	public void onClick(View p_oView) {
		if (p_oView != null) {
			// we send the click to the MDKFixedList widget
			super.getInnerWidget().onClick(p_oView);
			
			// process the click should it be linked with the dialog buttons
			treatDialogButtons(p_oView);
		}
	}

	@SuppressWarnings("unchecked")
	public MDKFixedListConnector<ITEM, ITEMVM, ListViewModel<ITEM, ITEMVM>> getAdapter() {
		return (MDKFixedListConnector<ITEM, ITEMVM, ListViewModel<ITEM, ITEMVM>>) this.getInnerWidget().getAdapter();
	}

	@Override
	public void setAdapter(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> connector) {
		if (!(connector instanceof MDKFixedListConnector)) {
			throw new MobileFwkException("MMFixedList", "Adapter must be of MDKFixedListConnector kind");
		}
		
		super.setAdapter(connector);
		
		this.getInnerWidget().addAddListener(this);
		this.getInnerWidget().addItemClickListener(this);
		this.getInnerWidget().addRemoveListener(this);
	}
	
	public boolean getTitleMode() {
		return this.aivDelegate.isTitleMode();
	}

	public int getItemCount() {
		return this.getInnerWidget().getAdapter().getItemCount();
	}

	public boolean getDisplayDetail() {
		return this.aivDelegate.isDisplayDetail();
	}

	public void removeItems() {
		// unregister the observer on the view model
		this.getAdapter().beforeViewModelChanged();
		// uninflate the views
		this.getAdapter().getAdapter().uninflate();
	}

	public void setMasterVM(ListViewModel<ITEM, ITEMVM> p_oMasterVM) {
		this.getAdapter().setMasterVM(p_oMasterVM);
	}

	public void updateTitle() {
		int iPos = 0;

		if (this.getAdapter() != null && this.getAdapter().getMasterVM() != null) {
			iPos = this.getAdapter().getMasterVM().getCount() ;
		}

		this.getInnerWidget().getMDKWidgetDelegate().setLabel(this.computeTitle(this.specifiedTitle, iPos));
	}

	/**
	 * Construct the title
	 * @param p_sDefaultTitle the default title
	 * @param p_iCount the item count
	 * @return the updated title
	 */
	protected String computeTitle(String p_sDefaultTitle, int p_iCount) {
		return new StringBuilder(p_sDefaultTitle).append(" (").append(p_iCount).append(')').toString();
	}
	
	/**
	 * called when the delete button it clicked
	 * @param oItemVM the viewmodel of the the clicked view
	 */
	protected void doOnClickDeleteButtonOnItem(ITEMVM oItemVM) {
		if (oItemVM != null) {
			this.getAdapter().getMasterVM().remove(oItemVM);
			this.updateTitle();
			this.getAdapter().getMasterVM().setDirectlyModified(true);
		}
	}
	
	/**
	 * Customize custom button
	 * @param p_oView the view of the button
	 * @return true if the custom button is treated
	 */
	@SuppressWarnings("unchecked")
	public boolean treatDialogButtons(View p_oView) {
		boolean r_bTreat = false;

		if( p_oView.getTag() instanceof MMDialogFragment) {
			this.dialog = (MMFixedListDialog<ITEM, ITEMVM>) p_oView.getTag();

			int iViewId = p_oView.getId();

			if (this.uiCancelButtonId == iViewId) {
				this.doOnClickCancelButtonOnItem(p_oView);
				r_bTreat = true;
			} else if (this.uiOkButtonId == iViewId) {
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
		this.getAdapter().resetSelectedItem();
		this.dialog.dismiss();
		this.dialog = null;
	}

	protected void doOnClickAddButton(ITEMVM p_oItemVm) {
		if ( p_oItemVm != null ) {
			this.getAdapter().setSelectedItem(p_oItemVm);
		} else {
			this.getAdapter().resetSelectedItem();
		}

        displayDialog(FIXED_LIST_MODE_CREATE);
	}

	/**
	 * Returns the AppCompatActivity context
	 * @return a context
	 */
	private AppCompatActivity getFragmentActivityContext() {
		return  (AppCompatActivity) ((AndroidApplication) Application.getInstance()).getCurrentActivity();
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

	protected void doOnClickSelectItem(ITEMVM p_oItemVM) {
		this.getAdapter().setSelectedItem(p_oItemVM);

        displayDialog(FIXED_LIST_MODE_EDIT);
	}

    /**
     * Displays a new dialog.
	 * @param mode the mode of the dialog (create / edit)
     */
    private void displayDialog(String mode) {
		this.isDisplayingDialog = true;

        Bundle oBundle = new Bundle();
        oBundle.putString("mode", mode);
        oBundle.putBoolean(
                MMFixedListDialog.FIXED_LIST_VIEW_DIALOG_FRAGMENT_READ_MODE_ARGUMENT_KEY,
                !this.aivFwkDelegate.isEdit());

        destroyDialog();

        this.dialog = (MMFixedListDialog<ITEM, ITEMVM>) createDialogFragment(oBundle);

        this.dialog.show(getFragmentActivityContext()
                .getSupportFragmentManager(), this.dialog
                .getFragmentDialogTag());

		this.isDisplayingDialog = false;
    }

    /**
	 * Called when the valid button is clicked.
	 * @param p_oView the view of the clicked button
	 * @return true if the item was validated regarding the view model
	 */
	@SuppressWarnings("unchecked")
	protected boolean doOnClickValidButtonOnItem(View p_oView) {
		ITEMVM fragmentVMCopy = (ITEMVM) this.dialog.getDetail().getViewModel();
		boolean bValid = doValidItem(this.dialog.isEditing(), this.getAdapter().getSelectedPosition(), fragmentVMCopy);
		if (bValid) {
			this.getAdapter().resetSelectedItem();
            destroyDialog();
			this.updateTitle();
		}
		return bValid;
	}

    /**
     * destroys the currently displayed dialog.
     */
    private void destroyDialog() {
        if (this.dialog != null) {
            this.dialog.dismiss();
            if (this.dialog.getDetail() != null) {
                this.dialog.getDetail().getDescriptor().unInflate(
                        String.valueOf(this.dialog.hashCode()),
                        this.dialog.getDetail(), null);
            }
            this.dialog = null;
        }
    }

    /**
	 * Valid a item of the FixedList
	 * @param p_bIsEditing is the item is in edit mode
	 * @param p_iIndexOfItem the index of the item
	 * @param p_oViewModel the ITEMVM to valid
	 * @return true if the item is valid
	 */
	protected boolean doValidItem(boolean p_bIsEditing, int p_iIndexOfItem, ITEMVM p_oViewModel) {
		Map<String, Object> mapParameters = new HashMap<>();
		mapParameters.put(AbstractConfigurableFixedListAdapter.SELECTED_VM, this.getAdapter().getSelectedItem());
		
		boolean bValidItem = !p_oViewModel.validComponents(null, mapParameters);

		if (bValidItem) {
			this.getAdapter().setSelectedPosition(p_iIndexOfItem);

			if (!p_bIsEditing) {
				this.getAdapter().add(p_oViewModel);
			} else {
				
				if (p_iIndexOfItem == -1) {
					Log.e("MMFixedListView", "The item has not been updated because it was not found in the adapter");
					return false;
				}
				
				this.getAdapter().getMasterVM().update(p_iIndexOfItem, p_oViewModel);
			}
			if(p_oViewModel.isDirectlyModified()){
				this.getAdapter().getMasterVM().setDirectlyModified(true);
			}
			this.updateTitle();

			InParameter oActionParameters = new InParameter();
			oActionParameters.vm = this.getAdapter().getSelectedItem();
		}
		return bValidItem;
	}
	
	public void onDismiss(DialogInterface p_oDialog) {
		if (this.addNewItem) {
			this.doOnClickAddButton(null);
			this.addNewItem = false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public MMDialogFragment createDialogFragment(Bundle p_oParameters) {
		try {
			if (this.dialogClassName == null) {
				this.dialog = MMFixedListDialog.newInstance(this);
				prepareDialogFragment(this.dialog, p_oParameters);
			} else {
				Class<?> oDialogClass = Class.forName(this.dialogClassName);
				Method m = oDialogClass.getMethod("newInstance", OnDismissListener.class);
				this.dialog = (MMFixedListDialog<ITEM, ITEMVM>) m.invoke(null, this);
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

	public void prepareDialogFragment(MMDialogFragment p_oDialog,
			Bundle p_oDialogFragmentArguments) {
		p_oDialog.setArguments(p_oDialogFragmentArguments);
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Bundle oToSave = new Bundle();
		oToSave.putParcelable("parent", super.onSaveInstanceState());
		oToSave.putString("class", MMFixedList.class.getName());
		oToSave.putInt("lastSelectedItem", this.getAdapter().getSelectedPosition());
		oToSave.putInt("requestCode", this.requestCode);
		oToSave.putString("specifiedTitle", this.specifiedTitle);
		return oToSave;
	}

	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		if (!(p_oState instanceof Bundle)) {
			super.onRestoreInstanceState(p_oState);
			return;
		}
		
		Bundle savedState = (Bundle) p_oState;
		String sClass = savedState.getString("class");
		if (!MMFixedList.class.getName().equals(sClass)) {
			super.onRestoreInstanceState(p_oState);
			return;
		}

		super.onRestoreInstanceState(savedState.getParcelable("parent"));

		this.getAdapter().setSelectedPosition(savedState.getInt("lastSelectedItem"));
		this.requestCode = savedState.getInt("requestCode");
		this.specifiedTitle = savedState.getString("specifiedTitle");
		AbstractMMActivity activity = (AbstractMMActivity) this.getContext();
		activity.replayOnActivityResults();
		
		this.updateTitle();
	}

	@Override
	public void onAddClick() {
		doOnClickAddButton(null);
	}

	@Override
	public void onItemClick(int position) {
		if (!isDisplayingDialog) {
			this.getAdapter().setSelectedPosition(position);
			doOnClickSelectItem(this.getAdapter().getSelectedItem());
		}
	}

	@Override
	public void onRemoveItemClick(int position) {
		this.getAdapter().setSelectedPosition(position);
		doOnClickDeleteButtonOnItem(this.getAdapter().getSelectedItem());
	}
	
}
