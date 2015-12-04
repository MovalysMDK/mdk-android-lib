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

import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.StringToEmailVOConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.AbstractComponentWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.EMailSVMImpl;
import com.soprasteria.movalysmdk.widget.core.MDKBaseRichWidget;
import com.soprasteria.movalysmdk.widget.core.MDKBaseWidget;
import com.soprasteria.movalysmdk.widget.core.R;
import com.soprasteria.movalysmdk.widget.core.behavior.HasDelegate;
import com.soprasteria.movalysmdk.widget.core.behavior.HasTextWatcher;
import com.soprasteria.movalysmdk.widget.core.behavior.HasValidator;
import com.soprasteria.movalysmdk.widget.core.behavior.types.HasEmail;
import com.soprasteria.movalysmdk.widget.core.delegate.MDKWidgetDelegate;

/**
 * Wrapper for the MDKRichEmail widget.
 * Used to bind the component to the MDK view model
 */
public class MDKRichEmailWidgetWrapper extends AbstractComponentWrapper<View>
        implements AndroidComponentWrapper, ConfigurableVisualComponent,
        VisualComponentDelegate<EMailSVMImpl>, TextWatcher {

    /** framework delegate of the wrapper */
    private AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate;

    /** list of components to hide */
    private List<ConfigurableVisualComponent> componentsToHide;

    /** DELAY used before value update in viewmodel */
    private static final int UPDATEVM_DELAY = 200;

    /** identifier of the message "text changed" */
    private static final int MESSAGE_TEXT_CHANGED = 0;

    /**
     * Handler for text change and send message to ViewModel
     */
    private Handler mDelayTextChangedHandler = new Handler() {
        @Override
        public void handleMessage(Message p_oMsg) {
            if (p_oMsg.what == MESSAGE_TEXT_CHANGED) {
                MDKRichEmailWidgetWrapper.this.aivFwkDelegate.changed();
            }
        }
    };

    @Override
    public void setComponent(View p_oComponent, boolean p_bIsRestoring) {
        super.setComponent(p_oComponent, p_bIsRestoring);

        this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this,
                new Class[]{AttributeSet.class},
                new Object[]{null});

        this.aivFwkDelegate.setCustomConverter(new StringToEmailVOConverter());

        if (p_oComponent instanceof HasTextWatcher) {
            ((HasTextWatcher) p_oComponent).addTextChangedListener(this);
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
        if (v != null && v instanceof HasTextWatcher) {
            ((HasTextWatcher) v).removeTextChangedListener(this);
        }
    }

    @Override
    public VisualComponentFwkDelegate getComponentFwkDelegate() {
        return this.aivFwkDelegate;
    }

    @Override
    public VisualComponentDelegate<EMailSVMImpl> getComponentDelegate() {
        return this;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        return null;
    }

    @Override
    public void onRestoreInstanceState(Parcelable p_oState) {

    }

    @Override
    public void configurationSetValue(EMailSVMImpl p_oObjectToSet) {
        this.writingData = true;
        View v = this.component.get();
        if (v != null) {
            if (v instanceof HasEmail) {
                String[] oObjectToSet = null;
                if (p_oObjectToSet != null) {
                    oObjectToSet = new String[5];
                    oObjectToSet[0] = p_oObjectToSet.to;
                    oObjectToSet[1] = p_oObjectToSet.cC;
                    oObjectToSet[2] = p_oObjectToSet.bCC;
                    oObjectToSet[3] = p_oObjectToSet.object;
                    oObjectToSet[4] = p_oObjectToSet.body;
                }
                ((HasEmail) v).setEmail(oObjectToSet);
            }
        }
        this.writingData = false;
    }

    @Override
    public Class<?> getValueType() {
        return EMailSVMImpl.class;
    }

    @Override
    public EMailSVMImpl configurationGetValue() {
        EMailSVMImpl r_Email = null;
        View v = this.component.get();
        if (v != null) {
            if (v instanceof HasEmail) {
                String[] sEmailDetail = ((HasEmail) v).getEmail();

                r_Email = new EMailSVMImpl();
                r_Email.to = sEmailDetail[0];
                r_Email.cC = sEmailDetail[1];
                r_Email.bCC = sEmailDetail[2];
                r_Email.object = sEmailDetail[3];
                r_Email.body = sEmailDetail[4];
            }
        }
        return r_Email;
    }

    @Override
    public boolean isNullOrEmptyValue(EMailSVMImpl p_oObject) {
        return p_oObject == null;
    }

    @Override
    public boolean isFilled() {
        View v = this.component.get();
        if (v != null) {
            if (v instanceof HasEmail) {
                return ((HasEmail) v).getEmail() != null;
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
    public void afterTextChanged(Editable arg0) {
        // nothing
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // nothing
    }

    @Override
    public void onTextChanged(CharSequence p_oText, int arg1, int arg2, int arg3) {
        if (!this.writingData) {
            mDelayTextChangedHandler.removeMessages(MESSAGE_TEXT_CHANGED);
            final Message msg = Message.obtain(mDelayTextChangedHandler, MESSAGE_TEXT_CHANGED, p_oText);
            mDelayTextChangedHandler.sendMessageDelayed(msg, UPDATEVM_DELAY);
        }
    }

}
