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
package com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentBindDestroy;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentError;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentMandatoryLabel;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentNullOrEmpty;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentValidator;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWriteOnly;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.ErrorDefinition;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.AbstractConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.ComponentWrapper;

/**
 * AndroidConfigurableVisualComponentDelegate
 * 
 * <p>
 * This class defines all the default behavior of a component.
 * For each type of behavior, an interface is linked and can be implemented in a component.
 * The interface method will be called in addition to the default delegate behavior.<br/>
 * For example:
 * in <code>configurationSetValue</code>, the default behavior is to call the hide/unhide method on the component to
 * "auto-hide" this component in case the component is empty. In addition to that, it checks if the component implements 
 * <code>ComponentGetSetAndFilled</code> interface, in that case it calls the method <code>configurationSetValue</code> 
 * on this component.
 * </p>
 *
 * @param <VALUE> the type of data handled by the delegate/component
 */
public class AndroidConfigurableVisualComponentDelegate<VALUE> extends AbstractConfigurableVisualComponentDelegate<VALUE> {
	/** component view */
	protected View currentView;
	/** auto-hide the component in case it empty */
	protected boolean autoHide;
	/** framework is writing data */
	protected boolean writingData = false;
	/**
	 * Component is locked when its configuration hides it. 
	 * It is impossible do display (unhide) component when it's locked
	 */
	protected List<ConfigurableVisualComponent> componentsToHide = null;

	/** component is lock when its configuration hide the component. It's impossible do display (unhide) component when it'is lock */
	protected boolean lock = false;
	
	/**
	 * Default constructor of AndroidConfigurableVisualComponentDelegate
	 * @param p_oCurrentView the view link to this delegate
	 * @param p_oDelegateType the type handled by the component
	 */
	public AndroidConfigurableVisualComponentDelegate(ConfigurableVisualComponent p_oCurrentView, Class<?> p_oDelegateType) {
		super(p_oCurrentView, p_oDelegateType);
		
		if (p_oCurrentView instanceof ComponentWrapper) {
			this.currentView = ((ComponentWrapper) p_oCurrentView).getComponent();
		} else {
			this.currentView = (View) p_oCurrentView;
		}
	}
	
	/**
	 * Default constructor of AndroidConfigurableVisualComponentDelegate
	 * @param p_oCurrentView the view link to this delegate
	 * @param p_oDelegateType the type handled by the component
	 * @param p_oAttrs the view AttributeSet (define in layout XML)
	 */
	public AndroidConfigurableVisualComponentDelegate(ConfigurableVisualComponent p_oCurrentView,  Class<?> p_oDelegateType, AttributeSet p_oAttrs) {
		this(p_oCurrentView, p_oDelegateType);
		
		if (p_oAttrs != null) {
			this.autoHide = p_oAttrs.getAttributeBooleanValue(Application.MOVALYSXMLNS, "auto-hide", true);
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
		if (this.currentCvComponent.getComponentFwkDelegate() != null && 
				(this.currentCvComponent.getComponentFwkDelegate().getConfiguration()==null || this.currentCvComponent.getComponentFwkDelegate().getConfiguration().isVisible()) ) {
			if (p_bLockModifier) {
				this.lock = true;
			}
			if (this.componentsToHide != null && (this.currentCvComponent.getComponentFwkDelegate() != null && this.currentCvComponent.getComponentFwkDelegate().isValue())) {
				for (ConfigurableVisualComponent oComponent : this.componentsToHide) {
					oComponent.getComponentDelegate().configurationUnsetError();
					
					View oComponentView = null;
					if (oComponent instanceof ComponentWrapper) {
						oComponentView = ((ComponentWrapper) oComponent).getComponent();
					} else {
						oComponentView = (View) oComponent;
					}
					oComponentView.setVisibility(View.GONE);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationUnHide(boolean p_bLockModifier) {
		if (this.currentCvComponent.getComponentFwkDelegate() != null && 
				(this.currentCvComponent.getComponentFwkDelegate().getConfiguration()==null || this.currentCvComponent.getComponentFwkDelegate().getConfiguration().isVisible()) ) {
			if (p_bLockModifier) {
				this.lock = false;
			}
			if (!this.lock && this.componentsToHide != null && (this.currentCvComponent.getComponentFwkDelegate() != null && this.currentCvComponent.getComponentFwkDelegate().isValue())) {
				for (ConfigurableVisualComponent oComponent : this.componentsToHide) {
					View oComponentView = null;
					if (oComponent instanceof ComponentWrapper) {
						oComponentView = ((ComponentWrapper) oComponent).getComponent();
					} else {
						oComponentView = (View) oComponent;
					}
					oComponentView.setVisibility(View.VISIBLE);
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
		if (this.currentCvComponent.getComponentFwkDelegate().getConfiguration() == null || this.currentCvComponent.getComponentFwkDelegate().getConfiguration().isVisible()) {
			this.currentView.setVisibility(View.GONE);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unHide() {
		if (this.currentCvComponent.getComponentFwkDelegate().getConfiguration()==null || this.currentCvComponent.getComponentFwkDelegate().getConfiguration().isVisible()) {
			this.currentView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void configurationDisabledComponent() {
		this.currentView.setEnabled(false);
		this.currentView.clearFocus();
		if (this.currentCvComponent instanceof ComponentEnable) {
			((ComponentEnable) this.currentCvComponent).enableComponent(false);
		}
	}

	@Override
	public void configurationEnabledComponent() {
		this.currentView.setEnabled(true);
		if (this.currentCvComponent instanceof ComponentEnable) {
			((ComponentEnable) this.currentCvComponent).enableComponent(true);
		}
	}
	
	@Override
	public void doOnPostAutoBind() {
		if (this.currentCvComponent instanceof ComponentBindDestroy) {
			((ComponentBindDestroy) this.currentCvComponent).doOnPostAutoBind();
		}
	}
	
	@Override
	public void destroy() {
		this.componentsToHide = null;
		if (this.currentCvComponent instanceof ComponentBindDestroy) {
			((ComponentBindDestroy) this.currentCvComponent).destroy();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void configurationSetValue(VALUE p_oObjectToSet) {
		
		this.writingData = true;
		
		this.currentCvComponent.getComponentDelegate().configurationUnsetError();
		if (!this.currentCvComponent.getComponentFwkDelegate().hasRules()) {
			if (!this.currentCvComponent.getComponentFwkDelegate().isEdit() || !this.currentView.isEnabled()) {
				if (this.isNullOrEmptyValue(p_oObjectToSet)) {
					this.configurationHide(false);
				} else {
					this.configurationUnHide(false);
				}
			} else {
				this.configurationUnHide(false);
			}
		}
		
		if (this.currentCvComponent instanceof ComponentWriteOnly<?>) {
			((ComponentWriteOnly<VALUE>) this.currentCvComponent).configurationSetValue(p_oObjectToSet);
		} else if (this.currentCvComponent instanceof ComponentReadableWrapper<?>) {
			((ComponentReadableWrapper<VALUE>) this.currentCvComponent).configurationSetValue(p_oObjectToSet);
		}
		
		this.writingData = false;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public VALUE configurationGetValue() {
		if (this.currentCvComponent instanceof ComponentWritableWrapper<?>) {
			return ((ComponentWritableWrapper<VALUE>) this.currentCvComponent).configurationGetValue();
		}
		return super.configurationGetValue();
	}
	
	@Override
	public Class<?> getValueType() {
		return this.delegateType;
	}

	@Override
	public void configurationUnsetError() {
		this.setError(null);
	}

	@Override
	public void configurationSetError(String p_oError) {
		this.setError(p_oError);
	}
	
	@Override
	public void configurationSetMandatoryLabel() {
		super.configurationSetMandatoryLabel();
		if (this.currentCvComponent instanceof ComponentMandatoryLabel) {
			((ComponentMandatoryLabel) this.currentCvComponent).setMandatoryLabel(true);
		}
	}
	
	@Override
	public void configurationRemoveMandatoryLabel() {
		if (this.currentCvComponent instanceof ComponentMandatoryLabel) {
			((ComponentMandatoryLabel) this.currentCvComponent).setMandatoryLabel(false);
		}
		super.configurationRemoveMandatoryLabel();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isFilled() {
		boolean r_bFilled = false;
		if (this.currentCvComponent instanceof ComponentWritableWrapper<?>) {
			r_bFilled  = ((ComponentWritableWrapper<VALUE>) this.currentCvComponent).isFilled();
		}
		return super.isFilled() || r_bFilled;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isNullOrEmptyValue(VALUE p_oObject) {
		boolean r_bNullOrEmptyValue = false;
		if (this.currentCvComponent instanceof ComponentNullOrEmpty) {
			r_bNullOrEmptyValue = ((ComponentNullOrEmpty<VALUE>) this.currentCvComponent).isNullOrEmptyValue(p_oObject);
		} else {
			r_bNullOrEmptyValue = super.isNullOrEmptyValue(p_oObject);
		}
		return r_bNullOrEmptyValue && this.autoHide;
	}
	
	@Override
	public boolean validate(BasicComponentConfiguration p_oConfiguration, Map<String, Object> p_mapParameters, StringBuilder p_oErrorBuilder) {

		boolean r_validate;
		
		if (this.currentCvComponent instanceof ComponentValidator && ((ComponentValidator) this.currentCvComponent).getValidator() != null) {
			r_validate = ((ComponentValidator) this.currentCvComponent).getValidator().validate(this.currentCvComponent, p_oConfiguration, p_oErrorBuilder);
		} else {
			r_validate = super.validate(p_oConfiguration, p_mapParameters, p_oErrorBuilder);
		}
		
		return r_validate;
	}

	/**
	 * Setter for writing data
	 * @param p_bWritingData true if the framework is writing data (not the user), false otherwise
	 */
	public void setWritingData(boolean p_bWritingData) {
		this.writingData = p_bWritingData;
	}
	
	/**
	 * Getter of the writing data attribute
	 * @return true if the framework is writing data, false otherwise
	 */
	public boolean isWritingData() {
		return this.writingData ;
	}

	/**
	 * Compute the mandatory character and append it on the original label
	 * @param p_sOriginalLabel the original label
	 * @param p_bMandatory true if add the mandatory, false to remove it
	 * @return the original label with (or without) the mandatory character
	 */
	public String computeMandatoryLabel(String p_sOriginalLabel, boolean p_bMandatory) {
		StringBuilder mandatoryTextBuilder = new StringBuilder();
		String mandatoryString = AndroidApplication.getInstance().getStringResource(AndroidApplicationR.fwk_mandatory_string);
		if (p_bMandatory) {
			if (!p_sOriginalLabel.endsWith(mandatoryString)) {
				mandatoryTextBuilder.append(p_sOriginalLabel).append(mandatoryString);
			} else {
				mandatoryTextBuilder.append(p_sOriginalLabel);
			}
		} else {
			if (p_sOriginalLabel.endsWith(mandatoryString)) {
				mandatoryTextBuilder.append(p_sOriginalLabel.substring(0, p_sOriginalLabel.length() - mandatoryString.length()));
			} else {
				mandatoryTextBuilder.append(p_sOriginalLabel);
			}
		}
		return mandatoryTextBuilder.toString();
	}
	
	/**
	 * Sets the given error to the component
	 * @param p_sError
	 */
	private void setError(String p_sError) {
		if (this.currentCvComponent instanceof ComponentWrapper 
				&& this.currentView.getParent().getClass().getName().equals("android.support.design.widget.TextInputLayout")) {
			// TODO : faire plus propre...
			try {
				Method oSetErroMethod = this.currentView.getParent().getClass().getMethod("setError", CharSequence.class);
				oSetErroMethod.invoke(this.currentView.getParent(), p_sError);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				Application.getInstance().getLog().error(ErrorDefinition.FWK_MOBILE_E_0120, StringUtils.concat(
						ErrorDefinition.FWK_MOBILE_E_0120_LABEL, "-setError"));
			}
		} else if (this.currentView instanceof TextView) {
			if ( p_sError != null || ((TextView) this.currentView).getError() != null ) {
				((TextView) this.currentView).setError(p_sError);
			}
		} else if (this.currentCvComponent instanceof ComponentError) {
			TextView oErrorView = ((ComponentError) this.currentCvComponent).getErrorView();
			if (oErrorView != null && (p_sError != null || oErrorView.getError() != null)) {
				oErrorView.setError(p_sError);
			}
		}
	}
}
