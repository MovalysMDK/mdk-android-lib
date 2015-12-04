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
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.MMViewPager;
import android.util.AttributeSet;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.AndroidConfigurableVisualComponentFwkDelegate;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.DelegateInitialiserHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.wrappers.MMWorkspaceAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableLayoutComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MMAlwaysEnable;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentDelegate;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.VisualComponentFwkDelegate;

/**
 * Base class for component workspace
 * <p>Handle default behaviors of workspace :
 * columns and adapter to create columns</p>
 * @param <VALUE> data type manipulated by the class
 */
@MMAlwaysEnable
public abstract class AbstractBaseWorkspaceFragmentLayout<VALUE> extends MMViewPager implements ConfigurableLayoutComponent,
		InstanceStatable {

	/** name of the MMWorkspaceView */
	public static final String MAIN_MMWORKSPACEVIEW_NAME = "Main_MMWorkspaceView";

	/** the application to access to centralized resources */
	private AndroidApplication application = (AndroidApplication) Application.getInstance();

	/** configurable view framework delegate */
	protected AndroidConfigurableVisualComponentFwkDelegate aivFwkDelegate = null;

	/** configurable view delegate */
	protected AndroidConfigurableVisualComponentDelegate<VALUE> aivDelegate = null;

	/** Background resource id */
	private int backgroundResId;

	/** the handled class type */
	private Class<VALUE> valueClass;

	/**
	 * Getter for the handled class type
	 * @return the valueClass
	 */
	public Class<VALUE> getValueClass() {
		return valueClass;
	}

	/**
	 * Create a new instance of MMBaseWorkspaceFragmentLayout
	 * @param p_oContext the android context
	 * @param p_oDelegateType the Class of the handled type
	 */
	@SuppressWarnings("unchecked")
	public AbstractBaseWorkspaceFragmentLayout(Context p_oContext, Class<?> p_oDelegateType) {
		super(p_oContext);
		this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<VALUE>) DelegateInitialiserHelper.initDelegate(this, 
				new Class[]{Class.class, AttributeSet.class}, 
				new Object[]{p_oDelegateType, null});
		this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
				new Class[]{AttributeSet.class}, 
				new Object[]{null});

		init();
	}

	/**
	 * Create a new instance of MMBaseWorkspaceFragmentLayout
	 * @param p_oContext the android context
	 * @param p_oAttrs the android view AttributeSet (in layout XML)
	 * @param p_oDelegateType the Class of the handled type
	 */
	@SuppressWarnings("unchecked")
	public AbstractBaseWorkspaceFragmentLayout(Context p_oContext, AttributeSet p_oAttrs, Class<VALUE> p_oDelegateType) {
		super(p_oContext, p_oAttrs);
		if (!isInEditMode()) {
			this.aivDelegate = (AndroidConfigurableVisualComponentDelegate<VALUE>) DelegateInitialiserHelper.initDelegate(this, 
					new Class[]{Class.class, AttributeSet.class}, 
					new Object[]{p_oDelegateType, p_oAttrs});
			this.aivFwkDelegate = (AndroidConfigurableVisualComponentFwkDelegate) DelegateInitialiserHelper.initFwkDelegate(this, 
					new Class[]{AttributeSet.class}, 
					new Object[]{p_oAttrs});
			
			this.backgroundResId = p_oAttrs.getAttributeResourceValue(AndroidApplication.MOVALYSXMLNS, "background", 0);
			this.valueClass = p_oDelegateType;
			init();
		}
	}

	/**
	 * init method
	 */
	public final void init() {
		this.initBackground();
	}

	/**
	 * Is the first screen of the workspace is visible
	 * @return true if the current page is 0, false otherwise
	 */
	public boolean isFirstScreenSelected() {
		return this.getCurrentItem() == 0;
	}

	/**
	 * Initialize background resource
	 * <p>Set the attribute backgroundResId on the component background resource</p>
	 */
	protected void initBackground() {
		if (this.backgroundResId > 0) {
			this.setBackgroundResource(this.backgroundResId);
		}
	}

	/**
	 * Getter for the application
	 * @return the Framework application
	 */
	public AndroidApplication getAndroidApplication() {
		return this.application;
	}

	/**
	 * Hide the detail columns
	 * 
	 * @param p_iColumnIndex the number of the detail column to hide
	 * @param p_bNeedRecompute if true, this method launch recomputeDisplay
	 */
	public void hideColumn(int p_iColumnIndex, boolean p_bNeedRecompute) {
		((MMWorkspaceAdapter) this.getAdapter()).hideColumn(p_iColumnIndex);
	}

	/**
	 * Unhide all detail columns
	 * @param p_bNeedRecompute true if the columns size need to be recompute, false otherwise
	 */
	public void unHideAllColumns(boolean p_bNeedRecompute) {
		((MMWorkspaceAdapter) this.getAdapter()).unHideAllColumns();
	}

	/**
	 * Hide all detail columns
	 * @param p_bNeedRecompute true if the columns size need to be recompute, false otherwise
	 */
	public void hideAllColumns(boolean p_bNeedRecompute) {
		((MMWorkspaceAdapter) this.getAdapter()).hideAllColumns();
	}

	@Override
	public void setId(int p_oId) {
		super.setId(p_oId);
		this.aivFwkDelegate.setId(p_oId);
	}

	@Override
	public Parcelable superOnSaveInstanceState() {
		Parcelable superParcel = super.onSaveInstanceState();
		Bundle r_oBundle = new Bundle();

		r_oBundle.putParcelable("parentParcelable", superParcel);
		r_oBundle.putInt("CurrentPage", this.getCurrentItem());

		return r_oBundle;
	}

	@Override
	public void superOnRestoreInstanceState(Parcelable p_oState) {

		Bundle r_oBundle = (Bundle) p_oState;

		Parcelable oParentParcelable = r_oBundle.getParcelable("parentParcelable");
		super.onRestoreInstanceState(oParentParcelable);
		if (r_oBundle.getInt("CurrentPage") != 0) {
			this.unHideAllColumns(true);
			int page = this.getAdapter().getCount() / this.application.getScreenColumnNumber();
			if (page - 1 < r_oBundle.getInt("CurrentPage")) {
				this.setCurrentItem(page - 1);
			} else {
				this.setCurrentItem(r_oBundle.getInt("CurrentPage"));
			}
		}

	}
	
	@Override
	public VisualComponentFwkDelegate getComponentFwkDelegate() {
		return this.aivFwkDelegate;
	}
	
	@Override
	public VisualComponentDelegate<VALUE> getComponentDelegate() {
		return this.aivDelegate;
	}

}
