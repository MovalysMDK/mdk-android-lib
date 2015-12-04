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
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.annotations.BaseComponent;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentReadableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.interfaces.ComponentWritableWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.base.MMBaseTextView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.visual.BasicComponentConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.SectionTitle;

/**
 * <p>
 * 	Custom EditText widget used in the Movalys Mobile product for Android
 *	This component is used as Section Title and replace</p>
 *	
 * 		style="@style/interventiondetail_sectiontitle_text" 
 * 		android:gravity="center_vertical|center_horizontal"
 * 		android:layout_gravity="center_vertical|center_horizontal"
 * 		/> 
 *
 *
 *
 */
@BaseComponent(baseName="MMBaseTextView", baseClass="android.widget.TextView", appCompatClass="android.support.v7.widget.AppCompatTextView")
public class MMSectionTitle extends MMBaseTextView implements ConfigurableVisualComponent, InstanceStatable, 
ComponentReadableWrapper<SectionTitle>, ComponentWritableWrapper<SectionTitle> {

	/** configurable view framework delegate */
	protected AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;
	
	/** configurable view delegate */
	protected AndroidConfigurableVisualComponentDelegate<SectionTitle> aivDelegate = null;
	
	/** the default value */
	private String defaultValue = "";
	
	/** a true value means the background will be overriden, when false then the style will handle it */
	private boolean withImageBackground = true ;

	/**
	 * Constructs a new MMSectionTitle
	 * @param p_oContext the context to use
	 * @see TextView#TextView(Context)
	 */
	@SuppressWarnings("unchecked")
	public MMSectionTitle(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<SectionTitle>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{SectionTitle.class, null});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{null});
		}
	}

	/**
	 * Constructs a new MMSectionTitle
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see TextView#TextView(Context, AttributeSet)
	 */
	public MMSectionTitle(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.init(p_oAttrs);
		}
	}

	/**
	 * Constructs a new MMSectionTitle
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use
	 * @see TextView#TextView(Context, AttributeSet, int)
	 */
	public MMSectionTitle(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			this.init(p_oAttrs);
		}
	}

	/**
	 * Initialize the default value of this title.
	 * @param p_oAttrs xml attributes to set on the component
	 */
	@SuppressWarnings("unchecked")
	protected final void init(AttributeSet p_oAttrs) {
		this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<SectionTitle>) DelegateInitialiserHelper.initDelegate(this, 
				new Class[]{Class.class, AttributeSet.class}, 
				new Object[]{SectionTitle.class, p_oAttrs});
		this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
				new Class[]{AttributeSet.class}, 
				new Object[]{p_oAttrs});
		this.withImageBackground = p_oAttrs.getAttributeBooleanValue(AndroidApplication.MOVALYSXMLNS, "withImageBackground", false);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (!isInEditMode()){
			this.setBackground(((AndroidApplication) Application.getInstance()).getAndroidIdByRKey(AndroidApplicationR.section_title_default));

			BasicComponentConfiguration oConfiguration = (BasicComponentConfiguration) this.aivFwkDelegate.getConfiguration();
			if (oConfiguration != null && oConfiguration.getLabel() != null) {
				this.defaultValue = oConfiguration.getLabel();
			}
		}
	}
	
	/**
	 * Initializes the background resource of the component
	 * @param p_iResourceId the resource id
	 */
	private final void setBackground(int p_iResourceId){
		if ( this.withImageBackground ) {
			setBackgroundResource(p_iResourceId);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see android.view.View#setId(int)
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
	 * Setter for background image
	 * @param p_bWithImageBackground true if the component has a background image, false otherwise
	 */
	public void setWithImageBackground(boolean p_bWithImageBackground) {
		this.withImageBackground = p_bWithImageBackground;
	}

	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}

	@Override
	public VisualComponentDelegate<SectionTitle> getComponentDelegate() {
		return this.aivDelegate;
	}

	/**
	 * Get the default value
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Setter for the default value
	 * @param p_sDefaultValue the defaultValue to set
	 */
	public void setDefaultValue(String p_sDefaultValue) {
		this.defaultValue = p_sDefaultValue;
	}

	/**
	 * Is it a background image
	 * @return true if there is a background image, false otherwise
	 */
	public boolean isWithImageBackground() {
		return withImageBackground;
	}

	/******************************************************************************************
	 ****************************** Framework delegate callback *******************************
	 ******************************************************************************************/
	
	/**
	 * {@inheritDoc}
	 * @see ComponentWritableWrapper#configurationGetValue()
	 */
	@Override
	public SectionTitle configurationGetValue() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see ComponentReadableWrapper#configurationSetValue(Object)
	 */
	@Override
	public void configurationSetValue(SectionTitle p_oObjectToSet) {
		if (p_oObjectToSet!=null) {
			this.setText(this.getDefaultValue() + ((SectionTitle)p_oObjectToSet).getTitle());

			if (this.isWithImageBackground() && ((SectionTitle)p_oObjectToSet).getBackground() != null) {
				this.setBackgroundResource((Integer)Application.getInstance().getImageByName(((SectionTitle)p_oObjectToSet).getBackground()));
			}
		}
		else {
			this.setText(StringUtils.EMPTY);
			if ( this.isWithImageBackground() ) {
				this.setBackgroundResource(0);
			}
		}
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
