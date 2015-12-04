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
package com.adeuza.movalysfwk.mobile.mf4android.ui.wrappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.View.BaseSavedState;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractBaseWorkspaceMMFragmentActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.AbstractMMFragment;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.AbstractMMTabbedFragment;
import com.adeuza.movalysfwk.mobile.mf4android.fragment.AbstractMMWorkspaceListFragment;
import com.adeuza.movalysfwk.mobile.mf4android.ui.adapters.MDKBaseAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.MMListAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPerformItemClickEventData;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.BusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementGroup;

/**
 * The workspace adapter class 
 * This class is the adapter of the workspace fragment
 */
public class MMWorkspaceAdapter extends FragmentStatePagerAdapter {

	/** Application */
	protected AndroidApplication application = (AndroidApplication) AndroidApplication.getInstance();

	/** list of Columns */
	protected List<MMWorkspaceColumn> columns;

	/** list of displayed Columns */
	protected List<MMWorkspaceColumn> shownColumns;

	/** map of active fragments */
	protected SparseArrayCompat<Fragment> mActiveFragments;

	/** Columns map Key: hashcode of group Value column */
	protected SparseArrayCompat<MMWorkspaceColumn> mapColumns;

	/** the fragment manager */
	protected FragmentManager mFm;

	/** the activity */
	private AbstractBaseWorkspaceMMFragmentActivity<?> activity;
	
	/** active list fragment */
	private AbstractMMWorkspaceListFragment mActiveFragment;
	
	/** true if width of workspace has changed */
	protected boolean bWidthChanged;
	
	/** old width */
	private float fOldWidth;

	/**
	 * The constructor
	 * 
	 * @param p_oFm the fragment manager
	 * @param p_oActivity the workspace activity
	 */
	public MMWorkspaceAdapter(FragmentManager p_oFm, AbstractBaseWorkspaceMMFragmentActivity<?> p_oActivity) {
		super(p_oFm);

		this.mFm = p_oFm;
		this.activity = p_oActivity;

		this.columns = new ArrayList<>();
		this.shownColumns = new ArrayList<>();
		this.mapColumns = new SparseArrayCompat<>();
		this.mActiveFragments = new SparseArrayCompat<>();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCount() {
		// modify to unhiddenColumns
		return this.shownColumns.size();
	}

	/**
	 * Add a column to the adapter
	 * 
	 * @param p_oGroup the columns group
	 * @param p_oMMWorkspaceColumn the workspace column
	 */
	public void addColumns(ManagementGroup p_oGroup, MMWorkspaceColumn p_oMMWorkspaceColumn) {
		if (this.mapColumns.indexOfKey(p_oGroup.hashCode()) < 0) {
			this.columns.add(p_oMMWorkspaceColumn);
			this.shownColumns.add(p_oMMWorkspaceColumn);
			this.mapColumns.put(p_oGroup.hashCode(), p_oMMWorkspaceColumn);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Fragment getItem(int p_iPosition) {
		Fragment r_oFragment = null;

		r_oFragment = this.shownColumns.get(p_iPosition).getInstance();
		r_oFragment.setRetainInstance(false);
		this.mActiveFragments.put(p_iPosition, r_oFragment);

		return r_oFragment;
	}

	@Override
	public int getItemPosition(Object p_oObject) {
		if (this.bWidthChanged) {
			return POSITION_NONE;
		} else {
			return super.getItemPosition(p_oObject);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getPageWidth(int p_iPosition) {
		return getColumnsWidth();
	}

	/**
	 * return the column of the given group
	 * 
	 * @param p_oGroup the group of the columns to retrieve
	 * @return the workspace column
	 */
	public MMWorkspaceColumn getColumnByGroup(ManagementGroup p_oGroup) {
		return this.mapColumns.get(p_oGroup.hashCode());
	}

	/**
	 * Reset the current selected item of the workspace
	 */
	public void resetSelectedItem() {

		for ( int i = 0 ; i < this.mActiveFragments.size(); i++ ) {
			int position = this.mActiveFragments.keyAt(i);
			if (this.columns.get(position).isList()) {

				mActiveFragment = null;
				if (this.mActiveFragments.get(position) instanceof AbstractMMTabbedFragment) {
					AbstractMMTabbedFragment oTabFrag = (AbstractMMTabbedFragment) this.mActiveFragments.get(position);
					mActiveFragment = oTabFrag.getCurrentListFragment();
				} else {
					mActiveFragment = ((AbstractMMWorkspaceListFragment) this.mActiveFragments.get(position));
				}
				if (mActiveFragment.getListAdapter() instanceof MDKBaseAdapter) { // TODO : passer sur MDKBaseAdapter
					((MDKBaseAdapter) mActiveFragment.getListAdapter()).resetSelectedItem();
				} else {
					((MMListAdapter) mActiveFragment.getListAdapter()).resetSelectedItem();
				}
				((AbstractMMWorkspaceListFragment) mActiveFragment).doOnMasterListChangeSelectedItem(
						mActiveFragment.getWorkspaceListHandler(),
						new BusinessEvent<MMPerformItemClickEventData>() {
							/**
							 * {@inheritDoc}
							 */
							@Override
							public Object getSource() {
								return mActiveFragment.getListAdapter();
							}

							/**
							 * {@inheritDoc}
							 */
							@Override
							public MMPerformItemClickEventData getData() {
								return null;
							}

							@Override
							public boolean isExitMode() {
								return false;
							}
							@Override
							public void setExitMode(boolean p_bExitMode) {
								// Do not exit, always return false
							}
							
							@Override
							public boolean isConsummed() {
								return false;
							}
							@Override
							public void setConsummed(boolean p_bConsummed) {
								// adapter never consume event
							}
							
							@Override
							public Class<?> getFilterClass() {
								return mActiveFragment.getListAdapter().getClass();
							}
						});
			}
		}
	}

	/**
	 * Inner class to represent a Workspace column
	 */
	public class MMWorkspaceColumn<FRAG extends AbstractMMFragment> {

		/** Can be hidden */
		private boolean canBeHidden = false;

		/** the fragment class */
		private Class<FRAG> fragment;

		/** is a list */
		private boolean list = false;

		/** the workspace adapter */
		private MMWorkspaceAdapter wAdapter;

		/** the argument to the fragment */
		private Bundle arguments;

		/**
		 * if the column can be hidden
		 * 
		 * @return true if the columns can be hide
		 */
		public boolean canBeHidden() {
			return this.canBeHidden;
		}

		/**
		 * return a new instance of the column fragment
		 * 
		 * @return the fragment to show
		 */
		public Fragment getInstance() {
			return AbstractMMFragment.instantiate(MMWorkspaceAdapter.this.activity, fragment.getName(), arguments);
		}

		/**
		 * set the workspace adapter
		 * 
		 * @param p_oAdapter the workspace adapter to set
		 */
		public void setWorkspaceAdapter(MMWorkspaceAdapter p_oAdapter) {
			this.wAdapter = p_oAdapter;
		}

		/**
		 * return the workspace adapter
		 * 
		 * @return the workspace adapter
		 */
		public MMWorkspaceAdapter getWorkspaceAdapter() {
			return this.wAdapter;
		}

		/**
		 * set if the column can be hidden
		 * 
		 * @param p_bCanBeHidden true if the column can be hide, false otherwise
		 */
		public void setCanBeHidden(boolean p_bCanBeHidden) {
			this.canBeHidden = p_bCanBeHidden;
		}

		/**
		 * return the fragment class of the column
		 * 
		 * @return the fragment class
		 */
		public Class<FRAG> getFragmentClass() {
			return this.fragment;
		}

		/**
		 * Set the fragment of this column
		 * 
		 * @param p_oFragment the fragment class
		 * @param p_oArgs the fragment arguments
		 */
		public void setFragmentClass(Class<FRAG> p_oFragment, Bundle p_oArgs) {
			this.fragment = p_oFragment;
			this.arguments = p_oArgs;
		}

		/**
		 * return if the column is a list
		 * 
		 * @return boolean
		 */
		public boolean isList() {
			return this.list;
		}

		/**
		 * set if the column is a list
		 * 
		 * @param p_bList is a list
		 */
		public void setList(boolean p_bList) {
			this.list = p_bList;
		}

		/**
		 * get the arguments
		 * @return the arguments
		 */
		public Bundle getArguments() {
			return this.arguments;
		}
	}

	/**
	 * hide a column
	 * 
	 * @param p_iColumnIndex the column index
	 */
	public void hideColumn(int p_iColumnIndex) {
		this.shownColumns.remove(this.columns.get(p_iColumnIndex));
		this.bWidthChanged = didWidthChange();
		this.notifyDataSetChanged();
	}

	/**
	 * unhide a column
	 * 
	 * @param p_iColumnIndex the column index
	 */
	public void unhideColumn(int p_iColumnIndex) {
		this.shownColumns.add(this.columns.get(p_iColumnIndex));
		this.bWidthChanged = didWidthChange();
		this.notifyDataSetChanged();
	}

	/**
	 * hide all columns
	 */
	public void hideAllColumns() {
		this.shownColumns.removeAll(this.columns);
		this.bWidthChanged = didWidthChange();
		this.notifyDataSetChanged();
	}

	/**
	 * unhide all columns
	 */
	public void unHideAllColumns() {
		if ( shownColumns.size() != this.columns.size()) {
			this.shownColumns.removeAll(this.columns);
			this.shownColumns.addAll(this.columns);
		}
		this.bWidthChanged = didWidthChange();
		this.notifyDataSetChanged();
	}

	/**
	 * Save active fragment
	 */
	class SaveActiveFragment extends BaseSavedState {

		/**
		 * Inner class to save fragment tag and id
		 */
		class SaveFragmentActiveTagId{

			/**
			 * Constructor for SaveFragmentActiveTagId
			 * @param p_sTag the fragment tag
			 * @param p_iId the fragment id
			 */
			public SaveFragmentActiveTagId(String p_sTag, int p_iId) {
				this.tag = p_sTag;
				this.id = p_iId;
			}
			/** tag of active fragment */
			String tag;
			/** id of active tag */
			int id;

		}

		/**
		 * Parcelable CREATOR
		 */
		public final Parcelable.Creator<SaveActiveFragment> CREATOR = new Parcelable.Creator<SaveActiveFragment>() {
			@Override
			public SaveActiveFragment createFromParcel(Parcel p_oSource) {
				return new SaveActiveFragment(p_oSource);
			}

			@Override
			public SaveActiveFragment[] newArray(int p_iSize) {
				return new SaveActiveFragment[p_iSize];
			}
		};

		/** save active fragment sparce array */
		SparseArrayCompat<Fragment> mSaveActiveFragments;

		/**
		 * Constructor on parcelable
		 * @param p_oSuperState the parcelable to build on
		 */
		public SaveActiveFragment(Parcelable p_oSuperState) {
			super(p_oSuperState);
		}

		/**
		 * Constructor on Parcel
		 * @param p_oSource the parcel to write in
		 */
		public SaveActiveFragment(Parcel p_oSource) {
			super(p_oSource);
			HashMap<Integer, SaveFragmentActiveTagId> oSaveMap = (HashMap<Integer, SaveFragmentActiveTagId>) p_oSource.readSerializable();
			mSaveActiveFragments = new SparseArrayCompat<Fragment>();
			Fragment oCurrentRestoringFragment;

			for (Entry<Integer, SaveFragmentActiveTagId> oCurrentEntry: oSaveMap.entrySet()) {
				oCurrentRestoringFragment = mFm.findFragmentByTag(oCurrentEntry.getValue().tag);
				if ( oCurrentRestoringFragment == null ) {
					oCurrentRestoringFragment = mFm.findFragmentById(oCurrentEntry.getValue().id);
				}
				if ( oCurrentRestoringFragment != null ) {
					mSaveActiveFragments.put(oCurrentEntry.getKey(), oCurrentRestoringFragment);
				}
			}
		}

		@Override
		public int describeContents() {
			return 1;
		}

		@Override
		public void writeToParcel(Parcel p_oDest, int p_iFlags) {

			HashMap<Integer, SaveFragmentActiveTagId> oSave = new HashMap<>();

			for (int i = 0; i < mSaveActiveFragments.size(); i++) {
				Fragment oF = mSaveActiveFragments.valueAt(i);
				oSave.put(mSaveActiveFragments.keyAt(i), new SaveFragmentActiveTagId(oF.getTag(), oF.getId()));
			}

			p_oDest.writeSerializable(oSave);

		}

	}

	/**
	 * Get the width of a workspace column
	 * @return the workspace columns width
	 */
	private float getColumnsWidth() {
		int iNumberOfColumns = Math.min(application.getScreenColumnNumber(),
				this.shownColumns.size());

		float r_fWidth = 1f;
		if (iNumberOfColumns != 0) {
			r_fWidth = 1.0f / iNumberOfColumns;
		}
		return r_fWidth;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		// restore in older width (if page width change)
		// we need to call 2 times
		if (this.bWidthChanged) {
			this.bWidthChanged = false;
			notifyDataSetChanged();
		}
	}

	@Override
	public Parcelable saveState() {

		Bundle oState = new Bundle();
		oState.putParcelable("parent", super.saveState());

		oState.putFloat("columnsWidht", getColumnsWidth());

		return oState;
	}

	@Override
	public void restoreState(Parcelable p_oState, ClassLoader p_oLoader) {
		if (p_oState != null) {

			Bundle bundle = (Bundle) p_oState;

			this.fOldWidth = bundle.getFloat("columnsWidht");

			this.bWidthChanged = didWidthChange();

			super.restoreState(bundle.getParcelable("parent"), p_oLoader);

		}

		this.notifyDataSetChanged();

		// repopulate current active fragments
		for (int i=0; i <this.columns.size(); i++) {
			MMWorkspaceColumn oColumn = this.columns.get(i);
			Bundle cCurrentBundle = oColumn.getArguments();
			for (Fragment oFrag : mFm.getFragments()) {
				if (oFrag != null && this.equalBundles(oFrag.getArguments(), cCurrentBundle)) {
					this.mActiveFragments.put(i, oFrag);
					break;
				}
			}
		}
	}

	/**
	 * Compare to Bundle
	 * @param p_oOne first bundle
	 * @param p_oTwo second bundle
	 * @return true if the to bundle are equals, false otherwise
	 */
	public boolean equalBundles(Bundle p_oOne, Bundle p_oTwo) {
		if(p_oOne==null || p_oTwo==null || p_oOne.size() != p_oTwo.size()) {
			return false;
		}
		
		Object valueOne;
		Object valueTwo;

		boolean result = true;
		
		for(String key : p_oOne.keySet()) {
			valueOne = p_oOne.get(key);
			valueTwo = p_oTwo.get(key);
			if( valueOne instanceof Bundle 
				&& valueTwo instanceof Bundle 
				&& !equalBundles((Bundle) valueOne, (Bundle) valueTwo) ) {
				result=false;
				break;
			}
			else if(valueOne == null && (valueTwo != null || !p_oTwo.containsKey(key))){
				result=false;
				break;
			}
			else if(valueOne != null && !valueOne.equals(valueTwo)){
				result=false;
				break;
			}
		}
		return result;
	}
	
	/**
	 * check if the workspace width have change
	 * @return true if the width have change, false otherwise
	 */
	protected boolean didWidthChange() {
		if (this.fOldWidth != getColumnsWidth()) {
			this.fOldWidth = getColumnsWidth();
			return true;
		} else {
			return false;
		}
	}

}
