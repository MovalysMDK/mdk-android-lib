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
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;

/**
 * A scrollable list view widget
 * 
 * @param <ITEM> ITEM in list
 * @param <ITEMVM> ITEMVM in list
 */
public class MMScrollableListView<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>> 
	extends MMFixedListView<ITEM, ITEMVM>
	implements MMPagedView.OnPageChangeListener {

	/** name of the MMWorkspaceView */
	public static final String PREFIX_WORKSPACE_NAME = "FL_MMWorkspaceView_";

	/** name of the xml attribute defining the number of items by columns */
	private static final String ATTR_ITEMS_BY_COLUMN_COUNT = "items-by-column";

	/** Count of column's item by default. */
	private static final int DEFAULT_ITEM_BY_COLUMN_COUNT = 1;

	/** the application to access to centralized resources */
	private AndroidApplication application = (AndroidApplication) Application.getInstance();

	/** The scrollable view encapsulated in the component */
	private MMPagedView scrollableView;

	/** the current number of items by columns */
	private int itemsByColumnCount;

	/** the layout of the last column */
	private LinearLayout lastColumn;

	/** the items change listener of the component */
	private OnItemChangeListener<ITEM, ITEMVM> onItemChangeListener;

	/**
	 * <p>
	 *  Construct an object <em>MMScrollableListView</em>.
	 * </p>
	 * @param p_oContext The application context
	 * @param p_oAttrs parameter to configure the <em>MMScrollableListView</em> object.
	 */
	public MMScrollableListView(final Context p_oContext, final AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.itemsByColumnCount = p_oAttrs.getAttributeIntValue(AndroidApplication.MOVALYSXMLNS, ATTR_ITEMS_BY_COLUMN_COUNT, DEFAULT_ITEM_BY_COLUMN_COUNT);
			this.scrollableView = new MMPagedView(this.getContext(), p_oAttrs);
			this.scrollableView.setOnChangePageListener(this);
			((AndroidApplication) Application.getInstance()).computeId(this.scrollableView, PREFIX_WORKSPACE_NAME + this.application.getAndroidIdStringByIntKey(this.getId()));
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if(!isInEditMode()) {
			super.addView(this.scrollableView);
		}
	}

	@Override
	public void destroyView(View p_oCurrentRow) {
		if (
			p_oCurrentRow != null && this.scrollableView != null 
			&& p_oCurrentRow.getId() == this.scrollableView.getId()
		) {
			int iDepth = 2;
			if(this.itemsByColumnCount != 1){
				iDepth = 2;
			}
			this.destroyView(this.scrollableView,iDepth);
		}
		else if (p_oCurrentRow != null) {
			super.destroyView(p_oCurrentRow);
		}
	}

	/**
	 * Destroys the component
	 * @param p_oView the parent view
	 * @param p_iDepth the depth to reach in the layout hierarchy
	 */
	protected void destroyView(View p_oView, int p_iDepth) {
		if (p_iDepth > 0) {
			ViewGroup oParentItem = (ViewGroup) p_oView;
			final int iChildCount = oParentItem.getChildCount();
			for (int i=0; i < iChildCount; i++) {
				this.destroyView(oParentItem.getChildAt(i), p_iDepth - 1);
			}
		}
		else {
			super.destroyView(p_oView);
		}
	}

	@Override
	public View getItemAt(final int p_iIndex) {
		if(this.scrollableView == null){
			return  null ;
		}
		else {
			return  this.scrollableView.getChildAt(p_iIndex);
		}
	}

	@Override
	public int getItemCount() {
		if(this.scrollableView == null){
			return 0;
		}
		else {
			return this.scrollableView.getChildCount();			
		}
	}

	@Override
	public void removeItems() {
		if (this.scrollableView != null) {
			this.scrollableView.removeAllViews();
		}
	}

	@Override
	public void addItem(View p_oChild) {
		if (this.itemsByColumnCount == 1) {
			this.scrollableView.addView(p_oChild);
		}
		else if (this.lastColumn == null || this.lastColumn.getChildCount() == this.itemsByColumnCount) {
			this.lastColumn = new LinearLayout(this.getContext());
			this.lastColumn.setOrientation(LinearLayout.VERTICAL);
			this.scrollableView.addView(this.lastColumn);
			this.lastColumn.addView(p_oChild);
		}
		else {
			this.lastColumn.addView(p_oChild);
		}
	}

	@Override
	public void update() {
		// Reset de la dernière colonne avant la reconstruction.
		this.lastColumn = null;
		super.update();
		this.scrollableView.refresh();
	}

	/**
	 * Return the inner scrollable view
	 * @return the scrollable view
	 */
	public MMPagedView getScrollableView() {
		return this.scrollableView;
	}

	/**
	 * Set the {@link OnItemChangeListener} on the component
	 * @param p_oListener the listener to set, or null to disable it
	 */
	public void setOnItemChangeListener(OnItemChangeListener<ITEM, ITEMVM> p_oListener) {
		this.onItemChangeListener = p_oListener;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent p_oEvent) {
		ViewParent oParent = this.getParent();
		if (oParent != null) {
			oParent.requestDisallowInterceptTouchEvent(true);
		}
		return super.onInterceptTouchEvent(p_oEvent);
	}

	@Override
	public void onPageSelected(MMPagedView p_oView, int p_iPosition) {
		if (this.onItemChangeListener != null) {
			ITEMVM oVM = this.getAdapter().getMasterVM().getCacheVMByPosition(p_iPosition * this.itemsByColumnCount);
			if (oVM != null) {
				this.onItemChangeListener.onItemSelected(this, oVM);
			}
		}
	}

	/**
	 * Describes a listener on items changing
	 * @param <ITEM> ITEM in list
	 * @param <ITEMVM> ITEMVM in list
	 */
	public interface OnItemChangeListener<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>> {
		/**
		 * called when the selected item changes
		 * @param p_oView the scrollable view hosting the item selected 
		 * @param p_oVM the view of the selected item
		 */
		public void onItemSelected(MMScrollableListView<ITEM, ITEMVM> p_oView, ITEMVM p_oVM);
	}
}
