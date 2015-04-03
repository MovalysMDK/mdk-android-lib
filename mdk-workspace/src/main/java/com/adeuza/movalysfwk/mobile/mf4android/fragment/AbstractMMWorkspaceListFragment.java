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
package com.adeuza.movalysfwk.mobile.mf4android.fragment;

import java.io.Serializable;

import android.view.ViewGroup;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractWorkspaceMasterDetailMMFragmentActivity;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.MMListAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMAdaptableListView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPerformItemClickEventData;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.BusinessEvent;

/**
 * Class to show workspace list panel
 *
 */
public abstract class AbstractMMWorkspaceListFragment extends AbstractMMListFragment {

	/** workspace list handler */
	private WorkspaceListHandler mWorkspaceHandler;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doAfterInflate(ViewGroup p_oRoot) {
		super.doAfterInflate(p_oRoot);
		
		this.mWorkspaceHandler = new WorkspaceListHandler(this.getListViewId(), this.mListAdapter);
		
		this.mWorkspaceHandler.setView( (MMAdaptableListView) p_oRoot.findViewById(getListViewId()) );
		
	}

	/**
	 * Get the workspace list handler
	 * @return the workspace list handler
	 */
	public WorkspaceListHandler getWorkspaceListHandler() {
		return this.mWorkspaceHandler;
	}
	
	/**
	 * Holds the adapter and list for the workspace
	 */
	public static class WorkspaceListHandler implements Serializable {
		/**
		 * serial uid
		 */
		private static final long serialVersionUID = -599197075611819784L;

		/**
		 * List view id
		 */
		private int resourceId ;

		/**
		 * Adapter
		 */
		private MMListAdapter adapter = null;

		/**
		 * List view
		 */
		private MMAdaptableListView view = null;

		/**
		 * Constructor
		 * @param p_iResourceId resource identifier
		 * @param p_oAdapter {@link MMListAdapter} adapter
		 */
		public WorkspaceListHandler(int p_iResourceId, MMListAdapter p_oAdapter) {
			this.resourceId = p_iResourceId ;
			this.adapter = p_oAdapter ;
		}

		/**
		 * Sets the list view
		 * @param p_oView the list view to set
		 */
		public void setView(MMAdaptableListView p_oView) {
			this.view = p_oView;
		}

		/**
		 * Returns the resource identifier
		 * @return the resource identifier
		 */
		public int getResourceId() {
			return this.resourceId;
		}

		/**
		 * Returns the list adapter
		 * @return the list adapter
		 */
		public MMListAdapter getAdapter() {
			return this.adapter;
		}
		
		/**
		 * returns the view
		 * @return the view
		 */
		public MMAdaptableListView getView() {
			return this.view;
		}
	}

	/**
	 * Triggered when the selected item on master list is changed
	 * @param p_oWorkspaceListHandler the workspace handler
	 * @param p_oBusinessEvent the event triggered
	 */
	public void doOnMasterListChangeSelectedItem(
			WorkspaceListHandler p_oWorkspaceListHandler,
			BusinessEvent<MMPerformItemClickEventData> p_oBusinessEvent) {
		if ( this.getActivity() != null ) {
			((AbstractWorkspaceMasterDetailMMFragmentActivity) this.getActivity()).doOnMasterListChangeSelectedItem(p_oWorkspaceListHandler, p_oBusinessEvent);
		}
	}
	
}
