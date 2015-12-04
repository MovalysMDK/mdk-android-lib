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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

/**
 * List adapter connector to create a new level in expandable list adapter
 *
 */
public class FlipperExpandableListConnector extends BaseExpandableListAdapter {
 
	/** the adapter for the Connector*/
	private  FlipperExpandableListAdapter oFlipperExpandableListAdapter = null;
	/** the current ID Title */
	private int iCurrentTitleID = -1;
	
	/** the Title Expandable List */
	private Map<Integer, ArrayList<GroupMetadata>> mTitleExpandableList = null;

	/**
	 * <p>
	 *  Construct an object <em>FlipperExpandableListConnector</em>.
	 * </p>
	 * @param p_oFlipperExpandableListAdapter
	 * 		The Adapter
	 * @param p_oCurrentTitleID
	 * 		The current ID Title
	 */
	public FlipperExpandableListConnector(FlipperExpandableListAdapter p_oFlipperExpandableListAdapter, int p_oCurrentTitleID) {
		super();
		
		this.oFlipperExpandableListAdapter = p_oFlipperExpandableListAdapter;
		this.iCurrentTitleID = p_oCurrentTitleID;
		this.mTitleExpandableList = new HashMap<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getGroupCount() {
		if (this.oFlipperExpandableListAdapter.getTitleCount() > 0) {
			return  this.oFlipperExpandableListAdapter.getGroupCount(iCurrentTitleID);
		} else {
			return 0;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getChildrenCount(int p_iGroupPosition) {
		return  this.oFlipperExpandableListAdapter.getChildrenCount(iCurrentTitleID, p_iGroupPosition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getGroup(int p_iGroupPosition) {
		return this.oFlipperExpandableListAdapter.getGroup(iCurrentTitleID, p_iGroupPosition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getChild(int p_iGroupPosition, int p_iChildPosition) {
		return this.oFlipperExpandableListAdapter.getChild(iCurrentTitleID, p_iGroupPosition, p_iChildPosition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getGroupId(int p_iGroupPosition) {
		return this.oFlipperExpandableListAdapter.getGroupId(iCurrentTitleID, p_iGroupPosition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getChildId(int p_iGroupPosition, int p_iChildPosition) {
		return this.oFlipperExpandableListAdapter.getChildId(iCurrentTitleID, p_iGroupPosition, p_iChildPosition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasStableIds() {
		return this.oFlipperExpandableListAdapter.hasStableIds();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getGroupView(int p_iGroupPosition, boolean p_bIsExpanded,
			View p_oConvertView, ViewGroup p_oParent) {
		return this.oFlipperExpandableListAdapter.getGroupView(iCurrentTitleID, p_iGroupPosition,  p_bIsExpanded, p_oConvertView, p_oParent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getChildView(int p_iGroupPosition, int p_iChildPosition,
			boolean p_bIsLastChild, View p_oConvertView, ViewGroup p_oParent) {
		return this.oFlipperExpandableListAdapter.getChildView(iCurrentTitleID, p_iGroupPosition, p_iChildPosition, p_bIsLastChild, p_oConvertView, p_oParent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChildSelectable(int p_iGroupPosition, int p_iChildPosition) {
		return true;
	}

	/**
	 * <p>
	 *  Set Current Title ID.
	 * </p>
	 * @param p_iCurrentTitleID
	 * 		The current ID Title
	 * @param p_bIsExpanded
	 * 		The Title Expanded
	 */
	public void setCurrentTitleID(int p_iCurrentTitleID, boolean p_bIsExpanded) {
		this.iCurrentTitleID = p_iCurrentTitleID;
		this.notifyDataSetInvalidated();
	}
	

	/**
	 * <p>
	 *  Get Title Expandable List
	 * </p>
	 *
	 * @return Map<Integer, ArrayList<GroupMetadata>>
	 */
	public Map<Integer, ArrayList<GroupMetadata>> getTitleExpandableList() {
		return mTitleExpandableList;
	}

	/**
	 * <p>
	 *  Set Title Expandable List
	 * </p>
	 *
	 * @param p_oTitleExpandableList The current ID Title
	 */
	public void setTitleExpandableList(Map<Integer, ArrayList<GroupMetadata>> p_oTitleExpandableList) {
		this.mTitleExpandableList = p_oTitleExpandableList;
	}

	/**
	 * <p>
	 *  Get Group Expandable List.
	 * </p>
	 * @param p_iCurrentTitleID The current ID Title
	 * @return ArrayList<GroupMetadata>
	 */
	public List<GroupMetadata> getGoupExpandableList(int p_iCurrentTitleID) {
		ArrayList<GroupMetadata> listGroupPosition = this.mTitleExpandableList.get(this.iCurrentTitleID);
		if (listGroupPosition != null && !listGroupPosition.isEmpty()) {
			ArrayList<GroupMetadata> myListGroupPosition = new ArrayList<>();
			for(GroupMetadata positionGroup : listGroupPosition) {
				myListGroupPosition.add(positionGroup);
			}
			return myListGroupPosition;
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onGroupCollapsed(int p_iGroupPosition) {
		super.onGroupCollapsed(p_iGroupPosition);

		ArrayList<GroupMetadata> listGroupPosition = this.mTitleExpandableList.get(this.iCurrentTitleID);
		if (listGroupPosition != null && !listGroupPosition.isEmpty()) {
			Iterator<GroupMetadata> myListGroup = listGroupPosition.iterator();
			while (myListGroup.hasNext()) {
				GroupMetadata positionGroup = myListGroup.next();
				if (positionGroup.currentID == p_iGroupPosition) {
					myListGroup.remove();
				}
			}
			//listGroupPosition.add(new GroupMetadata(groupPosition, false));
			this.mTitleExpandableList.put(this.iCurrentTitleID,listGroupPosition);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onGroupExpanded(int p_iGroupPosition) {
		super.onGroupExpanded(p_iGroupPosition);

		ArrayList<GroupMetadata> listGroupPosition = this.mTitleExpandableList.get(this.iCurrentTitleID);
		if (listGroupPosition == null) {
			listGroupPosition = new ArrayList<>();
		} else {
			Iterator<GroupMetadata> myListGroup = listGroupPosition.iterator();
			while (myListGroup.hasNext()) {
				GroupMetadata positionGroup = myListGroup.next();
				if (positionGroup.currentID == p_iGroupPosition) {
					myListGroup.remove();
				}
			}
		}
		listGroupPosition.add(new GroupMetadata(p_iGroupPosition, true));
		this.mTitleExpandableList.put(this.iCurrentTitleID,listGroupPosition);
	}

	/**
	 * Custom Group MetaDatas
	 */
	static class GroupMetadata implements Parcelable {
		/** current group id */
		int currentID;
		/** is group expanded */
		int isExpanded;

		/**
		 * Create new instance of GroupMetadata
		 * @param p_iCurrentID the group id
		 * @param p_isExpanded is the group expanded
		 */
		protected GroupMetadata(int p_iCurrentID, boolean p_isExpanded) {
			this.currentID = p_iCurrentID;
			if(p_isExpanded){
				this.isExpanded = 0;
			}
			else {
				this.isExpanded = 1;
			}
		}

		/**
		 * Getter
		 * @return the current group id
		 */
		public int getCurrentID() {
			return this.currentID;
		}

		/**
		 * Setter
		 * @param p_isExpanded is the group expanded
		 */
		public void setIsExpanded(boolean p_isExpanded) {
			if(p_isExpanded){
				this.isExpanded = 0;
			}
			else {
				this.isExpanded = 1;
			}
		}

		/**
		 * Getter
		 * @return true if the group is expanded, false otherwise
		 */
		public boolean isExpanded() {
			return this.isExpanded == 0;
		}

		@Override
		public void writeToParcel(Parcel p_oDest, int p_iFlags) {
			p_oDest.writeInt(currentID);
			p_oDest.writeInt(isExpanded);
		}

		@Override
		public int describeContents() {
			return 0;
		}
		
		/**
		 * Parcelable CREATOR
		 */
		public static final Parcelable.Creator<GroupMetadata> CREATOR =
				 new Parcelable.Creator<GroupMetadata>() {

			@Override
			public GroupMetadata createFromParcel(Parcel p_oSource) {
				return new GroupMetadata(p_oSource);
			}

			@Override
			public GroupMetadata[] newArray(int p_iSize) {
				return new GroupMetadata[p_iSize];
			}
		};
		
		/**
		 * Create GroupMetadata from parcel
		 * @param p_oIn the parcel
		 */
		public GroupMetadata(Parcel p_oIn) {
			this.currentID = p_oIn.readInt();
			this.isExpanded = p_oIn.readInt();
		}
	}

}
