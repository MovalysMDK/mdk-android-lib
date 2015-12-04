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
package com.adeuza.movalysfwk.mobile.mf4android.ui.adapters;

import java.util.HashSet;
import java.util.Set;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AdapterLevel;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ConfigurableListViewHolder;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMOnItemClickListener;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPerformItemClickView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.ComponentWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;

public class MDKSpinnerAdapter<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>, LISTVM extends ListViewModel<ITEM, ITEMVM>>
        extends MDKBaseAdapter implements MMOnItemClickListener {

    /**
     * the list model
     */
    private LISTVM masterVM = null;

    /**
     * android id of layout (the name of file) for item
     */
    private int spinnerItemLayoutId = 0;

    /**
     * android id of configurable layout for the drop down list
     */
    private int spinnerDropDownListLayoutId = 0;

    /**
     * android id of configurable view in the dropdown layout
     */
    private int configurableMasterDropDowId = 0;

    /**
     * android id of configurable view for the item
     */
    private int configurableMasterItemId = 0;

    /**
     * the component for selection
     */
    private int selectedComponent = -1;

    /**
     * the component for error
     */
    private int errorComponent = -1;

    /**
     * <code>true</code> if an empty value must be added by this adapter.
     */
    private boolean useEmptyValue = true;

    /**
     * the selected position
     */
    private int selectedPosition = -1;

    private String errorMessage;

    /**
     * Inflated components.
     */
    protected Set<MasterVisualComponent> components;

    private OnItemClickListener listener;

    /**
     * Constructs a new ConfigurableAdapter
     *
     * @param p_oMasterVM                    list's view model
     * @param p_iSpinnerItemLayoutId         id of layout (the file name) of the spinner item
     * @param p_iConfigurableMasterItemId    id of configurable layout (the first component) in this spinner item layout. This must be a masterRelativeLayout
     * @param p_iSpinnerDropDownListLayoutId id of the layout (the file name) of the spinner drop down list
     * @param p_iConfigurableMasterDropDowId id of the configurable layout (the first component) in this drop down spinner list. This must be a masterRelativeLayout
     */
    public MDKSpinnerAdapter(LISTVM p_oMasterVM,
                             int p_iSpinnerItemLayoutId, int p_iConfigurableMasterItemId,
                             int p_iSpinnerDropDownListLayoutId,
                             int p_iConfigurableMasterDropDowId) {

        this(p_oMasterVM, p_iSpinnerItemLayoutId, p_iConfigurableMasterItemId,
                p_iSpinnerDropDownListLayoutId, p_iConfigurableMasterDropDowId,
                -1, -1, true);
    }

    /**
     * Constructs a new ConfigurableAdapter
     *
     * @param p_oMasterVM                    list's view model
     * @param p_iSpinnerItemLayoutId         id of layout (the file name) of the spinner item
     * @param p_iConfigurableMasterItemId    id of configurable layout (the first component) in this spinner item layout. This must be a masterRelativeLayout
     * @param p_iSpinnerDropDownListLayoutId id of the layout (the file name) of the spinner drop down list
     * @param p_iConfigurableMasterDropDowId id of the configurable layout (the first component) in this drop down spinner list. This must be a masterRelativeLayout
     * @param p_iSelectedComponent           id of component to set selected
     * @param p_iErrorComponent              id of component to set error
     * @param p_bUseEmptyValue               <code>true</code> if an empty value must be added into the first position.
     */
    public MDKSpinnerAdapter(LISTVM p_oMasterVM,
                             int p_iSpinnerItemLayoutId, int p_iConfigurableMasterItemId,
                             int p_iSpinnerDropDownListLayoutId,
                             int p_iConfigurableMasterDropDowId, int p_iSelectedComponent,
                             int p_iErrorComponent, boolean p_bUseEmptyValue) {

        super();

        this.masterVM = p_oMasterVM;
        this.spinnerItemLayoutId = p_iSpinnerItemLayoutId;
        this.spinnerDropDownListLayoutId = p_iSpinnerDropDownListLayoutId;
        this.configurableMasterDropDowId = p_iConfigurableMasterDropDowId;
        this.configurableMasterItemId = p_iConfigurableMasterItemId;

        this.selectedComponent = p_iSelectedComponent;
        this.errorComponent = p_iErrorComponent;

        this.useEmptyValue = p_bUseEmptyValue;

        this.components = new HashSet<>();
    }

    /**
     * Constructs a new ConfigurableAdapter
     *
     * @param p_oMasterVM                    list's view model
     * @param p_iSpinnerItemLayoutId         id of layout (the file name) of the spinner item
     * @param p_iConfigurableMasterItemId    id of configurable layout (the first component) in this spinner item layout. This must be a masterRelativeLayout
     * @param p_iSpinnerDropDownListLayoutId id of the layout (the file name) of the spinner drop down list
     * @param p_iConfigurableMasterDropDowId id of the configurable layout (the first component) in this drop down spinner list. This must be a masterRelativeLayout
     * @param p_iSelectedComponent           id of component to set selected
     * @param p_iErrorComponent              id of component to set error
     */
    public MDKSpinnerAdapter(LISTVM p_oMasterVM,
                             int p_iSpinnerItemLayoutId, int p_iConfigurableMasterItemId,
                             int p_iSpinnerDropDownListLayoutId,
                             int p_iConfigurableMasterDropDowId, int p_iSelectedComponent,
                             int p_iErrorComponent) {
        this(
                p_oMasterVM,
                p_iSpinnerItemLayoutId,
                p_iConfigurableMasterItemId,
                p_iSpinnerDropDownListLayoutId,
                p_iConfigurableMasterDropDowId,
                p_iSelectedComponent,
                p_iErrorComponent,
                false);

    }

    @Override
    public View getLegacyViewByLevel(AdapterLevel p_oLevel, boolean p_bExpanded, View p_oView,
                                     ViewGroup p_oViewGroup, int p_iViewType, int... p_iPositions) {
        return this.mAdapterDelegate.getViewByLevel(p_oLevel, p_bExpanded, p_oView, p_oViewGroup, p_iViewType, p_iPositions);
    }

    /**
     * Creates and return a ConfigurableListViewHolder.
     *
     * @return An empty ConfigurableListViewHolder object.
     */
    public ConfigurableListViewHolder createViewHolder(View p_oView) {
        return (ConfigurableListViewHolder) BeanLoader.getInstance().instantiatePrototypeFromConstructor("ConfigurableListViewHolder",
                new Class[]{View.class},
                new Object[]{p_oView});
    }

    /**
     * Creates a DropDownViewHolder
     *
     * @return an empty dropdown view holder
     */
    public ConfigurableListViewHolder createDropDownViewHolder(View p_oView) {
        return (ConfigurableListViewHolder) BeanLoader.getInstance().instantiatePrototypeFromConstructor("ConfigurableListViewHolder",
                new Class[]{View.class},
                new Object[]{p_oView});
    }

    public LISTVM getMasterVM() {
        return this.masterVM;
    }

    /**
     * Sets the master viewmodel of the spinner
     *
     * @param p_oMasterVM the master viewmodel to set
     */
    public void setMasterVM(LISTVM p_oMasterVM) {
        this.masterVM = p_oMasterVM;
        this.selectedPosition = -1;
        this.notifyDataSetChanged();
    }

    /**
     * @return the components
     */
    public Set<MasterVisualComponent> getComponents() {
        return components;
    }

    /**
     * Return the object <em>selectedComponent</em>.
     *
     * @return Objet selectedComponent
     */
    public int getSelectedComponent() {
        return this.selectedComponent;
    }

    public void setSelectedComponent(int p_iSelectedComponent) {
        this.selectedComponent = p_iSelectedComponent;
    }

    public int getErrorComponent() {
        return this.errorComponent;
    }

    public void setErrorComponent(int p_iErrorComponent) {
        this.errorComponent = p_iErrorComponent;
    }

    /**
     * Return the layout id of the <em>spinnerDropDowListLayoutId</em>.
     *
     * @return the layout id
     */
    public int getSpinnerDropDownListLayoutId() {
        return this.spinnerDropDownListLayoutId;
    }

    /**
     * Return the layout id of the <em>configurableMasterDropDowId</em>.
     *
     * @return the layout id
     */
    public int getConfigurableMasterDropDowId() {
        return this.configurableMasterDropDowId;
    }

    /**
     * Returns the item layout for the given view type
     *
     * @param p_iViewType the view type to look for
     * @return the item layout
     */
    public int getItemLayout(int p_iViewType) {
//		int r_ViewId = -1;
//		if (p_iViewType == 0) {
//			r_ViewId = ((AndroidApplication) Application.getInstance()).getAndroidIdByRKey(AndroidApplicationR.component__simple_spinner_emptyitem__value);
//		} else {
//			r_ViewId =  this.spinnerItemLayoutId;
//		}
//		return r_ViewId;
        return this.spinnerItemLayoutId;
    }

    /**
     * Return the layout id of the <em>spinnerDropDowListLayoutId</em>.
     *
     * @return the layout id
     */
    public int getSpinnerDropDowListLayoutId(int p_iViewType) {
        return this.spinnerDropDownListLayoutId;
    }

    /**
     * Returns true if the spinner displays a selectable empty item
     *
     * @return true if the spinner has an empty item
     */
    public boolean hasEmptyItem() {
        return this.useEmptyValue;
    }

    /**
     * Sets whether the spinner should display an empty item
     *
     * @param p_bEmptyValue true if the spinner should display an empty item
     */
    public void enableEmptyItem(boolean p_bEmptyValue) {
        this.useEmptyValue = p_bEmptyValue;
    }

    /**
     * Returns the item layout for the given position
     *
     * @param p_iPosition the position to use
     * @return the item layout
     */
    public int getSpinnerItemLayoutId() {
        return this.spinnerItemLayoutId;
    }

    /**
     * Returns the selected position
     *
     * @return the selected position
     */
    public int getSelectedPosition() {
        return this.selectedPosition;
    }

    public void setSelectedPosition(int p_iPosition) {
        this.selectedPosition = p_iPosition;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * Sets the error message on the spinner
     *
     * @param p_sError the error message to set
     */
    public void setErrorMessage(String p_sError) {
        this.errorMessage = p_sError;
    }

    public int indexOf(ITEMVM p_oItem) {
        int iNdexOf = 0;
        if (this.hasEmptyItem()) {
            iNdexOf = 1;
        }
        return this.masterVM.indexOf(p_oItem) + iNdexOf;
    }

    @Override
    public void resetSelectedItem() {
        // TODO Auto-generated method stub

    }

    public ITEMVM getItem(int p_iParamInt) {
        ITEMVM r_oItem = null;

        int iIndex = p_iParamInt;
        if (this.hasEmptyItem()) {
            iIndex--;
        }
        if (iIndex > -1) {
            r_oItem = this.masterVM.getCacheVMByPosition(iIndex);
        }
        return r_oItem;
    }

    public long getItemId(int p_iPosition) {
        return p_iPosition;
    }

    public ITEMVM getSelectedItem() {
        int iRealSelectedPosition = this.selectedPosition;
        if (this.hasEmptyItem()) {
            iRealSelectedPosition--;
        }
        if (iRealSelectedPosition > -1) {
            return this.masterVM.getCacheVMByPosition(iRealSelectedPosition);
        }
        return null;
    }

    public int getCount() {
        int iIndex = 0;
        if (this.hasEmptyItem()) {
            iIndex = 1;
        }
        return this.masterVM.getCount() + iIndex;
    }

    /**
     * Called to uninflate the MDK component
     */
    public void uninflate() {
        for (MasterVisualComponent oCompositeComponent : this.components) {
            oCompositeComponent.getDescriptor().unInflate(null, oCompositeComponent, null);
        }
        this.resetComponentTags();
        this.components.clear();
    }

    /**
     * Called to reset the view model tags on the MDK component
     */
    protected void resetComponentTags() {
        View oView = null;

        for (MasterVisualComponent oComponent : this.components) {

            if (oComponent instanceof ComponentWrapper) {
                oView = ((ComponentWrapper) oComponent).getComponent();
            } else {
                oView = (View) oComponent;
            }

            ((ConfigurableListViewHolder) oView.getTag()).setViewModelID(null);
        }
    }

    public int getConfigurableMasterItemId() {
        return configurableMasterItemId;
    }

    public void setConfigurableMasterItemId(int configurableMasterItemId) {
        this.configurableMasterItemId = configurableMasterItemId;
    }

    public OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(View p_oView, int p_iPosition, long p_iId, MMPerformItemClickView p_oListView,
                            ConfigurableListViewHolder p_oViewHolder) {
        if (this.listener != null) {
            this.listener.onItemClick(null, p_oView, p_iPosition, p_iId);
        }
    }

    /**
     * This method can be overriden to wrap the components in the
     * MasterVisualCommponent Child item
     *
     * @param p_oView               the current view
     * @param p_oCurrentViewModel   The view Model of this child item
     * @param p_iParamGroupPosition the position of the current view in the list
     * @param p_iParamChildPosition the position of the current child view in the list
     */
    public void postInflateDropDownView(View p_oView) {
        // nothing to do in this implementation, the automatic mapping is used
    }

    /**
     * @param p_oView
     * @param p_oCurrentViewModel
     * @param p_iParamGroupPosition
     * @param p_iParamChildPosition
     */
    public void postBindDropDownView(View p_oView, ITEMVM p_oCurrentViewModel, int p_iPosition) {
        // nothing to do in this implementation, the automatic mapping is used
    }

    /**
     * @param p_oView
     * @param p_bExpanded
     */
    public void postInflateView(View p_oView) {
        // nothing to do in this implementation, the automatic mapping is used
    }

    /**
     * @param p_oView
     * @param p_oCurrentViewModel
     * @param p_iParamGroupPosition
     * @param p_bExpanded
     */
    public void postBindView(View p_oView, ITEMVM p_oCurrentViewModel, int p_iPosition) {
        // nothing to do in this implementation, the automatic mapping is used
    }
}
