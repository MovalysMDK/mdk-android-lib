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

import android.net.Uri;
import android.os.Parcelable;
import android.view.View;
import android.util.AttributeSet;

import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.AbstractComponentWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.MDKPresenterSVMImpl;
import com.soprasteria.movalysmdk.widget.core.behavior.HasChangeListener;
import com.soprasteria.movalysmdk.widget.core.behavior.types.HasPresenter;
import com.soprasteria.movalysmdk.widget.core.listener.ChangeListener;

import java.util.List;
import java.util.Map;


/**
 * Wrapper for the MDKPresenterView widget.
 * Used to bind the component to the MDK view model.
 */
public class MDKPresenterViewWrapper extends AbstractComponentWrapper<View>
        implements AndroidComponentWrapper, ConfigurableVisualComponent,
        VisualComponentDelegate<MDKPresenterSVMImpl>, ChangeListener {

    /**
     * Framework delegate of the wrapper.
     */
    private AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate;

    /**
     * List of components to hide.
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
    public MDKPresenterSVMImpl configurationGetValue() {
        MDKPresenterSVMImpl r_Presenter = null;
        View v = this.component.get();
        if (v != null) {
            if (v instanceof HasPresenter) {
                Object[] mdkPresenter = ((HasPresenter) v).getPresenter();
                r_Presenter = new MDKPresenterSVMImpl();
                r_Presenter.setTitle((String) mdkPresenter[0]);
                r_Presenter.setUri((Uri) mdkPresenter[1]);
            }
        }
        return r_Presenter;
    }

    @Override
    public void configurationSetValue(MDKPresenterSVMImpl p_oObjectToSet) {
        this.writingData = true;
        View v = this.component.get();
        if (v != null) {
            if (v instanceof HasPresenter) {
                Object[] oObjectToSet = null;
                if (p_oObjectToSet != null) {
                    oObjectToSet = new Object[2];
                    oObjectToSet[0] = p_oObjectToSet.getTitle();
                    oObjectToSet[1] = p_oObjectToSet.getUri();
                }
                ((HasPresenter) v).setPresenter(oObjectToSet);
            }
        }
        this.writingData = false;
    }

    @Override
    public boolean isNullOrEmptyValue(MDKPresenterSVMImpl p_oObject) {
        return p_oObject == null;
    }

    @Override
    public Class<?> getValueType() {
        return MDKPresenterSVMImpl.class;
    }

    @Override
    public boolean isFilled() {
        View v = this.component.get();
        if (v != null) {
            if (v instanceof HasPresenter) {
                return ((HasPresenter) v).getPresenter() != null;
            }
        }
        return false;
    }

    @Override
    public void doOnPostAutoBind() {
        // nothing to do
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
        // nothing
    }

    @Override
    public void configurationRemoveMandatoryLabel() {
        // nothing
    }

    @Override
    public void configurationEnabledComponent() {
        // nothing
    }

    @Override
    public void configurationDisabledComponent() {
        // nothing
    }

    @Override
    public void configurationSetError(String p_oError) {
        // nothing
    }

    @Override
    public void configurationUnsetError() {
        // nothing
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
    public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameter, StringBuilder p_oErrorBuilder) {
        return false;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        return null;
    }

    @Override
    public void onRestoreInstanceState(Parcelable p_oState) {
        // nothing
    }


    @Override
    public void onChanged() {
        if (!this.writingData) {
            this.aivFwkDelegate.changed();
        }
    }
}
