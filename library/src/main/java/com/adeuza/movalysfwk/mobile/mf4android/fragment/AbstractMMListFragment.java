package com.adeuza.movalysfwk.mobile.mf4android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.MMListAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMAdaptableListView;

public abstract class AbstractMMListFragment extends AbstractAutoBindMMFragment {

	/** list adapter */
	protected MMListAdapter mListAdapter;
	/** list view */
	private MMAdaptableListView mListView;
	/** checked item in list */
	private List<Integer> oCheckState;

	@Override
	protected void doAfterInflate(ViewGroup p_oRoot) {
		super.doAfterInflate(p_oRoot);

		this.mListAdapter = this.createListAdapter();

		this.mListView = (MMAdaptableListView) p_oRoot.findViewById(this.getListViewId());
		this.mListView.setListAdapter(this.mListAdapter);
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
	protected abstract MMListAdapter createListAdapter();

	@Override
	public void onViewCreated(View p_oView, Bundle p_oSavedInstanceState) {
		super.onViewCreated(p_oView, p_oSavedInstanceState);	

		updateListAdapterWithVM(p_oSavedInstanceState == null || !p_oSavedInstanceState.getBoolean("restored"));
	}

	/**
	 * Getter for list adapter
	 * @return the list adapter
	 */
	public MMListAdapter getListAdapter() {
		return this.mListAdapter;
	}

	/**
	 * Update the adapter list
	 * @param p_bRefreshList if the adapter need refresh
	 */
	public void updateListAdapterWithVM(boolean p_bRefreshList) {
		if (p_bRefreshList) {
			this.getListAdapter().notifyDataSetChanged();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mListView != null) {
			//On restaure l'état "check" des items
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
				if(this.oCheckState != null && this.mListView instanceof AbsListView) {
					for(int iPosition = 0 ; iPosition < oCheckState.size() ; iPosition++) {
						((AbsListView)this.mListView).setItemChecked(this.oCheckState.get(iPosition), true);
					}
				}
			} else {
				if(this.oCheckState != null && this.mListView instanceof ListView) {
					for(int iPosition = 0 ; iPosition < oCheckState.size() ; iPosition++) {
						((ListView)this.mListView).setItemChecked(this.oCheckState.get(iPosition), true);
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
			if(this.mListView instanceof AbsListView) {
				oCheckSparseBooleanArray = ((AbsListView) this.mListView).getCheckedItemPositions();
			}
		} else {
			if(this.mListView instanceof ListView) {
				oCheckSparseBooleanArray = ((ListView) this.mListView).getCheckedItemPositions();
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

	@Override
	public void onSaveInstanceState(Bundle p_oOutState) {

		p_oOutState.putBoolean("restored", true);

		super.onSaveInstanceState(p_oOutState);
	}



}
