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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentError;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;

/**
 * <p>Component that represents a group of checkBoxes: One field but several values.</p>
 * 
 * @param <VALUE> the component handled type
 */
public class MMCheckBoxGroup<VALUE> extends LinearLayout implements ConfigurableVisualComponent, InstanceStatable, 
ComponentReadableWrapper<Collection<VALUE>>, ComponentWritableWrapper<Collection<VALUE>>, ComponentError, ComponentEnable {

	/** map of checkbox by value */
	private Map<VALUE, CheckBox> checkBoxByValue;
	
	/** initial checked checkboxes */
	private Collection<CheckBox> initialCheckedCheckBoxes;

	/** error container */
	private TextView errorContainer = null;

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<Collection<VALUE>> aivDelegate = null;

	/**
	 * Constructs a new MMCheckBoxGroup
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 */
	@SuppressWarnings("unchecked")
	public MMCheckBoxGroup(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Collection<VALUE>>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Collection.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
			this.init();
		}
	}
	/**
	 * Initializes this component.
	 */
	private final void init() {
		this.setOrientation(LinearLayout.VERTICAL);

		this.checkBoxByValue			= new HashMap<>();
		this.initialCheckedCheckBoxes	= new ArrayList<>();
	}

	/**
	 * Adds a new checkBox to this group. A checkbox is defined by its label and its value.
	 * 
	 * @param p_oValue Value of the checkBox to add. Possibly a null or empty value.
	 * @param p_sLabel Label of the checkBox to add. Possibly a empty string.
	 */
	public void addCheckBox(VALUE p_oValue, String p_sLabel) {
		CheckBox oCheckBox = new CheckBox(this.getContext());
		oCheckBox.setText(p_sLabel);
		this.addView(oCheckBox);
		this.checkBoxByValue.put(p_oValue, oCheckBox);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivFwkDelegate.setId(p_oId);
	}

	/**
	 * Getter for initiale checked boxes
	 * @return the initiale checked boxes
	 */
	public Collection<CheckBox> getInitialCheckedBoxes() {
		return this.initialCheckedCheckBoxes;
	}
	
	/**
	 * Getter for check box by value
	 * @return the check box map
	 */
	public Map<VALUE, CheckBox> getCheckBoxByValue() {
		return this.checkBoxByValue;
	}

	/**
	 * initialize the error component
	 * <p>this component create it's compoent to store errors</p>
	 */
	private void initializeErrorContainer() {
		if (this.errorContainer == null) {
			AndroidApplication oApplication = (AndroidApplication) Application.getInstance();

			String sErrorContainer = oApplication.getAndroidIdStringByIntKey(this.getId());

			String sSuffix = TYPE_EDIT;
			if (sErrorContainer.endsWith(TYPE_VALUE)) {
				sSuffix = TYPE_VALUE;
			}
			sErrorContainer = sErrorContainer.substring(0, sErrorContainer.length() - sSuffix.length()).concat(TYPE_LABEL);
			this.errorContainer = (TextView) ((View) this.getParent()).findViewById(oApplication.getAndroidIdByStringKey(ApplicationRGroup.ID, sErrorContainer));
		}
	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.onSaveInstanceState();
		Bundle r_oBundle = new Bundle();
		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putSerializable("checkBoxByValue",(Serializable) this.configurationGetValue());
		return r_oBundle;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		Bundle r_oBundle = (Bundle) p_oState ;
		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.onRestoreInstanceState(oParentParcelable);
		this.configurationSetValue((Collection<VALUE>) r_oBundle.getSerializable("checkBoxByValue"));
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.TextView#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		return this.aivFwkDelegate.onSaveInstanceState(super.onSaveInstanceState());
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.TextView#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		this.aivFwkDelegate.onRestoreInstanceState(p_oState);
	}
	
	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}
	
	@Override
	public VisualComponentDelegate<?> getComponentDelegate() {
		return this.aivDelegate;
	}
	
	/********************************************************************************
	 ************************ Framework delegate callback****************************
	 ********************************************************************************/

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(Collection<VALUE> p_oValues) {
		for (Entry<VALUE, CheckBox> oEntry : this.checkBoxByValue.entrySet()) {
			oEntry.getValue().setChecked(false);
		}

		CheckBox oCheckBox = null;
		for (VALUE oValue : p_oValues) {
			oCheckBox = this.checkBoxByValue.get(oValue);
			if (oCheckBox != null) {
				oCheckBox.setChecked(true);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<VALUE> configurationGetValue() {
		Collection<VALUE> r_oValues = new ArrayList<>();
		for (Entry<VALUE, CheckBox> oEntry : this.checkBoxByValue.entrySet()) {
			if (oEntry.getValue().isChecked()) {
				r_oValues.add(oEntry.getKey());
			}
		}
		return r_oValues;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFilled() {
		boolean r_bFilled = false;

		Iterator<CheckBox> iterCheckBoxes = this.checkBoxByValue.values().iterator();
		while (iterCheckBoxes.hasNext() && !r_bFilled) {
			r_bFilled = iterCheckBoxes.next().isChecked();
		}
		return r_bFilled;
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentError#getErrorView()
	 */
	@Override
	public TextView getErrorView() {
		if (this.errorContainer == null) {
			this.initializeErrorContainer();
		}
		return errorContainer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void enableComponent(boolean p_bEnable) {
		for (CheckBox oCheckBox : initialCheckedCheckBoxes) {
			oCheckBox.setEnabled(p_bEnable);
		}
	}
}
