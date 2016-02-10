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
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;

public class MMScrollableListView<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>> extends MMFixedListView<ITEM, ITEMVM>
		implements MMPagedView.OnPageChangeListener {

	/**
	 * name of the MMWorkspaceView
	 */
	public static final String PREFIX_WORKSPACE_NAME = "FL_MMWorkspaceView_";

	private static final String ATTR_ITEMS_BY_COLUMN_COUNT = "items-by-column";

	/**
	 * Count of column's item by default.
	 */
	private static final int DEFAULT_ITEM_BY_COLUMN_COUNT = 1;

	/**
	 * the application to access to centralized resources
	 */
	private AndroidApplication application = (AndroidApplication) Application.getInstance();

	private MMPagedView scrollableView;

	private int itemsByColumnCount;

	private LinearLayout lastColumn;

	boolean updating;

	private OnItemChangeListener<ITEM, ITEMVM> onItemChangeListener;

	/**
	 * <p>
	 *  Construct an object <em>MMFixedListView</em>.
	 * </p>
	 * @param p_oContext The application context
	 */
	public MMScrollableListView(final Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.itemsByColumnCount = DEFAULT_ITEM_BY_COLUMN_COUNT;
			this.scrollableView = new MMPagedView(this.getContext());
			this.scrollableView.setOnChangePageListener(this);
			((AndroidApplication) Application.getInstance()).computeId(this.scrollableView, PREFIX_WORKSPACE_NAME + this.application.getAndroidIdStringByIntKey(this.getId()));
			this.addView(this.scrollableView);
		}
	}

	/**
	 * <p>
	 *  Construct an object <em>MMFixedListView</em>.
	 * </p>
	 * @param p_oContext
	 * 		The application context
	 * @param p_oAttrs
	 * 		parameter to configure the <em>MMFixedListView</em> object.
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

	/**
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.AbstractFixedListView#onFinishInflate()
	 */
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
		else {
			super.destroyView(p_oCurrentRow);
		}
	}

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
	public void configurationSetValue(ListViewModel<ITEM, ITEMVM> p_oObject) {
		super.configurationSetValue(p_oObject);
		this.scrollableView.scrollToScreen(0);
	}

	/**
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.AbstractFixedListView#getItemAt(int)
	 */
	@Override
	protected View getItemAt(final int p_iIndex) {
		if(this.scrollableView == null){
			return  null ;
		}
		else {
			return  this.scrollableView.getChildAt(p_iIndex);
		}
	}

	/**
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.AbstractFixedListView#getItemCount()
	 */
	@Override
	protected int getItemCount() {
		if(this.scrollableView == null){
			return 0;
		}
		else {
			return this.scrollableView.getChildCount();			
		}
	}

	/**
	 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.AbstractFixedListView#removeItems()
	 */
	@Override
	protected void removeItems() {
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

	public MMPagedView getScrollableView() {
		return this.scrollableView;
	}

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

	public interface OnItemChangeListener<ITEM extends MIdentifiable, ITEMVM extends ItemViewModel<ITEM>> {
		public void onItemSelected(MMScrollableListView<ITEM, ITEMVM> p_oView, ITEMVM p_oVM);
	}
}