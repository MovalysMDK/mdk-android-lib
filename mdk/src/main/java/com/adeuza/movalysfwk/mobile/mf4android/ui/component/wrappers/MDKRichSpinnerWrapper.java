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
package com.adeuza.movalysfwk.mobile.mf4android.ui.component.wrappers;

import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.AbstractComponentWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;
import com.sopragroup.mobility.mdk.R;
import com.soprasteria.movalysmdk.widget.core.MDKBaseRichWidget;
import com.soprasteria.movalysmdk.widget.core.MDKBaseWidget;
import com.soprasteria.movalysmdk.widget.core.behavior.HasChangeListener;
import com.soprasteria.movalysmdk.widget.core.behavior.HasDelegate;
import com.soprasteria.movalysmdk.widget.core.behavior.types.HasAdapter;
import com.soprasteria.movalysmdk.widget.core.behavior.HasValidator;
import com.soprasteria.movalysmdk.widget.core.delegate.MDKWidgetDelegate;
import com.soprasteria.movalysmdk.widget.core.listener.ChangeListener;



import java.util.List;
import java.util.Map;

/**
 * <p>
 * Spinner widget used in the Movalys Mobile product for Android
 * </p>
 *
 * @param <ITEM>   a model for item
 * @param <ITEMVM> a view model for item
 */

public class MDKRichSpinnerWrapper<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>> extends AbstractComponentWrapper<View>
        implements AndroidComponentWrapper, ConfigurableVisualComponent,
        VisualComponentDelegate<ITEMVM>, ChangeListener {
    /**
     * framework delegate of the wrapper
     */
    private AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate;


    /**
     * list of components to hide
     */
    private List<ConfigurableVisualComponent> componentsToHide;

    @Override
    public void setComponent(View p_oComponent, boolean p_bIsRestoring) {
        super.setComponent(p_oComponent, p_bIsRestoring);

        this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this,
                new Class[]{AttributeSet.class},
                new Object[]{null});

        if (p_oComponent instanceof HasChangeListener) {
            ((HasChangeListener) p_oComponent).registerChangeListener(this);
        }
		if (p_oComponent instanceof MDKBaseRichWidget) {
			MDKWidgetDelegate delegate = ((HasDelegate)((MDKBaseRichWidget) p_oComponent).getInnerWidget()).getMDKWidgetDelegate();
			// converter
			if (delegate.getAttributeMap().containsKey(R.attr.converter_name)) {
				this.aivFwkDelegate.setCustomConverter((CustomConverter) BeanLoader.getInstance().getBeanByType(delegate.getAttributeMap().getValue(R.attr.converter_name)));
			}
			// formatter
			if (delegate.getAttributeMap().containsKey(R.attr.formatter_name)) {
				this.aivFwkDelegate.setCustomFormatter((CustomFormatter) BeanLoader.getInstance().getBeanByType(delegate.getAttributeMap().getValue(R.attr.formatter_name)));
			}
		}
    }

    @Override
    public void unsetComponent() {
        View v = this.component.get();
        if (v != null && v instanceof HasChangeListener) {
            ((HasChangeListener) v).unregisterChangeListener(this);
        }
    }

    @Override
    public VisualComponentFwkDelegate getComponentFwkDelegate() {
        return this.aivFwkDelegate;
    }

    @Override
    public VisualComponentDelegate getComponentDelegate() {
        return this;
    }

    @Override
    public boolean isNullOrEmptyValue(ITEMVM p_oObject) {
        return p_oObject == null;
    }

    /**
     * To get the adapter frome spinner.
     *
     * @return the adapter
     */
    private BaseAdapter getAdapter() {
        View v = this.component.get();
        if (v != null) {
            if (v instanceof HasAdapter) {
                return ((HasAdapter) v).getAdapter();
            }
        }
        return null;
    }

    /**
     * To get selected item.
     *
     * @return item selected by the user
     */
    private Object getSelectedItem() {
        View v = this.component.get();
        if (v != null) {
            if (v instanceof HasAdapter) {
                return ((HasAdapter) v).getSelectedItem();
            }
        }
        return null;
    }

    /**
     * To get selected item position.
     *
     * @return the position of the item selected by the user
     */
    private int getSelectedItemPosition() {
        View v = this.component.get();
        if (v != null) {
            if (v instanceof HasAdapter) {
                return ((HasAdapter) v).getSelectedItemPosition();
            }
        }
        return -1;
    }

    /**
     * To set the selection a the specific position.
     *
     * @param iPositionSelected the position to set the selection
     */
    private void setSelection(int iPositionSelected) {
        View v = this.component.get();
        if (v != null) {
            if (v instanceof HasAdapter) {
                ((HasAdapter) v).setSelection(iPositionSelected);
            }
        }
    }

    /**
     * To get the adapter position by the object.
     *
     * @param adapter The spinner adapter
     * @param id      The object selected
     * @return the position selected
     */
    private int getAdapterPositionByItem(BaseAdapter adapter, Object id) {
        final int count = adapter.getCount();

        for (int pos = 0; pos < count; pos++) {
            if (id == adapter.getItem(pos)) {
                return pos;
            }
        }
        return -1;
    }

    @Override
    public void configurationSetValue(ITEMVM p_oObjectToSet) {
        this.writingData = true;
        if (p_oObjectToSet != null && !p_oObjectToSet.equals(this.configurationGetValue())
                || p_oObjectToSet == null && this.configurationGetValue() != null) {
            int iPositionSelected = 0;
            if (p_oObjectToSet != null) {
                iPositionSelected = this.getAdapterPositionByItem(this.getAdapter(), p_oObjectToSet);
                if (iPositionSelected == -1) {
                    iPositionSelected = 0;
                }
            }
            this.setSelection(iPositionSelected);
        } else if (p_oObjectToSet == null && this.getSelectedItemPosition() != 0) {
            this.setSelection(0);
        }
    }

    @Override
    public ITEMVM configurationGetValue() {
        return (ITEMVM) this.getSelectedItem();
    }

    @Override
    public Class<?> getValueType() {
        return ViewModel.class;
    }


    @Override
    public boolean isFilled() {
        return this.getSelectedItem() != null;
    }

    @Override
    public void doOnPostAutoBind() {

    }

    @Override
    public void destroy() {
        unsetComponent();
    }


    @Override
    public boolean isAlwaysEnabled() {
        return false;
    }

    @Override
    public void configurationSetMandatoryLabel() {
        View v = this.component.get();
        if (v != null) {
            if (v instanceof MDKBaseWidget) {
                ((MDKBaseWidget) v).setMandatory(true);
            }
        }
    }

    @Override
    public void configurationRemoveMandatoryLabel() {
        View v = this.component.get();
        if (v != null) {
            if (v instanceof MDKBaseWidget) {
                ((MDKBaseWidget) v).setMandatory(false);
            }
        }
    }

    @Override
    public void configurationEnabledComponent() {
        View v = this.component.get();
        if (v != null) {
            v.setEnabled(true);
        }
    }

    @Override
    public void configurationDisabledComponent() {
        View v = this.component.get();
        if (v != null) {
            v.setEnabled(false);
        }
    }

    @Override
    public void configurationSetError(String p_oError) {
        View v = this.component.get();
        if (v != null) {
            if (v instanceof HasValidator) {
                // this cannot work
                ((HasValidator) v).setError(p_oError);
            }
        }
    }

    @Override
    public void configurationUnsetError() {
        View v = this.component.get();
        if (v != null) {
            if (v instanceof HasValidator) {
                ((HasValidator) v).setError("");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configurationPrepareHide(List<ConfigurableVisualComponent> p_oComponentsToHide) {
        this.componentsToHide = p_oComponentsToHide;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configurationHide(boolean p_bLockModifier) {
        if (this.getComponentFwkDelegate() != null &&
                (this.getComponentFwkDelegate().getConfiguration() == null || this.getComponentFwkDelegate().getConfiguration().isVisible())) {

            if (this.componentsToHide != null && (this.getComponentFwkDelegate() != null && this.getComponentFwkDelegate().isValue())) {
                for (ConfigurableVisualComponent oComponent : this.componentsToHide) {
                    oComponent.getComponentDelegate().configurationUnsetError();
                    oComponent.getComponentDelegate().hide();
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configurationUnHide(boolean p_bLockModifier) {
        if (this.getComponentFwkDelegate() != null &&
                (this.getComponentFwkDelegate().getConfiguration() == null || this.getComponentFwkDelegate().getConfiguration().isVisible())) {

            if (this.componentsToHide != null
                    && (this.getComponentFwkDelegate() != null
                    && this.getComponentFwkDelegate().isValue())) {
                for (ConfigurableVisualComponent oComponent : this.componentsToHide) {
                    oComponent.getComponentDelegate().unHide();
                    oComponent.getComponentDelegate().configurationUnsetError();
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide() {
        View v = this.component.get();
        if (v != null) {
            v.setVisibility(View.GONE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unHide() {
        View v = this.component.get();
        if (v != null) {
            v.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean validate(BasicComponentConfiguration p_oConfiguration,
                            Map<String, Object> p_mapParameter, StringBuilder p_oErrorBuilder) {
        View v = this.component.get();
        if (v != null) {
            if (v instanceof HasValidator) {
                return ((HasValidator) v).validate();
            }
        }
        return true;
    }

    @Override
    public void onChanged() {
        if (!this.writingData) {
            this.aivFwkDelegate.changed();
        }
        this.writingData = false;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        return null;
    }

    @Override
    public void onRestoreInstanceState(Parcelable p_oState) {

    }
}
