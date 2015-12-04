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

import java.util.Calendar;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;

/**
 * component used to enter a date into a form
 * <b>Warning, this component takes up lot of space.</b>
 * MMDateTimeEditText can be used in mode time to maximize space and delegate inputs in a dialog
 * 
 * <b>this component is populated by a Long representing the complete date and time, only the time is taken into account by MMTimePicker.
 * the date returned isn't guaranteed to be correct regarding date provided and time modified.</b>
 *
 */
public class MMTimePicker extends TimePicker implements ConfigurableVisualComponent, OnTimeChangedListener, InstanceStatable, 
	ComponentReadableWrapper<Long>, ComponentWritableWrapper<Long> {
	
	/** configurable view framework delegate */
	protected AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/** configurable view delegate */
	protected AndroidConfigurableVisualComponentDelegate<Long> aivDelegate = null;
	
	/** the date used by the component*/
	private Calendar oCalendar = Calendar.getInstance();

	/**
	 * Constructs a new MMTimePicker
	 * @param p_oContext the context to use
	 */
	@SuppressWarnings("unchecked")
	public MMTimePicker(Context p_oContext) {
		super(p_oContext);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Long>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Long.class, null});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{null});
		}	
	}

	/**
	 * Constructs a new MMTimePicker
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attribute set to use
	 */
	@SuppressWarnings("unchecked")
	public MMTimePicker(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Long>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Long.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
		}	
	}

	/**
	 * Constructs a new MMTimePicker
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attribute set to use
	 * @param p_iDefStyle the style to use
	 */
	@SuppressWarnings("unchecked")
	public MMTimePicker(Context p_oContext, AttributeSet p_oAttrs, int p_iDefStyle) {
		super(p_oContext, p_oAttrs, p_iDefStyle);
		if (!isInEditMode()){
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<Long>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Long.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
		}	
	}

	/**
	 * Returns the value hosted by the component as a {@link Calendar} 
	 * @return the value hosted by the component
	 */
	public Calendar getCalendar() {
		return oCalendar;
	}

	/**
	 * called when the inflater has finished its job 
	 */	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		this.setIs24HourView(true);
		this.setOnTimeChangedListener(this);
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
	public void onTimeChanged(TimePicker p_oTimePicker, int p_iHour, int p_iMin) {
		oCalendar.set(Calendar.HOUR_OF_DAY,p_iHour);
		oCalendar.set(Calendar.MINUTE, p_iMin);
		if (!this.aivDelegate.isWritingData()) {
			this.aivFwkDelegate.changed();
		}
	}


	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		super.onRestoreInstanceState(p_oState);
	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		return super.onSaveInstanceState();
	}

	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}

	@Override
	public VisualComponentDelegate<Long> getComponentDelegate() {
		return this.aivDelegate;
	}

	/****************************************************************************************************
	 ******************************** Framework delegate callback ***************************************
	 ****************************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long configurationGetValue() {
		return Long.valueOf(this.getCalendar().getTimeInMillis());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configurationSetValue(Long p_oObjectToSet) {
		if (p_oObjectToSet != null && !p_oObjectToSet.equals(-1L)) {
			this.getCalendar().setTimeInMillis((Long) p_oObjectToSet);
			this.setCurrentHour(this.getCalendar().get(Calendar.HOUR_OF_DAY));
			this.setCurrentMinute(this.getCalendar().get(Calendar.MINUTE));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFilled() {
		return this.getCalendar().getTimeInMillis() > 0;
	}

}
