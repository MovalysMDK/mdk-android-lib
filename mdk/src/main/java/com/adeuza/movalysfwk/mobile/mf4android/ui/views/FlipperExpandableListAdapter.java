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

import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.MMListAdapter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.DataSetObserver;

/**
 * Adapter for MMFlipperExpandableListView
 * 
 * @see com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMFlipperExpandableListView
 *
 */
public interface FlipperExpandableListAdapter extends MMListAdapter{
	/**
	 * registerDataSetObserver(DataSetObserver)
	 *
	 * @param p_oDataSetObserver
	 *            Data SetObserver
	 */
	void registerDataSetObserver(DataSetObserver p_oDataSetObserver);

	/**
	 * unregisterDataSetObserver
	 * 
	 * @param p_oDataSetObserver
	 *            Data SetObserver 
	 */
	void unregisterDataSetObserver(DataSetObserver p_oDataSetObserver);

	/**
	 * Get Title
	 *  
	 * @param p_iTitlePosition
	 *            The position of the first level of the triple list
	 * @return the Object for title
	 */
	public Object getTitle(int p_iTitlePosition);

	/**
	 * Get Title ID
	 *  
	 * @param p_iTitlePosition
	 *            The position of the first level of the triple list
	 * @return the ID for title
	 */
	public long getTitleId(int p_iTitlePosition);

	/**
	 * Get Title Count
	 * 
	 * @return the Count for title
	 */
	public int getTitleCount();

	/**
	 * Get Title View
	 * 
	 * @param p_iTitlePosition
	 *            The position of the first level of the triple list
	 * @param p_bExpanded
	 *            The position is Expanded
	 * @param p_oCurrentViewModel
	 *            The view Model of this item
	 * @param p_oParentViewGroup
	 *            The view Group of Parent
	 * @return the View for title
	 */
	public View getTitleView(int p_iTitlePosition, boolean p_bExpanded, View p_oCurrentViewModel, ViewGroup p_oParentViewGroup);

	/**
	 * Get Group
	 * 
	 * @param p_iTitlePosition
	 *            The position of the first level of the triple list
	 * @param p_iGroupPosition
	 *            The position of the second level of the triple list
	 * @return the Object for group
	 */
	public Object getGroup(int p_iTitlePosition, int p_iGroupPosition);

	/**
	 * Get Group Id
	 * 
	 * @param p_iTitlePosition
	 *            The position of the first level of the triple list
	 * @param p_iGroupPosition
	 *            The position of the second level of the triple list
	 * @return the ID for group
	 */
	public long getGroupId(int p_iTitlePosition, int p_iGroupPosition);

	/**
	 * Get Group Count
	 * 
	 * @param p_iTitlePosition
	 *            The position of the first level of the triple list
	 * @return the Count for group
	 */
	public int getGroupCount(int p_iTitlePosition);

	/**
	 * Get Group View
	 * 
	 * @param p_iTitlePosition
	 *            The position of the first level of the triple list
	 * @param p_iGroupPosition
	 *            The position of the second level of the triple list
	 * @param p_bExpanded
	 *            The position is Expanded
	 * @param p_oCurrentViewModel
	 *            The view Model of this item
	 * @param p_oParentViewGroup
	 *            The view Group of Parent
	 * @return the View for title
	 */
	public View getGroupView(int p_iTitlePosition, int p_iGroupPosition, boolean p_bExpanded, View p_oCurrentViewModel, ViewGroup p_oParentViewGroup);

	/**
	 * Get Child
	 * 
	 * @param p_iTitlePosition
	 *            The position of the first level of the triple list
	 * @param p_iGroupPosition
	 *            The position of the second level of the triple list
	 * @param p_iChildPosition
	 *            The position of the third level of the triple list
	 * @return the Objet for child
	 */
	public Object getChild(int p_iTitlePosition, int p_iGroupPosition, int p_iChildPosition);

	/**
	 * Get Child Id
	 * 
	 * @param p_iTitlePosition
	 *            The position of the first level of the triple list
	 * @param p_iGroupPosition
	 *            The position of the second level of the triple list
	 * @param p_iChildPosition
	 *            The position of the third level of the triple list
	 * @return the ID for child
	 */
	public long getChildId(int p_iTitlePosition, int p_iGroupPosition, int p_iChildPosition);

	/**
	 * Get Children Count
	 * 
	 * @param p_iTitlePosition
	 *            The position of the first level of the triple list
	 * @param p_iGroupPosition
	 *            The position of the second level of the triple list
	 * @return the count for child
	 */
	public int getChildrenCount(int p_iTitlePosition, int p_iGroupPosition);

	/**
	 * Get Child View
	 * 
	 * @param p_iTitlePosition
	 *            The position of the first level of the triple list
	 * @param p_iGroupPosition
	 *            The position of the second level of the triple list
	 * @param p_iChildPosition
	 *            The position of the third level of the triple list
	 * @param p_bLastChild
	 *            The last Child
	 * @param p_oCurrentViewModel
	 *            The view Model of this item
	 * @param p_oParentViewGroup
	 *            The view Group of Parent
	 * @return the View for Child
	 */
	public View getChildView(int p_iTitlePosition, int p_iGroupPosition, int p_iChildPosition, boolean p_bLastChild, View p_oCurrentViewModel, ViewGroup p_oParentViewGroup);

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.widget.Adapter#hasStableIds()
	 */
	public boolean hasStableIds();

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.widget.HeterogeneousExpandableList#getGroupType()
	 */
	public int getGroupType(int p_iGroupPosition);

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.widget.HeterogeneousExpandableList#getChildType()
	 */
	int getChildType(int p_iGroupPosition, int p_iChildPosition);

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.widget.HeterogeneousExpandableList#getGroupTypeCount()
	 */
	int getGroupTypeCount();

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.widget.HeterogeneousExpandableList#getChildTypeCount()
	 */
	int getChildTypeCount();
}
