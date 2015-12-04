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
import android.os.AsyncTask;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.annotations.BaseComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentBindDestroy;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentEnable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.base.MMBaseTextView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ChronometerParameterVM;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.DurationUtils;

/**
 * <p>
 * Chronometer widget used in the Movalys Mobile product for Android
 * </p>
 */
@BaseComponent(baseName="MMBaseTextView", baseClass="android.widget.TextView", appCompatClass="android.support.v7.widget.AppCompatTextView")
public class MMChronometer extends MMBaseTextView implements
		ConfigurableVisualComponent, InstanceStatable, ComponentEnable,
		ComponentBindDestroy, ComponentReadableWrapper<ChronometerParameterVM>, ComponentWritableWrapper<ChronometerParameterVM> {

//	/** logging tag */
//	private static final String TAG = "MMChronometer";
	/** configurable view delegate */
	private AndroidConfigurableVisualComponentDelegate<ChronometerParameterVM> aivDelegate = null;
	/** configurable view delegate */
	private AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;

	/** Chronometer VM parameter */
	private ChronometerParameterVM valueChronometer = null;
	/** Chronometer task */
	private MMChronometerTask task = null;

	/**
	 * A task updating widgets every minute
	 */
	private class MMChronometerTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... p_oParamArrayOfParams) {
			boolean bStop = false;
			while (!bStop) {
				this.publishProgress();
				try {
					Thread.sleep(DurationUtils.MILLISECONDS_IN_ONE_MINUTE);
				} catch (InterruptedException e) {
					bStop = true;
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... p_oValues) {
			super.onProgressUpdate(p_oValues);
			MMChronometer.this.updateVisual();
		}
	}

	/**
	 * Constructs a new MMChronometer
	 * 
	 * @param p_oContext
	 *            the context to use
	 * @see TextView#TextView(Context)
	 */
	@SuppressWarnings("unchecked")
	public MMChronometer(Context p_oContext) {
		super(p_oContext);
		if (!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<ChronometerParameterVM>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Long.class, null});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{null});
		}
	}

	/**
	 * Constructs a new MMChronometer
	 * 
	 * @param p_oContext
	 *            the context to use
	 * @param p_oAttrs
	 *            the attributes to use
	 * @see TextView#TextView(Context)
	 */
	@SuppressWarnings("unchecked")
	public MMChronometer(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if (!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<ChronometerParameterVM>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Long.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
		}
	}

	/**
	 * Constructs a new MMChronometer
	 * 
	 * @param p_oContext
	 *            the context to use
	 * @param p_oAttrs
	 *            the attributes to use
	 * @param p_oDefStyle
	 *            the style to use
	 * @see TextView#TextView(Context)
	 */
	@SuppressWarnings("unchecked")
	public MMChronometer(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if (!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<ChronometerParameterVM>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{Long.class, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this,  
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
		}
	}

	/**
	 * Start the task
	 * <p>Start the count.</p>
	 */
	public void start() {
		((AndroidApplication) AndroidApplication.getInstance()).execAsyncTask(this.task);
	}

	/**
	 * Update the visual component
	 */
	public void updateVisual() {
		long lCurrentValue = this.aivDelegate.configurationGetValue().initValue
				+ (System.currentTimeMillis() - this.aivDelegate
						.configurationGetValue().lastStart);
		this.setText(DurationUtils.getInstance().format(
				lCurrentValue / DurationUtils.MILLISECONDS_IN_ONE_SECOND));
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
	public void superOnRestoreInstanceState(Parcelable p_oState) {
		super.onRestoreInstanceState(p_oState);
	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		return super.onSaveInstanceState();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.widget.TextView#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		return this.aivFwkDelegate.onSaveInstanceState(super
				.onSaveInstanceState());
	}

	/**
	 * {@inheritDoc}
	 * 
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
	public VisualComponentDelegate<ChronometerParameterVM> getComponentDelegate() {
		return this.aivDelegate;
	}

	/********************************************************************************************
	 ***************************** Framework delegate callback **********************************
	 ********************************************************************************************/

	/**
	 * {@inheritDoc}
	 * 
	 * @see ComponentEnable#enableComponent(boolean)
	 */
	@Override
	public void enableComponent(boolean p_bEnable) {
		if (this.task != null) {
			this.task.cancel(true);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ComponentBindDestroy#doOnPostAutoBind()
	 */
	@Override
	public void doOnPostAutoBind() {
		if (this.task != null && this.valueChronometer != null
				&& this.valueChronometer.start) {
			// this is normally not appening
			throw new UnsupportedOperationException(
					"may do somesing about that");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ComponentBindDestroy#destroy()
	 */
	@Override
	public void destroy() {
		if (this.task != null) {
			this.task.cancel(true);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public ChronometerParameterVM configurationGetValue() {
		return this.valueChronometer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(ChronometerParameterVM p_oObjectToSet) {
		long lCurrentValue;
		if (p_oObjectToSet != null) {
			this.valueChronometer = (ChronometerParameterVM) p_oObjectToSet;
			lCurrentValue = this.valueChronometer.initValue;
			if (this.task != null) {
				this.task.cancel(true);
			}
			this.task = new MMChronometerTask();
			if (((ChronometerParameterVM) p_oObjectToSet).start) {
				lCurrentValue = this.valueChronometer.initValue
						+ (System.currentTimeMillis() - this.valueChronometer.lastStart);
				((AndroidApplication) Application.getInstance())
						.execAsyncTask(this.task);
			}
			this.setText(DurationUtils.getInstance().format(
					lCurrentValue / DurationUtils.MILLISECONDS_IN_ONE_SECOND));
		} else {
			this.setText(StringUtils.EMPTY);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see ComponentWritableWrapper#isFilled()
	 */
	@Override
	public boolean isFilled() {
		return true;
	}
}
