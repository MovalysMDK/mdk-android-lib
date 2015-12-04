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
package com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.flipperlist;

import java.lang.reflect.InvocationTargetException;

import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKBaseAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKFlipperAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.MDKViewConnector;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AdapterLevel;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AdapterLevelTreatment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.FlipperAdapterChild;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.FlipperAdapterGroup;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.FlipperAdapterTitle;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.FlipperExpandableListAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.DataSetObserver;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ExpandableViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;

public class MDKFlipperListConnector<
		ITEM extends MIdentifiable, 
		SUBITEM extends MIdentifiable, 
		SUBSUBITEM extends MIdentifiable, 
		SUBSUBITEMVM extends ItemViewModel<SUBSUBITEM>, 
		SUBITEMVM extends ExpandableViewModel<SUBITEM, SUBSUBITEM, SUBSUBITEMVM>, 
		ITEMVM extends ExpandableViewModel<ITEM, SUBITEM, SUBITEMVM>> 
	implements FlipperExpandableListAdapter, MDKViewConnector {

	private MDKFlipperAdapter<ITEM, SUBITEM, SUBSUBITEM, SUBSUBITEMVM, SUBITEMVM, ITEMVM> mAdapter ;
	
	/**
	 * Enumeration describing the different "levels" in the adapter / list
	 */
	private enum Level implements AdapterLevel {
		/** Title level */
		TITLE(FlipperAdapterTitle.class),
		/** Group level */
		GROUP(FlipperAdapterGroup.class),
		/** Child level */
		CHILD(FlipperAdapterChild.class);
		
		/** treatments class for the level */
		private AdapterLevelTreatment level;
		
		/** 
		 * Constructor
		 * @param p_oLevelClass the treatment class for the level 
		 */
		Level(Class<? extends AdapterLevelTreatment> p_oLevelClass) {
			try {
				this.level = p_oLevelClass.getConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException 
					| NoSuchMethodException | SecurityException e) {
				Application.getInstance().getLog().error("ERROR", this.getClass().getName(), e);
			}
		}

		/**
		 * returns the treatment class for the level
		 * @return the treatment class for the level
		 */
		@Override
		public AdapterLevelTreatment getLevel() {
			return this.level;
		}
	}
	
	private DataSetObserver mObserver = new DataSetObserver() {
		
		@Override
		public void onChanged() {
			MDKFlipperListConnector.this.notifyDataSetChanged();
		}
		
		@Override
		public void onInvalidated() {
			MDKFlipperListConnector.this.notifyDataSetInvalidated();
		}
	};
	
	public MDKFlipperListConnector(MDKFlipperAdapter<ITEM, SUBITEM, SUBSUBITEM, SUBSUBITEMVM, SUBITEMVM, ITEMVM> p_oAdapter) {
		this.mAdapter = p_oAdapter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeViewModelChanged() {
		this.mAdapter.getMasterVM().unregisterObserver(this.mObserver);
	}
	
	@Override
	public void afterViewModelChanged() {
		this.mAdapter.getMasterVM().registerObserver(this.mObserver);
		this.notifyDataSetChanged();
	}

	@Override
	public void setSelectedItem(String p_sItemId) {
		// nothing to do
	}

	@Override
	public void resetSelectedItem() {
		// nothing to do
	}

	@Override
	public void notifyDataSetChanged() {
		// nothing to do
	}

	@Override
	public MDKBaseAdapter getAdapter() {
		return this.mAdapter;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver p_oDataSetObserver) {
		this.mAdapter.getMasterVM().registerObserver(p_oDataSetObserver);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver p_oDataSetObserver) {
		this.mAdapter.getMasterVM().unregisterObserver(p_oDataSetObserver);
	}

	@Override
	public Object getTitle(int p_iTitlePosition) {
		return this.mAdapter.getTitle(p_iTitlePosition);
	}

	@Override
	public long getTitleId(int p_iTitlePosition) {
		return this.mAdapter.getTitleId(p_iTitlePosition);
	}

	@Override
	public int getTitleCount() {
		return this.mAdapter.getTitleCount();
	}

	@Override
	public View getTitleView(int p_iTitlePosition, boolean p_bExpanded, View p_oCurrentView, ViewGroup p_oParentViewGroup) {
		return this.mAdapter.getLegacyViewByLevel(Level.TITLE, p_bExpanded, p_oCurrentView, p_oParentViewGroup, 0, p_iTitlePosition);
	}

	@Override
	public Object getGroup(int p_iTitlePosition, int p_iGroupPosition) {
		return this.mAdapter.getGroup(p_iTitlePosition, p_iGroupPosition);
	}

	@Override
	public long getGroupId(int p_iTitlePosition, int p_iGroupPosition) {
		return this.mAdapter.getGroupId(p_iTitlePosition, p_iGroupPosition);
	}

	@Override
	public int getGroupCount(int p_iTitlePosition) {
		return this.mAdapter.getGroupCount(p_iTitlePosition);
	}

	@Override
	public View getGroupView(int p_iTitlePosition, int p_iGroupPosition, boolean p_bExpanded, View p_oCurrentRow, ViewGroup p_oParentViewGroup) {
		return this.mAdapter.getLegacyViewByLevel(Level.GROUP, p_bExpanded, p_oCurrentRow, p_oParentViewGroup, 0, p_iTitlePosition, p_iGroupPosition);
	}

	@Override
	public Object getChild(int p_iTitlePosition, int p_iGroupPosition,
			int p_iChildPosition) {
		return this.mAdapter.getChild(p_iTitlePosition, p_iGroupPosition, p_iChildPosition);
	}

	@Override
	public long getChildId(int p_iTitlePosition, int p_iGroupPosition,
			int p_iChildPosition) {
		return this.mAdapter.getChildId(p_iTitlePosition, p_iGroupPosition, p_iChildPosition);
	}

	@Override
	public int getChildrenCount(int p_iTitlePosition, int p_iGroupPosition) {
		return this.mAdapter.getChildrenCount(p_iTitlePosition, p_iGroupPosition);
	}

	@Override
	public View getChildView(int p_iTitlePosition, int p_iGroupPosition, int p_iChildPosition, boolean p_bLastChild, View p_oCurrentRow, ViewGroup p_oParentViewGroup) {
		return this.mAdapter.getLegacyViewByLevel(Level.CHILD, false, p_oCurrentRow, p_oParentViewGroup, 0, p_iTitlePosition, p_iGroupPosition, p_iChildPosition);
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public int getGroupType(int p_iGroupPosition) {
		return 0;
	}

	@Override
	public int getChildType(int p_iGroupPosition, int p_iChildPosition) {
		return 0;
	}

	@Override
	public int getGroupTypeCount() {
		return 1;
	}

	@Override
	public int getChildTypeCount() {
		return 1;
	}

	protected void notifyDataSetInvalidated() {
		// nothing to do
	}

}
