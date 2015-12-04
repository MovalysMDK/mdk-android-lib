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

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKBaseAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.connectors.MDKViewConnectorWrapper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.component.configurable.WidgetWrapperHelper;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.MMListAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMAdaptableListView;

public abstract class AbstractMMListFragment extends AbstractAutoBindMMFragment {

	private View mListView ;
	protected Object mAdapter; // TODO : passer sur MDKBaseAdapter
	
	/** checked item in list */
	private List<Integer> oCheckState;

	@Override
	protected void doAfterInflate(ViewGroup p_oRoot) {
		super.doAfterInflate(p_oRoot);

		mAdapter = this.createListAdapter();
		mListView = p_oRoot.findViewById(this.getListViewId());
		
		if (mAdapter instanceof MDKBaseAdapter) {
			MDKViewConnectorWrapper mConnectorWrapper = WidgetWrapperHelper.getInstance().getConnectorWrapper(mListView.getClass());
			mConnectorWrapper.configure((MDKBaseAdapter)mAdapter, mListView);
		} else {
			((MMAdaptableListView)mListView).setListAdapter((MMListAdapter)mAdapter);
		}
	}

	/**
	 * Return the list view id
	 * @return the list view id
	 */
	protected abstract int getListViewId();

	/**
	 * create the list adapter
	 * @return the fragment list adapter
	 */
	protected abstract Object createListAdapter(); // TODO : passer sur MDKBaseAdapter

	@Override
	public void onViewCreated(View p_oView, Bundle p_oSavedInstanceState) {
		super.onViewCreated(p_oView, p_oSavedInstanceState);	

		updateListAdapterWithVM(!this.isRotated);
	}

	/**
	 * Getter for list adapter
	 * @return the list adapter
	 */
	public Object getListAdapter() { // TODO : passer sur MDKBaseAdapter
		return mAdapter;
	}

	/**
	 * Update the adapter list
	 * @param p_bRefreshList if the adapter need refresh
	 */
	public void updateListAdapterWithVM(boolean p_bRefreshList) {
		if (p_bRefreshList) {
			// TODO : passer sur MDKBaseAdapter
			if (mAdapter instanceof MDKBaseAdapter) {
				((MDKBaseAdapter)this.getListAdapter()).notifyDataSetChanged();
			} else {
				((MMListAdapter)this.getListAdapter()).notifyDataSetChanged();
			}
			// fin TODO
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mListView != null) {
			//On restaure l'état "check" des items
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
				if(this.oCheckState != null && mListView instanceof AbsListView) {
					for(int iPosition = 0 ; iPosition < oCheckState.size() ; iPosition++) {
						((AbsListView)mListView).setItemChecked(this.oCheckState.get(iPosition), true);
					}
				}
			} else {
				if(this.oCheckState != null && mListView instanceof ListView) {
					for(int iPosition = 0 ; iPosition < oCheckState.size() ; iPosition++) {
						((ListView)mListView).setItemChecked(this.oCheckState.get(iPosition), true);
					}
				}
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		//On sauvegarde l'état check des items

		SparseBooleanArray oCheckSparseBooleanArray = new SparseBooleanArray();
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			if(mListView instanceof AbsListView) {
				oCheckSparseBooleanArray = ((AbsListView) mListView).getCheckedItemPositions();
			}
		} else {
			if(mListView instanceof ListView) {
				oCheckSparseBooleanArray = ((ListView) mListView).getCheckedItemPositions();
			}
		}
		this.oCheckState = new ArrayList<>();

		if(oCheckSparseBooleanArray != null) {
			for(int i  = 0 ; i < oCheckSparseBooleanArray.size() ; i++) {
				if(oCheckSparseBooleanArray.valueAt(i)) {
					this.oCheckState.add(oCheckSparseBooleanArray.keyAt(i));
				}
			}
		}

	}
}
