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

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.InstanceStatable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.converter.CustomConverter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.formatter.CustomFormatter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.AbstractConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent.Attribute;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.wrapper.ComponentWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;


/**
 * Delegate for AbstractConfigurableVisualComponentFwkDelegate
 * This class defines the behavior of the component inside the MDK.
 * It takes care of 
 * <ul>
 * 	<li>binding a view to its viewmodel,</li> 
 * 	<li>setting its business attributes (hidden, mandatory, etc),</li>
 * 	<li>maintaining the state of the view during the screen rotations</li>
 * 	<li>holding the XML attributes on the view</li>
 * </ul>
 */

public class AndroidConfigurableVisualComponentFwkDelegate extends AbstractConfigurableVisualComponentFwkDelegate {
	
	/** string resources prefix */
	private static final String STRING_RESOURCES_PREFIX = "@string/";
	
	/** the current view */
	protected View currentView = null;
	
	/** the current view casted in ConfigurableVisualComponent */
	protected ConfigurableVisualComponent currentCvComponent = null;
	
	/**
	 * Indicates the holder fragment of this component. If this property is
	 * null, this component is not held by a fragment.
	 */
	protected String fragmentTag;

	/** Application */
	protected AndroidApplication application = null;

	/** A weak reference to the ViewModel */
	protected WeakReference<ViewModel> vm;

	/** XML attributes set on the component  */
	protected Map<ConfigurableVisualComponent.Attribute,String> attributes = new HashMap<>();
	
	/** the component has rules */
	protected boolean bHasRule = false;
	
	/**
	 * Constructs a new delegate
	 * @param p_oCurrentView the current view
	 */
	public AndroidConfigurableVisualComponentFwkDelegate(ConfigurableVisualComponent p_oCurrentView) {
		this(p_oCurrentView, null);
	}
	
	/**
	 * Construct a new AndroidConfigurableVisualComponentFwkDelegate
	 * @param p_oCurrentView the component view
	 * @param p_oAttrs the android view attributes
	 */
	public AndroidConfigurableVisualComponentFwkDelegate(ConfigurableVisualComponent p_oCurrentView, AttributeSet p_oAttrs) {
		super(false);
		
		this.application = (AndroidApplication) Application.getInstance();
		
		if (p_oCurrentView instanceof ComponentWrapper) {
			this.setId(((ComponentWrapper) p_oCurrentView).getComponent().getId());
			this.currentView = ((ComponentWrapper) p_oCurrentView).getComponent();
		} else {
			this.setId(((View) p_oCurrentView).getId());
			this.currentView = (View) p_oCurrentView;
		}
		
		this.currentCvComponent = (ConfigurableVisualComponent) p_oCurrentView;
		
		if (p_oAttrs != null) {
			setCustomConverter(p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, Attribute.CONVERTER_NAME_ATTRIBUTE.getName()), p_oAttrs);
			setCustomFormatter(p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, Attribute.FORMATTER_NAME_ATTRIBUTE.getName()), p_oAttrs);
		}
	}
	
	/**
	 * Get the android application instance
	 * @return the android application
	 */
	public AndroidApplication getAndroidApplication() {
		return this.application;
	}

	/**
	 * Sets an id on the component
	 * @param p_iId the component's id
	 */
	public final void setId(int p_iId) {
		if (p_iId > -1) {
			String sId = this.application.getAndroidIdStringByIntKey(p_iId);
			this.setName(sId);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetLabel(String p_sLabel) {
		if (this.currentView instanceof TextView && this.isLabel()) {
			((TextView) this.currentView).setText(p_sLabel);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isVisible() {
		return this.currentView.isShown();
	}

	/**
	 * Android save the current instance on rotation or when the application goes in background
	 * @param p_oInheritState the result of a call to super.onSaveInstanceState() from the component
	 * @return a {@link Parcelable} object with the modified values
	 */
	public Parcelable onSaveInstanceState(Parcelable p_oInheritState) {
		if (currentView instanceof InstanceStatable){
			Parcelable superState=((InstanceStatable)currentView).superOnSaveInstanceState();
			AndroidConfigurableVisualComponentSavedState oState=new AndroidConfigurableVisualComponentSavedState(superState);
			oState.setChanged(this.isChanged());
			oState.setVisibility(this.currentView.getVisibility());
			oState.setEnabled(this.currentView.isEnabled());
			oState.setHasRules(this.bHasRule);

			return oState;
		}
		else {
			Log.d(Application.LOG_TAG,"[onSaveInstanceState] "+currentView.getClass().getName()+" doit implémenter InstanceStatable !!!");
			return p_oInheritState;
		}
	}

	/**
	 * Android restore the current view instance after a rotation or a background restore
	 * @param p_oState the current view previous state
	 */
	public void onRestoreInstanceState(Parcelable p_oState) {
		//Log.d("LBR","[AndroidConfigurableVisualComponentDelegate.onRestoreInstanceState] "+currentView.getClass().getName());

		if (currentView instanceof InstanceStatable){
			((AndroidConfigurableVisualComponentDelegate)this.currentCvComponent.getComponentDelegate()).setWritingData(true);
			
			//Log.d("LBR","[AndroidConfigurableVisualComponentDelegate.onRestoreInstanceState] "+currentView.getClass().getName()+" implément InstanceStatable.");

			if(!(p_oState instanceof AndroidConfigurableVisualComponentSavedState)){
				//Log.d("LBR","[AndroidConfigurableVisualComponentDelegate.onRestoreInstanceState] "+currentView.getClass().getName()+" appel InstanceStatable.superOnRestoreInstanceState.");

				((InstanceStatable)currentView).superOnRestoreInstanceState(p_oState);
				return;
			}

			//Log.d("LBR","[AndroidConfigurableVisualComponentDelegate.onRestoreInstanceState] "+currentView.getClass().getName()+" implément AndroidConfigurableVisualComponentSavedState.");
			AndroidConfigurableVisualComponentSavedState savedState = (AndroidConfigurableVisualComponentSavedState) p_oState;

			// set the rules before calling super because super call configurationSetValue
			this.setHasRules(savedState.getHasRules());
			
			//Log.d("LBR","[AndroidConfigurableVisualComponentDelegate.onRestoreInstanceState] "+currentView.getClass().getName()+" appel AndroidConfigurableVisualComponentSavedState.superOnRestoreInstanceState.");
			// Etat sauvegardé par la classe mère
			((InstanceStatable)currentView).superOnRestoreInstanceState(savedState.getSuperState());

			//on repose l'état
			this.setChanged(savedState.isChanged());
			
			this.currentView.setVisibility(savedState.getVisibility());
			this.currentView.setEnabled(savedState.isEnabled());
			
			((AndroidConfigurableVisualComponentDelegate)this.currentCvComponent.getComponentDelegate()).setWritingData(false);
		}
		else {
			Log.v("AndroidConfigurableVisualComponentSavedState","[onRestoreInstanceState] "+currentView.getClass().getName()+"doit implémenter InstanceStatable !!!");
		}
	}

	@Override
	protected void doOnVisualComponentChanged() {
		// ABE stuff to save value on VM
		if (this.vm != null) {
			ViewModel oVm = this.vm.get();
			if (oVm != null) {
				oVm.readDataFromComponent(this.currentCvComponent);
				oVm.setDirectlyModified(true);
			} else {
				Log.w(Application.LOG_TAG, "doOnVisualComponentChanged() cannot write data for component "
						+ this.getName()+" with context : "+this.getCurrentView().getContext().getClass().getSimpleName());
			}
		} else {
			Log.w(Application.LOG_TAG, "doOnVisualComponentChanged() cannot write data for component "
				+ this.getName()+" with context : "+this.getCurrentView().getContext().getClass().getSimpleName());
		}
	}

	/**
	 * Returns the {@link View} object managed by this class instance
	 * @return the view managed by the class
	 */
	public View getCurrentView() {
		return this.currentView;
	}

	@Override
	public void registerVM(ViewModel p_oViewModel) {
		this.vm = new WeakReference<>(p_oViewModel);
		this.restoreInstanceAttributes();
	}
	
	@Override
	public void unregisterVM() {
		this.saveInstanceAttributes();
		this.vm = null;
	}

	@Override
	public CustomConverter customConverter() {
		return this.customConverter;
	}

	@Override
	public void setCustomConverter(CustomConverter p_oConverter) {
		this.customConverter = p_oConverter;
	}

	/**
	 * Set a custom converter for the delegate component
	 * @param p_oCustomConverter the key of the converter (in {@link BeanLoader})
	 * @param p_oAttributeValues the component attributes
	 */
	public void setCustomConverter(String p_oCustomConverter, AttributeSet p_oAttributeValues) {
		if(p_oCustomConverter != null) {
			this.customConverter = (CustomConverter) BeanLoader.getInstance().getBeanByType(p_oCustomConverter);
		}
		if(this.customConverter != null) {
			String[] oConverterParameters = setAttributes(p_oAttributeValues, customConverter.getAttributesName());
			customConverter.setAttributes(oConverterParameters);
		}
	}

	/**
	 * sets attributes array values from AttributeSet on a given range of keys
	 * @param p_oAttributeValues AttributeSet to go through
	 * @param p_oAttributesNames attributes keys to find
	 * @return the array of values found
	 */
	private String[] setAttributes(AttributeSet p_oAttributeValues, String[] p_oAttributesNames) {
		String[] r_oParameters = new String[p_oAttributesNames.length];
		int index = 0;
		for(String sAttributeName : p_oAttributesNames) {
			String sParameterValue = p_oAttributeValues.getAttributeValue(Application.MOVALYSXMLNS, sAttributeName);
			if(sParameterValue != null) {
				r_oParameters[index] = convertParameterValue(sParameterValue);
			}
			index++;
		}
		return r_oParameters;
	}
	
	/**
	 * Tries to grab a resource in android strings from a given attribute
	 * @param p_sValue the attribute value
	 * @return the computed value
	 */
	private String convertParameterValue(String p_sValue) {
		String r_sValue = p_sValue;
		if (r_sValue.startsWith(STRING_RESOURCES_PREFIX)) {
			r_sValue = r_sValue.substring(STRING_RESOURCES_PREFIX.length());
			r_sValue = this.application.getStringResource(this.application.getAndroidIdByStringKey(ApplicationRGroup.STRING, r_sValue));
		}
		if(r_sValue == null){
			return p_sValue;
		}
		else {
			return r_sValue;
		}
	}
	
	@Override
	public CustomFormatter customFormatter() {
		return this.customFormatter;
	}

	@Override
	public void setCustomFormatter(CustomFormatter p_oFormatter) {
		// Nothing to do		
	}
	
	/**
	 * Set a custom formatter for the delegate component
	 * @param p_oCustomFormatter the key of the formatter (in {@link BeanLoader})
	 * @param p_oAttributeValues the component attributes
	 */
	public void setCustomFormatter(String p_oCustomFormatter, AttributeSet p_oAttributeValues) {
		if(p_oCustomFormatter != null) {
			this.customFormatter = (CustomFormatter) BeanLoader.getInstance().instantiatePrototype(p_oCustomFormatter);
		}
		if(this.customFormatter != null) {
			String[] oConverterParameters = setAttributes(p_oAttributeValues, customFormatter.getAttributesName());
			customFormatter.setAttributes(oConverterParameters);
		}
	}
	
	/**
	 * The tag of the fragment container of this component, if exists.
	 * @return a tag or null
	 */
	@Override
	public String getFragmentTag() {
		return fragmentTag;
	}
	
	/**
	 * The tag of the fragment container of this component, if exists.
	 * @param p_sFragmentTag a tag or null
	 */
	@Override
	public void setFragmentTag(String p_sFragmentTag) {
		this.fragmentTag = p_sFragmentTag;
	}
	
	/**
	 * sets up the component with the given xml attributes
	 * 
	 * @param p_oAttrs xml attributes for the component
	 */
	public void defineParameters( Map<ConfigurableVisualComponent.Attribute,String> p_oAttrs ){
		this.attributes = p_oAttrs;
	}
	
	/**
	 * sets up the component with the given xml attributes
	 * 
	 * @param p_oAttrs xml attributes for the component
	 */
	public void defineParameters( AttributeSet p_oAttrs ){
		this.attributes = new HashMap<>();
		if ( p_oAttrs!= null ){
			for (ConfigurableVisualComponent.Attribute oAttribute : ConfigurableVisualComponent.Attribute.values() ){
				String sValue = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, oAttribute.getName()) ;
				if (sValue != null){
					this.attributes.put(oAttribute, convertParameterValue(sValue));
				}
			}
		}
	}
	
	/**
	 * Get component attributes
	 * @return the component attributes
	 */
	public Map<ConfigurableVisualComponent.Attribute,String> getAttributes() {
		return this.attributes;
	}

	@Override
	public boolean hasRules() {
		return this.bHasRule;
	}

	@Override
	public void setHasRules(boolean p_bHasRules) {
		this.bHasRule = p_bHasRules;
	}

	@Override
	public void saveInstanceAttributes() {
		if (this.vm != null && this.currentCvComponent instanceof ComponentWrapper) {
			ViewModel oVM = this.vm.get();
			
			if (oVM != null) {
				int iComponentId = this.currentView.getId();
				oVM.setDelegateAttributesForComponent(iComponentId, this.instanceAttributes());
			}
		}
	}

	@Override
	public void restoreInstanceAttributes() {
		if (this.vm != null) {
			ViewModel oVM = this.vm.get();
			
			if (oVM != null) {
				int iComponentId = this.currentView.getId();
				Map<String, Object> oAttrs = oVM.getDelegateAttributesForComponent(iComponentId);
				
				if (oAttrs != null) {
					this.bHasRule = (boolean) oAttrs.get("bHasRule");
					if ((boolean) oAttrs.get("isChanged")) {
						this.changed();
					} else {
						this.resetChanged();
					}
					this.currentView.setVisibility((int) oAttrs.get("visible"));
					this.currentView.setEnabled((boolean) oAttrs.get("enabled"));
				}
			}
		}
	}
	
	@Override
	public Map<String, Object> instanceAttributes() {
		Map<String, Object> r_oAttrs = new HashMap<String, Object>();
		
		r_oAttrs.put("bHasRule", this.bHasRule);
		r_oAttrs.put("isChanged", this.isChanged());
		r_oAttrs.put("visible", this.currentView.getVisibility());
		r_oAttrs.put("enabled", this.currentView.isEnabled());
		
		return r_oAttrs;
	}
	
}
