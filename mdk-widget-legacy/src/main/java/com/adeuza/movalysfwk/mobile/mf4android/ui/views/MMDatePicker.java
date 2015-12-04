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
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.common.AbstractUIRunnable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;

/**
 * component used to enter a date into a form
 * <b>Warning, this component takes up lot of space.</b>
 * MMDateTimeEditText can be used to maximize space and delegate inputs in a dialog
 *
 */
public class MMDatePicker extends DatePicker implements ConfigurableVisualComponent, OnDateChangedListener, InstanceStatable,
ComponentReadableWrapper<Long>, ComponentWritableWrapper<Long> {

	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<Long> aivDelegate = null;
	/** configurable view delegate */
	private AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	/** the date used by the component*/
	private Calendar calendar = Calendar.getInstance();

	/**
	 * Create new MMDatePicker
	 * @param p_oContext the android context
	 */
	@SuppressWarnings("unchecked")
	public MMDatePicker(Context p_oContext) {
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
	 * Create new MMDatePicker
	 * @param p_oContext the android context
	 * @param p_oAttrs the android view attributes (in layout XML)
	 */
	@SuppressWarnings("unchecked")
	public MMDatePicker(Context p_oContext, AttributeSet p_oAttrs) {
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
	 * Create new MMDatePicker
	 * @param p_oContext the android context
	 * @param p_oAttrs the android view attributes (in layout XML)
	 * @param p_iDefStyle the android style (define in XML)
	 */
	@SuppressWarnings("unchecked")
	public MMDatePicker(Context p_oContext, AttributeSet p_oAttrs, int p_iDefStyle) {
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
	
	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivFwkDelegate.setId(p_oId);
	}

	@Override
	public void onDateChanged(DatePicker p_oDatePicker, int p_iYyear, int p_iMonthOfYear, int p_iDayOfMonth) {
		this.updateCalendar();
		if (!this.aivDelegate.isWritingData()) {
			this.aivFwkDelegate.changed();
		}
	}

	/**
	 * Méthode privée pour instancier un nouveau Calendar avec les valeures du DatePicker courant.
	 */
	public void updateCalendar(){
		this.calendar.set(this.getYear(), this.getMonth(), this.getDayOfMonth());
	}

	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		super.onRestoreInstanceState(p_oState);
	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		return super.onSaveInstanceState();
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.DatePicker#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		return this.aivFwkDelegate.onSaveInstanceState(super.onSaveInstanceState());
	}

	/**
	 * {@inheritDoc}
	 * @see android.widget.DatePicker#onRestoreInstanceState(android.os.Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable p_oState) {
		this.aivFwkDelegate.onRestoreInstanceState(p_oState);
	}

	/**
	 * Runnable to execute clearFocus() in the ui thread
	 *
	 */
	public static class ClearFocusRun extends AbstractUIRunnable {

		/** the linked picker */
		private MMDatePicker datePicker = null;

		/**
		 * Create new instance of ClearFocusRun
		 * @param p_oMMDatePicker the associate MMDatePicker
		 */
		public ClearFocusRun(MMDatePicker p_oMMDatePicker) {
			this.datePicker = p_oMMDatePicker;
		}

		/**
		 * {@inheritDoc}
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void exec() {
			this.datePicker.clearFocus();
		}
	}


	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}

	@Override
	public VisualComponentDelegate<Long> getComponentDelegate() {
		return this.aivDelegate;
	}
	
	/****************************************************************************************************************
	 ****************************************** Framework delegate callback *****************************************
	 ****************************************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public Long configurationGetValue() {
		this.clearFocus();
		this.updateCalendar();
		return this.calendar.getTimeInMillis();
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(Long p_oObjectToSet) {
		if (p_oObjectToSet != null && !p_oObjectToSet.equals(-1L)) {
			this.calendar.setTimeInMillis((Long) p_oObjectToSet);
		}
		this.init(this.calendar.get(Calendar.YEAR), this.calendar.get(Calendar.MONTH), this.calendar.get(Calendar.DAY_OF_MONTH), this);
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return true;
	}
}
