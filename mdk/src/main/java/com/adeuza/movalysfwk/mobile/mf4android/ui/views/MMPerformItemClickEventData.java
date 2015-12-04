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

/**
 * MMPerformItemClickEventData
 * Class that contain data from a item click event
 * 
 */
public class MMPerformItemClickEventData {
	
	/** the tag */
	private String tag;
	/** the view */
	private View view;
	/** the position */
	private int position;
	/** the id */
	private long id ;
	/** the listview */
	private MMPerformItemClickView listView;
	
	/**
	 * The constructor of an event data
	 * @param p_sTag the tag
	 * @param p_oView the clicked view
	 * @param p_iPosition the view position in ListView
	 * @param p_iId the id of the view
	 * @param p_oListView the ListView
	 */
	public MMPerformItemClickEventData( String p_sTag, View p_oView, int p_iPosition, long p_iId,
			MMPerformItemClickView p_oListView) {
		super();
		this.tag = p_sTag;
		this.view = p_oView;
		this.position = p_iPosition;
		this.id = p_iId;
		this.listView = p_oListView;
	}
	
	/**
	 * perform the click on the listview
	 */
	public void performItemClick() {
		listView.superPerformItemClick(view, position, id);
	}

	/**
	 * Getter for the view
	 * @return the view
	 */
	public View getView() {
		return view;
	}

	/**
	 * Getter for the position
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Getter for the id
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Getter for the ListView
	 * @return the ListView
	 */
	public MMPerformItemClickView getListView() {
		return listView;
	}

	/**
	 * Getter for the tag
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}
}
