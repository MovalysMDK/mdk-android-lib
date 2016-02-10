package com.adeuza.movalysfwk.mobile.mf4android.ui.wrappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPerformItemClickEventData;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.BusinessEvent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementGroup;

/**
 * The workspace adapter class This class is the adapter to the framgent
 * workspace
 * 
 * 
 * @author abelliard
 * 
 */
public class MMWorkspaceAdapter extends FragmentStatePagerAdapter {

	/** Serialisable id */
	private static final long serialVersionUID = 8111015573518297566L;

	/** Log Tag */
	private static final String TAG = "MMWorkspaceAdapter";

	/** Applicaiton */
	protected AndroidApplication application = (AndroidApplication) AndroidApplication
			.getInstance();

	/** list of Columns */
	protected List<MMWorkspaceColumn> columns;

	/** list of displayed Columns */
	protected List<MMWorkspaceColumn> shownColumns;

	/** map of active fragment */
	protected SparseArrayCompat<Fragment> mActiveFragments;

	/**
	 * Columns map Key: hashcode of group Value: column
	 */
	protected SparseArrayCompat<MMWorkspaceColumn> mapColumns;

	/** the fragment manager */
	protected FragmentManager mFm;

	/** the activity */
	private AbstractBaseWorkspaceMMFragmentActivity<?> activity;

	private AbstractMMWorkspaceListFragment mActiveFragment;

	protected boolean bWidthChanged;

	private float fOldWidth;

	/**
	 * The constructor
	 * 
	 * @param fm the fragment manager
	 */
	public MMWorkspaceAdapter(FragmentManager fm,
			AbstractBaseWorkspaceMMFragmentActivity<?> p_oActivity) {
		super(fm);

		this.mFm = fm;
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
	 * @param p_oGroup the colomns group
	 * @param p_oMMWorkspaceColumn the workspace column
	 */
	public void addColumns(ManagementGroup p_oGroup,
			MMWorkspaceColumn p_oMMWorkspaceColumn) {
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
	public Fragment getItem(int position) {
		Fragment r_oFragment = null;

		r_oFragment = this.shownColumns.get(position).getInstance();
		r_oFragment.setRetainInstance(false);
		this.mActiveFragments.put(position, r_oFragment);

		return r_oFragment;
	}

	@Override
	public int getItemPosition(Object object) {
		if (this.bWidthChanged) {
			return POSITION_NONE;
		} else {
			return super.getItemPosition(object);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getPageWidth(int position) {
		return getColumnsWidth();
	}

	/**
	 * return the column of the given group
	 * 
	 * @param p_oGroup
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
				mActiveFragment.getListAdapter().resetSelectedItem();
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
							public void setExitMode(boolean exitMode) {
							}
							
							@Override
							public boolean isConsummed() {
								return false;
							}
							
							@Override
							public void setConsummed(boolean consummed) {
							}
						});
			}
		}
	}

	/**
	 * @author lmichenaud
	 * 
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
		 * @return
		 */
		public boolean canBeHidden() {
			return this.canBeHidden;
		}

		/**
		 * return a new instance of the column fragment
		 * 
		 * @return hte fragment to show
		 * @throws Exception if the fragment class doesn't exist
		 */
		public Fragment getInstance() {
			return AbstractMMFragment.instantiate(
					MMWorkspaceAdapter.this.activity, fragment.getName(),
					arguments);
		}

		/**
		 * set the workspace adapter
		 * 
		 * @param p_oAdapter
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
		 * @param p_bCanBeHidden
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
		 * @param boolean is a list
		 */
		public void setList(boolean p_bList) {
			this.list = p_bList;
		}

		/**
		 * get the arguments
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
	 * unhode a column
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

	class SaveActiveFragment extends BaseSavedState {

		class SaveFragmentActiveTagId{

			public SaveFragmentActiveTagId(String p_sTag, int p_iId) {
				this.tag = p_sTag;
				this.id = p_iId;
			}
			String tag;
			int id;

		}

		public final Parcelable.Creator<SaveActiveFragment> CREATOR = new Parcelable.Creator<SaveActiveFragment>() {
			@Override
			public SaveActiveFragment createFromParcel(Parcel source) {
				return new SaveActiveFragment(source);
			}

			@Override
			public SaveActiveFragment[] newArray(int size) {
				return new SaveActiveFragment[size];
			}
		};

		SparseArrayCompat<Fragment> mSaveActiveFragments;

		public SaveActiveFragment(Parcelable superState) {
			super(superState);
		}

		public SaveActiveFragment(Parcel source) {
			super(source);
			HashMap<Integer, SaveFragmentActiveTagId> oSaveMap = (HashMap<Integer, SaveFragmentActiveTagId>) source.readSerializable();

			SaveFragmentActiveTagId oCurrentTagId;
			Fragment oCurrentRestoringFragment;

			for (Integer key : oSaveMap.keySet()) {
				oCurrentTagId = oSaveMap.get(key);
				oCurrentRestoringFragment = mFm.findFragmentByTag(oCurrentTagId.tag);
				if ( oCurrentRestoringFragment == null ) {
					oCurrentRestoringFragment = mFm.findFragmentById(oCurrentTagId.id);
				}
				if ( oCurrentRestoringFragment != null ) {
					mSaveActiveFragments.put(key, oCurrentRestoringFragment);
				}

			}

		}

		@Override
		public int describeContents() {
			return 1;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {

			HashMap<Integer, SaveFragmentActiveTagId> oSave = new HashMap<>();

			for (int i = 0; i < mSaveActiveFragments.size(); i++) {
				Fragment oF = mSaveActiveFragments.valueAt(i);
				oSave.put(mSaveActiveFragments.keyAt(i), new SaveFragmentActiveTagId(oF.getTag(), oF.getId()));
			}

			dest.writeSerializable(oSave);

		}

	}

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

		//		Parcelable oSaveState = super.saveState();
		//		SaveActiveFragment ss = new SaveActiveFragment(oSaveState);
		//		
		//		ss.mSaveActiveFragments = mActiveFragments;
		//		return ss;
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
		//		super.restoreState(state, loader);
		//		this.notifyDataSetChanged();

		if (state != null) {

			Bundle bundle = (Bundle) state;

			this.fOldWidth = bundle.getFloat("columnsWidht");

			this.bWidthChanged = didWidthChange();

			super.restoreState(bundle.getParcelable("parent"), loader);

		}

		this.notifyDataSetChanged();

		// repopulate current active fragments
		for (int i=0; i <this.columns.size(); i++) {
			MMWorkspaceColumn oColumn = this.columns.get(i);
			Bundle cCurrentBundle = oColumn.getArguments();
			for (Fragment oFrag : mFm.getFragments()) {
				if (oFrag != null) {
					if (this.equalBundles(oFrag.getArguments(), cCurrentBundle)) {
						this.mActiveFragments.put(i, oFrag);
						break;
					}
				}
			}
		}

		//		//begin boilerplate code so parent classes can restore state
		//		if(!(state instanceof SaveActiveFragment)) {
		//			super.restoreState(state, loader);
		//			return;
		//		}
		//		
		//		SaveActiveFragment ss = (SaveActiveFragment)state;
		//		mActiveFragments = ss.mSaveActiveFragments;
		//		
		//		mFm.getFragments();
		//		super.restoreState(ss.getSuperState(), loader);
		//		//end
	}

	public boolean equalBundles(Bundle one, Bundle two) {
		if(one==null || two==null || one.size() != two.size())
			return false;

		Set<String> setOne = one.keySet();
		Object valueOne;
		Object valueTwo;

		boolean result = true;
		
		for(String key : setOne) {
			valueOne = one.get(key);
			valueTwo = two.get(key);
			if(
				valueOne instanceof Bundle 
				&& valueTwo instanceof Bundle 
				&& !equalBundles((Bundle) valueOne, (Bundle) valueTwo)
			) {
				result=false;
				break;
			}
			else if(valueOne == null && (valueTwo != null || !two.containsKey(key))){
				result=false;
				break;
			}
			else if(!valueOne.equals(valueTwo)){
				result=false;
				break;
			}
		}

		return result;
	}

	protected boolean didWidthChange() {
		if (this.fOldWidth != getColumnsWidth()) {
			this.fOldWidth = getColumnsWidth();
			return true;
		} else {
			return false;
		}
	}

}
