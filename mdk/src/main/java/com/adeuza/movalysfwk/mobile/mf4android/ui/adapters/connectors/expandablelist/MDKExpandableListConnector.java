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
package com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.expandablelist;

import java.lang.reflect.InvocationTargetException;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.adeuza.movalysfwk.mf4jcommons.core.beans.MIdentifiable;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKExpandableAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.MDKViewConnector;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AdapterLevel;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.AdapterLevelTreatment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ExpandableAdapterChild;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.ExpandableAdapterGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.DataSetObserver;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ExpandableViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ItemViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ListViewModel;

public class MDKExpandableListConnector
	<ITEM extends MIdentifiable, 
	SUBITEM extends MIdentifiable, 
	SUBITEMVM extends ItemViewModel<SUBITEM>, 
	ITEMVM extends ExpandableViewModel<ITEM, SUBITEM, SUBITEMVM>>
	extends BaseExpandableListAdapter implements MDKViewConnector {

	private MDKExpandableAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> mAdapter ;
	
	/**
	 * Enumeration describing the different "levels" in the adapter / list
	 */
	private enum Level implements AdapterLevel {
		/** Group level */
		GROUP(ExpandableAdapterGroup.class),
		/** Child level */
		CHILD(ExpandableAdapterChild.class);
		
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
			MDKExpandableListConnector.this.notifyDataSetChanged();
		}
		
		@Override
		public void onInvalidated() {
			MDKExpandableListConnector.this.notifyDataSetInvalidated();
		}
	};
	
	public MDKExpandableListConnector( MDKExpandableAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> p_oAdapter ) {
		this.mAdapter = p_oAdapter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeViewModelChanged() {
		this.mAdapter.getMasterVM().unregisterObserver(this.mObserver);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterViewModelChanged() {
		this.mAdapter.getMasterVM().registerObserver(this.mObserver);
		this.notifyDataSetChanged();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MDKExpandableAdapter<ITEM, SUBITEM, SUBITEMVM, ITEMVM> getAdapter() {
		return this.mAdapter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getGroupCount() {
		return this.mAdapter.getGroupCount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getChildrenCount(int p_iGroupPosition) {
		return mAdapter.getChildrenCount(p_iGroupPosition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITEMVM getGroup(int p_iGroupPosition) {
		return mAdapter.getGroup(p_iGroupPosition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SUBITEMVM getChild(int p_iGroupPosition, int p_iChildPosition) {
		ListViewModel<SUBITEM, SUBITEMVM> listChild = this.getGroup(p_iGroupPosition).getComposite();
		if (listChild == null) {
			return null;
		} else {
			return listChild.getCacheVMByPosition(p_iChildPosition);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getGroupId(int p_iGroupPosition) {
		int i = 0;
		try {
			i = Integer.parseInt(this.getGroup(p_iGroupPosition).getIdVM());
		} catch (NumberFormatException e) {
			i = this.getGroup(p_iGroupPosition).getIdVM().hashCode();
		}
		return i;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getChildId(int p_iGroupPosition, int p_iChildPosition) {
		int i = 0;
		try {
			i = Integer.parseInt(this.getChild(p_iGroupPosition, p_iChildPosition).getIdVM());
		} catch (NumberFormatException e) {
			i = this.getChild(p_iGroupPosition, p_iChildPosition).getIdVM().hashCode();
		}

		return i;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasStableIds() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getGroupView(int p_iGroupPosition, boolean p_bIsExpanded,
			View p_oConvertView, ViewGroup p_oParent) {
		return this.mAdapter.getLegacyViewByLevel(Level.GROUP, p_bIsExpanded, p_oConvertView, p_oParent, 0, p_iGroupPosition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getChildView(int p_iGroupPosition, int p_iChildPosition,
			boolean p_bIsLastChild, View p_oConvertView, ViewGroup p_oParent) {
		return this.mAdapter.getLegacyViewByLevel(Level.CHILD, false, 
				p_oConvertView, p_oParent, getChildType(p_iGroupPosition, p_iChildPosition), p_iGroupPosition, p_iChildPosition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
